package repast.simphony.gis.styleEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ExpressionBuilder;
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

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * Editor panel for single rule GIS style that lets the user specify the style
 * shape, fill, line color, etc.
 * 
 * @author Tom Howe
 * @author Eric Tatara
 */
public class RuleEditPanel extends JPanel implements IStyleEditor {

	protected StyleBuilder styleBuilder = new StyleBuilder();
	protected ExpressionBuilder expBuilder = new ExpressionBuilder();
	protected FeatureType type;
	protected Rule rule;
	protected Symbolizer symbolizer;
	protected Dimension preferred = new Dimension(70, 18);
	protected boolean titlesVisible = true;
	protected boolean filterVisible = true;
	protected boolean fillVisible = true;
	protected boolean markVisible = true;
	protected boolean strokeVisible = true;

	public RuleEditPanel() {
		initComponents();
	}

	public void init(FeatureType type, Rule rule) {
		DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
				CommonFactoryFinder.getStyleFactory(null), CommonFactoryFinder.getFilterFactory2(null));
		dsv.visit(rule);
		setRule((Rule)dsv.getCopy());
		this.type = type;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
		
		symbolizer = rule.getSymbolizers()[0];
		if (symbolizer instanceof PointSymbolizer) {
			initPoint();
		}
		else if (symbolizer instanceof PolygonSymbolizer) {
			initPolygon();
		}
		else if (symbolizer instanceof LineSymbolizer) {
			initLine();
		}
	}

	/**
	 * Setup the editor for a Point feature
	 */
	private void initPoint() {
		enablePointFields();
		PointSymbolizer ps = (PointSymbolizer) symbolizer;
		Mark mark = SLD.mark(ps);
		String wkn = SLD.wellKnownName(mark);
		int markSize = SLD.pointSize(ps);
	  
		int markRotation = ps.getGraphic().getRotation().evaluate(null, Integer.class);
		
		markBox.setSelectedItem(wkn);
		markSizeSpinner.setValue(markSize);
		markRotationSpinner.setValue(markRotation);
				
		Fill fill = mark.getFill();
		Color fillColor = SLD.color(fill.getColor());
		fillColorButton.setIcon(new SquareIcon(fillColor));
		
		double fillOpacity = SLD.opacity(fill);
		fillOpacitySpinner.setValue(fillOpacity);
		
		Stroke stroke = mark.getStroke();
		Color strokeColor = SLD.color(stroke);
		strokeColorButton.setIcon(new SquareIcon(strokeColor));
		
		int outlineThickness = (int) SLD.width(stroke);
		outlineThicknessSpinner.setValue(outlineThickness);
		
		double outlineOpacity = SLD.opacity(stroke);
		outlineOpacitySpinner.setValue(outlineOpacity);
		
		setMark();
	}
	
	/**
	 * Setup the editor for a Line feature
	 */
	private void initLine() {
		enableLineFields();
		LineSymbolizer ls = (LineSymbolizer) symbolizer;
		markPanel.setVisible(false);
		fillPanel.setVisible(false);
		
		Stroke stroke = SLD.stroke(ls);
		Color strokeColor = SLD.color(stroke);
		strokeColorButton.setIcon(new SquareIcon(strokeColor));
		
		int outlineThickness = (int) SLD.width(stroke);
		outlineThicknessSpinner.setValue(outlineThickness);
		
		double outlineOpacity = SLD.opacity(stroke);
		outlineOpacitySpinner.setValue(outlineOpacity);
		
		setStroke();
	}
	
	/**
	 * Setup the editor for a Polygon feature
	 */
	private void initPolygon() {
		enablePolygonFields();
		PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
		
		Fill fill = SLD.fill(ps);
		Color fillColor = SLD.color(fill.getColor());
		fillColorButton.setIcon(new SquareIcon(fillColor));
		
		double fillOpacity = SLD.polyFillOpacity(ps);
		fillOpacitySpinner.setValue(fillOpacity);
		
		Stroke stroke = SLD.stroke(ps);
		Color strokeColor = SLD.color(stroke);
		strokeColorButton.setIcon(new SquareIcon(strokeColor));
		
		double outlineOpacity = SLD.opacity(stroke);
		outlineOpacitySpinner.setValue(outlineOpacity);
		
		int outlineThickness = (int) SLD.width(stroke);
		outlineThicknessSpinner.setValue(outlineThickness);
		
		setFill();
		setStroke();
	}

	private void enablePointFields() {
		fillPanel.setVisible(true);
		strokePanel.setVisible(true);
		markPanel.setVisible(true);
	}

	private void enablePolygonFields() {
		fillPanel.setVisible(true);
		strokePanel.setVisible(true);
		markPanel.setVisible(false);
	}

	private void enableLineFields() {
		strokePanel.setVisible(true);
	}

	/**
	 * Sets the fill color and opacity for point and polygon features
	 */
	private void setFill() {
		if (symbolizer instanceof PolygonSymbolizer) {
			PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
			ps.setFill(styleBuilder.createFill(
					((SquareIcon) fillColorButton.getIcon()).getColor(),
					(Double) fillOpacitySpinner.getValue()));
		} 
		else if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			Mark mark = (Mark)ps.getGraphic().graphicalSymbols().get(0);
			
			mark.setFill(styleBuilder.createFill(
					((SquareIcon) fillColorButton.getIcon()).getColor(), 
					(Double) fillOpacitySpinner.getValue()));
		}
		updatePreview();
	}

	/**
	 * Set the stroke color, thickness, and opacity for all feature types.
	 */
	private void setStroke() {
		Stroke stroke = styleBuilder.createStroke(
				((SquareIcon) strokeColorButton.getIcon()).getColor(),	
				(Integer) outlineThicknessSpinner.getValue(), 
				(Double) outlineOpacitySpinner.getValue());
		
		if (symbolizer instanceof PolygonSymbolizer) {
			PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
			ps.setStroke(stroke);
		} 
		else if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			Mark mark = (Mark)ps.getGraphic().graphicalSymbols().get(0);
			mark.setStroke(stroke);
		} 
		else if (symbolizer instanceof LineSymbolizer) {
			LineSymbolizer ls = (LineSymbolizer) symbolizer;
			ls.setStroke(stroke);
		}
		updatePreview();
	}

	/**
	 * Sets the Mark type for Point features including size, fill, and stroke.
	 */
	private void setMark() {
		if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer ps = (PointSymbolizer) symbolizer;
			
			Mark newMark = styleBuilder.createMark((String) markBox.getSelectedItem());
			ps.getGraphic().graphicalSymbols().clear();
			ps.getGraphic().graphicalSymbols().add(newMark);
				
			setStroke();
			setFill();
		} else {
			throw new IllegalArgumentException("Cannot apply a mark to a "
					+ symbolizer.getClass().getName());
		}
		updatePreview();
	}
	
	private void setMarkSize(){
		PointSymbolizer ps = (PointSymbolizer) symbolizer;
		
		ps.getGraphic().setSize(CommonFactoryFinder.getFilterFactory().literal(
				((Number) markSizeSpinner.getValue()).intValue()));
		
		updatePreview();
	}
	
	private void setMarkRotation(){
		PointSymbolizer ps = (PointSymbolizer) symbolizer;
		
		ps.getGraphic().setRotation(CommonFactoryFinder.getFilterFactory().literal(
				((Number) markRotationSpinner.getValue()).intValue()));
		
		updatePreview();
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
	
	private void fillColorButtonClicked(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Select a Fill Color",
				((SquareIcon) fillColorButton.getIcon()).getColor());
		if (color != null) {
			fillColorButton.setIcon(new SquareIcon(color));
			setFill();
		}
	}
	
	private void outlineColorButtonClicked(ActionEvent e) {
		Color color = JColorChooser.showDialog(this, "Select an Outline Color",
				((SquareIcon) strokeColorButton.getIcon()).getColor());
		if (color != null) {
			strokeColorButton.setIcon(new SquareIcon(color));
			setStroke();
		}
	}

	private void fillOpacityChanged(ChangeEvent e) {
		setFill();
	}

	private void outlineOpacityChanged(ChangeEvent e) {
		setStroke();
	}

	private void markBoxChanged(ActionEvent e) {
		setMark();
	}

	private void markSizeChanged(ChangeEvent e) {
		setMarkSize();
	}
	private void markRotationChanged(ChangeEvent e) {
		setMarkRotation();
	}

	private void outlineThicknessChanged(ChangeEvent e) {
		setStroke();
	}

	private void initComponents() {
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		
		fillPanel = new JPanel();
		separator1 = compFactory.createSeparator("Fill");
		fillColorLabel = compFactory.createLabel("Fill Color");
		fillColorButton = new JButton();
		fillOpacityLabel = compFactory.createLabel("Fill Transparency");
		fillOpacitySpinner = new JSpinner();
		strokePanel = new JPanel();
		outlineSeparator = compFactory.createSeparator("Outline");
		strokeColorLabel = compFactory.createLabel("Outline Color");
		strokeColorButton = new JButton();
		strokeOpacityLabel = compFactory.createLabel("Outline Transparency");
		outlineOpacitySpinner = new JSpinner();
		strokeThicknessLabel = compFactory.createLabel("Outline Thickness");
		outlineThicknessSpinner = new JSpinner();
		markPanel = new JPanel();
		markSeparator = compFactory.createSeparator("Mark");
		markLabel = compFactory.createLabel("Mark");
		markBox = new JComboBox();
		markSizeLabel = compFactory.createLabel("Mark Size");
		markSizeSpinner = new JSpinner();
		markRotationLabel = compFactory.createLabel("Mark Rotation");
		markRotationSpinner = new JSpinner();
		previewPanel = new JPanel();
		previewSeparator = compFactory.createSeparator("Preview");
		previewLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		setBorder(new EmptyBorder(7, 7, 7, 7));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
		fillPanel.add(fillColorLabel, cc.xy(3, 3));

		fillColorButton.setIcon(new SquareIcon(Color.RED));
		fillColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillColorButtonClicked(e);
			}
		});
		fillColorButton.setPreferredSize(preferred);
		fillPanel.add(fillColorButton, cc.xy(5, 3));
		fillPanel.add(fillOpacityLabel, cc.xy(3, 5));

		fillOpacitySpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.01));
		fillOpacitySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fillOpacityChanged(e);
			}
		});
		fillOpacitySpinner.setPreferredSize(preferred);
		fillPanel.add(fillOpacitySpinner, cc.xy(5, 5));

		add(fillPanel);

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
		strokePanel.add(outlineSeparator, cc.xywh(1, 1, 6, 1));
		strokePanel.add(strokeColorLabel, cc.xy(3, 3));

		strokeColorButton.setIcon(new SquareIcon(Color.RED));
		strokeColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outlineColorButtonClicked(e);
			}
		});
		strokeColorButton.setPreferredSize(preferred);
		strokePanel.add(strokeColorButton, cc.xywh(5, 3, 2, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		strokePanel.add(strokeOpacityLabel, cc.xy(3, 5));

		outlineOpacitySpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.01));
		outlineOpacitySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				outlineOpacityChanged(e);
			}
		});
		outlineOpacitySpinner.setPreferredSize(preferred);
		strokePanel.add(outlineOpacitySpinner, cc.xywh(5, 5, 2, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		strokePanel.add(strokeThicknessLabel, cc.xy(3, 7));

		outlineThicknessSpinner.setModel(new SpinnerNumberModel(1, 0, null, 1));
		outlineThicknessSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				outlineThicknessChanged(e);
			}
		});
		outlineThicknessSpinner.setPreferredSize(preferred);
		strokePanel.add(outlineThicknessSpinner, cc.xywh(5, 7, 2, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		add(strokePanel);

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
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(10))
				}));
		markPanel.add(markSeparator, cc.xywh(1, 1, 6, 1));
		markPanel.add(markLabel, cc.xy(3, 3));

		markBox.setModel(new DefaultComboBoxModel(SimpleMarkFactory.getWKT_List()));
		markBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				markBoxChanged(e);
			}
		});
		markBox.setPreferredSize(preferred);
		markPanel.add(markBox, cc.xywh(5, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		
		markPanel.add(markSizeLabel, cc.xy(3, 5));
		markSizeSpinner.setModel(new SpinnerNumberModel(10, 0, null, 1));
		markSizeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				markSizeChanged(e);
			}
		});
		markSizeSpinner.setPreferredSize(preferred);
		markPanel.add(markSizeSpinner, cc.xywh(5, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		markPanel.add(markRotationLabel, cc.xy(3, 7));
		markRotationSpinner.setModel(new SpinnerNumberModel(0, null, null, 1));
		markRotationSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				markRotationChanged(e);
			}
		});
		markRotationSpinner.setPreferredSize(preferred);
		markPanel.add(markRotationSpinner, cc.xywh(5, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		
		add(markPanel);

		previewPanel.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default, center:default:grow"),
				new RowSpec[] {
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.LINE_GAP_ROWSPEC,
					new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				}));
		previewPanel.add(previewSeparator, cc.xywh(1, 1, 2, 1));
		previewPanel.add(previewLabel, cc.xy(2, 3));

		add(previewPanel);
	}

	
	private JPanel fillPanel;
	private JComponent separator1;
	private JLabel fillColorLabel;
	private JButton fillColorButton;
	private JLabel fillOpacityLabel;
	private JSpinner fillOpacitySpinner;
	private JPanel strokePanel;
	private JComponent outlineSeparator;
	private JLabel strokeColorLabel;
	private JButton strokeColorButton;
	private JLabel strokeOpacityLabel;
	private JSpinner outlineOpacitySpinner;
	private JLabel strokeThicknessLabel;
	private JSpinner outlineThicknessSpinner;
	private JPanel markPanel;
	private JComponent markSeparator;
	private JLabel markLabel;
	private JComboBox markBox;
	private JLabel markSizeLabel;
	private JSpinner markSizeSpinner;
	private JLabel markRotationLabel;
	private JSpinner markRotationSpinner;
	private JPanel previewPanel;
	private JComponent previewSeparator;
	private JLabel previewLabel;

	public Style getStyle() {
		Style style = styleBuilder.createStyle();
		FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(type
				.getName().getLocalPart(), rule);
		rule.setTitle(" ");
		style.featureTypeStyles().clear();
		List<FeatureTypeStyle> ftsList = new ArrayList<FeatureTypeStyle>();
	  ftsList.add(fts);
		style.featureTypeStyles().addAll(ftsList);
		return style;
	}

	public Rule getRule() {
		return rule;
	}
	
	public void updatePreview(){
		previewLabel.setIcon(StylePreviewFactory.createIcon(rule));
	}
}