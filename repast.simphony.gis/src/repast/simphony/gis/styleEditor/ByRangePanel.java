/*
 * Created by JFormDesigner on Wed Apr 11 11:12:31 EDT 2007
 */

package repast.simphony.gis.styleEditor;

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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.Layer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;

import repast.simphony.gis.display.LegendIconMaker;
import repast.simphony.gis.util.DoubleDocument;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #2
 */
public class ByRangePanel extends JPanel implements IStyleEditor {

	private static final String ID = ByRangePanel.class.toString();

	private SimpleFeatureType type;
	private SimpleFeature sample;

	private class IconCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			label.setText("");
			label.setIcon((Icon) value);
			label.setHorizontalAlignment(JLabel.CENTER);
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

	private Set<Class> numberTypes = new HashSet<Class>();
	private ByRangePanelMediator mediator;

	public ByRangePanel() {
		initComponents();
		numberTypes.add(int.class);
		numberTypes.add(double.class);
		numberTypes.add(long.class);
		numberTypes.add(float.class);
		numberTypes.add(Double.class);
		numberTypes.add(Integer.class);
		numberTypes.add(Long.class);
		numberTypes.add(Float.class);

		initListeners();
		paletteBox.setRenderer(new CellRenderer());
    startFld.setDocument(new DoubleDocument());
    endFld.setDocument(new DoubleDocument());
  }

	public Style getStyle() {
		FeatureTypeStyle fts = mediator.getFeatureTypeStyle();
		StyleFactory fac = CommonFactoryFinder.getStyleFactory();
		Style style = fac.createStyle();
		ArrayList styles = new ArrayList();
		styles.add(fts);
		style.featureTypeStyles().clear();
		style.featureTypeStyles().addAll(styles);
		
		style.getDescription().setAbstract(ID + ":" + attributeBox.getSelectedItem());
		return style;
	}

	private void initListeners() {
		classesSpn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int classes = ((Integer) classesSpn.getValue()).intValue();
				mediator.classesChanged(classes);
			}
		});

		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mediator.typeChanged();
			}
		});

		attributeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mediator.attributeChanged();
			}
		});

		paletteBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mediator.paletteChanged();
			}
		});

    endFld.setToolTipText("Hit enter to register new range");
    startFld.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.minChanged(Double.valueOf(startFld.getText()));
      }
    });

    endFld.setToolTipText("Hit enter to register new range");
    endFld.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.maxChanged(Double.valueOf(endFld.getText()));
      }
    });

    moreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Rule rule = mediator.getDefaultRule();
				if (rule != null) {
					SymbolEditorDialog dialog =
									new SymbolEditorDialog((JDialog) SwingUtilities.getWindowAncestor(ByRangePanel.this));
					dialog.init(type, rule);
					Rule newRule = dialog.display();
					if (newRule != null) {
						mediator.setDefaultRule(newRule);
						symbolLbl.setIcon(LegendIconMaker.makeLegendIcon(24, newRule, sample));
					}
				}
			}
		});


		previewTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && previewTable.getSelectedColumn() == 0) {
					int row = previewTable.getSelectedRow();
					SampleStyleTableModel tableModel = (SampleStyleTableModel) previewTable.getModel();
					Rule rule = tableModel.getRule(row);
					if (rule != null) {
						SymbolEditorDialog dialog =
										new SymbolEditorDialog((JDialog) SwingUtilities.getWindowAncestor(ByRangePanel.this));
						dialog.init(type, rule);
						Rule newRule = dialog.display();
						if (newRule != null) {
							tableModel.setRule(row, newRule);
							mediator.replaceRule(rule, newRule);
						}
					}
				}
			}
		});
	}

	public void init(Layer layer) {
		try {
			FeatureSource source = layer.getFeatureSource();
			this.type = (SimpleFeatureType)source.getSchema();

			Rule rule = layer.getStyle().featureTypeStyles().get(0).rules().get(0);
			mediator = new ByRangePanelMediator(source, rule);
			sample = (SimpleFeature) source.getFeatures().features().next();
			symbolLbl.setIcon(LegendIconMaker.makeLegendIcon(24, rule, sample));
			paletteBox.setModel(mediator.getPaletteModel());
			DefaultComboBoxModel model = mediator.getClassifcationTypeModel();
			typeBox.setModel(model);
			model = mediator.getAttributeModel();
			SimpleFeatureType type = (SimpleFeatureType)source.getSchema();
			for (AttributeType at : type.getTypes()) {
				if (numberTypes.contains(at.getBinding())) {
					model.addElement(at.getName());
				}
			}
			attributeBox.setModel(model);

			previewTable.setModel(mediator.getPreviewTableModel());
			previewTable.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());

			initFromStyle(layer.getStyle());
      startFld.setText(String.valueOf(mediator.getMin()));
      endFld.setText(String.valueOf(mediator.getMax()));

      mediator.classesChanged(((Integer) classesSpn.getValue()).intValue());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void initFromStyle(Style style) {
		String desc = "";
		if (style.getDescription().getAbstract() != null)
			desc = style.getDescription().getAbstract().toString();
		if (desc.contains(ByRangePanel.ID)) {
			String attribName = desc.substring(desc.indexOf(":") + 1, desc.length());
			java.util.List<Rule> rules = new ArrayList<Rule>();
			for (Rule rule : style.featureTypeStyles().get(0).rules()){
				// reusing the dsv, recreates the same rule every time
				// so we need to create a new one for each rule.
				DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
								CommonFactoryFinder.getStyleFactory(), CommonFactoryFinder.getFilterFactory2());
				dsv.visit(rule);
				rules.add((Rule) dsv.getCopy());
			}
			mediator.init(rules, attribName);
      if (rules.size() > 1) classesSpn.setValue(new Integer(rules.size() - 1));
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
		label5 = new JLabel();
		startFld = new JTextField();
		label6 = new JLabel();
		endFld = new JTextField();
		separator1 = compFactory.createSeparator("Classification");
		label3 = new JLabel();
		classesSpn = new JSpinner();
		label4 = new JLabel();
		typeBox = new JComboBox();
		separator3 = compFactory.createSeparator("Default Symbol");
		symbolLbl = new JLabel();
		panel1 = new JPanel();
		moreBtn = new JButton();
		separator2 = compFactory.createSeparator("");
		scrollPane1 = new JScrollPane();
		previewTable = new JTable();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, FormSpec.DEFAULT_GROW),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("min(default;100dlu):grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.PREF_COLSPEC
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			}));

		//---- label1 ----
		label1.setText("Attribute:");
		add(label1, cc.xy(1, 1));
		add(attributeBox, cc.xy(3, 1));

		//---- label2 ----
		label2.setText("Palette:");
		add(label2, cc.xy(5, 1));
		add(paletteBox, cc.xywh(7, 1, 3, 1));

		//---- label5 ----
		label5.setText("Range Start:");
		add(label5, cc.xy(1, 3));
		add(startFld, cc.xy(3, 3));

		//---- label6 ----
		label6.setText("Range End:");
		add(label6, cc.xy(5, 3));
		add(endFld, cc.xywh(7, 3, 3, 1));
		add(separator1, cc.xywh(1, 5, 9, 1));

		//---- label3 ----
		label3.setText("Classes:");
		add(label3, cc.xy(1, 7));

		//---- classesSpn ----
		classesSpn.setModel(new SpinnerNumberModel(2, 2, 12, 1));
		add(classesSpn, cc.xy(3, 7));

		//---- label4 ----
		label4.setText("Type:");
		add(label4, cc.xy(5, 7));
		add(typeBox, cc.xywh(7, 7, 3, 1));
		add(separator3, cc.xywh(1, 9, 9, 1));
		add(symbolLbl, cc.xy(1, 11));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				"pref",
				"pref"));

			//---- moreBtn ----
			moreBtn.setText("Edit");
			panel1.add(moreBtn, cc.xy(1, 1));
		}
		add(panel1, cc.xy(3, 11));
		add(separator2, cc.xywh(1, 13, 9, 1));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(previewTable);
		}
		add(scrollPane1, cc.xywh(1, 15, 9, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox attributeBox;
	private JLabel label2;
	private JComboBox paletteBox;
	private JLabel label5;
	private JTextField startFld;
	private JLabel label6;
	private JTextField endFld;
	private JComponent separator1;
	private JLabel label3;
	private JSpinner classesSpn;
	private JLabel label4;
	private JComboBox typeBox;
	private JComponent separator3;
	private JLabel symbolLbl;
	private JPanel panel1;
	private JButton moreBtn;
	private JComponent separator2;
	private JScrollPane scrollPane1;
	private JTable previewTable;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
