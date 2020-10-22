/*
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 */
package repast.simphony.visualization.visualization3D;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnAWTEvent;
import org.jogamp.java3d.WakeupOnBehaviorPost;
import org.jogamp.java3d.utils.behaviors.mouse.MouseBehavior;
import org.jogamp.java3d.utils.behaviors.mouse.MouseBehaviorCallback;
import org.jogamp.vecmath.Vector3d;

// ***** Updated by E. Tatara to work mouse event bit mask in JDK 9+ *****

/**
 * MouseTranslate is a Java3D behavior object that lets users control the
 * translation (X, Y) of an object via a mouse drag motion with the third
 * mouse button (alt-click on PC). See MouseRotate for similar usage info.
 */

public class MouseTranslate extends MouseBehavior {

	double x_factor = .02;
	double y_factor = .02;
	Vector3d translation = new Vector3d();

	private MouseBehaviorCallback callback = null;

	/**
	 * Creates a mouse translate behavior given the transform group.
	 * @param transformGroup The transformGroup to operate on.
	 */
	public MouseTranslate(TransformGroup transformGroup) {
		super(transformGroup);
	}

	/**
	 * Creates a default translate behavior.
	 */
	public MouseTranslate(){
		super(0);
	}

	/**
	 * Creates a translate behavior.
	 * Note that this behavior still needs a transform
	 * group to work on (use setTransformGroup(tg)) and
	 * the transform group must add this behavior.
	 * @param flags
	 */
	public MouseTranslate(int flags) {
		super(flags);
	}

	/**
	 * Creates a translate behavior that uses AWT listeners and behavior
	 * posts rather than WakeupOnAWTEvent.  The behavior is added to the
	 * specified Component. A null component can be passed to specify
	 * the behavior should use listeners.  Components can then be added
	 * to the behavior with the addListener(Component c) method.
	 * @param c The Component to add the MouseListener
	 * and MouseMotionListener to.
	 * @since Java 3D 1.2.1
	 */
	public MouseTranslate(Component c) {
		super(c, 0);
	}

	/**
	 * Creates a translate behavior that uses AWT listeners and behavior
	 * posts rather than WakeupOnAWTEvent.  The behaviors is added to
	 * the specified Component and works on the given TransformGroup.
	 * A null component can be passed to specify the behavior should use
	 * listeners.  Components can then be added to the behavior with the
	 * addListener(Component c) method.
	 * @param c The Component to add the MouseListener and
	 * MouseMotionListener to.
	 * @param transformGroup The TransformGroup to operate on.
	 * @since Java 3D 1.2.1
	 */
	public MouseTranslate(Component c, TransformGroup transformGroup) {
		super(c, transformGroup);
	}

	/**
	 * Creates a translate behavior that uses AWT listeners and behavior
	 * posts rather than WakeupOnAWTEvent.  The behavior is added to the
	 * specified Component.  A null component can be passed to specify
	 * the behavior should use listeners.  Components can then be added to
	 * the behavior with the addListener(Component c) method.
	 * Note that this behavior still needs a transform
	 * group to work on (use setTransformGroup(tg)) and the transform
	 * group must add this behavior.
	 * @param flags interesting flags (wakeup conditions).
	 * @since Java 3D 1.2.1
	 */
	public MouseTranslate(Component c, int flags) {
		super(c, flags);
	}

	@Override
	public void initialize() {
		super.initialize();
		if ((flags & INVERT_INPUT) == INVERT_INPUT) {
			invert = true;
			x_factor *= -1;
			y_factor *= -1;
		}
	}

	/**
	 * Return the x-axis movement multipler.
	 **/
	public double getXFactor() {
		return x_factor;
	}

	/**
	 * Return the y-axis movement multipler.
	 **/
	public double getYFactor() {
		return y_factor;
	}

	/**
	 * Set the x-axis amd y-axis movement multipler with factor.
	 **/
	public void setFactor( double factor) {
		x_factor = y_factor = factor;
	}

	/**
	 * Set the x-axis amd y-axis movement multipler with xFactor and yFactor
	 * respectively.
	 **/
	public void setFactor( double xFactor, double yFactor) {
		x_factor = xFactor;
		y_factor = yFactor;
	}

	@Override
	public void processStimulus (Iterator<WakeupCriterion> criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] events;
		MouseEvent evt;
		// 	int id;
		// 	int dx, dy;

		while (criteria.hasNext()) {
			wakeup = criteria.next();

			if (wakeup instanceof WakeupOnAWTEvent) {
				events = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
				if (events.length > 0) {
					evt = (MouseEvent) events[events.length-1];
					doProcess(evt);
				}
			}

			else if (wakeup instanceof WakeupOnBehaviorPost) {
				while (true) {
					// access to the queue must be synchronized
					synchronized (mouseq) {
						if (mouseq.isEmpty()) break;
						evt = (MouseEvent)mouseq.remove(0);
						// consolodate MOUSE_DRAG events
						while ((evt.getID() == MouseEvent.MOUSE_DRAGGED) &&
								!mouseq.isEmpty() &&
								(((MouseEvent)mouseq.get(0)).getID() ==
								MouseEvent.MOUSE_DRAGGED)) {
							evt = (MouseEvent)mouseq.remove(0);
						}
					}
					doProcess(evt);
				}
			}

		}
		wakeupOn(mouseCriterion);
	}

	void doProcess(MouseEvent evt) {
		int id;
		int dx, dy;

		processMouseEvent(evt);

		if (((buttonPress)&&((flags & MANUAL_WAKEUP) == 0)) || ((wakeUp)&&((flags & MANUAL_WAKEUP) != 0))){
			id = evt.getID();

			// JDK9+ requires use of MouseEvent masks for proper Button3 detection
			// JDK 8 uses MouseEvent.isMetaDown for Button3 detection
			// The condition below should work for either event type

			// If Button 3 pressed
			int button3_mask = MouseEvent.BUTTON3_DOWN_MASK;
			boolean do_translate = false;

			if ((evt.getModifiersEx() & button3_mask) == button3_mask) {
				do_translate = true;
			}
			if ((id == MouseEvent.MOUSE_DRAGGED) && (do_translate)) {

				x = evt.getX();
				y = evt.getY();

				dx = x - x_last;
				dy = y - y_last;

				if ((!reset) && ((Math.abs(dy) < 50) && (Math.abs(dx) < 50))) {
					//System.out.println("dx " + dx + " dy " + dy);
					transformGroup.getTransform(currXform);

					translation.x = dx*x_factor;
					translation.y = -dy*y_factor;

					transformX.set(translation);

					if (invert) {
						currXform.mul(currXform, transformX);
					} else {
						currXform.mul(transformX, currXform);
					}

					transformGroup.setTransform(currXform);

					transformChanged( currXform );

					if (callback!=null)
						callback.transformChanged( MouseBehaviorCallback.TRANSLATE,
								currXform );

				}
				else {
					reset = false;
				}
				x_last = x;
				y_last = y;
			}
			else if (id == MouseEvent.MOUSE_PRESSED) {
				x_last = evt.getX();
				y_last = evt.getY();
			}
		}
	}

	/**
	 * Users can overload this method  which is called every time
	 * the Behavior updates the transform
	 *
	 * Default implementation does nothing
	 */
	public void transformChanged( Transform3D transform ) {
	}

	/**
	 * The transformChanged method in the callback class will
	 * be called every time the transform is updated
	 */
	public void setupCallback( MouseBehaviorCallback callback ) {
		this.callback = callback;
	}
}

