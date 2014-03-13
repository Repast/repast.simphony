package repast.simphony.visualization.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.gui.styleBuilder.EditedEdgeStyleDialog;
import repast.simphony.visualization.gui.styleBuilder.SimpleEditedEdgeStyleDialog;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @author Eric Tatara
 */
public class EdgeStyleStep extends PanelWizardStep {
	private static final long serialVersionUID = 8604762391209707526L;

	private DisplayWizardModel model;
	private JList objList = new JList(new DefaultListModel());
	private DefaultComboBoxModel styleModel = new DefaultComboBoxModel();
	private int currentIndex = -1;
	private JComboBox styleBox;
	private Map<String, String> editedStyleFileMap = new HashMap<String, String>();
	private JButton buildStyleButton = new JButton();
	private String defaultStyle;

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
		this.setLayout(new BorderLayout());
		FormLayout layout = new FormLayout(
				"pref, 6dlu, pref, 4dlu, pref, 4dlu, fill:pref:grow, 4dlu, pref",
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, fill:pref:grow");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		final JTextField netNameFld = new JTextField();
		netNameFld.setEditable(false);
		styleBox = new JComboBox(styleModel);

		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Networks", cc.xyw(1, 1, 1));
		builder.add(new JScrollPane(objList), cc.xywh(1, 3, 1, 12));
		builder.addLabel("Network Name:", cc.xyw(3, 3, 7));
		builder.add(netNameFld, cc.xyw(3, 5, 7));
		builder.addLabel("Edge Style Class:", cc.xyw(3, 7, 7));
		builder.add(styleBox, cc.xyw(3, 9, 5));

		builder.add(buildStyleButton, cc.xy(9, 9));

		buildStyleButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				StyleStep.EDIT_ICON)));
		buildStyleButton.setToolTipText("Edit the style of the selected edge type");
		buildStyleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO Projections: Setup the style step based on data from the viz registry
				try {
					if (model.getDescriptor().getDisplayType().equals(DisplayType.TWO_D)) {
						SimpleEditedEdgeStyleDialog dialog = new SimpleEditedEdgeStyleDialog(
								(JDialog) SwingUtilities.getWindowAncestor(EdgeStyleStep.this));

						String userStyleName = model.getDescriptor().getNetworkEditedStyleName(
								netNameFld.getText());

						dialog.init(model.getContext(), netNameFld.getText(), userStyleName,
								model.getDescriptor());
						dialog.pack();
						dialog.setVisible(true);

						if (dialog.doSave()) {

							// Set the style class name based on display type
							String styleClassName;

							if (model.getDescriptor().getDisplayType().equals(DisplayType.TWO_D))
								styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyle2D";
							else if (model.getDescriptor().getDisplayType().equals(DisplayType.THREE_D))
								styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyle3D";
							else
								styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyleGIS3D";

							if (styleModel.getIndexOf(styleClassName) < 0)
								styleModel.addElement(styleClassName);

							styleBox.setSelectedItem(styleClassName);

							editedStyleFileMap.put(netNameFld.getText(), dialog.getUserStyleName());
						}

					} else {

						EditedEdgeStyleDialog dialog = new EditedEdgeStyleDialog((JDialog) SwingUtilities
								.getWindowAncestor(EdgeStyleStep.this));

						String userStyleName = model.getDescriptor().getNetworkEditedStyleName(
								netNameFld.getText());

						dialog.init(model.getContext(), netNameFld.getText(), userStyleName,
								model.getDescriptor());
						dialog.pack();
						dialog.setVisible(true);

						if (dialog.doSave()) {

							// Set the style class name based on display type
							String styleClassName;

							if (model.getDescriptor().getDisplayType().equals(DisplayType.TWO_D))
								styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyle2D";
							else if (model.getDescriptor().getDisplayType().equals(DisplayType.THREE_D))
								styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyle3D";
							else
								styleClassName = "repast.simphony.visualization.editedStyle.EditedEdgeStyleGIS3D";

							if (styleModel.getIndexOf(styleClassName) < 0)
								styleModel.addElement(styleClassName);

							styleBox.setSelectedItem(styleClassName);

							editedStyleFileMap.put(netNameFld.getText(), dialog.getUserStyleName());
						}
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

		add(builder.getPanel(), BorderLayout.CENTER);

	}

	@Override
	public void prepare() {
		currentIndex = -1;
		DefaultListModel listModel = (DefaultListModel) objList.getModel();
		listModel.clear();

		DisplayDescriptor descriptor = model.getDescriptor();

		// TODO Projections: get init from the viz registry
		// TODO implement stylE editor for 3D GIS
		if (descriptor.getDisplayType().equals(DisplayType.GIS3D)) {
			buildStyleButton.setEnabled(false);
		}

		if (descriptor.getDisplayType().equals(DisplayType.THREE_D))
			defaultStyle = descriptor.getDefaultNetStyles3D()[0].getName();

		else //if (descriptor.getDisplayType() == DisplayDescriptor.DisplayType.TWO_D)
			defaultStyle = descriptor.getDefaultNetStyles2D()[0].getName();

		// TODO WWJ - networks
		//    else
		//      defaultStyle = descriptor.getDefaultNetStylesGIS3D()[0].getName();

		List<String> style2DCache = null;
		List<String> style3DCache = null;
		List<String> style3DGISCache = null;

		java.util.List<String> vals;
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			if (style3DCache == null)
				style3DCache = StyleClassFinder.getAvailable3DEdgeStyles(model.getContext());

			vals = style3DCache;
		} 
		else {//if (descriptor.getDisplayType() == DisplayDescriptor.DisplayType.TWO_D) {
			if (style2DCache == null)
				style2DCache = StyleClassFinder.getAvailable2DEdgeStyles(model.getContext());

			vals = style2DCache;
		} 
		// TODO WWJ - network
		//    else {
		//      if (style3DGISCache == null)
		//        style3DGISCache = StyleClassFinder.getAvailable3DGISEdgeStyles(model.getContext());
		//
		//      vals = style3DGISCache;
		//    }

		vals.add(defaultStyle);
		Collections.sort(vals);
		styleModel.removeAllElements();

		for (String val : vals)
			styleModel.addElement(val);

		for (ProjectionData proj : model.getDescriptor().getProjections()) {
			if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {
				String name = proj.getId();
				String style = descriptor.getNetworkStyleClassName(name);
				String editedStyle = descriptor.getNetworkEditedStyleName(name);
				if (style == null) {
					style = defaultStyle;
				}
				listModel.addElement(new ListElement(name, style, editedStyle));
				editedStyleFileMap.put(name, editedStyle);
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
			Object obj = styleBox.getSelectedItem();
			if (obj != null)
				element.styleClassName = obj.toString();

			if ("repast.simphony.visualization.editedStyle.EditedEdgeStyle2D"
					.equals(element.styleClassName)
					|| "repast.simphony.visualization.editedStyle.EditedEdgeStyle3D"
					.equals(element.styleClassName)
					|| "repast.simphony.visualization.editedStyle.EditedEdgeStyleGIS3D"
					.equals(element.styleClassName))

				element.editedStyleName = editedStyleFileMap.get(element.netName);
			else
				element.editedStyleName = null;

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
}
