/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class CodeReceived implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private long codeId = 0l;
  private SubmitterProfile profile = null;
  private CodeTable table = null;
  private String receivedValue = "";
  private String codeValue = "";
  private String codeLabel = "";
  private CodeStatus codeStatus = null;
  private int receivedCount = 0;  
  private String contextValue = "";
  
  public String getContextValue()
  {
    return contextValue;
  }

  public void setContextValue(String contextValue)
  {
    this.contextValue = contextValue;
  }

  public CodeReceived()
  {
    // default
  }
  
  public CodeReceived(SubmitterProfile profile, CodeTable table, String receivedValue)
  {
    this.profile = profile;
    this.table = table;
    this.receivedValue = receivedValue;
  }
  
  public CodeReceived(CodeReceived parent, SubmitterProfile profile, String codeLabel)
  {
    this.profile = profile;
    this.table = parent.table;
    this.receivedValue = parent.receivedValue;
    this.codeValue = parent.codeValue;
    this.codeStatus = parent.codeStatus;
    this.receivedCount = 0;
    this.codeLabel = codeLabel;
    this.contextValue = parent.contextValue;
  }
  
  public long getCodeId()
  {
    return codeId;
  }
  public void setCodeId(long codeId)
  {
    this.codeId = codeId;
  }
  public SubmitterProfile getProfile()
  {
    return profile;
  }
  public String getCodeLabel()
  {
    return codeLabel;
  }

  public void setCodeLabel(String codeLabel)
  {
    this.codeLabel = codeLabel;
  }

  public void setProfile(SubmitterProfile profile)
  {
    this.profile = profile;
  }
  public CodeTable getTable()
  {
    return table;
  }
  public void setTable(CodeTable table)
  {
    this.table = table;
  }
  public String getReceivedValue()
  {
    return receivedValue;
  }
  public String getContextWithCodeValue()
  {
    if (contextValue != null && !contextValue.equals(""))
    {
      return contextValue + "-" + codeValue;
    }
    return codeValue;
  }
  
  public void setReceivedValue(String receivedValue)
  {
    this.receivedValue = receivedValue.toUpperCase();
  }
  public String getCodeValue()
  {
    return codeValue;
  }
  public void setCodeValue(String codeValue)
  {
    this.codeValue = codeValue;
  }
  public CodeStatus getCodeStatus()
  {
    return codeStatus;
  }
  public void setCodeStatus(CodeStatus codeStatus)
  {
    this.codeStatus = codeStatus;
  }
  public int getReceivedCount()
  {
    return receivedCount;
  }
  public void setReceivedCount(int receivedCount)
  {
    this.receivedCount = receivedCount;
  }
  
  public void incReceivedCount()
  {
    this.receivedCount++;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof CodeReceived)
    {
      CodeReceived compareTo = (CodeReceived) obj;
      if (getProfile() != null && getTable() != null & getReceivedValue() != null)
      {
        return getProfile().equals(compareTo.getProfile()) && getTable().equals(compareTo.getTable()) && getReceivedValue().equals(compareTo.getReceivedValue());        
      }
    }
    return super.equals(obj);
  }
  
  @Override
  public int hashCode()
  {
    if (getProfile() != null && getTable() != null & getReceivedValue() != null)
    {
      return (getProfile().getProfileId() + "." + getTable().getTableId() + "." + getReceivedValue()).hashCode();
    }
    return super.hashCode();
  }
}
