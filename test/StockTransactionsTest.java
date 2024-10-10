import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.StockTransactions;

import static org.junit.Assert.assertEquals;

/**
 * This is the StockTransaction Test class that tests
 * examples and tests of the getters in the class.
 */

public class StockTransactionsTest {

  private StockTransactions stockTransactions;
  private Date date;
  private Date date2;

  @Before
  public void setUp() throws IOException {
    date = new Date(10, 6, 2024);
    date2 = new Date(12, 6, 2024);
    stockTransactions = new StockTransactions("AAPL", 5, date, date2);
  }

  @Test
  public void testTransactionGetters() {
    assertEquals("AAPL", stockTransactions.getTickerSymbol());
    assertEquals(5, stockTransactions.getShares(), 0.001);
  }

  @Test
  public void testTransactionGettersDate() {
    assertEquals(date, stockTransactions.getPurchaseDate());
    assertEquals(date2, stockTransactions.getSellingDate());
  }
}
