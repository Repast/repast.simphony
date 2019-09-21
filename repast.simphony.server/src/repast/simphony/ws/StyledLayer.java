package repast.simphony.ws;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;

public class StyledLayer {

    private Map<Object, DisplayProperties> toBeAdded = new HashMap<>();
    private Set<Object> toBeRemoved = new HashSet<>();
    private Map<Object, DisplayProperties> objMap = new HashMap<>();
    private Map<Integer, Object> idMap = new HashMap<>();
    private ServerStyle2D style;
    private String layerName;
    private int layerid;

    public StyledLayer(ServerStyle2D style, String layerName, int layerid) {
        this.style = style;
        this.layerName = layerName;
        this.layerid = layerid;
    }

    public void addObject(Object obj, int id) {
        // remove it from the removed objects because
        // there might be a remove and then add in the same
        // update -- for example when a object switches contexts
        // the display is part of their parent context
        toBeRemoved.remove(obj);
        toBeAdded.put(obj, new DisplayProperties(id, obj));
    }

    public void removeObject(Object obj) {
        // if the object to remove is in addedObjects
        // we don't need to actually remove it because it
        // hasn't been added yet -- so just remove it from there
        // otherwise add it to the objects to remove.
        if (toBeAdded.remove(obj) == null) toBeRemoved.add(obj);
    }
    
    public int getLayerId() {
        return layerid;
    }

//    private void processRemoved(StringBuilder builder) {
//        builder.append("\"remove\": [");
//        boolean first = true;
//        for (Object obj : toBeRemoved) {
//            DisplayProperties props = objMap.remove(obj);
//            if (props != null) {
//                if (!first) {
//                    builder.append(",");
//                }
//                builder.append(props.id);
//                first = false;
//            }
//
//        }
//        builder.append("],");
//        toBeRemoved.clear();
//    }
    
    private void processRemoved(ByteBuffer bb) {
        bb.putInt(toBeRemoved.size());
        for (Object obj : toBeRemoved) {
            DisplayProperties props = objMap.remove(obj);
            idMap.remove(props.id);
            bb.putInt(props.id);
        }
        toBeRemoved.clear();
    }
    
//    private void appendUpdate(StringBuilder builder, DisplayProperties props, float x, float y) {
//        builder.append("{ \"id\":");
//        builder.append(props.id);
//        builder.append(String.format(", \"pt\": [%f, %f, 0]", x, y));
//        builder.append(", \"color\": ");
//        builder.append(String.format("\"0x%02x%02x%02x\"", props.color.getRed(), props.color.getGreen(),
//                props.color.getBlue()));
//        builder.append("}");
//        
//    }
//    
//    private void updateFromIterable(StringBuilder builder, Layout layout, Iterable<DisplayProperties> iter) {
//        Box box = layout.getBoundingBox();
//        float hOffset = box.getHeight() / 2f;
//        float wOffset = box.getWidth() / 2f;
//          
//        boolean first = true;
//        for (DisplayProperties props : iter) {
//            objMap.put(props.agent, props);
//            if (!first) {
//                builder.append(",");
//            }
//            props.color = style.getColor(props.agent);
//            float[] location = layout.getLocation(props.agent);
//            appendUpdate(builder, props, location[0] - wOffset, location[1] - hOffset);
//            first = false;
//        }
//    }
// 
//    private void processAdded(StringBuilder builder, Layout layout) {
//        builder.append("\"add\": [");
//        
//        Box box = layout.getBoundingBox();
//        //float hOffset = box.getHeight() / 2f;
//        //float wOffset = box.getWidth() / 2f;
//          
//        boolean first = true;
//        for (DisplayProperties props : toBeAdded.values()) {
//            objMap.put(props.agent, props);
//            if (!first) {
//                builder.append(",");
//            }
//            props.color = style.getColor(props.agent);
//            float[] location = layout.getLocation(props.agent);
//            appendUpdate(builder, props, location[0], location[1]);
//            first = false;
//        }
//        builder.append("]");
//        toBeAdded.clear();
//    }
//    
//    private void updateExisting(StringBuilder builder, Layout layout) {
//        builder.append("\"update\": [");
//        Box box = layout.getBoundingBox();
//        float hOffset = box.getHeight() / 2f;
//        float wOffset = box.getWidth() / 2f;
//          
//        boolean first = true;
//        for (DisplayProperties props : objMap.values()) {
//            if (!first) {
//                builder.append(",");
//            }
//            props.color = style.getColor(props.agent);
//            float[] location = layout.getLocation(props.agent);
//            appendUpdate(builder, props, location[0] - wOffset, location[1] - hOffset);
//            first = false;
//        }
//        
//        builder.append("],");
//    }
//    
   
    private void updateExisting(ByteBuffer bb, Layout layout) {
        Box box = layout.getBoundingBox();
        //float hOffset = box.getHeight() / 2f;
        //float wOffset = box.getWidth() / 2f;
        
        bb.putInt(objMap.size());
        float[] rgb = new float[3];
        for (DisplayProperties props : objMap.values()) {
            bb.putInt(props.id);
            props.color = style.getColor(props.agent);
            float[] location = layout.getLocation(props.agent);
            props.color.getRGBColorComponents(rgb);
            bb.putFloat(rgb[0]);
            bb.putFloat(rgb[1]);
            bb.putFloat(rgb[2]);
            bb.putFloat(location[0]);// - wOffset);
            bb.putFloat(location[1]); // - hOffset);
            bb.putFloat(0f);
        }
    }
    
    private void processAdded(ByteBuffer bb, Layout layout) {
        Box box = layout.getBoundingBox();
        //float hOffset = box.getHeight() / 2f;
        //float wOffset = box.getWidth() / 2f;
        
        bb.putInt(toBeAdded.size());
        float[] rgb = new float[3];
        for (DisplayProperties props : toBeAdded.values()) {
            objMap.put(props.agent, props);
            idMap.put(props.id, props.agent);
            bb.putInt(props.id);
            props.color = style.getColor(props.agent);
            float[] location = layout.getLocation(props.agent);
            props.color.getRGBColorComponents(rgb);
            bb.putFloat(rgb[0]);
            bb.putFloat(rgb[1]);
            bb.putFloat(rgb[2]);
            bb.putFloat(location[0]); // - wOffset);
            bb.putFloat(location[1]); // - hOffset);
            bb.putFloat(0f);
        }
        toBeAdded.clear();
    }
    
    public void getAgents(List<Integer> ids, List<Pair<Integer,Object>> agents) {
        for (Integer id : ids) {
            Object obj = idMap.get(id);
            if (obj != null) {
                agents.add(new Pair<Integer, Object>(id, obj));
            }
        }
    }

//    public void update(StringBuilder builder, Layout<?, ?> layout) {
//        builder.append("{\"name\":\"");
//        builder.append(layerName);
//        builder.append("\", \"layer_id\":");
//        builder.append(layerid);
//        builder.append(", \"data\": {");
//        processRemoved(builder);
//        
//        updateExisting(builder, layout);
//        processAdded(builder, layout);
//        builder.append("}}");
//    }
    
    public void update(ByteBuffer bb, Layout<?, ?> layout) {
        bb.putInt(layerid);
        processRemoved(bb);
        updateExisting(bb, layout);
        processAdded(bb, layout);
    }

    public void setStyle(ServerStyle2D style) {
        this.style = style;
    }
    
    public int getSizeInBytes() {
        // 4 = layer id, length of added, removed, updated
        return Integer.BYTES * 4 + 
                // added - id, 3 floats for position, 3 floats for color
                toBeAdded.size() * Integer.BYTES + (toBeAdded.size() * Float.BYTES * 3) + (toBeAdded.size() * Float.BYTES * 3) +
                // removed
                toBeRemoved.size() * Integer.BYTES +
                // updated - id, 3 floats for position, 3 floats for color
                objMap.size() * Integer.BYTES + (objMap.size() * Float.BYTES * 3) + (objMap.size() * Float.BYTES * 3);
                
    }
    
    public String getName() {
    	return layerName;
    }
}
