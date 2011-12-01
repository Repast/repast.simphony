package repast.simphony.visualization.visualization2D.layout;

import java.awt.Dimension;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.visualization2D.Display2D;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;

/**
 * Layout that wraps JUNG CircleLayout
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class CircleLayout2D<T> extends JungLayout<T>{

	public CircleLayout2D(){
		super();
	}
	
	@Override
	protected void setLayout(){
		layout = new CircleLayout<T, RepastEdge<T>>(((ContextJungNetwork<T>)network).getGraph());

		layout.setSize(new Dimension(Display2D.DISPLAY_WIDTH, Display2D.DISPLAY_HEIGHT));
	}
}