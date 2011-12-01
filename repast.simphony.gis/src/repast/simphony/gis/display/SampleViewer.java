package repast.simphony.gis.display;

import org.apache.log4j.BasicConfigurator;
import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import repast.simphony.gis.tools.PGISPanTool;
import repast.simphony.gis.tools.PMarqueeZoomIn;
import repast.simphony.gis.tools.PMarqueeZoomOut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleViewer {

    private MapContext context;

    private List<JComponent> comps = new ArrayList<JComponent>();
	private PiccoloMapPanel panel;

	/**
     * to run this, you call SampleViewer with two arguments. The first is the
     * location of the shapefile you wish to see. The second is the name of the
     * sld file you want to use for styling.
     *
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            String dataFileName = "data/chicago/BioWatch_Sites17nov05.shp";
            String styleFileName = "data/chicago/biowatch.xml";
            SampleViewer viewer = new SampleViewer();
            viewer.run(dataFileName, styleFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void run(WindowListener listener, MapContext context) {
		this.context = context; 
		display(listener);
	}

	public PGISCanvas getCanvas() {
		return panel.getCanvas();
	}

    public void run(MapLayer layer) {
        context = new DefaultMapContext();
        context.addLayer(layer);
        display(null);
    }

    public void addComponent(JComponent comp) {
        comps.add(comp);
    }

   private JFrame display(WindowListener listener) {
        JFrame frame = new JFrame();
	    if (listener != null) frame.addWindowListener(listener);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        panel = new PiccoloMapPanel(context);
        // Add a Zoom in tool
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Action.NAME, "Zoom In");
        panel.addTool(new PMarqueeZoomIn(context), params);
        // Add a Zoom out tool
        params = new HashMap<String, Object>();
        params.put(Action.NAME, "Zoom Out");
        panel.addTool(new PMarqueeZoomOut(context), params);
        // Add a Panning tool
        params = new HashMap<String, Object>();
        params.put(Action.NAME, "Pan");
        panel.addTool(new PGISPanTool(context, panel.getCanvas()), params); // PiccoloMapPanel
        // ships with a
        // default panning
        // tool

        for (JComponent comp : comps) {
            panel.getToolBar().add(comp);
        }
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
	      return frame;
    }

    private void run(String dataFileName, String styleFileName) throws IOException,
            FileNotFoundException {
        File shapefileName = new File(dataFileName);
        DataStore ds = new ShapefileDataStore(shapefileName.toURL());

        File styleFile = new File(styleFileName);
        StyleFactory fac = StyleFactoryFinder.createStyleFactory();

        SLDParser parser = new SLDParser(fac, styleFile);
        Style style = parser.readXML()[0];

        System.out.println(style);
        context = new DefaultMapContext();
        context.addLayer(ds.getFeatureSource(ds.getTypeNames()[0]), style);
        System.out.println(context.getAreaOfInterest());
        System.out.println(context.getLayerBounds());
    }
}
