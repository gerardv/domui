package to.etc.domui.rxjava;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import to.etc.domui.dom.html.NodeBase;
import to.etc.domui.dom.html.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This JavaRX scheduler takes asynchronous events and schedules them to
 * run on a DomUI page event. Meaning that after using this as a scheduler
 * inside an observer chain the following items can directly change page structures
 * of the page scheduled on.
 *
 * <h2>Example Usage:</h2>
 * <pre>
 *	 command.observeEvents()
 *	 .observeOn(PageScheduler.on(this))
 *	 .subscribe(
 *	 	o -> handlePacket(o),
 *	 	e-> ExceptionDialog.create(this, e),
 *	 	()-> handleCompletion()
 *	 );
 * </pre>
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 15-11-19.
 */
final public class PageScheduler extends Scheduler {
	final private Page m_page;

	public PageScheduler(Page page) {
		m_page = page;
	}

	static public Scheduler on(NodeBase pageNode) {
		Page page = pageNode.getPage();
		return new PageScheduler(page);
	}

	@Override
	public Worker createWorker() {
		DomUIPageWorker w = (DomUIPageWorker) m_page.getConversation().getAttribute("rx$worker");
		if(null == w) {
			w = new DomUIPageWorker(m_page);
			m_page.getConversation().setAttribute("rx$worker", w);
		}
		return w;
	}

	/**
	 * Worker that registers with a DomUI page, and uses the callbacks to execute rxjava events only
	 * when the page has been activated, for instance because of a PollingDiv.
	 */
	static private final class DomUIPageWorker extends Worker {
		private final List<PageWork> m_workList = new ArrayList<>();

		private boolean m_disposed;

		private final Page m_page;

		public DomUIPageWorker(Page page) {
			m_page = page;
			m_page.addDestroyListener(this::dispose);
			m_page.addBeforeRequestListener(this::sync);
		}

		/**
		 * Called when we're entering page context: this processes all queued work.
		 */
		private void sync() {
			if(m_workList.size() == 0)
				return;

			long cts = System.currentTimeMillis();
			List<PageWork> todoList = new ArrayList<>();
			synchronized(this) {
				while(m_workList.size() > 0) {
					PageWork w = m_workList.get(0);
					if(w.getExecuteWhen() > cts) {
						break;
					}
					todoList.add(w);
					m_workList.remove(0);
				}
			}

			for(PageWork pageWork : todoList) {
				pageWork.getWork().run();
			}
		}

		@Override
		public Disposable schedule(Runnable runnable, long l, TimeUnit timeUnit) {
			long ets = System.currentTimeMillis() + timeUnit.toMillis(l);			// Find execution time.
			PageWork w = new PageWork(this, runnable, ets);
			synchronized(this) {
				if(m_disposed)
					return Disposable.disposed();

				if(m_workList.size() == 0) {
					m_workList.add(w);
				} else {
					int index = Collections.binarySearch(m_workList, w, Comparator.comparingLong(PageWork::getExecuteWhen));
					if(index < 0)
						index = (-index - 1);
					m_workList.add(index, w);
				}
			}

			return w;
		}

		@Override
		public void dispose() {
			synchronized(this) {
				if(m_disposed)
					return;
				m_disposed = true;
				m_workList.clear();
			}
			m_page.removeDestroyListener(this::dispose);
		}

		@Override
		public synchronized boolean isDisposed() {
			return m_disposed;
		}
	}

	private final static class PageWork implements Disposable {
		private final DomUIPageWorker m_worker;

		private final Runnable m_work;

		private final long m_executeWhen;

		private boolean m_disposed;

		public PageWork(DomUIPageWorker worker, Runnable work, long executeWhen) {
			m_worker = worker;
			m_work = work;
			m_executeWhen = executeWhen;
		}

		public Runnable getWork() {
			return m_work;
		}

		public long getExecuteWhen() {
			return m_executeWhen;
		}

		@Override
		public void dispose() {
			synchronized(m_worker) {
				if(m_disposed)
					return;
				m_disposed = true;
				m_worker.m_workList.remove(this);
			}
		}

		@Override
		public boolean isDisposed() {
			synchronized(m_worker) {
				return m_disposed;
			}
		}
	}
}
