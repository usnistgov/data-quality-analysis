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

public class VaccineMvx implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private String mvxCode = "";
  private String mvxLabel = "";
  private Date validStartDate = null;
  private Date useStartDate = null;
  private Date useEndDate = null;
  private Date validEndDate = null;

  public String getMvxCode()
  {
    return mvxCode;
  }

  public void setMvxCode(String mvxCode)
  {
    this.mvxCode = mvxCode;
  }

  public String getMvxLabel()
  {
    return mvxLabel;
  }

  public void setMvxLabel(String mvxLabel)
  {
    this.mvxLabel = mvxLabel;
  }

  public Date getValidStartDate()
  {
    return validStartDate;
  }

  public void setValidStartDate(Date validStartDate)
  {
    this.validStartDate = validStartDate;
  }

  public Date getUseStartDate()
  {
    return useStartDate;
  }

  public void setUseStartDate(Date useStartDate)
  {
    this.useStartDate = useStartDate;
  }

  public Date getUseEndDate()
  {
    return useEndDate;
  }

  public void setUseEndDate(Date useEndDate)
  {
    this.useEndDate = useEndDate;
  }

  public Date getValidEndDate()
  {
    return validEndDate;
  }

  public void setValidEndDate(Date validEndDate)
  {
    this.validEndDate = validEndDate;
  }

  @Override
  public boolean equals(Object arg0)
  {
    if (arg0 instanceof VaccineMvx)
    {
      return ((VaccineMvx) arg0).getMvxCode().equals(mvxCode);
    }
    return super.equals(arg0);
  }

  @Override
  public int hashCode()
  {
    return getMvxCode().hashCode();
  }

}
