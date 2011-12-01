package repast.simphony.relogo.ide.wizards;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NetlogoImportWizardTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetResourceFromStream() {
		System.out.println(getClass().getClassLoader());
		InputStream catchAllTemplateStream = getClass()
				.getResourceAsStream("/templates/catchAll.stg");
		assert(catchAllTemplateStream != null);
	}

}
