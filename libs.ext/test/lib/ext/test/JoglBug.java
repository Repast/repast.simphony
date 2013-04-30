package lib.ext.test;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesImmutable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.opengl.GLAutoDrawableDelegate;
import com.jogamp.opengl.test.junit.jogl.demos.es2.GearsES2;

/**
 * Adapted from com.jogamp.opengl.test.junit.jogl.acore.TestGLAutoDrawableGLWindowOnOffscrnCapsNEWT.testGL2OnScreenDblBuf()
 *
 * @author eric
 *
 */
public class JoglBug {

	static final int widthStep = 800/4;
	static final int heightStep = 600/4;
	volatile int szStep = 2;

	static GLCapabilities getCaps(String profile) {
		if( !GLProfile.isAvailable(profile) )  {
			System.err.println("Profile "+profile+" n/a");
			return null;
		}
		return new GLCapabilities(GLProfile.get(profile));
	}

	void doTest(GLCapabilitiesImmutable reqGLCaps, GLEventListener demo) throws InterruptedException {
		final GLDrawableFactory factory = GLDrawableFactory.getFactory(reqGLCaps.getGLProfile());

		final Window window = NewtFactory.createWindow(reqGLCaps);

		window.setSize(widthStep*szStep, heightStep*szStep);
		window.setVisible(true);

		//
		// Create native OpenGL resources .. XGL/WGL/CGL .. 
		// equivalent to GLAutoDrawable methods: setVisible(true)
		//         
		final GLDrawable drawable = factory.createGLDrawable(window);
		drawable.setRealized(true);

		final GLContext context = drawable.createContext(null);
		context.makeCurrent();
		context.release();

		final GLAutoDrawableDelegate glad = new GLAutoDrawableDelegate(drawable, context, window, false, null) {
			@Override
			protected void destroyImplInLock() {
				super.destroyImplInLock();  // destroys drawable/context
				window.destroy(); // destroys the actual window, incl. the device
			}
		};

		window.setWindowDestroyNotifyAction( new Runnable() {
			public void run() {
				glad.windowDestroyNotifyOp();
			} } );

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowRepaint(WindowUpdateEvent e) {
				glad.windowRepaintOp();
			}

			@Override
			public void windowResized(WindowEvent e) {
				glad.windowResizedOp(window.getWidth(), window.getHeight());
			}
		});

		glad.addGLEventListener(demo);

		glad.display(); // initial resize/display

		// 1 - szStep = 2
		glad.display();

		// 2, 3 (resize + display)
		szStep = 1;
		window.setSize(widthStep*szStep, heightStep*szStep);
		glad.display();

		// 4, 5 (resize + display)
		szStep = 4;
		window.setSize(widthStep*szStep, heightStep*szStep);
		glad.display();

		Thread.sleep(50);

		glad.destroy();
		System.out.println("Fin Drawable: "+drawable);        
		System.out.println("Fin Window: "+window);
	}

	public static void main(String[] args){

		JoglBug bug = new JoglBug();

		final GLCapabilities reqGLCaps = getCaps(GLProfile.GL2);

		if(null == reqGLCaps) return;

		try {
			bug.doTest(reqGLCaps, new GearsES2(1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}