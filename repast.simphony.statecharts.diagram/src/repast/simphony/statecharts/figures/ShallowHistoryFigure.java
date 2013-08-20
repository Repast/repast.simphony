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

public class ShallowHistoryFigure extends Ellipse {

  private static final double HEIGHT_RATIO = 0.3;
  private static final double WIDTH_RATIO = 0.2;

  public ShallowHistoryFigure() {
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

  /**
   * @see org.eclipse.draw2d.Ellipse#outlineShape(org.eclipse.draw2d.Graphics)
   */
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
        bounds.getCenter().getTranslated((int) (bounds.width * WIDTH_RATIO),
            (int) (-bounds.height * HEIGHT_RATIO)),
        bounds.getCenter().getTranslated((int) (bounds.width * WIDTH_RATIO),
            (int) (bounds.height * HEIGHT_RATIO)));
    graphics.drawLine(bounds.getCenter().getTranslated((int) (-bounds.width * WIDTH_RATIO), 0),
        bounds.getCenter().getTranslated((int) (bounds.width * WIDTH_RATIO), 0));
    graphics.popState();
  }
}