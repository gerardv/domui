package to.etc.domui.component2.form4;

import to.etc.domui.component.binding.IBindingConverter;
import to.etc.domui.component.meta.MetaManager;
import to.etc.domui.component.meta.PropertyMetaModel;
import to.etc.domui.component2.controlfactory.ControlCreatorRegistry;
import to.etc.domui.dom.html.IControl;
import to.etc.domui.dom.html.Label;
import to.etc.domui.dom.html.NodeBase;
import to.etc.domui.dom.html.NodeContainer;
import to.etc.domui.dom.html.TBody;
import to.etc.domui.dom.html.TD;
import to.etc.domui.dom.html.TR;
import to.etc.domui.dom.html.Table;
import to.etc.domui.server.DomApplication;
import to.etc.webapp.annotations.GProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Yet another attempt at a generic form builder, using the Builder pattern. The builder
 * starts in vertical mode - call horizontal() to move horizontally.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on Jun 17, 2014
 */
final public class FormBuilder {
	/**
	 * Handle adding nodes generated by the form builder to the page.
	 *
	 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
	 * Created on Jun 13, 2012
	 */
	interface IAppender {
		void add(@Nonnull NodeBase formNode);
	}

	@Nonnull
	final private IAppender m_appender;

	private boolean m_horizontal;

	private boolean m_currentDirection;

	private String m_nextLabel;

	private String m_errorLocation;

	private Label m_nextLabelControl;

	private PropertyMetaModel< ? > m_propertyMetaModel;

	private Object m_instance;

	private Boolean m_mandatory;

	private boolean m_append;

	private Boolean m_readOnly;

	private NodeBase m_lastAddedControl;

	@Nullable
	private String m_controlCss;

	@Nullable
	private String m_labelCss;

	@Nullable
	private IBindingConverter<?, ?> m_bindingConverter;

	public FormBuilder(@Nonnull IAppender appender) {
		m_appender = appender;
	}

	public FormBuilder(@Nonnull final NodeContainer nb) {
		this(nb::add);
	}

	@Nonnull
	public FormBuilder append() {
		m_append = true;
		return this;
	}


	@Nonnull
	public FormBuilder horizontal() {
		m_horizontal = true;
		return this;
	}

	@Nonnull
	public FormBuilder vertical() {
		m_horizontal = false;
		return this;
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	Label control.										*/
	/*--------------------------------------------------------------*/
	/**
	 *
	 * @param label
	 * @return
	 */
	@Nonnull
	public FormBuilder label(@Nonnull String label) {
		if(null != m_nextLabelControl)
			throw new IllegalStateException("You already set a Label instance");
		m_nextLabel = label;
		return this;
	}

	@Nonnull
	public FormBuilder label(@Nonnull Label label) {
		if(null != m_nextLabel)
			throw new IllegalStateException("You already set a String label instance");
		m_nextLabelControl = label;
		return this;
	}

	@Nonnull
	public FormBuilder errorLocation(@Nonnull String errorLocation) {
		m_errorLocation = errorLocation;
		return this;
	}


	@Nonnull
	public FormBuilder unlabeled() {
		label("");
		return this;
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	Readonly, mandatory, disabled.						*/
	/*--------------------------------------------------------------*/
	/**
	 *
	 * @return
	 */
	@Nonnull
	public FormBuilder readOnly() {
		m_readOnly = Boolean.TRUE;
		return this;
	}

	@Nonnull
	public FormBuilder readOnly(boolean ro) {
		m_readOnly = Boolean.valueOf(ro);
		return this;
	}

	@Nonnull
	public FormBuilder mandatory() {
		m_mandatory = Boolean.TRUE;
		return this;
	}

	@Nonnull
	public FormBuilder	mandatory(boolean yes) {
		m_mandatory = Boolean.valueOf(yes);
		return this;
	}

	/*--------------------------------------------------------------*/
	/*	CODING: defining (manually created) controls.				*/
	/*--------------------------------------------------------------*/
	/**
	 * Add the specified control. Since the control is manually created this code assumes that the
	 * control is <b>properly configured</b> for it's task! This means that this code will not
	 * make any changes to the control! Specifically: if the form item is marked as "mandatory"
	 * but the control here is not then the control stays optional.
	 * The reverse however is not true: if the control passed in is marked as mandatory then the
	 * form item will be marked as such too.
	 *
	 * @param control
	 * @throws Exception
	 */
	public void control(@Nonnull IControl< ? > control) throws Exception {
		if(control.isMandatory()) {
			m_mandatory = Boolean.TRUE;
		}
		addControl((NodeBase) control);
		resetBuilder();
	}

	@Nonnull
	public IControl< ? > control() throws Exception {
		return controlMain(null);
	}

	@Nonnull
	public <T, C extends IControl<T>> C control(@Nullable Class<C> controlClass) throws Exception {
		return controlMain(controlClass);
	}

	@Nonnull
	private  <T, C extends IControl<T>> C controlMain(@Nullable Class<C> controlClass) throws Exception {
		ControlCreatorRegistry builder = DomApplication.get().getControlCreatorRegistry();
		PropertyMetaModel<T> pmm = (PropertyMetaModel<T>) m_propertyMetaModel;
		if(null == pmm)
			throw new IllegalStateException("You must have called 'property(...)' before");
		C control = builder.createControl(pmm, controlClass);
		bindControlData(control, pmm);
		addControl((NodeBase) control);
		resetBuilder();
		return control;
	}

	@Nonnull
	public FormBuilder converter(@Nonnull IBindingConverter<?, ?> converter) {
		m_bindingConverter = converter;
		return this;
	}

	/**
	 * Adds the specified css class to the control cell.
	 * @param cssClass
	 * @return
	 */
	@Nonnull
	public FormBuilder cssControl(@Nonnull String cssClass) {
		m_controlCss = cssClass;
		return this;
	}

	/**
	 * Adds the specified css class to the label cell.
	 * @param cssClass
	 * @return
	 */
	@Nonnull
	public FormBuilder cssLabel(@Nonnull String cssClass) {
		m_labelCss = cssClass;
		return this;
	}

	public void item(@Nonnull NodeBase item) throws Exception {
		addControl(item);
		resetBuilder();
	}

	public <T, C extends IControl<T>> void bindControlData(@Nonnull C control, @Nonnull PropertyMetaModel<T> pmm) throws Exception {


	}


	@Nonnull
	public <T> FormBuilder property(@Nonnull T instance, @GProperty String property) {
		if(null != m_propertyMetaModel)
			throw new IllegalStateException("You need to end the builder pattern with a call to 'control()'");
		m_propertyMetaModel = MetaManager.getPropertyMeta(instance.getClass(), property);
		m_instance = instance;
		return this;
	}

	private void resetBuilder() {
		m_readOnly = null;
		m_instance = null;
		m_propertyMetaModel = null;
		m_append = false;
		m_mandatory = null;
		m_nextLabel = null;
		m_nextLabelControl = null;
		m_controlCss = null;
		m_labelCss = null;
		m_errorLocation = null;
		m_bindingConverter = null;
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	Form building code.									*/
	/*--------------------------------------------------------------*/

	private Table m_table;

	private TBody m_body;

	private TR m_labelRow;

	private TR m_controlRow;

	private void addControl(@Nonnull NodeBase control) throws Exception {
		if (control.getClass().getSimpleName().contains("TextArea")
			&& m_labelCss == null) {
			m_labelCss = "ui-f4-ta";
		}

		resetDirection();
		if(m_horizontal)
			addHorizontal(control);
		else
			addVertical(control);

		if(control instanceof IControl) {
			IControl< ? > ctl = (IControl< ? >) control;
			PropertyMetaModel< ? > pmm = m_propertyMetaModel;
			if(null != pmm) {
				Object instance = m_instance;
				if(null != instance) {
					((NodeBase) ctl).bind().convert(m_bindingConverter).to(instance, pmm);
				}
			}

			if(isReadOnly()) {
				ctl.setReadOnly(true);
			}

			if(isMandatory()) {
				ctl.setMandatory(true);
			}
		}

		String label = labelTextCalculated();
		m_lastAddedControl =  control;
		if (null != m_errorLocation){
			control.setErrorLocation(m_errorLocation);
		} else {
			if(null != label) {
				control.setErrorLocation(label);
			}
		}
		if(null != label)
			control.setCalculcatedId(label.toLowerCase());
	}

	private void resetDirection() {
		if(m_horizontal == m_currentDirection)
			return;
		clearTable();
		m_currentDirection = m_horizontal;
	}

	public FormBuilder nl() {
		clearTable();
		return this;
	}

	private void clearTable() {
		m_table = null;
		m_body = null;
		m_labelRow = null;
		m_controlRow = null;
	}

	@Nonnull
	public TBody body() {
		if(m_body == null) {
			Table tbl = m_table = new Table();
			m_appender.add(tbl);
			tbl.setCssClass(m_horizontal ? "ui-f4 ui-f4-h" : "ui-f4 ui-f4-v");
			tbl.setCellPadding("0");
			tbl.setCellSpacing("0");
			TBody b = m_body = new TBody();
			tbl.add(b);
			return b;
		}
		return m_body;
	}

	private void addVertical(NodeBase control) {
		TBody b = body();
		Label lbl = determineLabel();
		if(m_append) {
			TD cell = b.cell();
			if(lbl != null) {
				lbl.addCssClass("ui-f4-lbl");
				lbl.setMarginLeft("10px");
				lbl.setMarginRight("3px");
				cell.add(lbl);
			}
			cell.add(control);
			final String controlCss = m_controlCss;
			if(null != controlCss)
				cell.addCssClass(controlCss);
		} else {
			TD labelcell = b.addRowAndCell();
			labelcell.setCssClass("ui-f4-lbl ui-f4-lbl-v");
			if(null != lbl)
				labelcell.add(lbl);
			String labelCss = m_labelCss;
			if(labelCss != null)
				labelcell.addCssClass(labelCss);

			TD controlcell = b.addCell();
			controlcell.setCssClass("ui-f4-ctl ui-f4-ctl-v");
			controlcell.add(control);

			final String controlCss = m_controlCss;
			if(null != controlCss)
				controlcell.addCssClass(controlCss);
		}
		if(null != lbl)
			lbl.setForNode(control);
	}

	@Nonnull
	private TR controlRow() {
		TR row = m_controlRow;
		if(null == row) {
			labelRow();
			row = m_controlRow = body().addRow();
		}
		return row;
	}

	@Nonnull
	private TR labelRow() {
		TR row = m_labelRow;
		if(null == row) {
			row = m_labelRow = body().addRow();
		}
		return row;
	}

	private void addHorizontal(NodeBase control) {
		TBody b = body();
		Label lbl = determineLabel();
		if(m_append) {

			TR row = controlRow();
			TD cell;
			if(row.getChildCount() == 0) {
				cell = row.addCell();
				cell.setCssClass("ui-f4-ctl ui-f4-ctl-h");
			} else {
				cell = (TD) row.getChild(row.getChildCount() - 1);
			}
			cell.add(control);

			final String controlCss = m_controlCss;
			if(null != controlCss)
				cell.addCssClass(controlCss);
		} else {
			TD labelcell = labelRow().addCell();
			labelcell.setCssClass("ui-f4-lbl ui-f4-lbl-h");
			if(null != lbl)
				labelcell.add(lbl);

			String labelCss = m_labelCss;
			if(labelCss != null)
				labelcell.addCssClass(labelCss);

			TD controlcell = controlRow().addCell();
			controlcell.setCssClass("ui-f4-ctl ui-f4-ctl-h");
			controlcell.add(control);

			final String controlCss = m_controlCss;
			if(null != controlCss)
				controlcell.addCssClass(controlCss);
		}
		if(null != lbl)
			lbl.setForNode(control);
	}

	public void appendAfterControl(@Nonnull NodeBase what) {
		getLastControlCell().add(what);
	}

	@Nonnull
	public NodeContainer getLastControlCell() {
		if (m_lastAddedControl == null) {
			throw new IllegalStateException("No controls were added yet.");
		}
		return m_lastAddedControl.getParent(TD.class);
	}

	/**
	 *
	 * @return
	 */
	@Nullable
	private Label determineLabel() {
		Label res = null;
		String txt = m_nextLabel;
		if(null != txt) {
			//m_nextLabel = null;
			if(txt.length() != 0)					// Not "unlabeled"?
				res = new Label(txt);
		} else {
			res = m_nextLabelControl;
			if(res == null) {
				//-- Property known?
				PropertyMetaModel< ? > pmm = m_propertyMetaModel;
				if(null != pmm) {
					txt = pmm.getDefaultLabel();
					if(txt != null && txt.length() > 0)
						res = new Label(txt);
				}
			}
		}
		if(res != null && calculateMandatory() && !isReadOnly()) {
			res.addCssClass("ui-f4-mandatory");
		}

		return res;
	}

	@Nullable
	private String labelTextCalculated() {
		String txt = m_nextLabel;
		if(null != txt) {
			if(txt.length() != 0)					// Not "unlabeled"?
				return txt;
			return null;
		} else {
			Label res = m_nextLabelControl;
			if(res != null) {
				return res.getTextContents();
			} else {
				//-- Property known?
				PropertyMetaModel< ? > pmm = m_propertyMetaModel;
				if(null != pmm) {
					txt = pmm.getDefaultLabel();
					if(txt != null && txt.length() > 0)
						return txt;
				}
			}
		}
		return null;
	}

	private boolean isReadOnly() {

		Boolean ro = m_readOnly;
		if(null != ro) {
			return ro.booleanValue();
		}
		return false;
	}

	private boolean isMandatory() {

		Boolean man = m_mandatory;
		if(null != man) {
			return man.booleanValue();
		}
		return false;
	}

	private boolean calculateMandatory() {
		Boolean m = m_mandatory;
		if(null != m)
			return m.booleanValue();						// If explicitly set: obey that
		PropertyMetaModel<?> pmm = m_propertyMetaModel;
		if(null != pmm) {
			return pmm.isRequired();
		}
		return false;
	}


}
