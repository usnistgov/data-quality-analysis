/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;


public class UserAccount implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  public static final String ACCOUNT_TYPE_ADMIN = "Admin";
  public static final String ACCOUNT_TYPE_SUBMITTER = "Submitter";
  
  private String username = "";
  private String password = "";
  private String accountType = "";
  private Organization organization = null;
  private String email = "";
  private boolean isAuthenticated = false;
  
  public boolean isAuthenticated()
  {
    return isAuthenticated;
  }
  public void setAuthenticated(boolean isAuthenticated)
  {
    this.isAuthenticated = isAuthenticated;
  }
  public String getUsername()
  {
    return username;
  }
  public void setUsername(String username)
  {
    this.username = username;
  }
  public String getPassword()
  {
    return password;
  }
  public void setPassword(String password)
  {
    this.password = password;
  }
  public String getAccountType()
  {
    return accountType;
  }
  public void setAccountType(String accountType)
  {
    this.accountType = accountType;
  }
  public Organization getOrganization()
  {
    return organization;
  }
  public void setOrganization(Organization organization)
  {
    this.organization = organization;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  
}
