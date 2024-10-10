import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.IModel;
import oldstocksimulator.oldmodel.ModelImpl;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Examples and tests for the Stock class.
 */
public class StockTest {
  private Stock stock1;
  private Stock stock2;
  private Stock stock3;

  private Date startDate1;
  private Date endDate1;
  private Date endDate2;
  private Date earliestDate;
  private Date futureDate;
  private Date weekendDate;
  private Date averageDate1;
  private Date invalidDate;

  private Stock finalStock;

  @Before
  public void setUp() throws IOException {
    IModel model = new ModelImpl();
    model.addPortfolio("Portfolio1", "Arshia Thillai");
    IPortfolio portfolio = model.getPortfolios().get(0);
    portfolio.buyStocks("AAPL", 100);
    portfolio.buyStocks("GOOG", 50);
    portfolio.buyStocks("MSFT", 75);

    stock1 = portfolio.getListOfStocks().get(0);
    stock1.setData("test/testingGOOG.csv");
    stock2 = portfolio.getListOfStocks().get(1);
    stock2.setData("test/testingGOOG.csv");
    stock3 = portfolio.getListOfStocks().get(2);
    stock3.setData("test/testingGOOG.csv");

    endDate1 = new Date(29, 5, 2024);
    endDate2 = new Date(24, 5, 2024);
    startDate1 = new Date(20, 5, 2024);
    earliestDate = new Date(26, 3, 2014);
    weekendDate = new Date(4, 5, 2024);
    averageDate1 = new Date(22, 5, 2024);
    invalidDate = new Date(25, 5, 2024);
  }

  @Test
  public void testInstantiation() throws IOException {
    setUp();
    assertEquals(100, stock1.getShares(), 0.001);
    assertEquals(50, stock2.getShares(), 0.001);
    assertEquals(75, stock3.getShares(), 0.001);

    assertEquals("AAPL", stock1.getTickerSymbol());
    assertEquals("GOOG", stock2.getTickerSymbol());
    assertEquals("MSFT", stock3.getTickerSymbol());
  }

  @Test
  public void testPriceChange() throws IOException {
    setUp();
    double difference = 177.4 - 178.46; //29th - 20th
    assertEquals(difference, stock2.getPriceChange(startDate1, endDate1), 0.001);
  }

  @Test
  public void testPriceChangeSameDate() throws IOException {
    setUp();
    double difference = 0;
    assertEquals(difference, stock2.getPriceChange(startDate1, startDate1), 0.001);
  }

  @Test
  public void testPriceChangeEndComesBeforeStartDate() throws IOException {
    setUp();
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock2.getPriceChange(endDate1, startDate1);
    });
    assertEquals("Invalid date range.", thrown.getMessage());

  }

  @Test
  public void testPriceChangeBeforeEarliestDate() throws IOException {
    setUp();
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock2.getPriceChange(earliestDate, startDate1);
    });
    assertEquals("Invalid date range.", thrown.getMessage());
  }

  @Test
  public void testPriceChangeAfterLatestDate() throws IOException {
    setUp();
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      stock2.getPriceChange(startDate1, futureDate);
    });
    assertEquals(null, thrown.getMessage());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testPriceChangeWeekend() throws IOException {
    setUp();
    double difference = 178.46 - 168.99; // may 20 - may 4 which is also may 3rds value
    assertEquals(difference, stock2.getPriceChange(weekendDate, startDate1), 0.001);
  }

  @Test
  public void testXDayMovingAverageSimple() throws IOException {
    setUp();
    double average = (178 + 179.54) / 2;
    assertEquals(average, stock2.getXDayAverage(averageDate1, 2), 0.001);
  }

  @Test
  public void testXDayMovingAverageNegativeDays() throws IOException {
    setUp();
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock2.getXDayAverage(weekendDate, -10);
    });
    assertEquals("Invalid date. 2024-05-04", thrown.getMessage());
  }

  @Test
  public void testXDayMovingAverageEarliestDays() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock2.getXDayAverage(earliestDate, 5);
    });
    assertEquals("Invalid date. 2014-03-26", thrown.getMessage());
  }

  @Test
  public void testXDayMovingAverageFutureDate() throws IOException {
    setUp();
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      stock2.getXDayAverage(futureDate, 10);
    });
    assertEquals(null, thrown.getMessage());
  }

  @Test
  public void testXDayMovingAverageZeroDays() throws IOException {
    setUp();
    assertEquals(178, stock2.getXDayAverage(averageDate1, 0), 0.001);
  }

  @Test
  public void testXDayMovingAverageWeekend() throws IOException {
    setUp();
    double average = (178 + 179.54 + 178.46 + 177.29 + 175.43) / 5; // may 22 and 5 days back
    assertEquals(average, stock2.getXDayAverage(averageDate1, 5), 0.001);
  }


  //CHANGED BELOW to "yes" -- not checked if it actually works tho:

  @Test
  public void testXDayCrossoverSimple() throws IOException {
    setUp();
    // may 20 to may 24
    // may 20th close (178.46) > 3 day average for may 20th
    assertEquals("No", stock2.getXDayCrossovers(startDate1, endDate2, 3));
  }

  @Test
  public void testXDayCrossover30Days() throws IOException {
    setUp();
    // may 20 to may 24
    // may 20th close (178.46) > 30 day average for may 20th
    assertEquals("Yes", stock2.getXDayCrossovers(startDate1, endDate2, 30));
  }

  @Test
  public void testXDayCrossoverSameStartAndEndDate() throws IOException {
    setUp();
    // may 20 to may 24
    // may 20th close (178.46) > 30 day average for may 20th
    assertEquals("Yes", stock2.getXDayCrossovers(startDate1, startDate1, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayCrossoverWeekend() throws IOException {
    setUp();
    stock2.getXDayCrossovers(new Date(11, 5, 2024), startDate1, 30);
    // may 10 to may 20
    // may 20th close (178.46) > 30 day average for may 20th
    //assertEquals("Yes", s);
  }

  @Test
  public void testXDayCrossoverSwitchedDates() throws IOException {
    setUp();
    // may 10 to may 20, 30
    // may 20th close (178.46) > 30 day average for may 20th
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock2.getXDayCrossovers(endDate1, startDate1, 30);
    });
    assertEquals("Invalid date range.", thrown.getMessage());
  }

  @Test
  public void testChangeSharesIncrease() throws IOException {
    setUp();
    stock1 = stock1.increaseShares(50);
    assertEquals("AAPL", stock1.getTickerSymbol());
    assertEquals(150, stock1.getShares(), 0.001);
  }

  @Test
  public void testDecreaseShares() throws IOException {
    stock1.decreaseShares(-30);
    assertEquals("AAPL", stock1.getTickerSymbol());
    assertEquals(70, stock1.getShares(), 0.001);
  }

  @Test
  public void testDecreaseSharesToZero() throws IOException {
    stock1.decreaseShares(100);
    assertEquals("AAPL", stock1.getTickerSymbol());
    assertEquals(0, stock1.getShares(), 0.001);
  }

  @Test
  public void testNoChangeInSharesIncrease() throws IOException {
    Stock newStock = stock1.increaseShares(0);
    assertEquals("AAPL", newStock.getTickerSymbol());
    assertEquals(100, newStock.getShares(), 0.001);
  }

  @Test
  public void testNoChangeInSharesDecrease() throws IOException {
    stock1.decreaseShares(0);
    assertEquals("AAPL", stock1.getTickerSymbol());
    assertEquals(100, stock1.getShares(), 0.001);
  }

  @Test
  public void testDecreaseSharesBelowZero() throws IOException {
    stock1.decreaseShares(150);
    assertEquals("AAPL", stock1.getTickerSymbol());
    assertEquals(0, stock1.getShares(), 0.001);
  }


  @Test
  public void testClosingPriceGoodDate() throws IOException {
    setUp();
    assertEquals(178.46, stock2.getClosingPrice(startDate1), 0.001);
  }

  @Test
  public void testGetClosingPriceInvalidDate() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      stock2.getClosingPrice(invalidDate); // not in csv
    });
    assertEquals("Invalid date. 2024-05-25", thrown.getMessage());
  }

}
