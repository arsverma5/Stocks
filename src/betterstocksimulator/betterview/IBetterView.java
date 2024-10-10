package betterstocksimulator.betterview;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import oldstocksimulator.oldview.IView;

/**
 * Represents the better view that extends the IView interface.
 * Has added functionality to support features of better model
 * that are communicated to this interface via better controller.
 */
public interface IBetterView extends IView {

  /**
   * Displays a message requesting the user to input a parameter,
   * given the parameter name.
   * @param text String
   * @throws IOException exception
   */
  void enter(String text) throws IOException;

  /**
   * Displays the bar chart of a portfolio.
   * @param portfolioName String
   * @param startDate String
   * @param endDate String
   * @param portfolioDates List
   * @param portfolioValues List
   * @param scale double
   */
  void displayBarChartPortfolio(String portfolioName, String startDate, String endDate,
                                List<String> portfolioDates,
                                List<Double> portfolioValues, double scale);

  /**
   * Displays the bar chart of a stock.
   * @param tickerSymbol String
   * @param startDate String
   * @param endDate String
   * @param stockDates List
   * @param stockValues List
   * @param scale double
   */
  void displayBarChartStock(String tickerSymbol, String startDate, String endDate,
                            List<String> stockDates, List<Double> stockValues, double scale);

  /**
   * Displays a message when the user purchases a stock.
   * @param tickerSymbol String
   * @param numShares int
   * @param buyDate String
   */
  void purchaseSharesMessage(String tickerSymbol, int numShares, String buyDate);

  /**
   * Displays a message when the user sells a stock.
   * @param tickerSymbol String
   * @param numShares double
   * @param sellDate String
   */
  void sellSharesMessage(String tickerSymbol, double numShares, String sellDate);

  /**
   * Displays the composition of a portfolio.
   * @param composition Map
   */
  void displayComposition(Map<String, Double> composition);

  /**
   * Displays the distribution of a portfolio.
   * @param distribution Map
   */
  void displayDistribution(Map<String, String> distribution);

}
