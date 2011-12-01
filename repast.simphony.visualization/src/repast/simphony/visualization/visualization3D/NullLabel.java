package repast.simphony.visualization.visualization3D;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.vecmath.Point3f;

import repast.simphony.visualization.visualization3D.style.Style3D;

/**
 * Null implementation of Label.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NullLabel implements Label {

	public BranchGroup getBranchGroup(Point3f center, Group object) {
		return null;
	}

	public Color getColor() {
		return null;
	}

	public Font getFont() {
		return null;
	}

	public String getLabel() {
		return null;
	}

	public float getOffset() {
		return 0;
	}

	public Style3D.LabelPosition getPosition() {
		return Style3D.LabelPosition.NORTH;
	}

	public void setColor(Color color) {

	}

	public void setFont(Font font) {

	}

	public void setLabel(String label) {

	}

	public void setOffset(float offset) {

	}

	public void setPosition(Style3D.LabelPosition position) {
		
	}
}

