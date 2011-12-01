package repast.simphony.gis.display;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.styling.*;
import repast.simphony.gis.legend.MapLegend;
import repast.simphony.gis.tools.*;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapViewer {

	private JFrame frame;

	private PiccoloMapPanel mapPanel;

	private MapLegend legend;

	private MapContext context;

	private JMenuBar bar = new JMenuBar();

	private StyleBuilder builder = new StyleBuilder();

	private File currentDirectory = new File(".");

	private StatusBar statusBar = new StatusBar();

	private Properties props;

	public MapViewer() {
		context = new DefaultMapContext();
		context.setAreaOfInterest(new Envelope(-90, -90, -90, 90));
		mapPanel = new PiccoloMapPanel(context);
		legend = new MapLegend(context, "legend");
		File propsFile = new File("mapview.properties");
		props = new Properties();
		props.put("currentDirectory", ".");
		if (propsFile.exists()) {
			try {
				props.load(new FileInputStream(propsFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		initTools();
		createLayers();

	}

	private void createLayers() {
		try {
			String dataFileName = "data/chicago/ChicagoMetroCounties.shp"; //BioWatch_Sites17nov05.shp";
			String styleFileName = "data/chicago/ChicagoMetroCounties.xml"; //biowatch.xml";
			IndexedShapefileDataStore store = new IndexedShapefileDataStore(new File(dataFileName).toURL());
			FeatureSource source = store.getFeatureSource();

			File styleFile = new File(styleFileName);
			StyleFactory fac = StyleFactoryFinder.createStyleFactory();
			SLDParser parser = new SLDParser(fac, styleFile);
			Style style = parser.readXML()[0];

			addLayer(new DefaultMapLayer(source, style, "Counties"));

			dataFileName = "data/chicago/BioWatch_Sites17nov05.shp";
			styleFileName = "data/chicago/biosite.xml";
			store = new IndexedShapefileDataStore(new File(dataFileName).toURL());
			source = store.getFeatureSource();

			styleFile = new File(styleFileName);
			fac = StyleFactoryFinder.createStyleFactory();
			parser = new SLDParser(fac, styleFile);
			style = parser.readXML()[0];

			style = builder.createStyle(builder.createPointSymbolizer(builder.createGraphic(null,
							builder.createMark("square", Color.RED), null)));

			addLayer(new DefaultMapLayer(source, style, "Biowatch Sites"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void addLayer(MapLayer layer) {
		context.addLayer(layer);
		try {
			context.setAreaOfInterest(context.getLayerBounds());
		} catch (IOException ex) {
			MessageCenter.getMessageCenter(getClass()).error(
							"Unable to load layer", ex);
		}
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
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				Thread t = new Thread() {
					public void run() {
						File f = new File("./mapview.properties");
						try {
							props.store(new FileOutputStream(f), "");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			}

		});
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
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		MapViewer viewer = new MapViewer();
		viewer.show();
	}
}

/*
JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		Action loadShapefile = new AbstractAction() {

			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser(new File(props
								.getProperty("currentDirectory")));
				chooser.setFileFilter(new FileFilter() {

					@Override
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						if (f.getName().endsWith("shp"))
							return true;
						return false;
					}

					@Override
					public String getDescription() {
						return "Shapefile";

					}

				});
				int result = chooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					// final TimerIcon icon = new TimerIcon(new Dimension(16,
					// 16));
					// icon.setMessage("Loading Shapefile: "
					// + chooser.getSelectedFile().getName());
					// icon.showClock(true);
					//					statusBar.setComponent(icon);
					Thread thread = new Thread() {
						public void run() {
							props.put("currentDirectory", chooser
											.getSelectedFile().getParentFile()
											.getAbsolutePath());
							try {
								Random random = new Random();
								float r = (float) random.nextDouble();
								float g = (float) random.nextDouble();
								float b = (float) random.nextDouble();

								IndexedShapefileDataStore store = new IndexedShapefileDataStore(
												chooser.getSelectedFile().toURL());
								FeatureSource source = store.getFeatureSource();
								if (source.getSchema().getDefaultGeometry()
												.getType().equals(Polygon.class)
												|| source.getSchema()
												.getDefaultGeometry().getType()
												.equals(MultiPolygon.class)) {
									Fill fill = builder.createFill(new Color(r,
													g, b), .4);
									addLayer(new DefaultMapLayer(
													source,
													builder
																	.createStyle(builder
																	.createPolygonSymbolizer(
																	builder
																					.createStroke(),
																	fill))));
								} else if (source.getSchema()
												.getDefaultGeometry().getType().equals(
												LineString.class)
												|| source.getSchema()
												.getDefaultGeometry().getType()
												.equals(MultiLineString.class)) {
									addLayer(new DefaultMapLayer(
													source,
													builder
																	.createStyle(builder
																	.createLineSymbolizer(new Color(
																	r, g, b)))));
								} else if (source.getSchema()
												.getDefaultGeometry().getType().equals(
												Point.class)
												|| source.getSchema()
												.getDefaultGeometry().getType()
												.equals(MultiPoint.class)) {
									addLayer(new DefaultMapLayer(
													source,
													builder
																	.createStyle(builder
																	.createPointSymbolizer(builder
																	.createGraphic(
																	null,
																	builder
																					.createMark(
																					"square",
																					new Color(
																									r,
																									g,
																									b)),
																	null)))));
								} else {
									throw new IllegalArgumentException(
													"Unable to read Shapefile because unknown geometry type");
								}
								// icon.showClock(false);
								ThreadUtilities.runInEventThread(new Runnable() {
									public void run() {
										statusBar.setMessage("");
									}
								});
							} catch (Exception e) {
								MessageCenter.getMessageCenter(getClass())
												.error("Unable to load layer", e);
							}

						}
					};
					thread.start();
				}
			}

		};
		loadShapefile.putValue(Action.NAME, "Load ShapeFile");
		fileMenu.add(loadShapefile);
 */
