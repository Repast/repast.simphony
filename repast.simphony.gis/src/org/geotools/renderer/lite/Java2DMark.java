/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2004-2006, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D.Double;
import java.util.logging.Logger;


/**
 * Utility class used to get well known marks
 *
 * @author Ian Turton
 * @source $URL: http://svn.geotools.org/geotools/tags/2.3.0/module/render/src/org/geotools/renderer/lite/Java2DMark.java $
 * @version $Id: Java2DMark.java,v 1.1 2007/04/26 18:32:33 howe Exp $
 */
public class Java2DMark {
    /** The logger for the rendering module. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.geotools.rendering");

    /** Cross general path */
    private static GeneralPath cross;

    /** Star general path */
    private static GeneralPath star;

    /** Triangle general path */
    private static GeneralPath triangle;

    /** Arrow general path */
    private static GeneralPath arrow;

    /** X general path */
    private static Shape X;
    
    /** hatch path */
    static GeneralPath hatch;

    static {
        cross = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        cross.moveTo(0.5f, 0.125f);
        cross.lineTo(0.125f, 0.125f);
        cross.lineTo(0.125f, 0.5f);
        cross.lineTo(-0.125f, 0.5f);
        cross.lineTo(-0.125f, 0.125f);
        cross.lineTo(-0.5f, 0.125f);
        cross.lineTo(-0.5f, -0.125f);
        cross.lineTo(-0.125f, -0.125f);
        cross.lineTo(-0.125f, -0.5f);
        cross.lineTo(0.125f, -0.5f);
        cross.lineTo(0.125f, -0.125f);
        cross.lineTo(0.5f, -0.125f);
        cross.lineTo(0.5f, 0.125f);

        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / 4.0);
        X = cross.createTransformedShape(at);
        star = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        star.moveTo(0.191f, 0.0f);
        star.lineTo(0.25f, 0.344f);
        star.lineTo(0.0f, 0.588f);
        star.lineTo(0.346f, 0.638f);
        star.lineTo(0.5f, 0.951f);
        star.lineTo(0.654f, 0.638f);
        star.lineTo(1.0f, 0.588f); // max = 7.887
        star.lineTo(0.75f, 0.344f);
        star.lineTo(0.89f, 0f);
        star.lineTo(0.5f, 0.162f);
        star.lineTo(0.191f, 0.0f);
        at = new AffineTransform();
        at.translate(-.5, -.5);
        star.transform(at);
        triangle = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        triangle.moveTo(0f, 1f);
        triangle.lineTo(0.866f, -.5f);
        triangle.lineTo(-0.866f, -.5f);
        triangle.lineTo(0f, 1f);
        at = new AffineTransform();

        at.translate(0, -.25);
        at.scale(.5, .5);

        triangle.transform(at);

        arrow = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        arrow.moveTo(0f, -.5f);
        arrow.lineTo(.5f, 0f);
        arrow.lineTo(0f, .5f);
        arrow.lineTo(0f, .1f);
        arrow.lineTo(-.5f, .1f);
        arrow.lineTo(-.5f, -.1f);
        arrow.lineTo(0f, -.1f);
        arrow.lineTo(0f, -.5f);

        hatch = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        hatch.moveTo(.55f,.57f);
        hatch.lineTo(.52f,.57f);
        hatch.lineTo(-.57f,-.52f);
        hatch.lineTo(-.57f,-.57f);
        hatch.lineTo(-.52f, -.57f);
        hatch.lineTo(.57f, .52f);
        hatch.lineTo(.57f,.57f);
                
        hatch.moveTo(.57f,-.49f);
        hatch.lineTo(.49f, -.57f);
        hatch.lineTo(.57f,-.57f);
        hatch.lineTo(.57f,-.49f);
                
        hatch.moveTo(-.57f,.5f);
        hatch.lineTo(-.5f, .57f);
        hatch.lineTo(-.57f,.57f);
        hatch.lineTo(-.57f,.5f);
    }

    /**
     * Java2DMark is an utility class, instantiation is not allowed
     */
    private Java2DMark() {
    }

    /**
     * Returns the shape representing the well known name passed
     *
     * @param wellKnownName the name of a well known shape
     *
     * @return the shape representing the well known name passed
     */
    public static Shape getWellKnownMark(String wellKnownName) {
        LOGGER.finer("fetching mark of name " + wellKnownName);

        if (wellKnownName.equalsIgnoreCase("cross")) {
            LOGGER.finer("returning cross");

            return cross;
        }

        if (wellKnownName.equalsIgnoreCase("circle")) {
            LOGGER.finer("returning circle");

            return new java.awt.geom.Ellipse2D.Double(-.5, -.5, 1., 1.);
        }

        if (wellKnownName.equalsIgnoreCase("triangle")) {
            LOGGER.finer("returning triangle");

            return triangle;
        }

        if (wellKnownName.equalsIgnoreCase("X")) {
            LOGGER.finer("returning X");

            return X;
        }

        if (wellKnownName.equalsIgnoreCase("star")) {
            LOGGER.finer("returning star");

            return star;
        }

        if (wellKnownName.equalsIgnoreCase("arrow")) {
            LOGGER.finer("returning arrow");

            return arrow;
        }
        
        if (wellKnownName.equalsIgnoreCase("hatch")) {
        	LOGGER.finer("returning hatch");
        	 
        	return hatch;
        }

        // failing that return a square?
        LOGGER.finer("returning square");

        return new Double(-.5, -.5, 1., 1.);
    }
}
