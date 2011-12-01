package repast.simphony.visualization;

/**
 * Interface for classes that wish to listen for probe events.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ProbeListener {

	public void objectProbed(ProbeEvent evt);
}
