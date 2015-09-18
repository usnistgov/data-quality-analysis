/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class BatchIssues implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int batchIssuesId = 0;
  private MessageBatch messageBatch = null;
  private PotentialIssue issue = null;
  private int issueCount = 0;
  
  public BatchIssues()
  {
    // default
  }
  
  public void inc(BatchIssues batchIssues)
  {
    this.issueCount += batchIssues.getIssueCount();
  }
  
  public BatchIssues(PotentialIssue issue, MessageBatch messageBatch)
  {
    this.issue = issue;
    this.messageBatch = messageBatch;
  }
  
  public int getBatchIssuesId()
  {
    return batchIssuesId;
  }
  public void setBatchIssuesId(int batchIssuesId)
  {
    this.batchIssuesId = batchIssuesId;
  }
  public MessageBatch getMessageBatch()
  {
    return messageBatch;
  }
  public void setMessageBatch(MessageBatch messageBatch)
  {
    this.messageBatch = messageBatch;
  }
  public PotentialIssue getIssue()
  {
    return issue;
  }
  public void setIssue(PotentialIssue issue)
  {
    this.issue = issue;
  }
  public int getIssueCount()
  {
    return issueCount;
  }
  public void incIssueCount()
  {
    issueCount++;
  }
  public void setIssueCount(int issueCount)
  {
    this.issueCount = issueCount;
  }
}
