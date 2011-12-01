/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math

import junit.framework.Test;
import junit.framework.TestSuite;

public class MatrixTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for repast.simphony.groovy");
		//$JUnit-BEGIN$
		suite.addTestSuite(MatrixCategoryTestJava.class);
		suite.addTestSuite(MatrixCategoryTestGroovy.class);
		//$JUnit-END$
		return suite;
	}

}
