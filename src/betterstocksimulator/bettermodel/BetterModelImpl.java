package betterstocksimulator.bettermodel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oldstocksimulator.oldmodel.AbstractModelImpl;
import oldstocksimulator.oldmodel.Date;
import oldstocksimulator.oldmodel.Stock;
import oldstocksimulator.oldmodel.StockTransactions;
import oldstocksimulator.oldmodel.portfolioversions.IPortfolio;
import oldstocksimulator.oldmodel.portfolioversions.Portfolio;

/**
 * This class extends the ModelImpl class, but has updated functionality
 * to better work with the updated program.
 */
public class BetterModelImpl extends AbstractModelImpl implements IBetterModel {

  /**
   * Creates a new BetterModelImpl object.
   */
  public BetterModelImpl() {
    super();
  }

  @Override
  public IPortfolio getPortfolio(String portfolioName, String ownerName) {


    for (IPortfolio portfolio : this.portfolios) {
      if (portfolio.getPortfolioName().equals(portfolioName)
              && portfolio.getOwnerName().equals(ownerName)) {
        return portfolio;
      }
    }

    throw new IllegalArgumentException("Requested portfolio does not exist. ");
  }


  /**
   * purchaseShares() buys a certain number of shares of stocks.
   *
   * @param tickerSymbol String
   * @param numShares    int
   */
  public void purchaseShares(String portfolioName, String ownerName, String tickerSymbol,
                             double numShares, Date buyDate)
          throws IOException, IllegalArgumentException {
    if (numShares - ((int) numShares) != 0) {
      throw new IllegalArgumentException("Number of shares cannot be a type of double. ");
    }
    Stock s = new Stock(tickerSymbol, numShares);
    s.retrieveData(this.apiCaller);

    // throws an error if the buy date is invalid
    s.findDateInStockInfoList(buyDate);

    IPortfolio portfolioToAddTo = null;
    StockTransactions st = new StockTransactions(tickerSymbol, numShares, buyDate, null);
    for (IPortfolio p : this.getPortfolios()) {
      if (portfolioName.equals(p.getPortfolioName()) && ownerName.equals(p.getOwnerName())) {
        portfolioToAddTo = p;
      }
    }
    if (portfolioToAddTo == null) {
      portfolioToAddTo = new Portfolio(portfolioName, ownerName);
      this.getPortfolios().add(portfolioToAddTo);
    }
    portfolioToAddTo.buyStocks(tickerSymbol, (int) numShares);
    portfolioToAddTo.getStockTransactions().add(st);
  }

  /**
   * Sells a number of shares of a stock if it exists in this portfolio.
   *
   * @param tickerSymbol String
   * @param numShares    int
   */
  public void sellShares(String portfolioName, String ownerName, String tickerSymbol,
                         double numShares, Date sellDate)
          throws IOException, IllegalArgumentException {

    Stock stock = new Stock(tickerSymbol, numShares);
    stock.retrieveData(this.apiCaller);

    // throws an error if the sell date is invalid
    stock.findDateInStockInfoList(sellDate);

    IPortfolio portfolioToSellFrom = null;
    Stock stockToSellFrom = new Stock(tickerSymbol, numShares);
    StockTransactions st = new StockTransactions(tickerSymbol, numShares, null, sellDate);
    for (IPortfolio p : this.getPortfolios()) {
      if (portfolioName.equals(p.getPortfolioName()) && ownerName.equals(p.getOwnerName())) {
        portfolioToSellFrom = p;
      }
    }
    if (portfolioToSellFrom == null) {
      throw new IllegalArgumentException("Portfolio not found");
    }
    for (StockTransactions s : portfolioToSellFrom.getStockTransactions()) {
      if (s.getTickerSymbol().equals(tickerSymbol)) {
        if (numShares < 0) {
          numShares = 0;
        }
        double currentAmountOfShares = s.getShares();
        if (currentAmountOfShares < numShares) {
          if (currentAmountOfShares - numShares <= 0) {
            currentAmountOfShares -= currentAmountOfShares;
            s.setShares(currentAmountOfShares);
            numShares -= currentAmountOfShares;
          }
        } else {
          currentAmountOfShares -= numShares;
          s.setShares(currentAmountOfShares);
          numShares -= currentAmountOfShares;
        }
      }
    }
    portfolioToSellFrom.sellStocks(tickerSymbol, -(int) numShares);
    portfolioToSellFrom.getStockTransactions().add(st);
  }

  /**
   * viewPortfolioComposition() Views the composition of a given portfolio
   * on a given date.
   *
   * @param p    Portfolio
   * @param date Date
   * @return Map
   */
  public Map<String, Double> viewPortfolioComposition(IPortfolio p, Date date)
          throws IOException, IllegalArgumentException {
    Map<String, Double> composition = new HashMap<>();

    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      // throws an error if date is invalid
      stock.findDateInStockInfoList(date);
    }

    List<StockTransactions> stockTransactions = p.getStockTransactions();

    for (StockTransactions st : stockTransactions) {
      double shares = 0;
      String ticker = st.getTickerSymbol();

      // Check if purchase date is not null and is before or equal to the specified date
      Date purchaseDate = st.getPurchaseDate();
      if (purchaseDate != null) {
        if (!purchaseDate.after(date)) {
          Double sharesValue = st.getShares();
          if (sharesValue != null) {
            shares = sharesValue;
          }
        }
      }

      if (shares > 0) {
        if (composition.containsKey(ticker)) {
          composition.put(ticker, composition.get(ticker) + shares);
        } else {
          composition.put(ticker, shares);
        }
      }
    }
    return composition;
  }


  /**
   * getPortfolioValue() Retrieves a given portfolio's value (price per share
   * multiplied by number of shares).
   *
   * @param p    Portfolio
   * @param date Date
   * @return double
   */
  public double getPortfolioValue(IPortfolio p, Date date)
          throws IOException, IllegalArgumentException {

    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      // throws an error if date is invalid
      stock.findDateInStockInfoList(date);
    }

    // check if the date passed in is after the buyDate from StockTransactions.
    // if not then double = 0.
    double total = 0.0;

    List<StockTransactions> stockTransactions = p.getStockTransactions();
    for (StockTransactions s : stockTransactions) {
      if (s.getPurchaseDate() != null) {
        if (s.getPurchaseDate().isBefore(date)) {
          Stock newStock = p.getStock(s.getTickerSymbol());
          newStock.retrieveData(this.apiCaller);
          total += s.getShares() * newStock.getClosingPrice(date);
        }
      }

    }
    return total;
  }


  /**
   * portfolioDistributionValue() gets the distribution of value of a portfolio on a given date.
   * It includes the value of each stock in the portfolio.
   *
   * @param p    Portfolio
   * @param date Date
   * @return a Map
   */
  public Map<String, String> portfolioDistributionValue(IPortfolio p, Date date)
          throws IOException, IllegalArgumentException {

    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      // throws an error if date is invalid
      stock.findDateInStockInfoList(date);
    }

    Map<String, Double> stockCounts = this.viewPortfolioComposition(p, date);
    for (String st : stockCounts.keySet()) {
      double closingVal = 0.0;
      p.getStock(st).retrieveData(this.apiCaller);
      closingVal = p.getStock(st).getClosingPrice(date) * stockCounts.get(st);
      stockCounts.put(st, closingVal);
    }

    Map<String, String> result = new HashMap<>();
    for (String key : stockCounts.keySet()) {
      result.put(key, "$" + stockCounts.get(key));
    }

    return result;
  }


  /**
   * rebalanceAPortfolio() rebalances a portfolio according to the desired breakdown of weights
   * inside of an array. It will automatically buy and/or sell shares from the portfolio
   * to adjust the weightage that user desires their portfolio to have.
   *
   * @param p           Portfolio
   * @param date        Date
   * @param percentages double array
   */
  public void rebalanceAPortfolio(IPortfolio p, Date date, double[] percentages)
          throws IOException, IllegalArgumentException {

    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      // throws an error if date is invalid
      stock.findDateInStockInfoList(date);
    }

    // user can choose the weightings they want for each stock in the portfolio
    // and to redustribute the costs they would need to sell or buy and just do it.

    // get the total value of money of stocks in the portfolio
    double totalPortfolioValue = getPortfolioValue(p, date);
    // ticker symbol, current price of stock as hashMap:
    Map<String, String> currentValues = portfolioDistributionValue(p, date);
    // gets all the ticker symbols in a list:
    List<String> stocksList = new ArrayList<>(currentValues.keySet());

    // calculate the target value:
    for (int i = 0; i < percentages.length; i++) {
      String stock = stocksList.get(i);
      double targetValue = totalPortfolioValue * percentages[i];
      double currentValue = Double.parseDouble(currentValues.get(stock));
      double difference = targetValue - currentValue;
      Stock s = new Stock(stock, 0);
      s.retrieveData(this.apiCaller);
      double stockPrice = s.getClosingPrice(date);
      // if difference is + then buy additional shares to reach target val:
      if (difference > 0) { //TODO check if this is ok
        purchaseShares(p.getPortfolioName(), p.getOwnerName(),
                stock, (int) (difference / stockPrice), date);
      } else if (difference < 0) {
        sellShares(p.getPortfolioName(), p.getOwnerName(),
                stock, -difference / stockPrice, date);
      }
    }
  }

  /**
   * Saves a portfolio to the given file path.
   * @param p IPortfolio
   * @param filePath String
   */
  public void savePortfolio(IPortfolio p, String filePath) throws IOException {
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonStock = new JSONArray();
    JSONArray jsonStockTransactions = new JSONArray();
    jsonObject.put("PortfolioName", p.getPortfolioName());
    jsonObject.put("OwnerName", p.getOwnerName());


    // putting in the list of stocks
    for (int i = 0; i < p.getListOfStocks().size(); i++) {
      Stock s = p.getListOfStocks().get(i);
      JSONObject stockObject = new JSONObject();
      stockObject.put("tickerSymbol", s.getTickerSymbol());
      stockObject.put("shares", s.getShares());
      jsonStock.add(stockObject);
    }
    jsonObject.put("ListOfStocks", jsonStock);

    // putting in transaction history
    for (int i = 0; i < p.getStockTransactions().size(); i++) {
      StockTransactions st = p.getStockTransactions().get(i);

      JSONObject stockTransactionObject = new JSONObject();
      stockTransactionObject.put("tickerSymbol", st.getTickerSymbol());
      stockTransactionObject.put("shares", st.getShares());

      if (st.getPurchaseDate() == null && st.getSellingDate() != null) {
        stockTransactionObject.put("purchaseDate", null);
        stockTransactionObject.put("sellingDate", st.getSellingDate().toString());
      }
      else if (st.getSellingDate() == null && st.getPurchaseDate() != null) {
        stockTransactionObject.put("sellingDate", null);
        stockTransactionObject.put("purchaseDate", st.getPurchaseDate().toString());
      }
      else if (st.getPurchaseDate() != null && st.getSellingDate() == null) {
        stockTransactionObject.put("purchaseDate", st.getPurchaseDate().toString());
        stockTransactionObject.put("sellingDate", st.getSellingDate().toString());
      }


      jsonStockTransactions.add(stockTransactionObject);
    }
    jsonObject.put("StockTransactions", jsonStockTransactions);

    try {
      FileWriter file = new FileWriter(filePath);
      file.write(jsonObject.toJSONString() + "\n");
      file.close();
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Loads a portfolio from the given file path.
   * @param p IPortfolio
   * @param filePath String
   */
  public void loadPortfolio(IPortfolio p, String filePath) throws IOException, ParseException {
    JSONParser parser = new JSONParser();

    try {
      JSONObject obj = (JSONObject) parser
              .parse(new InputStreamReader(new FileInputStream(filePath)));

      Object stocksObj = obj.get("ListOfStocks");
      if (stocksObj instanceof JSONArray) {
        JSONArray stocks = (JSONArray) stocksObj;

        for (Object stockObj : stocks) {
          if (stockObj instanceof JSONObject) {
            JSONObject stockJSON = (JSONObject) stockObj;
            String tickerSymbol = (String) stockJSON.get("tickerSymbol");
            Double shares = (Double) stockJSON.get("shares");
            if (tickerSymbol != null && shares != null) {
              // Setting the number of shares for that stock to the value from
              // the saved portfolio
              p.setStock(tickerSymbol, shares);
            } else {
              throw new IllegalArgumentException("Invalid stock data: " + stockJSON);
            }
          } else {
            throw new IllegalArgumentException("Invalid stock object: " + stockObj);
          }
        }
      } else {
        throw new IllegalArgumentException("ListOfStocks is not a JSONArray: "
                + stocksObj);
      }

      Object stockTransactionObj = obj.get("StockTransactions");
      List<StockTransactions> listST = new ArrayList<>();
      if (stockTransactionObj instanceof JSONArray) {
        JSONArray stockTransactions = (JSONArray) stockTransactionObj;

        for (Object stockTransactionsObj : stockTransactions) {
          if (stockTransactionsObj instanceof JSONObject) {
            JSONObject stockJSON = (JSONObject) stockTransactionsObj;
            String tickerSymbol = (String) stockJSON.get("tickerSymbol");
            Double shares = (Double) stockJSON.get("shares");
            String purchaseDate = (String) stockJSON.get("purchaseDate");
            String sellingDate = (String) stockJSON.get("sellingDate");
            if (tickerSymbol != null && shares != null) {
              // Adding the stock transactions to the portfolio
              listST.add(new StockTransactions(tickerSymbol, shares,
                      purchaseDate, sellingDate));
            } else {
              throw new IllegalArgumentException("Invalid stock transaction data: "
                      + stockTransactions);
            }
          } else {
            throw new IllegalArgumentException("Invalid stock transaction object: "
                    + stockTransactionsObj);
          }
        }
        p.setStockTransactions(listST);
      } else {
        throw new IllegalArgumentException("StockTransactions is not a JSONArray: "
                + stockTransactionObj);
      }
    } catch (IOException | ParseException e) {
      throw e;
    }
  }


  @Override
  public Map<String, String> getAllClosingPriceValue(IPortfolio p, Date date)
          throws IOException {
    Map<String, String> change = new HashMap<>();
    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      change.put(stock.getTickerSymbol(), "$" + stock.getClosingPrice(date));
    }
    return change;
  }

  @Override
  public Map<String, String> getAllStockPriceChange(IPortfolio p,
                                                        Date startDate, Date endDate)
          throws IOException {
    Map<String, String> change = new HashMap<>();
    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      change.put(stock.getTickerSymbol(), "$" + stock.getPriceChange(startDate, endDate));
    }
    return change;
  }

  @Override
  public Map<String, String> getAllStockXDayMovingAverage(IPortfolio p, Date date, int x)
          throws IOException {
    Map<String, String> change = new HashMap<>();
    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      change.put(stock.getTickerSymbol(), "$" + stock.getXDayAverage(date, x));
    }
    return change;
  }

  @Override
  public Map<String, String> getAllStockXDayCrossover(IPortfolio p, Date startDate,
                                                          Date endDate, int x)
          throws IOException {
    Map<String, String> change = new HashMap<>();
    for (Stock stock : p.getListOfStocks()) {
      stock.retrieveData(this.apiCaller);
      change.put(stock.getTickerSymbol(), stock.getXDayCrossovers(startDate, endDate, x));
    }
    return change;
  }
}

