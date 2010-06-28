package to.etc.webapp.query;

import java.util.*;

/**
 * A QDataContext proxy which allows queries to be sent to multiple rendering/selecting implementations. It delegates
 * all query handling to the appropriate query handler.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Apr 29, 2010
 */
abstract public class QAbstractDataContext implements QDataContext {
	private QDataContextFactory m_contextFactory;

	protected QAbstractDataContext(QDataContextFactory contextFactory) {
		m_contextFactory = contextFactory;
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	Determine query route helper code.					*/
	/*--------------------------------------------------------------*/

	protected QQueryExecutorRegistry getHandlerFactory() {
		return getFactory().getQueryHandlerList();
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	Partial delegating QDataContext implementation.		*/
	/*--------------------------------------------------------------*/
	/**
	 * {@inheritDoc}
	 */
	public QDataContextFactory getFactory() {
		return m_contextFactory;
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#find(java.lang.Class, java.lang.Object)
	 */
	public <T> T find(final Class<T> clz, final Object pk) throws Exception {
		return getHandlerFactory().getHandler(this, clz).find(this, clz, pk);
	}

	@Override
	public <T> T find(ICriteriaTableDef<T> metatable, Object pk) throws Exception {
		return getHandlerFactory().getHandler(this, metatable).find(this, metatable, pk);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#getInstance(java.lang.Class, java.lang.Object)
	 */
	public <T> T getInstance(Class<T> clz, Object pk) throws Exception {
		return getHandlerFactory().getHandler(this, clz).getInstance(this, clz, pk);
	}

	@Override
	public <T> T getInstance(ICriteriaTableDef<T> metatable, Object pk) throws Exception {
		return getHandlerFactory().getHandler(this, metatable).getInstance(this, metatable, pk);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#query(to.etc.webapp.query.QCriteria)
	 */
	public <T> List<T> query(final QCriteria<T> q) throws Exception {
		getFactory().getEventListeners().callOnBeforeQuery(this, q);
		return getHandlerFactory().getHandler(this, q).query(this, q);
	}

	/**
	 * {@inheritDoc}
	 *
	 * This overrides the behaviour of Hibernate where a single-column selection does not
	 * return an array but that single object, for consistency's sake. It is slightly more
	 * expensive because an Object[1] is needed for every row, but compared with the heaps
	 * of memory Hibernate is already wasting this is peanuts.
	 *
	 * @see to.etc.webapp.query.QDataContext#query(to.etc.webapp.query.QSelection)
	 */
	public List<Object[]> query(QSelection< ? > sel) throws Exception {
		getFactory().getEventListeners().callOnBeforeQuery(this, sel);
		return getHandlerFactory().getHandler(this, sel).query(this, sel);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#queryOne(to.etc.webapp.query.QCriteria)
	 */
	public <T> T queryOne(final QCriteria<T> q) throws Exception {
		List<T> res = query(q);
		if(res.size() == 0)
			return null;
		if(res.size() > 1)
			throw new IllegalStateException("The criteria-query " + q + " returns " + res.size() + " results instead of one");
		return res.get(0);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#queryOne(to.etc.webapp.query.QCriteria)
	 */
	public Object[] queryOne(final QSelection< ? > sel) throws Exception {
		List<Object[]> res = query(sel);
		if(res.size() == 0)
			return null;
		if(res.size() > 1)
			throw new IllegalStateException("The criteria-query " + sel + " returns " + res.size() + " results instead of one");
		return res.get(0);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#attach(java.lang.Object)
	 */
	public void attach(final Object o) throws Exception {
		getHandlerFactory().getHandler(this, o).attach(this, o);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#delete(java.lang.Object)
	 */
	public void delete(final Object o) throws Exception {
		getHandlerFactory().getHandler(this, o).delete(this, o);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#save(java.lang.Object)
	 */
	public void save(final Object o) throws Exception {
		getHandlerFactory().getHandler(this, o).save(this, o);
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.webapp.query.QDataContext#refresh(java.lang.Object)
	 */
	public void refresh(final Object o) throws Exception {
		getHandlerFactory().getHandler(this, o).refresh(this, o);
	}
}