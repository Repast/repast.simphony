/**
 * 
 */
package repast.simphony.relogo

import repast.simphony.context.Context
import repast.simphony.space.continuous.ContinuousSpace
import repast.simphony.space.grid.Grid
import repast.simphony.util.*
import repast.simphony.relogo.factories.*
import repast.simphony.ui.probe.ProbeID
import static java.lang.Math.*
import static repast.simphony.essentials.RepastEssentials.*
import static java.awt.Color.*
import static repast.simphony.relogo.Utility.*
import repast.simphony.space.graph.Network
import repast.simphony.space.projection.ProjectionEvent
import org.codehaus.groovy.runtime.MetaClassHelper;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import groovy.lang.Closure;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author jozik
 *
 */
public class BaseObserver extends AbstractObserver{
	
	/**
	 * 
	 * This value is used to automatically generate agent identifiers.
	 * 
	 * @field serialVersionUID
	 * 
	 */
	protected static final long serialVersionUID = 1L;

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
		

   
	/**
	* Does nothing, included for translation compatibility.
	*/
	public void watch(Object watched){
		
	}
	

	

}
