package repast.simphony.gis.layers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.gis.display.PiccoloMapPanel;
import repast.simphony.gis.display.RepastMapLayer;
import repast.simphony.gis.legend.MapLegend;
import repast.simphony.gis.tools.DistanceTool;
import repast.simphony.gis.tools.LocationSetter;
import repast.simphony.gis.tools.PGISPanTool;
import repast.simphony.gis.tools.PMarqueeZoomIn;
import repast.simphony.gis.tools.PMarqueeZoomOut;
import repast.simphony.gis.tools.PositionTool;
import repast.simphony.gis.tools.ToolManager;
import repast.simphony.gis.util.DataUtilities;
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
	List<SimpleFeature> features;
	List<SimpleFeature> newAgents = new ArrayList<SimpleFeature>();
	List<Layer> layerList = new ArrayList<Layer>();
	
	public MapViewer() {
		context = new MapContent();
		
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		
//		CRSAuthorityFactory  factory = CRS.getAuthorityFactory(true);
//		try {
//			crs = factory.createCoordinateReferenceSystem("EPSG:3005");
//		} catch (NoSuchAuthorityCodeException e) {
//			
//			e.printStackTrace();
//		} catch (FactoryException e) {
//			
//			e.printStackTrace();
//		}
		
//		context.getViewport().setCoordinateReferenceSystem(crs);
	
		
		context.getViewport().setBounds(new ReferencedEnvelope(
				new Envelope(-90, -90, -90, 90),	crs));		
	
		mapPanel = new PiccoloMapPanel(context);
		legend = new MapLegend(context, "legend");
		
//		initTools();
		
		createLayers();  // create layers from shapefiles
		
		createLayersFromAgents();  // create layers using agent factories
		
		System.out.println("Done creating layers.");
		
    show();
		
    Timer timer = new Timer();
    timer.schedule(new TimedTask(), 2000);
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


		for (int i = 0; i < 10; i++) {
			Boid agent = new Boid("Boid-" + i);
			ctxt.add(agent);
			Coordinate coord = new Coordinate(-103.823 + i / 100.0, 44.373);
			Point geom = geoFac.createPoint(coord);
			geography.move(agent, geom);

			SimpleFeature f = agentFac.getFeature(agent, geography);
			newAgents.add(f);
		} 
	
		FeatureSource source = DataUtilities.createFeatureSource(newAgents);
		
		Style style = builder.createStyle(builder.createPointSymbolizer(
				builder.createGraphic(null,	builder.createMark("square", Color.RED), null)));

		Layer layer = new RepastMapLayer(source, style);
		layerList.add(layer);

		featureSourceStyleMap.put(source, style);
		
		addLayer(layer);
	}
	
	private Map<FeatureSource, Style> featureSourceStyleMap = new HashMap<FeatureSource, Style>();
	
	private void createLayers() {
		try {
			// TODO Geotools [major] - the image doesnt initially line up with the shapefile
			//      but does after the map is moved/zoomed
			//      See Geotools JMapPane for how to handle resize and transforms
			// Test GridCoverage
//			File file = new File("../repast.simphony.gis/sampleData/earthlights.jpg");
			Style style;
			
//			GridFormatFinder.scanForPlugins();
//			AbstractGridFormat format = GridFormatFinder.findFormat(file);
//			AbstractGridCoverage2DReader reader = format.getReader(file);
//
//			GridCoverage2D coverage = reader.read(null);
//			CoverageStyleBuilder styleBuilder = new CoverageStyleBuilder();
//			style = styleBuilder.buildRGBStyle(coverage);
//
//			addLayer(new RepastRasterLayer(coverage, style));
			
			
			String dataFileName = "../repast.simphony.gis/sampleData/countries.shp"; 
			String styleFileName = "../repast.simphony.gis/sampleData/countries.xml";
			
			URL shapefile = new File(dataFileName).toURL();
			
			ShapefileDataStore store = new ShapefileDataStore(shapefile);
			FeatureSource source = store.getFeatureSource();
			
			File styleFile = new File(styleFileName);
			StyleFactory fac = CommonFactoryFinder.getStyleFactory(null);
			SLDParser parser = new SLDParser(fac, styleFile);
			style = parser.readXML()[0];
			
			FeatureCollection collection = source.getFeatures();
			
			System.out.println("FC ID: " + collection.getSchema().getName());
			
			addLayer(new RepastMapLayer(collection, style));

//			dataFileName = "sampleData/archsites.shp"; 
//			styleFileName = "sampleData/archsi.xml"; 
//			store = new IndexedShapefileDataStore(new File(dataFileName).toURL());
//			source = store.getFeatureSource();
//			styleFile = new File(styleFileName);
//			fac = CommonFactoryFinder.getStyleFactory(null);
//			parser = new SLDParser(fac, styleFile);
//			style = parser.readXML()[0];
//
//			addLayer(new RepastMapLayer(source, style, "Sites"));
			
			
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
		imageFile = PMarqueeZoomIn.class.getResource("mActionZoomIn.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put(Action.SHORT_DESCRIPTION, "Zoom In");
		mapPanel.addTool(new PMarqueeZoomIn(context), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Zoom Out");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = PMarqueeZoomOut.class.getResource("mActionZoomOut.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put(Action.SHORT_DESCRIPTION, "Zoom Out");
		mapPanel.addTool(new PMarqueeZoomOut(context), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Pan");
		toolParams.put(Action.SHORT_DESCRIPTION, "Pan the map");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = PMarqueeZoomOut.class.getResource("move.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		toolParams.put("DEFAULT", new Boolean(true));
		mapPanel.addTool(new PGISPanTool(context, canvas), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Zoom To Previous");
		toolParams.put(Action.SHORT_DESCRIPTION, "Zoom to the previous Extent");
		imageFile = PMarqueeZoomOut.class.getResource("mActionZoomFullExtent.png");
		image = Toolkit.getDefaultToolkit().getImage(imageFile);
		toolParams.put(Action.SMALL_ICON, new ImageIcon(image));
		// mapPanel.addButton(new PZoomToPreviousExtent(), toolParams);
		toolParams = new HashMap<String, Object>();
		// toolParams.put(Action.NAME, "Distance");
		toolParams.put(ToolManager.TOGGLE, true);
		imageFile = DistanceTool.class.getResource("ruler.png");
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
	}
	
	class TimedTask extends TimerTask{

		 public TimedTask() {
			
		}
		
		@Override
		public void run() {
			for (Layer layer : layerList){
				context.removeLayer(layer);
			}
			
			System.out.println("All layers removed.");
			
			for (FeatureSource source : featureSourceStyleMap.keySet()){
				addLayer(new RepastMapLayer(source, featureSourceStyleMap.get(source)));
			}
			
			
//			for (Layer layer : layerList){
//				context.addLayer(layer);
//				
//			}

		}	
	}
}