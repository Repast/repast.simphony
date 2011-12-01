package repast.simphony.gis.legend;

import java.util.Collection;

import org.geotools.map.MapLayer;

/**
 * This interface is designed to support complex hierarchies as the structure
 * for a set of layers describing a map.
 * 
 * @author howe
 * 
 */
public interface LayerManager {
	/**
	 * Add a layer into the manager. The path will be defined hierarchically
	 * based on the series of Objects passed in as the path.
	 * 
	 * @param path
	 *            The hierarchical path that describes the location of the
	 *            layer.
	 * @param layer
	 *            The layer to be managed.
	 */
	public void addLayer(MapLayer layer, Object... path);

	/**
	 * Remove the layer that is at the supplied path.
	 * 
	 * @param path
	 *            The hierarchical path that describes the location of the
	 *            layer.
	 * @param layer
	 *            The layer to be managed.
	 */
	public void removeLayer(MapLayer layer, Object... path);

	/**
	 * Get the layers that exist exactly at the path specified. This will not
	 * recursively get the layers.
	 * 
	 * @param path
	 *            The hierarchical path that describes the location of the
	 *            layer.
	 * @return The layers at the path.
	 */
	public Collection<MapLayer> getLayers(Object... path);

	/**
	 * Add the path into the hierarchy.
	 * 
	 * @param path
	 *            The path to add to the hierarchy
	 */
	public void addPath(Object... path);

	/**
	 * Return the non-leaf objects at the specified path.
	 * 
	 * @param path
	 *            The hierarchical path that describes the location of the
	 *            layer.
	 * @return The non-leaf children of the node defined by path.
	 */
	public Collection<Object> getChildren(Object... path);

	/**
	 * Remove the child object from the specified path. This will remove all
	 * children of the specified child as well.
	 * 
	 * @param path
	 *            The parent path of the child to be removed.
	 * @param child
	 *            The child to remove from the path.
	 */
	public void removePath(Object... path);
}
