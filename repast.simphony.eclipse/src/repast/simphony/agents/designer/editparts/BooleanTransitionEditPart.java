/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the
 * Flow4J-Eclipse project nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package repast.simphony.agents.designer.editparts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import repast.simphony.agents.designer.model.BooleanTransitionModelPart;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class BooleanTransitionEditPart extends TransitionEditPart {

	Label sourceBooleanLabel = new Label();

	/**
	 * Adds boolean labels to the Transition.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Connection transition = (Connection) super.createFigure();

		ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(
				transition, false);
		sourceEndpointLocator.setVDistance(10);
		sourceEndpointLocator.setUDistance(10);
		// must correspont with the modelPart's initial value
		sourceBooleanLabel.setText(((BooleanTransitionModelPart) getModel())
				.getStringValue());
		transition.add(sourceBooleanLabel, sourceEndpointLocator);

		return transition;
	}

	/**
	 * Listens to changes in properties of the BooleanTransitionModelPart (like
	 * the contents being carried), and reflects is in the visuals.
	 * 
	 * @param event
	 *            Event notifying the change.
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		if (BooleanTransitionModelPart.PROP_BOOLEAN_VALUE.equals(property))
			sourceBooleanLabel
					.setText(((BooleanTransitionModelPart.Value) event
							.getNewValue()).toString());
		else
			super.propertyChange(event);
	}

}
