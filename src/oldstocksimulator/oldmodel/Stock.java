package oldstocksimulator.oldmodel;

import java.io.IOException;
import java.util.List;

import sources.AlphaVantageDemo;

/**
 * This is the Stock class which implements its methods from IStock.
 * It represents a stock in the stock market, and its data
 * is retrieved using an api caller.
 */
public class Stock {

  protected final String tickerSymbol;
  protected double shares;
  protected List<StockInformation> listOfStocksInformation;
  protected IModel modelImpl;

  /**
   * This is the Stock constructor which takes in the tickerSymbol, the amount of shares.
   *
   * @param tickerSymbol String
   * @param shares       int
   */
  public Stock(String tickerSymbol, double shares) throws IOException {
    this.tickerSymbol = tickerSymbol;
    this.shares = shares;
    this.modelImpl = new ModelImpl();
  }

  /**
   * Creates a stock given the model impl.
   * @param tickerSymbol String
   * @param shares double
   * @param modelImpl IModel
   * @throws IOException exception
   */
  public Stock(String tickerSymbol, double shares, IModel modelImpl) throws IOException {
    this.tickerSymbol = tickerSymbol;
    this.shares = shares;
    this.modelImpl = modelImpl;
  }

  /**
   * Creates a new stock with no shares.
   *
   * @param tickerSymbol String
   */
  public Stock(String tickerSymbol, IModel modelImpl) throws IOException {
    this.tickerSymbol = tickerSymbol;
    this.shares = 0;
    this.modelImpl = modelImpl;
  }

  public void setShares(double shares) {
    this.shares = shares;
  }

  /**
   * Retrieves data from the API caller for this specific stock.
   * @throws IOException Exception
   */
  public void retrieveData(AlphaVantageDemo apiCaller) throws IOException {
    if (apiCaller == null) {
      throw new IllegalArgumentException("API caller has not been initialized");
    }

    try {
      String filePath = apiCaller.getCSVData(this.tickerSymbol);
      this.setData(filePath);
    } catch (IOException e) {
      throw e;
    }

  }

  /**
   * Sets this stock's data to the data in the given file path.
   * @param filePath String
   */
  public void setData(String filePath) {
    this.listOfStocksInformation = this.modelImpl.read(filePath, tickerSymbol);
  }

  // Checks if two dates are valid entries in the stock's data.
  private int[] checkValidDate(Date startDate, Date endDate) {
    int startIndex = -1;
    int endIndex = -1;

    for (StockInformation stockInformation : listOfStocksInformation) {
      if (stockInformation.getTimestamp().equalsTo(startDate)) {
        startIndex = listOfStocksInformation.indexOf(stockInformation);
      }
      if (stockInformation.getTimestamp().equalsTo(endDate)) {
        endIndex = listOfStocksInformation.indexOf(stockInformation);
      }
    }

    if (startIndex == -1 || endIndex == -1 || startIndex < endIndex) {
      throw new IllegalArgumentException("Invalid date range.");
    }

    return new int[] {endIndex, startIndex};
  }

  /**
   * Gets price change for a period of time.
   * @param startDate Date
   * @param endDate Date
   * @return double
   * @throws IOException exception
   */
  public double getPriceChange(Date startDate, Date endDate) throws IOException {
    int[] validDate = checkValidDate(startDate, endDate);
    return listOfStocksInformation.get(validDate[0]).getClosingPrice()
            - listOfStocksInformation.get(validDate[1]).getClosingPrice();
  }

  /**
   * Gets xday average for this date and x.
   * @param date Date
   * @param x int
   * @return double
   * @throws IOException exception
   */
  public double getXDayAverage(Date date, int x) throws IOException {
    double sum = 0;
    int count = findDateInStockInfoList(date);
    if (x < 0) {
      throw new IllegalArgumentException("Negative numbers are not allowed.");
    }

    if (x == 0) {
      return listOfStocksInformation.get(count).getClosingPrice();
    }

    for (int i = count; i < count + x; i++) {
      StockInformation stockInformation = this.listOfStocksInformation.get(i);
      if (stockInformation != null) {
        sum += stockInformation.getClosingPrice();
      }
    }

    return sum / x;
  }

  /**
   * Retrieves the index of the stock information that contains
   * all info for given date.
   * @param date Date
   * @return int
   */
  public int findDateInStockInfoList(Date date) {
    int count = -1;

    for (StockInformation stockInformation : listOfStocksInformation) {
      if (stockInformation.getTimestamp().equalsTo(date)) {
        count = listOfStocksInformation.indexOf(stockInformation);
      }
    }

    if (count == -1) {
      throw new IllegalArgumentException("Invalid date. " + date);
    }

    return count;
  }

  /**
   * Gets xday crossover for a given period of time and number of days.
   * @param startDate Date
   * @param endDate Date
   * @param x int
   * @return String
   * @throws IOException exception
   */
  public String getXDayCrossovers(Date startDate, Date endDate, int x) throws IOException {
    int[] validDate = checkValidDate(startDate, endDate);
    boolean checkCrossovers = true;

    for (int i = validDate[0]; i <= validDate[1]; i++) {
      StockInformation stockInformation = listOfStocksInformation.get(i);
      if (stockInformation != null) {
        double closingPrice = stockInformation.getClosingPrice();
        double average = this.getXDayAverage(stockInformation.getTimestamp(), x);
        if (closingPrice < average) {
          checkCrossovers = false;
          break;
        }
      }
    }

    if (checkCrossovers) {
      return "Yes";
    } else {
      return "No";
    }
  }

  /**
   * getShares() gets the amount of shares as an double.
   *
   * @return double
   */
  public double getShares() {
    return shares;
  }

  /**
   * getClosingPrice() retrieves the closing price for a specific date.
   *
   * @param date Date
   * @return double
   */
  public double getClosingPrice(Date date) throws IOException {
    int dateIndex = findDateInStockInfoList(date);
    return this.listOfStocksInformation.get(dateIndex).getClosingPrice();
  }

  /**
   * getTickerSymbol() gets the tickerSymbol as a String.
   *
   * @return String
   */
  public String getTickerSymbol() {
    return tickerSymbol;
  }

  /**
   * getTickerSymbol() gets the tickerSymbol as a String.
   *
   * @return String
   */
  public List<StockInformation> getListOfStocksInformation() {
    return listOfStocksInformation;
  }

  /**
   * Increases shares.
   * @param numShares int
   * @return Stock
   * @throws IOException exception
   */
  public Stock increaseShares(int numShares) throws IOException {
    return new Stock(this.tickerSymbol, this.shares + numShares);
  }

  /**
   * Decreases shares.
   * @param numShares int
   * @throws IOException exception
   */
  public void decreaseShares(int numShares) throws IOException {
    if (numShares >= this.shares) {
      this.shares = 0;
    }
    else {
      this.shares += numShares;
    }
  }

  @Override
  public String toString() {
    return "Symbol=" + this.tickerSymbol + ", shares=" + this.shares;
  }
}
