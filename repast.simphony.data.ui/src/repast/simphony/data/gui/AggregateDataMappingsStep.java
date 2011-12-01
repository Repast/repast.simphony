/*CopyrightHere*/
package repast.simphony.data.gui;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.pietschy.wizard.InvalidStateException;
import repast.simphony.data.logging.gather.AggregateDataMapping;
import repast.simphony.data.logging.gather.TimeDataMapping;
import repast.simphony.data.logging.gather.aggregate.AbstractStatsAggregateMapping;
import repast.simphony.ui.widget.DropDownButton;
import repast.simphony.util.wizard.DynamicWizard;
import repast.simphony.util.wizard.ModelAwarePanelStep;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jerry Vos
 */
public class AggregateDataMappingsStep extends ModelAwarePanelStep<DataSetWizardModel> {
	private static final long serialVersionUID = -6674308239621216483L;
	
	private static int count = 0;
	
	private AggregateDataMappingTableModel primaryModel;
	private AggregateDataMappingTableModel alternatedModel;
	
	public AggregateDataMappingsStep() {
		super("Aggregate Mappings", "Select what aggregate data you would like to produce.");
		
		initComponents();
	}

	private void removePrimaryButtonActionPerformed(ActionEvent e) {
		primaryModel.deleteRow(primaryTable.getSelectedRow());
	}

	private void removeAlternatedButtonActionPerformed(ActionEvent e) {
		alternatedModel.deleteRow(alternatedTable.getSelectedRow());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		panel1 = new JPanel();
		label3 = new JLabel();
		separator1 = compFactory.createSeparator("Mappings to Alternate Execution Of");
		label1 = new JLabel();
		sharedNameField = new JTextField();
		label2 = new JLabel();
		nameColumnField = new JTextField();
		scrollPane1 = new JScrollPane();
		alternatedTable = new JTable();
		sharedBar = new JPanel();
		addAlternatedButton = new DropDownButton();
		removeAlternatedButton = new JButton();
		separator2 = compFactory.createSeparator("Mappings to Execute with Every Mapping Above");
		scrollPane2 = new JScrollPane();
		primaryTable = new JTable();
		normalBar = new JPanel();
		addRepeatedButton = new DropDownButton();
		removePrimaryButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//======== panel1 ========
		{
			panel1.setBackground(Color.white);
			panel1.setBorder(new EtchedBorder());
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));

			//---- label3 ----
			label3.setText("<HTML>The first table represents mappings that map to the same column value (for example value).<br> These are executed alongside the mappings in the second table.");
			label3.setBackground(Color.white);
			panel1.add(label3, cc.xywh(1, 1, 3, 1));
		}
		add(panel1, cc.xywh(1, 1, 3, 1));
		add(separator1, cc.xywh(1, 3, 3, 1));

		//---- label1 ----
		label1.setText("Shared Column Name");
		add(label1, cc.xy(1, 5));

		//---- sharedNameField ----
		sharedNameField.setText("Value");
		add(sharedNameField, cc.xy(3, 5));

		//---- label2 ----
		label2.setText("Column Name For Name");
		add(label2, cc.xy(1, 7));

		//---- nameColumnField ----
		nameColumnField.setText("Name");
		add(nameColumnField, cc.xy(3, 7));

		//======== scrollPane1 ========
		{

			//---- alternatedTable ----
			alternatedTable.setCellSelectionEnabled(true);
			alternatedTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
				},
				new String[] {
					"Mapping Name", "Source"
				}
			));
			alternatedTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
			scrollPane1.setViewportView(alternatedTable);
		}
		add(scrollPane1, cc.xywh(1, 9, 3, 1));

		//======== sharedBar ========
		{
			sharedBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
			sharedBar.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.GLUE_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GLUE_COLSPEC
				},
				RowSpec.decodeSpecs("pref")));

			//---- addAlternatedButton ----
			addAlternatedButton.setText("Add");
			addAlternatedButton.setMnemonic('A');
			sharedBar.add(addAlternatedButton, cc.xy(3, 1));

			//---- removeAlternatedButton ----
			removeAlternatedButton.setText("Remove");
			removeAlternatedButton.setMnemonic('R');
			removeAlternatedButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeAlternatedButtonActionPerformed(e);
				}
			});
			sharedBar.add(removeAlternatedButton, cc.xy(5, 1));
		}
		add(sharedBar, cc.xywh(1, 11, 3, 1));
		add(separator2, cc.xywh(1, 13, 3, 1));

		//======== scrollPane2 ========
		{

			//---- primaryTable ----
			primaryTable.setCellSelectionEnabled(true);
			primaryTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
				},
				new String[] {
					"Column Name", "Source"
				}
			));
			primaryTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
			scrollPane2.setViewportView(primaryTable);
		}
		add(scrollPane2, cc.xywh(1, 15, 3, 1));

		//======== normalBar ========
		{
			normalBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
			normalBar.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.GLUE_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GLUE_COLSPEC
				},
				RowSpec.decodeSpecs("pref")));

			//---- addRepeatedButton ----
			addRepeatedButton.setText("Add");
			addRepeatedButton.setMnemonic('A');
			normalBar.add(addRepeatedButton, cc.xy(3, 1));

			//---- removePrimaryButton ----
			removePrimaryButton.setText("Remove");
			removePrimaryButton.setMnemonic('R');
			removePrimaryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removePrimaryButtonActionPerformed(e);
				}
			});
			normalBar.add(removePrimaryButton, cc.xy(5, 1));
		}
		add(normalBar, cc.xywh(1, 17, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		addCompleteListener(sharedNameField);
		
		setupTable();
		
		setComplete(true);
		
		setupAddButtons();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel label3;
	private JComponent separator1;
	private JLabel label1;
	private JTextField sharedNameField;
	private JLabel label2;
	private JTextField nameColumnField;
	private JScrollPane scrollPane1;
	private JTable alternatedTable;
	private JPanel sharedBar;
	private DropDownButton addAlternatedButton;
	private JButton removeAlternatedButton;
	private JComponent separator2;
	private JScrollPane scrollPane2;
	private JTable primaryTable;
	private JPanel normalBar;
	private DropDownButton addRepeatedButton;
	private JButton removePrimaryButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	@SuppressWarnings("serial")
	private void setupTable() {
		alternatedModel = new AggregateDataMappingTableModel(alternatedTable.getModel().getColumnName(0), alternatedTable.getModel().getColumnName(1)) {
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				Object value = getValueAt(rowIndex, columnIndex);
				if (value instanceof MappingSourceRepresentation) {
					MappingSourceRepresentation rep = (MappingSourceRepresentation) value;
					return rep.isMappingEditable();
				}
				return super.isCellEditable(rowIndex, columnIndex);
			}
		};
		alternatedTable.setModel(alternatedModel);
		primaryModel = new AggregateDataMappingTableModel(primaryTable.getModel().getColumnName(0), primaryTable.getModel().getColumnName(1)) {
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				Object value = getValueAt(rowIndex, columnIndex);
				if (value instanceof MappingSourceRepresentation) {
					MappingSourceRepresentation rep = (MappingSourceRepresentation) value;
					return rep.isMappingEditable();
				}
				return super.isCellEditable(rowIndex, columnIndex);
			}
		};
		primaryTable.setModel(primaryModel);
		
		alternatedTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		primaryTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		alternatedTable.setRowHeight(alternatedTable.getRowHeight() + 4);
		primaryTable.setRowHeight(primaryTable.getRowHeight() + 4);

		primaryTable.setDefaultRenderer(MappingSourceRepresentation.class, new AggregateMappingSourceRenderer());
		alternatedTable.setDefaultRenderer(MappingSourceRepresentation.class, new AggregateMappingSourceRenderer());
		
		primaryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					removePrimaryButton.setEnabled(primaryTable.getSelectedRow() != -1);
				}
			}
		});
		alternatedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					removeAlternatedButton.setEnabled(alternatedTable.getSelectedRow() != -1);
				}
			}
		});
	}
	
	@Override
	public void prepare() {
		super.prepare();
		
		if (model.getDescriptor().getAlternatedColumnName() != null) {
			sharedNameField.setText(model.getDescriptor().getAlternatedColumnName());			
		}
		if (model.getDescriptor().getNameColumnName() != null) {
			nameColumnField.setText(model.getDescriptor().getNameColumnName());			
		}
		refreshTables();
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		if (!sharedNameField.getText().equals("")) {
			model.getDescriptor().setAlternatedColumnName(sharedNameField.getText());
		}
		if (!this.nameColumnField.getText().equals("")) {
			model.getDescriptor().setNameColumnName(nameColumnField.getText());
		}
		model.setPrimaryAggregateDataMappings(primaryModel.createMappingsMap());
		model.setAlternatedAggregateDataMappings(alternatedModel.createMappingsMap());
	}
	
	private AggregateMappingSourceRepresentation findAggregateRep(final AggregateDataMapping mapping) {
		return new AggregateMappingSourceRepresentation() {
			public AggregateDataMapping createMapping() {
				return mapping;
			}

			public boolean equalsMappingSource(AggregateDataMapping mapping) {
				return false;
			}

			public boolean isMappingEditable() {
				return false;
			}
			
			@Override
			public String toString() {
				return mapping.toString();
			}
		};
	}

	private void refreshTables() {
		alternatedModel.clearMappings();
		primaryModel.clearMappings();

		if (model.getPrimaryAggregateDataMappings() != null) {
			for (AggregateDataMapping mapping : model.getPrimaryAggregateDataMappings()) {
				AggregateMappingSourceRepresentation rep = findAggregateRep(mapping);
				if (rep != null)
					addMapping(false, model.getDescriptor().getPrimaryAggregateColumnName(mapping), rep);
			}			
		}
		if (model.getAlternatedAggregateDataMappings() != null) {
			for (AggregateDataMapping mapping : model.getAlternatedAggregateDataMappings()) {
				AggregateMappingSourceRepresentation rep = findAggregateRep(mapping);
				if (rep != null)
					addMapping(true, model.getDescriptor().getAlternatedMappingName(mapping), rep);
			}			
		}
	}
	
	private void setupAddButtons() {
		addAlternatedButton.setPopupMenu(buildAddMenu(true));
		addRepeatedButton.setPopupMenu(buildAddMenu(false));
	}
	
	private JPopupMenu buildAddMenu(final boolean alternated) {
		final JPopupMenu addMenu = new JPopupMenu();

		JMenuItem item = new JMenuItem("Add Tick Mapping");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addMapping(alternated, TimeDataMapping.TICK_COLUMN, new repast.simphony.data.gui.aggregate.TimeSourceRepresentation());
			}
		});
		item.setMnemonic('m');
		addMenu.add(item);
		
		item = new JMenuItem("Add Run Number Mapping");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addMapping(alternated, "Run Number", new repast.simphony.data.gui.aggregate.RunNumberRepresentation());
			}
		});
		item.setMnemonic('r');
		addMenu.add(item);

		item = new JMenuItem("Add Using Wizard");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DynamicWizard wizard = DataMappingWizardPluginUtil.create(true, model.getAgentClass());
//				DataMappingWizard wizard = new DataMappingWizard();
				wizard.showDialog(AggregateDataMappingsStep.this, "Mapping Creator");

				if (wizard.wasCancelled()) {
					return;
				}
				
				DataMappingWizardModel model = (DataMappingWizardModel) wizard.getModel();
				
				final AbstractStatsAggregateMapping mapping = model.getAggregator();
				//mapping.setDecorated(model.getMappingRepresentation().createMapping());
				if (model.getMappingRepresentation() != null) {
					addMapping(alternated, "Aggregate Mapping " + count ++, new AggregateMappingSourceRepresentation() {
						public AggregateDataMapping createMapping() {
							return mapping;
						}

						public boolean equalsMappingSource(AggregateDataMapping eqMapping) {
							return mapping == eqMapping;
						}

						public boolean isMappingEditable() {
							return false;
						}
					});
				}
			}
		});
		item.setMnemonic('c');
		addMenu.add(item);
		
		return addMenu;
	}
	
	private void addMapping(boolean alternated, String name, AggregateMappingSourceRepresentation rep) {
		AggregateDataMappingTableModel model;
		JTable table;
		if (alternated) {
			model = alternatedModel;
			table = alternatedTable;
		} else {
			model = primaryModel;
			table = primaryTable;
		}
		model.addMapping(name, rep);
		table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
		table.scrollRectToVisible(table.getCellRect(model.getRowCount() - 1, 0, true));
	}
	
	@Override
	protected void updateComplete() {
		super.updateComplete();
		setComplete(alternatedTable.getRowCount() == 0 || !sharedNameField.getText().equals(""));
	}
}

class AggregateMappingSourceRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 5330076317696004972L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		if (value != null) {
			return super.getTableCellRendererComponent(table, value.toString(), isSelected,
					hasFocus, row, column);
		} else {
			return super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
		}
	}
}