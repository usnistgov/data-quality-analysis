/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.validate;

import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;

public class RegistryStatusValidator extends SectionValidator
{
  public RegistryStatusValidator()
  {
    super("Registry Status");
  }
  
  @Override
  public void validatePatient(MessageReceived message, Validator validator)
  {
    super.validatePatient(message, validator);
    validator.documentParagraph("Indicates the patient's current status in relation to the submitting organization. ");
    validator.documentParagraph("Note from CDC Immunization Guide: This field captures whether the sending provider organization considers this an active patient. There are several classes of responsibility. The status may be different between the sending and receiving systems. For instance, a person may no longer be active with a provider organization, but may still be active in the public health jurisdiction, which has the Immunization Information System (IIS). In this case the provider organization would indicate that the person was inactive in their system using this field in a message from them. The IIS would indicate that person was active in a message from the IIS.");
    validator.handleCodeReceived(message.getPatient().getRegistryStatus(), PotentialIssues.Field.PATIENT_REGISTRY_STATUS);
 }
}
