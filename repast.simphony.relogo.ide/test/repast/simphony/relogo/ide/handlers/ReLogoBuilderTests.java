package repast.simphony.relogo.ide.handlers;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReLogoBuilderTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private ReLogoBuilder rb;

	@Before
	public void setUp() throws Exception {
		rb = new ReLogoBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	// No need to parse the groovy source to get plural annotations.
	/*@Test
	public void testGetValueFromFullPluralAnnotationLine() {
		ReLogoBuilder.FullBuildInstrumentationInformationVisitor visitor = new ReLogoBuilder.FullBuildInstrumentationInformationVisitor(
				null, null);

		String line = "      @repast.simphony.relogo.Plural(\"somethings\")     ";
		String expected = "somethings";

		assertEquals(expected, visitor.getValueFromFullPluralAnnotationLine(line));

		line = "      @repast.simphony.relogo.Plural('somethings')     ";
		expected = "somethings";

		assertEquals(expected, visitor.getValueFromFullPluralAnnotationLine(line));

		// For lines that are commented out
		line = "//      @repast.simphony.relogo.Plural('somethings')     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromFullPluralAnnotationLine(line));

		// For lines that are commented out
		line = "			//@repast.simphony.relogo.Plural('somethings')     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromFullPluralAnnotationLine(line));

		// For empty plural forms
		line = "      @repast.simphony.relogo.Plural(\"\")     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromFullPluralAnnotationLine(line));

		// For incorrect annotation, but this shouldn't compile in the first place
		line = "      @repast.simphony.relogo.Plural     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromFullPluralAnnotationLine(line));
	}

	@Test
	public void testGetValueFromSimplePluralAnnotationLine() {
		ReLogoBuilder.FullBuildInstrumentationInformationVisitor visitor = new ReLogoBuilder.FullBuildInstrumentationInformationVisitor(
				null, null);

		String line = "      @Plural(\"somethings\")     ";
		String expected = "somethings";

		assertEquals(expected, visitor.getValueFromSimplePluralAnnotationLine(line));

		line = "      @Plural('somethings')     ";
		expected = "somethings";

		assertEquals(expected, visitor.getValueFromSimplePluralAnnotationLine(line));

		// For lines that are commented out
		line = "      //@Plural(\"somethings\")     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromSimplePluralAnnotationLine(line));

		// For lines that are commented out
		line = "//      @Plural('somethings')     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromSimplePluralAnnotationLine(line));

		// For empty plural forms
		line = "      @Plural(\"\")     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromSimplePluralAnnotationLine(line));

		// For incorrect annotation, but this shouldn't compile in the first place
		line = "      @Plural     ";
		expected = null;

		assertEquals(expected, visitor.getValueFromSimplePluralAnnotationLine(line));
	}*/

	@Test
	public void testInstrumentingPackageName() {
		ReLogoBuilder.FullBuildInstrumentationInformationVisitor visitor = new ReLogoBuilder.FullBuildInstrumentationInformationVisitor(
				null, null);

		String packageName = "a.b.c.relogo";
		String expected = "a.b.c";

		assertEquals(expected, visitor.getInstrumentingPackageName(packageName));

		packageName = "a.b.c.relogo.d";
		expected = "a.b.c";

		assertEquals(expected, visitor.getInstrumentingPackageName(packageName));

		packageName = "a.b.c.reLogo.d";
		expected = null;

		assertEquals(expected, visitor.getInstrumentingPackageName(packageName));
		
		packageName = "";
		expected = null;

		assertEquals(expected, visitor.getInstrumentingPackageName(packageName));

		packageName = "relogo";
		expected = "";

		assertEquals(expected, visitor.getInstrumentingPackageName(packageName));
		
		packageName = null;
		expected = null;

		assertEquals(expected, visitor.getInstrumentingPackageName(packageName));

	}
}
