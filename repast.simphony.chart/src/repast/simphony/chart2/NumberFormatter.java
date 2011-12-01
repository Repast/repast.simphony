package repast.simphony.chart2;

/**
 * Converts an Object into a double by casting to Number.
 * 
 * @author Nick Collier
 */
class NumberConverter implements DataConverter {

  @Override
  public double convert(Object obj) {
    return ((Number) obj).doubleValue();
  }
}