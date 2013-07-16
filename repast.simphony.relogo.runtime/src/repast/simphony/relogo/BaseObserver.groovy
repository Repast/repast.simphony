/**
 * 
 */
package repast.simphony.relogo

import static java.awt.Color.*
import static java.lang.Math.*
import static repast.simphony.essentials.RepastEssentials.*
import static repast.simphony.relogo.Utility.*

import java.beans.BeanInfo
import java.beans.Introspector
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field
import java.lang.reflect.Modifier

import org.apache.commons.lang3.math.NumberUtils
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory;

import repast.simphony.relogo.factories.*
import repast.simphony.ui.probe.ProbeID
import repast.simphony.util.*
import au.com.bytecode.opencsv.CSVReader


/**
 * @author jozik
 * @author Michael J. North
 */
public class BaseObserver extends AbstractObserver{

	/**
	 * 
	 * This value is used to automatically generate agent identifiers.
	 * 
	 * @field serialVersionUID
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	/**
	 * 
	 * This method provides a human-readable name for the agent.
	 * 
	 * @method toString
	 * 
	 */
	@ProbeID()
	public String toString() {
		return super.toString();
	}



	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void watch(Object watched){
	}

	/**
	 * Creates default turtles from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path to the CSV file
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromCSV(String fileName, Closure initClosure = null) {
		Class<? extends Turtle> turtleType = getTurtleFactory().getTurtleTypeClass("default");
		return createTurtlesFromCSV(fileName,turtleType,initClosure)
	}

	/**
	 * Creates turtles of specific type from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 * 
	 * @param fileName the path to the CSV file
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 * 
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromCSV(String fileName, Class<E> turtleType, Closure initClosure = null) {
		AgentSet<E> result = new AgentSet<>();
		// Read the data file.
		List<String[]> rows = new CSVReader(
				new InputStreamReader(new FileInputStream(fileName)))
				.readAll()

		// Define the fields lists.
		List fullFieldList
		List matchedFieldList

		// Create the agents.
		for (row in rows) {

			// Check the fields list.
			if (fullFieldList == null) {

				// Fill in the field lists.
				fullFieldList = (List) row
				List<String> fields = getPublicFieldsAndProperties(turtleType)
				matchedFieldList = fields.intersect((List) row)
			} else {

				// Define an index tracker.
				int index

				// Create the next agent.
				AgentSet turtleAgentSet = createTurtles(1, {

					// Assign properties from the file.
					for (field in matchedFieldList) {
						index = fullFieldList.indexOf(field)
						if (it."$field" instanceof Integer) {
							it."$field" = NumberUtils.toInt(row[index])
						} else if (it."$field" instanceof Double) {
							it."$field" = NumberUtils.toDouble(row[index])
						} else {
							it."$field" = row[index]
						}
					}
				}, turtleType.getSimpleName())
				if (turtleAgentSet){
					result.add(turtleAgentSet.first())
				}
			}
		}
		if (initClosure){
			ask(result, initClosure)
		}
		return result
	}
	
	/**
	 * Creates default ordered turtles from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 * 
	 * @param fileName the path to the CSV file
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromCSV(String fileName, Closure initClosure = null) {
		Class<? extends Turtle> turtleType = getTurtleFactory().getTurtleTypeClass("default");
		return createOrderedTurtlesFromCSV(fileName, turtleType, initClosure)
	}

	/**
	 * Creates ordered turtles of specific type from a CSV file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the CSV file is:<p>
	 * 	prop1,prop2,prop3<br>
	 * 	val1a,val2a,val3a<br>
	 * 	val1b,val2b,val3b<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path to the CSV file
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromCSV(String fileName, Class<E> turtleType, Closure initClosure = null) {
		
		// Read the data file.
		List<String[]> rows = new CSVReader(
				new InputStreamReader(new FileInputStream(fileName)))
				.readAll()

		// Define the fields lists.
		List fullFieldList
		List matchedFieldList

		// Gather field information and count agents to make
		int turtleCounter = 0
		for (row in rows) {

			// Check the fields list.
			if (fullFieldList == null) {

				// Fill in the field lists.
				fullFieldList = (List) row
				List<String> fields = getPublicFieldsAndProperties(turtleType)
				matchedFieldList = fields.intersect((List) row)
			} else {
				turtleCounter++
			}
		}

		if (turtleCounter > 0){
			// Define an index tracker.
			int index

			// Create all the ordered turtles
			AgentSet turtleAgentSet = createOrderedTurtles(turtleCounter, null, turtleType.getSimpleName())
			Iterator turtleIterator = turtleAgentSet.iterator()
			def t
			boolean firstItem = true
			for (row in rows){
				if (firstItem){
					firstItem = false
				}
				else {
					t = turtleIterator.next()
					for (field in matchedFieldList) {
						index = fullFieldList.indexOf(field)
						if (t."$field" instanceof Integer) {
							t."$field" = NumberUtils.toInt(row[index])
						} else if (t."$field" instanceof Double) {
							t."$field" = NumberUtils.toDouble(row[index])
						} else {
							t."$field" = row[index]
						}
					}
				}
			}
			
			if (initClosure){
				ask(turtleAgentSet, initClosure)
			}
			return turtleAgentSet
		}
		return new AgentSet<>();
	}

	/**
	 * Creates default turtles from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromExcel(String fileName, Closure initClosure = null) {
		Class<? extends Turtle> turtleType = getTurtleFactory().getTurtleTypeClass("default");
		return createTurtlesFromExcel(fileName, turtleType,initClosure)
	}

	/**
	 * Creates default turtles from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createTurtlesFromExcelWithSheet(String fileName, String sheetName, Closure initClosure = null) {
		Class<? extends Turtle> turtleType = getTurtleFactory().getTurtleTypeClass("default");
		return createTurtlesFromExcelWithSheet(fileName,sheetName,turtleType,initClosure)
	}

	/**
	 * Creates turtles of specific type from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromExcel(String fileName, Class<E> turtleType, Closure initClosure = null) {
		return createTurtlesFromExcelWithSheet(fileName, null, turtleType, initClosure)
	}

	/**
	 * Creates turtles of specific type from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createTurtlesFromExcelWithSheet(String fileName, String sheetName, Class<E> turtleType, Closure initClosure = null) {

		List<String> fields = getPublicFieldsAndProperties(turtleType)
		AgentSet<E> newTurtles = new AgentSet<>();
		Workbook wb;
		Sheet sheet;
		try {
			wb = WorkbookFactory.create(new FileInputStream(fileName))
			if (wb){
				if (sheetName){
					sheet = wb.getSheet(sheetName)
				}
				else {
					// Use the first sheet
					sheet = wb.getSheetAt(0)
				}
				if (sheet){
					int firstRow = sheet.getFirstRowNum()
					List<String> headerFields = []
					// obtain the header fields
					for (Cell cell in sheet.getRow(firstRow)) {
						headerFields.add(cell.getStringCellValue())
					}
					List<String> fieldsToUse = fields.intersect(headerFields)

					for (Row row : sheet) {
						if (row.getRowNum() != firstRow) {
							createTurtles(1,{t ->
								populateFieldValues(t,row,headerFields,fieldsToUse)
								newTurtles.add(t)
							},turtleType.getSimpleName())
						}
					}
					if (initClosure){
						ask(newTurtles, initClosure)
					}
				}
				else{
					if (sheetName != null){
						throw new IOException("Sheet ${sheetName} was not found in workbook ${fileName}.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newTurtles
	}

	/**
	 * Creates ordered default turtles from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromExcel(String fileName, Closure initClosure = null) {
		Class<? extends Turtle> turtleType = getTurtleFactory().getTurtleTypeClass("default");
		return createOrderedTurtlesFromExcel(fileName,turtleType,initClosure)
	}

	/**
	 * Creates ordered default turtles from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param initClosure the initialization routine
	 *
	 */
	public AgentSet<Turtle> createOrderedTurtlesFromExcelWithSheet(String fileName, String sheetName, Closure initClosure = null) {
		Class<? extends Turtle> turtleType = getTurtleFactory().getTurtleTypeClass("default");
		return createOrderedTurtlesFromExcelWithSheet(fileName,sheetName,turtleType,initClosure)
	}

	/**
	 * Creates ordered turtles of specific type from the first sheet of an Excel file.
	 * Optionally provide an initialization closure.
	 * <p>
	 * The format for the Excel file is:<p>
	 * 	| prop1 | prop2 | prop3 |<br>
	 * 	| val1a | val2a | val3a |<br>
	 * 	| val1b | val2b | val3b |<br>
	 * <p>
	 * Where if the turtle type has any of the properties 
	 * (prop1,prop2,prop3), they will be set to (val1a,val2a,val3a)
	 * for the first turtle, (val1b,val2b,val3b) for the second 
	 * turtle, and so on.<p>
	 *
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromExcel(String fileName, Class<E> turtleType, Closure initClosure = null) {
		return createOrderedTurtlesFromExcelWithSheet(fileName, null,turtleType,initClosure)
	}

	/**
	 * Creates ordered turtles of specific type from a specific sheet in an Excel file.
	 * Optionally provide an initialization closure.
	 *
	 * @param fileName the path from the default system directory
	 * @param sheetName the specific sheet name
	 * @param turtleType the class of turtle to create
	 * @param initClosure the initialization routine
	 *
	 */
	public <E extends ReLogoAgent> AgentSet<E> createOrderedTurtlesFromExcelWithSheet(String fileName, String sheetName, Class<E> turtleType, Closure initClosure = null) {
		
		List<String> fields = getPublicFieldsAndProperties(turtleType)

		Workbook wb;
		Sheet sheet;
		try {
			wb = WorkbookFactory.create(new FileInputStream(fileName))
			if (wb){
				if (sheetName){
					sheet = wb.getSheet(sheetName)
				}
				else {
					// Use the first sheet
					sheet = wb.getSheetAt(0)
				}
				if (sheet){
					int firstRow = sheet.getFirstRowNum()
					List<String> headerFields = []
					// obtain the header fields
					for (Cell cell in sheet.getRow(firstRow)) {
						headerFields.add(cell.getStringCellValue())
					}
					List<String> fieldsToUse = fields.intersect(headerFields)
					int turtleCounter = 0
					for (Row row : sheet) {
						if (row.getRowNum() != firstRow && row.getFirstCellNum() != -1) {
							turtleCounter++
						}
					}
					if (turtleCounter > 0){
						AgentSet<E> newTurtles = createOrderedTurtles(turtleCounter, null, turtleType.getSimpleName())
						Iterator turtleIterator = newTurtles.iterator()
						def t
						for (Row row : sheet) {
							if (row.getRowNum() != firstRow) {
								if (turtleIterator.hasNext()){
									t = turtleIterator.next()
									populateFieldValues(t,row,headerFields,fieldsToUse)
								}
							}
						}
						if (initClosure){
							ask(newTurtles, initClosure)
						}
					}
				}
				else {
					if (sheetName != null){
						throw new IOException("Sheet ${sheetName} was not found in workbook ${fileName}.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void populateFieldValues(def t, Row row, List<String> headerFields, List<String> fieldsToUse){
		for (Cell cell : row) {
			String fieldName = headerFields.get(cell.getColumnIndex())
			if (fieldsToUse.contains(fieldName)){
				Object o = getCellContent(cell)
				if (o){
					if (t."$fieldName" instanceof Integer) {
						if (o instanceof Number){
							t."$fieldName" = (Integer)o;
						}
					} else if (t."$fieldName" instanceof Double) {
						if (o instanceof Number){
							t."$fieldName" = (Double)o;
						}
					} else {
						t."$fieldName" = o;
					}
				}
			}
		}
	}

	/**
	 * Get Excel cell content according to type.
	 * @param cell
	 * @return
	 */
	protected Object getCellContent(Cell cell){
		Object o = null;
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				o = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					o = cell.getDateCellValue();
				} else {
					o = cell.getNumericCellValue();
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				o = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				o = cell.getCellFormula();
				break;
			default:
				break;
		}
		return o;
	}

	/**
	 * Utility method to gather all the names of the public fields and properties of a class.
	 * @param clazz class
	 * @return names of the public fields and properties of clazz
	 */
	protected List<String> getPublicFieldsAndProperties(Class clazz){
		List<String> fields = []
		Class curClass = clazz
		BeanInfo bi = Introspector.getBeanInfo(curClass)
		if (bi != null){
			for (PropertyDescriptor pd in bi.getPropertyDescriptors()){
				String propertyName = pd.getName()
				if (!["class", "metaClass"].contains(propertyName)){
					fields.add(propertyName)
				}
			}
		}

		while (true){
			curClass.getDeclaredFields().each{ Field field ->
				String fieldName = field.getName()
				if (Modifier.isPublic(field.getModifiers()) && !Modifier.isSynthetic(field.getModifiers())){
					fields.add(fieldName)
				}
			}
			curClass = curClass.getSuperclass()
			if (!curClass) break;
		}
		return fields.unique()
	}



}
