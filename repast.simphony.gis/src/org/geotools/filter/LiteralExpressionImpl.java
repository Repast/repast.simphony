package org.geotools.filter;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.ExpressionVisitor;

import com.vividsolutions.jts.geom.Geometry;

/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2006, GeoTools Project Managment Committee (PMC)
 *        
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */


/**
 * Defines an expression that holds a literal for return.
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL: http://svn.geotools.org/geotools/tags/2.3.0/module/main/src/org/geotools/filter/LiteralExpressionImpl.java $
 * @version $Id: LiteralExpressionImpl.java,v 1.2 2007/04/18 19:25:53 howe Exp $
 */
public class LiteralExpressionImpl extends DefaultExpression
    implements LiteralExpression {
	
    /** Holds a reference to the literal. */
    private Object literal = null;

    /**
     * Constructor with literal.
     */
    protected LiteralExpressionImpl() {
    }

    /**
     * Constructor with literal.
     *
     * @param literal The literal to store inside this expression.
     *
     * @throws IllegalFilterException This literal type is not in scope.
     */
    protected LiteralExpressionImpl(Object literal)
        throws IllegalFilterException {
        this.setLiteral(literal);
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience
     * one for integers an Integer object will be constructed, and no
     * IllegalFilterException can ever be thrown.
     *
     * @param value The integer to store inside this expression.
     */
    protected LiteralExpressionImpl(int value) {
        try {
            this.setLiteral(new Integer(value));
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and Integer is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Integers");
        }
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience
     * one for doubles an Double object will be constructed, and no
     * IllegalFilterException can ever be thrown.
     *
     * @param value The double to store inside this expression.
     */
    protected LiteralExpressionImpl(double value) {
        try {
            this.setLiteral(new Double(value));
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and Double is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Doubles");
        }
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience
     * one for doubles an Double object will be constructed, and no
     * IllegalFilterException can ever be thrown.
     *
     * @param value The double to store inside this expression.
     */
    protected LiteralExpressionImpl(String value) {
        try {
            this.setLiteral(value);
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and String is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Strings");
        }
    }

    /**
     * Returns the literal type.
     *
     * @return the short representation of the expression type.
     */
    public short getType() {
        return expressionType;
    }

    /**
     * This method calls {@link #setValue(Object)}.
     * 
     * @deprecated use {@link #setValue(Object)}.
     * 
     */
    public final void setLiteral(Object literal) throws IllegalFilterException {
        setValue(literal);
    }

    /**
     * This method calls {@link #getValue()}.
     * 
     * @deprecated use {@link #getValue()}.
     * 
     */
    public final Object getLiteral() {
        return getValue();
    }

    /**
     * Retrieves the literal of this expression.
     *
     * @return the literal held by this expression.
     * 
     */
    public Object getValue() {
    	return literal;
    }
    
    /**
     * Sets the literal.
     *
     * @param literal The literal to store inside this expression.
     *
     * @throws IllegalFilterException This literal type is not in scope.
     */
    public final void setValue(Object literal) {
	    if (literal instanceof Double) {
            expressionType = LITERAL_DOUBLE;
        } else if (literal instanceof Integer) {
            expressionType = LITERAL_INTEGER;
		     } else if (literal instanceof Long) {
            expressionType = ExpressionType.LITERAL_LONG;
		     } else if (literal instanceof Float) {
            expressionType = ExpressionType.LITERAL_FLOAT;
        } else if (literal instanceof String) {
            expressionType = LITERAL_STRING;
        } else if (literal instanceof Geometry) {
            expressionType = LITERAL_GEOMETRY;
        } else {
        	
            
    		throw new IllegalFilterException(
	            "Attempted to add a literal with non-supported type "
	            + "(ie. not Double, Integer, String).")
        ;
        }

        this.literal = literal;
    }
    
    /**
     * Gets the value of this literal.
     *
     * @param feature Required by the interface but not used.
     *
     * @return the literal held by this expression.  Ignores the passed in
     *         feature.
     *
     * @throws IllegalArgumentException Feature does not match declared schema.
     */
    public Object evaluate(SimpleFeature feature)
    	throws IllegalArgumentException {
    	return literal;
    }

    /**
     * Return this filter as a string.
     *
     * @return String representation of this geometry filter.
     */
    public String toString() {
        return literal == null ? "NULL" : literal.toString();
    }

    /**
     * Compares this filter to the specified object.  Returns true  if the
     * passed in object is the same as this expression.  Checks  to make sure
     * the expression types are the same as well as the literals.
     *
     * @param obj - the object to compare this ExpressionLiteral against.
     *
     * @return true if specified object is equal to this expression; false
     *         otherwise.
     *
     * @task REVISIT: missmatched types now considered not equal. This may be a
     *       problem when comparing Doubles and Integers
     */
    public boolean equals(Object obj) {
        if (obj instanceof LiteralExpressionImpl) {
            LiteralExpressionImpl expLit = (LiteralExpressionImpl) obj;
            boolean isEqual = (expLit.getType() == this.expressionType);

            if (!isEqual) {
                return false;
            }

            if ((expLit == null) && (this.literal == null)) {
                return true;
            }

            if (expressionType == LITERAL_GEOMETRY) {
                return ((Geometry) this.literal).equals((Geometry) expLit
                    .getLiteral());
            } else if (expressionType == LITERAL_INTEGER) {
                return ((Integer) this.literal).equals((Integer) expLit
                    .getLiteral());
            } else if (expressionType == LITERAL_STRING) {
                return ((String) this.literal).equals((String) expLit
                    .getLiteral());
            } else if (expressionType == LITERAL_DOUBLE) {
                return ((Double) this.literal).equals((Double) expLit
                    .getLiteral());
	             } else if (expressionType == ExpressionType.LITERAL_LONG) {
                return ((Long) this.literal).equals((Long) expLit
                    .getLiteral());
	             } else if (expressionType == ExpressionType.LITERAL_FLOAT) {
                return ((Float) this.literal).equals((Float) expLit
                    .getLiteral());
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return the hash code for this literal expression
     */
    public int hashCode() {
        int result = 17;

        result = (37 * result) + ((literal == null) ? 0 : literal.hashCode());
        result = (37 * result) + expressionType;

        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance.
     * Typicaly used by Filter decoders, but may also be used by any thing
     * which needs infomration from filter structure. Implementations should
     * always call: visitor.visit(this); It is importatant that this is not
     * left to a parent class unless the parents API is identical.
     *
     * @param visitor The visitor which requires access to this filter, the
     *        method must call visitor.visit(this);
     */
    public Object accept(ExpressionVisitor visitor, Object extraData) {
    	return visitor.visit(this,extraData);
    }
}
