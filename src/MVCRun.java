import org.json.simple.parser.ParseException;

import betterstocksimulator.bettercontroller.BetterStockControllerImpl;
import betterstocksimulator.bettercontroller.gui.Features;
import betterstocksimulator.bettercontroller.gui.GUIControllerImpl;
import betterstocksimulator.bettermodel.BetterModelImpl;
import betterstocksimulator.bettermodel.IBetterModel;
import betterstocksimulator.betterview.BetterViewImpl;
import betterstocksimulator.betterview.IBetterView;
import betterstocksimulator.betterview.gui.GUIViewImpl;
import betterstocksimulator.betterview.gui.IBetterGUIView;
import oldstocksimulator.oldcontroller.IController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main program that runs the Stock Simulator.
 * Supports two types of user interfaces:
 * - text-based interface
 * - GUI-based interface
 */
public class MVCRun {
  private static Readable readable;
  private static PrintStream out;

  /**
   * The main program.
   * @param args String[]
   * @throws IOException exception
   */
  public static void main(String[] args) throws IOException, ParseException {
    out = System.out;

    if (args.length > 0) {
      if ("-text".equals(args[0])) {
        readable = new InputStreamReader(System.in);
        runTextBased();
      } else {
        out.println("Invalid command line argument.");
      }
    } else {
      runGUI();
    }
  }

  private static void runGUI() throws IOException {
    IBetterModel model = new BetterModelImpl();
    Features controller = new GUIControllerImpl(model);
    IBetterGUIView view = new GUIViewImpl(controller);
    controller.setView(view);
    GUIViewImpl.setDefaultLookAndFeelDecorated(false);
    GUIViewImpl frame = new GUIViewImpl(controller);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
      out.println(e.getMessage());
    } catch (Exception ignored) {
    }
  }

  private static void runTextBased() throws IOException, ParseException {
    IBetterModel model = new BetterModelImpl();
    IBetterView view = new BetterViewImpl();
    IController controller = new BetterStockControllerImpl(model, readable, view);
    controller.goController();
  }
}
