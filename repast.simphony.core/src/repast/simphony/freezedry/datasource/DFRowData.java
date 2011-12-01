package repast.simphony.freezedry.datasource;

import java.util.Arrays;

public class DFRowData {
	public DFRowData(String[] line, int i) {
		this.row = line;
		this.index = i;
	}

	public String[] row;
	
	public int index;
	
	public String nextString() {
		return row[index++];
	}
	
	@Override
	public String toString() {
		return (row == null ? "null" : Arrays.toString(row)) + ": " + index;
	}
}