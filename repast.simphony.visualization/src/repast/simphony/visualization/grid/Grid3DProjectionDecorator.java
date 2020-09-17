package repast.simphony.visualization.grid;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.visualization3D.Display3D;
import repast.simphony.visualization.visualization3D.ShapeFactory;

import org.jogamp.java3d.Group;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3f;

/**
 * Decorator for 2D and 3D grids when displayed in 3D. This can
 * draw grid lines in 2 or 3 dimensions.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class Grid3DProjectionDecorator extends AbstractProjectionDecorator<Grid> implements
				ProjectionDecorator3D<Grid> {

	public static final String GRID_3D_DECORATOR = Grid3DProjectionDecorator.class.getName();

	/**
	 * Initializes the decorator by adding a 2 or 3D grid
	 * to the parent.
	 *
	 * @param display
	 * @param parent  the parent to which the decoration should be added
	 */
	public void init(Display3D display, Group parent) {

		GridDimensions dims = projection.getDimensions();
		int[] dimensionsVals = dims.toIntArray(null);
		Shape3D grid = ShapeFactory.createGrid(unitSize, color, dimensionsVals);
		// translate the grid 1/2 cell size -x, -y, +z, so that objects
		// are in center of cells
		Transform3D gridTrans = new Transform3D();
		float offset = -unitSize / 2;
		gridTrans.setTranslation(new Vector3f(offset,  offset, -offset));
		TransformGroup grp = new TransformGroup(gridTrans);
		grp.addChild(grid);
		parent.addChild(grp);
		//parent.addChild(grid);
		grid.setPickable(false);
	}

	/**
	 * Updates the decorator. This does nothing in this implementation
	 */
	public void update() {
	}
}
