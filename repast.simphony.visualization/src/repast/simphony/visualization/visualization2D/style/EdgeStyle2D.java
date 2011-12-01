package repast.simphony.visualization.visualization2D.style;

import java.awt.Paint;
import java.awt.Stroke;

import repast.simphony.space.graph.RepastEdge;
/**
 *  @deprecated replaced by ogl 2D
 */
// TODO: make this extends style2d?
public interface EdgeStyle2D {

	public static final int NULL_HEAD = 0;

	public static final int ARROW_HEAD = 1;

	public static final int CIRCLE_HEAD = 2;

	public static final int DIAMOND_HEAD = 3;

	public static final int DELTA_HEAD = 4;

	public static final int T_HEAD = 5;

	public Stroke getStroke(RepastEdge edge);

	public Paint getPaint(RepastEdge edge);

	public int getTargetEndStyle(RepastEdge edge);

	public int getSourceEndStyle(RepastEdge edge);

	public Paint getTargetEndPaint(RepastEdge edge);

	public Paint getSourceEndPaint(RepastEdge edge);

}
