import org.junit.Before;
import org.junit.Test;

import oldstocksimulator.oldmodel.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * DateTest represents the class for testing the methods and exceptions.
 */
public class DateTest {
  private Date date1;
  private Date date3;
  private Date date10;
  private Date date10Again;

  @Before
  public void setUp() {
    date1 = new Date(20, 4, 2004);
    date3 = new Date(30, 12, 2024);
    date10 = new Date(1, 3, 2001);
    date10Again = new Date(1, 3, 2001);
  }

  @Test
  public void testIfInvalidMonth() {
    Date date1;
    try {
      date1 = new Date(1, 15, 1000); // bigger
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date1 = new Date(1, -15, 1000); // negative
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date1 = new Date(1, 0, 1000); // zero
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date1 = new Date(1, 12, 1000); // december
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date1 = new Date(1, 5, 1000); // in between
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date1 = new Date(1, 1, 1000); // jan
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }
  }

  @Test
  public void testIfInvalidYear() {
    Date date2;
    try {
      date2 = new Date(4, 2, -1000); // negative
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date2 = new Date(4, 2, -0001); // close to 0
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date2 = new Date(4, 2, 0000); // 0
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date2 = new Date(9, 5, 2024); // works
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }
  }

  @Test
  public void testIfInvalidDay() {
    Date date3;

    try {
      date3 = new Date(30, 2, 2024); // feb 30 days
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(0, 2, 2024); // 0 days
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(-30, 10, 2024); // negative days
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(45, 10, 2024); // too high
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(31, 4, 2024); // 4 && 31
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(31, 6, 2024); // 6 && 31
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(31, 9, 2024); // 9 && 31
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(31, 11, 2024); // 11 && 31
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date3 = new Date(28, 2, 2024); // works feb
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date3 = new Date(3, 3, 2020); //works normal
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date3 = new Date(1, 10, 1986); // works normal
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date3 = new Date(28, 4, 2024); // 4 && 28
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date3 = new Date(28, 6, 2024); // 6 && 28
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date3 = new Date(28, 9, 2024); // 9 && 28
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date3 = new Date(28, 11, 2024); // 11 && 28
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }
  }

  @Test
  public void testIfLeapYear() {
    Date date4;

    try {
      date4 = new Date(30, 2, 1900); // leap year
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date4 = new Date(29, 2, 1899); // is not leap year but above 28
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date4 = new Date(31, 2, 2024); // > 29 and is 2024
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date4 = new Date(31, 2, 2023); // for a year that isnt leap year
      fail("The above line should have thrown an exception");
    } catch (IllegalArgumentException e) {
      // do not do anything except catch the exception and let the test continue
    }

    try {
      date4 = new Date(2, 2, 2024); // normal leap year
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date4 = new Date(29, 2, 2024); // normal leap year
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date4 = new Date(29, 2, 2072); // normal leap year
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date4 = new Date(25, 2, 2001); // noraml not leap year
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date4 = new Date(25, 3, 2001); // normal not feb
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

    try {
      date4 = new Date(25, 3, 2000); // normal not feb
    } catch (IllegalArgumentException e) {
      fail("The above line should not have thrown an exception");
    }

  }

  @Test
  public void testToString() {
    Date date1 = new Date(20, 2, 1900);
    assertEquals("1900-02-20", date1.toString());

    Date date2 = new Date(29, 2, 2000);
    assertEquals("2000-02-29", date2.toString());

    Date date3 = new Date(1, 1, 0000);
    assertEquals("0000-01-01", date3.toString());

    Date date4 = new Date(15, 11, 2087);
    assertEquals("2087-11-15", date4.toString());

    Date date5 = new Date(1, 1, 1);
    assertEquals("0001-01-01", date5.toString());

    Date date6 = new Date(31, 12, 9999);
    assertEquals("9999-12-31", date6.toString());
  }

  @Test
  public void testCompareToSame() {
    assertEquals(0, date10.compareTo(date10Again));
  }

  @Test
  public void testCompareToEarlierDate() {
    assertTrue(date1.compareTo(date3) < 0);
  }

  @Test
  public void testCompareToLaterDate() {
    assertTrue(date3.compareTo(date1) > 0);
  }

  @Test
  public void testIsBeforeDate() {
    assertEquals(true, date1.isBefore(date3));
    assertEquals(false, date1.isBefore(date10));
  }

  @Test
  public void testIsAfterDate() {
    assertEquals(false, date1.after(date3));
    assertEquals(false, date1.after(date10));
  }

  @Test
  public void testDaysInBetween() {
    assertEquals(7559, date1.daysBetween(date3));
    assertEquals(-8705, date3.daysBetween(date10));
    assertEquals(0, date10.daysBetween(date10Again));
  }

  @Test
  public void testAddDays() {
    assertEquals(new Date(30, 4, 2004), date1.addDays(10));
    assertEquals(new Date(03, 2, 2026), date3.addDays(400));
    assertEquals(new Date(01, 03, 2001), date10.addDays(0));
  }


}