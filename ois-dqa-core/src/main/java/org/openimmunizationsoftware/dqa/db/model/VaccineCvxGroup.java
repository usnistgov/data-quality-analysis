/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class VaccineCvxGroup implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int cvxGroupId = 0;
  private VaccineCvx vaccineCvx = null;
  private VaccineGroup vaccineGroup = null;

  public int getCvxGroupId()
  {
    return cvxGroupId;
  }

  public VaccineCvx getVaccineCvx()
  {
    return vaccineCvx;
  }

  public VaccineGroup getVaccineGroup()
  {
    return vaccineGroup;
  }

  public void setCvxGroupId(int cvxGroupId)
  {
    this.cvxGroupId = cvxGroupId;
  }

  public void setVaccineCvx(VaccineCvx vaccineCvx)
  {
    this.vaccineCvx = vaccineCvx;
  }

  public void setVaccineGroup(VaccineGroup vaccineGroup)
  {
    this.vaccineGroup = vaccineGroup;
  }
}
