/**
 *
 */
package repast.simphony.eclipse;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

/**
 * Opens a "Welcome to Repast Simphony" tab pointing at the online Repast site
 * the first time a given workspace is opened. This is a lightweight alternative
 * to the Eclipse intro/welcome framework (which is bound to the host product)
 * and simply opens an internal browser editor in the editor area.
 *
 * <p>
 * The tab is shown once per workspace: a flag is stored in this plugin's
 * (workspace-scoped) preference store after the tab is opened, so a freshly
 * created workspace shows it again while an existing one does not.
 *
 * @author Eric Tatara
 */
public class RepastWelcomeStartup implements IStartup {

  /** URL shown in the welcome tab. */
  public static final String WELCOME_URL = "https://repast.github.io/docs.html";

  /** Title shown on the welcome tab. */
  private static final String WELCOME_TITLE = "Welcome to Repast Simphony";

  /** Preference key recording that the welcome tab has been shown. */
  private static final String PREF_WELCOME_SHOWN = "repast.simphony.eclipse.welcomeShown";

  @Override
  public void earlyStartup() {
    final RepastSimphonyPlugin plugin = RepastSimphonyPlugin.getInstance();
    if (plugin == null) {
      return;
    }

    final IPreferenceStore store = plugin.getPreferenceStore();
    if (store.getBoolean(PREF_WELCOME_SHOWN)) {
      return;
    }

    final IWorkbench workbench = PlatformUI.getWorkbench();
    Display display = workbench.getDisplay();
    if (display == null || display.isDisposed()) {
      return;
    }

    display.asyncExec(new Runnable() {
      @Override
      public void run() {
        try {
          IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
          if (window == null) {
            return;
          }
          IWorkbenchPage page = window.getActivePage();
          if (page == null) {
            return;
          }

          // The host product's Welcome/intro opens maximized over the editor
          // area, so close it first or the welcome tab opens behind it. (The
          // ReLogo plugin's Startup does the same for its perspective.)
          IIntroManager introManager = workbench.getIntroManager();
          if (introManager != null) {
            IIntroPart intro = introManager.getIntro();
            if (intro != null) {
              introManager.closeIntro(intro);
            }
          }

          // Open as our own editor hosting an embedded SWT Browser so it always
          // renders inside Eclipse, regardless of the General > Web Browser
          // (internal vs external) preference.
          page.openEditor(new RepastWelcomeEditorInput(WELCOME_URL, WELCOME_TITLE), RepastWelcomeEditor.EDITOR_ID);

          // Only mark as shown once the tab has been opened successfully so a
          // failure is retried on the next start.
          store.setValue(PREF_WELCOME_SHOWN, true);
        } catch (Exception e) {
          plugin.log(e);
        }
      }
    });
  }
}
