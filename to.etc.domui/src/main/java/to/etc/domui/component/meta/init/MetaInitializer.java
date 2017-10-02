package to.etc.domui.component.meta.init;

import to.etc.domui.component.meta.ClassMetaModel;
import to.etc.domui.component.meta.MetaManager;
import to.etc.domui.component.meta.PropertyMetaModel;
import to.etc.domui.component.meta.impl.PathPropertyMetaModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 2-10-17.
 */
public final class MetaInitializer {
	static private List<IClassMetaModelFactory> m_modelList = new ArrayList<IClassMetaModelFactory>();

	/**
	 * Map indexed by Class<?> or IMetaClass returning the {@link ClassMetaModel} for that instance.
	 */
	static private Map<Object, ClassMetaModel> m_classMap = new HashMap<>();

	/** While a metamodel is being initialized this keeps track of recursive init's */
	final static private Stack<Object> m_initStack = new Stack<Object>();

	final static private List<Runnable> m_initList = new ArrayList<Runnable>();

	/** When set this means a meta init action is running, and any call coming in asking for class data is wrong/invalid */
	@Nullable
	static private MetaInitContext m_currentContext;

	static final class PropertyProviderRef {
		final private int m_order;

		final private IPropertyMetaProvider m_provider;

		public PropertyProviderRef(int order, IPropertyMetaProvider provider) {
			m_order = order;
			m_provider = provider;
		}

		public int getOrder() {
			return m_order;
		}

		public IPropertyMetaProvider getProvider() {
			return m_provider;
		}
	}

	static final class ClassProviderRef {
		final private int m_order;

		final private IClassMetaProvider m_provider;

		public ClassProviderRef(int order, IClassMetaProvider provider) {
			m_order = order;
			m_provider = provider;
		}

		public int getOrder() {
			return m_order;
		}

		public IClassMetaProvider getProvider() {
			return m_provider;
		}
	}

	static private List<PropertyProviderRef> m_propertyProviderList = Collections.emptyList();

	static private List<ClassProviderRef> m_classProviderList = Collections.emptyList();

	static public synchronized void register(int order, IPropertyMetaProvider provider) {
		ArrayList<PropertyProviderRef> list = new ArrayList<>(m_propertyProviderList);
		list.add(new PropertyProviderRef(order, provider));
		list.sort(Comparator.comparingInt(PropertyProviderRef::getOrder));
		m_propertyProviderList = list;
	}

	static public synchronized void register(int order, IClassMetaProvider provider) {
		ArrayList<ClassProviderRef> list = new ArrayList<>(m_classProviderList);
		list.add(new ClassProviderRef(order, provider));
		list.sort(Comparator.comparingInt(ClassProviderRef::getOrder));
		m_classProviderList = list;
	}

	static synchronized public void registerModel(@Nonnull IClassMetaModelFactory model) {
		List<IClassMetaModelFactory> mm = new ArrayList<IClassMetaModelFactory>(m_modelList);
		mm.add(model);
		m_modelList = mm;
	}

	@Nonnull
	static public synchronized List<IClassMetaModelFactory> getList() {
		if(m_modelList.size() == 0)
			registerModel(new DefaultJavaClassMetaModelFactory());
		return m_modelList;
	}

	/**
	 * Clears the cache. In use by reloading class mechanism,
	 * hence only ever called while developing never in
	 * production. <b>INTERNAL USE ONLY, DO NOT USE</b>
	 */
	public synchronized static void internalClear() {
		m_classMap.clear();
	}

	@Nonnull
	static public ClassMetaModel findAndInitialize(@Nonnull Object mc) {
		//-- We need some factory to create it.
		synchronized(MetaManager.class) {
			ClassMetaModel cmm = m_classMap.get(mc);
			if(cmm != null)
				return cmm;

			//-- Phase 1: create the metamodel and it's direct properties.
			if(m_currentContext != null)
				throw new IllegalStateException("ILLEGAL CALL TO MetaManager from metamodel initialization code!!");
			return initializeRecursively(mc);
		}
	}

	/**
	 * This method is the only one allowed during metamodel initialization. It will
	 * throw {@link ClassModelNotInitializedException} if the class is not yet known.
	 * The method will, however, return incomplete classes (the ones being initialzied).
	 *
	 * @param type
	 * @return
	 */
	static ClassMetaModel getModel(MetaInitContext mc, Object type) {
		ClassMetaModel cmm = m_classMap.get(type);
		if(null != cmm)
			return cmm;

		if(mc != m_currentContext)
			throw new IllegalStateException("Logic error: context passed is not the current context");
		mc.addPendingClass(type);
		throw new ClassModelNotInitializedException(type);
	}

	@Nonnull
	private static ClassMetaModel initializeRecursively(Object mc) {
		MetaInitContext context = new MetaInitContext();			// Keeps track of state, and provides services to metadata providers.
		m_currentContext = context;
		try {
			context.addPendingClass(mc);
			context.initializationLoop();
			//
			////checkInitStack(mc, "primary initialization");      	// Signal any ordering problems
			//IClassMetaModelFactory best = findModelFactory(mc);
			////m_initStack.add(mc);
			//ClassMetaModel cmm = best.createModel(m_initList, mc);
			//m_classMap.put(mc, cmm);
			////m_initStack.remove(mc);
			//
			////-- Phase 2: create the secondary model.
			//if(m_initStack.size() == 0 && m_initList.size() > 0) {
			//	List<Runnable> dl = new ArrayList<Runnable>(m_initList);
			//	m_initList.clear();
			//	for(Runnable r : dl) {
			//		r.run();
			//	}
			//}

			ClassMetaModel cmm = m_classMap.get(mc);
			if(cmm == null)
				throw new IllegalStateException(mc + ": class model not found after initialization");
			return cmm;
		} finally {
			m_currentContext = null;
		}
	}

	static private void initializationLoop(MetaInitContext context) {

	}

	private static void checkInitStack(Object mc, String msg) {
		if(m_initStack.contains(mc)) {
			m_initStack.add(mc);
			StringBuilder sb = new StringBuilder();
			for(Object o : m_initStack) {
				if(sb.length() > 0)
					sb.append(" -> ");
				sb.append(o.toString());
			}
			m_initStack.clear();

			throw new IllegalStateException("Circular reference in " + msg + ": " + sb.toString());
		}
	}

	/**
	 * We need to find a factory that knows how to deliver this metadata.
	 */
	@Nonnull
	synchronized static IClassMetaModelFactory findModelFactory(Object theThingy) {
		int bestscore = 0;
		int hitct = 0;
		IClassMetaModelFactory best = null;
		for(IClassMetaModelFactory mmf : getList()) {
			int score = mmf.accepts(theThingy);
			if(score > 0) {
				if(score == bestscore)
					hitct++;
				else if(score > bestscore) {
					bestscore = score;
					best = mmf;
					hitct = 1;
				}
			}
		}

		//-- We MUST have some factory now, or we're in trouble.
		if(best == null)
			throw new IllegalStateException("No IClassModelFactory accepts the type '" + theThingy + "', which is a " + theThingy.getClass());
		if(hitct > 1)
			throw new IllegalStateException("Two IClassModelFactory's accept the type '" + theThingy + "' (which is a " + theThingy.getClass() + ") at score=" + bestscore);
		return best;
	}


	static public PropertyMetaModel< ? > internalCalculateDottedPath(ClassMetaModel cmm, String name) {
		int pos = name.indexOf('.'); 							// Dotted name?
		if(pos == -1)
			return cmm.findSimpleProperty(name); 				// Use normal resolution directly on the class.

		//-- We must create a synthetic property.
		int ix = 0;
		int len = name.length();
		ClassMetaModel ccmm = cmm; 								// Current class meta-model for property reached
		List<PropertyMetaModel< ? >> acl = new ArrayList<PropertyMetaModel< ? >>(10);
		for(;;) {
			String sub = name.substring(ix, pos); 				// Get path component,
			ix = pos + 1;

			PropertyMetaModel< ? > pmm = ccmm.findSimpleProperty(sub); // Find base property,
			if(pmm == null)
				throw new IllegalStateException("Invalid property path '" + name + "' on " + cmm + ": property '" + sub + "' on classMetaModel=" + ccmm + " does not exist");
			acl.add(pmm); // Next access path,
			ccmm = MetaManager.findClassMeta(pmm.getActualType());

			if(ix >= len)
				break;
			pos = name.indexOf('.', ix);
			if(pos == -1)
				pos = len;
		}

		//-- Resolved to target. Return a complex proxy.
		return new PathPropertyMetaModel<Object>(name, acl.toArray(new PropertyMetaModel[acl.size()]));
	}

	/**
	 * Return a list of all classes whose metamodel is known.
	 *
	 * @return
	 */
	public static synchronized List<ClassMetaModel> getAllMetaClasses() {
		return new ArrayList<>(m_classMap.values());
	}

	public static synchronized List<PropertyProviderRef> getPropertyProviderList() {
		return m_propertyProviderList;
	}

	public static synchronized List<ClassProviderRef> getClassProviderList() {
		return m_classProviderList;
	}
}