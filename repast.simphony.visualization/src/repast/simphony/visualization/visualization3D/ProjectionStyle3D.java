/*CopyrightHere*/
package repast.simphony.visualization.visualization3D;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.space.projection.Projection;

public interface ProjectionStyle3D<U extends Projection> {
	// TODO: get this out of here
	
	// TODO: probably don't need this method, but its in here to keep it sort of like the 2d
	BranchGroup createBranchGroup();

	// TODO: make this ProjectionStyleProperties, or ProjectionDisplayProperties
	void init(VisualizationProperties properties, TransformGroup projectionTransGroup,
			BranchGroup branchGroup, Display3D display, U projection);

	void update(BranchGroup branchGroup, U projection);
}
