package repast.simphony.ui.sparkline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jscience.physics.amount.Amount;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.ui.probe.NumericProbedProperty;

import com.jgoodies.binding.PresentationModel;
import com.representqueens.spark.BarGraph;

/*
 * @author Michael J. North
 *
 */
public class PropertySourcedSparklineJComponent extends SparklineJComponent {

  public PresentationModel model;
  public NumericProbedProperty property;
  public PropertyChangeListener listener = null;

  public PropertySourcedSparklineJComponent() {
    super();
  }

  public PropertySourcedSparklineJComponent(PresentationModel newModel,
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

  public PresentationModel getModel() {
    return model;
  }

  public void setModel(PresentationModel newModel) {

    if ((this.model != null) && (this.listener != null)) {
      this.model.getModel(property.getName()).removePropertyChangeListener(this.listener);
    }

    this.model = newModel;
    this.listener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        addData(model.getValue(property.getName()));
      }
    };
    this.model.getModel(property.getName()).addPropertyChangeListener(this.listener);

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

    if ((newValue != null) && ((newValue instanceof Number) || (newValue instanceof Amount))) {

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
