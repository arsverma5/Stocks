package betterstocksimulator.bettercontroller;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import betterstocksimulator.betterview.IBetterView;
import oldstocksimulator.oldmodel.Date;
import betterstocksimulator.bettermodel.IBetterModel;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;

/**
 * Represents a better stock controller.
 */
public class BetterStockControllerImpl extends AbstractStockController {

  /**
   * This is the constructor. It takes in the InputStream
   * and the IView as its arguments.
   *
   * @param model model
   * @param in    InputStream
   * @param view  IView
   */
  public BetterStockControllerImpl(IBetterModel model, Readable in, IBetterView view) {
    super(model, in, view);
  }

  @Override
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
        case "4":
          this.view.enter("ticker symbol");
          String tickerSymbol = sc.next();
          Stock s = this.model.stockForViewing(tickerSymbol);

          if (checkValidTickerSymbol(s)) {
            this.view.stockMenu(tickerSymbol);
            quit = this.stockController(tickerSymbol, sc, s);
          }
          else {
            this.view.menu();
            break;
          }

          break;
        case "5":
          this.view.enter("portfolio name");
          String portfolioName = sc.next();
          this.view.enter("owner name");
          String ownerName = sc.next();

          this.view.portfolioMenu(portfolioName);
          quit = this.portfolioController(portfolioName, ownerName, sc);
          break;
        case "1":
          this.createPortfolio(sc);
          this.view.menu();
          break;
        case "2":
          this.deletePortfolio(sc);
          this.view.menu();
          break;
        case "3":
          this.viewPortfolios(sc);
          this.view.menu();
          break;
        case "6":
          this.view.menu();
          break;
        case "7":
          this.view.farewellMessage();
          quit = true;
          break;
        default:
          this.view.undefinedInput("" + userInput);
          this.view.menu();
          break;
      }
    }
  }

  private void createPortfolio(Scanner sc) throws IOException {
    try {
      this.view.enter("portfolio name");
      String portFolioName = sc.next();
      this.view.enter("owner name");
      String ownerName = sc.next();
      this.model.addPortfolio(portFolioName, ownerName);
      this.view.createPortfolioMessage(portFolioName, ownerName);
    } catch (IllegalArgumentException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void deletePortfolio(Scanner sc) throws IOException {
    try {
      this.view.enter("portfolio name");
      String portFolioName = sc.next();
      this.view.enter("owner name");
      String ownerName = sc.next();
      boolean checker = this.model.removePortfolio(portFolioName, ownerName);
      this.view.deletePortfolioMessage(portFolioName, ownerName, checker);
    } catch (IllegalArgumentException e) {
      this.view.undefinedInput(e.getMessage());
    }
  }

  private void viewPortfolios(Scanner sc) {
    String output = this.model.viewPortfolios();
    this.view.viewPortfolioInfo(output);
  }

  // stock controller (same options as before, but calls IModel methods now)
  protected boolean stockController(String tickerSymbol, Scanner sc, Stock s) throws IOException {
    boolean quit = false;
    boolean mainQuit = false;
    //Stock s = this.betterModel.stockForViewing(tickerSymbol);

    while (!quit && sc.hasNext()) {
      // scans for user-instruction
      String userInput = sc.next();
      switch (userInput) {
        case "4":
          this.getClosingPrice(sc, s);
          this.view.stockMenu(tickerSymbol);
          break;
        case "1":
          this.getPriceChange(sc, s);
          this.view.stockMenu(tickerSymbol);
          break;
        case "2":
          this.getXDayAverage(sc, s);
          this.view.stockMenu(tickerSymbol);
          break;
        case "3":
          this.getXDayCrossover(sc, s);
          this.view.stockMenu(tickerSymbol);
          break;
        case "5":
          this.displayBarChartStock(sc, s);
          this.view.stockMenu(tickerSymbol);
          break;
        case "6":
          this.view.menu();
          quit = true;
          break;
        case "7":
          mainQuit = true;
          quit = true;
          this.view.farewellMessage();
          break;
        default:
          this.view.undefinedInput("" + userInput);
          this.view.stockMenu(tickerSymbol);
          break;
      }
    }
    return mainQuit;
  }

  // gets the closing price from the mode
  private void getClosingPrice(Scanner sc, Stock s) throws IOException {
    try {
      Date d = this.getUserDate(sc, "");
      this.view.printStatisticalData("closing price",
              "$" + this.model.getClosingPrice(d, s));
    } catch (Exception e) {
      this.view.undefinedInput(e.getMessage());
      this.view.stockMenu(s.getTickerSymbol());
    }
  }

  // gets the price change from the model
  private void getPriceChange(Scanner sc, Stock s) throws IOException {
    Date d1 = this.getUserDate(sc, "starting");
    Date d2 = this.getUserDate(sc, "ending");

    try {
      this.view.printStatisticalData("closing price",
              "$" + this.model.getPriceChange(d1, d2, s));
    } catch (Exception e) {
      this.view.undefinedInput(e.getMessage());
      this.view.stockMenu(s.getTickerSymbol());
    }
  }

  // gets the x day average from the model
  private void getXDayAverage(Scanner sc, Stock s) throws IOException {
    try {
      Date d = this.getUserDate(sc, "");
      this.view.enter("x days");
      int x = sc.nextInt();
      this.view.printStatisticalData("closing price",
              "$" + this.model.getXDayAverage(d, x, s));
    } catch (Exception e) {
      this.view.undefinedInput(e.getMessage());
      this.view.stockMenu(s.getTickerSymbol());
    }
  }

  // gets the x day crossover from the model
  private void getXDayCrossover(Scanner sc, Stock s) throws IOException {
    try {
      Date d1 = this.getUserDate(sc, "starting");
      Date d2 = this.getUserDate(sc, "ending");
      this.view.enter("x days");
      int x = sc.nextInt();
      this.view.printStatisticalData("closing price",
              this.model.getXDayCrossovers(d1, d2, x, s));
    } catch (Exception e) {
      this.view.undefinedInput(e.getMessage());
      this.view.stockMenu(s.getTickerSymbol());
    }
  }

  private void displayBarChartStock(Scanner sc, Stock s) throws IOException {
    Date d1 = this.getUserDate(sc, "starting");
    Date d2 = this.getUserDate(sc, "ending");

    List<String> timeStamps = this.calculateTimeScale(d1, d2);
    List<String> stockDates = new ArrayList<>();
    List<Double> stockValues = new ArrayList<>();

    for (String timeStamp : timeStamps) {
      Date current;
      double value;

      try {
        current = this.convertStringToDate(timeStamp);
        value = this.model.getClosingPrice(current, s);
        stockDates.add(timeStamp);
        stockValues.add(value);
      } catch (IllegalArgumentException ignored) {
      }
    }
    // calculate the scale of the bar chart
    double maxValue = stockValues.get(0);
    for (double val : stockValues) {
      if (val > maxValue) {
        maxValue = val;
      }
    }
    int maxAsterisks = 45;
    double scale = maxValue / maxAsterisks;
    view.displayBarChartStock(s.getTickerSymbol(), d1.toString(),
            d2.toString(), stockDates, stockValues, scale);
  }

  // the portfolio controller, which has additional better model
  private boolean portfolioController(String portfolioName,
                                      String ownerName, Scanner sc)
          throws IOException, ParseException {
    IPortfolio p = this.model.getPortfolio(portfolioName, ownerName);
    boolean quit = false;
    boolean mainQuit = false;
    Date d1;
    Date d2;

    while (!quit && sc.hasNext()) {
      // scans for user-instruction
      String userInput = sc.next();
      switch (userInput) {
        case "3":
          d1 = this.getUserDate(sc, "");
          this.view.displayComposition(this.model.viewPortfolioComposition(p, d1));
          this.view.portfolioMenu(portfolioName);
          break;
        case "4":
          d1 = this.getUserDate(sc, "");
          this.view.printStatisticalData("value of portfolio " + portfolioName,
                  "$" + this.model.getPortfolioValue(p, d1));
          this.view.portfolioMenu(portfolioName);
          break;
        case "5":
          d1 = this.getUserDate(sc, "");
          this.view.displayDistribution(this.model.portfolioDistributionValue(p, d1));
          this.view.portfolioMenu(portfolioName);
          break;
        case "6":
          int x = p.getListOfStocks().size();
          System.out.println(x);
          double[] percents = new double[x];

          for (int i = 0; i < x; i++) {
            this.view.enter("desired percentage of "
                    + p.getListOfStocks().get(i).getTickerSymbol());
            percents[i] = (sc.nextDouble());
          }

          d1 = this.getUserDate(sc, "");

          this.model.rebalanceAPortfolio(p, d1, percents);
          this.view.viewPortfolioInfo("Rebalancing: \n" + p);
          this.view.portfolioMenu(portfolioName);
          break;
        case "1":
          this.view.enter("ticker symbol");
          String tickerSymbol = sc.next();
          this.view.enter("number of shares");
          double numShares = sc.nextDouble();
          d1 = this.getUserDate(sc, "buy");

          this.model.purchaseShares(portfolioName, ownerName,
                  tickerSymbol, (int) numShares, d1);
          this.view.purchaseSharesMessage(tickerSymbol, (int) numShares, d1.toString());
          this.view.portfolioMenu(portfolioName);
          break;
        case "2":
          this.view.enter("ticker symbol");
          String tickerSymbolSell = sc.next();
          this.view.enter("number of shares");
          double numSharesSell = sc.nextDouble();
          d1 = this.getUserDate(sc, "buy");

          this.model.sellShares(portfolioName, ownerName,
                  tickerSymbolSell, numSharesSell, d1);
          this.view.sellSharesMessage(tickerSymbolSell, numSharesSell, d1.toString());
          this.view.portfolioMenu(portfolioName);
          break;
        case "7":
          this.displayBarChartPortfolio(p, sc);
          this.view.portfolioMenu(portfolioName);
          break;
        case "8":
          this.model.savePortfolio(p, "src/portfolios/" + portfolioName);
          this.view.viewPortfolioInfo("Saving portfolio: \n" + p.toString());
          this.view.portfolioMenu(portfolioName);
          break;
        case "9":
          this.model.loadPortfolio(p, "src/portfolios/" + portfolioName);
          this.view.viewPortfolioInfo("Loading portfolio: \n" + p.toString());
          this.view.portfolioMenu(portfolioName);
          break;
        case "10":
          d1 = this.getUserDate(sc, "");
          this.view.displayPortfolioStats(this.model.getAllClosingPriceValue(p, d1));
          this.view.portfolioMenu(portfolioName);
          break;
        case "11":
          d1 = this.getUserDate(sc, "starting");
          d2 = this.getUserDate(sc, "ending");
          this.view.displayPortfolioStats(this.model.getAllStockPriceChange(p, d1, d2));
          this.view.portfolioMenu(portfolioName);
          break;
        case "12":
          d1 = this.getUserDate(sc, "");
          this.view.enter("x days");
          x = sc.nextInt();
          this.view.displayPortfolioStats(this.model.getAllStockXDayMovingAverage(p, d1, x));
          this.view.portfolioMenu(portfolioName);
          break;
        case "13":
          d1 = this.getUserDate(sc, "starting");
          d2 = this.getUserDate(sc, "ending");
          this.view.enter("x days");
          x = sc.nextInt();
          this.view.displayPortfolioStats(this.model.getAllStockXDayCrossover(p, d1, d2, x));
          this.view.portfolioMenu(portfolioName);
          break;
        case "14":
          quit = true;
          this.view.menu();
          break;
        case "15":
          quit = true;
          mainQuit = true;
          this.view.farewellMessage();
          break;
        default:
          this.view.undefinedInput(userInput + "");
          this.view.portfolioMenu(portfolioName);
          break;
      }
    }

    return mainQuit;

  }

  // gets the user's inputted date
  private Date getUserDate(Scanner sc, String msg) throws IOException {
    this.view.enter(msg + " year");
    String year = sc.next();
    this.view.enter(msg + " month");
    String month = sc.next();
    this.view.enter(msg + " day");
    String day = sc.next();

    try {
      return new Date(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
    } catch (IllegalArgumentException e) {
      this.view.undefinedInput(e.getMessage());
      return this.getUserDate(sc, msg);
    }
  }

  private void displayBarChartPortfolio(IPortfolio p, Scanner sc) throws IOException {

    Date d1 = this.getUserDate(sc, "starting");
    Date d2 = this.getUserDate(sc, "ending");

    // here we call the calculateTimeScale method.
    // this gives us the list of strings of timestamps for the given date range.
    List<String> timeStamps = this.calculateTimeScale(d1, d2);
    List<String> portfolioDates = new ArrayList<>();
    List<Double> portfolioValues = new ArrayList<>();

    for (String timeStamp : timeStamps) {
      Date current;
      double value;

      try {
        current = this.convertStringToDate(timeStamp);
        value = model.getPortfolioValue(p, current);
        portfolioDates.add(timeStamp);
        portfolioValues.add(value);
      } catch (IllegalArgumentException ignored) {
      }
    }
    // calculate the scale of the bar chart
    double maxValue = portfolioValues.get(0);
    for (double val : portfolioValues) {
      if (val > maxValue) {
        maxValue = val;
      }
    }
    int maxAsterisks = 45;
    double scale = maxValue / maxAsterisks;
    view.displayBarChartPortfolio(p.getPortfolioName(), d1.toString(), d2.toString(),
            portfolioDates, portfolioValues, scale);
  }

  /**
   * generateTimeScale() generates the list of strings that contains the
   * time scales made from calculateTimeScale(). It adds the interval to
   * the first date as long as d1 is before d2.
   *
   * @param d1       Date
   * @param d2       Date
   * @param interval int
   * @return List
   */
  protected List<String> generateTimeScale(Date d1, Date d2, int interval) {
    List<String> timeStamps = new ArrayList<>();
    Date current = d1;
    while (current.compareTo(d2) < 0 || current.equalsTo(d2)) {
      timeStamps.add(current.toString());
      current = current.addDays(interval);
    }
    return timeStamps;
  }

  /**
   * calculateTimeScale() calculates the timescale based on the days between the dates
   * inputted as parameters. Depending on that, it checks the range it lies between
   * and will divide the timescale into either a year, quarterly, monthly, 10-days,
   * every second-day, or every day.
   *
   * @param d1 Date
   * @param d2 Date
   * @return List
   */
  protected List<String> calculateTimeScale(Date d1, Date d2) {
    long differenceInDays = d1.daysBetween(d2);
    List<String> timeStamps;
    if (differenceInDays > 365 * 5) {
      timeStamps = generateTimeScale(d1, d2, 365);
    } else if (differenceInDays > 90 * 5) {
      timeStamps = generateTimeScale(d1, d2, 90);
    } else if (differenceInDays > 30 * 5) {
      timeStamps = generateTimeScale(d1, d2, 30);
    } else if (differenceInDays > 10 * 5) {
      timeStamps = generateTimeScale(d1, d2, 10);
    } else if (differenceInDays > 2 * 5) {
      timeStamps = generateTimeScale(d1, d2, 2);
    } else {
      timeStamps = generateTimeScale(d1, d2, 1);
    }
    return timeStamps;
  }

  // checks for valid ticker symbol
  private boolean checkValidTickerSymbol(Stock s) {
    boolean checker = true;

    try {
      s.retrieveData(this.model.getAPICaller());
    } catch (IllegalArgumentException | IOException e) {
      checker = false;
      this.view.undefinedInput(e.getMessage());
    }

    return checker;
  }
}
