import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import betterstocksimulator.bettermodel.BetterModelImpl;
import betterstocksimulator.bettermodel.IBetterModel;
import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.StockTransactions;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;
import oldstocksimulator.oldmodel.portfolioversions.Portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Examples and tests for BetterModelImpl.
 * This class tests all the new methods of functionality.
 */
public class BetterModelTest {
  private IBetterModel model;

  @Before
  public void setUp() {
    model = new BetterModelImpl();
  }

  @Test
  public void testGetPortfolio() {
    IPortfolio portfolio = new Portfolio("Portfolio1", "Owner1");
    model.getPortfolios().add(portfolio);

    IPortfolio result = model.getPortfolio("Portfolio1", "Owner1");
    assertEquals(portfolio, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNonExistingPortfolio() {
    model.getPortfolio("NonExistingPortfolio", "Owner1");
  }

  @Test
  public void testPurchaseShares() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate = new Date(6, 6, 2024);

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate);
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);

    assertEquals(1, portfolio.getStockTransactions().size());
    assertEquals("AAPL", portfolio.getStockTransactions().get(0).getTickerSymbol());
    assertEquals(10, portfolio.getStockTransactions().get(0).getShares(), 0.001);
  }


  @Test
  public void testPurchaseSameStockTwiceAndDifferentStocks() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate1 = new Date(10, 6, 2024);
    Date buyDate2 = new Date(10, 6, 2024);

    // Purchase the same stock twice
    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate1);
    model.purchaseShares(portfolioName, ownerName, "AAPL", 5, buyDate2);

    // Purchase a different stock
    model.purchaseShares(portfolioName, ownerName, "GOOG", 20, buyDate1);

    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);
    List<StockTransactions> transactions = portfolio.getStockTransactions();

    assertEquals(3, transactions.size());

    // Verify the first AAPL purchase
    assertEquals("AAPL", transactions.get(0).getTickerSymbol());
    assertEquals(10, transactions.get(0).getShares(), 0.001);
    assertEquals(buyDate1, transactions.get(0).getPurchaseDate());

    // Verify the second AAPL purchase
    assertEquals("AAPL", transactions.get(1).getTickerSymbol());
    assertEquals(5, transactions.get(1).getShares(), 0.001);
    assertEquals(buyDate2, transactions.get(1).getPurchaseDate());

    // Verify the GOOG purchase
    assertEquals("GOOG", transactions.get(2).getTickerSymbol());
    assertEquals(20, transactions.get(2).getShares(), 0.001);
    assertEquals(buyDate1, transactions.get(2).getPurchaseDate());
  }


  @Test
  public void testSellShares() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate = new Date(15, 5, 2023);
    Date sellDate = new Date(3, 6, 2024);

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate);
    model.sellShares(portfolioName, ownerName, "AAPL", 5, sellDate);
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);

    assertEquals(2, portfolio.getStockTransactions().size());
    assertEquals(5, portfolio.getStockTransactions().get(1).getShares(), 0.001);
  }

  @Test
  public void testViewPortfolioComposition() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate = new Date(10, 6, 2024);

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate);
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);

    Map<String, Double> composition = model.viewPortfolioComposition(portfolio, buyDate);
    assertEquals(1, composition.size());
    assertTrue(composition.containsKey("AAPL"));
    assertEquals(10.0, composition.get("AAPL"), 0.001);
  }

  @Test
  public void testGetPortfolioValue() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate = new Date(6, 6, 2024);

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate);
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);

    double value = model.getPortfolioValue(portfolio, buyDate);
    double expectedValue = 10 * new Stock("AAPL", 10, model).getClosingPrice(buyDate);
    assertEquals(expectedValue, value, 0.001);
  }

  @Test
  public void testPortfolioDistributionValue() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate = new Date(10, 6, 2024);

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate);
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);
    portfolio.getListOfStocks().get(0).setData("test/testingAAPL.csv");

    Map<String, String> distributionValue =
            model.portfolioDistributionValue(portfolio, buyDate);
    assertEquals(1, distributionValue.size());
    assertTrue(distributionValue.containsKey("AAPL"));
    double expectedValue = 10 * new Stock("AAPL", 10, model).getClosingPrice(buyDate);
    assertEquals("" + expectedValue, distributionValue.get("AAPL"));
  }

  @Test
  public void testRebalanceAPortfolio() throws IOException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    Date buyDate = new Date(10, 6, 2024);
    double[] percentages = {0.5, 0.5};

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, buyDate);
    model.purchaseShares(portfolioName, ownerName, "GOOG", 10, buyDate);
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);

    model.rebalanceAPortfolio(portfolio, buyDate, percentages);
    double aaplShares = 10 * 193.12;
    double googShares = 10 * 176.63;
    double sum = aaplShares + googShares;
    double a = sum * 0.5;
    double g = sum * 0.5;
    double aResult = a / 193.12;
    double gResult = g / 176.63;
    assertEquals(aResult, portfolio.getListOfStocks().get(0).getShares(), 0.001);
    assertEquals(gResult, portfolio.getListOfStocks().get(1).getShares(), 0.001);
  }

  @Test
  public void testSaveAndLoadPortfolio() throws IOException, ParseException {
    String portfolioName = "Portfolio1";
    String ownerName = "Owner1";
    String filePath = "portfolio.json";

    model.purchaseShares(portfolioName, ownerName, "AAPL", 10, new Date(10, 6, 2024));
    IPortfolio portfolio = model.getPortfolio(portfolioName, ownerName);

    model.savePortfolio(portfolio, filePath);

    IPortfolio loadedPortfolio = new Portfolio(portfolioName, ownerName);
    model.loadPortfolio(loadedPortfolio, filePath);

    assertEquals(portfolio.getPortfolioName(), loadedPortfolio.getPortfolioName());
    assertEquals(portfolio.getOwnerName(), loadedPortfolio.getOwnerName());
    assertEquals(portfolio.getStockTransactions().size(),
            loadedPortfolio.getStockTransactions().size());
  }
}
