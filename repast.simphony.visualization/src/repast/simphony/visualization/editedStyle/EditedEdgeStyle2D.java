package repast.simphony.visualization.editedStyle;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Stroke;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.visualization2D.style.EdgeStyle2D;

/**
 * 
 * @author Eric Tatara
 *
 */
public class EditedEdgeStyle2D implements EdgeStyle2D {

	EditedEdgeStyleData<Object> innerStyle;

	public EditedEdgeStyle2D(String userStyleFile) {
		innerStyle = EditedStyleUtils.getEdgeStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedEdgeStyleData2D<Object>();
	}
	
	int directedTargetEnd = EdgeStyle2D.ARROW_HEAD;

	public Paint getPaint(RepastEdge edge) {
		return EditedStyleUtils.getColor(innerStyle, edge);
	}

	public Stroke getStroke(RepastEdge edge) {
		float[] dash;
		
		LineStyle lineStyle = innerStyle.getLineStyle();
		
		if (lineStyle == LineStyle.DASH)
			dash = new float[]{10f, 10f};
		else if (lineStyle == LineStyle.DASH_DOT)
			dash = new float[]{10f, 4f, 2f, 4f};
		else if (lineStyle == LineStyle.DASH_DASH_DOT)
			dash = new float[]{10f, 4f, 10f, 4f, 2f, 4f};
		else
      dash = null;
		
		float width = EditedStyleUtils.getSize(innerStyle, edge);
		
		return new BasicStroke(width, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	}

	public Paint getSourceEndPaint(RepastEdge edge) {
		// TODO
		return EditedStyleUtils.getColor(innerStyle, edge);
	}

	public int getSourceEndStyle(RepastEdge edge) {
		return EdgeStyle2D.NULL_HEAD;
	}

	public Paint getTargetEndPaint(RepastEdge edge) {
		// TODO
		return EditedStyleUtils.getColor(innerStyle, edge);
	}

	public int getTargetEndStyle(RepastEdge edge) {
		if (edge.isDirected()) {
			return directedTargetEnd;
		}
		return EdgeStyle2D.NULL_HEAD;
	}
}
