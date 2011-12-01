/*CopyrightHere*/
package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data2.engine.ConsoleSinkDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor;

public class ConsoleSinkWizardModel extends StaticModel implements DataSetModel<ConsoleSinkDescriptor> {

  private ConsoleSinkDescriptor descriptor;
  private List<DataSetDescriptor> dataSets;

  public ConsoleSinkWizardModel(List<DataSetDescriptor> dataSets) {
    this(dataSets, new ConsoleSinkDescriptor("A Console Sink"));
  }

  public ConsoleSinkWizardModel(List<DataSetDescriptor> dataSets, ConsoleSinkDescriptor descriptor) {
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

  public void setDescriptor(ConsoleSinkDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public ConsoleSinkDescriptor getDescriptor() {
    return descriptor;
  }

}
