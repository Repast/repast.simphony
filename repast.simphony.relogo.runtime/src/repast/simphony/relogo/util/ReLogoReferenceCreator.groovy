package repast.simphony.relogo.util

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.apache.poi.ss.util.CellReference 
import java.util.List; 
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row 
import org.apache.poi.ss.usermodel.Workbook 
import org.apache.poi.ss.usermodel.WorkbookFactory 

import org.apache.poi.ss.usermodel.Sheet;

import repast.simphony.relogo.Link;
import repast.simphony.relogo.Observer;
import repast.simphony.relogo.Patch;
import repast.simphony.relogo.Turtle;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
//import sun.net.www.protocol.file.FileURLConnection;

//import org.apache.poi.ss.usermodel.*; 

class ReLogoReferenceCreator {
	
	static final String docTitle = "ReLogo Primitives ver. 2.7"
	static final String filename = "data/ReLogoPrimitives.xls"
	static final String docLocation = "docs/ReLogo API/repast/simphony/relogo/"
	static final String refURLbase = "repast/simphony/relogo/"
	private Map<String,List<String>> mapOfTPLOPublicMethods = [:]
	private Map<String,List<String>> mapOfPrimitives = [:]
	List<SheetInfo> workbookSheetsInfo = []
	Workbook wb
	
	static main(args) {
		
		ReLogoReferenceCreator rrc = new ReLogoReferenceCreator()
		rrc.readInPrimitivesExcelFile()
		rrc.createPrimitivesDocument()
		rrc.checkWhichPublicMethodsAreMissing()
	}
	
	public void checkWhichPublicMethodsAreMissing(){
		println "missing methods (Note: Not necessarily a bad thing)"
		List<Class> classes = [Turtle,Patch,Observer,Link,Utility,UtilityG]
		for (Class c in classes){
			String primSheetName = c.getSimpleName()
			if (primSheetName.equals("Utility") || primSheetName.equals("UtilityG")){
				primSheetName = "Utilities"
			}
			for (String methodName in getPublicMethods(c)){
				if (mapOfPrimitives.get(primSheetName).contains(methodName)){
					
				}
				else {
					println "${primSheetName}: $methodName"
				}
			}
		}
	}
	private final static String RSR = "repast.simphony.relogo"
	private final static String JL = "java.lang"
	
	private final static Map<String,String> keywordMap = [
		"Collection":"java.util",
		"Closure":"groovy.lang",
		"String":JL,
		"Class":JL,
		"Number":JL,
		"Object":JL,
		"Iterable":JL,
		"Iterator":"java.util",
		"List":"java.util",
		"AgentSet":RSR,
		"Turtle":RSR,
		"Patch":RSR,
		"Link":RSR,
		"Observer":RSR,
		"DiffusiblePatchVariable":RSR,
		"BigDecimal":"java.math",
		"OutOfContextSubject":RSR
		]
	
	private Map<String,String> classNameContentMap = [:]
	
	protected String createBaseURLname(String className){
			
	}
	
	protected String getContentText(String className){
		String content = classNameContentMap.get(className)
		if (content == null){
				String baseURLname = docLocation + className + ".html"
				URL url = new URL("file","",baseURLname)
				content = url.getText()
				classNameContentMap.put(className, content)
		}
		return content
	}
	
	protected String getFullURL(String sheetName, String methodRefString){
		String className = getClassNameForValidURL(sheetName, methodRefString)
		if (className){
			return refURLbase + className + ".html#" + methodRefString
		}
		return null
	}
	
	/**
	 * Returns the className for which this is a valid URL or null
	 * @param sheetName
	 * @param methodString
	 * @return
	 */
	protected String getClassNameForValidURL(String sheetName, String methodString){
		String pat = Pattern.quote(methodString)
		
		if (sheetName.equals("Utilities")){
			String u = "Utility"
			String content = getContentText(u)
			if (content.find(pat)) return u
			else {
				String g = "UtilityG"
				content = getContentText(g)
				if (content.find(pat)) return g
				else return null
			}
			
		}
		else {// sheetName is the same as className for these
			String content = getContentText(sheetName)
			
			if(content.find(pat)) return sheetName
			else return null
		}
	}
	
	protected String transformMethodString(String input){
		String result = input
		
		// remove all generics greedily
//		result = result.replaceAll(/<.+>/,"")
		
//		println("result in: " + result)  // testing
		
		// replace keywords
		Pattern word = Pattern.compile(/\b\w+/)
		def closure = {
			String res = keywordMap.get(it)
			if (res){
				return res + "." + it
			}
			return it
		}
		
		// Break up the method string into parts separated by commas
		List<String> resultParts =  result.tokenize(",")
		result = "";
		for (String part : resultParts){
			// If part contains doesn't contain generics, then format for the full 
			//    class name
			if (part.indexOf("<") == -1){
				result += part.replaceAll(word,closure)
			}
			else{  // otherwise just append the part
				result += part
			}
			
			// Add a comma to separate multiple method args
			if (resultParts.size > 1){
				result += ","
			}
		}
		
		// drop the last "," added to the end of multi arg methods
		if (resultParts.size > 1){
			result = result[0..-2 ] 
		}
		
		// add space after comma
		result = result.replaceAll(",",", ")
		
		// remove vararg ellipsis
		result = result.replaceAll(/\.\.\./,"")
		
//		println("  result out: " + result)  // testing
		
		return result
	}
	
	protected String getTypeName(Type type){
		if (type instanceof Class){
			Class c = (Class)type
			return c.getName().replaceAll(/<.+>/,"").replaceAll(/cern\.colt\.function\./,"").replaceAll(/\[L(.+);/,{it[1]})
		}
		return type.toString().replaceAll(/<.+>/,"")
	}
	
	protected List<String> getPublicMethods(Class c){
		List<String> pm = []
		Method [] ms = c.getDeclaredMethods()
		for (Method mm in ms){
			if (Modifier.isPublic(mm.getModifiers())){
				StringBuilder sb = new StringBuilder()
				sb.append(mm.getName() + "(")
				boolean first = true
				for (Type t in mm.getGenericParameterTypes()){
					if(!first){
						sb.append(", ")
					}
					first = false
					sb.append(getTypeName(t))
				}
				sb.append(")")
				pm.add(sb.toString())
			}
		}
		return pm
	}
	
	
	public void readInPrimitivesExcelFile(){
		wb = WorkbookFactory.create(new FileInputStream(filename))
		for (int k = 0; k < wb.getNumberOfSheets(); k++) {
			workbookSheetsInfo.add(getSheetInfo(k))
		}
	}
	
	public SheetInfo getSheetInfo(int index){
		Sheet sheet = wb.getSheetAt(index);
		String sheetName = sheet.getSheetName()
		mapOfPrimitives.put(sheetName,[])
//		sheetClassName = sheetClassName.equals("Utilities") ? "Utility" : sheetClassName
		def list = [];
		PrimitiveCategory currentCategory = null
		Iterator iter = sheet.rowIterator()

		for(Row row : sheet){
			Cell firstCell = row.getCell(0)
			// check that the cell is not null and string type
			if (firstCell != null && firstCell.getCellType() == Cell.CELL_TYPE_STRING){
				String firstCellString = firstCell.getRichStringCellValue().getString().trim()
					
				// check that the string cell is not a blank string cell
				if(!firstCellString.equals("")){
					// check if we are looking for the next category
					if (firstCellString.startsWith("#")){
						currentCategory = new PrimitiveCategory(firstCellString.substring(1))
						list.add(currentCategory)
					}
					else {
						// transform and check method string
						String methodRefString = transformMethodString(firstCellString)
						String fullURL = getFullURL(sheetName,methodRefString)
						if (fullURL){
							currentCategory.addMethodAndRef(firstCellString, fullURL)
							mapOfPrimitives.get(sheetName).add(methodRefString)
//							println "adding ${sheetName}: $methodRefString"
						}
						else {
							throw new RuntimeException("the methodRefString $methodRefString for class $sheetName on line ${row.rowNum} is not valid")
						}
					}
				}
			}
		}
		return new SheetInfo(sheetName, list)
	}
	
	/*private def getCellContent(Cell cell){
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
	}*/
	
	def createPrimitivesDocument(){
		def writer = new FileWriter('docs/ReLogo API/ReLogoPrimitives.html')
		def html	= new groovy.xml.MarkupBuilder(writer) 
		html.html {
			head { 
				title docTitle
				
				// Style inline using simple CSS to match the Javadoc atyle
				style(type:"text/css", '''
          table {
            border: 1px solid #dee3e9;
            border-collapse: collapse;
          }
          td {
            border: 2px solid #4D7A97;
            white-space: nowrap;
            border-collapse: collapse;
            vertical-align: top;
            padding: 10px;
            font-family:"DejaVu Sans", Arial, Helvetica, sans-serif;
            font-size:14px;
          }
          th {
            color:#4E4E4E;
            white-space: nowrap;
            border-collapse: collapse;
            vertical-align: top;
            padding: 10px;
            font-family:"DejaVu Sans", Arial, Helvetica, sans-serif;
            font-size:14px;
            background-color: #dee3e9;
          }
          body {

            font-family:"DejaVu Sans", Arial, Helvetica, sans-serif;
          }
          a:link, a:visited {
            text-decoration:none;
            color:#4A6782;
            font-weight:bold;
          }
          a:hover, a:focus {
            text-decoration:none;
            color:#bb7a2a;
            font-weight:bold;
          }

        ''')
			}
			
			
			body {
				h1 docTitle
				StringBuffer sb = new StringBuffer() 
				for (SheetInfo si in workbookSheetsInfo) {
					String name = si.getSheetName();
					font(size:5) {
						a(href:"#$name",name)
						mkp.yieldUnescaped('&nbsp;&nbsp;')
					}
					
				}
				hr(width:'100%')
				for (SheetInfo si in workbookSheetsInfo) {
					String name = si.getSheetName();
					A (NAME: name)
					h2(name)
					
					table {
						tr {
							for (PrimitiveCategory prim : si.getPrimitiveCategories()){
								th {
									font(size:4, prim.getName())
								}
							}
						}
						tr {
							for (PrimitiveCategory prim : si.getPrimitiveCategories()){
								td {
									
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
