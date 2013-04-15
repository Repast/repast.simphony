package repast.simphony.gis.styleEditor;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.type.FeatureType;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * Panel that holds the various sub-panel editors (Basic, Rule, Value, etc.). 
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class StyleEditorPanel extends JPanel implements IStyleEditor {

	static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

	public StyleEditorPanel() {
		initComponents();
	}
	
	// TODO Geotools [blocker] - need to reinitialize the range and value panels
	//  when the style/rule is edited in the basic ruleEditPanel.
	
	public void setData(FeatureType featureType, Style style, FeatureSource source) {
		Rule rule = style.featureTypeStyles().get(0).rules().get(0);
		ruleEditPanel.init(featureType, rule);
		
		FeatureTypeStyle fts = style.featureTypeStyles().get(0);
		
		if (fts.getDescription().getTitle() == null) {
			fts.getDescription().setTitle(featureType.getName().getLocalPart());
		}
		if (style.getDescription().getTitle() == null) {
			style.getDescription().setTitle(featureType.getName().getLocalPart());
		}
		
		styleTitleField.setText(style.getDescription().getTitle().toString());

		rangePanel.init(featureType, style);
		byValuePanel.init(featureType, style, source);
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		layerLabel = new JLabel();
		styleTitleField = new JTextField();
		tb = new JTabbedPane();
		ruleEditPanel = new RuleEditPanel();
		byValuePanel = new ByValuePanel();
		rangePanel = new ByRangePanel();
		CellConstraints cc = new CellConstraints();

		setLayout(new BorderLayout());


		dialogPane.setBorder(Borders.DIALOG);
		dialogPane.setLayout(new BorderLayout());

		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
						FormSpecs.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				}));


		layerLabel.setText("Layer Title");
		contentPanel.add(layerLabel, cc.xy(1, 1));
		contentPanel.add(styleTitleField, cc.xy(3, 1));

		ruleEditPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		tb.addTab("Simple Style", ruleEditPanel);

		byValuePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		tb.addTab("Value Style", byValuePanel);

		rangePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		tb.addTab("Range Style", rangePanel);

		contentPanel.add(tb, cc.xywh(1, 3, 3, 1));

		dialogPane.add(contentPanel, BorderLayout.CENTER);

		add(dialogPane, BorderLayout.CENTER);
	}

	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel layerLabel;
	private JTextField styleTitleField;
	private JTabbedPane tb;
	private RuleEditPanel ruleEditPanel;
	private ByValuePanel byValuePanel;
	private ByRangePanel rangePanel;

	public Style getStyle() {
		Style style = null;
		IStyleEditor editor = (IStyleEditor) tb.getSelectedComponent();
		style = editor.getStyle();

		style.getDescription().setTitle(styleTitleField.getText());
		style.featureTypeStyles().get(0).getDescription().setTitle(styleTitleField.getText());
		return style;
	}
}
