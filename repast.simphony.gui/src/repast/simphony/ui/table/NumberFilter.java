package repast.simphony.ui.table;

import repast.simphony.ui.table.NamedRowFilter.Operator;

/**
 * Row filter for numeric values;
 * 
 * @author Eric Tatara
 *
 * @param <M>
 * @param <I>
 */
public class NumberFilter<M,I> extends NamedRowFilter<M, I> {

	public NumberFilter(String columnName, int colIndex, Number filterValue,
			Operator operator) {
		super(columnName, colIndex, filterValue, operator);
	}

	@Override
	public boolean include(Entry<? extends M, ? extends I> entry) {

		if (entry.getValue(colIndex) == null) return false;
		
		Number value = (Number)entry.getValue(colIndex);
		Number filt = (Number) filterValue;
		
		switch(operator){
			case LESS_THAN: return (value.doubleValue() < filt.doubleValue());

			case LESS_THAN_OR_EQUALS: return (value.doubleValue() <= filt.doubleValue());
			
			case GREATER_THAN: return (value.doubleValue() > filt.doubleValue());
			
			case GREATER_THAN_OR_EQUALS: return (value.doubleValue() >= filt.doubleValue());
			
			case EQUALS: return (value.doubleValue() == filt.doubleValue());
			
			case NOT_EQUAL: return (value.doubleValue() != filt.doubleValue());
			
			default: return false;
		}
	}
}