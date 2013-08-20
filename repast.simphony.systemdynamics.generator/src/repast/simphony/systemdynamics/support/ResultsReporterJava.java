/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author bragen
 *
 */
public class ResultsReporterJava implements ResultsReporter {

    private String file;
    private Data data;
    private BufferedWriter bw;
    
//    public ResultsReporterJava(Data data) {
//	this.data = data;
//    }
//
//    public ResultsReporterJava(Data data, String file) {
//	this(data);
//	this.file = file;
//    }

    public void writeReport(String file, Data data) {
	this.file = file;
	this.data = data;
	bw = Utilities.openFileForWriting(file);

	try {

	    double step = getStepForReport();

	    bw.append("Variable/Time");
	    for (double time = data.getINITIALTIME(); time <= data.getFINALTIME(); time += step) {
		bw.append("," + time);
	    }
	    bw.append("\n");
	    for (String array : data.getArraysHistory().keySet()) {
		Map<String, List<Double>> subscripts = data.getArraysHistory().get(array);
		for (String subscript : subscripts.keySet()) {
		    List<Double> values = subscripts.get(subscript);
		    bw.append("\"" + array);
		    bw.append("[" + subscript + "]\"");

		    for (double time = data.getINITIALTIME(); time <= data.getFINALTIME(); time += step) {
			int index = toIndex(time - data.getINITIALTIME(), data.getTIMESTEP());
			if (index > values.size()-1)
			    index = 0;
			bw.append("," + values.get(index));
		    }
		    bw.append("\n");
		}
	    }
	    for (String var : data.getModelVariables()) {
		bw.append(var);

		for (double time = data.getINITIALTIME(); time <= data.getFINALTIME(); time += step) {
		    int index = toIndex(time - data.getINITIALTIME(), data.getTIMESTEP());
		    if (index > data.getDataHistoryFor(var).size()-1)
			index = 0;
		    bw.append("," + data.getDataHistoryFor(var).get(index));
		}
		bw.append("\n");
	    }
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    public int toIndex(double time, double timeStep) {

	return (int) (time/timeStep);

    }
    
    public int toIndex(double time) {

	return (int) ((time - data.getINITIALTIME())/data.getTIMESTEP());

    }

    public double getStepForReport() {
	// default to TIME STEP
	double step = data.getTIMESTEP();

	// if SAVEPER defined, use it
	if (data.hasData("SAVEPER"))
	    step = data.valueOf("SAVEPER");

	return step;
    }

    public void setFile(String file) {
        this.file = file;
    }


}
