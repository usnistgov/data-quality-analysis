package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;
import java.sql.Clob;

public class SubmissionAnalysis implements Serializable
{
  private int submissionAnalysisId = 0;
  private Submission submission = null;
  private String analysisLabel = "";
  private MessageReceived messageReceived = null;
  private Clob analysisContent = null;

  public Submission getSubmission()
  {
    return submission;
  }

  public void setSubmission(Submission submission)
  {
    this.submission = submission;
  }

  public int getSubmissionAnalysisId()
  {
    return submissionAnalysisId;
  }

  public void setSubmissionAnalysisId(int submissionAnalysisId)
  {
    this.submissionAnalysisId = submissionAnalysisId;
  }

  public String getAnalysisLabel()
  {
    return analysisLabel;
  }

  public void setAnalysisLabel(String analysisLabel)
  {
    this.analysisLabel = analysisLabel;
  }

  public MessageReceived getMessageReceived()
  {
    return messageReceived;
  }

  public void setMessageReceived(MessageReceived messageReceived)
  {
    this.messageReceived = messageReceived;
  }

  public Clob getAnalysisContent()
  {
    return analysisContent;
  }

  public void setAnalysisContent(Clob analysisContent)
  {
    this.analysisContent = analysisContent;
  }
}
