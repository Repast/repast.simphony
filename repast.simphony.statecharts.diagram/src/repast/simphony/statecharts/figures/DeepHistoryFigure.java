/**
 * Copyright (c) 2010 committers of YAKINDU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * 	committers of YAKINDU - initial API and implementation
 * 
 */
package repast.simphony.statecharts.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;

public class DeepHistoryFigure extends Ellipse {

  private static final double OFFSET = 0.05;
  private static final double HEIGHT_RATIO = 0.25;
  private static final double WIDTH_RATIO = 0.25;

  public DeepHistoryFigure() {
    this.setSize(new Dimension(15, 15));
    this.setForegroundColor(org.eclipse.draw2d.ColorConstants.black);
    this.setBackgroundColor(org.eclipse.draw2d.ColorConstants.black);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.draw2d.Ellipse#fillShape(org.eclipse.draw2d.Graphics)
   */
  @Override
  protected void fillShape(Graphics graphics) {
    // work around for something changing this to white
    setBackgroundColor(ColorConstants.black);
    super.fillShape(graphics);
  }

  @Override
  protected void outlineShape(Graphics graphics) {
    setForegroundColor(ColorConstants.black);
    super.outlineShape(graphics);

    // draw the 'H' letter

    graphics.pushState();
    graphics.setForegroundColor(org.eclipse.draw2d.ColorConstants.white);

    graphics.drawLine(
        bounds.getCenter().getTranslated((int) (-bounds.width * WIDTH_RATIO),
            (int) (-bounds.height * HEIGHT_RATIO)),
        bounds.getCenter().getTranslated((int) (-bounds.width * WIDTH_RATIO),
            (int) (bounds.height * HEIGHT_RATIO)));
    graphics.drawLine(
        bounds.getCenter().getTranslated((int) (bounds.width * OFFSET),
            (int) (-bounds.height * HEIGHT_RATIO)),
        bounds.getCenter().getTranslated((int) (bounds.width * OFFSET),
            (int) (bounds.height * HEIGHT_RATIO)));
    graphics.drawLine(bounds.getCenter().getTranslated((int) (-bounds.width * WIDTH_RATIO), 0),
        bounds.getCenter().getTranslated((int) (bounds.width * OFFSET), 0));

    // draw the '*' character
    graphics.drawLine(
        bounds.getCenter().getTranslated((int) (bounds.width * WIDTH_RATIO),
            (int) (-bounds.height * HEIGHT_RATIO)),
        bounds.getCenter().getTranslated((int) (bounds.width * WIDTH_RATIO),
            (int) (-bounds.height * OFFSET)));
    graphics.drawLine(
        bounds.getCenter()
            .getTranslated((int) (bounds.width * 0.15), (int) (-bounds.height * 0.20)), bounds
            .getCenter().getTranslated((int) (bounds.width * 0.35), (int) (-bounds.height * 0.10)));
    graphics.drawLine(
        bounds.getCenter()
            .getTranslated((int) (bounds.width * 0.35), (int) (-bounds.height * 0.20)), bounds
            .getCenter().getTranslated((int) (bounds.width * 0.15), (int) (-bounds.height * 0.10)));
    graphics.popState();
  }
}