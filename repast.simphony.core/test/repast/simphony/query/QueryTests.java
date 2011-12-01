package repast.simphony.query;

import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;

import java.util.*;

/**
 * Query tests.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class QueryTests extends TestCase {

  static class Agent {

    private int intVal;
    private String name;

    public Agent(int intVal, String name) {
      this.intVal = intVal;
      this.name = name;
    }

    public int getIntVal() {
      return intVal;
    }

    public String getName() {
      return name;
    }

    public String toString() {
      return name;
    }
  }

  static class Agent2 extends Agent {

    private String foo;

    public Agent2(int intVal, String name, String foo) {
      super(intVal, name);
      this.foo = foo;
    }

    public String getFoo() {
      return foo;
    }

    public void setFoo(String val) {
      foo = val;
    }

    public String toString() {
      return super.toString() + " - " + foo;
    }
  }

  public void testInstanceOfQuery() {
    Context<Agent> agents = new DefaultContext<Agent>();
    Set<Agent> agentSet = new HashSet<Agent>();
    Set<Agent> agent2Set = new HashSet<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentSet.add(agent);
    }

    Agent2 agent2 = null;
    for (int i = 0; i < 10; i++) {
      agent2 = new Agent2(i, "name: " + i, "agent2");
      agents.add(agent2);
      agentSet.add(agent2);
      agent2Set.add(agent2);
    }

    Query<Agent> query = new InstanceOfQuery<Agent>(agents, Agent.class);
    for (Agent agent : query.query()) {
      assertTrue(agentSet.remove(agent));
    }
    assertEquals(0, agentSet.size());

    query = new InstanceOfQuery<Agent>(agents, Agent2.class);
    for (Agent agent : query.query()) {
      assertTrue(agent2Set.remove(agent));
    }
    assertEquals(0, agent2Set.size());


    agent2Set.add(agent2);

    query = new InstanceOfQuery<Agent>(agents, Agent2.class);
    Iterator<Agent> iter = query.query(agent2Set).iterator();
    assertTrue(iter.hasNext());
    assertEquals(agent2, iter.next());
    assertTrue(!iter.hasNext());
  }

  public void testPropertyLessThan() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }

    PropertyLessThan<Agent> equals = new PropertyLessThan<Agent>(agents, "intVal", 2);
    Set<Object> expected = new HashSet<Object>();
    expected.add(agentList.get(0));
    expected.add(agentList.get(1));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyLessThan<Agent>(agents, "intVal", -3);
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }

  public void testPropertyLessThanEquals() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }

    PropertyLessThanEquals<Agent> equals = new PropertyLessThanEquals<Agent>(agents, "intVal", 2);
    Set<Object> expected = new HashSet<Object>();
    expected.add(agentList.get(0));
    expected.add(agentList.get(1));
    expected.add(agentList.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyLessThanEquals<Agent>(agents, "intVal", -3);
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }

  public void testPropertyGreaterThan() {
    Context<Agent> agents = new DefaultContext<Agent>();
    PropertyGreaterThan<Agent> equals = new PropertyGreaterThan<Agent>(agents, "intVal", 7);

    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }


    Set<Object> expected = new HashSet<Object>();
    expected.add(agentList.get(8));
    expected.add(agentList.get(9));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyGreaterThan<Agent>(agents, "intVal", 210.32343);
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }

  public void testPropertyGreaterThanEquals() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }

    PropertyGreaterThanEquals<Agent> equals = new PropertyGreaterThanEquals<Agent>(agents, "intVal", 7);
    Set<Object> expected = new HashSet<Object>();
    expected.add(agentList.get(7));
    expected.add(agentList.get(8));
    expected.add(agentList.get(9));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyGreaterThanEquals<Agent>(agents, "intVal", 210.32343);
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }


  public void testNotQuery() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }


    AndQuery<Agent> andQuery = new AndQuery<Agent>(new PropertyEquals<Agent>(agents, "name", "name: 2"),
            new PropertyGreaterThan<Agent>(agents, "intVal", 1));
    NotQuery<Agent> query = new NotQuery<Agent>(agents, andQuery);
    Set<Agent> expected = new HashSet<Agent>();
    expected.addAll(agentList);
    expected.remove(agentList.get(2));
    for (Agent agent : query.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    expected.add(agentList.get(1));
    for (Agent agent : query.query(expected)) {
      assertTrue(expected.remove(agent));
    }

    assertEquals(0, expected.size());
  }

  public void testAndQuery() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }


    AndQuery<Agent> query = new AndQuery<Agent>(new PropertyEquals<Agent>(agents, "name", "name: 2"),
            new PropertyGreaterThan<Agent>(agents, "intVal", 1));
    Set<Agent> expected = new HashSet<Agent>();
    expected.add(agentList.get(2));
    for (Agent agent : query.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    for (Agent agent : query.query(expected)) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }

  public void testOrQuery() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
    }

    OrQuery<Agent> query = new OrQuery<Agent>(new PropertyEquals<Agent>(agents, "name", "name: 2"),
            new PropertyLessThan<Agent>(agents, "intVal", 3));
    List<Agent> expected = new ArrayList<Agent>();
    // note that 2 in list only once, although it is returned
    // both of the or query sub queries.
    expected.add(agentList.get(2));
    expected.add(agentList.get(1));
    expected.add(agentList.get(0));
    for (Agent agent : query.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    expected.add(agentList.get(0));
    for (Agent agent : query.query(expected)) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }

  public void testPropertyEquals() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    List<Agent2> agent2List = new ArrayList<Agent2>();
    for (int i = 0; i < 10; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
      Agent2 agent2 = new Agent2(i, "name: " + i, "foo: " + i);
      agent2List.add(agent2);
      agents.add(agent2);
    }


    PropertyEquals<Agent> equals = new PropertyEquals<Agent>(agents, "name", "name: 2");
    Set<Object> expected = new HashSet<Object>();
    expected.add(agentList.get(2));
    expected.add(agent2List.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyEquals<Agent>(agents, "name", "name: 2",
            new PropertyEqualsPredicate() {
              public boolean evaluate(Object o, Object o1) {
                return o.equals(o1);
              }
            });
    expected = new HashSet<Object>();
    expected.add(agentList.get(2));
    expected.add(agent2List.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyEquals<Agent>(agents, "intVal", 2);
    expected = new HashSet<Object>();
    expected.add(agentList.get(2));
    expected.add(agent2List.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyEquals<Agent>(agents, "intVal", 234324);
    expected = new HashSet<Object>();
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());


    equals = new PropertyEquals<Agent>(agents, "foo", "foo: 2");
    expected = new HashSet<Object>();
    //expected.add(agentList.get(2));
    expected.add(agent2List.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyEquals<Agent>(agents, "foo", null);
    expected = new HashSet<Object>();
    //expected.add(agentList.get(2));
    agent2List.get(2).setFoo(null);
    expected.add(agent2List.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }

  public void testPropertyNotEquals() {
    Context<Agent> agents = new DefaultContext<Agent>();
    List<Agent> agentList = new ArrayList<Agent>();
    List<Agent2> agent2List = new ArrayList<Agent2>();
    for (int i = 0; i < 3; i++) {
      Agent agent = new Agent(i, "name: " + i);
      agents.add(agent);
      agentList.add(agent);
      Agent2 agent2 = new Agent2(i, "name: " + i, "foo: " + i);
      agent2List.add(agent2);
      agents.add(agent2);
    }


    PropertyNotEquals<Agent> equals = new PropertyNotEquals<Agent>(agents, "name", "name: 2");
    Set<Object> expected = new HashSet<Object>();
    expected.add(agentList.get(0));
    expected.add(agentList.get(1));
    expected.add(agent2List.get(0));
    expected.add(agent2List.get(1));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyNotEquals<Agent>(agents, "intVal", 2);
    expected = new HashSet<Object>();
    expected.add(agentList.get(0));
    expected.add(agentList.get(1));
    expected.add(agent2List.get(0));
    expected.add(agent2List.get(1));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyNotEquals<Agent>(agents, "intVal", 234324);
    expected = new HashSet<Object>();
    expected.add(agentList.get(0));
    expected.add(agentList.get(1));
    expected.add(agentList.get(2));
    expected.add(agent2List.get(0));
    expected.add(agent2List.get(1));
    expected.add(agent2List.get(2));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());


    equals = new PropertyNotEquals<Agent>(agents, "foo", "foo: 2");
    expected = new HashSet<Object>();
    //expected.add(agentList.get(2));
    expected.add(agent2List.get(0));
    expected.add(agent2List.get(1));
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());

    equals = new PropertyNotEquals<Agent>(agents, "foo", null);
    expected = new HashSet<Object>();
    //expected.add(agentList.get(2));
    //agent2List.get(2).setFoo(null);
    for (Agent agent : equals.query()) {
      assertTrue(expected.remove(agent));
    }
    assertEquals(0, expected.size());
  }
}
