/**
 * 
 */
package repast.simphony.systemdynamics.translator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import repast.simphony.systemdynamics.support.Utilities;

public class DumpWorkbook {
	
	BufferedWriter bw; //  = Utilities.openFileForWriting("dump_Excel_v13.csv");
	String workbookName; //  = "data/CoBAM_Buildings_v13.xlsx";
	XSSFWorkbook wb;
	
	public DumpWorkbook(String inFile, String outFile) {
	    this.workbookName = inFile;
	    bw = Utilities.openFileForWriting(outFile);
		
	}
	
	public static void main(String args[]) {
		DumpWorkbook dw = new DumpWorkbook(args[0], args[1]);
		dw.execute();
	}
	
	public void execute() {
		try {
			wb = new XSSFWorkbook(workbookName);
			bw.append("sheet,row,col,cellAddress,value,formula\n");
			for (int sheet = 0 ; sheet < wb.getNumberOfSheets(); sheet++) {
				dumpSheet(wb.getSheetName(sheet));
			}
//			dumpSheet("InputDesign");
//			dumpSheet("InputClimate");
//			dumpSheet("CALC");
//			dumpSheet("Calc2");
//			dumpSheet("Output");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dumpSheet(String sheet) {
		try {

			XSSFSheet aSheet = wb.getSheet(sheet);


			Iterator<Row> rowIt = aSheet.rowIterator();
			while (rowIt.hasNext()) {
				Row aRow = rowIt.next();
				Iterator<Cell> cellIt = aRow.cellIterator();
				while (cellIt.hasNext()) {
					Cell aCell = cellIt.next();
					bw.append(sheet+",");
					bw.append((aRow.getRowNum()+1)+","+(aCell.getColumnIndex()+1)+",");
					
					int rowNum = aRow.getRowNum()+1;
					int colNum = aCell.getColumnIndex()+1;


					bw.append(sheet+"_R"+(aRow.getRowNum()+1)+"C"+(aCell.getColumnIndex()+1)+",");
					if (aCell.getCellType() == Cell.CELL_TYPE_STRING ) {
						bw.append("\""+aCell.getRichStringCellValue().toString()+"\",");
						bw.append("Constant");

					} else if ( 
							aCell.getCellType() == Cell.CELL_TYPE_FORMULA && 
							aCell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
						bw.append("\""+aCell.getRichStringCellValue().toString()+"\",");
						String theFormula = aCell.getCellFormula();
						if (theFormula.startsWith("CELL"))
							theFormula = "CELL(address reference)";
						bw.append("\""+theFormula+"\"");
					} else if ( 
							aCell.getCellType() == Cell.CELL_TYPE_FORMULA && 
							aCell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
						bw.append("\""+aCell.getNumericCellValue()+"\",");
						String theFormula = "PROBLEM!";
							try {
								theFormula = aCell.getCellFormula();
							} catch (Exception e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
							}
							
						if (theFormula.startsWith("CELL"))
							theFormula = "CELL(address reference)";
						bw.append("\""+theFormula+"\"");
						
					} else {
						bw.append("\""+Double.toString(aCell.getNumericCellValue())+"\",");
						bw.append("Constant");
					}
					bw.append("\n");
					bw.flush();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}


