Here is how to run our JAR file:
1. In a command-prompt terminal, navigate to the folder containing this program file,
then res folder, then the Program_jar folder.
2. In the terminal, enter "java -jar Program.jar" for the graphical user interface
or "java -jar Program.jar -text" for the text-based interface, then hit ENTER.
If you'd like to quit, enter "quit".
3. The program should now run in the terminal.

Here is how to run our Stock Program GUI BASED PROGRAM:
1. Run the JAR file using above instructions.
2. You will see a pop-up of a new screen that is the program. There will be a welcome message
and a couple of buttons in the bottom right corner and then two bigger buttons.
3. Create a portfolio by entering your "portfolio name" and "owner name". Be sure
to enter both of the text fields or else you will see a message that says to enter both.
4. You can save this portfolio locally to this program by clicking "Save"
    4a. You can also load the portfolio by clicking "Load"
    4b. You cannot load a portfolio that has not been saved previously.
5. Next you type the name and owner name again and click "View" and then you will be directed
to a toggle bar that has different options of "Purchase Shares", "Sell Shares", "Portfolio Composition",
and "Portfolio Value".
6. Click on "Purchase Shares" and type in the name of the ticker symbol you want to buy from.
An example could be "AAPL" and then write how many shares, ex "100". (You cannot buy or sell negative
amounts of shares.)
7. Choose the date of what day you bought them on using the toggle bars for the year, month, and day.
If you enter a date that lies on a weekend or is a holiday, it will display a message saying its an invalid date.
8. You can now sell shares, if you desire as well using the same steps above.
9. You can click on portfolio composition to see the breakdown of the ticker symbols you own
in your portfolio with their respective number of shares you own, as well.
10. Additionally, you can do this for getting the portfolio's value as welll on a certain date.
11. At all times, you can always save and load your portfolio data. And you can always
go back to the main menu or exit the program.
12. Whenever you finish, click on exit. Thanks for using our program! We love to gamble.


Here is how to run our Stock Program TEXT-BASED PROGRAM:

1. Run the JAR file using above instructions.
2. You will see a menu text prompt that welcomes you to the program and a bunch of
commands you can choose. The number to the left of each command is the number you
should enter to run that command, and to the right of the command are parameters
you are expected to enter.
3. To create a portfolio, type "1". You will now be prompted for the portfolio name
and portfolio's owner's name. Input these as well. Once you are done the program will
return you to the main menu.
4. Now to add 3 different stocks to your portfolio, navigate by entering "5", followed
by the portfolio name and owner name.
5. To buy the stock, enter "1", followed by the ticker symbol, how many shares you'd like
to purchase, and the date you'd like to make this purchase on. The date should be whenever
the stock market is open.
6. Do this 3 times (or how many times you desire).
7. To add another portfolio, write "14" to return to the main menu.
8. After being navigated back to the menu page, write "1", followed by the portfolio name
and owner name.
9. Repeat step 4 and add new stocks again to this new portfolio.
10. Now if you wish to query the value of one stock on specific date, you can enter "4".
    **
    The list of stocks this program supports is AAPL, GOOG, MSFT, and other stocks that the AlphaVantage
    API has data for. When calling any of the computational methods, if you enter a start or end date
    that is a weekend, in other words a date that isn’t a day when the stock market is open, it will
    output saying you entered an invalid date. This includes weekends, holidays, etc.
    **
    10a. You can then choose a command such as "get-x-day-moving average [start date] [a number x]", enter
    the corresponding number, and this will calculate the x day moving average for you for that specific stock.
    10b. You can do this for get-price-change and x-day crossover, too, and closing-price for a specific
    date, as well.
11. If you wish to query the value of the portfolio or the values of all stocks in both portfolios, you can
 instead navigate to the portfolio menu by entering "5" once more.
    11a. You can get the value of the portfolio by entering "4", followed by the appropriate date.
    11b. You can now choose a computation you would like to be calculated for you.
    Ex: "get-all-x-day-moving-average [date] [integer x]"
    An example output (if I had AAPL, GOOG, and MSFT in my portfolio):
    "MSFT: 167.835
    GOOG: 170.0291
    AAPL: 171.5025"
    11c. You can do any other command you want as well in this menu.
12. Within portfolio-menu, you can also buy stocks or sell stocks if you would like increase
the amount of shares you hold. "buy-stock [ticker symbol] [num of shares]" or
"sell-stock [ticker symbol] [num of shares]"
13. To see the composition of your portfolio, navigate to "view-stocks": you can see a list of stocks that outputs
the symbol and the amount of shares you own of that company.
14. You can also remove any of your portfolios by doing "remove-portfolio [portfolio name] [owner name]"
15. Penultimately, you can also view the portfolios you own, too.
16. Lastly, if you are done using our program, simple type "quit" and you will be exited out of the program.

Thank you for using the Stock Simulator Program!