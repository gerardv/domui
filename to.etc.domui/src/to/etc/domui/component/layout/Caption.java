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

import to.etc.domui.component.buttons.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.util.*;

public class Caption extends Table {
	private String m_caption;

	private TD m_buttonpart;

	private List<SmallImgButton> m_btns = Collections.EMPTY_LIST;

	public Caption() {}

	public Caption(String ttl) {
		m_caption = ttl;
	}

	public String getCaption() {
		return m_caption;
	}

	public void setCaption(String caption) {
		if(DomUtil.isEqual(m_caption, caption))
			return;
		m_caption = caption;
		forceRebuild();
	}

	@Override
	public void createContent() throws Exception {
		setCssClass("ui-cptn");
		setCellPadding("0");
		setCellSpacing("0");
		setTableWidth("100%");
		TBody b = addBody();
		TD ttltd = b.addRowAndCell();
		ttltd.setCssClass("ui-cptn-ttl");
		ttltd.setNowrap(true);
		Div ttl = new Div();
		ttltd.add(ttl);
		//		ttl.setCssClass("ui-cptn-ttl");
		ttl.setText(m_caption);
		TD right = b.addCell();
		right.setCssClass("ui-cptn-btn");
		m_buttonpart = right;
		right.setAlign(TDAlignType.RIGHT);
		for(SmallImgButton btn : m_btns) {
			m_buttonpart.add(btn);
		}
	}

	public void addButton(String image, String hint, IClicked<NodeBase> handler) {
		SmallImgButton ib = new SmallImgButton(image);
		ib.setClicked(handler);
		internallyAddButton(ib, hint);
	}

	public void addButton(String image, String hint, String onClickJs) {
		SmallImgButton ib = new SmallImgButton(image);
		ib.setOnClickJS(onClickJs);
		internallyAddButton(ib, hint);
	}

	private void internallyAddButton(SmallImgButton ib, String hint) {
		if(m_btns == Collections.EMPTY_LIST) {
			m_btns = new ArrayList<SmallImgButton>();
		}
		ib.setTitle(hint);
		m_btns.add(ib);
		if(isBuilt() && m_buttonpart != null) {
			m_buttonpart.add(ib);
		}
	}
}
