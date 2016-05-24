package repast.simphony.valueLayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import repast.simphony.random.RandomHelper;

/**
 * A benchmark for value layer dense and sparse stores.  
 * Not a unit test.
 * 
 * @author eric
 *
 */
public class GridValueLayerBench {
	
	public static void main(String[] args) {

		RandomHelper.init();
		
		// Warm up
		for (int i=1; i<1E8; i++);
		
		// Dense value layer
		boolean isDense = true;
		int xdim = 1000;
		int ydim = 1000;

		long numWrites = (long)xdim * (long)ydim;
		long numReads = (long)xdim * (long)ydim*10;
		
		GridValueLayer grid = new GridValueLayer("Grid", isDense, xdim, ydim);
		
		long t0 = System.nanoTime();
		grid.set(1.0, 1, 1);
		for (int i=0; i<numWrites; i++){
			grid.set(1.0, RandomHelper.nextIntFromTo(0, xdim-1), RandomHelper.nextIntFromTo(0,ydim-1));
		}
		
		for (int i=0; i<numReads; i++){
			grid.get(1,1);
		}
		
		long tf = System.nanoTime();
		
		System.out.println("Dense GVL: " + (tf-t0)/1E9 + " Entries: " + grid.size()  ); 
//			+  " Size: " + getObjectSizeMB(grid)); //   Need to implement serializable on grid to use

		// Sparse value layer
		isDense = false;
		xdim = 50000;
		ydim = 50000;

		numWrites = (long)xdim * (long)ydim / 100;
		numReads = (long)xdim * (long)ydim / 100;
		
		try{
			grid = new GridValueLayer("Grid", isDense, xdim, ydim);

			t0 = System.nanoTime();
			grid.set(1.0, 1, 1);
			for (int i=0; i<numWrites; i++){
				grid.set(RandomHelper.nextIntFromTo(0,1), RandomHelper.nextIntFromTo(0, xdim-1), RandomHelper.nextIntFromTo(0,ydim-1));
			}
			for (int i=0; i<numReads; i++){
				grid.get(1,1);
			}

			tf = System.nanoTime();

			System.out.println("Sparse GVL: " + (tf-t0)/1E9 + " Entries: " + grid.size() );
//				+  " Size: " + getObjectSizeMB(grid));  //   Need to implement serializable on grid to use
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		// Best new sparse: 50k x 50k result
		// Sparse GVL: 8.426135835 Size: 570.426284
	

	}

	/**
	 * Gets the object size in MB. Assumes object and all fields implements Serializable.
	 * 
	 * @param o
	 * @return
	 */
	public static double getObjectSizeMB(Object o){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			oos.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.size()/1E6;
	}
}
