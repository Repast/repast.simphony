package repast.simphony.ui.editor;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class ClassListItem implements Comparable<ClassListItem> {

	private Class clazz;
	private String shortName;
	private String fullName;


	public ClassListItem(Class clazz) {
		this.clazz = clazz;
		this.fullName = clazz.getName();
		shortName = clazz.getName();
		if (shortName.lastIndexOf(".") != -1) {
			shortName = shortName.substring(shortName.lastIndexOf(".") + 1, shortName.length());
		}
	}

	public ClassListItem(String className) {
		this.fullName = className;
		shortName = fullName;
		if (shortName.lastIndexOf(".") != -1) {
			shortName = shortName.substring(shortName.lastIndexOf(".") + 1, shortName.length());
		}
	}

	public Class getClazz() throws ClassNotFoundException {
		if (clazz == null) {
			clazz = Class.forName(fullName);
		}
		return clazz;
	}

	public String getFullName() {
		return fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public String toString() {
		return shortName;
	}

	public int compareTo(ClassListItem classListItem) {
		return shortName.compareTo(classListItem.shortName);
	}

	public boolean equals(Object obj) {
		if (obj instanceof ClassListItem) {
			ClassListItem other = (ClassListItem) obj;
			return other.fullName.equals(fullName);
		}
		return false;
	}
}
