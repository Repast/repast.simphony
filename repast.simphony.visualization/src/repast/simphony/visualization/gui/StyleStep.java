package repast.simphony.visualization.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.widget.SquareIcon;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.gui.styleBuilder.EditedStyleDialog;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @author Eric Tatara
 */
public class StyleStep extends PanelWizardStep {

	private static final long serialVersionUID = 1179847211916347928L;
	public static final String UP_ICON = "agent_up.png";
	public static final String DOWN_ICON = "agent_down.png";
	public static final String EDIT_ICON = "edit.png";
	public static final String ADD_ICON = "edit_add.png";
	public static final String REMOVE_ICON = "edit_remove.png";

	private DisplayWizardModel model;

	private JList agentList;
	private DefaultListModel listModel;
	private boolean reordering = false;

	private DefaultComboBoxModel styleModel = new DefaultComboBoxModel();
	private JButton buildStyleButton = new JButton();
	private JButton bgcolorBtn = new JButton();
	private int currentIndex = -1;
	private JComboBox styleBox;
	private Map<String, String> editedStyleFileMap = new HashMap<String, String>();
	private String defaultStyle;
	private Color backgroundColor;

	static class AgentTypeElement {
		String agentName;
		String agentClassName;
		String styleClassName;
		String editedStyleName;

		public AgentTypeElement(String agentName, String agentClassName, String styleClassName,
				String editedStyleName) {

			this.agentName = agentName;
			this.agentClassName = agentClassName;
			this.styleClassName = styleClassName;
			this.editedStyleName = editedStyleName;
		}

		public String toString() {
			return agentName;
		}
	}

	public StyleStep() {
		super("Agent Style", "Please provide a style for each agent type in the model");
		this.setLayout(new BorderLayout());
		FormLayout layout = new FormLayout("pref, 6dlu, pref, 4dlu, pref, 4dlu, fill:pref:grow, 4dlu, pref",
		"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, fill:pref:grow");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		listModel = new DefaultListModel();
		agentList = new JList(listModel);
		agentList.setToolTipText("This is the list of agents currently defined for" + " this display");

		final JTextField classFld = new JTextField();
		classFld.setEditable(false);
		classFld.setToolTipText("This is the class name for the selected agent");
		styleBox = new JComboBox(styleModel);
		styleBox.setToolTipText("This is the style class for the selected agent. "
				+ "Select a style from this box or use the editor to create a new style");
		styleBox.setEditable(true);
		
		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Agents", cc.xyw(1, 1, 1));
		//builder.add(new JLabel("Foreground"), cc.xyw(1, 5, 3));
		builder.add(new JScrollPane(agentList), cc.xywh(1, 3, 1, 12));
		agentList.setVisibleRowCount(14);
		//builder.add(new JLabel("Background"), cc.xyw(1, 17, 3));

		// The right side has the agent and style class names and the edit, remove,
		// and add buttons.
		builder.addLabel("Agent Class:", cc.xyw(3, 3, 7));
		builder.add(classFld, cc.xyw(3, 5, 7));
		builder.addLabel("Style Class:", cc.xyw(3, 7, 7));
		builder.add(styleBox, cc.xyw(3, 9, 5));

		builder.add(buildStyleButton, cc.xy(9, 9));

		buildStyleButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(EDIT_ICON)));
		buildStyleButton.setToolTipText("Edit the style of the selected agent type");
		buildStyleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					EditedStyleDialog dialog = new EditedStyleDialog((JDialog) SwingUtilities
							.getWindowAncestor(StyleStep.this));

					String editedStyleName = model.getDescriptor().getEditedStyleName(classFld.getText());

					dialog.init(Class.forName(classFld.getText()), editedStyleName, model.getDescriptor());
					dialog.pack();
					dialog.setVisible(true);

					if (dialog.doSave()) {

						// Set the style class name based on display type
						String styleClassName;

						// TODO Projections: get the style class name from the viz registry data
						
						if (model.getDescriptor().getDisplayType().equals(DisplayType.TWO_D))
							styleClassName = "repast.simphony.visualization.editedStyle.EditedStyle2D";

						else if (model.getDescriptor().getDisplayType().equals(DisplayType.THREE_D))
							styleClassName = "repast.simphony.visualization.editedStyle.EditedStyle3D";

						// TODO Projections: GIS
						else
							styleClassName = "repast.simphony.visualization.editedStyle.EditedStyleGIS3D";

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

		builder.addLabel("Display Background Color:", cc.xy(3, 11));
		builder.add(bgcolorBtn, cc.xy(5, 11));
		bgcolorBtn.setToolTipText("Click to change background color");
		bgcolorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Color c = JColorChooser.showDialog(StyleStep.this, "Select Display Background Color",
						backgroundColor);
				if (c != null)
					setBackgroundColor(c);
			}
		});

		agentList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting() && !reordering) {
					setCurrentElementValues();
					currentIndex = agentList.getSelectedIndex();

					if (agentList.getSelectedValue() == null) {
						classFld.setText("");
					} else {
						AgentTypeElement element = (AgentTypeElement) agentList.getSelectedValue();
						classFld.setText(element.agentClassName);
						styleBox.setSelectedItem(element.styleClassName);
					}
				}
				reordering = false;
			}
		});

		add(builder.getPanel(), BorderLayout.CENTER);
	}

	private void setCurrentElementValues() {
		if (currentIndex != -1 && !(currentIndex >= listModel.size())) {
			AgentTypeElement element = (AgentTypeElement) listModel.get(currentIndex);

			Object selectedItem = styleBox.getSelectedItem();
			if (selectedItem != null) {
				element.styleClassName = selectedItem.toString();

				if ("repast.simphony.visualization.editedStyle.EditedStyle2D"
						.equals(element.styleClassName)
						|| "repast.simphony.visualization.editedStyle.EditedStyle3D"
						.equals(element.styleClassName)
						|| "repast.simphony.visualization.editedStyle.EditedStyleGIS3D"
						.equals(element.styleClassName))

					element.editedStyleName = editedStyleFileMap.get(element.agentClassName);
				else
					element.editedStyleName = null;
			}
		}
	}

	@Override
	public void init(WizardModel wizardModel) {
		this.model = (DisplayWizardModel) wizardModel;
	}

	@Override
	public void applyState() throws InvalidStateException {
		setCurrentElementValues();
		DisplayDescriptor descriptor = model.getDescriptor();

		descriptor.getStyles().clear();
		descriptor.getEditedStyles().clear();
		
		for (int i = 0; i < listModel.size(); i++) {
			AgentTypeElement element = (AgentTypeElement) listModel.get(i);
			descriptor.addStyle(element.agentClassName, element.styleClassName);
			descriptor.addEditedStyle(element.agentClassName, element.editedStyleName);
		}
		
		descriptor.setBackgroundColor(backgroundColor);
	}

	@Override
	public void prepare() {
		currentIndex = -1;
		listModel.clear();
		editedStyleFileMap.clear();
		defaultStyle = null;

		DisplayDescriptor descriptor = model.getDescriptor();

		// TODO Projections: init from viz registry data.
		
		// TODO implement style editor for 3D GIS
		if (descriptor.getDisplayType().equals(DisplayType.GIS3D)) {
			buildStyleButton.setEnabled(false);
		}

		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			defaultStyle = descriptor.getDefaultStyles3D()[0].getName();
			setBackgroundColor(Color.BLACK);
		}
		
		// TODO Projections: GIS
		// TODO WWJ - handle multiple styles
		else if (descriptor.getDisplayType().equals(DisplayType.GIS3D))
			defaultStyle = descriptor.getDefaultStylesGIS3D()[0].getName();

		else {
			defaultStyle = descriptor.getDefaultStyles2D()[0].getName();
			setBackgroundColor(Color.WHITE);
		}
		
		List<String> style2DCache = null;
		List<String> style3DCache = null;
		List<String> styleGIS3DCache = null;

		// TODO Projections: init from viz registry data entries
		//        The viz type should return a style interface from which 
		//        implementing classes will be searched.
		// Find all available style classes for the display type
		java.util.List<String> vals;
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			if (style3DCache == null)
				style3DCache = StyleClassFinder.getAvailable3DStyles(model.getContext());

			vals = style3DCache;
		} else if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
			if (style2DCache == null)
				style2DCache = StyleClassFinder.getAvailable2DStyles(model.getContext());

			vals = style2DCache;
		} else {
			if (styleGIS3DCache == null)
				styleGIS3DCache = StyleClassFinder.getAvailableGIS3DStyles(model.getContext());

			vals = styleGIS3DCache;
		}

		vals.add(defaultStyle);
		Collections.sort(vals);
		styleModel.removeAllElements();

		for (String val : vals)
			styleModel.addElement(val);

		// Get the list of available agent for display, sorted by order.
		Map<String,Integer> layers = descriptor.getLayerOrders();
		List<String> tempList = new ArrayList<String>();
		
		Map<Integer,String> layerMapInv = new HashMap<Integer,String>();
		List<Integer> orders = new ArrayList<Integer>();
		
		for (String className : layers.keySet()){
			layerMapInv.put(layers.get(className),className);
			orders.add(layers.get(className));
		}
	
		Collections.sort(orders);
		for (int i : orders)
			tempList.add(i, layerMapInv.get(i));
		
	  // NOTE that order layer starts with background at index 0, while the 
		// lists should be displayed with the foreground at index 0, so it needs 
		// to be reversed.
		Collections.reverse(tempList);
	  for (String className : tempList){
			String agentLabel = getShortName(className);
			String style = descriptor.getStyleClassName(className);
			String editedStyle = descriptor.getEditedStyleName(className);
			
			if (style == null)
				style = defaultStyle;
			
			listModel.addElement(new AgentTypeElement(agentLabel,
					className, style, editedStyle));
			
			editedStyleFileMap.put(className, editedStyle);
		}

		agentList.setSelectedIndex(0);

		if (descriptor.getBackgroundColor() != null)
			setBackgroundColor(descriptor.getBackgroundColor());

		setComplete(true);
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		bgcolorBtn.setIcon(new SquareIcon(14, 14, backgroundColor));
	}
	
	 private String getShortName(String className) {
	    int index = className.lastIndexOf(".");
	    if (index != -1 && index != className.length() - 1) {
	      return className.substring(index + 1, className.length());
	    }
	    return null;
	 }
}