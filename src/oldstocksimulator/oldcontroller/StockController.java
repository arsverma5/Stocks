package oldstocksimulator.oldcontroller;

import betterstocksimulator.bettercontroller.AbstractStockController;
import betterstocksimulator.bettermodel.IBetterModel;
import betterstocksimulator.betterview.IBetterView;

/**
 * StockController implements itself from the IController interface.
 * In this class it controls what the view sees from getting the
 * information from the model. Depending on what the user inputs,
 * it acts based on that and does the necessary action.
 * Some examples include creating a portfolio, adding a stock, and
 * calculating statistical information.
 */
public class StockController extends AbstractStockController {

  /**
   * This is the constructor. It takes in the InputStream
   * and the IView as its arguments.
   *
   * @param model model
   * @param in    InputStream
   * @param view  IView
   */
  public StockController(IBetterModel model, Readable in, IBetterView view) {
    super(model, in, view);
  }
}
