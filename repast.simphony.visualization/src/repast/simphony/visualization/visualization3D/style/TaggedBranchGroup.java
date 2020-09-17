package repast.simphony.visualization.visualization3D.style;

import org.jogamp.java3d.BranchGroup;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class TaggedBranchGroup {

  private String tag;
  private BranchGroup branchGroup = new BranchGroup();

  public TaggedBranchGroup() {
    branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
  }

  public TaggedBranchGroup(String tag) {
    this();
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public BranchGroup getBranchGroup() {
    return branchGroup;
  }

  public void setBranchGroup(BranchGroup branchGroup) {
    this.branchGroup = branchGroup;
  }
}
