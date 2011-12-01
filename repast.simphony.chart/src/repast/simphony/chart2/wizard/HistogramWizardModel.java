/*CopyrightHere*/
package repast.simphony.chart2.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.models.StaticModel;

import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;

public class HistogramWizardModel extends StaticModel /*implements DataSetModel<ConsoleSinkDescriptor>*/ {

  private HistogramChartDescriptor descriptor;
  private List<DataSetDescriptor> dataSets;

  public HistogramWizardModel(List<DataSetDescriptor> dataSets) {
    this(dataSets, new HistogramChartDescriptor("Histogram Chart"));
  }

  public HistogramWizardModel(List<DataSetDescriptor> dataSets, HistogramChartDescriptor descriptor) {
    this.descriptor = descriptor;
    this.dataSets = new ArrayList<DataSetDescriptor>(dataSets);
    Collections.sort(this.dataSets, new Comparator<DataSetDescriptor>() {
      @Override
      public int compare(DataSetDescriptor arg0, DataSetDescriptor arg1) {
        return arg0.getName().compareTo(arg1.getName());
      }
    });
  }

  public String getName() {
    return descriptor.getName();
  }

  public List<DataSetDescriptor> getDataSets() {
    return dataSets;
  }

  public void setName(String actionName) {
    descriptor.setName(actionName);
  }

  public void setDescriptor(HistogramChartDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public HistogramChartDescriptor getDescriptor() {
    return descriptor;
  }

  public DataSetType getDataSetType() {
    DataSetDescriptor ds = getDataSet();
    if (ds == null) return null;
    return ds.getType();
  }
  
  public DataSetDescriptor getDataSet() {
    String dsName = this.descriptor.getDataSet();
    for (DataSetDescriptor ds : dataSets) {
      if (ds.getName().equals(dsName)) return ds;
    }
    
    return null;
  }
}
