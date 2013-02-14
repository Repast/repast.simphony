/**
 * 
 */
package repast.simphony.statecharts.part;

import greclipse.org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import greclipse.org.eclipse.jdt.ui.CodeStyleConfiguration;

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
        StringBuilder buf = new StringBuilder("\t");
        buf.append("@ProbedProperty(usageName=\"");
        buf.append(statechartName);
        buf.append("\", displayName=\"");
        buf.append(statechartName);
        buf.append("\")");
        buf.append(System.getProperty("line.separator"));
        buf.append("\t");
        buf.append(className);
        buf.append(" ");
        buf.append(className.substring(0, 1).toLowerCase());
        buf.append(className.subSequence(1, className.length()));
        buf.append(" = ");
        buf.append(className);
        buf.append(".createStateChart(this, 1);");
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
