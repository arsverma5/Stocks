import org.junit.Test;

import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.StockInformation;

import static org.junit.Assert.assertEquals;

/**
 * Examples and tests for Stock Information.
 */
public class StockInformationTest {

  @Test
  public void testGetClosingPrice() {
    Date timestamp = new Date(6, 6, 2024);
    StockInformation stockInfo = new StockInformation(timestamp, 100.0, 110.0,
            95.0, 105.0, 10000.0);
    assertEquals(105.0, stockInfo.getClosingPrice(), 0.01);
  }

  @Test
  public void testGetTimestamp() {
    Date timestamp = new Date(6, 6, 2024);
    StockInformation stockInfo = new StockInformation(timestamp, 100.0, 110.0,
            95.0, 105.0, 10000.0);
    assertEquals(timestamp, stockInfo.getTimestamp());
  }

  @Test
  public void testToString() {
    Date timestamp = new Date(6, 6, 2024);
    StockInformation stockInfo = new StockInformation(timestamp, 100.0, 110.0,
            95.0, 105.0, 10000.0);
    String expected = "StockInformation{timestamp=2024-06-06, open=100.0, high=110.0," +
            " low=95.0, close=105.0, volume=10000.0}";
    assertEquals(expected, stockInfo.toString());
  }
}
