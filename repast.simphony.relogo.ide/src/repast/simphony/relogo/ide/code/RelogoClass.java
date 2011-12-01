/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.code;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import org.antlr.stringtemplate.*;

/**
 * 
 * @author CBURKE
 */
public class RelogoClass {

	static public final int RELOGO_CLASS_UNKNOWN = -1;
	static public final int RELOGO_CLASS_MODEL = 0;
	static public final int RELOGO_CLASS_OBSERVER = 1;
	static public final int RELOGO_CLASS_PATCH = 2;
	static public final int RELOGO_CLASS_TURTLE = 3;
	static public final int RELOGO_CLASS_LINK = 4;

	String packageName;
	String className;
	String parentClassName;
	Breed breed;
	LinkedList<Attribute> attributes;
	LinkedList<ProcedureDefinition> methods;
	int genericCategory;
	
	public Iterable<Attribute> attributes() {
		return attributes;
	}
	
	/**
	 * @return the breed
	 */
	public Breed getBreed() {
		return breed;
	}

	public void createBreed(String pluralName, String singularName){
		breed = new Breed(pluralName,singularName);
	}
	
	public void createLinkBreed(String pluralName, String singularName, boolean linkBreedDirected){
		breed = new Breed(pluralName,singularName,linkBreedDirected);
	}
	
	public class Breed {
		
		private String pluralName;
		private String singularName;
		private boolean linkBreedDirected;
		
		public Breed(String pluralName){
			this(pluralName,null);
		}
		
		public Breed(String pluralName, String singularName){
			this.pluralName = pluralName;
			this.singularName = singularName;
		}
		
		public Breed(String pluralName, String singularName, boolean linkBreedDirected){
			this.pluralName = pluralName;
			this.singularName = singularName;
			this.linkBreedDirected = linkBreedDirected;
		}
		
		/**
		 * @return the pluralName
		 */
		public String getPluralName() {
			return pluralName;
		}
		/**
		 * @return the singularName
		 */
		public String getSingularName() {
			return singularName;
		}

		public boolean isLinkBreedDirected() {
			return linkBreedDirected;
		}
		
	}
	
	public Iterable<ProcedureDefinition> methods() {
		return methods;
	}
	
	static public String RELOGO_GROOVY_TEMPLATE_FILE = "/templates/netlogo_groovy.st";
	static public String RELOGO_JAVA_TEMPLATE_FILE = "/templates/netlogo_java.st";
	static protected StringTemplateGroup templateGroup;
	static protected StringTemplateGroup groovyTemplateGroup;
	static protected StringTemplateGroup javaTemplateGroup;

	protected void loadStringTemplates() {
		if (groovyTemplateGroup == null) {
			InputStream netlogoTemplateStream = getClass().getResourceAsStream(
					RELOGO_GROOVY_TEMPLATE_FILE);
			groovyTemplateGroup = new StringTemplateGroup(
					new InputStreamReader(netlogoTemplateStream));
		}
		if (javaTemplateGroup == null) {
			InputStream netlogoTemplateStream = getClass().getResourceAsStream(
					RELOGO_JAVA_TEMPLATE_FILE);
			javaTemplateGroup = new StringTemplateGroup(new InputStreamReader(
					netlogoTemplateStream));
			templateGroup = groovyTemplateGroup; // default to Groovy code
													// generation
		}
	}

	public void setTemplate(String templateName) {
		loadStringTemplates();
		if (templateName.equalsIgnoreCase("java")) {
			templateGroup = javaTemplateGroup;
		} else {
			templateGroup = groovyTemplateGroup;
		}
	}

	public RelogoClass(String name, String pName) {
		genericCategory = RELOGO_CLASS_UNKNOWN;
		int p = name.lastIndexOf(".");
		if (p >= 0) {
			packageName = name.substring(0, p);
			className = name.substring(p + 1);
		} else {
			className = name;
		}
		parentClassName = pName;
		attributes = new LinkedList<Attribute>();
		methods = new LinkedList<ProcedureDefinition>();
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public void addAttribute(Attribute attr) {
		attributes.add(attr);
	}

	public void addMethod(ProcedureDefinition pd) {
		methods.add(pd);
	}

	protected String camelCase(String text) {
		StringBuffer buf = new StringBuffer(text);
		for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '-' && buf.length() > (i + 1)) {
                buf.deleteCharAt(i);
                if (buf.charAt(i) != '?' && buf.charAt(i) != '%'){
                	buf.setCharAt(i, Character.toUpperCase(buf.charAt(i)));
                }
                else if (buf.charAt(i) == '?') {
                    buf.setCharAt(i, 'Q');
                } else if (buf.charAt(i) == '%') {
                    buf.setCharAt(i, 'P');
                }
            } else if (buf.charAt(i) == '?') {
                buf.setCharAt(i, 'Q');
            } else if (buf.charAt(i) == '%') {
                buf.setCharAt(i, 'p');
            }
        }
		buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
		return buf.toString();
	}

	public void setGenericCategory(int gc) {
		genericCategory = gc;
	}

	public int getGenericCategory() {
		if (genericCategory == RELOGO_CLASS_UNKNOWN) {
			if (className == null) {
				return RELOGO_CLASS_UNKNOWN;
			} else if (className.equals("*global*")) {
				genericCategory = RELOGO_CLASS_MODEL;
			} else if (className.equals("*observer*")) {
				genericCategory = RELOGO_CLASS_OBSERVER;
			} else {
				// this ought to be set externally by inheritance path,
				// so don't muck about with it.
			}
		}
		return genericCategory;
	}

	public String getJavaName(String name) {
		if (name == null) {
			return null;
		} else if (name.equals("*global*")) {
			return "Model";
		} else if (name.equals("*observer*")) {
			return "Observer";
		} else {
			return camelCase(name);
		}
	}

	public String dumpToOutput() {
		setTemplate("groovy");
		StringTemplate classHeaderTpl = templateGroup
				.getInstanceOf("class_header");
		classHeaderTpl.setAttribute("class", getJavaName(className));
		if (packageName != null) {
			classHeaderTpl.setAttribute("package", packageName);
		}
		if (parentClassName != null) {
			classHeaderTpl.setAttribute("parentClass",
					getJavaName(parentClassName));
		}
		System.out.println(classHeaderTpl);
		if (!attributes.isEmpty()) {
			System.out.println();
			System.out.println("    //");
			System.out.println("    // Attributes");
			System.out.println("    //");
			for (Attribute attr : attributes) {
				if (!attr.generate) continue;
				StringTemplate attrTpl = null;
				if (attr.breed.equals("patches") && className.equals("turtles")) {
					attrTpl = templateGroup.getInstanceOf("attribute_of_patch");
					attrTpl.setAttribute("getter", attr.getter());
					attrTpl.setAttribute("setter", attr.setter());
				} else if (attr.breed.equalsIgnoreCase("*global*")) {
					attrTpl = templateGroup
							.getInstanceOf("attribute_of_global");
					attrTpl.setAttribute("getter", attr.getter());
					attrTpl.setAttribute("baseName", attr.baseName(false));
				} else {
					attrTpl = templateGroup
							.getInstanceOf("attribute_of_turtle");
					attrTpl.setAttribute("getter", attr.getter());
					attrTpl.setAttribute("baseName", attr.baseName(false));
				}
				System.out.println(attrTpl);
			}
		}
		if (!methods.isEmpty()) {
			System.out.println();
			System.out.println("    //");
			System.out.println("    // Methods");
			System.out.println("    //");
			for (ProcedureDefinition procDef : methods) {
				System.out.println();
				System.out.println(procDef);
			}
		}
		System.out.println("}");
		return null;
	}

	public String getGroovyCode() {
//		setTemplate("groovy");
		return getBodyCode();
	}

	public String getJavaCode() {
		setTemplate("java");
		return getBodyCode();
	}

	public String getBodyCode() {
		StringBuffer bodyBuffer = new StringBuffer();
		/*if (!attributes.isEmpty()) {
			bodyBuffer.append("\n");
			bodyBuffer.append("    //\n");
			bodyBuffer.append("    // ");
			bodyBuffer.append(className);
			bodyBuffer.append(" Attributes\n");
			bodyBuffer.append("    //\n");
			for (Attribute attr : attributes) {
				if (!attr.generate) continue;
				StringTemplate attrTpl = null;
				if (attr.breed.equals("patches") && className.equals("turtles")) {
					attrTpl = templateGroup.getInstanceOf("attribute_of_patch");
				} else if (attr.breed.equalsIgnoreCase("*global*")) {
					attrTpl = templateGroup
							.getInstanceOf("attribute_of_global");
				} else {
					attrTpl = templateGroup
							.getInstanceOf("attribute_of_turtle");
				}
				attrTpl.setAttribute("getter", attr.getter());
				attrTpl.setAttribute("setter", attr.setter());
				attrTpl.setAttribute("baseName", attr.baseName(false));
				bodyBuffer.append(attrTpl);
			}
		}*/
		if (!methods.isEmpty()) {
			bodyBuffer.append("\n");
			/*bodyBuffer.append("    //\n");
			bodyBuffer.append("    // ");
			bodyBuffer.append(className);
			bodyBuffer.append(" Methods\n");
			bodyBuffer.append("    //\n");*/
			for (ProcedureDefinition procDef : methods) {
				if (!(procDef.getName().startsWith("button_method_") || procDef.getName().startsWith("toggle_button_method_"))) {
					bodyBuffer.append(procDef);
					bodyBuffer.append("\n\n");
				}
			}
		}
		return bodyBuffer.toString();
	}
}
