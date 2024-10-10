import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.IModel;
import oldstocksimulator.oldmodel.ModelImpl;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;

import static org.junit.Assert.assertEquals;

/**
 * Examples and tests for Portfolio class.
 */
public class PortfolioTest {

  private IPortfolio portfolio;
  private Date testDate;
  private Date startDate;
  private Date endDate;


  @Before
  public void setUp() {
    IModel model = new ModelImpl();
    model.addPortfolio("Portfolio1", "Arshia Thillai");
    portfolio = model.getPortfolios().get(0);

    testDate = new Date(30, 5, 2024);
    startDate = new Date(28, 5, 2024);
    endDate = new Date(5, 6, 2024);
  }

  @Test
  public void testInstantiation() {
    setUp();
    assertEquals("Arshia Thillai", portfolio.getOwnerName());
    assertEquals("Portfolio1", portfolio.getPortfolioName());
  }

  @Test
  public void testBuyStock() throws IOException {
    setUp();
    assertEquals(new ArrayList<>(), portfolio.getListOfStocks());
    portfolio.buyStocks("AAPL", 100);
    assertEquals(1, portfolio.getListOfStocks().size());
    assertEquals("AAPL", portfolio.getListOfStocks().get(0).getTickerSymbol());
    assertEquals(100, portfolio.getListOfStocks().get(0).getShares(), 0.001);
  }

  @Test
  public void testBuyExistingStock() throws IOException {
    setUp();
    assertEquals(new ArrayList<>(), portfolio.getListOfStocks());
    portfolio.buyStocks("AAPL", 100);
    portfolio.buyStocks("AAPL", 100);
    assertEquals(1, portfolio.getListOfStocks().size());
    assertEquals("AAPL", portfolio.getListOfStocks().get(0).getTickerSymbol());
    assertEquals(200, portfolio.getListOfStocks().get(0).getShares(), 0.001);
  }

  @Test
  public void testSellStock() throws IOException {
    setUp();

    assertEquals(new ArrayList<>(), portfolio.getListOfStocks());
    portfolio.buyStocks("AAPL", 100);

    assertEquals(1, portfolio.getListOfStocks().size());
    assertEquals("AAPL", portfolio.getListOfStocks().get(0).getTickerSymbol());
    assertEquals(100, portfolio.getListOfStocks().get(0).getShares(), 0.001);

    portfolio.sellStocks("AAPL", -50);
    assertEquals(1, portfolio.getListOfStocks().size());
    assertEquals("AAPL", portfolio.getListOfStocks().get(0).getTickerSymbol());
    assertEquals(50, portfolio.getListOfStocks().get(0).getShares(), 0.001);
  }

  @Test
  public void testSellStockNumOverSharesOwned() throws IOException {
    setUp();
    assertEquals(new ArrayList<>(), portfolio.getListOfStocks());

    portfolio.buyStocks("AAPL", 100);
    assertEquals(1, portfolio.getListOfStocks().size());
    assertEquals("AAPL", portfolio.getListOfStocks().get(0).getTickerSymbol());
    assertEquals(100, portfolio.getListOfStocks().get(0).getShares(), 0.001);

    portfolio.sellStocks("AAPL", 150);
    assertEquals(0, portfolio.getListOfStocks().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockNotInPortfolio() throws IOException {
    setUp();
    assertEquals(new ArrayList<>(), portfolio.getListOfStocks());
    portfolio.buyStocks("AAPL", 100);
    assertEquals(1, portfolio.getListOfStocks().size());
    assertEquals("AAPL", portfolio.getListOfStocks().get(0).getTickerSymbol());
    assertEquals(100, portfolio.getListOfStocks().get(0).getShares(), 0.001);

    portfolio.sellStocks("GOOG", 50);
  }

  @Test
  public void testGetAllClosingPrice() throws IOException {
    setUp();
    portfolio.buyStocks("AAPL", 100);
    portfolio.getListOfStocks().get(0).setData("test/testingAAPL.csv");
    portfolio.buyStocks("GOOG", 50);
    portfolio.getListOfStocks().get(1).setData("test/testingGOOG.csv");

    HashMap<String, String> expected = new HashMap<>();
    expected.put("AAPL", "191.29");
    expected.put("GOOG", "173.56");

    HashMap<String, String> actualPrices = portfolio.getAllClosingPriceValue(testDate);
    assertEquals(expected, actualPrices);
  }

  //NEW
  @Test
  public void testGetAllStocksPriceChange() throws IOException {
    setUp();

    portfolio.buyStocks("AAPL", 100);
    portfolio.getListOfStocks().get(0).setData("test/testingAAPL.csv");
    portfolio.buyStocks("GOOG", 50);
    portfolio.getListOfStocks().get(1).setData("test/testingGOOG.csv");

    HashMap<String, String> expectedChanges = new HashMap<>();
    expectedChanges.put("AAPL", "5.8799999999999955"); // june 5 - may 28
    expectedChanges.put("GOOG", "-0.950000000000017"); // june 5 - may 28

    HashMap<String, String> actualChanges = portfolio.getAllStockPriceChange(startDate, endDate);
    assertEquals(expectedChanges, actualChanges);
  }

  //NEW
  @Test
  public void testGetAllStockXDayMovingAverage() throws IOException {
    setUp();
    portfolio.buyStocks("AAPL", 100);
    portfolio.getListOfStocks().get(0).setData("test/testingAAPL.csv");
    portfolio.buyStocks("GOOG", 50);
    portfolio.getListOfStocks().get(1).setData("test/testingGOOG.csv");

    int x = 3; // example of x-day period
    HashMap<String, String> expectedAverages = new HashMap<>();
    double appleAvg = (191.29 + 190.29 + 189.99) / x;
    double googAvg = (173.56 + 177.40 + 178.02) / x;
    expectedAverages.put("AAPL", "" + appleAvg);
    expectedAverages.put("GOOG", "" + googAvg);

    HashMap<String, String> actualAverages = portfolio.getAllStockXDayMovingAverage(testDate, x);
    assertEquals(expectedAverages, actualAverages);
  }

  //NEW
  @Test
  public void testGetAllStockXDayMovingAverageWeekend() throws IOException {
    setUp();
    portfolio.buyStocks("AAPL", 100);
    portfolio.getListOfStocks().get(0).setData("test/testingAAPL.csv");
    portfolio.buyStocks("GOOG", 50);
    portfolio.getListOfStocks().get(1).setData("test/testingGOOG.csv");

    int x = 5; // example of x-day period
    HashMap<String, String> expectedAverages = new HashMap<>();
    double appleAvg = (191.29 + 190.29 + 189.99 + 189.98 + 186.88) / x;
    double googAvg = (173.56 + 177.40 + 178.02 + 176.33 + 175.06) / x;
    expectedAverages.put("AAPL", "" + appleAvg);
    expectedAverages.put("GOOG", "" + googAvg);

    HashMap<String, String> actualAverages = portfolio.getAllStockXDayMovingAverage(testDate, x);
    assertEquals(expectedAverages, actualAverages);
  }

  @Test
  public void testGetAllStockXCrossover() throws IOException {
    setUp();

    portfolio.buyStocks("AAPL", 100);
    portfolio.getListOfStocks().get(0).setData("test/testingAAPL.csv");
    portfolio.buyStocks("GOOG", 50);
    portfolio.getListOfStocks().get(1).setData("test/testingGOOG.csv");

    int x = 10;
    HashMap<String, String> expectedCrossovers = new HashMap<>();
    expectedCrossovers.put("AAPL", "Yes");
    expectedCrossovers.put("GOOG", "No");

    // start = may 28 2024
    // end = june 5 2024
    HashMap<String, String> actualCrossovers = portfolio.getAllStockXDayCrossover(startDate,
            endDate, x);
    assertEquals(expectedCrossovers, actualCrossovers);
  }

  //NEW
  @Test
  public void testToString() throws IOException {
    setUp();

    List<Stock> listOfStocks = new ArrayList<>();
    portfolio.buyStocks("AAPL", 100);
    portfolio.buyStocks("GOOG", 75);

    String expected = "Portfolio Name=Portfolio1 Owner " +
            "Name=Arshia Thillai\n[listOfStocks]\nSymbol=AAPL, " +
            "shares=100.0\nSymbol=GOOG, shares=75.0\n";
    assertEquals(expected, portfolio.toString());
  }

}
