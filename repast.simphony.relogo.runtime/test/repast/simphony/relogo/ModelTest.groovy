/**
 * 
 */
package repast.simphony.relogo

import static repast.simphony.relogo.Utility.*
import static java.lang.Math.*



/**
 * @author jozik
 *
 */
public class ModelTest extends GroovyTestCase{
	
	public void testTick(){
		ReLogoModel model = Model.getInstance()
		assert(model.getTicks() == 0.0)
		model.tick()
		assert(model.getTicks() == 1)
		model.resetTicks()
		assert(model.getTicks() == 0.0)
		model.tickAdvance(1.5)
		assert(model.getTicks() == 1.5)
	}


}
