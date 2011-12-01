/**
 * 
 */
package repast.simphony.relogo

import groovy.lang.Closure;

import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.relogo.factories.PatchFactoryimport repast.simphony.util.collections.FilteredIterator;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.PredicateUtils;
import org.apache.commons.collections15.functors.InstanceofPredicate;
import org.apache.commons.collections15.functors.NotPredicate;
import org.apache.commons.collections15.functors.EqualPredicate;

import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.QueryUtils;
import repast.simphony.query.space.grid.VNQuery;
import repast.simphony.space.SpatialException;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.space.grid.Grid
import org.codehaus.groovy.runtime.MetaClassHelper;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * @author jozik
 *
 */
public class BasePatch extends AbstractPatch{

	
	public void setBasePatchProperties(Observer observer, PatchFactory patchFactory){
		setMyObserver(observer);
		setMyPatchFactory(patchFactory);
	}
		
	/**
	 *
	 * This value is used to automatically generate agent identifiers.
	 * @field serialVersionUID
	 *
	 */
	protected static final long serialVersionUID = 1L;
	

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
