/*CopyrightHere*/
package repast.simphony.integration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import simphony.util.messages.Log4jMessageListener;
import simphony.util.messages.MessageCenter;

/**
 * Class with some functions that can be used for working with data files.
 * 
 * @author Jerry Vos
 */
public class DataFileUtils {
	public static final String IGNORE_DATA_TARGET = "null";

	public static final String SCHEMA_FILE_NAME = "datadescriptor.xsd";
	
	public static final String GENERATED_SCHEMA_FILE_NAME = "dataDescriptorEcore.xsd";

	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(DataFileUtils.class);

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	private static final String GENERATED_EXT = "filedescription";

	/**
	 * Loads in a descriptor file and returns the resulting Document. This will set the schema
	 * explicitly to the one found in the current package called {@link #SCHEMA_FILE_NAME}, so the
	 * file need not have the schema specified.
	 * 
	 * @param fileName
	 *            the name of the descriptor file
	 * @return a Document object corresponding to the specified descriptor file
	 * @throws JDOMException
	 *             if there was an error inside JDOM
	 * @throws IOException
	 *             if there was an error reading
	 * @throws RunTimeException
	 *             if the file cannot be found
	 */
	public static Document loadDescriptor(String fileName) throws JDOMException, IOException {
		try {
			SAXBuilder docBuilder = new SAXBuilder();
			docBuilder.setErrorHandler(new MessageCenterHandler());
			docBuilder.setValidation(true);
			docBuilder.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);


			InputStream schemaStream;
			
			InputStream fileInStream;
			
			if (fileName.endsWith(GENERATED_EXT)) {
				// handle as generated file
				LineNumberReader reader = new LineNumberReader(new FileReader(fileName));
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				// chop off the extra generated garbage
				String line;
				while ((line = reader.readLine()) != null) {
					if (!line.contains("DataDescriptorSchema:DocumentRoot")) {
						byteStream.write(line.getBytes());
					}
				}
				
				fileInStream = new ByteArrayInputStream(byteStream.toByteArray());
				
			} else {
				fileInStream = new FileInputStream(fileName);
			}
			
			schemaStream = DataFileUtils.class.getResourceAsStream(SCHEMA_FILE_NAME);
			
			if (schemaStream == null) {
				RuntimeException ex = new RuntimeException("Error, the data file schema file("
						+ SCHEMA_FILE_NAME + ") could not be fetched as a resource Stream.");
				msgCenter.error(ex.getMessage(), ex);
				throw ex;
			}

			docBuilder.setProperty(JAXP_SCHEMA_SOURCE, schemaStream);

			// *****domBuilderFactory.setIgnoringComments(true);
			// domBuilderFactory.setIgnoringElementContentWhitespace(true);
			return docBuilder.build(fileInStream);

		} catch (JDOMException ex) {
			msgCenter.error(
					"Error loading XML descriptor file; could not find the descriptor XML file("
							+ fileName + ")", ex);
			throw ex;
		}
	}

	/**
	 * Tries to retrieve an attribute from the nodeToHandle based on the target. If useXPath is true
	 * it then will query the Queryable using it's selectNode function and return the result of the
	 * Queryable's getValue on that result (or null if the result is null). If useXPath is false it
	 * will skip the XPath/Queryable steps and just return the toString of the attribute's value.
	 * 
	 * @param queryable
	 *            the queryable source for fetching values from
	 * @param element
	 *            the element to fetch the target from
	 * @param attributeName
	 *            the attribute's name.
	 * @return a value as speciied
	 */
	public static String findBestValue(Queryable queryable, Element nodeToHandle, String attributeName,
			boolean useXPath) {
		if (nodeToHandle == null || nodeToHandle.getAttribute(attributeName) == null) {
			return null;
		}

		String nodeValue = nodeToHandle.getAttribute(attributeName).getValue();
		if (useXPath) {
			// first see if it works as an XPath expression
			try {
				Object xpathResult = queryable.selectNode(nodeValue);
				
				if (xpathResult != null) {
					return queryable.getValue(xpathResult).toString();
				} /* else {
					return null;
				}*/
					
			} catch (Exception ex) {
				// exception from xpath, so ignore it
			}
		}
		
		// otherwise we'll juse return the value of the node
		return nodeValue.toString();
	}

	/**
	 * 
	 * 
	 * @param queryable
	 *            the queryable source for fetching values from
	 * @param targetPath
	 * 	
	 * @return a value as speciied
	 */
	public static Object findExplicitTargetParent(Queryable queryable, String targetPath) {
		if (targetPath == null) 
			return null;
		
		int lastSlash = targetPath.lastIndexOf("/");
		if (lastSlash <= 0) {
			return null;
		}
		
		Object parent = queryable.selectNode(targetPath.substring(0, lastSlash));
		
		return parent;
	}

	/**
	 * 
	 * 
	 * @param targetPath
	 * 	
	 * @return a value as speciied
	 */
	public static String getName(String targetPath) {
		if (targetPath == null) 
			return null;
		
		int lastSlash = targetPath.lastIndexOf("/");
		if (lastSlash <= 0) {
			return targetPath;
		}
		
		return targetPath.substring(lastSlash + 1, targetPath.length());
	}

	/**
	 * Checks if a string is null or "".
	 * 
	 * @param string
	 *            a string to check
	 * @return true if the string is either null or equal to ""
	 */
	public static boolean isGarbage(String string) {
		if (string != null && !string.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Same as <code>findBestValue(queryable, element, attributeName, true)</code>
	 * 
	 * @see findBestValue
	 */
	public static String findBestValue(Queryable queryable, Element element, String attributeName) {
		return findBestValue(queryable, element, attributeName, true);
	}
	
	public static void main(String[] args) {
		Log4jMessageListener.loadDefaultSettings();
		
		try {
			loadDescriptor("Untitled.datadescriptor");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
