/**
 * 
 */
package repast.simphony.relogo

import groovy.lang.Closure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.Collections
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.context.Context;
import repast.simphony.space.SpatialException;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.graph.Network
import repast.simphony.space.continuous.WrapAroundBorders
import repast.simphony.relogo.factories.*
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.QueryUtils;
import repast.simphony.util.collections.FilteredIterator;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.PredicateUtils;
import org.apache.commons.collections15.functors.InstanceofPredicate;
import org.apache.commons.collections15.functors.NotPredicate;
import org.apache.commons.collections15.functors.EqualPredicate;
import org.codehaus.groovy.runtime.MetaClassHelper;

import static repast.simphony.essentials.RepastEssentials.*;

/**
 * @author jozik
 *
 */
public class BaseTurtle extends AbstractTurtle {                         					
	
	/**
	 *
	 * This value is used to automatically generate agent identifiers.
	 * @field serialVersionUID
	 *
	 */
	protected static final long serialVersionUID = 1L;
	
	
	/**
	 * Moves the turtle to the lowest value of a patch variable of eight
	 * neighboring patches.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void downhill(String patchVariable){
		
		String getterString = "get" + MetaClassHelper.capitalize(patchVariable)			 
		moveTo(patchHere());
		// TODO: can change this to use the same mechanism as diffuse
		def p = Utility.minOneOfU(neighbors(),{"$getterString"()});
		def pVal = p."$getterString"()
		def thisVal = this."$getterString"()
		if (pVal < thisVal) {
			face(p)
			moveTo(p)
		}
	}
	
	/**
	 * Moves the turtle to the lowest value of a patch variable of four
	 * neighboring patches.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void downhill4(String patchVariable){

		String getterString = "get" + MetaClassHelper.capitalize(patchVariable)			 
		moveTo(patchHere());
		// TODO: can change this to use the same mechanism as diffuse
		def p = Utility.minOneOfU(neighbors4(),{"$getterString"()});
		def pVal = p."$getterString"()
		def thisVal = this."$getterString"()
		if (pVal < thisVal) {
			face(p)
			moveTo(p)
		}
	}
	
	/**
	 * Moves the turtle to the highest valued patch of patchVariable in the
	 * eight neighboring patches plus the current patch.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void uphill(String patchVariable){
		
		String getterString = "get" + MetaClassHelper.capitalize(patchVariable)			 
		moveTo(patchHere());
		// TODO: can change this to use the same mechanism as diffuse
		def p = Utility.maxOneOfU(this,neighbors(),{"$getterString"()});
		def pVal = p."$getterString"()
		def thisVal = this."$getterString"()
		if (pVal > thisVal) {
			face(p)
			moveTo(p)
		}
	}
	
	/**
	 * Moves the turtle to the highest valued patch of patchVariable in the four
	 * neighboring patches plus the current patch.
	 * 
	 * @param patchVariable
	 *            a string
	 */
	public void uphill4(String patchVariable){
		
		String getterString = "get" + MetaClassHelper.capitalize(patchVariable)			 
		moveTo(patchHere());
		// TODO: can change this to use the same mechanism as diffuse
		def p = Utility.maxOneOfU(this,neighbors4(),{"$getterString"()});
		def pVal = p."$getterString"()
		def thisVal = this."$getterString"()
		if (pVal > thisVal) {
			face(p)
			moveTo(p)
		}
	}
	
	/**
	 *
	 * This method provides a human-readable name for the agent.
	 * @method toString
	 *
	 */
	@ProbeID()
	public String toString() {
		return super.toString();
	}
}
