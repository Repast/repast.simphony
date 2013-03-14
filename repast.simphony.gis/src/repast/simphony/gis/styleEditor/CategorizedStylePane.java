/*
 * Created by JFormDesigner on Tue May 16 15:19:31 CDT 2006
 */

package repast.simphony.gis.styleEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.brewer.color.PaletteType;

import repast.simphony.gis.display.SquareIcon;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author Tom Howe
 */
public class CategorizedStylePane extends JPanel {
	ColorBrewer brewer;

	public CategorizedStylePane() {
		brewer = new ColorBrewer();
		try {
			brewer.loadPalettes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initComponents();
	}

	public JComboBox getPaletteBox() {
		return paletteBox;
	}

	public JComboBox getPaletteTypeBox() {
		return paletteTypeBox;
	}

	private void paletteTypeSelected(ActionEvent e) {
		DefaultComboBoxModel model = (DefaultComboBoxModel) getPaletteBox()
				.getModel();
		model.removeAllElements();
		BrewerPalette[] palettes = brewer
				.getPalettes((PaletteType) getPaletteTypeBox()
						.getSelectedItem());
		for (BrewerPalette palette : palettes) {
			model.addElement(palette);
		}
	}

	public JSpinner getNumCatSpinner() {
		return numCatSpinner;
	}

	private void paletteSelected(ActionEvent e) {
		updateNumCategories();
		updateColors();
	}

	public JList getColorList() {
		return colorList;
	}

	private void numCatSpinnerStateChanged(ChangeEvent e) {
		updateColors();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Tom Howe
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		label1 = compFactory.createLabel("Palette Type");
		paletteTypeBox = new JComboBox();
				DefaultComboBoxModel model = new DefaultComboBoxModel();
				model.addElement(ColorBrewer.ALL);
				model.addElement(ColorBrewer.DIVERGING);
				model.addElement(ColorBrewer.QUALITATIVE);
				model.addElement(ColorBrewer.SEQUENTIAL);
				paletteTypeBox.setModel(model);
				paletteTypeBox.setRenderer(new DefaultListCellRenderer() {

					@Override
					public Component getListCellRendererComponent(JList list,
							Object value, int index, boolean isSelected,
							boolean cellHasFocus) {
						// TODO Auto-generated method stub
						return super.getListCellRendererComponent(list,
								((PaletteType) value).getName(), index, isSelected,
								cellHasFocus);
					}

				});
		label2 = compFactory.createLabel("Palette");
		paletteBox = new JComboBox();
		DefaultComboBoxModel paletteModel = new DefaultComboBoxModel(brewer
						.getPalettes((PaletteType) getPaletteTypeBox()
								.getSelectedItem()));
				paletteBox.setModel(paletteModel);
		paletteBox.setRenderer(new DefaultListCellRenderer() {
					@Override
					public Component getListCellRendererComponent(JList list,
							Object value, int index, boolean isSelected,
							boolean cellHasFocus) {
						// TODO Auto-generated method stub
						return super.getListCellRendererComponent(list,
								((BrewerPalette) value).getName(), index, isSelected,
								cellHasFocus);
					}
				});		
		label3 = compFactory.createLabel("Num Categories");
		numCatSpinner = new JSpinner();
		updateNumCategories();

		label4 = compFactory.createLabel("Colors");
		scrollPane1 = new JScrollPane();
		colorList = new JList(new DefaultListModel());
		colorList.setCellRenderer(new DefaultListCellRenderer() {
					public Component getListCellRendererComponent(JList list,
							Object value, int index, boolean isSelected,
							boolean cellHasFocus) {
						// TODO Auto-generated method stub
						JLabel label = (JLabel) super.getListCellRendererComponent(
								list, value, index, isSelected, cellHasFocus);
						label.setText("");
						label.setIcon(new SquareIcon((Color) value));
						return label;
					}
				});
				updateColors();

		CellConstraints cc = new CellConstraints();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC
			},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC
			}));
		add(label1, cc.xy(3, 1));

		//---- paletteTypeBox ----
		paletteTypeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paletteTypeSelected(e);
			}
		});
		add(paletteTypeBox, cc.xy(5, 1));
		add(label2, cc.xy(3, 3));

		//---- paletteBox ----
		paletteBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paletteSelected(e);
			}
		});
		add(paletteBox, cc.xy(5, 3));
		add(label3, cc.xy(3, 5));

		//---- numCatSpinner ----
		numCatSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				numCatSpinnerStateChanged(e);
			}
		});
		add(numCatSpinner, cc.xy(5, 5));
		add(label4, cc.xywh(3, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(colorList);
		}
		add(scrollPane1, cc.xy(5, 7));
		// //GEN-END:initComponents
	} // JFormDesigner - Variables declaration - DO NOT MODIFY //

	private void updateColors() {
		DefaultListModel colorModel = (DefaultListModel) getColorList()
				.getModel();
		BrewerPalette palette = ((BrewerPalette) paletteBox.getSelectedItem());
		if (palette == null) {
			return;
		}
		Color[] colors = palette.getColors(((Number) getNumCatSpinner()
				.getValue()).intValue());
		colorModel.clear();
		for (Color color : colors) {
			colorModel.addElement(color);
		}

	}

	private void updateNumCategories() {
		SpinnerNumberModel numCatModel = (SpinnerNumberModel) getNumCatSpinner()
				.getModel();
		BrewerPalette palette = ((BrewerPalette) paletteBox.getSelectedItem());
		if (palette == null) {
			return;
		}
		numCatModel.setMaximum(palette.getMaxColors());
		int minColors = ((BrewerPalette) paletteBox.getSelectedItem())
				.getMinColors();
		if (((Number) numCatModel.getValue()).intValue() < minColors) {
			numCatModel.setValue(minColors);
		}
		numCatModel.setMinimum(((BrewerPalette) paletteBox.getSelectedItem())
				.getMinColors());
		numCatModel.setStepSize(1);
	} // //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Tom Howe
	private JLabel label1;
	private JComboBox paletteTypeBox;
	private JLabel label2;
	private JComboBox paletteBox;
	private JLabel label3;
	private JSpinner numCatSpinner;
	private JLabel label4;
	private JScrollPane scrollPane1;
	private JList colorList;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setLayout(new BorderLayout());
		frame.add(new CategorizedStylePane(), BorderLayout.CENTER);
		frame.setVisible(true);
	}
}
