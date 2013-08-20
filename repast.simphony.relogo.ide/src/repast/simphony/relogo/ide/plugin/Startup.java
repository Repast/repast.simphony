package repast.simphony.relogo.ide.plugin;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

import repast.simphony.relogo.ide.ReLogoFilter;

public class Startup implements IStartup {

	private static final String RELOGO_PERSPECTIVE_ID = "repast.simphony.relogo.ide.relogoperspective";

	@Override
	public void earlyStartup() {
		// System.out.println("Hello from Startup");
		// System.out.println("The groovy version is: " +
		// groovy.lang.GroovySystem.getVersion());
		//
		final Runnable packageViewerRefresh = new Runnable() {

			@Override
			public void run() {
				IWorkbench wb = PlatformUI.getWorkbench();
				if (wb != null) {
					IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
					if (ww != null) {
						IWorkbenchPage wp = ww.getActivePage();
						if (wp != null) {
							IViewPart vp = wp.findView(JavaUI.ID_PACKAGES);
							if (vp != null && vp instanceof IPackagesViewPart) {
								IPackagesViewPart pvp = (IPackagesViewPart) vp;
								TreeViewer tv = pvp.getTreeViewer();
								if (tv != null) {
									tv.refresh();
								}
							}
						}
					}
				}
			}
		};
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// register the ReLogoResourceChangeListener
		workspace.addResourceChangeListener(new ReLogoResourceChangeListener());

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				
				
				
				final IPerspectiveListener reLogoFilterToggle = new PerspectiveAdapter() {

					@Override
					public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {

						if (perspective.getId().equals(RELOGO_PERSPECTIVE_ID)) {
							ReLogoFilter.isInReLogoPerspective = true;
							Display.getDefault().asyncExec(packageViewerRefresh);
						} else {
							ReLogoFilter.isInReLogoPerspective = false;
							Display.getDefault().asyncExec(packageViewerRefresh);
						}
					}
				};
				
				
				
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.addPerspectiveListener(reLogoFilterToggle);

				PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {

					@Override
					public boolean preShutdown(IWorkbench workbench, boolean forced) {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.removePerspectiveListener(reLogoFilterToggle);
						return true;
					}

					@Override
					public void postShutdown(IWorkbench workbench) {

					}
				});
			}
		});
		
		// We use the perspective bar extras to check for whether
		// the earlyStartup was run in this workspace before
		String extras = PlatformUI.getPreferenceStore().getString(
				IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS);
		// If not, we open the ReLogo perspective, remove the Intro screen, and
		// enable the ReLogo Resource Filter
		if (!extras.contains(RELOGO_PERSPECTIVE_ID)) {
			if (extras.equals("")) {
				extras = RELOGO_PERSPECTIVE_ID;
			} else {
				extras = extras + "," + RELOGO_PERSPECTIVE_ID;
			}
			PlatformUI.getPreferenceStore().setValue(
					IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS, extras);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IPerspectiveDescriptor[] openPersps = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage().getOpenPerspectives();
					boolean found = false;
					for (IPerspectiveDescriptor pd : openPersps) {
						if (pd.getId().equals(RELOGO_PERSPECTIVE_ID)) {
							found = true;
							break;
						}
					}
					if (!found) {
						IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
						IPerspectiveDescriptor relogoPersp = reg.findPerspectiveWithId(RELOGO_PERSPECTIVE_ID);
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.setPerspective(relogoPersp);
					}

					// Close the intro screen to avoid confusion.
					IIntroManager im = PlatformUI.getWorkbench().getIntroManager();
					if (im != null) {
						IIntroPart iip = im.getIntro();
						if (iip != null) {
							im.closeIntro(iip);
						}
					}

				}
			});
		}
		
		// Needed for restart case, since the perspective is activated before the earlyStartup code.
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				IPerspectiveDescriptor perspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
				if (perspective != null){
					if (perspective.getId().equals(RELOGO_PERSPECTIVE_ID)){
						ReLogoFilter.isInReLogoPerspective = true;
						Display.getDefault().asyncExec(packageViewerRefresh);
					}
				}
			}
		});

	}
}
