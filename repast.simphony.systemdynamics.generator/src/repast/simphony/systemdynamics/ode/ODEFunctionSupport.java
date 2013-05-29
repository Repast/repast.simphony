package repast.simphony.systemdynamics.ode;

public class ODEFunctionSupport {
	
	public ODEFunctionSupport() {
		
	}

	
	public double MIN(double arg1, double arg2) {
		return arg1 < arg2 ? arg1 : arg2;
	    }
	
    public double IFTHENELSE(boolean condition, double arg1, double arg2) {
	if (condition)
	    return arg1;
	else
	    return arg2;

    }
    
    public double MAX(double arg1, double arg2) {
	return arg1 > arg2 ? arg1 : arg2;
    }
    public double ZIDZ(double arg1, double arg2) {
	return arg2 == 0.0 ? 0.0 : arg1 / arg2;
    }

    public double XIDZ(double arg1, double arg2, double arg3) {
	return arg2 == 0.0 ? arg3 : arg1 / arg2;
    }
	
    public double MODULO(double arg1, double arg2) {
	return arg1 % arg2;
    }
}
