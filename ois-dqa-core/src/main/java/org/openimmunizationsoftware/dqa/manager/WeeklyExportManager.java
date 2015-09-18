/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.construct.ConstructFactory;
import org.openimmunizationsoftware.dqa.construct.ConstructerInterface;
import org.openimmunizationsoftware.dqa.db.model.KeyedSetting;
import org.openimmunizationsoftware.dqa.db.model.MessageBatch;
import org.openimmunizationsoftware.dqa.db.model.MessageHeader;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.ReceiveQueue;
import org.openimmunizationsoftware.dqa.db.model.SubmitStatus;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.received.NextOfKin;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;

public class WeeklyExportManager extends ManagerThread
{

  private static final boolean EXPORT_UNIQUE = true;
  private static WeeklyExportManager singleton = null;

  public static WeeklyExportManager getWeeklyExportManager()
  {
    if (singleton == null)
    {
      singleton = new WeeklyExportManager();
      singleton.start();
    }
    return singleton;
  }

  private WeeklyExportManager() {
    super("Weekly Export Manager");
  }

  private Date processingStartTime;
  private Date processingEndTime;
  private int exportDayHighest;
  private int exportDayHigh;
  private int exportDayNormal;
  private int exportDayLow;
  private int exportDayLowest;
  private File exportDir = null;

  private PrintWriter out = null;
  private File file = null;

  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

  @Override
  public void run()
  {
    internalLog.append("Starting weekly export manager\r");
    while (keepRunning)
    {
      try
      {
        boolean okayToProceed = setWeeklyParameters();
        if (okayToProceed)
        {
          Date now = new Date();
          if (processingStartTime.before(now) && processingEndTime.before(now))
          {
            runNow(now);
          } else
          {
            internalLog.append("Weekly Export Manager will not run now\r");
          }
        }
      } catch (Exception e)
      {
        e.printStackTrace();
        lastException = e;
      }

      try
      {
        synchronized (sdf)
        {
          sdf.wait(10 * 1000 * 60);
        }
      } catch (InterruptedException ie)
      {
        keepRunning = false;
        return;
      }
      internalLog.setLength(0);
    }
  }

  @Override
  public void runNow(Date now) throws IOException
  {
    internalLog.append("Running now " + sdf.format(new Date()) + " \n");
    Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    int today = cal.get(Calendar.DAY_OF_WEEK);
    if (today >= exportDayHighest)
    {
      export(SubmitterProfile.TRANSFER_PRIORITY_HIGHEST);
    }
    if (today >= exportDayHigh)
    {
      export(SubmitterProfile.TRANSFER_PRIORITY_HIGH);
    }
    if (today >= exportDayNormal)
    {
      export(SubmitterProfile.TRANSFER_PRIORITY_NORMAL);
    }
    if (today >= exportDayLow)
    {
      export(SubmitterProfile.TRANSFER_PRIORITY_LOW);
    }
    if (today >= exportDayLowest)
    {
      export(SubmitterProfile.TRANSFER_PRIORITY_LOWEST);
    }
    internalLog.append("Finished exporting\n");
  }

  private void export(String transferPriority) throws IOException
  {
    internalLog.append("Exporting profiles with " + transferPriority + " priority \n");
    SessionFactory factory = OrganizationManager.getSessionFactory();
    Session session = factory.openSession();
    Query query = session.createQuery("from SubmitterProfile where profileStatus = ? and transferPriority = ?");
    query.setParameter(0, SubmitterProfile.PROFILE_STATUS_PROD);
    query.setParameter(1, transferPriority);
    List<SubmitterProfile> submitterProfiles = query.list();
    for (SubmitterProfile profile : submitterProfiles)
    {
      internalLog.append("Looking for exports under profile " + profile.getProfileCode() + "\n");
      Transaction tx = session.beginTransaction();
      if (EXPORT_UNIQUE)
      {
        exportUnique(session, profile);
      } else
      {
        exportSubmissionOrder(session, profile);
      }
      tx.commit();
    }
    session.close();
  }

  private void exportUnique(Session session, SubmitterProfile profile) throws IOException
  {
    internalLog.append("Looking for exports from profile " + profile.getProfileCode() + " \n");
    ConstructerInterface constructor = ConstructFactory.getConstructer(profile);
    // Find profile to export
    Query query = session.createQuery("from MessageBatch where profile = ? and submitStatus = ?");
    query.setParameter(0, profile);
    query.setParameter(1, SubmitStatus.PREPARED);
    List<MessageBatch> messageBatchList = query.list();
    for (MessageBatch messageBatch : messageBatchList)
    {
      internalLog.append(" +  Exporting batch for week ending " + sdf.format(messageBatch.getEndDate()) + " \n");
      openOutputFile(profile, messageBatch.getEndDate());
      MessageReceived currMr = null;
      SQLQuery sqlQuery = session.createSQLQuery("select rq.receive_queue_id "
          + "from dqa_receive_queue rq, dqa_patient pat, dqa_message_received mr "
          + "where rq.received_id = pat.received_id " + "  and rq.submit_code = 'P' " + "  and batch_id = ? "
          + "  and rq.received_id = mr.received_id "
          + "order by pat.name_last, pat.name_first, pat.name_middle, pat.id_submitter_number, mr.received_date");
      sqlQuery.setParameter(0, messageBatch.getBatchId());
      List receiveQueueIds = sqlQuery.list();
      ReceiveQueue receiveQueue = null;
      boolean nothingExported = true;
      for (Object receiveQueueId : receiveQueueIds)
      {
        if (receiveQueueId instanceof BigDecimal)
        {
          receiveQueue = (ReceiveQueue) session.get(ReceiveQueue.class, ((BigDecimal) receiveQueueId).intValue());
        } else
        {
          receiveQueue = (ReceiveQueue) session.get(ReceiveQueue.class, (Integer) receiveQueueId);
        }
        MessageReceived nextMr = receiveQueue.getMessageReceived();
        populate(session, receiveQueue, nextMr);
        if (currMr == null)
        {
          currMr = nextMr;
        } else
        {
          Patient nextPatient = nextMr.getPatient();
          Patient currPatient = currMr.getPatient();
          if (nextPatient.getIdSubmitterNumber().equals(currPatient.getIdSubmitterNumber()))
          {
            currMr.setPatient(nextPatient);
            currMr.setMessageHeader(nextMr.getMessageHeader());
            currMr.setNextOfKins(nextMr.getNextOfKins());
            for (String id : nextMr.getVaccinationMap().keySet())
            {
              currMr.getVaccinationMap().put(id, nextMr.getVaccinationMap().get(id));
            }
          } else
          {
            if (nothingExported)
            {
              out.print(constructor.makeHeader(currMr));
              nothingExported = false;
            }
            currMr.setVaccinations(getAndSortVaccinations(currMr));
            out.print(constructor.constructMessage(currMr));
            currMr = nextMr;
          }
          receiveQueue.setSubmitStatus(SubmitStatus.SUBMITTED);
          nextMr.setSubmitStatus(SubmitStatus.SUBMITTED);
        }
      }
      if (currMr != null)
      {
        if (!receiveQueue.getSubmitStatus().isExcluded())
        {
          if (nothingExported)
          {
            out.print(constructor.makeHeader(currMr));
            nothingExported = false;
          }
          currMr.setVaccinations(getAndSortVaccinations(currMr));
          out.print(constructor.constructMessage(currMr));
        }
      }
      if (!nothingExported && currMr != null)
      {
        out.print(constructor.makeFooter(currMr));
      }
      closeOutput(nothingExported);
    }
  }

  private void closeOutput(boolean nothingExported)
  {
    out.close();
    out = null;
    if (nothingExported && file != null && file.exists())
    {
      try
      {
        file.delete();
      } catch (Exception e)
      {
        // ignore, if unable to delete
      }
    }
    file = null;
  }

  private List<Vaccination> getAndSortVaccinations(MessageReceived currMr)
  {
    List<Vaccination> vaccinations = new ArrayList<Vaccination>(currMr.getVaccinationMap().values());
    Collections.sort(vaccinations, new Comparator<Vaccination>() {
      public int compare(Vaccination o1, Vaccination o2)
      {
        if (o1.getAdminDate() == null && o2.getAdminDate() == null)
        {
          return 0;
        } else if (o1.getAdminDate() == null)
        {
          return 1;
        } else if (o2.getAdminDate() == null)
        {
          return -1;
        } else
        {
          return o1.getAdminDate().compareTo(o2.getAdminDate());
        }
      }
    });
    return vaccinations;
  }

  private void exportSubmissionOrder(Session session, SubmitterProfile profile) throws IOException
  {
    Query query;
    query = session.createQuery("from MessageBatch where submitStatus = ? and profile = ? order by endDate");
    query.setParameter(0, SubmitStatus.PREPARED);
    query.setParameter(1, profile);
    List<MessageBatch> messageBatches = query.list();
    for (MessageBatch messageBatch : messageBatches)
    {
      openOutputFile(profile, messageBatch.getEndDate());
      export(messageBatch, profile, session);
      out.close();
    }
  }

  private void export(MessageBatch messageBatch, SubmitterProfile profile, Session session)
  {
    internalLog.append(" + " + messageBatch.getEndDate() + "\n");
    ConstructerInterface constructor = ConstructFactory.getConstructer(profile);
    Query query = session.createQuery("from ReceiveQueue where messageBatch = ? and submitStatus = ?");
    query.setParameter(0, messageBatch);
    query.setParameter(1, SubmitStatus.PREPARED);
    List<ReceiveQueue> receiveQueues = query.list();
    internalLog.append(" + receiveQueues.size() = " + receiveQueues.size() + "\n");
    for (ReceiveQueue receiveQueue : receiveQueues)
    {
      MessageReceived messageReceived = receiveQueue.getMessageReceived();
      populate(session, receiveQueue, messageReceived);
      if (!receiveQueue.getSubmitStatus().isExcluded())
      {
        out.print(constructor.constructMessage(messageReceived));
        messageReceived.setSubmitStatus(SubmitStatus.SUBMITTED);
        receiveQueue.setSubmitStatus(SubmitStatus.SUBMITTED);
      }
    }
    messageBatch.setSubmitStatus(SubmitStatus.SUBMITTED);
  }

  private void populate(Session session, ReceiveQueue receiveQueue, MessageReceived messageReceived)
  {
    Query query;
    // Patient
    query = session.createQuery("from Patient where messageReceived = ?");
    query.setParameter(0, messageReceived);
    List<Patient> patients = query.list();
    if (patients.size() == 0)
    {
      // This should not happen, but if it does
      receiveQueue.setSubmitStatus(SubmitStatus.EXCLUDED);
      messageReceived.setSubmitStatus(SubmitStatus.EXCLUDED);
      return;
    }
    messageReceived.setPatient(patients.get(0));
    // Message Header
    query = session.createQuery("from MessageHeader where messageReceived = ?");
    query.setParameter(0, messageReceived);
    List<MessageHeader> messageHeaders = query.list();
    if (messageHeaders.size() > 0)
    {
      messageReceived.setMessageHeader(messageHeaders.get(0));
    }
    query = session.createQuery("from NextOfKin where messageReceived = ? order by positionId");
    query.setParameter(0, messageReceived);
    List<NextOfKin> nextOfKins = query.list();
    messageReceived.setNextOfKins(nextOfKins);
    query = session.createQuery("from Vaccination where messageReceived = ? order by positionId");
    query.setParameter(0, messageReceived);
    List<Vaccination> vaccinations = query.list();
    HashMap<String, Vaccination> vaccinationMap = createVaccinationMap(vaccinations);
    vaccinations = new ArrayList<Vaccination>(vaccinationMap.values());
    messageReceived.setVaccinations(vaccinations);
    messageReceived.setVaccinationMap(vaccinationMap);
  }

  private HashMap<String, Vaccination> createVaccinationMap(List<Vaccination> vaccinations)
  {
    HashMap<String, Vaccination> vaccinationMap = new HashMap<String, Vaccination>();
    for (Vaccination vaccination : vaccinations)
    {
      vaccinationMap.put(vaccination.getIdSubmitter(), vaccination);
    }
    return vaccinationMap;
  }

  private boolean setWeeklyParameters()
  {
    {
      KeyedSettingManager ksm = KeyedSettingManager.getKeyedSettingManager();
      exportDir = new File(ksm.getKeyedValue(KeyedSetting.OUT_FILE_DIR, "c:\\data\\out"));
      if (!exportDir.exists())
      {
        internalLog.append("Internal dir '" + exportDir + "' does not exist, unable to export");
        return false;
      }
      exportDayHighest = ksm.getKeyedValueInt(KeyedSetting.WEEKLY_EXPORT_DAY_HIGHEST, 2);
      exportDayHigh = ksm.getKeyedValueInt(KeyedSetting.WEEKLY_EXPORT_DAY_HIGH, 2);
      exportDayNormal = ksm.getKeyedValueInt(KeyedSetting.WEEKLY_EXPORT_DAY_NORMAL, 2);
      exportDayLow = ksm.getKeyedValueInt(KeyedSetting.WEEKLY_EXPORT_DAY_LOW, 2);
      exportDayLowest = ksm.getKeyedValueInt(KeyedSetting.WEEKLY_EXPORT_DAY_LOWEST, 2);
      processingStartTime = getTimeToday(ksm.getKeyedValue(KeyedSetting.WEEKLY_EXPORT_START_TIME, "13:00"));
      processingEndTime = getTimeToday(ksm.getKeyedValue(KeyedSetting.WEEKLY_EXPORT_END_TIME, "19:00"));
      internalLog.append("Highest priority export day = " + exportDayHighest + "\r");
      internalLog.append("High priority export day = " + exportDayHigh + "\r");
      internalLog.append("Normal priority export day = " + exportDayNormal + "\r");
      internalLog.append("Low priority export day = " + exportDayLow + "\r");
      internalLog.append("Lowest priority export day = " + exportDayLowest + "\r");
      internalLog.append("Processing start time = " + processingStartTime + "\r");
      internalLog.append("Processing end time = " + processingEndTime + "\r");
      return true;
    }
  }

  private void openOutputFile(SubmitterProfile profile, Date generateDate) throws IOException
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(generateDate);
    String filename = createFilename(cal, profile);
    file = new File(exportDir, filename);
    try
    {
      out = new PrintWriter(new FileWriter(file));
    } catch (IOException ioe)
    {
      internalLog.append("Unable to open file '" + filename + "'");
      lastException = ioe;
      throw ioe;
    }
  }

  private String createFilename(Calendar endOfWeek, SubmitterProfile profile)
  {
    return profile.getProfileCode() + pad(2000 - endOfWeek.get(Calendar.YEAR), 2)
        + pad(endOfWeek.get(Calendar.DAY_OF_YEAR), 3) + ".hl7";
  }

  private String pad(int i, int pad)
  {
    String s = ("000" + i);
    return s.substring(s.length() - pad);
  }

}
