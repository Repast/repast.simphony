/*CopyrightHere*/
package repast.simphony.util.collections;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;
import repast.simphony.TestUtils;
import repast.simphony.util.collections.DelegatedIterable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tests for the {@link repast.simphony.util.collections.DelegatedIterable}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class DelegatedIterableTest extends TestCase {
	public class DelSource {
		public ArrayList<Object> list = new ArrayList<Object>();

		public DelSource() {
			list.add(new Object());
			list.add(new Object());
			list.add(new Object());

		}

		public Iterator getObjs() {
			return list.iterator();
		}
	}

	DelSource source = new DelSource();

	public void testHashCode() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		DelegatedIterable<Object> objSource = new DelegatedIterable<Object>(
				source, DelSource.class.getMethod("getObjs"));

		assertEquals(objSource.hashCode(), source.hashCode()
				^ DelSource.class.getMethod("getObjs").hashCode());
	}

	public void testDelegatedObjectsSource() throws SecurityException,
			NoSuchMethodException, NoSuchFieldException {

		DelegatedIterable objSource = new DelegatedIterable(source,
				DelSource.class.getMethod("getObjs"));

		assertEquals(PrivateAccessor.getField(objSource, "toCallOn"), source);

	}

	@SuppressWarnings("unchecked")
	public void testGetObjects() throws SecurityException,
			NoSuchMethodException, NoSuchFieldException {
		DelegatedIterable objSource = new DelegatedIterable(source,
				DelSource.class.getMethod("getObjs"));

		assertTrue(TestUtils.collectionsContentsEqual(objSource, source.list));
	}

	/*
	 * Class under test for boolean equals(Object)
	 */
	public void testEqualsObject() throws SecurityException,
			NoSuchMethodException, NoSuchFieldException {
		DelegatedIterable objSource1 = new DelegatedIterable(source,
				DelSource.class.getMethod("getObjs"));

		DelegatedIterable objSource2 = new DelegatedIterable(source,
				DelSource.class.getMethod("getObjs"));

		assertEquals(objSource1, objSource2);
		
		assertFalse(objSource1.equals(new Object()));
	}

}
