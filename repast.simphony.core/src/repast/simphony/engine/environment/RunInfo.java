/*$$
 * Copyright (c) 2007, Argonne National Laboratory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the Repast project nor the names the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package repast.simphony.engine.environment;


/**
 * A class that contains information on a simulation run.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class RunInfo {

	// TODO: retrieve the rootContext name parameter from an annotation on the
	// rootContext
	private String modelName;

	private int batchNumber=0;

	private int runNumber=0;

	private boolean isBatch = false;


	/**
	 * Creates this run info with the specified model name, batch run number
	 * number, and run number and default value of false for isBatch.
	 *
	 * @param modelName the name of the model
	 * @param batchNumber the batch number of the simulation
	 * @param runNumber the run number of the simulation
	 */

	public RunInfo(String modelName, int batchNumber, int runNumber) {
		this(modelName, batchNumber, runNumber, false);
	}

   public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
	/**
	 * Creates this run info with the specified model name, batch run number
	 * number, run number, and whether or not this is a run in a true batch run.
	 *
	 * @param modelName the name of the model
	 * @param batchNumber the batch number of the simulation
	 * @param runNumber the run number of the simulation
	 */
	public RunInfo(String modelName, int batchNumber, int runNumber, boolean isBatch) {
		super();
		this.modelName = modelName;
		this.batchNumber = batchNumber;
		this.runNumber = runNumber;
		this.isBatch = isBatch;
	}

	public RunInfo(RunInfo base) {
		this(base.modelName, base.batchNumber, base.runNumber, base.isBatch);
	}

	/**
	 * Retrieves the name of the rootContext the logging is running for
	 * 
	 * @return the name of the rootContext the logging is running for
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * Retrieves the current batch number
	 * 
	 * @return the current batch number
	 */
	public int getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @return true if the current run is one of series of runs in a "true" batch run.
	 */
	public boolean isBatch() {
		return isBatch;
	}


	/**
	 * Retrieves the current run number. During a batch run the batch number
	 * would be the number associated with a set of batch runs, while the run
	 * number is the actual run in a set of batch runs.<p/>
	 * 
	 * <pre>
	 *  Batch number (1):
	 *  -&gt;run(1)
	 *  -&gt;run(2)
	 *  Batch Run (2): 
	 *  -&gt;run(2)
	 *  -&gt;run(3)
	 *  ...
	 *  </pre>
	 *  @return the current sub-run number
	 * 
	 */
	public int getRunNumber() {
		return runNumber;
	}

//	/**
//	 * Retrieves the root context of the model
//	 * 
//	 * @return the root context of the model
//	 */
//	public Context getRootContext() {
//		return rootContext;
//	}

	/**
	 * Compares run info objects based on their publicly accessible attributes.
	 * 
	 * @param obj
	 *            a run info object
	 * 
	 * @return true if the objects are equal according to the getters, false if
	 *         the object is not a run info object or if the attributes are not
	 *         equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RunInfo)) {
			return false;
		}

		RunInfo runInfo = (RunInfo) obj;

		return /*runInfo.getRootContext().equals(rootContext)
				&& */runInfo.getModelName().equals(modelName)
				&& runInfo.getBatchNumber() == batchNumber
				&& runInfo.getRunNumber() == runNumber;
	}

	/**
	 * Returns a hashcode of the rootContext's properties.
	 * 
	 * @return <code> rootContext.hashCode() ^ modelName.hashCode()
	 *			^ batchNumber ^ runNumber </code>
	 */
	@Override
	public int hashCode() {
		return /*rootContext.hashCode() ^ */modelName.hashCode() ^ batchNumber
				^ runNumber;
	}
}