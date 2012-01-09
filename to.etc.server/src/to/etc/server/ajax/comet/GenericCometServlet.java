package to.etc.server.ajax.comet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This is a generic Ajax Comet pattern servlet. It implements the
 * Comet pattern (http://www.ajaxian.com/archives/comet-a-new-approach-to-ajax-applications)
 * using a standard Servlet container. This implementation is unusable
 * for large amounts of clients due to thread and connection exthaustion on
 * the web server. The base interface implemented by this servlet is however
 * well suited to be implemented by different servlet containers that do
 * have asynchronous servlet implementations like Tomcat 6, Bea and Jetty.
 *
 * <h3>See also:</h3>
 * http://blogs.webtide.com/gregw/2006/07/25/1153845234453.html<br/>
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Oct 25, 2006
 */
public class GenericCometServlet extends HttpServlet {
	static private final int	WAIT_TIMEOUT	= 2 * 60 * 1000;

	private Class<CometContext>	m_contextClass;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			System.out.println("GenericCometServlet: initializing");

			//-- This servlet must have a single parameter with the name 'class'.
			String cn = getServletConfig().getInitParameter("class");
			if(cn == null)
				throw new UnavailableException("Missing servlet parameter 'class', which should indicate the class implementing CometContext to handle comet requests to this servlet");
			Class cl = null;
			try {
				cl = Class.forName(cn);
			} catch(Exception x) {
				throw new UnavailableException("The class '" + cn + " cannot be loaded: " + x);
			}
			if(!CometContext.class.isAssignableFrom(cl))
				throw new UnavailableException("The class '" + cn + "' does not implement " + CometContext.class.getName());
			try {
				cl.newInstance();
			} catch(Exception x) {
				throw new UnavailableException("The class '" + cn + "' does not allow me to create instances: " + x);
			}
			m_contextClass = cl;
			System.out.println("GenericCometServlet: init has completed");
		} catch(ServletException x) {
			x.printStackTrace();
			throw x;
		} catch(RuntimeException x) {
			x.printStackTrace();
			throw x;
		}
	}

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		handle(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		handle(arg0, arg1);
	}

	private void handle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ContinuationImpl ci = new ContinuationImpl();

		//-- Step 1: 'begin' processing.
		CometContext ctx = null;
		try {
			ctx = m_contextClass.newInstance();
			ctx.begin(this, req, ci);
		} catch(Exception x) {
			handleException(x);
			throw new RuntimeException(x); // NOTREACHED
		}

		//-- Step 2: wait for the request to complete, or the streams to close.
		long stime = System.currentTimeMillis();
		long etime = ci.getTimeout();
		if(etime < 0 || etime > WAIT_TIMEOUT) // Default AND max timeout is 60 secs
			etime = WAIT_TIMEOUT;
		etime += stime;
		boolean timeout = true;
		while(etime > stime) {
			long wtime = etime - stime; // How much longer to wait?
			//			if(wtime > 5000)
			//				wtime = 5000;
			synchronized(ci) {
				if(ci.hasCompleted()) {
					timeout = false;
					break;
				}
				//				checkForDisconnection(req, res);
				try {
					ci.wait(wtime);
				} catch(Exception x) { // Treat interrupted as timeout
					break;
				}
			}
			stime = System.currentTimeMillis(); // Reset time again
		}

		/*
		 * Handle the response.
		 */
		try {
			ctx.respond(res, timeout); // Call the response generator
		} catch(Exception x) {
			handleException(x);
		}
	}

	//	/**
	//	 * @param req
	//	 * @param res
	//	 * @throws IOException
	//	 */
	//	private void	checkForDisconnection(HttpServletRequest req, HttpServletResponse res) throws IOException {
	//		try {
	//			res.getWriter().write(' ');
	//			res.getWriter().flush();
	//		} catch(IOException x) {
	//			System.out.println("Exception: browser disconnected");
	//			throw x;
	//		}
	//	}

	protected void handleException(Exception x) throws ServletException, IOException {
		x.printStackTrace();
		if(x instanceof RuntimeException)
			throw (RuntimeException) x;
		else if(x instanceof ServletException)
			throw (ServletException) x;
		else if(x instanceof IOException)
			throw (IOException) x;
		else
			throw new ServletException(x.toString(), x);
	}

	@Override
	public void destroy() {
		System.out.println("GenericCometServlet: destroy called");
		super.destroy();
	}
}
