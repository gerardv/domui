package to.etc.domui.component.input;

import java.util.*;

import to.etc.domui.component.meta.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.server.*;
import to.etc.domui.trouble.*;
import to.etc.domui.util.*;
import to.etc.util.*;

public class ComboComponentBase<T, V> extends Select implements IInputNode<V>, IHasModifiedIndication {
	private String m_emptyText;

	private V m_currentValue;

	/**
	 * If this combobox has a "unselected" option currently this contains that option. When present it
	 * means that indexes in the <i>combo</i> list are one <i>higher</i> than indexes in the backing
	 * dataset (because this empty option is always choice# 0).
	 */
	private SelectOption m_emptyOption;

	private List<T> m_data;

	/** The specified ComboRenderer used. */
	private INodeContentRenderer<T> m_contentRenderer;

	private INodeContentRenderer<T> m_actualContentRenderer;

	private Class< ? extends INodeContentRenderer<T>> m_contentRendererClass;

	private PropertyMetaModel m_propertyMetaModel;

	/** When set this maker will be used to provide a list of values for this combo. */
	private IListMaker<T> m_listMaker;

	private Class< ? extends IComboDataSet<T>> m_dataSetClass;

	private IComboDataSet<T> m_dataSet;

	private IValueTransformer<V> m_valueTransformer;

	public ComboComponentBase() {}

	public ComboComponentBase(IListMaker<T> maker) {
		m_listMaker = maker;
	}

	public ComboComponentBase(IComboDataSet<T> dataSet) {
		m_dataSet = dataSet;
	}

	public ComboComponentBase(Class< ? extends IComboDataSet<T>> dataSetClass) {
		m_dataSetClass = dataSetClass;
	}

	public ComboComponentBase(List<T> in) {
		m_data = in;
	}

	public ComboComponentBase(Class< ? extends IComboDataSet<T>> set, INodeContentRenderer<T> r) {
		m_dataSetClass = set;
		m_contentRenderer = r;
	}

	/**
	 * Render the actual combobox. This renders the value domain as follows:
	 * <ul>
	 *	<li>If the combobox is <i>optional</i> then the value list will always start with an "unselected" option
	 *		which will be shown if the value is null.</li>
	 *	<li>If the combobox is mandatory <i>but</i> it's current value is not part of the value domain (i.e. it
	 *		is null, or the value cannot be found in the list of values) then it <i>also</i> renders an initial
	 *		"unselected" option value which will become selected.</li>
	 *	<li>For a mandatory combo with a valid value the "empty" choice will not be rendered.</li>
	 * </ul>
	 * Fixes bug# 790.
	 */
	@Override
	public void createContent() throws Exception {
		//-- Append shtuff to the combo
		List<T>	list = getData();
		V raw = internalGetCurrentValue();

		//-- First loop over all values to find out if current value is part of value domain.
		boolean isvalidselection = false;
		int ix = 0;
		ClassMetaModel cmm = null;
		for(T val : list) {
			SelectOption o = new SelectOption();
			add(o);
			renderOptionLabel(o, val);
			if(null != raw) {
				V res = listToValue(val);
				if(cmm == null)
					cmm = MetaManager.findClassMeta(res.getClass());
				boolean eq = MetaManager.areObjectsEqual(res, raw, cmm);
				if(eq) {
					o.setSelected(eq);
					internalSetSelectedIndex(ix);
					isvalidselection = true;
				}
			}
			ix++;
		}

		//-- Decide if an "unselected" option needs to be present, and add it at index 0 if so.
		if(!isMandatory() || !isvalidselection) {
			//-- We need the "unselected" option.
			SelectOption o = new SelectOption();
			if(getEmptyText() != null)
				o.setText(getEmptyText());
			add(0, o); // Insert as the very 1st item
			setEmptyOption(o); // Save this to mark it in-use.
			if(!isvalidselection) {
				o.setSelected(true);
				internalSetSelectedIndex(0);
			} else
				internalSetSelectedIndex(getSelectedIndex() + 1); // Increment selected index thingy
		}
	}


	/*--------------------------------------------------------------*/
	/*	CODING:	value setting logic.								*/
	/*--------------------------------------------------------------*/
	/**
	 * @see to.etc.domui.dom.html.IInputNode#getValue()
	 */
	@Override
	final public V getValue() {
		if(isMandatory() && m_currentValue == null) {
			setMessage(UIMessage.error(Msgs.BUNDLE, Msgs.MANDATORY));
			throw new ValidationException(Msgs.MANDATORY);
		} else
			clearMessage();
		return m_currentValue;
	}

	/**
	 * Set the combo to the specified value. The value <i>must</i> be in the
	 * domain specified by the data list and must be findable in that list; if
	 * not <b>the results are undefined</b>.
	 * If the value set is null and the combobox is a mandatory one the code will
	 * check if an "unselected" item is present to select. If not the unselected
	 * item will be added by this call(!).
	 * @see to.etc.domui.dom.html.IInputNode#setValue(java.lang.Object)
	 */
	final public void setValue(V v) {
		ClassMetaModel cmm = v != null ? MetaManager.findClassMeta(v.getClass()) : null;
		if(MetaManager.areObjectsEqual(v, m_currentValue, cmm))
			return;
		m_currentValue = v;
		if(!isBuilt())
			return;

		//-- If the value is NULL we MUST have an unselected option: add it if needed and select that one.
		int ix = findListIndexForValue(v);
		if(null == v || ix < 0) { // Also create "unselected" if the value is not part of the domain.
			if(getEmptyOption() == null) {
				//-- No empty option yet!! Create one;
				SelectOption o = new SelectOption();
				if(getEmptyText() != null)
					o.setText(getEmptyText());
				add(0, o); // Insert as the very 1st item
				setEmptyOption(o); // Save this to mark it in-use.
			}
			setSelectedIndex(0);
			return;
		}

		//-- Value is not null. Find the index of the option in the dataset
		if(getEmptyOption() != null)
			ix++;
		setSelectedIndex(ix);
	}

	/**
	 * The user selected a different option.
	 * @see to.etc.domui.dom.html.Select#internalOnUserInput(int, int)
	 */
	@Override
	protected boolean internalOnUserInput(int oldindex, int nindex) {
		V newval;

		if(nindex < 0) {
			newval = null; // Should never happen
		} else if(getEmptyOption() != null) {
			//-- We have an "unselected" choice @ index 0. Is that one selected?
			if(nindex <= 0) // Empty value chosen?
				newval = null;
			else {
				nindex--;
				newval = findListValueByIndex(nindex);
			}
		} else {
			newval = findListValueByIndex(nindex);
		}

		ClassMetaModel cmm = newval == null ? null : MetaManager.findClassMeta(newval.getClass());
		if(MetaManager.areObjectsEqual(newval, m_currentValue, cmm))
			return false;
		m_currentValue = newval;
		return true;
	}

	/**
	 * Return the index in the data list for the specified value, or -1 if not found or if the value is null.
	 * @param newvalue
	 * @return
	 */
	private int findListIndexForValue(V newvalue) {
		if(null == newvalue)
			return -1;
		try {
			ClassMetaModel	cmm = newvalue == null ? null : MetaManager.findClassMeta(newvalue.getClass());;
			List<T> data = getData();
			for(int ix = 0; ix < data.size(); ix++) {
				V	value = listToValue(data.get(ix));
				if(MetaManager.areObjectsEqual(value, newvalue, cmm))
					return ix;
			}
			return -1;
		} catch(Exception x) { // Need to wrap; James Gosling is an idiot.
			throw WrappedException.wrap(x);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see to.etc.domui.component.input.SelectBasedControl#findListValueByIndex(int)
	 */
	private V findListValueByIndex(int ix) {
		try {
			List<T> data = getData();
			if(ix < 0 || ix >= data.size())
				return null;
			return listToValue(data.get(ix));
		} catch(Exception x) { // Need to wrap; James Gosling is an idiot.
			throw WrappedException.wrap(x);
		}
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	List - value conversions and list management		*/
	/*--------------------------------------------------------------*/
	/**
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected V listToValue(T in) throws Exception {
		if(m_valueTransformer == null)
			return (V) in;
		return m_valueTransformer.getValue(in);
	}

	private INodeContentRenderer<T> calculateContentRenderer(Object val) {
		if(m_contentRenderer != null)
			return m_contentRenderer;
		if(m_contentRendererClass != null)
			return DomApplication.get().createInstance(m_contentRendererClass);

		if(val == null)
			throw new IllegalStateException("Cannot calculate content renderer for null value");
		ClassMetaModel cmm = MetaManager.findClassMeta(val.getClass());
		return (INodeContentRenderer<T>) MetaManager.createDefaultComboRenderer(m_propertyMetaModel, cmm);
	}

	protected void renderOptionLabel(SelectOption o, T object) throws Exception {
		if(m_actualContentRenderer == null)
			m_actualContentRenderer = calculateContentRenderer(object);
		m_actualContentRenderer.renderNodeContent(this, o, object, this);
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	All the myriad ways of providing data.				*/
	/*--------------------------------------------------------------*/
	/**
	 * Can be used to set a specific list-of-values. When called this clears the existing dataset.
	 * @param data
	 */
	public void setData(List<T> data) {
		if(m_data != data) {
			forceRebuild();
			m_actualContentRenderer = null;
		}
		m_data = data;
	}

	/**
	 * Returns the data to use as the list-of-values of this combo. This must contain actual selectable
	 * values only, it may not contain a "no selection made" value thingerydoo.
	 * @return
	 * @throws Exception
	 */
	public List<T> getData() throws Exception {
		if(m_data == null)
			m_data = provideData();
		return m_data;
	}

	/**
	 * Creates the list-of-values that is to be used if no specific lov is set using setData(). The
	 * default implementation walks the data providers to see if one is present.
	 * @return
	 * @throws Exception
	 */
	protected List<T> provideData() throws Exception {
		if(m_listMaker != null)
			return DomApplication.get().getCachedList(m_listMaker);

		//-- Try datasets,
		IComboDataSet<T> builder = m_dataSet;
		if(builder == null && m_dataSetClass != null)
			builder = DomApplication.get().createInstance(m_dataSetClass);
		if(builder != null)
			return builder.getComboDataSet(getPage().getConversation(), null);
		return Collections.EMPTY_LIST;
		//
		//		throw new IllegalStateException("I have no way to get data to show in my combo..");
	}


	/*--------------------------------------------------------------*/
	/*	CODING:	IInputNode<T> implementation.						*/
	/*--------------------------------------------------------------*/
	/**
	 * @see to.etc.domui.dom.html.IInputNode#getValueSafe()
	 */
	@Override
	public V getValueSafe() {
		return DomUtil.getValueSafe(this);
	}

	/**
	 * @see to.etc.domui.dom.html.IInputNode#hasError()
	 */
	@Override
	public boolean hasError() {
		getValueSafe();
		return super.hasError();
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	IBindable interface (EXPERIMENTAL)					*/
	/*--------------------------------------------------------------*/
	/** When this is bound this contains the binder instance handling the binding. */
	private SimpleBinder m_binder;

	/**
	 * Return the binder for this control.
	 * @see to.etc.domui.component.input.IBindable#bind()
	 */
	@Override
	public IBinder bind() {
		if(m_binder == null)
			m_binder = new SimpleBinder(this);
		return m_binder;
	}

	/**
	 * Returns T if this control is bound to some data value.
	 *
	 * @see to.etc.domui.component.input.IBindable#isBound()
	 */
	@Override
	public boolean isBound() {
		return m_binder != null && m_binder.isBound();
	}


	/*--------------------------------------------------------------*/
	/*	CODING:	Getters, setters and other boring crud.				*/
	/*--------------------------------------------------------------*/
	public INodeContentRenderer<T> getContentRenderer() {
		return m_contentRenderer;
	}

	public void setContentRenderer(INodeContentRenderer<T> contentRenderer) {
		m_contentRenderer = contentRenderer;
	}

	public Class< ? extends INodeContentRenderer<T>> getContentRendererClass() {
		return m_contentRendererClass;
	}

	public void setContentRendererClass(Class< ? extends INodeContentRenderer<T>> contentRendererClass) {
		m_contentRendererClass = contentRendererClass;
	}

	public PropertyMetaModel getPropertyMetaModel() {
		return m_propertyMetaModel;
	}

	public void setPropertyMetaModel(PropertyMetaModel propertyMetaModel) {
		m_propertyMetaModel = propertyMetaModel;
	}

	public IListMaker<T> getListMaker() {
		return m_listMaker;
	}

	public void setListMaker(IListMaker<T> listMaker) {
		m_listMaker = listMaker;
	}

	public IValueTransformer<V> getValueTransformer() {
		return m_valueTransformer;
	}

	public void setValueTransformer(IValueTransformer<V> valueTransformer) {
		m_valueTransformer = valueTransformer;
	}

	public String getEmptyText() {
		return m_emptyText;
	}

	public void setEmptyText(String emptyText) {
		m_emptyText = emptyText;
	}

	/**
	 * If this combobox has a "unselected" option currently this contains that option. When present it
	 * means that indexes in the <i>combo</i> list are one <i>higher</i> than indexes in the backing
	 * dataset (because this empty option is always choice# 0).
	 * @return
	 */
	protected SelectOption getEmptyOption() {
		return m_emptyOption;
	}

	/**
	 * See getter.
	 * @param emptyOption
	 */
	protected void setEmptyOption(SelectOption emptyOption) {
		m_emptyOption = emptyOption;
	}

	protected V internalGetCurrentValue() {
		return m_currentValue;
	}

	protected void internalSetCurrentValue(V val) {
		m_currentValue = val;
	}
}
