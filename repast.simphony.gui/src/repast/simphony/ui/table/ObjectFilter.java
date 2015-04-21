package repast.simphony.ui.table;


/**
 * Row filter for objects other than string or numeric
 * 
 * @author Eric Tatara
 *
 * @param <M>
 * @param <I>
 */
public class ObjectFilter<M,I> extends NamedRowFilter<M, I> {

	public ObjectFilter(String columnName, int colIndex, String filterValue,
			Operator operator) {
		super(columnName, colIndex, filterValue, operator);
	}

	@Override
	public boolean include(Entry<? extends M, ? extends I> entry) {

		if (entry.getValue(colIndex) == null) return false;
		
		String value = entry.getValue(colIndex).toString();
		String filt = (String) filterValue;
		
		return value.equals(filt);
	}
}