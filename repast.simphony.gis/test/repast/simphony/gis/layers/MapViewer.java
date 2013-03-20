package repast.simphony.gis.layers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.gis.data.DataUtilities;
import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.gis.display.PiccoloMapPanel;
import repast.simphony.gis.display.RepastMapLayer;
import repast.simphony.gis.display.StatusBar;
import repast.simphony.gis.legend.MapLegend;
import repast.simphony.gis.tools.DistanceTool;
import repast.simphony.gis.tools.LocationSetter;
import repast.simphony.gis.tools.PGISPanTool;
import repast.simphony.gis.tools.PMarqueeZoomIn;
import repast.simphony.gis.tools.PMarqueeZoomOut;
import repast.simphony.gis.tools.PositionTool;
import repast.simphony.gis.tools.ToolManager;
import repast.simphony.space.gis.DefaultFeatureAgentFactory;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class MapViewer {

	private JFrame frame;

	private PiccoloMapPanel mapPanel;

	private MapLegend legend;

	private MapContent context;

	private JMenuBar bar = new JMenuBar();

	private StyleBuilder builder = new StyleBuilder();

	private StatusBar statusBar = new StatusBar();

	private Properties props;
	
	SimpleFeatureCollection collection;
	
	SimpleFeatureCollection newAgents;
	
	SimpleFeature feature1;
	SimpleFeature feature2;
	
	public MapViewer() {
		context = new MapContent();
		context.getViewport().setBounds(new ReferencedEnvelope(new Envelope(-90, -90, -90, 90), 
				DefaultGeographicCRS.WGS84));		
		
		mapPanel = new PiccoloMapPanel(context);

		legend = new MapLegend(context, "legend");
		
		initTools();
		
//		createLayers();  // create layers from shapefiles
		
		createLayersFromAgents();  // create layers using agent factories
		
//		createLayers2();  // create layers from generated points
		
		System.out.println("Done creating layers.");
	}
	
	 private static SimpleFeatureType createFeatureType() {

     SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
     builder.setName("Location");
     builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

     // add attributes in order
     builder.add("Location", Point.class);
     builder.length(15).add("Name", String.class); // <- 15 chars width for name field

     // build the type
     final SimpleFeatureType LOCATION = builder.buildFeatureType();

     return LOCATION;
 }
	
	 /**
	  * Test just adding plain GeoTools Features - not agents
	  */
	private void createLayers2() {
		collection = FeatureCollections.newCollection();
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

		SimpleFeatureType type = createFeatureType();
		EchoSimpleFeatureBuilder featureBuilder = new EchoSimpleFeatureBuilder(type);

    int ats = type.getAttributeCount();
		
		for (int i=0; i<ats; i++){
			System.out.println(" lulz " + type.getType(i));
		}
		
		for (int i = 0; i < 10; i++) {
			Coordinate coord = new Coordinate(-103.823 + i / 100.0, 44.373);
			Point point = geometryFactory.createPoint(coord);

			featureBuilder.add(point);
			featureBuilder.add("Agent-" + i);
			SimpleFeature feature = featureBuilder.buildFeature(null);
			collection.add(feature);
			
			feature1 = feature;
		}

		FeatureSource source = DataUtilities.createFeatureSource(collection);

		Style style = builder.createStyle(builder.createPointSymbolizer(
				builder.createGraphic(null, builder.createMark("square", Color.RED), null)));

		// !!! WORKS !!!!
		addLayer(new RepastMapLayer(source, style));
//		addLayer(new FeatureLayer(source, style));
	}

	/**
	 * Create agents, add to geography with a feature
	 */
	@SuppressWarnings("unchecked")
	private void createLayersFromAgents(){
		Context ctxt = new DefaultContext();
		GeographyParameters geoParams = new GeographyParameters();
		Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
         .createGeography("Geography", ctxt, geoParams);
		
		GeometryFactory geoFac = new GeometryFactory();

		FeatureAgentFactoryFinder finder = FeatureAgentFactoryFinder.getInstance();
			
		DefaultFeatureAgentFactory agentFac = 
					finder.getFeatureAgentFactory(Boid.class, Point.class, geography.getCRS());

		int ats = agentFac.getFeatureType().getAttributeCount();
		
		for (int i=0; i<ats; i++){
			System.out.println(" lol " + agentFac.getFeatureType().getType(i));
		}

		for (int i = 0; i < 10; i++) {
			Boid agent = new Boid("Boid-" + i);
			ctxt.add(agent);
			Coordinate coord = new Coordinate(-103.823 + i / 100.0, 44.373);
			Point geom = geoFac.createPoint(coord);
			geography.move(agent, geom);

			feature2 = agentFac.getFeature(agent, geography);
		} 

		newAgents = agentFac.getFeatures();
		
		FeatureSource source = DataUtilities.createFeatureSource(newAgents);
		
		Style style = builder.createStyle(builder.createPointSymbolizer(
				builder.createGraphic(null,	builder.createMark("square", Color.RED), null)));

		addLayer(new RepastMapLayer(source, style));
	}
	
	private void createLayers() {
		try {
			String dataFileName = "sampleData/streams.shp"; 
			String styleFileName = "sampleData/streams.xml";
			
			URL shapefile = new File(dataFileName).toURL();
			
			ShapefileDataStore store = new ShapefileDataStore(shapefile);
			FeatureSource source = store.getFeatureSource();
			
			File styleFile = new File(styleFileName);
			StyleFactory fac = CommonFactoryFinder.getStyleFactory(null);
			SLDParser parser = new SLDParser(fac, styleFile);
			Style style = parser.readXML()[0];
			
			addLayer(new RepastMapLayer(source, style, "Streams"));

			dataFileName = "sampleData/archsites.shp"; 
			styleFileName = "sampleData/archsites.xml"; 
			store = new IndexedShapefileDataStore(new File(dataFileName).toURL());
			source = store.getFeatureSource();
			styleFile = new File(styleFileName);
			fac = CommonFactoryFinder.getStyleFactory(null);
			parser = new SLDParser(fac, styleFile);
			style = parser.readXML()[0];

//			style = builder.createStyle(builder.createPointSymbolizer(builder.createGraphic(null,
//							builder.createMark("square", Color.RED), null)));

			addLayer(new RepastMapLayer(source, style, "Sites"));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void addLayer(Layer layer) {
		context.addLayer(layer);
		context.getViewport().setBounds(context.getMaxBounds());
	}

	private void initTools() {
		URL imageFile = null;
		Image image = null;
		Map<String, Object> toolParams = null;
		
		PGISCanvas canvas = mapPanel.getCanvas();
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Zoom In");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = PMarqueeZoomIn.class.getResource("MagnifyPlus.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put(Action.SHORT_DESCRIPTION, "Zoom In");
		mapPanel.addTool(new PMarqueeZoomIn(context), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Zoom Out");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = PMarqueeZoomOut.class.getResource("MagnifyMinus.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put(Action.SHORT_DESCRIPTION, "Zoom Out");
		mapPanel.addTool(new PMarqueeZoomOut(context), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Pan");
		toolParams.put(Action.SHORT_DESCRIPTION, "Pan the map");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = PMarqueeZoomOut.class.getResource("Move.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put("DEFAULT", new Boolean(true));
		mapPanel.addTool(new PGISPanTool(context, canvas), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Zoom To Previous");
		toolParams.put(Action.SHORT_DESCRIPTION, "Zoom to the previous Extent");
		imageFile = PMarqueeZoomOut.class.getResource("PreviousExtent.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		// mapPanel.addButton(new PZoomToPreviousExtent(), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Distance");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = DistanceTool.class.getResource("ruler-icon.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put(Action.SHORT_DESCRIPTION,
						"Calculate Distance between 2 points");
    /*
    DistanceSetter setter = new DistanceSetter() {
			DecimalFormat format = new DecimalFormat("###.####");

			public void setDistance(double distance, Unit units) {
				statusBar.setMessage(format.format(distance) + " " + units);
			}

			public void clearDistance() {
				statusBar.setMessage("");
			}

		};
		*/
		//mapPanel.addTool(new DistanceTool(context, SI.METER, setter),
		//				toolParams);

		LocationSetter locationSetter = new LocationSetter() {
			DecimalFormat format = new DecimalFormat("###.####");

			public void setLocation(double lon, double lat) {
				Coordinate coordinate = new Coordinate(lon, lat);
				statusBar.setMessage(format.format(coordinate.x) + ", "
								+ format.format(coordinate.y));
			}

			public void unsetLocation() {
				statusBar.setMessage("");
			}

		};

		mapPanel.getCanvas().addInputEventListener(
						new PositionTool(context.getCoordinateReferenceSystem(),
										locationSetter));

	}

	public void show() {
		frame = new JFrame("Map Viewer");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
	  frame.add(mapPanel, BorderLayout.CENTER);
		frame.add(legend, BorderLayout.WEST);
		frame.add(bar, BorderLayout.PAGE_START);
		frame.add(statusBar, BorderLayout.PAGE_END);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		Logger.getLogger("org.geotools.map").setLevel(Level.FINEST);
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		MapViewer viewer = new MapViewer();
		viewer.show();
	}
}
