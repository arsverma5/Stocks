package oldstocksimulator.oldmodel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

/**
 * this is the main class that represents the Date
 * and has the day, month, year as its fields and
 * then also the isLeapYear field.
 */
public class Date implements Comparable<Date> {
  private final int day;
  private final int month;
  private final int year;

  /**
   * the constructor for MyDate that tests different exceptions.
   * It tests for leap years, days, months, and years.
   *
   * @param day // represents the day of a date
   * @param month // represents the month of a date
   * @param year // represents the year of a date
   *
   */
  public Date(int day, int month, int year) {
    boolean isLeapYear = year % 4 == 0 && (year % 100 != 0 || (year % 400 == 0 && year % 100 == 0));
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month");
    } else if (day < 1 || day > 31) {
      throw new IllegalArgumentException("Invalid day");
    } else if (month == 2 && day > 29 && isLeapYear) {
      throw new IllegalArgumentException("Not a valid leap year");
    } else if (month == 2 && day > 28 && !isLeapYear) {
      throw new IllegalArgumentException("Not a valid date for February");
    } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
      throw new IllegalArgumentException("Not a valid day");
    } else if (year < 0000) {
      throw new IllegalArgumentException("Invalid year");
    }
    this.day = day;
    this.month = month;
    this.year = year;
  }

  @Override
  //  It should return a string that reports this date in the format YYYY-MM-DD
  public String toString() {
    return String.format("%04d-%02d-%02d", this.year, this.month, this.day);
  }

  /**
   * Converts a string to a date.
   * @param date String
   * @return Date
   */
  public static Date convertStringToDate(String date) {
    int year = Integer.parseInt(date.substring(0, 4));
    int month = Integer.parseInt(date.substring(5, 7));
    int day = Integer.parseInt(date.substring(8, 10));

    return new Date(day, month, year);
  }

  /**
   * Compares two dates.
   * @param d Date
   * @return boolean
   */
  public boolean equalsTo(Date d) {
    return this.toString().equals(d.toString());
  }

  @Override
  public int compareTo(Date o) {
    if (this.year != o.year) {
      return this.year - o.year;
    }
    if (this.month != o.month) {
      return this.month - o.month;
    }
    return this.day - o.day;
  }

  /**
   * Checks if a date is before this one.
   * @param portfolioAsOfDate Date
   * @return boolean
   */
  public boolean isBefore(Date portfolioAsOfDate) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, portfolioAsOfDate.year);
    cal.set(Calendar.MONTH, portfolioAsOfDate.month);
    cal.set(Calendar.DAY_OF_MONTH, portfolioAsOfDate.day);
    java.util.Date pDate = cal.getTime();

    cal.set(Calendar.YEAR, this.year);
    cal.set(Calendar.MONTH, this.month);
    cal.set(Calendar.DAY_OF_MONTH, this.day);
    java.util.Date sDate = cal.getTime();

    return sDate.before(pDate);
  }

  /**
   * Checks if a date is after the given date.
   * @param date Date
   * @return boolean
   */
  public boolean after(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, date.year);
    cal.set(Calendar.MONTH, date.month);
    cal.set(Calendar.DAY_OF_MONTH, date.day);
    java.util.Date thisDate = cal.getTime();

    cal.set(Calendar.YEAR, date.year);
    cal.set(Calendar.MONTH, date.month);
    cal.set(Calendar.DAY_OF_MONTH, date.day);
    java.util.Date otherDate = cal.getTime();

    return thisDate.after(otherDate);
  }

  // Convert the custom Date class to LocalDate
  private LocalDate toLocalDate() {
    return LocalDate.of(this.year, this.month, this.day);
  }

  /**
   * Gets how many days are in between this date and another.
   * @param otherDate Date
   * @return long
   */
  public long daysBetween(Date otherDate) {
    LocalDate thisDate = this.toLocalDate();
    LocalDate thatDate = otherDate.toLocalDate();
    return ChronoUnit.DAYS.between(thisDate, thatDate);
  }

  /**
   * Adds days to this one and returns the new date.
   * @param days int
   * @return Date
   */
  public Date addDays(int days) {
    LocalDate localDate = this.toLocalDate().plusDays(days);
    return new Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
  }
}
