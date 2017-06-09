package repast.simphony.engine.environment;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.IDisplay;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * This interface represents an object that contains GUI elements generated for
 * a simulation and that are to be displayed to the user.
 * 
 * @author Jerry Vos
 * @author Eric Tatara
 */
public interface GUIRegistry {

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
	public void addComponent(JComponent component, GUIRegistryType type, String name);

	/**
	 * Removes a component from the registry.
	 * 
	 * @param component
	 *            the component to remove
	 * 
	 * @return true if something was removed (false if this didn't contain the
	 *          component
	 */
	 public boolean removeComponent(JComponent component);

	/**
	 * Retrieves the name of a given component. This may return null if the name
	 * was null or if the component does not exist in the registry.
	 * 
	 * @param component
	 *            the component who's name to retrieve
	 * @return the specified component's name
	 */
	 public String getName(JComponent component);

	/**
	 * Retrieves the (type, components) pairs that were registered with this
	 * registry.
	 * 
	 * @return a collection of (type, components) pairs
	 */
	 public Collection<Pair<GUIRegistryType, Collection<JComponent>>> getTypesAndComponents();


  /**
   * Adds an IDisplay to this GUIRegistry. If the display is added, there is NO need
   * to add the compoment associated with the IDisplay as well.
   *
   * @param display the renderer to add
   */
	 public void addDisplay(String name, GUIRegistryType type, IDisplay display);

  /**
   * Gets the list of IDisplay-s registered with this GUIRegistry.
   *
   * @return the list of IDisplays-s registered with this GUIRegistry.
   */
	 public List<IDisplay> getDisplays();

  /**
   * Gets the display associated with the specified component.
   *
   * @param comp the component whose display we want
   *
   * @return the associated display
   */
	 public IDisplay getDisplayForComponent(JComponent comp);
  
  /**
	 * Get the IDisplay associated with the specified display name;
	 * @param displayName the display name
	 * @return the IDisplay with the provided name
	 */
	 public IDisplay getDisplayForName(String displayName);
}