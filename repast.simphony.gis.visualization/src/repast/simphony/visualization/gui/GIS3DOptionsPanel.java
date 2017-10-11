package repast.simphony.visualization.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.gis3D.RepastStereoOptionSceneController;

/**
 * Panel for GIS options step
 * 
 * @author Eric Tatara
 *
 * TODO GIS Agent tracking is very inconsistent so disabled for now.
 */
public class GIS3DOptionsPanel extends JPanel {

	// List of available WWJ layers in Basic Model
	static LayerList DEFAULT_GLOBE_LAYERS = new BasicModel().getLayers();
	
	private static final long serialVersionUID = 5139522879873045768L;
	protected GISDisplayDescriptor descriptor;
	private JComboBox<GISDisplayDescriptor.VIEW_TYPE> typeBox;
	private JComboBox<RepastStereoOptionSceneController.RenderQuality> renderQualityBox;
//	private JCheckBox trackAgentsCheckBox = new JCheckBox();
	
	protected JTable layerSelectTable;
	
	public GIS3DOptionsPanel() {
		setLayout(new BorderLayout());
    FormLayout layout = new FormLayout(
    		"right:pref, 3dlu, pref, 3dlu, pref, 3dlu, pref:grow", // columns
        "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref"); // rows
    
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();
    CellConstraints cc = new CellConstraints();
    
    // General settings
    builder.addSeparator("General", cc.xyw(1, 1, 7));
    builder.addLabel("View Type:", cc.xy(1, 3));
    typeBox = new JComboBox();
    builder.add(typeBox, cc.xy(3, 3));
//    builder.addLabel("Track agents:", cc.xy(1, 5));
//    builder.add(trackAgentsCheckBox, cc.xy(3, 5));
    
    builder.addLabel("Render Quality:", cc.xy(1, 5));
    
    renderQualityBox = new JComboBox();
    builder.add(renderQualityBox, cc.xy(3, 5));
    
    // Layer settings
    builder.addSeparator("Background Layers", cc.xyw(1, 7, 7));
    
    // Create a 3-column table for WWJ default layers 
    // Override the DefaultTableModel to allow editing of boolean columns 0, 3
    //  and column class Boolean on column 0, 3 to enable checkboxes
    DefaultTableModel model = new DefaultTableModel() {
    	
    	@Override
    	public Class getColumnClass(int column) {
    		if (column == 1) return String.class;
    		else return Boolean.class;
    	}
    	
    	@Override
    	public boolean isCellEditable(int row, int column) {
    		if (column == 1) return false;
    		else return true;
    	}
    };
    
    // Listener repaints whenever checkbox data changes for row highlighting
    model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent arg0) {
				repaint();
			}
    });
    
    // Override the default table rendering to highlight rows based on layer
    //  selection values
    layerSelectTable = new JTable(model) {

    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
    		Component comp = super.prepareRenderer(renderer, row, col);
    		
    		 Boolean indisplay = (Boolean)getModel().getValueAt(row, 0);
         Boolean enabled = (Boolean)getModel().getValueAt(row, 2);
   
         // Included layer rows are gray
         if (indisplay && !enabled) {
        	 comp.setBackground(Color.LIGHT_GRAY);
         }
         // Included and enabled layer rows are green
         else if (indisplay && enabled) {
        	 comp.setBackground(Color.GREEN); 
         }
         else {
        	 comp.setBackground(Color.WHITE);
         }
         return comp;
    	}
    };
    
		model.setColumnCount(3);
		model.setColumnIdentifiers(new String[] {"Include", "Layer Name", "Enabled"});
		layerSelectTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		layerSelectTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		layerSelectTable.getColumnModel().getColumn(2).setPreferredWidth(50);
    

    layerSelectTable.setRowSelectionAllowed(false);
    layerSelectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    layerSelectTable.setFillsViewportHeight(true);
    layerSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    layerSelectTable.setDragEnabled(false);
    layerSelectTable.setPreferredScrollableViewportSize(new Dimension(400, 130)); 
    
    JScrollPane scrollPane = new JScrollPane(layerSelectTable);
    scrollPane.getViewport().add(layerSelectTable);
    
		JPanel panel = new JPanel();
		panel.add(scrollPane, BorderLayout.CENTER);
		builder.add(panel, cc.xyw(1, 9, 7));
    add(builder.getPanel(), BorderLayout.WEST);
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
		
		DefaultComboBoxModel<RepastStereoOptionSceneController.RenderQuality> rendermodel = 
				new DefaultComboBoxModel<RepastStereoOptionSceneController.RenderQuality>();
	
		for (RepastStereoOptionSceneController.RenderQuality type : RepastStereoOptionSceneController.RenderQuality.values()){
			rendermodel.addElement(type);		
		}
		renderQualityBox.setModel(rendermodel);
		renderQualityBox.setSelectedItem(descriptor.getRenderQuality());
		renderQualityBox.setToolTipText("Sets the display render quality for agents and networks.");
		
		Map<String,Boolean> globeLayerMap = descriptor.getGlobeLayersMap();
		
		DefaultTableModel model = (DefaultTableModel)layerSelectTable.getModel();
		model.setRowCount(DEFAULT_GLOBE_LAYERS.size());
		
		int row=0;
		for (Layer layer : DEFAULT_GLOBE_LAYERS) {
			
			Boolean indisplay = false;
			Boolean enabled = false;
			if (globeLayerMap.get(layer.getName()) != null){
				indisplay = true;
				enabled = globeLayerMap.get(layer.getName());
			}
			
			model.setValueAt(indisplay, row, 0);
			model.setValueAt(layer.getName(), row, 1);
			model.setValueAt(enabled, row, 2);
			row++;
		}
		
		
//		trackAgentsCheckBox.setSelected(descriptor.getTrackAgents());
//		trackAgentsCheckBox.setToolTipText("Keep agents in view by tracking the view.");
	}

	 public void applyChanges() {
		 descriptor.setViewType((GISDisplayDescriptor.VIEW_TYPE)typeBox.getSelectedItem());
//		 descriptor.setTrackAgents(trackAgentsCheckBox.isSelected());
		 descriptor.setRenderQuality(
				 (RepastStereoOptionSceneController.RenderQuality)renderQualityBox.getSelectedItem());
		 
		 int numRow = layerSelectTable.getModel().getRowCount();
		 for (int i=0; i<numRow; i++) {
			 boolean include = (boolean)layerSelectTable.getModel().getValueAt(i, 0);
			 
			 if (include) {
				 String layerName = (String)layerSelectTable.getModel().getValueAt(i, 1);
				 boolean enabled = (boolean)layerSelectTable.getModel().getValueAt(i, 2);
				 descriptor.addGlobeLayer(layerName,enabled);
			 }
		 }
	 }
}