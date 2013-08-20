package repast.simphony.systemdynamics.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cern.jet.random.Beta;
import cern.jet.random.Binomial;
import cern.jet.random.Exponential;
import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

public class SDFunctionsWithXLSColt extends SDFunctions {
    
    private Map<String, XSSFWorkbook> workbooks = new HashMap<String, XSSFWorkbook>();
    
    private Uniform uniformDistribution = null;
    private Beta betaDistribution = null;
    private Binomial binomialDistribution = null;
    private Exponential exponentialDistribution = null;
    private Gamma gammaDistribution = null;
    private Normal normalDistribution = null;
    private Poisson poissonDistribution = null;

    

    public SDFunctionsWithXLSColt(SDModel model) {
	super(model);
	// TODO Auto-generated constructor stub
    }
    
    private XSSFWorkbook getWorkbook(String name) {
	try {
	    if (!workbooks.containsKey(name))
	        workbooks.put(name, new XSSFWorkbook(name));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return workbooks.get(name);
    }
    
    public double GETXLSCONSTANTS(String book,String tab, String cell) {
	XSSFWorkbook wb = getWorkbook(book);
	XSSFSheet sheet = wb.getSheet(tab);

	return getNumericDataInCell(sheet, cell);
    }
    
    public double[] GETXLSCONSTANTS(String book,String tab, String cell, int numRows, int numColumns) {
	double[] fromXls = new double[numRows*numColumns];
	int startRow =  getRowNumberFromCellAddress(cell);
	int startCol =  getColumnNumberFromCellAddress(cell);
	XSSFWorkbook wb = getWorkbook(book);
	XSSFSheet sheet = wb.getSheet(tab);
	int pos = 0;
	for (int r = startRow; r < startRow + numRows; r++) {
	    for (int c = startCol; c < startCol + numColumns; c++) {
		fromXls[pos++] = getNumericDataInCell(sheet, r, c);
	    }
	}

	return fromXls;

}
    
    public double GETXLSCONSTANTS(String varName, double currentValue, double time, double timeStep, String book,String tab,String cell) {
	    XSSFWorkbook wb = getWorkbook(book);
	    XSSFSheet sheet = wb.getSheet(tab);
	    return getNumericDataInCell(sheet, cell);
	    
    }
    
    private double getNumericDataInCell(XSSFSheet sheet, int row, int col) {
	Iterator<Row> rowIt = sheet.rowIterator();
	int currentRow = 0;
	int currentColumn = 0;
	Row aRow = null;
	Cell aCell = null;
	while (rowIt.hasNext()) {
		aRow = rowIt.next();
		currentRow = aRow.getRowNum()+1;
		if (currentRow == row)
		    break;
	}
	if (currentRow < row)
	    return -1.0;
	Iterator<Cell> cellIt = aRow.cellIterator();
	while (cellIt.hasNext()) {
		aCell = cellIt.next();
		currentColumn = aCell.getColumnIndex()+1;
		if (currentColumn == col)
		    break;
	}
	
	if (currentColumn < col)
	    return -1.0;
	
	try {
	    
	    int t = aCell.getCellType();
//	    System.out.println("Type "+t);
	    if (t == 1) {
		String s = aCell.getStringCellValue();
//		 System.out.println("Value "+s);
		return -1.0;
		//String
	    } else {
	    
		double val = aCell.getNumericCellValue();
		return val;
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    return -1.0;
	}
	
    }
    
    private double getNumericDataInCell(XSSFSheet sheet, String cell) {
	return getNumericDataInCell(sheet, getRowNumberFromCellAddress(cell), getColumnNumberFromCellAddress(cell));
    }
    
    private double[] getTimeValues(XSSFSheet sheet, String timeRowOrCol, int startRow, int startCol) {
	     List<Double> timeList = new ArrayList<Double>();
	     double[] timeArray = null;
	     
	     boolean byRow = isInteger(timeRowOrCol);
	     int row;
	     int col;
	     double value;
	     
	     if (byRow) {
		 row = Integer.parseInt(timeRowOrCol);
		 col = startCol;
		 while((value = getNumericDataInCell(sheet, row, col++)) >= 0.0) {
		     timeList.add(value);
		 }
	     } else {
		 row = startRow;
		 col = Utilities.convertColumnToNumber(timeRowOrCol);
		 while((value = getNumericDataInCell(sheet, row++, col)) >= 0.0) {
		     timeList.add(value);
		 }
	     }
	     
	     timeArray = new double[timeList.size()];
	     int pos = 0;
	     for (double d : timeList)
		 timeArray[pos++] = d;
	     return timeArray;
	 }
	    
	    public double GETXLSDATA(String varName, double currentValue, double time, double timeStep, String book,String tab,String timeRowOrCol,String cell) {
		
		// this needs to return
		
		    notImplemented();
		    return -999.0;
	}
	    
	    public TimeSeriesInstance GETXLSLOOKUPS(String book, String tab, String timeRowOrCol, String cell, int numRows, int numColumns) {
		return GETXLSDATA(book, tab, timeRowOrCol, cell, numRows, numColumns);
	    }
	    
	    public TimeSeriesInstance GETXLSDATA(String book, String tab, String timeRowOrCol, String cell, int numRows, int numColumns) {

		     TimeSeriesInstance tsi = null;

		     XSSFWorkbook wb = getWorkbook(book);
		     XSSFSheet sheet = wb.getSheet(tab);

		     // for now, assume there is only a single subscript being applied
		     // numCols in this case is the number of colums or rows that contain data points
		     // still need to determine the number of time values in the series

		     boolean byRow = isInteger(timeRowOrCol);

		     int startRow =  getRowNumberFromCellAddress(cell);
		     int startCol =  getColumnNumberFromCellAddress(cell);

		     double[] timeValues = getTimeValues(sheet, timeRowOrCol, startRow, startCol);

		     double[] fromXls = new double[numRows*numColumns*timeValues.length];

		     int pos = 0;
		     if (byRow) {
			 for (int r = startRow; r < startRow + numRows; r++) {
			     for (int c = startCol; c < startCol + timeValues.length; c++) {
				 fromXls[pos++] = getNumericDataInCell(sheet, r, c);
			     }
			 }
		     } else {
			 for (int c = startCol; c < startCol + numColumns; c++) {
			     for (int r = startRow; r < startRow + timeValues.length; r++) {

				 fromXls[pos++] = getNumericDataInCell(sheet, r, c);
			     }
			 }
		     }
		     
		     tsi = new TimeSeriesInstance(timeValues, fromXls);

		     return tsi;
		}
	    
	    public double RANDOMUNIFORM(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2, double arg3) {
		if (uniformDistribution == null) {
		    uniformDistribution = new Uniform(arg1, arg2, (int) arg3);
		}
		return uniformDistribution.nextDouble();
	    }

}
