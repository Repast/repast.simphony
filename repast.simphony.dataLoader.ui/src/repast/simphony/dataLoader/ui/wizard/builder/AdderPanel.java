package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import repast.simphony.annotate.AgentAnnot;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PInputEventListener;

public class AdderPanel extends JPanel {
	ContextDescriptor descriptor;

	JToolBar toolbar;

	JPanel legendBar;

	PInputEventListener currentListener;

	ContextCreatorCanvas editorCanvas;

	AgentAdder agentAdder;

	RelationshipAdder relationshipAdder;
	
	ProbeListener prober;

	Color[] colors = {Color.RED, Color.BLUE, Color.BLACK, Color.CYAN,
										Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY,
										Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE, Color.YELLOW};

	int colorIndex = 0;

	int dashIndex = 5;

	Map<Class, AgentLayer> layerMap;

	private RemoveListener remover;

	public AdderPanel(ContextDescriptor descriptor) {
		this.setLayout(new BorderLayout());
		this.descriptor = descriptor;
		layerMap = new HashMap<Class, AgentLayer>();
		toolbar = new JToolBar();
		Action newAgentAction = new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (currentListener != null) {
					editorCanvas.removeInputEventListener(currentListener);
				}
				currentListener = agentAdder;
				editorCanvas.addInputEventListener(currentListener);
			}
		};
		newAgentAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("kopeteavailable.png")));
		// newAgentAction.putValue(Action.NAME, "Add Agent");
		newAgentAction.putValue(Action.SHORT_DESCRIPTION,
						"Add a new object to the context.");
		toolbar.add(new JButton(newAgentAction));
		
		
		Action newRelationshipAction = new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (currentListener != null) {
					editorCanvas.removeInputEventListener(currentListener);
				}
				currentListener = relationshipAdder;
				editorCanvas.addInputEventListener(currentListener);
			}
		};
		newRelationshipAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("add_task.png")));
		// newRelationshipAction.putValue(Action.NAME, "Add Relationship");
		newRelationshipAction.putValue(Action.SHORT_DESCRIPTION,
						"Add a new Relationship to the context.");
		toolbar.add(new JButton(newRelationshipAction));
		
		prober = new ProbeListener();
		Action selectAction = new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (currentListener != null) {
					editorCanvas.removeInputEventListener(currentListener);
				}
				currentListener = prober;
				editorCanvas.addInputEventListener(currentListener);
			}
		};
		selectAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("pointer.png")));
		selectAction.putValue(Action.SHORT_DESCRIPTION,
						"Select Agents.");
		toolbar.add(new JButton(selectAction));
		
		remover = new RemoveListener(descriptor, editorCanvas);
		Action removeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (currentListener != null) {
					editorCanvas.removeInputEventListener(currentListener);
				}
				remover.setCanvas(editorCanvas);
				currentListener = remover;
				editorCanvas.addInputEventListener(currentListener);
			}
		};
		removeAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("X.png")));
		// newRelationshipAction.putValue(Action.NAME, "Add Relationship");
		removeAction.putValue(Action.SHORT_DESCRIPTION,
						"Remove Agent.");
		toolbar.add(new JButton(removeAction));

		final JCheckBox box = new JCheckBox("Directed");
		box.setSelected(true);
		box.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent evt) {
		    AdderPanel.this.descriptor.setDirected(box.isSelected());
		  }
		});


		toolbar.add(box);
		this.add(toolbar, BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		this.add(splitPane, BorderLayout.CENTER);
		editorCanvas = new ContextCreatorCanvas(descriptor);
		editorCanvas.setSize(360, 340);
		JPanel canvasPanel = new JPanel();
		canvasPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(editorCanvas, BorderLayout.CENTER);
		splitPane.setRightComponent(canvasPanel);
		editorCanvas
						.removeInputEventListener(editorCanvas.getPanEventHandler());
		editorCanvas.removeInputEventListener(editorCanvas
						.getZoomEventHandler());
		agentAdder = new AgentAdder(editorCanvas, descriptor);
		relationshipAdder = new RelationshipAdder(editorCanvas, descriptor);
		legendBar = new JPanel();
		legendBar.setSize(60, 300);
		legendBar.setPreferredSize(new Dimension(100, 300));
		legendBar.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		// legendBar.setLayout(new BoxLayout(legendBar, BoxLayout.PAGE_AXIS));
		// legendBar.setBackground(Color.WHITE);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		JTree legendTree = createLegendTree(descriptor, root);
		JScrollPane scroller = new JScrollPane(legendTree,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setBorder(BorderFactory.createLoweredBevelBorder());
		splitPane.setLeftComponent(scroller);
	}

	private JTree createLegendTree(ContextDescriptor descriptor, DefaultMutableTreeNode root) {
		JTree legendTree = new JTree(root);
		legendTree.setRowHeight(legendTree.getRowHeight() + 6);
		DefaultMutableTreeNode agentLayer = new DefaultMutableTreeNode("Object Layers");
		root.add(agentLayer);
		DefaultMutableTreeNode networkLayer = new DefaultMutableTreeNode("Network Layers");
		root.add(networkLayer);

		CheckNodeRenderer renderer = new CheckNodeRenderer();
		legendTree.setCellRenderer(renderer);
		legendTree.addMouseListener(new NodeSelectionListener(legendTree));
		for (Class<?> clazz : descriptor.getAgentClasses()) {
			AgentLayer layer = editorCanvas.getAgentLayer(clazz);
			String title = clazz.getSimpleName();
			if (clazz.isAnnotationPresent(AgentAnnot.class)) {
				title = clazz.getAnnotation(AgentAnnot.class).displayName();
			}
			CheckNode node = new AgentLayerCheckNode(title, layer, true);
			agentLayer.add(node);
		}

		for (NetworkDescriptor network : descriptor.getNetworkDescriptors()) {
			String title = network.getName();
			NetworkLayer layer = editorCanvas.getNetworkLayer(network);
			CheckNode node = new NetworkCheckNode(title, layer, true);
			networkLayer.add(node);

		}
		legendTree.expandPath(new TreePath(new Object[]{root, agentLayer}));
		legendTree
						.expandPath(new TreePath(new Object[]{root, networkLayer}));
		legendTree.setRootVisible(false);

		return legendTree;
	}

	class NodeSelectionListener extends MouseAdapter {
		JTree tree;

		NodeSelectionListener(JTree tree) {
			this.tree = tree;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			int row = tree.getRowForLocation(x, y);
			TreePath path = tree.getPathForRow(row);
			if (path != null) {
				Object comp = path.getLastPathComponent();
				if (comp instanceof CheckNode) {
					Rectangle bounds = tree.getPathBounds(path);
					if (x < bounds.x + 21) {
						CheckNode node = (CheckNode) comp;

						node.setSelected(!node.isSelected());
						PLayer layer = node.getLayer();
						layer.setVisible(!layer.getVisible());
						layer.repaint();
						tree.revalidate();
						tree.repaint();
					}
				}
				// Rectangle bounds = tree.getPathBounds(path);
				// if (x < bounds.x + 21) {
				// // checkbox clicked
				// CheckNode node = (CheckNode) path.getLastPathComponent();
				// boolean isSelected = !(node.isSelected());
				// node.setSelected(isSelected);
				//
				// ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
				// if (row == 0) {
				// tree.revalidate();
				// tree.repaint();
				// }
				// }
			}
		}
	}

	public ContextDescriptor getContextDescriptor() {
		return descriptor;
	}

	/*
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		List<Class<?>> agentClass = new ArrayList<Class<?>>();
		agentClass.add(Agent1.class);
		agentClass.add(Agent2.class);
		List<NetworkDescriptor> networks = new ArrayList<NetworkDescriptor>();
		networks.add(new NetworkDescriptor("Friendship",
				UndirectedNetworkGraph.class));
		networks.add(new NetworkDescriptor("Workplace", DirectedGraph.class));

		final ContextDescriptor desc = new ContextDescriptor("Root",
				DefaultContext.class, networks, agentClass);
		TestFrame frame = new TestFrame(new AdderPanel(desc));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				for (NetworkDescriptor nd : desc.getNetworkDescriptors()) {
					for (RelationshipDescriptor rd : nd.getRelationships()) {
						System.out.println(rd.getSource() + "--"
								+ rd.getTarget());
					}
				}
			}

		});
	}
	*/

}
