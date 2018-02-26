package to.etc.domuidemo.pages.overview.input;

import to.etc.domui.component.buttons.DefaultButton;
import to.etc.domui.component.input.SearchAsYouType;
import to.etc.domui.derbydata.db.Genre;
import to.etc.domui.dom.html.Div;
import to.etc.domui.dom.html.UrlPage;
import to.etc.webapp.query.QCriteria;

import java.util.List;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 18-2-18.
 */
public class DemoSearchAsYouType1 extends UrlPage {
	@Override public void createContent() throws Exception {
		//-- Make a set of cities.
		List<Genre> genreList = getSharedContext().query(QCriteria.create(Genre.class));
		SearchAsYouType<Genre> st = new SearchAsYouType<>(Genre.class)
			.setData(genreList)
			.setSearchProperty("name")
			;
		st.setMandatory(true);
		add(st);

		Div d = new Div();
		add(d);

		DefaultButton b = new DefaultButton("validate", a -> {
			Div res = new Div();
			add(res);
			res.add("Result is " + st.getValue());
		});
		add(b);
	}
}
