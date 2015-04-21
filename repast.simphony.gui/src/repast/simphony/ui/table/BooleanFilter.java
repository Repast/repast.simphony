package repast.simphony.ui.table;


/**
 * Row filter for boolean values
 * 
 * @author Eric Tatara
 *
 * @param <M>
 * @param <I>
 */
public class BooleanFilter<M,I> extends NamedRowFilter<M, I, Boolean> {

	public BooleanFilter(String columnName, int colIndex, Boolean filterValue,
			Operator operator) {
		super(columnName, colIndex, filterValue, operator);
	}

	@Override
	public boolean include(Entry<? extends M, ? extends I> entry) {

		if (entry.getValue(colIndex) == null) return false;
		
		Boolean value = (Boolean)entry.getValue(colIndex);
		Boolean filt = filterValue;
		
		return value.equals(filt);
	}
}