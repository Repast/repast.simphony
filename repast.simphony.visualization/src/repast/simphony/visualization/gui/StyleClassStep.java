package repast.simphony.visualization.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import repast.simphony.ui.plugin.editor.SquareIcon;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.VisualizationRegistry;
import repast.simphony.visualization.engine.VisualizationRegistryData;
import repast.simphony.visualization.gui.styleBuilder.EditedStyleDialog;
import repast.simphony.visualization.visualization3D.style.DefaultStyle3D;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Style editor step for displays that use style classes to style agents.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 * 
 * TODO Add a slider on the agent list
 * 
 */
public class StyleClassStep extends StyleStep {
	private static final long serialVersionUID = -8619245538953523657L;
	private static final MessageCenter msg = MessageCenter.getMessageCenter(StyleClassStep.class);
	
	private static final String STYLE_KEY = "style class name";
	private static final String EDITED_STYLE_KEY = "edited style class name";
	
	protected DefaultComboBoxModel<String> styleModel;
	protected JButton buildStyleButton;
	protected JButton bgcolorBtn;
	protected JComboBox<String> styleBox;
	protected Map<String, String> editedStyleFileMap = new HashMap<String, String>();
	protected Color backgroundColor;

	private boolean showBackgroundButton = true; 
	
	protected static Map<Class<?>,List<String>> styleCache = new HashMap<Class<?>,List<String>>();
	
	public StyleClassStep() {
		super("Agent Style", "Please provide a style for each agent type in the model");
	}
	
	@Override
	protected JPanel getStylePanel(){
		
		// Columns: Style class | | | edit button
		FormLayout layout = new FormLayout(
				"pref:grow, 4dlu, pref, 4dlu, pref, 4dlu, pref",  // columns
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref:grow");  // rows
		PanelBuilder builder = new PanelBuilder(layout);

		styleModel = new DefaultComboBoxModel<String>();
		styleBox = new JComboBox<String>(styleModel);
		styleBox.setToolTipText("This is the style class for the selected agent. "
				+ "Select a style from this box or use the editor to create a new style");
		styleBox.setEditable(true);
		
		CellConstraints cc = new CellConstraints();

		builder.addLabel("Style Class:", cc.xyw(1, 1, 7));
		builder.add(styleBox, cc.xyw(1, 3, 5));

		buildStyleButton = new JButton();
		builder.add(buildStyleButton, cc.xy(7, 3));
		
		buildStyleButton.setEnabled(false);  // False unless enabled based on descriptor
		buildStyleButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(EDIT_ICON)));
		buildStyleButton.setToolTipText("Edit the style of the selected agent type");
		buildStyleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					EditedStyleDialog dialog = new EditedStyleDialog((JDialog) SwingUtilities
							.getWindowAncestor(StyleClassStep.this));

					String editedStyleName = model.getDescriptor().getEditedStyleName(classFld.getText());
					
					dialog.init(Class.forName(classFld.getText()), editedStyleName, model.getDescriptor());
					dialog.pack();
					dialog.setVisible(true);

					if (dialog.doSave()) {
						// Set the style class name based on display type
						String styleClassName = getEditedStyleClassForDisplay(model.getDescriptor());
				
						if (styleModel.getIndexOf(styleClassName) < 0)
							styleModel.addElement(styleClassName);

						styleBox.setSelectedItem(styleClassName);
						editedStyleFileMap.put(classFld.getText(), dialog.getUserStyleName());
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	
		// TODO Projections this is a temporary fix to toggle BG button visibility
		//      in the agent style dialog since the GIS wizard also shows it in the
		//      coverage layers dialog.  
		//      It should probably be moved to the display misc options dialog.
		bgcolorBtn = new JButton();
		
		if (showBackgroundButton) {
			builder.addLabel("Display Background Color:", cc.xy(1, 5));	
			builder.add(bgcolorBtn, cc.xy(7, 5));
			bgcolorBtn.setToolTipText("Click to change background color");
			bgcolorBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Color c = JColorChooser.showDialog(StyleClassStep.this, 
							"Select Display Background Color", backgroundColor);
					if (c != null)
						setBackgroundColor(c);
				}
			});
		}
		
		return builder.getPanel();
	}
	
	@Override
	protected void agentListChanged(AgentTypeElement element){
		styleBox.setSelectedItem(element.getValue(STYLE_KEY));
	}

	@Override
	protected void setCurrentElementValues() {
		if (currentIndex != -1 && !(currentIndex >= agentListModel.size())) {
			AgentTypeElement element = (AgentTypeElement) agentListModel.get(currentIndex);

			Object selectedItem = styleBox.getSelectedItem();
			if (selectedItem != null) {
				String styleClassName = selectedItem.toString();
				
				// Set the agent style class
				element.setValue(STYLE_KEY, styleClassName);
				
				// Set the edited agent style data (may be null)
				element.setValue(EDITED_STYLE_KEY, editedStyleFileMap.get(element.getAgentClassName()));
			}
		}
	}

	@Override
	public void applyChanges() {
		setCurrentElementValues();
		DisplayDescriptor descriptor = model.getDescriptor();

		descriptor.getStyles().clear();
		descriptor.getEditedStyles().clear();
		
		for (int i = 0; i < agentListModel.size(); i++) {
			AgentTypeElement element = (AgentTypeElement) agentListModel.get(i);
			descriptor.addStyle(element.getAgentClassName(), 
					(String)element.getValue(STYLE_KEY));
			descriptor.addEditedStyle(element.getAgentClassName(), 
					(String)element.getValue(EDITED_STYLE_KEY));
		}
		descriptor.setBackgroundColor(backgroundColor);
	}

	@Override
	public void initialize() {
		editedStyleFileMap.clear();
		DisplayDescriptor descriptor = model.getDescriptor();
		
		// Check if the display type supports a style editor
		boolean allowStyleEditor = checkForStyleEditor(descriptor);
		buildStyleButton.setEnabled(allowStyleEditor); 
		
		// Find all available styles on classpath for this display type
		List<String>foundStyleClasses = findStylesForDisplay(descriptor);
		
		Collections.sort(foundStyleClasses);
		styleModel.removeAllElements();

		for (String style : foundStyleClasses){
			styleModel.addElement(style);
		}
		
		// Set the agent / style mappings from the current descriptor
	  for (Enumeration<AgentTypeElement> elem = agentListModel.elements(); 
	  		elem.hasMoreElements();){
			
	  	AgentTypeElement e = elem.nextElement();
	  	String className = e.getAgentClassName();
			
			String style = descriptor.getStyleClassName(className);
			String editedStyle = descriptor.getEditedStyleName(className);
			
			if (style == null)
				style = getDefaultStyle(descriptor);
			
			e.setValue(STYLE_KEY, style);
			e.setValue(EDITED_STYLE_KEY, editedStyle);  // may be null
			
			editedStyleFileMap.put(className, editedStyle);
		}

		if (descriptor.getBackgroundColor() != null)
			setBackgroundColor(descriptor.getBackgroundColor());
		else
			setBackgroundColor(getDefaultBackgroundColorForDisplay(descriptor));
	}
	
	/**
	 * Returns true if the display type in the descriptor supports edited styles.
	 * 
	 * @param descriptor
	 * @return
	 */
	protected boolean checkForStyleEditor(DisplayDescriptor descriptor) {
		if (descriptor instanceof CartesianDisplayDescriptor){
			return true;
		}
		// Check the viz registry for support of edited styles for this display type
		else {
			Class<? extends EditedStyleData> defaultEditedStyleClass =
					VisualizationRegistry.getDataFor(descriptor.getDisplayType()).getDefaultEditedStyleDataClass();
			
			if (defaultEditedStyleClass != null) 
				return true;
			
		}
		return false;
	}
	
	/**
	 * Finds all classes that implement the style interface for the type in the descriptor.
	 * 
	 * @param descriptor
	 * @return
	 */
	protected List<String> findStylesForDisplay(DisplayDescriptor descriptor){
		// Find all available style classes for the display type
		List<String> foundStyleClasses = new ArrayList<String>();
		String defaultStyle = getDefaultStyle(descriptor);
		
		// Cartesian displays are explicit here
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			foundStyleClasses = findStylesFor(Style3D.class, defaultStyle);
		} 
		else if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
			foundStyleClasses = findStylesFor(StyleOGL2D.class, defaultStyle);
		} 
		// For other displays, find the style interface from the registry
		else{
			Class<?> styleInterface = VisualizationRegistry.getDataFor(
					descriptor.getDisplayType()).getStyleInterface();

			if (styleInterface != null){
				foundStyleClasses = findStylesFor(styleInterface, defaultStyle);
			} 
			else {
				msg.warn("No style interface defined for" + descriptor.getDisplayType());	
			}
		}
		return foundStyleClasses;
	}
	
	/**
	 * Provides the default style class name for the display defined in the descriptor.
	 * 
	 * @param descriptor
	 * @return
	 */
	protected String getDefaultStyle(DisplayDescriptor descriptor){
		String defaultStyle = null;
	
		// Cartesian default styles are explicit here
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			defaultStyle = DefaultStyle3D.class.getName();
		}
		else if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
			defaultStyle = DefaultStyleOGL2D.class.getName();
		}

		// For other displays, get the default style from the viz registry.
		else{
			VisualizationRegistryData data = VisualizationRegistry.getDataFor(
					descriptor.getDisplayType());

			if (data != null){
				Class<?>[] defaultStyleClasses = data.getDefaultStyles();
				
				if (defaultStyleClasses != null){
					defaultStyle = defaultStyleClasses[0].getName();
				}
			}
			else{
				msg.error("Error creating style step for " + descriptor.getDisplayType() 
						+ ". No visualization registry data found.", null);
			}
		}
		return defaultStyle;
	}
	
	protected Color getDefaultBackgroundColorForDisplay(DisplayDescriptor descriptor) {
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			return Color.BLACK;
		}
		
		// TODO Projections get default BG color from registry or set elswhere
		return Color.WHITE;
	}
	
	/**
	 * Returns the class name for edited styles based on the display type in the descriptor.
	 * 
	 * @param descriptor
	 * @return
	 */
	protected String getEditedStyleClassForDisplay(DisplayDescriptor descriptor){
		String styleClassName = null;
	
		if (descriptor.getDisplayType().equals(DisplayType.TWO_D))
			styleClassName = "repast.simphony.visualization.editedStyle.EditedStyle2D";

		else if (descriptor.getDisplayType().equals(DisplayType.THREE_D))
			styleClassName = "repast.simphony.visualization.editedStyle.EditedStyle3D";

		// For other displays, get the default style from the viz registry.
		else{
			VisualizationRegistryData data = VisualizationRegistry.getDataFor(
					descriptor.getDisplayType());

			if (data != null){
				Class clazz = data.getEditedStyleClass();
				
				if (clazz != null)
					styleClassName = clazz.getName();
			}
			else{
				msg.error("Error creating style step for " + descriptor.getDisplayType() 
						+ ". No visualization registry data found.", null);
			}
		}
		return styleClassName;
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

	public void setShowBackgroundButton(boolean showBackgroundButton) {
		this.showBackgroundButton = showBackgroundButton;
	}
		
	
}