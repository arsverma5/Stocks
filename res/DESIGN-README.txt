CHANGES IN PART 3:

Going into part 3, we knew that we had to preserve our old design as much as possible while still
implementing new features in a way that adheres to SOLID principles.

Looking back at part 2 of this assignment, we did not check the methods (purchase, sell,
view composition, get portfolio value, etc.) if they had bought it or wanted to view any
information on a valid date. Therefore, we included that implementation in there, as well now.
Additionally, in regard to the methods view composition and portfolio value, we ran into the error
of purchase date being null. So, we added a check within there for null purchase date. Additionally,
we made sure that those methods return type was of the interface type (Map versus HashMap).
Penultimately, we removed the field of the AlphaVantageDemo apiCaller from Stock and used it in
AbstractModelImpl as intended from part 1. This was done as a new AlphaVantage was being called
each time the user wanted something calculated or retrieved. Lastly, the catch blocks in the methods
for saving and loading a portfolio were changed to throw an error rather than print the stack trace
so the controller can catch it.

Moving on to the new implementations, to support the Swing UI part of the program,
 we created new interfaces and classes for this. We created a new package "gui" within the
 bettercontroller package, as well as the betterview package. For the view, we created an
 interface called IBetterGUIView that supports the view component of the MVC particularly
 for the GUI. The class GUIViewImpl implements the interface and handles the components
 of displaying the program to the user by using Swing.

 In our program, we use JButtons so the user can toggle between what they want to do on the
 main menu frist (create or view) with 3 buttons in the lower right corner if they want to
 save, load, or exit. Then, using a JComboBox, they can go through and choose what sort of
 feature they want (buy/sell/composition/get value). The JComboBox is also used for choosing
 the date they did any of the actions on. The program also makes sure that the user
 entered all mandatory boxes to ensure they don't skip any step such as forgetting to enter
 their protfolio name or owner name. It also checks if it is a valid date, as well as
 checks if the stocks they want to buy/sell is negative.

 Within the controller gui part, we made an interface called Features and a class called
 GUIControllerImpl that implements this interface. The GUI controller discusses
 with the GUI view and the functionality of the model from part 2 to execute anything the
 user wants executed. It contains the feature of checking if all textFields in the program
 have something in there, calls the IBetterModel Model of the MVC pattern, and creates
 a log at all times, as well.

CHANGES IN PART 2:

Going into part 2, we knew that we had to preserve our old design as much as possible while still
implementing new features in a way that adheres to SOLID principles.

We began by looking at what design choices we had not implemented correctly in part 1,
which included not having an explicit IModel interface. So, we created this new interface,
abstracted our old and new design into separate classes, and did the same for our controller
and view interfaces/classes.

This helped us ensure that we were minimizing modifications (for the sake of implementing
new functionalities, rather than integral design choices) to our old code. We also did put
Stock and Portfolio into their own separate classes, rather than having them be subclasses,
so that we could create an interface for portfolio and allow for stricter communication between
the model and the controller (so the controller could not directly communicate with portfolios
and stocks).

In our better model and controller classes, we got to work on implementing the new features
as well as making user input simpler. We made sure to use $ sign units and ask for numbers
rather than long, error-prone commands.

BetterModel now supports purchasing and selling stocks on a given date, viewing the
composition of a portfolio, getting the value and distribution of a portfolio on a
given date, rebalancing a portfolio, displaying the bar chart over a given period of time
for a stock or portfolio, loading and saving portfolios externally, as well as all previously
implemented features.

BetterController communicates with BetterModel and BetterView to allow the user to run
commands for each of these features and display the appropriate result.

PART 1 DESIGN-README:

Our design for the Stock Simulator Program follows the Model-View-Controller (MVC) Pattern.
There are three main packages that include three interfaces: IModel, IController, and IView.

IModel: ModelImpl extends the IModel interface and implements the necessary public methods such as
doing necessary functions with one or multiple portfolios (this is the object). Within ModelImpl,
it has the implementations of adding, removing, viewing, and getting the portfolios, as well as
reading through the csv file and parsing it and adding it into a list of stockInformation.
ModelImpl also has two subclasses:

 - Portfolio: Portfolio has getter methods, as well as allowing the user to buy and sell stocks.
              We also added a feature to getAll computations from a list of portfolios
              (did not just limit it to a singular stock), and this is also within the Portfolio class.

 - Stock: Stock has the features of computing and calculating any statistical information
          such as getting x-day average, closing price, and crossovers. It also has private
          helper methods, and methods to sell and buy shares of a specific stock.

Our data design choice of using subclasses within the main ModelImpl class is because ModelImpl in a way
acts as a Stock Manager for the two subclasses. It essentially combines two objects into one class.

StockInformation: is another class that has fields from the csv/spreadsheet that has getters and a toString().
                  It goes through the csv and assigns the values to the fields in that class.

Date: Date is a class that determines if a date is an invalid input within its constructor based on what
      the user inputs and has a toString() method, too.


IController: This is the controller of the MVC Stock program, and acts as the glue to the Model and View.
             It communicated back and forth between the backend and frontend of the program. It has
             the implementation of Controller -- StockController.

- StockController: Depending on what the user inputs, it acts based on that and does the necessary action.
                   Some examples include creating a portfolio, adding a stock, and calculating statistical information.
                   It has the goController() method that passes in the model, Readable, and View.
                   It takes in the user's input by waiting for what they type, and acts on a switch case:
                   Depending on what action they want to take, it calls a method and then calls the model and view's
                   respective methods, as well.

View: The last component to the MVC pattern is the IView interface which has the ViewImpl implementation that
      has the ViewImpl implementation class. View displays to the console and uses a command line based user interface.

- ViewImpl: ViewImpl uses PrintStream and InputStream to append any messages and then displays onto console.
            Depending on what menu the user wants to see, it navigates between that and calculates or retrieves
            any data based on their input, as well.

Overall, our program follows the MVC program. And we made meticulous decisions for making goo design practices.
To call the API, we have a HashMap within our AlphaVantageDemo class that accounts for what tickerSymbol and filepaths
are already stored. Depending on that, we either refer to the already existing file to limit querying to the API
multiple times or calling the API if it doesn't already exist. This ensures that our data is always referencable and
does not tell the user "the daily limit was used."

The MVCExampleStockSimulator is the main method that runs our actual program.

Lastly, we wrote well thought out tests that account for edge cases, retrieve data, and ensure any computations
are correct.
