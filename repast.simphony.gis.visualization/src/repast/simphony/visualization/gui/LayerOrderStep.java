package repast.simphony.visualization.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * Style step for layer ordering in GIS display.
 *   
 * @author Eric Tatara
 */
public class LayerOrderStep extends PluginWizardStep {	

	private static final long serialVersionUID = -4242831160959132358L;
	
	// TODO move to a viz constants class
	public static final String UP_ICON = "agent_up.png";
	public static final String DOWN_ICON = "agent_down.png";
	
	private static final String AGENT_KEY = "agent";
	
	protected DisplayWizardModel model;

	protected JList<ListModelElement> layerList;
	protected DefaultListModel<ListModelElement> layerListModel;
	
	protected int currentIndex = -1;
	
	/**
	 * Custom ListCellRenderer that provides neat display names for layers.
	 *
	 */
	protected static class ListDataRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, 
				int index, boolean isSelected, boolean cellHasFocus) {

			ListModelElement e = (ListModelElement)value;
			String name = e.getName();

			if (e.getValue(AGENT_KEY) != null && (Boolean)e.getValue(AGENT_KEY)) {
				int i = name.lastIndexOf(".");
				if (i != -1 && i != name.length() - 1) {
					value = name.substring(i + 1, name.length());
				}
				else {
					value = name;
				}
			}

			return super.getListCellRendererComponent(list, value, index, 
					isSelected, cellHasFocus);
		}
	}
	
	/**
	 * Stores list model data
	 */
	public static class ListModelElement {
		private String name;
		
		
		private Map<String,Object> values = new HashMap<String,Object>();

		public ListModelElement(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		public Object getValue(String key){
			return values.get(key);
		}
		
		public void setValue(String key, Object value){
			values.put(key, value);
		}

		public String getName() {
			return name;
		}
	}

	public LayerOrderStep() {
		super("Layer Order", "Please specify all layer orders in the dislpay.");		
	}
	
	@Override
	protected JPanel getContentPanel(){
		// Columns: Agent List | gap | Control panel
		FormLayout layout = new FormLayout(
				// 150dlu is wide enough - if you set pref:grow the scrollbar never shows
	// col    1      2       3			
				"150dlu, 6dlu, pref:grow",  
				
	// row    1    2             3      4      5     		
				"pref, pref, fill:pref:grow, pref, pref"); 
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		layerListModel = new DefaultListModel<ListModelElement>();
		layerList = new JList<ListModelElement>(layerListModel);
		layerList.setToolTipText("List of currently defined layers");
		layerList.setCellRenderer(new ListDataRenderer());
		
		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Layers", cc.xyw(1, 1, 1));
		builder.addLabel("Foreground", cc.xyw(1, 2, 1));
		
		builder.add(new JScrollPane(layerList), cc.xyw(1, 3, 1));
		layerList.setVisibleRowCount(12);
		
		builder.addLabel("Background", cc.xyw(1,4,1));

		builder.add(getControlPanel(), cc.xy(3, 3));

		return builder.getPanel();
	}

	/**
	 * Provide a JPanel with controls for the layer list
	 * 
	 * @return the style editor panel
	 */
	protected JPanel getControlPanel(){
		
		// Columns: Style class | | | edit button
		FormLayout layout = new FormLayout(		
				"30dlu, 4dlu, pref, 4dlu, pref, 4dlu, pref:grow",  // columns
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref:grow");  // rows
		
		PanelBuilder builder = new PanelBuilder(layout);

		JButton upBtn = new JButton();
	  JButton downBtn = new JButton();
		
	  upBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(LayerOrderStep.UP_ICON)));
    upBtn.setToolTipText("Move the selected agent towards the foreground");
    downBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(LayerOrderStep.DOWN_ICON)));
    downBtn.setToolTipText("Move the selected agent towards the background");

    upBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) layerList.getModel();
        Object obj = layerList.getSelectedValue();
        int newIndex = model.indexOf(obj) - 1;
        if (newIndex > -1) {
          model.removeElement(obj);
          model.insertElementAt(obj, newIndex);
          layerList.setSelectedValue(obj, true);
        }
      }
    });

    downBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) layerList.getModel();
        Object obj = layerList.getSelectedValue();
        int newIndex = model.indexOf(obj) + 1;
        if (newIndex < model.size()) {
          model.removeElement(obj);
          model.insertElementAt(obj, newIndex);
          layerList.setSelectedValue(obj, true);
        }
      }
    });
	  
		CellConstraints cc = new CellConstraints();
		
		builder.add(upBtn, cc.xyw(1, 1, 1));
		builder.add(downBtn, cc.xyw(1, 3, 1));
		
		return builder.getPanel();
	}
	
	
	/**
	 * Apply the editor changes to the DisplayDescriptor.
	 */
	protected void applyChanges() {
		GISDisplayDescriptor descriptor = (GISDisplayDescriptor)model.getDescriptor();

		descriptor.getLayerOrders().clear();
		
	// TODO GIS descriptor set layer orders
		
		// NOTE that order layer starts with background at index 0, while the
    // lists should be displayed with the foreground at index 0, so it needs
    // to be reversed.
		 for (int i = 0; i < layerListModel.getSize(); i++) {
			 ListModelElement e = (ListModelElement) layerListModel.getElementAt(i);
       
       // Layer order reverse of list order
       descriptor.addLayerOrder(e.getName(), layerListModel.getSize() - i - 1);
     }
	}
	
	public void initialize() {
		GISDisplayDescriptor descriptor = (GISDisplayDescriptor)model.getDescriptor();
		
		// NOTE that order layer starts with background at index 0, while the
    // lists should be displayed with the foreground at index 0, so it needs
    // to be reversed.
		
		// Create an ordered list of all layers in the display.  Order by
		//   agent layers, networks, coverages, static coverages, WWJ globe layers.
		// Each subgroup will always contain all layers of the group, even when
		//   some layers may be unordered, e.g. an unordered agent layer will always
		//   be higher than a coverage layer.
		
		// Reverse ordered sorted map for layer orders
		Map<Integer,ListModelElement> orderedMap = 
				new TreeMap<Integer,ListModelElement>(Collections.reverseOrder());
		
		Map<String,Integer> currentLayerOrder = descriptor.getLayerOrders();
		
		List<ListModelElement> unsortedList = new ArrayList<ListModelElement>();
		//  agent layers
		for (String agentName : descriptor.getStyles().keySet()) {
			Integer order = currentLayerOrder.get(agentName);
			
			ListModelElement e = new ListModelElement(agentName);
			e.setValue(AGENT_KEY, true);
			
			// If agent layer has an order
			if (order != null) {	
				orderedMap.put(order, e);
			}
			else {
				unsortedList.add(e);
			}			
		}

		// Add the ordered agent layers
		for (Integer order : orderedMap.keySet()) {
			layerListModel.addElement(orderedMap.get(order));
		}
		
		// Ad the unordered agent layers
		for (ListModelElement e : unsortedList) {
			layerListModel.addElement(e);
		}
		
		orderedMap.clear();
		unsortedList.clear();
		
		// Networks
		for (ProjectionData data : descriptor.getProjections()) {
			if (data.getType().equals(ProjectionData.NETWORK_TYPE)) {
				Integer order = currentLayerOrder.get(data.getId());
				
				ListModelElement e = new ListModelElement(data.getId());
				
				// If network layer has an order
				if (order != null) {	
					orderedMap.put(order, e);
				}
				else {
					unsortedList.add(e);
				}			
			}
		}
		
		// Add the ordered network layers
		for (Integer order : orderedMap.keySet()) {
			layerListModel.addElement(orderedMap.get(order));
		}
		
		// Ad the unordered network layers
		for (ListModelElement e : unsortedList) {
			layerListModel.addElement(e);
		}
		
		orderedMap.clear();
		unsortedList.clear();
		
		// Dynamic model coverages
		Map<String,String> coverageLayerMap = descriptor.getCoverageLayers();
		for (String coverageName : coverageLayerMap.keySet()){
			Integer order = currentLayerOrder.get(coverageName);
			
			ListModelElement e = new ListModelElement(coverageName);
			
			// If layer has an order
			if (order != null) {	
				orderedMap.put(order, e);
			}
			else {
				unsortedList.add(e);
			}			
		}
		
		// Add the ordered coverage layers
		for (Integer order : orderedMap.keySet()) {
			layerListModel.addElement(orderedMap.get(order));
		}

		// Ad the unordered coverage layers
		for (ListModelElement e : unsortedList) {
			layerListModel.addElement(e);
		}

		orderedMap.clear();
		unsortedList.clear();

		// Static coverages
		
		Map<String,String> staticCoverageLayerMap = descriptor.getStaticCoverageMap();
		for (String path : staticCoverageLayerMap.keySet()) {	
			//			String name = new File(path).getName();  // short file name.ext

			Integer order = currentLayerOrder.get(path);

			ListModelElement e = new ListModelElement(path);

			// If layer has an order
			if (order != null) {	
				orderedMap.put(order, e);
			}
			else {
				unsortedList.add(e);
			}			
		}
		
		// Add the ordered static coverage layers
		for (Integer order : orderedMap.keySet()) {
			layerListModel.addElement(orderedMap.get(order));
		}

		// Ad the unordered static coverage layers
		for (ListModelElement e : unsortedList) {
			layerListModel.addElement(e);
		}

		orderedMap.clear();
		unsortedList.clear();
		
		// Last is WWJ globe layers
		
		Map<String,Boolean>globeLayerMap = descriptor.getGlobeLayersMap();
		for (String layerName : globeLayerMap.keySet()) {
			Integer order = currentLayerOrder.get(layerName);

			ListModelElement e = new ListModelElement(layerName);

			// If layer has an order
			if (order != null) {	
				orderedMap.put(order, e);
			}
			else {
				unsortedList.add(e);
			}			
		}
		
		// Add the ordered globe layers
		for (Integer order : orderedMap.keySet()) {
			layerListModel.addElement(orderedMap.get(order));
		}

		// Ad the unordered globe layers
		for (ListModelElement e : unsortedList) {
			layerListModel.addElement(e);
		}

		orderedMap.clear();
		unsortedList.clear();
	}
	
	@Override
	public void init(WizardModel wizardModel) {
		this.model = (DisplayWizardModel) wizardModel;
	}

	@Override
	public void applyState() throws InvalidStateException {
		applyChanges();
	}

	@Override
	public void prepare() {
		currentIndex = -1;
		layerListModel.clear();

		initialize();
		layerList.setSelectedIndex(0);	 // Setting syncs any listeners 
		
		setComplete(true);
	}
}