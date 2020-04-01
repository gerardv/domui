package to.etc.domui.component.delayed;

import to.etc.domui.dom.html.Page;
import to.etc.domui.state.DelayedActivityInfo;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 01-04-20.
 */
public interface IAsyncContainer {
	Page getPage();

	void updateProgress(DelayedActivityInfo dai) throws Exception;

	/**
	 * Called when a delayed activity is cancelled.
	 */
	void confirmCancelled();
}
