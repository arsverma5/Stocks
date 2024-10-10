package oldstocksimulator.oldmodel;

/**
 * This is the StockInformation class. It represents the features
 * of a stock. It includes the date, the open value, high price,
 * low value, closing price, and volume. This data is obtained
 * from the Alpha Vantage Demo.
 */
public class StockInformation {
  private final Date timestamp;
  private final double open;
  private final double high;
  private final double low;
  private final double close;
  private final double volume;

  /**
   * This is the constructor for StockInformation. It contains
   * all of the necessary fields from the API.
   * @param timestamp Date
   * @param open double
   * @param high double
   * @param low double
   * @param close double
   * @param volume double
   */
  public StockInformation(Date timestamp, double open, double high, double low,
                          double close, double volume) {
    this.timestamp = timestamp;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  /**
   * getClosingPrice() gets the closing price.
   * @return double
   */
  public double getClosingPrice() {
    return close;
  }

  /**
   * getTimeStamp() gets the date.
   * @return Date
   */
  public Date getTimestamp() {
    return timestamp;
  }


  /**
   * toString() returns the string value in an ordered fashion
   * of the fields in a list form.
   * @return String
   */
  @Override
  public String toString() {
    return "StockInformation{" +
            "timestamp=" + timestamp +
            ", open=" + open +
            ", high=" + high +
            ", low=" + low +
            ", close=" + close +
            ", volume=" + volume +
            '}';
  }
}

