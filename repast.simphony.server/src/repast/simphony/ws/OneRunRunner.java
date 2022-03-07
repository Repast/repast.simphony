/*CopyrightHere*/
package repast.simphony.ws;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import repast.simphony.batch.BatchScenarioLoader;
import repast.simphony.context.Context;
import repast.simphony.engine.controller.DefaultController;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultRunEnvironmentBuilder;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.gis.ui.probe.GeographyLocationProbe;
import repast.simphony.gis.visualization.engine.GIS3DVisualizationRegistryData;
import repast.simphony.gis.visualization.engine.GISVisualizationRegistryData;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.parameter.Schema;
import repast.simphony.scenario.ScenarioLoadException;
import repast.simphony.scenario.ScenarioLoader;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.scenario.data.UserPathFileReader;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.grid.Grid;
import repast.simphony.ui.probe.GridLocationProbe;
import repast.simphony.ui.probe.MethodPropertyDescriptor;
import repast.simphony.ui.probe.ProbeInfo;
import repast.simphony.ui.probe.ProbeIntrospector;
import repast.simphony.ui.probe.SpaceLocationProbe;
import repast.simphony.util.ClassPathEntry;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.collections.Pair;
import repast.simphony.util.collections.Tree;
import repast.simphony.util.collections.TreeVisitor;
import repast.simphony.visualization.engine.VisualizationRegistry;
import simphony.util.messages.MessageCenter;

/**
 * Runs a single run of a simulation.
 * 
 * @author Nick Collier
 */
public class OneRunRunner implements RunListener {

    private static class Entry {

        private String name;
        private String display_name;
        private String location;

    }

    private static final MessageCenter LOG = MessageCenter.getMessageCenter(OneRunRunner.class);

    private static class ORBController extends DefaultController {

        private int runNumber;
        private String outgoingAddr;

        /**
         * @param runEnvironmentBuilder
         */
        public ORBController(RunEnvironmentBuilder runEnvironmentBuilder, String outgoingAddr) {
            super(runEnvironmentBuilder);
            this.outgoingAddr = outgoingAddr;
        }

        public void setRunNumber(int runNumber) {
            this.runNumber = runNumber;
        }

        @Override
        protected boolean prepare() {
            boolean retVal = super.prepare();
            RunState runState = this.getCurrentRunState();
            runState.addToRegistry("outgoing.addr", outgoingAddr);
            this.getCurrentRunState().getRunInfo().setRunNumber(runNumber);
            return retVal;
        }

        /*
         * (non-Javadoc)
         * 
         * @see repast.simphony.engine.controller.DefaultController#prepareForNextRun()
         */
        @Override
        protected void prepareForNextRun() {
            super.prepareForNextRun();
            this.getCurrentRunState().getRunInfo().setRunNumber(runNumber);
        }
    }

    private class MessageHandler implements Callable<Void> {

        private Socket incoming;
        private boolean started = false;

        public MessageHandler(Socket incoming) {
            this.incoming = incoming;
        }

        public Void call() {
            while (true) {
                // waits for message
                String msg = incoming.recvStr();
                LOG.info("ORR Received: " + msg);

                if (msg.startsWith("{\"params\"")) {
                    initParamsFromJSON(msg);

                } else if (msg.equals("init")) {
                    initSim();
                } else if (msg.equals("start")) {
                    start();
                    started = true;
                } else if (msg.equals("step")) {
                	step();
                	started = true;
                } else if (msg.equals("stop")) {
                    stop();
                    if (!started) {
                        // stop after init with no start, so we need to
                        // call stopped explicitly rather than through
                        // the RunListener mechanism
                        stopped();
                    }
                } else if (msg.equals("pause")) {
                    pause();
                } else if (msg.equals("shutdown")) {
                    shutdown();
                    break;
                } else if (msg.startsWith("{\"picked\"")) {
                    onPicked(msg);

                }
            }
            return null;
        }
    }

    private RunEnvironmentBuilder runEnvironmentBuilder;
    protected ORBController controller;
    protected boolean pause = false;
    protected Object monitor = new Object();
    private ZContext ctx = null;
    private Socket incoming = null, outgoing = null;

    private boolean initRequired = true;
    private boolean startSim = true;
    private Parameters params;
    private Future<Void> msgFuture;
    private TickServer tickServer;

    public OneRunRunner(Path workingDir, Path scenario, String incomingAddr, String outgoingAddr)
            throws ScenarioLoadException {
        try {
            initMessageCenter(workingDir);
            initSocket(incomingAddr, outgoingAddr);
            loadParameters(scenario);
        } catch (IOException | ParserConfigurationException | SAXException | ScenarioLoadException ex) {
            ex.printStackTrace();
        }

        // TODO The viz registry may not be necceessary since we're running from
        // the r.s.bin_and_src.jar

        // Initialize the viz registry for GIS. In the RS runtime, this is done
        // as part of the GIS viz plugin via a SAF extenstion.
        VisualizationRegistry.addRegistryData(new GISVisualizationRegistryData());
        VisualizationRegistry.addRegistryData(new GIS3DVisualizationRegistryData());

        // Similar init for the GIS projection registry
//    		ProjectionRegistry.addRegistryData(new GeographyProjectionRegistryData());

        tickServer = new TickServer(outgoingAddr);
        ScheduleRunner scheduleRunner = new ScheduleRunner();
        scheduleRunner.setTickListener(tickServer);
        scheduleRunner.addRunListener(this);
        
        boolean isRelogo = false;
        UserPathFileReader fr = new UserPathFileReader();
        try {
			UserPathData upd = fr.read(scenario.resolve("user_path.xml").toFile());
			for (ClassPathEntry entry : upd.agentEntries()) {
				for (File f : entry.getClassPaths()) {
					if (f.toString().contains("repast.simphony.relogo.runtime")) {
						isRelogo = true;
						break;
					}
				}
			}
		} catch (IOException | XMLStreamException e) {
			LOG.error("Error reading user path", e);
		}
        
        runEnvironmentBuilder = new DefaultRunEnvironmentBuilder(scheduleRunner, isRelogo);

        controller = new ORBController(runEnvironmentBuilder, outgoingAddr);
        controller.setScheduleRunner(scheduleRunner);
        init(scenario.toFile());
    }

    public void run() {
        ExecutorService executorService = null;
        try {
            MessageHandler handler = new MessageHandler(incoming);
            executorService = Executors.newSingleThreadExecutor();
            msgFuture = executorService.submit(handler);
            ZMsg.newStringMsg("json", "{\"id\": \"status\", \"value\": \"ready\"}").send(outgoing);
            // outgoing.send("{\"id\": \"status\", \"value\": \"ready\"}");
            msgFuture.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (CancellationException ex) {
            LOG.info("MSG Handler Canceled -- probably via stop");
        } finally {
            cleanUp();
            if (executorService != null)
                executorService.shutdown();
        }
    }

    private void cleanUp() {
        if (incoming != null) {
            incoming.close();
        }

        if (outgoing != null) {
            outgoing.close();
        }
        if (ctx != null) {
            ctx.close();
            ctx.destroy();
        }
        LOG.info("One Run Runner Cleaned Up");
    }

    private void initSocket(String incomingAddr, String outgoingAddr) {
        ctx = new ZContext();
        incoming = ctx.createSocket(SocketType.PULL);
        outgoing = ctx.createSocket(SocketType.PUSH);
        incoming.connect(incomingAddr);
        outgoing.connect(outgoingAddr);
    }

    private void initMessageCenter(Path root) throws ScenarioLoadException {
        try (BufferedReader reader = Files
                .newBufferedReader(Paths.get(root.toAbsolutePath().toString(), "MessageCenter.log4j.properties"))) {
            Properties orig = new Properties();
            orig.load(reader);
            Properties props = new Properties(orig);
            // replace any references to MessageCenterLayout with PatternLayout as
            // MessageCenterLayout is incompatible with log4j-2
            for (Map.Entry<Object, Object> entry : orig.entrySet()) {
                if (entry.getValue().toString().trim().equals("simphony.util.messages.MessageCenterLayout")) {
                    // System.out.println("Replacing: " + entry.getKey());
                    props.put(entry.getKey(), "org.apache.log4j.PatternLayout");
                }
            }
            PropertyConfigurator.configure(props);
        } catch (IOException e) {
            throw new ScenarioLoadException(e);
        }
    }

    private void loadParameters(Path scenario) throws ParserConfigurationException, SAXException, IOException {
        Path parameters = scenario.resolve("parameters.xml");
        ParametersParser parser = new ParametersParser(parameters.toFile());
        params = parser.getParameters();
    }

    private void initParamsFromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            Map<String, List<Map<String, Object>>> pMap = mapper.readValue(json,
                    new TypeReference<Map<String, List<Map<String, Object>>>>() {
                    });
            List<Map<String, Object>> pvals = pMap.get("params");
            Schema schema = params.getSchema();
            Set<String> badParams = new HashSet<>();
            for (Map<String, Object> param : pvals) {
                String name = param.get("name").toString();
                String value = param.get("value").toString();
                ParameterSchema details = schema.getDetails(name);
                Object val = value;
                try {
                    if (!details.getType().equals(String.class)) {
                        val = details.fromString(value);
                    }
                } catch (Exception ex) {
                    badParams.add(name);
                }

                if (schema.validate(name, val)) {
                    params.setValue(name, value);
                } else {
                    badParams.add(name);
                }
            }
            StringBuilder builder = new StringBuilder("{\"id\": \"parameters_status\", \"errors\" : [");
            boolean first = true;
            for (String pname : badParams) {
                if (!first) {
                    builder.append(",");
                }
                builder.append("\"");
                builder.append(pname);
                builder.append("\"");
                first = false;
            }

            builder.append("]}");
            ZMsg.newStringMsg("json", builder.toString()).send(outgoing);
            // outgoing.send(builder.toString());
            if (badParams.size() > 0) {
                msgFuture.cancel(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(File scenarioDir) throws ScenarioLoadException {
        if (scenarioDir.exists()) {
            ScenarioLoader loader = new BatchScenarioLoader(scenarioDir);
            loader.addInitializer(new WSInitializer());
            ControllerRegistry registry = loader.load(runEnvironmentBuilder);
            controller.setControllerRegistry(registry);
        } else {
            // msgCenter.error("Scenario not found", new
            // IllegalArgumentException("Invalid scenario "
            // + scenarioDir.getAbsolutePath()));
           LOG.error("Scenario not found", new IOException("Scenario Directory " + scenarioDir + " not found."));
        }
    }

    public void onPicked(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });

            int displayId = (Integer) map.get("display_id");
            List<Integer> ids = (List<Integer>) map.get("picked");
            String type = map.get("display_type").toString();
            List<Pair<Integer, Object>> agents = new ArrayList<>();
            
            if (type.equals("2D")) {
                Object mcid = controller.getControllerRegistry().getMasterContextId();
                Tree<ControllerAction> tree = controller.getControllerRegistry().getActionTree(mcid);
                // Array to work around having to be final in lambda expression
                DisplayServerControllerAction[] act = { null };
                TreeVisitor<ControllerAction> tv = (action) -> {
                    if (action instanceof DisplayServerControllerAction) {
                        act[0] = (DisplayServerControllerAction) action;
                    }
                };
    
                tree.preOrderTraversal(tv);
                agents = act[0].getAgents(displayId, ids);
            }
            
            StringBuilder builder = new StringBuilder("{\"id\" : \"probed\", \"display_id\" : ");
            builder.append(displayId);
            builder.append(", \"display_type\" : \"");
            builder.append(type);
            builder.append("\", \"value\" : [");
            boolean first = true;
            for (Pair<Integer, Object> p : agents) {
                if (!first) {
                    builder.append(",");
                }
                builder.append(probeObject(p.getFirst(), p.getSecond()));
                first = false;
            }
            builder.append("]}");
            ZMsg.newStringMsg("json", builder.toString()).send(outgoing);
        } catch (IOException ex) {
            LOG.error("Error parsing picked json", ex);
        }
    }

    private String getProbeTitle(ProbeInfo pbInfo, Object obj)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String title;
        if (pbInfo.getIDProperty() == null) {
            title = obj.toString();
            if (title.lastIndexOf('.') > 0) {
                title = title.substring(title.lastIndexOf(".") + 1, title.length());
            }
        } else {
            title = (String) pbInfo.getIDProperty().getReadMethod().invoke(obj, new Object[0]);
        }

        return title;
    }

    private String probeObject(Integer viewId, Object obj) {
        StringBuilder builder = new StringBuilder("{\"id\" : \"");
        try {
            ProbeInfo pbInfo = ProbeIntrospector.getInstance().getProbeInfo(obj.getClass());
            builder.append(getProbeTitle(pbInfo, obj));
            builder.append("\", \"view_id\" : ");
            builder.append(viewId);
            builder.append(", \"props\" : [");
            boolean first = true;
            for (MethodPropertyDescriptor desc : pbInfo.methodPropertyDescriptors()) {
                if (!first) {
                    builder.append(",");
                }
                builder.append("{\"name\" : \"");
                builder.append(desc.getName());
                builder.append("\", \"display_name\" : \"");
                builder.append(desc.getDisplayName());
                builder.append("\", \"value\" : \"");
                if (desc.getReadMethod() != null) {
                    builder.append(desc.getReadMethod().invoke(obj));
                }
                builder.append("\"}");
                first = false;
            }
            builder.append("], \"locations\": [");
            
            List<Entry> locations = getLocations(obj);
            first = true;
            for (Entry entry : locations) {
                if (!first) {
                    builder.append(",");
                }
                builder.append("{\"name\" : \"");
                builder.append(entry.name);
                builder.append("\", \"display_name\" : \"");
                builder.append(entry.display_name);
                builder.append("\", \"value\" : \"");
                builder.append(entry.location);
                builder.append("\"}");
                first = false;
            }
                    
            builder.append("]}");

        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            LOG.error("Error while probing agent", e);
            return "";
        }

        return builder.toString();
    }

    private List<Entry> getLocations(Object obj) {
        List<Entry> locations = new ArrayList<>();
        Context<?> context = ContextUtils.getContext(obj);

        if (context != null) {

            for (Grid<?> grid : context.getProjections(Grid.class)) {
                try {
                    GridLocationProbe probe = new GridLocationProbe(obj, grid);
                    Entry entry = new Entry();
                    entry.display_name = probe.getLocationDescriptor().getDisplayName();
                    entry.name = grid.getName() + "_location";
                    entry.location = probe.getLocation();
                    locations.add(entry);
                } catch (IntrospectionException ex) {
                    LOG.error("Error while getting probed grid location", ex);
                }
            }

            for (ContinuousSpace<?> space : context.getProjections(ContinuousSpace.class)) {
                try {
                    SpaceLocationProbe probe = new SpaceLocationProbe(obj, space);
                    Entry entry = new Entry();
                    entry.display_name = probe.getLocationDescriptor().getDisplayName();
                    entry.name = space.getName() + "_location";
                    entry.location = probe.getLocation();
                    locations.add(entry);
                } catch (IntrospectionException ex) {
                    LOG.error("Error while getting probed space location", ex);
                }
            }

            for (Geography<?> space : context.getProjections(Geography.class)) {
                try {
                    GeographyLocationProbe probe = new GeographyLocationProbe(obj, space);
                    Entry entry = new Entry();
                    entry.display_name = probe.getLocationDescriptor().getDisplayName();
                    entry.name = space.getName() + "_location";
                    entry.location = probe.getLocation();
                    locations.add(entry);
                } catch (IntrospectionException ex) {
                    LOG.error("Error while getting probed geography location", ex);
                }
            }
        }
        
        return locations;
    }

    public void initSim() {
        if (initRequired) {
            controller.batchInitialize();
            params = checkForSeed();
            controller.runParameterSetters(params);
            controller.setRunNumber(1);
            controller.runInitialize(params);
            initRequired = false;
            ZMsg.newStringMsg("json", "{\"id\": \"status\", \"value\": \"initialized\"}").send(outgoing);
        }
    }
    
    public void step() {
    	if (initRequired) {
    		initSim();
    		try {
    			// hack to let the browser GUI complete the init
    			// before the model run update messages start
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	if (startSim) {
            controller.execute();
            startSim = false;
    	}
    	controller.getScheduleRunner().step();
    }


    public void start() {
        if (initRequired) {
            initSim();
            try {
    			// hack to let the browser GUI complete the init
    			// before the model run update messages start
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }

        if (startSim) {
            controller.execute();
            startSim = false;
        } else {
            controller.getScheduleRunner().setPause(false);
        }
    }

    public void pause() {
        controller.getScheduleRunner().setPause(true);
    }

    public void stop() {
        controller.getScheduleRunner().stop();
    }

    public void shutdown() {
        LOG.info("Shutting down the Repast model runner");
        msgFuture.cancel(true);
        // System.exit(0);
    }

//    public void reset() {
//        initRequired = true;
//        startSim = true;
//        controller.runCleanup();
//        controller.batchCleanup();
//    }

    private Parameters checkForSeed() {

        if (!params.getSchema().contains(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
            ParametersCreator creator = new ParametersCreator();
            creator.addParameters(params);
            creator.addParameter(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, Integer.class,
                    (int) System.currentTimeMillis(), false);
            params = creator.createParameters();
        }

        return params;
    }

    /**
     * Invoked when the current run has been paused.
     */
    public void paused() {
        tickServer.update(true);
    }

    /**
     * Invoked when the current run has been restarted after a pause.
     */
    public void restarted() {
    }

    /**
     * Invoked when the current run has been started.
     */
    public void started() {
    }

    /**
     * Invoked when the current run has been stopped. This will stop this thread
     * from waiting for the current run to finish.
     */
    public void stopped() {
        // try {
        LOG.info("Model stopped callback triggered");
        tickServer.update(true);
        tickServer.close();
        controller.runCleanup();
        controller.batchCleanup();
        ZMsg.newStringMsg("json", "{\"id\": \"status\", \"value\": \"stopped\"}").send(outgoing);
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        Path p = Paths.get(args[0]);
        try (BufferedReader reader = Files.newBufferedReader(p)) {
            props.load(reader);
            Path parent = p.normalize().toAbsolutePath().getParent();
            Path workingDir = parent.resolve(props.getProperty("working.directory"));
            Path scenario = parent.resolve(props.getProperty("scenario"));
            String incomingAddr = props.getProperty("incoming");
            String outgoingAddr = props.getProperty("outgoing");

            OneRunRunner runner = new OneRunRunner(workingDir, scenario, incomingAddr, outgoingAddr);
            runner.run();

        } catch (IOException | ScenarioLoadException ex) {
            ex.printStackTrace();
        }
    }
}
