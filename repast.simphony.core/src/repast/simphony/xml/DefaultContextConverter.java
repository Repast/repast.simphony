package repast.simphony.xml;

import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.projection.Projection;
import repast.simphony.valueLayer.ValueLayer;

import java.lang.reflect.Field;
import java.util.*;

/**
 * XStream converter for DefaultContexts.
 *
 * @author Nick Collier
 */
public class DefaultContextConverter extends AbstractConverter implements Converter {

  // sorts projections based on instance such that geography projections
  // are always last.
  private class ProjectionComparator implements Comparator<Projection> {

    public int compare(Projection o1, Projection o2) {
      return o1 instanceof Geography ? 1 : 0;
    }
  }

  public boolean canConvert(Class aClass) {
    return (DefaultContext.class.isAssignableFrom(aClass));
  }

  public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    try {
      DefaultContext context = (DefaultContext) obj;
      Field field = DefaultContext.class.getDeclaredField("contents");
      field.setAccessible(true);
      Collection<Object> items = (Collection<Object>) field.get(context);

      // write the context ids
      writeObject("id", context.getId(), writer, mContext);
      writeObject("type_id", context.getTypeID(), writer, mContext);

      // write the context contents
      writeString("size", String.valueOf(items.size()), writer);
      for (Object item : items) {
        writeObject("agent", item, writer, mContext);
      }

      // write its subcontexts
      List<Context> subs = new ArrayList<Context>();
      for (Context sub : (Iterable<Context>) context.getSubContexts()) {
        subs.add(sub);
      }

      writeString("sub_count", String.valueOf(subs.size()), writer);
      for (Context sub : subs) {
        writeObject("sub_context", sub, writer, mContext);
      }

      // write its projections
      writeString("proj_count", String.valueOf(context.getProjections().size()), writer);
      // we need to make sure to do the geography after the network
      // in case we have network edges that are part of the geography
      // but not part of the context.
      List<Projection> projs = new ArrayList<Projection>(context.getProjections());
      Collections.sort(projs, new ProjectionComparator());
      for (Projection proj : projs) {
        writeObject("projection", proj, writer, mContext);
      }

      writeString("vl_count", String.valueOf(context.getValueLayers().size()), writer);
      for (ValueLayer layer : (Iterable<ValueLayer>) context.getValueLayers()) {
        writeObject("vl", layer, writer, mContext);
      }


    } catch (Exception ex) {
      throw new XStreamException("Error while writing context", ex);
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    DefaultContext context = new DefaultContext();
    try {
      Object id = readNextObject(context, reader, umContext);
      context.setId(id);
      Object typeID = readNextObject(context, reader, umContext);
      context.setTypeID(typeID);

      umContext.put(Keys.CONTEXT, context);
      int size = Integer.parseInt(readNextString(reader));
      for (int i = 0; i < size; i++) {
        context.add(readNextObject(context, reader, umContext));
      }

      int cCount = Integer.parseInt(readNextString(reader));
      for (int i = 0; i < cCount; i++) {
        Context sub = (Context) readNextObject(context, reader, umContext);
        context.addSubContext(sub);
      }


      int pCount = Integer.parseInt(readNextString(reader));
      umContext.put(Keys.CONTEXT, context);
      for (int i = 0; i < pCount; i++) {
        // unMarshalling of the projection should add that
        // projection to the context, so we don't do it here.
        readNextObject(context, reader, umContext);
      }

      int vlCount = Integer.parseInt(readNextString(reader));
      umContext.put(Keys.CONTEXT, context);
      for (int i = 0; i < vlCount; i++) {
        // unMarshalling of the value layer should add that
        // value layer to the context, so we don't do it here.
        readNextObject(context, reader, umContext);
      }


    } catch (ClassNotFoundException ex) {
      throw new ConversionException("Error while deserializing context", ex);
    }
    return context;


  }

}
