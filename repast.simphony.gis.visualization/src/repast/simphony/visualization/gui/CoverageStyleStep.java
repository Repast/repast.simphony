package repast.simphony.visualization.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * Abstract style step for display wizards that provides basic agent selection.
 *   Implementing subclasses should provide a style editing panel.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public abstract class CoverageStyleStep extends PluginWizardStep {	
	private static final long serialVersionUID = 1179847211916347928L;
	
	// TODO move to a viz constants class
	public static final String UP_ICON = "agent_up.png";
	public static final String DOWN_ICON = "agent_down.png";
	public static final String EDIT_ICON = "edit.png";
	public static final String ADD_ICON = "edit_add.png";
	public static final String REMOVE_ICON = "edit_remove.png";

	protected DisplayWizardModel model;

	protected JList<CoverageLayerElement> agentList;
	protected DefaultListModel<CoverageLayerElement> agentListModel;
	protected boolean reordering = false;
	protected int currentIndex = -1;
	
	
	/**
	 * Stores coverage name and style data
	 */
	public static class CoverageLayerElement {
		private String name;
		
		
		private Map<String,Object> values = new HashMap<String,Object>();

		public CoverageLayerElement(String name) {
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

	public CoverageStyleStep(String name, String description) {
		super(name,description);		
	}
	
	@Override
	protected JPanel getContentPanel(){
		// Columns: Agent List | gap | Style panel
		FormLayout layout = new FormLayout(
				// 80dlu is wide enough - if you set pref:grow the scrollbar never shows
				"80dlu, 6dlu, pref:grow",  // columns
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, fill:pref:grow"); //rows
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		agentListModel = new DefaultListModel<CoverageLayerElement>();
		agentList = new JList<CoverageLayerElement>(agentListModel);
		agentList.setToolTipText("This is the list of coverages currently defined for" + " this display");

		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Coverage Layers", cc.xyw(1, 1, 1));
		builder.add(new JScrollPane(agentList), cc.xywh(1, 3, 1, 5));
		agentList.setVisibleRowCount(14);

		// The right side has the agent and style class names and the edit, remove,
		// and add buttons.

		builder.add(getStylePanel(), cc.xy(3, 3));

		agentList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting() && !reordering) {
					setCurrentElementValues();
					currentIndex = agentList.getSelectedIndex();

					if (agentList.getSelectedValue() == null) {
//						classFld.setText("");
					} 
					else {
						CoverageLayerElement element = (CoverageLayerElement) agentList.getSelectedValue();
//						classFld.setText(element.agentClassName);
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
	protected abstract void agentListChanged(CoverageLayerElement element);
	
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

		initialize();
		agentList.setSelectedIndex(0);	 // Setting syncs any listeners 
		
		setComplete(true);
	}
	
}