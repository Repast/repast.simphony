package repast.simphony.chart2;

/**
 * Converts a Boolean into a 1 or 0.
 * 
 * @author Nick Collier
 */
class BooleanConverter implements DataConverter {

  @Override
  public double convert(Object obj) {
    return (Boolean) obj ? 1 : 0;
  }
}