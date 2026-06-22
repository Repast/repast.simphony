/**
 *
 */
package repast.simphony.eclipse;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * Editor input for {@link RepastWelcomeEditor}. Carries the URL to display in
 * the embedded browser.
 *
 * @author Eric Tatara
 */
public class RepastWelcomeEditorInput implements IEditorInput {

  private final String url;
  private final String name;

  public RepastWelcomeEditorInput(String url, String name) {
    this.url = url;
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public boolean exists() {
    return false;
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return ImageDescriptor.getMissingImageDescriptor();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public IPersistableElement getPersistable() {
    return null;
  }

  @Override
  public String getToolTipText() {
    return name;
  }

  @Override
  public <T> T getAdapter(Class<T> adapter) {
    return null;
  }

  @Override
  public int hashCode() {
    return url == null ? 0 : url.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof RepastWelcomeEditorInput)) {
      return false;
    }
    RepastWelcomeEditorInput other = (RepastWelcomeEditorInput) obj;
    return url == null ? other.url == null : url.equals(other.url);
  }
}
