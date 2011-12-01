/*CopyrighHere*/
package repast.simphony.integration;

import junit.framework.TestCase;


/**
 * Really simple test for {@link repast.simphony.integration.FileDef}
 * 
 * @author Jerry Vos
 */
public class FileDefTest extends TestCase {

	public void testGetFileDef() {
		Object obj = new Object();
		
		FileDef def = new FileDef(obj);
		assertSame(obj, def.getFileDef());
	}

}
