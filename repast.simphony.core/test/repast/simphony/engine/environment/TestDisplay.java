package repast.simphony.engine.environment;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.DisplayListener;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.ProbeListener;

/**
 * Test IDisplay implementation.
 * 
 * @author Eric Tatara
 *
 */
public class TestDisplay implements IDisplay {

	protected JPanel panel;
	
	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPause(boolean pause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayout(Layout<?, ?> layout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDisplayListener(DisplayListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutFrequency(LayoutFrequency frequency, int interval) {
		// TODO Auto-generated method stub

	}

	@Override
	public JPanel getPanel() {
		if (panel != null) return panel;
		
		else{
			panel = new JPanel();
			return panel;
		}
	}

	@Override
	public void registerToolBar(JToolBar bar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void iconified() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deIconified() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addProbeListener(ProbeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Layout<?, ?> getLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DisplayEditorLifecycle createEditor(JPanel panel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetHomeView() {
		// TODO Auto-generated method stub

	}

}
