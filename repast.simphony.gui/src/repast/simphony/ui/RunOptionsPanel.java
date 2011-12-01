/*
 * Created by JFormDesigner on Wed Aug 27 14:10:37 CDT 2008
 */

package repast.simphony.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import repast.simphony.engine.environment.RunEnvironment;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #5
 */
public class RunOptionsPanel extends JPanel {
	public RunOptionsPanel() {
		initComponents();
	}

	public void init(RunOptionsModel optionsModel) {
		PresentationModel pModel = new PresentationModel(optionsModel);
		ValueModel model = pModel.getModel("pauseAt");
		Bindings.bind(pauseAtFld, model);

		model = pModel.getModel("stopAt");
		Bindings.bind(stopAtFld, model);

		model = pModel.getModel("sparklineLength");
		Bindings.bind(sparklineLengthFld, model);
		sparklineLengthFld
				.setText("" + RunEnvironment.DEFAULT_SPARKLINE_LENGTH);

		model = pModel.getModel("sparklineType");
		Bindings.bind(sparklineTypeFld, model);
		sparklineTypeFld.setSelected(RunEnvironment.DEFAULT_SPARKLINE_TYPE);

		pauseAtFld.addFocusListener(tempFocusCommitter);
		stopAtFld.addFocusListener(tempFocusCommitter);
		sparklineLengthFld.addFocusListener(tempFocusCommitter);
		
		model = pModel.getModel("scheduleTickDelay");
		Bindings.addComponentPropertyHandler(slider1, model);
    slider1.setModel(new BoundedRangeAdapter(model, 0, 0, 100));
	}


	// we need this because moving focus from a one of the parameter
	// or probed fields to a menu item does not normally cause the
	// the field to commit an edit. If the menu item works with the
	// field the menu item will then be working with the old field
	// value not the new one
	private FocusAdapter tempFocusCommitter = new FocusAdapter() {

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource() instanceof JFormattedTextField && e.isTemporary()) {
				JFormattedTextField source = ((JFormattedTextField) e
						.getSource());
				try {
					source.commitEdit();
					source.setValue(source.getValue());
				} catch (ParseException e1) {
					source.setValue(source.getValue());
				}

			}
		}
	};

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		separator1 = compFactory.createSeparator("Schedule Options");
		label1 = new JLabel();
		pauseAtFld = new JFormattedTextField();
		label2 = new JLabel();
		stopAtFld = new JFormattedTextField();
		separator3 = compFactory.createSeparator("Schedule Tick Delay");
		slider1 = new JSlider();
		separator2 = compFactory.createSeparator("Sparkline Options");
		label3 = new JLabel();
		sparklineLengthFld = new JFormattedTextField();
		sparklineTypeFld = new JCheckBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));
		add(separator1, cc.xywh(1, 1, 3, 1));

		//---- label1 ----
		label1.setText("Pause At:");
		add(label1, cc.xy(1, 3));
		add(pauseAtFld, cc.xy(3, 3));

		//---- label2 ----
		label2.setText("Stop At:");
		add(label2, cc.xy(1, 5));
		add(stopAtFld, cc.xy(3, 5));
		add(separator3, cc.xywh(1, 9, 3, 1));

		//---- slider1 ----
		slider1.setMaximum(100);
		slider1.setPaintTicks(true);
		slider1.setValue(0);
		slider1.setPaintLabels(true);
		slider1.setMajorTickSpacing(10);
		slider1.setMinorTickSpacing(5);
		slider1.setSnapToTicks(false);
		add(slider1, cc.xywh(1, 11, 3, 1));
		add(separator2, cc.xywh(1, 15, 3, 1));

		//---- label3 ----
		label3.setText("Sparkline Points:");
		add(label3, cc.xy(1, 17));
		add(sparklineLengthFld, cc.xy(3, 17));

		//---- sparklineTypeFld ----
		sparklineTypeFld.setText("Sparklines are Drawn as Line Graphs");
		add(sparklineTypeFld, cc.xywh(1, 19, 3, 1));
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComponent separator1;
	private JLabel label1;
	private JFormattedTextField pauseAtFld;
	private JLabel label2;
	private JFormattedTextField stopAtFld;
	private JComponent separator3;
	private JSlider slider1;
	private JComponent separator2;
	private JLabel label3;
	private JFormattedTextField sparklineLengthFld;
	private JCheckBox sparklineTypeFld;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
