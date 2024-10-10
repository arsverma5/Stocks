package betterstocksimulator.bettercontroller.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import betterstocksimulator.bettermodel.IBetterModel;
import betterstocksimulator.betterview.gui.IBetterGUIView;
import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;

/**
 * The class that implements Features. Acts as a controller
 * for the GUI, communicating back and forth with IBetterGUIView
 * and IBetterModel.
 */
public class GUIControllerImpl implements Features {
  private final IBetterModel model;
  private IBetterGUIView guiView;
  private Appendable log;

  /**
   * Constructs a new GUIControllerImpl.
   * @param model IBetterModel
   */
  public GUIControllerImpl(IBetterModel model) {
    this.model = model;
    this.log = new StringBuilder();
  }

  @Override
  public Appendable getLog() {
    return log;
  }

  @Override
  public void setView(IBetterGUIView gv) throws IOException {
    this.guiView = gv;
    this.guiView.setFeatures(this);
  }

  @Override
  public void createPortfolio(String portfolioName, String ownerName) throws IOException {

    if (portfolioName.isEmpty() || ownerName.isEmpty()) {
      this.guiView.displayMessage("Please enter both portfolio name and owner name.");
      log.append("An error occurred. \n");
      return;
    }

    try {
      this.model.addPortfolio(portfolioName, ownerName);
      this.guiView.displayMessage("Creating portfolio " + portfolioName + " owned by "
              + ownerName);
      log.append("Created successfully. \n");
    } catch (IllegalArgumentException | IOException e) {
      this.guiView.displayMessage(e.getMessage());
      log.append("An error occurred. \n");
    }
    this.guiView.clearInput();
    this.guiView.resetFieldsVisibility();
  }

  @Override
  public void viewPortfolio(String portfolioName, String ownerName) throws IOException {
    if (portfolioName.isEmpty() || ownerName.isEmpty()) {
      this.guiView.displayMessage("Please enter both portfolio name and owner name.");
      log.append("An error occurred. \n");
      return;
    }

    try {
      this.model.getPortfolio(portfolioName, ownerName);
      this.guiView.displayMessage("Viewing " + ownerName + "'s portfolio " + portfolioName + ".");
      this.guiView.clearDisplay();
      this.guiView.stockMenu();
      this.guiView.mainMenuVisibility(false);
      this.log.append("Viewed successfully. \n");
    } catch (IllegalArgumentException | IOException e) {
      this.guiView.displayMessage(e.getMessage());
      this.guiView.clearInput();
      this.log.append("An error occurred. \n");
    }

  }

  @Override
  public void purchaseShares(String portfolioName, String ownerName, String tickerSymbol,
                             String numShares, String year, String month, String day)
          throws IOException {

    if (tickerSymbol.isEmpty() || numShares.isEmpty()) {
      this.guiView.displayMessage("Please enter both ticker symbol and number of shares.");
      this.log.append("An error occurred. \n");
      return;
    }

    try {
      Double shares = Double.parseDouble(numShares);

      if (shares <= 0) {
        this.guiView.displayMessage("Please enter a positive number.");
        this.log.append("An error occurred. \n");
        return;
      }

      Date d = this.convertToDate(year, month, day);
      if (!this.checkIfFutureDate(d)) {
        this.model.purchaseShares(portfolioName, ownerName, tickerSymbol, shares, d);
        this.guiView.displayMessage("Purchased " + numShares + " shares of " + tickerSymbol
                + " on " + d);
        this.log.append("Purchased successfully. \n");
      }
      else {
        this.guiView.displayMessage(("Future date entered."));
        this.log.append("Future date entered. \n");
      }

    } catch (NumberFormatException e) {
      this.guiView.displayMessage("Please enter a valid number for shares.");
      this.log.append("An error occurred. \n");
    } catch (IllegalArgumentException e) {
      this.guiView.displayMessage(e.getMessage());
      this.log.append("An error occurred. \n");
    }
  }

  @Override
  public void sellShares(String portfolioName, String ownerName, String tickerSymbol,
                         String numShares, String year, String month, String day)
          throws IOException {

    if (tickerSymbol.isEmpty() || numShares.isEmpty()) {
      this.guiView.displayMessage("Please enter both ticker symbol and number of shares.");
      this.log.append("An error occurred. \n");
      return;
    }

    try {
      Double shares = Double.parseDouble(numShares);

      if (shares <= 0) {
        this.guiView.displayMessage("Please enter a positive number.");
        this.log.append("An error occurred. \n");
        return;
      }

      Date d = this.convertToDate(year, month, day);
      if (!this.checkIfFutureDate(d)) {
        this.model.sellShares(portfolioName, ownerName, tickerSymbol, shares, d);
        this.guiView.displayMessage("Sold " + numShares + " shares of " + tickerSymbol
                + " on " + d);
        this.log.append("Sold successfully. \n");
      }
      else {
        this.guiView.displayMessage(("Future date entered."));
        this.log.append("Future date entered. \n");
      }
    } catch (NumberFormatException e) {
      this.guiView.displayMessage("Please enter a valid number for shares.");
      this.log.append("An error occurred. \n");
    } catch (IllegalArgumentException e) {
      this.guiView.displayMessage(e.getMessage());
      this.log.append("An error occurred. \n");
    }
  }

  @Override
  public void getPortfolioComposition(String portfolioName, String ownerName,
                                      String year, String month, String day) throws IOException {
    try {
      Date d = this.convertToDate(year, month, day);
      if (!this.checkIfFutureDate(d)) {
        IPortfolio portfolio = this.model.getPortfolio(portfolioName, ownerName);
        if (portfolio == null) {
          this.guiView.displayMessage("Portfolio is null.");
          this.log.append("An error occurred.");
          return;
        }
        this.guiView.displayMessage("Portfolio Composition " + portfolioName + " owned by "
                + ownerName + " composed on " + d);


        Map<String, Double> m = this.model
                .viewPortfolioComposition(this.model.getPortfolio(portfolioName,
                ownerName), d);

        for (String key : m.keySet()) {
          this.guiView.displayMessage(key + ": " + m.get(key));
        }

        this.log.append("Composed successfully. \n");
      }
      else {
        this.guiView.displayMessage(("Future date entered."));
        this.log.append("Future date entered. \n");
      }
    } catch (IllegalArgumentException | IOException e) {
      this.guiView.displayMessage(e.getMessage());
      this.log.append("An error occurred. \n");
    }
  }

  @Override
  public void getPortfolioValue(String portfolioName, String ownerName,
                                String year, String month, String day) throws IOException {
    try {
      Date d = this.convertToDate(year, month, day);
      if (!this.checkIfFutureDate(d)) {
        Double num = this.model.getPortfolioValue(this.model.getPortfolio(portfolioName,
                        ownerName), this.convertToDate(year, month, day));
        this.guiView.displayMessage("Portfolio Value " + portfolioName + " owned by "
                + ownerName + " on " + d + ": " + num);
        this.log.append("Value retrieved successfully. \n");
      }
      else {
        this.guiView.displayMessage(("Future date entered."));
        this.log.append("Future date entered. \n");
      }
    } catch (IllegalArgumentException e) {
      this.guiView.displayMessage(e.getMessage());
      this.log.append("An error occurred. \n");
    }

  }

  @Override
  public void savePortfolio(String portfolioName, String ownerName, String filePath)
          throws IOException {
    try {
      IPortfolio portfolio = this.model.getPortfolio(portfolioName, ownerName);
      this.model.savePortfolio(portfolio, filePath);
      this.guiView.displayMessage("Portfolio saved successfully.");
      this.log.append("Portfolio saved successfully. \n");
    } catch (Exception e) {
      this.guiView.displayMessage(e.getMessage());
      this.log.append("An error occurred. \n");
    }
  }

  @Override
  public void loadPortfolio(String portfolioName, String ownerName, String filePath)
          throws IOException {
    try {
      IPortfolio portfolio = this.model.getPortfolio(portfolioName, ownerName);
      this.model.loadPortfolio(portfolio, filePath);
      this.guiView.displayMessage("Portfolio loaded successfully.");
      this.log.append("Portfolio loaded successfully. \n");
    } catch (Exception e) {
      this.guiView.displayMessage(e.getMessage());
      this.log.append("An error occurred. \n");
    }
  }

  private Date convertToDate(String year, String month, String day) {
    int newYear = Integer.parseInt(year);
    int newMonth = Integer.parseInt(month);
    int newDay = Integer.parseInt(day);

    return new Date(newDay, newMonth, newYear);
  }

  private boolean checkIfFutureDate(Date d) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime now = LocalDateTime.now();
    String nowStr = dtf.format(now);
    int nowYear = Integer.parseInt(nowStr.substring(0, 4));
    int nowMonth = Integer.parseInt(nowStr.substring(5, 7));
    int nowDay = Integer.parseInt(nowStr.substring(8, 10));

    Date nowDate = new Date(nowDay, nowMonth, nowYear);

    return nowDate.isBefore(d);
  }
}
