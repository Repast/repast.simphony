/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.code;

import repast.simphony.relogo.ide.wizards.WizardUtilities;

/**
 *
 * @author CBURKE
 */
public class Attribute {
    
    public String name;
    public String label;
    public String type;
    public Object initialValue;
    public boolean generate;
    public String breed;
    
    public String getBreed(){
    	return breed;
    }
    
    public Attribute(String n, String b) {
        name=n; breed=b; generate=true;
    }

    public Attribute(String n, String b, String l, String t, Object v, boolean g) {
        this(n, b);
        label = l;
        type = t;
        initialValue = v;
        generate = g;
    }

    protected String camelCase(String text) {
        return camelCase(text, true);
    }

    protected String camelCase(String text, boolean cap) {
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
        if (cap) buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
        return buf.toString();
    }

    public String baseName() {
    	return baseName(true);
    }
    
    public String baseName(boolean cap) {
        if (breed.equals("*CLOSURE*")) {
            // special local variables get special treatment
            if (name.length() == 1) {
                return "__0";
            }
            return "__" + name.substring(1);
        } else if (breed.equals("*LOCAL*") || breed.equals("*FORMAL*")) {
            return camelCase(name, false);
        }
        return camelCase(name, cap);
    }
    
    /**
     * Generate a canonical name for the method that is invoked to set the
     * value of this attribute on the target object.
     * 
     * @return
     */
    public String setter() {
        if (breed.equals("*CLOSURE*")) {
            throw new RuntimeException("Setting of '?' variables not permitted!");
        } else if (breed.equals("*LOCAL*") || breed.equals("*FORMAL*")) {
            return camelCase(name, false);
        }
        return "set"+camelCase(name);
    }
    
    /**
     * Generate a canonical name for the method that is invoked to get the
     * value of this attribute from the target object.
     * 
     * @return
     */
    public String getter() {
        if (breed.equals("*CLOSURE*")) {
            // special local variables get special treatment
            if (name.length() == 1) {
                return "__0";
            }
            return "__" + name.substring(1);
        } else if (breed.equals("*LOCAL*") || breed.equals("*FORMAL*")) {
            return camelCase(name, false);
        }
        return "get"+camelCase(name)+"()";
    }
    
    /**
     * Set this attribute to the specified value on the target object.
     * Only used when the code is interpreted.
     * 
     * @param target
     * @param value
     */
    public void set(Object target, Object value) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Get the value of this attribute on the specified object.
     * Only used when the code is interpreted.
     * 
     * @param target
     * @return
     */
    public Object get(Object target) {
        throw new UnsupportedOperationException();
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
//        if (breed.equalsIgnoreCase("*global*")) {
//        	buf.append("Model.getInstance().");
//        }
        buf.append(WizardUtilities.getJavaName(name));
        return buf.toString();
    }

    public String toStringOrig() {
        StringBuffer buf = new StringBuffer();
        buf.append("@");
        if (breed != null) {
            buf.append(breed);
            buf.append(".");
        }
        buf.append(name);
        return buf.toString();
    }
}
