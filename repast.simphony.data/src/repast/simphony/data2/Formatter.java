package repast.simphony.data2;

public interface Formatter {
  
  /**
   * Gets the delimiter used by this Formatter.
   * 
   * @return
   */
  public String getDelimiter();
  
  /**
   * Sets the delimiter used by this formatter.
   * 
   * @param delimiter
   */
  public void setDelimiter(String delimiter);

  /**
   * Gets the header, if any, approriate to this formatter.
   * 
   * @return the heaader, if any, approriate to this formatter.
   */
  String getHeader();

  /**
   * Clears this formatter of any data that has been added for formatting.
   */
  void clear();

  /**
   * Adds the specified object to the data to be formatted.
   * 
   * @param id
   *          the id of the data to add
   * @param obj
   *          the object to add
   */
  void addData(String id, Object obj);

  /**
   * Formats the data that has been added to this Formatter.
   * 
   * @return the formatted data.
   */
  String formatData();
  
  /**
   * Gets the type associated with this Formatter.
   * 
   * @return the type associated with this Formatter.
   */
  FormatType getFormatType();

}