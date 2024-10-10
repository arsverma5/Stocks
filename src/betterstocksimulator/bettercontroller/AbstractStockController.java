package betterstocksimulator.bettercontroller;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;
import betterstocksimulator.bettermodel.IBetterModel;
import oldstocksimulator.oldcontroller.IController;
import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.Stock;
import betterstocksimulator.betterview.IBetterView;

/**
 * The abstract stock controller that implements IController.
 */
public class AbstractStockController implements IController {
  protected Readable in;
  protected IBetterView view;
  protected IBetterModel model;

  /**
   * This is the constructor. It takes in the InputStream
   * and the IView as its arguments.
   * @param in InputStream
   * @param view IView
   */
  public AbstractStockController(IBetterModel model, Readable in, IBetterView view) {
    this.view = view;
    this.in = in;
    this.model = model;
  }

  /**
   * goController() is the main controller that depending on the case
   * given, it tells the view what to output based on the data retrieved
   * from the model.
   */
  public void goController() throws IOException, ParseException {
    boolean quit = false;

    Scanner sc = new Scanner(this.in);

    // displays menu of options --> view
    this.view.welcomeMessage();
    this.view.menu();

    while (!quit && sc.hasNext()) {
      // scans for user-instruction
      String userInput = sc.next();

      switch (userInput) {
        case "create-portfolio":
          this.createPortfolio(sc);
          this.view.menu();
          break;
        case "remove-portfolio":
          this.deletePortfolio(sc);
          this.view.menu();
          break;
        case "view-portfolios":
          this.viewPortfolios(sc);
          this.view.menu();
          break;
        case "stock-menu":
          String tickerSymbol = sc.next();
          this.view.stockMenu(tickerSymbol);
          quit = this.stockController(tickerSymbol, sc);
          break;
        case "portfolio-menu":
          String portFolioName = sc.next();
          boolean checker = false;

          for (IPortfolio portfolio : this.model.getPortfolios()) {
            if (portfolio.getPortfolioName().equals(portFolioName)) {
              checker = true;
              this.view.portfolioMenu(portFolioName);
              quit = this.portfolioController(portfolio, sc);
            }
          }
          if (!checker) {
            this.view.undefinedInput("Portfolio not found. ");
          }
          break;
        case "main-menu":
          goController();
          break;
        case "quit":
          this.view.farewellMessage();
          quit = true;
          break;
        default:
          this.view.undefinedInput("");
          this.view.menu();
          break;
      }
    }
  }

  private void createPortfolio(Scanner sc) {
    String portFolioName = sc.next();
    String ownerName = sc.next();
    try {
      this.model.addPortfolio(portFolioName, ownerName);
      this.view.createPortfolioMessage(portFolioName, ownerName);
    } catch (IllegalArgumentException e) {
      this.view.undefinedInput(e.getMessage());
    }

  }

  private void deletePortfolio(Scanner sc) {
    String portFolioName = sc.next();
    String ownerName = sc.next();
    boolean checker = this.model.removePortfolio(portFolioName, ownerName);
    this.view.deletePortfolioMessage(portFolioName, ownerName, checker);
  }

  private void viewPortfolios(Scanner sc) {
    String output = this.model.viewPortfolios();
    this.view.viewPortfolioInfo(output);
  }

  // controller for stock!
  protected boolean stockController(String tickerSymbol, Scanner sc) throws IOException {
    boolean quit = false;
    boolean smQuit = false;

    while (!smQuit && sc.hasNext()) {
      String userInput = sc.next();
      switch (userInput) {
        case "get-price-change":
          this.getPriceChange(tickerSymbol, sc);
          this.view.stockMenu(tickerSymbol);
          break;
        case "get-x-day-moving-average":
          this.getXDayMovingAvg(tickerSymbol, sc);
          this.view.stockMenu(tickerSymbol);
          break;
        case "get-x-day-crossover":
          this.getXDayCrossover(tickerSymbol, sc);
          this.view.stockMenu(tickerSymbol);
          break;
        case "get-closing-price":
          this.getClosingPrice(tickerSymbol, sc);
          this.view.stockMenu(tickerSymbol);
          break;
        case "main-menu":
          this.view.menu();
          smQuit = true;
          break;
        case "quit":
          quit = true;
          smQuit = true;
          this.view.farewellMessage();
          break;
        default:
          this.view.undefinedInput("");
          this.view.stockMenu(tickerSymbol);
          this.stockController(tickerSymbol, sc);
          break;
      }
    }
    return quit;
  }

  private void getPriceChange(String tickerSymbol, Scanner sc) throws IOException {
    String startDate = sc.next();
    String endDate = sc.next();

    Date d1 = this.convertStringToDate(startDate);
    Date d2 = this.convertStringToDate(endDate);

    Stock stock1 = model.stockForViewing(tickerSymbol);

    try {
      stock1.retrieveData(this.model.getAPICaller());
      this.view.printStatisticalData("price change",
              "" + stock1.getPriceChange(d1, d2));
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void getXDayMovingAvg(String tickerSymbol, Scanner sc) throws IOException {
    String startDate = sc.next();
    int x = sc.nextInt();

    Date d = this.convertStringToDate(startDate);

    Stock stock1 = model.stockForViewing(tickerSymbol);

    try {
      stock1.retrieveData(this.model.getAPICaller());
      this.view.printStatisticalData((x + "-day moving average"),
              "" + stock1.getXDayAverage(d, x));
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void getXDayCrossover(String tickerSymbol, Scanner sc) throws IOException {
    String startDate = sc.next();
    String endDate = sc.next();
    int x = sc.nextInt();

    Date d1 = this.convertStringToDate(startDate);
    Date d2 = this.convertStringToDate(endDate);

    Stock stock1 = model.stockForViewing(tickerSymbol);

    try {
      stock1.retrieveData(this.model.getAPICaller());
      this.view.printStatisticalData((x + "-day crossover"),
              stock1.getXDayCrossovers(d1, d2, x));
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }

  }

  private void getClosingPrice(String tickerSymbol, Scanner sc) throws IOException {
    String startDate = sc.next();

    Date d1 = this.convertStringToDate(startDate);

    Stock stock1 = model.stockForViewing(tickerSymbol);
    stock1.retrieveData(this.model.getAPICaller());
    this.view.printStatisticalData("closing price",
            "" + stock1.getClosingPrice(d1));
  }

  // controller for portfolio!
  private boolean portfolioController(IPortfolio p, Scanner sc) throws IOException {
    boolean quit = false;
    boolean pmQuit = false;

    while (!pmQuit && sc.hasNext()) {
      String userInput = sc.next();
      switch (userInput) {
        case "buy-stock":
          this.buyStock(p, sc);
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "sell-stock":
          this.sellStock(p, sc);
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "view-stocks":
          this.view.viewPortfolioInfo(p.toString());
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "get-all-closing-price":
          this.getAllClosingPrice(p, sc);
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "get-all-stock-price-change":
          this.getAllStockPriceChange(p, sc);
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "get-all-x-day-moving-average":
          this.getAllXDayMovingAverage(p, sc);
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "get-all-x-day-crossover":
          this.getAllXDayCrossover(p, sc);
          this.view.portfolioMenu(p.getPortfolioName());
          break;
        case "main-menu":
          pmQuit = true;
          this.view.menu();
          break;
        case "quit":
          this.view.farewellMessage();
          pmQuit = true;
          quit = true;
          break;
        default:
          this.view.undefinedInput("");
          this.view.portfolioMenu(p.getPortfolioName());
          portfolioController(p, sc);
          break;
      }
    }
    return quit;
  }

  private void buyStock(IPortfolio p, Scanner sc) throws IOException {
    String tickerSymbol = sc.next();
    int numShares = sc.nextInt();

    p.buyStocks(tickerSymbol, numShares);
    String portfolioName = p.getPortfolioName();

    this.view.buyOrSellStockMessage(portfolioName, numShares, tickerSymbol, "Buying");
  }

  private void sellStock(IPortfolio p, Scanner sc) throws IOException {
    String tickerSymbol = sc.next();
    int numShares = - (sc.nextInt());

    p.sellStocks(tickerSymbol, numShares);
    String portfolioName = p.getPortfolioName();

    try {
      this.view.buyOrSellStockMessage(portfolioName, numShares, tickerSymbol, "Selling");
    } catch (IllegalArgumentException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void getAllClosingPrice(IPortfolio p, Scanner sc) {
    String startDate = sc.next();
    Date d1 = this.convertStringToDate(startDate);

    Map<String, String> m;

    try {
      m = p.getAllClosingPriceValue(d1);
      this.view.displayPortfolioStats(m);
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void getAllStockPriceChange(IPortfolio p, Scanner sc) {
    String startDate = sc.next();
    String endDate = sc.next();

    Date d1 = this.convertStringToDate(startDate);
    Date d2 = this.convertStringToDate(endDate);

    Map<String, String> m;

    try {
      m = p.getAllStockPriceChange(d1, d2);
      this.view.displayPortfolioStats(m);
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void getAllXDayMovingAverage(IPortfolio p, Scanner sc) {
    String startDate = sc.next();
    int x = sc.nextInt();

    Date d1 = this.convertStringToDate(startDate);

    Map<String, String> m;

    try {
      m = p.getAllStockXDayMovingAverage(d1, x);
      this.view.displayPortfolioStats(m);
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void getAllXDayCrossover(IPortfolio p, Scanner sc) {
    String startDate = sc.next();
    String endDate = sc.next();
    int x = sc.nextInt();

    Date d1 = this.convertStringToDate(startDate);
    Date d2 = this.convertStringToDate(endDate);

    Map<String, String> m;

    try {
      m = p.getAllStockXDayCrossover(d1, d2, x);
      this.view.displayPortfolioStats(m);
    } catch (IllegalArgumentException | IOException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  // converts a string to a date (given that it is in the correct format)
  protected Date convertStringToDate(String date) {
    int year = Integer.parseInt(date.substring(0, 4));
    int month = Integer.parseInt(date.substring(5, 7));
    int day = Integer.parseInt(date.substring(8, 10));

    return new Date(day, month, year);
  }

}
