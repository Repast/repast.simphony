package repast.simphony.relogo;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collections;

import cern.colt.function.Double9Function;
import cern.colt.matrix.DoubleFactory2D;
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
	
	
//	public static void diffuseMatrix(DoubleMatrix2D m2d, double num){
//		final double number = num;
//		Double9Function f = new Double9Function() {
//			   public final double apply(
//			      double a00, double a01, double a02,
//			      double a10, double a11, double a12,
//			      double a20, double a21, double a22) {
//			         return a11 + ((a00+a01+a02 + a10+a12 + a20+a21+a22)* number/8) - (number * a11);
//			      }
//		};
//		DoubleMatrix2D m2dn = new DenseDoubleMatrix2D(100,100);
//		m2d.zAssign8Neighbors(m2dn, f);
//	}
	
	public static void diffuse(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, double diffusionCoeff, boolean isMoore, boolean isPeriodic){
		if(isPeriodic){
			diffuseP(ddm, ddm2, diffusionCoeff, isMoore);
		}
		else {
			diffuseNP(ddm, ddm2, diffusionCoeff, isMoore);
		}
	}
	
	/**
	 * Periodic diffuse
	 * @param ddm
	 * @param ddm2
	 * @param diffusionCoeff
	 * @param width
	 * @param height
	 * @param isMoore
	 */
	private static void diffuseP(DoubleMatrix2D ddm, DoubleMatrix2D ddm2,
			double diffusionCoeff, boolean isMoore) {
		int origRows = ddm.rows();
		int origCols = ddm.columns();
		DoubleMatrix2D tempBuffered = bufferMatrix1(ddm);
		ddm = new DenseDoubleMatrix2D(tempBuffered.rows(),tempBuffered.columns());
		if (isMoore){
			tempBuffered.zAssign8Neighbors(ddm, new DiffuseBody(diffusionCoeff));
		}
		else{
			tempBuffered.zAssign8Neighbors(ddm, new Diffuse4Body(diffusionCoeff));
		}
		ddm2.assign(ddm.viewPart(1, 1, origRows, origCols));
		
	}
	
	protected static DoubleMatrix2D bufferMatrix1(DoubleMatrix2D ddm){
		DoubleFactory2D F = DoubleFactory2D.dense;
		int [] zero = {0};
		int [] aRows = {ddm.rows()-1};
		int [] aColumns = {ddm.columns()-1};
		
		DoubleMatrix2D[][] parts = 
			{
			   { ddm.viewSelection(aRows, aColumns),       ddm.viewSelection(aRows, null) , ddm.viewSelection(aRows, zero)        },
			   { ddm.viewSelection(null, aColumns), ddm,        ddm.viewSelection(null, zero) },
			   { ddm.viewSelection( zero, aColumns),       ddm.viewSelection(zero, null), ddm.viewSelection(zero, zero)        }
			};
		return F.compose(parts);
	}

	/**
	 * Non periodic diffuse
	 * @param ddm
	 * @param ddm2
	 * @param diffusionCoeff
	 * @param width
	 * @param height
	 * @param isMoore
	 */
	private static void diffuseNP(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, double diffusionCoeff, boolean isMoore){
		if (isMoore){
			diffuseNPMoore(ddm, ddm2, diffusionCoeff);
		}
		else {
			diffuseNPVN(ddm, ddm2, diffusionCoeff);
		}
		
		
		
		

		
	}
	
	private static void diffuseNPMoore(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, double diffusionCoeff){
		ddm.zAssign8Neighbors(ddm2, new DiffuseBody(diffusionCoeff));
		// Non-periodic Moore Neighbors
		int[][] firstRowNeighbors = {{0,-1},{0,1},{1,-1},{1,0},{1,1}};
		int[][] lastRowNeighbors = {{0,-1},{0,1},{-1,-1},{-1,0},{-1,1}};
		int[][] firstColNeighbors = {{-1,0},{1,0},{-1,1},{0,1},{1,1}};
		int[][] lastColNeighbors = {{-1,0},{1,0},{-1,-1},{0,-1},{1,-1}};
		int[][] topLeftNeighbors = {{0,1},{1,0},{1,1}};
		int[][] topRightNeighbors = {{0,-1},{1,0},{1,-1}};
		int[][] bottomLeftNeighbors = {{0,1},{-1,0},{-1,1}};
		int[][] bottomRightNeighbors = {{0,-1},{-1,0},{-1,-1}};
		allEdgeDiffuse(ddm,ddm2,firstRowNeighbors,lastRowNeighbors,firstColNeighbors,lastColNeighbors,topLeftNeighbors,topRightNeighbors, bottomLeftNeighbors,bottomRightNeighbors,diffusionCoeff,true);
	}
	
	private static void diffuseNPVN(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, double diffusionCoeff){
		ddm.zAssign8Neighbors(ddm2, new Diffuse4Body(diffusionCoeff));
		// Non-periodic VN Neighbors
		int[][] firstRowNeighbors = {{0,-1},{0,1},{1,0}};
		int[][] lastRowNeighbors = {{0,-1},{0,1},{-1,0}};
		int[][] firstColNeighbors = {{-1,0},{1,0},{0,1}};
		int[][] lastColNeighbors = {{-1,0},{1,0},{0,-1}};
		int[][] topLeftNeighbors = {{0,1},{1,0}};
		int[][] topRightNeighbors = {{0,-1},{1,0}};
		int[][] bottomLeftNeighbors = {{0,1},{-1,0}};
		int[][] bottomRightNeighbors = {{0,-1},{-1,0}};
		allEdgeDiffuse(ddm,ddm2,firstRowNeighbors,lastRowNeighbors,firstColNeighbors,lastColNeighbors,topLeftNeighbors,topRightNeighbors, bottomLeftNeighbors,bottomRightNeighbors,diffusionCoeff,false);
	}
	
	
	private static void allEdgeDiffuse(DoubleMatrix2D ddm, DoubleMatrix2D ddm2,
			int[][] firstRowNeighbors, int[][] lastRowNeighbors,
			int[][] firstColNeighbors, int[][] lastColNeighbors,
			int[][] topLeftNeighbors, int[][] topRightNeighbors,
			int[][] bottomLeftNeighbors, int[][] bottomRightNeighbors,
			double diffusionCoeff, boolean isMoore) {

		// first and last rows and columns
		// rows -> grid locations (height-1 -> 0)
		// columns -> grid locations (0 -> width)

		int rows = ddm.rows();
		int columns = ddm.columns();
		
		edgeDiffuse(ddm,ddm2,firstRowNeighbors,0,0,1,columns-2,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,lastRowNeighbors,rows-1,rows-1,1,columns-2,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,firstColNeighbors,1,rows-2,0,0,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,lastColNeighbors,1,rows-2,columns-1,columns-1,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,topLeftNeighbors,0,0,0,0,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,topRightNeighbors,0,0,columns-1,columns-1,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,bottomLeftNeighbors,rows-1,rows-1,0,0,diffusionCoeff,isMoore);
		
		edgeDiffuse(ddm,ddm2,bottomRightNeighbors,rows-1,rows-1,columns-1,columns-1,diffusionCoeff,isMoore);
		
	}

	public static void edgeDiffuse(DoubleMatrix2D ddm, DoubleMatrix2D ddm2, int[][] neighbors, int rowMin, int rowMax, int colMin, int colMax, double diffusionCoeff, boolean isMoore){
		int totalPossibleNeighbors = isMoore ? 8 : 4;
		int maxRow = ddm.rows() - 1;
		int maxCol = ddm.columns() - 1 ;
		for (int row = rowMin; row <= rowMax; row++){
			for (int col = colMin; col <= colMax; col++){
				double neighborTally = 0.0;
				int numNeighbors = 0;
				for (int neighbor = 0 ; neighbor < neighbors.length; neighbor++){
					int rowIndex = neighbors[neighbor][0] + row;
					int colIndex = neighbors[neighbor][1] + col;
					if (rowIndex >= 0 && rowIndex <= maxRow && colIndex >= 0 && colIndex <= maxCol){
						neighborTally += ddm.getQuick(rowIndex,colIndex);
						numNeighbors++;
					}
				}
				ddm2.setQuick(row, col , ddm.getQuick(row,col)*(1-diffusionCoeff*numNeighbors/totalPossibleNeighbors) + neighborTally*diffusionCoeff/totalPossibleNeighbors);
			}
		}
	}

}
