package oldstocksimulator.oldmodel.portfolioversions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.StockTransactions;

/**
 * Represents a portfolio in the stock simulator.
 * Allows for retrieval of stock transactions, portfolio information,
 * statistical analysis of portfolio, and bar chart.
 */
public interface IPortfolio {
  /**
   * Retrieves stock transactions.
   * @return List
   */
  List<StockTransactions> getStockTransactions();

  /**
   * Sets the stock transactions.
   * @param stockTransactions List
   */
  void setStockTransactions(List<StockTransactions> stockTransactions);

  /**
   * Retrieves all closing price values of all stocks in the portfolio.
   * @param date Date
   * @return Map
   * @throws IOException exception
   */
  HashMap<String, String> getAllClosingPriceValue(Date date) throws IOException;

  /**
   * Retrieves the portfolio name.
   * @return String
   */
  String getPortfolioName();

  /**
   * Retrieves the owner name.
   * @return String
   */
  String getOwnerName();

  /**
   * Buy stocks for this portfolio.
   * @param tickerSymbol String
   * @param numShares int
   * @throws IOException exception
   */
  void buyStocks(String tickerSymbol, int numShares) throws IOException;

  /**
   * Sells stocks from this portfolio.
   * @param tickerSymbol String
   * @param numShares int
   * @throws IOException exception
   */
  void sellStocks(String tickerSymbol, int numShares) throws IOException;

  /**
   * Gets a stock based on its ticker symbol.
   * @param tickerSymbol String
   * @return Stock
   * @throws IOException exception
   */
  Stock getStock(String tickerSymbol) throws IOException;

  /**
   * Retrieves the list of stocks.
   * @return List
   */
  List<Stock> getListOfStocks();

  /**
   * Gets all stock price changes for all stocks in this portfolio.
   * @param startDate Date
   * @param endDate Date
   * @return Map
   * @throws IOException exception
   */
  HashMap<String, String> getAllStockPriceChange(Date startDate, Date endDate) throws IOException;

  /**
   * Gets all stock moving averages for all stocks in this portfolio.
   * @param date Date
   * @param x int
   * @return Map
   * @throws IOException exception
   */
  HashMap<String, String> getAllStockXDayMovingAverage(Date date, int x) throws IOException;

  /**
   * Gets all stock crossovers for all stocks in this portfolio.
   * @param startDate Date
   * @param endDate Date
   * @param x int
   * @return Map
   * @throws IOException exception
   */
  HashMap<String, String> getAllStockXDayCrossover(Date startDate, Date endDate, int x)
          throws IOException;

  /**
   * Returns this portfolio in String format.
   * @return String
   */
  String toString();

  /**
   * Sets a stock in this portfolio to the given number of shares.
   * @param tickerSymbol String
   * @param numShares double
   * @throws IOException exception
   */
  void setStock(String tickerSymbol, double numShares) throws IOException;

}
