/**
 * 
 */
package repast.simphony.statecharts.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaModelMarker;

import repast.simphony.statecharts.generator.CodeGeneratorConstants;
import repast.simphony.statecharts.generator.GeneratorUtil;

/**
 * Visit resource deltas looking for marker changes that flag bad code. The
 * elements produced the bad code are then tagged as such in the Validator.
 * 
 * @author Nick Collier
 */
public class BadCodeFinder implements IResourceDeltaVisitor {

  private static final String[] ACTION_NAMES = { "OnEnterAction", "OnExitAction", "Guard",
      "OnTransition", "TriggerDoubleFunction", "ConditionTriggerCondition", "MessageCondition",
      "MessageEquals" };

  private boolean foundBadCode = false;

  private boolean isCheckedClass(String path) {
    for (String name : ACTION_NAMES) {
      if (path.contains(name))
        return true;
    }
    return false;
  }

  /**
   * Resets for next run.
   */
  public void reset() {
    foundBadCode = false;
  }

  /**
   * Gets whether or not this found bad code during its last visit.
   * 
   * @return true if bad code was found otherwise false.
   */
  public boolean foundBadCode() {
    return foundBadCode;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core
   * .resources.IResourceDelta)
   */
  @Override
  public boolean visit(IResourceDelta delta) throws CoreException {
    IPath path = delta.getFullPath();
    if (path != null) {

      if (path.segmentCount() < 2)
        return true;
      else {
        String sPath = path.toPortableString();

        if (sPath.contains(CodeGeneratorConstants.SRC_GEN)) {

          String ext = path.getFileExtension();
          if (ext != null && ext.equals("java") && isCheckedClass(sPath)) {
            for (IMarkerDelta mDelta : delta.getMarkerDeltas()) {
              int kind = mDelta.getKind();
              if (kind == IResourceDelta.ADDED || kind == IResourceDelta.CHANGED) {
                IMarker marker = mDelta.getMarker();
                if (marker.getType().equals(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER)
                    && marker.getAttribute(IMarker.SEVERITY).equals(IMarker.SEVERITY_ERROR)) {
                  String uuid = GeneratorUtil.getUUIDForTypeName(path.removeFileExtension()
                      .lastSegment());
                  if (uuid != null) {
                    foundBadCode = true;
                    Validator.addBadCodeUUID(uuid);
                  }

                }

              } else {
                String uuid = GeneratorUtil.getUUIDForTypeName(path.removeFileExtension()
                    .lastSegment());
                if (uuid != null) {
                  foundBadCode = true;
                  Validator.removeBadCodeUUID(uuid);
                }

              }
            }
          }
        }
        return true;
      }
    }

    return false;
  }
}