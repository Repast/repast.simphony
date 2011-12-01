package repast.simphony.context.space.graph;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import java.util.ArrayList;
import java.util.List;

/**
 * A network adjacency matrix that stores its values as doubles.
 *
 * @author Nick Collier
 */
public class AdjacencyDoubleMatrix implements AdjacencyMatrix {

  private DenseDoubleMatrix2D matrix;
  private String matrixLabel = "";
  private List<String> labels = new ArrayList<String>();

  /**
   * Constructs an AdjacencyMatrix with the specified number of rows
   * and columns. The intial state of every ij is 0.
   *
   * @param rows the number of rows in the matrix
   * @param cols the number of cols in the matrix
   */
  public AdjacencyDoubleMatrix(int rows, int cols) {
    matrix = new DenseDoubleMatrix2D(rows, cols);
  }

  /**
   * Constructs an AdjacencyMatrix with the specified row/col labels. The
   * matrix row and column size are set equal to the size of the lables vector.
   *
   * @param labels the row & column labels
   */
  public AdjacencyDoubleMatrix(List<String> labels) {
    matrix = new DenseDoubleMatrix2D(labels.size(), labels.size());
    this.labels = labels;
  }

  /**
   * Constructs an AdjacencyMatrix from the specified DenseDoubleMatrix2D
   *
   * @param m the DenseDoubleMatrix2D to construct this AdjacencyMatrix from
   */

  public AdjacencyDoubleMatrix(DenseDoubleMatrix2D m) {
    matrix = m;
  }

  /**
   * Constructs an AdjacencyMatrix from the specified two dimensional double
   * array.
   *
   * @param m the 2D double array to construct this AdjacencyMatrix from.
   */
  public AdjacencyDoubleMatrix(double[][] m) {
    matrix = new DenseDoubleMatrix2D(m);
  }


  /**
   * Sets the actual matrix for this AdjacencyMatrix.
   *
   * @param m the actual matrix data for this AdjacencyMatrix
   */
  public void setMatrix(DenseDoubleMatrix2D m) {
    matrix = m;
  }

  /**
   * Sets the actual matrix for this AdjacencyMatrix.
   *
   * @param m the actual matrix data for this AdjacencyMatrix
   */
  public void setMatrix(double[][] m) {
    matrix = new DenseDoubleMatrix2D(m);
  }

  /**
   * Gets the specified row of data for this matrix.
   *
   * @param row the index of the row to get
   */
  public DenseDoubleMatrix1D getRow(int row) {
    return (DenseDoubleMatrix1D) matrix.viewRow(row);
  }

  /**
   * Sets a data value in this matrix.
   *
   * @param row the row index (i)
   * @param col the col index (j)
   * @param val the value to set ij to.
   */
  public void set(int row, int col, double val) {
    matrix.setQuick(row, col, val);
  }

  /**
   * Gets the value at row, col.
   *
   * @param row the row index (i)
   * @param col the col index (j)
   * @return the double value at row, col (ij)
   */
  public double get(int row, int col) {
    return matrix.getQuick(row, col);
  }

  /**
   * Returns the number of rows int matrix.
   */
  public int rows() {
    return matrix.rows();
  }

  /**
   * Returns the number of columns in the matrix.
   */
  public int columns() {
    return matrix.columns();
  }

  /**
   * Returns a String representation of only the actual data matrix.
   */
  public String matrixToString() {
    String m = matrix.toString();
    int index = m.indexOf("\n");
    return m.substring(index + 1, m.length());
  }

  /**
   * Returns a String representation of this AdjacencyMatrix (comment etc.)
   * together with the actual data matrix.
   */
  public String toString() {
    return matrix.toString();
  }

  /**
   * Gets the node labels, if any, for this matrix.
   *
   * @return the node labels, if any, for this matrix.
   */
  public List<String> getLabels() {
    return labels;
  }

  /**
   * Gets the label for this matrix.
   *
   * @return the label for this matrix.
   */
  public String getMatrixLabel() {
    return matrixLabel;
  }

  /**
   * Sets the matrix label.
   *
   * @param label the matrix label
   */
  public void setMatrixLabel(String label) {
    this.matrixLabel = label;
  }
}

