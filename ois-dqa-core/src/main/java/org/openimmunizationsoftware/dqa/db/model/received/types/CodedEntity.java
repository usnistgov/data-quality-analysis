/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model.received.types;

import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.nist.Extracted;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class CodedEntity extends Extracted
{
  private String code = "";
  private String text = "";
  private String table = "";
  private String altCode = "";
  private String altText = "";
  private String altTable = "";
  private CodeTable.Type tableType = null;
  private CodeReceived codeReceived = null;
  
  public boolean isEmpty()
  {
    return code == null || code.equals("");
  }

  public boolean isValid()
  {
    return codeReceived != null && codeReceived.getCodeStatus().isValid();
  }

  public boolean isInvalid()
  {
    return codeReceived != null && codeReceived.getCodeStatus().isInvalid();
  }

  public boolean isUnrecognized()
  {
    return codeReceived != null && codeReceived.getCodeStatus().isUnrecognized();
  }

  public boolean isDeprecated()
  {
    return codeReceived != null && codeReceived.getCodeStatus().isDeprecated();
  }

  public boolean isIgnored()
  {
    return codeReceived != null && codeReceived.getCodeStatus().isIgnored();
  }

  public CodedEntity(CodeTable.Type tableType) {
    this.tableType = tableType;
  }

  public CodeReceived getCodeReceived()
  {
    return codeReceived;
  }

  public void setCodeReceived(CodeReceived codeReceived)
  {
    this.codeReceived = codeReceived;
  }

  public CodeTable.Type getTableType()
  {
    return tableType;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
	this.put("code", Locator.getPath());
    this.code = nullSafe(code);
  }
  
  public void setCode(String code,String where)
  {
	location.put("code", where);
    this.code = nullSafe(code);
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
	this.put("text", Locator.getPath());
    this.text = nullSafe(text);
  }

  public String getTable()
  {
    return table;
  }

  public void setTable(String table)
  {
	this.put("table", Locator.getPath());
    this.table = nullSafe(table);
  }

  public String getAltCode()
  {
    return altCode;
  }

  public void setAltCode(String altCode)
  {
    this.put("altCode", Locator.getPath());
    this.altCode = altCode;
  }

  public String getAltText()
  {
    return altText;
  }

  public void setAltText(String altText)
  {
		this.put("altText", Locator.getPath());
    this.altText = nullSafe(altText);
  }

  public String getAltTable()
  {
    return altTable;
  }

  public void setAltTable(String altTable)
  {
		this.put("altTable", Locator.getPath());
    this.altTable = nullSafe(altTable);
  }
  
  private static String nullSafe(String s)
  {
    if (s == null)
    {
      return "";
    }
    else
    {
      return s;
    }
  }
}
