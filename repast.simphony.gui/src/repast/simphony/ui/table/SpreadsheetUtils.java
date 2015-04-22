package repast.simphony.ui.table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility to convert TableModels to spreadsheet files via POI 
 * 
 * @author Eric Tatara
 *
 */
public class SpreadsheetUtils {

	/**
	 * Creates an Excel workbook from a single TableModel
	 * 
	 * @param model
	 * @param sheetName
	 * @param file the name of the save file
	 */
	public static void saveSingleTableAsExcel(TableModel model, String sheetName, File file){
		
		Map<String,TableModel> models = new HashMap<String,TableModel>();
		models.put(sheetName, model);
		
		saveTablesAsExcel(models, file);
	}
	
	/**
	 * Creates an Excel workbook from a list of Table models, with each TableModel
	 *   inserted as a seprate sheet.
	 *   
	 * @param models a list of TableModel 
	 * @param file the name of the save file
	 */
	public static void saveTablesAsExcel(Map<String,TableModel> models, File file){

		XSSFWorkbook wb = new XSSFWorkbook(); 

		for (String sheetName : models.keySet()){
			createSheet(wb, models.get(sheetName), sheetName);
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a Sheet in the workbook using data from the TableModel
	 * 
	 * @param wb
	 * @param model
	 * @param sheetName
	 */
	private static void createSheet(XSSFWorkbook wb, TableModel model, String sheetName){
		Sheet sheet = wb.createSheet(sheetName);    

		Row headerRow = sheet.createRow(0);
		headerRow.setHeightInPoints(12.75f);

		XSSFFont boldFont = wb.createFont();
		boldFont.setFontHeightInPoints((short)22);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle headerStyle = wb.createCellStyle();

		// Create the header cells
		int numColumns = model.getColumnCount();
		for (int col=0; col<numColumns; col++) {	
			Cell cell = headerRow.createCell(col);
			cell.setCellValue(model.getColumnName(col));
			cell.setCellStyle(headerStyle);
		}

		// Set the cell values
		int numRows = model.getRowCount();
		for (int row=0; row<numRows; row++){	
			Row sheetRow = sheet.createRow(row+1);	// account for header row (0)

			for (int col=0; col<numColumns; col++) {
				Cell cell = sheetRow.createCell(col);
				Object val = model.getValueAt(row, col);

				if (val == null) continue;
				
				if (val instanceof Number){
					cell.setCellValue(((Number)val).doubleValue());	
				}
				else if (val instanceof Boolean){
					cell.setCellValue((boolean)val);	
				}
				else if (val instanceof String){
					cell.setCellValue((String)val);	
				}
				else if (val instanceof Date){
					cell.setCellValue((Date)val);
				}
				else if (val instanceof JComponent){
					// don't output any GUI controls in the cell
				}
				else{
					cell.setCellValue(val.toString());
				}
			}
		}
	}
}