/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageBatch implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private Map<IssueAction, BatchActions> batchActionsMap = new HashMap<IssueAction, BatchActions>();
  private Map<CodeReceived, BatchCodeReceived> batchCodeReceivedMap = new HashMap<CodeReceived, BatchCodeReceived>();
  private int batchId = 0;
  private Map<PotentialIssue, BatchIssues> batchIssuesMap = new HashMap<PotentialIssue, BatchIssues>();
  private String batchTitle = "";
  private BatchType batchType = null;
  private Map<VaccineCvx, BatchVaccineCvx> batchVaccineCvxMap = new HashMap<VaccineCvx, BatchVaccineCvx>();
  private Date endDate = null;
  private SubmitterProfile profile = null;
  private BatchReport batchReport = new BatchReport();
  private Date startDate = null;
  private SubmitStatus submitStatus = null;
  
  public MessageBatch()
  {
    batchReport.setMessageBatch(this);
  }
  
  public void addToCounts(MessageBatch messageBatch)
  {
    this.getBatchReport().addToCounts(messageBatch.getBatchReport());
  }
  
  public BatchActions getBatchActions(IssueAction issueAction)
  {
    BatchActions batchActions = batchActionsMap.get(issueAction);
    if (batchActions == null)
    {
      batchActions = new BatchActions(issueAction, this);
      batchActionsMap.put(issueAction, batchActions);
    }
    return batchActions;
  }
  
  public Map<IssueAction, BatchActions> getBatchActionsMap()
  {
    return batchActionsMap;
  }

  public BatchCodeReceived getBatchCodeReceived(CodeReceived codeReceived)
  {
    BatchCodeReceived batchCodeReceived = batchCodeReceivedMap.get(codeReceived);
    if (batchCodeReceived == null)
    {
      batchCodeReceived = new BatchCodeReceived(codeReceived, this);
      batchCodeReceivedMap.put(codeReceived, batchCodeReceived);
    }
    return batchCodeReceived;
  }

  public Map<CodeReceived, BatchCodeReceived> getBatchCodeReceivedMap()
  {
    return batchCodeReceivedMap;
  }

  public int getBatchId()
  {
    return batchId;
  }

  public BatchIssues getBatchIssues(PotentialIssue potentialIssue)
  {
    BatchIssues batchIssues = batchIssuesMap.get(potentialIssue);
    if (batchIssues == null)
    {
      batchIssues = new BatchIssues(potentialIssue, this);
      batchIssuesMap.put(potentialIssue, batchIssues);
    }
    return batchIssues;
  }

  public Map<PotentialIssue, BatchIssues> getBatchIssuesMap()
  {
    return batchIssuesMap;
  }
  
  public String getBatchTitle()
  {
    return batchTitle;
  }
  
  public BatchType getBatchType()
  {
    return batchType;
  }
  
  public BatchVaccineCvx getBatchVaccineCvx(VaccineCvx vaccineCvx)
  {
    BatchVaccineCvx batchVaccineCvx = batchVaccineCvxMap.get(vaccineCvx);
    if (batchVaccineCvx == null)
    {
      batchVaccineCvx = new BatchVaccineCvx(vaccineCvx, this);
      batchVaccineCvxMap.put(vaccineCvx, batchVaccineCvx);
    }
    return batchVaccineCvx;
  }

  public Map<VaccineCvx, BatchVaccineCvx> getBatchVaccineCvxMap()
  {
    return batchVaccineCvxMap;
  }
  public Date getEndDate()
  {
    return endDate;
  }

  public SubmitterProfile getProfile()
  {
    return profile;
  }

  public BatchReport getBatchReport()
  {
    return batchReport;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public SubmitStatus getSubmitStatus()
  {
    return submitStatus;
  }

  public void incBatchActionCount(IssueAction issueAction)
  {
    getBatchActions(issueAction).incActionCount();
  }

  public void incBatchIssueCount(PotentialIssue potentialIssue)
  {
    BatchIssues batchIssues = getBatchIssues(potentialIssue);
    batchIssues.incIssueCount();
  }

  public void setBatchActions(BatchActions batchActions)
  {
    this.batchActionsMap.put(batchActions.getIssueAction(), batchActions);
  }

  public void setBatchId(int batchId)
  {
    this.batchId = batchId;
  }

  public void setBatchTitle(String batchTitle)
  {
    this.batchTitle = batchTitle;
  }

  public void setBatchType(BatchType batchType)
  {
    this.batchType = batchType;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public void setProfile(SubmitterProfile profile)
  {
    this.profile = profile;
  }

  public void setBatchReport(BatchReport report)
  {
    this.batchReport = report;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public void setSubmitStatus(SubmitStatus submitStatus)
  {
    this.submitStatus = submitStatus;
  }


}
