package repast.simphony.relogo.styles;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ListIterator;

import simphony.util.messages.MessageCenter;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class BoundingPPath extends PPath {

	private static MessageCenter msgCenter = MessageCenter
			.getMessageCenter(BoundingPPath.class);
	public final static String CHANGE_COLOR = "CHANGE_COLOR";

	public BoundingPPath(Rectangle2D shape) {
		super(shape, null);
	}

	public void setPaint(Paint newPaint) {
		ListIterator<PNode> li = this.getChildrenIterator();
		while (li.hasNext()) {
			PNode child = li.next();
			if (child.getBooleanAttribute(BoundingPPath.CHANGE_COLOR, false)) {
				if (child instanceof PPath) {
					if (((PPath) child).getStroke() != null) {
						((PPath) child).setStrokePaint(newPaint);
					}
				}
				child.setPaint(newPaint);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.umd.cs.piccolo.PNode#setBounds(java.awt.geom.Rectangle2D)
	 */
	@Override
	public boolean setBounds(Rectangle2D arg0) {
		// msgCenter.debug("in the setBounds method of BoundingPPath");
		double newWidth = arg0.getWidth();
		double newHeight = arg0.getHeight();
		Rectangle2D oldBounds = this.getBounds();
		double oldWidth = oldBounds.getWidth();
		double oldHeight = oldBounds.getHeight();
		double widthRatio = newWidth / oldWidth;
		double heightRatio = newHeight / oldHeight;
		boolean returnBoolean = super.setBounds(arg0);
		ListIterator<PNode> li = this.getChildrenIterator();
		while (li.hasNext()) {
			PNode child = li.next();
			child.setTransform(AffineTransform.getScaleInstance(widthRatio,
					heightRatio));
		}
		return returnBoolean;
	}

}
