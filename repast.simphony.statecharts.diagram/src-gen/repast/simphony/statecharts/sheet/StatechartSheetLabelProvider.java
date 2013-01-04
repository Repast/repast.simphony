package repast.simphony.statecharts.sheet;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

import repast.simphony.statecharts.navigator.StatechartNavigatorGroup;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class StatechartSheetLabelProvider extends BaseLabelProvider implements ILabelProvider {
  /**
   * @generated NOT
   */
  private ImageRegistry imageRegistry;

  /**
   * @generated
   */
  public String getText(Object element) {
    element = unwrap(element);
    if (element instanceof StatechartNavigatorGroup) {
      return ((StatechartNavigatorGroup) element).getGroupName();
    }
    IElementType etype = getElementType(getView(element));
    return etype == null ? "" : etype.getDisplayName();
  }

  /**
   * @generated NOT
   */
  public Image getImage(Object element) {
    IElementType etype = getElementType(getView(unwrap(element)));
    // new code from here on
    if (etype == null) return null;
    ImageRegistry registry = getImageRegistry();
    Image image = registry.get(etype.getIconURL().toString());
    if (image == null) {
      ImageDescriptor desc = ImageDescriptor.createFromURL(etype.getIconURL());
      image = desc.createImage();
      registry.put(etype.getIconURL().toString(), image);
    }
    return image;
    
    // old codereturn etype == null ? null : StatechartElementTypes.getImage(etype);
  }
  
  /**
   * @generated
   */
  private ImageRegistry getImageRegistry() {
    if (imageRegistry == null) {
      imageRegistry = new ImageRegistry();
    }
    return imageRegistry;
  }

  /**
   * @generated
   */
  private Object unwrap(Object element) {
    if (element instanceof IStructuredSelection) {
      return ((IStructuredSelection) element).getFirstElement();
    }
    return element;
  }

  /**
   * @generated
   */
  private View getView(Object element) {
    if (element instanceof View) {
      return (View) element;
    }
    if (element instanceof IAdaptable) {
      return (View) ((IAdaptable) element).getAdapter(View.class);
    }
    return null;
  }

  /**
   * @generated
   */
  private IElementType getElementType(View view) {
    // For intermediate views climb up the containment hierarchy to find the one associated with an element type.
    while (view != null) {
      int vid = StatechartVisualIDRegistry.getVisualID(view);
      IElementType etype = StatechartElementTypes.getElementType(vid);
      if (etype != null) {
        return etype;
      }
      view = view.eContainer() instanceof View ? (View) view.eContainer() : null;
    }
    return null;
  }

}
