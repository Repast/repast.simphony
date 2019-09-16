package repast.simphony.visualization.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.VisualizationRegistry;
import repast.simphony.visualization.engine.VisualizationRegistryData;
import repast.simphony.visualization.gui.styleBuilder.AbstractStyleDialog;
import repast.simphony.visualization.gui.styleBuilder.SimpleEditedEdgeStyleDialog;
import repast.simphony.visualization.visualization3D.style.DefaultEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualizationOGL2D.DefaultEdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;
import simphony.util.messages.MessageCenter;

/**
 * Display wizard step for network edge styles.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class EdgeStyleStep extends PluginWizardStep {
	private static final long serialVersionUID = 8604762391209707526L;
	private static final MessageCenter msg = MessageCenter.getMessageCenter(EdgeStyleStep.class);
	
	private DisplayWizardModel model;
	private JList objList;
	private DefaultComboBoxModel<String> styleModel;
	private int currentIndex;
	private JComboBox<String> styleBox;
	private Map<String, String> editedStyleFileMap;
	private JButton buildStyleButton;

	protected static Map<Class<?>,List<String>> styleCache = new HashMap<Class<?>,List<String>>();
	
	static class ListElement {
		String netName;
		String styleClassName;
		String editedStyleName;

		public ListElement(String netName, String styleClassName, String editedStyleName) {
			this.netName = netName;
			this.styleClassName = styleClassName;
			this.editedStyleName = editedStyleName;
		}

		public String toString() {
			return netName;
		}
	}

	public EdgeStyleStep() {
		super("Edge Style", "Please provide an edge style for each network in the display");
	}
	
	@Override
	protected  JPanel getContentPanel(){ 
		objList = new JList(new DefaultListModel());
		styleModel = new DefaultComboBoxModel<String>();
		currentIndex = -1;
		editedStyleFileMap = new HashMap<String, String>();
		buildStyleButton = new JButton();
		
		FormLayout layout = new FormLayout(
				"pref, 6dlu, pref, 4dlu, pref, 4dlu, fill:pref:grow, 4dlu, pref",
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, fill:pref:grow");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		final JTextField netNameFld = new JTextField();
		netNameFld.setEditable(false);
		styleBox = new JComboBox<String>(styleModel);

		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Networks", cc.xyw(1, 1, 1));
		builder.add(new JScrollPane(objList), cc.xywh(1, 3, 1, 12));
		builder.addLabel("Network Name:", cc.xyw(3, 3, 7));
		builder.add(netNameFld, cc.xyw(3, 5, 7));
		builder.addLabel("Edge Style Class:", cc.xyw(3, 7, 7));
		builder.add(styleBox, cc.xyw(3, 9, 5));

		builder.add(buildStyleButton, cc.xy(9, 9));

		buildStyleButton.setEnabled(false);  // False unless enabled based on descriptor
		buildStyleButton.setIcon(new ImageIcon(
				getClass().getClassLoader().getResource(StyleStep.EDIT_ICON)));
		buildStyleButton.setToolTipText("Edit the style of the selected edge type");
		buildStyleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Select the dialog based on display type
					AbstractStyleDialog dialog = null;
					
					String userStyleName = model.getDescriptor().getNetworkEditedStyleName(netNameFld.getText());
					
					dialog = new SimpleEditedEdgeStyleDialog((JDialog) SwingUtilities.getWindowAncestor(EdgeStyleStep.this));
				  dialog.init(model.getContext(), netNameFld.getText(), userStyleName, model.getDescriptor());								
					dialog.pack();
					dialog.setVisible(true);
					
					if (dialog.doSave()) {
						// Set the style class name based on display type
						String styleClassName = getEditedStyleClassForDisplay(model.getDescriptor());

						if (styleModel.getIndexOf(styleClassName) < 0)
							styleModel.addElement(styleClassName);

						styleBox.setSelectedItem(styleClassName);
						editedStyleFileMap.put(netNameFld.getText(), dialog.getUserStyleName());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		styleBox.setEditable(true);

		objList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					setElementValues(currentIndex);
					currentIndex = objList.getSelectedIndex();
					if (objList.getSelectedValue() == null) {
						netNameFld.setText("");
					} else {
						ListElement element = (ListElement) objList.getSelectedValue();
						netNameFld.setText(element.netName);
						styleBox.setSelectedItem(element.styleClassName);
					}
				}
			}
		});

		return builder.getPanel();
	}

	@Override
	public void prepare() {
		currentIndex = -1;
		DefaultListModel listModel = (DefaultListModel) objList.getModel();
		listModel.clear();

		DisplayDescriptor descriptor = model.getDescriptor();
	
		// Check if the display type supports a style editor
		boolean allowStyleEditor = checkForStyleEditor(descriptor);
		buildStyleButton.setEnabled(allowStyleEditor); 

		// Find all available style classes for the display type
		List<String>foundStyleClasses = findStylesForDisplay(descriptor);
		
		Collections.sort(foundStyleClasses);
		styleModel.removeAllElements();

		for (String style : foundStyleClasses){
			styleModel.addElement(style);
		}
	
		// Set the style mappings from the current descriptor
		for (ProjectionData proj : descriptor.getProjections()) {
			if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {
				String networkName = proj.getId();
				String style = descriptor.getNetworkStyleClassName(networkName);
				String editedStyle = descriptor.getNetworkEditedStyleName(networkName);
				
				if (style == null) 
					style = getDefaultStyle(descriptor);;
				
				listModel.addElement(new ListElement(networkName, style, editedStyle));
				editedStyleFileMap.put(networkName, editedStyle);
			}
		}

		objList.setSelectedIndex(0);
		setComplete(true);
	}

	private void setCurrentElementValues() {
		setElementValues(currentIndex);
	}

	private void setElementValues(int elementIndex) {
		DefaultListModel listModel = (DefaultListModel) objList.getModel();
		if (elementIndex != -1 && elementIndex < listModel.getSize()) {
			ListElement element = (ListElement) listModel.get(elementIndex);
			
			Object selectedItem = styleBox.getSelectedItem();
			if (selectedItem != null) {
				// Set the network style class
				element.styleClassName = selectedItem.toString();

				// Set the edited network style data (may be null)
				element.editedStyleName = editedStyleFileMap.get(element.netName);
			}
		}
	}

	@Override
	public void applyState() throws InvalidStateException {
		setCurrentElementValues();
		DefaultListModel listModel = (DefaultListModel) objList.getModel();
		DisplayDescriptor descriptor = model.getDescriptor();
		for (int i = 0, n = listModel.size(); i < n; i++) {
			ListElement element = (ListElement) listModel.get(i);
			descriptor.addNetworkStyle(element.netName, element.styleClassName);
			descriptor.addNetworkEditedStyle(element.netName, element.editedStyleName);
		}
	}

	@Override
	public void init(WizardModel wizardModel) {
		this.model = (DisplayWizardModel) wizardModel;
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
			Class<? extends EditedEdgeStyleData> defaultEditedStyleClass =
					VisualizationRegistry.getDataFor(descriptor.getDisplayType()).getDefaultEditedEdgeStyleDataClass();
			
			if (defaultEditedStyleClass != null) 
				return true;
			
		}
		return false;
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
			styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyle2D";

		else if (descriptor.getDisplayType().equals(DisplayType.THREE_D))
			styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyle3D";

		// For other displays, get the default style from the viz registry.
		else{
			VisualizationRegistryData data = VisualizationRegistry.getDataFor(
					descriptor.getDisplayType());

			if (data != null){
				Class clazz = data.getEditedEdgeStyleClass();
				
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
			foundStyleClasses = findStylesFor(EdgeStyle3D.class, defaultStyle);
		} 
		else if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
			foundStyleClasses = findStylesFor(EdgeStyleOGL2D.class, defaultStyle);
		} 
		// For other displays, find the style interface from the registry
		else{
			Class<?> styleInterface = VisualizationRegistry.getDataFor(
					descriptor.getDisplayType()).getEdgeStyleInterface();

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
	
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)){
			defaultStyle = DefaultEdgeStyle3D.class.getName();
		}
		else if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
			defaultStyle = DefaultEdgeStyleOGL2D.class.getName();
		}

		// Get the default style for the display from the viz registry.
		else{
			VisualizationRegistryData data = VisualizationRegistry.getDataFor(descriptor.getDisplayType());

			if (data != null){
				Class<?>[] defaultStyleClasses =  data.getDefaultEdgeStyles();
				
				if (defaultStyleClasses != null){
					defaultStyle = defaultStyleClasses[0].getName();
				}
			}
			else{
				msg.error("Error creating edge style step for " + descriptor.getDisplayType() 
						+ ". No visualization registry data found.", null);
			}
		}
		return defaultStyle;
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
}
