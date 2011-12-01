/**
 * 
 */
package repast.simphony.relogo

import groovy.lang.Closure;
import repast.simphony.relogo.factories.LinkFactoryimport repast.simphony.context.space.graph.NetworkBuilderimport repast.simphony.relogo.factories.ReLogoEdgeCreatorimport repast.simphony.space.graph.Networkimport repast.simphony.context.Context 
import repast.simphony.space.graph.RepastEdge
import repast.simphony.ui.probe.ProbeID;

import org.codehaus.groovy.runtime.MetaClassHelper;


/**
 * @author jozik
 *
 */
public class BaseLink<T> extends AbstractLink<T>{


	/**
	 * Sets tie mode to "none", "fixed", or "free".
	 * @param mode one of ["none", "fixed", "free"]
	 */
	public void setTieMode(String mode){
		 switch( mode ){
		    case 'none': 
		    	this.untie()
		      	break
		    case 'fixed':
		    	this.tie()
		    	break
		    case 'free': 
		    	this.free()
		      	break
	      	default:
	      		println "\"$mode\" is not a valid tie mode"
		 }
	}
	
	/**
	*
	* This method provides a human-readable name for the agent.
	*
	* @method toString
	*
	*/
   @ProbeID()
   public String toString() {
	   return super.toString();
   }
	
}
