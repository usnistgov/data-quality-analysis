/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model.received.types;

import java.io.Serializable;

import org.openimmunizationsoftware.dqa.db.model.CodeTable.Type;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Skippable;
import org.openimmunizationsoftware.dqa.nist.Extracted;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class PatientImmunity  extends Extracted implements Skippable, Serializable
{
  private int immunityId = 0;
  private Patient patient = null;
  private boolean skipped = false;
  private CodedEntity immunity = new CodedEntity(Type.EVIDENCE_OF_IMMUNITY);
  
  public String getImmunityCode()
  {
    return immunity.getCode();
  }
  
  public void setImmunityCode(String immunityCode)
  {
    immunity.setCode(immunityCode);
  }
  
  public CodedEntity getImmunity()
  {
    return immunity;
  }
  public int getImmunityId()
  {
    return immunityId;
  }
  public void setImmunityId(int immunityId)
  {
	  this.put("immunityId", Locator.getPath());  
    this.immunityId = immunityId;
  }
  public Patient getPatient()
  {
    return patient;
  }
  public void setPatient(Patient patient)
  {
    this.patient = patient;
  }
  public boolean isSkipped()
  {
    return skipped;
  }
  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

public void setImmunityCode(String observationValue, String locate) {
	immunity.setCode(observationValue,locate);
	
}


}
