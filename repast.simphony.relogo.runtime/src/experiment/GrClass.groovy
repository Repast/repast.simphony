package experiment

import org.codehaus.groovy.ast.expr.MethodCallExpression

class GrClass {

	public void hi(MethodCallExpression mce){
		mce.getMethodAsString()
	}
	
	public void greet(){
		println("Hi there")
	}
	

}
