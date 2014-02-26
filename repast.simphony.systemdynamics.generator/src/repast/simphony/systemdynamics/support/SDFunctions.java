package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SDFunctions {

    private Map<String, SmoothFunction> smoothFunctions = new HashMap<String, SmoothFunction>();
    private Map<String, DelayFunction> delayFunctions = new HashMap<String, DelayFunction>();
    private Map<String, Lookup> lookups = new HashMap<String, Lookup>();

    private Map<String, Double> initials = new HashMap<String, Double>();
    
    private int obCount = 0;
    private static int MAX_OB = 300;



    private SDModel model;

    public SDFunctions(SDModel model) {
	this.model = model;
    }

//    public Double LOOKUP(String varName, double x) {
//	Lookup l = lookups.get(varName);
//	if (l != null) {
//	    return l.getValue(x);
//	} else {
//	    System.out.println("Attempting tp access non-existent lookup: "+varName);
//	    return null;
//
//	}
//    }
    
    public Double LOOKUP(double[][] lookupArray, double x) {
	Double yVal = null;
	int xval = 0;
	int yval = 1;
	int len = lookupArray[xval].length;
	// out of range
	if (x < lookupArray[0][0]) {
		if (++obCount <= MAX_OB) {
			System.out.println("(func) WARNING: Below Out of range access to lookup: "+ x+" range: "+lookupArray[0][0]+" - "+lookupArray[xval][len - 1]);
//			System.out.println("Size: "+lookupArray.length+" x "+lookupArray[0].length);
		}
	    return lookupArray[1][0];
	}

	if (x > lookupArray[xval][len - 1]) {
		if (++obCount <= MAX_OB) {
			System.out.println("(func) WARNING: Above Out of range access to lookup: "+ x+" range: "+lookupArray[0][0]+" - "+lookupArray[xval][len - 1]);
//			System.out.println("Size: "+lookupArray.length+" x "+lookupArray[0].length);
		}
	    return lookupArray[yval][len - 1];
	}

	int pos = 0;
	for (int i = 0; i < len; i++) {
	    if (lookupArray[xval][i] == x) {
		return lookupArray[yval][i];
	    } else if (x < lookupArray[xval][i]) {
		pos = i - 1;
		break;
	    }
	}

	if (lookupArray[xval][pos] == lookupArray[xval][pos + 1])
	    return null;
	yVal = lookupArray[yval][pos] * (x - lookupArray[xval][pos + 1])
		/ (lookupArray[xval][pos] - lookupArray[xval][pos + 1])
		+ lookupArray[yval][pos + 1] * (x - lookupArray[xval][pos])
		/ (lookupArray[xval][pos + 1] - lookupArray[xval][pos]);

	return yVal;
    }

    public void ADDLOOKUP(String varName, double...ds ) {
	lookups.put(varName, new Lookup(varName, ds));
    }
    
    public void ADDLOOKUP(String varName, double[] time, double[] values) {
	lookups.put(varName, new Lookup(varName, time, values));
    }

    public void ADDLOOKUPPAIRS(String varName, double minX, double minXY, double maxX, double maxXY, double...ds ) {
	lookups.put(varName, new Lookup(varName,  minX,  minXY,  maxX,  maxXY, ds));
    }
    
    public double[][] ADDLOOKUPNATIVE(String varName, double... ds) {
	lookups.put(varName, new Lookup(varName, ds));
	return lookups.get(varName).getAsArray();
    }
    
    public double[][] ADDLOOKUPNATIVE(String varName, double[] time, double[] values) {
	lookups.put(varName, new Lookup(varName, time, values));
	return lookups.get(varName).getAsArray();
    }

    public double[][] ADDLOOKUPPAIRSNATIVE(String varName, int numValues, double minX,
	    double minXY, double maxX, double maxXY, double... ds) {
	lookups.put(varName, new Lookup(varName, minX, minXY, maxX, maxXY, ds));
	return lookups.get(varName).getAsArray();
    }

    public double INTEG(String varName, double currentValue, double time, double timeStep, double val, double initial) {
	if (model.getCurrentTime() <= model.getINITIALTIME())
	    return initial;

	else
	    return currentValue + (val * model.getTIMESTEP());
    }


    public double IFTHENELSE(String varName,   double currentValue, double time, double timeStep, boolean test, double arg1, double arg2) {
	if (test)
	    return arg1;
	else
	    return arg2;

    }

    public double NOP(String varName,  double currentValue, double time, double timeStep) {
	return currentValue;
    }



    public double MIN(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2) {
	return arg1 < arg2 ? arg1 : arg2;
    }

    public double MAX(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2) {
	return arg1 > arg2 ? arg1 : arg2;
    }
    public double ZIDZ(String varName,  double currentValue,double time, double timeStep, double arg1, double arg2) {
	return arg2 == 0.0 ? 0.0 : arg1 / arg2;
    }

    public double XIDZ(String varName,  double currentValue,double time, double timeStep, double arg1, double arg2, double arg3) {
	return arg2 == 0.0 ? arg3 : arg1 / arg2;
    }
    public double INITIAL(String varName, double currentValue, double time, double timeStep, double arg1) {
	if (!initials.containsKey(varName))
	    initials.put(varName, arg1);
	return initials.get(varName);
    }
    public double STEP(String varName,  double currentValue,double time, double timeStep, double arg1, double arg2) {
	if (time < arg2) {
	    return 0.0;
	} else {
	    return arg1;
	}

    }

    public double PULSE(String varName,  double currentValue,double time, double timeStep, double arg1, double arg2) {
	if (time >= arg1 && time < arg1 + arg2) {
	    return 1.0;
	} else {
	    return 0.0;
	}
    }

    public double MODULO(String varName,  double currentValue,double time, double timeStep, double arg1, double arg2) {
	return arg1 % arg2;
    }

    public double DELAY1(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    delayFunctions.put(varName, new Delay1(this, varName, arg2));
	}
	DelayFunction delay = delayFunctions.get(varName);	
	return delay.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2);
    }
    public double DELAY1I(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    delayFunctions.put(varName, new Delay1(this, varName, arg2));
	}
	DelayFunction delay = delayFunctions.get(varName);	
	return delay.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, arg3);
    }


    public double DELAY3(String varName, double currentValue,double time, double timeStep,double arg1,double arg2) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    delayFunctions.put(varName, new DelayN(this, varName, arg2));
	}
	DelayFunction delay = delayFunctions.get(varName);	
	return delay.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, 3, 3);
    }
    public double DELAY3I(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    delayFunctions.put(varName, new DelayN(this, varName, arg2));
	}
	DelayFunction delay = delayFunctions.get(varName);	
	return delay.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, 3, arg3);
    }

    public double DELAYN(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2, double arg3, double arg4) {

	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    delayFunctions.put(varName, new DelayN(this, varName, arg2));
	}
	DelayFunction delay = delayFunctions.get(varName);	
	return delay.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, arg3, arg4);
    }

    public double SMOOTH(String varName, double currentValue, double time, double timeStep, double arg1, double arg2) {

	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    smoothFunctions.put(varName, new Smooth(this, varName, arg2));
	}
	SmoothFunction smooth = smoothFunctions.get(varName);	
	return smooth.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2);

    }

    public double SMOOTHI(String varName,  double currentValue,double time, double timeStep, double arg1, double arg2, double arg3) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    smoothFunctions.put(varName, new Smooth(this, varName, arg2));
	}
	SmoothFunction smooth = smoothFunctions.get(varName);	
	return smooth.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, arg3);
    }

    public double SMOOTH3(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {

	return SMOOTH(varName, currentValue, time, timeStep, arg1, arg2);
	//	if (model.getCurrentTime() == model.getINITIALTIME()) {
	//	    smoothFunctions.put(varName, new SmoothN(this, varName, arg2));
	//	}
	//	SmoothFunction smooth = smoothFunctions.get(varName);	
	//	return smooth.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, 3);

    }
    public double SMOOTH3I(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    smoothFunctions.put(varName, new SmoothN(this, varName, arg2));
	}
	SmoothFunction smooth = smoothFunctions.get(varName);	
	return smooth.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, 3, arg3);
    }




    public double SMOOTHN(String varName,   double currentValue, double time, double timeStep, double arg1, double arg2, double arg3, double arg4) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    smoothFunctions.put(varName, new SmoothN(this, varName, arg2));
	}
	SmoothFunction smooth = smoothFunctions.get(varName);	
	return smooth.getValue(model.getCurrentTime(), model.getTIMESTEP(), arg1, arg2, arg3, arg4);
    }



    public double GAME(String varName,  double currentValue,double time, double timeStep, double arg1) {
	return arg1;
    }

    public double SIN(String varName,  double currentValue,double time, double timeStep, double arg1) {
	return Math.sin(arg1);
    }

    public double RAMP(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2, double arg3) {
	if (time < arg2) {
	    return 0.0;
	} else if (time < arg3) {
	    return currentValue + (timeStep * arg1);
	} else {
	    return currentValue;
	}
    }

    public double GETDATAMAX(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2, double arg3) {
	notImplemented();
	return arg1;
    }

    public double TREND(String varName,  double currentValue, double time, double timeStep, double arg1, double arg2, double arg3) {
	notImplemented();
	return arg1;
    }

    public double SAMPLEIFTRUE(String varName,  double currentValue, double time, double timeStep, boolean arg1, double arg2, double arg3) {
	notImplemented();
	return arg2;
    }

    protected void notImplemented() {
	ArrayList<String> al = null; //  = new ArrayList<String>();
	String x = al.get(10);
    }

    public double ABS(String varName, double currentValue, double time, double timeStep,double arg1) {

	return Math.abs(arg1);
    }
    public double ACTIVEINITIAL(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	notImplemented();
	return arg1;
    }
    public double ALLOCATEAVAILABLE(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	notImplemented();
	return arg1;
    }
    public double ALLOCATEBYPRIORITY(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5) {
	notImplemented();
	return arg1;
    }
    public double ALLOC(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4) {
	notImplemented();
	return arg1;
    }
    public double ARCCOS(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.acos(arg1);
    }
    public double ARCSIN(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.asin(arg1);
    }
    public double ARCTAN(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.atan(arg1);
    }
    public double COS(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.cos(arg1);
    }
    public double COSH(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.cosh(arg1);
    }
    public double CUMULATE(String varName, double currentValue, double time, double timeStep,double arg1) {

	notImplemented();
	return arg1;
    }
    public double CUMULATEF(String varName, double currentValue, double time, double timeStep,double arg1) {

	notImplemented();
	return arg1;
    }
    public double DELAYBATCH(String varName, double currentValue,double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6) {
	notImplemented();
	return arg1;
    }
    public double DELAYCONVEYOR(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6) {
	notImplemented();
	return arg1;
    }
    public double DELAYFIXED(String varName, double currentValue, double time, double timeStep,double input,double delayTime,double initialValue) {
	if (model.getCurrentTime() == model.getINITIALTIME()) {
	    delayFunctions.put(varName, new DelayFixed(this, varName, timeStep, delayTime, initialValue));
	}
	DelayFunction delay = delayFunctions.get(varName);	
	return delay.getValue(model.getCurrentTime(), model.getTIMESTEP(), input);
    }
    public double DELAYINFORMATION(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	notImplemented();
	return arg1;
    }
    public double DELAYMATERIAL(String varName, double currentValue,double time, double timeStep,double arg1,double arg2,double arg3,double arg4) {
	notImplemented();
	return arg1;
    }

    public double DELAYPROFILE(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5) {
	notImplemented();
	return arg1;
    }


    public double DELAYP(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	notImplemented();
	return arg1;
    }
    public double DEMANDATPRICE(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	notImplemented();
	return arg1;
    }
    public double DEPRECIATEBYSCHEDULE(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5) {
	notImplemented();
	return arg1;
    }
    public double DEPRECIATESTRAIGHTLINE(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4) {
	notImplemented();
	return arg1;
    }
    public double ELMCOUNT(String varName, double currentValue, double time, double timeStep,double arg1) {
	notImplemented();
	return arg1;
    }
    public double EXP(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.exp(arg1);
    }
    public double FINDZERO(String varName, double currentValue, double time, double timeStep,double arg1) {
	notImplemented();
	return arg1;
    }
    public double FORECAST(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3) {
	notImplemented();
	return arg1;
    }

    public double GAMMALN(String varName, double currentValue, double time, double timeStep,double arg1) {
	notImplemented();
	return arg1;
    }
    public double GET123CONSTANTS(String varName, double currentValue, double time, double timeStep,String arg1,String arg2,String arg3) {
	notImplemented();
	return -1.0;
    }
    public double GET123DATA(String varName, double currentValue, double time, double timeStep,String arg1,String arg2,String arg3,String arg4) {
	notImplemented();
	return -1.0;
    }
    public double GETDATAATTIME(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	notImplemented();
	return arg1;
    }
    public double GETDATABETWEENTIMES(/* String varName, double currentValue, double time, double timeStep, */ String ts, double forTime ,double mode) {

	// ts can be an array reference, 
	if (model.getTimeSeriesData().hasTimeSeriesFor(ts)) {
	    return model.getTimeSeriesData().getDataBetweenTimes(ts, forTime, mode);
	} else {
	    return 0.0;
	}
    }
    public double GETDATAFIRSTTIME(String varName, double currentValue, double time, double timeStep,double arg1) {
	notImplemented();
	return arg1;
    }
    public double GETDATALASTTIME(String varName, double currentValue, double time, double timeStep,double arg1) {
	notImplemented();
	return arg1;
    }
    public double GETDATATOTALPOINTS(String varName, double currentValue, double time, double timeStep,double mode) {

	notImplemented();
	return mode;
    }



    protected int getRowNumberFromCellAddress(String cell) {
	return Utilities.getRowFromCellAddress(cell);
    }

    protected int getColumnNumberFromCellAddress(String cell) {
	return Utilities.getColumnFromCellAddress(cell);
    }





    public double INTEGER(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	notImplemented();
	return arg1;
    }
    public double INTEGER(String varName, double currentValue, double time, double timeStep,double arg1) {
	return (int) arg1;
    }
    public double INTERNALRATEOFRETURN(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4) {
	notImplemented();
	return arg1;
    }
    public double INVERTMATRIX(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	notImplemented();
	return arg1;
    }
    public double LN(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.log(arg1);
    }
    public double LOG(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	return Math.log10(arg2);
    }

    public double POWER(String varName, double currentValue, double time, double timeStep,double arg1,double arg2) {
	return Math.pow(arg1, arg2);
    }

    public double PULSETRAIN(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4) {
	notImplemented();
	return arg1;
    }

    public double RANDOMBETA(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6,double arg7) {
	notImplemented();
	return arg1;
    }
    public double RANDOMBINOMIAL(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6,double arg7) {
	notImplemented();
	return arg1;

    }
    public double RANDOMEXPONENTIAL(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5) {
	notImplemented();
	return arg1;
    }
    public double RANDOMGAMMA(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6) {
	notImplemented();
	return arg1;
    }
    public double RANDOMLOOKUP(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6) {
	notImplemented();
	return arg1;
    }
    public double RANDOMNORMAL(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5) {
	notImplemented();
	return arg1;
    }
    public double RANDOMPOISSON(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6) {
	notImplemented();
	return arg1;
    }
    public double RANDOMTRIANGULAR(String varName, double currentValue, double time, double timeStep,double arg1,double arg2,double arg3,double arg4,double arg5,double arg6) {
	notImplemented();
	return arg1;
    }

    public double SINH(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.sinh(arg1);
    }




    public double SQRT(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.sqrt(arg1);
    }

    public  double TAN(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.tan(arg1);
    }
    public double TANH(String varName, double currentValue, double time, double timeStep,double arg1) {
	return Math.tanh(arg1);
    }

    public static boolean isInteger( String input )
    {
	try
	{
	    Integer.parseInt( input );
	    return true;
	}
	catch( Exception e)
	{
	    return false;
	}
    }

    /**
     * @param string
     * @param arrayValueOf
     * @param time
     * @param timeStep
     * @param valueOf
     * @return
     */
    public double VDMLOOKUP(String string, double arrayValueOf,
	    double time, double timeStep, double valueOf) {


	return model.getData().valueOf(string);
    }
    
    /**
     * @param string
     * @param arrayValueOf
     * @param time
     * @param timeStep
     * @param valueOf
     * @return
     */
    public double VDMLOOKUP(String string, double arrayValueOf,
	    double time, double timeStep, double[] valueOf) {


	return model.getData().valueOf(string);
    }
    
    /**
     * @param string
     * @param arrayValueOf
     * @param time
     * @param timeStep
     * @param valueOf
     * @return
     */
    public double VDMLOOKUP(String string, double arrayValueOf,
	    double time, double timeStep, double[][] valueOf) {


	return model.getData().valueOf(string);
    }

    /**
     * @param string
     * @param arrayValueOf
     * @param time
     * @param timeStep
     * @param sarray
     * @param vexp
     * @param mval
     * @param operation
     * @param err
     * @return
     */
    public double VECTORSELECT(String string, double arrayValueOf,
	    double time, double timeStep, double[] sarray, double[] vexp,
	    double mval, double operation, double err) {
	
	double result = 0.0;
	int numIncluded = 0;
	
	// set initial value of result to appropriate value for operation
	
	if (operation == 0.0 || operation == 4.0) {
	    result= 0.0;
	} else if (operation == 1.0) {
	    result = 1.0;
	} else if (operation == 2.0) {
	    result = Double.MAX_VALUE;
	} else if (operation == 3.0) {
	    result = Double.MIN_VALUE;
	}
	
	// perform the requested operation
	
	for (int i = 0; i < sarray.length; i++) {
	    if (sarray[i] == 1.0) {
		numIncluded++;
		if (operation == 0.0 || operation == 4.0) {
		    result += vexp[i];
		} else if (operation == 1.0) {
		    result *= vexp[i];
		} else if (operation == 2.0) {
		    result = Math.min(result, vexp[i]);
		} else if (operation == 3.0) {
		    result = Math.max(result, vexp[i]);
		}
	    }
	}
	
	// check the error conditions
	// need to figure out how to handle these error conditions
	
	if (err == 0.0) {
	    // do nothing
	} else if (err == 1.0) {
	    if (numIncluded == 0) {
		// raise error
	    }
	} else if (err == 2.0) {
	    if (numIncluded > 1) {
		// raise error
	    }
	} else if (err == 3.0) {
	    if (numIncluded == 0 || numIncluded > 1) {
		// raise error
	    }
	}
	
	// finally return the result
	
	if (operation == 4.0) {
	    return result / (double) numIncluded;
	} else {
	    return result;
	}
    }
    
    public double VECTORELMMAP(String string, double arrayValueOf,
	    double time, double timeStep, double[] _da1,  double e) {
	
	// c implementation
	/*
	
	double VECTORELMMAP(char *string, double arrayValueOf,double time, double timeStep, *double _da1,  double e) {
		
		return _da1[(int)e];
	}
	*/

	
	
	// TODO Auto-generated method stub
	return 0.0;
    }
    
    public double[] VECTORSORTORDER(String string, double arrayValueOf,
	    double time, double timeStep, double[] _da1, double e) {
	
	// use a bubble sort for the ordering -- based on assumption that vectors to be
	// sorted are relatively short
	
	// first make a copy of the original vector and index vector
	double[] _da1_copy = new double[_da1.length];
	double[] index = new double[_da1.length];
	for (int i = 0; i < _da1.length; i++) {
	    _da1_copy[i] = _da1[i];
	    index[i] = i;
	}
	
	// now sort both copy and index arrays
	
	int i = 0, j = 0;
	double tempv, tempi;
	 
	  for (i = (_da1_copy.length - 1); i > 0; i--)
	  {
	    for (j = 1; j <= i; j++)
	    {
	      if ((e > 0.0 && _da1_copy[j-1] > _da1_copy[j]) ||
		      (e <= 0.0 && _da1_copy[j-1] < _da1_copy[j]))
	      {
		  // swap data values
	        tempv = _da1_copy[j-1];
	        _da1_copy[j-1] = _da1_copy[j];
	        _da1_copy[j] = tempv;
	        
	        // swap index values
	        tempi = index[j-1];
	        index[j-1] = index[j];
	        index[j] = tempi;
	      }
	    }
	  }

	
	
	return index;
    }
}
