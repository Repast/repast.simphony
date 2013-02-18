/*
 * Created by JFormDesigner on Mon Jul 31 13:07:10 CDT 2006
 */

package repast.simphony.gis.styleEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.xml.transform.TransformerException;

import org.geotools.map.Layer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #1
 */
public class StyleEditorPanel extends JPanel implements IStyleEditor {

	private Layer layer;

	public StyleEditorPanel(Layer layer) {
		initComponents();
		setMapLayer(layer);
	}

	public StyleEditorPanel() {
		initComponents();
	}

	public void setMapLayer(Layer layer) {
		this.layer = layer;
		ruleEditPanel1.setMapLayer(layer);
		
		FeatureTypeStyle[] fts = 
				layer.getStyle().featureTypeStyles().toArray(new FeatureTypeStyle[0]);
		
		if (fts[0].getDescription().getTitle().toString().equals("title")) {
			layer.getStyle().getFeatureTypeStyles()[0].setTitle(layer
					.getFeatureSource().getSchema().getName().getLocalPart());
		}
		if (layer.getStyle().getDescription().getTitle().toString().equals("title")) {
			layer.getStyle().getDescription().setTitle(
					layer.getFeatureSource().getSchema().getName().getLocalPart());
		}
		styleTitleField.setText(layer.getStyle().getDescription().getTitle().toString());

		rangePanel.init(layer);
		byValuePanel.init(layer);
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
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					new RowSpec[] {
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
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

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750, 800);
		frame.setLayout(new BorderLayout());
		final StyleEditorPanel pane = new StyleEditorPanel(SampleLayer
				.getSampleLayer());
		frame.add(pane, BorderLayout.CENTER);
		JButton button = new JButton("ok");
		button.addActionListener(new ActionListener() {
			SLDTransformer transformer = new SLDTransformer();

			public void actionPerformed(ActionEvent ev) {
				try {
					transformer.transform(pane.getStyle(), System.out);
				} catch (TransformerException e) {
					e.printStackTrace();
				}
			}
		});
		frame.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

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
		style.setTitle(styleTitleField.getText());
		style.getFeatureTypeStyles()[0].setTitle(styleTitleField.getText());
		return style;
	}
}
