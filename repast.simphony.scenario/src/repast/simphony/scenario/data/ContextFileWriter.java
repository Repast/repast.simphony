package repast.simphony.scenario.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import repast.simphony.parameter.StringConverterFactory;

/**
 * Writes a context file from ContextData.
 * 
 * @author Nick Collier
 */
public class ContextFileWriter {
  
  /**
   * Writes the specified ContextData to the specified file.
   * The ContextData and its decendent subcontexts will be written.
   * 
   * @param file the file to write to
   * @param data the ContextData to write
   * 
   * @throws IOException if there is an error writing the data.
   */
  public void write(File file, ContextData data) throws IOException {
    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writeContextData(writer, data);
    writer.close();
    if (writer.checkError()) {
      throw new IOException("Error writing context data to file '" + file.getCanonicalPath() + "'");
    }
  }
  
  private void writeContextData(PrintWriter writer, ContextData data) {
    writer.print("<context id=\"");
    writer.print(data.getId());
    if (data.getContextClassName() != null) {
      writer.print("\" class=\"");
      writer.print(data.getContextClassName());
    }
    writer.println("\">");
    
    for (int i = 0; i < data.getProjectionCount(); i++) {
      ProjectionData proj = data.getProjection(i);
      writer.print("<projection id=\"");
      writer.print(proj.getId());
      writer.print("\" type=\"");
      writer.print(proj.getType());
      if (proj.getAttributeCount() == 0) writer.println("\" />");
      else writer.println("\" >");
      
      for (Attribute attrib : proj.attributes()) {
        writeAttribute(writer, attrib);
      }  
      
      if (proj.getAttributeCount() > 0) {
        writer.println("</projection>");
      }
    }
    
    for (Attribute attrib : data.attributes()) {
      writeAttribute(writer, attrib);
    }
    
    /*
    for (int i = 0; i < data.getAgentCount(); i++) {
      String agentClass = data.getAgentData(i).getClassName();
      writer.print("<agent class=\"");
      writer.print(agentClass);
      writer.println("\" />");
    }
    */
    
    for (ContextData child : data.subContexts()) {
      writeContextData(writer, child);
    }
    
    writer.println("</context>");
  }
  
  private void writeAttribute(PrintWriter writer, Attribute attrib) {
    writer.print("<attribute id=\"");
    writer.print(attrib.getId());
    writer.print("\" value=\"");
    writer.print(attrib.getValue());
    writer.print("\" display_name=\"");
    writer.print(attrib.getDisplayName());
    writer.print("\" type=\"");
    writer.print(AttributeFactory.getTypeName(attrib.getType()));
    
    if (StringConverterFactory.instance().getConverter(attrib.getType()) == null) {
      // not part of the default converters so write it out
      writer.print("\" converter=\"");
      writer.print(attrib.getConverter().getClass().getName());
    }
    
    writer.println("\" />");
  }
}
