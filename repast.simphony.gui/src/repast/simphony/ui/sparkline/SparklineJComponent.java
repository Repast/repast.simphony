package repast.simphony.ui.sparkline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.jscience.physics.amount.Amount;

import simphony.util.ThreadUtilities;

import com.representqueens.spark.BarGraph;
import com.representqueens.spark.LineGraph;
import com.representqueens.spark.SizeParams;

/*
 * @author Michael J. North
 *
 */
public class SparklineJComponent extends JPanel {

	public static int DEFAULT_LENGTH = 2;

	public Number[] data;
	public BufferedImage image;
	public SizeParams params = new SizeParams(100, 12, 1);
	public Dimension previousComponentSize = null;
	public boolean lineGraph = true;
	private Updater updater;

	public Number[] getData() {
		return data;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public Color getHighlightColor() {
		return highlightColor;
	}

	public void setHighlightColor(Color highlightColor) {
		this.highlightColor = highlightColor;
	}

	public Color getLastColor() {
		return lastColor;
	}

	public void setLastColor(Color lastColor) {
		this.lastColor = lastColor;
	}

	public Color foregroundColor = Color.red.darker();
	public Color highlightColor = Color.red.brighter();
	public Color lastColor = Color.red;

	public SparklineJComponent() {

		super();

		this.data = new Double[DEFAULT_LENGTH];
		for (int i = 0; i < DEFAULT_LENGTH; i++) {
			this.data[i] = 0.0;
		}

		this.makeNewGraph();

		this.setSize(image.getWidth(), image.getHeight());
		this.previousComponentSize = new Dimension(image.getWidth(), image
				.getHeight());
		this.setPreferredSize(this.previousComponentSize);
		this.setMinimumSize(this.previousComponentSize);
		this.setMinimumSize(this.previousComponentSize);

		updater = new Updater();
	}

	public void addData(Object newValue) {

		if ((newValue != null)
				&& ((newValue instanceof Number) || (newValue instanceof Amount))) {

			for (int i = 1; i < data.length; i++) {
				data[i - 1] = data[i];
			}
			Number newValueNumber;
			if (newValue instanceof Number) {
				newValueNumber = (Number) newValue;
			} else if (newValue instanceof Amount) {
				newValueNumber = ((Amount) newValue).getEstimatedValue();
			} else {
				newValueNumber = 0.0;
			}
			data[data.length - 1] = newValueNumber.doubleValue();
			image = null;
			//ThreadUtilities.runInEventThreadAndWait(updater);
			updater.run();

		}

	}

	public void setData(Number newData[]) {

		this.data = newData;
		this.image = null;
		//ThreadUtilities.runInEventThreadAndWait(updater);
		updater.run();

	}

	public void setData(Amount newAmountData[]) {

		Double[] newData = new Double[newAmountData.length];
		for (int i = 0; i < newAmountData.length; i++) {
			newData[i] = newAmountData[i].getEstimatedValue();
		}
		this.setData(newData);

	}

	public void setData(double newDoubleData[]) {

		Double[] newData = new Double[newDoubleData.length];
		for (int i = 0; i < newDoubleData.length; i++) {
			newData[i] = newDoubleData[i];
		}
		this.setData(newData);

	}

	public void setData(float newFloatData[]) {

		Float[] newData = new Float[newFloatData.length];
		for (int i = 0; i < newFloatData.length; i++) {
			newData[i] = newFloatData[i];
		}
		this.setData(newData);

	}

	public void setData(int newIntData[]) {

		Integer[] newData = new Integer[newIntData.length];
		for (int i = 0; i < newIntData.length; i++) {
			newData[i] = newIntData[i];
		}
		this.setData(newData);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.previousComponentSize != this.getSize()) {
			this.previousComponentSize = this.getSize();
			this.params = new SizeParams(this.previousComponentSize.width - 2,
					this.previousComponentSize.height - 2, 1);
			this.image = null;
		}
		if (this.image == null)
			this.makeNewGraph();
		g.drawImage(this.image, 1, 1, this);
	}

	public void makeNewGraph() {

		if (this.lineGraph) {
			this.image = LineGraph.createGraph(this.data, this.params,
					this.foregroundColor);
		} else {
			this.image = BarGraph.createGraph(this.data, this.params,
					this.foregroundColor, this.highlightColor, this.lastColor);
		}

	}

	public boolean isLineGraph() {
		return lineGraph;
	}

	public void setLineGraph(boolean lineGraph) {
		this.lineGraph = lineGraph;
	}

	public void clearData() {
		this.setData(new Double[] { 0.0, 0.0 });
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	private class Updater implements Runnable {

		public void run() {
			repaint();
		}

	}

}
