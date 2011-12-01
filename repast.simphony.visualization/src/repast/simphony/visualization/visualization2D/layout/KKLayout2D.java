package repast.simphony.visualization.visualization2D.layout;

import java.awt.Dimension;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.visualization2D.Display2D;
import edu.uci.ics.jung.algorithms.layout.KKLayout;

/**
 * Layout that wraps JUNG KKLayout
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class KKLayout2D<T> extends IterableLayout<T>{
	
	public KKLayout2D(){
		super();
	}

	@Override
	protected void setLayout(){
		layout = new KKLayout<T, RepastEdge<T>>(((ContextJungNetwork<T>)network).getGraph());

		layout.setSize(new Dimension(Display2D.DISPLAY_WIDTH, Display2D.DISPLAY_HEIGHT));

		iterate();
	}
}