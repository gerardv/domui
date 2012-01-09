/*
 * DomUI Java User Interface - shared code
 * Copyright (c) 2010 by Frits Jalvingh, Itris B.V.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * See the "sponsors" file for a list of supporters.
 *
 * The latest version of DomUI and related code, support and documentation
 * can be found at http://www.domui.org/
 * The contact for the project is Frits Jalvingh <jal@etc.to>.
 */
package to.etc.smtp;

import java.io.*;
import java.util.*;

import to.etc.util.*;

/**
 * Utility class to generate email messages containing embedded HTML and plaintext, and allowing
 * for attachment images. This generates an HTML and text-only message at the same time.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Jan 22, 2010
 */
public class MailBuilder {
	/** The stringbuffer for the text-only part */
	private StringBuilder m_text_sb = new StringBuilder();

	/** The part for the HTML variant. */
	private StringBuilder m_html_sb = new StringBuilder();

	private String m_subject;

	static private class Attachment implements IMailAttachment {
		public String mime;

		public String ident;

		public File source;

		public Attachment(String mime, String ident, File source) {
			this.mime = mime;
			this.ident = ident;
			this.source = source;
		}

		public String getIdent() {
			return ident;
		}

		public void setIdent(String ident) {
			this.ident = ident;
		}

		public InputStream getInputStream() throws Exception {
			return new FileInputStream(source);
		}

		public String getMime() {
			return mime;
		}

		public void setMime(String mime) {
			this.mime = mime;
		}
	}

	private List<Attachment> m_attachmentList = new ArrayList<Attachment>();

	private int m_attindex = 1;

	public MailBuilder() {}

	public void initialize(String subject) {
		m_subject = subject;
		m_text_sb.setLength(0);
		m_html_sb.setLength(0);
		m_attachmentList.clear();
		m_attindex = 0;

		m_html_sb.append("<html><head>");
		m_html_sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
		m_html_sb.append("</head>");
		m_html_sb.append("<body>");
		//		m_html_sb.append("");
		//		m_html_sb.append("");
		//		m_html_sb.append("");
		//		m_html_sb.append("");
		//		m_html_sb.append("");
		//		m_html_sb.append("");


	}

	/**
	 * Just add verbatim text, without anything else. Quotes all html content.
	 * @param s
	 * @return
	 */
	public MailBuilder append(String s) {
		m_text_sb.append(s);
		StringTool.htmlStringize(m_html_sb, s);
		return this;
	}

	public MailBuilder appendText(String s) {
		m_text_sb.append(s);
		return this;
	}

	public StringBuilder getHtmlBuffer() {
		return m_html_sb;
	}

	public StringBuilder getTextBuffer() {
		return m_text_sb;
	}

	public MailBuilder appendHTML(String s) {
		m_html_sb.append(s);
		return this;
	}

	public MailBuilder ttl(String s) {
		m_text_sb.append(s);
		m_text_sb.append("\n");
		for(int i = s.length(); --i >= 0;)
			m_text_sb.append('=');
		m_text_sb.append("\n");

		//-- HTML fragment
		m_html_sb.append("<h2>");
		StringTool.htmlStringize(m_html_sb, s);
		m_html_sb.append("</h2>\n");
		return this;
	}

	public MailBuilder i(String s) {
		m_text_sb.append(s);
		m_html_sb.append("<i>");
		StringTool.htmlStringize(m_html_sb, s);
		m_html_sb.append("</i>");
		return this;
	}

	public MailBuilder b(String s) {
		m_text_sb.append(s);
		m_html_sb.append("<b>");
		StringTool.htmlStringize(m_html_sb, s);
		m_html_sb.append("</b>");
		return this;
	}

	public MailBuilder nl() {
		m_text_sb.append("\n");
		m_html_sb.append("<br>");
		return this;
	}

	/**
	 * Render a link in HTML, embedding "text" in the link. The text message will show
	 * <pre>
	 * text (link)
	 * </pre>
	 * @param rurl
	 * @param text
	 * @return
	 */
	public MailBuilder link(String url, String text) {
		m_text_sb.append(text);
		m_text_sb.append(" (");
		m_text_sb.append(url);
		m_text_sb.append(")");

		m_html_sb.append("<a href=\"");
		m_html_sb.append(StringTool.encodeURLEncoded(url));
		m_html_sb.append("\">");
		StringTool.htmlStringize(m_html_sb, text);
		m_html_sb.append("</a>");
		return this;
	}

	public MailBuilder linkNoText(String url, String text) {
		m_text_sb.append(url);
		m_html_sb.append("<a href=\"");
		m_html_sb.append(url);
		m_html_sb.append("\">");
		StringTool.htmlStringize(m_html_sb, text);
		m_html_sb.append("</a>");
		return this;
	}

	/**
	 * Append an image as an attachment, and embed the image in the HTML stream. The text
	 * stream just contains a reference like (see image xxx). The image must be a supported
	 * mime type.
	 *
	 * @param name
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public MailBuilder image(String name, File source, String mime) throws Exception {
		String imgkey = name + "-" + (m_attindex++);

		m_text_sb.append("(see attached image ");
		m_text_sb.append(imgkey);
		m_text_sb.append(") ");

		m_html_sb.append("<img src=\"cid:");
		m_html_sb.append(imgkey);
		m_html_sb.append("\">");

		//-- Create the attachment image.
		m_attachmentList.add(new Attachment(mime, imgkey, source));
		return this;
	}

	public MailBuilder image(String name, String mime, Attachment a) throws Exception {
		String imgkey = name + "-" + (m_attindex++);

		m_text_sb.append("(see attached image ");
		m_text_sb.append(imgkey);
		m_text_sb.append(") ");

		m_html_sb.append("<img src=\"cid:");
		m_html_sb.append(imgkey);
		m_html_sb.append("\">");
		a.setMime(mime);
		a.setIdent(imgkey);

		//-- Create the attachment image.
		m_attachmentList.add(a);
		return this;
	}

	public MailBuilder image(String name, final Class< ? > rbase, final String rname, String mime) throws Exception {
		String imgkey = name + "-" + (m_attindex++);

		m_text_sb.append("(see attached image ");
		m_text_sb.append(imgkey);
		m_text_sb.append(") ");

		m_html_sb.append("<img src=\"cid:");
		m_html_sb.append(imgkey);
		m_html_sb.append("\">");

		Attachment a = new Attachment(mime, imgkey, null) {
			@Override
			public InputStream getInputStream() throws Exception {
				InputStream is = rbase.getResourceAsStream(rname);
				if(is == null)
					throw new IllegalArgumentException("Missing class resource " + rname + " using base class " + rbase);
				return is;
			}
		};

		//-- Create the attachment image.
		m_attachmentList.add(a);
		return this;
	}


	/**
	 * Send it.
	 * @param dest
	 * @throws Exception
	 */
	public void send(Message m) throws Exception {
		//-- Finish html
		m_html_sb.append("</body></html>\n");
		m.setSubject(m_subject);
		m.setBody(m_text_sb.toString());
		m.setHtmlBody(m_html_sb.toString());
		for(Attachment a: m_attachmentList)
			m.addAttachment(a);
		m.send();
	}

	public void send(SmtpTransport t, Address from, List<Address> dest) throws Exception {
		Message m = t.createMessage();
		m.setTo(dest);
		m.setFrom(from);
		send(m);
	}

	public void send(SmtpTransport t, Address from, Address to) throws Exception {
		Message m = t.createMessage();
		List<Address> tol = new ArrayList<Address>();
		tol.add(to);
		m.setTo(tol);
		m.setFrom(from);
		send(m);
	}
}