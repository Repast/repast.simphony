package repast.simphony.batch.ssh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 
 * 
 * @author Nick Collier
 */
public class ModelArchiveConfigurator {

  // 4MB buffer
  private static final byte[] BUFFER = new byte[4096 * 1024];

  private static final String PARAMS_FILE = "scenario.rs/batch_unrolled_params.txt";
  private static final String PROP_FILE_CONTENTS = "unrolled.batch.parameter.file=./scenario.rs/batch_unrolled_params.txt\n"
      + "scenario.directory=./scenario.rs\n"
      + "working.directory=./\n"
      + "repast.lib.directory=./lib\n";

  /**
   * Configures the existing model archive to work with the specified remote.
   * 
   * @param remote
   * @param modelArchive
   * @throws IOException
   */
  public File configure(Session remote, Configuration config)
      throws ModelArchiveConfiguratorException {
    File file = null;
    ZipFile zip = null;
    ZipOutputStream out = null;
    
    try {
      file = File.createTempFile(remote.getUser() + "_" + remote.getHost(), ".zip");
      out = new ZipOutputStream(new FileOutputStream(file));

      zip = new ZipFile(config.getModelArchive());
      for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
        ZipEntry entry = iter.nextElement();
        if (!entry.getName().equals(Constants.LOCAL_BATCH_PROPS_FNAME)) {
          out.putNextEntry(entry);
          if (!entry.isDirectory()) {
            copy(zip.getInputStream(entry), out);
          }
          out.closeEntry();
        }
      }

      ZipEntry entry = new ZipEntry(Constants.LOCAL_BATCH_PROPS_FNAME);
      out.putNextEntry(entry);
      String params = config.getBatchParamsFile();
      if (!params.startsWith("./"))
        params = "./" + params;
      String contents = PROP_FILE_CONTENTS + "instance.count = " + remote.getInstances() + "\n"
          + "batch.parameter.file = " + params;
      out.write(contents.getBytes());
      out.closeEntry();

      entry = new ZipEntry(PARAMS_FILE);
      out.putNextEntry(entry);
      out.write(remote.getInput().getBytes());
      out.closeEntry();
      out.close();

    } catch (IOException ex) {
      throw new ModelArchiveConfiguratorException(ex);
    } finally {
      try {
      if (out != null) out.close();
      if (zip != null) zip.close();
      } catch (IOException e) {}
    }

    return file;
  }

  private void copy(InputStream input, OutputStream output) throws IOException {
    int bytesRead;
    while ((bytesRead = input.read(BUFFER)) != -1) {
      output.write(BUFFER, 0, bytesRead);
    }
  }

}
