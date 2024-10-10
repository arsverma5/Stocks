package oldstocksimulator.oldmodel.portfolioversions;
//portfolio subclass:

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.StockTransactions;

/**
 * DEPRECATED
 * This is the class for Portfolio. It includes the listOfStocks the user has purchased,
 * the name of the portfolio, and the name of the person who owns it.
 * It includes features that allow the user to view information about their portfolios
 * and the stocks within them. They can add/delete/view stocks/profiles.
 * They can use statistical trends to make decisions about investing in the future.
 *
 */
public class Portfolio implements IPortfolio {

  protected List<Stock> listOfStocks;
  protected final String ownerName;
  protected final String portfolioName;
  private List<StockTransactions> stockTransactions;


  /**
   * CHANGED CONSTRUCTOR:
   * the constructor for Portfolio.
   * @param portfolioName String
   * @param ownerName String
   */
  public Portfolio(String portfolioName, String ownerName) {
    this.ownerName = ownerName;
    this.portfolioName = portfolioName;
    this.listOfStocks = new ArrayList<>();
    this.stockTransactions = new ArrayList<>();
  }

  @Override
  public List<StockTransactions> getStockTransactions() {
    return stockTransactions;
  }

  public void setStockTransactions(List<StockTransactions> stockTransactions) {
    this.stockTransactions = stockTransactions;
  }

  /**
   * getAllClosingPriceValue() retrieves the value of all stocks in
   * the portfolio on a given date.
   * Allows users to track the closing price patterns to see
   * where the stock stands during major events,
   * holidays, weekends, etc.
   *
   * @param date Date
   * @return HashMap
   */
  public HashMap<String, String> getAllClosingPriceValue(Date date) throws IOException {
    HashMap<String, String> change = new HashMap<>();
    for (Stock stock : listOfStocks) {
      change.put(stock.getTickerSymbol(), "" + stock.getClosingPrice(date));
    }
    return change;
  }

  /**
   * getOwnerName() returns the owner's name.
   *
   * @return String
   */
  public String getOwnerName() {
    return ownerName;
  }

  /**
   * getPortfolioName() returns the portfolio's name.
   *
   * @return String
   */
  public String getPortfolioName() {
    return portfolioName;
  }

  /**
   * purchaseShares() buys a certain number of shares of stocks.
   * @param tickerSymbol String
   * @param numShares int
   */
  public void buyStocks(String tickerSymbol, int numShares) throws IOException {
    boolean checker = true;
    for (Stock stock : listOfStocks) {
      if (stock.getTickerSymbol().equals(tickerSymbol)) {
        checker = false;
        listOfStocks.set(listOfStocks.indexOf(stock), stock.increaseShares(numShares));
      }
    }

    if (checker) {
      listOfStocks.add(new Stock(tickerSymbol, numShares));
    }
  }

  /**
   * Sells a number of shares of a stock if it exists in this portfolio.
   * @param tickerSymbol String
   * @param numShares int
   */
  public void sellStocks(String tickerSymbol, int numShares) throws IOException {
    boolean checker = true;

    Iterator<Stock> iterator = listOfStocks.iterator();
    while (iterator.hasNext()) {
      Stock stock = iterator.next();
      if (stock.getTickerSymbol().equals(tickerSymbol)) {
        if (stock.getShares() <= numShares) {
          iterator.remove();
        }
        else {
          stock.decreaseShares(numShares);
        }
        checker = false;
      }
    }

    if (checker) {
      throw new IllegalArgumentException("Stock not in portfolio. ");
    }
  }

  @Override
  public Stock getStock(String tickerSymbol) {
    for (Stock stock : listOfStocks) {
      if (stock.getTickerSymbol().equals(tickerSymbol)) {
        return stock;
      }
    }

    throw new IllegalArgumentException("Stock not in portfolio. ");
  }

  /**
   * getListOfStocks() returns the list of stocks.
   *
   * @return List
   */
  public List<Stock> getListOfStocks() {
    return listOfStocks;
  }

  /**
   * getAllStockPriceChange() this retrieves all price changes during the given period of time.
   * for all stocks in this portfolio.
   *
   * @param startDate Date
   * @param endDate   Date
   * @return HashMap
   */
  public HashMap<String, String> getAllStockPriceChange(Date startDate, Date endDate)
          throws IOException {
    HashMap<String, String> change = new HashMap<>();
    for (Stock stock : listOfStocks) {
      change.put(stock.getTickerSymbol(), "" + stock.getPriceChange(startDate, endDate));
    }
    return change;
  }

  /**
   * getAllStockXDayMovingAverage() this retrieves all x day
   * averages during the given period of time.
   * for all stocks in this portfolio.
   *
   * @param date Date
   * @param x    int
   * @return HashMap
   */
  public HashMap<String, String> getAllStockXDayMovingAverage(Date date, int x)
          throws IOException {
    HashMap<String, String> change = new HashMap<>();
    for (Stock stock : listOfStocks) {
      change.put(stock.getTickerSymbol(), "" + stock.getXDayAverage(date, x));
    }
    return change;
  }

  /**
   * getAllStockXDayCrossover() this retrieves all x day crossovers
   * during the given period of time
   * for all stocks in this portfolio.
   *
   * @param startDate Date
   * @param endDate   Date
   * @param x         int
   * @return HashMap
   */
  public HashMap<String, String> getAllStockXDayCrossover(Date startDate, Date endDate, int x)
          throws IOException {
    HashMap<String, String> change = new HashMap<>();
    for (Stock stock : listOfStocks) {
      change.put(stock.getTickerSymbol(), stock.getXDayCrossovers(startDate, endDate, x));
    }
    return change;
  }


  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("Portfolio Name="
            + this.portfolioName + " Owner Name="
            + this.ownerName + "\n[listOfStocks]");
    for (Stock stock : listOfStocks) {
      result.append("\n").append(stock);
    }

    return result + "\n";
  }

  @Override
  public void setStock(String tickerSymbol, double numShares) throws IOException {
    boolean checker = true;
    for (Stock stock : listOfStocks) {
      if (stock.getTickerSymbol().equals(tickerSymbol)) {
        checker = false;
        stock.setShares(numShares);
        listOfStocks.set(listOfStocks.indexOf(stock), stock);
      }
    }

    if (checker) {
      listOfStocks.add(new Stock(tickerSymbol, numShares));
    }
  }
}