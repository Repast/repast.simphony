/*
 * Created by JFormDesigner on Tue May 16 10:14:45 CDT 2006
 */

package repast.simphony.gis.styleEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ExpressionBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.map.Layer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.type.FeatureType;

import repast.simphony.gis.display.SquareIcon;
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
 * @author Tom Howe
 */
public class RuleEditPanel extends JPanel implements IStyleEditor {

	StyleBuilder styleBuilder = new StyleBuilder();

	ExpressionBuilder expBuilder = new ExpressionBuilder();

	boolean point = false;

	FeatureType type;

	Rule rule;

	Symbolizer symbolizer;

	Dimension preferred = new Dimension(70, 18);

	boolean ruleOnly = false;

	public RuleEditPanel(Layer layer) {
		initComponents();
		setMapLayer(layer);
	}

	public RuleEditPanel() {
		initComponents();
	}

	public void setMapLayer(Layer layer) {
		Style style = layer.getStyle();
		DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
				CommonFactoryFinder.getStyleFactory(null), CommonFactoryFinder.getFilterFactory2(null));
		dsv.visit(style.getFeatureTypeStyles()[0].getRules()[0]);
		setRule((Rule) dsv.getCopy());
		type = layer.getFeatureSource().getSchema();
	}

	public void init(FeatureType type, Rule rule) {
		DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
				CommonFactoryFinder.getStyleFactory(null), CommonFactoryFinder.getFilterFactory2(null));
		dsv.visit(rule);
		ruleOnly = true;
		setRule((Rule)dsv.getCopy());
		this.type = type;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
		if (ruleOnly) {
			filterField.setEnabled(false);
		} else if (rule.getFilter() != null) {
			filterField.setText(rule.getFilter().toString());
		}

		symbolizer = rule.getSymbolizers()[0];
		if (PointSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			point = true;
		}
		if (PointSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			initPoint();
		}
		if (PolygonSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			initPolygon();
		}
		if (LineSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			initLine();
		}

	}

	boolean titlesVisible = true;

	boolean filterVisible = true;

	boolean fillVisible = true;

	boolean markVisible = true;

	boolean strokeVisible = true;

	public void showFilterPane(boolean show) {
		filterPanel.setVisible(show);
	}

	public void showFillPane(boolean show) {
		fillPanel.setVisible(show);
	}

	public void showMarkPane(boolean show) {
		markPanel.setVisible(show);
	}

	public void showStrokePane(boolean show) {
		strokePanel.setVisible(show);
	}

	private void initLine() {
		enableLineFields();
		preview.setShapeToLine();
		LineSymbolizer ls = (LineSymbolizer) symbolizer;
		markPanel.setVisible(false);
		fillPanel.setVisible(false);
		Stroke stroke = SLD.stroke(ls);
		Color strokeColor = SLD.color(stroke);
		getPreview().setOutlineColor(strokeColor);
		getStrokeColorButton().setIcon(new SquareIcon(strokeColor));
		int outlineThickness = (int) SLD.width(stroke);
		getOutlineThicknessSpinner().setValue(outlineThickness);
		getPreview().setOutlineThickness(outlineThickness);
		double outlineOpacity = SLD.opacity(stroke);
		getPreview().setOutlineOpacity(outlineOpacity);
		getOutlineOpacitySpinner().setValue(outlineOpacity);
	}

	public Color getFillColor() {
		return preview.getFillColor();
	}

	public float getFillTransparency() {
		return (float) preview.getFillOpacity();
	}

	public double getStrokeWidth() {
		return preview.getOutlineThickness();
	}

	public float getStrokeTransparency() {
		return (float) preview.getOutlineOpacity();
	}

	public Color getStrokeColor() {
		return preview.getOutlineColor();
	}

	public String getMark() {
		return preview.getMark();
	}

	public int getMarkSize() {
		return preview.getMarkSize();
	}

	private void enablePointFields() {
		fillPanel.setVisible(true);
		strokePanel.setVisible(true);
		markPanel.setVisible(true);
	}

	private void enablePolygonFields() {
		// titlePanel.setVisible(true);
		fillPanel.setVisible(true);
		strokePanel.setVisible(true);
		markPanel.setVisible(false);
	}

	private void enableLineFields() {
		strokePanel.setVisible(true);
	}

	private void initPoint() {
		enablePointFields();
		PointSymbolizer ps = (PointSymbolizer) symbolizer;
		Mark mark = SLD.mark(ps);
		MessageCenter.getMessageCenter(getClass()).debug("initPoint: " + mark);
		String wkn = SLD.wellKnownName(mark);
		getPreview().setMark(wkn);
		getMarkBox().setSelectedItem(wkn);
		double markSize = SLD.size(mark);
		getPreview().setMarkSize((int) markSize);
		getMarkSizeSpinner().setValue(markSize);
		Fill fill = mark.getFill();
		Color fillColor = SLD.color(fill.getColor());
		getFillColorButton().setIcon(new SquareIcon(fillColor));
		getPreview().setFillColor(fillColor);
		double fillOpacity = SLD.pointOpacity(ps);
		getFillOpacitySpinner().setValue(fillOpacity);
		getPreview().setFillOpacity(fillOpacity);
		Stroke stroke = SLD.stroke(ps);
		Color strokeColor = SLD.color(stroke);
		getPreview().setOutlineColor(strokeColor);
		getStrokeColorButton().setIcon(new SquareIcon(strokeColor));
		int outlineThickness = (int) SLD.width(stroke);
		getPreview().setOutlineThickness(outlineThickness);
		double outlineOpacity = SLD.opacity(stroke);
		getPreview().setOutlineOpacity(outlineOpacity);
		getOutlineOpacitySpinner().setValue(outlineOpacity);
	}

	private void initPolygon() {
		enablePolygonFields();
		PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
		Fill fill = SLD.fill(ps);
		Color fillColor = SLD.color(fill.getColor());
		getFillColorButton().setIcon(new SquareIcon(fillColor));
		getPreview().setFillColor(fillColor);
		double fillOpacity = SLD.polyFillOpacity(ps);
		getFillOpacitySpinner().setValue(fillOpacity);
		getPreview().setFillOpacity(fillOpacity);
		Stroke stroke = SLD.stroke(ps);
		getPreview().setOutlineColor(SLD.color(stroke.getColor()));
		getStrokeColorButton().setIcon(
				new SquareIcon(SLD.color(stroke.getColor())));
		getOutlineOpacitySpinner().setValue(SLD.polyFillOpacity(ps));
		getOutlineThicknessSpinner().setValue(
				((Number) stroke.getWidth().evaluate(null)).intValue());

	}

	private void fillColorButtonClicked(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Select a Fill Color",
				((SquareIcon) getFillColorButton().getIcon()).getColor());
		if (color != null) {
			getFillColorButton().setIcon(new SquareIcon(color));
			getPreview().setFillColor(color);
			setFill();
		}
	}

	private void setFill() {
		if (symbolizer instanceof PolygonSymbolizer) {
			PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
			ps.setFill(styleBuilder.createFill(getFillColor(),
					(Double) fillOpacitySpinner.getValue()));
		} else if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			ps.getGraphic().getMarks()[0].setFill(styleBuilder.createFill(
					getFillColor(), (Double) fillOpacitySpinner.getValue()));
		}
	}

	private void setStroke() {
		Stroke stroke = styleBuilder.createStroke(getStrokeColor(),
				getStrokeWidth(), getStrokeTransparency());
		if (symbolizer instanceof PolygonSymbolizer) {
			PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
			ps.setStroke(stroke);
		} else if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			Mark mark = ps.getGraphic().getMarks()[0];
			mark.setStroke(stroke);
		} else if (symbolizer instanceof LineSymbolizer) {
			LineSymbolizer ls = (LineSymbolizer) symbolizer;
			ls.setStroke(stroke);
		}
	}

	private void setFilter() {
		String filter = filterField.getText();
		
		if (filter == null || filter.length() < 1) {
			return;
		}
		
		try {
			rule.setFilter(CQL.toFilter(filter));
		} catch (CQLException e) {
			filterField.setForeground(Color.RED);
			JOptionPane.showConfirmDialog(this, e.getCause().getMessage());
		}
	}

	private void setMark() {
		if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			Fill fill = styleBuilder.createFill(getFillColor(),
					getFillTransparency());
			Stroke stroke = styleBuilder.createStroke(getStrokeColor(),
					getStrokeWidth(), getStrokeTransparency());
			ps.getGraphic().setMarks(
					new Mark[] { styleBuilder.createMark(getMark(), fill,
							stroke) });
			 ps.getGraphic().setSize(
					 CommonFactoryFinder.getFilterFactory(null).literal(getMarkSize())); 
		} else {
			throw new IllegalArgumentException("Cannot apply a mark to a "
					+ symbolizer.getClass().getName());
		}
	}

	private void outlineButtonClicked(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Select an Outline Color",
				((SquareIcon) getStrokeColorButton().getIcon()).getColor());
		if (color != null) {
			getStrokeColorButton().setIcon(new SquareIcon(color));
			getPreview().setOutlineColor(color);
			setStroke();
		}
	}

	private void fillOpacityChanged(ChangeEvent e) {
		getPreview()
				.setFillOpacity((Double) getFillOpacitySpinner().getValue());
		setFill();
	}

	private void outlineOpacityChanged(ChangeEvent e) {
		getPreview().setOutlineOpacity(
				(Double) getOutlineOpacitySpinner().getValue());
		setStroke();
	}

	private void markBoxChanged(ActionEvent e) {
		getPreview().setMark((String) getMarkBox().getSelectedItem());
		setMark();
	}

	private void markSizeChanged(ChangeEvent e) {
		getPreview().setMarkSize(
				((Number) getMarkSizeSpinner().getValue()).intValue());
		setMark();
	}

	private void outlineThicknessChanged(ChangeEvent e) {
		getPreview().setOutlineThickness(
				(Integer) getOutlineThicknessSpinner().getValue());
		setStroke();
	}

	public JButton getFillColorButton() {
		return fillColorButton;
	}

	public JSpinner getFillOpacitySpinner() {
		return fillOpacitySpinner;
	}

	public JButton getStrokeColorButton() {
		return strokeColorButton;
	}

	public JSpinner getOutlineOpacitySpinner() {
		return outlineOpacitySpinner;
	}

	public JSpinner getOutlineThicknessSpinner() {
		return outlineThicknessSpinner;
	}

	public JComboBox getMarkBox() {
		return markBox;
	}

	public JSpinner getMarkSizeSpinner() {
		return markSizeSpinner;
	}

	public PreviewLabel getPreview() {
		return preview;
	}

	private void filterFieldFocusLost(FocusEvent e) {
		setFilter();
	}

	private void filterFieldFocusGained(FocusEvent e) {
		filterField.setForeground(Color.BLACK);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		filterPanel = new JPanel();
		separator5 = compFactory.createSeparator("Filter");
		label10 = new JLabel();
		filterField = new JTextField();
		fillPanel = new JPanel();
		separator1 = compFactory.createSeparator("Fill");
		label1 = compFactory.createLabel("Fill Color");
		fillColorButton = new JButton();
		label3 = compFactory.createLabel("Fill Transparency");
		fillOpacitySpinner = new JSpinner();
		strokePanel = new JPanel();
		separator2 = compFactory.createSeparator("Outline");
		label2 = compFactory.createLabel("Outline Color");
		strokeColorButton = new JButton();
		label4 = compFactory.createLabel("Outline Transparency");
		outlineOpacitySpinner = new JSpinner();
		label7 = compFactory.createLabel("Outline Thickness");
		outlineThicknessSpinner = new JSpinner();
		markPanel = new JPanel();
		separator3 = compFactory.createSeparator("Mark");
		label5 = compFactory.createLabel("Mark");
		markBox = new JComboBox();
		label6 = compFactory.createLabel("Mark Size");
		markSizeSpinner = new JSpinner();
		previewPanel = new JPanel();
		separator6 = compFactory.createSeparator("Preview");
		preview = new PreviewLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(new EmptyBorder(7, 7, 7, 7));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//======== filterPanel ========
		{
			filterPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(80)),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					new RowSpec(Sizes.dluY(10))
				}));
			filterPanel.add(separator5, cc.xywh(1, 1, 5, 1));

			//---- label10 ----
			label10.setText("Filter: ");
			filterPanel.add(label10, cc.xy(3, 3));

			//---- filterField ----
			filterField.setColumns(20);
			filterField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					filterFieldFocusGained(e);
				}
				@Override
				public void focusLost(FocusEvent e) {
					filterFieldFocusLost(e);
				}
			});
			filterPanel.add(filterField, cc.xy(5, 3));
		}
		add(filterPanel);

		//======== fillPanel ========
		{
			fillPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(80)),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					new RowSpec(Sizes.dluY(10))
				}));
			fillPanel.add(separator1, cc.xywh(1, 1, 5, 1));
			fillPanel.add(label1, cc.xy(3, 3));

			//---- fillColorButton ----
			fillColorButton.setIcon(new SquareIcon(Color.RED));
			fillColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fillColorButtonClicked(e);
				}
			});
			fillColorButton.setPreferredSize(preferred);
			fillPanel.add(fillColorButton, cc.xy(5, 3));
			fillPanel.add(label3, cc.xy(3, 5));

			//---- fillOpacitySpinner ----
			fillOpacitySpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.01));
			fillOpacitySpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fillOpacityChanged(e);
				}
			});
			fillOpacitySpinner.setPreferredSize(preferred);
			fillPanel.add(fillOpacitySpinner, cc.xy(5, 5));
		}
		add(fillPanel);

		//======== strokePanel ========
		{
			strokePanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(80)),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC
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
					new RowSpec(Sizes.dluY(10))
				}));
			strokePanel.add(separator2, cc.xywh(1, 1, 6, 1));
			strokePanel.add(label2, cc.xy(3, 3));

			//---- strokeColorButton ----
			strokeColorButton.setIcon(new SquareIcon(Color.RED));
			strokeColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					outlineButtonClicked(e);
				}
			});
			strokeColorButton.setPreferredSize(preferred);
			strokePanel.add(strokeColorButton, cc.xywh(5, 3, 2, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
			strokePanel.add(label4, cc.xy(3, 5));

			//---- outlineOpacitySpinner ----
			outlineOpacitySpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.01));
			outlineOpacitySpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					outlineOpacityChanged(e);
				}
			});
			outlineOpacitySpinner.setPreferredSize(preferred);
			strokePanel.add(outlineOpacitySpinner, cc.xywh(5, 5, 2, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
			strokePanel.add(label7, cc.xy(3, 7));

			//---- outlineThicknessSpinner ----
			outlineThicknessSpinner.setModel(new SpinnerNumberModel(1, 0, null, 1));
			outlineThicknessSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					outlineThicknessChanged(e);
				}
			});
			outlineThicknessSpinner.setPreferredSize(preferred);
			strokePanel.add(outlineThicknessSpinner, cc.xywh(5, 7, 2, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		}
		add(strokePanel);

		//======== markPanel ========
		{
			markPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(80)),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.LEFT, Sizes.dluX(80), FormSpec.DEFAULT_GROW),
					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC
				},
				new RowSpec[] {
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					new RowSpec(Sizes.dluY(10))
				}));
			markPanel.add(separator3, cc.xywh(1, 1, 6, 1));
			markPanel.add(label5, cc.xy(3, 3));

			//---- markBox ----
			markBox.setModel(new DefaultComboBoxModel(new String[] {
				"circle",
				"cross",
				"star",
				"square",
				"triangle"
			}));
			markBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					markBoxChanged(e);
				}
			});
			markBox.setPreferredSize(preferred);
			markPanel.add(markBox, cc.xywh(5, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
			markPanel.add(label6, cc.xy(3, 5));

			//---- markSizeSpinner ----
			markSizeSpinner.setModel(new SpinnerNumberModel(10, 0, null, 1));
			markSizeSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					markSizeChanged(e);
				}
			});
			markSizeSpinner.setPreferredSize(preferred);
			markPanel.add(markSizeSpinner, cc.xywh(5, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		}
		add(markPanel);

		//======== previewPanel ========
		{
			previewPanel.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default, center:default:grow"),
				new RowSpec[] {
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				}));
			previewPanel.add(separator6, cc.xywh(1, 1, 2, 1));
			previewPanel.add(preview, cc.xy(2, 3));
		}
		add(previewPanel);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel filterPanel;
	private JComponent separator5;
	private JLabel label10;
	private JTextField filterField;
	private JPanel fillPanel;
	private JComponent separator1;
	private JLabel label1;
	private JButton fillColorButton;
	private JLabel label3;
	private JSpinner fillOpacitySpinner;
	private JPanel strokePanel;
	private JComponent separator2;
	private JLabel label2;
	private JButton strokeColorButton;
	private JLabel label4;
	private JSpinner outlineOpacitySpinner;
	private JLabel label7;
	private JSpinner outlineThicknessSpinner;
	private JPanel markPanel;
	private JComponent separator3;
	private JLabel label5;
	private JComboBox markBox;
	private JLabel label6;
	private JSpinner markSizeSpinner;
	private JPanel previewPanel;
	private JComponent separator6;
	private PreviewLabel preview;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public Style getStyle() {
		Style style = styleBuilder.createStyle();
		FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(type
				.getName().getLocalPart(), rule);
		rule.setTitle(" ");
		style.setFeatureTypeStyles(new FeatureTypeStyle[] { fts });
		return style;
	}

	public Rule getRule() {
		return rule;
	}

}
