package repast.simphony.visualization.visualization2D.layout;

import edu.uci.ics.jung.algorithms.util.IterativeContext;

/**
 * Abstract Layout class for Jung IterativeContext
 * 
 * @author tatara
 *
 * @param <T>
 */
public abstract class IterableLayout<T> extends JungLayout<T> {

	public void iterate(){
		while (!((IterativeContext)layout).done()) 
			((IterativeContext)layout).step();
	}
}
