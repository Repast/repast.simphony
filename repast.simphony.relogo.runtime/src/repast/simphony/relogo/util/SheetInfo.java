package repast.simphony.relogo.util;

import java.util.List;

public class SheetInfo {
	String sheetName;
	List<PrimitiveCategory> primitiveCategories;
	
	public SheetInfo(String sheetName, List<PrimitiveCategory> list){
		this.sheetName = sheetName;
		this.primitiveCategories = list;
	}

	public String getSheetName() {
		return sheetName;
	}

	public List<PrimitiveCategory> getPrimitiveCategories() {
		return primitiveCategories;
	}

}
