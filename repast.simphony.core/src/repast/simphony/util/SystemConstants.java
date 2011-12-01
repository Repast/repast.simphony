package repast.simphony.util;

import java.io.File;

/**
 * Holds some system specific constants.
 *
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public interface SystemConstants {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String DIR_SEPARATOR = File.separator;
}
