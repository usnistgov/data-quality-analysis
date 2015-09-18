/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

import org.openimmunizationsoftware.dqa.quality.ToolTip;

public class PotentialIssue implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  public static final String CHANGE_PRIORITY_BLOCKED = "Blocked";
  public static final String CHANGE_PRIORITY_CAN = "Can";
  public static final String CHANGE_PRIORITY_MAY = "May";
  public static final String CHANGE_PRIORITY_MUST = "Must";
  public static final String CHANGE_PRIORITY_SHOULD = "Should";

  public static final String ISSUE_TYPE_EXCEPTION = "exception";
  public static final String ISSUE_TYPE_IS_BEFORE_BIRTH = "is before birth";
  public static final String ISSUE_TYPE_IS_DEPRECATE = "is deprecated";
  public static final String ISSUE_TYPE_IS_IGNORED = "is ignored";
  public static final String ISSUE_TYPE_IS_IN_FUTURE = "is in future";
  public static final String ISSUE_TYPE_IS_INCOMPLETE = "is incomplete";
  public static final String ISSUE_TYPE_IS_INVALID = "is invalid";
  public static final String ISSUE_TYPE_IS_MISSING = "is missing";
  public static final String ISSUE_TYPE_IS_REPEATED = "is repeated";
  public static final String ISSUE_TYPE_IS_UNRECOGNIZED = "is unrecognized";
  public static final String ISSUE_TYPE_IS_VALUED_AS = "is valued as";
  
  private String changePriority = "";
  private IssueAction defaultIssueAction = null;
  private String fieldValue = "";
  private String issueDescription = "";
  private int issueId = 0;
  private String issueType = "";
  private String reportDenominator;
  private String targetField = "";
  private String targetObject = "";
  private ToolTip toolTip = null;
  private CodeTable table = null;
  private String hl7Reference = null;
  private String hl7ErrorCode = null;
  private String appErrorCode = null;
  
  private String draft = null;

  public PotentialIssue lastSeen(String path){
	  this.draft = path;
	  return this;
  }
  
  public String getAppErrorCode()
  {
    return appErrorCode;
  }

  public void setAppErrorCode(String appErrorCode)
  {
    this.appErrorCode = appErrorCode;
  }

  public String getHl7ErrorCode()
  {
    return hl7ErrorCode;
  }

  public void setHl7ErrorCode(String hl7ErrorCode)
  {
    this.hl7ErrorCode = hl7ErrorCode;
  }

  public String getHl7Reference()
  {
    return hl7Reference;
  }

  public void setHl7Reference(String hl7Reference)
  {
    this.hl7Reference = hl7Reference;
  }

  public ToolTip getToolTip()
  {
    if (toolTip == null)
    {
      toolTip = new ToolTip(getDisplayText(), issueDescription);
    }
    return toolTip;
  }
  
  public CodeTable getTable()
  {
    return table;
  }
  public void setTable(CodeTable table)
  {
    this.table = table;
  }

  public PotentialIssue() {
    // default
  }

  public String getChangePriority()
  {
    return changePriority;
  }

  public IssueAction getDefaultIssueAction()
  {
    return defaultIssueAction;
  }

  public String getDisplayText()
  {
    StringBuilder displayText = new StringBuilder();

    displayText.append(targetObject + " " + targetField + " " + issueType);
    if (fieldValue != null && !fieldValue.equals(""))
    {
      displayText.append(" " + fieldValue);
    }
    return displayText.toString();
  }

  public String getFieldValue()
  {
    return fieldValue;
  }

  public String getIssueDescription()
  {
    return issueDescription;
  }

  public int getIssueId()
  {
    return issueId;
  }

  public String getIssueType()
  {
    return issueType;
  }

  public String getReportDenominator()
  {
    return reportDenominator;
  }

  public String getTargetField()
  {
    return targetField;
  }

  public String getTargetObject()
  {
    return targetObject;
  }

  public void setChangePriority(String changePriority)
  {
    this.changePriority = changePriority;
  }

  public void setDefaultIssueAction(IssueAction defaultIssueAction)
  {
    this.defaultIssueAction = defaultIssueAction;
  }

  public void setFieldValue(String fieldValue)
  {
    this.fieldValue = fieldValue;
  }

  public void setIssueDescription(String issueDescription)
  {
    this.issueDescription = issueDescription;
  }

  public void setIssueId(int issueId)
  {
    this.issueId = issueId;
  }

  public void setIssueType(String issueType)
  {
    this.issueType = issueType;
  }
  
  public void setReportDenominator(String reportDenominator)
  {
    this.reportDenominator = reportDenominator;
  }
  
  public void setTargetField(String targetField)
  {
    this.targetField = targetField;
  }

  public void setTargetObject(String targetObject)
  {
    this.targetObject = targetObject;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof PotentialIssue)
    {
      return issueId == ((PotentialIssue) obj).getIssueId();
    }
    return super.equals(obj);
  }
  
  @Override
  public int hashCode()
  {
    return issueId;
  }

public String getDraft() {
	return draft;
}

public void setDraft(String draft) {
	this.draft = draft;
}
  
}
