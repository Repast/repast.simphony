package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collections;

import cern.colt.function.Double9Function;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

public class JavaUtility {
	
	public static AgentSet inRadiusUtility(AgentSet a, Number num, ContinuousSpace space, NdPoint location){
		double distSq = num.doubleValue() * num.doubleValue();
		ArrayList list = new ArrayList();
		for (Object o : a){
			if (space.getDistanceSq(location, space.getLocation(o)) <= distSq){
				list.add(o);
			}
		}
//		ContinuousWithin cwq = new ContinuousWithin(getMyObserver().getSpace(), this, num.doubleValue())
//		List listWithin = cwq.query().iterator().toList();
//		Predicate pr = QueryUtils.createContains(cwq.query());
//		FilteredIterator result = new FilteredIterator(a.iterator(),pr)
//		return new AgentSet(result.toList());
//		return new AgentSet(a.intersect(listWithin));
		return new AgentSet(list);
		
	}
	
	public static void askAgentSet(Closure cl, AgentSet a){
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		ArrayList temp = new ArrayList(a);
		Collections.shuffle(temp);
		for (Object o : temp){
			cl.setDelegate(o);
			cl.call(o);
		}
	}
	
	public static void diffuseMatrix(DoubleMatrix2D m2d, double num){
		final double number = num;
		Double9Function f = new Double9Function() {
			   public final double apply(
			      double a00, double a01, double a02,
			      double a10, double a11, double a12,
			      double a20, double a21, double a22) {
			         return a11 + ((a00+a01+a02 + a10+a12 + a20+a21+a22)* number/8) - (number * a11);
			      }
		};
		DoubleMatrix2D m2dn = new DenseDoubleMatrix2D(100,100);
		m2d.zAssign8Neighbors(m2dn, f);
	}
	
	public static void diffuse(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, double diffusionCoeff, int width, int height){
		
		ddm.zAssign8Neighbors(ddm2, new DiffuseBody(diffusionCoeff));
		// first and last rows and columns
		// rows -> grid locations (height-1 -> 0)
		// columns -> grid locations (0 -> width)
		
		int rows = height;
		int columns = width;
		
		int[][] firstRowNeighbors = {{0,-1},{0,1},{1,-1},{1,0},{1,1}};
		int[][] lastRowNeighbors = {{0,-1},{0,1},{-1,-1},{-1,0},{-1,1}};
		int[][] firstColNeighbors = {{-1,0},{1,0},{-1,1},{0,1},{1,1}};
		int[][] lastColNeighbors = {{-1,0},{1,0},{-1,-1},{0,-1},{1,-1}};
		int[][] topLeftNeighbors = {{0,1},{1,0},{1,1}};
		int[][] topRightNeighbors = {{0,-1},{1,0},{1,-1}};
		int[][] bottomLeftNeighbors = {{0,1},{-1,0},{-1,1}};
		int[][] bottomRightNeighbors = {{0,-1},{-1,0},{-1,-1}};

		edgeDiffuse(ddm,ddm2,firstRowNeighbors,0,0,1,width-2,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,lastRowNeighbors,rows-1,rows-1,1,columns-2,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,firstColNeighbors,1,rows-2,0,0,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,lastColNeighbors,1,rows-2,columns-1,columns-1,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,topLeftNeighbors,0,0,0,0,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,topRightNeighbors,0,0,columns-1,columns-1,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,bottomLeftNeighbors,rows-1,rows-1,0,0,diffusionCoeff);
		
		edgeDiffuse(ddm,ddm2,bottomRightNeighbors,rows-1,rows-1,columns-1,columns-1,diffusionCoeff);
	}
	
	public static void edgeDiffuse(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, int[][] neighbors, int rowMin, int rowMax, int colMin, int colMax, double diffusionCoeff){
		for (int row = rowMin; row <= rowMax; row++){
			for (int col = colMin; col <= colMax; col++){
				double neighborTally = 0.0;
				for (int neighbor = 0 ; neighbor < neighbors.length; neighbor++){
					neighborTally += ddm.getQuick(neighbors[neighbor][0] + row, neighbors[neighbor][1] + col);
				}
				ddm2.setQuick(row, col , ddm.getQuick(row,col)*(1-diffusionCoeff) + neighborTally*diffusionCoeff/((double)neighbors.length));
			}
		}
	}

}
