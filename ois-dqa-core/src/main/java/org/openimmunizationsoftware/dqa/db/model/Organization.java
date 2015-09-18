/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;


public class Organization implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int orgId = 0;
  private String orgLabel = "";
  private String orgLocalCode = "";
  private Organization parentOrganization = null;
  private SubmitterProfile primaryProfile = null;
  
  public int getOrgId()
  {
    return orgId;
  }
  public String getOrgLabel()
  {
    return orgLabel;
  }
  public String getOrgLocalCode()
  {
    return orgLocalCode;
  }
  public Organization getParentOrganization()
  {
    return parentOrganization;
  }
  public SubmitterProfile getPrimaryProfile()
  {
    return primaryProfile;
  }
  public void setOrgId(int orgId)
  {
    this.orgId = orgId;
  }
  public void setOrgLabel(String orgLabel)
  {
    this.orgLabel = orgLabel;
  }
  public void setOrgLocalCode(String orgLocalCode)
  {
    this.orgLocalCode = orgLocalCode;
  }
  public void setParentOrganization(Organization parentOrganization)
  {
    this.parentOrganization = parentOrganization;
  }
  public void setPrimaryProfile(SubmitterProfile primaryProfile)
  {
    this.primaryProfile = primaryProfile;
  }
}
