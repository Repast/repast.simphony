package repast.simphony.visualization.engine;

import repast.simphony.context.Context;

public abstract class AbstractDisplayCreator implements DisplayCreator{

  protected Context<?> context;
  protected DisplayDescriptor descriptor; 
  
  public AbstractDisplayCreator(Context<?> context, DisplayDescriptor descriptor) {
    this.context = context;
    this.descriptor = descriptor;
  }
}
