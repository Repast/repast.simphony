package repast.simphony.context.space.graph;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;

import java.util.List;


/**
 * A social network adjacency matrix. This is used as an itermediary data
 * structure when moving between Simphony networks.
 * and other kinds of network representations. The matrix is assumed to
 * be square and that the rows and columns refer to the same nodes.
 *
 * @author Nick Collier
 */
public interface AdjacencyMatrix {

  /**
   * Gets the label for this matrix.
   *
   * @return the label for this matrix.
   */
  public String getMatrixLabel();


  /**
   * Gets the node labels, if any, for this matrix.
   *
   * @return the node labels, if any, for this matrix.
   */
  public List<String> getLabels();

  /**
   * Gets the specified row of data for this matrix.
   *
   * @param row the index of the row to get
   * @return the specified row of data for this matrix.
   */
  public DenseDoubleMatrix1D getRow(int row);

  /**
   * Gets the value at row, col.
   *
   * @param row the row index (i)
   * @param col the col index (j)
   * @return the double value at row, col (ij)
   */
  public double get(int row, int col);

  /**
   * Returns the number of rows in this matrix.
   *
   * @return the row count
   */
  public int rows();

  /**
   * Returns the number of columns in the matrix.
   *
   * @return the column count
   */
  public int columns();

  /**
   * Returns a String representation of only the actual data matrix.
   *
   * @return a String representation of only the actual data matrix.
   */
  public String matrixToString();
}

