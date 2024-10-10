package sources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Uses the AlphaVantage API to retrieve stock information data.
 */
public class AlphaVantageDemo {
  // pass in userinput and then whatever they enter it will

  public Map<String, String> savedFiles;

  /**
   * Creates a new alpha vantage demo that can call the API and retrieve
   * data for a valid ticker symbol.
   */
  public AlphaVantageDemo() {
    this.savedFiles = new HashMap<>();
  }

  /**
   * Gets CSV data.
   * @param stockSymbolByUser String
   * @throws IOException exception
   */
  public String getCSVData(String stockSymbolByUser) throws IllegalArgumentException, IOException {

    // returns file path if it already exists
    if (savedFiles.containsKey(stockSymbolByUser)) {
      return savedFiles.get(stockSymbolByUser);
    }
    // otherwise, creates a new file path using the API
    else {
      //the API key needed to use this web service.
      //Please get your own free API key here: https://www.alphavantage.co/
      //Please look at documentation here: https://www.alphavantage.co/documentation/
      String apiKey = "7TI4IRSWZWDASJHV";
      String stockSymbol = stockSymbolByUser; // ticker symbol for API retrieval
      URL url = null;

      try {
        /*
        create the URL. This is the query to the web service. The query string
        includes the type of query (DAILY stock prices), stock symbol to be
        looked up, the API key and the format of the returned
        data (comma-separated values:csv). This service also supports JSON
        which you are welcome to use.
         */
        url = new URL("https://www.alphavantage"
                + ".co/query?function=TIME_SERIES_DAILY"
                + "&outputsize=full"
                + "&symbol"
                + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");
      } catch (MalformedURLException e) {
        throw new RuntimeException("the alphavantage API has either changed or "
                + "no longer works");
      }

      InputStream in = null;
      StringBuilder output = new StringBuilder();

      try {
        /*
        Execute this query. This returns an InputStream object.
        In the csv format, it returns several lines, each line being separated
        by commas. Each line contains the date, price at opening time, highest
        price for that date, lowest price for that date, price at closing time
        and the volume of trade (no. of shares bought/sold) on that date.

        This is printed below.
         */
        in = url.openStream();
        int b;

        while ((b = in.read()) != -1) {
          output.append((char) b);
        }
      } catch (IOException ignored) {
      }


      if (!output.toString().contains("timestamp,open,high,low,close,volume")) {
        throw new IllegalArgumentException("No price data for " + stockSymbolByUser);
      }
      else {
        String filePath = stockSymbolByUser + ".csv";
        savedFiles.put(stockSymbolByUser, filePath);
        File outputFile = new File(filePath);
        FileWriter fileWriter = new FileWriter(outputFile);

        fileWriter.write(output.toString());

        fileWriter.flush();

        fileWriter.close();

        return filePath;
      }
    }
  }
}
