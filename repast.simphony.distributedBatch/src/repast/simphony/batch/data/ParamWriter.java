package repast.simphony.batch.data;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import repast.simphony.batch.setup.BatchMainSetup;
import repast.simphony.parameter.Parameters;

import java.io.*;

/**
 * Writes a parameter file based on params.vt or parameters.vt. See ParameterWriter in repast.simphony.core for a similar class.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class ParamWriter {

/**
 * Method writes parameters to UTF-8 xml file.
 * @param params the parameters to write
 * @param writer the writer object
 * @param templateFile the string template to use to write
 * @throws IOException
 */
  private void write(Parameters params, Writer writer, String templateFile) throws IOException {
    VelocityContext context = new VelocityContext();
    context.internalPut("parameters", params);
    context.internalPut("NULL", Parameters.NULL);

    String template = getClass().getPackage().getName();
    template = template.replace('.', '/');
    template = template + "/" + templateFile;
    try {
      Velocity.mergeTemplate(template, "UTF-8", context, writer);
    } catch (Exception ex) {
      IOException ioEx = new IOException("Error writing parameters");
      ioEx.initCause(ex);
      throw ioEx;
    }
  }

  /**
   * Writes to params.vt formatted file.
   * @param params the parameters
   * @return a string of the file output
   * @throws IOException an exception thrown in writing the file
   */
  public String writeValuesToString(Parameters params) throws IOException {
    StringWriter writer = new StringWriter();
    write(params, writer, "params.vt");
    return writer.toString();
  }

  /**
   * Writes to the parameters.vt format.
   * @param params the parameters
   * @param file a file to send to the FileWriter
   * @throws IOException exception throw in writing the file
   */
  public void writeValuesToFile(Parameters params, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    if (BatchMainSetup.MJB_SUGGEST) {
    	write(params, writer, "parameterValues.vt");
    } else {
    write(params, writer, "parameters.vt");
    }
    writer.close();
  }

  /**
   * Method to write a file to a parameters.vt format
   * @param params the parameters
   * @param file the file to give to the FileWriter
   * @throws IOException exception thrown in writing the file
   */
  public void writeSpecificationToFile(Parameters params, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    write(params, writer, "parameters.vt");
    writer.close();
  }
}
