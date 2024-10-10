package betterstocksimulator.betterview;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents a better view implementation that extends AbstractViewImpl.
 * Has the same functionality but added features/outputs include:
 * - bar chart
 * - being able to query value of a portfolio
 * - being able to query rebalancing a portfolio
 * - being able to query for distribution of a portfolio
 * - saving and loading portfolios
 */
public class BetterViewImpl extends AbstractViewImpl implements IBetterView {

  /**
   * Creates a BetterViewImpl.
   */
  public BetterViewImpl() {
    super();
  }

  /**
   * Creates a new BetterViewImpl with given InputStream.
   */
  public BetterViewImpl(Appendable out) {
    super(out);
  }

  @Override
  public void menu() {
    //print the UI
    writeMessage("Menu: ");
    writeMessage("Enter a number for a command, "
            + "after which you will be asked for specific parameters: ");
    writeMessage("1. create-portfolio <portfolioName> <ownerName> (create a new portfolio)");
    writeMessage("2. remove-portfolio <portfolioName> <ownerName> (remove an existing portfolio)");
    writeMessage("3. view-portfolios (view existing portfolios)");
    writeMessage("4. stock-menu <tickerSymbol> (access stock options)");
    writeMessage("5. portfolio-menu <portfolioName> <ownerName> (access portfolio options)");
    writeMessage("6. main-menu (print this menu again)");
    writeMessage("7. quit (quit the program)");
    writeMessage("Enter your choice: ");
  }

  @Override
  public void stockMenu(String tickerSymbol) {
    writeMessage("Stock Menu: " + tickerSymbol);
    writeMessage("Enter a number for a command, "
            + "after which you will be asked for specific parameters: ");
    writeMessage("1. get-price-change start-date end-date");
    writeMessage("2. get-x-day-moving-average start-date x");
    writeMessage("3. get-x-day-crossover start-date end-date x");
    writeMessage("4. get-closing-price date");
    writeMessage("5. display-bar-chart");
    writeMessage("6. main-menu");
    writeMessage("7. quit");
    writeMessage("Enter your choice: ");
  }

  @Override
  public void portfolioMenu(String portfolioName) {
    writeMessage("Portfolio Menu: " + portfolioName);
    writeMessage("Enter a number of a command, "
            + "after which you will be asked for specific parameters: ");
    writeMessage("1. purchase-shares ticker-symbol num-shares buy-date");
    writeMessage("2. sell-shares ticker-symbol num-shares sell-date");
    writeMessage("3. view-portfolio-composition date");
    writeMessage("4. get-portfolio-value <date>"); // new
    writeMessage("5. get-portfolio-distribution <date>"); // new
    writeMessage("6. rebalance-portfolio <percentages> <date>"); // new
    writeMessage("7. display-bar-chart ");
    writeMessage("8. save-data");
    writeMessage("9. load-data");
    writeMessage("10. get-all-closing-price <start-date>");
    writeMessage("11. get-all-stock-price-change <start-date> <end-date>");
    writeMessage("12. get-all-x-day-moving-average <start-date> <x>");
    writeMessage("13. get-all-x-day-crossover <start-date> <end-date> <x>");
    writeMessage("14. main-menu");
    writeMessage("15. quit");
    writeMessage("Enter your choice: ");
  }

  /**
   * Displays a message when the user purchases shares.
   * @param tickerSymbol String
   * @param numShares int
   * @param buyDate String
   */
  public void purchaseSharesMessage(String tickerSymbol, int numShares, String buyDate) {
    writeMessage("Purchased: " + numShares + " of " + tickerSymbol + " on " + buyDate);
  }

  /**
   * Displays a message when the user sells shares.
   * @param tickerSymbol String
   * @param numShares double
   * @param sellDate String
   */
  public void sellSharesMessage(String tickerSymbol, double numShares, String sellDate) {
    writeMessage("Sold: " + numShares + " of " + tickerSymbol + " on " + sellDate);
  }

  /**
   * Displays the given composition of a portfolio.
   * @param composition Map
   */
  public void displayComposition(Map<String, Double> composition) {
    writeMessage("Composition: ");
    for (String key : composition.keySet()) {
      writeMessage(key + ": " + composition.get(key));
    }
  }

  /**
   * Displays the distribution of a given portfolio.
   * @param distribution Map
   */
  public void displayDistribution(Map<String, String> distribution) {
    writeMessage("Distribution: ");
    for (String key : distribution.keySet()) {
      writeMessage(key + ": " + distribution.get(key));
    }
  }

  @Override
  public void enter(String text) throws IOException {
    this.out.append("Enter ").append(text).append(": ");
  }

  @Override
  public void displayBarChartPortfolio(String portfolioName, String startDate, String endDate,
                                       List<String> portfolioDates,
                                       List<Double> portfolioValues, double scale) {
    StringBuilder builder = new StringBuilder();
    builder.append("Performance of portfolio ").append(portfolioName).append(" from ")
            .append(startDate).append(" to ").append(endDate + "\n");

    int maxTimeStamps = 0;
    for (String timeStamp : portfolioDates) {
      if (timeStamp.length() > maxTimeStamps) {
        maxTimeStamps = timeStamp.length();
      }
    }

    for (int i = 0; i < portfolioDates.size(); i++) {
      String timeStamp = portfolioDates.get(i);
      double value = portfolioValues.get(i);
      int asterisksTotal = (int) Math.round(value / scale);
      builder.append(String.format("%-" + maxTimeStamps + "s", timeStamp)).append(": ");
      builder.append("*".repeat(Math.max(0, asterisksTotal)));
      builder.append("\n");
    }
    builder.append("\nScale: * = ").append(scale).append(" dollars\n");
    writeMessage(builder.toString());
  }

  @Override
  public void displayBarChartStock(String tickerSymbol, String startDate, String endDate,
                            List<String> stockDates, List<Double> stockValues, double scale) {
    StringBuilder builder = new StringBuilder();
    builder.append("Performance of ").append(tickerSymbol).append(" from ")
            .append(startDate).append(" to ").append(endDate).append("\n");

    int maxTimeStamps = 0;
    for (String timeStamp : stockDates) {
      if (timeStamp.length() > maxTimeStamps) {
        maxTimeStamps = timeStamp.length();
      }
    }

    for (int i = 0; i < stockDates.size(); i++) {
      String timeStamp = stockDates.get(i);
      double value = stockValues.get(i);
      int asterisksTotal = (int) Math.round(value / scale);
      builder.append(String.format("%-" + maxTimeStamps + "s", timeStamp)).append(": ");
      builder.append("*".repeat(Math.max(0, asterisksTotal)));
      builder.append("\n");
    }
    builder.append("\nScale: * = ").append(scale).append(" dollars\n");
    writeMessage(builder.toString());
  }

}
