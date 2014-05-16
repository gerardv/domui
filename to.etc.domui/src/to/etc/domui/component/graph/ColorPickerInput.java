package to.etc.domui.component.graph;

import javax.annotation.*;

import to.etc.domui.component.input.*;
import to.etc.domui.dom.header.*;
import to.etc.domui.dom.html.*;

/**
 * An input button to enter a color code, with a small div behind it showing the
 * currently selected code's color.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on May 1, 2011
 */
public class ColorPickerInput extends Input implements IControl<String> {
	final private Div		m_coldiv = new Div();

	private boolean m_mandatory = true;

	@Override
	public void createContent() throws Exception {
		setMaxLength(6);
		setSize(6);
		m_coldiv.setCssClass("ui-cpin-div");
//		setCssClass("ui-cpbt-btn");
//		add(m_hidden);
//		add(m_coldiv);
//		if(m_hidden.getRawValue() == null)
//			m_hidden.setRawValue("ffffff");
//		m_coldiv.setBackgroundColor("#" + m_hidden.getRawValue());
//		appendCreateJS("$('#"+getActualID()+"').ColorPicker({});");
		appendAfterMe(m_coldiv);
		if(!isOff())
			appendCreateJS("WebUI.colorPickerInput('#" + getActualID() +"','#"+m_coldiv.getActualID() + "','" + getRawValue() + "'," + Boolean.valueOf(getOnValueChanged() != null) + ");");
	}

	private boolean isOff() {
		return isDisabled() || isReadOnly();
	}

	@Override
	public void onAddedToPage(Page p) {
		p.addHeaderContributor(HeaderContributor.loadJavascript("$js/colorpicker.js"), 100);
//		if(m_coldiv.getPage() == null)
//			appendAfterMe(m_coldiv);
	}
	@Override
	public void onRemoveFromPage(Page p) {
		m_coldiv.remove();
	}

	@Override
	public void setValue(@Nullable String value) {
		if(value == null)
			value = "000000"; // We do not allow null here.
		if(value.startsWith("#"))
			value = value.substring(1); // Remove any #
		setRawValue(value); // Set the color value;
		m_coldiv.setBackgroundColor("#" + value);
		if(!isBuilt())
			return;

		//-- Force update existing value.
		if(!isOff())
			appendJavascript("$('#" + getActualID() + "').ColorPickerSetColor('" + value + "');");
	}
	@Override
	public String	getValue() {
		String v = getRawValue();
		if(v == null || v.length() == 0)
			v = "000000";
		return v;
	}

	/*--------------------------------------------------------------*/
	/*	CODING:	IBindable interface (EXPERIMENTAL)					*/
	/*--------------------------------------------------------------*/
	/** When this is bound this contains the binder instance handling the binding. */
	@Nullable
	private SimpleBinder m_binder;

	/**
	 * Return the binder for this control.
	 * @see to.etc.domui.component.input.IBindable#bind()
	 */
	@Override
	@Nonnull
	public IBinder bind() {
		SimpleBinder binder = m_binder;
		if(binder == null)
			binder = m_binder = new SimpleBinder(this);
		return binder;
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

	@Override
	public String getValueSafe() {
		return getValue();
	}
	@Override
	public boolean isMandatory() {
		return m_mandatory;
	}

	@Override
	public void setMandatory(boolean ro) {
		m_mandatory = ro;
	}
	@Override
	public void setDisabled(boolean disabled) {
		boolean wasoff = isOff();
		super.setDisabled(disabled);
		update(wasoff);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		boolean wasoff = isOff();
		super.setReadOnly(readOnly);
		update(wasoff);
	}

	private void update(boolean old) {
		if(isOff() == old)
			return;
		if(isOff())
			appendJavascript("WebUI.colorPickerDisable('#"+getActualID()+"');");
		else
			appendCreateJS("WebUI.colorPickerInput('#" + getActualID() +"','#"+m_coldiv.getActualID() + "','" + getRawValue() + "'," + Boolean.valueOf(getOnValueChanged() != null) + ");");
	}
}