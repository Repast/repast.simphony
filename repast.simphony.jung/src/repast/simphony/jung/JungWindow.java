/*
 * Created by JFormDesigner on Fri Aug 29 16:59:22 CDT 2008
 */

package repast.simphony.jung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import org.jscience.physics.amount.Amount;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.engine.environment.RunState;
import repast.simphony.jung.tablemodels.AverageUnweightedDistanceTableModel;
import repast.simphony.jung.tablemodels.AverageWeightedDistanceTableModel;
import repast.simphony.jung.tablemodels.ClusteringCoefficientTableModel;
import repast.simphony.jung.tablemodels.ConnectivityMatrixTableModel;
import repast.simphony.jung.tablemodels.DijkstraDistanceTableModel;
import repast.simphony.jung.tablemodels.NodeDegreeDiagonalMatrixTableModel;
import repast.simphony.jung.tablemodels.NodeDegreeTableModel;
import repast.simphony.jung.tablemodels.NullTableModel;
import repast.simphony.jung.tablemodels.RankingTableModel;
import repast.simphony.jung.tablemodels.WeightedConnectivityMatrixTableModel;
import repast.simphony.ui.filters.FileExtensionFilter;
import repast.simphony.ui.sparkline.SparklineJComponent;
import repast.simphony.ui.table.SortedJTable;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;

/**
 * @author User #5
 */
public class JungWindow extends JPanel {

	public Map<String, ContextJungNetwork> sourceMap = new HashMap<String, ContextJungNetwork>();
	public static final String NO_SPARKLINE = "No Sparkline                                                       ";

	public JungWindow() {
		initComponents();

		this.sparklineJComponent.setLineGraph(true);

		this.resultsTable.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				redrawSparkline();
			}
		});

		this.sparklineComboBox.addItem(NO_SPARKLINE);

		this.findSources();

		List<String> sortedList = new ArrayList<String>(this.sourceMap.keySet());
		Collections.sort(sortedList);
		for (String key : sortedList) {
			this.sourceComboBox.addItem(key);
		}

	}

	public void findSources() {

		Context mainContext = RunState.getInstance().getMasterContext();
		String pathName = mainContext.getId().toString();
		this.findSources(pathName + "/", mainContext);

	}

	public void findSources(String pathName, Context context) {

		for (Object childObject : context.getSubContexts()) {
			if (childObject instanceof Context) {
				Context childContext = (Context) childObject;
				String childPathName = pathName
						+ childContext.getId().toString();
				this.findSources(childPathName + "/", childContext);
			}
		}

		for (Object childObject : context.getProjections()) {
			if (childObject instanceof ContextJungNetwork) {
				ContextJungNetwork childNetwork = (ContextJungNetwork) childObject;
				String childPathName = pathName + childNetwork.getName();
				this.sourceMap.put(childPathName, childNetwork);
			}
		}

	}

	private void executeButtonActionPerformed(ActionEvent e) {

		ContextJungNetwork network = this.sourceMap.get(this.sourceComboBox
				.getSelectedItem());

		if (network != null) {

			try {
				if (this.algorithmChoiceComboBox.getSelectedItem().equals(
						"Average Unweighted Distance")) {
					Graph graph = network.getGraph();
					this.resultsTable
							.setModel(new AverageUnweightedDistanceTableModel(
									graph));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Average Weighted Distance")) {
					Graph graph = network.getGraph();
					this.resultsTable
							.setModel(new AverageWeightedDistanceTableModel(
									graph));
				} else if (this.algorithmChoiceComboBox.getSelectedItem() == null) {
					this.resultsTable.setModel(new NullTableModel());
				} 
//				else if (this.algorithmChoiceComboBox.getSelectedItem()
//						.equals("Bary Center")) {
//					this.resultsTable.setModel(new RankingTableModel(
//							new BaryCenter(network.getGraph())));
//				} 
				else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Betweenness Centrality for Edges")) {
					this.resultsTable.setModel(new RankingTableModel(
							new BetweennessCentrality(network.getGraph(),
									false, true)));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Betweenness Centrality for Nodes")) {
					this.resultsTable.setModel(new RankingTableModel(
							new BetweennessCentrality(network.getGraph(), true,
									false)));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Clustering Coefficients")) {
					Graph graph = network.getGraph();
					this.resultsTable
							.setModel(new ClusteringCoefficientTableModel(graph));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Connectivity Matrix")) {
					Graph graph = network.getGraph();
					this.resultsTable
							.setModel(new ConnectivityMatrixTableModel(graph));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Connectivity Matrix with Edge Weights")) {
					Graph graph = network.getGraph();
					this.resultsTable
							.setModel(new WeightedConnectivityMatrixTableModel(
									graph));
				} 
//				else if (this.algorithmChoiceComboBox.getSelectedItem()
//						.equals("Degree Distribution with Indegree")) {
//					this.resultsTable.setModel(new RankingTableModel(
//							new DegreeDistributionRanker(network.getGraph(),
//									true)));
//				} 
//				else if (this.algorithmChoiceComboBox.getSelectedItem()
//						.equals("Degree Distribution with Outdegree")) {
//					this.resultsTable.setModel(new RankingTableModel(
//							new DegreeDistributionRanker(network.getGraph(),
//									false)));
//				} 
//				else if (this.algorithmChoiceComboBox.getSelectedItem()
//						.equals("\"Hubs-and-Authorities\" Importance Measure")) {
//					this.resultsTable.setModel(new RankingTableModel(new HITS(
//							network.getGraph())));
//				} 
				else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Node Undirected Degree Diagonal Matrix")) {
					Graph graph = network.getGraph();
					this.resultsTable
							.setModel(new NodeDegreeDiagonalMatrixTableModel(
									graph));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Node Undirected Degree List")) {
					Graph graph = network.getGraph();
					this.resultsTable.setModel(new NodeDegreeTableModel(graph,
							NodeDegreeTableModel.Type.UNDIRECTED));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Node Indegree List")) {
					Graph graph = network.getGraph();
					this.resultsTable.setModel(new NodeDegreeTableModel(graph,
							NodeDegreeTableModel.Type.IN));
				} else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Node Outdegree List")) {
					Graph graph = network.getGraph();
					this.resultsTable.setModel(new NodeDegreeTableModel(graph,
							NodeDegreeTableModel.Type.OUT));
				}
//				else if (this.algorithmChoiceComboBox.getSelectedItem()
//						.equals("Page Rank for Directed Graphs")) {
//					if (network.isDirected()) {
//						String biasString = JOptionPane
//								.showInputDialog("Please input a bias coefficient from 0.0 to 1.0:");
//						if (biasString != null) {
//							double biasDouble;
//							try {
//								biasDouble = Double.parseDouble(biasString);
//							} catch (NumberFormatException e1) {
//								JOptionPane
//										.showMessageDialog(
//												null,
//												"The input value (\""
//														+ biasString
//														+ "\") could not be read. Zero will be used for the bias.",
//												"Warning",
//												JOptionPane.INFORMATION_MESSAGE);
//								biasDouble = 0.0;
//								this.resultsTable
//										.setModel(new NullTableModel());
//							}
//							this.resultsTable.setModel(new RankingTableModel(
//									new PageRank((DirectedGraph) network
//											.getGraph(), biasDouble)));
//						} else {
//							this.resultsTable.setModel(new NullTableModel());
//						}
//					} else {
//						JOptionPane.showMessageDialog(null,
//								"The network is not directed.", "Warning",
//								JOptionPane.INFORMATION_MESSAGE);
//						this.resultsTable.setModel(new NullTableModel());
//					}
//				} 
				else if (this.algorithmChoiceComboBox.getSelectedItem()
						.equals("Shortest Paths")) {
					Graph graph = network.getGraph();
					this.resultsTable.setModel(new DijkstraDistanceTableModel(
							graph));
				}

				TableModel model = this.resultsTable.getModel();
				if (model != null) {
					this.sparklineComboBox.removeAllItems();
					this.sparklineComboBox.addItem(NO_SPARKLINE);
					this.sparklineComboBox.setSelectedItem(NO_SPARKLINE);
					for (int col = 0; col < model.getColumnCount(); col++) {
						this.sparklineComboBox.addItem("Sparkline for "
								+ model.getColumnName(col));
					}
				}

			} catch (Exception e2) {

				JOptionPane.showMessageDialog(null, e2.getLocalizedMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				this.resultsTable.setModel(new NullTableModel());
				e2.printStackTrace();

			}

		} else {

			this.resultsTable.setModel(new NullTableModel());

		}

	}

	private void exportButtonActionPerformed(ActionEvent e) {

		JFileChooser chooser = new JFileChooser(new File("results.csv"));
		FileFilter filter = new FileExtensionFilter("CSV",
				new String[] { "csv" });
		chooser.setFileFilter(filter);
		if (chooser.showSaveDialog(this.getTopLevelAncestor()) == JFileChooser.APPROVE_OPTION) {

			try {

				PrintWriter writer = new PrintWriter(chooser.getSelectedFile());

				TableModel model = resultsTable.getModel();

				for (int col = 0; col < model.getColumnCount(); col++) {

					writer.print(model.getColumnName(col));
					if (col < model.getColumnCount() - 1) {
						writer.print(", ");
					} else {
						writer.println();
					}

				}
				for (int row = 0; row < model.getRowCount(); row++) {

					for (int col = 0; col < model.getColumnCount(); col++) {

						writer.print(model.getValueAt(row, col));
						if (col < model.getColumnCount() - 1) {
							writer.print(", ");
						} else {
							writer.println();
						}

					}

				}

				writer.close();

			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Error" + e1.getMessage());
			}

		}

	}

	private void sparklineComboBoxActionPerformed(ActionEvent e) {
		this.redrawSparkline();
	}

	private void redrawSparkline() {

		int col = this.sparklineComboBox.getSelectedIndex() - 1;
		TableModel model = this.resultsTable.getModel();
		if ((col >= 0) && (model.getRowCount() > 0)) {
			Number newData[] = new Number[model.getRowCount()];
			for (int row = 0; row < model.getRowCount(); row++) {
				try {
					Object value = model.getValueAt(row, col);
					if (value instanceof Number) {
						newData[row] = ((Number) value).doubleValue();
					} else if (value instanceof Amount) {
						newData[row] = ((Amount) value).getEstimatedValue();
					} else if (value instanceof String) {
						newData[row] = Double.parseDouble((String) value);
					} else {
						newData[row] = 0.0;
					}
				} catch (Exception ex) {
					newData[row] = 0.0;
				}
			}
			this.sparklineJComponent.setData(newData);
		} else {
			this.sparklineJComponent.clearData();
		}

	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		sourceComboBox = new JComboBox();
		executeButton = new JButton();
		algorithmChoiceComboBox = new JComboBox();
		exportButton = new JButton();
		sparklineComboBox = new JComboBox();
		sparklineJComponent = new SparklineJComponent();
		scrollPane1 = new JScrollPane();
		resultsTable = new SortedJTable();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormSpecs.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//---- label1 ----
		label1.setText("Source Network:");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label1, cc.xy(1, 1));
		add(sourceComboBox, cc.xy(3, 1));

		//---- executeButton ----
		executeButton.setText("Execute Algorithm");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeButtonActionPerformed(e);
			}
		});
		add(executeButton, cc.xy(1, 3));

		//---- algorithmChoiceComboBox ----
		algorithmChoiceComboBox.setModel(new DefaultComboBoxModel(new String[] {
			"Average Unweighted Distance",
			"Average Weighted Distance",
//			"Bary Center",
			"Betweenness Centrality for Edges",
			"Betweenness Centrality for Nodes",
			"Clustering Coefficients",
			"Connectivity Matrix",
			"Connectivity Matrix with Edge Weights",
//			"Degree Distribution with Indegree",
//			"Degree Distribution with Outdegree",
//			"\"Hubs-and-Authorities\" Importance Measure",
			"Node Undirected Degree Diagonal Matrix",
			"Node Undirected Degree List",
			"Node Indegree List",
			"Node Outdegree List",
//			"Page Rank for Directed Graphs",
			"Shortest Paths"
		}));
		add(algorithmChoiceComboBox, cc.xy(3, 3));

		//---- exportButton ----
		exportButton.setText("Export to CSV");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportButtonActionPerformed(e);
			}
		});
		add(exportButton, cc.xy(1, 5));

		//---- sparklineComboBox ----
		sparklineComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sparklineComboBoxActionPerformed(e);
			}
		});
		add(sparklineComboBox, cc.xy(1, 7));
		add(sparklineJComponent, cc.xywh(3, 7, 1, 1, CellConstraints.FILL, CellConstraints.FILL));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(resultsTable);
		}
		add(scrollPane1, cc.xywh(1, 9, 3, 1));
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox sourceComboBox;
	private JButton executeButton;
	private JComboBox algorithmChoiceComboBox;
	private JButton exportButton;
	private JComboBox sparklineComboBox;
	private SparklineJComponent sparklineJComponent;
	private JScrollPane scrollPane1;
	private SortedJTable resultsTable;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
