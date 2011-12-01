package repast.simphony.visualization.continuous;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.Display3D;
import repast.simphony.visualization.visualization3D.ShapeFactory;

public class Continuous3DProjectionDecorator extends AbstractProjectionDecorator<ContinuousSpace>
				implements ProjectionDecorator3D<ContinuousSpace> {

	public void init(Display3D display, Group parentGroup) {
		ContinuousSpace space = (ContinuousSpace) projection;
		float width = (float) space.getDimensions().getWidth() * unitSize;
		float height = (float) space.getDimensions().getHeight() * unitSize;
		float depth = (float) space.getDimensions().getDimension(2) * unitSize;

		if (width == Float.NEGATIVE_INFINITY || height == Float.NEGATIVE_INFINITY
						|| depth == Float.NEGATIVE_INFINITY) {
			Shape3D axes = ShapeFactory.createAxes(width, height, depth, 1000, color, "axes");
			axes.setPickable(false);
			parentGroup.addChild(axes);
		} else {
			Shape3D boundingBox = ShapeFactory.createWireframeBox(width, height, depth, color,
							"boundingbox");
			Appearance app = AppearanceFactory.setColoredAppearance(
							boundingBox.getAppearance(), color);
			boundingBox.setAppearance(app);
			boundingBox.setPickable(false);
			parentGroup.addChild(boundingBox);
		}
	}

	public void update() {}
}
