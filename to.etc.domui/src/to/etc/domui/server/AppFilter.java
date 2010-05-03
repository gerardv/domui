package to.etc.domui.server;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.*;
import org.slf4j.Logger;
import org.slf4j.bridge.*;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.joran.*;
import ch.qos.logback.core.util.*;

import to.etc.domui.util.*;
import to.etc.net.*;
import to.etc.util.*;
import to.etc.webapp.nls.*;

/**
 * Base filter which accepts requests to the dom windows. This accepts all URLs that end with a special
 * suffix and redigates them to the appropriate handler.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on May 22, 2008
 */
public class AppFilter implements Filter {
	static final Logger LOG = LoggerFactory.getLogger(AppFilter.class);

	private ConfigParameters m_config;

	private String m_applicationClassName;

	private boolean m_logRequest;

	static private String m_appContext;

	/**
	 * If a reloader is needed for debug/development pps this will hold the reloader.
	 */

	private IContextMaker m_contextMaker;

	/** After the 1st request has been seen, this contains the application's root url. */
	static private String m_applicationURL;

	public void destroy() {
		//-- Pass DESTROY on to Application, if present.
		if(DomApplication.get() != null)
			DomApplication.get().internalDestroy();
	}

	static public String minitime() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY) + StringTool.intToStr(cal.get(Calendar.MINUTE), 10, 2) + StringTool.intToStr(cal.get(Calendar.SECOND), 10, 2) + "." + cal.get(Calendar.MILLISECOND);
	}

	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest rq = (HttpServletRequest) req;
			rq.setCharacterEncoding("UTF-8"); // FIXME jal 20080804 Encoding of input was incorrect?
			if(m_logRequest) {
				String rs = rq.getQueryString();
				rs = rs == null ? "" : "?" + rs;
				System.out.println(minitime() + " rq=" + rq.getRequestURI() + rs);
			}
			NlsContext.setLocale(rq.getLocale());
			//			NlsContext.setLocale(new Locale("nl", "NL"));
			initContext(req);

			if(m_contextMaker.handleRequest(rq, (HttpServletResponse) res, chain))
				return;
		} catch(RuntimeException x) {
			DomUtil.dumpException(x);
			throw x;
		} catch(ServletException x) {
			DomUtil.dumpException(x);
			throw x;
		} catch(IOException x) {
			DomUtil.dumpException(x);
			throw x;
		} catch(Exception x) {
			DomUtil.dumpException(x);
			throw new WrappedException(x); // James Gosling is an idiot
		} catch(Error x) {
			x.printStackTrace();
			throw x;
		}
	}

	static synchronized private void initContext(ServletRequest req) {
		if(m_appContext != null || !(req instanceof HttpServletRequest))
			return;

		m_appContext = NetTools.getApplicationContext((HttpServletRequest) req);
		m_applicationURL = NetTools.getApplicationURL((HttpServletRequest) req);
	}

	static synchronized public String internalGetWebappContext() {
		return m_appContext;
	}

	static synchronized public String getApplicationURL() {
		return m_applicationURL;
	}

	private InputStream findLogConfig(String logconfig) {
		if(logconfig != null) {
			//-- Try to find this as a class-relative resource;
			if(!logconfig.startsWith("/")) {
				InputStream is = getClass().getResourceAsStream("/" + logconfig);
				if(is != null) {
					System.out.println("DomUI: using user-specified logback config file from classpath-resource " + logconfig);
					return is;
				}
			}

			try {
				File f = new File(logconfig);
				if(f.exists() && f.isFile()) {
					System.out.println("DomUI: using logback logging configuration file " + f.getAbsolutePath());
					return new FileInputStream(f);
				}
			} catch(Exception x) {}
		}
		InputStream is = AppFilter.class.getResourceAsStream("logback.xml");
		if(is != null)
			System.out.println("DomUI: using internal logback.xml");
		return is;
	}

	/**
	 * Initialize by reading config from the web.xml.
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(final FilterConfig config) throws ServletException {
		try {
			String logconfig = config.getInitParameter("logpath");
			InputStream logStream = findLogConfig(logconfig);
			if(logStream != null) {
				JoranConfigurator jc = new JoranConfigurator();
				LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
				jc.setContext(lc);
				lc.reset();
				jc.doConfigure(logStream);
				System.out.println("DomUI: logging configured.");
				StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
				SLF4JBridgeHandler.install();

			}
		} catch(Exception x) {
			x.printStackTrace();
			throw WrappedException.wrap(x);
		} catch(Error x) {
			x.printStackTrace();
			throw x;
		}
		try {
			m_logRequest = DeveloperOptions.getBool("domui.logurl", false);

			//-- Get the root for all files in the webapp
			File approot = new File(config.getServletContext().getRealPath("/"));
			System.out.println("WebApp root=" + approot);
			if(!approot.exists() || !approot.isDirectory())
				throw new IllegalStateException("Internal: cannot get webapp root directory");

			m_config = new ConfigParameters(config, approot);

			//-- Handle application construction
			m_applicationClassName = getApplicationClassName(m_config);
			if(m_applicationClassName == null)
				throw new UnavailableException("The application class name is not set. Use 'application' in the Filter parameters to set a main class.");

			//-- Are we running in development mode?
			String autoload = m_config.getString("auto-reload");
			autoload = DeveloperOptions.getString("domui.reload", autoload); // Allow override of web.xml values.

			if(DeveloperOptions.isDeveloperWorkstation() && DeveloperOptions.getBool("domui.developer", true) && autoload != null && autoload.trim().length() > 0)
				m_contextMaker = new ReloadingContextMaker(m_applicationClassName, m_config, autoload);
			else
				m_contextMaker = new NormalContextMaker(m_applicationClassName, m_config);
		} catch(RuntimeException x) {
			DomUtil.dumpException(x);
			throw x;
		} catch(ServletException x) {
			DomUtil.dumpException(x);
			throw x;
		} catch(Exception x) {
			DomUtil.dumpException(x);
			throw new RuntimeException(x); // James Gosling is an idiot
		} catch(Error x) {
			x.printStackTrace();
			throw x;
		}
	}

	public String getApplicationClassName(final ConfigParameters p) {
		return p.getString("application");
	}
}
