package repast.simphony.visualization.gis3D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.Layer;

/**
 * Modified version of WorldWind LayerPanel example to reverse layer order in panel
 * 
 * @author Eric Tatara
 *
 */
public class LayerPanel extends JPanel{
	private JPanel layersPanel;
	private JPanel westPanel;
	private JScrollPane scrollPane;

	// Layers to exclude from the legend panel
	protected Set<String> layersToHide = new HashSet<String>();
	
	public LayerPanel(WorldWindow wwd){
		super(new BorderLayout());
		
		layersToHide.add(DisplayGIS3D.BACKGROUND_LAYER_NAME);
		makePanel(wwd, new Dimension(200, 400));
	}

	

	private void makePanel(WorldWindow wwd, Dimension size){
		// Make and fill the panel holding the layer titles.
		this.layersPanel = new JPanel(new GridLayout(0, 1, 0, 10));
		this.layersPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.fill(wwd);

		// Must put the layer grid in a container to prevent scroll panel from stretching their vertical spacing.
		JPanel dummyPanel = new JPanel(new BorderLayout());
		dummyPanel.add(this.layersPanel, BorderLayout.NORTH);

		// Put the name panel in a scroll bar.
		this.scrollPane = new JScrollPane(dummyPanel);
		this.scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		if (size != null)
			this.scrollPane.setPreferredSize(size);

		// Add the scroll bar and name panel to a titled panel that will resize with the main window.
		westPanel = new JPanel(new GridLayout(0, 1, 0, 10));
		westPanel.setBorder(
				new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Layers")));
		westPanel.setToolTipText("Layers to Show");
		westPanel.add(scrollPane);
		this.add(westPanel, BorderLayout.CENTER);

	}

	private void fill(WorldWindow wwd){
		// Fill the layers panel with the titles of all layers in the world window's current model.
		
		// Reverse the standarard order so that the foreground is the first in the list
		ArrayList<Layer> layers = new ArrayList<Layer>(wwd.getModel().getLayers());
		Collections.reverse(layers);
		
		for (Layer layer : layers){
			if (!layersToHide.contains(layer.getName())){
				LayerAction action = new LayerAction(layer, wwd, layer.isEnabled());
				JCheckBox jcb = new JCheckBox(action);
				jcb.setSelected(action.selected);
				layersPanel.add(jcb);
			}
		}
	}

	public void update(WorldWindow wwd){
		// Replace all the layer names in the layers panel with the names of the current layers.
		layersPanel.removeAll();
		fill(wwd);
		westPanel.revalidate();
		westPanel.repaint();
	}

	@Override
	public void setToolTipText(String string){
		scrollPane.setToolTipText(string);
	}

	private static class LayerAction extends AbstractAction{
		WorldWindow wwd;
		private Layer layer;
		private boolean selected;

		public LayerAction(Layer layer, WorldWindow wwd, boolean selected){
			super(layer.getName());
			this.wwd = wwd;
			this.layer = layer;
			this.selected = selected;
			this.layer.setEnabled(this.selected);
		}

		public void actionPerformed(ActionEvent actionEvent){
			// Simply enable or disable the layer based on its toggle button.
			if (((JCheckBox) actionEvent.getSource()).isSelected())
				this.layer.setEnabled(true);
			else
				this.layer.setEnabled(false);

			wwd.redraw();
		}
	}
}
