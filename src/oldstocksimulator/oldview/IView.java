package oldstocksimulator.oldview;

import java.util.Map;

/**
 * Represents a view for the Stock Simulator. Communicates with controller
 * to display information about the user's current portfolios as well as
 * general stock information (from API). Includes methods to display
 * the menu of instructions as well as successful user actions.
 */
public interface IView {

  /**
   * Displays the welcome message.
   */
  void welcomeMessage();

  /**
   * Displays the main menu for the simulator, in which the user can:
   * - create a portfolio
   * - delete a portfolio
   * - view a portfolio
   * - view all portfolios
   * - view a stock (regardless of if that stock is in any of the user's
   * portfolios).
   * - display the main menu again
   * - quit the program
   */
  void menu();

  /**
   * Prints the stock menu for a stock given its ticker symbol.
   * The user can do any of the following, given that they correctly format
   * the dates as MM-DD-YYYY:
   * - get the price change over a given period of time
   * - get the x-day-moving average given a starting date and number of days
   * - get the x-day-crossover given two dates and number of days x
   * - get the closing price on a given date
   * - return to the main menu
   * - quit the program
   *
   * @param tickerSymbol String
   */
  void stockMenu(String tickerSymbol);

  /**
   * Prints the portfolio menu for a portfolio given its portfolio name.
   * The user can do any of the following, given that they correctly format
   * the dates as MM-DD-YYYY:
   * - buy a stock given the ticker symbol and number of shares the user would like to buy
   * - sell a given number of shares of a stock given its ticker symbol
   * - view all the basic stock info of all stocks in this portfolio
   * - get closing prices of all stocks
   * - get price changes of all stocks
   * - get x-day moving averages of all stocks
   * - get x-day-crossovers of all stocks
   * - return to the main menu
   * - quit the program
   *
   * @param portfolioName String
   */
  void portfolioMenu(String portfolioName);

  /**
   * Outputs a message when the user attempts to create a new portfolio.
   *
   * @param portfolioName String
   * @param ownerName     String
   */
  void createPortfolioMessage(String portfolioName, String ownerName);

  /**
   * Outputs a message when the user attempts to delete a portfolio.
   *
   * @param portfolioName String
   * @param ownerName     String
   * @param checker       String
   */
  void deletePortfolioMessage(String portfolioName, String ownerName, boolean checker);

  /**
   * Prints statistical data and the command that was given.
   *
   * @param command String
   * @param output  String
   */
  void printStatisticalData(String command, String output);

  /**
   * Outputs a message when the user attempts to buy or sell a stock in a portfolio.
   *
   * @param portfolioName String
   * @param numShares     int
   * @param tickerSymbol  String
   * @param command       String
   */
  void buyOrSellStockMessage(String portfolioName, int numShares,
                             String tickerSymbol, String command);

  /**
   * Prints the given stats of a portfolio.
   *
   * @param m map
   */
  void displayPortfolioStats(Map<String, String> m);

  /**
   * Can view the information of portfolio.
   *
   * @param output String
   */
  void viewPortfolioInfo(String output);

  /**
   * Prints a farewell message.
   */
  void farewellMessage();

  /**
   * Outputs a message when the user inputs an undefined instruction.
   *
   * @param msg String
   */
  void undefinedInput(String msg);
}

