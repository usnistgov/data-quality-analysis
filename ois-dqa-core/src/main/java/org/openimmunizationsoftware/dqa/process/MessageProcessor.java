/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.process;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.BatchReport;
import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.IssueAction;
import org.openimmunizationsoftware.dqa.db.model.KeyedSetting;
import org.openimmunizationsoftware.dqa.db.model.MessageBatch;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.MessageReceivedGeneric;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.QueryReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.received.NextOfKin;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.db.model.received.VaccinationVIS;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientAddress;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientIdNumber;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientImmunity;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientPhone;
import org.openimmunizationsoftware.dqa.manager.CodesReceived;
import org.openimmunizationsoftware.dqa.manager.KeyedSettingManager;
import org.openimmunizationsoftware.dqa.manager.MessageReceivedManager;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;
import org.openimmunizationsoftware.dqa.parse.HL7Util;
import org.openimmunizationsoftware.dqa.parse.VaccinationParserHL7;
import org.openimmunizationsoftware.dqa.quality.QualityCollector;
import org.openimmunizationsoftware.dqa.validate.Validator;

public class MessageProcessor
{

  // set for webservice
  // results.setBatchId(qualityCollector.getMessageBatch().getBatchId());

  public static String processDebugOutput(Session session, SubmitterProfile profile, QualityCollector qualityCollector)
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter out = new PrintWriter(stringWriter);
    if (qualityCollector != null)
    {
      processMessageBatch(out, qualityCollector);
    }
    out.print("\r");
    out.print("\r");
    CodesReceived cr = CodesReceived.getCodesReceived(profile, session);
    CodesReceived masterCr = CodesReceived.getCodesReceived();
    for (CodeTable codeTable : cr.getCodeTableList())
    {
      List<CodeReceived> codesReceived = cr.getCodesReceived(codeTable);
      if (codesReceived.size() > 0)
      {
        out.print("-- " + padSlash(codeTable.getTableLabel() + " ", 92) + "\r");
        out.print("\r");
        out.print("VALUES RECEIVED\r");
        out.print(pad("value", 20));
        out.print(pad("label", 30));
        out.print(pad("use instead", 20));
        out.print(pad("status", 15));
        out.print(pad("count", 7));
        out.print("\r");
        for (CodeReceived codeReceived : codesReceived)
        {
          out.print(pad(codeReceived.getReceivedValue(), 20));
          out.print(pad(codeReceived.getCodeLabel(), 30));
          if (codeReceived.getCodeValue() == null || (codeReceived.getCodeValue().equals(codeReceived.getReceivedValue())))
          {
            out.print(pad("", 20));
          } else
          {
            out.print(pad(codeReceived.getCodeValue(), 20));
          }
          out.print(pad(codeReceived.getCodeStatus().getCodeLabel(), 15));
          out.print(pad(String.valueOf(codeReceived.getReceivedCount()), 7));
          out.print("\r");
        }
        codesReceived = masterCr.getCodesReceived(codeTable);
        out.print("\r");
        out.print("MASTER VALUE LIST\r");
        out.print(pad("value", 20));
        out.print(pad("label", 30));
        out.print(pad("use instead", 20));
        out.print(pad("status", 15));
        out.print("\r");

        for (CodeReceived codeReceived : codesReceived)
        {
          out.print(pad(codeReceived.getReceivedValue(), 20));
          out.print(pad(codeReceived.getCodeLabel(), 30));
          if (codeReceived.getCodeValue() == null || (codeReceived.getCodeValue().equals(codeReceived.getReceivedValue())))
          {
            out.print(pad("", 20));
          } else
          {
            out.print(pad(codeReceived.getCodeValue(), 20));
          }
          out.print(pad(codeReceived.getCodeStatus().getCodeLabel(), 15));
          out.print("\r");
        }
        out.print("\r");
      }
    }
    return stringWriter.toString();
  }

  /**
   * @deprecated
   * @param debugFlag
   * @param parser
   * @param sb
   * @param profile
   * @param session
   * @param qualityCollector
   * @return
   */
  public static MessageReceivedGeneric processMessage(boolean debugFlag, VaccinationParserHL7 parser, String sb, SubmitterProfile profile,
      Session session, QualityCollector qualityCollector)
  {
    MessageProcessRequest request = new MessageProcessRequest(sb);
    request.setDebugFlag(debugFlag);
    request.setParser(parser);
    request.setProfile(profile);
    request.setSession(session);
    request.setQualityCollector(qualityCollector);

    return processMessage(request).getMessageReceived();
  }

  public static MessageProcessResponse processMessage(MessageProcessRequest request)
  {

    MessageProcessResponse response = new MessageProcessResponse();
    MessageReceivedGeneric messageReceived;

    if (request.isHL7v2())
    {
      if (request.getMessageType().equals(HL7Util.MESSAGE_TYPE_VXU))
      {
        messageReceived = new MessageReceived();
        processVXU(request, (MessageReceived) messageReceived);
      } else if (request.getMessageType().equals(HL7Util.MESSAGE_TYPE_QBP))
      {
        messageReceived = new QueryReceived();
        processQBP(request, (QueryReceived) messageReceived);
      } else
      {
        messageReceived = new MessageReceived();
        String ackMessage;
        if (request.getMessageType().equals(""))
        {
          PotentialIssue pi = PotentialIssues.getPotentialIssues().Hl7MshMessageTypeIsMissing;
          ackMessage = HL7Util.makeAckMessage(HL7Util.ACK_REJECT, HL7Util.SEVERITY_ERROR, pi.getDisplayText(), request, pi);
        } else
        {
          PotentialIssue pi = PotentialIssues.getPotentialIssues().Hl7MshMessageTypeIsUnrecognized;
          ackMessage = HL7Util.makeAckMessage(HL7Util.ACK_REJECT, HL7Util.SEVERITY_ERROR, pi.getDisplayText(), request, pi);
        }
        messageReceived.setResponseText(ackMessage);
      }
    } else
    {
      messageReceived = new MessageReceived();
      PotentialIssue pi = PotentialIssues.getPotentialIssues().Hl7MshSegmentIsMissing;
      String ackMessage = HL7Util.makeAckMessage(HL7Util.ACK_REJECT, HL7Util.SEVERITY_ERROR, pi.getDisplayText(), request, pi);
      messageReceived.setResponseText(ackMessage);
      messageReceived.setSuccessfulCompletion(false);
    }
    response.setMessageReceived(messageReceived);
    return response;
  }

  public static void processVXU(MessageProcessRequest request, MessageReceived messageReceived)
  {
    Transaction tx = request.getSession().beginTransaction();
    try
    {

      request.getProfile().initPotentialIssueStatus(request.getSession());
      messageReceived.setProfile(request.getProfile());
      messageReceived.setRequestText(request.getMessageText());
      request.getParser().createVaccinationUpdateMessage(messageReceived);
      if (!messageReceived.hasErrors())
      {
        Validator validator = new Validator(request.getProfile(), request.getSession());
        validator.validateVaccinationUpdateMessage(messageReceived, null);
      }
      request.getQualityCollector().registerProcessedMessage(messageReceived);

      String ackMessage = request.getParser().makeAckMessage(messageReceived);
      messageReceived.setResponseText(ackMessage);
      messageReceived.setIssueAction(IssueAction.ACCEPT);
      MessageReceivedManager.saveMessageReceived(request.getProfile(), messageReceived, request.getSession());

      tx.commit();
      tx = null;
      messageReceived.setSuccessfulCompletion(true);

    } catch (Exception exception)
    {
      PotentialIssue pi = PotentialIssues.getPotentialIssues().GeneralProcessingException;
      String ackMessage = HL7Util.makeAckMessage(HL7Util.ACK_ERROR, HL7Util.SEVERITY_ERROR, "Unable to process because of unexpected exception:  "
          + exception.getMessage(), request, pi);
      messageReceived.setResponseText(ackMessage);
      messageReceived.setSuccessfulCompletion(false);
      messageReceived.setException(exception);
    } finally
    {
      if (tx != null)
      {
        tx.rollback();
        tx = null;
      }
    }
  }

  public static void processQBP(MessageProcessRequest request, QueryReceived queryReceived)
  {
    Session session = request.getSession();
    Transaction tx = session.beginTransaction();
    try
    {
      request.getProfile().initPotentialIssueStatus(request.getSession());
      queryReceived.setProfile(request.getProfile());
      queryReceived.setRequestText(request.getMessageText());
      request.getParser().createQueryMessage(queryReceived);

      QueryResult queryResult = new QueryResult();

      if (!queryReceived.hasErrors())
      {
        Query query = session.createQuery("from Patient where messageReceived.profile = ? and idSubmitterNumber = ? and "
            + "(messageReceived.issueAction = 'W' or messageReceived.issueAction = 'A') order by messageReceived.receivedDate ASC ");
        query.setParameter(0, request.getProfile());
        query.setParameter(1, queryReceived.getPatient().getIdSubmitterNumber());
        List<Patient> patientList = query.list();
        for (Iterator<Patient> patientIt = patientList.iterator(); patientIt.hasNext();)
        {
          Patient patient = patientIt.next();
          if (!patient.getNameFirst().equalsIgnoreCase(queryReceived.getPatient().getNameFirst())
              || !patient.getNameLast().equalsIgnoreCase(queryReceived.getPatient().getNameLast()))
          {
            patientIt.remove();
          }
        }
        if (patientList.size() > 0)
        {
          queryResult.setPatient(patientList.get(patientList.size() - 1));
        }
        List<NextOfKin> nextOfKinListComplete = queryResult.getNextOfKinList();
        for (Patient patient : patientList)
        {
          MessageReceived messageReceived = patient.getMessageReceived();
          query = session.createQuery("from NextOfKin where messageReceived = ?");
          query.setParameter(0, messageReceived);
          List<NextOfKin> nextOfKinList = query.list();
          for (NextOfKin nextOfKin : nextOfKinList)
          {
            if (!nextOfKin.isSkipped())
            {
              int pos = 0;
              while (pos < nextOfKinListComplete.size())
              {
                NextOfKin vc = nextOfKinListComplete.get(pos);
                if (same(vc, nextOfKin))
                {
                  nextOfKinListComplete.remove(pos);

                  break;
                }
                pos++;
              }
              nextOfKinListComplete.add(nextOfKin);
            }
          }
          query = session.createQuery("from PatientIdNumber where patient = ?");
          query.setParameter(0, patient);
          List<PatientIdNumber> patientIdNumberList = query.list();
          for (PatientIdNumber patientIdNumber : patientIdNumberList)
          {
            patient.getPatientIdNumberList().add(patientIdNumber);
          }

          query = session.createQuery("from PatientPhone where patient = ?");
          query.setParameter(0, patient);
          List<PatientPhone> patientPhoneList = query.list();
          for (PatientPhone patientPhone : patientPhoneList)
          {
            patient.getPatientPhoneList().add(patientPhone);
          }

          query = session.createQuery("from PatientAddress where patient = ?");
          query.setParameter(0, patient);
          List<PatientAddress> patientAddressList = query.list();
          if (patientAddressList.size() > 0)
          {
            patient.getPatientAddressList().set(0, patientAddressList.get(0));
          }
          for (int i = 1; i < patientAddressList.size(); i++)
          {
            patient.getPatientAddressList().add(patientAddressList.get(i));
          }
        }
        List<Vaccination> vaccinationListComplete = queryResult.getVaccinationList();
        List<PatientImmunity> patientImmunityListComplete = queryResult.getPatient().getPatientImmunityList();
        for (Patient patient : patientList)
        {
          MessageReceived messageReceived = patient.getMessageReceived();
          query = session.createQuery("from Vaccination where messageReceived = ?");
          query.setParameter(0, messageReceived);
          List<Vaccination> vaccinationList = query.list();
          for (Vaccination vaccination : vaccinationList)
          {
            if (!vaccination.isSkipped())
            {
              boolean addToList = true;
              int pos = 0;
              while (pos < vaccinationListComplete.size())
              {
                Vaccination vc = vaccinationListComplete.get(pos);
                if (same(vc, vaccination))
                {
                  vaccinationListComplete.remove(pos);
                  if (vaccination.isActionDelete())
                  {
                    addToList = false;
                  }
                  break;
                }
                pos++;
              }
              if (addToList)
              {
                vaccinationListComplete.add(vaccination);
              }
            }
          }
          query = session.createQuery("from PatientImmunity where patient = ?");
          query.setParameter(0, patient);
          List<PatientImmunity> patientImmunityList = query.list();
          for (PatientImmunity patientImmunity : patientImmunityList)
          {
            if (!patientImmunity.isSkipped())
            {
              int pos = 0;
              boolean addToList = true;
              while (pos < patientImmunityListComplete.size())
              {
                PatientImmunity pi = patientImmunityListComplete.get(pos);
                if (pi.getImmunityCode().equals(patientImmunity.getImmunityCode()))
                {
                  addToList = false;
                  break;
                }
                pos++;
              }
              if (addToList)
              {
                patientImmunityListComplete.add(patientImmunity);
              }
            }
          }
        }
        for (Vaccination vaccination : vaccinationListComplete)
        {
          query = session.createQuery("from VaccinationVIS where vaccination = ?");
          query.setParameter(0, vaccination);
          List<VaccinationVIS> vaccinationVISList = query.list();
          vaccination.getVaccinationVisList().addAll(vaccinationVISList);
        }

      }

      if (queryResult.getPatient() != null)
      {

        KeyedSettingManager ksm = KeyedSettingManager.getKeyedSettingManager();
        if (ksm.getKeyedValueBoolean(KeyedSetting.CDS_SOFTWARE_SERVICE_ENABLED, false))
        {
          String cdsServiceType = ksm.getKeyedValue(KeyedSetting.CDS_SOFTWARE_SERVICE_TYPE, "");
          String cdsServiceUrl = ksm.getKeyedValue(KeyedSetting.CDS_SOFTWARE_SERVICE_URL, "");
        }
      }

      String ackMessage = request.getParser().makeAckMessage(queryReceived, queryResult, session);
      queryReceived.setResponseText(ackMessage);
      queryReceived.setIssueAction(IssueAction.ACCEPT);

      tx.commit();
      tx = null;
      queryReceived.setSuccessfulCompletion(true);

    } catch (Exception exception)
    {
      PotentialIssue pi = PotentialIssues.getPotentialIssues().GeneralProcessingException;
      String ackMessage = HL7Util.makeAckMessage(HL7Util.ACK_ERROR, HL7Util.SEVERITY_ERROR, "Unable to process because of unexpected exception:  "
          + exception.getMessage(), request, pi);
      queryReceived.setResponseText(ackMessage);
      queryReceived.setSuccessfulCompletion(false);
      queryReceived.setException(exception);
    } finally
    {
      if (tx != null)
      {
        tx.rollback();
        tx = null;
      }
    }
  }

  private static boolean same(NextOfKin nk1, NextOfKin nk2)
  {
    boolean firstNameConflicts;
    boolean lastNameConflicts;
    if (nk1.getNameFirst() == null || nk2.getNameFirst() == null)
    {
      firstNameConflicts = nk1.getNameFirst() != null || nk2.getNameFirst() != null;
    } else
    {
      firstNameConflicts = !nk1.getNameFirst().equals(nk2.getNameFirst());
    }
    if (nk1.getNameLast() == null || nk2.getNameLast() == null)
    {
      lastNameConflicts = nk1.getNameLast() != null || nk2.getNameLast() != null;
    } else
    {
      lastNameConflicts = !nk1.getNameLast().equals(nk2.getNameLast());
    }
    return !firstNameConflicts && !lastNameConflicts;
  }

  private static boolean same(Vaccination v1, Vaccination v2)
  {
    boolean s;
    if (v1.getAdminDate() == null && v2.getAdminDate() == null)
    {
      s = true;
    } else if (v1.getAdminDate() == null || v2.getAdminDate() == null)
    {
      s = false;
    } else
    {
      s = v1.getAdminDate().equals(v2.getAdminDate());
    }
    if (s)
    {
      return v1.getAdminCvxCode().equals(v2.getAdminCvxCode());
    }
    return s;
  }

  private static final String PAD = "                                                                                                          ";

  private static String pad(String s, int size)
  {
    s += PAD;
    return s.substring(0, size - 1) + " ";
  }

  private static final String PAD_SLASH = "-----------------------------------------------------------------------------------------------------";

  private static String padSlash(String s, int size)
  {
    s += PAD_SLASH;
    return s.substring(0, size - 1) + "-";
  }

  private static void processMessageBatch(PrintWriter out, QualityCollector qualityCollector)
  {
    MessageBatch mb = qualityCollector.getMessageBatch();
    BatchReport r = mb.getBatchReport();
    out.print("\r");
    out.print("\r");
    out.print("Message Batch Summary: \r");
    out.print("Message Count:       " + r.getMessageCount() + "\r");
    out.print("Patient Count:       " + r.getMessageCount() + "\r");
    out.print("Vaccination      \r");
    out.print(" + Administered:     " + r.getVaccinationAdministeredCount() + "\r");
    out.print(" + Historical:       " + r.getVaccinationHistoricalCount() + "\r");
    out.print(" + Not Administered: " + r.getVaccinationNotAdministeredCount() + "\r");
    out.print(" + Deleted:          " + r.getVaccinationDeleteCount() + "\r");
  }
}
