package repast.simphony.context.space.graph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Matrix reader for UCINET dl format files. This will only read
 * square matrices whose rows and columns refer to the same nodes.
 *
 * @author Nick Collier
 * @version $Revision: 1.5 $ $Date: 2004/11/03 19:51:00 $
 */
public class DLMatrixReader implements NetworkMatrixReader {

  private BufferedReader reader;
  private long numNodes = -1;
  private int numMatrices = 1;
  private List<String> labels;
  private List<String> matrixLabels;
  private boolean hasMatrixLabels = false;

  /**
   * Creates a DlReader to read the specified file.
   *
   * @param filename the name of the file to read (in dl format)
   * @throws IOException if the file cannot be read.
   */
  public DLMatrixReader(String filename) throws IOException {
    reader = new BufferedReader(new FileReader(filename));
  }

  /**
   * Creates a DlRedaer to read from the specified InputStream.
   *
   * @param stream the InputStream to read from.
   */
  public DLMatrixReader(InputStream stream) {
    reader = new BufferedReader(new InputStreamReader(stream));
  }

  /**
   * Returns a list of the read AdjacencyMatrices.
   *
   * @return a list of the read AdjacencyMatrices.
   * @throws java.io.IOException if there is an error while attempting to read the matrices
   */
  public List<AdjacencyMatrix> getMatrices() throws IOException {
    readHeader();
    readLabels();
    if (hasMatrixLabels) {
      readMatrixLabels();
    }

    List<AdjacencyMatrix> matrices = new ArrayList<AdjacencyMatrix>();

    for (int i = 0; i < numMatrices; i++) {
      AdjacencyDoubleMatrix m;
      if (labels.size() > 0) {
        m = new AdjacencyDoubleMatrix(labels);
      } else {
        m = new AdjacencyDoubleMatrix((int) this.numNodes, (int) this.numNodes);
      }

      if (hasMatrixLabels) {
        m.setMatrixLabel(matrixLabels.get(i));
      }
      matrices.add(loadData(m));
    }

    return matrices;
  }

  private void readHeader() throws IOException {

    // all this is rather sloppy and naive.

    String header;

    while ((header = reader.readLine()) != null) {
      header = header.trim();
      if (header.length() > 0)
        break;
    }

    if (header == null) {
      throw new IOException("File is not a valid dl file");
    }


    if (!header.startsWith("dl")) {
      throw new IOException("File is not a valid dl file");
    }

    parseForNumNodesMatrices(header);
  }

  private void parseForNumNodesMatrices(String header) throws IOException {

    StringTokenizer tok = new StringTokenizer(header, " ");

    // get the 'dl'
    tok.nextToken();
    numNodes = parseForValue("n", tok, 1);


    if (tok.hasMoreTokens()) {
      numMatrices = (int) parseForValue("nm", tok, 2);
    }

    if (numNodes < 0) {
      throw new IOException("File is not a valid dl file");
    }
  }

  private long parseForValue(String lookFor, StringTokenizer tok, int length) throws
          IOException {
    if (tok.hasMoreTokens()) {
      String token = tok.nextToken();
      if (!token.startsWith(lookFor)) {
        throw new IOException("File is not a valid dl file");
      }

      if (token.length() > length) {
        // next better be an '=' or a ','
        char[] c = token.toCharArray();
        if (c[length] == '=' || c[length] == ',') {
          if (c.length > 1) {
            try {
              if (token.endsWith(",")) {
                token = token.substring(0, token.length() - 1);
              }
              return Long.parseLong(token.substring(length + 1, token.length()));
            } catch (NumberFormatException ex) {
              throw new IOException("File is not a valid dl file");
            }
          } else {
            throw new IOException("File is not a valid dl file");
          }
        } else {
          throw new IOException("File is not a valid dl file");
        }
        // token length == 1 so should more tokens
      } else {
        if (tok.hasMoreTokens()) {
          token = tok.nextToken();
          if (token.startsWith("=")) {
            if (token.length() > 1) {
              try {
                if (token.endsWith(",")) {
                  token = token.substring(0, token.length() - 1);
                }
                return Long.parseLong(token.substring(1, token.length()));
              } catch (NumberFormatException ex) {
                throw new IOException("File is not a valid dl file");
              }
            } else {
              if (tok.hasMoreTokens()) {
                try {
                  token = tok.nextToken();
                  if (token.endsWith(",")) {
                    token = token.substring(0, token.length() - 1);
                  }
                  return Long.parseLong(token);
                } catch (NumberFormatException ex) {
                  throw new IOException("File is not a valid dl file");
                }
              } else {
                throw new IOException("File is not a valid dl file");
              }
            }
          } else {
            try {
              if (token.endsWith(",")) {
                token = token.substring(0, token.length() - 1);
              }
              return Long.parseLong(token);
            } catch (NumberFormatException ex) {
              throw new IOException("File is not a valid dl file");
            }
          }
        } else {
          throw new IOException("File is not a valid dl file");
        }
      }
    } else {
      throw new IOException("File is not a valid dl file");
    }
  }

  private void parseLineForLabel(String line, List<String> labelArray) throws IOException {
    char[] array = line.trim().toCharArray();
    int labelStart = -1;
    boolean labelStarted = false;

    for (int i = 0; i < array.length; i++) {
      if (array[i] == '\"') {
        labelStart = ++i;
        while (i < array.length) {
          if (array[i] == '\"') {
            labelArray.add(line.substring(labelStart, i));
            labelStart = -1;
            break;
          } else {
            i++;
          }
        }
        if (labelStart != -1) {
          // if here then never found the other '"' so throw error
          throw new IOException("File is not valid dl file");
        }
      } else if (array[i] == ' ' || array[i] == ',' || array[i] == '\r' ||
              array[i] == '\n') {
        if (labelStarted) {
          labelArray.add(line.substring(labelStart, i));
          labelStarted = false;
          labelStart = -1;
        }
      } else if (!labelStarted && array[i] != '\"') {
        System.out.println(array[i]);
        labelStart = i;
        labelStarted = true;
      }
    }

    if (labelStart != -1) {
      throw new IOException("File not a valid dl file");
    }
  }

  private void readMatrixLabels() throws IOException {
    matrixLabels = new ArrayList<String>();
    String line;
    while ((line = reader.readLine()) != null) {
      if (line.equals("data:")) {
        break;
      }

      parseLineForLabel(line, matrixLabels);
    }
  }

  private void readLabels() throws IOException {
    labels = new ArrayList<String>();

    // might not be any labels if so return null for getLabels;
    String line = reader.readLine();
    if (!line.equals("labels:")) {
      labels = new ArrayList<String>();
      if (line.equals("matrix labels:")) {
        hasMatrixLabels = true;
      }
      return;
    }

    while ((line = reader.readLine()) != null) {
      if (line.equals("data:")) {
        break;
      }

      if (line.equals("matrix labels:")) {
        hasMatrixLabels = true;
        break;
      }

      parseLineForLabel(line, labels);
    }
  }

  private AdjacencyMatrix loadData(AdjacencyDoubleMatrix matrix) throws IOException {
    String line = reader.readLine();

    // remove whitespace
    while (line.trim().length() == 0) {
      line = reader.readLine();
    }

    // now should have data
    for (int i = 0; i < numNodes; i++) {
      StringTokenizer t = new StringTokenizer(line, " ");
      int j = 0;
      while (t.hasMoreTokens()) {
        String val = t.nextToken();
        matrix.set(i, j, Double.parseDouble(val));
        j++;
      }
      line = reader.readLine();
    }

    return matrix;
  }

  /**
   * Closes the reader.
   */
  public void close() {
    try {
      reader.close();
    } catch (IOException ex) {
    }
  }
}
