package repast.simphony.query.space.gis;

import org.apache.commons.collections15.Predicate;

import repast.simphony.query.Query;
import repast.simphony.query.QueryUtils;
import repast.simphony.space.gis.Geography;
import repast.simphony.util.collections.FilteredIterator;
import simphony.util.messages.MessageCenter;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * Abstract implementation of geometry based queries. This can be used
 * as the basis for queries can performed on the basis of intersection
 * with a source object's geometry.
 */
public abstract class AbstractGeometryQuery<T> implements Query<T> {

	protected Geography<T> geography;
  protected Geometry geom;
  protected Predicate<T> predicate;
  protected T sourceObject;
  
  private static final MessageCenter msg = MessageCenter.getMessageCenter(AbstractGeometryQuery.class);
	
  public AbstractGeometryQuery(Geography<T> geography, T sourceObject) {
    this(geography, geography.getGeometry(sourceObject));
    this.sourceObject = sourceObject;
    this.predicate = createPredicate();
  }

  public AbstractGeometryQuery(Geography<T> geography, Geometry geom) {
		
  	if (geom == null) {
  		msg.error("Error creating geography query", 
  				new IllegalArgumentException("Null geometry argument."));
		}
  	
  	this.geography = geography;
		this.geom = geom;
    this.predicate = createPredicate();
  }

  /**
   * Create a predicate that will be used to test
   * objects that intersect the source object. Those that
   * pass the predicate evaluate will be included in the query
   * results.
   *
   * @return Create a predicate that will be used to test
   * objects that intersect the source object.
   */
  protected abstract Predicate<T> createPredicate();


  /**
	 * Returns an iterable over the objects that are the result of the query
	 * and are in the passed in iterable. This allows queries to be chained
	 * together where the result of one query is passed into another.
	 *
	 * @param set the iterable to test for inclusion
	 * @return an iterable over the objects that are the result of the query
	 *         and are in the passed in iterable.
	 */
  public Iterable<T> query(Iterable<T> set) {
    return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
	}

  /**
	 * Returns the result of the query.
	 *
	 * @return an iterable over the objects that are the result of the query.
	 */
  public Iterable<T> query() {
  	if (geom == null) return null;
  	
		Envelope envelope = geom.getEnvelopeInternal();
		Iterable<T> potential = geography.queryInexact(envelope);
    return new FilteredIterator<T>(potential.iterator(), predicate);
	}
}
