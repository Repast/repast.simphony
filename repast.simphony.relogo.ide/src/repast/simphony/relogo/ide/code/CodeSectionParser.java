package repast.simphony.relogo.ide.code;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

// NOTE: current segregation of elements into code sequences and code blocks
// causes some problems
public class CodeSectionParser {

    protected SymbolTable symbolTable;
    protected LinkedList<ProcedureDefinition> generatedProcedures;
    protected LinkedList<String> includeFiles;

    public void dumpTree(CommonTree tree) {
        dumpChildren(tree, 0);
    }

    public CodeSectionParser() {
        symbolTable = new SymbolTable();
    }

    public CodeSectionParser(StringBuffer sb) {
        this();
        parse(sb.toString());
    }

    public CodeSectionParser(String s) {
        this();
        parse(s);
    }
    boolean ok;

    public boolean parse(final String s) {
        ok = true;
        NetLogoRGWLexer lex = new NetLogoRGWLexer(new ANTLRStringStream(s));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        NetLogoRGWParser g = new NetLogoRGWParser(tokens) {

            public String getErrorMessage(RecognitionException e, String[] tokenNames) {
                System.err.println("Problem with parsing interface stream: " + s);
                ok = false;
                return super.getErrorMessage(e, tokenNames);
            }

            public void reportError(RecognitionException e) {
                System.err.println("Problem with parsing interface stream: " + s);
                ok = false;
                super.reportError(e);
            }
        };
        try {
            // parse the file
            CommonTree tree = (CommonTree) g.prog().getTree();
            if (tree == null) {
                return false;
            }
            // build the symbol table
            if (tree.getToken() != null) {
                extractSymbols(tree);
            } else {
                int n = tree.getChildCount();
                if (n > 0) {
                    for (int i = 0; i < n; i++) {
                        extractSymbols((CommonTree) tree.getChild(i));
                    }
                }
            }
            // process executable statements, now that all procedures, breeds,
            // and reporters have been defined.
        } catch (RecognitionException e) {
            ok = false;
            e.printStackTrace();
        }
        return ok;
    }

    public LinkedList<String> getIncludeFiles() {
        return includeFiles;
    }

    public LinkedList<ProcedureDefinition> getGeneratedProcedures() {
        return generatedProcedures;
    }

    public MethodPartition getMethodPartition() {
        return new MethodPartition(generatedProcedures, symbolTable);
    }

    protected LinkedHashMap<String, Object> getSymbolCategory(String type) {
        return symbolTable.getSymbolCategory(type);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    public void declareClass(String cls, String parent) {
        symbolTable.declareClass(cls, parent);
    }

//    public void declareExtensionSymbols() {
//        symbolTable.declareExtensionSymbols();
//    }

    public void declareSymbol(String domain, String name, Object data) {
        symbolTable.declareSymbol(domain, name, data);
    }

    public void declareAttribute(String domain, String name) {
        symbolTable.declareAttribute(domain, name);
    }

    public void declareAttribute(String domain, String name, String label, String type, Object initialValue, boolean generate) {
        symbolTable.declareAttribute(domain, name, label, type, initialValue, generate);
    }
    
    public void declareAttribute(String domain, String name, String label, String type, Object initialValue) {
        symbolTable.declareAttribute(domain, name, label, type, initialValue, true);
    }

    public void declareAttribute(Attribute attr) {
        symbolTable.declareAttribute(attr);
    }

    public void declarePrimitive(Profile prof) {
        symbolTable.declarePrimitive(prof);
    }

    protected void declareBreed(String plural, String singular) {
        symbolTable.declareBreed(plural, singular);
    }

    protected void declareDirectedLinkBreed(String plural, String singular) {
        symbolTable.declareDirectedLinkBreed(plural, singular);
    }

    protected void declareUndirectedLinkBreed(String plural, String singular) {
        symbolTable.declareUndirectedLinkBreed(plural, singular);
    }

    protected void declareSymbols(String domain, CommonTree block) {
        if (block == null) {
            return;
        }
        if (domain.equals("global")) {
            domain = "*GLOBAL*";
            int n = block.getChildCount();
            for (int i = 0; i < n; i++) {
                CommonTree decl = (CommonTree) block.getChild(i);
                String var = decl.getToken().getText();
                if (!var.equals("IDN")) {
                    System.err.println("global attribute " + var + " not IDN!");
                } else {
                    decl = (CommonTree) decl.getChild(0);
                    var = decl.getToken().getText();
                    declareAttribute(domain, var);
                }
            }
        } else if (domain.equals("extensions")) {
        	// Not supporting extensions
            /*domain = "*EXTENSION*";
            int n = block.getChildCount();
            for (int i = 0; i < n; i++) {
                CommonTree decl = (CommonTree) block.getChild(i);
                String var = decl.getToken().getText();
                if (!var.equals("IDN")) {
                    System.err.println("extension name " + var + " not IDN!");
                } else {
                    decl = (CommonTree) decl.getChild(0);
                    var = decl.getToken().getText();
                    declareSymbol(domain, var, null);
                    symbolTable.declareExtension(var);
                }
            }*/
        } else if (domain.equals("__include")) {
            includeFiles = new LinkedList<String>();
            int n = block.getChildCount();
            for (int i = 0; i < n; i++) {
                CommonTree decl = (CommonTree) block.getChild(i);
                String var = decl.getToken().getText();
                if (!var.equals("STR")) {
                    System.err.println("include file " + var + " not STR!");
                } else {
                    decl = (CommonTree) decl.getChild(0);
                    var = decl.getToken().getText();
                    includeFiles.add(var);
                }
            }
        } else if (domain.equals("directed-link-breed")) {
            // [plural singular]
            // defines the following procedures:
            // create-<singular>-from create-<plural>-from
            // create-<singular>-to create-<plural>-to
            // in-<singular>-neighbor? in-<singular>-neighbors
            // in-<singular>-from my-in-<plural> my-out-<plural>
            // out-<singular>-neighbor? out-<singular>-neighbors
            // out-<singular>-to 
            String plural = null;
            if (block.getChildCount() >= 1) {
                plural = ((CommonTree) block.getChild(0).getChild(0)).getToken().getText();
            }
            String singular = null;
            if (block.getChildCount() >= 2) {
                singular = ((CommonTree) block.getChild(1).getChild(0)).getToken().getText();
            }
            declareDirectedLinkBreed(plural, singular);
        } else if (domain.equals("undirected-link-breed")) {
            // [plural singular]
            // defines the following procedures:
            //  create-<singular>-with create-<plural>-with
            // <singular>-neighbor? <singular>-neighbors
            // <singular>-with my-<plural>
            String plural = null;
            if (block.getChildCount() >= 1) {
                plural = ((CommonTree) block.getChild(0).getChild(0)).getToken().getText();
            }
            String singular = null;
            if (block.getChildCount() >= 2) {
                singular = ((CommonTree) block.getChild(1).getChild(0)).getToken().getText();
            }
            declareUndirectedLinkBreed(plural, singular);
        } else if (domain.endsWith("-own")) {
            domain = domain.substring(0, domain.length() - 4);
            // first assume that the domain is a legal breed.
            int n = block.getChildCount();
            for (int i = 0; i < n; i++) {
                CommonTree decl = (CommonTree) block.getChild(i);
                String var = decl.getToken().getText();
                if (!var.equals("IDN")) {
                    System.err.println("own attribute " + var + " not IDN!");
                } else {
                    decl = (CommonTree) decl.getChild(0);
                    var = decl.getToken().getText();
//                    if (domain.contains("patches")){
//                    	domain = "tandp";//custom patch variables are accessible from both turtle and patch contexts
//                    }
                    declareAttribute(domain, var);
                }
            }
            // now verify that the resulting domain was a defined breed plural.
            LinkedHashMap<String, Object> breedAttributes = getSymbolCategory("*BREED*");
            Object isPlural = breedAttributes.get(domain);
            if (isPlural != null) {
                if ("plural".equals(isPlural)) {
                // we did the right thing.
                } else {
                    System.err.println("Warning: '" + domain + "' is not a plural breed!");
                }
                return;
            }
            breedAttributes = getSymbolCategory("*DIRECTED-LINK-BREED*");
            isPlural = breedAttributes.get(domain);
            if (isPlural != null) {
                if ("plural".equals(isPlural)) {
                // we did the right thing.
                } else {
                    System.err.println("Warning: '" + domain + "' is not a plural breed!");
                }
                return;
            }
            breedAttributes = getSymbolCategory("*UNDIRECTED-LINK-BREED*");
            isPlural = breedAttributes.get(domain);
            if (isPlural != null) {
                if ("plural".equals(isPlural)) {
                // we did the right thing.
                } else {
                    System.err.println("Warning: '" + domain + "' is not a plural breed!");
                }
                return;
            }
            System.err.println("Warning: '" + domain + "' is not a defined breed!");
        } else {
            System.err.println("unrecognized attribute declaration '" + domain + "'");
        }
    }

    public String getJavaName(String name) {
        StringBuffer buf = new StringBuffer();
        if (name == null || name.trim().length() == 0 || name.equals("null")) {
            buf.append("anonymous" + anonymousID++);
        } else {
            buf.append(name.trim());
        }
        for (int i = 0; i < buf.length(); i++) {
            if (Character.isLetterOrDigit(buf.charAt(i))) {
                continue;
            } else if (buf.charAt(i) == '_') {
                continue;
            } else if (buf.charAt(i) == '?') {
                buf.setCharAt(i, 'Q');
            }  else if (buf.charAt(i) == '%') {
                buf.setCharAt(i, 'p');
            } else if (buf.charAt(i) == '!') {
                buf.setCharAt(i, 'X');
            } else if (Character.isWhitespace(buf.charAt(i)) || buf.charAt(i) == '-') {
                buf.deleteCharAt(i);
                if (i < buf.length() && Character.isLetterOrDigit(buf.charAt(i))) {
                	if (buf.charAt(i) != '?' && buf.charAt(i) != '%' && buf.charAt(i) != '!'){
                		buf.setCharAt(i, Character.toUpperCase(buf.charAt(i)));
                	}
                	else if (buf.charAt(i) == '_') {
                        continue;
                    } else if (buf.charAt(i) == '?') {
                        buf.setCharAt(i, 'Q');
                    } else if (buf.charAt(i) == '%') {
                        buf.setCharAt(i, 'P');
                    } else if (buf.charAt(i) == '!') {
                        buf.setCharAt(i, 'X');
                    }
                }
            } else {
                buf.setCharAt(i, 'Q');
            }
        }
        if (Character.isDigit(buf.charAt(0))) {
            buf.insert(0, '_');
        }
        return buf.toString();
    }
    static private int anonymousID = 0;

    protected Block interpretExpression(CommonTree decl) {
        String declType = decl.getToken().getText();
        if (declType == null) {
            System.err.println("expression with null token!");
        } else if (declType.equals("CS")) {
            if (decl.getChildCount() >= 1) {
                Block code = generateCode((CommonTree) decl.getChild(0));
                return code;
            } else {
                System.err.println("CS with no children!");
            }
        } else {
            System.err.println("unrecognized declaration type '" + declType + "'");
        }
        return null;
    }

    protected void extractSymbols(CommonTree decl) {
        String declType = decl.getToken().getText();
        if (declType == null) {
            System.err.println("declaration with null token!");
        } else if (declType.equals("BREED")) {
            // block contains plural and singular names; singular may be optional.
            // defines the following procedures:
            // create-<plural>, hatch-<plural>, sprout-<plural>, <plural>-here,
            // <plural>-at, <plural>-on, is-a-<plural>?
            String plural = null;
            if (decl.getChildCount() >= 1) {
                plural = ((CommonTree) decl.getChild(0)).getToken().getText();
            }
            String singular = null;
            if (decl.getChildCount() >= 2) {
                singular = ((CommonTree) decl.getChild(1)).getToken().getText();
            }
            declareBreed(plural, singular);
        } else if (declType.equals("OWN")) {
        	//TODO: include bit to declare breeds for links
            String varType = ((CommonTree) decl.getChild(0)).getToken().getText();
            declareSymbols(varType, (CommonTree) decl.getChild(1));
        } else if (declType.equals("GLOBALS")) {
            declareSymbols("global", (CommonTree) decl.getChild(0));
        } else if (declType.equals("INCLUDES")) {
            declareSymbols("__include", (CommonTree) decl.getChild(0));
        } else if (declType.equals("ToEndBlock")) {
            String cmdName = ((CommonTree) decl.getChild(0)).getToken().getText();
            declareSymbol("*PROCEDURE*", cmdName, processCommand((CommonTree) decl));
        } else if (declType.equals("ToReportBlock")) {
            String rptName = ((CommonTree) decl.getChild(0)).getToken().getText();
            declareSymbol("*PROCEDURE*", rptName, processReporter((CommonTree) decl));
        } else {
            System.err.println("unrecognized declaration type '" + declType + "'");
        }
    }

    protected Profile processCommand(CommonTree decl) {
        String name = ((CommonTree) decl.getChild(0)).getToken().getText();
        int argCt = ((CommonTree) decl.getChild(1)).getChildCount();
        String profile = "void=void/";
        if (argCt == 0) {
            profile += "void";
        } else {
            for (int i = 0; i < argCt; i++) {
                if (i > 0) {
                    profile += ",";
                }
                profile += "*";
            }
        }
        Profile cmd = new Profile(name, profile);
        cmd.body = decl;
        return cmd;
    }

    protected Profile processReporter(CommonTree decl) {
        String name = ((CommonTree) decl.getChild(0)).getToken().getText();
        int argCt = ((CommonTree) decl.getChild(1)).getChildCount();
        String profile = "*=void/";
        if (argCt == 0) {
            profile += "void";
        } else {
            for (int i = 0; i < argCt; i++) {
                if (i > 0) {
                    profile += ",";
                }
                profile += "*";
            }
        }
        Profile rpt = new Profile(name, profile);
        rpt.body = decl;
        return rpt;
    }

    public static void dumpChildren(CommonTree tree, int depth) {
        if (tree == null) {
            return;
        }
        for (int j = 0; j < depth * 4; j++) {
            System.out.print(" ");
        }
        if (tree.getToken() == null) {
            System.out.print("*nil*");
        } else {
            System.out.print(tree.getToken().getText());
        }
        int n = tree.getChildCount();
        if (n > 0) {
            System.out.println("[");
            for (int i = 0; i < n; i++) {
                dumpChildren((CommonTree) tree.getChild(i), depth + 1);
            }
            for (int j = 0; j < depth * 4; j++) {
                System.out.print(" ");
            }
            System.out.println("]");
        } else {
            System.out.println();
        }
    }

    public LinkedList<ProcedureDefinition> generateCode() {
        generatedProcedures = new LinkedList<ProcedureDefinition>();
        LinkedHashMap<String, Object> procedures = symbolTable.getSymbolCategory("*PROCEDURE*");
        for (Map.Entry me : procedures.entrySet()) {
            if (me.getValue() != null) {
            	if (me.getValue() instanceof String){
            		System.out.println("me.getValue() returns the string " + me.getValue());
            	}
                for (Profile profile = (Profile) me.getValue(); profile != null; profile = profile.alternative) {
                    if (profile.body != null) {  // user-defined
                        Block code = new Block();
                        symbolTable.openScope(code);
                        CommonTree ast = (CommonTree) profile.body;
                        LinkedList<String> formals = new LinkedList<String>();
                        if (ast.getChild(1) != null) {
                            CommonTree ftree = (CommonTree) ast.getChild(1);
                            for (int st = 0; st < ftree.getChildCount(); st++) {
                                String formalName = getJavaName(((CommonTree) ftree.getChild(st).getChild(0)).getToken().getText());
                                formals.add(formalName);
                                symbolTable.defineLocalVariable(new Attribute(formalName, "*FORMAL*"));
                            }
                        }
                        LinkedList genCode = generateCode((CommonTree) ast.getChild(2), false);
                        symbolTable.closeScope();
                        code.setContents(genCode);
                        generatedProcedures.add(new ProcedureDefinition(profile, formals, code));
                    }
                }
            }
        }
        return generatedProcedures;
    }

    /**
     * Generate code for an anonymous, parameterless block from the AST.
     * Useful for generating interpretable instantiations of statements.
     * 
     * @param ast
     * @return
     */
    public Block generateCode(CommonTree ast) {
        Block code = null;
        if (ast != null) {
            code = new Block();
            symbolTable.openScope(code);
            LinkedList genCode = generateCode(ast, true);
            symbolTable.closeScope();
            code.setContents(genCode);
        } else {
        //
        }
        return code;
    }

    protected int getPrecedence(Object defns, boolean atParen) {
        int precedence = -1;
        for (Object o : (LinkedList) defns) {
            if (!(o instanceof Profile)) {
                continue;
            }
            Profile prof = (Profile) o;
            while (prof != null) {
                if ((prof.isVarargs() & atParen) || !prof.isVarargs()) {
                    if (precedence < prof.getPrecedence()) {
                        precedence = prof.getPrecedence();
                    }
                }
                prof = prof.alternative;
            }
        }
        return precedence;
    }

    protected boolean isInfix(Object defns, boolean atParen) {
        boolean infix = false;
        for (Object o : (LinkedList) defns) {
            if (!(o instanceof Profile)) {
                continue;
            }
            Profile prof = (Profile) o;
            while (prof != null) {
                if ((prof.isVarargs() & atParen) || !prof.isVarargs()) {
                    if (prof.getPreSize() > 0) {
                        infix = true;
                    }
                }
                prof = prof.alternative;
            }
        }
        return infix;
    }

    protected int getReductions(Object defns, LinkedList work, LinkedList<Profile> possibleReductions, boolean atParen, int pos) {
        int stackPrecedence = -1;
        int stackReductionSize = 0;
        for (Object o : (LinkedList) defns) {
            if (!(o instanceof Profile)) {
                continue;
            }
            // we found an operator, so proceed no farther
            stackPrecedence = 0;
            // something we could possibly reduce
            Profile prof = (Profile) o;
            while (prof != null) {
                // for any command with different syntax depending on
                // whether enclosed in parentheses, 
                if ((prof.isVarargs() && atParen && (pos == 0)) || !prof.isVarargs()) {
                    if (stackPrecedence < prof.getPrecedence()) {
                        stackPrecedence = prof.getPrecedence();
                    }
                    // exclude any which require more context than is
                    // already available, because they can't possibly
                    // reduce with the available data. 
                    int postSize = prof.getPostSize();
                    if (pos < prof.getPreSize()) {
                    // not enough preceding context to support this interpretation
                    } else if ((pos + postSize) < work.size()) {
                        //the contents of work can reduce without
                        // appending this operator.
                        possibleReductions.add(prof);
                        if (prof.getSize() > stackReductionSize) {
                            stackReductionSize = prof.getSize();
                        }
                    }
                }
                prof = prof.alternative;
            }
        }
        return stackPrecedence;
    }

    protected int getReductionSize(LinkedList<Profile> possibleReductions) {
        int stackReductionSize = 0;
        for (Profile prof : possibleReductions) {
            if (prof.getSize() > stackReductionSize) {
                stackReductionSize = prof.getSize();
            }
        }
        return stackReductionSize;
    }

    protected void tryReduction(LinkedList newDefns, LinkedList work, boolean atParen) {
        // get precedence of operator
        int precedence = getPrecedence(newDefns, atParen);
        boolean infix = isInfix(newDefns, atParen);
        int tos = work.size() - 1; //if work is empty, the for loop is skipped
        for (int i = tos; i >= 0; i--) {
            Object w = work.get(i);
            if (w instanceof Profile) {
                System.out.println("assertion violation!");
            }
            if (w instanceof LinkedList) {
                LinkedList<Profile> possibleReductions = new LinkedList<Profile>();
                int stackPrecedence = getReductions(w, work, possibleReductions, atParen, i);
                if (stackPrecedence < 0) {   // didn't find an op yet.
                    continue;
                }
                // reduce what's on the stack if it's higher precedence than
                // the new operator, or if it's a zero-argument functor,
                // or if the stacked arguments for the stacked operator do not
                // overlap with the new operator. The goal is to reduce operators
                // as early as possible, so that side-effects of the reduction
                // are applied as early as possible. The primary example of this
                // is the 'let' command.
                int reductionSize = getReductionSize(possibleReductions);
                if (precedence < stackPrecedence ||
                        reductionSize == 1 ||
                        !infix && reductionSize <= (tos - i + 1) ||
                        infix && reductionSize <= (tos - i)) {
                    // try reducing by this procedure
                    // first sort by decreasing size
                    for (int k = 0; k < possibleReductions.size() - 1; k++) {
                        for (int m = k + 1; m < possibleReductions.size(); m++) {
                            Profile pk = possibleReductions.get(k);
                            Profile pm = possibleReductions.get(m);
                            if (pk.getSize() < pm.getSize()) {
                                possibleReductions.set(k, pm);
                                possibleReductions.set(m, pk);
                            }
                        }
                    }
                    //System.out.println("Trying to reduce by " + possibleReductions);
                    for (Profile prof : possibleReductions) {
                        if (prof.isCompatibleWith(work, i)) {
                            int start = i - prof.getPreSize();
                            int end = 0;
                            if (prof.isVarargs()) {
                                // absorb everything in this scope
                                end = work.size();
                            } else {
                                //System.out.println("Reducing by "+prof);
                                end = i + prof.getPostSize() + 1;
                            }
                            ProcedureInvocation pi = new ProcedureInvocation(prof, work, start, i, end);
                            // apply side-effects, if any.
                            // since the only side-effect known at this time
                            // is the definition of local symbols, we hack it
                            // in directly rather than adding more complexity.
                            if (prof.name.equals("let")) {
                                Object arg = pi.arguments.get(0);
                                if (!(arg instanceof Attribute)) {
                                    System.err.println("'let' argument 1 is not a variable name!");
                                }
                                Attribute attr = (Attribute) arg;
                                if (!attr.breed.equals("*LOCAL*")) {
                                    System.err.println("'let' argument 1 hides another name!");
                                }
                                symbolTable.defineLocalVariable(attr);
                            }
                            for (int k = start; k < end; k++) {
                                work.remove(start);
                            }
                            work.add(start, pi);
                            // this succeeded, now try again against the
                            // modified stack.
                            tryReduction(newDefns, work, atParen);
                            break;
                        }
                    }
                }
                // whether it reduced or not, exit.
                break;
            }
        }
    }

    /**
     * Once code has been generated, statements which are to be assigned
     * as arguments are already resolved to concrete interpretations.
     * Procedures which are to be interpreted in value closures, however,
     * may still be represented as lists. Fix all of these by taking the first
     * available interpretation.
     * 
     * @param contents
     * @return
     */
    protected LinkedList disambiguateBlock(LinkedList contents) {
        if (contents.isEmpty()) {
            return contents;
        }
        LinkedList newContents = new LinkedList();
        for (Object o : contents) {
            if (o instanceof LinkedList) {
                newContents.addLast(((LinkedList)o).getFirst());
            } else {
                newContents.addLast(o);
            }
        }
        return newContents;
    }
    
    public LinkedList generateCode(CommonTree statement, boolean inParen) {
        LinkedList work = new LinkedList();
        if (statement == null) {
            /* procedure is a no-op, return an empty op list */
            return work;
        }
        int maxch = statement.getChildCount();
        for (int ch = 0; ch < maxch; ch++) {
            boolean atParen = inParen && (ch == 0);
            /*
             * Shift the next child onto the stack.
             */
            CommonTree child = (CommonTree) statement.getChild(ch);
            if (child.getChildCount() != 0) {
                // element is composite, generate contents
                if (child.getToken().getText().equals("INUM")) {
                    try {
                        long n = Long.parseLong(child.getChild(0).getText());
                        //System.out.println("integer=" + n);
                        work.addLast(n);
                    } catch (Exception e) {
                        System.out.println("Exception while processing integer constant!");
                    }
                } else if (child.getToken().getText().equals("FNUM")) {
                    try {
                        double d = Double.parseDouble(child.getChild(0).getText());
                        //System.out.println("double=" + d);
                        work.addLast(d);
                    } catch (Exception e) {
                        System.out.println("Exception while processing floating-point constant!");
                    }
                } else if (child.getToken().getText().equals("CB")) {
                    // this is either a command block, a reporter,
                    // or a list of items. create a local scope for
                    // variable declarations.
                    Block scope = new Block();
                    symbolTable.openScope(scope);
                    Object subordinateCode = generateCode(child, false);
                    symbolTable.closeScope();
                    scope.setContents(disambiguateBlock((LinkedList) subordinateCode));
                    work.addLast(scope);
                } else if (child.getToken().getText().equals("Paren")) {
                    Object subordinateCode = generateCode(child, true);
                    // if we reduced to a single element, remove the paren.
                    // Rev: we don't want to do this as it can affect operator precedence
                    if (subordinateCode != null) {
                        LinkedList subCodeList = (LinkedList) subordinateCode;
                        work.addLast(new Paren((LinkedList) subordinateCode));
//                        if (subCodeList.size() == 1) {
//                            work.addLast(subCodeList.get(0));
//                        } else {
//                            work.addLast(new Paren((LinkedList) subordinateCode));
//                        }
                    } else {
                        System.err.println("empty parentheses removed");
                    }
                } else if (child.getToken().getText().equals("STR")) {
                    // this is a string constant
                    String stringText = child.getChild(0).getText();
                    work.addLast(stringText);
                } else if (child.getToken().getText().equals("IDN")) {
                    // this is an identifier or arithmetic symbol
                    String symbol = child.getChild(0).getText();
                    // lookup the item in the symbol table.
                    // if we're immediately enclosed in parentheses,
                    // try to find a varargs alternate first
                    Object defns = symbolTable.findSymbol(symbol);
                    if (atParen) {
                        Object altDefns = symbolTable.findSymbol("(" + symbol);
                        if (altDefns != null) {
                            defns = altDefns;
                        }
                    }
                    if (defns == null) {
                        // this is commonly showing up as either local attributes
                        // defined with 'let', which are not yet added to the
                        // symbol table, or negative integers, which the
                        // excessively loose token syntax is treating as
                        // identifiers.
                        try {
                            long longValue = Long.parseLong(symbol);
                            if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
                                work.addLast(longValue);
                            } else {
                                work.addLast((int) longValue);
                            }
                        } catch (NumberFormatException nfe) {
                        	Attribute attr = new Attribute(symbol,"*LOCAL*",null,null,null,false);
                        	work.addLast(attr);
                            // We could not convert it to an integer. Assume that
                            // this is a locally defined symbol which has not
                            // yet been defined; no permitted in this app.
//                            throw new RuntimeException("Undefined symbol " + symbol);
                        }
                    } else {
                        //System.out.println("value: "+value+" "+defns);
                    	LinkedList defnsList = (LinkedList) defns;
                        tryReduction(defnsList, work, atParen);
                        // If defnsList contains only one Attribute, then just add the Attribute.
                        if (defnsList.size() == 1 && defnsList.get(0) instanceof Attribute){
                        	work.addLast((Attribute) defnsList.get(0));
                        }
                        else {
                        	work.addLast(defns);
                        }
                    }
                } else {
                	// This bit doesn't get visited, as far as I know.
                    Object subordinateCode = generateCode(child, false);
                    work.addLast(subordinateCode);
                }
            } else {
                String value = child.getToken().getText();
                if (child.getToken().getText().equals("CB")) {
                    // this is either a command block, a reporter,
                    // or a list of items. whatever it is, it is empty.
                    work.addLast(new Block(null));
                } else {
                    // element is atomic; there shouldn't be any cases of this!
                    System.out.println("###  Atomic element " + value);
                }
            }
            // this flag is only here to handle those few functions which
            // have different signatures (variable number of arguments)
            // if they are immediately preceded by an open parenthesis.
            // It's possible that the reference implementation of NetLogo
            // handles this by incorporating the parenthesis into the token.
            // This implementation is doing it by setting a flag upon entry
            // into a parenthesized expression, and if the first token is
            // a function with variable arguments that syntax is triggered.
            // Since we have now passed the first child, deactivate the
            // varargs flag.
            atParen = false;
        }
        /*
         * Reduce any operators which remain, whose precedence should not
         * conflict with each other any more.
         */
        boolean reducing = true;
        while (reducing) {
            reducing = false; // if we fall out, there are no ops to reduce
            for (int i = work.size() - 1; i >= 0; i--) {
                boolean atParen = inParen && (i == 0);
                Object w = work.get(i);
                if (w instanceof LinkedList) {
                    boolean pendingReduction = false;
                    LinkedList<Profile> possibleReductions = new LinkedList<Profile>();
                    int stackPrecedence = getReductions(w, work, possibleReductions, atParen, i);
                    if (possibleReductions.isEmpty()) {
                        //System.out.println("No reductions for stack");
                    } else {
                        // try reducing by this procedure
                        // first sort by decreasing size
                        for (int k = 0; k < possibleReductions.size() - 1; k++) {
                            for (int m = k + 1; m < possibleReductions.size(); m++) {
                                Profile pk = possibleReductions.get(k);
                                Profile pm = possibleReductions.get(m);
                                if (pk.getSize() < pm.getSize()) {
                                    possibleReductions.set(k, pm);
                                    possibleReductions.set(m, pk);
                                }
                            }
                        }
                        //System.out.println("Trying to reduce by " + possibleReductions);
                        for (Profile prof : possibleReductions) {
                            if (prof.isCompatibleWith(work, i)) {
                                int start = i - prof.getPreSize();
                                int end = 0;
                                if (prof.isVarargs()) {
                                    // absorb everything in this scope
                                    end = work.size();
                                } else {
                                    //System.out.println("Reducing by "+prof);
                                    end = i + prof.getPostSize() + 1;
                                }
                                ProcedureInvocation pi = new ProcedureInvocation(prof, work, start, i, end);
                                for (int k = start; k < end; k++) {
                                    work.remove(start);
                                }
                                work.add(start, pi);
                                pendingReduction = true;
                                reducing = true;
                                break;
                            }
                        }
                    }
                    if (pendingReduction) {
                        break;
                    }
                }
            }
        }
        //System.out.println(work);
        return work;
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
}
