/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class ReportType implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int reportTypeId = 0;
  private String reportTypeLabel = "";

  public int getReportTypeId()
  {
    return reportTypeId;
  }

  public void setReportTypeId(int reportTypeId)
  {
    this.reportTypeId = reportTypeId;
  }

  public String getReportTypeLabel()
  {
    return reportTypeLabel;
  }

  public void setReportTypeLabel(String reportTypeLabel)
  {
    this.reportTypeLabel = reportTypeLabel;
  }
}
