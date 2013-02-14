package repast.simphony.ui.probe;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import repast.simphony.context.Context;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.StringConverter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ClassUtilities;
import repast.simphony.util.ContextUtils;
import simphony.util.messages.MessageCenter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Creates a panel showing the properties of a probed object.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProbePanelCreator {

  private MessageCenter msgCenter = MessageCenter.getMessageCenter(ProbePanelCreator.class);
  private List<PresentationModel<?>> models = new ArrayList<PresentationModel<?>>();
  private Map<String, List> listContstraints = new HashMap<String, List>();
  private ProbeableBeanInfo pbInfo;
  private Object targetBean;

  public ProbePanelCreator(Parameters params) {
    try {
      pbInfo = new ParameterProbeBeanCreator().createProbeableBean(params);
      models.add(new PresentationModel(pbInfo.getBean()));

      for (String name : params.getSchema().parameterNames()) {
	ParameterSchema details = params.getSchema().getDetails(name);
	List list = details.getConstrainingList();
	if (list != null) {
	  if (needsConversion(details)) {
	    List<String> strList = new ArrayList<String>();
	    StringConverter conv = details.getConverter();
	    for (Object obj : list) {
	      strList.add(conv.toString(obj));
	    }
	    listContstraints.put(name, strList);
	  } else
	    listContstraints.put(name, list);
	}
      }

    } catch (Exception ex) {
      msgCenter.warn("Error creating probe panel.", ex);
    }
  }

  private boolean needsConversion(ParameterSchema details) {
    Class type = details.getType();
    return details.getConverter() != null
	&& !(ClassUtilities.isNumericType(type) || type.equals(boolean.class)
	    || type.equals(Boolean.class) || type.equals(String.class));
  }

  private List<AbstractProbedProperty> createProperties(BeanInfo info, OldProbeModel bean, boolean wrap) {
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    List<AbstractProbedProperty> props = new ArrayList<AbstractProbedProperty>();
    for (PropertyDescriptor pd : pds) {
      String name = pd.getName();
      String displayName = pbInfo.getDisplayName(name);
      if (displayName == null) {
	// see if its one of those funny "A*" type parameter names
	String propName = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
	displayName = pbInfo.getDisplayName(propName);
      }
      pd.setDisplayName(displayName);
      List constraints = listContstraints.get(pd.getName());
      AbstractProbedProperty prop;
      if (constraints == null)
	prop = ProbedPropertyFactory.createProbedProperty(pd, wrap);
      else
	prop = ProbedPropertyFactory.createProbedProperty(pd, constraints, wrap);
      if (prop != null)
	props.add(prop);
    }
    return props;
  }

  private JPanel createPanel(List<AbstractProbedProperty> props, String title, boolean buffered) {
    Collections.sort(props, new Comparator<AbstractProbedProperty>() {
      public int compare(AbstractProbedProperty o1, AbstractProbedProperty o2) {
	return o1.getDisplayName().compareTo(o2.getDisplayName());
      }
    });
    FormLayout layout = new FormLayout("3dlu, right:pref, 6dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator(title);
    builder.setLeadingColumnOffset(1);
    builder.nextLine();
    PresentationModel model = models.get(0);
    for (AbstractProbedProperty prop : props) {
      JComponent component = prop.getComponent(model, buffered);
      if (component instanceof JFormattedTextField) {
	component.addFocusListener(tempFocusCommitter);
      } else if (component instanceof JPanel) {
	for (Component jcomponent : ((JPanel) component).getComponents()) {
	  if (jcomponent instanceof JFormattedTextField) {
	    jcomponent.addFocusListener(tempFocusCommitter);
	  }
	}
      }
      builder.append(prop.getDisplayName() + ":", component);
      builder.nextLine();
    }

    if (targetBean != null) {
      builder.appendSeparator("Locations");
      addLocations(builder, buffered);
    }

    return builder.getPanel();
  }

  private void addLocations(DefaultFormBuilder builder, boolean buffered) {
    // see if we can find the context for it and the projections
    Context context = ContextUtils.getContext(targetBean);
    try {
      BeanInfo info;
      if (context != null) {
	info = Introspector.getBeanInfo(GridLocationProbe.class, Object.class);
	for (Grid grid : (Iterable<Grid>) context.getProjections(Grid.class)) {
	  GridLocationProbe probe = new GridLocationProbe(targetBean, grid);
	  PresentationModel pModel = new PresentationModel(probe);
	  models.add(pModel);
	  StringProbedProperty prop = new StringProbedProperty(info.getPropertyDescriptors()[0]);

	  JComponent component = prop.getComponent(pModel, buffered);
	  builder.append(grid.getName() + ":", component);
	  builder.nextLine();
	}

	info = Introspector.getBeanInfo(SpaceLocationProbe.class, Object.class);
	for (ContinuousSpace space : (Iterable<ContinuousSpace>) context
	    .getProjections(ContinuousSpace.class)) {
	  SpaceLocationProbe probe = new SpaceLocationProbe(targetBean, space);
	  PresentationModel pModel = new PresentationModel(probe);
	  models.add(pModel);
	  StringProbedProperty prop = new StringProbedProperty(info.getPropertyDescriptors()[0]);

	  JComponent component = prop.getComponent(pModel, buffered);
	  builder.append(space.getName() + ":", component);
	  builder.nextLine();
	}

	info = Introspector.getBeanInfo(GeographyLocationProbe.class, Object.class);
	for (Geography space : (Iterable<Geography>) context.getProjections(Geography.class)) {
	  GeographyLocationProbe probe = new GeographyLocationProbe(targetBean, space);
	  PresentationModel pModel = new PresentationModel(probe);
	  models.add(pModel);
	  StringProbedProperty prop = new StringProbedProperty(info.getPropertyDescriptors()[0]);

	  JComponent component = prop.getComponent(pModel, buffered);
	  builder.append(space.getName() + ":", component);
	  builder.nextLine();
	}
      }
      info = Introspector.getBeanInfo(ValueLayerLocationProbe.class, Object.class);
      if (targetBean instanceof ValueLayerProbeObject2D) {
	ValueLayerLocationProbe probe = new ValueLayerLocationProbe(targetBean);
	PresentationModel pModel = new PresentationModel(probe);
	models.add(pModel);
	StringProbedProperty prop = new StringProbedProperty(info.getPropertyDescriptors()[0]);

	JComponent component = prop.getComponent(pModel, buffered);
	builder.append("value layer:", component);
	builder.nextLine();
      }
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
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
	JFormattedTextField source = ((JFormattedTextField) e.getSource());
	try {
	  source.commitEdit();
	  source.setValue(source.getValue());
	} catch (ParseException e1) {
	  source.setValue(source.getValue());
	}

      }
    }
  };

  /**
   * Creates a Map containing the names and values for properties in a probed
   * object. This method may be referenced elsewhere for getting probe info on
   * an object.
   * 
   * @return a Map containing the names and values for properties in a probed
   *         object.
   */
  public Map<String, Object> getProbedProperties() {
    Map<String, Object> valuesMap = new LinkedHashMap<String, Object>();

    try {
      PresentationModel model = models.get(0);
      BeanInfo info = Introspector.getBeanInfo(model.getBean().getClass(), Object.class);

      List<AbstractProbedProperty> props = createProperties(info, (OldProbeModel) model.getBean(), false);

      Collections.sort(props, new Comparator<AbstractProbedProperty>() {
	public int compare(AbstractProbedProperty o1, AbstractProbedProperty o2) {
	  return o1.getDisplayName().compareTo(o2.getDisplayName());
	}
      });

      for (AbstractProbedProperty prop : props) {
	valuesMap.put(prop.getName(), model.getValue(prop.getName()));
      }

      return valuesMap;

    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Probe getProbe(String title, boolean wrap) {
    try {
      PresentationModel model = models.get(0);
      BeanInfo info = Introspector.getBeanInfo(model.getBean().getClass(), Object.class);
      List<AbstractProbedProperty> props = createProperties(info, (OldProbeModel) model.getBean(), wrap);
      JPanel panel = createPanel(props, title, false);
      return new Probe(models, panel, title);
    } catch (IntrospectionException e) {
      msgCenter.warn("Error creating probe.", e);
    }
    return null;
  }

  public Probe getBufferedProbe(String title, boolean wrap) {
    try {
      PresentationModel model = models.get(0);
      BeanInfo info = Introspector.getBeanInfo(model.getBean().getClass(), Object.class);
      List<AbstractProbedProperty> props = createProperties(info, (OldProbeModel) model.getBean(), wrap);
      JPanel panel = createPanel(props, title, true);
      return new Probe(models, panel, title, true);
    } catch (IntrospectionException e) {
      msgCenter.warn("Error creating probe.", e);
    }
    return null;
  }
}
