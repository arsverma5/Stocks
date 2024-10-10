package oldstocksimulator.oldmodel;

import java.io.IOException;

/**
 * StockTransactions is the class that handles the features of the transactions
 * being made. It includes fields such as the ticker symbol, the number of shares,
 * and when the stock is purchased and sold on a date provided.
 */
public class StockTransactions {

  private final String tickerSymbol;
  private final Date purchaseDate;
  private final Date sellingDate;
  private double shares;

  /**
   * This is the first constructor that sets all of the fields to itself.
   *
   * @param tickerSymbol String
   * @param shares       double
   * @param purchaseDate Date
   * @param sellingDate  Date
   * @throws IOException exception
   */
  public StockTransactions(String tickerSymbol, double shares,
                           Date purchaseDate, Date sellingDate) throws IOException {
    this.tickerSymbol = tickerSymbol;
    this.shares = shares;
    this.purchaseDate = purchaseDate;
    this.sellingDate = sellingDate;
  }

  /**
   * this is the second constructor that checks if any of the dates are null
   * and then if not it will convert String to Date.
   *
   * @param tickerSymbol String
   * @param shares       double
   * @param purchaseDate Date
   * @param sellingDate  Date
   * @throws IOException exception
   */
  public StockTransactions(String tickerSymbol, double shares,
                           String purchaseDate, String sellingDate) throws IOException {
    this.tickerSymbol = tickerSymbol;
    this.shares = shares;

    if (purchaseDate != null) {
      this.purchaseDate = Date.convertStringToDate(purchaseDate);
    } else {
      this.purchaseDate = null;
    }

    if (sellingDate != null) {
      this.sellingDate = Date.convertStringToDate(sellingDate);
    } else {
      this.sellingDate = null;
    }

  }

  /**
   * getTickerSymbol gets the tickerSymbol.
   *
   * @return String
   */
  public String getTickerSymbol() {
    return tickerSymbol;
  }

  /**
   * getShares gets the shares.
   *
   * @return double
   */
  public double getShares() {
    return shares;
  }

  /**
   * setShares sets the shares.
   *
   * @param shares double
   */
  public void setShares(double shares) {
    this.shares = shares;
  }

  /**
   * getPurchaseDate gets the purchase date.
   *
   * @return Date
   */
  public Date getPurchaseDate() {
    return purchaseDate;
  }

  /**
   * getSellingDate gets the selling date.
   *
   * @return Date.
   */
  public Date getSellingDate() {
    return sellingDate;
  }
}

