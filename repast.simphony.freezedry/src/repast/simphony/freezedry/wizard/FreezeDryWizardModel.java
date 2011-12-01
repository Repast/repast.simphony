package repast.simphony.freezedry.wizard;

import com.thoughtworks.xstream.converters.Converter;
import org.pietschy.wizard.models.Path;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.scenario.Scenario;
import repast.simphony.util.wizard.DynamicWizardModel;

import java.util.ArrayList;
import java.util.List;

public class FreezeDryWizardModel extends DynamicWizardModel<FreezeDryWizardOption> {

  private List<Converter> converters = new ArrayList<Converter>();

  private FreezerControllerAction action = null;
  private DataSourceBuilder builder;

  private Object freezeDryedContextId;

  private FreezeDryWizardOption freezeDryOption;

  private ScheduleParameters scheduleParams;
  private boolean useRoot;
  private String xmlFile = null;

  public FreezeDryWizardModel(Path path, Scenario scenario, Object contextID) {
    super(path, scenario, contextID);
  }

  public DataSourceBuilder getBuilder() {
    if (builder == null || getChosenOption() != freezeDryOption) {
      freezeDryOption = getChosenOption();
      if (action == null) {
        builder = getChosenOption().createDataSourceBuilder(null);
      } else {
        builder = getChosenOption().createDataSourceBuilder(action.getDataSource());
      }
    }
    return builder;
  }

  public String getXMLFile() {
    return xmlFile;
  }

  public void setXMLFile(String xmlFile) {
    this.xmlFile = xmlFile;
  }

  public FreezerControllerAction getAction() {
    return freezeDryOption.createAction(this, getBuilder()).createAction();
  }

  public Object getFreezeDryedContextId() {
    return freezeDryedContextId;
  }

  public void setFreezeDryedContextId(Object contextId) {
    this.freezeDryedContextId = contextId;
  }

  public ScheduleParameters getScheduleParams() {
    return scheduleParams;
  }

  public void setScheduleParams(ScheduleParameters scheduleParams) {
    this.scheduleParams = scheduleParams;
  }

  public void setUseRoot(boolean b) {
    useRoot = b;
  }

  public boolean useRoot() {
    return useRoot;
  }

  public void setAction(FreezerControllerAction action) {
    this.action = action;
  }

  public void addConverter(Converter converter) {
    if (!converters.contains(converter)) converters.add(converter);
  }

  public void clearConverters() {
    converters.clear();
  }

  public Iterable<Converter> converters() {
    return converters;
  }
}
