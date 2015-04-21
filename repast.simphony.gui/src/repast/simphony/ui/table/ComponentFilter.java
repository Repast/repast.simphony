package repast.simphony.ui.table;

import java.awt.Component;


/**
 * Row filter for Components
 * 
 * @author Eric Tatara
 *
 * @param <M>
 * @param <I>
 */
public class ComponentFilter<M,I> extends NamedRowFilter<M, I, String> {

	public ComponentFilter(String columnName, int colIndex, String filterValue,
			Operator operator) {
		super(columnName, colIndex, filterValue, operator);
	}

	@Override
	public boolean include(Entry<? extends M, ? extends I> entry) {

		if (entry.getValue(colIndex) == null) return false;
		
		Component comp = (Component)entry.getValue(colIndex);
		
		String value = comp.getName();
		
		if (value == null) return false;
		
		String filt = filterValue;
		
		return value.equals(filt);
	}
}