package repast.simphony.ws;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetManager;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.builder.DataSetBuilder;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.FastMethodConvertor;
import simphony.util.messages.MessageCenter;

/**
 * ControllerAction for creating DataServers.
 * 
 * @author nick
 */
public class ChartServerControllerAction extends DefaultControllerAction {

    private static final MessageCenter LOG = MessageCenter.getMessageCenter(ChartServerControllerAction.class);

    private Map<String, DataServer> dataServers = new HashMap<>();
    private Map<Object, List<TimeSeriesChartDescriptor>> descriptors = new HashMap<>();

    public ChartServerControllerAction(Path scenarioDirectory) {
        ScenarioFileParser parser = new ScenarioFileParser();
        try {

            XStream xstream = createXStream();
            List<ScenarioFileParser.ScenarioElement> elements = parser
                    .parseScenario(scenarioDirectory.resolve("scenario.xml"));
            for (ScenarioFileParser.ScenarioElement element : elements) {
                if (element.name.startsWith("repast.simphony.action.time_series_chart")) {
                    try (BufferedReader reader = Files.newBufferedReader(scenarioDirectory.resolve(element.file))) {
                        TimeSeriesChartDescriptor descriptor = TimeSeriesChartDescriptor.class
                                .cast(xstream.fromXML(reader));
                        List<TimeSeriesChartDescriptor> ds = descriptors.get(element.context);
                        if (ds == null) {
                            ds = new ArrayList<>();
                            descriptors.put(element.context, ds);
                        }
                        ds.add(descriptor);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOG.error("Error reading scenario file", e);
        }
    }

    private XStream createXStream() {
        XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
            protected boolean useXStream11XmlFriendlyMapper() {
                return true;
            }
        };
        
        // Xstream Security settings.  We can either allow any types to be deserialized,
        // or filter by regex.  Here we allow loading any class.
        // JRE classes are allowed by default.
        xstream.addPermission(AnyTypePermission.ANY);
        
        xstream.registerConverter(new FastMethodConvertor(xstream));
        xstream.alias("SeriesData", TimeSeriesChartDescriptor.SeriesData.class);
        // this is necessary to call the constructor of the TimeSeriesChartDescriptor.
        // We
        // want to run the constructor so that the new showLegend field will be set to
        // true
        // rather than default to false which it would otherwise if the constructor was
        // not called.
        // We want this to default to true because showing the legend was the default
        // before we
        // made it optional
        xstream.registerConverter(new ReflectionConverter(xstream.getMapper(), new PureJavaReflectionProvider()) {
            @SuppressWarnings("rawtypes")
            public boolean canConvert(Class type) {
                return type != null && type.equals(TimeSeriesChartDescriptor.class);
            }
        });

        return xstream;
    }

    private void initializeChart(TimeSeriesChartDescriptor descriptor, OutgoingMessageSocket outgoing) {
        LOG.info("Initializing Chart");
        StringBuilder builder = new StringBuilder("{ \"id\" : \"chart_init\", \"value\" : { \"title\" : \"");

        builder.append(descriptor.getChartTitle());
        builder.append("\", \"dataset\" : \"");
        builder.append(descriptor.getDataSet());
        builder.append("\", \"xAxisLabel\" : \"");
        builder.append(descriptor.getXAxisLabel());
        builder.append("\", \"yAxisLabel\" : \"");
        builder.append(descriptor.getYAxisLabel());
        builder.append("\", \"items\" : [");
        boolean first = true;

        for (String id : descriptor.getSeriesIds()) {
            if (!first) {
                builder.append(",");
            }
            builder.append("{\"id\" : \"");
            builder.append(id);
            builder.append("\", \"label\" : \"");
            builder.append(descriptor.getSeriesLabel(id));
            builder.append("\", \"color\" : [");
            Color c = descriptor.getSeriesColor(id);
            builder.append(c.getRed());
            builder.append(",");
            builder.append(c.getGreen());
            builder.append(",");
            builder.append(c.getBlue());
            builder.append(",");
            builder.append(c.getAlpha());
            builder.append("]}");
            first = false;
        }

        builder.append("]}}");
        outgoing.send(builder.toString());
    }

    private DataServer initializeDataServer(String dataSet, String outgoingAddr, RunState runState, Object contextId) {
        DataSetRegistry registry = (DataSetRegistry) runState.getFromRegistry(DataConstants.REGISTRY_KEY);
        DataSetManager manager = registry.getDataSetManager(contextId);
        DataSetBuilder<?> builder = manager.getDataSetBuilder(dataSet);
        DataServer dataServer = new DataServer(outgoingAddr, dataSet);
        builder.addDataSink(dataServer);
        return dataServer;
    }

    private boolean isAggregate(String dataSet, RunState runState, Object contextId) {
        DataSetRegistry registry = (DataSetRegistry) runState.getFromRegistry(DataConstants.REGISTRY_KEY);
        DataSetManager manager = registry.getDataSetManager(contextId);
        DataSetBuilder<?> builder = manager.getDataSetBuilder(dataSet);
        return builder.isAggregate();

    }

    @Override
    public void batchInitialize(RunState runState, Object contextId) {
    	// TODO Auto-generated method stub
    	//super.batchInitialize(runState, contextId);
    //}
    //@Override
    //public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
        LOG.info("Run Initialize");

        dataServers.clear();
        String outgoingAddr = runState.getFromRegistry("outgoing.addr").toString();
        OutgoingMessageSocket outgoing = new OutgoingMessageSocket(outgoingAddr);
        try {
            List<TimeSeriesChartDescriptor> ds = descriptors.get(contextId);
            if (ds != null) {
                // Initialize chart by sending info, and create data server if not created
                // already
                for (TimeSeriesChartDescriptor descriptor : ds) {
                    // TODO: remove when non-aggregate charts are supported
                    if (isAggregate(descriptor.getDataSet(), runState, contextId)) {
                        initializeChart(descriptor, outgoing);
                        String dataSet = descriptor.getDataSet();
                        if (!dataServers.containsKey(dataSet)) {
                            DataServer server = initializeDataServer(dataSet, outgoingAddr, runState, contextId);
                            dataServers.put(dataSet, server);
                        }
                    }
                }
            }
            
        } finally {
            outgoing.close();
        }
    }

    @Override
    public void batchCleanup(RunState runState, Object contextId) {
        for (DataServer ds : dataServers.values()) {
            ds.close();
        }
    }

}
