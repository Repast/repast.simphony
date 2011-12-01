/*CopyrightHere*/
package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.FileSinkDescriptor;

public class FileSinkWizardModel extends StaticModel implements DataSetModel<FileSinkDescriptor> {

  private FileSinkDescriptor descriptor;
  private List<DataSetDescriptor> dataSets;

  public FileSinkWizardModel(List<DataSetDescriptor> dataSets) {
    this(dataSets, new FileSinkDescriptor("A File Sink"));
  }

  public FileSinkWizardModel(List<DataSetDescriptor> dataSets, FileSinkDescriptor descriptor) {
    this.descriptor = descriptor;
    this.dataSets = new ArrayList<DataSetDescriptor>(dataSets);
    Collections.sort(this.dataSets, new Comparator<DataSetDescriptor>(){
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

  public void setDescriptor(FileSinkDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public FileSinkDescriptor getDescriptor() {
    return descriptor;
  }

}
