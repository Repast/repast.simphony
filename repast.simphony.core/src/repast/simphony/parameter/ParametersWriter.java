package repast.simphony.parameter;

import org.apache.commons.lang3.Range;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * Writes the current values of a Parameters object out to an xml file. The
 * format consists of a name value pair where the value is a string
 * representation of parameter value. For example,
 * 
 * <pre>
 * &lt;parameters type=&quot;valuesOnly&quot;&gt;
 * 			&lt;parameter name=&quot;double&quot; value=&quot;3.2343&quot;/&gt;
 * &lt;/parameters&gt;
 * </pre>
 * <p>
 * The full parameter format provides the full parameter definition, including
 * the name, type, defaultValue, and optionally the displayName, value
 * constraints, and isReadOnly. If the type is a non-primitive (number, boolean
 * or String), then a converter must also be specified. For example,
 * 
 * <pre>
 * &lt;parameters&gt;
 * 				...finish example
 * &lt;/parameters&gt;
 * </pre>
 * 
 * @author Nick Collier
 * @author Michelle Kehrer
 */
public class ParametersWriter {

  private void write(Parameters params, Writer writer, String templateFile) throws IOException {
    VelocityContext context = new VelocityContext();
    context.put("parameters", params);
    context.put("NULL", Parameters.NULL);

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

  public String writeValuesToString(Parameters params) throws IOException {
    StringWriter writer = new StringWriter();
    write(params, writer, "params.vt");
    return writer.toString();
  }

  public void writeValuesToFile(Parameters params, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    write(params, writer, "params.vt");
    writer.close();
  }

  public void writeSpecificationToFile(Parameters params, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    write(params, writer, "parameters.vt");
    writer.close();
  }

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      props.put("resource.loader", "class");
      props.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
      props.put("class.resource.loader.class",
	  "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
      Velocity.init(props);

      DefaultParameters p = new DefaultParameters();
      p.addParameter("foo", String.class, "Hello", true);
      p.addParameter("bar", int.class, 12, false);
      p.addParameter("test", "Test Number", int.class, 10, false);

      p.addConstraint("foo", Arrays.asList("Hello", "Test 1", "test2", "test 3"));
      //p.addConstraint("bar", Range.between(10, 15));

      ParametersWriter writer = new ParametersWriter();
      writer.writeValuesToFile(p, new File("/Users/kehrer/tmp/params.xml"));
      writer.writeSpecificationToFile(p, new File("/Users/kehrer/tmp/parameters.xml"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
