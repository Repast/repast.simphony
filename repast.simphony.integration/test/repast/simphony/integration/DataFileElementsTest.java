/*CopyrightHere*/
package repast.simphony.integration;

import org.jdom.Element;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import repast.simphony.integration.DataFileElements.ARRAY_ATTRS;
import repast.simphony.integration.DataFileElements.DATA_ATTRS;
import repast.simphony.integration.DataFileElements.FILE_DEF_ATTRS;
import repast.simphony.integration.DataFileElements.RECORD_ATTRS;
import repast.simphony.integration.DataFileElements.TABLE_ATTRS;

public class DataFileElementsTest extends MockObjectTestCase {
	private Mock mockQuerier;
	private Queryable queryable;

	@Override
	protected void setUp() throws Exception {
		mockQuerier = mock(Queryable.class);
		mockQuerier.stubs().will(returnValue(null));
		queryable = (Queryable) mockQuerier.proxy();
	}
	
	public void testFILE_DEF_ATTRSNewLineType() {
		Element element = new Element("element");
		assertEquals(NewLinePatternConverter.NONE.getType(), FILE_DEF_ATTRS
				.getNewLineType(element));
		element
				.setAttribute(FILE_DEF_ATTRS.NEW_LINE_TYPE, NewLinePatternConverter.SYSTEM
						.getType());
		assertEquals(NewLinePatternConverter.SYSTEM.getType(), FILE_DEF_ATTRS
				.getNewLineType(element));
	}

	public void testDATA_ATTRSType() {
		Element element = new Element("element");
		element.setAttribute(DATA_ATTRS.TYPE, "int");
		assertEquals("int", DATA_ATTRS.getType(queryable, element));
	}

	public void testDATA_ATTRSTarget() {
		Element element = new Element("element");
		element.setAttribute(DATA_ATTRS.TARGET, "mytarget");
		assertEquals("mytarget", DATA_ATTRS.getTarget(queryable, element));
	}

	public void testDATA_ATTRSLength() {
		Element element = new Element("element");

		assertEquals(-1, DATA_ATTRS.getLength(queryable, element));
		
		element.setAttribute(DATA_ATTRS.LENGTH, "9");
		assertEquals(9, DATA_ATTRS.getLength(queryable, element));
	}

	public void testDATA_ATTRSDelimiter() {
		Element element = new Element("element");
		
		assertNull(DATA_ATTRS.getDelimiter(queryable, element, "int"));
		assertNull(DATA_ATTRS.getDelimiter(queryable, element, "fakeTypesasdf"));
		assertEquals(DATA_ATTRS.DEFAULT_STRING_DELIMITER, DATA_ATTRS.getDelimiter(queryable, element, "String"));
		
		element.setAttribute(DATA_ATTRS.DELIMITER, "asdfasdf");
		assertEquals("asdfasdf", DATA_ATTRS.getDelimiter(queryable, element, null));
	}

	public void testDATA_ATTRSPattern() {
		Element element = new Element("element");
		
		assertNull(DATA_ATTRS.getPattern(queryable, element));
		assertEquals(DATA_ATTRS.DEFAULT_STRING_DELIMITER, DATA_ATTRS.getDelimiter(queryable, element, "String"));
		
		element.setAttribute(DATA_ATTRS.PATTERN, "asdfasdf");
		assertEquals("asdfasdf", DATA_ATTRS.getPattern(queryable, element));
	}

	public void testRECORD_ATTRSTarget() {
		Element element = new Element("element");
		
		assertEquals(null, RECORD_ATTRS.getTarget(queryable, element));
		
		element.setAttribute(RECORD_ATTRS.TARGET, "mytarget");
		assertEquals("mytarget", RECORD_ATTRS.getTarget(queryable, element));
	}

	public void testRECORD_ATTRSCount() {
		Element element = new Element("element");
		
		assertEquals(-1, RECORD_ATTRS.getCount(queryable, element));
		
		element.setAttribute(RECORD_ATTRS.COUNT, "7");
		assertEquals(7, RECORD_ATTRS.getCount(queryable, element));
	}

	public void testARRAY_ATTRSCount() {
		Element element = new Element("element");
		
		assertEquals(null, ARRAY_ATTRS.getTarget(queryable, element));
		
		element.setAttribute(ARRAY_ATTRS.TARGET, "mytarget");
		assertEquals("mytarget", ARRAY_ATTRS.getTarget(queryable, element));
	}

	public void testARRAY_ATTRSType() {
		Element element = new Element("element");
		
		assertEquals(null, ARRAY_ATTRS.getTarget(queryable, element));
		
		element.setAttribute(ARRAY_ATTRS.TYPE, "mytype");
		assertEquals("mytype", ARRAY_ATTRS.getType(queryable, element));
	}

	public void testARRAY_ATTRSColDelimiter() {
		Element element = new Element("element");
		
		assertEquals(null, ARRAY_ATTRS.getColDelimiter(queryable, element));
		
		element.setAttribute(ARRAY_ATTRS.COL_DELIMITER, "mycoldelim");
		assertEquals("mycoldelim", ARRAY_ATTRS.getColDelimiter(queryable, element));
	}

	public void testARRAY_ATTRSRowDelimiter() {
		Element element = new Element("element");
		
		assertEquals(null, ARRAY_ATTRS.getRowDelimiter(queryable, element));
		
		element.setAttribute(ARRAY_ATTRS.ROW_DELIMITER, "myrowdelim");
		assertEquals("myrowdelim", ARRAY_ATTRS.getRowDelimiter(queryable, element));
	}

	public void testARRAY_ATTRSRows() {
		Element element = new Element("element");
		
		assertEquals(-1, ARRAY_ATTRS.getRows(queryable, element));
		
		element.setAttribute(ARRAY_ATTRS.ROWS, "10");
		assertEquals(10, ARRAY_ATTRS.getRows(queryable, element));
	}

	public void testARRAY_ATTRSCols() {
		Element element = new Element("element");
		
		assertEquals(-1, ARRAY_ATTRS.getRows(queryable, element));
		
		element.setAttribute(ARRAY_ATTRS.COLS, "10");
		assertEquals(10, ARRAY_ATTRS.getCols(queryable, element));
	}

	public void testTABLE_ATTRSRows() {
		Element element = new Element("element");
		
		assertEquals(-1, TABLE_ATTRS.getRows(queryable, element));
		
		element.setAttribute(TABLE_ATTRS.ROWS, "10");
		assertEquals(10, TABLE_ATTRS.getRows(queryable, element));
	}

	public void testTABLE_ATTRSColumns() {
		Element element = new Element("element");
		
		assertEquals(null, ARRAY_ATTRS.getRowDelimiter(queryable, element));
		
		Element data = new Element(TABLE_ATTRS.COLUMNS);
		element.addContent(data);
		assertEquals(data, TABLE_ATTRS.getColumns(element));
	}
}
