package repast.simphony.context.space.graph;

import repast.simphony.context.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads a Network from an excel file.
 * <p/>
 * ?The matrix is assumed to be square.
 * Each worksheet is treated as a matrix, and any worksheets
 * that do not contain matrices will cause an error. The worksheet name
 * is treated as the matrix label unless the name begins with Sheet
 * (Excel's generic worksheet name). The format for excel files is that
 * imported and exported by UCINet. The first cell is empty, and the
 * node labels begin on this first row in the second column. The column
 * node labels begin in first column on the second row. The actual data
 * begins in cell 2,2. For example,
 * <code><pre>
 *              | first_label | second_label | ...
 * -------------+-------------+--------------+----
 * first_label  | 0           | 1            | ...
 * -------------+-------------+--------------+----
 * second_label | 1           | 0            | ...
 * -------------+-------------+--------------+----
 * ...          | ...         | ...          | ...
 * </pre></code>
 * <p/>
 * If the matrix has no node labels, RePast will expect the first row and
 * column to be blank and as before, for the data to begin in cell 2,2.<p>
 *
 * @author Nick Collier
 */
public class ExcelNetworkLoader extends MatrixNetworkLoader {

  public ExcelNetworkLoader(Context context, InputStream stream, Class agentClass, int matrixIndex) {
    super(context, stream, agentClass, matrixIndex);
  }

  public ExcelNetworkLoader(Context context, InputStream stream, NodeCreator nodeCreator, int matrixIndex) {
    super(context, stream, nodeCreator, matrixIndex);
  }

  protected NetworkMatrixReader getMatrixReader(InputStream stream) throws IOException {
    return new ExcelMatrixReader(stream);
  }
}