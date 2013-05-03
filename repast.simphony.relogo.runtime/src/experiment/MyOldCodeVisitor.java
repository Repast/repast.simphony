package experiment;

import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.MethodCallExpression;

public class MyOldCodeVisitor extends CodeVisitorSupport {

		int askFound = 0;
		
		public int getAskFound() {
			return askFound;
		}
		public void setAskFound(int askFound) {
			this.askFound = askFound;
		}
		public void visitMethodCallExpression(MethodCallExpression call) {
			if (call!=null && call.getMethodAsString().equals("ask")){
				System.out.println("Ask Found");
				askFound++;
			}
			call.getObjectExpression().visit(this);
			call.getMethod().visit(this);
			call.getArguments().visit(this);
		}

}
