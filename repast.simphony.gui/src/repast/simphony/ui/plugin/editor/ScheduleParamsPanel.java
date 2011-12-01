package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import repast.simphony.engine.schedule.Frequency;
import repast.simphony.engine.schedule.ScheduleParameters;
import saf.core.ui.util.DoubleDocument;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class ScheduleParamsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected JTextField startFld = new JTextField();
	protected JTextField intervalFld = new JTextField();
	protected JComboBox priorityFld = new JComboBox(new String[]{"Random", "First", "Last"});
	protected JTextField durationFld = new JTextField();
	protected JComboBox frequencyBox = new JComboBox(new Frequency[]{Frequency.ONE_TIME, Frequency.REPEAT});
	
	/**
	 * This is the same as <code>new ScheduleParamsPanel(ScheduleParameters.createOneTime(1.0))</code>.
	 */
	public ScheduleParamsPanel() {
		this(ScheduleParameters.createOneTime(1.0));
	}
	
	public ScheduleParamsPanel(ScheduleParameters params) {
		this(params, true);
	}

	public ScheduleParamsPanel(ScheduleParameters params, boolean showDuration) {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow",
						"pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		CellConstraints cc = new CellConstraints();

		// Add a titled separator to cell (1, 1) that spans 7 columns.
		builder.addSeparator("Schedule Parameters", cc.xyw(1, 1, 3));
		builder.addLabel("Start Time:", cc.xy(1, 3));
		builder.add(startFld, cc.xy(3, 3));
		builder.addLabel("Priority:", cc.xy(1, 5));
		builder.add(priorityFld, cc.xy(3, 5));
		if (showDuration) {
			builder.addLabel("Duration:", cc.xy(1, 7));
			builder.add(durationFld, cc.xy(3, 7));
		}
		builder.addLabel("Frequency:", cc.xy(1, 9));
		builder.add(frequencyBox, cc.xy(3, 9));
		builder.addLabel("Interval:", cc.xy(1, 11));
		builder.add(intervalFld, cc.xy(3, 11));

		add(builder.getPanel(), BorderLayout.CENTER);

		startFld.setDocument(new DoubleDocument());
		intervalFld.setDocument(new DoubleDocument());
		durationFld.setDocument(new DoubleDocument());
		priorityFld.setEditable(true);
		//((JTextField)priorityFld.getEditor().getEditorComponent()).setDocument(new DoubleDocument());
		setScheduleParameters(params);
	}

	public void setScheduleParameters(ScheduleParameters params) {

		startFld.setText(String.valueOf(params.getStart()));
		if (ScheduleParameters.isRandomPriority(params)) {
			priorityFld.setSelectedItem("Random");
		} else if (params.getPriority() == ScheduleParameters.FIRST_PRIORITY) {
			priorityFld.setSelectedItem("First");
		} else if (params.getPriority() == ScheduleParameters.LAST_PRIORITY) {
			priorityFld.setSelectedItem("Last");
		} else {
			priorityFld.getEditor().setItem(String.valueOf(params.getPriority()));
		}

		if (params.getInterval() > 0) intervalFld.setText(String.valueOf(params.getInterval()));
		if (params.getDuration() > 0) durationFld.setText(String.valueOf(params.getDuration()));
		frequencyBox.setSelectedItem(params.getFrequency());
		intervalFld.setEnabled(frequencyBox.getSelectedIndex() == 1);

		frequencyBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				intervalFld.setEnabled(frequencyBox.getSelectedIndex() == 1);
			}
		});
	}

	public void showError(String fieldName, String message) {
		JOptionPane.showMessageDialog(this, "Invalid " + fieldName + " value. " + message, "Schedule Parameters Error",
						JOptionPane.ERROR_MESSAGE);
	}

	public ScheduleParameters createScheduleParameters() {
		double start, interval, priority, duration;
		try {
			start = Double.valueOf(startFld.getText());
		} catch (NumberFormatException ex) {
			showError("Start Time", "Start time cannot be blank");
			return null;
		}

		if (start < 0) {
			showError("Start Time", "Start time must be greater than 0");
			return null;
		}


		String priorityVal = priorityFld.getSelectedItem().toString();
		if (priorityVal == null || priorityVal.length() == 0) {
			priority = ScheduleParameters.RANDOM_PRIORITY;
		} else {
			if (priorityVal.equals("First")) {
				priority = Double.POSITIVE_INFINITY;
			} else if (priorityVal.equals("Last")) {
				priority = Double.NEGATIVE_INFINITY;
			} else if (priorityVal.equals("Random")) {
				priority = ScheduleParameters.RANDOM_PRIORITY;
			} else {
				try {
					priority = Double.valueOf(priorityVal);
				} catch (NumberFormatException ex) {
					showError("Priority", "Priority must be a number, 'First', 'Last', or 'Random'");
					return null;
				}
			}
		}


		if (durationFld.getText() == null || durationFld.getText().length() == 0) {
			duration = ScheduleParameters.NO_DURATION;
		} else {
			duration = Double.valueOf(durationFld.getText());
			if (duration < 0) duration = ScheduleParameters.NO_DURATION;
		}

		Frequency frequency = (Frequency) frequencyBox.getSelectedItem();
		if (frequency.equals(Frequency.REPEAT)) {
			try {
				interval = Double.valueOf(intervalFld.getText());
			} catch (NumberFormatException ex) {
				showError("Interval", "Interval cannot be blank");
				return null;
			}

			if (interval < 0) {
				showError("Interval", "Interval must be greater than 0");
				return null;
			}

			return ScheduleParameters.createRepeating(start, interval, priority, duration);
		}

		return ScheduleParameters.createOneTime(start, priority, duration);
	}
}
