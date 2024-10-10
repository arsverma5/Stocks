import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;

import betterstocksimulator.bettercontroller.BetterStockControllerImpl;
import betterstocksimulator.betterview.IBetterView;
import oldstocksimulator.oldcontroller.IController;
import betterstocksimulator.bettermodel.BetterModelImpl;
import betterstocksimulator.bettermodel.IBetterModel;
import betterstocksimulator.betterview.BetterViewImpl;

/**
 * OLD!
 * This is the main program that runs the Stock project.
 */
public class MVCExampleStockSimulator {
  /**
   * The main program that runs the program.
   * @param args String[]
   * @throws IOException exception
   */
  public static void main(String[] args) throws IOException, ParseException {
    IBetterModel model = new BetterModelImpl();
    Readable rd = new InputStreamReader(System.in);
    IBetterView view = new BetterViewImpl();
    IController controller = new BetterStockControllerImpl(model, rd, view);
    controller.goController();
  }
}