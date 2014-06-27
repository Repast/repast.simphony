/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.JavaCompositeReconcilingStrategy;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerExtension;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;

/**
 * Source code viewer for Java used in the OnTransition etc. code editors.
 * 
 * @author Nick Collier
 */
public class JavaSourceViewer extends SourceViewer implements StatechartSourceViewer {

  /** Preference key for automatically closing strings */
  private final static String CLOSE_STRINGS = PreferenceConstants.EDITOR_CLOSE_STRINGS;
  /** Preference key for automatically closing brackets and parenthesis */
  private final static String CLOSE_BRACKETS = PreferenceConstants.EDITOR_CLOSE_BRACKETS;

  private final BracketInserter bracketInserter;

  public JavaSourceViewer(Composite parent, IVerticalRuler ruler, IOverviewRuler oRuler) {
    super(parent, ruler, oRuler, true, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
        | SWT.FULL_SELECTION);
    bracketInserter = new BracketInserter(this);
  }

  private void initFont(Font font) {
    StyledText styledText = getTextWidget();
    styledText.setFont(font);

    if (getVerticalRuler() instanceof IVerticalRulerExtension) {
      IVerticalRulerExtension e = (IVerticalRulerExtension) getVerticalRuler();
      e.setFont(font);
    }
  }

  /**
   * Configures this viewer with a default JavaSourceViewerConfiguration.
   */
  public void configure(IPreferenceStore prefStore, StatechartJavaEditor editor) {
    JavaTextTools textTools = JavaPlugin.getDefault().getJavaTextTools();
    JavaSourceViewerConfiguration config = new JavaSourceViewerConfiguration(textTools.getColorManager(),
        prefStore, editor, null);
    super.configure(config);
    
    // uninstall the quick assist as this doesn't
    // work correctly when showing editor in property sheet
    // options shown on the resulting window will open the editor
    // for the template file
    fQuickAssistAssistant.uninstall();
    fQuickAssistAssistantInstalled = false;

    // from CompilationUnitEditor
    boolean closeBrackets = prefStore.getBoolean(CLOSE_BRACKETS);
    boolean closeStrings = prefStore.getBoolean(CLOSE_STRINGS);
    boolean closeAngularBrackets = JavaCore.VERSION_1_5.compareTo(prefStore
        .getString(JavaCore.COMPILER_SOURCE)) <= 0;

    bracketInserter.setCloseBracketsEnabled(closeBrackets);
    bracketInserter.setCloseStringsEnabled(closeStrings);
    bracketInserter.setCloseAngularBracketsEnabled(closeAngularBrackets);

    if (this instanceof ITextViewerExtension)
      ((ITextViewerExtension) this).prependVerifyKeyListener(bracketInserter);
    // end from CompilationUnitEditor

    configureFont(prefStore);
  }
  
  public void unconfigure() {
    super.unconfigure();
    removeVerifyKeyListener(bracketInserter);
  }

  private void configureFont(IPreferenceStore prefStore) {

    Font font = null;
    if (prefStore.contains(JFaceResources.TEXT_FONT)
        && !prefStore.isDefault(JFaceResources.TEXT_FONT)) {
      FontData data = PreferenceConverter.getFontData(prefStore, JFaceResources.TEXT_FONT);

      if (data != null) {

        font = new Font(getTextWidget().getDisplay(), data);
      }
    }

    if (font == null)
      font = JFaceResources.getTextFont();

    if (!font.equals(getTextWidget().getFont())) {
      initFont(font);

    } else {
      font.dispose();
    }
  }

  /**
   * Ignores auto-indenting when set to true.
   * 
   * @param ignore
   */
  public void ignoreAutoIndent(boolean ignore) {
    super.ignoreAutoEditStrategies(ignore);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.text.TextViewer#getTextHover(int, int)
   */
  @Override
  protected ITextHover getTextHover(int offset, int stateMask) {
    // turns off the error hovering
    return null;
  }
  
  
}
