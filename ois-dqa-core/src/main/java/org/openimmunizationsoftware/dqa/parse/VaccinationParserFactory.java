package org.openimmunizationsoftware.dqa.parse;

import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;

public class VaccinationParserFactory
{

  public static final String HL7_START = "MSH";
  public static final String ASC_START = "Patient|";

  public static VaccinationParser createVaccinationParser(String messageBody, SubmitterProfile profile)
  {
    if (messageBody.startsWith("MSH"))
    {
      return new VaccinationParserHL7(profile);
    } else if (messageBody.startsWith(ASC_START))
    {
      return new VaccinationParserASC(profile);
    }
    throw new IllegalArgumentException("Unrecognized message content, unable to instantiate correct parser");
  }
}
