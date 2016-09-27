package repast.simphony.visualization.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Abstract style step for display wizards that provides basic agent selection.
 *   Implementing subclasses should provide a style editing panel.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public abstract class StyleStep extends PluginWizardStep {	
	private static final long serialVersionUID = 1179847211916347928L;
	
	// TODO move to a viz constants class
	public static final String UP_ICON = "agent_up.png";
	public static final String DOWN_ICON = "agent_down.png";
	public static final String EDIT_ICON = "edit.png";
	public static final String ADD_ICON = "edit_add.png";
	public static final String REMOVE_ICON = "edit_remove.png";

	protected DisplayWizardModel model;

	protected JList<AgentTypeElement> agentList;
	protected DefaultListModel<AgentTypeElement> agentListModel;
	protected boolean reordering = false;
	protected int currentIndex = -1;
	protected JTextField classFld;
	
	/**
	 * Stores agent name, class and style attributes in the list entries
	 */
	protected static class AgentTypeElement {
		private String agentName;
		private String agentClassName;
		
		private Map<String,Object> values = new HashMap<String,Object>();

		public AgentTypeElement(String agentName, String agentClassName) {
			this.agentName = agentName;
			this.agentClassName = agentClassName;
		}

		public String toString() {
			return agentName;
		}
		
		public Object getValue(String key){
			return values.get(key);
		}
		
		public void setValue(String key, Object value){
			values.put(key, value);
		}

		public String getAgentName() {
			return agentName;
		}

		public String getAgentClassName() {
			return agentClassName;
		}
	}

	public StyleStep(String name, String description) {
		super(name,description);		
	}
	
	@Override
	protected JPanel getContentPanel(){
		// Columns: Agent List | gap | Style panel
		FormLayout layout = new FormLayout(
				"pref:grow, 6dlu, pref:grow",  // columns
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, fill:pref:grow"); //rows
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		agentListModel = new DefaultListModel<AgentTypeElement>();
		agentList = new JList<AgentTypeElement>(agentListModel);
		agentList.setToolTipText("This is the list of agents currently defined for" + " this display");

		classFld = new JTextField();
		classFld.setEditable(false);
		classFld.setToolTipText("This is the class name for the selected agent");

		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Agents", cc.xyw(1, 1, 1));
		builder.add(new JScrollPane(agentList), cc.xywh(1, 3, 1, 5));
		agentList.setVisibleRowCount(14);

		// The right side has the agent and style class names and the edit, remove,
		// and add buttons.
		builder.addLabel("Agent Class:", cc.xy(3, 3));
		builder.add(classFld, cc.xy(3, 5));

		builder.add(getStylePanel(), cc.xy(3, 7));

		agentList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting() && !reordering) {
					setCurrentElementValues();
					currentIndex = agentList.getSelectedIndex();

					if (agentList.getSelectedValue() == null) {
						classFld.setText("");
					} 
					else {
						AgentTypeElement element = (AgentTypeElement) agentList.getSelectedValue();
						classFld.setText(element.agentClassName);
						agentListChanged(element);
					}
				}
				reordering = false;
			}
		});

		return builder.getPanel();
	}

	/**
	 * Provide a JPanel with style editing capabilities.
	 * 
	 * @return the style editor panel
	 */
	protected abstract JPanel getStylePanel();
	
	/**
	 * Set the values of the current element in the agent list when another element
	 *   in the list is selected.
	 */
	protected abstract void setCurrentElementValues();
	
	/**
	 * Do something whenever the agent list changes to a new value through selection.
	 */
	protected abstract void agentListChanged(AgentTypeElement element);
	
	/**
	 * Apply the editor changes to the DisplayDescriptor.
	 */
	protected abstract void applyChanges();
	
	/**
	 * Initialize sub classes.
	 */
	protected abstract void initialize();
	
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
		agentListModel.clear();

		DisplayDescriptor descriptor = model.getDescriptor();
		
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
			
			agentListModel.addElement(new AgentTypeElement(agentLabel,className));
		}
	  
		initialize();
		agentList.setSelectedIndex(0);	 // Setting syncs any listeners 
		
		setComplete(true);
	}
	
	 private String getShortName(String className) {
	    int index = className.lastIndexOf(".");
	    if (index != -1 && index != className.length() - 1) {
	      return className.substring(index + 1, className.length());
	    }
	    return null;
	 }
}