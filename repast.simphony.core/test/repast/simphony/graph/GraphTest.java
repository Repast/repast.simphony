package repast.simphony.graph;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.*;
import repast.simphony.engine.environment.RunState;
import repast.simphony.query.space.graph.NetworkAdjacent;
import repast.simphony.query.space.graph.NetworkPredecessor;
import repast.simphony.query.space.graph.NetworkSuccessor;
import repast.simphony.query.space.projection.Linked;
import repast.simphony.query.space.projection.LinkedFrom;
import repast.simphony.query.space.projection.LinkedTo;
import repast.simphony.query.space.projection.Within;
import repast.simphony.space.graph.*;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

import java.io.IOException;
import java.util.*;

/**
 * Tests for Graphs and networks.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GraphTest extends TestCase {

  private DefaultContext<Integer> context = new DefaultContext<Integer>(
          "A Context");

  private List<Integer> ints = new ArrayList<Integer>();

  public class TestEdgeCreator implements EdgeCreator {

    /**
     * Creates an Edge with the specified source, target, direction and weight.
     *
     * @param source     the edge source
     * @param target     the edge target
     * @param isDirected whether or not the edge is directed
     * @param weight     the weight of the edge
     * @return the created edge.
     */
    public TestEdge createEdge(Object source, Object target, boolean isDirected, double weight) {
      return new TestEdge(source, target, isDirected, weight);
    }

    /**
     * Gets the edge type produced by this EdgeCreator.
     *
     * @return the edge type produced by this EdgeCreator.
     */
    public Class getEdgeType() {
      return TestEdge.class;
    }
  }

  public class NodeAgent {

    public String id;

    public NodeAgent(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }


  public void setUp() {
    RunState.init(null, null, null);
    for (int i = 0; i < 30; i++) {
      ints.add(i);
    }
    context.clear();
    NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
            "Network 1", context, true);
    NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
            "Network 2", context, false);
  }

  public void testEdgeCreatorFactory() throws Exception {
    EdgeCreatorFactory fac = new EdgeCreatorFactory();
    EdgeCreator creator = fac.createEdgeCreator(TestEdge.class.getName());
    RepastEdge edge = creator.createEdge(1, 2, true, 10.0);
    assertTrue(edge instanceof TestEdge);
    assertEquals(1, edge.getSource());
    assertEquals(2, edge.getTarget());
    assertEquals(10.0, edge.getWeight());

  }

  public void testEdgeCreator() {
    Network net3 = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
            "Network 3", context, true, new TestEdgeCreator());
    context.add(1);
    context.add(2);
    context.add(3);
    context.add(4);
    RepastEdge edge = net3.addEdge(1, 2);
    assertTrue(edge instanceof TestEdge);
    assertEquals(edge, net3.getEdge(1, 2));
    assertTrue(net3.addEdge(3, 4, 1.234) instanceof TestEdge);

    Network net4 = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
            "Network 4", context, false, new TestEdgeCreator());
    edge = net4.addEdge(1, 2);
    assertTrue(edge instanceof TestEdge);
    assertEquals(edge, net4.getEdge(1, 2));
    assertTrue(net4.addEdge(3, 4, 1.234) instanceof TestEdge);
  }

  public void testContextSync() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    assertEquals(2, net1.size());
    assertEquals(2, net2.size());

    Set<Integer> set = new HashSet<Integer>();
    set.add(ints.get(0));
    set.add(ints.get(1));
    for (Integer val : net1.getNodes()) {
      assertTrue(set.remove(val));
    }
    assertEquals(0, set.size());

    set.add(ints.get(1));
    context.remove(ints.get(0));
    assertEquals(1, net1.size());
    assertEquals(1, net2.size());
    for (Integer val : net1.getNodes()) {
      assertTrue(set.remove(val));
    }
    assertEquals(0, set.size());

  }

  public void testEdgeAddRemove() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));

    RepastEdge<Integer> edge = net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));

    assertEquals(2, net1.numEdges());
    assertEquals(0, net2.numEdges());

    net1.removeEdge(edge);
    assertEquals(1, net1.numEdges());
    assertEquals(0, net2.numEdges());
  }

  public void testEdgeNodeRemove() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");

    context.add(ints.get(0));
    context.add(ints.get(1));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    assertEquals(2, net1.numEdges());

    context.remove(ints.get(0));
    assertEquals(0, net1.numEdges());
  }

  public void testDegree() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(1), ints.get(2));

    assertEquals(2, net1.getDegree(ints.get(0)));
    assertEquals(1, net1.getOutDegree(ints.get(0)));
    assertEquals(1, net1.getInDegree(ints.get(0)));

    assertEquals(3, net1.getDegree(ints.get(1)));
    assertEquals(2, net1.getOutDegree(ints.get(1)));
    assertEquals(1, net1.getInDegree(ints.get(1)));

    assertEquals(2, net2.getDegree(ints.get(2)));
    assertEquals(2, net2.getOutDegree(ints.get(2)));
    assertEquals(2, net2.getInDegree(ints.get(2)));
  }

  public void testPredecessors() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network<Integer>) context
            .getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(2));

    Set<Integer> set = new HashSet<Integer>();
    for (Integer i : net1.getPredecessors(ints.get(0))) {
      set.add(i);
    }

    assertEquals(2, set.size());
    assertTrue(set.contains(ints.get(2)));
    assertTrue(set.contains(ints.get(1)));

    List<Integer> list = new ArrayList<Integer>();
    for (Integer i : net2.getPredecessors(ints.get(0))) {
      list.add(i);
    }
    assertEquals(1, list.size());
    assertTrue(list.contains(ints.get(2)));
  }

  public void testRandomPredecessors() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(2));
    net2.addEdge(ints.get(3), ints.get(2));

    Object obj = net1.getRandomPredecessor(ints.get(0));
    Set<Integer> set = new HashSet<Integer>();
    set.add(ints.get(1));
    set.add(ints.get(2));
    assertTrue(set.contains(obj));

    obj = net1.getRandomPredecessor(ints.get(3));
    assertTrue(obj == null);

    obj = net2.getRandomPredecessor(ints.get(0));
    set = new HashSet<Integer>();
    set.add(ints.get(3));
    set.add(ints.get(2));
    assertTrue(set.contains(obj));
  }

  public void testRandomSuccessors() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(2));
    net2.addEdge(ints.get(3), ints.get(2));

    Object obj = net1.getRandomSuccessor(ints.get(1));
    Set<Integer> set = new HashSet<Integer>();
    set.add(ints.get(0));
    set.add(ints.get(2));
    assertTrue(set.contains(obj));

    obj = net1.getRandomSuccessor(ints.get(3));
    assertTrue(obj == null);

    obj = net2.getRandomSuccessor(ints.get(0));
    set = new HashSet<Integer>();
    set.add(ints.get(3));
    set.add(ints.get(2));
    assertTrue(set.contains(obj));
  }

  public void testRandomAdjacent() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(2));
    net2.addEdge(ints.get(3), ints.get(2));

    Object obj = net1.getRandomAdjacent(ints.get(0));
    Set<Integer> set = new HashSet<Integer>();
    set.add(ints.get(1));
    set.add(ints.get(2));
    assertTrue(set.contains(obj));

    obj = net1.getRandomAdjacent(ints.get(3));
    assertTrue(obj == null);

    obj = net2.getRandomAdjacent(ints.get(0));
    set = new HashSet<Integer>();
    set.add(ints.get(3));
    set.add(ints.get(2));
    assertTrue(set.contains(obj));
  }

  public void testSuccessors() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(2));

    Set<Integer> set = new HashSet<Integer>();
    for (Object i : net1.getSuccessors(ints.get(1))) {
      set.add((Integer) i);
    }

    assertEquals(2, set.size());
    assertTrue(set.contains(ints.get(0)));
    assertTrue(set.contains(ints.get(2)));

    List<Integer> list = new ArrayList<Integer>();
    for (Iterator<Integer> iter = net2.getSuccessors(ints.get(2))
            .iterator(); iter.hasNext();) {
      list.add(iter.next());
    }
    assertEquals(1, list.size());
    assertTrue(list.contains(ints.get(0)));
  }

  public void testAdjacent() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(2));

    List<Integer> list = new ArrayList<Integer>();
    for (Iterator<Integer> iter = net1.getAdjacent(ints.get(1)).iterator(); iter
            .hasNext();) {
      list.add(iter.next());
    }

    assertEquals(2, list.size());
    assertTrue(list.contains(ints.get(0)));
    assertTrue(list.contains(ints.get(2)));

    list = new ArrayList<Integer>();
    for (Iterator<Integer> iter = net2.getAdjacent(ints.get(2)).iterator(); iter
            .hasNext();) {
      list.add((Integer) iter.next());
    }
    assertEquals(1, list.size());
    assertTrue(list.contains(ints.get(0)));
  }

  public void testNodeOps() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(1));

    assertTrue(net1.isAdjacent(ints.get(2), ints.get(0)));
    assertTrue(net1.isAdjacent(ints.get(0), ints.get(2)));
    assertTrue(!net1.isAdjacent(ints.get(1), ints.get(3)));

    assertTrue(net1.isPredecessor(ints.get(2), ints.get(0)));
    assertTrue(!net1.isPredecessor(ints.get(0), ints.get(2)));

    assertTrue(!net1.isSuccessor(ints.get(2), ints.get(0)));
    assertTrue(net1.isSuccessor(ints.get(0), ints.get(2)));

    assertTrue(net2.isAdjacent(ints.get(2), ints.get(0)));
    assertTrue(net2.isPredecessor(ints.get(0), ints.get(2)));
    assertTrue(net2.isPredecessor(ints.get(2), ints.get(0)));
    assertTrue(net2.isSuccessor(ints.get(2), ints.get(0)));
    assertTrue(net2.isSuccessor(ints.get(0), ints.get(2)));
    assertTrue(!net2.isSuccessor(ints.get(0), ints.get(3)));
  }

  public void testParallelEdges() {
    Network<Integer> dirNet = context.getProjection(Network.class, "Network 1");
    Network<Integer> undirNet = context.getProjection(Network.class, "Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));

    // test 2 directed edges with different source/target
    dirNet.addEdge(ints.get(0), ints.get(1));
    dirNet.addEdge(ints.get(1), ints.get(0));
    assertEquals(dirNet.getDegree(ints.get(0)), 2);
    assertEquals(dirNet.numEdges(), 2);

    // add another directed and check if there are still only 2
    dirNet.addEdge(ints.get(0), ints.get(1));
    assertEquals(dirNet.getDegree(ints.get(0)), 2);
    assertEquals(dirNet.numEdges(), 2);

    // test 2 undirected edges with same source/target
    undirNet.addEdge(ints.get(0), ints.get(1));
    undirNet.addEdge(ints.get(0), ints.get(1));
    assertEquals(undirNet.numEdges(), 1);

    // add another undirected and check if there is still only 1
    undirNet.addEdge(ints.get(1), ints.get(0));
    assertEquals(undirNet.numEdges(), 1);
  }

  public void testIterationOrder() {
    // tests that iteration order matches insertion order
    Network<Integer> net = context.getProjection(Network.class,
            "Network 1");
    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    Integer[] vals = new Integer[]{1, 2, 3};

    for (int i = 0; i < 3000; i++) {
      net.addEdge(ints.get(0), ints.get(1));
      net.addEdge(ints.get(0), ints.get(2));
      net.addEdge(ints.get(0), ints.get(3));

      assertEquals(3, net.getDegree(ints.get(0)));
      int j = 0;
      for (RepastEdge edge : net.getEdges(ints.get(0))) {
        assertEquals(vals[j++], edge.getTarget());
      }

      net.removeEdge(net.getEdge(ints.get(0), ints.get(1)));
      net.removeEdge(net.getEdge(ints.get(0), ints.get(2)));
      net.removeEdge(net.getEdge(ints.get(0), ints.get(3)));
    }

    net = (Network) context.getProjection("Network 2");

    for (int i = 0; i < 3000; i++) {
      net.addEdge(ints.get(0), ints.get(1));
      net.addEdge(ints.get(0), ints.get(2));
      net.addEdge(ints.get(0), ints.get(3));

      assertEquals(3, net.getDegree(ints.get(0)));
      int j = 0;
      for (RepastEdge edge : net.getEdges(ints.get(0))) {
        assertEquals(vals[j++], edge.getTarget());
      }

      net.removeEdge(net.getEdge(ints.get(0), ints.get(1)));
      net.removeEdge(net.getEdge(ints.get(0), ints.get(2)));
      net.removeEdge(net.getEdge(ints.get(0), ints.get(3)));
    }

  }

  public void testEdges() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    RepastEdge<Integer> anEdge = new RepastEdge<Integer>(ints.get(1), ints
            .get(0), false);
    anEdge = net1.addEdge(anEdge);
    // edge should be changed to match net directionality
    assertTrue(anEdge.isDirected());
    net1.addEdge(ints.get(1), ints.get(2));
    RepastEdge<Integer> edge = net1.addEdge(ints.get(2), ints.get(0));
    assertEquals(1.0, edge.getWeight());
    assertTrue(edge.isDirected());

    net2.addEdge(ints.get(2), ints.get(0));
    edge = net2.addEdge(ints.get(0), ints.get(1), 3.14);
    assertEquals(3.14, edge.getWeight());
    assertTrue(!edge.isDirected());

    int count = 0;
    for (Iterator<RepastEdge<Integer>> iter = net1.getEdges(ints.get(0))
            .iterator(); iter.hasNext();) {
      edge = iter.next();
      count++;
    }
    // should be 3 edges total
    assertEquals(3, count);
    net1.removeEdge(net1.getEdge(ints.get(2), ints.get(0)));
    // removed the edge between 0 and 2 so they should no longer be adjacent
    assertTrue(!net1.isAdjacent(ints.get(0), ints.get(2)));

    count = 0;
    for (Iterator<RepastEdge<Integer>> iter = net2.getEdges(ints.get(0))
            .iterator(); iter.hasNext();) {
      edge = iter.next();
      count++;
    }
    // should be 3 edges total
    assertEquals(2, count);
    net2.removeEdge(net2.getEdge(ints.get(0), ints.get(2)));
    // removed the edge between 0 and 2 so they should no longer be adjacent
    assertTrue(!net2.isAdjacent(ints.get(0), ints.get(2)));
  }

  public void testInEdges() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(1));

    int count = 0;
    for (Iterator<RepastEdge<Integer>> iter = net1.getInEdges(ints.get(0))
            .iterator(); iter.hasNext();) {
      RepastEdge<Integer> edge = iter.next();
      count++;
    }
    // should be 3 edges total
    assertEquals(2, count);
    net1.removeEdge(net1.getEdge(ints.get(2), ints.get(0)));
    // removed the edge between 0 and 2 so they should no longer be adjacent
    assertTrue(!net1.isAdjacent(ints.get(0), ints.get(2)));

    // should find it even though source and target are
    // different from adding order -- shouldn't matter
    // because undirected
    assertTrue(net2.getEdge(ints.get(0), ints.get(2)) != null);

    count = 0;
    for (Iterator<RepastEdge<Integer>> iter = net2.getInEdges(ints.get(0))
            .iterator(); iter.hasNext();) {
      RepastEdge<Integer> edge = iter.next();
      count++;
    }
    // should be 3 edges total
    assertEquals(2, count);
    net2.removeEdge(net2.getEdge(ints.get(0), ints.get(2)));
    // removed the edge between 0 and 2 so they should no longer be adjacent
    assertTrue(!net2.isAdjacent(ints.get(0), ints.get(2)));
  }

  public void testOutEdges() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    Network<Integer> net2 = (Network) context.getProjection("Network 2");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    context.add(ints.get(3));

    net1.addEdge(ints.get(0), ints.get(1));
    net1.addEdge(ints.get(1), ints.get(0));
    net1.addEdge(ints.get(1), ints.get(2));
    net1.addEdge(ints.get(2), ints.get(0));

    net2.addEdge(ints.get(2), ints.get(0));
    net2.addEdge(ints.get(0), ints.get(1));

    int count = 0;
    RepastEdge<Integer> removeEdge = null;

    for (Iterator<RepastEdge<Integer>> iter = net1.getOutEdges(ints.get(1))
            .iterator(); iter.hasNext();) {
      RepastEdge<Integer> edge = iter.next();
      if (edge.getTarget().equals(ints.get(0))) {
        removeEdge = edge;
      }
      count++;
    }
    // should be 3 edges total
    assertEquals(2, count);
    net1.removeEdge(removeEdge);
    // removed the edge between 0 and 2 so they should no longer be adjacent
    assertTrue(!net1.isPredecessor(ints.get(1), ints.get(0)));

    count = 0;
    for (Iterator<RepastEdge<Integer>> iter = net2.getOutEdges(ints.get(0))
            .iterator(); iter.hasNext();) {
      RepastEdge<Integer> edge = iter.next();
      if (edge.getSource().equals(ints.get(2))) {
        removeEdge = edge;
      }
      count++;
    }
    // should be 3 edges total
    assertEquals(2, count);
    net2.removeEdge(removeEdge);
    // removed the edge between 0 and 2 so they should no longer be adjacent
    assertTrue(!net2.isAdjacent(ints.get(0), ints.get(2)));
  }

  class Listener implements ProjectionListener<Integer> {

    Set<ProjectionEvent.Type> types = new HashSet<ProjectionEvent.Type>();
    Object obj;
    RepastEdge<Integer> edge;

    public void projectionEventOccurred(ProjectionEvent<Integer> ev) {
      types.add(ev.getType());
      if (ev.getSubject() instanceof RepastEdge) {
        edge = (RepastEdge<Integer>) ev.getSubject();
      } else {
        obj = ev.getSubject();
      }
    }

    public void reset() {
      types.clear();
      obj = null;
    }
  }

  public void testListener() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));

    Listener listener = new Listener();
    net1.addProjectionListener(listener);

    context.add(ints.get(3));
    assertEquals(1, listener.types.size());
    assertTrue(listener.types.contains(ProjectionEvent.Type.OBJECT_ADDED));
    assertEquals(ints.get(3), listener.obj);
    listener.reset();

    RepastEdge<Integer> edge = net1.addEdge(ints.get(0), ints.get(1));
    assertEquals(1, listener.types.size());
    assertTrue(listener.types.contains(ProjectionEvent.Type.EDGE_ADDED));
    assertEquals(edge, listener.edge);
    listener.reset();

    context.remove(ints.get(0));
    // should remove 0 and 0's edge.
    assertEquals(2, listener.types.size());
    assertTrue(listener.types.contains(ProjectionEvent.Type.EDGE_REMOVED));
    assertTrue(listener.types.contains(ProjectionEvent.Type.OBJECT_REMOVED));
    assertEquals(edge, listener.edge);
    assertEquals(ints.get(0), listener.obj);
    listener.reset();

    context.add(ints.get(0));
    edge = net1.addEdge(ints.get(0), ints.get(1));
    listener.reset();

    net1.removeEdge(edge);
    assertEquals(1, listener.types.size());
    assertTrue(listener.types.contains(ProjectionEvent.Type.EDGE_REMOVED));
    assertEquals(edge, listener.edge);
    assertTrue(listener.obj == null);
  }

  public void testPredicates() {
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");

    context.add(ints.get(0));
    context.add(ints.get(1));
    context.add(ints.get(2));
    net1.addEdge(ints.get(0), ints.get(1));

    LinkedTo linkedTo = new LinkedTo(ints.get(0), ints.get(1));
    assertTrue(net1.evaluate(linkedTo));
    linkedTo = new LinkedTo(ints.get(1), ints.get(0));
    assertTrue(!net1.evaluate(linkedTo));

    LinkedFrom from = new LinkedFrom(ints.get(1), ints.get(0));
    assertTrue(net1.evaluate(from));
    from = new LinkedFrom(ints.get(0), ints.get(1));
    assertTrue(!net1.evaluate(from));

    Linked linked = new Linked(ints.get(1), ints.get(0));
    assertTrue(net1.evaluate(linked));
    linked = new Linked(ints.get(2), ints.get(1));
    assertTrue(!net1.evaluate(linked));
  }

  public void testDirectedNetLinked() {
    Network<Integer> net = NetworkFactoryFinder.createNetworkFactory(null)
            .createNetwork("net", context, true);
    context.addAll(ints);
    net.addEdge(1, 2);
    net.addEdge(2, 3);
    net.addEdge(3, 4);

    Linked linked = new Linked(1, 2);
    assertTrue(net.evaluate(linked));

    linked = new Linked(2, 10);
    assertFalse(net.evaluate(linked));

    LinkedTo linkedTo = new LinkedTo(2, 3);
    assertTrue(net.evaluate(linkedTo));

    linkedTo = new LinkedTo(3, 2);
    assertFalse(net.evaluate(linkedTo));

    LinkedFrom linkedFrom = new LinkedFrom(2, 3);
    assertFalse(net.evaluate(linkedFrom));

    linkedFrom = new LinkedFrom(3, 2);
    assertTrue(net.evaluate(linkedFrom));

    Within within = new Within(1, 4, 3);
    assertTrue(net.evaluate(within));

    within = new Within(1, 4, 2);
    assertFalse(net.evaluate(within));

    within = new Within(4, 1, 5);
    assertFalse(net.evaluate(within));
  }

  public void testUndirectedNetLinked() {
    Network<Integer> net = NetworkFactoryFinder.createNetworkFactory(null)
            .createNetwork("net", context, false);
    context.addAll(ints);
    net.addEdge(1, 2);
    net.addEdge(2, 3);
    net.addEdge(3, 4);

    Linked linked = new Linked(1, 2);
    assertTrue(net.evaluate(linked));

    linked = new Linked(2, 10);
    assertFalse(net.evaluate(linked));

    LinkedTo linkedTo = new LinkedTo(2, 3);
    assertTrue(net.evaluate(linkedTo));

    linkedTo = new LinkedTo(3, 2);
    assertTrue(net.evaluate(linkedTo));

    LinkedFrom linkedFrom = new LinkedFrom(2, 3);
    assertTrue(net.evaluate(linkedFrom));

    linkedFrom = new LinkedFrom(3, 2);
    assertTrue(net.evaluate(linkedFrom));

    Within within = new Within(1, 4, 3);
    assertTrue(net.evaluate(within));

    within = new Within(1, 4, 2);
    assertFalse(net.evaluate(within));

    within = new Within(4, 1, 5);
    assertTrue(net.evaluate(within));
  }

  public void testShortestJungPath() {
    for (int i = 1; i < 7; i++) {
      context.add(ints.get(i));
    }
    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");

    net1.addEdge(ints.get(1), ints.get(2), 1);
    net1.addEdge(ints.get(2), ints.get(3), 1);

    net1.addEdge(ints.get(1), ints.get(4), 1);
    net1.addEdge(ints.get(2), ints.get(5), 10);
    net1.addEdge(ints.get(3), ints.get(6), 1);

    net1.addEdge(ints.get(4), ints.get(5), 5);
    net1.addEdge(ints.get(5), ints.get(6), 1);

    net1.addEdge(ints.get(1), ints.get(3), 10);

    ShortestPath<Integer> path = new ShortestPath<Integer>(net1);

    List<RepastEdge<Integer>> list = path.getPath(ints.get(1), ints.get(6));

    assertEquals(3, list.size());

    double pathLength = 0;
    for (RepastEdge<Integer> edge : list)
      pathLength += edge.getWeight();

    assertEquals(3.0, pathLength);
    assertEquals(3.0, path.getPathLength(ints.get(1), ints.get(6)));

    list = path.getPath(ints.get(1), ints.get(3));
    assertEquals(2, list.size());

    pathLength = 0;
    for (RepastEdge<Integer> edge : list)
      pathLength += edge.getWeight();

    assertEquals(2.0, pathLength);
    assertEquals(2.0, path.getPathLength(ints.get(1), ints.get(3)));

    list = path.getPath(ints.get(1), ints.get(5));
    assertEquals(2, list.size());

    pathLength = 0;
    for (RepastEdge<Integer> edge : list)
      pathLength += edge.getWeight();

    assertEquals(6.0, pathLength);
    assertEquals(6.0, path.getPathLength(ints.get(1), ints.get(5)));
  }


  public void testShortestPath() {
    for (int i = 0; i < 7; i++) {
      context.add(ints.get(i));
    }

    Network<Integer> net1 = context.getProjection(Network.class,
            "Network 1");
    net1.addEdge(ints.get(0), ints.get(1), 2);
    net1.addEdge(ints.get(0), ints.get(3), 1);

    net1.addEdge(ints.get(1), ints.get(3), 3);
    net1.addEdge(ints.get(1), ints.get(4), 10);

    net1.addEdge(ints.get(2), ints.get(0), 4);
    net1.addEdge(ints.get(2), ints.get(5), 5);

    net1.addEdge(ints.get(3), ints.get(2), 2);
    net1.addEdge(ints.get(3), ints.get(4), 2);
    net1.addEdge(ints.get(3), ints.get(5), 8);
    net1.addEdge(ints.get(3), ints.get(6), 4);

    net1.addEdge(ints.get(4), ints.get(6), 6);

    net1.addEdge(ints.get(6), ints.get(5), 1);

    ShortestPath<Integer> path = new ShortestPath<Integer>(net1, ints.get(0));

    double val = path.getPathLength(ints.get(0));
    assertEquals(0.0, val);
    val = path.getPathLength(ints.get(1));
    assertEquals(2.0, val);
    val = path.getPathLength(ints.get(2));
    assertEquals(3.0, val);
    val = path.getPathLength(ints.get(3));
    assertEquals(1.0, val);
    val = path.getPathLength(ints.get(4));
    assertEquals(3.0, val);
    val = path.getPathLength(ints.get(5));
    assertEquals(6.0, val);
    val = path.getPathLength(ints.get(6));
    assertEquals(5.0, val);

    path = null;
    path = new ShortestPath<Integer>(net1, ints.get(5));
    val = path.getPathLength(ints.get(0)); // no path from 5 to 0
    assertEquals(Double.POSITIVE_INFINITY, val); // tests projection
    // listener code
    // edge from 5 to 0
    RepastEdge<Integer> edge = net1.addEdge(ints.get(5), ints.get(0));
    val = path.getPathLength(ints.get(0));
    assertEquals(1.0, val);

    // failing here!
    net1.removeEdge(edge);
    val = path.getPathLength(ints.get(0));
    // no path from 5 to 0
    assertEquals(Double.POSITIVE_INFINITY, val);
  }

  public void testNetworkAdjacent() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 20; i++) {
      context.add(i);
    }

    Network<Integer> undirNet = NetworkFactoryFinder.createNetworkFactory(
            null).createNetwork("net1", context, false);
    Network<Integer> dirNet = NetworkFactoryFinder.createNetworkFactory(
            null).createNetwork("net2", context, true);

    undirNet.addEdge(1, 2);
    undirNet.addEdge(3, 1);
    Set<Integer> edgeSet1 = new HashSet<Integer>();
    edgeSet1.add(2);
    edgeSet1.add(3);

    NetworkAdjacent<Integer> adj = new NetworkAdjacent<Integer>(undirNet, 1);
    for (Integer val : adj.query()) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    edgeSet1.add(3);
    for (Integer val : adj.query(edgeSet1)) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    dirNet.addEdge(1, 2);
    dirNet.addEdge(3, 1);

    edgeSet1.add(2);
    edgeSet1.add(3);

    adj = new NetworkAdjacent<Integer>(dirNet, 1);
    for (Integer val : adj.query()) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    edgeSet1.add(3);
    for (Integer val : adj.query(edgeSet1)) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(2);
    expected.add(2);
    expected.add(3);
    expected.add(3);

    adj = new NetworkAdjacent<Integer>(context, 1);
    for (Integer val : adj.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    expected.add(3);
    expected.add(3);
    for (Integer val : adj.query(expected)) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());
  }

  public void testNetworkSuccessor() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 20; i++) {
      context.add(i);
    }

    Network<Integer> undirNet = NetworkFactoryFinder.createNetworkFactory(
            null).createNetwork("net1", context, false);
    Network<Integer> dirNet = NetworkFactoryFinder.createNetworkFactory(
            null).createNetwork("net2", context, true);

    undirNet.addEdge(1, 2);
    undirNet.addEdge(3, 1);
    Set<Integer> edgeSet1 = new HashSet<Integer>();
    edgeSet1.add(2);
    edgeSet1.add(3);

    NetworkSuccessor<Integer> adj = new NetworkSuccessor<Integer>(undirNet,
            1);
    for (Integer val : adj.query()) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    edgeSet1.add(3);
    for (Integer val : adj.query(edgeSet1)) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    dirNet.addEdge(1, 2);
    dirNet.addEdge(3, 1);

    edgeSet1.add(2);

    adj = new NetworkSuccessor<Integer>(dirNet, 1);
    for (Integer val : adj.query()) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    for (Integer val : adj.query(edgeSet1)) {
      edgeSet1.add(val);
    }
    assertEquals(0, edgeSet1.size());

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(2);
    expected.add(2);
    expected.add(3);

    adj = new NetworkSuccessor<Integer>(context, 1);
    for (Integer val : adj.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    expected.add(3);
    for (Integer val : adj.query(expected)) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());
  }

  public void testNetworkPredecessor() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 20; i++) {
      context.add(i);
    }

    Network<Integer> undirNet = NetworkFactoryFinder.createNetworkFactory(
            null).createNetwork("net1", context, false);
    Network<Integer> dirNet = NetworkFactoryFinder.createNetworkFactory(
            null).createNetwork("net2", context, true);

    undirNet.addEdge(1, 2);
    undirNet.addEdge(3, 1);
    Set<Integer> edgeSet1 = new HashSet<Integer>();
    edgeSet1.add(2);
    edgeSet1.add(3);

    NetworkPredecessor<Integer> adj = new NetworkPredecessor<Integer>(
            undirNet, 1);
    for (Integer val : adj.query()) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    edgeSet1.add(3);
    for (Integer val : adj.query(edgeSet1)) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    dirNet.addEdge(1, 2);
    dirNet.addEdge(3, 1);

    edgeSet1.add(3);
    adj = new NetworkPredecessor<Integer>(dirNet, 1);
    for (Integer val : adj.query()) {
      assertTrue(edgeSet1.remove(val));
    }
    assertEquals(0, edgeSet1.size());

    for (Integer val : adj.query(edgeSet1)) {
      edgeSet1.add(val);
    }
    assertEquals(0, edgeSet1.size());

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(2);
    expected.add(3);
    expected.add(3);

    adj = new NetworkPredecessor<Integer>(context, 1);
    for (Integer val : adj.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    expected.add(2);
    for (Integer val : adj.query(expected)) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());
  }

  public void testLattice2D() {
    context.clear();
    for (int i = 0; i < 9; i++) {
      context.add(i);
    }

    Network<Integer> net1 = context.getProjection(Network.class, "Network 1");
    Network net2 = (Network) context.getProjection("Network 2");

    Lattice2DGenerator gen = new Lattice2DGenerator(false);
    net1 = gen.createNetwork(net1);

    assertEquals(24, net1.numEdges());

    int fourCount = 0, eightCount = 0, sixCount = 0;
    for (int i = 0; i < 9; i++) {
      int degree = net1.getDegree(i);
      if (degree == 4) fourCount++;
      else if (degree == 8) eightCount++;
      else if (degree == 6) sixCount++;
    }

    assertEquals(4, fourCount);
    assertEquals(1, eightCount);
    assertEquals(4, sixCount);

    gen = new Lattice2DGenerator(true);
    net2 = gen.createNetwork(net2);

    assertEquals(18, net2.numEdges());
    for (int i = 0; i < 9; i++) {
      assertEquals("node: " + i, 4, net2.getOutDegree(i));
    }
  }

  public void testLattice1D() {
    context.clear();
    for (int i = 0; i < 9; i++) {
      context.add(i);
    }

    Network<Integer> net1 = context.getProjection(Network.class, "Network 1");
    Network net2 = (Network) context.getProjection("Network 2");

    Lattice1DGenerator gen = new Lattice1DGenerator(false, true);
    net1 = gen.createNetwork(net1);

    assertEquals(16, net1.numEdges());

    int twoCount = 0, oneCount = 0;
    for (int i = 0; i < 9; i++) {
      int degree = net1.getOutDegree(i);
      if (degree == 2) twoCount++;
      else if (degree == 1) oneCount++;
    }

    assertEquals(7, twoCount);
    assertEquals(2, oneCount);

    context.clear();
    for (int i = 0; i < 9; i++) {
      context.add(i);
    }

    gen = new Lattice1DGenerator(false, false);
    net1 = gen.createNetwork(net1);

    assertEquals(8, net1.numEdges());

    twoCount = 0;
    oneCount = 0;
    int zeroCount = 0;
    for (int i = 0; i < 9; i++) {
      int degree = net1.getOutDegree(i);
      if (degree == 2) twoCount++;
      else if (degree == 1) oneCount++;
      else if (degree == 0) zeroCount++;
    }

    assertEquals(0, twoCount);
    assertEquals(8, oneCount);
    assertEquals(1, zeroCount);

    gen = new Lattice1DGenerator(true, false);
    net2 = gen.createNetwork(net2);

    assertEquals(9, net2.numEdges());
    for (int i = 0; i < 9; i++) {
      assertEquals("node: " + i, 2, net2.getDegree(i));
    }
  }

  public void testWattsBeta() {
    context.clear();
    for (int i = 0; i < 12; i++) {
      context.add(i);
    }

    Network<Integer> net1 = context.getProjection(Network.class, "Network 1");
    Network net2 = (Network) context.getProjection("Network 2");

    // test plain lattice with no re-wiring
    WattsBetaSmallWorldGenerator gen = new WattsBetaSmallWorldGenerator(0, 2, true);
    net1 = gen.createNetwork(net1);
    assertEquals(24, net1.numEdges());
    for (int i = 0; i < 12; i++) {
      assertTrue(!net1.isAdjacent(i, i));
      assertEquals(2, net1.getOutDegree(i));
      assertEquals(2, net1.getInDegree(i));
    }

    gen = new WattsBetaSmallWorldGenerator(0, 2, false);
    net2 = gen.createNetwork(net2);
    assertEquals(12, net2.numEdges());
    for (int i = 0; i < 12; i++) {
      assertTrue(!net2.isAdjacent(i, i));
      assertEquals(2, net2.getDegree(i));
    }

    context.clear();
    for (int i = 0; i < 12; i++) {
      context.add(i);
    }

    gen = new WattsBetaSmallWorldGenerator(0, 4, true);
    net1 = gen.createNetwork(net1);
    assertEquals(48, net1.numEdges());
    for (int i = 0; i < 12; i++) {
      assertTrue(!net1.isAdjacent(i, i));
      assertEquals(4, net1.getOutDegree(i));
      assertEquals(4, net1.getInDegree(i));
    }

    context.clear();
    for (int i = 0; i < 12; i++) {
      context.add(i);
    }

    gen = new WattsBetaSmallWorldGenerator(.25, 2, true);
    net1 = gen.createNetwork(net1);
    assertTrue(net1.getDegree() > 0);
    assertEquals(24, net1.numEdges());
    
    gen = new WattsBetaSmallWorldGenerator(.5, 4, true);
    net2 = gen.createNetwork(net2);
    assertTrue(net2.getDegree() > 0);
  }

  public void testRandomDensity() {
    context.clear();
    for (int i = 0; i < 100; i++) {
      context.add(i);
    }

    Network<Integer> net1 = context.getProjection(Network.class, "Network 1");
    Network net2 = (Network) context.getProjection("Network 2");

    RandomDensityGenerator gen = new RandomDensityGenerator(.5, false, true);
    net1 = gen.createNetwork(net1);
    double density = (2.0 * (net1.numEdges() / 2)) / (net1.size() * (net1.size() - 1));
    assertEquals(.5, density, .1);
    for (int i = 0; i < 100; i++) {
      assertTrue(!net1.isAdjacent(i, i));
    }

    gen = new RandomDensityGenerator(.3, false, false);
    net2 = gen.createNetwork(net2);
    density = (2.0 * net2.numEdges()) / (net2.size() * (net2.size() - 1));
    assertEquals(.3, density, .1);


    context.clear();
    for (int i = 0; i < 100; i++) {
      context.add(i);
    }
  }

  public void testNetworkDLLoader() throws IOException {
    Context<NodeAgent> context = new DefaultContext<NodeAgent>();
    NetworkBuilder builder = new NetworkBuilder("Network", context, true);
    builder.load("./repast.simphony.core/test/repast/simphony/graph/double_matrix.dl", NetworkFileFormat.DL,
            new NodeCreator() {
              public Object createNode(String label) {
                return new NodeAgent(label);
              }
            });
    Network net = builder.buildNetwork();

    assertEquals(9, context.size());
    assertEquals(19, net.getDegree());

    Map<String, NodeAgent> agentMap = new HashMap<String, NodeAgent>();
    for (NodeAgent agent : context) {
      agentMap.put(agent.getId(), agent);
    }

    // the expected adjacency matrix
    double[][] expected = {
            {0, 0, 120000.23, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 1, 1, 0},
            {0, -31123123.112, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 32343, 0, 0}
    };

    for (int i = 0; i < 9; i++) {
      NodeAgent agent = agentMap.get(String.valueOf(i));
      double[] vals = expected[i];
      int degree = 0;
      for (int j = 0; j < 9; j++) {
        if (vals[j] != 0) degree++;
      }

      for (int j = 0; j < 9; j++) {
        assertEquals(degree, net.getOutDegree(agent));
        GraphTest.NodeAgent target = agentMap.get(String.valueOf(j));
        RepastEdge edge = net.getEdge(agent, target);
        double weight = vals[j];
        if (weight == 0) {
          assertNull(edge);
        } else {
          assertNotNull(edge);
          assertEquals(weight, edge.getWeight());
        }
      }
    }
  }

  // tests first matrix in excel file
  public void testNetworkExcelLoader1() throws IOException {
    Context<NodeAgent> context = new DefaultContext<NodeAgent>();
    NetworkBuilder builder = new NetworkBuilder("Network", context, true);
    builder.load("./repast.simphony.core/test/repast/simphony/graph/matrices.xls", NetworkFileFormat.EXCEL,
            new NodeCreator() {
              public Object createNode(String label) {
                return new NodeAgent(label);
              }
            });
    Network net = builder.buildNetwork();

    assertEquals(5, context.size());
    assertEquals(12, net.getDegree());

    Map<String, NodeAgent> agentMap = new HashMap<String, NodeAgent>();
    for (NodeAgent agent : context) {
      agentMap.put(agent.getId(), agent);
    }

    // the expected adjacency matrix
    double[][] expected = {
            {0.00, 0.00, 1.00, 0.00, 1.00},
            {1.00, 0.00, 0.00, 1.00, 1.00},
            {1.00, 1.00, 0.00, 0.00, 1.00},
            {0.00, 2.00, 0.00, 0.00, 1.00},
            {1.20, 0.00, 0.00, 1.00, 0.00}};

    String[] nodeID = {"A", "B", "C", "D", "E"};

    for (int i = 0; i < nodeID.length; i++) {
      NodeAgent agent = agentMap.get(nodeID[i]);
      double[] vals = expected[i];
      int degree = 0;
      for (int j = 0; j < nodeID.length; j++) {
        if (vals[j] != 0) degree++;
      }

      for (int j = 0; j < nodeID.length; j++) {
        assertEquals(degree, net.getOutDegree(agent));
        GraphTest.NodeAgent target = agentMap.get(nodeID[j]);
        RepastEdge edge = net.getEdge(agent, target);
        double weight = vals[j];
        if (weight == 0) {
          assertNull(edge);
        } else {
          assertNotNull(edge);
          assertEquals(weight, edge.getWeight());
        }
      }
    }
  }

  // tests second matrix in excel file
  public void testNetworkExcelLoader2() throws IOException {
    Context<NodeAgent> context = new DefaultContext<NodeAgent>();
    NetworkBuilder builder = new NetworkBuilder("Network", context, true);
    builder.load("./repast.simphony.core/test/repast/simphony/graph/matrices.xls", NetworkFileFormat.EXCEL,
            new NodeCreator() {
              public Object createNode(String label) {
                return new NodeAgent(label);
              }
            }, 1);
    Network net = builder.buildNetwork();

    assertEquals(5, context.size());
    assertEquals(12, net.getDegree());

    Map<String, NodeAgent> agentMap = new HashMap<String, NodeAgent>();
    for (NodeAgent agent : context) {
      agentMap.put(agent.getId(), agent);
    }

    // the expected adjacency matrix
    double[][] expected = {
            {0.00, 0.00, 1.00, 0.00, 1.00},
            {1.00, 0.00, 0.00, 1.00, 1.00},
            {1.00, 1.00, 0.00, 0.00, 1.00},
            {0.00, 2.00, 0.00, 0.00, 1.00},
            {1.20, 0.00, 0.00, 1.00, 0.00}};

    String[] nodeID = {"0", "1", "2", "3", "4"};

    for (int i = 0; i < nodeID.length; i++) {
      NodeAgent agent = agentMap.get(nodeID[i]);
      double[] vals = expected[i];
      int degree = 0;
      for (int j = 0; j < nodeID.length; j++) {
        if (vals[j] != 0) degree++;
      }

      for (int j = 0; j < nodeID.length; j++) {
        assertEquals(nodeID[i], degree, net.getOutDegree(agent));
        GraphTest.NodeAgent target = agentMap.get(nodeID[j]);
        RepastEdge edge = net.getEdge(agent, target);
        double weight = vals[j];
        if (weight == 0) {
          assertNull(edge);
        } else {
          assertNotNull(edge);
          assertEquals(weight, edge.getWeight());
        }
      }
    }

  }

  public static junit.framework.Test suite() {
    return new TestSuite(GraphTest.class);
  }
}
