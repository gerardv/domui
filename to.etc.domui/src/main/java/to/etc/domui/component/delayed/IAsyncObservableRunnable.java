package to.etc.domui.component.delayed;

import io.reactivex.rxjava3.core.Emitter;
import org.eclipse.jdt.annotation.NonNullByDefault;
import to.etc.util.Progress;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 01-04-20.
 */
@NonNullByDefault
public interface IAsyncObservableRunnable<T> {
	void run(Emitter<T>emitter, Progress progress) throws Exception;
}
