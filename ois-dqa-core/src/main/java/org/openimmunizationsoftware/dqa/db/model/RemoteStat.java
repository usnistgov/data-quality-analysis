/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.util.Date;

public class RemoteStat
{
  private int statId = 0;
  private RemoteConnection remoteConnection = null;
  private Date reportedDate = null;
  private Date upSinceDate = null;
  private String statusLabel = "";
  private int attemptCount = 0;
  private int sentCount = 0;
  private int errorCount = 0;

  public RemoteConnection getRemoteConnection()
  {
    return remoteConnection;
  }

  public void setRemoteConnection(RemoteConnection remoteConnection)
  {
    this.remoteConnection = remoteConnection;
  }

  public int getStatId()
  {
    return statId;
  }

  public void setStatId(int statId)
  {
    this.statId = statId;
  }

  public Date getReportedDate()
  {
    return reportedDate;
  }

  public void setReportedDate(Date reportedDate)
  {
    this.reportedDate = reportedDate;
  }

  public Date getUpSinceDate()
  {
    return upSinceDate;
  }

  public void setUpSinceDate(Date upSinceDate)
  {
    this.upSinceDate = upSinceDate;
  }

  public String getStatusLabel()
  {
    return statusLabel;
  }

  public void setStatusLabel(String statusLabel)
  {
    this.statusLabel = statusLabel;
  }

  public int getAttemptCount()
  {
    return attemptCount;
  }

  public void setAttemptCount(int attemptCount)
  {
    this.attemptCount = attemptCount;
  }

  public int getSentCount()
  {
    return sentCount;
  }

  public void setSentCount(int sentCount)
  {
    this.sentCount = sentCount;
  }

  public int getErrorCount()
  {
    return errorCount;
  }

  public void setErrorCount(int errorCount)
  {
    this.errorCount = errorCount;
  }
}
