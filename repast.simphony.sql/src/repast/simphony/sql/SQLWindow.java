package repast.simphony.sql;

import java.awt.Font;
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;
import org.josql.contrib.JoSQLSwingTableModel;
import org.jscience.physics.amount.Amount;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.ui.filters.FileExtensionFilter;
import repast.simphony.ui.sparkline.SparklineJComponent;
import repast.simphony.ui.table.TablePanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * Panel for executing and displaying SQL results.
 * 
 * @author Michael North
 * @author Eric Tatara
 */
public class SQLWindow extends JPanel {

	public String currentSource = "";
	public Map<String, Object> sourceMap = new HashMap<String, Object>();
	public static final String NO_SPARKLINE = "No Sparkline          ";

	public SQLWindow() {
		initComponents();

		this.sparklineJComponent.setLineGraph(true);

		this.tablePanel.getTable().getTableHeader().addMouseListener(new MouseAdapter() {
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

		queryTextArea
		.setText("/* Queries use JoSQL (http://josql.sourceforge.net/index.html) "
				+ System.getProperty("line.separator")
				+ " for execution. Please see their web site for instructions. */"
				+ System.getProperty("line.separator")
				+ "SELECT toString Name, class.name Class, propertyNames(:_currobj) Properties, "
				+ "propertyValues(:_currobj) Values"
				+ System.getProperty("line.separator")
				+ "FROM java.lang.Object");
	}

	public void findSources() {
		Context mainContext = RunState.getInstance().getMasterContext();
		String pathName = mainContext.getId().toString();
		this.sourceMap.put("Context (Main): " + pathName, mainContext);
		this.findSources(pathName + "/", mainContext);
	}

	public void findSources(String pathName, Context context) {

		for (Object childObject : context.getSubContexts()) {
			if (childObject instanceof Context) {
				Context childContext = (Context) childObject;
				String childPathName = pathName
						+ childContext.getId().toString();
				this.sourceMap.put("Context: " + childPathName, childContext);
				this.findSources(childPathName + "/", childContext);
			}
		}

		for (Object childObject : context.getProjections()) {
			if (childObject instanceof Network) {
				Network childNetwork = (Network) childObject;
				String childPathName = pathName + childNetwork.getName();
				this.sourceMap.put("Network: " + childPathName, childNetwork);
			}
		}
	}

	private void executeButtonActionPerformed(ActionEvent e) {
		try {
			this.messageTextArea.setText("");

			JoSQLSwingTableModel model = new JoSQLSwingTableModel();
			model.setClassLoader(this.getClass().getClassLoader());
			model.addFunctionHandler(new RepastJoSQLFunctionHandler());

			String functionHandlerClassName = this.functionHandlerTextField
					.getText();
			if ((functionHandlerClassName != null)
					&& (!functionHandlerClassName.equals(""))) {
				try {
					Class functionHandlerClass = Class
							.forName(functionHandlerClassName);
					model
					.addFunctionHandler(functionHandlerClass
							.newInstance());
				} catch (Exception eFH) {
					this.messageTextArea.setText(this.messageTextArea.getText()
							+ "Could not load function handler: "
							+ eFH.getLocalizedMessage());
				}
			}

			model.parse(queryTextArea.getText());

			List list = new ArrayList();
			Object source = this.sourceMap.get(this.sourceComboBox
					.getSelectedItem());
			Iterable sourceList = null;
			if (source instanceof Context) {
				sourceList = ((Context) source).getObjects(model
						.getFromObjectClass());
			} else if (source instanceof Network) {
				sourceList = ((Network) source).getEdges();
				if (!Network.class.isAssignableFrom(model.getFromObjectClass())) {
					model.setFromObjectClass(RepastEdge.class);
				}
			}

			if (source == null) {
				this.messageTextArea.setText(this.messageTextArea.getText()
						+ " Invalid source. Please select another source.");
			} else {
				Class fromClass = model.getFromObjectClass();
				for (Object obj : sourceList) {
					if ((obj != null)
							&& (fromClass.isAssignableFrom(obj.getClass()))) {
						list.add(obj);
					}
				}
			}

			QueryResults results = model.execute(list);

			tablePanel.getTable().setModel(model);

			Map saveValue = results.getSaveValues();
			if ((saveValue != null) && (saveValue.size() > 0)) {
				String message = "";
				Object value = null;
				for (Object key : saveValue.keySet()) {
					value = saveValue.get(key);
					message = message + key + " = " + value
							+ System.getProperty("line.separator");
				}
				messageTextArea.setText(this.messageTextArea.getText() + " "
						+ message);
			}

			if (model != null) {
				this.sparklineComboBox.removeAllItems();
				this.sparklineComboBox.addItem(NO_SPARKLINE);
				this.sparklineComboBox.setSelectedItem(NO_SPARKLINE);
				for (int col = 0; col < model.getColumnCount(); col++) {
					this.sparklineComboBox.addItem("Sparkline for "
							+ model.getColumnName(col));
				}
			}

		} catch (QueryParseException e2) {
			messageTextArea.setText(this.messageTextArea.getText() + " "
					+ e2.getMessage());
		} catch (QueryExecutionException e3) {
			messageTextArea.setText(this.messageTextArea.getText() + " "
					+ e3.getMessage());
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
				TableModel model = tablePanel.getTable().getModel();

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

	private void sourceComboBoxActionPerformed(ActionEvent e) {
		String selectedItem = (String) this.sourceComboBox.getSelectedItem();
		if ((selectedItem != null)
				&& (!selectedItem.equals(this.currentSource))) {
			this.currentSource = selectedItem;
		}
	}

	private void sparklineComboBoxActionPerformed(ActionEvent e) {
		this.redrawSparkline();
	}

	public void redrawSparkline() {
		int col = this.sparklineComboBox.getSelectedIndex() - 1;
		TableModel model = this.tablePanel.getTable().getModel();
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
		label1 = new JLabel();
		sourceComboBox = new JComboBox();
		label2 = new JLabel();
		label3 = new JLabel();
		panel1 = new JPanel();
		executeButton = new JButton();
		exportButton = new JButton();
		scrollPane2 = new JScrollPane();
		queryTextArea = new JTextArea();
		scrollPane3 = new JScrollPane();
		messageTextArea = new JTextArea();
		label4 = new JLabel();
		functionHandlerTextField = new JTextField();
		sparklineComboBox = new JComboBox();
		sparklineJComponent = new SparklineJComponent();
		tablePanel = new TablePanel();
		CellConstraints cc = new CellConstraints();

		setLayout(new FormLayout(
				new ColumnSpec[] {
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.CENTER, Sizes.DEFAULT, FormSpec.NO_GROW)
				},
				new RowSpec[] {
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				}));

		label1.setText("Source Container:");
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label1, cc.xy(1, 1));

		sourceComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sourceComboBoxActionPerformed(e);
			}
		});
		add(sourceComboBox, cc.xywh(3, 1, 3, 1));

		label2.setText("Query");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		add(label2, cc.xy(3, 3));

		label3.setText("Message");
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		add(label3, cc.xy(5, 3));

		panel1.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default"),
				new RowSpec[] {
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC
				}));

		//---- executeButton ----
		executeButton.setText("Execute Query");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeButtonActionPerformed(e);
			}
		});
		panel1.add(executeButton, cc.xy(1, 1));

		//---- exportButton ----
		exportButton.setText("Export to CSV");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportButtonActionPerformed(e);
			}
		});
		panel1.add(exportButton, cc.xy(1, 3));

		add(panel1, cc.xy(1, 5));

		queryTextArea.setColumns(75);
		queryTextArea.setRows(10);
		queryTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		queryTextArea.setLineWrap(true);
		scrollPane2.setViewportView(queryTextArea);

		add(scrollPane2, cc.xy(3, 5));

		messageTextArea.setEditable(false);
		messageTextArea.setColumns(50);
		messageTextArea.setRows(10);
		messageTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		messageTextArea.setLineWrap(true);
		scrollPane3.setViewportView(messageTextArea);

		add(scrollPane3, cc.xy(5, 5));

		label4.setText("Function Handler:");
		add(label4, cc.xywh(1, 7, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		add(functionHandlerTextField, cc.xywh(3, 7, 3, 1));

		sparklineComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sparklineComboBoxActionPerformed(e);
			}
		});
		add(sparklineComboBox, cc.xy(1, 9));
		add(sparklineJComponent, cc.xywh(3, 9, 3, 1, CellConstraints.FILL, CellConstraints.FILL));


		add(tablePanel, cc.xywh(1, 11, 5, 1));
	}

	private JLabel label1;
	private JComboBox sourceComboBox;
	private JLabel label2;
	private JLabel label3;
	private JPanel panel1;
	private JButton executeButton;
	private JButton exportButton;
	private JScrollPane scrollPane2;
	private JTextArea queryTextArea;
	private JScrollPane scrollPane3;
	private JTextArea messageTextArea;
	private JLabel label4;
	private JTextField functionHandlerTextField;
	private JComboBox sparklineComboBox;
	private SparklineJComponent sparklineJComponent;
	private TablePanel tablePanel;
}
