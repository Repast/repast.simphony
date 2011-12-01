/**
 * 
 */
package repast.simphony.visualization.editedStyle;

import java.awt.Color;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;

/**
 * Edge style from xml serialized style info.
 * 
 * @author Nick Collier
 */
public class EditedEdgeStyleOGL2D implements EdgeStyleOGL2D {
  
  private EditedEdgeStyleData<Object> edgeData;
  
  public EditedEdgeStyleOGL2D(String fileName) {
    edgeData = EditedStyleUtils.getEdgeStyle(fileName);
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getLineWidth(repast.simphony.space.graph.RepastEdge)
   */
  @Override
  public int getLineWidth(RepastEdge<?> edge) {
    return (int)EditedStyleUtils.getSize(edgeData, edge);
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getColor(repast.simphony.space.graph.RepastEdge)
   */
  @Override
  public Color getColor(RepastEdge<?> edge) {
    return EditedStyleUtils.getColor(edgeData, edge);
  }

}
