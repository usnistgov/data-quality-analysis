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

public class VaccineProduct implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int productId = 0;
  private String productName = "";
  private String productLabel = "";
  private VaccineCvx cvx = null;
  private VaccineMvx mvx = null;
  private Date validStartDate = null;
  private Date useStartDate = null;
  private Date useEndDate = null;
  private Date validEndDate = null;
  
  public String getProductCode()
  {
    return cvx.getCvxCode() + "-" + mvx.getMvxCode();
  }

  public int getProductId()
  {
    return productId;
  }
  public void setProductId(int productId)
  {
    this.productId = productId;
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
  public VaccineCvx getCvx()
  {
    return cvx;
  }
  public void setCvx(VaccineCvx cvx)
  {
    this.cvx = cvx;
  }

  public VaccineMvx getMvx()
  {
    return mvx;
  }
  public void setMvx(VaccineMvx mvx)
  {
    this.mvx = mvx;
  }

  public String getProductName()
  {
    return productName;
  }
  public void setProductName(String productName)
  {
    this.productName = productName;
  }
  public String getProductLabel()
  {
    return productLabel;
  }
  public void setProductLabel(String productLabel)
  {
    this.productLabel = productLabel;
  }
}
