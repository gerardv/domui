package to.etc.domuidemo.pages.lookupform2;

import to.etc.domui.component.lookupform2.LookupForm2;
import to.etc.domui.derbydata.db.Invoice;
import to.etc.domui.dom.html.UrlPage;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 21-1-18.
 */
public class Lookup2OnePage extends UrlPage {
	@Override public void createContent() throws Exception {
		LookupForm2<Invoice> lf = new LookupForm2<>(Invoice.class);






	}
}
