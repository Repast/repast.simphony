package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
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

public class ErrorAnnotationHider implements IAnnotationModelListener,
    IAnnotationModelListenerExtension {

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

  static class Owner extends WorkingCopyOwner {

    PR pr;

    public Owner(PR pr) {
      this.pr = pr;
    }

    @Override
    public IProblemRequestor getProblemRequestor(ICompilationUnit workingCopy) {
      return pr;
    }
  }

  private IDocument doc;
  private int count = 0;

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
    if (count > 0) {
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
        try {
          System.out.println("Error in last line: " + doc.get(data.getOffset(), data.getLength()));
          ICompilationUnit unit = ((CompilationUnitDocumentProvider.ProblemAnnotation) targetError)
              .getCompilationUnit();

          unit = unit.getWorkingCopy(owner, null);
          IBuffer buffer = unit.getBuffer();
          count = 3;
          buffer.replace(data.getOffset(), 0, "return ");
          unit.reconcile(ICompilationUnit.NO_AST, false, owner, null);
          if (pr.count < errors.size()) {
            event.getAnnotationModel().removeAnnotation(targetError);
          }
        } catch (BadLocationException | JavaModelException ex) {
          ex.printStackTrace();
        }
      }
    }

  }

  @Override
  public final void modelChanged(IAnnotationModel model) {
  }
}
