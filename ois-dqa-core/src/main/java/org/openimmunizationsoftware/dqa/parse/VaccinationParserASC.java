package org.openimmunizationsoftware.dqa.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.QueryReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientImmunity;
import org.openimmunizationsoftware.dqa.manager.CodeMasterManager;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;

public class VaccinationParserASC extends VaccinationParser
{

  public static final String PATIENT_SEGMENT = "Patient";
  public static final String VACCINATION_SEGMENT = "Vaccination";

  private int startField = 0;
  private int currentSegmentPos = -1;
  private String segmentName = "";
  private List<List<String>> segmentValuesListList;
  private List<String> currentSegmentValuesList;
  private int vaccinationCount = 0;
  private int nextOfKinCount = 0;
  private Session session = null;

  private void setup()
  {
    startField = 0;
    currentSegmentPos = -1;
    segmentName = "";
    segmentValuesListList = new ArrayList<List<String>>();
    currentSegmentValuesList = new ArrayList<String>();

    patient = null;
    vaccination = null;
    nextOfKin = null;

    vaccinationCount = 0;
    nextOfKinCount = 0;

    pi = PotentialIssues.getPotentialIssues();
  }

  public VaccinationParserASC(SubmitterProfile profile) {
    super(profile);
  }

  @Override
  public void createVaccinationUpdateMessage(MessageReceived messageReceived)
  {
    String messageText = messageReceived.getRequestText();
    message = messageReceived;
    issuesFound = message.getIssuesFound();
    setup();
    readFields(messageText);

    patient = message.getPatient();
    currentSegmentValuesList = segmentValuesListList.get(0);

    boolean foundPatient = false;
    for (List<String> segmentValuesList : segmentValuesListList)
    {
      currentSegmentValuesList = segmentValuesList;
      String segmentName = getValue(0);
      Date maxSystemEntryDate = null;
      if (!foundPatient && segmentName.equals(PATIENT_SEGMENT))
      {
        foundPatient = true;
        skippableItem = patient;
        patient.setIdSubmitterNumber(getValue(1));
        patient.setBirthDate(createDate(pi.PatientBirthDateIsInvalid, null, getValue(2)));
        patient.setSystemCreationDate(createDate(pi.PatientSystemCreationDateIsInvalid, null, getValue(3)));
        patient.setEthnicityCode(getValue(4));
        patient.setSexCode(getValue(5));
        String immunityToVaricella = getValue(6);
        if (immunityToVaricella.equalsIgnoreCase("Y") || immunityToVaricella.equalsIgnoreCase("Yes")
            || immunityToVaricella.equalsIgnoreCase("T")|| immunityToVaricella.equalsIgnoreCase("True"))
        {
          PatientImmunity patientImmunity = new PatientImmunity();
          patientImmunity.setPatient(patient);
          patientImmunity.setImmunityCode(CodeMasterManager.EVIDENCE_OF_IMMUNITY_HISTORY_OF_VARICELLA_INFECTION);
          patient.getPatientImmunityList().add(patientImmunity);
        }
      } else if (segmentName.equals(VACCINATION_SEGMENT))
      {
        vaccinationCount++;
        Vaccination vaccination = new Vaccination();
        skippableItem = vaccination;
        vaccination.setPositionId(vaccinationCount);
        message.getVaccinations().add(vaccination);
        vaccination.setAdminDate(createDate(pi.VaccinationAdminDateIsInvalid, null, getValue(1)));
        vaccination.setAdminCvxCode(getValue(2));
        vaccination.setManufacturerCode(getValue(3));
        vaccination.setTradeNameCode(getValue(4));
        vaccination.setBodyRouteCode(getValue(5));
        vaccination.setBodySiteCode(getValue(6));
        vaccination.setInformationSourceCode(getValue(7));
        vaccination.setVaccineValidityCode(getValue(8));
        vaccination.setFacilityIdNumber(getValue(9));
        vaccination.setSystemEntryDate(createDate(pi.VaccinationSystemEntryTimeIsInvalid, null, getValue(10)));
        vaccination.setFacilityTypeCode(getValue(10));

        if (maxSystemEntryDate == null || maxSystemEntryDate.before(vaccination.getSystemEntryDate()))
        {
          maxSystemEntryDate = vaccination.getSystemEntryDate();
        }
      }
      if (maxSystemEntryDate != null)
      {
        message.setReceivedDate(maxSystemEntryDate);
      }
    }
  }

  private String getValue(int fieldNumber)
  {
    String value = null;
    if (currentSegmentValuesList.size() > fieldNumber)
    {
      value = currentSegmentValuesList.get(fieldNumber);
    }
    if (value == null)
    {
      return "";
    }
    return value;
  }

  private void readFields(String messageText)
  {
    String[] lines = messageText.split("\\\r");
    for (String line : lines)
    {
      String[] segmentTexts = line.split("\\|\\*");
      for (String segmentLine : segmentTexts)
      {
        List<String> fieldTextList = new ArrayList<String>();
        segmentValuesListList.add(fieldTextList);
        String[] fieldTexts = segmentLine.split("\\|");
        for (String fieldText : fieldTexts)
        {
          fieldTextList.add(fieldText);
        }
      }
    }
  }

  @Override
  public void createQueryMessage(QueryReceived queryReceived)
  {
    throw new IllegalArgumentException("ASC format does not support query");

  }

  @Override
  public String makeAckMessage(MessageReceived messageReceived)
  {
    return "Patient Accepted|" + messageReceived.getPatient().getIdSubmitterNumber() + "|\r";
  }

}
