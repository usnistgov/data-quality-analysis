/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.validate;

import org.openimmunizationsoftware.dqa.db.model.MessageHeader;
import org.openimmunizationsoftware.dqa.db.model.KeyedSetting;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.validate.immtrac.PfsSupport;

public class FacilityValidator extends SectionValidator
{
  public FacilityValidator() {
    super("Facility");
  }

  @Override
  public void validateHeader(MessageReceived message, Validator validator)
  {
    super.validateHeader(message, validator);
    MessageHeader header = message.getMessageHeader();
    validator.documentParagraph("Indicates the facility or organization that originated this message. "
        + "This is normally assigned by the receiving system. In HL7 this can be sent in MSH-4. ");
    validator.documentValuesFound("Sending Facility", header.getSendingFacility());
    if (validator.notEmpty(header.getSendingFacility(), pi.Hl7MshSendingFacilityIsMissing,header.locate("this"),header.locate("sendingFacility")))
    {
      String id = header.getSendingFacility();
//      boolean mustBePfs = validator.ksm.getKeyedValueBoolean(KeyedSetting.VALIDATE_HEADER_SENDING_FACILITY_PFS, false);
//      if (mustBePfs)
//      {
//        validator.documentParagraph("Facility id must be a valid ImmTrac PFS number. "
//            + "Please contact ImmTrac to obtain a correct and valid PFS number.");
//        if (!PfsSupport.verifyCorrect(id))
//        {
//          validator.registerIssue(pi.Hl7MshSendingFacilityIsInvalid);
//        }
//      } else
//      {
//        int minLen = validator.ksm.getKeyedValueInt(KeyedSetting.VALIDATE_HEADER_SENDING_FACILITY_MIN_LEN, 0);
//        int maxLen = validator.ksm.getKeyedValueInt(KeyedSetting.VALIDATE_HEADER_SENDING_FACILITY_MAX_LEN, 30);
//        boolean mustBeNumeric = validator.ksm.getKeyedValueBoolean(KeyedSetting.VALIDATE_HEADER_SENDING_FACILITY_NUMERIC, false);
//        if (validator.isDocument())
//        {
//          if (minLen == maxLen)
//          {
//            validator.documentParagraph("A valid facility id must be" + (mustBeNumeric ? " numeric and " : " ") + minLen + " characters long. ");
//
//          } else
//          {
//            validator.documentParagraph("A valid facility id must be" + (mustBeNumeric ? " numeric and " : " ") + "between " + minLen + " and "
//                + maxLen + " characters long. ");
//
//          }
//        }
//        if (!goodId(id, minLen, maxLen, mustBeNumeric))
//        {
//          validator.registerIssue(pi.Hl7MshSendingFacilityIsInvalid);
//        }
//      }
    }

  }

  @Override
  public void validateVaccination(Vaccination vaccination, Validator validator)
  {
    super.validateVaccination(vaccination, validator);
    if (vaccination.isAdministered())
    {
      validator.documentParagraph("Indicates in which facility or organization the vaccination was "
          + "administered at. (Do not use to indicate the body site.) This field is normally expected when "
          + "reporting administered vaccinations in order to account for where the vaccination was administered "
          + "and to support vaccination inventory functions. For historical vaccinations this is not normally indicated. "
          + "In addition, if the submitter receives vaccines for children (free vaccines) from the state, it"
          + "is important that this field indicates the receiving location (organization with the VFC refrigerator) "
          + "that matches what is expected from the state registry. ");
      validator.documentValuesFound("Vaccination Facility Id Number", vaccination.getFacility().getId().getNumber());
      if (validator.notEmpty(vaccination.getFacility().getIdNumber(), pi.VaccinationFacilityIdIsMissing, vaccination.isAdministered(),vaccination.locate("this"),vaccination.getFacility().locate("idNumberId")))
      {
        String id = vaccination.getFacility().getId().getNumber();
        boolean mustBePfs = validator.ksm.getKeyedValueBoolean(KeyedSetting.VALIDATE_VACCINATION_FACILITY_PFS, false);
        if (mustBePfs && !PfsSupport.verifyCorrect(id))
        {
          validator.documentParagraph("Vaccination facility id must be a valid ImmTrac PFS number. "
              + "Please contact ImmTrac to obtain a correct and valid PFS number.");
          validator.registerIssue(pi.VaccinationFacilityIdIsInvalid,vaccination.getFacility().getId().locate("number"));
        } else
        {
          int minLen = validator.ksm.getKeyedValueInt(KeyedSetting.VALIDATE_VACCINATION_FACILITY_MIN_LEN, 0);
          int maxLen = validator.ksm.getKeyedValueInt(KeyedSetting.VALIDATE_VACCINATION_FACILITY_MAX_LEN, 30);
          boolean mustBeNumeric = validator.ksm.getKeyedValueBoolean(KeyedSetting.VALIDATE_VACCINATION_FACILITY_NUMERIC, false);
          if (validator.isDocument())
          {
            if (minLen == maxLen)
            {
              validator.documentParagraph("A valid facility id must be" + (mustBeNumeric ? " numeric and " : " ") + minLen + " characters long. ");

            } else
            {
              validator.documentParagraph("A valid facility id must be" + (mustBeNumeric ? " numeric and " : " ") + "between " + minLen + " and "
                  + maxLen + " characters long. ");

            }
          }
          if (!goodId(id, minLen, maxLen, mustBeNumeric))
          {
            validator.registerIssue(pi.VaccinationFacilityIdIsInvalid,vaccination.getFacility().getId().locate("number"));
          }
        }
      }
    } else
    {
      validator.documentParagraph("Facility where vaccination is given at is not validated when the vaccination is not administered.");
    }
  }

  protected static boolean goodId(String sf, int minLen, int maxLen, boolean validateNumeric)
  {
    boolean good = sf.length() >= minLen && sf.length() <= maxLen;
    if (good)
    {
      if (validateNumeric)
      {
        for (char c : sf.toCharArray())
        {
          if (c < '0' || c > '9')
          {
            good = false;
            break;
          }
        }
      }
    }
    return good;
  }

}
