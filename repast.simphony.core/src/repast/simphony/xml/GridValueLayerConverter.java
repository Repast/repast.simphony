package repast.simphony.xml;

import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.util.collections.Pair;
import repast.simphony.valueLayer.GridValueLayer;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * XStream converter for GridValueLayers.
 *
 * @author Nick Collier
 */
public class GridValueLayerConverter extends AbstractConverter {

  public boolean canConvert(Class aClass) {
    return aClass.equals(GridValueLayer.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    try {
    	
      GridValueLayer grid = (GridValueLayer) o;
      Field field = GridValueLayer.class.getDeclaredField("dense");
      field.setAccessible(true);
      Boolean dense = (Boolean) field.get(grid);
      writeString("name", grid.getName(), writer);
      writeString("dense", dense.toString(), writer);
      writeString("translator", grid.getGridPointTranslator().getClass().getName(), writer);
      int[] dims = grid.getDimensions().toIntArray(null);
      //writeObject("dims", dims, writer, mContext);
      writeString("dims", arrayToString(dims), writer);
      int[] origin = grid.getDimensions().originToIntArray(null);
      //writeObject("origin", origin, writer, mContext);
      writeString("origin", arrayToString(origin), writer);

      int[] pt = new int[dims.length];
      pt[dims.length - 1] = -1;
      int count = 1;
      for (int val : dims) {
        count *= val;
      }

      writeString("item_count", String.valueOf(count), writer);
      for (int i = 0; i < count; i++) {
        nextPoint(dims, pt);
        int[] actualPt = new int[pt.length];
        double[] actualPtD = new double[pt.length];
        for (int j=0; j<pt.length; j++){
        	actualPt[j] = pt[j] - origin[j];
        	actualPtD[j] = actualPt[j];
        }
        Pair p = new Pair(actualPt, grid.get(actualPtD));
        writeObject("grid_entry", p, writer, mContext);
      }

    } catch (Exception ex) {
      throw new XStreamException("Error while writing GridValueLayer", ex);
    }
  }

  private void nextPoint(int[] dims, int[] pt) {
    int index = dims.length - 1;
    int val = pt[index] + 1;
    while (val >= dims[index]) {
      pt[index] = 0;
      if (index == 0) {
        val = 0;
        break;
      }
      index--;
      val = pt[index] + 1;
    }
    pt[index] = val;
  }

  public static void main(String[] args) {
    GridValueLayerConverter con = new GridValueLayerConverter();
    int[] dims = {2, 2, 3};
    int[] pt = {0, 0, -1};
    for (int i = 0; i < 12; i++) {
      con.nextPoint(dims, pt);
      System.out.println("pt = " + Arrays.toString(pt));
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {

      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);

      String strDense = readNextString(reader);
      boolean dense = Boolean.valueOf(strDense);
      
      Class transClass = Class.forName(readNextString(reader));
      GridPointTranslator trans = (GridPointTranslator) transClass.newInstance();

      int[] dims = stringToIntArray(readNextString(reader));   //(int[]) readNextObject(context, reader, umContext);
      int[] origin = stringToIntArray(readNextString(reader)); //(int[]) readNextObject(context, reader, umContext);

      GridValueLayer grid = new GridValueLayer(name, 0.0, dense, trans, dims, origin);
      context.addValueLayer(grid);
      int itemCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < itemCount; i++) {
        Pair pair = (Pair) readNextObject(grid, reader, umContext);
        int[] pt = (int[]) pair.getFirst();
        double val = (Double) pair.getSecond();
        grid.set(val, pt);
      }

      return grid;
    } catch (Exception ex) {
      throw new ConversionException("Error deserializing GridValueLayer", ex);
    }

  }
}