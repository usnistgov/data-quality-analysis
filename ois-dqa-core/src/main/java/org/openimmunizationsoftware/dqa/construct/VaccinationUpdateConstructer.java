/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.construct;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openimmunizationsoftware.dqa.db.model.KeyedSetting;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.received.NextOfKin;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.db.model.received.types.Address;
import org.openimmunizationsoftware.dqa.db.model.received.types.CodedEntity;
import org.openimmunizationsoftware.dqa.db.model.received.types.Id;
import org.openimmunizationsoftware.dqa.db.model.received.types.Name;
import org.openimmunizationsoftware.dqa.db.model.received.types.OrganizationName;
import org.openimmunizationsoftware.dqa.db.model.received.types.PhoneNumber;
import org.openimmunizationsoftware.dqa.manager.KeyedSettingManager;

public class VaccinationUpdateConstructer implements ConstructerInterface
{

  private SubmitterProfile profile = null;
  private StringBuilder out;
  private String[] fields;
  private Date generationDate = new Date();
  private KeyedSettingManager ksm = KeyedSettingManager.getKeyedSettingManager();
  private SimpleDateFormat dateTimezoneFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");
  private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyMMddHHmmss");
  private int increment = 0;
  private String orgLocalCode = "";

  public VaccinationUpdateConstructer(SubmitterProfile profile) {
    this.profile = profile;
    orgLocalCode = profile.getOrganization() == null ? "" : profile.getOrganization().getOrgLocalCode();
    if (orgLocalCode == null || orgLocalCode.equals(""))
    {
      orgLocalCode = profile.getProfileCode();
      if (orgLocalCode == null)
      {
        orgLocalCode = "";
      }
    }
  }
  
  public String constructMessage(MessageReceived messageReceived)
  {
    out = new StringBuilder();
    makeMSH(messageReceived);
    Patient patient = messageReceived.getPatient();
    if (patient != null)
    {
      makePID(patient);
    }
    int pos = 0;
    for (NextOfKin nextOfKin : messageReceived.getNextOfKins())
    {
      if (!nextOfKin.isSkipped())
      {
        pos++;
        makeNK1(nextOfKin, pos);
      }
    }
    makePV1(patient);
    for (Vaccination vaccination : messageReceived.getVaccinations())
    {
      if (!vaccination.isSkipped())
      {
        makeORC(vaccination);
        makeRXA(vaccination);
      }
    }
    return out.toString();
  }

  private void makeRXA(Vaccination vaccination)
  {
    makeSegment("RXA");
    fields[1] = "0";
    fields[2] = "999";
    fields[3] = makeFieldDateOnly(vaccination.getAdminDate());
    if (isEmpty(vaccination.getAdminCptCode()))
    {
      fields[5] = makeField(vaccination.getAdminCvxCode(), vaccination.getAdminCvx().getTable(), "CVX");
    } else
    {
      fields[5] = makeField(vaccination.getAdminCptCode(), vaccination.getAdminCpt().getTable(), "C4");
    }
    if (isEmpty(vaccination.getAmount()))
    {
      fields[6] = "999";
    } else
    {
      fields[6] = makeField(vaccination.getAmount());
      if (isEmpty(vaccination.getAmountUnitCode()))
      {
        fields[7] = "ML";
      } else
      {
        fields[7] = vaccination.getAmountUnitCode();
      }
    }
    fields[9] = makeField(vaccination.getInformationSource());
    fields[10] = makeField(vaccination.getGivenBy());
    fields[11] = makeField(vaccination.getFacility());
    fields[15] = makeField(vaccination.getLotNumber());
    fields[17] = makeField(vaccination.getManufacturer());
    printFields();
  }

  private void makeORC(Vaccination vaccination)
  {
    makeSegment("ORC");
    fields[1] = "RE";
    fields[2] = vaccination.getIdSubmitter();
    printFields();
  }

  private void makePV1(Patient patient)
  {
    makeSegment("PV1");
    fields[2] = "R";
    fields[20] = makeField(patient.getFinancialEligibilityCode());
    printFields();
  }

  private void makeNK1(NextOfKin nextOfKin, int pos)
  {
    makeSegment("NK1");
    fields[1] = String.valueOf(pos);
    fields[2] = makeField(nextOfKin.getName());
    fields[3] = makeField(nextOfKin.getRelationship());
    printFields();
  }

  private void makePID(Patient patient)
  {
    makeSegment("PID");
    fields[3] = makeFieldIdOnly(patient.getIdSubmitter());
    if (!isEmpty(patient.getIdSsnNumber()))
    {
      fields[3] += "~" + makeFieldIdOnly(patient.getIdSsn());
    }
    if (!isEmpty(patient.getIdMedicaidNumber()))
    {
      fields[3] += "~" + makeFieldIdOnly(patient.getIdMedicaid());
    }
    fields[5] = makeField(patient.getName());
    fields[6] = makeField(patient.getMotherMaidenName());
    fields[7] = makeFieldDateOnly(patient.getBirthDate());
    fields[8] = makeField(patient.getSexCode());
    fields[10] = makeField(patient.getRace());
    fields[11] = makeField(patient.getAddress());
    fields[13] = makeField(patient.getPhone());
    fields[22] = makeField(patient.getEthnicity());
    if (patient.getBirthMultiple() != null && patient.getBirthMultiple().equals("Y"))
    {
      fields[24] = "Y";
      fields[25] = patient.getBirthOrderCode();
    } else if (patient.getBirthMultiple() != null && patient.getBirthMultiple().equals("N"))
    {
      fields[24] = "N";
    }
    printFields();
  }
  
  public String makeHeader(MessageReceived messageReceived)
  {
    out = new StringBuilder();
    makeSegment("FHS");
    fields[3] = makeField(ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_SENDING_APPLICATION, "DQA"));
    fields[4] = makeField(messageReceived.getMessageHeader().getSendingFacility());
    fields[5] = ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_RECEIVING_APPLICATION, "TxImmTrac");
    fields[6] = ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_RECEIVING_APPLICATION, "TxDSHS");
    fields[7] = dateTimezoneFormat.format(messageReceived.getReceivedDate());
    printFields();
    makeSegment("BHS");
    fields[3] = makeField(ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_SENDING_APPLICATION, "DQA"));
    fields[4] = makeField(messageReceived.getMessageHeader().getSendingFacility());
    fields[5] = ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_RECEIVING_APPLICATION, "TxImmTrac");
    fields[6] = ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_RECEIVING_APPLICATION, "TxDSHS");
    fields[7] = dateTimezoneFormat.format(messageReceived.getReceivedDate());
    printFields();
    return out.toString();
  }

  public String makeFooter(MessageReceived messageReceived)
  {
    out = new StringBuilder();
    makeSegment("BTS");
    printFields();
    makeSegment("FTS");
    printFields();
    return out.toString();
  }

  private void makeMSH(MessageReceived messageReceived)
  {
    makeSegment("MSH");
    fields[3] = makeField(ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_SENDING_APPLICATION, "DQA"));
    fields[4] = makeField(messageReceived.getMessageHeader().getSendingFacility());
    fields[5] = ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_RECEIVING_APPLICATION, "TxImmTrac");
    fields[6] = ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_RECEIVING_FACILITY, "TxDSHS");
    fields[7] = dateTimezoneFormat.format(messageReceived.getReceivedDate());
    fields[9] = makeField("VXU", "V04");
    fields[10] = (++increment) + "-" + dateTimeFormat.format(generationDate);
    fields[11] = makeField(ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_PROCESSING_ID, "P"));
    fields[12] = makeField(ksm.getKeyedValue(KeyedSetting.OUT_HL7_MSH_VERSION_ID, "2.5"));
    printFields();
  }

  protected Date getGenerationDate()
  {
    return generationDate;
  }

  private void makeSegment(String segmentName)
  {
    fields = new String[60];
    fields[0] = segmentName;
  }

  private void printFields()
  {
    out.append(fields[0]);
    out.append("|");
    int pos = 1;
    if (fields[0].equals("MSH") || fields[0].equals("BHS") || fields[0].equals("FHS"))
    {
      out.append("^~\\&|");
      pos = 3;
    }
    int lastPos = 1;
    for (int i = 1; i < fields.length; i++)
    {
      if (fields[i] != null)
      {
        if (fields[i].equals(""))
        {
          fields[i] = null;
        } else
        {
          lastPos = i;
        }
      }
    }
    while (pos <= lastPos)
    {
      if (fields[pos] != null)
      {
        out.append(fields[pos]);
      }
      out.append("|");
      pos++;
    }
    out.append("\r");
  }

  private String clean(String s)
  {
    if (s == null)
    {
      return null;
    }
    s = s.replace("\\^", "");
    s = s.replace("\\\\", "");
    s = s.replace("\\~", "");
    s = s.replace("\\&", "");
    s = s.replace("\\|", "");
    s = s.replace("\\\r", "");
    return s;
  }

  private String makeField(Name name)
  {
    return makeField(name.getLast(), name.getFirst(), name.getMiddle(), name.getSuffix(), name.getPrefix(), "",
        name.getTypeCode());
  }

  private String makeField(String... fields)
  {
    StringBuilder sb = new StringBuilder();
    int lastPos = 0;
    for (int i = 0; i < fields.length; i++)
    {
      if (!isEmpty(fields[i]))
      {
        lastPos = i;
      }
    }
    for (int i = 0; i <= lastPos; i++)
    {
      if (i > 0)
      {
        sb.append("^");
      }
      String s = fields[i];
      s = clean(s);
      if (s != null)
      {
        sb.append(s);
      }
    }
    return sb.toString();
  }

  SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyyMMdd");

  private String makeFieldDateOnly(Date date)
  {
    if (date == null)
    {
      return null;
    }
    return dateOnlyFormat.format(date);
  }

  private String makeField(CodedEntity ce)
  {
    if (isEmpty(ce.getCode()) && isEmpty(ce.getAltCode()))
    {
      return null;
    }
    return makeField(ce.getCode(), ce.getText(), ce.getTable(), ce.getAltCode(), ce.getAltText(), ce.getAltTable());
  }

  private String makeField(Address ad)
  {
    return makeField(ad.getStreet(), ad.getStreet2(), ad.getCity(), ad.getStateCode(), ad.getZip(),
        ad.getCountryCode(), ad.getTypeCode(), "", ad.getCountyParishCode());
  }

  private String makeField(PhoneNumber ph)
  {
    return makeField(ph.getNumber(), ph.getTelUseCode(), ph.getTelEquipCode(), ph.getEmail(), ph.getCountryCode(),
        ph.getAreaCode(), ph.getLocalNumber(), ph.getExtension());
  }

  private String makeField(Id id)
  {
    Name n = id.getName();
    return makeField(id.getNumber(), n.getLast(), n.getFirst(), n.getMiddle(), n.getSuffix(), n.getPrefix(), "", "",
        id.getAssigningAuthorityCode(), "", "", "", id.getTypeCode());
  }

  private String makeField(OrganizationName on)
  {
    return makeField("", "", "", on.getIdNumber());
  }

  private String makeFieldIdOnly(Id id)
  {
    return makeField(id.getNumber(), "", "", id.getAssigningAuthorityCode(), id.getTypeCode());
  }

  private boolean isEmpty(String s)
  {
    return s == null || s.equals("");
  }

}
