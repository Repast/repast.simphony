///*CopyrightHere*/
//package repast.simphony.scenario;
//
//import repast.score.SNamed;
//
//import com.thoughtworks.xstream.converters.Converter;
//import com.thoughtworks.xstream.converters.MarshallingContext;
//import com.thoughtworks.xstream.converters.UnmarshallingContext;
//import com.thoughtworks.xstream.io.HierarchicalStreamReader;
//import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//
////todo is this converter neccessary?
//public abstract class SNamedConverter implements Converter {
//	
//	public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
//		SNamed named = (SNamed) o;
////		writer.startNode("type");
////		context.convertAnother(projection.getType());
////		writer.endNode();
//		writer.startNode("name");
//		writer.setValue(named.getLabel());
//		writer.endNode();
//	}
//	
//	public SNamed unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
//		SNamed named = createClass();
////		reader.moveDown();
////		SProjectionType type = (SProjectionType) context.convertAnother(null, SProjectionType.class);
////		projection.setType(type);
////		reader.moveUp();
//		reader.moveDown();
//		String name = reader.getValue();
//		named.setLabel(name);
//		reader.moveUp();
//		return named;
//	}
//
//    public abstract SNamed createClass();
//
//}