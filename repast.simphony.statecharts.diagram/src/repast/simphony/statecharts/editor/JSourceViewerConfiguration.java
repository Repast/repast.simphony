/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.ContentAssistPreference;
import org.eclipse.jdt.internal.ui.text.JavaCompositeReconcilingStrategy;
import org.eclipse.jdt.internal.ui.text.JavaReconciler;
import org.eclipse.jdt.internal.ui.text.java.ContentAssistProcessor;
import org.eclipse.jdt.internal.ui.text.javadoc.JavadocCompletionProcessor;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author Nick Collier
 */
public class JSourceViewerConfiguration extends JavaSourceViewerConfiguration {

  public JSourceViewerConfiguration(IColorManager colorManager, IPreferenceStore preferenceStore,
      CodePropertyEditor editor) {
    super(colorManager, preferenceStore, editor, null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration#getReconciler(org
   * .eclipse.jface.text.source.ISourceViewer)
   */
  @Override
  public IReconciler getReconciler(ISourceViewer sourceViewer) {
    final ITextEditor editor = getEditor();
    if (editor != null && editor.isEditable()) {

      JavaCompositeReconcilingStrategy strategy = new JavaCompositeReconcilingStrategy(
          sourceViewer, editor, getConfiguredDocumentPartitioning(sourceViewer));
      JavaReconciler reconciler = new JavaReconciler(editor, strategy, false);
      reconciler.setIsAllowedToModifyDocument(false);
      reconciler.setDelay(500);

      return reconciler;
    }
    return null;
  }

  /*
   * @see SourceViewerConfiguration#getContentAssistant(ISourceViewer)
   */
  @Override
  public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

    // copied from JavaSourceViewerConfiguration, substituting JCompletionProcessor for
    // original JavaCompletionProcessor
    if (getEditor() != null) {
      ContentAssistant assistant = new ContentAssistant();
      assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

      assistant.setRestoreCompletionProposalSize(getSettings("completion_proposal_size")); //$NON-NLS-1$

      IContentAssistProcessor javaProcessor = new JCompletionProcessor(getEditor(), assistant,
          IDocument.DEFAULT_CONTENT_TYPE);
      assistant.setContentAssistProcessor(javaProcessor, IDocument.DEFAULT_CONTENT_TYPE);

      ContentAssistProcessor singleLineProcessor = new JCompletionProcessor(getEditor(),
          assistant, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);
      assistant.setContentAssistProcessor(singleLineProcessor,
          IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);

      ContentAssistProcessor stringProcessor = new JCompletionProcessor(getEditor(), assistant,
          IJavaPartitions.JAVA_STRING);
      assistant.setContentAssistProcessor(stringProcessor, IJavaPartitions.JAVA_STRING);

      ContentAssistProcessor multiLineProcessor = new JCompletionProcessor(getEditor(),
          assistant, IJavaPartitions.JAVA_MULTI_LINE_COMMENT);
      assistant.setContentAssistProcessor(multiLineProcessor,
          IJavaPartitions.JAVA_MULTI_LINE_COMMENT);

      ContentAssistProcessor javadocProcessor = new JavadocCompletionProcessor(getEditor(),
          assistant);
      assistant.setContentAssistProcessor(javadocProcessor, IJavaPartitions.JAVA_DOC);

      ContentAssistPreference.configure(assistant, fPreferenceStore);

      assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
      assistant.setInformationControlCreator(new IInformationControlCreator() {
        public IInformationControl createInformationControl(Shell parent) {
          return new DefaultInformationControl(parent, JavaPlugin
              .getAdditionalInfoAffordanceString());
        }
      });

      return assistant;
    }

    return null;
  }
  
  private IDialogSettings getSettings(String sectionName) {
    IDialogSettings settings= JavaPlugin.getDefault().getDialogSettings().getSection(sectionName);
    if (settings == null)
            settings= JavaPlugin.getDefault().getDialogSettings().addNewSection(sectionName);

    return settings;
}
}
