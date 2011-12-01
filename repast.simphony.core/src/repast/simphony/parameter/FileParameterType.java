package repast.simphony.parameter;

import simphony.util.messages.MessageCenter;

import java.io.File;
import java.io.IOException;

/**
 * ParameterType and StringConverter for a File type.
 *
 * @author Nick Collier
 */
public class FileParameterType implements ParameterType<File>, StringConverter<File> {

  private static MessageCenter msg = MessageCenter.getMessageCenter(FileParameterType.class);


  /**
   * Gets the Java class associated with this type, i.e. a File.
   *
   * @return the Java class associated with this type.
   */
  public Class<File> getJavaClass() {
    return File.class;
  }

  private File fileFromString(String val) {
    if (val == null) val = "";
    return new File(val);
    /*
   if (val.trim().length() != 0 && !file.exists()) {
      msg.warn("Creating File parameter for non-existent file");
    }
    */
  }

  /**
   * Creates a File from a path string.
   *
   * @param val a path.
   * @return the created File
   * @throws ParameterFormatException not thrown
   */
  public File getValue(String val) throws ParameterFormatException {
    return fileFromString(val);

  }

  /**
   * Gets a StringConverter that can be used to convert
   * objects of this ParameterType to and from strings.
   *
   * @return a StringConverter for objects of this parameter type.
   */
  public StringConverter<File> getConverter() {
    return this;
  }

  /**
   * Returns the path of the file.
   *
   * @param obj the Object to convert.
   * @return the path of the file.
   */
  public String toString(File obj) {
    String path;
    try {
      path = obj.getCanonicalPath();
    } catch (IOException ex) {
      path = obj.getAbsolutePath();
    }
    return path;
  }

  /**
   * Returns a new File with the specified path
   *
   * @param path the file path
   * @return the new File.
   */
  public File fromString(String path) {
    return fileFromString(path);
  }
}
