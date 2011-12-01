package repast.simphony.visualization.visualization3D.style;

import javax.media.j3d.Appearance;

import repast.simphony.visualization.visualization3D.AppearanceFactory;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class TaggedAppearance {
  
  private String tag;
	private Appearance appearance = AppearanceFactory.createAppearance();

	public TaggedAppearance() {}

	public TaggedAppearance(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Appearance getAppearance() {
		return appearance;
	}
}
