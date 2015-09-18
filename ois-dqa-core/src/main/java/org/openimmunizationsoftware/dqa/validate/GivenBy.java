/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.validate;

import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;

public class GivenBy extends SectionValidator
{
  public GivenBy() {
    super("Given By");
  }

  @Override
  public void validateVaccination(Vaccination vaccination, Validator validator)
  {
    super.validateVaccination(vaccination, validator);
    validator.documentParagraph("Indicates who administered the vaccination. The number should be unique " +
    		"for the submitting system. The first and last name is optional and are not validated.");
    validator.documentValuesFound("First Name", vaccination.getGivenBy().getName().getFirst(), "Last Name", vaccination
        .getGivenBy().getName().getLast(), "Number", vaccination.getGivenBy().getNumber());
    validator.handleCodeReceived(vaccination.getGivenBy(), PotentialIssues.Field.VACCINATION_GIVEN_BY,
        vaccination.isAdministered());

  }
}
