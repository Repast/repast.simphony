package repast.simphony.adaptation.regression;

/* @author Michael J. North */

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.MultipleLinearRegressionModel;

public class RepastRegressionModel {

	public ForecastingModel model;

	public DataSet history = new DataSet();
	
	public boolean findBest = false;

	public String dependentVariableName = "X";

	public String independentVariableName = "Y";
	
	public boolean recalculate = false;

	public RepastRegressionModel(boolean newFindBest) {
		this.findBest = newFindBest;
	}

	public void add(double x, double... yValues) {

		DataPoint dataPoint = new Observation(x);
		dataPoint.setIndependentValue(this.dependentVariableName, x);

		int yIndex = 0;
		for (double y : yValues) {
			dataPoint.setIndependentValue(this.independentVariableName
					+ (yIndex++), y);
		}

		history.add(dataPoint);
		this.recalculate = true;

	}

	public double forecast(double... yValues) {

		DataPoint dataPoint = new Observation(0);

		int yIndex = 0;
		String independentVariableNames[] = new String[yValues.length];
		for (double y : yValues) {
			independentVariableNames[yIndex] = this.independentVariableName
					+ yIndex;
			dataPoint.setIndependentValue(independentVariableNames[yIndex], y);
			yIndex = yIndex + 1;
		}

		if (this.model == null) {
			if (findBest) {
				this.model = Forecaster.getBestForecast(history);
			} else {
				this.model = new MultipleLinearRegressionModel(
						independentVariableNames);
			}
		}

		if (this.recalculate) {
			this.model.init(this.history);
			this.recalculate = false;
		}

		return model.forecast(dataPoint);

	}
	
	public String getForecastType() {
	
		return this.model.getForecastType();
		
	}
	
}
