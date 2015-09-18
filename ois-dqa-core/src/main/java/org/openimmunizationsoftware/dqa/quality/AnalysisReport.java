/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.IssueAction;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.MessageBatch;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.ReceiveQueue;
import org.openimmunizationsoftware.dqa.db.model.Submission;
import org.openimmunizationsoftware.dqa.db.model.SubmissionAnalysis;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.VaccineCpt;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineMvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineProduct;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;

public class AnalysisReport extends QualityReport
{

  private File analysisDir = null;
  private File analysisFile = null;
  private Submission submission = null;
  private String prefix = null;
  private MessageReceived messageReceivedError = null;
  private MessageReceived messageReceivedWarn = null;
  private MessageReceived messageReceivedAccept = null;
  private MessageReceived messageReceivedSkip = null;

  public File getAnalysisFile()
  {
    return analysisFile;
  }

  public AnalysisReport(QualityCollector qualityCollector, Session session, SubmitterProfile profile, File analysisDir) {
    super(qualityCollector, profile, session, null);
    this.analysisDir = analysisDir;
    if (!analysisDir.exists())
    {
      analysisDir.mkdir();
    }
  }

  public AnalysisReport(QualityCollector qualityCollector, Session session, SubmitterProfile profile, Submission submission) {
    super(qualityCollector, profile, session, null);
    this.submission = submission;
    this.prefix = submission.getProfile().getProfileId() + "." + submission.getRequestName();
  }

  public void printReport() throws IOException
  {
    if (analysisDir != null)
    {
      out = new PrintWriter(new File(analysisDir, "Analysis Report.html"));
    } else
    {
      analysisFile = File.createTempFile(prefix, ".analysis.hl7");
      out = new PrintWriter(new FileWriter(analysisFile));
    }
    MessageBatch messageBatch = qualityCollector.getMessageBatch();
    printHead(out, "Analysis Report");
    try
    {
      printTitleBar(messageBatch, "Analysis Report");
      printSummary(messageBatch);
      printMessagesReceived(messageBatch);
      printExampleMessages(messageBatch);
      printDocumentation(messageBatch);
      printFooter();
    } catch (Exception e)
    {
      e.printStackTrace(out);
    }
    printFoot(out);
    out.close();
  }

  public static void printFoot(PrintWriter printWriter)
  {
    printWriter.println("  </body>");
    printWriter.println("<html>");
  }

  public static void printHead(PrintWriter printWriter, String title)
  {
    printWriter.println("<html>");
    printWriter.println("  <head>");
    printWriter.println("    <title>" + title + "</title>");
    printCss(printWriter);
    printWriter.println("  </head>");
    printWriter.println("  <body>");
  }

  private void printMessagesReceived(MessageBatch messageBatch) throws IOException
  {

    out.println("    <h2>Messages Received</h2>");
    out.println("    <table width=\"720\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Num</th>");
    out.println("        <th align=\"left\">Control Id</th>");
    out.println("        <th align=\"left\">Name</th>");
    out.println("        <th align=\"left\">DOB</th>");
    out.println("        <th align=\"left\">Action</th>");
    out.println("        <th align=\"left\">Admin</th>");
    out.println("        <th align=\"left\">Hist</th>");
    out.println("        <th align=\"left\">Del</th>");
    out.println("        <th align=\"left\">Not Admin</th>");
    out.println("      </tr>");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    int count = 0;
    Query query = session.createQuery("from ReceiveQueue where messageBatch = ? order by messageReceived.receivedId");
    query.setParameter(0, qualityCollector.getMessageBatch());
    List<ReceiveQueue> receiveQueueList = query.list();
    for (ReceiveQueue receiveQueue : receiveQueueList)
    {
      count++;
      MessageReceived messageReceived = receiveQueue.getMessageReceived();
      messageReceived.setInternalTemporaryId(count);
      String messageName = "Message " + count + " " + messageReceived.getIssueAction().getActionLabelForMessageReceivedPastTense() + "";

      query = session.createQuery("from Vaccination where messageReceived = ?");
      query.setParameter(0, messageReceived);
      List<Vaccination> vaccinationList = query.list();
      messageReceived.setVaccinations(vaccinationList);
      query = session.createQuery("from Patient where messageReceived = ?");
      query.setParameter(0, messageReceived);
      List<Patient> patientList = query.list();
      Patient patient = null;
      if (patientList.size() > 0)
      {
        patient = patientList.get(0);
        messageReceived.setPatient(patient);
      }

      String link = "<a class=\"tooltip\" href=\"" + messageName + ".html\">";
      out.println("      <tr>");
      out.println("        <td>" + link + count + "</a></td>");
      out.println("        <td>" + link + messageReceived.getMessageKey() + "</a></td>");
      if (patient == null)
      {
        out.println("        <td>&nbsp;</td>");
        out.println("        <td>&nbsp;</td>");
      } else
      {
        out.println("        <td>" + link + patient.getNameLast() + ", " + patient.getNameFirst() + "</a></td>");
        if (patient.getBirthDate() != null)
        {
          out.println("        <td>" + link + sdf.format(patient.getBirthDate()) + "</a></td>");
        } else
        {
          out.println("        <td>&nbsp;</td>");
        }
      }
      if (messageReceived.getIssueAction().equals(IssueAction.ERROR))
      {
        out.println("        <td><span class=\"problem\">" + messageReceived.getIssueAction().getActionLabel() + "</span></td>");
      } else
      {
        out.println("        <td>" + link + messageReceived.getIssueAction().getActionLabel() + "</a></td>");
      }
      int adminCount = 0;
      int histCount = 0;
      int delCount = 0;
      int nonAdminCount = 0;
      if (vaccinationList != null)
      {
        for (Vaccination vaccination : vaccinationList)
        {
          if (vaccination.isActionDelete())
          {
            delCount++;
          } else if (vaccination.isCompletionNotAdministered() || vaccination.isCompletionPartiallyAdministered()
              || vaccination.isCompletionRefused())
          {
            nonAdminCount++;
          } else if (vaccination.isInformationSourceAdmin())
          {
            adminCount++;
          } else
          {
            histCount++;
          }
        }
      }
      out.println("        <td align=\"center\">" + adminCount + "</td>");
      out.println("        <td align=\"center\">" + histCount + "</td>");
      out.println("        <td align=\"center\">" + delCount + "</td>");
      out.println("        <td align=\"center\">" + nonAdminCount + "</td>");
      out.println("      </tr>");

      if (messageReceivedError == null && messageReceived.getIssueAction().equals(IssueAction.ERROR))
      {
        messageReceivedError = messageReceived;
      } else if (messageReceivedWarn == null && messageReceived.getIssueAction().equals(IssueAction.WARN))
      {
        messageReceivedWarn = messageReceived;
      } else if (messageReceivedAccept == null && messageReceived.getIssueAction().equals(IssueAction.ACCEPT))
      {
        messageReceivedAccept = messageReceived;
      } else if (messageReceivedSkip == null && messageReceived.getIssueAction().equals(IssueAction.SKIP))
      {
        messageReceivedSkip = messageReceived;
      }

      printMessageReceived(messageBatch, count, receiveQueue.getMessageReceived(), messageName);
    }
    out.println("    </table>");

  }

  private void printExampleMessages(MessageBatch messageBatch) throws IOException
  {
    out.println("<h2>Example Messages</h2>");
    if (messageReceivedError != null)
    {
      printMessageText(messageReceivedError, out);
    }
    if (messageReceivedWarn != null)
    {
      printMessageText(messageReceivedWarn, out);
    }
    if (messageReceivedAccept != null)
    {
      printMessageText(messageReceivedAccept, out);
    }
    if (messageReceivedSkip != null)
    {
      printMessageText(messageReceivedSkip, out);
    }

  }

  private ToolTip RELEVANT_CODES_CODE = new ToolTip("Code", "The code received or mapped to");
  private ToolTip RELEVANT_CODES_PRODUCT_NAME = new ToolTip("Name", "The name of the product as defined by DQA");
  private ToolTip RELEVANT_CODES_LABEL = new ToolTip("Label", "The label defined by DQA for this code");
  private ToolTip RELEVANT_CODES_DATE = new ToolTip("Vac Admin", "Date vaccination was reported to be administered");
  private ToolTip RELEVANT_CODES_PRODUCT = new ToolTip("Product", "Product that was administered");
  private ToolTip RELEVANT_CODES_AGE = new ToolTip("Age", "Age of patient in months at time of administration");
  private ToolTip RELEVANT_CODES_DOB = new ToolTip("DOB", "Date patient was born");
  private ToolTip RELEVANT_CODES_VALID = new ToolTip("Valid", "The first date when this code is valid for describing an administered vaccination");
  private ToolTip RELEVANT_CODES_IN_USE = new ToolTip("In Use",
      "The first date when this code would normally be expected to be used for describing an administered vaccination");
  private ToolTip RELEVANT_CODES_NOT_IN_USE = new ToolTip("Not in Use",
      "The first date when this code would not normally be expected to be used to describe an administered vaccination");
  private ToolTip RELEVANT_CODES_NOT_VALID = new ToolTip("Not Valid",
      "The first date when this code is NOt valid to describe an administered vaccination");
  private ToolTip RELEVANT_CODES_START_AGE = new ToolTip("Start", "The first month of age when a patient could be administered this vaccination");
  private ToolTip RELEVANT_CODES_END_AGE = new ToolTip("End", "The first month of age when a patient can not be administered this vaccination");
  private ToolTip RELEVANT_CODES_TYPE = new ToolTip("Type", "The type of concept, used for CVX codes");
  private ToolTip RELEVANT_CODES_MVX_CODE = new ToolTip("MVX", "The MVX Code associated with this product");
  private ToolTip RELEVANT_CODES_MVX_LABEL = new ToolTip("MVX", "The MVX label associated with this product");
  private ToolTip RELEVANT_CODES_CPT_CVX_CODE = new ToolTip("CVX", "The CVX code this CPT is mapped to");
  private ToolTip RELEVANT_CODES_CPT_CVX_LABEL = new ToolTip("Label", "The CVX label this CPT is mapped to");
  private ToolTip RELEVANT_CODES_PRODUCT_CVX_CODE = new ToolTip("CVX", "The CVX code this vaccine product is associated with");
  private ToolTip RELEVANT_CODES_PRODUCT_CVX_LABEL = new ToolTip("Label", "The CVX label this vaccine product is associated with");
  private ToolTip RELEVANT_CODES_STATUS = new ToolTip("Status",
      "Status of received code in context of adminstered date and patient birth date (if applicable)");

  public void printMessageReceived(MessageBatch messageBatch, int count, MessageReceived messageReceived, String messageName) throws IOException
  {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Date wayOffIntoFuture = null;
    try
    {
      wayOffIntoFuture = sdf.parse("01/01/2100");
    } catch (ParseException pe)
    {
      throw new NullPointerException("Unexpected parse exception");
    }

    File file = null;
    PrintWriter messageOut;
    if (analysisDir != null)
    {
      messageOut = new PrintWriter(new File(analysisDir, messageName + ".html"));
    } else
    {
      file = File.createTempFile(prefix, messageName + ".html");
      messageOut = new PrintWriter(file);
    }
    printHead(messageOut, messageName);
    Query query;
    String messageName1 = "Message " + messageReceived.getInternalTemporaryId() + " "
        + messageReceived.getIssueAction().getActionLabelForMessageReceivedPastTense() + "";
    messageOut.println("<h2>" + messageName1 + "</h2>");
    messageOut.println("<h4>Message Received:</h4>");
    messageOut.println("<pre>");
    messageOut.print(messageReceived.getRequestText());
    messageOut.println("</pre>");
    messageOut.println("<h3>Issues Found:</h3>");
    messageOut.println("    <table width=\"720\">");
    messageOut.println("      <tr>");
    messageOut.println("        <th>Action</th>");
    messageOut.println("        <th>Description</th>");
    messageOut.println("        <th>HL7</th>");
    messageOut.println("        <th>Position</th>");
    messageOut.println("        <th>Value</th>");
    messageOut.println("      </tr>");
    Set<PotentialIssue> potentialIssueFoundSet = new HashSet<PotentialIssue>();
    query = session.createQuery("from IssueFound where messageReceived = ? order by positionId");
    query.setParameter(0, messageReceived);
    List<IssueFound> issueFoundList = query.list();
    for (IssueFound issueFound : issueFoundList)
    {
      if (issueFound.getIssueAction().equals(IssueAction.ERROR))
      {
        potentialIssueFoundSet.add(issueFound.getIssue());
        addIssue(messageReceived, issueFound);
        printIssue(issueFound, messageOut);
      }
    }
    for (IssueFound issueFound : issueFoundList)
    {
      if (issueFound.getIssueAction().equals(IssueAction.WARN))
      {
        potentialIssueFoundSet.add(issueFound.getIssue());
        addIssue(messageReceived, issueFound);
        printIssue(issueFound, messageOut);
      }
    }
    for (IssueFound issueFound : issueFoundList)
    {
      if (issueFound.getIssueAction().equals(IssueAction.SKIP))
      {
        potentialIssueFoundSet.add(issueFound.getIssue());
        addIssue(messageReceived, issueFound);
        printIssue(issueFound, messageOut);
      }
    }
    messageOut.println("    </table>");

    if (messageReceived.getVaccinations() != null)
    {

      Date dob = messageReceived.getPatient() == null ? null : messageReceived.getPatient().getBirthDate();

      boolean printed = false;

      for (Vaccination vaccination : messageReceived.getVaccinations())
      {
        if (!vaccination.getAdminCvxCode().equals("") && vaccination.getAdminDate() != null)
        {
          VaccineCvx vaccineCvx = vaccination.getVaccineCvx();
          if (vaccineCvx == null)
          {
            try
            {
              int cvxId = Integer.parseInt(vaccination.getAdminCvxCode());
              vaccineCvx = (VaccineCvx) session.get(VaccineCvx.class, cvxId);
              vaccination.setVaccineCvx(vaccineCvx);
            } catch (NumberFormatException nfe)
            {
              // ignore
            }
          }
          if (vaccineCvx != null)
          {
            if (!printed)
            {
              messageOut.println("<h3>Relevant CVX Codes</h3>");

              messageOut.println("<table width=\"720\">");
              messageOut.println("  <tr>");
              messageOut.println("    <th>" + RELEVANT_CODES_CODE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_LABEL.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_DATE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_VALID.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_IN_USE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_NOT_IN_USE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_NOT_VALID.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_STATUS.getHtml() + "</th>");
              messageOut.println("  </tr>");
              printed = true;
            }
            messageOut.println("  <tr>");
            messageOut.println("    <td>" + vaccineCvx.getCvxCode() + "</td>");
            messageOut.println("    <td>" + vaccineCvx.getCvxLabel() + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccination.getAdminDate()) + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccineCvx.getValidStartDate()) + "</td>");
            if (vaccineCvx.getUseStartDate().after(vaccineCvx.getValidStartDate()))
            {
              messageOut.println("    <td>" + sdf.format(vaccineCvx.getUseStartDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            if (vaccineCvx.getUseEndDate().before(vaccineCvx.getValidEndDate()))
            {
              messageOut.println("    <td>" + sdf.format(vaccineCvx.getUseEndDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            if (vaccineCvx.getValidEndDate().before(wayOffIntoFuture))
            {
              messageOut.println("    <td>" + sdf.format(vaccineCvx.getUseEndDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            String status;
            if (vaccination.getAdminDate().before(vaccineCvx.getValidStartDate()))
            {
              status = "Invalid: given before vaccine licensed";
            } else if (vaccineCvx.getValidEndDate().before(vaccination.getAdminDate()))
            {
              status = "Invalid: given after vaccine licensed";
            } else if (vaccination.getAdminDate().before(vaccineCvx.getUseStartDate()))
            {
              status = "Valid: given earlier than expected";
            } else if (vaccineCvx.getUseEndDate().before(vaccination.getAdminDate()))
            {
              status = "Valid: given later than expected";
            } else
            {
              status = "";
            }
            messageOut.println("    <td><span class=\"problem\">" + status + "</span></td>");
            messageOut.println("  </tr>");
          }

        }
      }
      if (printed)
      {
        messageOut.println("</table>");
      }
      printed = false;

      for (Vaccination vaccination : messageReceived.getVaccinations())
      {
        if (!vaccination.getAdminCvxCode().equals("") && vaccination.getAdminDate() != null)
        {
          VaccineCvx vaccineCvx = null;
          try
          {
            int cvxId = Integer.parseInt(vaccination.getAdminCvxCode());
            vaccineCvx = (VaccineCvx) session.get(VaccineCvx.class, cvxId);
          } catch (NumberFormatException nfe)
          {
            // ignore
          }
          if (vaccineCvx != null)
          {
            if (!printed)
            {
              messageOut.println("<h3>Relevant CVX Codes by Age</h3>");

              messageOut.println("<table width=\"720\">");
              messageOut.println("  <tr>");
              messageOut.println("    <th>" + RELEVANT_CODES_CODE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_LABEL.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_DATE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_DOB.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_AGE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_START_AGE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_END_AGE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_TYPE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_STATUS.getHtml() + "</th>");
              messageOut.println("  </tr>");
              printed = true;
            }
            messageOut.println("  <tr>");
            messageOut.println("    <td>" + vaccineCvx.getCvxCode() + "</td>");
            messageOut.println("    <td>" + vaccineCvx.getCvxLabel() + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccination.getAdminDate()) + "</td>");
            int age;
            if (dob != null)
            {
              messageOut.println("    <td>" + sdf.format(dob) + "</td>");
              Calendar calendar = Calendar.getInstance();
              calendar.setTime(dob);
              int i = 0;
              age = 0;
              while (calendar.getTime().before(vaccination.getAdminDate()) || calendar.getTime().equals(vaccination.getAdminDate()))
              {
                age = i;
                i++;
                calendar = Calendar.getInstance();
                calendar.setTime(dob);
                calendar.add(Calendar.MONTH, i);
              }
            } else
            {
              messageOut.println("    <td>&nsbp;</td>");
              age = 0;
            }
            if (age == 0)
            {
              messageOut.println("    <td>&nbsp;</td>");
            } else
            {
              messageOut.println("    <td>" + age + "</td>");
            }
            messageOut.println("    <td>" + vaccineCvx.getUseMonthStart() + "</td>");
            messageOut.println("    <td>" + vaccineCvx.getUseMonthEnd() + "</td>");
            messageOut.println("    <td>" + vaccineCvx.getConceptType() + "</td>");
            String status;
            if (age > 0)
            {
              if (age < vaccineCvx.getUseMonthStart())
              {
                status = "Invalid: given at too young of age";
              } else if (age >= vaccineCvx.getUseMonthEnd())
              {
                status = "Invalid: given at too old of age";
              } else
              {
                status = "";
              }
            } else
            {
              status = "";
            }
            messageOut.println("    <td><span class=\"problem\">" + status + "</span></td>");
            messageOut.println("  </tr>");
          }

        }
      }
      if (printed)
      {
        messageOut.println("</table>");
      }
      printed = false;

      for (Vaccination vaccination : messageReceived.getVaccinations())
      {

        if (!vaccination.getAdminCptCode().equals("") && vaccination.getAdminDate() != null)
        {
          query = session.createQuery("from VaccineCpt where cptCode = ?");
          query.setString(0, vaccination.getAdminCptCode());
          List<VaccineCpt> vaccineCpts = query.list();
          for (VaccineCpt vaccineCpt : vaccineCpts)
          {
            if (!printed)
            {
              messageOut.println("<h3>Relevant CPT Codes</h3>");
              messageOut.println("<table width=\"720\">");
              messageOut.println("  <tr>");
              messageOut.println("    <th>" + RELEVANT_CODES_CODE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_LABEL.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_DATE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_VALID.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_IN_USE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_NOT_IN_USE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_NOT_VALID.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_CPT_CVX_CODE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_CPT_CVX_LABEL.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_STATUS.getHtml() + "</th>");
              messageOut.println("  </tr>");
              printed = true;
            }
            messageOut.println("  <tr>");
            messageOut.println("    <td>" + vaccineCpt.getCptCode() + "</td>");
            messageOut.println("    <td>" + vaccineCpt.getCptLabel() + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccination.getAdminDate()) + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccineCpt.getValidStartDate()) + "</td>");
            if (vaccineCpt.getUseStartDate().after(vaccineCpt.getValidStartDate()))
            {
              messageOut.println("    <td>" + sdf.format(vaccineCpt.getUseStartDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            if (vaccineCpt.getUseEndDate().before(vaccineCpt.getValidEndDate()))
            {
              messageOut.println("    <td>" + sdf.format(vaccineCpt.getUseEndDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            if (vaccineCpt.getValidEndDate().before(wayOffIntoFuture))
            {
              messageOut.println("    <td>" + sdf.format(vaccineCpt.getUseEndDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            messageOut.println("    <td>" + vaccineCpt.getCvx().getCvxCode() + "</td>");
            messageOut.println("    <td>" + vaccineCpt.getCvx().getCvxLabel() + "</td>");
            String status;
            if (vaccination.getAdminDate().before(vaccineCpt.getValidStartDate()))
            {
              status = "Invalid: given before vaccine licensed";
            } else if (vaccineCpt.getValidEndDate().before(vaccination.getAdminDate()))
            {
              status = "Invalid: given after vaccine licensed";
            } else if (vaccination.getAdminDate().before(vaccineCpt.getUseStartDate()))
            {
              status = "Valid: given earlier than expected";
            } else if (vaccineCpt.getUseEndDate().before(vaccination.getAdminDate()))
            {
              status = "Valid: given later than expected";
            } else
            {
              status = "";
            }
            messageOut.println("    <td><span class=\"problem\">" + status + "</span></td>");
            messageOut.println("  </tr>");
          }

        }

      }
      if (printed)
      {
        messageOut.println("</table>");
      }
      printed = false;

      for (Vaccination vaccination : messageReceived.getVaccinations())
      {
        if (!vaccination.getManufacturerCode().equals("") && vaccination.getAdminDate() != null)
        {
          if (!printed)
          {
            messageOut.println("<h3>Relevant MVX Codes</h3>");

            messageOut.println("<table width=\"720\">");
            messageOut.println("  <tr>");
            messageOut.println("    <th>" + RELEVANT_CODES_CODE.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_LABEL.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_DATE.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_VALID.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_IN_USE.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_NOT_IN_USE.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_NOT_VALID.getHtml() + "</th>");
            messageOut.println("    <th>" + RELEVANT_CODES_STATUS.getHtml() + "</th>");
            messageOut.println("  </tr>");
            printed = true;
          }
          VaccineMvx vaccineMvx = (VaccineMvx) session.get(VaccineMvx.class, vaccination.getManufacturerCode());

          messageOut.println("  <tr>");
          messageOut.println("    <td>" + vaccineMvx.getMvxCode() + "</td>");
          messageOut.println("    <td>" + vaccineMvx.getMvxLabel() + "</td>");
          messageOut.println("    <td>" + sdf.format(vaccination.getAdminDate()) + "</td>");
          messageOut.println("    <td>" + sdf.format(vaccineMvx.getValidStartDate()) + "</td>");
          if (vaccineMvx.getUseStartDate().after(vaccineMvx.getValidStartDate()))
          {
            messageOut.println("    <td>" + sdf.format(vaccineMvx.getUseStartDate()) + "</td>");
          } else
          {
            messageOut.println("    <td>&nbsp;</td>");
          }
          if (vaccineMvx.getUseEndDate().before(vaccineMvx.getValidEndDate()))
          {
            messageOut.println("    <td>" + sdf.format(vaccineMvx.getUseEndDate()) + "</td>");
          } else
          {
            messageOut.println("    <td>&nbsp;</td>");
          }
          if (vaccineMvx.getValidEndDate().before(wayOffIntoFuture))
          {
            messageOut.println("    <td>" + sdf.format(vaccineMvx.getUseEndDate()) + "</td>");
          } else
          {
            messageOut.println("    <td>&nbsp;</td>");
          }
          String status;
          if (vaccination.getAdminDate().before(vaccineMvx.getValidStartDate()))
          {
            status = "Invalid: given before manufacturer existed";
          } else if (vaccineMvx.getValidEndDate().before(vaccination.getAdminDate()))
          {
            status = "Invalid: given after manufacturer existed";
          } else if (vaccination.getAdminDate().before(vaccineMvx.getUseStartDate()))
          {
            status = "Valid: given earlier than expected";
          } else if (vaccineMvx.getUseEndDate().before(vaccination.getAdminDate()))
          {
            status = "Valid: given later than expecte";
          } else
          {
            status = "";
          }
          messageOut.println("    <td><span class=\"problem\">" + status + "</span></td>");
          messageOut.println("  </tr>");
        }

      }
      if (printed)
      {
        messageOut.println("</table>");
      }
      printed = false;

      for (Vaccination vaccination : messageReceived.getVaccinations())
      {
        if (vaccination.getAdminDate() != null && vaccination.getVaccineCvx() != null)
        {
          query = session.createQuery("from VaccineProduct where cvx = ?");
          query.setParameter(0, vaccination.getVaccineCvx());
          List<VaccineProduct> vaccineProductList = query.list();
          for (VaccineProduct vaccineProduct : vaccineProductList)
          {
            if (!printed)
            {
              messageOut.println("<h3>Relevant Vaccine Products</h3>");

              messageOut.println("<table width=\"720\">");
              messageOut.println("  <tr>");
              messageOut.println("    <th>" + RELEVANT_CODES_PRODUCT_NAME.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_LABEL.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_DATE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_VALID.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_IN_USE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_NOT_IN_USE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_NOT_VALID.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_STATUS.getHtml() + "</th>");
              messageOut.println("  </tr>");
              printed = true;
            }

            messageOut.println("  <tr>");
            messageOut.println("    <td>" + vaccineProduct.getProductName() + "</td>");
            messageOut.println("    <td>" + vaccineProduct.getProductLabel() + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccination.getAdminDate()) + "</td>");
            messageOut.println("    <td>" + sdf.format(vaccineProduct.getValidStartDate()) + "</td>");
            if (vaccineProduct.getUseStartDate().after(vaccineProduct.getValidStartDate()))
            {
              messageOut.println("    <td>" + sdf.format(vaccineProduct.getUseStartDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            if (vaccineProduct.getUseEndDate().before(vaccineProduct.getValidEndDate()))
            {
              messageOut.println("    <td>" + sdf.format(vaccineProduct.getUseEndDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            if (vaccineProduct.getValidEndDate().before(wayOffIntoFuture))
            {
              messageOut.println("    <td>" + sdf.format(vaccineProduct.getUseEndDate()) + "</td>");
            } else
            {
              messageOut.println("    <td>&nbsp;</td>");
            }
            String status;
            if (vaccination.getAdminDate().before(vaccineProduct.getValidStartDate()))
            {
              status = "Invalid: given before product existed";
            } else if (vaccineProduct.getValidEndDate().before(vaccination.getAdminDate()))
            {
              status = "Invalid: given after product existed";
            } else if (vaccination.getAdminDate().before(vaccineProduct.getUseStartDate()))
            {
              status = "Valid: given earlier than expected";
            } else if (vaccineProduct.getUseEndDate().before(vaccination.getAdminDate()))
            {
              status = "Valid: given later than expecte";
            } else
            {
              status = "";
            }
            messageOut.println("    <td><span class=\"problem\">" + status + "</span></td>");
            messageOut.println("  </tr>");
          }
        }

      }
      if (printed)
      {
        messageOut.println("</table>");
      }
      printed = false;

      for (Vaccination vaccination : messageReceived.getVaccinations())
      {
        if (vaccination.getAdminDate() != null && vaccination.getVaccineCvx() != null)
        {
          query = session.createQuery("from VaccineProduct where cvx = ?");
          query.setParameter(0, vaccination.getVaccineCvx());
          List<VaccineProduct> vaccineProductList = query.list();
          for (VaccineProduct vaccineProduct : vaccineProductList)
          {
            if (!printed)
            {
              messageOut.println("<h3>Vaccine Product Associations</h3>");

              messageOut.println("<table width=\"720\">");
              messageOut.println("  <tr>");
              messageOut.println("    <th>" + RELEVANT_CODES_PRODUCT_NAME.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_PRODUCT_CVX_CODE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_PRODUCT_CVX_LABEL.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_MVX_CODE.getHtml() + "</th>");
              messageOut.println("    <th>" + RELEVANT_CODES_MVX_LABEL.getHtml() + "</th>");
              messageOut.println("  </tr>");
              printed = true;
            }

            messageOut.println("  <tr>");
            messageOut.println("    <td>" + vaccineProduct.getProductName() + "</td>");
            messageOut.println("    <td>" + vaccineProduct.getCvx().getCvxCode() + "</td>");
            messageOut.println("    <td>" + vaccineProduct.getCvx().getCvxLabel() + "</td>");
            if (vaccination.getManufacturerCode().equals(""))
            {
              messageOut.println("    <td>&nbsp;</td>");
              messageOut.println("    <td>&nbsp;</td>");
            } else
            {
              messageOut.println("    <td>" + vaccineProduct.getMvx().getMvxCode() + "</td>");
              messageOut.println("    <td>" + vaccineProduct.getMvx().getMvxLabel() + "</td>");
            }
            messageOut.println("  </tr>");
          }
        }

      }
      if (printed)
      {
        messageOut.println("</table>");
      }
      printed = false;
    }

    printDocumentation(messageBatch, potentialIssueFoundSet, messageOut);

    printFoot(messageOut);
    messageOut.close();
    
    if (analysisDir == null)
    {
      Transaction transaction = session.beginTransaction();
      SubmissionAnalysis submissionAnalysis = new SubmissionAnalysis();
      submissionAnalysis.setSubmission(submission);
      submissionAnalysis.setAnalysisLabel(messageName);
      submissionAnalysis.setMessageReceived(messageReceived);
      session.save(submissionAnalysis);
      session.flush();
      FileReader fileReader = new FileReader(file);
      //submissionAnalysis.setAnalysisContent(Hibernate.createClob(fileReader, file.length(), session));
      session.update(submissionAnalysis);
      transaction.commit();
      fileReader.close();
    }
  }

  public void printMessageText(MessageReceived messageReceived, PrintWriter messageOut)
  {
    String messageName = "Message " + messageReceived.getInternalTemporaryId() + " "
        + messageReceived.getIssueAction().getActionLabelForMessageReceivedPastTense() + "";
    messageOut.println("<h3>" + messageName + "</h3>");
    messageOut.println("<h4>Message Received:</h4>");
    messageOut.println("<pre>");
    messageOut.print(messageReceived.getRequestText());
    messageOut.println("</pre>");
    messageOut.println("<p><a class=\"tooltip\" href=\"" + messageName + ".html\">More Information</a></p>");
  }

  public void addIssue(MessageReceived messageReceived, IssueFound issueFound)
  {
    potentialIssueFoundSet.add(issueFound.getIssue());
    if (!potentialIssueFoundMessageReceivedExample.containsKey(issueFound.getIssue()))
    {
      potentialIssueFoundMessageReceivedExample.put(issueFound.getIssue(), messageReceived);
    }
  }

  public void printIssue(IssueFound issueFound, PrintWriter messageOut)
  {
    messageOut.println("      <tr>");
    if (issueFound.getIssueAction().equals(IssueAction.ERROR))
    {
      messageOut.println("        <td><span class=\"problem\">" + issueFound.getIssueAction().getActionLabel() + "</span></td>");
    } else
    {
      messageOut.println("        <td>" + issueFound.getIssueAction().getActionLabel() + "</td>");
    }
    messageOut.println("        <td>" + issueFound.getDisplayText() + "</td>");
    messageOut.println("        <td>" + issueFound.getIssue().getHl7Reference() + "</td>");
    messageOut.println("        <td>" + issueFound.getPositionId() + "</td>");
    if (issueFound.getCodeReceived() != null)
    {
      messageOut.println("        <td>" + issueFound.getCodeReceived().getReceivedValue() + "</td>");
    } else
    {
      messageOut.println("        <td>&nbsp;</td>");
    }
    messageOut.println("      </tr>");
  }
}
