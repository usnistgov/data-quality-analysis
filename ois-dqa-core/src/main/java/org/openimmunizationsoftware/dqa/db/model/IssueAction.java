/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class IssueAction implements Serializable
{

  private static final long serialVersionUID = 1l;

  public static final IssueAction ERROR = new IssueAction("E", "Error");
  public static final IssueAction WARN = new IssueAction("W", "Warn");
  public static final IssueAction ACCEPT = new IssueAction("A", "Accept");
  public static final IssueAction SKIP = new IssueAction("S", "Skip");

  public String getActionLabelForMessageReceivedPastTense()
  {
    if (this.equals(ERROR))
    {
      return "Rejected with Errors";
    } else if (this.equals(WARN))
    {
      return "Accepted with Warnings";
    } else if (this.equals(ACCEPT))
    {
      return "Accepted";
    } else if (this.equals(SKIP))
    {
      return "Skipped";
    }
    return actionLabel;

  }

  public boolean isError()
  {
    return this.actionCode.equals(ERROR.actionCode);
  }

  public boolean isWarn()
  {
    return this.actionCode.equals(WARN.actionCode);
  }

  public boolean isAccept()
  {
    return this.actionCode.equals(ACCEPT.actionCode);
  }

  public boolean isSkip()
  {
    return this.actionCode.equals(SKIP.actionCode);
  }

  public IssueAction() {
    // default
  }

  private IssueAction(String actionCode, String actionLabel) {
    this.actionCode = actionCode;
    this.actionLabel = actionLabel;
  }

  private String actionCode = "";
  private String actionLabel = "";

  public String getActionCode()
  {
    return actionCode;
  }

  public void setActionCode(String actionCode)
  {
    this.actionCode = actionCode;
  }

  public String getActionLabel()
  {
    return actionLabel;
  }

  public void setActionLabel(String actionLabel)
  {
    this.actionLabel = actionLabel;
  }

  @Override
  public String toString()
  {
    return this.getActionLabel();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof IssueAction)
    {
      return this.toString().equals(((IssueAction) obj).toString());
    }
    return toString().equals(obj.toString());
  }

  @Override
  public int hashCode()
  {
    return actionCode.hashCode();
  }
}
