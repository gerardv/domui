package to.etc.domui.hibernate.generic;

import java.util.*;

import org.hibernate.*;
import org.hibernate.engine.*;
import org.hibernate.impl.*;

import to.etc.domui.state.*;
import to.etc.webapp.query.*;

/**
 * A context that keeps the session alive but in disconnected mode while running.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Oct 23, 2008
 */
public class HibernateLongSessionContext extends BuggyHibernateBaseContext {
	public HibernateLongSessionContext(final QDataContextFactory src, final HibernateSessionMaker sessionMaker) {
		super(sessionMaker, src);
	}

	@Override
	public Session getSession() throws Exception {
		if(m_session == null) {
			super.getSession();
			m_session.setFlushMode(FlushMode.MANUAL);
		}
		if(!m_session.isConnected())
			System.out.println("Hibernate: reconnecting session.");
		return m_session;
	}

	@Override
	public void conversationDestroyed(final ConversationContext cc) throws Exception {
		conversationDetached(cc);
	}

	@Override
	public void conversationDetached(final ConversationContext cc) throws Exception {
		if(m_session == null || !m_session.isConnected())
			return;
		SessionImpl sim = (SessionImpl) m_session;
		StatefulPersistenceContext spc = (StatefulPersistenceContext) sim.getPersistenceContext();
		Map< ? , ? > flups = spc.getEntitiesByKey();
		System.out.println("Hibernate: disconnecting session containing " + flups.size() + " persisted instances");
		if(m_session.getTransaction().isActive())
			m_session.getTransaction().rollback();
		m_session.disconnect(); // Disconnect the dude.
		//		if(m_session.isConnected())
		//			System.out.println("Session connected after disconnect ;-)");
	}

	@Override
	public void commit() throws Exception {
		m_session.flush();
		super.commit();
	}
}
