package repast.simphony.gis.legend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.geotools.map.MapLayer;

public class DefaultLayerManager extends DefaultTreeModel implements
		LayerManager {

	DefaultMutableTreeNode root;

	Map<TreePath, DefaultMutableTreeNode> nodeMap = new HashMap<TreePath, DefaultMutableTreeNode>();

	public DefaultLayerManager() {
		super(new DefaultMutableTreeNode("Layers"));
		root = (DefaultMutableTreeNode) super.getRoot();
		nodeMap.put(new TreePath("Layers"), root);
	}

	public void addLayer(MapLayer layer, Object... path) {
		addPath(path);
		DefaultMutableTreeNode layerNode = new DefaultMutableTreeNode(layer);
		TreePath tp = new TreePath(root.getUserObject());
		for (Object o : path) {
			tp = tp.pathByAddingChild(o);
		}
		DefaultMutableTreeNode parent = nodeMap.get(tp);
		this.insertNodeInto(layerNode, parent, parent.getChildCount());
		tp = tp.pathByAddingChild(layer);
		nodeMap.put(tp, layerNode);
	}

	public void addPath(Object... path) {
		TreePath tp = new TreePath(root.getUserObject());
		if (path.length > 0) {
			for (int i = 0; i < path.length; i++) {
				tp = tp.pathByAddingChild(path[i]);
				DefaultMutableTreeNode childNode = nodeMap.get(tp);
				if (childNode == null) {
					childNode = new DefaultMutableTreeNode(path[i]);
					nodeMap.put(tp, childNode);
					DefaultMutableTreeNode parentNode = nodeMap.get(tp
							.getParentPath());
					this.insertNodeInto(childNode, parentNode, parentNode
							.getChildCount());
				}
			}
		}
	}

	public Collection<Object> getChildren(Object... path) {
		List<Object> layerList = new ArrayList<Object>();
		DefaultMutableTreeNode node = nodeMap.get(createPath(path));
		for (int i = 0; i < node.getChildCount(); i++) {
			layerList.add(((DefaultMutableTreeNode) node.getChildAt(i))
					.getUserObject());
		}
		return layerList;
	}

	public Collection<MapLayer> getLayers(Object... path) {
		List<MapLayer> layerList = new ArrayList<MapLayer>();
		TreePath tp;
		if (path.length == 0) {
			tp = new TreePath(root.getUserObject());
		} else {
			Object[] fullPath = new Object[path.length + 1];
			fullPath[0] = root.getUserObject();
			System.arraycopy(path, 0, fullPath, 1, path.length);
			tp = new TreePath(fullPath);
		}
		DefaultMutableTreeNode node = nodeMap.get(tp);
		for (int i = 0; i < node.getChildCount(); i++) {
			Object userObject = ((DefaultMutableTreeNode) node.getChildAt(i))
					.getUserObject();
			if (userObject instanceof MapLayer) {
				layerList.add((MapLayer) userObject);
			}
		}
		return layerList;
	}

	public void removeLayer(MapLayer layer, Object... path) {
		TreePath tp = createPath(path);
		tp = tp.pathByAddingChild(layer);
		DefaultMutableTreeNode node = nodeMap.get(tp);
		this.removeNodeFromParent(node);
	}

	private TreePath createPath(Object... path) {
		TreePath tp = new TreePath(root.getUserObject());
		for (Object o : path) {
			tp = tp.pathByAddingChild(o);
		}
		return tp;
	}

	public void removePath(Object... path) {
		DefaultMutableTreeNode node = nodeMap.get(createPath(path));

		removeNodeFromParent(node);
	}

}
