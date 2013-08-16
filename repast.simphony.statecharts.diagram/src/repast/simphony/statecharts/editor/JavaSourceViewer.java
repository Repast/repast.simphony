/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerExtension;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.VerticalRuler;
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
@SuppressWarnings("restriction")
public class JavaSourceViewer extends SourceViewer {

  /** Preference key for automatically closing strings */
  private final static String CLOSE_STRINGS = PreferenceConstants.EDITOR_CLOSE_STRINGS;
  /** Preference key for automatically closing brackets and parenthesis */
  private final static String CLOSE_BRACKETS = PreferenceConstants.EDITOR_CLOSE_BRACKETS;

  private final BracketInserter bracketInserter;

  public JavaSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
    super(parent, ruler, styles);
    bracketInserter = new BracketInserter(this);
  }

  public JavaSourceViewer(Composite parent) {
    this(parent, new VerticalRuler(12), SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
        | SWT.FULL_SELECTION);
  }

  private void initFont(Font font) {
    StyledText styledText = getTextWidget();
    styledText.setFont(font);

    if (getVerticalRuler() instanceof IVerticalRulerExtension) {
      IVerticalRulerExtension e = (IVerticalRulerExtension) getVerticalRuler();
      e.setFont(font);
    }
  }

  public void configure(SourceViewerConfiguration config) {
    super.configure(config);

    IPreferenceStore prefStore = JavaPlugin.getDefault().getCombinedPreferenceStore();
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

    //configureFont(prefStore);

  }

  /**
   * Configures this viewer with a default JavaSourceViewerConfiguration.
   */
  public void configure(CodePropertyEditor editor) {
    JavaTextTools textTools = JavaPlugin.getDefault().getJavaTextTools();
    IPreferenceStore prefStore = JavaPlugin.getDefault().getCombinedPreferenceStore();
    JSourceViewerConfiguration config = new JSourceViewerConfiguration(textTools.getColorManager(),
        prefStore, editor);
    super.configure(config);

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
}