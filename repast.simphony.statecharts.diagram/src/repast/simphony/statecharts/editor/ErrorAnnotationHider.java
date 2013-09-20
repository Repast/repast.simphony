package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IBufferChangedListener;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaMarkerAnnotation;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

public class ErrorAnnotationHider implements IAnnotationModelListener,
    IAnnotationModelListenerExtension {
  
  static class Buffer implements IBuffer {
    
    String contents;
    ICompilationUnit owner;
    
    public Buffer(ICompilationUnit owner, String contents) {
        this.contents = contents;
        this.owner = owner;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#addBufferChangedListener(org.eclipse.jdt.core.IBufferChangedListener)
     */
    @Override
    public void addBufferChangedListener(IBufferChangedListener listener) {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#append(char[])
     */
    @Override
    public void append(char[] text) {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#append(java.lang.String)
     */
    @Override
    public void append(String text) {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#close()
     */
    @Override
    public void close() {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getChar(int)
     */
    @Override
    public char getChar(int position) {
      // TODO Auto-generated method stub
      return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getCharacters()
     */
    @Override
    public char[] getCharacters() {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getContents()
     */
    @Override
    public String getContents() {
      return contents;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getLength()
     */
    @Override
    public int getLength() {
      // TODO Auto-generated method stub
      return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getOwner()
     */
    @Override
    public IOpenable getOwner() {
      return owner;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getText(int, int)
     */
    @Override
    public String getText(int offset, int length) throws IndexOutOfBoundsException {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#getUnderlyingResource()
     */
    @Override
    public IResource getUnderlyingResource() {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#hasUnsavedChanges()
     */
    @Override
    public boolean hasUnsavedChanges() {
      // TODO Auto-generated method stub
      return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#isClosed()
     */
    @Override
    public boolean isClosed() {
      // TODO Auto-generated method stub
      return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
      // TODO Auto-generated method stub
      return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#removeBufferChangedListener(org.eclipse.jdt.core.IBufferChangedListener)
     */
    @Override
    public void removeBufferChangedListener(IBufferChangedListener listener) {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#replace(int, int, char[])
     */
    @Override
    public void replace(int position, int length, char[] text) {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#replace(int, int, java.lang.String)
     */
    @Override
    public void replace(int position, int length, String text) {
      contents = contents.substring(0, position) + text + contents.substring(position);
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#save(org.eclipse.core.runtime.IProgressMonitor, boolean)
     */
    @Override
    public void save(IProgressMonitor progress, boolean force) throws JavaModelException {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#setContents(char[])
     */
    @Override
    public void setContents(char[] contents) {
      // TODO Auto-generated method stub
      
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IBuffer#setContents(java.lang.String)
     */
    @Override
    public void setContents(String contents) {
      // TODO Auto-generated method stub
      
    }
    
  }

  static class PR implements IProblemRequestor {

    int count = 0;

    @Override
    public void acceptProblem(IProblem problem) {
      if (problem.isError())
        ++count;
    }

    @Override
    public void beginReporting() {
      count = 0;
    }

    @Override
    public void endReporting() {
    }

    @Override
    public boolean isActive() {
      return true;
    }
  }

  class Owner extends WorkingCopyOwner {

    PR pr;

    public Owner(PR pr) {
      this.pr = pr;
    }

    @Override
    public IProblemRequestor getProblemRequestor(ICompilationUnit workingCopy) {
      return pr;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.WorkingCopyOwner#createBuffer(org.eclipse.jdt.core.ICompilationUnit)
     */
    @Override
    public IBuffer createBuffer(ICompilationUnit workingCopy) {
      return new Buffer(workingCopy, doc.get());
    }
  }

  private IDocument doc;
  private int count = 0;
  private boolean running = false;

  public ErrorAnnotationHider(IDocument doc) {
    this.doc = doc;
  }

  private IRegion findLastLineInMethod() {
    int bracketCount = 0;
    int lastLine = 0;
    IRegion data = null;
    try {
      for (int i = doc.getNumberOfLines() - 1; i >= 0; i--) {
        int offset = doc.getLineOffset(i);
        // includes the delimiter
        int length = doc.getLineLength(i);
        String text = doc.get(offset, length).trim();
        if (text.startsWith(("public"))) {
          lastLine = -1;
          break;
        }
        if (bracketCount == 2) {
          // if in here then inside method looking for any text
          if (text.length() > 0) {
            lastLine = i;
            break;
          }

        } else if (text.endsWith("}")) {
          bracketCount++;
          if (bracketCount == 2 && text.length() > 1) {
            lastLine = i;
            break;
          }
        }
      }
      if (lastLine != -1)
        data = doc.getLineInformation(lastLine);
      // System.out.println(doc.get(data.getOffset(), data.getLength()));
    } catch (BadLocationException ex) {
      data = null;
    }

    return data;
  }

  @Override
  public void modelChanged(AnnotationModelEvent event) {
    
    if (running) return;
    if (count > 0) {
      //System.out.println("\n\n");
      //new RuntimeException().printStackTrace();
      --count;
      return;
    }

    List<Annotation> errors = new ArrayList<Annotation>();

    for (Annotation ann : event.getAddedAnnotations()) {
      if (JavaMarkerAnnotation.ERROR_ANNOTATION_TYPE.equals(ann.getType())) {
        errors.add(ann);
      }
    }

    Annotation targetError = null;

    if (errors.size() > 0) {
      // find end of method
      IRegion data = findLastLineInMethod();
      if (data != null) {
        for (Annotation error : errors) {
          Position pos = event.getAnnotationModel().getPosition(error);
          if (pos.overlapsWith(data.getOffset(), data.getLength())) {
            if (targetError == null) {
              targetError = error;
            } else {
              targetError = null;
              break;
            }
          }
        }
      }

      if (targetError != null) {
        PR pr = new PR();
        Owner owner = new Owner(pr);
        //ICompilationUnit unit = null;
        //try {
          ASTParser parser = ASTParser.newParser(AST.JLS4);
          String source = doc.get();
          source = source.substring(0, data.getOffset()) + "return " + source.substring(data.getOffset());
          System.out.println(source);
          
          parser.setSource(source.toCharArray());
          CompilationUnit result = (CompilationUnit)parser.createAST(null);
          for (IProblem problem : result.getProblems()) {
            System.out.println(problem);
          }
          
          System.out.println("errors size: " + errors.size());
          if (result.getProblems().length < errors.size()) {
          running = true;
          System.out.println("removed error");
          event.getAnnotationModel().removeAnnotation(targetError);
          running = false;
        }
          
//          System.out.println("Error in last line: " + doc.get(data.getOffset(), data.getLength()));
//          unit = ((CompilationUnitDocumentProvider.ProblemAnnotation) targetError)
//              .getCompilationUnit();
//
//          unit = unit.getWorkingCopy(owner, null);
//          
//          IBuffer buffer = unit.getBuffer();
//          count = 1;
//         buffer.replace(data.getOffset(), 0, "return ");
//          unit.reconcile(ICompilationUnit.NO_AST, false, owner, null);
//          System.out.println("pr count: " + pr.count);
//          if (pr.count < errors.size()) {
//            running = true;
//            System.out.println("removed error");
//            event.getAnnotationModel().removeAnnotation(targetError);
//            running = false;
//          }
        //} catch (BadLocationException | JavaModelException ex) {
        //  ex.printStackTrace();
        //} finally {
        //  try {
            //unit.discardWorkingCopy();
         // } catch (JavaModelException ex) {}
        }
      }
    

  }

  @Override
  public final void modelChanged(IAnnotationModel model) {
  }
}
