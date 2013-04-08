package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextEvent.EventType;
import repast.simphony.context.ContextListener;
import repast.simphony.util.ContextUtils;

/**
 * Integrates a statechart with a simphony simulation by listening for agent
 * removal events and stopping the statechart.
 * 
 * @author Nick Collier
 */
public class DefaultIntegrator implements StateChartSimIntegrator, ContextListener<Object> {

  private static class Element {
    Context<Object> context;
    Map<Object, List<StateChart<?>>> chartMap = new HashMap<Object, List<StateChart<?>>>();
  }

  private List<Element> elements = new ArrayList<Element>();

  private void addChart(Context<Object> context, StateChart<?> chart) {
    Object agent = chart.getAgent();
    Element element = findElement(context);
    if (element == null) {
      element = new Element();
      elements.add(element);
      element.context = context;
      context.addContextListener(this);
    }

    List<StateChart<?>> charts = element.chartMap.get(agent);
    if (charts == null) {
      charts = new ArrayList<StateChart<?>>();
      element.chartMap.put(agent, charts);
    }
    charts.add(chart);
  }

  private Element findElement(Context<?> context) {
    for (Element element : elements) {
      if (context.equals(element.context))
        return element;
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.statecharts.StateChartSimIntegrator#integrate(repast.simphony
   * .statecharts.StateChart)
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean integrate(StateChart<?> chart) {
    Context<Object> context = ContextUtils.getContext(chart.getAgent());
    if (context == null)
      return false;

    addChart(context, chart);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.statecharts.StateChartSimIntegrator#reset()
   */
  @Override
  public void reset() {
    for (Element element : elements) {
      element.context.removeContextListener(this);
    }
    elements.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.context.ContextListener#eventOccured(repast.simphony.context
   * .ContextEvent)
   */
  @Override
  public void eventOccured(ContextEvent<Object> ev) {

    if (ev.getType() == EventType.AGENT_REMOVED) {
      Element element = findElement(ev.getContext());
      List<StateChart<?>> charts = element.chartMap.remove(ev.getTarget());
      // charts can be null if the agent is removed before it is added
      // here -- i.e. if it is removed before its begin is called.
      if (charts != null) {
        for (StateChart<?> chart : charts) {
          chart.stop();
        }
      }
    }
  }
}
