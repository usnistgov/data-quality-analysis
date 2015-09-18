/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HL7DateAnalyzer
{
  private int year = 0;
  private int month = 0;
  private int day = 0;
  private int hour = 0;
  private int min = 0;
  private int sec = 0;
  private int ms = 0;
  private int timezone = 0;
  private int timezoneHour = 0;
  private int timezoneMinute = 0;

  private boolean precisionYear = false;
  private boolean precisionMonth = false;
  private boolean precisionDay = false;
  private boolean precisionHour = false;
  private boolean precisionMin = false;
  private boolean precisionSec = false;
  private boolean precisionMs = false;
  private boolean hasTimezone = false;

  public Date getDate()
  {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.YEAR, year);
    if (precisionDay)
    {
      cal.set(Calendar.MONTH, month - 1);
      if (precisionDay)
      {
        cal.set(Calendar.DAY_OF_MONTH, day);
        if (precisionHour)
        {
          cal.set(Calendar.HOUR_OF_DAY, hour);
          if (precisionMin)
          {
            cal.set(Calendar.MINUTE, min);
            if (precisionSec)
            {
              cal.set(Calendar.SECOND, sec);
              if (precisionMs)
              {
                cal.set(Calendar.MILLISECOND, ms);
              } else
              {
                cal.set(Calendar.MILLISECOND, 0);
              }
            } else
            {
              cal.set(Calendar.SECOND, 0);
              cal.set(Calendar.MILLISECOND, 0);
            }
          } else
          {
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
          }
        } else
        {
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
        }
      } else
      {
        return null;
      }
      if (hasTimezone)
      {
        int rawOffset = (timezoneHour * 60 + timezoneMinute) * 60 * 1000;
        String[] timezones = TimeZone.getAvailableIDs(rawOffset);
        if (timezones.length > 0)
        {
          cal.setTimeZone(TimeZone.getTimeZone(timezones[0]));
        }
      }
    } else
    {
      return null;
    }
    return cal.getTime();
  }

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

  public int getMonth()
  {
    return month;
  }

  public void setMonth(int month)
  {
    this.month = month;
  }

  public int getDay()
  {
    return day;
  }

  public void setDay(int day)
  {
    this.day = day;
  }

  public int getHour()
  {
    return hour;
  }

  public void setHour(int hour)
  {
    this.hour = hour;
  }

  public int getMin()
  {
    return min;
  }

  public void setMin(int min)
  {
    this.min = min;
  }

  public int getSec()
  {
    return sec;
  }

  public void setSec(int sec)
  {
    this.sec = sec;
  }

  public int getMs()
  {
    return ms;
  }

  public void setMs(int ms)
  {
    this.ms = ms;
  }

  public int getTimezone()
  {
    return timezone;
  }

  public void setTimezone(int timezone)
  {
    this.timezone = timezone;
  }

  public boolean isPrecisionYear()
  {
    return precisionYear;
  }

  public void setPrecisionYear(boolean precisionYear)
  {
    this.precisionYear = precisionYear;
  }

  public boolean isPrecisionMonth()
  {
    return precisionMonth;
  }

  public void setPrecisionMonth(boolean precisionMonth)
  {
    this.precisionMonth = precisionMonth;
  }

  public boolean isPrecisionDay()
  {
    return precisionDay;
  }

  public void setPrecisionDay(boolean precisionDay)
  {
    this.precisionDay = precisionDay;
  }

  public boolean isPrecisionHour()
  {
    return precisionHour;
  }

  public void setPrecisionHour(boolean precisionHour)
  {
    this.precisionHour = precisionHour;
  }

  public boolean isPrecisionMin()
  {
    return precisionMin;
  }

  public void setPrecisionMin(boolean precisionMin)
  {
    this.precisionMin = precisionMin;
  }

  public boolean isPrecisionSec()
  {
    return precisionSec;
  }

  public void setPrecisionSec(boolean precisionSec)
  {
    this.precisionSec = precisionSec;
  }

  public boolean isPrecisionMs()
  {
    return precisionMs;
  }

  public void setPrecisionMs(boolean precisionMs)
  {
    this.precisionMs = precisionMs;
  }

  public boolean isHasTimezone()
  {
    return hasTimezone;
  }

  public void setHasTimezone(boolean hasTimezone)
  {
    this.hasTimezone = hasTimezone;
  }

  public HL7DateAnalyzer(String time) {
    this.time = time;
    analyze();
  }

  private String time;
  private List<String> errorMessageList = new ArrayList<String>();

  public boolean isOkay()
  {
    return errorMessageList.size() == 0;
  }

  public boolean hasErrors()
  {
    return errorMessageList.size() > 0;
  }

  public void analyze()
  {
    if (year == 0 && !time.equals(""))
    {
      try
      {
        int i = time.indexOf("-");
        if (i == -1)
        {
          i = time.indexOf("+");
        }
        if (i != -1)
        {
          String timezoneString = time.substring(i);
          time = time.substring(0, i);
          timezone = Integer.parseInt(timezoneString);
          if (timezoneString.length() == 5)
          {
            timezoneHour = Integer.parseInt(timezoneString.substring(0, 3));
            timezoneMinute = Integer.parseInt(timezoneString.substring(2, 4));
            if (timezoneHour < 0)
            {
              timezoneMinute = -timezoneMinute;
            }
            hasTimezone = true;
          }

        }
        if (timezone > 2400 || timezone < -2400)
        {
          errorMessageList.add("Time zone is not within the range of 2400 to -2400");
        }
        if (time.length() < 4)
        {
          errorMessageList.add("Year is required by DTM definition");
        } else
        {
          year = Integer.parseInt(time.substring(0, 4));
          precisionYear = true;
        }
        if (time.length() >= 6)
        {
          month = Integer.parseInt(time.substring(4, 6));
          precisionMonth = true;
          if (time.length() >= 8)
          {
            day = Integer.parseInt(time.substring(6, 8));
            precisionDay = true;
            if (time.length() >= 10)
            {
              hour = Integer.parseInt(time.substring(8, 10));
              precisionHour = true;
              if (time.length() >= 12)
              {
                min = Integer.parseInt(time.substring(10, 12));
                precisionMin = true;
                if (time.length() >= 14)
                {
                  if (time.length() > 14 && time.charAt(14) != '.')
                  {
                    errorMessageList.add("Invalid format for DTM data type, value in position 15 must be one of these . + -");
                  } else
                  {
                    sec = Integer.parseInt(time.substring(12));
                    precisionSec = true;
                    if (time.length() > 15)
                    {
                      ms = Integer.parseInt(time.substring(15));
                      precisionMs = true;
                      int digits = time.length() - 15;
                      if (digits > 4)
                      {
                        errorMessageList.add("Invalid format for DTM data type, ms can only be 4 digits long");
                      } else
                      {
                        ms = ms * 10 ^ (4 - digits);
                      }
                    }
                  }
                }
              }
            }
          }
        }

        if (month > 12)
        {
          errorMessageList.add("Month is not valid US month");
        }
        if (day > 31)
        {
          errorMessageList.add("Day is not a valid US day");
        }
        if (hour > 23)
        {
          errorMessageList.add("Hour is not valid, must be between 0 and 24");
        }
        if (min > 59)
        {
          errorMessageList.add("Minute is not valid, must be between 0 and 59");
        }
        if (sec > 59)
        {
          errorMessageList.add("Second is not valid, must be between 0 and 59");
        }
        if (sec > 59)
        {
          errorMessageList.add("Second is not valid, must be between 0 and 59");
        }
        if (ms > 9999)
        {
          errorMessageList.add("Millisecond is not valid, must be between 0 and 9999");
        }
      } catch (NumberFormatException nfe)
      {
        errorMessageList.add("Format of date/time does not conform to DTM definition, cannot be converted into numeric values");
      }

    }
  }
}
