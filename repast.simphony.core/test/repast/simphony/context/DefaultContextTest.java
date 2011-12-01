package repast.simphony.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.PredicateUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.jmock.MockObjectTestCase;

import repast.simphony.engine.environment.RunState;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.space.projection.ProjectionPredicate;
import repast.simphony.util.collections.IndexedIterable;
import simphony.util.messages.MessageCenter;
import simphony.util.messages.MessageEvent;
import simphony.util.messages.MessageEventListener;

public class DefaultContextTest extends MockObjectTestCase {

  static {
    BasicConfigurator.configure();
  }

  Context<Object> parent;

  Context<String> child1;

  Context<Integer> child2;

  /**
   *
   */
  public DefaultContextTest() {
    super();
  }

  /**
   * @param name
   */
  public DefaultContextTest(String name) {
    super(name);
  }

  public void setUp() {
    RunState.init(null, null, null);
    parent = new DefaultContext<Object>("parent");
    child1 = new DefaultContext<String>("child1");
    child2 = new DefaultContext<Integer>("child2");
    parent.addSubContext(child1);
    parent.addSubContext(child2);
    child1.add("Hello");
    child2.add(10);
    parent.add("GoodBye");
  }

  class TestListener implements ContextListener {

    public Context subContext;
    public Context context;

    /**
     * Called to nofify the listener of a change to a context.
     * 
     * @param ev
     *          The event of which to notify the listener.
     */
    public void eventOccured(ContextEvent ev) {
      subContext = ev.getSubContext();
      context = ev.getContext();
    }
  }

  public void testSubContextListeners() {
    TestListener listener = new TestListener();
    parent.addContextListener(listener);
    Context<Integer> subContext = new DefaultContext<Integer>();
    parent.addSubContext(subContext);
    assertEquals(subContext, listener.subContext);
    assertEquals(parent, listener.context);

    parent.removeSubContext(child1);
    assertEquals(child1, listener.subContext);
    assertEquals(parent, listener.context);
  }

  /*
   * Test method for 'repast.context.DefaultContext.size()'
   */
  public void testSize() {
    assertEquals(3, parent.size());
    assertEquals(1, child1.size());
    assertEquals(1, child2.size());
    child1.add("Maybe");
    assertEquals(4, parent.size());
    assertEquals(2, child1.size());
  }

  /*
   * Test method for 'repast.context.DefaultContext.iterator()'
   */
  public void testIterator() {
    Iterator<Object> iter = parent.iterator();
    assertEquals("GoodBye", iter.next());
    assertEquals("Hello", iter.next());
    assertEquals(10, iter.next());
    Iterator<String> stringIter = child1.iterator();
    assertEquals((Object) "Hello", (Object) stringIter.next());
    Iterator<Integer> intIter = child2.iterator();
    assertEquals(new Integer(10), intIter.next());
  }

  /*
   * Test method for 'repast.context.DefaultContext.contains(Object)'
   */
  public void testContainsObject() {
    assertTrue(parent.contains("Hello"));
    assertTrue(parent.contains("GoodBye"));
    assertTrue(parent.contains(10));
    assertTrue(child1.contains("Hello"));
    assertTrue(child2.contains(10));
    assertTrue(!child1.contains(10));
    assertTrue(!child2.contains("Hello"));
  }

  /*
   * Test method for 'repast.context.AbstractContext.getId()'
   */
  public void testGetId() {
    assertEquals("parent", parent.getId());
    assertEquals("child1", child1.getId());
    assertEquals("child2", child2.getId());
    Context<Object> c = new DefaultContext<Object>("Context");
    assertTrue(((String) c.getId()).startsWith("Context"));
  }

  class SSTestListener implements ContextListener {

    boolean added = false;

    public void eventOccured(ContextEvent ev) {
      if (ev.getType() == ContextEvent.EventType.AGENT_ADDED)
        added = true;
    }
  }

  public void testSetSemantic() {
    Context<Integer> context = new DefaultContext<Integer>();
    SSTestListener listener = new SSTestListener();
    context.addContextListener(listener);
    assertTrue(context.add(0));
    assertTrue(context.add(1));
    listener.added = false;

    // should return false because 0
    // already added
    assertFalse(context.add(0));

    // listener.added should still be false
    // as the add failed.
    assertFalse(listener.added);

    // size should be 2
    assertEquals(2, context.size());

    int count = 0;
    Set<Integer> expected = new HashSet<Integer>();
    expected.add(0);
    expected.add(1);
    for (Integer val : context) {
      assertTrue(expected.remove(val));
      count++;
    }
    assertEquals(0, expected.size());
    assertEquals(2, count);

    count = 0;
    expected.add(0);
    expected.add(1);
    for (Integer val : context.getObjects(Integer.class)) {
      count++;
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());
    assertEquals(2, count);
  }

  /*
   * Test method for 'repast.context.AbstractContext.setId(Object)'
   */
  public void testSetId() {
    parent.setId("My Context");
    assertEquals("My Context", parent.getId());
  }

  /*
   * Test method for 'repast.context.AbstractContext.query(UnaryPredicate<T>)'
   */
  public void testQuery() {
    Iterator<Object> iter = parent.query(PredicateUtils.instanceofPredicate(String.class))
        .iterator();
    assertEquals("GoodBye", iter.next());
    assertEquals("Hello", iter.next());
    assertTrue(!iter.hasNext());
  }

  class CountingListener implements ContextListener<Object> {
    int objectsRemoved = 0;
    int objectsAdded = 0;

    public void eventOccured(ContextEvent<Object> ev) {
      if (ev.getType() == ContextEvent.ADDED) {
        objectsAdded++;
      } else {
        objectsRemoved++;
      }
    }
  }

  /*
   * Test method for
   * 'repast.context.AbstractContext.removeContextListener(ContextListener<T>)'
   */
  public void testContextListener() {
    
    CountingListener listener = new CountingListener();
    parent.addContextListener(listener);
    parent.add("Foo");
    assertEquals(1, listener.objectsAdded);
    parent.remove("Foo");
    assertEquals(1, listener.objectsRemoved);
    parent.removeContextListener(listener);
    parent.add("Foo");
    assertEquals(1, listener.objectsAdded);
    parent.remove("Foo");
    assertEquals(1, listener.objectsRemoved);
  }

  public void testContextListenerAllTests() {
    parent.removeSubContext(child1);
    parent.removeSubContext(child2);
    parent.remove(parent.iterator().next());


    CountingListener listener = new CountingListener();
    parent.addContextListener(listener);
    String[] objs = { "Foo", "Bar", "Baz" };
    // need to use ArrayList so we can call remove on it below
    List<String> all = new ArrayList<String>(Arrays.asList(objs));
    assertEquals(0, parent.size());
    parent.addAll(all);

    assertEquals(3, listener.objectsAdded);
    assertEquals(3, parent.size());

    parent.removeAll(all);
    assertEquals(3, listener.objectsRemoved);
    assertEquals(0, parent.size());
    
    listener.objectsAdded = 0;
    listener.objectsRemoved = 0;
    parent.addAll(all);
    
    assertEquals(3, listener.objectsAdded);
    assertEquals(3, parent.size());
    
    all.remove(0);
    parent.retainAll(all);
    assertEquals(1, listener.objectsRemoved);
    assertEquals(2, parent.size());
    
    for (Object obj : parent) {
      assertTrue(all.remove(obj));
    }
    assertEquals(0, all.size());
    
  }

  /*
   * Test method for 'repast.context.AbstractContext.getSubContexts()'
   */
  public void testGetSubContexts() {
    Iterator<Context<? extends Object>> iter = parent.getSubContexts().iterator();
    assertEquals(child1, iter.next());
    assertEquals(child2, iter.next());
    assertTrue(!iter.hasNext());
    Iterator<Context<? extends String>> stringIter = child1.getSubContexts().iterator();
    assertTrue(!stringIter.hasNext());
  }

  /*
   * Test method for 'repast.context.AbstractContext.removeSubContext(Context<?
   * extends T>)'
   */
  public void testRemoveSubContext() {
    parent.removeSubContext(child1);
    assertEquals(null, parent.getSubContext("child1"));
    child1.removeSubContext(new DefaultContext<String>());
  }

  /*
   * Test method for 'repast.context.AbstractContext.remove(Object)'
   */
  public void testRemoveObject() {
    assertTrue(parent.remove("Hello"));
    assertTrue(!parent.contains("Hello"));
    assertTrue(!child1.contains("Hello"));
  }

  /*
   * Test method for 'repast.context.AbstractContext.getSubContext(Object)'
   */
  public void testGetSubContext() {
    assertEquals(child1, parent.getSubContext("child1"));
    assertEquals(null, child1.getSubContext("child3"));
  }

  public void testGetObjects() {
    /*
     * child1.add("Hello"); child2.add(10); parent.add("GoodBye");
     */
    for (int i = 20; i < 30; i++) {
      child2.add(i);
      child1.add(String.valueOf(i));
    }

    parent.add("Foo");
    parent.add("Bar");

    Set set = new HashSet();
    for (Object obj : parent) {
      set.add(obj);
    }
    assertEquals(25, set.size());

    IndexedIterable iter = parent.getObjects(Object.class);
    assertEquals(25, iter.size());
    for (Object obj : iter) {
      assertTrue(set.remove(obj));
    }
    assertEquals(0, set.size());

    for (int i = 20; i < 30; i++) {
      set.add(i);
    }
    set.add(10);

    iter = parent.getObjects(Integer.class);
    for (Object obj : iter) {
      assertTrue(set.remove(obj));
    }
    assertEquals(0, set.size());

    iter = parent.getObjects(List.class);
    assertEquals(0, iter.size());

    for (String str : child1) {
      set.add(str);
    }

    iter = child1.getObjects(String.class);
    for (Object obj : iter) {
      assertTrue(set.remove(obj));
    }
    assertEquals(0, set.size());
  }

  public void testGetRandomObject() {
    for (int i = 20; i < 30; i++) {
      child2.add(i);
      child1.add(String.valueOf(i));
    }

    parent.add("Foo");
    parent.add("Bar");

    Set set = new HashSet();
    for (Object obj : parent) {
      set.add(obj);
    }

    for (int i = 0; i < 200; i++) {
      set.contains(parent.getRandomObject());
    }
  }

  public void testGetRandomObjects() {
    for (int i = 20; i < 30; i++) {
      child2.add(i);
      child1.add(String.valueOf(i));
    }

    parent.add("Foo");
    parent.add("Bar");

    Set set = new HashSet();
    for (Object obj : parent) {
      set.add(obj);
    }

    Set pickedObjs = new HashSet();
    Iterable iter = parent.getRandomObjects(Object.class, 3);
    int count = 0;
    for (Object obj : iter) {
      pickedObjs.add(obj);
      assertTrue(set.remove(obj));
      count++;
    }
    assertEquals(3, count);

    set.clear();
    iter = parent.getRandomObjects(Object.class, 3);
    for (Object obj : iter) {
      set.add(obj);
    }

    // this may fail occasionally, but it should be unlikely that we draw
    // the same 3 every time.
    assertTrue(!pickedObjs.containsAll(set));
  }

  // tests for duplicate projection adding
  public void testAddProjection() {
    TestProjection projection = new TestProjection();
    parent.addProjection(projection);
    assertEquals(projection, parent.getProjection(projection.getName()));

    MSEL msel = new MSEL();
    MessageCenter.addMessageListener(msel);

    // should throw an exception
    System.out.println("!!!!EXCEPTION IS EXPECTED!!!!");
    parent.addProjection(projection);

    assertNotNull(msel.event);
    assertEquals(Level.ERROR, msel.event.getLevel());
  }

  class MSEL implements MessageEventListener {

    MessageEvent event = null;

    public void messageReceived(MessageEvent event) {
      this.event = event;
    }
  }

  class TestProjection implements Projection {

    public void addProjectionListener(ProjectionListener listener) {
      // TODO Auto-generated method stub
    }

    public boolean evaluate(ProjectionPredicate predicate) {
      // TODO Auto-generated method stub
      return false;
    }

    public String getName() {
      return "Test Projection";
    }

    public Collection getProjectionListeners() {
      // TODO Auto-generated method stub
      return null;
    }

    public boolean removeProjectionListener(ProjectionListener listener) {
      // TODO Auto-generated method stub
      return false;
    }

  }

  class Cell extends DefaultContext {

  }

  class TestTube extends DefaultContext<Cell> {
  }

  public void testContextAsAgent() {
    Context<Object> top = new DefaultContext<Object>();
    Context<Cell> tube = new TestTube();
    for (int i = 0; i < 4; i++) {
      Cell cell = new Cell();
      tube.add(cell);
      tube.addSubContext(cell);
    }

    top.addSubContext(tube);
    top.add(tube);

    IndexedIterable iter = top.getObjects(TestTube.class);
    assertEquals(1, iter.size());

    assertEquals(tube, iter.get(0));
    assertEquals(tube, iter.iterator().next());

    iter = top.getObjects(Cell.class);
    assertEquals(4, iter.size());
  }
}
