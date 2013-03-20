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
	
	public void setData(FeatureType featureType, Style style, FeatureSource source) {
		ruleEditPanel1.setData(featureType,style);
		
		FeatureTypeStyle fts = style.featureTypeStyles().get(0);
		
		if (fts.getDescription().getTitle() == null) {
			fts.getDescription().setTitle(featureType.getName().getLocalPart());
		}
		if (style.getDescription().getTitle() == null) {
			style.getDescription().setTitle(featureType.getName().getLocalPart());
		}
		
		styleTitleField.setText(style.getDescription().getTitle().toString());

		rangePanel.init(featureType, style, ruleEditPanel1.getPreview());
		byValuePanel.init(featureType, style, source, ruleEditPanel1.getPreview());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		styleTitleField = new JTextField();
		tb = new JTabbedPane();
		ruleEditPanel1 = new RuleEditPanel();
		byValuePanel = new ByValuePanel();
		rangePanel = new ByRangePanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
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

				//---- label1 ----
				label1.setText("Layer Title");
				contentPanel.add(label1, cc.xy(1, 1));
				contentPanel.add(styleTitleField, cc.xy(3, 1));

				//======== tb ========
				{

					//---- ruleEditPanel1 ----
					ruleEditPanel1.setBorder(new EmptyBorder(5, 5, 5, 5));
					ruleEditPanel1.showFilterPane(false);
					tb.addTab("Simple Style", ruleEditPanel1);


					//---- byValuePanel ----
					byValuePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
					tb.addTab("Value Style", byValuePanel);


					//---- rangePanel ----
					rangePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
					tb.addTab("Range Style", rangePanel);

				}
				contentPanel.add(tb, cc.xywh(1, 3, 3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
		}
		add(dialogPane, BorderLayout.CENTER);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextField styleTitleField;
	private JTabbedPane tb;
	private RuleEditPanel ruleEditPanel1;
	private ByValuePanel byValuePanel;
	private ByRangePanel rangePanel;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public Style getStyle() {
		Style style = null;
		IStyleEditor editor = (IStyleEditor) tb.getSelectedComponent();
		style = editor.getStyle();
		/*
		if (tb.indexOfTab("Simple Style") == tb.getSelectedIndex()) {
			style = ruleEditPanel1.getStyle();
		} else if (tb.indexOfTab("Range Based Style") == tb.getSelectedIndex()) {
			style = rangePanel.getStyle();
		} else if (tb.indexOfTab("Value Based Style") == tb.getSelectedIndex()) {
			style = byValuePanel.getStyle();
		}
		*/
		style.getDescription().setTitle(styleTitleField.getText());
		style.featureTypeStyles().get(0).getDescription().setTitle(styleTitleField.getText());
		return style;
	}
}
