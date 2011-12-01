/**
 * 
 */
package repast.simphony.relogo.factories

import repast.simphony.relogo.BaseObserver
import repast.simphony.context.DefaultContextimport repast.simphony.context.Context
import repast.simphony.relogo.RLDimensionsimport repast.simphony.relogo.BaseTurtleimport repast.simphony.relogo.BasePatchimport repast.simphony.relogo.BaseLink/**
 * @author jozik
 *
 */
public class ObserverFactoryImplTest extends GroovyTestCase{
	
	public void testFactoryCreation(){
		ObserverFactory obsF = new ObserverFactory(BaseObserver)
		String observerID = "obs1"
		Context context = new DefaultContext("context1")
		TurtleFactory tf = new TurtleFactory(BaseTurtle)
		PatchFactory pf = new PatchFactory(BasePatch)
		LinkFactory lf = new LinkFactory(BaseLink)
		ReLogoWorldFactory rLWorldFactory = new ReLogoWorldFactory()
		RLDimensions rLDimensions = new RLDimensions(-5, 5, -7, 7)
		BaseObserver dO = obsF.createObserver(observerID, context, rLWorldFactory, rLDimensions, tf, pf, lf, lf)
		
		assertTrue(dO != null)
		assertTrue(dO.getObserverID() == "obs1")
	}
	
	public void testFactoryCreationFailure(){
		// I want this constructor to fail since Object does not extend DefaultObserver
		try{
			ObserverFactory obsF = new ObserverFactory(Object)
			fail("Object accepted as valid class.")
			}
		catch (Exception e) {
			assertTrue(true)
		}
	}	
}