/**
 * 
 */
package repast.simphony.batch.standalone;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Finds Bundles given BundleData and completes that BundleData.
 * 
 * @author Nick Collier
 */
public class BundleFinder {
  
  public void findBundle(File bundleDir, BundleData data) {
    Map<String, BundleData> bundles = new HashMap<String, BundleData>();
    bundles.put(data.getName(), data);
    
    for (File file : bundleDir.listFiles()) {
      if (file.isFile() && file.getName().endsWith("jar")) {
        if (processJar(file, bundles)) break;
      } else if (file.isDirectory()) {
        if (processDir(file, bundles)) break;
      }
    }
  }

  public void findBundles(File bundleDir, BundleData data) {
    Map<String, BundleData> bundles = new HashMap<String, BundleData>();
    for (BundleData bd : data.requiredBundles()) {
      bundles.put(bd.getName(), bd);
    }

    for (File file : bundleDir.listFiles()) {

      if (file.isFile() && file.getName().endsWith("jar")) {
        processJar(file, bundles);
      } else if (file.isDirectory()) {
        processDir(file, bundles);
      }
    }
  }

  private boolean processJar(File file, Map<String, BundleData> bundles) {
    try {
      JarFile jarFile = new JarFile(file);
      Manifest mf = jarFile.getManifest();
      if (mf != null) {
        return processManifest(mf, bundles, file);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  private boolean processDir(File file, Map<String, BundleData> bundles) {
    try {
      File mfFile = new File(file, "META-INF/MANIFEST.MF");
      if (mfFile.exists()) {
        Manifest mf = new Manifest(new FileInputStream(mfFile));
        return processManifest(mf, bundles, file);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return false;
  }
  
  private boolean processManifest(Manifest mf, Map<String, BundleData> bundles, File bundleLocation) {
    Attributes attributes = mf.getMainAttributes();
    String name = attributes.getValue(BundleDataReader.NAME_KEY);
    name = name.split(";")[0];
    String version = attributes.getValue(BundleDataReader.VERSION_KEY);
   
    if (name != null) {
      BundleData bd = bundles.get(name);
      if (bd != null) {
        version = version == null ? "" : version;
       
        if (bd.getVersion().lessEqual(new Version(version))) {
          bd.setLocation(bundleLocation.getAbsoluteFile());
          BundleDataReader reader = new BundleDataReader();
          reader.read(mf, bd);
          return true;
        }
      }
    }
    return false;
  }
}
