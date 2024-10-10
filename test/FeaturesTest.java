import org.junit.Before;
import org.junit.Test;

import betterstocksimulator.bettercontroller.gui.Features;
import betterstocksimulator.bettercontroller.gui.GUIControllerImpl;
import betterstocksimulator.bettermodel.BetterModelImpl;
import betterstocksimulator.bettermodel.IBetterModel;
import betterstocksimulator.betterview.gui.IBetterGUIView;
import betterstocksimulator.betterview.gui.GUIViewImpl;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This is the test class which tests the Features of this Swing UI program.
 * It tests creating/viewing portfolios, adding/selling shares,
 * and composition & value.
 */
public class FeaturesTest {
  private Features controller;
  private IBetterModel model;
  private Appendable logResult;

  @Before
  public void setUp() throws IOException {
    model = new BetterModelImpl();
    controller = new GUIControllerImpl(model);
    IBetterGUIView view = new GUIViewImpl(controller);
    controller.setView(view);
    logResult = new StringBuilder();
  }

  @Test
  public void testCreatePortfolioValid() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia&Thillai", this.model.getPortfolios().get(0).getOwnerName());
    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testCreatePortfolioAlreadyExists() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia&Thillai", this.model.getPortfolios().get(0).getOwnerName());
    assertEquals(logResult.toString(), controller.getLog().toString());

    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("An error occurred. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testCreatePortfolioDidNotInputBothFields() throws IOException {
    setUp();
    controller.createPortfolio("", "Arshia&Thillai");
    assertEquals("An error occurred. \n", controller.getLog().toString());
  }

  @Test
  public void testViewPortfolioValid() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.viewPortfolio("Portfolio1", "Arshia&Thillai");
    assertEquals(1, this.model.getPortfolios().size());
    logResult.append("Viewed successfully. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }


  @Test
  public void testViewPortfolioDidNotInputBothFields() throws IOException {
    setUp();
    controller.viewPortfolio("", "Arshia&Thillai");

    logResult.append("An error occurred. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());

    controller.viewPortfolio("Portfolio1", "");

    logResult.append("An error occurred. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());

    controller.viewPortfolio("", "");

    logResult.append("An error occurred. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testSavePortfolio() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.savePortfolio("Portfolio1", "Arshia&Thillai", "src/portfolios/a");
    logResult.append("Portfolio saved successfully. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testSavePortfolioDidNotInputBothFields() throws IOException {
    setUp();

    controller.savePortfolio("", "TestOwner", "src/portfolios/a");
    logResult.append("An error occurred. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());

    controller.savePortfolio("TestPortfolio", "", "src/portfolios/a");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testLoadPortfolioValid() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.savePortfolio("Portfolio1", "Arshia&Thillai", "src/portfolios/a");
    logResult.append("Portfolio saved successfully. \n");

    controller.loadPortfolio("Portfolio1", "Arshia&Thillai", "src/portfolios/a");
    logResult.append("Portfolio loaded successfully. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testLoadPortfolioDidNotSave() throws IOException {
    setUp();
    controller.loadPortfolio("NonexistentPortfolio", "TestOwner", "src/portfolios/a");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testLoadPortfolioDidNotInputBothFields() throws IOException {
    setUp();
    controller.loadPortfolio("", "TestOwner", "src/portfolios/a");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testPurchaseSharesValidDate() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai",
            "AAPL", "100", "2024", "6", "6");
    logResult.append("Purchased successfully. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testPurchaseSharesFutureDate() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai", "AAPL",
            "100", "2025", "6", "6");
    logResult.append("Future date entered. \n");
    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testPurchaseSharesInvalidDate() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai", "AAPL", "100",
            "2024", "6", "8");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testPurchaseSharesNegative() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai", "AAPL", "-100", "2024", "6", "6");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testSellSharesValidDate() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai", "AAPL",
            "100", "2024", "6", "6");
    logResult.append("Purchased successfully. \n");

    controller.sellShares("Portfolio1", "Arshia&Thillai", "AAPL",
            "50", "2024", "6", "7");
    logResult.append("Sold successfully. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());

  }

  @Test
  public void testSellSharesFutureDate() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai", "AAPL", "100", "2024", "6", "6");
    logResult.append("Purchased successfully. \n");

    controller.sellShares("Portfolio1", "Arshia&Thillai", "AAPL", "50", "2025", "6", "7");
    logResult.append("Future date entered. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testSellSharesInvalidDate() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.purchaseShares("Portfolio1", "Arshia&Thillai", "AAPL", "100",
            "2024", "6", "7");
    logResult.append("Purchased successfully. \n");

    controller.sellShares("Portfolio1", "Arshia&Thillai", "AAPL", "75",
            "2024", "6", "9");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());
  }

  @Test
  public void testSellSharesNegative() throws IOException {
    setUp();
    controller.createPortfolio("Portfolio1", "Arshia&Thillai");
    logResult.append("Created successfully. \n");

    controller.sellShares("Portfolio1", "Arshia&Thillai", "AAPL",
            "-50", "2024", "6", "7");
    logResult.append("An error occurred. \n");

    assertEquals(logResult.toString(), controller.getLog().toString());
  }


}
