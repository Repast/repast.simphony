package repast.simphony.visualization.visualization2D.layout;

import java.awt.Dimension;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.visualization2D.Display2D;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;

/**
 * Layout that wraps JUNG ISOMLayout
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class ISOMLayout2D<T> extends IterableLayout<T>{

	public ISOMLayout2D(){
		super();
	}

	@Override
	protected void setLayout(){
		layout = new ISOMLayout<T, RepastEdge<T>>(((ContextJungNetwork<T>)network).getGraph());

		layout.setSize(new Dimension(Display2D.DISPLAY_WIDTH, Display2D.DISPLAY_HEIGHT));

		iterate();
	}	
}