/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class SubmitStatus implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  public static final SubmitStatus EXCLUDED = new SubmitStatus("E", "Excluded");
  public static final SubmitStatus QUEUED = new SubmitStatus("Q", "Queued");
  public static final SubmitStatus HOLD = new SubmitStatus("H", "Hold");
  public static final SubmitStatus PREPARED = new SubmitStatus("P", "Prepared");
  public static final SubmitStatus SUBMITTED = new SubmitStatus("S", "Submitted");

  public SubmitStatus() {
    // default
  }

  public boolean isExcluded()
  {
    return EXCLUDED.getSubmitCode().equals(submitCode);
  }

  public boolean isQueued()
  {
    return QUEUED.getSubmitCode().equals(submitCode);
  }

  public boolean isHold()
  {
    return HOLD.getSubmitCode().equals(submitCode);
  }

  public boolean isPrepared()
  {
    return PREPARED.getSubmitCode().equals(submitCode);
  }

  public boolean isSubmitted()
  {
    return SUBMITTED.getSubmitCode().equals(submitCode);
  }

  public SubmitStatus(String submitCode, String submitLabel) {
    this.submitCode = submitCode;
    this.submitLabel = submitLabel;
  }

  private String submitCode = "";
  private String submitLabel = "";

  public String getSubmitCode()
  {
    return submitCode;
  }

  public void setSubmitCode(String submitCode)
  {
    this.submitCode = submitCode;
  }

  public String getSubmitLabel()
  {
    return submitLabel;
  }

  public void setSubmitLabel(String submitLabel)
  {
    this.submitLabel = submitLabel;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SubmitStatus)
    {
      return getSubmitCode().equals(((SubmitStatus) obj).getSubmitCode());
    }
    return toString().equals(obj.toString());
  }

  @Override
  public int hashCode()
  {
    return getSubmitCode().hashCode();
  }
}
