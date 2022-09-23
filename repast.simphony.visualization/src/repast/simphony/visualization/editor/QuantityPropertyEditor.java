package repast.simphony.visualization.editor;

import javax.measure.Quantity;

import com.l2fprod.common.beans.editor.StringConverterPropertyEditor;

import repast.simphony.ui.probe.QuantityConverter;

/**
 * Adapts an AmountConverter into a PropertyEditor usable by the property sheet.
 * 
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class QuantityPropertyEditor extends StringConverterPropertyEditor {

	QuantityConverter converter = new QuantityConverter();

	@Override
	protected String convertToString(Object o) {
		if (o instanceof Quantity) {
			return converter.toString((Quantity) o);
		} else {
			return o.toString();
		}
	}

	protected Object convertFromString(String s) {
		return converter.fromString(s);
	}
}
