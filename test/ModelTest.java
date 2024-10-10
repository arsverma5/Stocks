import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import oldstocksimulator.oldmodel.IModel;
import oldstocksimulator.oldmodel.ModelImpl;
import oldstocksimulator.oldmodel.portfolioversions.Portfolio;

import static org.junit.Assert.assertEquals;

/**
 * Examples and tests for Model interface.
 */
public class ModelTest {
  private IModel model;


  @Before
  public void setUp() {
    model = new ModelImpl();
  }

  @Test
  public void testInstantiation() {
    setUp();
    assertEquals(new ArrayList<>(), this.model.getPortfolios());
  }

  @Test
  public void testAddPortfolio() {
    setUp();
    this.model.addPortfolio("Portfolio1", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddExistingPortfolio() {
    setUp();
    this.model.addPortfolio("Portfolio1", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());

    this.model.addPortfolio("Portfolio1", "Arshia");
  }

  @Test
  public void testAddTwoPortfolios() {
    setUp();
    this.model.addPortfolio("Portfolio1", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());

    this.model.addPortfolio("Portfolio2", "Thillai");
    assertEquals(2, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());

    assertEquals("Portfolio2", this.model.getPortfolios().get(1).getPortfolioName());
    assertEquals("Thillai", this.model.getPortfolios().get(1).getOwnerName());
  }

  @Test
  public void testRemoveValidPortfolio() {
    setUp();
    List<Portfolio> resultList = new ArrayList<>();

    this.model.addPortfolio("Portfolio1", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());

    this.model.removePortfolio("Portfolio1", "Arshia");
    assertEquals(resultList, this.model.getPortfolios());
  }

  @Test
  public void testRemoveInvalidPortfolio() {
    setUp();
    List<Portfolio> resultList = new ArrayList<>();

    this.model.addPortfolio("Portfolio1", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());

    this.model.removePortfolio("Portfolio2", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());
  }

  @Test
  public void testViewPortfolios() {
    setUp();
    this.model.addPortfolio("Portfolio1", "Arshia");
    assertEquals(1, this.model.getPortfolios().size());
    assertEquals("Portfolio1", this.model.getPortfolios().get(0).getPortfolioName());
    assertEquals("Arshia", this.model.getPortfolios().get(0).getOwnerName());

    assertEquals("Portfolio Name=Portfolio1 Owner Name=Arshia\n[listOfStocks]\n",
            this.model.viewPortfolios());
  }

}
