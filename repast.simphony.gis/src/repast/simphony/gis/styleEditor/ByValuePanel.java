/*
 * Created by JFormDesigner on Thu Apr 12 15:44:27 EDT 2007
 */

package repast.simphony.gis.styleEditor;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.data.FeatureSource;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.map.MapLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author User #2
 */
public class ByValuePanel extends JPanel implements IStyleEditor {

	private static final String ID = ByValuePanel.class.toString();

	private static MessageCenter msg = MessageCenter.getMessageCenter(ByValuePanel.class);
	private ValueTableModel tableModel;
	private FeatureSource source;
	private int colorIndex = 1;

  private class IconCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			label.setText("");
			label.setIcon((Icon) value);
			if (row == 0) {
				label.setEnabled(defaultBox.isSelected());
			} else {
				label.setEnabled(true);
			}
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}

	private class LabelCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (row == 0) {
				label.setEnabled(defaultBox.isSelected());
			} else {
				label.setEnabled(true);
			}
			return label;
		}
	}

	private class CellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
		                                              boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value != null) {
				Palette palette = (Palette) value;
				label.setText(""); //palette.getDescription());
				label.setIcon(new PaletteIcon(palette, PaletteIcon.Orientation.HORIZONTAL));
				label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			}
			return label;
		}
	}

	public ByValuePanel() {
		initComponents();
		deleteBtn.setEnabled(false);
		paletteBox.setRenderer(new CellRenderer());

		DefaultComboBoxModel model = new DefaultComboBoxModel();
		BrewerPalette[] pals = ColorBrewer.instance().getPalettes(ColorBrewer.ALL, 10);
		for (BrewerPalette pal : pals) {
			Palette palette = new Palette(pal.getColors(10), pal.getDescription());
			model.addElement(palette);
		}
		paletteBox.setModel(model);
		valueTable.setShowHorizontalLines(false);
		valueTable.setShowVerticalLines(false);
		valueTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		initListeners();
	}

	private void initListeners() {

    paletteBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        resetRuleColors();
      }
    });

    deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				delete();
			}
		});


		addAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addAll();
			}
		});

		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addRule();
			}
		});

		attributeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AttributeType aType = source.getSchema().
								getAttributeType(attributeBox.getSelectedItem().toString());
				tableModel.init(aType.getType());
			}
		});

		defaultBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tableModel.useDefaultChanged();
			}
		});

		valueTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int[] rows = valueTable.getSelectedRows();
					deleteBtn.setEnabled(!(rows == null || (rows.length == 1 && rows[0] == 0) || rows.length == 0));
				}
			}
		});

		valueTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && valueTable.getSelectedColumn() == 0) {
					int row = valueTable.getSelectedRow();
					Rule rule = tableModel.getRule(row);
					if (rule != null) {
						SymbolEditorDialog dialog =
										new SymbolEditorDialog((JDialog) SwingUtilities.getWindowAncestor(ByValuePanel.this));
						dialog.init(source.getSchema(), rule);
						Rule newRule = dialog.display();
						if (newRule != null) {
							tableModel.setRule(row, newRule);
						}
					}
				}
			}
		});

	}

	private void delete() {
		int[] selected = valueTable.getSelectedRows();
		if (selected != null) {
			tableModel.delete(selected);
		}
    colorIndex = tableModel.getRowCount() + 1;
  }

	private SymbolizerFactory getSymbolizerFactory(Color color) {
		Rule rule = tableModel.getDefaultRule();
		Symbolizer sym = rule.getSymbolizers()[0];
		return SymbolizerFactoryBuilder.getSymbolizerFactory(color, sym);
	}

	private void addRule() {
		try {
			RuleCreator creator = new RuleCreator();
			String att = attributeBox.getSelectedItem().toString();
			Feature feature = (Feature) source.getFeatures().iterator().next();
			Rule rule = creator.createValueRule(feature, att, getSymbolizerFactory(getColor(colorIndex++)));
			tableModel.addRule(rule);
		} catch (IOException e) {
			msg.error("Error getting features", e);
		}
	}

  private void resetRuleColors() {
    java.util.List<Rule> rules = tableModel.getRules(false);
    colorIndex = 0;
    for (Rule rule : rules) {
      RuleCreator.setColor(rule, getColor(colorIndex++));
    }
    tableModel.fireTableDataChanged();
  }

  private void addAll() {
		try {
			RuleCreator creator = new RuleCreator();
			String att = attributeBox.getSelectedItem().toString();
			Set<Object> vals = new HashSet<Object>();
			for (Iterator iter = source.getFeatures().iterator(); iter.hasNext();) {
				Feature feature = (Feature) iter.next();
				Object obj = feature.getAttribute(att);
				if (obj != null && !vals.contains(obj)) {
					Rule rule = creator.createValueRule(feature, att, getSymbolizerFactory(getColor(colorIndex++)));
					tableModel.addRule(rule);
					vals.add(obj);
				}
			}
		} catch (IOException e) {
			msg.error("Error getting features", e);
		}
	}

	private Color getColor(int val) {
		Palette pal = (Palette) paletteBox.getSelectedItem();
		int lastIndex = pal.getColorCount() - 1;
		if (val > lastIndex) return pal.getColor(lastIndex);
		else return pal.getColor(val);
	}

	public Style getStyle() {
		java.util.List<Rule> rules = tableModel.getRules(defaultBox.isSelected());
		String att = attributeBox.getSelectedItem().toString();
		Style style = new RuleCreator().createStyle(att, rules);
		style.setAbstract(ID + ":" + att);
		return style;
	}

	public void init(MapLayer layer) {
		source = layer.getFeatureSource();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		FeatureType type = source.getSchema();
		for (AttributeType at : type.getAttributeTypes()) {
			String name = at.getName();
			if (!name.equals("the_geom")) model.addElement(name);
		}
		attributeBox.setModel(model);
		try {
			Feature feature = (Feature) source.getFeatures().iterator().next();
			tableModel = new ValueTableModel(feature);
		} catch (IOException ex) {
			msg.error("Error initializing ByValuePanel", ex);
		}
		valueTable.setModel(tableModel);
		valueTable.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());
		valueTable.getColumnModel().getColumn(1).setCellRenderer(new LabelCellRenderer());
		valueTable.getColumnModel().getColumn(2).setCellRenderer(new LabelCellRenderer());

		Style style = layer.getStyle();
		initTable(style);
	}

	private void initTable(Style style) {
		FeatureType type = source.getSchema();
		AttributeType aType = type.getAttributeType(attributeBox.getSelectedItem().toString());
		String desc = style.getAbstract();

		if (desc.contains(ByValuePanel.ID)) {
			String attribName = desc.substring(desc.indexOf(":") + 1, desc.length());
			attributeBox.setSelectedItem(attribName);
			java.util.List<Rule> rules = new ArrayList<Rule>();
			for (Rule rule : style.getFeatureTypeStyles()[0].getRules()) {
				// reusing the dsv, recreates the same rule every time
				// so we need to create a new one for each rule.
				DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
							StyleFactoryFinder.createStyleFactory(), FilterFactoryFinder
							.createFilterFactory());
				dsv.visit(rule);
				rules.add((Rule) dsv.getCopy());
			}

			tableModel.init(aType.getType(), rules);
		} else {
			tableModel.init(aType.getType());
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		label1 = new JLabel();
		attributeBox = new JComboBox();
		label2 = new JLabel();
		paletteBox = new JComboBox();
		separator1 = compFactory.createSeparator("Values");
		defaultBox = new JCheckBox();
		scrollPane1 = new JScrollPane();
		valueTable = new JTable();
		separator2 = compFactory.createSeparator("");
		panel1 = new JPanel();
		addAllBtn = new JButton();
		addBtn = new JButton();
		deleteBtn = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
						new ColumnSpec[]{
										FormFactory.DEFAULT_COLSPEC,
										FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
										new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
										FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
										FormFactory.DEFAULT_COLSPEC,
										FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
										new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[]{
										FormFactory.DEFAULT_ROWSPEC,
										FormFactory.LINE_GAP_ROWSPEC,
										FormFactory.DEFAULT_ROWSPEC,
										FormFactory.LINE_GAP_ROWSPEC,
										FormFactory.DEFAULT_ROWSPEC,
										FormFactory.LINE_GAP_ROWSPEC,
										new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
										FormFactory.LINE_GAP_ROWSPEC,
										FormFactory.DEFAULT_ROWSPEC,
										FormFactory.LINE_GAP_ROWSPEC,
										FormFactory.DEFAULT_ROWSPEC
						}));

		//---- label1 ----
		label1.setText("Attribute:");
		add(label1, cc.xy(1, 1));
		add(attributeBox, cc.xy(3, 1));

		//---- label2 ----
		label2.setText("Palette:");
		add(label2, cc.xy(5, 1));
		add(paletteBox, cc.xy(7, 1));
		add(separator1, cc.xywh(1, 3, 7, 1));

		//---- defaultBox ----
		defaultBox.setText("Include Default");
		defaultBox.setSelected(true);
		add(defaultBox, cc.xywh(1, 5, 3, 1));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(valueTable);
		}
		add(scrollPane1, cc.xywh(1, 7, 7, 1));
		add(separator2, cc.xywh(1, 9, 7, 1));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
							new ColumnSpec[]{
											FormFactory.DEFAULT_COLSPEC,
											FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
											FormFactory.DEFAULT_COLSPEC,
											FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
											FormFactory.PREF_COLSPEC
							},
							RowSpec.decodeSpecs("default")));

			//---- addAllBtn ----
			addAllBtn.setText("Add All Values");
			panel1.add(addAllBtn, cc.xy(1, 1));

			//---- addBtn ----
			addBtn.setText("Add Value");
			panel1.add(addBtn, cc.xy(3, 1));

			//---- deleteBtn ----
			deleteBtn.setText("Delete Value");
			panel1.add(deleteBtn, cc.xy(5, 1));
		}
		add(panel1, cc.xywh(1, 11, 7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox attributeBox;
	private JLabel label2;
	private JComboBox paletteBox;
	private JComponent separator1;
	private JCheckBox defaultBox;
	private JScrollPane scrollPane1;
	private JTable valueTable;
	private JComponent separator2;
	private JPanel panel1;
	private JButton addAllBtn;
	private JButton addBtn;
	private JButton deleteBtn;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
