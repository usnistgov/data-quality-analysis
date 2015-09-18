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

public class OrderControlValidator extends SectionValidator
{
  public OrderControlValidator() {
    super("Order Control");
  }

  @Override
  public void validateVaccination(Vaccination vaccination, Validator validator)
  {
    super.validateVaccination(vaccination, validator);
    validator.documentParagraph("The vaccination order control id should always be RE.");
    validator.documentValuesFound("Vaccination Order Control Code", vaccination.getOrderControlCode());
    validator.handleCodeReceived(vaccination.getOrderControl(), PotentialIssues.Field.VACCINATION_ORDER_CONTROL_CODE,
        !validator.hasIssue(pi.Hl7OrcSegmentIsMissing));
  }
}
