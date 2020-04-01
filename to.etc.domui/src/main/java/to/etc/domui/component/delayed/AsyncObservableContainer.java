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
package to.etc.domui.component.delayed;

import io.reactivex.rxjava3.core.Observable;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.component.buttons.DefaultButton;
import to.etc.domui.dom.css.DisplayType;
import to.etc.domui.dom.html.Div;
import to.etc.domui.dom.html.Img;
import to.etc.domui.state.DelayedActivityInfo;
import to.etc.domui.state.DelayedActivityInfo.State;
import to.etc.domui.themes.Theme;
import to.etc.domui.util.Msgs;
import to.etc.parallelrunner.IAsyncCompletionListener;
import to.etc.parallelrunner.IAsyncRunnable;
import to.etc.util.Progress;

/**
 * A DomUI container (node) that embeds a task producing output to an Observable.
 *
 */
final public class AsyncObservableContainer<T> extends Div implements IAsyncContainer {
	@NonNull
	final private IAsyncRunnable m_runnable;

	@Nullable
	final private IAsyncCompletionListener m_resultListener;

	private DelayedActivityInfo m_scheduledActivity;

	private Div m_progress;

	/**
	 * Defines if async action can be cancelled. T by default.
	 */
	private boolean m_abortable = true;

	/**
	 * Defines busy image src. If not set uses default framework resource.
	 */
	private String m_busyMarkerSrc = "THEME/asy-container-busy.gif";

	public AsyncObservableContainer(@NonNull IAsyncRunnable arunnable) {
		this(arunnable, null);
	}

	public AsyncObservableContainer(@NonNull IAsyncRunnable arunnable, @Nullable IAsyncCompletionListener listener) {
		m_runnable = arunnable;
		m_resultListener = listener;
	}

	public AsyncObservableContainer<T> inline() {
		setDisplay(DisplayType.INLINE_BLOCK);
		setAbortable(false);
		setBusyMarkerSrc("THEME/io-blk-wait.gif");
		return this;
	}

	public Observable<T> observe() {
		throw new IllegalStateException();



	}

	@Override
	public void createContent() throws Exception {
		if(m_scheduledActivity == null) {
			m_scheduledActivity = getPage().getConversation().scheduleDelayed(this, m_runnable);
		}

		//-- Render a thingy containing a spinner
		setCssClass("ui-asc");
		Img img = new Img();
		img.setSrc(getBusyMarkerSrc());
		add(img);
		m_progress = new Div();
		add(m_progress);
		if(isAbortable()) {
			DefaultButton db = new DefaultButton(Msgs.BUNDLE.getString(Msgs.LOOKUP_FORM_CANCEL), Theme.BTN_CANCEL, b -> {
				cancel();
				b.setDisabled(true);
			});
			add(db);
		}
	}

	void cancel() {
		if(m_scheduledActivity != null)
			m_scheduledActivity.cancel();
	}

	/**
	 * Update the progress report.
	 */
	@Override
	public void updateProgress(DelayedActivityInfo dai) throws Exception {
		if(dai.getState() == State.DONE) {
			updateCompleted(dai);
		} else {
			Progress progress = dai.getMonitor();
			StringBuilder sb = new StringBuilder();
			sb.append(progress.getPercentage());
			sb.append("%");

			sb.append(' ');
			String actionPath = progress.getActionPath(3);
			sb.append(actionPath);

			//if(msg != null) {
			//	sb.append(' ');
			//	sb.append(msg);
			//} else {
			//	sb.append(" " + Msgs.BUNDLE.getString(Msgs.ASYNC_CONTAINER_COMPLETE_INDICATOR));
			//}
			m_progress.setText(sb.toString());
		}
	}

	private void updateCompleted(DelayedActivityInfo dai) throws Exception {
		//-- Call the node's update handler *before* removing myself.
		try {
			IAsyncCompletionListener resultListener = m_resultListener;
			if(null != resultListener)
				resultListener.onCompleted(dai.getMonitor().isCancelled(), dai.getException());
			else {
				new DefaultAsyncCompletionListener(getParent()).onCompleted(dai.getMonitor().isCancelled(), dai.getException());
			}
			//dai.getActivity().onCompleted(dai.getMonitor().isCancelled(), dai.getException());
		} finally {
			try {
				remove();								// Remove myself *after* this all.
			} catch(Exception x) {
				System.err.println("Could not remove AsyncContainer: " + x);
				x.printStackTrace();
			}
		}
	}

	public void confirmCancelled() {
		setText(Msgs.BUNDLE.getString(Msgs.ASYNC_CONTAINER_CANCELLED));
	}

	public boolean isAbortable() {
		return m_abortable;
	}

	public void setAbortable(boolean abortable) {
		m_abortable = abortable;
	}

	public String getBusyMarkerSrc() {
		return m_busyMarkerSrc;
	}

	public void setBusyMarkerSrc(String busyMarkerSrc) {
		m_busyMarkerSrc = busyMarkerSrc;
	}
}
