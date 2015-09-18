/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class MessageReceivedGeneric implements Serializable
{
  private IssueAction issueAction = null;
  private List<IssueFound> issuesFound = new ArrayList<IssueFound>();
  private SubmitterProfile profile = null;
  private Date receivedDate = new Date();
  private String requestText = null;
  private String responseText = null;
  private SubmitStatus submitStatus = null;
  private MessageHeader messageHeader = new MessageHeader();
  private boolean successfulCompletion = true;
  private Exception exception = null;
  private String messageKey = "";
  public static String looking_at = ""; 
  
  public IssueAction getIssueAction()
  {
    return issueAction;
  }
  public void setIssueAction(IssueAction issueAction)
  {
    this.issueAction = issueAction;
  }
  public List<IssueFound> getIssuesFound()
  {
    return issuesFound;
  }
  public void setIssuesFound(List<IssueFound> issuesFound)
  {
    this.issuesFound = issuesFound;
  }
  public SubmitterProfile getProfile()
  {
    return profile;
  }
  public void setProfile(SubmitterProfile profile)
  {
    this.profile = profile;
  }
  public Date getReceivedDate()
  {
    return receivedDate;
  }
  public void setReceivedDate(Date receivedDate)
  {
    this.receivedDate = receivedDate;
  }
  public String getRequestText()
  {
    return requestText;
  }
  public void setRequestText(String requestText)
  {
    this.requestText = requestText;
  }
  public String getResponseText()
  {
    return responseText;
  }
  public void setResponseText(String responseText)
  {
    this.responseText = responseText;
  }
  public SubmitStatus getSubmitStatus()
  {
    return submitStatus;
  }
  public void setSubmitStatus(SubmitStatus submitStatus)
  {
    this.submitStatus = submitStatus;
  }
  public MessageHeader getMessageHeader()
  {
    return messageHeader;
  }
  public void setMessageHeader(MessageHeader messageHeader)
  {
    this.messageHeader = messageHeader;
  }
  public boolean isSuccessfulCompletion()
  {
    return successfulCompletion;
  }
  public void setSuccessfulCompletion(boolean successfulCompletion)
  {
    this.successfulCompletion = successfulCompletion;
  }
  public Exception getException()
  {
    return exception;
  }
  public void setException(Exception exception)
  {
    this.exception = exception;
  }
  public String getMessageKey()
  {
    return messageKey;
  }
  public void setMessageKey(String messageKey)
  {
    this.messageKey = messageKey;
  }


  public boolean hasErrors()
  {
    for (IssueFound issueFound : issuesFound)
    {
      if (issueFound.getIssueAction().equals(IssueAction.ERROR))
      {
        return true;
      }
    }
    return false;
  }

  public boolean hasWarns()
  {
    for (IssueFound issueFound : issuesFound)
    {
      if (issueFound.getIssueAction().equals(IssueAction.WARN))
      {
        return true;
      }
    }
    return false;
  }


}