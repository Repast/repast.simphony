/*
 * Represents the invocation of some procedure (including access of an
 * attribute) within a Netlogo simulation.
 * TODO: add a method that permits execution vectored through the Profile
 * for interpreted operation.
 */
package repast.simphony.relogo.ide.code;

import java.util.LinkedList;

/**
 *
 * @author CBURKE
 */
public class ProcedureInvocation {

    static public boolean SHOW_PI_TYPE = false;
    protected Object type;
    protected Profile profile;
    protected LinkedList arguments;

    public ProcedureInvocation(Profile p, LinkedList source, int start, int op, int end) {
        profile = p;
        arguments = new LinkedList();
        for (int i = start; i < end; i++) {
            if (i == op) {
                continue;
            }
            try {
                Object actualParameter = p.disambiguateParameter(i - op, end - op, source.get(i));
                arguments.addLast(actualParameter);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        type = calculateType(profile.result);
    }

    protected String getType(Object o) {
        String argType = null;
        for (int i = 0; i < arguments.size(); i++) {
            if (o != arguments.get(i)) {
                continue;
            }
            if (profile.getPreSize() > 0) {
                if (i == 0) {
                    argType = (String) profile.pre;
                } else {
                    argType = (String) profile.post[i - 1];
                }
            } else {
                // this does not handle varargs calls. TODO: fix!
                argType = (String) profile.post[i];
            }
            if (o instanceof ProcedureInvocation) {
                argType = ((ProcedureInvocation) o).calculateType(argType);
            }
        }
        return argType;
    }

    public Profile getProfile() {
        return profile;
    }
    
    public LinkedList getArguments() {
        return arguments;
    }
    
    public String calculateType(String result) {
        if (result == null) {
        // method does not return a value.
        } else if (result.equals("agtset")) {
            if (profile.breed != null) {
                result = "agtset(" + profile.breed + ")";
            } else {
                // need to probe arguments for type information
                result = profile.getReturnType(this);
            }
        } else if (result.startsWith("agtset(")) {
        // result passed through unchanged
        } else if (result.equals("agt")) {
            if (profile.breed != null) {
                result = "agt(" + profile.breed + ")";
            } else {
                // need to probe arguments for type information
                result = profile.getReturnType(this);
            }
        } else if (result.startsWith("agt(")) {
        // result passed through unchanged
        } else {
        // not a type which influences location of routines,
        // so not immediately useful in early parsing.
        // TODO: elaborate if more detailed compile-time type checking
        // is desired.
        }
        return result;
    }

    public String toString() {
        return toString(4);
    }

    public String toString(int indent) {
        return profile.encodeInstance(this, indent);
    }

    public Object execute(Object context) {
        return profile.executeInstance(this, context);
    }
}
