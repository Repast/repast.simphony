package repast.simphony.ui.probe;

import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

import simphony.util.ThreadUtilities;

import com.jgoodies.binding.PresentationModel;

/**
 * Encapsulates the gui representation and the model of a gui probe.S
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class Probe {

  // 60 times per second
  private static long UPDATE_INTERVAL = 17;

  private JPanel panel;
  // TODO change to list of ProbeModels when parameter
  // code is updated
  private List<? extends PresentationModel<?>> models;
  private boolean buffered = false;
  private Updater updater;

  private long lastUpdateTS = 0;
  private String title;

  private class Updater implements Runnable {
    public void run() {
      for (PresentationModel<?> model : models) {
        ((ProbeModel)model).update();
      }
    }
  }

  public Probe(List<? extends PresentationModel<?>> models, JPanel panel, String title) {
    this(models, panel, title, false);
  }

  public Probe(List<? extends PresentationModel<?>> models, JPanel panel, String title, boolean buffered) {
    this.panel = panel;
    this.models = models;
    this.buffered = buffered;
    this.title = title;
    updater = new Updater();
  }
  
  /**
   * Gets the title of this Probe.
   * 
   * @return the title of this Probe.
   */
  public String getTitle() {
    return title;
  }

  /**
   * A probe is buffered if changes to its values are not automatically
   * reflected in the probed object. Calling commit on a buffered probe will
   * commit the changes to the probed object.
   * 
   * @return true if this Probe is buffered, false otherwise.
   */
  public boolean isBuffered() {
    return buffered;
  }

  /**
   * Updates the probe to show the latest values of the probed property.
   */
  public void update() {
    // only update every 60th of a second so we don't flood
    // the event queue
    long ts = System.currentTimeMillis();
    if (ts - lastUpdateTS > UPDATE_INTERVAL) {
      ThreadUtilities.runInEventThread(updater);
      lastUpdateTS = ts;
    }
  }

  /**
   * Flush any pending changes from the bean. Note that is only has an effect if
   * this is a buffered probe.
   */
  public void flush() {
    for (PresentationModel<?> model : models) {
      model.triggerFlush();
    }
  }

  /**
   * Commit any pending changes to the bean. Note that is only has an effect if
   * this is a buffered probe.
   */
  public void commit() {
    for (PresentationModel<?> model : models) {
      model.triggerCommit();
    }
  }

  /**
   * Gets the panel that displays the gui widgets for this probe.
   * 
   * @return the panel that displays the gui widgets for this probe.
   */
  public JPanel getPanel() {
    return panel;
  }

  /**
   * Adds a property change listener to this Probe. This listener will be called
   * when the probed object is updated.
   * 
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    for (PresentationModel<?> model : models) {
      ((ProbeModel)model).addPropertyChangeListener(listener);
    }
  }

  /**
   * Removes a property change listener from this Probe.
   * 
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    for (PresentationModel<?> model : models) {
      ((ProbeModel)model).removePropertyChangeListener(listener);
    }
  }
}
