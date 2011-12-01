package repast.simphony.freezedry;

public class FreezeDryedParentChild {
	private Class parentClass;

	private String parentId;

	private Class childClass;

	private String childId;

	private int index;

	/**
	 * @param parentClass
	 * @param parentId
	 */
	public FreezeDryedParentChild(Class parentClass, String parentId) {
		super();
		this.parentClass = parentClass;
		this.parentId = parentId;
	}

	/**
	 * @param class1
	 * @param id
	 * @param class2
	 * @param id2
	 */
	public FreezeDryedParentChild(Class parentClass, String parentId, Class childClass,
			String childId) {
		super();
		this.parentClass = parentClass;
		this.parentId = parentId;
		this.childClass = childClass;
		this.childId = childId;
	}

	/**
	 * 
	 */
	public FreezeDryedParentChild() {
		super();
	}

	/**
	 * 
	 * @param parentClass
	 * @param parentId
	 * @param childClass
	 * @param childId
	 * @param index
	 */
	public FreezeDryedParentChild(Class parentClass, String parentId, Class childClass,
			String childId, int index) {
		super();
		this.parentClass = parentClass;
		this.parentId = parentId;
		this.childClass = childClass;
		this.childId = childId;
		this.index = index;
	}

	/**
	 * @return Returns the childClass.
	 */
	public Class getChildClass() {
		return childClass;
	}

	/**
	 * @param childClass
	 *            The childClass to set.
	 */
	public void setChildClass(Class childClass) {
		this.childClass = childClass;
	}

	/**
	 * @return Returns the childId.
	 */
	public String getChildId() {
		return childId;
	}

	/**
	 * @param childId
	 *            The childId to set.
	 */
	public void setChildId(String childId) {
		this.childId = childId;
	}

	/**
	 * @return Returns the parentClass.
	 */
	public Class getParentClass() {
		return parentClass;
	}

	/**
	 * @param parentClass
	 *            The parentClass to set.
	 */
	public void setParentClass(Class parentClass) {
		this.parentClass = parentClass;
	}

	/**
	 * @return Returns the parentId.
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            The parentId to set.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		return parentId + ":" + parentClass + ":" + childId + ":" + childClass;
	}

}
