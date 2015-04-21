package repast.simphony.ui.table;

import javax.swing.RowFilter;

/**
 * Abstract row filter for table filtering. 
 * 
 * @author Eric Tatara
 *
 * @param <M>
 * @param <I>
 */
public abstract class NamedRowFilter<M,I> extends RowFilter<M,I> {

	public static enum Operator{
		LESS_THAN("<"), 
		GREATER_THAN(">"), 
		EQUALS("="), 
		LESS_THAN_OR_EQUALS("<="), 
		GREATER_THAN_OR_EQUALS(">="), 
		NOT_EQUAL("!=");

		private String symbol;	

		private Operator(String symbol){
			this.symbol = symbol;
		}
		
		@Override
		public String toString(){
			return symbol;
		}
	}
	
	protected String name;
	protected int colIndex;
	protected Object filterValue;
  protected Operator operator;
	
	public NamedRowFilter(String columnName, int colIndex, Object filterValue, 
			Operator operator) {
		this.colIndex = colIndex;
		this.filterValue = filterValue;
		this.operator = operator;
		
		name = columnName + " " + operator + " " + filterValue;
	}
	
	@Override
	public abstract boolean include(Entry<? extends M, ? extends I> entry);
	
	@Override
	public String toString(){
		return name;
	}
}
