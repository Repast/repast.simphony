package repast.simphony.visualization.engine;

/**
 * DisplayDescriptorValidator can be used to check a DisplayDescriptor to make
 *   sure that it contains valid entries.  Useful for updating older or
 *   deprecated DisplayDescriptor instances.
 * 
 * @author Eric Tatara
 *
 */
public class DisplayDescriptorValidator {

	/**
	 * Check the provided DisplayDescriptor validity and provide a new updated
	 * DisplayDescriptor if necessary.
	 * 
	 * @param descriptor
	 * @return
	 */
	public DisplayDescriptor validateDescriptor(DisplayDescriptor descriptor){
		
		// Convert the Repast Simphony ver 2.1 DefaultDisplayDescriptor
		if (descriptor instanceof DefaultDisplayDescriptor){
			
			DefaultDisplayDescriptorConverter converter = new DefaultDisplayDescriptorConverter();
			
			return converter.convertDesriptor((DefaultDisplayDescriptor)descriptor);
		}
		
		// No additional checks for now, just return the descriptor as is.
		else{
			return descriptor;
		}
	}
}