package repast.simphony.xml;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.ContextGrid;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.space.grid.*;
import repast.simphony.util.collections.Pair;

/**
 * XStream converter for ContextGrids, the default grid type
 * in simphony.
 *
 * @author Nick Collier
 */
public class GridConverter extends AbstractConverter {

  public boolean canConvert(Class aClass) {
    return aClass.equals(ContextGrid.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    Grid grid = (Grid) o;
    writeString("name", grid.getName(), writer);
    writeString("adder", grid.getAdder().getClass().getName(), writer);
    writeString("translator", grid.getGridPointTranslator().getClass().getName(), writer);
    int[] dims = grid.getDimensions().toIntArray(null);
    //writeObject("dims", dims, writer, mContext);
    writeString("dims", arrayToString(dims), writer);
    int[] origin = grid.getDimensions().originToIntArray(null);
    //writeObject("origin", origin, writer, mContext);
    writeString("origin", arrayToString(origin), writer);
    
    writeString("multi", String.valueOf(grid.getCellAccessor().allowsMultiOccupancy()), writer);

    writeString("item_count", String.valueOf(grid.size()), writer);
    for (Object obj : grid.getObjects()) {
      GridPoint point = grid.getLocation(obj);
      Pair p = new Pair(obj, point);
      writeObject("grid_entry", p, writer, mContext);
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {

      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);

      String adder = readNextString(reader);
      Class adderClass = Class.forName(adder);
      GridAdder gAdder = (GridAdder) adderClass.newInstance();

      Class transClass = Class.forName(readNextString(reader));
      GridPointTranslator trans = (GridPointTranslator) transClass.newInstance();

      int[] dims = stringToIntArray(readNextString(reader));   //(int[]) readNextObject(context, reader, umContext);      
      int[] origin = stringToIntArray(readNextString(reader)); //(int[]) readNextObject(context, reader, umContext);
      
      boolean multi = Boolean.valueOf(readNextString(reader));

      GridBuilderParameters params = new GridBuilderParameters(trans, gAdder, multi, dims, origin);
      Grid grid = GridFactoryFinder.createGridFactory(null).createGrid(name, context, params);

      int itemCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < itemCount; i++) {
        Pair pair = (Pair) readNextObject(grid, reader, umContext);
        GridPoint point = (GridPoint) pair.getSecond();
        if (point != null) {
          if (!context.contains(pair.getFirst())) {
            context.add(pair.getFirst());
          }
          grid.moveTo(pair.getFirst(), point.toIntArray(null));
        }
      }

      return grid;
    } catch (Exception ex) {
      throw new ConversionException("Error deserializing Grid", ex);
    }

  }
}