package repast.simphony.freezedry.datasource;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.lang.StringEscapeUtils;
import repast.simphony.freezedry.AbstractDataSource;
import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryingException;
import repast.simphony.util.SystemConstants;
import simphony.util.messages.MessageCenter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DelimitedFileDataSource extends AbstractDataSource<DFRowData> {
  public static final char DEFAULT_DELIMITER = ',';

  private static final MessageCenter LOG = MessageCenter.getMessageCenter(DelimitedFileDataSource.class);

  protected transient HashMap<String, CSVWriter> writerMap;

  protected String path;
  protected boolean reading = false;

  private transient HashMap<String, CSVWriter> childWriters;

  private transient HashMap<String, CSVReader> typeReaders;

  private transient HashMap<String, List<String>> typeFields;

  private transient HashMap<String, CSVReader> childReaders;

  private Set<String> writtenFiles = new HashSet<String>();

  private char delimiter = DEFAULT_DELIMITER;
  private String zipFileName;


  public DelimitedFileDataSource(String zipFileName) {
    this(zipFileName, DEFAULT_DELIMITER);
  }


  public DelimitedFileDataSource(String zipFileName, char delimiter) {
    this(zipFileName, delimiter, new File(zipFileName).exists());
  }

  public DelimitedFileDataSource(String zipFileName, char delimiter, boolean read) {
    this.delimiter = delimiter;
    this.reading = read;
    this.zipFileName = zipFileName;
    if (reading) unzip(zipFileName);
    else createDirectory(zipFileName);
    setupMaps();
  }

  private void unzip(String zipFile) {
    int bufSize = 1024;
    try {
      File file = new File(zipFile);
      createDirectory(file.getParentFile().getAbsolutePath());
      ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
      File parent = new File(path);
      ZipEntry entry;
      while ((entry = in.getNextEntry()) != null) {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(parent, entry.getName())),
                bufSize);
        byte[] buf = new byte[bufSize];
        int len;
        while ((len = in.read(buf)) != -1) {
          out.write(buf, 0, len);
        }
        out.flush();
        out.close();
      }
      in.close();
    } catch (IOException ex) {

    }

  }

  private void createDirectory(String zipFileName) {
    int index = 0;
    File f = new File(zipFileName);
    String parentPath = f.getParentFile().getAbsolutePath();
    String pathName = parentPath + File.separator + "tmp_" + index++;
    f = new File(pathName);
    while (f.exists()) {
      pathName = parentPath + File.separator + "tmp_" + index++;
      f = new File(pathName);

    }
    f.mkdirs();
    path = f.getAbsolutePath();
  }

  /**
   * Resets this data source by deleting all the files in the current directory.
   */
  public void reset() {
    //File f = new File(directory);
    //for (File file : f.listFiles()) {
    //  file.delete();
    //}
  }

  /**
   * Closes this data source.
   *
   * @throws repast.simphony.freezedry.FreezeDryingException
   *
   */
  @Override
  public void close() throws FreezeDryingException {
    try {
      if (!reading) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String tmp = zipFileName;
        if (tmp.endsWith(".zip")) {
          tmp = tmp.substring(0, tmp.length() - 4);
        }

        File zipFile = new File(new File(path).getParentFile(), new File(tmp).getName() + "_" +
                format.format(new Date()) + ".zip");
        if (!zipFile.exists()) zipFile.createNewFile();
        byte[] buf = new byte[1024];

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        for (String path : writtenFiles) {
          File file = new File(path);
          FileInputStream in = new FileInputStream(file);
          out.putNextEntry(new ZipEntry(file.getName()));
          int len;
          while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
          }
          out.closeEntry();
          in.close();
        }
        out.close();
      }
      File dir = new File(path);
      for (File file : dir.listFiles()) {
        file.delete();
      }
      dir.delete();
    } catch (IOException ex) {
      throw new FreezeDryingException("Error while zipping freezedry files", ex);
    }
  }

  /**
   * This is needed because the fields are marked transient and if we serialize with XStream the constructor isn't called,
   * so these wouldn't be initialized.
   */
  private void setupMaps() {
    writerMap = new HashMap<String, CSVWriter>();
    childWriters = new HashMap<String, CSVWriter>();
    typeReaders = new HashMap<String, CSVReader>();
    typeFields = new HashMap<String, List<String>>();
    childReaders = new HashMap<String, CSVReader>();
  }

  protected void startTypeRead(Class clazz) throws FreezeDryingException {
    setupMaps();
    CSVReader reader = getReader(getType(clazz));

    try {
      String[] line = reader.readNext();
      if (line == null) {
        throw new FreezeDryingException("Empty Data Source");
      }

      List<String> fields = buildHeader(line);

      typeFields.put(getType(clazz), fields);
    } catch (IOException ex) {
      throw new FreezeDryingException(ex);
    }
  }

  @Override
  protected DFRowData readRow(Class clazz, String key) throws FreezeDryingException {
    try {
      String[] line = typeReaders.get(getType(clazz)).readNext();

      if (line == null) {
        return null;
      }

      return new DFRowData(line, 0);
    } catch (IOException ex) {
      throw new FreezeDryingException(ex);
    }
  }

  protected CSVReader getReader(String className) throws FreezeDryingException {
    try {
      if (typeReaders.get(className) == null) {
        File file = new File(path + File.separator + className);
        if (!file.exists()) file = new File(path + File.separator + className + ".csv");
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(file)), delimiter);

        typeReaders.put(className, reader);
      }

      return typeReaders.get(className);
    } catch (IOException ex) {
      throw new FreezeDryingException(ex);
    }
  }

  protected void endTypeRead(Class clazz) {
    try {
      if (typeReaders.containsKey(clazz.getName())) {
        typeReaders.get(clazz.getName()).close();
      }
    } catch (IOException ex) {
      LOG.warn("Exception when finishing type read on class '" + clazz + "'.", ex);
    } finally {
      typeReaders.remove(clazz.getName());
    }
  }

  protected Object convert(String field, String val) {
    if (val.startsWith("\"")) {
      return readString(val);
    } else {
      return readNonString(val);
    }
  }

  @Override
  protected void startTypeWrite(FreezeDryedObject object) throws FreezeDryingException {
    setupMaps();
    String type = getType(object);


    try {
      if (!writerMap.containsKey(type)) {
        File file = new File(path + File.separator + type + ".csv");
        CSVWriter writer;

        if (file.exists()) {
          writer = new CSVWriter(new FileWriter(file, true), delimiter,
                  CSVWriter.DEFAULT_QUOTE_CHARACTER, SystemConstants.LINE_SEPARATOR);
        } else {
          file.createNewFile();
          writtenFiles.add(file.getAbsolutePath());
          writer = new CSVWriter(new FileWriter(file), delimiter,
                  CSVWriter.DEFAULT_QUOTE_CHARACTER, SystemConstants.LINE_SEPARATOR);

          // write header
          List<String> cols = getColumns(object);
          String[] array = new String[cols.size()];
          writer.writeNext(cols.toArray(array));
        }
        writerMap.put(type, writer);
      }
    } catch (IOException ex) {
      throw new FreezeDryingException("Error writing type to file '" + path
              + File.separator + type + "'", ex);
    }
  }

  @Override
  protected void writeRow(FreezeDryedObject object, Map<String, Object> row)
          throws FreezeDryingException {
    CSVWriter writer = writerMap.get(getType(object));

    List<String> cols = getColumns(object);
    try {
      String[] array = new String[cols.size()];
      for (int i = 0; i < cols.size(); i++) {
        array[i] = row.get(cols.get(i)).toString();
      }
      writer.writeNext(array);
    } catch (Exception ex) {
      throw new FreezeDryingException(ex);
    }
  }

  @Override
  protected void finishTypeWrite(FreezeDryedObject object) throws FreezeDryingException {
    if (writerMap.containsKey(getType(object))) {
      CSVWriter writer = writerMap.get(getType(object));

      try {
        writer.close();
      } catch (IOException ex) {
        throw new FreezeDryingException(ex);
      }

      writerMap.remove(getType(object));
    } else {
      LOG.warn("Finishing a write on type '" + getType(object)
              + "' when it appears the write hasn't started.");
    }
  }

  public List<String> buildHeader(String[] header) {
    return Arrays.asList(header);
  }

  protected Object getFieldValue(DFRowData row, String field) {
    try {
      String val = row.nextString();
      if (val.startsWith("\'") || val.startsWith("\"")) {
        return readString(val);
      } else {
        return readNonString(val);
      }
    } catch (RuntimeException ex) {
      LOG.info("Error fetching field value for field '" + field + "' and row '" + row + "'", ex);
      throw ex;
    }

  }

  private String readString(String unprocessed) {
    String value = unprocessed.substring(1, unprocessed.length() - 1);
    return StringEscapeUtils.unescapeJava(value);
  }

  private Object readNonString(String unprocessed) {
    if (unprocessed.equalsIgnoreCase("true")) {
      return Boolean.TRUE;
    } else if (unprocessed.equalsIgnoreCase("false")) {
      return Boolean.FALSE;
    }
    return Double.parseDouble(unprocessed);
  }

  @Override
  protected void startChildrenRead(FreezeDryedObject fdo) throws FreezeDryingException {
    try {
      if (!childReaders.containsKey(getType(fdo))) {
        File file = new File(path + File.separator + fdo.getType().getName() + CHILDREN_MARKER);
        if (!file.exists()) {
          return;
        }

        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(file)));

        // read in the header row and discard
        reader.readNext();

        childReaders.put(getType(fdo), reader);
      }
    } catch (IOException ex) {
      childReaders.remove(getType(fdo));
      throw new FreezeDryingException(ex);
    }
  }

  @Override
  protected DFRowData readChildsRow(FreezeDryedObject parent, String id) throws FreezeDryingException {
    try {
      if (!childReaders.containsKey(getType(parent))) {
        return null;
      }

      CSVReader reader = childReaders.get(getType(parent));

      String[] line = reader.readNext();

      if (line == null) {
        return null;
      }

      return new DFRowData(line, 0);
    } catch (IOException ex) {
      throw new FreezeDryingException(ex);
    }
  }

  @Override
  protected void finishChildrenRead(FreezeDryedObject parent) {
    try {
      if (childReaders.containsKey(getType(parent))) {
        childReaders.get(getType(parent)).close();
      }

    } catch (IOException e) {
      LOG.warn("Error while reading children for parent '" + parent
              + "'. Not loading any children for this object.");
    } finally {
      childReaders.remove(getType(parent));
    }
  }

  @Override
  protected String writeByteArray(FreezeDryedObject object, String fieldName, byte[] bs) throws FreezeDryingException {
    String id = object.getId();

    String fileName = path + File.separator + id + "_" + fieldName + ".ser";
    OutputStream os = null;
    try {
      File file = new File(fileName);
      if (!file.exists()) {
        file.createNewFile();
        writtenFiles.add(file.getAbsolutePath());
      }
      os = new FileOutputStream(file);
      os.write(bs);

      return fileName;

    } catch (IOException e) {
      throw new FreezeDryingException(e);
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException e) {
          throw new FreezeDryingException(e);
        }
      }
    }
  }

  @Override
  protected byte[] readByteArray(FreezeDryedObject fdo, String field, Object bytesId) throws FreezeDryingException {
    String unprocessed = (String) bytesId;

    // TODO: implement this
    String fileName = unprocessed.substring(1, unprocessed.length() - 1);
    InputStream os = null;
    byte[] ba = null;
    try {
      File file = new File(fileName);
      os = new FileInputStream(file);
      ba = new byte[(int) file.length()];
      os.read(ba);
    } catch (IOException e) {
      throw new FreezeDryingException(e);
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException e) {
          throw new FreezeDryingException(e);
        }
      }
    }
    return ba;
  }

  @Override
  protected void startChildrenWrite(FreezeDryedObject fdo) throws FreezeDryingException {
    File file = new File(path + File.separator + getType(fdo) + CHILDREN_MARKER);
    CSVWriter writer = null;

    try {
      if (childWriters.containsKey(getType(fdo))) {
        return;
      }

      if (file.exists())
        writer = new CSVWriter(new FileWriter(file, true), delimiter,
                CSVWriter.DEFAULT_QUOTE_CHARACTER, SystemConstants.LINE_SEPARATOR);
      else {
        file.createNewFile();
        writtenFiles.add(file.getAbsolutePath());
        writer = new CSVWriter(new FileWriter(file), delimiter,
                CSVWriter.DEFAULT_QUOTE_CHARACTER, SystemConstants.LINE_SEPARATOR);
        writer.writeNext(new String[]{PARENT_ID_COL, CHILD_CLASS_COL, CHILD_ID_COL});
      }

      childWriters.put(getType(fdo), writer);

    } catch (IOException ex) {
      LOG.error("Error beginning write of children for '" + fdo + "'.", ex);
      throw new FreezeDryingException(ex);
    }
  }

  @Override
  protected void writeChildData(FreezeDryedObject fdo, HashMap<String, Object> child) throws FreezeDryingException {
    CSVWriter writer = childWriters.get(getType(fdo));

    try {
      writer.writeNext(new String[]{
              getValue(child.get(PARENT_ID_COL).toString()).toString(),
              getValue(child.get(CHILD_CLASS_COL).toString()).toString(),
              getValue(child.get(CHILD_ID_COL).toString()).toString()});
    } catch (Exception ex) {
      LOG.error("Error writing child data for '" + fdo + "', child='" + child + "'.", ex);
      throw new FreezeDryingException(ex);
    }
  }

  @Override
  protected void finishChildrenWrite(FreezeDryedObject fdo) {
    CSVWriter writer = childWriters.get(getType(fdo));

    try {
      if (writer != null) {
        childWriters.remove(getType(fdo));
        writer.close();
      }
    } catch (IOException ex) {
      LOG.warn("Error closing children file for '" + fdo + "'.", ex);
    }
  }

  @Override
  protected List<String> getFields(String type) {
    return typeFields.get(type);
  }

  public char getDelimiter() {
    return delimiter;
  }

  public String getPath() {
    return path;
  }

//	public static void main(String[] args) throws IOException {
//		String string = "\"\"a\"\", \"\"b\"\", \"\"\"c\"\", \"\"d\"\"\"";
//		StringTokenizer izer = new StringTokenizer(string);
//		System.out.println("string toks: (" + string + ")");
//		while (izer.hasMoreTokens()) {
//			System.out.println(izer.nextToken());
//		}
//		System.out.println("scanner toks");
//		Scanner scanner = new Scanner(string);
//		while (scanner.hasNext()) {
//			System.out.println(scanner.next());
//		}
//		StreamTokenizer streamReader = new StreamTokenizer(new StringReader(string));
//		System.out.println("string toks: (" + string + ")");
//		streamReader.quoteChar('"');
//		while (streamReader.nextToken() != StreamTokenizer.TT_EOF) {
//			System.out.println(streamReader.sval);
//		}
//		System.out.println("scanner regex");
//		Scanner scannerReg = new Scanner(string);
//		while (scanner.hasNext("((?[^\",\\r\\n]*)|\"(?([^\"]|\"\")*)\")(,))")) {
//			System.out.println(scannerReg.next("((?[^\",\\r\\n]*)|\"(?([^\"]|\"\")*)\")(,))"));
//		}
//	}
}
