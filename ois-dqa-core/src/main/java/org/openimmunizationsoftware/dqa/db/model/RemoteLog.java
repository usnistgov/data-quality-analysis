/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.util.Date;

public class RemoteLog
{
  private int logId = 0;
  private RemoteStat remoteStat = null;
  private Date reportedDate = null;
  private int logLevel = 0;
  private String issueText = "";
  private String exceptionTrace = "";

  public Date getReportedDate()

  {
    return reportedDate;
  }
  
  public void setReportedDate(Date reportedDate)
  {
    this.reportedDate = reportedDate;
  }
  
  public int getLogId()
  {
    return logId;
  }

  public void setLogId(int logId)
  {
    this.logId = logId;
  }

  public RemoteStat getRemoteStat()
  {
    return remoteStat;
  }

  public void setRemoteStat(RemoteStat remoteStat)
  {
    this.remoteStat = remoteStat;
  }

  public int getLogLevel()
  {
    return logLevel;
  }

  public void setLogLevel(int logLevel)
  {
    this.logLevel = logLevel;
  }

  public String getIssueText()
  {
    return issueText;
  }

  public void setIssueText(String issueText)
  {
    this.issueText = issueText;
  }

  public String getExceptionTrace()
  {
    return exceptionTrace;
  }

  public void setExceptionTrace(String exceptionTrace)
  {
    this.exceptionTrace = exceptionTrace;
  }
}
