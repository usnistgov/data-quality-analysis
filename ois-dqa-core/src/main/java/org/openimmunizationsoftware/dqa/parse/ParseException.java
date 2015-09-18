/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

public class ParseException extends Exception
{
  private String message = null;
  private int line = 0;
  private int column = 0;
  private String fieldName = "";
  
  public String getFieldName()
  {
    return fieldName;
  }

  public String getMessage()
  {
    return message;
  }

  public int getLine()
  {
    return line;
  }

  public int getColumn()
  {
    return column;
  }
  
  public ParseException(String message, String fieldName)
  {
    this.message = message;
    this.fieldName = fieldName;
  }

  public ParseException(String message, int line, int column)
  {
    super(message);
  }
}
