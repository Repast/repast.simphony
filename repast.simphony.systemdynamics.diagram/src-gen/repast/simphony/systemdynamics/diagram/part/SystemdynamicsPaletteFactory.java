package repast.simphony.systemdynamics.diagram.part;

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

import repast.simphony.systemdynamics.diagram.providers.SystemdynamicsElementTypes;

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
    paletteContainer.add(createInfluenceArrow1CreationTool());
    paletteContainer.add(createRate2CreationTool());
    paletteContainer.add(createStock3CreationTool());
    paletteContainer.add(createConstant4CreationTool());
    paletteContainer.add(createVariable5CreationTool());
    paletteContainer.add(createCloud6CreationTool());
    return paletteContainer;
  }

  /**
   * @generated
   */
  private ToolEntry createInfluenceArrow1CreationTool() {
    LinkToolEntry entry = new LinkToolEntry(Messages.InfluenceArrow1CreationTool_title,
        Messages.InfluenceArrow1CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.InfluenceLink_4004));
    entry.setId("createInfluenceArrow1CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.InfluenceLink_4004));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createRate2CreationTool() {
    LinkToolEntry entry = new LinkToolEntry(Messages.Rate2CreationTool_title,
        Messages.Rate2CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Rate_4003));
    entry.setId("createRate2CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Rate_4003));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createStock3CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Stock3CreationTool_title,
        Messages.Stock3CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Stock_2003));
    entry.setId("createStock3CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Stock_2003));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createConstant4CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Constant4CreationTool_title,
        Messages.Constant4CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Variable_2004));
    entry.setId("createConstant4CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Variable_2004));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createVariable5CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Variable5CreationTool_title,
        Messages.Variable5CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Variable_2001));
    entry.setId("createVariable5CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Variable_2001));
    entry.setLargeIcon(entry.getSmallIcon());
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createCloud6CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.Cloud6CreationTool_title,
        Messages.Cloud6CreationTool_desc,
        Collections.singletonList(SystemdynamicsElementTypes.Cloud_2002));
    entry.setId("createCloud6CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(SystemdynamicsElementTypes
        .getImageDescriptor(SystemdynamicsElementTypes.Cloud_2002));
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
