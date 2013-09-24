package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider;
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

/**
 * Listens for changes in the AnnotationModel and removes return value type errors,
 * if appropriate. The intention here is that users can provide the return value
 * without the return statement. For example, "0.5" to return a double. This
 * classes looks for errors on the last line the method that returns the value
 * and then compiles a new class with return prefixed to that line. If that
 * compiled class no longer contains the error, then the error annotation
 * is removed.
 *  
 * @author Nick Collier
 */
public class ErrorAnnotationHider implements IAnnotationModelListener,
    IAnnotationModelListenerExtension {

  // struct like class that holds data
  // about the "return line"
  class ErrorLineData {
    int offset, length, line;

    public ErrorLineData(IRegion region, int line) {
      this.offset = region.getOffset();
      this.length = region.getLength();
      this.line = line;
    }
  }

  /**
   * Determines whether or not the return line error has been
   * fixed by comparing compiler diagnostics with the error line.
   * If there is still an error on that line, then the problem
   * has not been fixed.
   * 
   * @author Nick Collier
   */
  class DL implements DiagnosticListener<JavaFileObject> {

    // intention is that if the diagnostic reports
    // an error at the line number then the issue
    // is not fixed.
    int lineNumber = 0;
    boolean fixed = true;

    public DL(int lineNumber) {
      this.lineNumber = lineNumber;
      fixed = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.tools.DiagnosticListener#report(javax.tools.Diagnostic)
     */
    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
      //System.out.printf("%d: %s, %s%n", diagnostic.getLineNumber(), diagnostic.getKind(), diagnostic.getMessage(null));
      if (diagnostic.getKind() == Kind.ERROR && diagnostic.getLineNumber() == lineNumber)
        fixed = false;
    }
  }

  private IDocument doc;
  private boolean running = false;
  private IJavaProject project;

  public ErrorAnnotationHider(IDocument doc, IProject project) {
    this.doc = doc;
    this.project = JavaCore.create(project);
  }

  private ErrorLineData findLastLineInMethod() {
    int bracketCount = 0;
    int lastLine = 0;
    ErrorLineData data = null;
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
        data = new ErrorLineData(doc.getLineInformation(lastLine), lastLine);
      // System.out.println(doc.get(data.getOffset(), data.getLength()));
    } catch (BadLocationException ex) {
      data = null;
    }

    return data;
  }

  @Override
  public void modelChanged(AnnotationModelEvent event) {

    if (running)
      return;

    List<Annotation> errors = new ArrayList<Annotation>();

    for (Annotation ann : event.getAddedAnnotations()) {
      if (JavaMarkerAnnotation.ERROR_ANNOTATION_TYPE.equals(ann.getType())) {
        errors.add(ann);
      }
    }

    Annotation targetError = null;

    if (errors.size() > 0) {
      // find end of method
      ErrorLineData data = findLastLineInMethod();
      if (data != null) {
        for (Annotation error : errors) {
          Position pos = event.getAnnotationModel().getPosition(error);
          if (pos.overlapsWith(data.offset, data.length)) {
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

        String name = ((CompilationUnitDocumentProvider.ProblemAnnotation) targetError)
            .getCompilationUnit().getElementName();

        String source = doc.get();
        source = source.substring(0, data.offset) + "return " + source.substring(data.offset);
        // compiler starts line numbers at 1
        DL dl = new DL(data.line + 1);
        Compiler.INSTANCE.getCompilerTask(project).compile(name.substring(0, name.lastIndexOf(".")), source, dl);
        if (dl.fixed) {
          running = true;
          //System.out.println("removed error");
          event.getAnnotationModel().removeAnnotation(targetError);
          running = false;
        }
      }
    }
  }

  @Override
  public final void modelChanged(IAnnotationModel model) {
  }
}
