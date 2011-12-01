package repast.simphony.parameter;

/**
 * Abstract implementation of Parameters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractParameters implements Parameters {

	protected Object contextID;

	protected AbstractParameters(Object contextID) {
		this.contextID = contextID;
	}

	/**
	 * Gets the context id for the context that these parameters for.
	 *
	 * @return the context id for the context that these parameters for.
	 */
	public Object getContextID() {
		return contextID;
	}
	
	public Parameters clone(){
		try{
			return (Parameters) super.clone();
		}
		catch(CloneNotSupportedException e){
			throw new InternalError(e.toString());
		}
		
	}
}
