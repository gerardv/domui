package to.etc.webapp.ajax.eventmanager;

/**
 * Holds an event as generated by the code, and the data
 * needed to cleanup the event.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Oct 25, 2006
 */
final public class QueuedEvent {
	/** The channel that this event fires on */
	private final String m_channel;

	private final Object m_data;

	/** The framework-assigned event ID */
	private final int m_eventID;

	/** The time the event was fired (for expiry pps) */
	private final long m_eventTS;

	public QueuedEvent(final int eventID, final long eventTS, final String channel, final Object data) {
		m_eventID = eventID;
		m_eventTS = eventTS;
		m_channel = channel;
		m_data = data;
	}

	QueuedEvent createCopy(final Object newdata) {
		return new QueuedEvent(m_eventID, m_eventTS, m_channel, newdata);
	}

	public String getChannel() {
		return m_channel;
	}

	public Object getData() {
		return m_data;
	}

	public int getEventID() {
		return m_eventID;
	}

	long getEventTS() {
		return m_eventTS;
	}
}
