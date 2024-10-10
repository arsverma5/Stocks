import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import betterstocksimulator.bettercontroller.BetterStockControllerImpl;
import betterstocksimulator.bettermodel.BetterModelImpl;
import betterstocksimulator.bettermodel.IBetterModel;
import betterstocksimulator.betterview.BetterViewImpl;
import betterstocksimulator.betterview.IBetterView;

import static org.junit.Assert.assertEquals;

/**
 * This test class will test the BetterStockControllerImpl as well as the view
 * implementations. Using mocks, it tests the controller and its methods
 * and cases for what the user may enter, and for the view it tests if the
 * output it prints/displays is correct.
 */
public class BetterStockControllerTest {

  private final StringBuilder expected =
          new StringBuilder("Welcome to the stock program!" + System.lineSeparator());
  private StringBuilder sb;
  private Readable input;
  private Appendable output;
  private IBetterModel model;
  private IBetterView view;

  @Before
  public void setUp() {
    sb = new StringBuilder();
    input = new StringReader("");
    output = new StringBuilder();
    model = new BetterModelImpl();
    view = new BetterViewImpl(output);
  }

  @Before
  public void setUpExpected() {
    expected.append("Menu: " + System.lineSeparator() +
            "Enter a number for a command, after which you will be asked for specific parameters: "
            + System.lineSeparator() +
            "1. create-portfolio <portfolioName> <ownerName> (create a new portfolio)"
            + System.lineSeparator() +
            "2. remove-portfolio <portfolioName> <ownerName> (remove an existing portfolio)"
            + System.lineSeparator() +
            "3. view-portfolios (view existing portfolios)" + System.lineSeparator() +
            "4. stock-menu <tickerSymbol> (access stock options)" + System.lineSeparator() +
            "5. portfolio-menu <portfolioName> <ownerName> (access portfolio options)"
            + System.lineSeparator() +
            "6. main-menu (print this menu again)" + System.lineSeparator() +
            "7. quit (quit the program)" + System.lineSeparator());
    expected.append("Enter your choice: \n");
  }

  @Test
  public void testWrongTickerSymbol() throws IOException, ParseException {
    setUp();
    sb.append("4 234h129j")
            .append(System.lineSeparator());
    expected.append("Enter ticker symbol: ");
    expected.append("Undefined Input: No price data for 234h129j")
            .append(System.lineSeparator());
    setUpExpected();

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  @Test
  public void setUpCreatePortfolio() throws IOException, ParseException {
    setUp();
    sb.append("1 MyPortfolio John")
            .append(System.lineSeparator());
    expected.append("Enter portfolio name: Enter owner name: ");
    expected.append("Creating portfolio MyPortfolio owned by John")
            .append(System.lineSeparator());
    setUpExpected();

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  @Test
  public void setUpRemovePortfolio() throws IOException, ParseException {
    setUp();
    sb.append("1 MyPortfolio John")
            .append(System.lineSeparator());
    expected.append("Enter portfolio name: Enter owner name: " +
                    "Creating portfolio MyPortfolio owned by John")
            .append(System.lineSeparator());
    setUpExpected();
    sb.append("2 MyPortfolio John")
            .append(System.lineSeparator());
    expected.append("Enter portfolio name: Enter owner name: " +
                    "Deleting portfolio MyPortfolio for John")
            .append(System.lineSeparator());
    setUpExpected();

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  @Test
  public void setUpViewPortfolios() throws IOException, ParseException {
    setUp();
    sb.append("1 MyPortfolio John")
            .append(System.lineSeparator());
    expected.append("Enter portfolio name: Enter owner name: " +
                    "Creating portfolio MyPortfolio owned by John")
            .append(System.lineSeparator());
    setUpExpected();
    sb.append("3")
            .append(System.lineSeparator());
    expected.append("Portfolio Name=MyPortfolio Owner Name=John\n" +
                    "[listOfStocks]\n")
            .append(System.lineSeparator());
    setUpExpected();

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  @Test
  public void setUpStockMenu() throws IOException, ParseException {
    setUp();
    sb.append("4 GOOG")
            .append(System.lineSeparator());
    expected.append("Enter ticker symbol: Stock Menu: GOOG\n")
            .append("Enter a number for a command, after which you will be " +
                    "asked for specific parameters: \n")
            .append("1. get-price-change start-date end-date\n")
            .append("2. get-x-day-moving-average start-date x\n")
            .append("3. get-x-day-crossover start-date end-date x\n")
            .append("4. get-closing-price date\n")
            .append("5. display-bar-chart\n")
            .append("6. main-menu\n")
            .append("7. quit\n")
            .append("Enter your choice: \n");

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  //TODO SIMPLE FIX:
  @Test
  public void setUpPortfolioMenu() throws IOException, ParseException {
    setUp();
    sb.append("1 MyPortfolio John")
            .append(System.lineSeparator());
    expected.append("Enter portfolio name: Enter owner name: ");
    expected.append("Creating portfolio MyPortfolio owned by John")
            .append(System.lineSeparator());
    setUpExpected();

    sb.append("5 MyPortfolio John")
            .append(System.lineSeparator());
    expected.append("Enter portfolio name: Enter owner name: ");
    expected.append("Portfolio Menu: MyPortfolio\n")
            .append("Enter a number of a command, after which you will " +
                    "be asked for specific parameters: \n")

            .append("1. purchase-shares ticker-symbol num-shares buy-date\n")
            .append("2. sell-shares ticker-symbol num-shares sell-date\n")
            .append("3. view-portfolio-composition date\n")
            .append("4. get-portfolio-value <date>\n")
            .append("5. get-portfolio-distribution <date>\n")
            .append("6. rebalance-portfolio <percentages> <date>\n")
            .append("7. display-bar-chart \n")
            .append("8. save-data\n")
            .append("9. load-data\n")
            .append("10. get-all-closing-price <start-date>\n")
            .append("11. get-all-stock-price-change <start-date> <end-date>\n")
            .append("12. get-all-x-day-moving-average <start-date> <x>\n")
            .append("13. get-all-x-day-crossover <start-date> <end-date> <x>\n")
            .append("14. main-menu\n")
            .append("15. quit\n")
            .append("Enter your choice: \n");

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  @Test
  public void setUpMainMenu() throws IOException, ParseException {
    setUp();
    sb.append("6")
            .append(System.lineSeparator());
    setUpExpected();

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }

  @Test
  public void setUpQuit() throws IOException, ParseException {
    setUp();
    sb.append("7").append(System.lineSeparator());
    expected.append("Thank you for using this program!\n");

    input = new StringReader(sb.toString());

    BetterStockControllerImpl controller = new BetterStockControllerImpl(model, input, view);
    controller.goController();

    assertEquals(expected.toString(), output.toString());
  }
}