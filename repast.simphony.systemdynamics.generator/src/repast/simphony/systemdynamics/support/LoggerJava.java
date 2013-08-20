package repast.simphony.systemdynamics.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LoggerJava implements Logger {
    
    private BufferedWriter bw;
    private String logFile = "NonInitialized"+"Log.csv";

    @Override
    public void log(String s) {
	// TODO Auto-generated method stub

	if (bw == null) {
	    try {
		
//		if (logFile == null)
//		    logFile = "NonInitialized"+"Log.csv";
		
		File aFile = new File(logFile);

		bw = new BufferedWriter(new OutputStreamWriter(
			(new FileOutputStream(aFile, false))));
	    } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }     
	}
	
	try {
	    bw.append(s+"\n");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    
    public void close() {
	if (bw != null)
	    try {
		bw.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

}
