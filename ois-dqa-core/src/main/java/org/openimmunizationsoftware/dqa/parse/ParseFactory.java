/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.validate.ValidateMessage;



public class ParseFactory
{
  public static ValidateMessage getVaccinationUpdateParser(SubmitterProfile profile)
  {
    return new VaccinationParserHL7(profile);
  }
}
