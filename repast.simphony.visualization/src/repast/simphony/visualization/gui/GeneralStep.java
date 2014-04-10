package repast.simphony.visualization.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.ProjectionRegistryData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.ui.widget.ListSelector;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.CartesianProjectionDescritorFactory;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.ProjectionDescriptorFactory;
import repast.simphony.visualization.engine.ValueLayerDescriptor;
import repast.simphony.visualization.engine.VisualizationRegistry;
import repast.simphony.visualization.engine.VisualizationRegistryData;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class GeneralStep extends PanelWizardStep {
	
	private static final MessageCenter msg = MessageCenter.getMessageCenter(GeneralStep.class);
	public static final String DEFAULT_DISPLAY_TITLE = "New Display";

  class DisplayItem {
    String displayName;
    ProjectionData proj;

    public DisplayItem(String displayName, ProjectionData proj) {
      this.proj = proj;
      this.displayName = displayName;
    }

    public boolean equals(Object obj) {
      if (obj instanceof DisplayItem) {
        DisplayItem other = (DisplayItem) obj;
        if (proj == null) {
          if (other.proj == null)
            return other.displayName.equals(displayName);
          return false;
        } else {
          if (other.proj != null)
            return proj.equals(other.proj);
          return false;
        }
      }
      return false;
    }

    public String toString() {
      return displayName;
    }

    public ProjectionData getProjectionData() {
      return proj;
    }
  }

  private DisplayWizardModel model;
  private JTextField nameFld = new JTextField();
  private JComboBox<String> typeBox = new JComboBox();
  private ListSelector<DisplayItem> selector;
  
  /**
   * The descriptor cache is handy in case the user changes the display type back
   *   and forth during one wizard sessions, so that changes aren't lost between
   *   switches.
   */
  private Map<String,DisplayDescriptor> descriptorCache = new HashMap<String,DisplayDescriptor>();

  public GeneralStep() {
    super("Display Details", "Please enter the name and type of the display as well the "
        + "projections the display should visualize");
    
    this.setLayout(new BorderLayout());
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow",
        "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();
    builder.addSeparator("General", cc.xyw(1, 1, 3));
    builder.addLabel("Name:", cc.xy(1, 3));
    builder.add(nameFld, cc.xy(3, 3));
    builder.addLabel("Type:", cc.xy(1, 5));
    builder.add(typeBox, cc.xy(3, 5));

    java.util.List<DisplayItem> list = new ArrayList<DisplayItem>();
    selector = new ListSelector<DisplayItem>(list, list, false);
    builder.addSeparator("Projections and Value Layers", cc.xyw(1, 7, 3));
    builder.add(selector.getPanel(), cc.xyw(1, 9, 3));

    add(builder.getPanel(), BorderLayout.CENTER);
    selector.addActionListeners(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setComplete(selector.getSelectedItems().size() > 0 && nameFld.getText().length() > 0);
      }
    });
  }

  private boolean doValidate() {
    int geoCount = 0; // # geography projections
    int netCount = 0; // # network projections
    int cartCount = 0; // # cartesian (grid,continuous) projections
    int valueCount = 0; // # value layers

    // TODO Projections: Have the viz registry entries provide info on 
    //        what projections they support.
    for (DisplayItem item : selector.getSelectedItems()) {
      if (item.getProjectionData().getType().equals(ProjectionData.VALUE_LAYER_TYPE))
        valueCount++;
      
      // TODO Projections: GIS
      else if (item.getProjectionData().getType().equals(ProjectionData.GEOGRAPHY_TYPE))
        geoCount++;
      
      else if (item.getProjectionData().getType().equals(ProjectionData.NETWORK_TYPE))
        netCount++;
      
      else
        cartCount++;
    }

    String displayType = (String) typeBox.getSelectedItem();

    // TODO Projections: move to GIS viz registry data
    // Validate GIS / Geography projection displays
    // Check if a geography projection is used with a non-GIS display type
    if (geoCount > 0
        && (displayType.equals(DisplayType.TWO_D) || displayType.equals(DisplayType.THREE_D))) {
      JOptionPane.showMessageDialog(selector,
          "Geography projections can only be used in a GIS display", "Display Error",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    // TODO Projections: move to GIS viz registry data
    // Check if GIS displays contain a geography projection
    else if (displayType.equals(DisplayType.GIS) || displayType.equals(DisplayType.GIS3D)) {
      if (geoCount != 1) {
        JOptionPane.showMessageDialog(selector, "A GIS display must contain a single Geography.",
            "Display Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      else if (geoCount <= 1 && (cartCount > 0 || valueCount > 0)) {
        JOptionPane.showMessageDialog(selector,
            "GIS displays may only contain Geography and Network projections.", "Display Error",
            JOptionPane.ERROR_MESSAGE);
        return false;
      }

      else if (geoCount == 1 && netCount > 0) {
        JOptionPane.showMessageDialog(selector,
            "Note that network edges are styled as agents in GIS displays.", "Display Info",
            JOptionPane.INFORMATION_MESSAGE);
        return true;
      }
    }

    // If the display contains value layers...
    if (valueCount > 0) {

      // Check if there is at least one layout projection
      if (cartCount == 0) {
        JOptionPane
            .showMessageDialog(
                selector,
                "Displays containing Value Layers must also contain at least one Grid or Continuous projection.",
                "Display Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      // Check if there is no more than one value layer
      if (valueCount > 1) {
        JOptionPane.showMessageDialog(selector, "Displays may only contain one Value Layer.",
            "Display Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

    }
    return true;
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  @Override
  public void prepare() {
    super.prepare();

    // Comboboxmodel is a list of available displays
    DefaultComboBoxModel<String> cmbModel = new DefaultComboBoxModel<String>();
     
    // The default Repast projections are handled by the 2D/3D Cartesian displays.
    if (model.contextContainsProjectionType(ProjectionData.CONTINUOUS_SPACE_TYPE) ||
    		model.contextContainsProjectionType(ProjectionData.GRID_TYPE) ||
    		model.contextContainsProjectionType(ProjectionData.NETWORK_TYPE) ||
    		model.contextContainsProjectionType(ProjectionData.VALUE_LAYER_TYPE)){
    	
    	cmbModel.addElement(DisplayType.TWO_D);
    	cmbModel.addElement(DisplayType.THREE_D);
    }
    	
    // Check the projection registry for additional projection types
    for (ProjectionRegistryData<?> prd : ProjectionRegistry.getRegistryData()){
    	
    	String projectionType = prd.getTypeName();
    	
    	// If the model contains a projection type and a display supports the 
    	//  projection type, then add the display type to the list of display types
    	//  in the wizard.
    	if (model.contextContainsProjectionType(projectionType)){
    		for (VisualizationRegistryData vizData : VisualizationRegistry.getRegistryData()){	
    			if (vizData.handlesProjectionType(projectionType)){
    				cmbModel.addElement(vizData.getVisualizationType());
    			}
    		}
    	}
    }
    
    typeBox.setModel(cmbModel);
    
    // Build a cache of display descriptor types.
    // Built in Cartesian descriptors
    descriptorCache.put(DisplayType.TWO_D, 
    		new CartesianDisplayDescriptor(nameFld.getText().trim()));
    
    descriptorCache.put(DisplayType.THREE_D, 
    		new CartesianDisplayDescriptor(nameFld.getText().trim()));
    
    // Descriptors from registry
    for (VisualizationRegistryData data : VisualizationRegistry.getRegistryData()){
    	DisplayDescriptor newDesc = 
    			data.getDescriptorFactory().createDescriptor(nameFld.getText().trim());
    	descriptorCache.put(data.getVisualizationType(), newDesc);
    }
    
    java.util.List<DisplayItem> source = new ArrayList<DisplayItem>();
    java.util.List<DisplayItem> target = new ArrayList<DisplayItem>();
    for (ProjectionData proj : model.getContext().projections()) {
      DisplayItem item = null;
      item = new DisplayItem(proj.getId(), proj);
      source.add(item);
    }
    
    DisplayDescriptor descriptor = model.getDescriptor();

    // TODO Projections: maybe this is cause of not being able to add/remove 
    //        projections from the display once it's created (has a descriptor).
    // If the display is already defined, eg editing a display
    if (descriptor != null){
    	nameFld.setText(descriptor.getName());

    	String displayType = descriptor.getDisplayType();
    	typeBox.setSelectedItem(displayType);
    	
    	for (ProjectionData proj : descriptor.getProjections()) {
    		DisplayItem item = null;
    		item = new DisplayItem(proj.getId(), proj);
    		source.remove(item);
    		target.add(item);
    	}
    }
    // Otherwise it's creating a new display - Cartesian is default
    else{ 
    	nameFld.setText(DEFAULT_DISPLAY_TITLE); 
    	model.setDescriptor(new CartesianDisplayDescriptor(DEFAULT_DISPLAY_TITLE));
    }

    selector.setLists(source, target);
    setComplete(target.size() > 0);

    typeBox.addActionListener(new ActionListener(){

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		String type = (String) typeBox.getSelectedItem();

    		// If there's no current descritor or if the current model descriptor 
    		//  is different from what's selected, assign the selected descriptor
    		//  type from the cache.
    		if (model.getDescriptor() == null || !model.getDescriptor().getDisplayType().equals(type)){
    			model.setDescriptor(descriptorCache.get(type));
    		}					
    	}
    });
  }

  /*
   * (non-Javadoc)
   * @see org.pietschy.wizard.PanelWizardStep#applyState()
   * 
   * This method is called whenever the user presses next while this step is active.
   */
  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    
    if (!doValidate()) {
      throw new InvalidStateException();
    }

    DisplayDescriptor descriptor = model.getDescriptor();
    descriptor.setName(nameFld.getText().trim());
    String curType = descriptor.getDisplayType();
    String newType = (String) typeBox.getSelectedItem();
    
    
    // TODO Projections: does this even get used now with the cache? ---V
    if (curType != newType) {
      descriptor.setDisplayType(newType, true);
    }
    descriptor.clearProjections();
    descriptor.clearProjectionDescriptors();

    if (descriptor instanceof ValueLayerDescriptor){
    	((ValueLayerDescriptor)descriptor).clearValueLayerNames();
    }
    
    // -----------------------------------------------------------------^^
    
    for (DisplayItem item : selector.getSelectedItems()) {
    	if (item.proj != null) {
    		
    		if (item.getProjectionData().getType().equals(ProjectionData.VALUE_LAYER_TYPE)) {
    			if (descriptor instanceof ValueLayerDescriptor){
    				((ValueLayerDescriptor)descriptor).addValueLayerName(item.displayName);
    			}
    		} 
    		else {
    			ProjectionDescriptorFactory pdf = null;
    			
    			// TODO Projections: move 2D/3D to proj registry
    			if (DisplayType.TWO_D.equals(curType) || DisplayType.THREE_D.equals(curType)){
    				pdf = new CartesianProjectionDescritorFactory();
    			}
    			else{
    				pdf = VisualizationRegistry.getDataFor(curType).getProjectionDescriptorFactory();
    			}
    			
    			if (pdf != null){	
    				descriptor.addProjection(item.proj, pdf.createDescriptor(item.proj));
    			}
    			else{
    				msg.error("No ProjectionDescriptorFactory found for type: " + curType, null);
    			}
    		}
    	}
    }
  }
}