package repast.simphony.ui.table;

/**
 * Row filter for string values.
 * 
 * @author Eric Tatara
 *
 * @param <M>
 * @param <I>
 */
public class StringFilter<M,I> extends NamedRowFilter<M, I, String> {

	public StringFilter(String columnName, int colIndex, String filterValue, 
			Operator operator) {
		super(columnName, colIndex, filterValue, operator);
	}

	@Override
	public boolean include(Entry<? extends M, ? extends I> entry) {

		if (entry.getValue(colIndex) == null) return false;
		
		String value = (String)entry.getValue(colIndex);
		String filt = filterValue;
		
		return value.equals(filt);
	}
}