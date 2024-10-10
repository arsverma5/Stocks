package betterstocksimulator.betterview;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import oldstocksimulator.oldview.IView;

/**
 * The abstract view implementation that implements the
 * interface IView. Contains all old implementations.
 */
public class AbstractViewImpl implements IView {
  protected final Appendable out;

  /**
   * Creates a new AbstractViewImpl with a blank PrintStream.
   */
  public AbstractViewImpl() {
    this.out = new PrintStream(System.out);
  }

  /**
   * Creates a new AbstractViewImpl with given InputStream.
   */
  public AbstractViewImpl(Appendable out) {
    this.out = out;
  }

  protected void writeMessage(String message) throws IllegalStateException {
    try {
      this.out.append(message).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public void welcomeMessage() {
    writeMessage("Welcome to the stock program!");
  }

  @Override
  public void menu() {
    //print the UI
    writeMessage("Menu: ");
    writeMessage("create-portfolio <portfolioName> <ownerName> (create a new portfolio)");
    writeMessage("remove-portfolio <portfolioName> <ownerName> (remove an existing portfolio)");
    writeMessage("view-portfolios (view existing portfolios)");
    writeMessage("stock-menu <tickerSymbol> (access stock options)");
    writeMessage("portfolio-menu <portfolioName> (access portfolio options)");
    writeMessage("main-menu (print this menu again)");
    writeMessage("quit (quit the program)");
    writeMessage("Enter your choice: ");
  }

  @Override
  public void createPortfolioMessage(String portfolioName, String ownerName) {
    writeMessage("Creating portfolio " + portfolioName + " owned by " + ownerName);
  }

  @Override
  public void deletePortfolioMessage(String portfolioName, String ownerName, boolean checker) {
    if (checker) {
      writeMessage("Deleting portfolio " + portfolioName + " for " + ownerName);
    } else {
      writeMessage("Portfolio " + portfolioName + " not found for " + ownerName);
    }
  }

  @Override
  public void printStatisticalData(String command, String output) {
    writeMessage("Calculated/retrieved " + command + " and the result was: " + output);
  }

  @Override
  public void buyOrSellStockMessage(String portfolioName, int numShares,
                                    String tickerSymbol, String command) {
    writeMessage(command + " " + numShares + " shares " + tickerSymbol
            + " to " + portfolioName + " to portfolio");
  }

  @Override
  public void displayPortfolioStats(Map<String, String> m) {
    for (Map.Entry<String, String> entry : m.entrySet()) {
      writeMessage(entry.getKey() + ": " + entry.getValue());
    }
  }

  @Override
  public void viewPortfolioInfo(String output) {
    writeMessage(output);
  }

  @Override
  public void farewellMessage() {
    writeMessage("Thank you for using this program!");
  }

  @Override
  public void undefinedInput(String msg) {
    writeMessage("Undefined Input: " + msg);
  }

  @Override
  public void stockMenu(String tickerSymbol) {
    writeMessage("Stock Menu: " + tickerSymbol);
    writeMessage("Please format dates as YYYY-MM-DD. ");
    writeMessage("get-price-change start-date end-date");
    writeMessage("get-x-day-moving-average start-date x");
    writeMessage("get-x-day-crossover start-date end-date x");
    writeMessage("get-closing-price date");
    writeMessage("main-menu");
    writeMessage("quit");
  }

  @Override
  public void portfolioMenu(String portfolioName) {
    writeMessage("Portfolio Menu: " + portfolioName);
    writeMessage("Please format dates as YYYY-MM-DD. ");
    writeMessage("buy-stock ticker-symbol num-shares");
    writeMessage("sell-stock ticker-symbol num-shares");
    writeMessage("view-stocks");
    writeMessage("get-all-closing-price start-date");
    writeMessage("get-all-stock-price-change start-date end-date");
    writeMessage("get-all-x-day-moving-average start-date x");
    writeMessage("get-all-x-day-crossover start-date end-date x");
    writeMessage("main-menu");
    writeMessage("quit");
  }

}
