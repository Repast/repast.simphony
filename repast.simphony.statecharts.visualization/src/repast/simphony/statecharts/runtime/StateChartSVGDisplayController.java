package repast.simphony.statecharts.runtime;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.svg.SVGDocument;

import repast.simphony.statecharts.AbstractState;
import repast.simphony.statecharts.StateChart;
import repast.simphony.statecharts.StateChartListener;

public class StateChartSVGDisplayController implements StateChartListener {

  private StateChart stateChart;
  StateChartSVGDisplay svgDisplay;
  StateChartSVGModel model;

  Object agent;

  public StateChartSVGDisplayController(Object agent, StateChart stateChart) {
    this.agent = agent;
    this.stateChart = stateChart;
  }

  private String createFrameTitle() {
    StringBuilder sb = new StringBuilder();
    sb.append("Agent: ");
    sb.append(agent);
    sb.append(", ");
    sb.append("Statechart: ");
    sb.append(stateChart.getClass().getName());
    return sb.toString();
  }

  protected URI locateSVGResource() {
    StringBuilder sb = new StringBuilder();
    sb.append('/');
    String path = stateChart.getClass().getName().replace('.', '/');
    sb.append(path);
    sb.append(".svg");
    URL resource = stateChart.getClass().getResource(sb.toString());
    if (resource != null) {
      return new File(resource.getFile()).toURI();
    }
    return null;
  }

  public void createAndShowDisplay() {
    final String frameTitle = createFrameTitle();
    final URI uri = locateSVGResource();
    if (uri != null) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          initializeDisplay(frameTitle, uri);
        }
      });
    } else {
      System.out.println("SVG resource not located.");
    }
  }

  /**
   * Initialize display. Should be called from EDT.
   * 
   * @param frameTitle
   * @param uri
   */
  private void initializeDisplay(String frameTitle, URI uri) {
    svgDisplay = new StateChartSVGDisplay(this, frameTitle, uri);
    svgDisplay.initialize();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

  }

  boolean tryAnotherUpdate = false;
  @Override
  public void update() {
    List<AbstractState> states = stateChart.getCurrentStates();
    Set<String> activeUUIDs = new LinkedHashSet<String>();
    for (AbstractState state : states) {
      activeUUIDs.add(stateChart.getUuidForState(state));
    }
    model.setActiveUUIDs(activeUUIDs);
    tryAnotherUpdate = false;
    svgDisplay.renewDocument();
  }

  // Called from display code after the svg document is loaded by the JSVGCanvas
  public void initializeModel(SVGDocument svgDoc) {
    model = new StateChartSVGModel(svgDoc);
    svgDisplay.setModel(model);
    stateChart.registerStateChartListener(this);
  }

  private List<StatechartCloseListener> scls = new ArrayList<StatechartCloseListener>();

  public void registerCloseListener(StatechartCloseListener scl) {
    scls.add(scl);
  }

  public void notifyCloseListeners() {
    for (StatechartCloseListener scl : scls) {
      scl.statechartClosed();
    }
  }

  public void closeDisplayWithoutNotification() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        svgDisplay.closeFrame();
      }
    });

  }

}
