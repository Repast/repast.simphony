package repast.simphony.ui.sparkline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.measure.Quantity;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.ui.probe.NumericProbedProperty;

import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueModel;
import com.representqueens.spark.BarGraph;

/*
 * @author Michael J. North
 *
 */
@SuppressWarnings("serial")
public class PropertySourcedSparklineJComponent extends SparklineJComponent {

  public AbstractValueModel model;
  public NumericProbedProperty property;
  public PropertyChangeListener listener = null;

  public PropertySourcedSparklineJComponent() {
    super();
  }

  public PropertySourcedSparklineJComponent(AbstractValueModel newModel,
      NumericProbedProperty newProperty) {

    super();

    if (RunEnvironment.getInstance() != null) {
      int tempLength = RunEnvironment.getInstance().getSparklineLength();
      if (tempLength != this.data.length) {
        this.data = new Double[tempLength];
        for (int i = 0; i < this.data.length; i++) {
          this.data[i] = 0.0;
        }
        this.makeNewGraph();
      }
    }

    this.property = newProperty;
    this.setModel(newModel);

  }

  public ValueModel getModel() {
    return model;
  }

  public void setModel(AbstractValueModel newModel) {

    if ((this.model != null) && (this.listener != null)) {
      this.model.removePropertyChangeListener(this.listener);
    }

    this.model = newModel;
    this.listener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        addData(model.getValue());
      }
    };
    this.model.addPropertyChangeListener(this.listener);

  }

  public NumericProbedProperty getProperty() {
    return property;
  }

  public void setProperty(NumericProbedProperty property) {
    this.property = property;
  }

  public void makeNewGraph() {

    if ((RunEnvironment.getInstance() == null) || (RunEnvironment.getInstance().getSparklineType())) {
      this.setLineGraph(true);
    } else {
      this.image = BarGraph.createGraph(this.data, this.params, this.foregroundColor,
          this.highlightColor, this.lastColor);
      this.setLineGraph(false);
    }
    super.makeNewGraph();

  }

  public void setData(Object newValue) {
    addData(newValue);
  }

  public void addData(Object newValue) {

    if ((newValue != null) && ((newValue instanceof Number) || (newValue instanceof Quantity))) {

      if (RunEnvironment.getInstance() != null) {
        int tempLength = RunEnvironment.getInstance().getSparklineLength();

        if (data.length != tempLength) {

          if (data.length < tempLength) {
            tempLength = data.length + 1;
          }

          Number[] newData = new Number[tempLength];
          for (int i = 0; i < Math.max(data.length, newData.length); i++) {
            if ((i < data.length) && (i < newData.length)) {
              newData[i] = data[i];
            } else if (i < newData.length) {
              newData[i] = 0.0;
            }
          }
          data = newData;

        }

        super.addData(newValue);

      }

    }

  }

}
