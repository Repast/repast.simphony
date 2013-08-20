package repast.simphony.gis.styleEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * The "Value Style" panel in the GisStyleEditor dialog that provides the
 * capability of setting the shape fill color using value rules.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class ByValuePanel extends JPanel implements IStyleEditor {

	private static final String ID = ByValuePanel.class.toString();

	private static MessageCenter msg = MessageCenter.getMessageCenter(ByValuePanel.class);
	private ValueTableModel tableModel;
	private SimpleFeatureType featureType;
	private int colorIndex = 1;
	private Set<Class> valueTypes = new HashSet<Class>();
	private FeatureSource<SimpleFeatureType,SimpleFeature> source;  // for shapefiles only

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
		valueTypes.add(int.class);
		valueTypes.add(double.class);
		valueTypes.add(long.class);
		valueTypes.add(float.class);
		valueTypes.add(Double.class);
		valueTypes.add(Integer.class);
		valueTypes.add(Long.class);
		valueTypes.add(Float.class);
		valueTypes.add(String.class);
		
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
				AttributeType aType = 
						featureType.getType(attributeBox.getSelectedItem().toString());
				tableModel.init(aType.getBinding());
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
						dialog.init(featureType, rule);
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

	/**
	 * Adds a rule based on either the FeatureType for agent features, or the 
	 * FeatureSource for shapefile features.
	 */
	private void addRule() {
		String att = attributeBox.getSelectedItem().toString();
		RuleCreator creator = new RuleCreator();
		Rule rule;
		
		// When styling an agent class
		if (source == null){  
			rule = creator.createValueRule(null, att, 
					getSymbolizerFactory(getColor(colorIndex++)));
			tableModel.addRule(rule);
		}
		
		// When styling a feature source as from a shapefile
		else try {
			SimpleFeature feature = source.getFeatures().features().next();
			rule = creator.createValueRule(feature, att, 
					getSymbolizerFactory(getColor(colorIndex++)));
			tableModel.addRule(rule);
		} 
		catch (IOException e) {
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

  /**
   * Adds a list of rules based on the complete set of features in a shapefile
   * FeatureSource. 
   */
  private void addAll() {
  	
  	try {
  		RuleCreator creator = new RuleCreator();
  		String att = attributeBox.getSelectedItem().toString();
  		Set<Object> vals = new HashSet<Object>();

  		FeatureIterator<SimpleFeature> iterator =  source.getFeatures().features();
  		while (iterator.hasNext()) {
  			SimpleFeature feature = iterator.next();
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
		style.getDescription().setAbstract(ID + ":" + att);
		return style;
	}

	public void init(FeatureType type, Style style, 
			FeatureSource<SimpleFeatureType, SimpleFeature> source) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		featureType = (SimpleFeatureType)type;
		this.source = source;
				
		for (AttributeType at : featureType.getTypes()) {
			if (valueTypes.contains(at.getBinding())) {
				model.addElement(at.getName());
			}
		}
		
		attributeBox.setModel(model);
		tableModel = new ValueTableModel(featureType, style);
		valueTable.setModel(tableModel);
		valueTable.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());
		valueTable.getColumnModel().getColumn(1).setCellRenderer(new LabelCellRenderer());
		valueTable.getColumnModel().getColumn(2).setCellRenderer(new LabelCellRenderer());
		
		// Disable the "Add All" button which is used to add all Feature attribute
		//  values contained in a feature source, as from a shapefile.
		if (source == null)
			addAllBtn.setEnabled(false);
		
		initTable(style);
	}

	private void initTable(Style style) {
		AttributeType aType = featureType.getType(attributeBox.getSelectedItem().toString());
		String desc = "";

		if (style.getDescription().getAbstract() != null)
			desc = style.getDescription().getAbstract().toString();
		
		if (desc.contains(ByValuePanel.ID)) {
			String attribName = desc.substring(desc.indexOf(":") + 1, desc.length());
			attributeBox.setSelectedItem(attribName);
			java.util.List<Rule> rules = new ArrayList<Rule>();
			for (Rule rule : style.featureTypeStyles().get(0).rules()) {
				// reusing the dsv, recreates the same rule every time
				// so we need to create a new one for each rule.
				DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
							CommonFactoryFinder.getStyleFactory(), CommonFactoryFinder.getFilterFactory2());
				dsv.visit(rule);
				rules.add((Rule) dsv.getCopy());
			}

			tableModel.init(aType.getClass(), rules);
		} else {
			tableModel.init(aType.getClass());
		}
	}

	private void initComponents() {
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		label1 = new JLabel();
		attributeBox = new JComboBox();
		paletteLabel = new JLabel();
		paletteBox = new JComboBox();
		valuesSeparator = compFactory.createSeparator("Values");
		defaultBox = new JCheckBox();
		scrollPane1 = new JScrollPane();
		valueTable = new JTable();
		separator2 = compFactory.createSeparator("");
		panel1 = new JPanel();
		addAllBtn = new JButton();
		addBtn = new JButton();
		deleteBtn = new JButton();
		CellConstraints cc = new CellConstraints();

		setLayout(new FormLayout(
						new ColumnSpec[]{
										FormSpecs.DEFAULT_COLSPEC,
										FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
										new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
										FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
										FormSpecs.DEFAULT_COLSPEC,
										FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
										new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[]{
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.LINE_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.LINE_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.LINE_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.LINE_GAP_ROWSPEC,
										new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
										FormSpecs.LINE_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC,
										FormSpecs.LINE_GAP_ROWSPEC,
										FormSpecs.DEFAULT_ROWSPEC
						}));

		
	  // Span the attribute and palette elements across multiple columns to 
		//  provide room for large agent attribute names and palette icons
		label1.setText("Attribute:");
		add(label1, cc.xy(1, 1));
		add(attributeBox, cc.xywh(3, 1, 5, 1));

		paletteLabel.setText("Palette:");
		add(paletteLabel, cc.xy(1, 3));
		add(paletteBox, cc.xywh(3, 3, 5, 1));
		add(valuesSeparator, cc.xywh(1, 5, 7, 1));

		defaultBox.setText("Include Default");
		defaultBox.setSelected(true);
		add(defaultBox, cc.xywh(1, 7, 3, 1));

		scrollPane1.setViewportView(valueTable);
		
		add(scrollPane1, cc.xywh(1, 9, 7, 1));
		add(separator2, cc.xywh(1, 11, 7, 1));


		panel1.setLayout(new FormLayout(
				new ColumnSpec[]{
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						FormSpecs.PREF_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

		addAllBtn.setText("Add All Values");
		panel1.add(addAllBtn, cc.xy(1, 1));

		addBtn.setText("Add Value");
		panel1.add(addBtn, cc.xy(3, 1));

		deleteBtn.setText("Delete Value");
		panel1.add(deleteBtn, cc.xy(5, 1));

		add(panel1, cc.xywh(1, 13, 7, 1));
	}

	private JLabel label1;
	private JComboBox attributeBox;
	private JLabel paletteLabel;
	private JComboBox paletteBox;
	private JComponent valuesSeparator;
	private JCheckBox defaultBox;
	private JScrollPane scrollPane1;
	private JTable valueTable;
	private JComponent separator2;
	private JPanel panel1;
	private JButton addAllBtn;
	private JButton addBtn;
	private JButton deleteBtn;
}