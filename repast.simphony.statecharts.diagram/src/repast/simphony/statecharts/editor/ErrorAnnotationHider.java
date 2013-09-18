package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitDocumentProvider;
import org.eclipse.jdt.internal.ui.javaeditor.JavaMarkerAnnotation;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

public class ErrorAnnotationHider implements IAnnotationModelListener,
    IAnnotationModelListenerExtension {

  private IDocument doc;

  public ErrorAnnotationHider(IDocument doc) {
    this.doc = doc;
  }

  @Override
  public void modelChanged(AnnotationModelEvent event) {
    List<Annotation> errors = new ArrayList<Annotation>();
    IMethod method = null;
    for (Annotation ann : event.getAddedAnnotations()) {
      if (JavaMarkerAnnotation.ERROR_ANNOTATION_TYPE.equals(ann.getType())) {
        errors.add(ann);
        if (method == null) {
          try {
            method = ((CompilationUnitDocumentProvider.ProblemAnnotation)ann).getCompilationUnit().getAllTypes()[0].getMethods()[0];
          } catch (JavaModelException e) {
            e.printStackTrace();
          }
        }
      }
    }

   
    if (method != null) {
      try {
        String[] lines = method.getSource().split("\\n");
        
        ISourceRange range = method.getSourceRange();
        int lastOffset = range.getOffset() + range.getLength();
        for (int i = lines.length - 1, j = 2; i >= 0; i--, j++) {
          System.out.println(lastOffset - (lines[i].length() + j) + ":" + lines[i]);
        }
      } catch (JavaModelException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      for (Annotation error : errors) {
        Position pos = event.getAnnotationModel().getPosition(error);
        System.out.println(pos);
        
        // CompilationUnitDocumentProvider.ProblemAnnotation problem =
        // (CompilationUnitDocumentProvider.ProblemAnnotation)error;
        // try {
        // IMethod method =
        // problem.getCompilationUnit().getAllTypes()[0].getMethods()[0];
        // method.
        // } catch (JavaModelException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
      }
    }
  }

  @Override
  public final void modelChanged(IAnnotationModel model) {
  }
}
