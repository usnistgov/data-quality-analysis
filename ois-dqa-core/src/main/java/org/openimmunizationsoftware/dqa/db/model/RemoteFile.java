/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.util.Date;

public class RemoteFile
{
  private int fileId = 0;
  private RemoteStat remoteStat = null;
  private Date reportedDate = null;
  private String fileName = "";
  private String statusLabel = "";
  private int messageCount = 0;
  private int sentCount = 0;
  private int errorCount = 0;
  public int getFileId()
  {
    return fileId;
  }
  public void setFileId(int fileId)
  {
    this.fileId = fileId;
  }
  public RemoteStat getRemoteStat()
  {
    return remoteStat;
  }
  public void setRemoteStat(RemoteStat remoteStat)
  {
    this.remoteStat = remoteStat;
  }
  public Date getReportedDate()
  {
    return reportedDate;
  }
  public void setReportedDate(Date reportedDate)
  {
    this.reportedDate = reportedDate;
  }
  public String getFileName()
  {
    return fileName;
  }
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  public String getStatusLabel()
  {
    return statusLabel;
  }
  public void setStatusLabel(String statusLabel)
  {
    this.statusLabel = statusLabel;
  }
  public int getMessageCount()
  {
    return messageCount;
  }
  public void setMessageCount(int messageCount)
  {
    this.messageCount = messageCount;
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