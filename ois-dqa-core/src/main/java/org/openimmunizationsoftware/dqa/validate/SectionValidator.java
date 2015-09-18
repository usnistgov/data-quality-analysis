/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.validate;

import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;

public class SectionValidator
{
  protected PotentialIssues pi = PotentialIssues.getPotentialIssues();
  private String fieldName = "";

  public SectionValidator(String fieldName) {
    this.fieldName = fieldName;
  }

  public void validateVaccination(Vaccination vaccination, Validator validator)
  {
    validator.documentHeaderSub("Vaccination " + fieldName);
  }

  public void validatePatient(MessageReceived message, Validator validator)
  {
    validator.documentHeaderSub("Patient " + fieldName);
  }

  public void validateHeader(MessageReceived message, Validator validator)
  {
    validator.documentHeaderSub("Header " + fieldName);
  }
}
