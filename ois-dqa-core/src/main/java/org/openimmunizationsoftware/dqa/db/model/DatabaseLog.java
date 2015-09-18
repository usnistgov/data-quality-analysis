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

public class DatabaseLog implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int changeId = 0;
  private Date changeDate = null;
  private String changeVersion = "";
  private String changeComment = "";

  public int getChangeId()
  {
    return changeId;
  }
  
  public void setChangeId(int changeId)
  {
    this.changeId = changeId;
  }
  
  public Date getChangeDate()
  {
    return changeDate;
  }

  public void setChangeDate(Date changeDate)
  {
    this.changeDate = changeDate;
  }

  public String getChangeVersion()
  {
    return changeVersion;
  }

  public void setChangeVersion(String changeVersion)
  {
    this.changeVersion = changeVersion;
  }

  public String getChangeComment()
  {
    return changeComment;
  }

  public void setChangeComment(String changeComment)
  {
    this.changeComment = changeComment;
  }
}
