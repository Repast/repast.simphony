package repast.simphony.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.NonModelAction;
import repast.simphony.gis.visualization.engine.GIS3DVisualizationRegistryData;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.FastMethodConvertor;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.DefaultDisplayData;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.NetworkStyleRegistrar;
import repast.simphony.visualization.engine.NetworkStyleRegistrarOGL2D;
import repast.simphony.visualization.engine.ProjectionDescriptor;
import repast.simphony.visualization.engine.StyleRegistrar;
import repast.simphony.visualization.engine.StyleRegistrarOGL2D;
import repast.simphony.visualization.engine.VisualizationRegistry;
import repast.simphony.visualization.engine.VisualizationRegistryData;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import repast.simphony.ws.gis.ServerDisplayCreatorGIS;
import simphony.util.messages.MessageCenter;

public class DisplayServerControllerAction extends DefaultControllerAction {

	private static final MessageCenter MSG_LOG = MessageCenter.getMessageCenter(DisplayServerControllerAction.class);

	private List<Pair<String, DisplayDescriptor>> descriptors = new ArrayList<>();
	private List<ISchedulableAction> actions = new ArrayList<>();
	private List<DisplayServer> displayServers = new ArrayList<>();
	private RunListener rlUpdater = new UpdatingRunListener();
	private int dsIdx = 0;
	
	class UpdatingRunListener implements RunListener {
	    
	    void update() {
	        for (DisplayServer ds : displayServers) {
	            ds.update(true);
	        }
	    }

        @Override
        public void stopped() {
            // rather than rely on the listener to update on
            // stop we call update in runCleanup (see below) in order
            // to properly update and then destroy the server.
        }

        @Override
        public void paused() {
            update();
            
        }

        @Override
        public void started() {}

        @Override
        public void restarted() {}
    }
	

	@NonModelAction
	static class DisplayUpdater implements IAction {

		DisplayServer dserver;

		public DisplayUpdater(DisplayServer dserver) {
			this.dserver = dserver;
		}

		public void execute() {
			dserver.update(false);
		}
	}

	public DisplayServerControllerAction(Path scenarioDirectory) {
		ScenarioFileParser parser = new ScenarioFileParser();
		try {
			XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
				protected boolean useXStream11XmlFriendlyMapper() {
					return true;
				}
			};
			xstream.registerConverter(new FastMethodConvertor(xstream));
			List<ScenarioFileParser.ScenarioElement> elements = parser
					.parseScenario(scenarioDirectory.resolve("scenario.xml"));
			for (ScenarioFileParser.ScenarioElement element : elements) {
				if (element.name.equals("repast.simphony.action.display")) {
					try (BufferedReader reader = Files.newBufferedReader(scenarioDirectory.resolve(element.file))) {
						DisplayDescriptor descriptor = DisplayDescriptor.class.cast(xstream.fromXML(reader));
						descriptors.add(new Pair<String, DisplayDescriptor>(element.context, descriptor));
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MSG_LOG.error("Error reading scenario file", e);
		}
	}

	private Context<?> findContext(String contextId, Context<?> context) {
		Context<?> found = null;
		if (context.getId().equals(contextId)) {
			found = context;
		} else {
			for (Context<?> child : context.getSubContexts()) {
				found = findContext(contextId, child);
				if (found != null)
					break;
			}
		}

		return found;
	}

	private DefaultDisplayData<?> createDisplayData(Context<?> context, DisplayDescriptor descriptor) {
		DefaultDisplayData<?> data = new DefaultDisplayData<>(context);
		for (ProjectionData pData : descriptor.getProjections()) {
			data.addProjection(pData.getId());
		}

		// TODO cast descriptor using descriptor getType
		// to get any additional properties, e.g.
		// for (String vlName : descriptor.getValueLayerNames()) {
		// data.addValueLayer(vlName);
		// }
		return data;
	}

	private Layout<?, ?> layoutFromClassName(String layoutClassName, Context<?> context, DisplayDescriptor descriptor)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// get the layout class, checking if the projection to base
		// the layout on has an implied layout (i.e. like grid) that
		// overrides the layout in the display descriptor.
		if (layoutClassName == null || layoutClassName.length() == 0) {
			layoutClassName = descriptor.getLayoutClassName();
		}

		Class<?> clazz = Class.forName(layoutClassName, true, this.getClass().getClassLoader());
		if (!Layout.class.isAssignableFrom(clazz)) {
			// todo better errors.
			throw new IllegalArgumentException("Layout class must implements Layout interface.");
		}
		Layout<?, Projection<?>> layout = (Layout<?, Projection<?>>) clazz.getDeclaredConstructor().newInstance();
		String layoutProj = descriptor.getLayoutProjection();
		Projection<?> projection = context.getProjection(layoutProj);
		if (projection == null) {
			throw new RuntimeException("Projection '" + layoutProj + "' not found.");
		}
		layout.setProjection(projection);
		layout.setLayoutProperties(descriptor.getLayoutProperties());
		return layout;
	}

	private Layout<?, ?> createLayout(DisplayDescriptor descriptor, Context<?> context) {
		String layoutProjection = descriptor.getLayoutProjection();
		ProjectionDescriptor pd = descriptor.getProjectionDescriptor(layoutProjection);
		String className = null;

		if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
			className = pd.getImpliedLayout2D();
		}

		// TODO See DisplayProducer:44+ for how the different DisplayCreators are
		// chosen based on display type info

		if (className != null) {
			try {
				return layoutFromClassName(className, context, descriptor);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				MSG_LOG.error("Error creating layout class ", e);
			}
		}

		return null;
	}

	@Override
	public void runInitialize(RunState runState, Object contextId, Parameters params) {

		MSG_LOG.info("DisplayServerControllerAction.runInitialize()");
		displayServers.clear();

		String outgoingAddr = runState.getFromRegistry("outgoing.addr").toString();
		for (Pair<String, DisplayDescriptor> p : descriptors) {
			Context<?> context = findContext(p.getFirst(), runState.getMasterContext());
			if (context == null) {
				MSG_LOG.error("Error finding context " + p.getFirst(), new IllegalArgumentException());
			}

			// TODO refactor below into a display creator class
			 
			DisplayDescriptor descriptor = p.getSecond();
			DisplayData<?> displayData = createDisplayData(context, descriptor);
			Layout<?, ?> layout = createLayout(descriptor, context);

			DisplayServer ds = null;

			MSG_LOG.info("Initializing display : " + descriptor.getName() + " - " + descriptor.getDisplayType());

			if (descriptor.getDisplayType().equals(DisplayType.TWO_D)) {
				final DisplayServer2D ds2d = new DisplayServer2D(outgoingAddr, displayData, descriptor, layout, getDisplayID());
				
				try {
					StyleRegistrarOGL2D styleReg = new StyleRegistrarOGL2D();
					styleReg.registerStyles(new StyleRegistrar.Registrar<StyleOGL2D<?>>() {
						public void register(Class<?> agentClass, StyleOGL2D<?> style) {
							ds2d.registerStyle(agentClass, style);
						}
					}, descriptor);
					
					 NetworkStyleRegistrarOGL2D netReg = new NetworkStyleRegistrarOGL2D();
			      netReg.registerNetworkStyles(new NetworkStyleRegistrar.Registrar<EdgeStyleOGL2D>() {
			        public void register(Network<?> network, EdgeStyleOGL2D style) {
			        	ds2d.registerNetworkStyle(network, style);
			        }
			      }, descriptor, context);
					
					ds = ds2d;
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					MSG_LOG.error("Error creating style.", e);
				}

			}
			else if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
				
				// TODO create 3D displays
			}
			 else {
		    	VisualizationRegistryData data = VisualizationRegistry.getDataFor(descriptor.getDisplayType());
		    	
		    	if (data == null) {
		    		MSG_LOG.warn("No visualization registry found rot display type: " + descriptor.getDisplayType());
		    		continue;
		    	}
		    	
		    	MSG_LOG.info("Found viz type in registry: " + data.getVisualizationType());
		    
		    	if (descriptor.getDisplayType().equals(GIS3DVisualizationRegistryData.TYPE)) {
		    		ServerDisplayCreatorGIS creator = new ServerDisplayCreatorGIS(outgoingAddr, 
		    				context, (GISDisplayDescriptor)descriptor);

		    		try {
		    			ds = creator.createDisplay(getDisplayID());
		    		} 
		    		catch (Exception ex) {
		    			MSG_LOG.error("Error creating display for " + descriptor.getDisplayType(), ex);
		    		}
		    	}	
			 }
			
			if (ds == null) { 
				MSG_LOG.warn("Can't create display for type: " + descriptor.getDisplayType());
				continue;
			}
			
			ds.init();
			displayServers.add(ds);
			DisplayUpdater updater = new DisplayUpdater(ds);
			ISchedulableAction action = runState.getScheduleRegistry().getModelSchedule()
					.schedule(descriptor.getScheduleParameters(), updater);
			actions.add(action);
			runState.getScheduleRegistry().getScheduleRunner().addRunListener(rlUpdater);
		}
	}
	
	public List<Pair<Integer,Object>> getAgents(int displayId, List<Integer> agentIds) {
	    List<Pair<Integer,Object>> list = null;
	    for (DisplayServer server : displayServers) {
	        if (server.getId() == displayId) {
	            list = server.getAgents(agentIds);
	            break;
	        }
	    }
	    return list;
	}

	public void runCleanup(RunState runState, Object contextId) {
		for (ISchedulableAction action : actions) {
			runState.getScheduleRegistry().getModelSchedule().removeAction(action);
		}
		actions.clear();
		// force servers to send an update
		rlUpdater.paused();

		for (DisplayServer ds : displayServers) {
			ds.destroy();
		}
		// We don't clear the display server list here as we need
		// to be able to get probed agents from them.
		runState.getScheduleRegistry().getScheduleRunner().removeRunListener(rlUpdater);
	}
	
	private int getDisplayID() {
		dsIdx++;
		return dsIdx;
	}
}
