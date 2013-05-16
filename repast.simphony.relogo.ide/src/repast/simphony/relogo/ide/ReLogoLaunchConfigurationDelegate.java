package repast.simphony.relogo.ide;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.core.DebugCoreMessages;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

/**
 * This is not used anymore after move to ReLogo2.
 * @author jozik
 *
 */
public class ReLogoLaunchConfigurationDelegate extends
		JavaLaunchDelegate {

	public ReLogoLaunchConfigurationDelegate() {
		super();
	}

	/**
	 * Performs an incremental build on each of the given projects.
	 * 
	 * @param projects projects to build
	 * @param monitor progress monitor
	 * @throws CoreException if an exception occurs while building
	 */
	protected void buildProjects(final IProject[] projects, IProgressMonitor monitor) throws CoreException {
		IWorkspaceRunnable build = new IWorkspaceRunnable(){
			public void run(IProgressMonitor pm) throws CoreException {
				SubMonitor localmonitor = SubMonitor.convert(pm, DebugCoreMessages.LaunchConfigurationDelegate_scoped_incremental_build, projects.length);
				try {
					for (int i = 0; i < projects.length; i++ ) {
						if (localmonitor.isCanceled()) {
							throw new OperationCanceledException();
						}
						projects[i].build(IncrementalProjectBuilder.CLEAN_BUILD, localmonitor.newChild(1));
						projects[i].build(IncrementalProjectBuilder.INCREMENTAL_BUILD, localmonitor.newChild(1));
					}
				} finally {
					localmonitor.done();
				}
			}
		};
		ResourcesPlugin.getWorkspace().run(build, monitor);
	}

}
