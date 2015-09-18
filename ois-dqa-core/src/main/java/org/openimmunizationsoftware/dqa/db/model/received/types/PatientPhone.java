/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model.received.types;

import java.io.Serializable;

import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Skippable;

public class PatientPhone extends PhoneNumber implements Skippable, Serializable
{
  
  private static final long serialVersionUID = 1l;
 
  private long phoneId = 0;
  private Patient patient = null;
  private int positionId = 0;
  private boolean skipped = false;
  public long getPhoneId()
  {
    return phoneId;
  }
  public void setPhoneId(long phoneId)
  {
    this.phoneId = phoneId;
  }
  public Patient getPatient()
  {
    return patient;
  }
  public void setPatient(Patient patient)
  {
    this.patient = patient;
  }
  public int getPositionId()
  {
    return positionId;
  }
  public void setPositionId(int positionId)
  {
    this.positionId = positionId;
  }
  public boolean isSkipped()
  {
    return skipped;
  }
  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }


}
