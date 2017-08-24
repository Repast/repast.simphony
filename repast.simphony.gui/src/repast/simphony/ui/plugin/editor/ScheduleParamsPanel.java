package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import repast.simphony.engine.schedule.Frequency;
import repast.simphony.engine.schedule.ScheduleParameters;
import saf.core.ui.util.DoubleDocument;

/**
 * @author Nick Collier
 */
public class ScheduleParamsPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  protected JTextField startFld = new JTextField();
  protected JTextField intervalFld = new JTextField();
  protected JComboBox priorityFld = new JComboBox(new String[] { "Random", "First", "Last" });
  protected JTextField durationFld = new JTextField();
  protected JComboBox frequencyBox = new JComboBox(new Frequency[] { Frequency.ONE_TIME,
      Frequency.REPEAT });
  protected JCheckBox atEndBox = new JCheckBox();

  /**
   * This is the same as
   * <code>new ScheduleParamsPanel(ScheduleParameters.createOneTime(1.0))</code>
   * .
   */
  public ScheduleParamsPanel() {
    this(ScheduleParameters.createOneTime(1.0));
  }

  public ScheduleParamsPanel(ScheduleParameters params) {
    this(params, true);
  }

  public ScheduleParamsPanel(ScheduleParameters params, boolean showDuration) {
    this(params, showDuration, false);
  }

  public ScheduleParamsPanel(ScheduleParameters params, boolean showDuration, boolean showAtEnd) {
    setLayout(new BorderLayout());

    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow", "");
    // "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");

    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.border(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    // Add a titled separator to cell (1, 1) that spans 7 columns.
    builder.appendSeparator("Schedule Parameters");
    builder.nextLine();
    builder.append("Start Time:", startFld);
    builder.append("Priority:", priorityFld);
    if (showDuration) {
      builder.append("Duration:", durationFld);
    }
    builder.append("Frequency:", frequencyBox);
    builder.append("Interval:", intervalFld);

    if (showAtEnd) {
      builder.append("At End:", atEndBox);
    }

    add(builder.getPanel(), BorderLayout.CENTER);

    startFld.setDocument(new DoubleDocument());
    intervalFld.setDocument(new DoubleDocument());
    durationFld.setDocument(new DoubleDocument());
    priorityFld.setEditable(true);
    // ((JTextField)priorityFld.getEditor().getEditorComponent()).setDocument(new
    // DoubleDocument());
    setScheduleParameters(params);
  }
  
  public void setAtEnd(boolean atEnd) {
    atEndBox.setSelected(atEnd);
  }
  
  public boolean getAtEnd() {
    return atEndBox.isSelected();
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

    if (params.getInterval() > 0)
      intervalFld.setText(String.valueOf(params.getInterval()));
    if (params.getDuration() > 0)
      durationFld.setText(String.valueOf(params.getDuration()));
    frequencyBox.setSelectedItem(params.getFrequency());
    intervalFld.setEnabled(frequencyBox.getSelectedIndex() == 1);

    frequencyBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        intervalFld.setEnabled(frequencyBox.getSelectedIndex() == 1);
      }
    });
  }

  public void showError(String fieldName, String message) {
    JOptionPane.showMessageDialog(this, "Invalid " + fieldName + " value. " + message,
        "Schedule Parameters Error", JOptionPane.ERROR_MESSAGE);
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
      if (duration < 0)
        duration = ScheduleParameters.NO_DURATION;
    }
    

    Frequency frequency = (Frequency) frequencyBox.getSelectedItem();
    if (frequency.equals(Frequency.REPEAT)) {
      try {
        interval = Double.valueOf(intervalFld.getText());
      } catch (NumberFormatException ex) {
        showError("Interval", "Interval cannot be blank");
        return null;
      }

      if (interval <= 0) {
        showError("Interval", "Interval must be greater than 0.0");
        return null;
      }

      return ScheduleParameters.createRepeating(start, interval, priority, duration);
    }

    return ScheduleParameters.createOneTime(start, priority, duration);
  }
}
