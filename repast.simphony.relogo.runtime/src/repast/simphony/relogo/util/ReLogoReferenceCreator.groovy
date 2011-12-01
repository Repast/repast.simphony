package repast.simphony.relogo.util

import java.io.File;
import org.apache.poi.ss.util.CellReference 
import java.util.List; 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row 
import org.apache.poi.ss.usermodel.Workbook 
import org.apache.poi.ss.usermodel.WorkbookFactory 

import org.apache.poi.ss.usermodel.Sheet;

//import org.apache.poi.ss.usermodel.*; 

class ReLogoReferenceCreator {
	
	String filename = "data/ReLogoPrimitives.xls"
	List<SheetInfo> workbookSheetsInfo = []
	Workbook wb
	
	static main(args) {
		
		ReLogoReferenceCreator rrc = new ReLogoReferenceCreator()
		rrc.readInPrimitivesExcelFile()
		rrc.createPrimitivesDocument()
	}
	
	public void readInPrimitivesExcelFile(){
		wb = WorkbookFactory.create(new FileInputStream(filename))
		for (int k = 0; k < wb.getNumberOfSheets(); k++) {
			workbookSheetsInfo.add(getSheetInfo(k))
		}
	}
	
	public SheetInfo getSheetInfo(int index){
		Sheet sheet = wb.getSheetAt(index);
		def list = [];
		PrimitiveCategory currentCategory = null
		Iterator iter = sheet.rowIterator()
		for(Row row : sheet){
			Cell firstCell = row.getCell(0)
			Cell secondCell = row.getCell(1)
			if (firstCell != null && firstCell.getCellType().equals(Cell.CELL_TYPE_STRING)){
				String firstCellString = firstCell.getStringCellValue()
				if (secondCell == null || secondCell.getCellType().equals(Cell.CELL_TYPE_BLANK)){
					currentCategory = new PrimitiveCategory(firstCellString)
					list.add(currentCategory)
				}
				else {
					String secondCellString = secondCell.getStringCellValue()
					currentCategory.addMethodAndRef(firstCellString, secondCellString)
				}
			}
		}
		return new SheetInfo(sheet.getSheetName(), list)
	}
	
	def createPrimitivesDocument(){
		def writer = new FileWriter('docs/ReLogoDocs/ReLogoPrimitives.html')
		def html	= new groovy.xml.MarkupBuilder(writer) 
		html.html {
			head { title 'ReLogo Primitives' }
			body {
				h1 'ReLogo Primitives'
				StringBuffer sb = new StringBuffer() 
				for (SheetInfo si in workbookSheetsInfo) {
					String name = si.getSheetName();
					font(size:4) {
						a(href:"#$name",name)
						mkp.yieldUnescaped('&nbsp;&nbsp;')
					}
					
				}
				hr()
				for (SheetInfo si in workbookSheetsInfo) {
					String name = si.getSheetName();
					A (NAME: name)
					h2(name)
					
					table (border:1){
						tr {
							for (PrimitiveCategory prim : si.getPrimitiveCategories()){
								th {
									font(size:4, prim.getName())
								}
							}
						}
						tr {
							for (PrimitiveCategory prim : si.getPrimitiveCategories()){
								td(valign:"top"){
									
									for (String method : prim.getMethods() ){

										a (href:prim.getRefForMethod(method), method)

										br()
									}
									
								}
							}
						}
					}
				}
				
			}
		}
	}
}
