/**
 * 
 */
package repast.simphony.relogo

import static java.awt.Color.*
import static java.lang.Math.*
import static repast.simphony.essentials.RepastEssentials.*
import static repast.simphony.relogo.Utility.*

import java.beans.BeanInfo
import java.beans.Introspector
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field
import java.lang.reflect.Modifier

import org.apache.commons.lang3.math.NumberUtils

import repast.simphony.relogo.factories.*
import repast.simphony.ui.probe.ProbeID
import repast.simphony.util.*
import au.com.bytecode.opencsv.CSVReader


/**
 * @author jozik
 *
 */
public class BaseObserver extends AbstractObserver{

	/**
	 * 
	 * This value is used to automatically generate agent identifiers.
	 * 
	 * @field serialVersionUID
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	/**
	 * 
	 * This method provides a human-readable name for the agent.
	 * 
	 * @method toString
	 * 
	 */
	@ProbeID()
	public String toString() {
		return super.toString();
	}



	/**
	 * Does nothing, included for translation compatibility.
	 */
	public void watch(Object watched){
	}


	/**
	 * This routine is a file reader that creates turtles from CSV files.
	 * 
	 * @author Michael J. North
	 * @author jozik
	 * 
	 * @param fileName the path from the default system directory
	 * @param turtleType the class of turtle to create
	 * @param defaultShape the turtle shape (default defaultShape is "default")
	 * @param defaultSize the turtle size (default defaultSize is 1)
	 * @param defaultHeading the heading of the turtle (default defaultHeading is 0)
	 * @param defaultColor the turtle color (default defaultColor is white())
	 * 
	 */
	public <E> AgentSet<E> createTurtlesFromCSV(String fileName, Class<E> turtleType,
			String defaultShape = "default", double defaultSize = 1, double defaultHeading = 0, double defaultColor = white()) {
		AgentSet<E> result = new AgentSet<>();
		// Read the data file.
		List<String[]> rows = new CSVReader(
				new InputStreamReader(new FileInputStream(fileName)))
				.readAll()

		// Define the fields lists.
		List fullFieldList
		List matchedFieldList

		// Create the agents.
		for (row in rows) {

			// Check the fields list.
			if (fullFieldList == null) {

				// Fill in the field lists.
				fullFieldList = (List) row
				List<String> fields = getPublicFieldsAndProperties(turtleType)
				matchedFieldList = fields.intersect((List) row)
			} else {

				// Define an index tracker.
				int index

				// Create the next agent.
				AgentSet turtleAgentSet = createTurtles(1, {
					// Set the shape.
					setShape(defaultShape)

					// Set the size.
					setSize(defaultSize)

					// Set the default heading.
					setHeading(defaultHeading)

					// Set the default color.
					setColor(defaultColor)

					// Assign properties from the file.
					for (field in matchedFieldList) {
						index = fullFieldList.indexOf(field)
						if (it."$field" instanceof Integer) {
							it."$field" = NumberUtils.toInt(row[index])
						} else if (it."$field" instanceof Double) {
							it."$field" = NumberUtils.toDouble(row[index])
						} else {
							it."$field" = row[index]
						}
					}
				}, turtleType.getSimpleName())
				if (turtleAgentSet){
					result.add(turtleAgentSet.first())
				}
			}
		}
		return result
	}

	protected List<String> getPublicFieldsAndProperties(Class clazz){
		List<String> fields = []
		Class curClass = clazz
		BeanInfo bi = Introspector.getBeanInfo(curClass)
		if (bi != null){
			for (PropertyDescriptor pd in bi.getPropertyDescriptors()){
				String propertyName = pd.getName()
				if (!["class", "metaClass"].contains(propertyName)){
					fields.add(propertyName)
				}
			}
		}

		while (true){
			curClass.getDeclaredFields().each{ Field field ->
				String fieldName = field.getName()
				if (Modifier.isPublic(field.getModifiers()) && !Modifier.isSynthetic(field.getModifiers())){
					fields.add(fieldName)
				}
			}
			curClass = curClass.getSuperclass()
			if (!curClass) break;
		}
		return fields.unique()
	}



}
