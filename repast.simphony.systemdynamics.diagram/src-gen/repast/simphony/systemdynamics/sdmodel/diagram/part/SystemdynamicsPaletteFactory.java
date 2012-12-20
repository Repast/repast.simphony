package repast.simphony.systemdynamics.sdmodel.diagram.part;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

import repast.simphony.systemdynamics.sdmodel.diagram.providers.SystemdynamicsElementTypes;

/**
 * @generated
 */
public class SystemdynamicsPaletteFactory {

  /**
   * @generated
   */
  public void fillPalette(PaletteRoot paletteRoot) {
    paletteRoot.add(createSdmodel1Group());
  }

  /**
   * Creates "sdmodel" palette tool group
   * @generated
   */
  private PaletteContainer createSdmodel1Group() {
    PaletteGroup paletteContainer = new PaletteGroup(Messages.Sdmodel1Group_title);
    paletteContainer.setId("createSdmodel1Group"); //$NON-NLS-1$
    paletteContainer.add(createCausalLink1CreationTool());
    paletteContainer.add(createRateLink2CreationTool());
    paletteContainer.add(createVariable3CreationTool());
    paletteContainer.add(createCloud4CreationTool());
    paletteContainer.add(createStock5CreationTool());
    return paletteContainer;
  }

  /**
   * @generated
   */
  private ToolEntry createCausalLink1CreationTool() {
    LinkToolEntry entry = new LinkToolEntry(Messages.CausalLink1CreationTool_title,
        Messages.CausalLink1CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.CausalLink_4002));
    entry.setId("createCausalLink1CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.CausalLink_4002));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createRateLink2CreationTool() {
    LinkToolEntry entry = new LinkToolEntry(Messages.RateLink2CreationTool_title,
        Messages.RateLink2CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Rate_4003));
    entry.setId("createRateLink2CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Rate_4003));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createVariable3CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Variable3CreationTool_title,
        Messages.Variable3CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Variable_2001));
    entry.setId("createVariable3CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Variable_2001));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createCloud4CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Cloud4CreationTool_title,
        Messages.Cloud4CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Cloud_2002));
    entry.setId("createCloud4CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Cloud_2002));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createStock5CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Stock5CreationTool_title,
        Messages.Stock5CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Stock_2003));
    entry.setId("createStock5CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Stock_2003));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private static class NodeToolEntry extends ToolEntry {

    /**
     * @generated
     */
    private final List<IElementType> elementTypes;

    /**
     * @generated
     */
    private NodeToolEntry(String title, String description, List<IElementType> elementTypes) {
      super(title, description, null, null);
      this.elementTypes = elementTypes;
    }

    /**
     * @generated
     */
    public Tool createTool() {
      Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
      tool.setProperties(getToolProperties());
      return tool;
    }
  }

  /**
   * @generated
   */
  private static class LinkToolEntry extends ToolEntry {

    /**
     * @generated
     */
    private final List<IElementType> relationshipTypes;

    /**
     * @generated
     */
    private LinkToolEntry(String title, String description, List<IElementType> relationshipTypes) {
      super(title, description, null, null);
      this.relationshipTypes = relationshipTypes;
    }

    /**
     * @generated
     */
    public Tool createTool() {
      Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
      tool.setProperties(getToolProperties());
      return tool;
    }
  }
}
