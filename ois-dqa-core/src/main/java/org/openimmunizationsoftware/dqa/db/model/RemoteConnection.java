/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

public class RemoteConnection
{
  private int connectionId = 0;
  private String connectionLabel = "";
  private String connectionCode = "";
  private String supportCenterCode = "";
  private String locationTo = "";
  private String locationFrom = "";
  private String accountName = "";
  private int logLevel = 0;

  public int getConnectionId()
  {
    return connectionId;
  }

  public void setConnectionId(int connectionId)
  {
    this.connectionId = connectionId;
  }

  public String getConnectionLabel()
  {
    return connectionLabel;
  }

  public void setConnectionLabel(String connectionLabel)
  {
    this.connectionLabel = connectionLabel;
  }

  public String getConnectionCode()
  {
    return connectionCode;
  }

  public void setConnectionCode(String connectionCode)
  {
    this.connectionCode = connectionCode;
  }

  public String getSupportCenterCode()
  {
    return supportCenterCode;
  }

  public void setSupportCenterCode(String supportCenterCode)
  {
    this.supportCenterCode = supportCenterCode;
  }

  public String getLocationTo()
  {
    return locationTo;
  }

  public void setLocationTo(String locationTo)
  {
    this.locationTo = locationTo;
  }

  public String getLocationFrom()
  {
    return locationFrom;
  }

  public void setLocationFrom(String locationFrom)
  {
    this.locationFrom = locationFrom;
  }

  public String getAccountName()
  {
    return accountName;
  }

  public void setAccountName(String accountName)
  {
    this.accountName = accountName;
  }

  public int getLogLevel()
  {
    return logLevel;
  }

  public void setLogLevel(int logLevel)
  {
    this.logLevel = logLevel;
  }
}
