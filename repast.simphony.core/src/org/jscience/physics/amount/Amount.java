/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.amount;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.measure.Measurable;
import javax.measure.converter.ConversionException;
import javax.measure.converter.RationalConverter;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import javax.realtime.MemoryArea;

import javolution.context.ObjectFactory;
import javolution.lang.Immutable;
import javolution.lang.MathLib;
import javolution.text.Text;
import javolution.util.FastComparator;
import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.jscience.mathematics.structure.Field;

/**
 * <p> This class represents a determinate or estimated amount for which 
 *     operations such as addition, subtraction, multiplication and division
 *     can be performed (it implements the {@link Field} interface).</p>
 *     
 * <p> The nature of an amount can be deduced from its parameterization 
 *     (compile time) or its {@link #getUnit() unit} (run time).
 *     Its precision is given by its {@link #getAbsoluteError() error}.</p>
 *     
 * <p> Amounts can be {@link #isExact() exact}, in which case they can be
 *     expressed as an exact <code>long</code> integer in the amount unit.
 *     The framework tries to keep amount exact as much as possible.
 *     For example:[code]
 *        Amount<Length> m = Amount.valueOf(33, FOOT).divide(11).times(2);
 *        System.out.println(m);
 *        System.out.println(m.isExact() ? "exact" : "inexact");
 *        System.out.println(m.getExactValue());
 *        > 6 ft
 *        > exact
 *        > 6[/code] 
 *     </p>
 *     
 * <p> Errors (including numeric errors) are calculated using numeric intervals.
 *     It is possible to resolve systems of linear equations involving 
 *     {@link org.jscience.mathematics.vector.Matrix matrices}, even if the 
 *     system is close to singularity; in which case the error associated with
 *     some (or all) components of the solution may be large.</p>
 *     
 * <p> By default, non-exact amounts are shown using the plus/minus  
 *     character ('±') (see {@link AmountFormat}). For example, 
 *     <code>"(2.0 ± 0.001) km/s"</code> represents a velocity of 
 *     2 km/s with an absolute error of ± 1 m/s. Exact amount use an
 *     integer notation (no decimal point, e.g. <code>"2000 m"</code>).</p>
 *     
 * <p> Operations between different amounts may or may not be authorized 
 *     based upon the current {@link org.jscience.physics.model.PhysicalModel
 *     PhysicalModel}. For example, adding <code>Amount&lt;Length&gt; and 
 *     <code>Amount&lt;Duration&gt; is not allowed by the 
 *     {@link org.jscience.physics.model.StandardModel StandardModel}, 
 *     but is authorized with the {@link
 *     org.jscience.physics.model.RelativisticModel RelativisticModel}.</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.0, June 4, 2007
 * @see <a href="http://en.wikipedia.org/wiki/Measuring">
 *       Wikipedia: Measuring</a>
 *       
 * Modified on 8/14/2007 by Michael J. North to allow Groovy comparision operators
 * to work despite the following issues:
 * 
 *   http://jira.codehaus.org/browse/GROOVY-1888
 *   http://jira.codehaus.org/browse/GROOVY-1889
 *   
 */
public final class Amount<Q extends Quantity> implements
        Measurable<Q>, Field<Amount<?>>, Serializable, Immutable {

////////////////////////////////////////////////////////////////////////////////
// Note: In the future, Amount might be abstract (with more measure types)   // 
//       We don't provide public constructors, factory methods should be used.//
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Holds a dimensionless measure of zero (exact).
     */
    public static final Amount<Dimensionless> ZERO = new Amount<Dimensionless>();
    static {
        ZERO._unit = Unit.ONE;
        ZERO._isExact = true;
        ZERO._exactValue = 0L;
        ZERO._minimum = 0;
        ZERO._maximum = 0;        
    }

    /**
     * Holds a dimensionless measure of one (exact).
     */
    public static final Amount<Dimensionless> ONE = new Amount<Dimensionless>();
    static {
        ONE._unit = Unit.ONE;
        ONE._isExact = true;
        ONE._exactValue = 1L;
        ONE._minimum = 1.0;
        ONE._maximum = 1.0;        
    }
    
    /**
     * Holds the default XML representation for measures.
     * This representation consists of a <code>value</code>, 
     * an <code>unit</code> and an optional <code>error</code> attribute 
     * when the measure is not exact.
     * The unit attribute determinates the measurement type. For example:<pre>
     * &lt;Amount value="12" unit="µA"/&gt;</pre>
     * represents an electric current measurement.
     */
    protected static final XMLFormat<Amount> XML = new XMLFormat<Amount>(
            Amount.class) {
        
        @Override
        public Amount newInstance(Class<Amount> cls, InputElement xml) throws XMLStreamException {
            Unit unit = Unit.valueOf(xml.getAttribute("unit"));
            Amount<?> m = Amount.newInstance(unit);
            if (xml.getAttribute("error") == null) // Exact.
                return m.setExact(xml.getAttribute("value", 0L));
            m._isExact = false;
            double estimatedValue = xml.getAttribute("value", 0.0);
            double error = xml.getAttribute("error", 0.0);
            m._minimum = estimatedValue - error;
            m._maximum = estimatedValue + error;
            return m;
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement arg0, Amount arg1) throws XMLStreamException {
            // Nothing to do.
        }

        @Override
        public void write(Amount m, OutputElement xml) throws XMLStreamException {
            if (m._isExact) {
                xml.setAttribute("value", m._exactValue);
            } else {
                xml.setAttribute("value", m.getEstimatedValue());
                xml.setAttribute("error", m.getAbsoluteError());
            }
            xml.setAttribute("unit", m._unit.toString());
        }
    };

    /**
     * Returns the exact measure corresponding to the value stated in the 
     * specified unit.
     *
     * @param value the exact value stated in the specified unit.
     * @param unit the unit in which the value is stated.
     * @return the corresponding measure object.
     */
    public static <Q extends Quantity> Amount<Q> valueOf(long value,
            Unit<Q> unit) {
        Amount<Q> m = Amount.newInstance(unit);
        return m.setExact(value);
    }

    /**
     * Returns the measure corresponding to an approximate value 
     * (<code>double</code>) stated in the specified unit; 
     * the precision of the measure is assumed to be the 
     * <code>double</code> precision (64 bits IEEE 754 format).
     *
     * @param value the estimated value (± LSB) stated in the specified unit.
     * @param unit the unit in which the value is stated.
     * @return the corresponding measure object.
     */
    public static <Q extends Quantity> Amount<Q> valueOf(double value,
            Unit<Q> unit) {
        Amount<Q> m = Amount.newInstance(unit);
        m._isExact = false;
        double valInc = value * INCREMENT;
        double valDec = value * DECREMENT;
        m._minimum = (value < 0) ? valInc : valDec;
        m._maximum = (value < 0) ? valDec : valInc;
        return m;
    }

    /**
     * Returns the measure corresponding to the specified approximate value 
     * and measurement error, both stated in the specified unit.
     *
     * @param value the estimated amount (± error) stated in the specified unit.
     * @param error the measurement error (absolute).
     * @param unit the unit in which the amount and the error are stated.
     * @return the corresponding measure object.
     * @throws IllegalArgumentException if <code>error &lt; 0.0</code>
     */
    public static <Q extends Quantity> Amount<Q> valueOf(double value,
            double error, Unit<Q> unit) {
        if (error < 0)
            throw new IllegalArgumentException("error: " + error
                    + " is negative");
        Amount<Q> m = Amount.newInstance(unit);
        double min = value - error;
        double max = value + error;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the measure corresponding to the specified interval stated 
     * in the specified unit.
     *
     * @param  minimum the lower bound for the measure value.
     * @param  maximum the upper bound for the measure value.
     * @param unit the unit for both the minimum and maximum values.
     * @return the corresponding measure object.
     * @throws IllegalArgumentException if <code>minimum &gt; maximum</code>
     */
    public static <Q extends Quantity> Amount<Q> rangeOf(double minimum,
            double maximum, Unit<Q> unit) {
        if (minimum > maximum)
            throw new IllegalArgumentException("minimum: " + minimum
                    + " greater than maximum: " + maximum);
        Amount<Q> m = Amount.newInstance(unit);
        m._isExact = false;
        m._minimum = (minimum < 0) ? minimum * INCREMENT : minimum * DECREMENT;
        m._maximum = (maximum < 0) ? maximum * DECREMENT : maximum * INCREMENT;
        return m;
    }

    /**
     * Returns the measure represented by the specified character sequence.
     *
     * @param csq the character sequence.
     * @return <code>AmountFormat.getInstance().parse(csq)</code>
     */
    public static Amount<?> valueOf(CharSequence csq) {
        return AmountFormat.getInstance().parse(csq);
    }

    /**
     * Indicates if this measure is exact.
     */
    private boolean _isExact;

    /**
     * Holds the exact value (when exact) stated in this measure unit.
     */
    private long _exactValue;

    /**
     * Holds the minimum value stated in this measure unit.
     * For inexact measures: _minimum < _maximum 
     */
    private double _minimum;

    /**
     * Holds the maximum value stated in this measure unit.
     * For inexact measures: _maximum > _minimum 
     */
    private double _maximum;

    /**
     * Holds this measure unit. 
     */
    private Unit<Q> _unit;

    /**
     * Indicates if this measure amount is exact. An exact amount is 
     * guarantee exact only when stated in this measure unit
     * (e.g. <code>this.longValue()</code>); stating the amount
     * in any other unit may introduce conversion errors. 
     *
     * @return <code>true</code> if this measure is exact;
     *         <code>false</code> otherwise.
     */
    public boolean isExact() {
        return _isExact;
    }

    /**
     * Returns the unit in which the {@link #getEstimatedValue()
     * estimated value} and {@link #getAbsoluteError() absolute error}
     * are stated.
     *
     * @return the measure unit.
     */
    public Unit<Q> getUnit() {
        return _unit;
    }

    /**
     * Returns the exact value for this measure stated in this measure
     * {@link #getUnit unit}. 
     *
     * @return the exact measure value (<code>long</code>) stated 
     *         in this measure's {@link #getUnit unit}
     * @throws AmountException if this measure is not {@link #isExact()}
     */
    public long getExactValue() throws AmountException {
        if (!_isExact)
            throw new AmountException(
                    "Inexact measures don't have exact values");
        return _exactValue;
    }

    /**
     * Returns the estimated value for this measure stated in this measure
     * {@link #getUnit unit}. 
     *
     * @return the median value of the measure interval.
     */
    public double getEstimatedValue() {
        return (_isExact) ? _exactValue : (_minimum + _maximum) * 0.5;
    }

    /**
     * Returns the lower bound interval value for this measure stated in 
     * this measure unit.
     *
     * @return the minimum value.
     */
    public double getMinimumValue() {
        return _minimum;
    }

    /**
     * Returns the upper bound interval value for this measure stated in 
     * this measure unit.
     *
     * @return the maximum value.
     */
    public double getMaximumValue() {
        return _maximum;
    }

    /**
     * Returns the value by which the{@link #getEstimatedValue() estimated 
     * value} may differ from the true value (all stated in base units).
     *
     * @return the absolute error stated in base units.
     */
    public double getAbsoluteError() {
        return MathLib.abs(_maximum - _minimum) * 0.5;
    }

    /**
     * Returns the percentage by which the estimated amount may differ
     * from the true amount.
     *
     * @return the relative error.
     */
    public double getRelativeError() {
        return _isExact ? 0 : (_maximum - _minimum) / (_minimum + _maximum);
    }

    /**
     * Returns the measure equivalent to this measure but stated in the 
     * specified unit. The returned measure may not be exact even if this 
     * measure is exact due to conversion errors. 
     *
     * @param  unit the unit of the measure to be returned.
     * @return a measure equivalent to this measure but stated in the 
     *         specified unit.
     * @throws ConversionException if the current model does not allows for
     *         conversion to the specified unit.
     */
    @SuppressWarnings("unchecked")
    public <R extends Quantity> Amount<R> to(Unit<R> unit) {
        if ((_unit == unit) || this._unit.equals(unit))
            return (Amount<R>) this;
        UnitConverter cvtr = Amount.converterOf(_unit, unit);
        if (cvtr == UnitConverter.IDENTITY) { // No conversion necessary.
            Amount result = Amount.copyOf(this);
            result._unit = unit;
            return result;
        }
        if (cvtr instanceof RationalConverter) { // Exact conversion.
             RationalConverter rc = (RationalConverter) cvtr;
             Amount result = this.times(rc.getDividend()).divide(rc.getDivisor());
             result._unit = unit;
             return result;
        }
        Amount<R> result = Amount.newInstance(unit);
        double min = cvtr.convert(_minimum);
        double max = cvtr.convert(_maximum);
        result._isExact = false;
        result._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        result._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return result;
    }

    /**
     * Returns the opposite of this measure.
     *
     * @return <code>-this</code>.
     */
    public Amount<Q> opposite() {
        Amount<Q> m = Amount.newInstance(_unit);
        if ((_isExact) && (_exactValue != Long.MAX_VALUE))
            return m.setExact(-_exactValue);
        m._isExact = false;
        m._minimum = -_maximum;
        m._maximum = -_minimum;
        return m;
    }

    /**
     * Returns the sum of this measure with the one specified.
     *
     * @param  that the measure to be added.
     * @return <code>this + that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be added.
     */
    @SuppressWarnings("unchecked")
    public Amount<Q> plus(Amount that) throws ConversionException {
        final Amount thatToUnit = that.to(_unit);
        Amount<Q> m = Amount.newInstance(_unit);
        if (this._isExact && thatToUnit._isExact) {
            long sumLong = this._exactValue + thatToUnit._exactValue;
            double sumDouble = ((double) this._exactValue)
                    + ((double) thatToUnit._exactValue);
            if (sumLong == sumDouble)
                return m.setExact(sumLong);
        }
        double min = this._minimum + thatToUnit._minimum;
        double max = this._maximum + thatToUnit._maximum;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the difference of this measure with the one specified.
     *
     * @param  that the measure to be subtracted.
     * @return <code>this - that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be subtracted.
     */
    @SuppressWarnings("unchecked")
    public Amount<Q> minus(Amount that) throws ConversionException {
        final Amount thatToUnit = that.to(_unit);
        Amount<Q> m = Amount.newInstance(_unit);
        if (this._isExact && thatToUnit._isExact) {
            long diffLong = this._exactValue - thatToUnit._exactValue;
            double diffDouble = ((double) this._exactValue)
                    - ((double) thatToUnit._exactValue);
            if (diffLong == diffDouble)
                return m.setExact(diffLong);
        }
        double min = this._minimum - thatToUnit._maximum;
        double max = this._maximum - thatToUnit._minimum;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure scaled by the specified exact factor 
     * (dimensionless).
     *
     * @param  factor the scaling factor.
     * @return <code>this · factor</code>.
     */
    public Amount<Q> times(long factor) {
        Amount<Q> m = Amount.newInstance(_unit);
        if (this._isExact) {
            long productLong = _exactValue * factor;
            double productDouble = ((double) _exactValue) * factor;
            if (productLong == productDouble)
                return m.setExact(productLong);
        }
        m._isExact = false;
        m._minimum = (factor > 0) ? _minimum * factor : _maximum * factor;
        m._maximum = (factor > 0) ? _maximum * factor : _minimum * factor;
        return m;
    }

    /**
     * Returns this measure scaled by the specified approximate factor
     * (dimensionless).
     *
     * @param  factor the scaling factor.
     * @return <code>this · factor</code>.
     */
    public Amount<Q> times(double factor) {
        Amount<Q> m = Amount.newInstance(_unit);
        double min = (factor > 0) ? _minimum * factor : _maximum * factor;
        double max = (factor > 0) ? _maximum * factor : _minimum * factor;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the product of this measure with the one specified.
     *
     * @param  that the measure multiplier.
     * @return <code>this · that</code>.
     */
    @SuppressWarnings("unchecked")
    public Amount<? extends Quantity> times(Amount that) {
        Unit<?> unit = Amount.productOf(this._unit, that._unit);
        if (that._isExact) {
            Amount m = this.times(that._exactValue);
            m._unit = unit;
            return m;
        }
        Amount<Q> m = Amount.newInstance(unit);
        double min, max;
        if (_minimum >= 0) {
            if (that._minimum >= 0) {
                min = _minimum * that._minimum;
                max = _maximum * that._maximum;
            } else if (that._maximum < 0) {
                min = _maximum * that._minimum;
                max = _minimum * that._maximum;
            } else {
                min = _maximum * that._minimum;
                max = _maximum * that._maximum;
            }
        } else if (_maximum < 0) {
            if (that._minimum >= 0) {
                min = _minimum * that._maximum;
                max = _maximum * that._minimum;
            } else if (that._maximum < 0) {
                min = _maximum * that._maximum;
                max = _minimum * that._minimum;
            } else {
                min = _minimum * that._maximum;
                max = _minimum * that._minimum;
            }
        } else {
            if (that._minimum >= 0) {
                min = _minimum * that._maximum;
                max = _maximum * that._maximum;
            } else if (that._maximum < 0) {
                min = _maximum * that._minimum;
                max = _minimum * that._minimum;
            } else { // Both around zero.
                min = MathLib.min(_minimum * that._maximum, _maximum
                        * that._minimum);
                max = MathLib.max(_minimum * that._minimum, _maximum
                        * that._maximum);
            }
        }
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the multiplicative inverse of this measure.
     * If this measure is possibly zero, then the result is unbounded
     * (]-infinity, +infinity[).
     *
     * @return <code>1 / this</code>.
     */
    public Amount<? extends Quantity> inverse() {
        Amount<? extends Quantity> m = newInstance(Amount.inverseOf(_unit));
        if ((_isExact) && (_exactValue == 1L)) { // Only one exact inverse: one
            m.setExact(1L);
            return m;
        }
        m._isExact = false;
        if ((_minimum <= 0) && (_maximum >= 0)) { // Encompass zero.
            m._minimum = Double.NEGATIVE_INFINITY;
            m._maximum = Double.POSITIVE_INFINITY;
            return m;
        }
        double min = 1.0 / _maximum;
        double max = 1.0 / _minimum;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure divided by the specified exact divisor
     * (dimensionless).
     *
     * @param  divisor the exact divisor.
     * @return <code>this / divisor</code>.
     * @throws ArithmeticException if this measure is exact and the 
     *         specified divisor is zero.
     */
    public Amount<Q> divide(long divisor) {
        Amount<Q> m = Amount.newInstance(_unit);
        if (this._isExact) {
            long quotientLong = _exactValue / divisor;
            double quotientDouble = ((double) _exactValue) / divisor;
            if (quotientLong == quotientDouble)
                return m.setExact(quotientLong);
        }
        double min = (divisor > 0) ? _minimum / divisor : _maximum / divisor;
        double max = (divisor > 0) ? _maximum / divisor : _minimum / divisor;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure divided by the specified approximate divisor
     * (dimensionless).
     *
     * @param  divisor the approximated divisor.
     * @return <code>this / divisor</code>.
     */
    public Amount<Q> divide(double divisor) {
        Amount<Q> m = Amount.newInstance(_unit);
        double min = (divisor > 0) ? _minimum / divisor : _maximum / divisor;
        double max = (divisor > 0) ? _maximum / divisor : _minimum / divisor;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure divided by the one specified.
     *
     * @param  that the measure divisor.
     * @return <code>this / that</code>.
     */
    @SuppressWarnings("unchecked")
    public Amount<? extends Quantity> divide(Amount that) {
        if (that._isExact) {
            Amount m = this.divide(that._exactValue);
            m._unit = Amount.productOf(this._unit, Amount
                    .inverseOf(that._unit));
            return m;
        }
        return this.times(that.inverse());
    }

    /**
     * Returns the absolute value of this measure.
     *
     * @return  <code>|this|</code>.
     */
    public Amount<Q> abs() {
        return (_isExact) ? ((_exactValue < 0) ? this.opposite() : this)
                : (_minimum >= -_maximum) ? this : this.opposite();
    }

    /**
     * Returns the square root of this measure.
     *
     * @return <code>sqrt(this)</code>
     * 
     */
    public Amount<? extends Quantity> sqrt() {
        Amount<Q> m = Amount.newInstance(_unit.root(2));
        if (this._isExact) {
            double sqrtDouble = MathLib.sqrt(_exactValue);
            long sqrtLong = (long) sqrtDouble;
            if (sqrtLong * sqrtLong == _exactValue)
                return m.setExact(sqrtLong);
        }
        double min = MathLib.sqrt(_minimum);
        double max = MathLib.sqrt(_maximum);
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the given root of this measure.
     *
     * @param  n the root's order (n != 0).
     * @return the result of taking the given root of this quantity.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public Amount<? extends Quantity> root(int n) {
        if (n == 0)
            throw new ArithmeticException("Root's order of zero");
        if (n < 0)
            return this.root(-n).inverse();
        if (n == 2)
            return this.sqrt();
        Amount<Q> m = Amount.newInstance(_unit.root(n));
        if (this._isExact) {
            double rootDouble = MathLib.pow(_exactValue, 1.0 / n);
            long rootLong = (long) rootDouble;
            long thisLong = rootLong;
            for (int i = 1; i < n; i++) {
                thisLong *= rootLong;
            }
            if (thisLong == _exactValue)
                return m.setExact(rootLong);
        }
        double min = MathLib.pow(_minimum, 1.0 / n);
        double max = MathLib.pow(_maximum, 1.0 / n);
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure raised at the specified exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     */
    public Amount<? extends Quantity> pow(int exp) {
        if (exp < 0)
            return this.pow(-exp).inverse();
        if (exp == 0)
            return ONE;
        Amount<?> pow2 = this;
        Amount<?> result = null;
        while (exp >= 1) { // Iteration.
            if ((exp & 1) == 1) {
                result = (result == null) ? pow2 : result.times(pow2);
            }
            pow2 = pow2.times(pow2);
            exp >>>= 1;
        }
        return result;
    }
    
    /**
     * Compares this measure with the specified measurable object.
     *
     * @param  that the measure to compare with.
     * @return a negative integer, zero, or a positive integer as this measure
     *         is less than, equal to, or greater than that measurable.
     * @throws ConversionException if the current model does not allows for
     *         these measure to be compared.
     *         
     * Modified on 8/14/2007 by Michael J. North to allow Groovy comparision operators
     * to work despite the following issues:
     * 
     *   http://jira.codehaus.org/browse/GROOVY-1888
     *   http://jira.codehaus.org/browse/GROOVY-1889
     *   
     */
    @SuppressWarnings("unchecked")
//    public int compareTo(Measurable that) {
    public int compareTo(Object that) {

        double thatMinValue = Double.NaN;
        double thatMaxValue = Double.NaN;
        if (that instanceof Amount) {
        	Amount thatAmount = ((Amount) that); 
            thatMinValue = thatAmount.to(_unit).getMinimumValue();
            thatMaxValue = thatAmount.to(_unit).getMaximumValue();
        } else if (that instanceof Measurable) {
            thatMinValue = ((Measurable) that).doubleValue(_unit);
            thatMaxValue = thatMinValue;
        } else if (that instanceof BigDecimal) {
        	Amount thatAmount = Amount.valueOf(((BigDecimal) that).doubleValue(), Unit.ONE);
            thatMinValue = thatAmount.to(_unit).getMinimumValue();
            thatMaxValue = thatMinValue;
        }
        
        double thisMinValue = this.getMinimumValue();
        double thisMaxValue = this.getMaximumValue();
        
        if (Double.isNaN(thatMinValue) || Double.isNaN(thatMaxValue)) {

        	if (Double.isNaN(thisMinValue) || Double.isNaN(thisMaxValue)) {
            	return 0;
            } else {
            	return 1;
            }
        	
        } else if (Double.isNaN(thisMinValue) || Double.isNaN(thisMaxValue)) {
        	
        	return -1;
        	
        } else if (Double.isInfinite(thatMaxValue) && (thatMaxValue > 0.0)) {
        
        	if (Double.isInfinite(thisMaxValue) && (thisMaxValue > 0.0)) {
            	return 0;
            } else {
            	return 1;
            }
        	
        } else if (Double.isInfinite(thisMaxValue) && (thisMaxValue > 0.0)) {
            
        	return -1;
        	
        } else if (Double.isInfinite(thatMinValue) && (thatMinValue < 0.0)) {
            
        	if (Double.isInfinite(thisMinValue) && (thisMinValue < 0.0)) {
            	return 0;
            } else {
            	return -1;
            }
        	
        } else if (Double.isInfinite(thisMinValue) && (thisMinValue < 0.0)) {
            
        	return 1;
        	
        } else if (((thisMinValue <= thatMaxValue) && (thatMaxValue <= thisMaxValue)) ||
        		((thatMinValue <= thisMaxValue) && (thisMaxValue <= thatMaxValue))) {
        	
        	return 0;
        	
        } else if (thisMaxValue < thatMinValue) {
        	
        	return -1;
        	
        } else {
        	
        	return 1;
        	
        }
        
    }

    /**
     * Compares this measure against the specified object for strict 
     * equality (same value interval and same units).
     *
     * @param  that the object to compare with.
     * @return <code>true</code> if this measure is identical to that
     *         measure; <code>false</code> otherwise.
     *         
     * Modified on 8/14/2007 by Michael J. North to allow Groovy comparision operators
     * to work despite the following issues:
     * 
     *   http://jira.codehaus.org/browse/GROOVY-1888
     *   http://jira.codehaus.org/browse/GROOVY-1889
     *   
     */
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Amount) {
            return (this.compareTo((Amount) that) == 0);
        } else if (that instanceof Number) {
       		return (this.compareTo(Amount.valueOf(((Number) that).doubleValue(), Unit.ONE)) == 0);    		
        } else {
        	return false;
        }
    }

    /**
     * Returns the hash code for this measure.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        int h = Float.floatToIntBits((float) _minimum);
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    /**
     * Indicates if this measure approximates that measure.
     * Measures are considered approximately equals if their value intervals
     * overlaps. It should be noted that less accurate measurements are 
     * more likely to be approximately equals. It is therefore recommended
     * to ensure that the measurement error is not too large before testing
     * for approximate equality.
     *
     * @return <code>this ≅ that</code>
     */
    @SuppressWarnings("unchecked")
    public boolean approximates(Amount that) {
        Amount thatToUnit = that.to(_unit);
        return (this._maximum >= thatToUnit._minimum)
                && (thatToUnit._maximum >= this._minimum);
    }

    /**
     * Indicates if this measure is ordered before that measure
     * (independently of the measure unit).
     *
     * @return <code>this.compareTo(that) < 0</code>.
     */
    public boolean isLessThan(Amount<Q> that) {
        return this.compareTo(that) < 0;
    }

    /**
     * Indicates if this measure is ordered after that measure
     * (independently of the measure unit).
     *
     * @return <code>this.compareTo(that) > 0</code>.
     */
    public boolean isGreaterThan(Amount<Q> that) {
        return this.compareTo(that) > 0;
    }

    /**
     * Compares this measure with that measure ignoring the sign.
     *
     * @return <code>|this| > |that|</code>
     */
    public boolean isLargerThan(Amount<Q> that) {
        return this.abs().isGreaterThan(that.abs());
    }

    /**
     * Returns the text representation of this measure.
     *
     * @return <code>AmountFormat.getInstance().format(this)</code>
     */
    public Text toText() {
        return AmountFormat.getInstance().format(this);
    }

    /**
     * Returns the text representation of this amount as a 
     * <code>java.lang.String</code>.
     * 
     * @return <code>toText().toString()</code>
     */
    public final String toString() {
        return this.getEstimatedValue() + " " + this.getUnit();
    }

    // Implements Quantity.
    public double doubleValue(Unit<Q> unit) {
        return ((_unit == unit) || _unit.equals(unit)) ? this
                .getEstimatedValue() : this.to(unit).getEstimatedValue();
    }

    // Implements Quantity.
    public final long longValue(Unit<Q> unit) {
        if (!_unit.equals(unit))
            return this.to(unit).longValue(unit);
        if (_isExact)
            return _exactValue;
        double doubleValue = this.getEstimatedValue();
        if ((doubleValue >= Long.MIN_VALUE) && (doubleValue <= Long.MAX_VALUE))
            return Math.round(doubleValue);
        throw new ArithmeticException(doubleValue + " " + _unit
                + " cannot be represented as long");
    }

    ///////////////////
    // Lookup tables //
    ///////////////////

    static final FastMap<Unit, FastMap<Unit, Unit>> MULT_LOOKUP = new FastMap<Unit, FastMap<Unit, Unit>>(
            "UNITS_MULT_LOOKUP").setKeyComparator(FastComparator.DIRECT);

    static final FastMap<Unit, Unit> INV_LOOKUP = new FastMap<Unit, Unit>(
            "UNITS_INV_LOOKUP").setKeyComparator(FastComparator.DIRECT);

    static final FastMap<Unit, FastMap<Unit, UnitConverter>> CVTR_LOOKUP = new FastMap<Unit, FastMap<Unit, UnitConverter>>(
            "UNITS_CVTR_LOOKUP").setKeyComparator(FastComparator.DIRECT);

    private static Unit productOf(Unit left, Unit right) {
        FastMap<Unit, Unit> leftTable = MULT_LOOKUP.get(left);
        if (leftTable == null)
            return calculateProductOf(left, right);
        Unit result = leftTable.get(right);
        if (result == null)
            return calculateProductOf(left, right);
        return result;
    }

    private static synchronized Unit calculateProductOf(final Unit left, final Unit right) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(MULT_LOOKUP);
        memoryArea.executeInArea(new Runnable() {
            public void run() {
                FastMap<Unit, Unit> leftTable = MULT_LOOKUP.get(left);
                if (leftTable == null) {
                    leftTable = new FastMap<Unit, Unit>().setKeyComparator(
                            FastComparator.DIRECT);
                    MULT_LOOKUP.put(left, leftTable);
                }
                Unit result = leftTable.get(right);
                if (result == null) {
                    result = left.times(right);
                    leftTable.put(right, result);
                }
            }
        });
        return MULT_LOOKUP.get(left).get(right);
    }

    private static Unit inverseOf(Unit unit) {
        Unit inverse = INV_LOOKUP.get(unit);
        if (inverse == null)
            return calculateInverseOf(unit);
        return inverse;
    }

    private static synchronized Unit calculateInverseOf(final Unit unit) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(INV_LOOKUP);
        memoryArea.executeInArea(new Runnable() {
            public void run() {
                Unit inverse = INV_LOOKUP.get(unit);
                if (inverse == null) {
                    inverse = unit.inverse();
                    INV_LOOKUP.put(unit, inverse);
                }
            }
        });
        return INV_LOOKUP.get(unit);
    }

    private static UnitConverter converterOf(Unit left, Unit right) {
        FastMap<Unit, UnitConverter> leftTable = CVTR_LOOKUP.get(left);
        if (leftTable == null)
            return calculateConverterOf(left, right);
        UnitConverter result = leftTable.get(right);
        if (result == null)
            return calculateConverterOf(left, right);
        return result;
    }

    private static synchronized UnitConverter calculateConverterOf(final Unit left,
            final Unit right) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(CVTR_LOOKUP);
        memoryArea.executeInArea(new Runnable() {
            public void run() {
                FastMap<Unit, UnitConverter> leftTable = CVTR_LOOKUP.get(left);
                if (leftTable == null) {
                    leftTable = new FastMap<Unit, UnitConverter>()
                            .setKeyComparator(FastComparator.DIRECT);
                    synchronized (CVTR_LOOKUP) {
                        CVTR_LOOKUP.put(left, leftTable);
                    }
                }
                UnitConverter result = leftTable.get(right);
                if (result == null) {
                    result = left.getConverterTo(right);
                    synchronized (leftTable) {
                        leftTable.put(right, result);
                    }
                }
            }
        });
        return CVTR_LOOKUP.get(left).get(right);
    }
    
    public Amount<Q> copy() {
        Amount<Q> estimate = Amount.newInstance(_unit);
        estimate._isExact = _isExact;
        estimate._exactValue = _exactValue;
        estimate._minimum = _minimum;
        estimate._maximum = _maximum;
        return estimate;
    }
    
    //////////////////////
    // Factory Creation //
    //////////////////////

    @SuppressWarnings("unchecked")
    private static <Q extends Quantity> Amount<Q> newInstance(Unit unit) {
        Amount<Q> measure = FACTORY.object();
        measure._unit = unit;
        return measure;
    }

    @SuppressWarnings("unchecked")
    private static <Q extends Quantity> Amount<Q> copyOf(Amount original) {
        Amount<Q> measure = FACTORY.object();
        measure._exactValue = original._exactValue;
        measure._isExact = original._isExact;
        measure._maximum = original._maximum;
        measure._minimum = original._minimum;
        measure._unit = original._unit;
        return measure;
    }

    private static final ObjectFactory<Amount> FACTORY = new ObjectFactory<Amount>() {

        @Override
        protected Amount create() {
            return new Amount();
        }
    };

    private Amount() {
    }

    private Amount<Q> setExact(long exactValue) {
        _isExact = true;
        _exactValue = exactValue;
        double doubleValue = exactValue;
        if (doubleValue == exactValue) {
            _minimum = doubleValue;
            _maximum = doubleValue;
        } else {
            double valInc = exactValue * INCREMENT;
            double valDec = exactValue * DECREMENT;
            _minimum = (_exactValue < 0) ? valInc : valDec;
            _maximum = (_exactValue < 0) ? valDec : valInc;
        }
        return this;
    }

    static final double DOUBLE_RELATIVE_ERROR = MathLib.pow(2, -53);

    static final double DECREMENT = (1.0 - DOUBLE_RELATIVE_ERROR);

    static final double INCREMENT = (1.0 + DOUBLE_RELATIVE_ERROR);

    private static final long serialVersionUID = 1L;

   
}