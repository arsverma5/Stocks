package betterstocksimulator.betterview.gui;

import betterstocksimulator.bettercontroller.gui.Features;

/**
 * The interface that contains all the methods for the view component
 * of the GUI MVC pattern.
 */
public interface IBetterGUIView {

  /**
   * Sets up features for proper communication.
   * @param features String
   */
  void setFeatures(Features features);

  /**
   * Displays a message in the display area.
   * @param message String
   */
  void displayMessage(String message);

  /**
   * Clears all input boxes.
   */
  void clearInput();

  /**
   * Clears the display area.
   */
  void clearDisplay();

  /**
   * Resets stock menu text field visibility.
   */
  void resetFieldsVisibility();

  /**
   * Resets the main menu visibility.
   */
  void mainMenuVisibility(boolean visible);

  /**
   * Displays the stock menu for a portfolio.
   */
  void stockMenu();
}
