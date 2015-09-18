package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

public class Submission implements Serializable
{
  public static final String SUBMISSION_STATUS_CREATED = "C";
  public static final String SUBMISSION_STATUS_SUBMITTED = "S";
  public static final String SUBMISSION_STATUS_PROCESSING = "P";
  public static final String SUBMISSION_STATUS_FINISHED = "F";
  public static final String SUBMISSION_STATUS_DELETE = "D";
  public static final String SUBMISSION_STATUS_ERROR = "E";

  private int submissionId = 0;
  private String submitterName = "";
  private Clob requestContent = null;
  private String requestName = "";
  private SubmitterProfile profile = null;
  private boolean returnResponse = false;
  private boolean returnDetailLog = false;
  private boolean returnDetailError = false;
  private boolean returnReport = false;
  private boolean returnAnalysis = false;
  private Clob responseContent = null;
  private Clob responseDetailLog = null;
  private Clob responseDetailError = null;
  private Clob responseReport = null;
  private Clob responseAnalysis = null;
  private MessageBatch batch = null;
  private String submissionStatus = "";
  private Date submissionStatusDate = null;
  private Date createdDate = null;
  private String submitterDefinedValue1 = "";
  private String submitterDefinedValue2 = "";

  
  public int getSubmissionId()
  {
    return submissionId;
  }

  public void setSubmissionId(int submissionId)
  {
    this.submissionId = submissionId;
  }

  public String getSubmitterName()
  {
    return submitterName;
  }

  public void setSubmitterName(String submitterName)
  {
    this.submitterName = submitterName;
  }

  public Clob getRequestContent()
  {
    return requestContent;
  }

  public void setRequestContent(Clob requestContent)
  {
    this.requestContent = requestContent;
  }

  public String getRequestName()
  {
    return requestName;
  }

  public void setRequestName(String requestName)
  {
    this.requestName = requestName;
  }

  public SubmitterProfile getProfile()
  {
    return profile;
  }

  public void setProfile(SubmitterProfile profile)
  {
    this.profile = profile;
  }

  public boolean isReturnResponse()
  {
    return returnResponse;
  }

  public void setReturnResponse(boolean returnResponse)
  {
    this.returnResponse = returnResponse;
  }

  public boolean isReturnDetailLog()
  {
    return returnDetailLog;
  }

  public void setReturnDetailLog(boolean returnDetailLog)
  {
    this.returnDetailLog = returnDetailLog;
  }

  public boolean isReturnDetailError()
  {
    return returnDetailError;
  }

  public void setReturnDetailError(boolean returnDetailError)
  {
    this.returnDetailError = returnDetailError;
  }

  public boolean isReturnReport()
  {
    return returnReport;
  }

  public void setReturnReport(boolean returnReport)
  {
    this.returnReport = returnReport;
  }

  public boolean isReturnAnalysis()
  {
    return returnAnalysis;
  }

  public void setReturnAnalysis(boolean returnAnalysis)
  {
    this.returnAnalysis = returnAnalysis;
  }

  public Clob getResponseContent()
  {
    return responseContent;
  }

  public void setResponseContent(Clob responseContent)
  {
    this.responseContent = responseContent;
  }

  public Clob getResponseDetailLog()
  {
    return responseDetailLog;
  }

  public void setResponseDetailLog(Clob responseDetailLog)
  {
    this.responseDetailLog = responseDetailLog;
  }

  public Clob getResponseDetailError()
  {
    return responseDetailError;
  }

  public void setResponseDetailError(Clob responseDetailError)
  {
    this.responseDetailError = responseDetailError;
  }

  public Clob getResponseReport()
  {
    return responseReport;
  }

  public void setResponseReport(Clob responseReport)
  {
    this.responseReport = responseReport;
  }

  public Clob getResponseAnalysis()
  {
    return responseAnalysis;
  }

  public void setResponseAnalysis(Clob responseAnalysis)
  {
    this.responseAnalysis = responseAnalysis;
  }

  public MessageBatch getBatch()
  {
    return batch;
  }

  public void setBatch(MessageBatch batch)
  {
    this.batch = batch;
  }

  public String getSubmissionStatus()
  {
    return submissionStatus;
  }
  
  public String getSubmissionStatusLabel()
  {
    if (submissionStatus.equals(SUBMISSION_STATUS_CREATED))
    {
      return "Created";
    }
    if (submissionStatus.equals(SUBMISSION_STATUS_SUBMITTED))
    {
      return "Submitted";
    }
    if (submissionStatus.equals(SUBMISSION_STATUS_PROCESSING))
    {
      return "Processing";
    }
    if (submissionStatus.equals(SUBMISSION_STATUS_FINISHED))
    {
      return "Finished";
    }
    if (submissionStatus.equals(SUBMISSION_STATUS_DELETE))
    {
      return "Delete";
    }
    if (submissionStatus.equals(SUBMISSION_STATUS_ERROR))
    {
      return "Error";
    }
    return submissionStatus;
  }

  public void setSubmissionStatus(String submissionStatus)
  {
    this.submissionStatus = submissionStatus;
  }

  public Date getSubmissionStatusDate()
  {
    return submissionStatusDate;
  }

  public void setSubmissionStatusDate(Date submissionStatusDate)
  {
    this.submissionStatusDate = submissionStatusDate;
  }

  public Date getCreatedDate()
  {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate)
  {
    this.createdDate = createdDate;
  }

  public String getSubmitterDefinedValue1()
  {
    return submitterDefinedValue1;
  }

  public void setSubmitterDefinedValue1(String submitterDefinedValue1)
  {
    this.submitterDefinedValue1 = submitterDefinedValue1;
  }

  public String getSubmitterDefinedValue2()
  {
    return submitterDefinedValue2;
  }

  public void setSubmitterDefinedValue2(String submitterDefinedValue2)
  {
    this.submitterDefinedValue2 = submitterDefinedValue2;
  }
}
