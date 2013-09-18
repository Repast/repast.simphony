/**
 * 
 */
package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.groovy.core.util.ReflectionUtils;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.ITextPresentationListener;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerExtension;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;

/**
 * Source code viewer for Java used in the OnTransition etc. code editors.
 * 
 * @author Nick Collier
 */
public class GroovySourceViewer extends SourceViewer implements StatechartSourceViewer {

  /** Preference key for automatically closing strings */
  private final static String CLOSE_STRINGS = PreferenceConstants.EDITOR_CLOSE_STRINGS;
  /** Preference key for automatically closing brackets and parenthesis */
  private final static String CLOSE_BRACKETS = PreferenceConstants.EDITOR_CLOSE_BRACKETS;

  /** Preference key for automatically closing curly braces */
  private final static String CLOSE_BRACES = PreferenceConstants.EDITOR_CLOSE_BRACES;

  private final GroovyBracketInserter groovyBracketInserter;

  public GroovySourceViewer(Composite parent, IVerticalRuler ruler, IOverviewRuler oRuler) {
    super(parent, ruler, oRuler, true, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
        | SWT.FULL_SELECTION);

    groovyBracketInserter = new GroovyBracketInserter(this);
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
   * From GroovyEditor.
   * 
   * Ensure that the Java breakpoint updater is removed because we need to use
   * Groovy's breakpoint updater instead
   */
  @SuppressWarnings("unchecked")
  private void unsetJavaBreakpointUpdater() {
    IAnnotationModel model = this.getAnnotationModel();
    if (model instanceof AbstractMarkerAnnotationModel) {
      // force instantiation of the extension points
      ReflectionUtils.executePrivateMethod(AbstractMarkerAnnotationModel.class,
          "installMarkerUpdaters", new Class<?>[0], model, new Object[0]);
      // remove the marker updater for Java breakpoints, the groovy one will be
      // used instead
      List<IConfigurationElement> updaterSpecs = (List<IConfigurationElement>) ReflectionUtils
          .getPrivateField(AbstractMarkerAnnotationModel.class, "fMarkerUpdaterSpecifications",
              model);
      for (Iterator<IConfigurationElement> specIter = updaterSpecs.iterator(); specIter.hasNext();) {
        IConfigurationElement spec = specIter.next();
        if (spec.getAttribute("class").equals(
            org.eclipse.jdt.internal.debug.ui.BreakpointMarkerUpdater.class.getCanonicalName())) {
          specIter.remove();
          break;
        }
      }
    }

  }

  /**
   * Configures this viewer with a default JavaSourceViewerConfiguration.
   */
  public void configure(IPreferenceStore preferenceStore, StatechartGroovyEditor editor) {
    super.configure(editor.createJavaSourceViewerConfiguration());

    // from GroovyEditor.createPartControl(Composite)
    unsetJavaBreakpointUpdater();

    boolean closeBrackets = preferenceStore.getBoolean(CLOSE_BRACKETS);
    boolean closeStrings = preferenceStore.getBoolean(CLOSE_STRINGS);
    boolean closeBraces = preferenceStore.getBoolean(CLOSE_BRACES);
    boolean closeAngularBrackets = JavaCore.VERSION_1_5.compareTo(preferenceStore
        .getString(JavaCore.COMPILER_SOURCE)) <= 0;

    groovyBracketInserter.setCloseBracketsEnabled(closeBrackets);
    groovyBracketInserter.setCloseStringsEnabled(closeStrings);
    groovyBracketInserter.setCloseAngularBracketsEnabled(closeAngularBrackets);
    groovyBracketInserter.setCloseBracesEnabled(closeBraces);

    if (this instanceof ITextViewerExtension)
      ((ITextViewerExtension) this).prependVerifyKeyListener(groovyBracketInserter);

    configureFont(preferenceStore);
  }
  
  public void unconfigure() {
    super.unconfigure();
    removeVerifyKeyListener(groovyBracketInserter);
  }

  /**
   * Prepends the text presentation listener at the beginning of the viewer's
   * list of text presentation listeners. If the listener is already registered
   * with the viewer this call moves the listener to the beginning of the list.
   * 
   * @param listener
   *          the text presentation listener
   * @since 3.0
   */
  // from eclipse's JavaSourceViewer -- required to make the
  // GroovySemanticHighlighter work
  public void prependTextPresentationListener(ITextPresentationListener listener) {

    Assert.isNotNull(listener);

    if (fTextPresentationListeners == null)
      fTextPresentationListeners = new ArrayList<ITextPresentationListener>();

    fTextPresentationListeners.remove(listener);
    fTextPresentationListeners.add(0, listener);
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
}