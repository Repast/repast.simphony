package anl.test.relogo

class UserPatch {

	// 1a
	double oneAvar
	
	public double getOneAvar(){
		return oneAvar
	}
	
	public void setOneAvar(double oa){
		oneAvar = oa
	}
	
	// 1b
	double oneBvar
	
	private double getOneBvar(){
		return oneBvar
	}
	
	boolean oneBvar2
	
	private void setOneBvar2(boolean ob2){
		oneBvar2 = ob2 
	}
	
	boolean oneBvar3
	
	private boolean isOneBvar3(){
		return oneBvar3
	}
	
	
	
	// 2
	public def twoDef, twoDef2
	public int twoInt, twoInt2, twoInt3
	public double twoDouble, twoDouble2
	public boolean twoBoolean
	public Boolean twoBoolean2
	
	
	// 3
	private double threeDouble
	
	public double getThreeDouble(){
		threeDouble	
	}
	
	public void setThreeDouble(double td){
		threeDouble = td
	}
	
	private boolean threeBoolean
	
	public boolean isThreeBoolean(){
		threeBoolean
	}
	
	public void setThreeBoolean(boolean tb){
		threeBoolean = tb
	}
	
	// 4
	
	public void setFourDouble(double fd){
		// nothing
	}
	
	public double getFourDouble(){
		return 2d;
	}
	
	double hi
	private double privar, privar2
	protected double provar, provar2
	
}