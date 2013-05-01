/**
 * 
 */
package repast.simphony.data2;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import simphony.util.messages.MessageCenter;

/**
 * Creates aggregate DataSources. The data sources produced by this creator are
 * share a common statistical object such that calling get on one of these is a
 * record for all of them, until reset is called. Consequently, these assume
 * that the collection passed will not change between calls to get.
 * 
 * @author Nick Collier
 */
public class AggregateDSCreator {

  static interface Calculator {
    void calcStats(NonAggregateDataSource source, SummaryStatistics stats, Iterable<?> objs,
        int size);
  }

  static class NumberCalculator implements Calculator {

    public void calcStats(NonAggregateDataSource source, SummaryStatistics stats, Iterable<?> objs,
        int size) {

      if (stats.getN() == 0) {
        for (Object obj : objs) {
          stats.addValue(((Number) source.get(obj)).doubleValue());
        }
      }

    }
  }

  static class ObjectCalculator implements Calculator {

    private static MessageCenter msg = MessageCenter.getMessageCenter(ADS.class);

    String id;
    
    public ObjectCalculator(String id) {
      this.id = id;
    }

    public void calcStats(NonAggregateDataSource source, SummaryStatistics stats, Iterable<?> objs,
        int size) {
      try {
        if (stats.getN() == 0) {
          for (Object obj : objs) {
            stats.addValue(((Number) source.get(obj)).doubleValue());
          }
        }
      } catch (ClassCastException ex) {
        msg.error("Aggregate Data Source Error: Data Source '" + id + "' is non-numeric", ex);
      }
    }
  }

  static class BooleanCalculator implements Calculator {
    public void calcStats(NonAggregateDataSource source, SummaryStatistics stats, Iterable<?> objs,
        int size) {
      if (stats.getN() == 0) {
        for (Object obj : objs) {
          int val = (Boolean) source.get(obj) ? 1 : 0;
          stats.addValue(val);
        }
      }
    }
  }

  static abstract class ADS implements AggregateDataSource {

    private NonAggregateDataSource source;
    private SummaryStatistics stats = new SummaryStatistics();
    private String id;
    private Calculator calculator;

    /**
     * @param source
     * @param stats
     * @param id
     */
    public ADS(NonAggregateDataSource source, SummaryStatistics stats, String id) {
      Class<?> type = source.getDataType();
      if (type.equals(Boolean.class) || type.equals(boolean.class))
        calculator = new BooleanCalculator();
      else if (type.equals(Object.class))
        calculator = new ObjectCalculator(id);
      else
        calculator = new NumberCalculator();
      this.source = source;
      this.stats = stats;
      this.id = id;
    }

    @Override
    public Class<Double> getDataType() {
      return Double.class;
    }

    public String getId() {
      return id;
    }

    @Override
    public Object get(Iterable<?> objs, int size) {
      calculator.calcStats(source, stats, objs, size);
      return doGet();
    }

    @Override
    public void reset() {
      stats.clear();
    }

    public abstract Double doGet();

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSource#getSourceType()
     */
    @Override
    public Class<?> getSourceType() {
      return source.getSourceType();
    }
  }

  private NonAggregateDataSource source;
  private SummaryStatistics stats = new SummaryStatistics();

  /**
   * Creates a builder that will create aggregate data sources using the
   * specified DataSource as the source for the non-aggregate data that will
   * then be aggegated.
   * 
   * @param source
   *          the source for the non-aggregate data that will then be aggegated
   */
  public AggregateDSCreator(NonAggregateDataSource source) {
    this.source = source;
  }

  /**
   * Creates an aggregate data source that will sum across the collection of
   * objects passed into it. The id should be unique across the DataSet that
   * this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will sum across the collection of
   *         objects passed into it.
   */
  public AggregateDataSource createSumSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getSum();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the mean of the collection
   * of objects passed into it. The id should be unique across the DataSet that
   * this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the mean of the collection
   *         of objects passed into it.
   */
  public AggregateDataSource createMeanSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getMean();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the min of the collection of
   * objects passed into it. The id should be unique across the DataSet that
   * this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the min the collection of
   *         objects passed into it.
   */
  public AggregateDataSource createMinSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getMin();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the max of the collection of
   * objects passed into it. The id should be unique across the DataSet that
   * this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the max the collection of
   *         objects passed into it.
   */
  public AggregateDataSource createMaxSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getMax();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the standard deviation of
   * the collection of objects passed into it. The id should be unique across
   * the DataSet that this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the std dev the collection
   *         of objects passed into it.
   */
  public AggregateDataSource createStdDevSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getStandardDeviation();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the sum of the logs of the
   * collection of objects passed into it. The id should be unique across the
   * DataSet that this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the sum of the logs the
   *         collection of objects passed into it.
   */
  public AggregateDataSource createSumLogsSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getSumOfLogs();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the sum of the squares of
   * the collection of objects passed into it. The id should be unique across
   * the DataSet that this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the sum of the squares the
   *         collection of objects passed into it.
   */
  public AggregateDataSource createSumSquaresSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getSumsq();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the variance of the
   * collection of objects passed into it. The id should be unique across the
   * DataSet that this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the variance the collection
   *         of objects passed into it.
   */
  public AggregateDataSource createVarianceSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getVariance();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the geometric mean of the
   * collection of objects passed into it. The id should be unique across the
   * DataSet that this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the geometric mean the
   *         collection of objects passed into it.
   */
  public AggregateDataSource createGeoMeanSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getGeometricMean();
      }
    };
  }

  /**
   * Creates an aggregate data source that will get the second moment of the
   * collection of objects passed into it. The id should be unique across the
   * DataSet that this source is added to.
   * 
   * @param id
   *          the unique id of the data source.
   * 
   * @return an aggregate data source that will get the second moment the
   *         collection of objects passed into it.
   */
  public AggregateDataSource createSecondMomentSource(String id) {
    return new ADS(source, stats, id) {
      @Override
      public Double doGet() {
        return stats.getSecondMoment();
      }
    };
  }

  /**
   * Convenience method for creating a count data source that will return all
   * the objects of a particular type.
   * 
   * @param id
   *          the unique id of the data source
   * 
   * @return the created count data source
   */
  public AggregateDataSource createCountSource(String id) {
    return new CountDataSource(id, source.getSourceType());
  }

  /**
   * Creates an AggregateDataSource of the specified type with the specified id.
   * 
   * @param id
   * @param type
   * 
   * @return the created AggregateDataSource.
   */
  public AggregateDataSource createDataSource(String id, AggregateOp type) {

    switch (type) {
    case SUM:
      return createSumSource(id);
    case MEAN:
      return createMeanSource(id);
    case MIN:
      return createMinSource(id);
    case MAX:
      return createMaxSource(id);
    case COUNT:
      return createCountSource(id);
    case STD_DEV:
      return createStdDevSource(id);
    case SUM_LOGS:
      return createSumLogsSource(id);
    case SUM_SQRS:
      return createSumSquaresSource(id);
    case VARIANCE:
      return createVarianceSource(id);
    case GEO_MEAN:
      return createGeoMeanSource(id);
    case SECOND_MOMENT:
      return createSecondMomentSource(id);
    default:
      throw new IllegalArgumentException("Unknown AggregateType '" + type + "'.");
    }
  }
}
