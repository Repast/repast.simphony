package repast.simphony.statecharts.part;

import java.util.ArrayList;
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

import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class StatechartPaletteFactory {

  /**
   * @generated
   */
  public void fillPalette(PaletteRoot paletteRoot) {
    paletteRoot.add(createStatechart1Group());
  }

  /**
   * Creates "statechart" palette tool group
   * @generated
   */
  private PaletteContainer createStatechart1Group() {
    PaletteGroup paletteContainer = new PaletteGroup(Messages.Statechart1Group_title);
    paletteContainer.setId("createStatechart1Group"); //$NON-NLS-1$
    paletteContainer.add(createTransition1CreationTool());
    paletteContainer.add(createEntryStateMarker2CreationTool());
    paletteContainer.add(createState3CreationTool());
    paletteContainer.add(createCompositeState4CreationTool());
    paletteContainer.add(createInitialStateMarker5CreationTool());
    paletteContainer.add(createShallowHistory6CreationTool());
    paletteContainer.add(createDeepHistory7CreationTool());
    paletteContainer.add(createFinalState8CreationTool());
    paletteContainer.add(createChoice9CreationTool());
    return paletteContainer;
  }

  /**
   * @generated
   */
  private ToolEntry createTransition1CreationTool() {
    LinkToolEntry entry = new LinkToolEntry(Messages.Transition1CreationTool_title,
        Messages.Transition1CreationTool_desc,
        Collections.singletonList(StatechartElementTypes.Transition_4001));
    entry.setId("createTransition1CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(StatechartDiagramEditorPlugin
        .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/transition-16.png")); //$NON-NLS-1$
    entry.setLargeIcon(StatechartDiagramEditorPlugin
        .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/transition-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createEntryStateMarker2CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.EntryStateMarker2CreationTool_title,
        Messages.EntryStateMarker2CreationTool_desc,
        Collections.singletonList(StatechartElementTypes.PseudoState_2007));
    entry.setId("createEntryStateMarker2CreationTool"); //$NON-NLS-1$
    entry
        .setSmallIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/First-State-16.png")); //$NON-NLS-1$
    entry
        .setLargeIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/First-State-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createState3CreationTool() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(StatechartElementTypes.State_2003);
    types.add(StatechartElementTypes.State_3001);
    NodeToolEntry entry = new NodeToolEntry(Messages.State3CreationTool_title,
        Messages.State3CreationTool_desc, types);
    entry.setId("createState3CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(StatechartDiagramEditorPlugin
        .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/State-16.png")); //$NON-NLS-1$
    entry.setLargeIcon(StatechartDiagramEditorPlugin
        .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/State-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createCompositeState4CreationTool() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(StatechartElementTypes.CompositeState_2004);
    types.add(StatechartElementTypes.CompositeState_3002);
    NodeToolEntry entry = new NodeToolEntry(Messages.CompositeState4CreationTool_title,
        Messages.CompositeState4CreationTool_desc, types);
    entry.setId("createCompositeState4CreationTool"); //$NON-NLS-1$
    entry
        .setSmallIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/Composite-State-16.png")); //$NON-NLS-1$
    entry
        .setLargeIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/Composite-State-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createInitialStateMarker5CreationTool() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(StatechartElementTypes.PseudoState_3003);
    types.add(StatechartElementTypes.PseudoState_2005);
    NodeToolEntry entry = new NodeToolEntry(Messages.InitialStateMarker5CreationTool_title,
        Messages.InitialStateMarker5CreationTool_desc, types);
    entry.setId("createInitialStateMarker5CreationTool"); //$NON-NLS-1$
    entry
        .setSmallIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/Initial-State-16.png")); //$NON-NLS-1$
    entry
        .setLargeIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/Initial-State-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createShallowHistory6CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.ShallowHistory6CreationTool_title,
        Messages.ShallowHistory6CreationTool_desc,
        Collections.singletonList(StatechartElementTypes.History_3008));
    entry.setId("createShallowHistory6CreationTool"); //$NON-NLS-1$
    entry
        .setSmallIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/Shallow-History-16.png")); //$NON-NLS-1$
    entry
        .setLargeIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/Shallow-History-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createDeepHistory7CreationTool() {
    NodeToolEntry entry = new NodeToolEntry(Messages.DeepHistory7CreationTool_title,
        Messages.DeepHistory7CreationTool_desc,
        Collections.singletonList(StatechartElementTypes.History_3009));
    entry.setId("createDeepHistory7CreationTool"); //$NON-NLS-1$
    entry
        .setSmallIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/Deep-History-16.png")); //$NON-NLS-1$
    entry
        .setLargeIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/Deep-History-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createFinalState8CreationTool() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(StatechartElementTypes.FinalState_3007);
    types.add(StatechartElementTypes.FinalState_2008);
    NodeToolEntry entry = new NodeToolEntry(Messages.FinalState8CreationTool_title,
        Messages.FinalState8CreationTool_desc, types);
    entry.setId("createFinalState8CreationTool"); //$NON-NLS-1$
    entry
        .setSmallIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/Final-State-16.png")); //$NON-NLS-1$
    entry
        .setLargeIcon(StatechartDiagramEditorPlugin
            .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/Final-State-32.png")); //$NON-NLS-1$
    return entry;
  }

  /**
   * @generated
   */
  private ToolEntry createChoice9CreationTool() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(StatechartElementTypes.PseudoState_3006);
    types.add(StatechartElementTypes.PseudoState_2006);
    NodeToolEntry entry = new NodeToolEntry(Messages.Choice9CreationTool_title,
        Messages.Choice9CreationTool_desc, types);
    entry.setId("createChoice9CreationTool"); //$NON-NLS-1$
    entry.setSmallIcon(StatechartDiagramEditorPlugin
        .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj16/Choice-16.png")); //$NON-NLS-1$
    entry.setLargeIcon(StatechartDiagramEditorPlugin
        .findImageDescriptor("/repast.simphony.statecharts.diagram/icons/obj32/Choice-32.png")); //$NON-NLS-1$
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
