package repast.simphony.visualization.editor;

import org.jscience.physics.amount.Amount;

import repast.simphony.ui.probe.AmountConverter;

import com.l2fprod.common.beans.editor.StringConverterPropertyEditor;

/**
 * Adapts an AmountConverter into a PropertyEditor usable by the property sheet.
 * 
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class AmountPropertyEditor extends StringConverterPropertyEditor {

	AmountConverter converter = new AmountConverter();

	@Override
	protected String convertToString(Object o) {
		if (o instanceof Amount) {
			return converter.toString((Amount) o);
		} else {
			return o.toString();
		}
	}

	protected Object convertFromString(String s) {
		return converter.fromString(s);
	}
}
