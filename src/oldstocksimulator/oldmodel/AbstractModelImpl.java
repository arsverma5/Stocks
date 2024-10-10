package oldstocksimulator.oldmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;
import oldstocksimulator.oldmodel.portfolioversions.Portfolio;
import sources.AlphaVantageDemo;

/**
 * Represents the abstract model implementation of the IModel interface.
 * Contains all old code from part 1 (ModelImpl).
 */
public class AbstractModelImpl implements IModel {
  protected final List<IPortfolio> portfolios;
  protected final AlphaVantageDemo apiCaller;

  /**
   * Creates a new Model.
   */
  public AbstractModelImpl() {
    this.portfolios = new ArrayList<>();
    this.apiCaller = new AlphaVantageDemo();
  }

  @Override
  public AlphaVantageDemo getAPICaller() {
    return this.apiCaller;
  }

  /**
   * addPortfolio adds a portfolio to list of portfolios.
   *
   * @param portfolioName String
   * @param ownerName     String
   */
  public void addPortfolio(String portfolioName, String ownerName) {
    boolean checker = true;

    for (IPortfolio portfolio : this.portfolios) {
      if (portfolio.getPortfolioName().equals(portfolioName)
              && portfolio.getOwnerName().equals(ownerName)) {
        checker = false;
        break;
      }
    }

    if (checker) {
      this.portfolios.add(new Portfolio(portfolioName, ownerName));
    } else {
      throw new IllegalArgumentException("Portfolio already exists. ");
    }

  }

  /**
   * removes portfolio from list.
   *
   * @param portfolioName String
   * @param ownerName     String
   * @return boolean
   */
  public boolean removePortfolio(String portfolioName, String ownerName) {
    boolean checker = false;
    for (IPortfolio portfolio : this.portfolios) {
      if (portfolio.getOwnerName().equals(ownerName)
              && portfolio.getPortfolioName().equals(portfolioName)) {
        this.portfolios.remove(portfolio);
        checker = true;
        break;
      }
    }

    return checker;
  }

  /**
   * allows user to view their portfolios.
   *
   * @return String
   */
  public String viewPortfolios() {
    StringBuilder output = new StringBuilder();

    for (IPortfolio portfolio : this.portfolios) {
      output.append(portfolio.toString());
    }

    return output.toString();
  }

  /**
   * Creates a stock for viewing.
   *
   * @param tickerSymbol String
   * @return Stock
   */
  public Stock stockForViewing(String tickerSymbol) throws IOException {
    return new Stock(tickerSymbol, this);
  }

  /**
   * Reads the csv file and creates a list of stock information.
   *
   * @param csvFile      String
   * @param tickerSymbol String
   * @return List
   */
  public List<StockInformation> read(String csvFile, String tickerSymbol) {
    final String delimiter = ",";
    List<StockInformation> listOfStocksInformation = new ArrayList<>();
    try {
      File file = new File(csvFile);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line = "";

      // Skip the header row
      if ((line = br.readLine()) != null) {
        // Header row skipped
      }

      String[] tempArr;

      while ((line = br.readLine()) != null) {
        tempArr = line.split(delimiter);
        if (tempArr.length == 6) {
          StockInformation stockInformation = getStockInformation(tempArr);
          listOfStocksInformation.add(stockInformation);
        }
      }
      br.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return listOfStocksInformation;

  }

  // parses through a list of strings and returns a new StockInformation
  protected static StockInformation getStockInformation(String[] tempArr) {
    String[] partsOfDate = tempArr[0].split("-");
    int year = Integer.parseInt(partsOfDate[0]);
    int month = Integer.parseInt(partsOfDate[1]);
    int day = Integer.parseInt(partsOfDate[2]);
    Date timeStamp = new Date(day, month, year);
    double open = Double.parseDouble(tempArr[1]);
    double high = Double.parseDouble(tempArr[2]);
    double low = Double.parseDouble(tempArr[3]);
    double close = Double.parseDouble(tempArr[4]);
    double volume = Double.parseDouble(tempArr[5]);

    StockInformation stockInformation = new StockInformation(timeStamp, open,
            high, low, close, volume);
    return stockInformation;
  }

  @Override
  public List<IPortfolio> getPortfolios() {
    return this.portfolios;
  }

  @Override
  public double getPriceChange(Date startDate, Date endDate, Stock s) throws IOException {
    s.retrieveData(this.apiCaller);
    return s.getPriceChange(startDate, endDate);
  }

  @Override
  public double getXDayAverage(Date date, int x, Stock s) throws IOException {
    s.retrieveData(this.apiCaller);
    return s.getXDayAverage(date, x);
  }

  @Override
  public String getXDayCrossovers(Date startDate, Date endDate, int x, Stock s) throws IOException {
    s.retrieveData(this.apiCaller);
    return s.getXDayCrossovers(startDate, endDate, x);
  }

  @Override
  public double getClosingPrice(Date date, Stock s) throws IOException {
    s.retrieveData(this.apiCaller);
    return s.getClosingPrice(date);
  }
}
