package repast.simphony.visualization.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.plugin.editor.SquareIcon;
import repast.simphony.visualization.gis3D.style.CoverageStyle;
import repast.simphony.visualization.gis3D.style.DefaultCoverageStyle;
import simphony.util.messages.MessageCenter;

/**
 * Style editor step for coverage layers
 * 
 * @author Eric Tatara
 * 
 * TODO GIS complete for coverage layers
 * 
 * TODO refactor with parent CoverageStyleStep to use a standard StyleStep that
 *      is generic wrt the types of classes and styles used.
 */
public class CoverageStyleClassStep extends CoverageStyleStep {
	private static final long serialVersionUID = -7729788451078274654L;

	private static final MessageCenter msg = MessageCenter.getMessageCenter(CoverageStyleClassStep.class);
	
	public static final String defaultStyle = DefaultCoverageStyle.class.getName();
	public static File lastDirectory;
	private static final String STYLE_KEY = "style class name";
	private static final String EDITED_STYLE_KEY = "edited style class name";
	private static final String STATIC_LAYER_PATH = "static layer path";
	
	protected DefaultComboBoxModel<String> styleModel;
	protected JButton buildStyleButton;
	protected JButton bgcolorBtn;
	protected JButton addStaticLayerButton;
	protected JButton removeStaticLayerButton;
	protected JComboBox<String> styleBox;
	protected Map<String, String> editedStyleFileMap = new HashMap<String, String>();
	protected Color backgroundColor;
  
	static {
    File scenarioDir = RSApplication.getRSApplicationInstance().getCurrentScenario().getScenarioDirectory();
    
    // Trim the actual .rs folder so we're left with the model folder
    String modelFolder = scenarioDir.getAbsolutePath().replace(scenarioDir.getName(), "");
    lastDirectory = new File(modelFolder);
  }
	
	protected static Map<Class<?>,List<String>> styleCache = 
			new HashMap<Class<?>,List<String>>();
	
	public CoverageStyleClassStep() {
		super("Coverage Layer Style", "Please provide a style for each coverage");
	}
	
	@Override
	protected JPanel getStylePanel(){
		
		// Columns: Style class | | | edit button
		FormLayout layout = new FormLayout(		
				"30dlu, 4dlu, pref, 4dlu, pref, 4dlu, pref:grow",  // columns
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref:grow");  // rows
		
		PanelBuilder builder = new PanelBuilder(layout);

		styleModel = new DefaultComboBoxModel<String>();
		styleBox = new JComboBox<String>(styleModel);
		styleBox.setToolTipText("This is the style class for the selected coverage layer. "
				+ "Select a style from this box or use the editor to create a new style");
		styleBox.setEditable(true);
		
		CellConstraints cc = new CellConstraints();

		builder.addLabel("Coverage Style Class:", cc.xyw(1, 1, 7));
		builder.add(styleBox, cc.xyw(1, 3, 7));

//		buildStyleButton = new JButton();
//		builder.add(buildStyleButton, cc.xy(7, 3));
//		buildStyleButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(EDIT_ICON)));
//		buildStyleButton.setToolTipText("Edit the style of the selected coverage");
//		buildStyleButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			
//				// TODO GIS coverage style editor
//			}
//		});
//		buildStyleButton.setEnabled(false);  // TODO GIS enable when editor available
		
		builder.addLabel("Background Layer Color:", cc.xyw(1, 5, 7));
		bgcolorBtn = new JButton();
		builder.add(bgcolorBtn, cc.xyw(1, 7, 1));
		bgcolorBtn.setToolTipText("Click to change background color");
		bgcolorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Color c = JColorChooser.showDialog(CoverageStyleClassStep.this, 
						"Select Display Background Color", backgroundColor);
				if (c != null)
					setBackgroundColor(c);
			}
		});
		
		builder.addLabel("Static Raster Layer:", cc.xyw(1, 9, 7));
		
		addStaticLayerButton = new JButton();
		addStaticLayerButton.setIcon(new ImageIcon(
				getClass().getClassLoader().getResource(CoverageStyleStep.ADD_ICON)));
		addStaticLayerButton.setToolTipText("Add a background coverage layer from file");
		
		builder.add(addStaticLayerButton, cc.xyw(1, 11, 1));
		
		addStaticLayerButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastDirectory);
        chooser.setMultiSelectionEnabled(true);
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          addLayer(chooser.getSelectedFiles());
          lastDirectory = chooser.getCurrentDirectory();
        }
      }
    });
		
		removeStaticLayerButton = new JButton();
		removeStaticLayerButton.setIcon(new ImageIcon(
				getClass().getClassLoader().getResource(CoverageStyleStep.REMOVE_ICON)));
		removeStaticLayerButton.setToolTipText("Remove selected background layer");
		
		builder.add(removeStaticLayerButton, cc.xyw(3, 11, 1));
		
		removeStaticLayerButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) agentList.getModel();
        CoverageLayerElement elem = agentList.getSelectedValue();
        
        // only remove static layers
        if (elem.getValue(STATIC_LAYER_PATH) != null) {
        	int index = model.indexOf(elem);
        	model.removeElement(elem);
        	agentList.setSelectedIndex(index == model.size() ? index - 1 : index);
        }
        else {
        	String msg = "Only static coverage layers can be deleted.";
        	JOptionPane.showMessageDialog(null, msg, "Layer delete error", 
        			JOptionPane.ERROR_MESSAGE);
        }
      }
    });
		
		// TODO GIS static coverage layer preview panel
		
		return builder.getPanel();
	}
	
  private void addLayer(File[] files) {
    for (File file : files) {
    
    	// If the shapefile path is contained within model path, then set the
    	// path to a relative path so that model distribution is easier.  Otherwise
    	// set the path to absolute path.
      String filePath = file.getAbsolutePath();
      
      File scenarioDir = RSApplication.getRSApplicationInstance().getCurrentScenario().getScenarioDirectory();
      
      // Trim the actual .rs folder so we're left with the model folder
      String modelFolder = scenarioDir.getAbsolutePath().replace(scenarioDir.getName(), "");
      
      if (filePath.contains(modelFolder)){
      	filePath = filePath.replace(modelFolder, "." + File.separator);	
        file = new File (filePath);
      }  
      
      String name = file.getName();
      
      // Use Apache commons io lib to force serialized filename to unix
//      FilenameUtils;
      
      CoverageLayerElement elem = new CoverageLayerElement(name);
      elem.setValue(STATIC_LAYER_PATH, file.getPath());
      elem.setValue(STYLE_KEY, defaultStyle);
      
      DefaultListModel<CoverageLayerElement> model = 
      		(DefaultListModel<CoverageLayerElement>) agentList.getModel();
      model.addElement(elem);
      agentList.setSelectedIndex(model.size() - 1);
     
    }
  }
	
	@Override
	protected void agentListChanged(CoverageLayerElement element){
		styleBox.setSelectedItem(element.getValue(STYLE_KEY));
	}

	@Override
	protected void setCurrentElementValues() {
		if (currentIndex != -1 && !(currentIndex >= agentListModel.size())) {
			CoverageLayerElement element = (CoverageLayerElement) agentListModel.get(currentIndex);

			Object selectedItem = styleBox.getSelectedItem();
			if (selectedItem != null) {
				String styleClassName = selectedItem.toString();
				
				element.setValue(STYLE_KEY, styleClassName);

				// TODO GIS edited coverage style
//				if ("repast.simphony.visualization.editedStyle.EditedStyle2D"
//						.equals(styleClassName)
//						|| "repast.simphony.visualization.editedStyle.EditedStyle3D"
//						.equals(styleClassName)
//						|| "repast.simphony.visualization.editedStyle.EditedStyleGIS3D"
//						.equals(styleClassName))
//
//					element.setValue(EDITED_STYLE_KEY, editedStyleFileMap.get(
//							element.getAgentClassName()));
//				else
//					element.setValue(EDITED_STYLE_KEY, null);
			}
		}
	}

	@Override
	public void applyChanges() {
		setCurrentElementValues();
		GISDisplayDescriptor descriptor = (GISDisplayDescriptor)model.getDescriptor();

		descriptor.getCoverageLayers().clear();
		descriptor.getStaticCoverageMap().clear();
		
		for (int i = 0; i < agentListModel.size(); i++) {
			CoverageLayerElement e = (CoverageLayerElement) agentListModel.get(i);
			
			String staticLayerPath = (String)e.getValue(STATIC_LAYER_PATH);
			String style = (String)e.getValue(STYLE_KEY);
			
			if (staticLayerPath == null)
				descriptor.addCoverageLayer(e.getName(), style);
			else
				descriptor.addStaticCoverage(staticLayerPath, style);
		}
		descriptor.setBackgroundColor(backgroundColor);
	}

	@Override
	public void initialize() {
		GISDisplayDescriptor descriptor = (GISDisplayDescriptor)model.getDescriptor();

		// Find all available style classes for the display type
		List<String> foundStyleClasses = findStylesFor(CoverageStyle.class, defaultStyle);
		
		Collections.sort(foundStyleClasses);
		styleModel.removeAllElements();

		for (String style : foundStyleClasses){
			styleModel.addElement(style);
		}
		
		Map<String,String> coverageLayerMap = descriptor.getCoverageLayers();
		// Set the coverage style mappings from the current descriptor
		for (String coverageName : coverageLayerMap.keySet()){
			
			CoverageLayerElement e = new CoverageLayerElement(coverageName);
			String styleclass = coverageLayerMap.get(coverageName);
			
			if (styleclass == null)
				styleclass = defaultStyle;
			
			e.setValue(STYLE_KEY, styleclass);
			
			agentListModel.addElement(e);		
		}
		
		Map<String,String> staticCoverageLayerMap = descriptor.getStaticCoverageMap();
		for (String path : staticCoverageLayerMap.keySet()) {
			
			// Trim any leading folders for the displayed coverage name
			String name = path;
			int idx = path.lastIndexOf(File.separator) + 1;
			if (idx >= 0)
				name = path.substring(idx);
			
			CoverageLayerElement e = new CoverageLayerElement(name);
			
			String styleclass = staticCoverageLayerMap.get(path);
			
			if (styleclass == null)
				styleclass = defaultStyle;
			
			e.setValue(STATIC_LAYER_PATH, path);
			e.setValue(STYLE_KEY, styleclass);
			
			agentListModel.addElement(e);	
		}
		
		if (descriptor.getBackgroundColor() != null)
			setBackgroundColor(descriptor.getBackgroundColor());
	}
		
	/**
	 * Provides a list of available style classes (including default) for the specified
	 *   interface.
	 *   
	 * @param styleInterface
	 * @return
	 */
	protected List<String> findStylesFor(Class<?> styleInterface, String defaultStyle){
		List<String> foundStyleClasses = styleCache.get(styleInterface);

		if (foundStyleClasses == null){
			foundStyleClasses = StyleClassFinder.getAvailableStyles(model.getContext(), styleInterface);

			if (defaultStyle != null){
				foundStyleClasses.add(defaultStyle);
			}
			else{
				msg.warn("No default styles found for " + model.getDescriptor().getDisplayType());	
			}
			styleCache.put(styleInterface, foundStyleClasses);
		} 
		return foundStyleClasses;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		bgcolorBtn.setIcon(new SquareIcon(14, 14, backgroundColor));
	}
}