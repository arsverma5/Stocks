package betterstocksimulator.betterview.gui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.io.IOException;
import java.util.Calendar;

import betterstocksimulator.bettercontroller.gui.Features;

/**
 * The implementation of the IBetterGUIView interface.
 * Supports Graphical User Interface (GUI), allowing the user
 * to type in text and click buttons and scroll bars.
 */
public class GUIViewImpl extends JFrame implements IBetterGUIView {

  private final JTextArea displayArea;
  private final JComboBox<String> menu;
  private JLabel titleMessage;
  private JLabel directionsMessage1;
  private JLabel directionsMessage2;
  private final JPanel inputPanel;

  private final JLabel tsInputLabel = new JLabel("Enter ticker symbol: ");
  private final JLabel pnInputLabel = new JLabel("Enter portfolio name: ");
  private final JLabel onInputLabel = new JLabel("Enter owner name: ");
  private final JLabel nsInputLabel = new JLabel("Enter number of shares: ");
  private JLabel dateLabel = new JLabel("Select date: ");

  private final JTextField tsInput = new JTextField(""); // ticker symbol
  private final JTextField pnInput = new JTextField(""); // portfolio name
  private final JTextField onInput = new JTextField(""); // owner name
  private final JTextField nsInput = new JTextField(""); // number of shares

  private JComboBox<Integer> yearComboBox;
  private JComboBox<String> monthComboBox;
  private JComboBox<Integer> dayComboBox;

  private final JButton createButton = new JButton("Create");
  private final JButton viewButton = new JButton("View");
  private final JButton submitButton = new JButton("Submit");
  private final JButton backButton = new JButton("Back");
  private final JButton exitButton = new JButton("Exit");
  private final JButton saveButton = new JButton("Save");
  private final JButton loadButton = new JButton("Load");

  private Features features;
  private String currentPortfolioName;
  private String currentOwnerName;

  /**
   * This is the GUIViewImpl constructor which takes in a type of Features.
   * @param features Features
   * @throws IOException exception
   */
  public GUIViewImpl(Features features) throws IOException {
    super();
    this.features = features;
    features.setView(this);

    // Set cross-platform look and feel
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      displayMessage(e.getMessage());
    }

    // Setup JFrame
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create and add components
    displayArea = new JTextArea();
    displayArea.setEditable(false);
    add(new JScrollPane(displayArea), BorderLayout.CENTER);

    inputPanel = new JPanel();
    inputPanel.setLayout(new GridBagLayout());

    // Initialize and populate menu
    menu = new JComboBox<>();
    setUpMenuOptions();

    // Initialize titleMessage
    welcomeMessage();

    // Add welcome message to inputPanel, centered
    this.addToPanel(this.inputPanel, titleMessage, 0, 0, 2);
    this.addToPanel(this.inputPanel, directionsMessage1, 0, 1, 2);
    this.addToPanel(this.inputPanel, directionsMessage2, 0, 1, 2);

    this.directionsMessage1.setVisible(true);
    this.directionsMessage2.setVisible(false);

    // Add menu to inputPanel, centered
    this.addToPanel(this.inputPanel, menu, 0, 4, 2);
    this.menu.setVisible(false);

    // Add back button but make it invisible initially
    this.addToPanel(inputPanel, backButton, 0, 9, 2);
    backButton.setVisible(false);


    // add inputPanel to frame
    add(inputPanel, BorderLayout.NORTH);

    // Portfolio info input
    this.addToPanel(this.inputPanel, pnInputLabel, 0, 5, 1);
    this.addToPanel(this.inputPanel, pnInput, 1, 5, 1);
    this.addToPanel(this.inputPanel, onInputLabel, 0, 6, 1);
    this.addToPanel(this.inputPanel, onInput, 1, 6, 1);

    // Add create and view buttons
    this.addToPanel(this.inputPanel, createButton, 0, 7, 1);
    this.addToPanel(this.inputPanel, viewButton, 1, 7, 1);

    // Add ticker symbol input but make it invisible initially
    addToPanel(inputPanel, tsInputLabel, 0, 8, 1);
    addToPanel(inputPanel, tsInput, 1, 8, 1);
    tsInputLabel.setVisible(false);
    tsInput.setVisible(false);

    // Add number of shares input but make it invisible initially
    addToPanel(inputPanel, nsInputLabel, 0, 9, 1);
    addToPanel(inputPanel, nsInput, 1, 9, 1);
    nsInputLabel.setVisible(false);
    nsInput.setVisible(false);

    // Add submit button but make it invisible initially
    addToPanel(inputPanel, submitButton, 0, 10, 2);
    submitButton.setVisible(false);

    // Ensure date components are invisible initially
    initializeDate();
    yearComboBox.setVisible(false);
    monthComboBox.setVisible(false);
    dayComboBox.setVisible(false);
    dateLabel.setVisible(false);

    // Ensure frame is centered on screen
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

    createButton.addActionListener(e -> {
      try {
        handleCreateButtonClick();
      } catch (IOException ex) {
        displayMessage(ex.getMessage());
      }
    });
    viewButton.addActionListener(e -> {
      try {
        handleViewButtonClick();
      } catch (IOException ex) {
        displayMessage(ex.getMessage());
      }
    });
    menu.addActionListener(e -> handleMenuInput());
    submitButton.addActionListener(e -> {
      try {
        handleSubmitInput();
      } catch (IOException ex) {
        displayMessage(ex.getMessage());
      }
    });
    backButton.addActionListener(e -> handleBackButtonClick());
    exitButton.addActionListener(e -> System.exit(0)); // Exit the program
    loadButton.addActionListener(e -> handleLoadButtonClick());
    saveButton.addActionListener(e -> handleSaveButtonClick());

    // Set button colorsssss
    createButton.setBackground(Color.MAGENTA);
    viewButton.setBackground(Color.CYAN);
    exitButton.setBackground(Color.RED);
    loadButton.setBackground(Color.BLUE);
    saveButton.setBackground(Color.GREEN);

    // Create a panel for the bottom-right corner buttons
    JPanel bottomRightPanel = new JPanel();
    bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    // Add load and save buttons to the bottom-right panel
    bottomRightPanel.add(loadButton);
    bottomRightPanel.add(saveButton);

    // Add exit button to the bottom-right panel
    bottomRightPanel.add(backButton);
    bottomRightPanel.add(exitButton);

    // Add the bottom-right panel to the bottom of the frame
    add(bottomRightPanel, BorderLayout.SOUTH);

    // Add exit button to the input panel
    //this.addToPanel(this.inputPanel, exitButton, 0, 9, 2);


    // Initialize date components (but make them invisible initially)
    initializeDate();
    yearComboBox.setVisible(false);
    monthComboBox.setVisible(false);
    dayComboBox.setVisible(false);
    dateLabel.setVisible(false);
  }

  private void handleLoadButtonClick() {
    if (pnInput.getText().isEmpty() || onInput.getText().isEmpty()) {
      displayMessage("Please enter both portfolio name and owner name.");
      return;
    }
    try {
      currentPortfolioName = pnInput.getText();
      currentOwnerName = onInput.getText();
      features.loadPortfolio(currentPortfolioName, currentOwnerName,
              "src/portfolios/" + currentPortfolioName);
      displayMessage("Previously saved portfolio data loaded and stored in current portfolio. ");
    } catch (IllegalArgumentException | IOException e) {
      displayMessage(e.getMessage());
    }
  }

  private void handleSaveButtonClick() {
    if (pnInput.getText().isEmpty() || onInput.getText().isEmpty()) {
      displayMessage("Please enter both portfolio name and owner name.");
      return;
    }

    try {
      currentPortfolioName = pnInput.getText();
      currentOwnerName = onInput.getText();
      features.savePortfolio(currentPortfolioName, currentOwnerName,
              "src/portfolios/" + currentPortfolioName);
      displayMessage("Current portfolio data saved. ");
    } catch (IllegalArgumentException | IOException e) {
      displayMessage(e.getMessage());
    }
  }

  private void handleSubmitInput() throws IOException {
    String actionCommand = submitButton.getActionCommand();
    String tickerSymbol = tsInput.getText();
    String numSharesText = nsInput.getText();

    switch (actionCommand) {
      case "Purchase Shares":
        features.purchaseShares(currentPortfolioName, currentOwnerName, tickerSymbol,
                numSharesText, getSelectedYear(), getSelectedMonth(), getSelectedDay());
        break;
      case "Sell Shares":
        features.sellShares(currentPortfolioName, currentOwnerName, tickerSymbol,
                numSharesText, getSelectedYear(), getSelectedMonth(), getSelectedDay());
        break;
      case "Portfolio Composition":
        features.getPortfolioComposition(currentPortfolioName, currentOwnerName,
                getSelectedYear(), getSelectedMonth(), getSelectedDay());
        break;
      case "Portfolio Value":
        features.getPortfolioValue(currentPortfolioName, currentOwnerName, getSelectedYear(),
                getSelectedMonth(), getSelectedDay());
        break;
      default:
        displayMessage("No valid action selected.");
        break;
    }
    this.inputInvisible();
  }

  private void handleMenuInput() {
    String input = menu.getSelectedItem().toString();

    switch (input) {
      case "Purchase Shares":
        purchaseSharesInputVisible();
        submitButton.setActionCommand("Purchase Shares");
        yearComboBox.setVisible(true);
        monthComboBox.setVisible(true);
        dayComboBox.setVisible(true);
        dateLabel.setVisible(true);
        backButton.setVisible(true);
        break;
      case "Sell Shares":
        sellSharesInputVisible();
        submitButton.setActionCommand("Sell Shares");
        yearComboBox.setVisible(true);
        monthComboBox.setVisible(true);
        dayComboBox.setVisible(true);
        dateLabel.setVisible(true);
        backButton.setVisible(true);
        break;
      case "Portfolio Composition":
        portfolioCompositionVisible();
        submitButton.setActionCommand("Portfolio Composition");
        yearComboBox.setVisible(true);
        monthComboBox.setVisible(true);
        dayComboBox.setVisible(true);
        dateLabel.setVisible(true);
        backButton.setVisible(true);

        tsInput.setVisible(false);
        tsInputLabel.setVisible(false);
        nsInput.setVisible(false);
        nsInputLabel.setVisible(false);
        break;
      case "Portfolio Value":
        portfolioValueVisible();
        submitButton.setActionCommand("Portfolio Value");
        yearComboBox.setVisible(true);
        monthComboBox.setVisible(true);
        dayComboBox.setVisible(true);
        dateLabel.setVisible(true);
        backButton.setVisible(true);

        tsInput.setVisible(false);
        tsInputLabel.setVisible(false);
        nsInput.setVisible(false);
        nsInputLabel.setVisible(false);
        break;
      default:
        inputInvisible();
        submitButton.setActionCommand("");
        yearComboBox.setVisible(false);
        monthComboBox.setVisible(false);
        dayComboBox.setVisible(false);
        dateLabel.setVisible(false);
        backButton.setVisible(false);

        tsInput.setVisible(false);
        tsInputLabel.setVisible(false);
        nsInput.setVisible(false);
        nsInputLabel.setVisible(false);
        break;
    }
    displayArea.append("Input received: " + input + "\n");
  }

  private void initializeDate() {
    yearComboBox = new JComboBox<>();
    populateYearComboBox();
    yearComboBox.addActionListener(e -> populateDayComboBox());

    monthComboBox = new JComboBox<>(getMonths());
    monthComboBox.addActionListener(e -> populateDayComboBox());

    dayComboBox = new JComboBox<>();
    populateDayComboBox();

    dateLabel = new JLabel("Select date: ");

    this.addToPanel(this.inputPanel, dateLabel, 0, 2, 1);
    this.addToPanel(this.inputPanel, yearComboBox, 1, 2, 1);
    this.addToPanel(this.inputPanel, monthComboBox, 2, 2, 1);
    this.addToPanel(this.inputPanel, dayComboBox, 3, 2, 1);
  }

  private void inputInvisible() {
    tsInputLabel.setVisible(false);
    tsInput.setVisible(false);
    nsInputLabel.setVisible(false);
    nsInput.setVisible(false);
    submitButton.setVisible(false);
  }

  private void portfolioCompositionVisible() {
    submitButton.setVisible(true);
  }

  private void portfolioValueVisible() {
    submitButton.setVisible(true);
  }

  private void purchaseSharesInputVisible() {
    tsInputLabel.setVisible(true);
    tsInput.setVisible(true);
    nsInputLabel.setVisible(true);
    nsInput.setVisible(true);
    submitButton.setVisible(true);
  }

  private void sellSharesInputVisible() {
    tsInputLabel.setVisible(true);
    tsInput.setVisible(true);
    nsInputLabel.setVisible(true);
    nsInput.setVisible(true);
    submitButton.setVisible(true);
  }

  private void handleBackButtonClick() {
    resetFieldsVisibility();
    inputInvisible();
    menu.setVisible(false);
    this.mainMenuVisibility(true);
    this.directionsMessage1.setVisible(true);
    this.directionsMessage2.setVisible(false);
  }


  private void addToPanel(JPanel panel, JComponent component, int x, int y, int w) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = x;
    gbc.gridy = y;
    gbc.gridwidth = w;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(component, gbc);
    component.setVisible(true);
  }

  private void setUpMenuOptions() {
    String[] options = {"Select one of the options below", "Purchase Shares", "Sell Shares",
                        "Portfolio Composition", "Portfolio Value"};
    for (String option : options) {
      menu.addItem(option);
    }
  }

  private void handleCreateButtonClick() throws IOException {
    currentPortfolioName = pnInput.getText();
    currentOwnerName = onInput.getText();
    features.createPortfolio(currentPortfolioName, currentOwnerName);
  }

  private void handleViewButtonClick() throws IOException {
    currentPortfolioName = pnInput.getText();
    currentOwnerName = onInput.getText();
    features.viewPortfolio(currentPortfolioName, currentOwnerName);
  }

  @Override
  public void stockMenu() {
    this.inputInvisible();
    this.menu.setVisible(true);
    resetFieldsVisibility();
    backButton.setVisible(true);
    this.directionsMessage1.setVisible(false);
    this.directionsMessage2.setVisible(true);
  }

  @Override
  public void resetFieldsVisibility() {
    tsInputLabel.setVisible(false);
    tsInput.setVisible(false);
    nsInputLabel.setVisible(false);
    nsInput.setVisible(false);
    submitButton.setVisible(false);
    yearComboBox.setVisible(false);
    monthComboBox.setVisible(false);
    dayComboBox.setVisible(false);
    dateLabel.setVisible(false);
  }

  @Override
  public void setFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void displayMessage(String message) {
    displayArea.append(message + "\n");
  }

  @Override
  public void clearInput() {
    this.onInput.setText("");
    this.pnInput.setText("");
    this.tsInput.setText("");
  }

  @Override
  public void clearDisplay() {
    this.displayArea.setText("");
  }

  @Override
  public void mainMenuVisibility(boolean visible) {
    pnInput.setVisible(visible);
    pnInputLabel.setVisible(visible);
    onInput.setVisible(visible);
    onInputLabel.setVisible(visible);
    createButton.setVisible(visible);
    viewButton.setVisible(visible);
    saveButton.setVisible(visible);
    loadButton.setVisible(visible);
  }

  private void welcomeMessage() {
    this.titleMessage = new JLabel("Welcome to the Stock Simulator!\nWe like to make money :)\n");
    this.directionsMessage1 = new JLabel("To create, view, load, or save a portfolio, "
            + "enter its name and the owner name. \n");
    this.directionsMessage2 = new JLabel("You can view a portfolio and purchase or sell stocks "
            + "as well as view the portfolio's composition or value on a valid date. \n");
  }

  private void populateYearComboBox() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    for (int year = currentYear; year >= 1970; year--) {
      yearComboBox.addItem(year);
    }
  }

  private void populateDayComboBox() {
    int selectedYear = (int) yearComboBox.getSelectedItem();
    int selectedMonth = monthComboBox.getSelectedIndex();
    Calendar cal = Calendar.getInstance();
    cal.set(selectedYear, selectedMonth, 1);
    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    dayComboBox.removeAllItems();
    for (int day = 1; day <= maxDay; day++) {
      dayComboBox.addItem(day);
    }
  }

  private String getSelectedYear() {
    return yearComboBox.getSelectedItem().toString();
  }

  private String getSelectedMonth() {
    return String.format("%02d", monthComboBox.getSelectedIndex() + 1);
  }

  private String getSelectedDay() {
    return dayComboBox.getSelectedItem().toString();
  }

  private String[] getMonths() {
    return new String[]{"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"};
  }
}
