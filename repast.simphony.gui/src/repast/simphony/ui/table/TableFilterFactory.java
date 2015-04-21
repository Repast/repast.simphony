package repast.simphony.ui.table;

import java.awt.Component;


public class TableFilterFactory<M,I> {

	public NamedRowFilter<M,I,?> createFilter(String name, int col, 
			Class<?>colClass, String filterVal, NamedRowFilter.Operator op){

		if (String.class.isAssignableFrom(colClass)){
			return new StringFilter<M,I>(name, col, filterVal, op);
		}
		
		else if (Number.class.isAssignableFrom(colClass)){
			return new NumberFilter<M,I>(name, col, Double.valueOf(filterVal), op);
		}
		
		else if (Boolean.class.isAssignableFrom(colClass)){
			return new BooleanFilter<M,I>(name, col, Boolean.valueOf(filterVal), op);
		}
		
		else if (Component.class.isAssignableFrom(colClass)){
			return new ComponentFilter<M,I>(name, col, filterVal, op);
		}
		
		return new ObjectFilter<M,I>(name, col, filterVal, op);
	}

}
