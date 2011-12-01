package repast.simphony.space.graph;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

public class RepastPajekVertexTransformer implements Transformer {

	Map<Object, String> idMap = new HashMap<Object, String>();

	public Object transform(Object obj) {
		String name = obj.toString();
		if ((name == null) || (name.equals(""))) {
			name = obj.getClass().getName();
		}
		String qualifiedName = name.replace("\"", "");
		int counter = 1;
		while (idMap.get(qualifiedName) != null) {
			qualifiedName = (name + counter).replace("\"", "");
			counter++;
		}
		idMap.put(obj, qualifiedName);
		return qualifiedName;
	}

}
