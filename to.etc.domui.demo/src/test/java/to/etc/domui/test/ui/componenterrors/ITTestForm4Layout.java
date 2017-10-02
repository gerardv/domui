package to.etc.domui.test.ui.componenterrors;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import to.etc.domuidemo.pages.test.componenterrors.Form4LayoutTestPage;

import java.util.Map;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 12-9-17.
 */
public class ITTestForm4Layout extends AbstractLayoutTest {
	public ITTestForm4Layout() {
	}

	@Override
	protected void initializeScreen() throws Exception {
		wd().openScreenIf(this, Form4LayoutTestPage.class);
		wd().cmd().type("aaaaaaaaa").on("two", "input");
		wd().wait(By.className("ui-lui-popup"));
		wd().cmd().type("aaaaaaaaa").on("four", "input");
	}

	@Test
	public void testLookup1Baseline() throws Exception {
		checkBaseLine("one", "span");
	}

	@Test
	public void testLookup2Baseline() throws Exception {
		checkBaseLine("two", "input");
	}

	@Test
	public void testLookup3Baseline() throws Exception {
		checkBaseLine("three", "span");
	}

	@Test
	public void testLookup4Baseline() throws Exception {
		checkBaseLine("four", "input");
	}

	@Test
	public void testText1baseline() throws Exception {
		checkBaseLine("six", "");
	}

	@Test
	public void testDate1Baseline() throws Exception {
		checkBaseLine("date", "");
	}

	@Test
	public void testTextArea1() throws Exception {
		checkBaseLine("memo", "");
	}

	@Test
	public void testTextArea2() throws Exception {
		WebElement memo = wd().getElement("memo");
		Map<String, String> styles = wd().getComputedStyles(memo, a -> ! a.startsWith("-"));
		System.out.println("styles = " + styles);
	}
}