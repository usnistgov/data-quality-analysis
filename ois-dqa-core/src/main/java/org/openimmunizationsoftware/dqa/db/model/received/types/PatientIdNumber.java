/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model.received.types;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class PatientIdNumber extends Id
{
  public PatientIdNumber()
  {
    super(CodeTable.Type.PATIENT_ID);
  }
  
  private int idNumberId = 0;
  private Patient patient = null;
  private int positionId = 0;
  private boolean skipped = false;
  
  public int getIdNumberId()
  {
    return idNumberId;
  }
  public void setIdNumberId(int idNumberId)
  {
	this.put("idNumberId", Locator.getPath());  
    this.idNumberId = idNumberId;
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
