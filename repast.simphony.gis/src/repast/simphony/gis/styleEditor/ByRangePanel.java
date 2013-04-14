package repast.simphony.gis.styleEditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

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
 * The "Range Style" panel in the GisStyleEditor dialog that provides the
 * capability of setting the shape fill color using ranged rules.
 * 
 * TODO Geotools [major] add mark size ranging
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class ByRangePanel extends JPanel implements IStyleEditor {

	private static final String ID = ByRangePanel.class.toString();

	private SimpleFeatureType type;
	
	private class IconCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, 
					isSelected, hasFocus, row, column);
			label.setText("");
			label.setIcon((Icon) value);
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}

	private class CellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, 
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, 
					index, isSelected, cellHasFocus);
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
		style.featureTypeStyles().clear();		
		style.featureTypeStyles().add(fts);
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

    // Edit the default shape
    moreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Rule rule = mediator.getDefaultRule();
				if (rule != null) {
					SymbolEditorDialog dialog =
									new SymbolEditorDialog((JDialog) SwingUtilities.getWindowAncestor(ByRangePanel.this));
					dialog.init(type, rule);
					Rule newRule = dialog.display();
					SampleStyleTableModel tableModel = (SampleStyleTableModel) previewTable.getModel();
					if (newRule != null) {
						mediator.setDefaultRule(newRule);
						symbolLbl.setIcon(PreviewLabel.createSmallIcon(newRule));
					}
				}
			}
		});

    // Provide the style editor dialog to each row icon for custom styling.
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

	public void init(FeatureType featureType, Style style) {
			this.type = (SimpleFeatureType)featureType;

			Rule rule = style.featureTypeStyles().get(0).rules().get(0);
			mediator = new ByRangePanelMediator(featureType, rule);
					
			symbolLbl.setIcon(PreviewLabel.createSmallIcon(rule));
			
			paletteBox.setModel(mediator.getPaletteModel());
			DefaultComboBoxModel model = mediator.getClassifcationTypeModel();
			typeBox.setModel(model);
			model = mediator.getAttributeModel();
			
			for (AttributeType at : type.getTypes()) {
				if (numberTypes.contains(at.getBinding())) {
					model.addElement(at.getName());
				}
			}
			attributeBox.setModel(model);

			previewTable.setModel(mediator.getPreviewTableModel());
			previewTable.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());

			initFromStyle(style);
      startFld.setText(String.valueOf(mediator.getMin()));
      endFld.setText(String.valueOf(mediator.getMax()));

      mediator.classesChanged(((Integer) classesSpn.getValue()).intValue());
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
      if (rules.size() > 1) 
      	classesSpn.setValue(new Integer(rules.size() - 1));
    }
	}

	private void initComponents() {
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		attributeLabel = new JLabel();
		attributeBox = new JComboBox();
		paletteLabel = new JLabel();
		paletteBox = new JComboBox();
		rangeStartLabel = new JLabel();
		startFld = new JTextField();
		rangeEndLabel = new JLabel();
		endFld = new JTextField();
		categorySeparator = compFactory.createSeparator("Categories");
		classesLabel = new JLabel();
		classesSpn = new JSpinner();
		typeLabel = new JLabel();
		typeBox = new JComboBox();
		defaultSymbolSeparator = compFactory.createSeparator("Default Symbol");
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

		attributeLabel.setText("Attribute:");
		add(attributeLabel, cc.xy(1, 1));
		add(attributeBox, cc.xy(3, 1));

		paletteLabel.setText("Palette:");
		add(paletteLabel, cc.xy(5, 1));
		add(paletteBox, cc.xywh(7, 1, 3, 1));

		rangeStartLabel.setText("Range Start:");
		add(rangeStartLabel, cc.xy(1, 3));
		add(startFld, cc.xy(3, 3));

		rangeEndLabel.setText("Range End:");
		add(rangeEndLabel, cc.xy(5, 3));
		add(endFld, cc.xywh(7, 3, 3, 1));
		add(categorySeparator, cc.xywh(1, 5, 9, 1));

		classesLabel.setText("Classes:");
		add(classesLabel, cc.xy(1, 7));

		classesSpn.setModel(new SpinnerNumberModel(2, 2, 12, 1));
		add(classesSpn, cc.xy(3, 7));

		typeLabel.setText("Type:");
		add(typeLabel, cc.xy(5, 7));
		add(typeBox, cc.xywh(7, 7, 3, 1));
		add(defaultSymbolSeparator, cc.xywh(1, 9, 9, 1));
		add(symbolLbl, cc.xy(1, 11));

		panel1.setLayout(new FormLayout("pref",	"pref"));
		moreBtn.setText("Edit");
		panel1.add(moreBtn, cc.xy(1, 1));

		add(panel1, cc.xy(3, 11));
		add(separator2, cc.xywh(1, 13, 9, 1));

		scrollPane1.setViewportView(previewTable);

		add(scrollPane1, cc.xywh(1, 15, 9, 1));
	}
	
	private JLabel attributeLabel;
	private JComboBox attributeBox;
	private JLabel paletteLabel;
	private JComboBox paletteBox;
	private JLabel rangeStartLabel;
	private JTextField startFld;
	private JLabel rangeEndLabel;
	private JTextField endFld;
	private JComponent categorySeparator;
	private JLabel classesLabel;
	private JSpinner classesSpn;
	private JLabel typeLabel;
	private JComboBox typeBox;
	private JComponent defaultSymbolSeparator;
	private JLabel symbolLbl;
	private JPanel panel1;
	private JButton moreBtn;
	private JComponent separator2;
	private JScrollPane scrollPane1;
	private JTable previewTable;
}