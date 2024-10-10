package oldstocksimulator.oldview;

import betterstocksimulator.betterview.AbstractViewImpl;

/**
 * An implementation of the IView interface, which implements
 * all methods of said interface.
 */
public class ViewImpl extends AbstractViewImpl {

  /**
   * The ViewImpl constructor calls its super class.
   */
  public ViewImpl() {
    super();
  }

  /**
   * Creates a new ViewImpl with given InputStream.
   */
  public ViewImpl(Appendable out) {
    super(out);
  }
}

