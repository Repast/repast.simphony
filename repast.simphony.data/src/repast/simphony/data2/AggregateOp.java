/**
 * 
 */
package repast.simphony.data2;

/**
 * Enumeration of the different types of aggregation operations that can 
 * be done on a numeric data source.
 * 
 * @author Nick Collier
 */
public enum AggregateOp {
  
  NONE() {
    public String toString() {
      return "None";
    }
  },
  
  COUNT() {
    public String toString() {
      return "Count";
    }
  },
  
  GEO_MEAN() {
    public String toString() {
      return "Geometric Mean";
    }
  },
  
  MAX() {
    public String toString() {
      return "Max";
    }
  },
  
  MEAN() {
    public String toString() {
      return "Mean";
    }
  },
  
  MIN() {
    public String toString() {
      return "Min";
    }
  },
  
  SECOND_MOMENT() {
    public String toString() {
      return "Second Central Moment";
    }
  },
  
  STD_DEV() {
    public String toString() {
      return "Standard Deviation";
    }
  },
  
  SUM() {
    public String toString() {
      return "Sum";
    }
    
  },
  
  SUM_LOGS() {
    public String toString() {
      return "Sum of the Logs";
    }
  },
  
  SUM_SQRS() {
    public String toString() {
      return "Sum of the Squares";
    }
  },
  
  VARIANCE() {
    public String toString() {
      return "Variance";
    }
  }
}
