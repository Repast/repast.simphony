package repast.simphony.systemdynamics.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


    public class SDModelWithPropertiesVDM_Native extends SDModel {
        
        public static Properties PROPERTIES = new Properties();
        private static String DEFAULT_PROPERTIES = "DefaultRunner.properties";
        
        LoggerJava logger = new LoggerJava();
        
        
        protected VDM vdm = null;
        
        public SDModelWithPropertiesVDM_Native(String name, String[] args) {
    	super(name, args);
    	
    	if (args != null && args.length > 0)
    	    loadProperties(args[0]);
    	else
    	    loadProperties();
    	OUTPUT_DIRECTORY = PROPERTIES.getProperty("outputDirectory");
    	DATA_DIRECTORY = PROPERTIES.getProperty("dataDirectory");
    	
    	logger.setLogFile(name+"Log.csv");
    	
        }

        @Override
        public void execute() {
            
//            logger.setLogFile(name+"Log.csv");

    	Initializer.initialize();
    	initializeVDM();
    	if (vdm != null)
    	    vdm.advanceTime();
    	
    	    oneTime();
    	    int tick = 0;
    	    for (double time = getINITIALTIME(); time <= getFINALTIME(); time += getTIMESTEP()) {
    	         data.setCurrentTime(time);
    	         currentTime = time;
    	         Synchronizer.synchronize(currentTime, getTIMESTEP());
    	         repeated(time, getTIMESTEP());
    	         reportTimeStep(time);
    	         
    	         if (dumpMemory) {
    	             // dump memory at the end of each time tick
    	             String filename = "_memoryDump_"+(tick++)+".csv";
    	             results.writeReport(RunnerConstants.OUTPUT_DIRECTORY+name+filename, data);
    	         }
    	         
    	         if (vdm != null)
    	 	    vdm.advanceTime();
    	    }
//    	    results.writeReport(RunnerConstants.OUTPUT_DIRECTORY+name+"_sdReport.csv", data);
    	    logger.close();
    	}
        
        protected void logit(String var, double time, double value, double savper) {
        	int t = (int) (time/savper);
        	double remainder = (time - ((double) t * savper));
//        	if ((time - ((double) t * savper)) == 0.0)
        		logger.log(var+","+time+","+value+","+remainder);
        }
        
        protected void logitVector(String var, double time, int length, double[] value) {
            logger.log(var+","+time+","+value);
        }
        
        public boolean loadProperties(String file) {
    	File props = new File(file);
    	if (props.exists()) {
    		try {
    		    PROPERTIES.load(new FileInputStream(props));
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return true;
    	} else {
    	    return false;
    	}
        }
        
        public boolean loadProperties() {
    	return loadProperties(DEFAULT_PROPERTIES);
        }
        
        private void initializeVDM() {
    	    String vdmFile = SDModelWithPropertiesVDM_Native.PROPERTIES.getProperty("vdmfile");
    	    if (vdmFile.length() > 0) {
    		vdm = new VDM(this);
    		vdm.setFile(vdmFile);
    		vdm.loadFromFile();
    	    }
    	   
    	    
    }

}
