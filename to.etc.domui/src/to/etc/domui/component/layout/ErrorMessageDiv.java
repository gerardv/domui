/*
 * DomUI Java User Interface library
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
package to.etc.domui.component.layout;

import java.util.*;

import javax.annotation.*;

import to.etc.domui.dom.css.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.util.*;

/**
 * This is the default in-component error handling panel, for components that
 * handle their own errors. By default it is hidden. There are two modes of use:
 * <ul>
 *	<li>First direct approach use ErrorMessageDiv(NodeContainer parent) constructor. Once ErrorMessageDiv is
 *		created, fence is set to parent container, and ErrorMessageDiv is registered as error listener.</li>
 *	<li>Second approach is to use ErrorMessageDiv() constructor. In this mode of use setting of ErrorMessageDiv as
 *		error listener has do be done by hand. This is used when setting ErrorMessageDiv as external error
 *		listener to domui components. Once an error is raised ErrorMessageDiv becomes visible, otherwise
 *		it is rendered as hidden.</li>
 * </ul>
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * @author <a href="mailto:vmijic@execom.eu">Vladimir Mijic</a>
 * Created on Dec 9, 2009
 */
public class ErrorMessageDiv extends Div implements IErrorMessageListener {
	private List<UIMessage> m_msgList = new ArrayList<UIMessage>();

	public ErrorMessageDiv(NodeContainer parent) {
		this(parent, false);
	}

	public ErrorMessageDiv(NodeContainer parent, final boolean propagateThroughFence) {
		if(propagateThroughFence) {
			PropagatingErrorFenceHandler handler = new PropagatingErrorFenceHandler(parent);
			parent.setErrorFence(handler);
		} else {
			parent.setErrorFence();
		}
		DomUtil.getMessageFence(parent).addErrorListener(this);
		setCssClass("ui-emd");
		setVisibility(VisibilityType.HIDDEN);
	}

	public ErrorMessageDiv() {
		setCssClass("ui-emd");
		setVisibility(VisibilityType.HIDDEN);
	}

	public void setAsErrorFence(NodeContainer parent) {
		parent.setErrorFence();
		DomUtil.getMessageFence(parent).addErrorListener(this);
	}

	/**
	 * Adds the error message to the visible list. If this div is not yet visible then
	 * make it visible. If the message added is NOT an INFO message (meaning it is more
	 * important) then all INFO messages that are <i>already shown</i> are removed from
	 * the display and the message list.
	 *
	 * @see to.etc.domui.dom.errors.IErrorMessageListener#errorMessageAdded(to.etc.domui.dom.errors.UIMessage)
	 */
	@Override
	public void errorMessageAdded(@Nonnull UIMessage m) {
		if(m_msgList.contains(m))
			return;

		if(m.getType() != MsgType.INFO) {
			List<Div> errorDivs = getChildren(Div.class); // FIXME Why DIV's only?
			for(Div errorDiv : errorDivs) {
				if(errorDiv.getUserObject() instanceof UIMessage && ((UIMessage) errorDiv.getUserObject()).getType() == MsgType.INFO) {
					m_msgList.remove(errorDiv.getUserObject());
					errorDiv.remove();
				}
			}
		}
		m_msgList.add(m);
		createErrorUI(m);
	}

	protected void createErrorUI(UIMessage m) {
		if(getVisibility() != VisibilityType.VISIBLE) {
			setVisibility(VisibilityType.VISIBLE);
		}
		NodeContainer nc = createErrorLine(m);
		add(nc);
	}

	protected NodeContainer createErrorLine(UIMessage m) {
		Div d = new Div();
		add(d);
		d.setCssClass("ui-emd-msg ui-emd-" + m.getType().name().toLowerCase());
		d.setUserObject(m);
		DomUtil.renderErrorMessage(d, m);
		if(m.getErrorNode() != null) {
			m.getErrorNode().addCssClass("ui-input-err");
		}
		return d;
	}

	@Override
	public void errorMessageRemoved(@Nonnull UIMessage m) {
		if(!m_msgList.remove(m))
			return;

		for(NodeBase b : this) {
			if(b.getUserObject() == m) {
				//-- Remove this object!
				b.remove();
				if(m.getErrorNode() != null)
					m.getErrorNode().removeCssClass("ui-input-err");
				break;
			}
		}

		if(getChildCount() == 0) {
			setVisibility(VisibilityType.HIDDEN);
		}
	}
}
