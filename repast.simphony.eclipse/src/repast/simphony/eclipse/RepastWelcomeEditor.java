/**
 *
 */
package repast.simphony.eclipse;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

/**
 * A simple editor that hosts an embedded SWT {@link Browser} widget, used to
 * show the Repast Simphony welcome page as a tab inside Eclipse. Unlike the
 * workbench browser support (which honors the General &gt; Web Browser
 * preference and may open an external window), this always renders inside the
 * Eclipse editor area.
 *
 * <p>
 * A small navigation toolbar (back, forward, reload, home, address field, and
 * open-in-external-browser) is shown above the embedded browser.
 *
 * @author Eric Tatara
 */
public class RepastWelcomeEditor extends EditorPart {

  public static final String EDITOR_ID = "repast.simphony.eclipse.welcomeEditor";

  private Browser browser;
  private Text locationText;
  private ToolItem backItem;
  private ToolItem forwardItem;
  private String homeUrl;

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
    setSite(site);
    setInput(input);
    if (input instanceof RepastWelcomeEditorInput) {
      homeUrl = ((RepastWelcomeEditorInput) input).getUrl();
    }
    setPartName(input.getName());
  }

  @Override
  public void createPartControl(Composite parent) {
    Composite root = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout(1, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.verticalSpacing = 0;
    root.setLayout(layout);

    createNavBar(root);

    browser = new Browser(root, SWT.NONE);
    GridData browserData = new GridData(SWT.FILL, SWT.FILL, true, true);
    browser.setLayoutData(browserData);

    // Keep the address field and nav buttons in sync with the page.
    browser.addLocationListener(new LocationAdapter() {
      @Override
      public void changed(LocationEvent event) {
        if (event.top) {
          locationText.setText(event.location == null ? "" : event.location);
          updateNavState();
        }
      }
    });

    if (homeUrl != null) {
      browser.setUrl(homeUrl);
    }
  }

  private void createNavBar(Composite root) {
    Composite navBar = new Composite(root, SWT.NONE);
    navBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout navLayout = new GridLayout(3, false);
    navLayout.marginHeight = 2;
    navBar.setLayout(navLayout);

    ToolBar toolBar = new ToolBar(navBar, SWT.FLAT);
    toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    ISharedImages images = PlatformUI.getWorkbench().getSharedImages();

    backItem = new ToolItem(toolBar, SWT.PUSH);
    backItem.setToolTipText("Back");
    backItem.setImage(images.getImage(ISharedImages.IMG_TOOL_BACK));
    backItem.setDisabledImage(images.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
    backItem.setEnabled(false);
    backItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        browser.back();
      }
    });

    forwardItem = new ToolItem(toolBar, SWT.PUSH);
    forwardItem.setToolTipText("Forward");
    forwardItem.setImage(images.getImage(ISharedImages.IMG_TOOL_FORWARD));
    forwardItem.setDisabledImage(images.getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
    forwardItem.setEnabled(false);
    forwardItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        browser.forward();
      }
    });

    new ToolItem(toolBar, SWT.SEPARATOR);

    ToolItem reloadItem = new ToolItem(toolBar, SWT.PUSH);
    reloadItem.setText("Reload");
    reloadItem.setToolTipText("Reload the current page");
    reloadItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        browser.refresh();
      }
    });

    ToolItem homeItem = new ToolItem(toolBar, SWT.PUSH);
    homeItem.setText("Home");
    homeItem.setToolTipText("Return to the Repast Simphony home page");
    homeItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        if (homeUrl != null) {
          browser.setUrl(homeUrl);
        }
      }
    });

    // Address field grabs the remaining horizontal space in the nav bar.
    locationText = new Text(navBar, SWT.SINGLE | SWT.BORDER);
    locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    locationText.addListener(SWT.DefaultSelection, event -> navigateTo(locationText.getText()));

    // External-browser button on its own short toolbar so it sits at the far
    // right edge of the row.
    ToolBar externalBar = new ToolBar(navBar, SWT.FLAT);
    externalBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    ToolItem externalItem = new ToolItem(externalBar, SWT.PUSH);
    externalItem.setText("Open in Browser");
    externalItem.setToolTipText("Open the current page in your external web browser");
    externalItem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        String url = browser.getUrl();
        if (url != null && url.length() > 0) {
          Program.launch(url);
        }
      }
    });
  }

  private void navigateTo(String url) {
    if (url == null) {
      return;
    }
    url = url.trim();
    if (url.isEmpty()) {
      return;
    }
    if (!url.matches("(?i)^[a-z][a-z0-9+.-]*://.*")) {
      url = "https://" + url;
    }
    browser.setUrl(url);
  }

  private void updateNavState() {
    if (backItem != null && !backItem.isDisposed()) {
      backItem.setEnabled(browser.isBackEnabled());
    }
    if (forwardItem != null && !forwardItem.isDisposed()) {
      forwardItem.setEnabled(browser.isForwardEnabled());
    }
  }

  @Override
  public void setFocus() {
    if (browser != null && !browser.isDisposed()) {
      browser.setFocus();
    }
  }

  @Override
  public void doSave(IProgressMonitor monitor) {
    // read-only; nothing to save
  }

  @Override
  public void doSaveAs() {
    // read-only; nothing to save
  }

  @Override
  public boolean isDirty() {
    return false;
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }
}
