package repast.simphony.visualization.engine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.visualization.IDisplay;
import simphony.util.messages.MessageCenter;

/**
 * Produces an IDisplay from a DisplayDescriptor.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class DisplayProducer {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(DisplayProducer.class);

  private DisplayDescriptor displayDescriptor;

  private Context<?> context;

  public DisplayProducer(Object contextID, RunState runState, DisplayDescriptor displayDescriptor) {
    this.displayDescriptor = displayDescriptor;
    Context<?> masterContext = runState.getMasterContext();
    setContext(masterContext, contextID);
    if (context == null) {
      throw new IllegalArgumentException("Unable to create display for context id: '" + contextID + "'. This is must match that set in the model's ContextBuilder.");
    }
  }

  private void setContext(Context<?> context, Object contextID) {
    if (context.getId().equals(contextID)) {
      this.context = context;
    } else {
      for (Context<?> child : context.getSubContexts()) {
        setContext(child, contextID);
      }
    }
  }

  public IDisplay createDisplay() throws IllegalAccessException, InvocationTargetException,
      InstantiationException, ClassNotFoundException, IOException, DisplayCreationException {
    if (displayDescriptor.getDisplayType().equals(DisplayType.TWO_D))
      //return new DisplayCreator2D(context, displayDescriptor).createDisplay();
      return new DisplayCreatorOGL2D(context, (CartesianDisplayDescriptor)displayDescriptor).createDisplay();
   
    else if (displayDescriptor.getDisplayType().equals(DisplayType.THREE_D))
      return new DisplayCreator3D(context, (CartesianDisplayDescriptor)displayDescriptor).createDisplay(); 
        
    // TODO Projections: above code can be removed in favor of the registry approach
    //       if adopted for other displays.
    
    else {
    	VisualizationRegistryData data = VisualizationRegistry.getDataFor(displayDescriptor.getDisplayType());

    	if (data != null){ 
    		try{
    			DisplayCreatorFactory factory = data.getDisplayCreatorFactory();
    			return factory.createDisplayCreator(context, displayDescriptor).createDisplay();
    		} 
    		catch (Exception ex) {
    			msg.error("Error creating display for " + displayDescriptor.getDisplayType(), ex);
    		}
    	}
    	else{
    		msg.error("No display implementation found for " + displayDescriptor.getDisplayType(), null);
    	}
    }
    
    return null;
  }
}