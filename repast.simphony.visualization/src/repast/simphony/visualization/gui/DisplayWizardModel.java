package repast.simphony.visualization.gui;

import org.pietschy.wizard.WizardEvent;
import org.pietschy.wizard.WizardListener;
import org.pietschy.wizard.models.DynamicModel;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.ProjectionDescriptor;

/**
 * Wizard model for configuring displays.
 * 
 * @author Nick Collier
 */
public class DisplayWizardModel extends DynamicModel implements WizardListener {

  private ContextData context;

  private DisplayDescriptor descriptor;
  private boolean cancelled = false;

  // contextID is not necessarily that of the rootContext, but rather the
  // context that display configuration is for.
  public DisplayWizardModel(Object contextID, DisplayDescriptor descriptor, ContextData rootContext) {
    super();
    this.descriptor = descriptor;
    context = rootContext.find(contextID.toString());
  }

  public void wizardCancelled(WizardEvent wizardEvent) {
    cancelled = true;
  }

  public void wizardClosed(WizardEvent wizardEvent) {
    // todo implement method
  }

  public DisplayDescriptor getDescriptor() {
    return cancelled ? null : descriptor;
  }

  public ProjectionDescriptor getTypeDescriptor(String type) {
    for (ProjectionData proj : getDescriptor().getProjections()) {
      if (type.equals(proj.getType())) {
        return getDescriptor().getProjectionDescriptor(proj.getId());
      }
    }
    return null;
  }

  public String getDefaultStyle() {
  	
  	// TODO Projections: get the default style for the display type from the viz registry.
  	
    if (descriptor.getDisplayType().equals(DisplayType.THREE_D))
      return descriptor.getDefaultStyles3D()[0].getName();
        
    if (descriptor.getDisplayType().equals(DisplayType.TWO_D))
      return descriptor.getDefaultStyles2D()[0].getName();
    
    // TODO Projections: GIS
    // TODO WWJ - handle multiple styles
    if (descriptor.getDisplayType().equals(DisplayType.GIS3D))
      return descriptor.getDefaultStylesGIS3D()[0].getName();

    
    // return null for 2D GIS as there is no default style class for that
    return null;
  }

  /**
   * 
   * @param type
   *          the type of projection (e.g. Network)
   * @return true if this contains only projections of the specified type
   *         otherwise false
   */
  public boolean contextContainsOnlyProjectionType(String type) {
    for (ProjectionData proj : context.projections()) {
      if (! type.equals(proj.getType())) {
        return false;
      }
    }
    return true;
  }

  /**
   * 
   * @param type
   * @return true if the current list of projections contains the specified
   *         type.
   */
  public boolean contextContainsProjectionType(String type) {
    for (ProjectionData proj : context.projections()) {
      if (type.equals(proj.getType())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 
   * @param type
   *          the type of projection (e.g. SNetwork)
   * @return true if this contains only projections of the specified type
   *         otherwise false
   */
  public boolean containsOnlyProjectionType(String type) {
    for (ProjectionData proj : getDescriptor().getProjections()) {
      if (! type.equals(proj.getType())) {
        return false;
      }
    }
    return true;
  }

  /**
   * 
   * @param type
   * @return true if the current list of projections contains the specified
   *         type.
   */
  public boolean containsProjectionType(String type) {
    for (ProjectionData proj : getDescriptor().getProjections()) {
      if (type.equals(proj.getType())) {
        return true;
      }
    }
    return false;
  }

  public boolean containsValueLayer() {
    return descriptor.getValueLayerCount() > 0;
  }

  /**
   * @return the context
   */
  public ContextData getContext() {
    return context;
  }
}
