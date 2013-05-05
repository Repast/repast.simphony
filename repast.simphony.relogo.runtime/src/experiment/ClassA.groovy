package experiment


class ClassA {



	public void sayHelloA(){
	}

	public void sayGoodByeA(){
	}

	public void ask(ClassB b, Closure closure){
	}
	public void ask(ClassA a,Closure closure){
	
}

	public void ask(Collection<ClassB> cb, Closure closure){
	}

	public static void main(String[] args) {

		
	}
	
	public static class ClassB{
		public void ask(Collection<ClassA> ca,Closure closure){
		}

		public void ask(ClassB b,Closure closure){
		}
		
		public void ask(ClassA a,Closure closure){
			
		}
		
		public void sayHelloB(){
		}

		public void sayGoodByeB(){
		}
		
		public ClassB method3(){
			return new ClassB()
		}
	}

	public void hello(){
		ClassB b = new ClassB()
		ClassB bb = new ClassB()
		ClassB bbb = new ClassB()
		ClassA a = new ClassA()
		
//		ask(a) {
//			ask(b) {
//				ask(bb){
//					ask(bbb){
//						
//					}
//				}
//			}
//		}
		
//		ask(a){
//			
//		}
		ask(a){
			
			ask(b){
				
			}
		}
		
//		test1(b){
//			test1(bb){
//				
//			}
//		}
		
//		ask(method3()){
//			
//			sayHelloB()
//		}
//		ask(method2()){
//			sayHelloB()
//			
//		}
//		ask(method2()){
//			
//			ask(a) {
//				
//				
//			}
//		}
	}
	
	public void test1(ClassB b, Closure closure){
		
	}

	public void myMethod(int one, Closure closure){
	}

	public void myMethod(String one, Closure closure){
	}

	public void myMethod(GrClass grClass, Closure closure){
	}

	public Collection<ClassB> method2(){
		return new ArrayList<ClassB>()
	}

	public ClassB method3(){
		return new ClassB()
	}
	
	public void myMethod(Collection<String> cs, Closure closure){
	}
}
