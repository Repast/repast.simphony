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
import org.jfree.data.statistics.SimpleHistogramBin;

import cern.colt.list.DoubleArrayList;

/**
 * Dynamic histogram whose bin limits change as new data is added.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class DynamicHistogramDataset extends AbstractHistogramDataset {

  private int numBins;
  private double min, max;

  public DynamicHistogramDataset(Comparable<?> seriesKey, int numBins) {
    super(seriesKey);
    this.numBins = numBins;
    min = Double.POSITIVE_INFINITY;
    max = Double.NEGATIVE_INFINITY;
  }

  @Override
  protected void addValues(DoubleArrayList vals) {
    for (int i = 0, n = vals.size(); i < n; i++) {
      double val = vals.getQuick(i);
      min = Math.min(min, val);
      max = Math.max(max, val);
      buffer.add(val);
    }
  }

  @Override
  protected void doUpdate() {
    notifyListeners = false;
    if (buffer.size() > 0) {
      double curMin = -1, curMax = -1;
      int itemCount = getItemCount(0);
      if (itemCount > 1) {
        curMin = getStartXValue(0, 0);
        curMax = getEndXValue(0, itemCount - 1);
      } 

      if (curMin != min || curMax != max) {
        removeAllBins();

        if (max == min) {
          addBin(new SimpleHistogramBin(min, min + 1, true, true));
        } else {

          double interval = (max - min) / numBins;
          double start = min;
          for (int i = 0, n = numBins - 1; i < n; i++) {
            double end = start + interval;
            addBin(new SimpleHistogramBin(start, end, true, false));
            start = end;
          }

          // add the last bin
          addBin(new SimpleHistogramBin(start, max, true, true));
        }
      }

      clearObservations();
      for (int i = 0, n = buffer.size(); i < n; i++) {
        addObservation(buffer.getQuick(i), false);
      }

    } else {
      clearObservations();
    }
    notifyListeners = true;
    this.notifyListeners(new DatasetChangeEvent(this, this));

    min = Double.POSITIVE_INFINITY;
    max = Double.NEGATIVE_INFINITY;
  }
}
