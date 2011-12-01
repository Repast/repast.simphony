package repast.simphony.context.space.graph;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nick Collier
 *         Date: Apr 29, 2008 4:29:05 PM
 */
public class ExcelMatrixReader implements NetworkMatrixReader {

  private HSSFWorkbook workbook = null;

  public ExcelMatrixReader(InputStream in) throws IOException {

    POIFSFileSystem fs = new POIFSFileSystem(in);
    workbook = new HSSFWorkbook(fs);
    in.close();
  }


  private AdjacencyDoubleMatrix getLabeledMatrix(HSSFSheet sheet) throws IOException {
    AdjacencyDoubleMatrix m = null;
    HSSFRow row;
    HSSFCell cell;

    List<String> labels = new ArrayList<String>();
    row = sheet.getRow(0);
    if (row != null) {
      Iterator ci = row.cellIterator();
      while (ci.hasNext()) {
        cell = (HSSFCell) ci.next();
        String label = cell.getRichStringCellValue().getString();
        if (cell.getCellNum() == 0) {
          if (!(label.equals("")))
            throw new IOException("Badly formatted Excel matrix file: " +
                    "labels must start at 1, 2");
        }
        if (label.length() > 0) labels.add(label);

      }
    }
    if (labels.size() > 0)
      m = new AdjacencyDoubleMatrix(labels);
    return m;
  }


  private AdjacencyDoubleMatrix getNonLabeledMatrix(HSSFSheet sheet) {
    int height = sheet.getPhysicalNumberOfRows();
    return new AdjacencyDoubleMatrix(height, height);
  }

  private AdjacencyMatrix getMatrix(String sheetName) throws IOException {
    HSSFSheet sheet = workbook.getSheet(sheetName);
    HSSFRow row;
    HSSFCell cell;
    AdjacencyDoubleMatrix matrix;
    matrix = getLabeledMatrix(sheet);
    int numRows = sheet.getPhysicalNumberOfRows();
    boolean hasLabels = true;
    if (matrix == null) {
      matrix = getNonLabeledMatrix(sheet);
      hasLabels = false;
      // increment num rows because no labels
      // so one less physical row
      numRows++;
    }

    for (int i = 1; i < numRows; i++) {
      row = sheet.getRow(i);
      Iterator ci = row.cellIterator();
      // advance past first labeled cell
      if (hasLabels && ci.hasNext()) ci.next();
      while (ci.hasNext()) {
        cell = (HSSFCell) ci.next();
        Short pos = cell.getCellNum();
        matrix.set(i - 1, (pos.intValue() - 1), cell.getNumericCellValue());
      }
    }
    if (!sheetName.startsWith("Sheet"))
      matrix.setMatrixLabel(sheetName);
    return matrix;
  }

  /**
   * Returns a list of the read AdjacencyMatrices.
   *
   * @return a list of the read AdjacencyMatrices.
   * @throws IOException if there is an error while attempting to read the matrices
   */
  public List<AdjacencyMatrix> getMatrices() throws IOException {
    List<AdjacencyMatrix> list = new ArrayList<AdjacencyMatrix>();
    int numSheets = workbook.getNumberOfSheets();
    for (int i = 0; i < numSheets; i++) {
      list.add(getMatrix(workbook.getSheetName(i)));
    }
    return list;
  }

  public void close() {
    workbook = null;
  }
}
