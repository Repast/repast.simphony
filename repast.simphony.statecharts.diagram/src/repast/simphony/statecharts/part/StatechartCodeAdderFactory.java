/**
 * 
 */
package repast.simphony.statecharts.part;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;

import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Factory for creating a statechart code adder.
 * 
 * @author Nick Collier
 */
public class StatechartCodeAdderFactory {

  public static StatechartCodeAdder createCodeAdder(ICompilationUnit unit, IProgressMonitor monitor) {
    if (monitor == null)
      monitor = new NullProgressMonitor();
    if (unit instanceof GroovyCompilationUnit) return new GroovyStatechartCodeAdder(unit, monitor);
      
    return new JavaStatechartCodeAdder(unit, monitor);
  }

  private static abstract class AbstractAdder implements StatechartCodeAdder {
    
    protected ICompilationUnit unit;
    protected IProgressMonitor monitor;
    
    public AbstractAdder(ICompilationUnit unit, IProgressMonitor monitor) {
      this.unit = unit;
      this.monitor = monitor;
    }
    
    public void run(String statechartName, String packageName, String className, String fqAgentName)
        throws Exception {
      // make sure that the agent name is the same the compilation unit.
      IType aType = unit.getTypes()[0];
      String agentType = unit.getParent().getElementName() + "." + aType.getElementName();
      if (agentType.equals(fqAgentName)) {
        createImport(packageName + "." + className);
        createImport("repast.simphony.ui.probe.ProbedProperty");
        String fieldName = className.substring(0, 1).toLowerCase() + className.subSequence(1, className.length());
        StringBuilder buf = new StringBuilder("\t");
        buf.append("@ProbedProperty(displayName=\"");
        buf.append(statechartName);
        buf.append("\")");
        buf.append(System.getProperty("line.separator"));
        buf.append("\t");
        buf.append(className);
        buf.append(" ");
        buf.append(fieldName);
        buf.append(" = ");
        buf.append(className);
        buf.append(".createStateChart(this, 0);");
        buf.append(System.getProperty("line.separator"));
        buf.append(System.getProperty("line.separator"));
        buf.append("public String get");
        buf.append(className);
        buf.append("State(){");
        buf.append(System.getProperty("line.separator"));
        buf.append("\t\t");
        buf.append("if (");
        buf.append(fieldName);
        buf.append(" == null) return \"\";");
        buf.append(System.getProperty("line.separator"));
        buf.append("\t\t");
        buf.append("Object result = ");
        buf.append(fieldName);
        buf.append(".getCurrentSimpleState();");
        buf.append(System.getProperty("line.separator"));
        buf.append("\t\t");
        buf.append("return result == null ? \"\" : result.toString();");
        buf.append(System.getProperty("line.separator"));
        buf.append("}");
        aType.createField(buf.toString(), null, true, monitor);
        unit.save(monitor, true);
      }
    }
    
    protected abstract void createImport(String importString) throws Exception;
  }
  
  private static class JavaStatechartCodeAdder extends AbstractAdder {

    public JavaStatechartCodeAdder(ICompilationUnit unit, IProgressMonitor monitor) {
      super(unit, monitor);
    }

    /* (non-Javadoc)
     * @see repast.simphony.statecharts.part.StatechartCodeAdderFactory.AbstractAdder#createImport(java.lang.String)
     */
    @Override
    protected void createImport(String importString) throws JavaModelException {
      unit.createImport(importString, null, monitor);
    }
  }
  
  private static class GroovyStatechartCodeAdder extends AbstractAdder {
   
    public GroovyStatechartCodeAdder(ICompilationUnit unit, IProgressMonitor monitor) {
      super(unit, monitor);
    }

    /* (non-Javadoc)
     * @see repast.simphony.statecharts.part.StatechartCodeAdderFactory.AbstractAdder#createImport(java.lang.String)
     */
    @Override
    protected void createImport(String importString) throws Exception {
	
      ImportRewrite rewriter = CodeStyleConfiguration.createImportRewrite(unit, true);
      rewriter.addImport(importString);
      unit.applyTextEdit(rewriter.rewriteImports(monitor), null);
    }
  }
}
