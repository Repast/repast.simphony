/*CopyrightHere*/
package repast.simphony.visualization.visualization3D;

import javax.media.j3d.Shape3D;

import repast.simphony.visualization.IDisplayLayer;

public interface IDisplayLayer3D extends IDisplayLayer<VisualItem3D> {
	Object findObjsForItem(Shape3D shape);
}
