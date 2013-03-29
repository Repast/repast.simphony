/**
 * 
 */
package repast.simphony.batch.standalone;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Nick Collier
 */
public class BundleDataReader {

  private static final String REQUIRE_BUNDLE_KEY = "Require-Bundle";
  private static final String BUNDLE_VERSION_KEY = "bundle-version";
  private static final String CLASSPATH_KEY = "Bundle-ClassPath";
  public static final String NAME_KEY = "Bundle-SymbolicName";
  public static final String VERSION_KEY = "Bundle-Version";

  public BundleData reader(InputStream is) throws IOException {
    
    Manifest mf = new Manifest(is);

    Attributes attributes = mf.getMainAttributes();
    String name = attributes.getValue(NAME_KEY);
    String version = attributes.getValue(VERSION_KEY);
    
    name = name == null ? "" : name;
    version = version == null ? "" : version;
    BundleData data = new BundleData(name, new Version(version));
    String line = attributes.getValue(REQUIRE_BUNDLE_KEY);

    parseRequiredBundles(line, data);
    line = attributes.getValue(CLASSPATH_KEY);
    parseClassPath(line, data);

    return data;
  }
  
  private void parseClassPath(String line, BundleData data) {
    if (line != null) {
      String[] paths = line.split(",");
      for (String path : paths) {
        data.addToClassPath(path.trim());
      }
    }
  }

  private void parseRequiredBundles(String line, BundleData data) {
    if (line != null) {
      String[] bundles = line.split(",");
      for (String bundle : bundles) {
        String[] items = bundle.split(";");
        String version = "";
        String name = items[0].trim();
        for (int i = 1; i < items.length; ++i) {
          String item = items[i].trim();
          if (item.startsWith(BUNDLE_VERSION_KEY)) {
            String[] vals = item.split("=");
            version = vals[1].trim().substring(1, vals[1].trim().length() - 1);
          }
        }
        data.addRequiredBundle(new BundleData(name, new Version(version)));
      }
    }

  }

  /**
   * Reads the contents of the specified Manifest into specified BundleData. This
   * only reads the required bundles and class info. 
   * 
   * @param mf
   * @param bd
   */
  public void read(Manifest mf, BundleData bd) {
    Attributes attributes = mf.getMainAttributes();
    String line = attributes.getValue(REQUIRE_BUNDLE_KEY);
    parseRequiredBundles(line, bd);
    line = attributes.getValue(CLASSPATH_KEY);
    parseClassPath(line, bd);
  }
}
