/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.util.Date;

import org.openimmunizationsoftware.dqa.db.model.received.Patient;

public class QueryReceived extends MessageReceivedGeneric
{

  public static final String QUERY_REQUEST_IMMUNIZATION_HISTORY = "Z34";
  
  private String messageQueryName = "";
  private String queryTag = "";
  private Patient patient = null;
  
  public boolean isRequestForImmunizationHistory()
  {
    return QUERY_REQUEST_IMMUNIZATION_HISTORY.equalsIgnoreCase(messageQueryName);
  }
  
  public String getMessageQueryName()
  {
    return messageQueryName;
  }
  public void setMessageQueryName(String messageQueryName)
  {
    this.messageQueryName = messageQueryName;
  }
  public String getQueryTag()
  {
    return queryTag;
  }
  public void setQueryTag(String queryTag)
  {
    this.queryTag = queryTag;
  }
  public Patient getPatient()
  {
    return patient;
  }
  public void setPatient(Patient patient)
  {
    this.patient = patient;
  }

}
