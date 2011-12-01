/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.IDisplay;

import javax.swing.*;
import java.util.*;

/**
 * A default implementation of a GUI registry.
 *
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultGUIRegistry implements GUIRegistry {

	private Hashtable<GUIRegistryType, ArrayList<JComponent>> typeComponentTable;
	private Hashtable<JComponent, GUIRegistryType> componentTypeTable;
	private HashMap<JComponent, String> componentNameTable;

	private Map<JComponent, IDisplay> compDisplayMap = new HashMap<JComponent, IDisplay>();

	/**
	 * Constructs the GUI Registry.
	 */
	public DefaultGUIRegistry() {
		super();

		this.typeComponentTable = new Hashtable<GUIRegistryType, ArrayList<JComponent>>();
		this.componentNameTable = new HashMap<JComponent, String>();
		this.componentTypeTable = new Hashtable<JComponent, GUIRegistryType>();
	}

	/**
	 * Adds a component with the given type and name. The type is not meant to
	 * be "JPanel" or "JTable," but to be along the lines of "Graph" or
	 * "Display." The name can be null.
	 * 
	 * @param component
	 *            the component to add
	 * @param type
	 *            the <i>type</i> of the component
	 * @param name
	 *            the name of the component (can be null)
	 */
	public void addComponent(JComponent component, GUIRegistryType type, String name) {
		Collection<JComponent> components = getComponents(type);
		components.add(component);

		this.componentNameTable.put(component, name);
		this.componentTypeTable.put(component, type);
	}

	/**
	 * Removes a component from the registry.
	 * 
	 * @param component
	 *            the component to remove
	 */
	public boolean removeComponent(JComponent component) {
		GUIRegistryType type = this.componentTypeTable.remove(component);
		if (type != null) {
			getComponents(type).remove(component);
			componentTypeTable.remove(component);
			return componentNameTable.remove(component) != null;
		} else {
			return false;
		}
	}
	
	/**
	 * Retrieves the components that have the same specified type as the given
	 * argument.
	 * 
	 * @param type
	 *            the type of the component
	 * @return the components of the specified type
	 */
	private Collection<JComponent> getComponents(GUIRegistryType type) {
		if (typeComponentTable.get(type) == null) {
			typeComponentTable.put(type, new ArrayList<JComponent>());
		}

		return typeComponentTable.get(type);
	}


	/**
	 * Retrieves the name of a given component. This may return null if the name
	 * was null or if the component does not exist in the registry.
	 * 
	 * @param component
	 *            the component who's name to retrieve
	 * @return the specified component's name
	 */
	public String getName(JComponent component) {
		return this.componentNameTable.get(component);
	}

	/**
	 * Retrieves the (type, components) pairs that were registered with this
	 * registry.
	 * 
	 * @return a collection of (type, components) pairs
	 */
	public Collection<Pair<GUIRegistryType, Collection<JComponent>>> getTypesAndComponents() {
		ArrayList<Pair<GUIRegistryType, Collection<JComponent>>> typeComponentPairs = new ArrayList<Pair<GUIRegistryType, Collection<JComponent>>>();

		for (GUIRegistryType type : typeComponentTable.keySet()) {
			typeComponentPairs.add(new Pair<GUIRegistryType, Collection<JComponent>>(
					type, typeComponentTable.get(type)));
		}
		return typeComponentPairs;
	}

  /**
   * Adds an IDisplay to this GUIRegistry. If the display is added, there is NO need
   * to add the compoment associated with the IDisplay as well.
   *
   * @param display the renderer to add
   */
  public void addDisplay(String name, GUIRegistryType type, IDisplay display) {
	  JPanel panel = display.getPanel();
	  addComponent(panel, type, name);
	  compDisplayMap.put(panel, display);
  }

  /**
   * Gets the list of IDisplay-s registered with this GUIRegistry.
   *
   * @return the list of IDisplays-s registered with this GUIRegistry.
   */
  public List<IDisplay> getDisplays() {
    return new ArrayList<IDisplay>(compDisplayMap.values());
  }

	/**
	 * Gets the IDisplay associated with the specified component.
	 *
	 * @param comp the component whose IDisplay we want to get
	 * @return the IDisplay associated with the specified component.
	 */
	public IDisplay getDisplayForComponent(JComponent comp) {
		return compDisplayMap.get(comp);
	}
}
