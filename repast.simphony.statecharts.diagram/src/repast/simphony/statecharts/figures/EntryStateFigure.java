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
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

public class EntryStateFigure extends Shape {

  /**
   * @generated
   */
  public EntryStateFigure() {
    this.setOutline(false);
    this.setLineWidth(0);
    this.setForegroundColor(ColorConstants.black);
    this.setBackgroundColor(ColorConstants.black);
    this.setPreferredSize(new Dimension(25, 25));
    this.setMaximumSize(new Dimension(25, 25));
    this.setMinimumSize(new Dimension(25, 25));
    
  }

  @Override
  protected void fillShape(Graphics graphics) {
    // work around for something changing this to white
    setBackgroundColor(ColorConstants.black);
    Rectangle bounds = getBounds();
    Rectangle circleBounds = new Rectangle(bounds.x + 3, bounds.y + 5, 14, 14);
    graphics.fillOval(circleBounds);
    
    Rectangle barBounds = new Rectangle(bounds.x + 1, bounds.y, bounds.width - 2, 3);
    graphics.fillRectangle(barBounds);
    
  }

  @Override
  protected void outlineShape(Graphics graphics) {
    // workaround for something changing this to gray
    setForegroundColor(ColorConstants.black);
    Rectangle bounds = getBounds();
    Rectangle circleBounds = new Rectangle(bounds.x + 3, bounds.y + 5, 14, 14);
    graphics.drawOval(circleBounds);
    
    Rectangle barBounds = new Rectangle(bounds.x + 1, bounds.y, bounds.width - 2, 3);
    graphics.drawRectangle(barBounds);

  }
}