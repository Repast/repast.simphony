/**
 * 
 */
package repast.simphony.integration;

import java.util.ArrayList;

public class DemoData {
	int[] col1;
	String[] col2;
	double[] col3;
	double[] col4;
	double[] col5;
	
	public int[] getCol1() {
		return col1;
	}
	public void setCol1(int[] col1) {
		this.col1 = col1;
	}
	public String[] getCol2() {
		return col2;
	}
	public void setCol2(String[] col2) {
		this.col2 = col2;
	}
	public double[] getCol3() {
		return col3;
	}
	public void setCol3(double[] col3) {
		this.col3 = col3;
	}
	public double[] getCol4() {
		return col4;
	}
	public void setCol4(double[] col4) {
		this.col4 = col4;
	}
	public double[] getCol5() {
		return col5;
	}
	public void setCol5(double[] col5) {
		this.col5 = col5;
	}
	
	public static class SpectralData {
		double col1;
		double col2;
		double col3;
		double col4;
		public double getCol1() {
			return col1;
		}
		public void setCol1(double col1) {
			this.col1 = col1;
		}
		public double getCol2() {
			return col2;
		}
		public void setCol2(double col2) {
			this.col2 = col2;
		}
		public double getCol3() {
			return col3;
		}
		public void setCol3(double col3) {
			this.col3 = col3;
		}
		public double getCol4() {
			return col4;
		}
		public void setCol4(double col4) {
			this.col4 = col4;
		}
	}
	
	String title;
	String date;
	
	int spectrometerType;
	
	ArrayList<SpectralData> spectralData = new ArrayList<SpectralData>();
	
	String xAxisType;
	int numXCols;
	int[][] xData;
	
	int numCalibrationRuns;

	public ArrayList<SpectralData> getSpectralData() {
		return spectralData;
	}
	public void addSpectralData(SpectralData data) {
		spectralData.add(data);
	}
	public void removeSpectralData(SpectralData data) {
		spectralData.remove(data);
	}
	
	public void setXdata(int[][] data) {
		this.xData = data;
	}
	public int[][] getXdata() {
		return xData;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getNumCalibrationRuns() {
		return numCalibrationRuns;
	}
	public void setNumCalibrationRuns(int numCalibrationRuns) {
		this.numCalibrationRuns = numCalibrationRuns;
	}
	public int getNumXCols() {
		return numXCols;
	}
	public void setNumXCols(int numXCols) {
		this.numXCols = numXCols;
	}
	public int getSpectrometerType() {
		return spectrometerType;
	}
	public void setSpectrometerType(int spectrometerType) {
		this.spectrometerType = spectrometerType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getXaxisType() {
		return xAxisType;
	}
	public void setXaxisType(String axisType) {
		xAxisType = axisType;
	}
}