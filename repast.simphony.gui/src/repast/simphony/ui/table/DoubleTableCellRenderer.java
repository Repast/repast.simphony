package repast.simphony.ui.table;

import java.math.RoundingMode;
import java.text.NumberFormat;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * TableCell renderer for double values with variable precision.
 * 
 * @author Eric Tatara
 *
 */
public class DoubleTableCellRenderer extends DefaultTableCellRenderer {

	private NumberFormat nf;

	public DoubleTableCellRenderer(int p_precision) {
		super();
		setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		nf = NumberFormat.getNumberInstance();  
		nf.setMaximumFractionDigits(p_precision);
		nf.setRoundingMode(RoundingMode.HALF_UP);  
	}

	@Override
	public void setValue(Object value) {
		if ((value != null) && (value instanceof Number)) {
			Number num = (Number) value;
			value = nf.format(num.doubleValue());
		}
		super.setValue(value);
	}	

}
