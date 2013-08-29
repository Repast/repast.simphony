package repast.simphony.batch.ssh;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import repast.simphony.batch.BatchConstants;

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
   * @param session
   * @param modelArchive
   * @throws IOException
   */
  public File configure(Session session, Configuration config)
      throws ModelArchiveConfiguratorException {
    File file = null;
    ZipFile zip = null;
    ZipOutputStream out = null;
    
    try {
      file = File.createTempFile(session.getUser() + "_" + session.getHost(), ".zip");
      out = new ZipOutputStream(new FileOutputStream(file));

      zip = new ZipFile(config.getModelArchive());
      for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
        ZipEntry entry = iter.nextElement();
        if (!entry.getName().equals(BatchConstants.LOCAL_RUN_PROPS_FILE)) {
          out.putNextEntry(entry);
          if (!entry.isDirectory()) {
            copy(zip.getInputStream(entry), out);
          }
          out.closeEntry();
        }
      }

      ZipEntry entry = new ZipEntry(BatchConstants.LOCAL_RUN_PROPS_FILE);
      out.putNextEntry(entry);
      String params = config.getBatchParamsFile();
      if (!params.startsWith("./"))
        params = "./" + params;
      String contents = PROP_FILE_CONTENTS + "instance.count = " + session.getInstances() + "\n"
          + "batch.parameter.file = " + params;
      contents += "\n" + BatchConstants.VM_ARGS + " = " + config.getVMArguments();
      out.write(contents.getBytes());
      out.closeEntry();

      entry = new ZipEntry(PARAMS_FILE);
      out.putNextEntry(entry);
      copy(new BufferedInputStream(new FileInputStream(session.getInput())), out);
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
