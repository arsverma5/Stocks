package oldstocksimulator.oldmodel;

import java.io.IOException;
import java.util.List;

import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;
import sources.AlphaVantageDemo;

/**
 * This is the model interface that contains all of the
 * methods that compute statistical stuff. It allows the user to
 * add and remove and view their portfolios.
 */
public interface IModel {

  /**
   * Returns the api caller of this model.
   * @return AlphaVantageDemo
   */
  AlphaVantageDemo getAPICaller();

  /**
   * Adds a portfolio to this model, that can manage
   * stocks and view all of their information at once.
   * @param portfolioName String
   * @param ownerName String
   */
  void addPortfolio(String portfolioName, String ownerName);

  /**
   * Removes a portfolio from this model.
   * @param portfolioName String
   * @param ownerName String
   * @return boolean
   */
  boolean removePortfolio(String portfolioName, String ownerName);

  /**
   * Returns a string containing all the portfolio info;
   * all portfolios' names, owner names, and a list of each of their stocks.
   * @return String
   */
  String viewPortfolios();

  /**
   * Returns a stock that contains data retrieved using an API caller.
   *
   * @param tickerSymbol String
   * @return Stock
   */
  Stock stockForViewing(String tickerSymbol) throws IOException;

  /**
   * Reads a CSV file and parses it into a list of StockInformation.
   * @param csvFile String
   * @param tickerSymbol String
   * @return List
   */
  List<StockInformation> read(String csvFile, String tickerSymbol);

  /**
   * Retrieves the portfolios that are in this model.
   *
   * @return List
   */
  List<IPortfolio> getPortfolios();

  /**
   * getPriceChange retrieves price change given dates for a stock.
   *
   * @param startDate Date
   * @param endDate Date
   * @param s Stock
   * @return double
   */
  double getPriceChange(Date startDate, Date endDate, Stock s) throws IOException;

  /**
   * getXDayAverage retrieves the x day average given a date, x, and stock.
   * @param date Date
   * @param x int
   * @param s Stock
   * @return double
   */
  double getXDayAverage(Date date, int x, Stock s) throws IOException;

  /**
   * getXDayCrossovers retrieves the x day crossovers for a range of date, x, and a stock.
   * @param startDate Date
   * @param endDate Date
   * @param x int
   * @param s Stock
   * @return String
   */
  String getXDayCrossovers(Date startDate, Date endDate, int x, Stock s) throws IOException;

  /**
   * getClosingPrice retrieves the closing price given a date and a stock.
   * @param date Date
   * @param s Stock
   * @return double
   */
  double getClosingPrice(Date date, Stock s) throws IOException;

}
