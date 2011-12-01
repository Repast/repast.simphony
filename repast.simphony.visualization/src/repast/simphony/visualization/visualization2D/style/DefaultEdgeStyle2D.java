package repast.simphony.visualization.visualization2D.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import repast.simphony.space.graph.RepastEdge;

/**
 *  @deprecated replaced by ogl 2D
 */
public class DefaultEdgeStyle2D implements EdgeStyle2D {

	Paint color = Color.BLUE;

	Stroke stroke = new BasicStroke(1);

	Paint endColor = color;

	int directedTargetEnd = EdgeStyle2D.ARROW_HEAD;

	public DefaultEdgeStyle2D() {
		super();
	}

	public Paint getPaint(RepastEdge edge) {
		return color;
	}

	public Stroke getStroke(RepastEdge edge) {
		return stroke;
	}

	public void setPaint(Paint paint) {
		color = paint;
    endColor = paint;
  }

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	public Paint getSourceEndPaint(RepastEdge edge) {
		return this.endColor;
	}

	public int getSourceEndStyle(RepastEdge edge) {
		return EdgeStyle2D.NULL_HEAD;
	}

	public Paint getTargetEndPaint(RepastEdge edge) {
		return this.endColor;
	}

	public int getTargetEndStyle(RepastEdge edge) {
		if (edge.isDirected()) {
			return directedTargetEnd;
		}
		return EdgeStyle2D.NULL_HEAD;
	}

}
