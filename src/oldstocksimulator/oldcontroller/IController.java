package oldstocksimulator.oldcontroller;

import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Represents the controller for the Stock Simulator. Communicates
 * with the model and view of the simulator, gets the user's input
 * and retrieves model data and outputs that to the view.
 */
public interface IController {
  /**
   * goController() is the main controller that depending on the case
   * given, it tells the view what to output based on the data retrieved
   * from the model.
   */
  void goController() throws IllegalArgumentException, IOException, ParseException;

}
