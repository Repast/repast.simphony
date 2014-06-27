/*CopyrightHere*/
package repast.simphony.visualization.visualization2D;

import java.util.Collection;

import org.piccolo2d.PNode;

import repast.simphony.visualization.IDisplayLayer;

/**
 *  @deprecated replaced by ogl 2D
 */
public interface IDisplayLayer2D extends IDisplayLayer<PNode> {
	Collection<Object> findObjsForItems(Collection<PNode> nodes);
}
