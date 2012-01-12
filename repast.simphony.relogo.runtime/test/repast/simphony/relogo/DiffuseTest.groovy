/**
 * 
 */
package repast.simphony.relogo

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import repast.simphony.relogo.factories.LinkFactory
import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*
import static java.lang.Math.*


/**
 * @author jozik
 *
 */
public class DiffuseTest extends GroovyTestCase{
	
		DoubleMatrix2D ddm
		DoubleMatrix2D ddm2 
				
		protected void setUp() throws Exception {
			
		}
		
		/**
		 * Simple test with isMoore T, isPeriodic F.
		 */
		public void testDiffuseTF1(){
			double [][] sd = [[0,0,0],[0,1,0],[0,0,0]]
			ddm = new DenseDoubleMatrix2D(sd)
			ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
			double diffusionCoeff = 0.3
			JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, false)
			
			double outs = diffusionCoeff/8
			double [][] sde = [[outs,outs,outs],[outs,0.7,outs],[outs,outs,outs]]
			DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
			assertEquals(expected,ddm2) // expected diffusion pattern
			assertEquals(ddm.zSum(),ddm2.zSum(),0.001) // total value is conserved
		}
		
		/**
		* Simple test 2 with isMoore T, isPeriodic F.
		* 
		*/
		public void testDiffuseTF2(){
			double [][] sd = [[1,1,1],[1,2,1],[1,1,1]]
			ddm = new DenseDoubleMatrix2D(sd)
			ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
			double diffusionCoeff = 0.3
			JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, false)
			
			double outs = 1 + diffusionCoeff/8
			double [][] sde = [[outs,outs,outs],[outs,1.7,outs],[outs,outs,outs]]
			DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
			assertEquals(expected,ddm2)
			assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
		}
		
		/**
		* Simple test 3 with isMoore T, isPeriodic F.
		*
		*/
		public void testDiffuseTF3(){
			double [][] sd = [[1, 1, 1], [0, 0, 0], [0, 0, 0]]
			ddm = new DenseDoubleMatrix2D(sd)
			ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
			double diffusionCoeff = 0.3
			JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, false)
	
			double [][] sde = [
				[0.925, 0.8875, 0.925],
				[0.075, 0.1125, 0.075],
				[0, 0, 0]
			]
			DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
			assertEquals(expected,ddm2)
			assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
		}
		
		/**
		* Simple test 4 with isMoore T, isPeriodic F. unequal dimensions
		*
		*/
		public void testDiffuseTF4(){
			double [][] sd = [[1, 1], [0, 0], [0, 0]]
			ddm = new DenseDoubleMatrix2D(sd)
			ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
			double diffusionCoeff = 0.3
			JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, false)
	
			double [][] sde = [
				[0.925, 0.925],
				[0.075, 0.075],
				[0, 0]
			]
			DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
			assertEquals(expected,ddm2)
			assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
		}
		
		/**
		* Simple test 5 with isMoore T, isPeriodic F. unequal dimensions smaller
		*
		*/
		public void testDiffuseTF5(){
			double [][] sd = [[1], [0], [ 0]]
			ddm = new DenseDoubleMatrix2D(sd)
			ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
			double diffusionCoeff = 0.3
			JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, false)
	
			double [][] sde = [
				[0.9625],
				[0.0375],
				[0]
			]
			DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
			assertEquals(expected,ddm2)
			assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
		}
		
		/**
		* Simple test 6 with isMoore T, isPeriodic F. unequal dimensions larger
		*
		*/
		public void testDiffuseTF6(){
			double [][] sd = [[1,1,1], [0,0,0], [0,0,0],[0,0,0]]
			ddm = new DenseDoubleMatrix2D(sd)
			ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
			double diffusionCoeff = 0.3
			JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, false)
	
			double [][] sde = [
				[0.925, 0.8875, 0.925],
				[0.075, 0.1125, 0.075],
				[0, 0, 0],
				[0, 0, 0]
			]
			DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
			assertEquals(expected,ddm2)
			assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
		}
		
		
			
		/**
		* Simple test with isMoore F, isPeriodic F.
		*/
	   public void testDiffuseFF1(){
		   double [][] sd = [[0,0,0],[0,1,0],[0,0,0]]
		   ddm = new DenseDoubleMatrix2D(sd)
		   ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		   double diffusionCoeff = 0.3
		   JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, false, false)
		   
		   double outs = diffusionCoeff/4
		   double [][] sde = [[0,outs,0],[outs,0.7,outs],[0,outs,0]]
		   DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		   assertEquals(expected,ddm2) // expected diffusion pattern
		   assertEquals(ddm.zSum(),ddm2.zSum(),0.001) // total value is conserved
	   }
	   
	   /**
	   * Simple test 2 with isMoore F, isPeriodic F.
	   *
	   */
	   public void testDiffuseFF2(){
		   double [][] sd = [[1,1,1],[1,2,1],[1,1,1]]
		   ddm = new DenseDoubleMatrix2D(sd)
		   ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		   double diffusionCoeff = 0.3
		   JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, false, false)
		   
		   double outs = 1 + diffusionCoeff/4
		   double [][] sde = [[1,outs,1],[outs,1.7,outs],[1,outs,1]]
		   DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		   assertEquals(expected,ddm2)
		   assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	   }
	   
	   // Periodic cases
	   /**
	   * Simple test with isMoore T, isPeriodic T.
	   */
	  public void testDiffuseTT1(){
		  double [][] sd = [[0,0,0],[0,1,0],[0,0,0]]
		  ddm = new DenseDoubleMatrix2D(sd)
		  ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		  double diffusionCoeff = 0.3
		  JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, true)
		  
		  double outs = diffusionCoeff/8
		  double [][] sde = [[outs,outs,outs],[outs,0.7,outs],[outs,outs,outs]]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,ddm2) // expected diffusion pattern
		  assertEquals(ddm.zSum(),ddm2.zSum(),0.001) // total value is conserved
	  }
	  
	  /**
	  * Simple test 2 with isMoore T, isPeriodic T.
	  *
	  */
	  public void testDiffuseTT2(){
		  double [][] sd = [[1,1,1],[1,2,1],[1,1,1]]
		  ddm = new DenseDoubleMatrix2D(sd)
		  ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		  double diffusionCoeff = 0.3
		  JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, true)
		  
		  double outs = 1 + diffusionCoeff/8
		  double [][] sde = [[outs,outs,outs],[outs,1.7,outs],[outs,outs,outs]]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,ddm2)
		  assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	  }
	  
	  /**
	  * Simple test 3 with isMoore T, isPeriodic T.
	  *
	  */
	  public void testDiffuseTT3(){
		  double [][] sd = [[1,1,1],[0,0,0],[0,0,0]]
		  ddm = new DenseDoubleMatrix2D(sd)
		  ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		  double diffusionCoeff = 0.3
		  JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, true)
		  
		  double [][] sde = [[0.775,  0.775,  0.775],[0.1125, 0.1125, 0.1125],[0.1125, 0.1125, 0.1125]]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,ddm2)
		  assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	  }
	  
	  /**
	  * Simple test 4 with isMoore T, isPeriodic T. unequal dimensions
	  *
	  */
	  public void testDiffuseTT4(){
		  double [][] sd = [[1, 1], [0, 0], [0, 0]]
		  ddm = new DenseDoubleMatrix2D(sd)
		  ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		  double diffusionCoeff = 0.3
		  JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, true)
  
		  double [][] sde = [
			  [0.775, 0.775],
			  [0.1125, 0.1125],
			  [0.1125, 0.1125]
		  ]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,ddm2)
		  assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	  }
	  
	  /**
	  * Simple test 5 with isMoore T, isPeriodic T. unequal dimensions smaller
	  *
	  */
	  public void testDiffuseTT5(){
		  double [][] sd = [[1], [0], [ 0]]
		  ddm = new DenseDoubleMatrix2D(sd)
		  ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		  double diffusionCoeff = 0.3
		  JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, true)
  
		  double [][] sde = [
			  [0.775],
			  [0.1125],
			  [0.1125]
		  ]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,ddm2)
		  assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	  }
	  
	  /**
	  * Simple test 6 with isMoore T, isPeriodic T. unequal dimensions larger
	  *
	  */
	  public void testDiffuseTT6(){
		  double [][] sd = [[1,1,1], [0,0,0], [0,0,0],[0,0,0]]
		  ddm = new DenseDoubleMatrix2D(sd)
		  ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		  double diffusionCoeff = 0.3
		  JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, true, true)
  
		  double [][] sde = [
			  [0.775, 0.775, 0.775],
			  [0.1125, 0.1125, 0.1125],
			  [0, 0, 0],
			  [0.1125, 0.1125, 0.1125]
		  ]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,ddm2)
		  assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	  }
	  
	  /**
	  * Simple test with isMoore F, isPeriodic T.
	  */
	 public void testDiffuseFT1(){
		 double [][] sd = [[0,0,0],[0,1,0],[0,0,0]]
		 ddm = new DenseDoubleMatrix2D(sd)
		 ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		 double diffusionCoeff = 0.3
 		JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, false, true)

		double outs = diffusionCoeff/4
		double [][] sde = [
			[0, outs, 0],
			[outs, 0.7, outs],
			[0, outs, 0]
		]
		DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		assertEquals(expected,ddm2) // expected diffusion pattern
		assertEquals(ddm.zSum(),ddm2.zSum(),0.001) // total value is conserved
	 }
	 
	 /**
	 * Simple test 2 with isMoore F, isPeriodic T.
	 *
	 */
	 public void testDiffuseFT2(){
		 double [][] sd = [[1,1,1],[1,2,1],[1,1,1]]
		 ddm = new DenseDoubleMatrix2D(sd)
		 ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		 double diffusionCoeff = 0.3
		 JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, false, true)
		 
		 double outs = 1 + diffusionCoeff/4
		   double [][] sde = [[1,outs,1],[outs,1.7,outs],[1,outs,1]]
		   DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		   assertEquals(expected,ddm2)
		   assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	 }
	 
	 /**
	 * Simple test 3 with isMoore F, isPeriodic T.
	 *
	 */
	 public void testDiffuseFT3(){
		 double [][] sd = [[1,1,1],[0,0,0],[0,0,0]]
		 ddm = new DenseDoubleMatrix2D(sd)
		 ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		 double diffusionCoeff = 0.3
		 JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, false, true)
		 
		 double [][] sde = [[0.85,  0.85,  0.85],[0.075, 0.075, 0.075],[0.075, 0.075, 0.075]]
		 DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		 assertEquals(expected,ddm2)
		 assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	 }
	 
	 /**
	 * Simple test 6 with isMoore F, isPeriodic T. unequal dimensions larger
	 *
	 */
	 public void testDiffuseFT6(){
		 double [][] sd = [[1,1,1], [0,0,0], [0,0,0],[0,0,0]]
		 ddm = new DenseDoubleMatrix2D(sd)
		 ddm2 = new DenseDoubleMatrix2D(ddm.rows(),ddm.columns());
		 double diffusionCoeff = 0.3
		 JavaUtility.diffuse(ddm, ddm2, diffusionCoeff, false, true)
 
		 double [][] sde = [
			 [0.85, 0.85, 0.85],
			 [0.075,0.075,0.075],
			 [0, 0, 0],
			 [0.075,0.075,0.075]
		 ]
		 DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		 assertEquals(expected,ddm2)
		 assertEquals(ddm.zSum(),ddm2.zSum(),0.001)
	 }
	  
	  public void testBufferMatrix1(){
		  DoubleFactory2D F = DoubleFactory2D.dense;
		  ddm = F.ascending(3, 3);
		  double [][] sde = [[9, 7, 8, 9, 7],[3, 1, 2, 3, 1],[6, 4, 5, 6, 4],[9, 7, 8, 9, 7],[3, 1, 2, 3, 1]]
		  DoubleMatrix2D expected = new DenseDoubleMatrix2D(sde)
		  assertEquals(expected,JavaUtility.bufferMatrix1(ddm))
	  }
	
	
}
