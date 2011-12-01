package repast.simphony.util;

import java.util.Comparator;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class ToStringComparator implements Comparator<Object> {

	public int compare(Object o, Object o1) {
		return o.toString().compareTo(o1.toString());
	}
}
