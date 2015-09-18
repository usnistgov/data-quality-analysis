/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

import java.util.Date;

import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.QueryReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.nist.Locator;
import org.openimmunizationsoftware.dqa.validate.ValidateMessage;

public abstract class VaccinationParser extends ValidateMessage
{
  public VaccinationParser(SubmitterProfile profile) {
    super(profile);
  }

  public abstract void createVaccinationUpdateMessage(MessageReceived messageReceived);

  public abstract void createQueryMessage(QueryReceived queryReceived);

  public abstract String makeAckMessage(MessageReceived messageReceived);
  
  protected Date createDate(PotentialIssue piInvalid, PotentialIssue piNoTimeZone, String fieldValue)
  {
    HL7DateAnalyzer dateAnalyzer = new HL7DateAnalyzer(fieldValue);

    if (dateAnalyzer.hasErrors())
    {
      registerIssue(piInvalid,Locator.getPath());
    }
    if (piNoTimeZone != null && !dateAnalyzer.isHasTimezone())
    {
      registerIssue(piNoTimeZone,Locator.getPath());
    }
    return dateAnalyzer.getDate();

  }

}
