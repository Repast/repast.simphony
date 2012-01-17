/*$$
 * Copyright (c) 2007, Argonne National Laboratory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *       Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *       Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
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
package repast.simphony.chart2;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.statistics.SimpleHistogramDataset;

import cern.colt.list.DoubleArrayList;

/**
 * Base class for histogram datasets to be used with JFreeCharts. This will
 * accumulate values in a buffer until {@link #update()} is called. At that
 * point the current histogram data will be removed and the data in the buffer
 * will be histogramed. The default is never to fire change events and let the
 * chart execute code do that actual update via the plot.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public abstract class AbstractHistogramDataset extends SimpleHistogramDataset {

  protected DoubleArrayList buffer = new DoubleArrayList();
  protected boolean notifyListeners = true;

  public AbstractHistogramDataset(Comparable<?> seriesKey) {
    super(seriesKey);
    setAdjustForBinSize(false);
  }

  /**
   * Adds a data value to the buffer of values. These values will be
   * histogrammed on a call to {@link #update()}.
   * 
   * @param val
   */
  protected abstract void addValues(DoubleArrayList vals);

  /**
   * Updates the histogram with the values in the buffer.
   */
  public void update(DoubleArrayList vals) {
    addValues(vals);
    doUpdate();
    buffer.clear();
  }

  /**
   * Performs the actual histogramming.Implementation is left to subclasses
   */
  protected abstract void doUpdate();

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jfree.data.general.AbstractDataset#notifyListeners(org.jfree.data.general
   * .DatasetChangeEvent)
   */
  @Override
  protected void notifyListeners(DatasetChangeEvent arg0) {
    if (notifyListeners)
      super.notifyListeners(arg0);
  }
}
