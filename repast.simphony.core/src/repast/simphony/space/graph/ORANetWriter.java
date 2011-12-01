package repast.simphony.space.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import simphony.util.messages.MessageCenter;
import edu.uci.ics.jung.graph.Graph;

public class ORANetWriter {

	private static MessageCenter LOG = MessageCenter
			.getMessageCenter(ORANetWriter.class);

	public String save(String title, Graph network, String fileName) {

		String userHomeDirectoryName = System.getProperty("user.home")
				+ System.getProperty("file.separator");
		String preferencesFileName = userHomeDirectoryName + ".OraPreferences";
		String projectFileName = userHomeDirectoryName + ".oraProject.ows";

		try {

			File file = new File(fileName);
			String xmlFileName = file.getAbsolutePath();

			PrintWriter writer = new PrintWriter(file);
			writer.println("<?xml version=\"1.0\" standalone=\"yes\"?>");
			writer.println("");
			writer.println("<DynamicNetwork>");
			writer.println("    <MetaNetwork id=\"Meta Network\">");
			writer.println("        <documents></documents>");
			writer.println("        <nodes>");
			writer
					.println("            <nodeclass type=\"Agent\" id=\"Agent\">");

			Map<Object, String> idMap = new HashMap<Object, String>();
			for (Object obj : network.getVertices()) {
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
				writer.println("                <node id=\"" + qualifiedName
						+ "\">");

				boolean wrotePropertyTag = false;
				for (Method method : obj.getClass().getMethods()) {
					String methodName = method.getName();
					if ((method.getReturnType() != Void.TYPE)
							&& (method.getParameterTypes().length == 0)
							&& (!methodName.equals("toString"))
							&& (!methodName.equals("getMetaClass"))
							&& (!methodName.equals("hashCode"))
							&& (!methodName.equals("getClass"))) {
						try {
							Object methodResult = method.invoke(obj);
							if (methodName.startsWith("get")) {
								methodName = methodName.substring(3);
							}
							if (!wrotePropertyTag) {
								writer
										.println("                    <properties>");
							}
							writer
									.println("                        <property name=\""
											+ methodName
											+ "\" type=\"String\" value=\""
											+ methodResult + "\"/>");
							wrotePropertyTag = true;
						} catch (IllegalArgumentException e) {
						} catch (IllegalAccessException e) {
						} catch (InvocationTargetException e) {
						}
					}

				}
				if (wrotePropertyTag) {
					writer.println("                    </properties>");
				}
				writer.println("</node>");

			}
			;

			writer.println("            </nodeclass>");
			writer.println("        </nodes>");
			writer.println("        <networks>");
			writer
					.println("            <network sourceType=\"Agent\" source=\"Agent\" targetType=\"Agent\" target=\"Agent\" id=\""
							+ title.replace(" ", "") + "\">");

			for (Object obj : network.getEdges()) {
				if (obj instanceof RepastEdge) {
					RepastEdge edge = (RepastEdge) obj;
					Object src = edge.getSource();
					Object tar = edge.getTarget();
					String srcName = idMap.get(src);
					String tarName = idMap.get(tar);
					double weight = edge.getWeight();
					writer.println("                <link source=\"" + srcName
							+ "\" target=\"" + tarName
							+ "\" type=\"double\" value=\"" + weight
							+ "\"></link>");
				}
			}

			writer.println("            </network>");
			writer.println("        </networks>");
			writer.println("    </MetaNetwork>");
			writer.println("</DynamicNetwork>");
			writer.close();

			writer = new PrintWriter(new File(projectFileName));
			writer.println(xmlFileName);
			writer.close();
			
			writer = new PrintWriter(new File(preferencesFileName));
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<PreferencesFile>");
			writer.println("  <preference>");
			writer.println("    <name>Restore Workspace</name>");
			writer.println("    <class>java.lang.String</class>");
			writer.println("    <value>Load Previous Workspace</value>");
			writer.println("  </preference>");
			writer.println("</PreferencesFile>");
			writer.close();
			
		} catch (FileNotFoundException e) {
			LOG.error("ORA export file \"" + fileName
					+ "\" could not be written: ", e);
		}

		return preferencesFileName;

	}

}
