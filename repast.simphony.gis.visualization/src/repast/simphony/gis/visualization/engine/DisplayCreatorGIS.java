package repast.simphony.gis.visualization.engine;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.xml.styling.SLDParser;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.DefaultDisplayData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.engine.DisplayCreationException;
import repast.simphony.visualization.engine.DisplayCreator;
import repast.simphony.visualization.gis.DisplayGIS;

/**
 * Display creator for 2D GIS displays.
 * 
 * @author Nick Collier
 * 
 * @deprecated Use 3D GIS display instead
 */
@Deprecated
public class DisplayCreatorGIS implements DisplayCreator {
	 protected Context<?> context;
	 protected GISDisplayDescriptor descriptor; 
	 
  public DisplayCreatorGIS(Context<?> context, GISDisplayDescriptor descriptor) {
  	this.context = context;
    this.descriptor = descriptor;
  }
  
  @SuppressWarnings("unchecked")
  protected DefaultDisplayData<?> createDisplayData() {
    DefaultDisplayData<?> data = new DefaultDisplayData(context);
    for (ProjectionData pData : descriptor.getProjections()) {
      data.addProjection(pData.getId());
    }
  
    return data;
  }

  private FeatureSource createFeatureSource(File shpFile) throws IOException {
    ShapefileDataStore dataStore = new ShapefileDataStore(shpFile.toURL());
    return dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.engine.DisplayCreator#createDisplay()
   */
  @SuppressWarnings("unchecked")
  public IDisplay createDisplay() throws DisplayCreationException {
    try {
      DefaultDisplayData<?> data = createDisplayData();

      DisplayGIS display = new DisplayGIS(data);

      for (String agentName : descriptor.agentClassStyleNames()) {
        String styleXML = descriptor.getStyleClassName(agentName);
        SLDParser parser = new SLDParser(new StyleFactoryImpl(), new StringReader(styleXML));
        Style style = parser.readXML()[0];
        display.registerAgentStyle(agentName, style, descriptor
            .getLayerOrder(agentName));
      }

      Map<String, String> shpMap = (Map<String, String>) descriptor
          .getProperty(DisplayGIS.SHP_FILE_STYLE_PROP);
      if (shpMap != null) {
        for (Map.Entry<String, String> entry : shpMap.entrySet()) {
          File file = new File(entry.getKey());
          FeatureSource source = createFeatureSource(file);
          Style style = null;
          if (entry.getValue().length() > 0) {
            SLDParser parser = new SLDParser(new StyleFactoryImpl(), new StringReader(entry
                .getValue()));
            style = parser.readXML()[0];
          }
          display.registerFeatureSource(source, style, descriptor.getLayerOrder(entry.getKey()));
        }
      }

      return display;
    } catch (IOException ex) {
      throw new DisplayCreationException(ex);
    }
  }
}
