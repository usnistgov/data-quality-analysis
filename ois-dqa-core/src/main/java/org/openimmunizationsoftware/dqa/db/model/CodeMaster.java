/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class CodeMaster implements Serializable
{
  
  private static final long serialVersionUID = 2l;
  
  
  private int codeMasterId = 0;
  private CodeTable table = null;
  private String codeValue = "";
  private String codeLabel = "";
  private String useValue = "";
  private CodeStatus codeStatus = null;
  private CodeMaster context = null;
  private CodeTable indicates = null;
  
  public CodeTable getIndicates()
  {
    return indicates;
  }
  public void setIndicates(CodeTable indicates)
  {
    this.indicates = indicates;
  }
  public CodeMaster getContext()
  {
    return context;
  }
  public void setContext(CodeMaster context)
  {
    this.context = context;
  }
  
  public String getContextValue()
  {
    if (context == null)
    {
      return null;
    }
    else
    {
      String parentContextValue = context.getContextValue();
      if (parentContextValue == null)
      {
        return context.getCodeValue();
      }
      return parentContextValue + "-" + context.getCodeValue();
    }
  }
  
  public int getCodeMasterId()
  {
    return codeMasterId;
  }
  public void setCodeMasterId(int codeMasterId)
  {
    this.codeMasterId = codeMasterId;
  }
  public CodeTable getTable()
  {
    return table;
  }
  public void setTable(CodeTable table)
  {
    this.table = table;
  }
  public String getCodeValue()
  {
    return codeValue;
  }
  public void setCodeValue(String codeValue)
  {
    this.codeValue = codeValue;
  }
  public String getCodeLabel()
  {
    return codeLabel;
  }
  public void setCodeLabel(String codeLabel)
  {
    this.codeLabel = codeLabel;
  }
  public String getUseValue()
  {
    return useValue;
  }
  public void setUseValue(String useValue)
  {
    this.useValue = useValue;
  }
  public CodeStatus getCodeStatus()
  {
    return codeStatus;
  }
  public void setCodeStatus(CodeStatus codeStatus)
  {
    this.codeStatus = codeStatus;
  }
  
}
