/*
 * Created by JFormDesigner on Mon Jul 31 09:35:46 CDT 2006
 */

package repast.simphony.gis.styleEditor;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This is a component for editing multiple rules within a style
 * 
 * @author User #1
 */
public class RuledStyleEditorPanel extends JPanel implements IStyleEditor {
	DefaultListModel model;

	MapLayer layer;

	StyleBuilder builder = new StyleBuilder();

	public RuledStyleEditorPanel(MapLayer layer) {
		initComponents();
		setMapLayer(layer);
	}

	public RuledStyleEditorPanel() {
		initComponents();
	}

	public void setMapLayer(MapLayer layer) {
		this.layer = layer;
		ruleEditPanel1.setRule(layer.getStyle().getFeatureTypeStyles()[0]
				.getRules()[0]);
		list1.setSelectedIndex(0);
		Style style = layer.getStyle();
		for (Rule rule : style.getFeatureTypeStyles()[0].getRules()) {
			model.addElement(rule);
		}
	}

	private void removeRuleButtonActionPerformed(ActionEvent e) {
		model.removeElement(list1.getSelectedValue());
	}

	private void list1MouseClicked(MouseEvent e) {
		Rule selected = (Rule) list1.getSelectedValue();
		ruleEditPanel1.setRule(selected);
		if (e.getClickCount() == 2) {
			String s = (String) JOptionPane.showInputDialog(this,
					"Choose a title for your rule");
			Rule rule = (Rule) list1.getSelectedValue();
			rule.setTitle(s);
		}
	}

	private void addRuleButtonActionPerformed(ActionEvent e) {
		String s = (String) JOptionPane.showInputDialog(this,
				"Choose a title for your rule");
		if (s == null) {
			return;
		}
		Rule rule = builder.createRule(DefaultStyler.getDefaultSymbolizer(layer
				.getFeatureSource()));
		rule.setTitle(s);
		model.addElement(rule);
	}

	private void list1ValueChanged(ListSelectionEvent e) {
		ruleEditPanel1.setRule((Rule) list1.getSelectedValue());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		title1 = compFactory.createTitle("Style Rules");
		panel2 = new JPanel();
		scrollPane1 = new JScrollPane();
		model = new DefaultListModel();
		list1 = new JList(model);
		panel1 = new JPanel();
		ruleEditPanel1 = new RuleEditPanel();
		panel3 = new JPanel();
		addRuleButton = new JButton();
		removeRuleButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC
			},
			new RowSpec[] {
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));
		add(title1, cc.xy(3, 1));

		//======== panel2 ========
		{
			panel2.setLayout(new BorderLayout());

			//======== scrollPane1 ========
			{

				//---- list1 ----
				list1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						list1MouseClicked(e);
					}
				});
				list1.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						list1ValueChanged(e);
					}
				});
						list1.setCellRenderer(new DefaultListCellRenderer() {

							@Override
							public Component getListCellRendererComponent(JList list,
									Object value, int index, boolean isSelected,
									boolean cellHasFocus) {
								JLabel label = (JLabel) super.getListCellRendererComponent(
										list, value, index, isSelected, cellHasFocus);
								label.setText(((Rule) value).getTitle());
								return label;
							}

						});
				scrollPane1.setViewportView(list1);
			}
			panel2.add(scrollPane1, BorderLayout.CENTER);
		}
		add(panel2, cc.xy(3, 3));

		//======== panel1 ========
		{
			panel1.setLayout(new BorderLayout());

			//---- ruleEditPanel1 ----
			ruleEditPanel1.showFilterPane(true);
			panel1.add(ruleEditPanel1, BorderLayout.CENTER);
		}
		add(panel1, cc.xy(5, 3));

		//======== panel3 ========
		{
			panel3.setLayout(new FlowLayout());

			//---- addRuleButton ----
			addRuleButton.setText("Add Rule");
			addRuleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addRuleButtonActionPerformed(e);
				}
			});
			panel3.add(addRuleButton);

			//---- removeRuleButton ----
			removeRuleButton.setText("Remove Rule");
			removeRuleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeRuleButtonActionPerformed(e);
				}
			});
			panel3.add(removeRuleButton);
		}
		add(panel3, cc.xy(3, 5));
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel title1;
	private JPanel panel2;
	private JScrollPane scrollPane1;
	private JList list1;
	private JPanel panel1;
	private RuleEditPanel ruleEditPanel1;
	private JPanel panel3;
	private JButton addRuleButton;
	private JButton removeRuleButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public Style getStyle() {
		Rule[] rules = new Rule[model.getSize()];
		model.copyInto(rules);
		Style style = builder.createStyle();
		FeatureTypeStyle fts = builder.createFeatureTypeStyle(layer
				.getFeatureSource().getSchema().getTypeName(), rules);
		style.setFeatureTypeStyles(new FeatureTypeStyle[] { fts });
		fts.setTitle(layer.getFeatureSource().getSchema().getTypeName());
		return style;
	}
}
