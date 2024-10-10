package betterstocksimulator.bettermodel;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;
import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.IModel;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;

/**
 * The IBetterModel interface that has added functionality.
 * This interface supports the new methods of allowing the user to rebalance, save, load,
 * get, distribute, and buy/sell stocks from their portfolio.
 */
public interface IBetterModel extends IModel {

  /**
   * viewPortfolioComposition allows the user to see the stocks they own and the
   * number of shares they own as well. It is representes as a hashmap of the ticker symbol
   * company name and then the value of shares as a double.
   * @param p IPortfolio
   * @param date Date
   * @return Map
   */
  Map<String, Double> viewPortfolioComposition(IPortfolio p, Date date) throws IOException;

  /**
   * getPortfolioValue retrieves the portfolio's value on a specific date
   * (to be exact, the end of that day).
   * @param p IPortfolio
   * @param date Date
   * @return double
   * @throws IOException exception
   */
  double getPortfolioValue(IPortfolio p, Date date) throws IOException;

  /**
   * portfolioDistributionValue retrieves the hashmap of the tickersymbol's in their portfolio
   * and the value of the portfolio on a specific date  (to be exact, the end of that day).
   * @param p IPortfolio
   * @param date Date
   * @return Map
   * @throws IOException exception
   */
  Map<String, String> portfolioDistributionValue(IPortfolio p, Date date) throws IOException;

  /**
   * rebalanceAPortfolio rebalances the distributions of the stocks and their shares
   * within their portfolio. Depending on the weightage of doubles the user enters it will
   * sell or buy the shares of their respective stocks to get those desired percentages.
   * @param p IPortfolio
   * @param date Date
   * @param percentages double[]
   * @throws IOException exception
   */
  void rebalanceAPortfolio(IPortfolio p, Date date, double[] percentages) throws IOException;

  /**
   * savePortfolio saves the portfolio to files.
   * @param p IPortfolio
   * @param filePath String
   */
  void savePortfolio(IPortfolio p, String filePath) throws IOException;

  /**
   * loadPortfolio loads the portfolio to files.
   * @param p IPortfolio
   * @param filePath String
   */
  void loadPortfolio(IPortfolio p, String filePath) throws IOException, ParseException;

  /**
   * getPortfolio retrieves the given portfolio, throws an error if not in list.
   * @param portfolioName String
   * @param ownerName String
   * @return IPortfolio
   */
  IPortfolio getPortfolio(String portfolioName, String ownerName);


  /**
   * purchaseShares buys a certain number of shares on a particular date.
   * @param portfolioName String
   * @param ownerName String
   * @param tickerSymbol String
   * @param numShares double
   * @param buyDate Date
   * @throws IOException exception
   */
  void purchaseShares(String portfolioName, String ownerName, String tickerSymbol,
                      double numShares, Date buyDate) throws IOException;

  /**
   * Sells a number of shares of a stock if it exists in this portfolio on a particular date.
   * @param portfolioName String
   * @param ownerName String
   * @param tickerSymbol String
   * @param numShares double
   * @param sellDates Date
   * @throws IOException exception
   */
  void sellShares(String portfolioName, String ownerName, String tickerSymbol,
                  double numShares, Date sellDates) throws IOException;

  /**
   * getAllClosingPriceValue() retrieves the value of all stocks in
   * the portfolio on a given date.
   * Allows users to track the closing price patterns to see
   * where the stock stands during major events,
   * holidays, weekends, etc.
   *
   * @param date Date
   * @return Map
   */
  Map<String, String> getAllClosingPriceValue(IPortfolio p, Date date)
          throws IOException;

  /**
   * getAllStockPriceChange() this retrieves all price changes during the given period of time.
   * for all stocks in this portfolio.
   *
   * @param startDate Date
   * @param endDate   Date
   * @return Map
   */
  Map<String, String> getAllStockPriceChange(IPortfolio p, Date startDate, Date endDate)
          throws IOException;

  /**
   * getAllStockXDayMovingAverage() this retrieves all x day
   * averages during the given period of time.
   * for all stocks in this portfolio.
   *
   * @param date Date
   * @param x    int
   * @return Map
   */
  Map<String, String> getAllStockXDayMovingAverage(IPortfolio p, Date date, int x)
          throws IOException;

  /**
   * getAllStockXDayCrossover() this retrieves all x day crossovers
   * during the given period of time
   * for all stocks in this portfolio.
   *
   * @param startDate Date
   * @param endDate   Date
   * @param x         int
   * @return Map
   */
  Map<String, String> getAllStockXDayCrossover(IPortfolio p, Date startDate,
                                                   Date endDate, int x) throws IOException;

}
