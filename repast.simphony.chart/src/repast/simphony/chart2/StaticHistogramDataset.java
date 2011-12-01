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

import java.util.Arrays;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.statistics.SimpleHistogramBin;


/**
 * Static histogram dataset for use with JFreeChart histograms.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class StaticHistogramDataset extends AbstractHistogramDataset {

  private class YHandler {
    public double getY(int series, int item) {
      return StaticHistogramDataset.super.getYValue(series, item);
    }
  }

  private class AddYHandler extends YHandler {
    public double getY(int series, int item) {
      double val = StaticHistogramDataset.super.getYValue(series, item);
      if (item == 0)
        return underFlow + val;
      else if (item == maxItemIndex)
        return overFlow + val;
      return val;
    }
  }

  private double min, max;
  private int overFlow, underFlow;
  private int maxItemIndex;
  private YHandler yHandler;

  public StaticHistogramDataset(Comparable<?> key, double[] edges, OutOfRangeHandling ooHandling) {
    super(key);
    Arrays.sort(edges);
    min = edges[0];
    max = edges[edges.length - 1];

    double start = 0;
    for (int i = 1; i < edges.length; i++) {
      boolean includeUpper = i == edges.length - 1;
      addBin(new SimpleHistogramBin(start, edges[i], true, includeUpper));
      start = edges[i];
    }
    maxItemIndex = getItemCount(0) - 1;
    initHandling(ooHandling);

  }

  public StaticHistogramDataset(Comparable<?> key, double min, double max, int numBins,
      OutOfRangeHandling ooHandling) {
    super(key);
    this.min = min;
    this.max = max;
    double interval = (max - min) / numBins;
    double start = min;
    for (int i = 0, n = numBins - 1; i < n; i++) {
      double end = start + interval;
      addBin(new SimpleHistogramBin(start, end, true, false));
      start = end;
    }
    // add the last bin
    addBin(new SimpleHistogramBin(start, max, true, true));
    maxItemIndex = getItemCount(0) - 1;
    initHandling(ooHandling);
  }

  private void initHandling(OutOfRangeHandling handling) {
    if (handling == OutOfRangeHandling.IGNORE || handling == OutOfRangeHandling.DISPLAY)
      yHandler = new YHandler();
    else if (handling == OutOfRangeHandling.ADD)
      yHandler = new AddYHandler();
  }

  @Override
  public void addValue(double val) {
    synchronized (buffer) {
      if (val < min)
        underFlow++;
      else if (val > max)
        overFlow++;
      else
        super.addValue(val);
    }
  }

  @Override
  public Number getY(int series, int item) {
    return new Double(getYValue(series, item));
  }

  @Override
  public double getYValue(int series, int item) {
    return yHandler.getY(series, item);
  }
  
  public int getOverflow() {
    return overFlow;
  }
  
  public int getUnderflow() {
    return underFlow;
  }

  @Override
  protected void doUpdate() {
    synchronized (buffer) {
      notifyListeners = false;
      clearObservations();
      for (int i = 0, n = buffer.size(); i < n; i++) {
        addObservation(buffer.getQuick(i), false);
      }
      notifyListeners = true;
      this.notifyListeners(new DatasetChangeEvent(this, this));
      underFlow = overFlow = 0;
    }
  }
}
