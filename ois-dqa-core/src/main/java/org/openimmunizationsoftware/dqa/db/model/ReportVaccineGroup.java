/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class ReportVaccineGroup implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int reportVaccineGroupId = 0;
  private VaccineGroup vaccineGroup = null;
  private String groupStatus = "";
  private SubmitterProfile profile = null;

  public SubmitterProfile getProfile()
  {
    return profile;
  }

  public void setProfile(SubmitterProfile profile)
  {
    this.profile = profile;
  }

  public int getReportVaccineGroupId()
  {
    return reportVaccineGroupId;
  }

  public void setReportVaccineGroupId(int reportVaccineGroupId)
  {
    this.reportVaccineGroupId = reportVaccineGroupId;
  }

  public VaccineGroup getVaccineGroup()
  {
    return vaccineGroup;
  }

  public void setVaccineGroup(VaccineGroup vaccineGroup)
  {
    this.vaccineGroup = vaccineGroup;
  }

  public String getGroupStatus()
  {
    return groupStatus;
  }

  public void setGroupStatus(String groupStatus)
  {
    this.groupStatus = groupStatus;
  }
}
