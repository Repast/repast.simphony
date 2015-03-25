package repast.simphony.visualization.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.engine.DisplayDescriptor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel for GIS options step
 * 
 * @author Eric Tatara
 *
 * TODO GIS Agent tracking is very inconsistent so disabled for now.
 */
public class GIS3DOptionsPanel extends JPanel {

	private static final long serialVersionUID = 5139522879873045768L;
	protected GISDisplayDescriptor descriptor;
	private JComboBox<GISDisplayDescriptor.VIEW_TYPE> typeBox = new JComboBox();
//	private JCheckBox trackAgentsCheckBox = new JCheckBox();
	
	public GIS3DOptionsPanel() {
		setLayout(new BorderLayout());
    FormLayout layout = new FormLayout(
    		"right:pref, 3dlu, pref, 3dlu, pref, 3dlu, pref:grow", // columns
        "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref"); // rows
    
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();
    CellConstraints cc = new CellConstraints();
    
    // General settings
    builder.addSeparator("General", cc.xyw(1, 1, 7));
    builder.addLabel("View Type:", cc.xy(1, 3));
    builder.add(typeBox, cc.xy(3, 3));
//    builder.addLabel("Track agents:", cc.xy(1, 5));
//    builder.add(trackAgentsCheckBox, cc.xy(3, 5));
    
    
    // Layer settings
    builder.addSeparator("Layers", cc.xyw(1, 7, 7));
    
//    java.util.List<DisplayItem> list = new ArrayList<DisplayItem>();
//    selector = new ListSelector<DisplayItem>(list, list, false);
//    builder.addSeparator("Projections and Value Layers", cc.xyw(1, 7, 3));
//    builder.add(selector.getPanel(), cc.xyw(1, 9, 3));

    add(builder.getPanel(), BorderLayout.CENTER);
		

	}

	public void init(ContextData context, GISDisplayDescriptor descriptor) {
		if (!(descriptor instanceof GISDisplayDescriptor)) {
			throw new IllegalArgumentException("Descriptor must be an instance of GISDisplayDescriptor.");
		}
		this.descriptor = (GISDisplayDescriptor)descriptor;
		
		DefaultComboBoxModel<GISDisplayDescriptor.VIEW_TYPE> cmbModel = 
				new DefaultComboBoxModel<GISDisplayDescriptor.VIEW_TYPE>();
	
		for (GISDisplayDescriptor.VIEW_TYPE type : GISDisplayDescriptor.VIEW_TYPE.values()){
			cmbModel.addElement(type);		
		}
		typeBox.setModel(cmbModel);
		typeBox.setSelectedItem(descriptor.getViewType());
		typeBox.setToolTipText("Sets the map to a globe sphere or flat.");
		
//		trackAgentsCheckBox.setSelected(descriptor.getTrackAgents());
//		trackAgentsCheckBox.setToolTipText("Keep agents in view by tracking the view.");
	}

	 public void done() {
		 descriptor.setViewType((GISDisplayDescriptor.VIEW_TYPE)typeBox.getSelectedItem());
//		 descriptor.setTrackAgents(trackAgentsCheckBox.isSelected());
	 }
}