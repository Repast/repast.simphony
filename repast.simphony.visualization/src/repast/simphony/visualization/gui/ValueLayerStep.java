package repast.simphony.visualization.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

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

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.ValueLayerDescriptor;
import repast.simphony.visualization.gui.styleBuilder.EditedValueLayerStyleDialog;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class ValueLayerStep extends PluginWizardStep {
	private static final long serialVersionUID = -60523735303617830L;
	private static final String EDIT_ICON = "edit.png";
	private DisplayWizardModel model;
	private JList objList;
	private DefaultComboBoxModel styleModel;
	private JComboBox styleBox;
	private JButton buildStyleButton;
	private String editedStyleName;

	public ValueLayerStep() {
		super("Value Layer Details", "Please provide a style for the value layers below. " +
		"These value layers will be passed to the selected style.");
	}

	@Override
	protected JPanel getContentPanel(){
		objList = new JList(new DefaultListModel());
		styleModel = new DefaultComboBoxModel();
		buildStyleButton = new JButton();
		
		FormLayout layout = new FormLayout("50, 3dlu, 50, 3dlu, 50, pref:grow",
		"6dlu, pref, 3dlu, pref, 3dlu, pref, pref:grow");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		final JTextField classFld = new JTextField();
		classFld.setEditable(false);
		styleBox = new JComboBox(styleModel);
		styleBox.setEditable(true);

		CellConstraints cc = new CellConstraints();
		builder.add(new JScrollPane(objList), cc.xywh(1,1,3,7));
		builder.addLabel("Value Layer Style Class Name:", cc.xyw(5,2,2));
		builder.add(styleBox, cc.xyw(5,4,2));
		
		builder.add(buildStyleButton, cc.xy(5,6));
		buildStyleButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(EDIT_ICON)));
		buildStyleButton.setToolTipText("Edit the style of the selected agent type");
		buildStyleButton.addActionListener(new editBtnListener());
				
		styleBox.setEditable(true);
		styleBox.setPrototypeDisplayValue("repast.simphony.visualization.visualization3D.style.DefaultStyle3D");
		objList.setPrototypeCellValue("SimpleHappyAgent");

		return builder.getPanel();
	}

	@Override
	public void init(WizardModel wizardModel) {
		this.model = (DisplayWizardModel) wizardModel;
	}

	@Override
	public void applyState() throws InvalidStateException {
		DisplayDescriptor descriptor = model.getDescriptor();
		((ValueLayerDescriptor)descriptor).setValueLayerStyleName(styleBox.getSelectedItem().toString());
		((ValueLayerDescriptor)descriptor).setValueLayerEditedStyleName(editedStyleName);
	}

	@Override
	public void prepare() {
		DisplayDescriptor descriptor = model.getDescriptor();

		List<String> style2DCache = null;
		List<String> style3DCache = null;
		
		// TODO Projections: init with viz registry data entries
		java.util.List<String> vals;
		if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
			if (style3DCache == null) 
				style3DCache = ClassFinder.getFoundClasses(model.getContext(), ValueLayerStyle3D.class);
			
			vals = style3DCache;
		} else {
			if (style2DCache == null) 
				style2DCache = ClassFinder.getFoundClasses(model.getContext(), ValueLayerStyleOGL.class);
			
			vals = style2DCache;
		}

		String styleName = ((ValueLayerDescriptor)descriptor).getValueLayerStyleName();
		if (styleName != null && !vals.contains(styleName)) 
			vals.add(styleName);
		
		Collections.sort(vals);
		styleModel.removeAllElements();
		for (String val : vals) 
			styleModel.addElement(val);
		
		if (styleName != null) styleBox.setSelectedItem(styleName);

		DefaultListModel listModel = (DefaultListModel) objList.getModel();
		listModel.removeAllElements();
		for (String name : ((ValueLayerDescriptor)descriptor).getValueLayerNames()) 
			listModel.addElement(name);

		setComplete(true);
	}
	
	class editBtnListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DisplayDescriptor descriptor = model.getDescriptor();
			
			try {
				EditedValueLayerStyleDialog dialog = new EditedValueLayerStyleDialog((JDialog) 
						SwingUtilities.getWindowAncestor(ValueLayerStep.this));

				editedStyleName = ((ValueLayerDescriptor)descriptor).getValueLayerEditedStyleName();

				String layerName = ((ValueLayerDescriptor)descriptor).getValueLayerNames().iterator().next();
				
				dialog.init(layerName, editedStyleName, (CartesianDisplayDescriptor)model.getDescriptor());
				dialog.pack();
				dialog.setVisible(true);

				if (dialog.doSave()){

					// Set the style class name based on display type
					String styleClassName = null;

					// TODO Projections: get the style class from viz registry data 
					
					if (model.getDescriptor().getDisplayType().equals(DisplayType.TWO_D))
						styleClassName = "repast.simphony.visualization.editedStyle.EditedValueLayerStyle2D";

					else if (model.getDescriptor().getDisplayType().equals(DisplayType.THREE_D))
						styleClassName = "repast.simphony.visualization.editedStyle.EditedValueLayerStyle3D";

					if (styleModel.getIndexOf(styleClassName) < 0) 
						styleModel.addElement(styleClassName);

					styleBox.setSelectedItem(styleClassName);

					editedStyleName = dialog.getUserStyleName();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}			
		}
	}
}