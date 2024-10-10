package betterstocksimulator.bettercontroller.gui;

import java.io.IOException;
import betterstocksimulator.betterview.gui.IBetterGUIView;

/**
 * The interface features which contains all the methods
 * that the GUI user-interface (which are managed by the
 * GUI controller and view) is capable of executing.
 */
public interface Features {

  /**
   * Gets the log of the controller.
   * @return Appendable
   */
  Appendable getLog();

  /**
   * Creates a portfolio.
   * @param portfolioName String
   * @param ownerName String
   */
  void createPortfolio(String portfolioName, String ownerName) throws IOException;

  /**
   * Views a portfolio.
   * @param portfolioName String
   * @param ownerName String
   */
  void viewPortfolio(String portfolioName, String ownerName) throws IOException;

  /**
   * Purchase shares for a given portfolio of a given stock
   * on a given date.
   * @param portfolioName String
   * @param ownerName String
   * @param tickerSymbol String
   * @param numShares String
   * @param year String
   * @param month String
   * @param day String
   * @throws IOException exception
   */
  void purchaseShares(String portfolioName, String ownerName, String tickerSymbol,
                      String numShares, String year, String month, String day) throws IOException;

  /**
   * Sells shares from a given portfolio of a given stock
   * on a given date.
   * @param portfolioName String
   * @param ownerName String
   * @param tickerSymbol String
   * @param numShares String
   * @param year String
   * @param month String
   * @param day String
   * @throws IOException exception
   */
  void sellShares(String portfolioName, String ownerName,
                  String tickerSymbol, String numShares,
                  String year, String month, String day) throws IOException;

  /**
   * Gets the given portfolio's composition on a given date.
   * @param portfolioName String
   * @param ownerName String
   * @param year String
   * @param month String
   * @param day String
   */
  void getPortfolioComposition(String portfolioName, String ownerName,
                               String year, String month, String day) throws IOException;

  /**
   * Gets the given portfolio's value on a given date.
   * @param portfolioName String
   * @param ownerName String
   * @param year String
   * @param month String
   * @param day String
   * @throws IOException exception
   */
  void getPortfolioValue(String portfolioName, String ownerName,
                         String year, String month, String day) throws IOException;

  /**
   * Saves a portfolio locally.
   * @param portfolioName String
   * @param ownerName String
   * @param filePath String
   */
  void savePortfolio(String portfolioName, String ownerName, String filePath) throws IOException;

  /**
   * Loads a previously saved portfolio and saves all the stored information to this
   * portfolio.
   * @param portfolioName String
   * @param ownerName String
   * @param filePath String
   */
  void loadPortfolio(String portfolioName, String ownerName, String filePath) throws IOException;

  /**
   * Sets up the view for proper communication between
   * controller and view.
   * @param gv IBetterGUIView
   */
  void setView(IBetterGUIView gv) throws IOException;

}
