/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

/**
 * <p> This interface represents the measurable, countable, or comparable 
 *     property or aspect of a thing.</p>
 *     
 * <p> Implementing instances are typically the result of a measurement:[code]
 *         Measurable<Mass> weight = Measure.valueOf(180.0, POUND);
 *     [/code]
 *     They can also be created from custom classes:[code]
 *     class Delay implements Measurable<Duration> {
 *          private long nanoSeconds; // Implicit internal unit.
 *          public double doubleValue(Unit<Velocity> unit) { ... }
 *          public long longValue(Unit<Velocity> unit) { ... }
 *     }
 *     Thread.wait(new Delay(24, HOUR)); // Assuming Thread.wait(Measurable<Duration>) method.
 *     [/code]</p>
 *     
 * <p> Although measurable instances are for the most part scalar quantities; 
 *     more complex implementations (e.g. vectors, data set) are allowed as 
 *     long as an aggregate magnitude can be determined. For example:[code]
 *     class Velocity3D implements Measurable<Velocity> {
 *          private double x, y, z; // Meter per seconds.
 *          public double doubleValue(Unit<Velocity> unit) { ... } // Returns vector norm.
 *          ... 
 *     }
 *     class Sensors<Q extends Quantity> extends Measure<double[], Q> {
 *          public doubleValue(Unit<Q> unit) { ... } // Returns median value. 
 *          ...
 *     } [/code]</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.1, June 8, 2007
 * 
 * Modified on 8/14/2007 by Michael J. North to allow Groovy comparision operators
 * to work despite the following issues:
 * 
 *   http://jira.codehaus.org/browse/GROOVY-1888
 *   http://jira.codehaus.org/browse/GROOVY-1889
 *   
 */
//public interface Measurable<Q extends Quantity> extends Comparable<Measurable<Q>> {
public interface Measurable<Q extends Quantity> extends Comparable {
    
    /**
     * Returns the estimated value of this measurable quantity stated 
     * in the specified unit as a <code>double</code>.
     * 
     * @param unit the unit in which the measurement value is stated.
     * @return the numeric value after conversion to type <code>double</code>.
     */
    double doubleValue(Unit<Q> unit);

    /**
     * Returns the estimated value of this quantity stated in the specified 
     * unit as a <code>long</code>.
     * 
     * @param unit the unit in which the measurement value is stated.
     * @return the numeric value after conversion to type <code>long</code>.
     * @throws ArithmeticException if this quantity cannot be represented 
     *         as a <code>long</code> number in the specified unit.
     */
    long longValue(Unit<Q> unit) throws ArithmeticException;
    
}
