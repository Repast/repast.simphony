package repast.simphony.relogo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseObserverTests {

	BaseObserver obs;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		obs = new BaseObserver();
	}

	@After
	public void tearDown() throws Exception {
	}

	// Test for getPublicFieldsAndProperties
	@Test
	public void simpleGroovyClass() {
		List<String> fieldsAndProps = obs.getPublicFieldsAndProperties(ClassAGroovy.class);
		assertEquals(3,fieldsAndProps.size());
		assertTrue(fieldsAndProps.contains("prop1"));
		assertTrue(fieldsAndProps.contains("prop2"));
		assertTrue(fieldsAndProps.contains("field1"));
		assertFalse(fieldsAndProps.contains("field2"));
		
	}
	
	// Test for getPublicFieldsAndProperties
	@Test
	public void inheritedGroovyClass() {
		List<String> fieldsAndProps = obs.getPublicFieldsAndProperties(ClassBGroovy.class);
		assertEquals(4,fieldsAndProps.size());
		assertTrue(fieldsAndProps.contains("bprop1"));
		assertTrue(fieldsAndProps.contains("prop1"));
		assertTrue(fieldsAndProps.contains("prop2"));
		assertTrue(fieldsAndProps.contains("field1"));
		
	}
	
	// Test for getPublicFieldsAndProperties
	@Test
	public void simpleJavaClass() {
		List<String> fieldsAndProps = obs.getPublicFieldsAndProperties(ClassC.class);
		assertEquals(2,fieldsAndProps.size());
		assertTrue(fieldsAndProps.contains("cprop1"));
		assertTrue(fieldsAndProps.contains("cfield1"));
		
	}
	
	// Test for getPublicFieldsAndProperties
	@Test
	public void inheritedJavaFromGroovyClass() {
		List<String> fieldsAndProps = obs.getPublicFieldsAndProperties(ClassD.class);
		assertEquals(5,fieldsAndProps.size());
		assertTrue(fieldsAndProps.contains("bprop1"));
		assertTrue(fieldsAndProps.contains("prop1"));
		assertTrue(fieldsAndProps.contains("prop2"));
		assertTrue(fieldsAndProps.contains("field1"));
		assertTrue(fieldsAndProps.contains("dfield1"));
	}
	
	// Test for getPublicFieldsAndProperties
	@Test
	public void inheritedGroovyFromJavaClass() {
		List<String> fieldsAndProps = obs.getPublicFieldsAndProperties(ClassEGroovy.class);
		assertEquals(3,fieldsAndProps.size());
		assertTrue(fieldsAndProps.contains("cprop1"));
		assertTrue(fieldsAndProps.contains("cfield1"));
		assertTrue(fieldsAndProps.contains("eprop1"));
	}

}
