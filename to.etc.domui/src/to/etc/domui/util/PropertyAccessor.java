package to.etc.domui.util;

import java.beans.*;
import java.lang.reflect.*;

/**
 * Generalized access to a property's value.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Jun 18, 2008
 */
final public class PropertyAccessor<T> implements IValueAccessor<T> {
	private Method m_readm;

	private Method m_writem;

	public PropertyAccessor(Method getmethod, Method setmethod) {
		m_readm = getmethod;
		m_writem = setmethod;
	}

	public PropertyAccessor(PropertyDescriptor pd) {
		this(pd.getReadMethod(), pd.getWriteMethod());
	}

	public void setValue(Object target, T value) throws Exception {
		if(target == null)
			throw new IllegalStateException("The 'target' object is null");
		try {
			m_writem.invoke(target, value);
		} catch(InvocationTargetException itx) {
			Throwable c = itx.getCause();
			if(c instanceof Exception)
				throw (Exception) c;
			else if(c instanceof Error)
				throw (Error) c;
			else
				throw itx;
		}
	}

	/**
	 * Retrieve the value from this object. If the input object is null
	 * this throws NPE.
	 *
	 * @see to.etc.domui.util.IValueTransformer#getValue(java.lang.Object)
	 */
	public T getValue(Object in) throws Exception {
		if(in == null)
			throw new IllegalStateException("The 'input' object is null (getter method=" + m_readm + ")");
		try {
			return (T) m_readm.invoke(in);
		} catch(InvocationTargetException itx) {
			System.err.println("(in calling " + m_readm + " with input object " + in + ")");
			Throwable c = itx.getCause();
			if(c instanceof Exception)
				throw (Exception) c;
			else if(c instanceof Error)
				throw (Error) c;
			else
				throw itx;
		} catch(Exception x) {
			System.err.println("in calling " + m_readm + " with input object " + in);
			throw x;
		}
	}

}
