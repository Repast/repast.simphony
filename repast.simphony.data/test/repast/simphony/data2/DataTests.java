package repast.simphony.data2;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.data2.engine.DataInitActionCreator;
import repast.simphony.data2.engine.DataSetComponentControllerAction;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.data2.engine.DataSetsActionCreator;
import repast.simphony.data2.engine.FileSinkComponentControllerAction;
import repast.simphony.data2.engine.FileSinkDescriptor;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultGUIRegistry;
import repast.simphony.engine.environment.DefaultScheduleRegistry;
import repast.simphony.engine.environment.RunInfo;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.DefaultScheduleFactory;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.DefaultParameters;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;

public class DataTests {

  class Sink implements DataSink {

    boolean opened = false, closed = false;
    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

    @Override
    public void open(List<String> ids) {
      opened = true;
    }

    @Override
    public void rowStarted() {
      items.add(new HashMap<String, Object>());
    }

    @Override
    public void append(String key, Object value) {
      items.get(items.size() - 1).put(key, value);
    }

    @Override
    public void rowEnded() {
    }
    
    public void flush() {
      
    }

    @Override
    public void close() {
      closed = true;
    }

    /* (non-Javadoc)
     * @see repast.simphony.data2.DataSink#recordEnded()
     */
    @Override
    public void recordEnded() {}
  }
  
  
  private int[][] data = { { 234, 23, 14 }, { 53, 31, 100 }, { 12, 0, -2 } };
  

  @Test
  public void testMethodDS() {
    ObjectA a = new ObjectA();
    MethodDataSource source = new MethodDataSource("double", ObjectA.class,
        "getDouble");
    assertEquals(a.getDouble(), source.get(a));

    ObjectB b = new ObjectB();
    source = new MethodDataSource("double", ObjectB.class, "getDouble");
    assertEquals(b.getDouble(), source.get(b));

    source = new MethodDataSource("double", ObjectA.class, "getDouble");
    assertEquals(b.getDouble(), source.get(b));

    try {
      source = new MethodDataSource("foo", ObjectA.class, "foo");
      fail();
    } catch (DataException ex) {
    }

    MethodDataSource osource = new MethodDataSource("object", ObjectB.class,
        "object");
    assertEquals(b.object(), osource.get(b));

    try {
      osource.get(a);
      fail();
    } catch (DataException ex) {
    }
  }
  
  @SuppressWarnings("rawtypes")
  private Map<Class<?>, SizedIterable<?>> createRecordMap(final List<?> list, Class<?> clazz) {
    Map<Class<?>, SizedIterable<?>> map = new HashMap<Class<?>, SizedIterable<?>>();
    map.put(clazz, new SizedIterable() {
      
      @Override
      public Iterator iterator() {
        return list.iterator();
      }

      @Override
      public int size() {
        return list.size();
      }
    });
    
    return map;
  }

  @Test
  public void testNonAggregate() {
    List<Object> objs = new ArrayList<Object>();
    objs.add(new ObjectB());
    objs.add(new ObjectB());
    objs.add(new ObjectB());

    List<NonAggregateDataSource> sources = new ArrayList<NonAggregateDataSource>();
    sources.add(new MethodDataSource("double", ObjectB.class, "getDouble"));
    sources.add(new MethodDataSource("object", ObjectB.class, "object"));
    sources.add(new MethodDataSource("int", ObjectB.class, "getInt"));

    List<Sink> sinks = new ArrayList<Sink>();
    sinks.add(new Sink());
    sinks.add(new Sink());

    DataSet dataSet = new NonAggregateDataSet("ds1", sources, sinks);
    dataSet.init();
    for (int i = 0; i < 3; i++) {
      dataSet.record(createRecordMap(objs, ObjectB.class));
    }
    dataSet.close();

    for (Sink sink : sinks) {
      assertTrue(sink.opened);
      assertTrue(sink.closed);

      Set<Object> expected = new HashSet<Object>();

      // 3 objs x 3 records
      assertEquals(sink.items.size(), (9));
      for (Map<String, Object> map : sink.items) {
        assertEquals(map.size(), (3));
        assertTrue(map.containsKey("double"));
        assertTrue(map.containsKey("object"));
        assertTrue(map.containsKey("int"));
        assertEquals((Double) map.get("double"), (3.14), .0001);
        assertEquals(((Integer) map.get("int")).intValue(), (3));
        assertEquals((String) map.get("object"), ("hello"));

        assertEquals(expected.size(), (0));
      }
    }
  }
  
  @Test
  public void testAggregateDSBoolObj() {
    List<ObjectC> objs = new ArrayList<ObjectC>();
    ObjectC obj = new ObjectC();
    obj.val = true;
    objs.add(obj);
    objs.add(new ObjectC());
    objs.add(new ObjectC());
    
    AggregateDSCreator creator = new AggregateDSCreator(new MethodDataSource("obj", ObjectC.class, "getObj"));
    AggregateDataSource ds = creator.createSumSource("sum");
    ds.reset();
    Double val = (Double)ds.get(objs, objs.size());
    assertEquals(9, val, 0);
    
    creator = new AggregateDSCreator(new MethodDataSource("bool", ObjectC.class, "getBool"));
    ds = creator.createSumSource("sum");
    val = (Double)ds.get(objs, objs.size());
    assertEquals(1.0, val, 0);
    
    /*
    creator = new AggregateDSCreator(new MethodDataSource("hello", ObjectC.class, "getString"));
    ds = creator.createSumSource("sum");
    try {
      val = (Double)ds.get(objs, objs.size());
      fail("Should have thrown class cast exception");
    } catch (ClassCastException ex) {}
    */
  }

  @Test
  public void testAggregateDS() {
    List<ObjectB> objs = new ArrayList<ObjectB>();
    objs.add(new ObjectB());
    objs.add(new ObjectB());
    objs.add(new ObjectB());

    List<Sink> sinks = new ArrayList<Sink>();
    sinks.add(new Sink());
    sinks.add(new Sink());

    List<AggregateDataSource> sources = new ArrayList<AggregateDataSource>();
    AggregateDSCreator creator = new AggregateDSCreator(new MethodDataSource("double",
        ObjectB.class, "getDouble"));
    sources.add(creator.createSumSource("double_sum"));
    sources.add(creator.createMeanSource("double_mean"));
    sources.add(creator.createMinSource("double_min"));
    sources.add(creator.createMaxSource("double_max"));

    creator = new AggregateDSCreator(new MethodDataSource("int", ObjectB.class, "getInt"));
    sources.add(creator.createSumSource("int_sum"));
    sources.add(creator.createMeanSource("int_mean"));

    DataSet dataSet = new AggregateDataSet("ds1", sources, sinks);
    dataSet.init();
    SummaryStatistics stats = new SummaryStatistics();

    double expected[][] = new double[3][];

    for (int i = 0; i < 3; i++) {
      stats.clear();
      objs.get(0).setDouble(data[i][0]);
      stats.addValue(data[i][0]);
      objs.get(1).setDouble(data[i][1]);
      stats.addValue(data[i][1]);
      objs.get(2).setDouble(data[i][2]);
      stats.addValue(data[i][2]);

      objs.get(0).setInt(data[i][0]);
      objs.get(1).setInt(data[i][1]);
      objs.get(2).setInt(data[i][2]);

      dataSet.record(createRecordMap(objs,  ObjectB.class));
      expected[i] = new double[] { stats.getSum(), stats.getMean(), stats.getMin(), stats.getMax() };
    }
    dataSet.close();

    for (Sink sink : sinks) {
      assertEquals(sink.opened, true);
      assertEquals(sink.closed, true);

      assertEquals(sink.items.size(), (3));
      int i = 0;
      for (Map<String, Object> map : sink.items) {
        assertEquals(map.size(), (6));
        assertEquals(map.containsKey("double_sum"), (true));
        assertEquals(map.containsKey("double_min"), (true));
        assertEquals(map.containsKey("double_max"), (true));
        assertEquals(map.containsKey("double_mean"), (true));
        assertEquals(map.containsKey("int_sum"), (true));
        assertEquals(map.containsKey("int_mean"), (true));

        assertEquals((Double) map.get("double_sum"), (expected[i][0]), 0);
        assertEquals((Double) map.get("double_mean"), (expected[i][1]), 0);
        assertEquals((Double) map.get("double_min"), (expected[i][2]), 0);
        assertEquals((Double) map.get("double_max"), (expected[i][3]), 0);

        assertEquals(((Number) map.get("int_sum")).doubleValue(), (expected[i][0]), 0);
        assertEquals(((Number) map.get("int_mean")).doubleValue(), (expected[i][1]), 0);
        i++;
      }
    }
  }

  @Test
  public void testFormatter() {

    List<NonAggregateDataSource> sources = new ArrayList<NonAggregateDataSource>();
    sources.add(new MethodDataSource("double", ObjectB.class, "getDouble"));
    sources.add(new MethodDataSource("object", ObjectB.class, "object"));
    sources.add(new MethodDataSource("int", ObjectB.class, "getInt"));

    TabularFormatter formatter = new TabularFormatter(sources, ",");
    assertEquals(formatter.getHeader(), ("\"double\",\"object\",\"int\""));

    formatter.addData("double", 3.14);
    formatter.addData("object", "Hello");
    formatter.addData("int", 10);

    assertEquals(formatter.formatData(), ("3.14,\"Hello\",10"));

    formatter.clear();
    formatter.addData("int", 1000);
    formatter.addData("object", "Goodbye");
    formatter.addData("double", 5.14);

    assertEquals(formatter.formatData(), ("5.14,\"Goodbye\",1000"));
  }

  @Test
  public void testFileSink() {

    List<NonAggregateDataSource> sources = new ArrayList<NonAggregateDataSource>();
    sources.add(new MethodDataSource("double", ObjectB.class, "getDouble"));
    sources.add(new MethodDataSource("object", ObjectB.class, "object"));
    sources.add(new MethodDataSource("int", ObjectB.class, "getInt"));

    File file = new File("./test_output/file_sink_test.txt");
    FileDataSink sink = new FileDataSink("fs1", file, new TabularFormatter(sources, ","));

    sink.open(new ArrayList<String>());
    sink.rowStarted();
    sink.append("double", 3.14);
    sink.append("object", "hello");
    sink.append("int", 1000);
    sink.rowEnded();
    sink.rowStarted();
    sink.append("int", 103);
    sink.append("double", 2.14);
    sink.append("object", "bug");
    sink.rowEnded();
    sink.close();

    String[] expected = { "\"double\",\"object\",\"int\"", "3.14,\"hello\",1000",
        "2.14,\"bug\",103" };

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line = null;
      int i = 0;
      while ((line = reader.readLine()) != null) {
        assertEquals(expected[i], (line));
        i++;
      }
      assertEquals(i, (3));
    } catch (IOException ex) {
      fail();
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (IOException ex) {
      }
    }
  }

  private RunState createRunState(int runNum, RunState runState) {
    if (runState == null) {
      runState = RunState.init(new RunInfo("test", runNum, 1), new DefaultScheduleRegistry(),
          new DefaultGUIRegistry());
    } else {
      runState.getRunInfo().setRunNumber(runNum);
    }
    ISchedule schedule = new DefaultScheduleFactory().createSchedule();
    // need a dummy action so that schedule will iterate
    schedule.schedule(ScheduleParameters.createRepeating(1, 1), new IAction() {
      @Override
      public void execute() {
      }
    });
    runState.getScheduleRegistry().setModelSchedule(schedule);
    return runState;
  }

  private Parameters createParameters() {
    DefaultParameters params = new DefaultParameters();
    params.addParameter(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME,
        ParameterConstants.DEFAULT_RANDOM_SEED_DISPLAY_NAME, Integer.class, Integer.valueOf(1),
        false);
    return params;
  }

  private List<ObjectB> createObjs() {
    List<ObjectB> objs = new ArrayList<ObjectB>();
    objs.add(new ObjectB("A"));
    objs.add(new ObjectB("B"));
    objs.add(new ObjectB("C"));
    return objs;
  }
  
  @Test
  public void testAction() throws IOException {
    DataSetDescriptor descriptor = new DataSetDescriptor("ds1", DataSetType.NON_AGGREGATE);
    descriptor.setIncludeRandomSeed(true);
    descriptor.setIncludeTick(true);
    descriptor.addMethodDataSource("double", ObjectB.class.getName(), "getDouble");
    descriptor.addMethodDataSource("object", ObjectB.class.getName(), "object");
    descriptor.addMethodDataSource("int", ObjectB.class.getName(), "getInt");
    descriptor.setScheduleParameters(ScheduleParameters.createRepeating(1, 1));
    descriptor.setSourceType(Object.class.getName());
    
    DataSetComponentControllerAction dsAction = new DataSetComponentControllerAction(descriptor);
    
    FileSinkDescriptor fdesc = new FileSinkDescriptor("ds1");
    String filename = "./test_output/sr_manager_test.txt";
    fdesc.setFileName(filename);
    fdesc.setAddTimeStamp(false);
    fdesc.setFormat(FormatType.TABULAR);
    fdesc.setDelimiter(",");
    
    FileSinkComponentControllerAction fsAction = new FileSinkComponentControllerAction(fdesc);
    List<ObjectB> objs = createObjs();
    Context<ObjectB> context = new DefaultContext<ObjectB>("root");
    context.addAll(objs);
    RunState runState = createRunState(1, null);
    runState.setMasterContext(context);
    ISchedule schedule = runState.getScheduleRegistry().getModelSchedule();
    
    DataSetsActionCreator dsCreator = new DataSetsActionCreator();
    DataInitActionCreator dsInit = new DataInitActionCreator();
    
    dsCreator.createControllerAction().batchInitialize(runState, "root");
    dsAction.batchInitialize(runState, "root");
    fsAction.batchInitialize(runState, "root");
    ControllerAction initAction = dsInit.createControllerAction();
    initAction.batchInitialize(runState, "root");
    initAction.runInitialize(runState, "root", createParameters());
    
    for (int i = 0; i < 3; i++) {
      schedule.execute();
      for (ObjectB objB : objs) {
        objB.setInt(i);
      }
    }

    initAction.runCleanup(runState, "root");
    initAction.batchCleanup(runState, "root");
    
    testSRFileOutput(filename);
  }

  @Test
  public void testSRDataManager() throws IOException {
    SingleRunDataSetManager manager = new SingleRunDataSetManager();
   
    List<NonAggregateDataSource> sources = new ArrayList<NonAggregateDataSource>();
    sources.add(manager.getTickCountDataSource());
    sources.add(new MethodDataSource("double", ObjectB.class, "getDouble"));
    sources.add(new MethodDataSource("object", ObjectB.class, "object"));
    sources.add(new MethodDataSource("int", ObjectB.class, "getInt"));
    sources.add(manager.getRandomSeedDataSource());

    List<DataSink> sinks = new ArrayList<DataSink>();
    String filename = "./test_output/sr_manager_test.txt";
    File file = new File(filename);
    FileDataSink fsink = new FileDataSink("fs1", file, new TabularFormatter(sources, ","));
    sinks.add(fsink);
    Sink sink1 = new Sink();
    sinks.add(sink1);

    NonAggregateDataSet set1 = new NonAggregateDataSet("ds1", sources, sinks);
    manager.addDataSet(set1, ScheduleParameters.createRepeating(1, 1));

    sinks = new ArrayList<DataSink>();
    Sink sink2 = new Sink();
    sinks.add(sink2);

    NonAggregateDataSet set2 = new NonAggregateDataSet("ds2", sources, sinks);
    manager.addDataSet(set2, ScheduleParameters.createRepeating(1, 1));

    List<ObjectB> objs = createObjs();
    Context<ObjectB> context = new DefaultContext<ObjectB>("root");
    context.addAll(objs);
    RunState runState = createRunState(1, null);
    runState.setMasterContext(context);
    ISchedule schedule = runState.getScheduleRegistry().getModelSchedule();
    manager.batchStarted();
    manager.runStarted(runState, "root", createParameters());
   
    assertEquals(manager.getRandomSeedDataSource().get(null), (Integer.valueOf(1)));
    assertEquals(manager.getRandomSeedDataSource().get(new ArrayList<Object>()),
        (Integer.valueOf(1)));

    for (int i = 0; i < 3; i++) {
      schedule.execute();
      for (ObjectB objB : objs) {
        objB.setInt(i);
      }
    }

    manager.runEnded(runState, "root");
    manager.batchEnded();

    testSRSinks(filename, sink1, sink2);
  }
  
  

  private List<String> readLines(String filename) throws IOException {
    List<String> lines = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    String line = null;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }
    reader.close();
    return lines;
  }
  
  private  void testSRFileOutput(String fileName) throws IOException {
    List<String> found = readLines(fileName);
    List<String> expectedLines = readLines("./test_output/sr_expected.txt");

    assertEquals(found.size(), (expectedLines.size()));
    for (int i = 0; i < found.size(); i++) {
      //System.out.println(found.get(i));
      //System.out.println(expectedLines.get(i));
      assertEquals(found.get(i), (expectedLines.get(i)));
    }
    
  }

  private void testSRSinks(String fileName, Sink sink1, Sink sink2) throws IOException {
    Sink[] sinks = { sink1, sink2 };
    List<String> found = readLines(fileName);
    List<String> expectedLines = readLines("./test_output/sr_expected.txt");

    assertEquals(found.size(), (expectedLines.size()));
    for (int i = 0; i < found.size(); i++) {
      assertEquals(found.get(i), (expectedLines.get(i)));
    }

    List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>();
    for (int i = 1; i < found.size(); i++) {
      String[] vals = found.get(i).split(",");
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("tick", Double.valueOf(vals[0]));
      map.put("double", Double.valueOf(vals[1]));
      map.put("object", vals[2].substring(1, vals[2].length() - 1));
      map.put("int", Integer.valueOf(vals[3]));
      map.put("random_seed", Integer.valueOf(vals[4]));
      expected.add(map);
    }
    assertEquals(sink1.items.size(), (12));
    assertEquals(sink2.items.size(), (12));
    assertEquals(expected.size(), (12));

    for (int i = 0; i < 9; i++) {
      for (Sink sink : sinks) {
        Map<String, Object> data = sink.items.get(i);
        Map<String, Object> exp = expected.get(i);
        assertEquals(data.size(), (exp.size()));
        for (String key : exp.keySet()) {
          assertEquals(data.get(key), (exp.get(key)));
        }
      }
    }
  }

  @Test
  public void testBatchDataManager() throws IOException {
    BatchRunDataSetManager manager = new BatchRunDataSetManager();
    manager.clearDataSets();

    List<NonAggregateDataSource> sources = new ArrayList<NonAggregateDataSource>();
    sources.add(manager.getBatchRunDataSource());
    sources.add(manager.getRandomSeedDataSource());
    sources.add(manager.getTickCountDataSource());
    sources.add(new MethodDataSource("double", ObjectB.class, "getDouble"));
    sources.add(new MethodDataSource("object", ObjectB.class, "object"));
    sources.add(new MethodDataSource("int", ObjectB.class, "getInt"));

    List<DataSink> sinks = new ArrayList<DataSink>();
    String filename = "./test_output/batch_manager_test.txt";
    File file = new File(filename);
    FileDataSink fsink = new FileDataSink("fs1", file, new TabularFormatter(sources, ","));
    sinks.add(fsink);
    Sink sink1 = new Sink();
    sinks.add(sink1);

    NonAggregateDataSet set1 = new NonAggregateDataSet("ds1", sources, sinks);
    manager.addDataSet(set1, ScheduleParameters.createRepeating(1, 1));

    sinks = new ArrayList<DataSink>();
    Sink sink2 = new Sink();
    sinks.add(sink2);

    NonAggregateDataSet set2 = new NonAggregateDataSet("ds2", sources, sinks);
    manager.addDataSet(set2, ScheduleParameters.createRepeating(1, 1));

    RunState runState = createRunState(1, null);
    manager.batchStarted();
    for (int r = 0; r < 3; r++) {
      runState = createRunState(r + 1, runState);
      ISchedule schedule = runState.getScheduleRegistry().getModelSchedule();
      List<ObjectB> objs = createObjs();
      Context<ObjectB> context = new DefaultContext<ObjectB>("root");
      context.addAll(objs);
      runState.setMasterContext(context);
      manager.runStarted(runState, "root", createParameters());
      assertEquals(manager.getRandomSeedDataSource().get(null), (Integer.valueOf(1)));
      assertEquals(manager.getRandomSeedDataSource().get(new ArrayList<Object>()),
          (Integer.valueOf(1)));

      for (int i = 0; i < 3; i++) {
        schedule.execute();
        for (ObjectB objB : objs) {
          objB.setInt(i);
        }
      }
      manager.runEnded(runState, "root");
    }

    manager.batchEnded();
    testBatchSinks(filename, sink1, sink2);
  }

  private void testBatchSinks(String fileName, Sink sink1, Sink sink2) throws IOException {
    Sink[] sinks = { sink1, sink2 };
    List<String> found = readLines(fileName);
    List<String> expectedLines = readLines("./test_output/batch_expected.txt");

    assertEquals(found.size(), (expectedLines.size()));
    for (int i = 0; i < found.size(); i++) {
      assertEquals(found.get(i), (expectedLines.get(i)));
    }

    List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>();
    for (int i = 1; i < found.size(); i++) {
      String[] vals = found.get(i).split(",");
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("run", Integer.valueOf(vals[0]));
      map.put("random_seed", Integer.valueOf(vals[1]));
      map.put("tick", Double.valueOf(vals[2]));
      map.put("double", Double.valueOf(vals[3]));
      map.put("object", vals[4].substring(1, vals[4].length() - 1));
      map.put("int", Integer.valueOf(vals[5]));
      expected.add(map);
    }
    assertEquals(sink1.items.size(), (36));
    assertEquals(sink2.items.size(), (36));
    assertEquals(expected.size(), (36));

    for (int i = 0; i < 9; i++) {
      for (Sink sink : sinks) {
        Map<String, Object> data = sink.items.get(i);
        Map<String, Object> exp = expected.get(i);
        assertEquals(data.size(), (exp.size()));
        for (String key : exp.keySet()) {
          assertEquals(data.get(key), (exp.get(key)));
        }
      }
    }
  }
}
