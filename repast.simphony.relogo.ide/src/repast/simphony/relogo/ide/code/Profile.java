/**
 * Reflects a parameter profile for a command or reporter in Netlogo.
 */
package repast.simphony.relogo.ide.code;

import java.util.LinkedList;

/**
 *
 * @author CBURKE
 */
public class Profile {

    protected String name;
    protected String lowerCaseName;
    //TODO: need to allow for ambiguous owning class (turtle/patch or turtle/link ambiguity)
    protected String breed; // owning class, if any
    protected Object context;   // what is the immediate context?
    protected boolean contextCreator;  // creates a separate context, either in an argument or as a result.
    protected Object pre;   // what is the prefix argument (subject)?
    protected Object[] post; // what are the first postfix arguments?
    protected String result;    // what is the result of this operation? (reporters only)
    protected Object body;

    /**
     * Creates a global method.
     * 
     * @param n
     * @param pro
     */
    public Profile(String n, String pro) {
        name = n;
        lowerCaseName = n.toLowerCase();
        parseProfile(pro);
    }
    public Profile alternative;

    public void addAlternative(Profile p) {
        if (alternative == null) {
            alternative = p;
            p.alternative = null;  // belong to one and only one chain
        } else {
            alternative.addAlternative(p);
        }
    }

    public void setCode(Object b) {
        body = b;
    }

    /**
     * Creates a member method of a specified breed.
     * 
     * @param n
     * @param b
     * @param pro
     */
    public Profile(String n, String b, String pro) {
        name = n;
        lowerCaseName = n.toLowerCase();
        if (b != null) {
            breed = b.toLowerCase();
        /* has unfortunate side-effects when partitioning methods.
        name = name.replace("<breed>", breed);
        name = name.replace("<breeds>", breed);
        pro = pro.replace("<breed>", breed);
        pro = pro.replace("<breeds>", breed);
         */
        }
        parseProfile(pro);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (isVarargs()) {
            buf.append("(");
        }
        buf.append(name);
        buf.append("{");
        buf.append(result == null ? "void" : result);
        buf.append("=");
        buf.append(pre == null ? "void" : result);
        buf.append("/");
        if (post == null) {
            buf.append("void");
        } else {
            for (int i = 0; i < post.length; i++) {
                if (i > 0) {
                    buf.append(",");
                }
                buf.append(post[i]);
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public int getPreSize() {
        return (pre != null ? 1 : 0);
    }

    public int getPostSize() {
        return (post == null ? 0 : post.length);
    }

    public int getSize() {
        return getPreSize() + 1 + getPostSize();
    }
    static private final int DEFAULT_PRECEDENCE = 100;
    static private final int REPORTER_PRECEDENCE = 200;
    private int precedence = -1;

    public void setPrecedence(int prec) {
        precedence = prec;
        if (alternative != null) {
            alternative.setPrecedence(prec);
        }
    }

    public int getPrecedence() {
        if (precedence != -1) {
            return precedence;
        }
        return (result == null ? DEFAULT_PRECEDENCE : REPORTER_PRECEDENCE);
    }
    private boolean _varargs = false;

    public boolean isVarargs() {
        return _varargs;
    }

    public boolean createsContext() {
        return contextCreator;
    }

    public Object disambiguateParameter(int pos, int postCnt, Object actual) {
        Object typeCode = null;
        switch (pos) {
            case -1:
                typeCode = pre;
                break;
            case 0:
                break;
            default:
                if (isVarargs()) {
                    if (post.length == 1) {
                        typeCode = post[0];
                    } else if (pos < (postCnt - post.length) + 1) {
                        typeCode = post[0];
                    } else {
                        pos = pos - (postCnt - post.length);
                        try {
                            typeCode = post[pos - 1];
                        } catch (IndexOutOfBoundsException ioobe) {
                            ioobe.printStackTrace();
                        }
                    }
                } else {
                    try {
                        typeCode = post[pos - 1];
                    } catch (IndexOutOfBoundsException ioobe) {
                        ioobe.printStackTrace();
                    }
                }
                break;
            }
        if (actual instanceof LinkedList) {
            // greedily grab the first interpretation that is compatible
//        	System.out.println("actual "+ actual + " in Profile " + getJavaName() + " is instanceof LinkedList");
        	//TODO: not sure how this makes sense, what if the method takes a list? why is this just looking at the type of
        	// 		a "random" list element?
            for (Object o : ((LinkedList) actual)) {
                if (typeIsCompatible(typeCode, o)) {
                    actual = o;
                }
            }
        }
        // if the typeCode specifies a closure, we need to make sure we have
        // a block that can be marked as a closure. Other type checks would
        // be good, too, but the closure activities have to be done to support
        // distribution of methods to agent classes.
        if (typeCode.toString().equals("cmd")) {
            if (actual instanceof Block) {
                ((Block) actual).setType("void-closure");
            } else {
                //System.err.println("*** void closure required, creating block");
                Block voidBlock = new Block();
                voidBlock.setType("void-closure");
                LinkedList contents = new LinkedList();
                contents.add(actual);
                voidBlock.setContents(contents);
                actual = voidBlock;
            }
        } else if (typeCode.toString().equals("rpt")) {
            if (actual instanceof Block) {
                ((Block) actual).setType("value-closure");
            } else {
                //System.err.println("*** value closure required, creating block: "+actual);
                // NOTE: empirically this happens quite a bit; should look
                // more closely at the symbol table to see if it's necessary.
                Block valueBlock = new Block();
                valueBlock.setType("value-closure");
                LinkedList contents = new LinkedList();
                contents.add(actual);
                valueBlock.setContents(contents);
                actual = valueBlock;
            }
        } else if (typeCode.toString().contains("list") || typeCode.toString().contains("agtset") || typeCode.toString().equals("*")) {
        	if (actual instanceof Block) {
                ((Block) actual).setType("list");
            }
        	
        }
        
        return actual;
    }

    protected boolean typeIsCompatible(Object typeCode, Object actual) {
        if (actual instanceof LinkedList) {
            // it's compatible if one of the interpretations of the actual
            // parameter is compatible. it would be better to actually update
            // the list with the actuals that are compatible, but that needs
            // more machinery to carry off correctly.
            boolean compatible = false;
            for (Object o : ((LinkedList) actual)) {
                compatible = compatible || typeIsCompatible(typeCode, o);
            }
            return compatible;
        } else {
            // test the type codes against the type of the actual parameter.
            // this may be the Java class or some other type indicator.
            if (typeCode.toString().equals("sel")) {
                return (actual instanceof Selector);
            }
            if (typeCode.toString().equals("breed")) {
                return (actual.equals("plural"));
            }
        }
        return true;
    }

    public boolean isCompatibleWith(LinkedList work, int pos) {
        boolean compatible = true;
        if (getPreSize() > 0) {
            compatible = compatible && typeIsCompatible(pre, work.get(pos - 1));
        }
        if (post != null) {
            for (int i = 0; i < post.length; i++) {
                compatible = compatible && typeIsCompatible(post[i], work.get(pos + i + 1));
            }
        }
        return compatible;
    }

    protected void parseProfile(String pro) {
        String[] lvl0 = pro.split("!");
        if (lvl0.length > 1) {
            for (int i = 1; i < lvl0.length; i++) {
                if (breed == null) {
                    addAlternative(new Profile(name, lvl0[i]));
                } else {
                    addAlternative(new Profile(name, breed, lvl0[i]));
                }
            }
        }
        if (lvl0[0].startsWith("[")) {
            context = lvl0[0].substring(1, lvl0[0].indexOf("]"));
            lvl0[0] = lvl0[0].substring(lvl0[0].indexOf("]") + 1);
        }
        if (lvl0[0].startsWith("(")) {
            _varargs = true;
            lvl0[0] = lvl0[0].substring(1);
        }
        if (lvl0[0].startsWith("@")) {
            contextCreator = true;
            lvl0[0] = lvl0[0].substring(1);
        }
        String[] lvl1 = lvl0[0].split("=");
        if (!lvl1[0].equals("void")) {
            result = lvl1[0];
        }
        if (lvl1.length <= 1) {
            return;
        }
        String[] lvl2 = lvl1[1].split("/");
        if (!lvl2[0].equals("void")) {
            pre = lvl2[0];
        }
        String[] lvl3 = lvl2[1].split(",");
        if (!lvl3[0].equals("void")) {
            post = new Object[lvl3.length];
            for (int i = 0; i < lvl3.length; i++) {
                post[i] = lvl3[i];
            }
        }
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
        return buf.toString();
    }

    public String getJavaName() {
        if (name == null) {
            return null;
        } else {
            return camelCase(name);
        }
    }

    /**
     * Return the calculated result type for this instance of this method.
     * Override this method if the return type is dependent upon the type
     * of one or more arguments. For example, the 'with' operator in NetLogo
     * selects a subset of an existing agentset, therefore it has a return type
     * that depends on the agentset argument.
     * 
     * @param pi Specific instance of this method to be checked.
     * @return
     */
    protected String getReturnType(ProcedureInvocation pi) {
        return result;
    }

    protected String encodeArguments(LinkedList list, int indent) {
        StringBuffer buf = new StringBuffer();
        boolean notFirst = false;
        for (Object o : list) {
            if (notFirst) {
                buf.append(", ");
            } else {
                notFirst = true;
            }
            encodeArgument(o, indent, buf);
        }
        return buf.toString();
    }

    protected void encodeArgument(Object o, int indent, StringBuffer buf) {
        if (o instanceof Block) {
        	buf.append(((Block) o).toString(indent + 4));
//        	if (((Block) o).getType() != null && ((Block) o).getType().equals("list")){
//        		buf.append(((Block) o).toStringAsList());
//        	}
//        	else {
//        		buf.append(((Block) o).toString(indent + 4));
//        	}
        } else if (o instanceof ProcedureInvocation) {
            buf.append(((ProcedureInvocation) o).toString(indent + 4));
        } else if (o == null) {
            buf.append(" /* NULL ARG NO-OP */ ");
        } else {
            buf.append(o.toString());
        }
    }

    public String encodeInstance(ProcedureInvocation pi, int indent) {
        StringBuffer buf = new StringBuffer();
        if (pi.SHOW_PI_TYPE) {
            buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
        }
        if (getPreSize() > 0) {
            encodeArgument(pi.arguments.get(0), indent, buf);
            buf.append(" ");
            buf.append(getJavaName());
            for (int i = 0; i < getPostSize(); i++) {
                buf.append(" ");
                encodeArgument(pi.arguments.get(i + 1), indent, buf);
            }
        } else {
            buf.append(getJavaName() + "(" + encodeArguments(pi.arguments, indent) + ")");
        }
        return buf.toString();
    }

    /**
     * Execute the code associated with this Profile for the specified
     * ProcedureInvocation, using the specified object as the context object.
     * 
     * @param pi
     * @return
     */
    public Object executeInstance(ProcedureInvocation pi, Object context) {
        LinkedList args = new LinkedList();
        if (pi.arguments != null) {
            for (Object arg : pi.arguments) {
                if (arg instanceof Attribute) {
                    Attribute attr = (Attribute) arg;
                    args.add(attr.get(context));
                } else if (arg instanceof Block) {
                    args.add(arg);  // defer evaluation of this block
                } else if (arg instanceof Number) {
                    args.add(arg);  // numeric constant needs no evaluation
                } else if (arg instanceof String) {
                    args.add(arg);  // string constant needs no evaluation
                } else if (arg instanceof ProcedureInvocation) {
                    ProcedureInvocation api = (ProcedureInvocation) arg;
                    args.add(api.profile.executeInstance(api, context));
                } else {
                    throw new ClassCastException(breed+":"+name);
                }
            }
        }
        return execute(args, context);
    }

    public Object execute(LinkedList args, Object context) {
        throw new UnsupportedOperationException(breed+":"+name);
    }
}
