/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openimmunizationsoftware.dqa.SoftwareVersion;
import org.openimmunizationsoftware.dqa.db.model.BatchCodeReceived;
import org.openimmunizationsoftware.dqa.db.model.BatchIssues;
import org.openimmunizationsoftware.dqa.db.model.BatchReport;
import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.KeyedSetting;
import org.openimmunizationsoftware.dqa.db.model.MessageHeader;
import org.openimmunizationsoftware.dqa.db.model.IssueAction;
import org.openimmunizationsoftware.dqa.db.model.MessageBatch;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssueStatus;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineGroup;
import org.openimmunizationsoftware.dqa.manager.DatabaseLogManager;
import org.openimmunizationsoftware.dqa.manager.KeyedSettingManager;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;
import org.openimmunizationsoftware.dqa.manager.VaccineGroupManager;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues.Field;
import org.openimmunizationsoftware.dqa.nist.CompactReportModel;
import org.openimmunizationsoftware.dqa.quality.model.ModelForm;
import org.openimmunizationsoftware.dqa.quality.model.ModelSection;

public class QualityReport
{
  private static final ToolTip RECEIVED_PATIENTS = new ToolTip("Patients", "The number of patients sent, one per message").setEmphasize(true);
  private static final ToolTip RECEIVED_NEXT_OF_KINS = new ToolTip("Next-of-Kins", "The number of responsible and associate parties sent")
      .setEmphasize(true);
  private static final ToolTip RECEIVED_VACCINATIONS = new ToolTip("Vaccinations", "The number of vaccinations sent").setEmphasize(true).setLink(
      "completeness.vaccinations");
  private static final ToolTip RECEIVED_VACCINATIONS_ADMININISTERED = new ToolTip("Administered", "Number of vaccinations indicated as administered",
      true).setLink("completeness.vaccineGroup");
  private static final ToolTip RECEIVED_VACCINATIONS_HISTORICAL = new ToolTip("Historical", "Number of vaccinations NOT indicated as administered",
      true);
  private static final ToolTip RECEIVED_VACCINATIONS_DELETED = new ToolTip("Deleted", "Number of vaccinations indicated as deleted", true);
  private static final ToolTip RECEIVED_VACCINATIONS_NOT_ADMINISTERED = new ToolTip("Not Administered",
      "Number of vaccinations that are indicated as not administered.", true);
  private static final ToolTip STATUS_ACCEPTED = new ToolTip("Accepted", "Number of messages that were accepted without warnings or errors");
  private static final ToolTip STATUS_ERRORED = new ToolTip("Rejected with Errors", "Number of messages that were rejected because of errors")
      .setLink("quality.errors");
  private static final ToolTip STATUS_WARNED = new ToolTip("Accepted with Warnings", "Number of messages that were accepted but had warnings")
      .setLink("quality.warnings");
  private static final ToolTip STATUS_SKIPPED = new ToolTip("Skipped",
      "Number of messages that are accepted without errors but will not be processed").setLink("quality.skips");

  private static final ToolTip SCORE_COMPLETENESS = new ToolTip("Completeness",
      "Indicates how consistently required, expected, and recommended fields are valued").setEmphasize(true).setLink("#completeness");
  private static final ToolTip SCORE_COMPLETENESS_SCORE_PATIENT = new ToolTip("Patient", "Indicates the completeness of patient related fields", true)
      .setLink("#completeness.patient");
  private static final ToolTip SCORE_COMPLETENESS_SCORE_VACCINATION = new ToolTip("Vaccination",
      "Indicates the completeness of vaccination related fields", true).setLink("#completeness.vaccination");
  private static final ToolTip SCORE_COMPLETENESS_SCORE_VACCINE_GROUP = new ToolTip("Vaccine Group",
      "Indicates the completess of the type of vaccines reported", true).setLink("#completeness.vaccineGroup");
  private static final ToolTip SCORE_QUALITY = new ToolTip("Quality", "Indicates the level of errors and warnings generated").setEmphasize(true)
      .setLink("#quality");
  private static final ToolTip SCORE_QUALITY_SCORE_ERRORS = new ToolTip("No Errors",
      "Indicates the level of errors found, message are expected to generate less than one percent error rate", true).setLink("#quality.errors");
  private static final ToolTip SCORE_QUALITY_SCORE_WARNINGS = new ToolTip("No Warnings",
      "Indicates the level of warnings found, message are expected to generate less than ten percent warning rate", true)
      .setLink("#quality.warnings");
  private static final ToolTip SCORE_TIMELINESS = new ToolTip("Timeliness",
      "Indicates the amount of time between administration of vaccinations and reporting them").setEmphasize(true).setLink("#timeliness");
  private static final ToolTip SCORE_TIMELINESS_SCORE_EARLY = new ToolTip("Early", "Indicates the score for messages received earlier than required",
      true).setLink("#timeliness");
  private static final ToolTip SCORE_TIMELINESS_SCORE_ON_TIME = new ToolTip("On Time", "Indicates the score for messages received when required",
      true).setLink("#timeliness");
  private static final ToolTip SCORE_TIMELINESS_SCORE_LATE = new ToolTip("Late", "Indicates the score for messages received later than required",
      true).setLink("#timeliness");
  private static final ToolTip SCORE_TIMELINESS_SCORE_VERY_LATE = new ToolTip("Very Late",
      "Indicates the score for messages received much later than required", true).setLink("#timeliness");

  private static final ToolTip COMPLETENESS_SCORE_PATIENT = new ToolTip("Patient", "Indicates the completeness of patient related fields")
      .setLink("#completeness.patient");
  private static final ToolTip COMPLETENESS_SCORE_VACCINATION = new ToolTip("Vaccination", "Indicates the completeness of vaccination related fields")
      .setLink("#completeness.vaccination");
  private static final ToolTip COMPLETENESS_SCORE_VACCINE_GROUP = new ToolTip("Vaccine Group",
      "Indicates the completess of the type of vaccines reported").setLink("#completeness.vaccineGroup");

  private static final ToolTip QUALITY_SCORE_NO_ERRORS = new ToolTip("No Errors",
      "Indicates the level of errors found, message are expected to generate less than one percent error rate").setLink("#quality.errors");
  private static final ToolTip QUALITY_SCORE_NO_WARNINGS = new ToolTip("No Warnings",
      "Indicates the level of warnings found, message are expected to generate less than ten percent warning rate").setLink("#quality.warnings");

  private static final ToolTip QUALITY_SCORE_INVALID = new ToolTip("Invalid Codes",
      "Codes were received that were not acceptable, must use standard codes").setLink("#quality.invalidCodes");
  private static final ToolTip QUALITY_SCORE_UNRECOGNIZED = new ToolTip("Unrecognized Codes",
      "Codes were received that were not recognized, standard codes are recommended").setLink("#quality.unrecognizedCodes");
  private static final ToolTip QUALITY_SCORE_DEPRECATED = new ToolTip("Deprecated Codes", "Codes were recognized but standard codes are recommended")
      .setLink("#quality.deprecatedCodes");

  private static final ToolTip TIMELINESS_SCORE_EARLY = new ToolTip("Early", "Indicates the score for messages received earlier than required");
  private static final ToolTip TIMELINESS_SCORE_ON_TIME = new ToolTip("On Time", "Indicates the score for messages received when required");
  private static final ToolTip TIMELINESS_SCORE_LATE = new ToolTip("Late", "Indicates the score for messages received later than required");
  private static final ToolTip TIMELINESS_SCORE_VERY_LATE = new ToolTip("Very Late",
      "Indicates the score for messages received much later than required");

  private static final ToolTip TIMELINESS_EARLY = new ToolTip(
      "Early",
      "Latest vaccination reported sooner than required (reports of previous administered vaccinations in same message and historical vaccinations not counted)");
  private static final ToolTip TIMELINESS_ON_TIME = new ToolTip(
      "On Time",
      "Latest vaccination reported within time required (reports of previous administered vaccinations in same message and historical vaccinations not counted)");
  private static final ToolTip TIMELINESS_LATE = new ToolTip(
      "Late",
      "Latest vaccination reported later than required (reports of previous administered vaccinations in same message and historical vaccinations not counted)");
  private static final ToolTip TIMELINESS_VERY_LATE = new ToolTip(
      "Very Late",
      "Latest vaccination reported much later than required (reports of previous administered vaccinations in same message and historical vaccinations not counted)");
  private static final ToolTip TIMELINESS_OLD_DATA = new ToolTip(
      "Old Data",
      "Latest vaccination administered quite some time ago (reports of previous administered vaccinations in same message and historical vaccinations not counted)");

  private static final ToolTip VACCINATION_GROUP_OTHER = new ToolTip("Undefined",
      "Vaccination is not classified as part of a particular vaccine group");

  protected QualityCollector qualityCollector = null;
  private SubmitterProfile profile = null;
  private String filename = "";
  protected PrintWriter out = null;
  private ModelForm modelForm = null;
  private KeyedSettingManager ksm = null;
  protected Set<PotentialIssue> potentialIssueFoundSet = new HashSet<PotentialIssue>();
  protected Map<PotentialIssue, MessageReceived> potentialIssueFoundMessageReceivedExample = new HashMap<PotentialIssue, MessageReceived>();
  protected Session session = null;

  private int messageCount = 0;
  private int nextOfKinCount = 0;
  private int patientCount = 0;
  private int patientUnderageCount = 0;
  private int vaccinationCount = 0;
  private int vaccinationAdministeredCount = 0;

  public QualityReport(QualityCollector qualityCollector, SubmitterProfile profile, Session session, PrintWriter out) {
    this.qualityCollector = qualityCollector;
    this.profile = profile;
    this.out = out;
    this.modelForm = qualityCollector.getModelForm();
    this.ksm = KeyedSettingManager.getKeyedSettingManager();
    this.session = session;
  }

  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  private SimpleDateFormat dateTime = new SimpleDateFormat("EEE, MMM d, yyyy h:mm aaa");
  private SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm aaa");
  private SimpleDateFormat dateOnly = new SimpleDateFormat("EEE, MMM d, yyyy");

  private String makeDateTimeRange(Date start, Date end)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(dateTime.format(start));
    long difference = end.getTime() - start.getTime();
    if (difference > 120 * 1000)
    {
      // more than two minutes, need to set ending time
      String endString = dateOnly.format(end);
      if (sb.toString().startsWith(endString))
      {
        // same starting date, just add ending time
        endString = timeOnly.format(end);
      }
      sb.append(" thru ");
      sb.append(endString);
    }
    return sb.toString();
  }

  private String makeDateRange(Date start, Date end)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(dateOnly.format(start));
    long difference = end.getTime() - start.getTime();
    if (difference > 120 * 1000)
    {
      String endString = dateOnly.format(end);
      if (sb.toString().equals(endString))
      {
        // same starting date, do nothing
        return sb.toString();
      }
      sb.append(" thru ");
      sb.append(endString);
    }
    return sb.toString();
  }

  public void printReport() throws IOException
  {
    MessageBatch messageBatch = qualityCollector.getMessageBatch();
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>Data Quality Report</title>");
    printCss(out);
    out.println("  </head>");
    out.println("  <body>");
    try
    {
      printTitleBar(messageBatch, "Quality Report");
      printScore(messageBatch.getBatchReport());
      printSummary(messageBatch);
      printCompleteness(messageBatch);
      printQuality(messageBatch);
      printDocumentation(messageBatch);
      printCodesReceived(messageBatch);
      printTimeliness(messageBatch);
      printFooter();
    } catch (Exception e)
    {
      e.printStackTrace(out);
    }
    out.println("  </body>");
    out.println("<html>");
  }
  
  public void printNeeded() throws IOException
  {
    MessageBatch messageBatch = qualityCollector.getMessageBatch();
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>First Step report</title>");
    printCss(out);
    out.println("  </head>");
    out.println("  <body>");
    try
    {
      //printTitleBar(messageBatch, "Quality Report");
      //printScore(messageBatch.getBatchReport());
      //printSummary(messageBatch);
      //printCompleteness(messageBatch);
      //printQuality(messageBatch);
      for(BatchIssues pi : qualityCollector.getWarnIssues())
    	  this.potentialIssueFoundSet.add(pi.getIssue());
      printDocumentation(messageBatch);
      //printCodesReceived(messageBatch);
      //printTimeliness(messageBatch);
      //printFooter();
    } catch (Exception e)
    {
      e.printStackTrace(out);
    }
    out.println("  </body>");
    out.println("<html>");
  }

  protected void printFooter()
  {
    out.println("    <pre>Report generated: " + dateTime.format(new Date()));
    out.println("Report template:  " + profile.getReportTemplate().getTemplateLabel());
    out.println("Software version: " + SoftwareVersion.VERSION + "</pre>");
  }

  private void printCompleteness(MessageBatch messageBatch)
  {
    BatchReport report = messageBatch.getBatchReport();
    QualityScoring scoring = qualityCollector.getCompletenessScoring();
    ScoringSet patientExpected = scoring.getScoringSet(QualityScoring.PATIENT_EXPECTED);
    ScoringSet patientOptional = scoring.getScoringSet(QualityScoring.PATIENT_OPTIONAL);
    ScoringSet patientRecommended = scoring.getScoringSet(QualityScoring.PATIENT_RECOMMENDED);
    ScoringSet patientRequired = scoring.getScoringSet(QualityScoring.PATIENT_REQUIRED);
    ScoringSet vaccinationExpected = scoring.getScoringSet(QualityScoring.VACCINATION_EXPECTED);
    ScoringSet vaccinationOptional = scoring.getScoringSet(QualityScoring.VACCINATION_OPTIONAL);
    ScoringSet vaccinationRecommended = scoring.getScoringSet(QualityScoring.VACCINATION_RECOMMENDED);
    ScoringSet vaccinationRequired = scoring.getScoringSet(QualityScoring.VACCINATION_REQUIRED);
    out.println("    <h2><a name=\"completeness\">Completeness</h2>");
    out.println("    <p class=\"dottedbox\">");
    out.println("      Completeness measures how many required, expected and ");
    out.println("      recommended fields have been received and also indicates");
    out.println("      if expected vaccinations have been reported. ");
    out.println("    </p>");
    out.println("    <h3>Score</h3>");
    printScoringSummary("Completeness", report.getCompletenessScore());
    out.println("    <table width=\"350\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Measurement</th>");
    out.println("        <th align=\"center\">Score</th>");
    out.println("        <th align=\"center\">Description</th>");
    out.println("        <th align=\"center\">Weight</th>");
    out.println("      </tr>");
    printScore(COMPLETENESS_SCORE_PATIENT, report.getCompletenessPatientScore(), per(modelForm.getAbsoluteWeight("completeness.patient")), REQUIRED);
    printScore(COMPLETENESS_SCORE_VACCINATION, report.getCompletenessVaccinationScore(),
        per(modelForm.getAbsoluteWeight("completeness.vaccination")), REQUIRED);
    printScore(COMPLETENESS_SCORE_VACCINE_GROUP, report.getCompletenessVaccineGroupScore(),
        per(modelForm.getAbsoluteWeight("completeness.vaccineGroup")), REQUIRED);
    out.println("    </table>");

    out.println("    <h3><a name=\"completeness.patient\">Patient</h3>");
    printCompletenessScoring("Patient", "#completeness.patient", patientRequired, patientExpected, patientRecommended,
        report.getCompletenessPatientScore(), modelForm.getAbsoluteWeight("completeness.patient"));
    out.println("    <br/>");
    printCompleteness(patientRequired, "completeness.patient.required", "Required", modelForm.getAbsoluteWeight("completeness.patient.required"),
        REQUIRED);
    out.println("    <br/>");
    printCompleteness(patientExpected, "completeness.patient.expected", "Expected", modelForm.getAbsoluteWeight("completeness.patient.expected"),
        REQUIRED);
    out.println("    <br/>");
    printCompleteness(patientRecommended, "completeness.patient.recommended", "Recommended",
        modelForm.getAbsoluteWeight("completeness.patient.recommended"), REQUIRED);
    out.println("    <br/>");
    printCompleteness(patientOptional, "completeness.patient.optional", "Optional", modelForm.getAbsoluteWeight("completeness.patient.optional"),
        OPTIONAL);
    out.println("    <br/>");

    out.println("    <h3><a name=\"completeness.vaccination\">Vaccination</h3>");
    printCompletenessScoring("Vaccination", "#completeness.vaccination", vaccinationRequired, vaccinationExpected, vaccinationRecommended,
        report.getCompletenessVaccinationScore(), modelForm.getAbsoluteWeight("completeness.vaccination"));
    out.println("    <br/>");
    printCompleteness(vaccinationRequired, "completeness.vaccination.required", "Required",
        modelForm.getAbsoluteWeight("completeness.vaccination.required"), REQUIRED);
    out.println("    <br/>");
    printCompleteness(vaccinationExpected, "completeness.vaccination.expected", "Expected",
        modelForm.getAbsoluteWeight("completeness.vaccination.expected"), REQUIRED);
    out.println("    <br/>");
    printCompleteness(vaccinationRecommended, "completeness.vaccination.recommended", "Recommended",
        modelForm.getAbsoluteWeight("completeness.vaccination.recommended"), REQUIRED);
    out.println("    <br/>");
    printCompleteness(vaccinationOptional, "completeness.vaccination.optional", "Optional",
        modelForm.getAbsoluteWeight("completeness.vaccination.optional"), OPTIONAL);

    out.println("    <h3><a name=\"completeness.vaccineGroup\">Vaccine Group</h3>");
    Set<VaccineCvx> vaccinesNotYetPrinted = new HashSet<VaccineCvx>(messageBatch.getBatchVaccineCvxMap().keySet());

    printVaccines("Expected", messageBatch, vaccinesNotYetPrinted, modelForm.getModelSection("completeness.vaccineGroup.expected"), false);
    out.println("    <br/>");
    printVaccines("Recommended", messageBatch, vaccinesNotYetPrinted, modelForm.getModelSection("completeness.vaccineGroup.recommended"), true);
    out.println("    <br/>");
    printVaccines("Optional", messageBatch, vaccinesNotYetPrinted, modelForm.getModelSection("completeness.vaccineGroup.optional"), true);
    out.println("    <br/>");
    printVaccines("Unexpected", messageBatch, vaccinesNotYetPrinted, modelForm.getModelSection("completeness.vaccineGroup.unexpected"), true);
    if (vaccinesNotYetPrinted.size() > 0)
    {
      out.println("    <br/>");
      printVaccinesLeftover(messageBatch, vaccinesNotYetPrinted);
    }
  }

  private void printVaccines(String label, MessageBatch messageBatch, Set<VaccineCvx> vaccinesNotPrinted, ModelSection vgsection, boolean skipZeroSize)
  {
    VaccineGroupManager vaccineGroupManager = VaccineGroupManager.getVaccineGroupManager();
    out.println("    <table width=\"650\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">" + label + "</th>");
    out.println("        <th align=\"left\">CVX</th>");
    out.println("        <th align=\"left\">Label</th>");
    out.println("        <th align=\"center\">Count</th>");
    out.println("        <th align=\"center\">Percent</th>");
    out.println("      </tr>");
    boolean printedAtLeastOneRow = false;
    for (ModelSection section : vgsection.getSections())
    {
      VaccineGroup vaccineGroup = vaccineGroupManager.getVaccineGroup(section.getName());
      if (vaccineGroup == null)
      {
        throw new IllegalArgumentException("Invalid vaccine group name '" + section.getName() + "'");
      }
      List<VaccineCvx> vaccinesToPrint = new ArrayList<VaccineCvx>();
      for (VaccineCvx vaccineCvx : vaccineGroup.getVaccineCvxList())
      {
        int count = qualityCollector.getVaccineCvxCount(vaccineCvx);
        if (count > 0)
        {
          vaccinesToPrint.add(vaccineCvx);
        }
      }
      int size = vaccinesToPrint.size();
      if (size == 0)
      {
        if (skipZeroSize)
        {
          continue;
        }
      }
      ToolTip toolTip = vaccineGroup.getToolTip();
      printVaccinesRow(messageBatch, vaccinesNotPrinted, vaccinesToPrint, size, toolTip, size == 0);
      printedAtLeastOneRow = true;
    }
    if (!printedAtLeastOneRow)
    {
      out.println("      <tr>");
      out.println("        <td colspan=\"5\">None to Display</td>");
      out.println("      </tr>");
    }
    out.println("    </table>");
  }

  private void printVaccinesLeftover(MessageBatch messageBatch, Set<VaccineCvx> vaccinesNotPrinted)
  {
    out.println("    <table width=\"650\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Other</th>");
    out.println("        <th align=\"center\">CVX</th>");
    out.println("        <th align=\"left\">Label</th>");
    out.println("        <th align=\"center\">Count</th>");
    out.println("        <th align=\"center\">Percent</th>");
    out.println("      </tr>");
    List<VaccineCvx> vaccinesToPrint = new ArrayList<VaccineCvx>(vaccinesNotPrinted);
    ToolTip toolTip = VACCINATION_GROUP_OTHER;
    printVaccinesRow(messageBatch, vaccinesNotPrinted, vaccinesToPrint, vaccinesToPrint.size(), toolTip, true);
    out.println("    </table>");
  }

  private void printVaccinesRow(MessageBatch messageBatch, Set<VaccineCvx> vaccinesNotPrinted, List<VaccineCvx> vaccinesToPrint, int size,
      ToolTip toolTip, boolean outOfRange)
  {
    BatchReport report = messageBatch.getBatchReport();

    out.println("      <tr>");
    out.println("        <td align=\"left\"" + (outOfRange ? " class=\"alert\"" : "") + " rowspan=\"" + size + "\">" + toolTip.getHtml());
    out.println("        </td>");
    boolean nextRow = false;
    for (VaccineCvx vaccineCvx : vaccinesToPrint)
    {
      int count = qualityCollector.getVaccineCvxCount(vaccineCvx);
      int denominator = report.getVaccinationAdministeredCount();
      String percent = "&nbsp;";
      if (denominator != 0)
      {
        if (count == 0)
        {
          percent = "-";
        } else
        {
          percent = per(((float) count) / denominator);
        }
      }
      if (nextRow)
      {
        out.println("      </tr>");
        out.println("      <tr>");
      }
      String[] fields = { vaccineCvx.getCvxCode(), vaccineCvx.getCvxLabel(), num(count), percent };
      for (int i = 0; i < fields.length; i++)
      {
        String align = i == 1 ? "left" : "center";
        out.println("        <td align=\"" + align + "\"" + (outOfRange ? " class=\"alert\"" : "") + ">" + fields[i] + "</td>");
      }
      vaccinesNotPrinted.remove(vaccineCvx);
      nextRow = true;
    }
    if (!nextRow)
    {
      out.println("       <td colspan=\"4\"><span class=\"problem\">Problem: no vaccines received for this group</span></td>");
    }
    out.println("      </tr>");
  }

  private void printCompletenessScoring(String label, String link, ScoringSet required, ScoringSet expected, ScoringSet recommended, int score,
      double weightFactor)
  {
    out.println("    <table width=\"350\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">" + label + " Fields</th>");
    out.println("        <th>Score</th>");
    out.println("        <th>Description</th>");
    out.println("        <th>Weight</th>");
    out.println("      </tr>");
    out.println("      <tr>");
    out.println("        <td class=\"highlight\">Overall</td>");
    out.println("        <td class=\"highlight\" align=\"center\">" + score + "</td>");
    out.println("        <td class=\"highlight\" align=\"center\">" + getScoreDescription(score, REQUIRED) + "</td>");
    out.println("        <td class=\"highlight\">&nbsp;</td>");
    out.println("      </tr>");
    printScoreAndWeight(required, link + ".required", "Required", weightFactor);
    printScoreAndWeight(expected, link + ".expected", "Expected", weightFactor);
    printScoreAndWeight(recommended, link + ".recommended", "Recommended", weightFactor);
    out.println("    </table>");
  }

  private void printScoreAndWeight(ScoringSet ss, String link, String label, double weightFactor)
  {
    int score = (int) (100 * ss.getScore() + 0.5);
    boolean showRed = score < 70;
    out.println("      <tr>");
    out.println("        <td align=\"left\"" + (showRed ? " class=\"alert\"" : "") + "><a href=\"" + link + "\">" + label + "</a></td>");
    out.println("        <td align=\"center\"" + (showRed ? " class=\"alert\"" : "") + ">" + score + "</td>");
    out.println("        <td align=\"center\"" + (showRed ? " class=\"alert\"" : "") + ">" + getScoreDescription(score, REQUIRED) + "</td>");
    out.println("        <td align=\"center\"" + (showRed ? " class=\"alert\"" : "") + ">" + per(weightFactor * ss.getWeight()) + "</td>");
    out.println("      </tr>");
  }

  private void printCompleteness(ScoringSet scoringSet, String objectLabel, String typeLabel, double weightFactor, char type)
  {
    List<CompletenessRow> completenessRowList = scoringSet.getCompletenessRow();
    out.println("    <table width=\"" + (scoringSet.getWeight() > 0 ? "550" : "390") + "\">");
    out.println("      <tr>");
    out.println("        <th width=\"180\" align=\"left\"><a name=\"" + objectLabel + "\"/>" + typeLabel + "</th>");
    out.println("        <th width=\"70\" align=\"left\">HL7</th>");
    out.println("        <th width=\"70\" align=\"center\">Count</th>");
    out.println("        <th width=\"70\" align=\"center\">Percent</th>");
    if (scoringSet.getWeight() > 0)
    {
      out.println("        <th width=\"90\" align=\"center\">Description</th>");
      out.println("        <th width=\"70\" align=\"center\">Weight</th>");
    }
    out.println("      </tr>");
    boolean firstSectionFound = false;
    boolean firstSectionSkipped = false;
    for (CompletenessRow completenessRow : completenessRowList)
    {
      if (firstSectionSkipped && completenessRow.getToolTip().hasIndent())
      {
        continue;
      }
      if (completenessRow.getScoreWeight() > 0 || completenessRow.getCount() > 0)
      {
        print(completenessRow, scoringSet.getOverallWeight(), weightFactor, type);
        firstSectionFound = true;
      } else
      {
        if (!firstSectionFound)
        {
          firstSectionSkipped = true;
        }
      }
    }
    out.println("    </table>");
  }

  protected void printTitleBar(MessageBatch messageBatch, String reportName)
  {
    BatchReport report = messageBatch.getBatchReport();
    out.println("    <h1>" + profile.getOrganization().getOrgLabel() + " " + reportName + "</h1>");
    out.println("    <table width=\"720\">");
    out.println("      <tr>");
    out.println("        <th>Batch Title</th>");
    out.println("        <th>Batch Type</th>");
    out.println("        <th>Profile</th>");
    if (filename != null && !filename.equals(""))
    {
      out.println("        <th>File Name</th>");
    }
    out.println("        <th>Received</th>");
    out.println("      </tr>");
    if (filename != null && !filename.equals(""))
    {
      printRow(messageBatch.getBatchTitle(), messageBatch.getBatchType().getTypeLabel(), profile.getProfileLabel(), filename,
          makeDateTimeRange(messageBatch.getStartDate(), messageBatch.getEndDate()));
    } else
    {
      printRow(messageBatch.getBatchTitle(), messageBatch.getBatchType().getTypeLabel(), profile.getProfileLabel(),
          makeDateTimeRange(messageBatch.getStartDate(), messageBatch.getEndDate()));

    }
    out.println("    </table>");
    // out.println("    <p class=\"dottedbox\">");
    // out.println("      Immunization data quality is measured in three main areas: ");
    // out.println("      Timeliness, Quality and Completeness.  ");
    // out.println("      Timeliness measures how quickly administered vaccinations are ");
    // out.println("      reported. ");
    // out.println("      Quality measures how accurate patient and vaccination data is ");
    // out.println("      recorded and whether it reflects expected practice.");
    // out.println("      Completeness measures if fields necessary for registry functions ");
    // out.println("      been valued. ");
    // out.println("    </p>");

  }

  protected void printScore(BatchReport report)
  {
    QualityScoring scoring = qualityCollector.getCompletenessScoring();

    if (ksm.getKeyedValueBoolean(KeyedSetting.DQA_REPORT_READY_FOR_PRODUCTION_ENABLED, true))
    {
      int triggerLevel = ksm.getKeyedValueInt(KeyedSetting.DQA_REPORT_READY_FOR_PRODUCTION_TRIGGER_LEVEL, 50);
      if (report.getMessageCount() > triggerLevel)
      {
        if (scoring.getScoringSet(QualityScoring.PATIENT_REQUIRED).getScore() >= 0.99
            && scoring.getScoringSet(QualityScoring.VACCINATION_REQUIRED).getScore() >= 0.99)
        {
          out.println("    <h3>Ready for Production</h3>");
          out.println("    <p class=\"dottedbox\">All required fields are present, interface is ready for production.</p>");
        } else
        {
          out.println("    <h3>Not Ready for Production</h3>");
          out.println("    <p class=\"dottedbox\">Required fields are not all present, interface is not ready for production.</p>");
        }
      } else
      {
        out.println("    <p class=\"dottedbox\">At least " + triggerLevel + " messages must be submitted to enable production readiness check.</p>");
      }

    }
    out.println("    <h2>Scoring Summary</h2>");
    printScoringSummary("DQA", report.getOverallScore());
    out.println("    <table width=\"400\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Measurement</th>");
    out.println("        <th align=\"center\">Score</th>");
    out.println("        <th align=\"center\">Description</th>");
    out.println("        <th align=\"center\">Weight</th>");
    out.println("      </tr>");
    printScoreOverall(SCORE_COMPLETENESS, report.getCompletenessScore(), per(modelForm.getAbsoluteWeight("completeness")), REQUIRED);
    printScoreOverall(SCORE_COMPLETENESS_SCORE_PATIENT, report.getCompletenessPatientScore(),
        per(modelForm.getAbsoluteWeight("completeness.patient")), REQUIRED);
    printScoreOverall(SCORE_COMPLETENESS_SCORE_VACCINATION, report.getCompletenessVaccinationScore(),
        per(modelForm.getAbsoluteWeight("completeness.vaccination")), REQUIRED);
    printScoreOverall(SCORE_COMPLETENESS_SCORE_VACCINE_GROUP, report.getCompletenessVaccineGroupScore(),
        per(modelForm.getAbsoluteWeight("completeness.vaccineGroup")), REQUIRED);
    printScoreOverall(SCORE_QUALITY, report.getQualityScore(), per(modelForm.getAbsoluteWeight("quality")), REQUIRED);
    printScoreOverall(SCORE_QUALITY_SCORE_ERRORS, report.getQualityErrorScore(), per(modelForm.getAbsoluteWeight("quality.errors")), REQUIRED);
    printScoreOverall(SCORE_QUALITY_SCORE_WARNINGS, report.getQualityWarnScore(), per(modelForm.getAbsoluteWeight("quality.warnings")), REQUIRED);
    printScoreOverall(SCORE_TIMELINESS, report.getTimelinessScore(), per(modelForm.getAbsoluteWeight("timeliness")), REQUIRED);
    if (modelForm.getWeight("timeliness.early") > 0 && report.getTimelinessScoreEarly() > 0)
    {
      printScoreOverall(SCORE_TIMELINESS_SCORE_EARLY, report.getTimelinessScoreEarly(), per(modelForm.getAbsoluteWeight("timeliness.early")),
          EXTRA_CREDIT);
    }
    if (modelForm.getWeight("timeliness.onTime") > 0 && report.getTimelinessScoreOnTime() > 0)
    {
      printScoreOverall(SCORE_TIMELINESS_SCORE_ON_TIME, report.getTimelinessScoreOnTime(), per(modelForm.getAbsoluteWeight("timeliness.onTime")),
          REQUIRED);
    }
    if (modelForm.getWeight("timeliness.late") > 0 && report.getTimelinessScoreLate() > 0)
    {
      printScoreOverall(SCORE_TIMELINESS_SCORE_LATE, report.getTimelinessScoreLate(), per(modelForm.getAbsoluteWeight("timeliness.late")), DEMERIT);
    }
    if (modelForm.getWeight("timeliness.veryLate") > 0 && report.getTimelinessScoreVeryLate() > 0)
    {
      printScoreOverall(SCORE_TIMELINESS_SCORE_VERY_LATE, report.getTimelinessScoreVeryLate(),
          per(modelForm.getAbsoluteWeight("timeliness.veryLate")), DEMERIT);
    }
    out.println("    </table>");
  }

  private void printScoringSummary(String label, int score)
  {
    out.println("    <table>");
    out.println("      <tr>");
    out.println("        <th align=\"center\">" + label + " Score</th>");
    out.println("        <th align=\"center\">Description</th>");
    out.println("      </tr>");
    out.println("      <tr>");
    out.println("        <td align=\"center\"><span class=\"score\">" + score + "</span></th>");
    out.println("        <td align=\"center\"><span class=\"score\">" + getScoreDescription(score, REQUIRED) + "</span></th>");
    out.println("      </tr>");
    out.println("    </table>");
    out.println("    <br/>");

  }

  protected void printSummary(MessageBatch messageBatch)
  {
    BatchReport report = messageBatch.getBatchReport();
    out.println("    <h3>Data Received</h3>");
    out.println("    <table width=\"350\">");
    out.println("      <tr>");
    out.println("        <th width=\"60%\" align=\"left\">Received</th>");
    out.println("        <th width=\"20%\" align=\"center\">Count</th>");
    out.println("        <th width=\"20%\" align=\"center\">Percent</th>");
    out.println("      </tr>");
    messageCount = report.getMessageCount();
    patientCount = report.getPatientCount();
    vaccinationCount = report.getVaccinationCount();
    vaccinationAdministeredCount = report.getVaccinationAdministeredCount();
    patientUnderageCount = report.getPatientUnderageCount();
    nextOfKinCount = report.getNextOfKinCount();
    printPer(RECEIVED_PATIENTS, patientCount, 0);
    printPer(RECEIVED_NEXT_OF_KINS, nextOfKinCount, 0);
    printPer(RECEIVED_VACCINATIONS, vaccinationCount, 0);
    printPer(RECEIVED_VACCINATIONS_ADMININISTERED, report.getVaccinationAdministeredCount(), vaccinationCount);
    printPer(RECEIVED_VACCINATIONS_HISTORICAL, report.getVaccinationHistoricalCount(), vaccinationCount);
    printPer(RECEIVED_VACCINATIONS_DELETED, report.getVaccinationDeleteCount(), vaccinationCount);
    printPer(RECEIVED_VACCINATIONS_NOT_ADMINISTERED, report.getVaccinationNotAdministeredCount(), vaccinationCount);
    out.println("    </table>");
    out.println("    <h3>Processing Status</h3>");
    out.println("    <table width=\"350\">");
    out.println("      <tr>");
    out.println("        <th width=\"60%\" align=\"left\">Status</th>");
    out.println("        <th width=\"20%\" align=\"center\">Count</th>");
    out.println("        <th width=\"20%\" align=\"center\">Percent</th>");
    out.println("      </tr>");
    printPer(STATUS_ACCEPTED, messageBatch.getBatchActions(IssueAction.ACCEPT).getActionCount(), patientCount, 90, 100);
    printPer(STATUS_WARNED, messageBatch.getBatchActions(IssueAction.WARN).getActionCount(), patientCount, 0, 10);
    printPer(STATUS_ERRORED, messageBatch.getBatchActions(IssueAction.ERROR).getActionCount(), patientCount, 0, 0);
    printPer(STATUS_SKIPPED, messageBatch.getBatchActions(IssueAction.SKIP).getActionCount(), patientCount, 0, 10);
    out.println("    </table>");
    out.println("    <h3>Message Header Details</h3>");
    MessageHeader exampleHeader = qualityCollector.getExampleHeader();
    if (exampleHeader != null)
    {
      out.println("    <table width=\"350\">");
      out.println("      <tr>");
      out.println("        <th width=\"60%\" align=\"left\">Field</th>");
      out.println("        <th width=\"20%\" align=\"center\">Value</th>");
      out.println("      </tr>");
      printRow("Sending Application", exampleHeader.getSendingApplication());
      printRow("Sending Facility", exampleHeader.getSendingFacility());
      printRow("Receiving Application", exampleHeader.getReceivingApplication());
      printRow("Receiving Facility", exampleHeader.getReceivingFacility());
      printRow("Message Type", exampleHeader.getMessageType());
      printRow("Message Trigger", exampleHeader.getMessageTrigger());
      printRow("Message Structure", exampleHeader.getMessageStructure());
      printRow("Processing Id", exampleHeader.getProcessingStatus().getCode());
      printRow("HL7 Version", exampleHeader.getMessageVersion());
      out.println("    </table>");
    } else
    {
      out.println("    <p class=\"dottedbox\">No example header to show, none sent.</p>");
    }
  }

  public void printQuality(MessageBatch messageBatch)
  {
    BatchReport report = messageBatch.getBatchReport();
    /*out.println("    <h2><a name=\"quality\">Quality</h2>");
    out.println("    <p class=\"dottedbox\">");
    out.println("      Quality  measures the number of errors and warnings that are encountered");
    out.println("      during processing. Total errors registry must account for less than ");
    out.println("      one percent of total number of ");
    out.println("      patients and vaccinations. ");
    out.println("      Total warnings registered are expected to account for ");
    out.println("      less than ten percent of the total patients and vaccinations. ");
    out.println("    </p>");
    out.println("    <h3>Quality Score</h3>");
    printScoringSummary("Quality", report.getQualityScore());
    out.println("    <table width=\"400\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Measurement</th>");
    out.println("        <th align=\"center\">Score</th>");
    out.println("        <th align=\"center\">Description</th>");
    out.println("        <th align=\"center\">Weight</th>");
    out.println("      </tr>");
    printScore(QUALITY_SCORE_NO_ERRORS, report.getQualityErrorScore(), per(modelForm.getAbsoluteWeight("quality.errors")), REQUIRED);
    printScore(QUALITY_SCORE_NO_WARNINGS, report.getQualityWarnScore(), per(modelForm.getAbsoluteWeight("quality.warnings")), REQUIRED);
    out.println("    </table>");
    out.println("    <br/>");
    if (qualityCollector.getInvalidCodes().size() > 0 || qualityCollector.getUnrecognizedCodes().size() > 0
        || qualityCollector.getErrorIssues().size() > 0)
    {
      out.println("    <table width=\"400\">");
      out.println("      <tr>");
      out.println("        <th align=\"left\">Coded Value Issues</th>");
      out.println("        <th align=\"center\">Count</th>");
      out.println("      </tr>");
      if (qualityCollector.getInvalidCodes().size() > 0)
      {
        printRow(QUALITY_SCORE_INVALID, true, num(qualityCollector.getInvalidCodes().size()));
      }
      if (qualityCollector.getUnrecognizedCodes().size() > 0)
      {
        printRow(QUALITY_SCORE_UNRECOGNIZED, true, num(qualityCollector.getUnrecognizedCodes().size()));
      }
      if (qualityCollector.getDeprecatedCodes().size() > 0)
      {
        printRow(QUALITY_SCORE_DEPRECATED, true, num(qualityCollector.getDeprecatedCodes().size()));
      }
      out.println("    </table>");
    }*/
    if (qualityCollector.getErrorIssues().size() > 0)
    {
      out.println("      <p class=\"dottedbox\">Errors are expected to be encountered on less than one percent of messages.</p>");
      out.println("    <h3><a name=\"quality.errors\">Errors</h3>");
      printBatchIssues(qualityCollector.getErrorIssues());
    }
    if (qualityCollector.getWarnIssues().size() > 0)
    {
      out.println("      <p class=\"dottedbox\">Warning to message size rate is expected to be less than ten percent.</p>");
      out.println("    <h3><a name=\"quality.warnings\">Warnings</h3>");
      printBatchIssues(qualityCollector.getWarnIssues());
    }
    if (qualityCollector.getSkipIssues().size() > 0)
    {
      out.println("    <h3><a name=\"quality.skips\">Skips</h3>");
      printBatchIssues(qualityCollector.getSkipIssues());
    }
    if (qualityCollector.getInvalidCodes().size() > 0)
    {
      out.println("    <h3><a name=\"quality.invalidCodes\">Invalid Codes</h3>");
      printCodeReceived(qualityCollector.getInvalidCodes());
    }
    if (qualityCollector.getUnrecognizedCodes().size() > 0)
    {
      out.println("    <h3><a name=\"quality.unrecognizedCodes\">Unrecognized Codes</h3>");
      printCodeReceived(qualityCollector.getUnrecognizedCodes());
    }
    if (qualityCollector.getDeprecatedCodes().size() > 0)
    {
      out.println("    <h3><a name=\"quality.deprecatedCodes\">Deprecated Codes</h3>");
      printCodeReceived(qualityCollector.getDeprecatedCodes());
    }
  }

  private void printCodesReceived(MessageBatch messageBatch)
  {
    out.println("    <h2><a name=\"quality\">Codes Received</h2>");
    Map<CodeReceived, BatchCodeReceived> codeReceivedMap = messageBatch.getBatchCodeReceivedMap();
    Map<CodeTable, List<BatchCodeReceived>> codeTableMap = new HashMap<CodeTable, List<BatchCodeReceived>>();

    for (CodeReceived codeReceived : codeReceivedMap.keySet())
    {
      List<BatchCodeReceived> batchCodeReceivedList = codeTableMap.get(codeReceived.getTable());
      if (batchCodeReceivedList == null)
      {
        batchCodeReceivedList = new ArrayList<BatchCodeReceived>();
        codeTableMap.put(codeReceived.getTable(), batchCodeReceivedList);
      }
      batchCodeReceivedList.add(codeReceivedMap.get(codeReceived));
    }
    List<CodeTable> codeTableList = new ArrayList<CodeTable>(codeTableMap.keySet());
    Collections.sort(codeTableList, new Comparator<CodeTable>() {
      public int compare(CodeTable arg0, CodeTable arg1)
      {
        return arg0.getTableLabel().compareTo(arg1.getTableLabel());
      }
    });

    for (CodeTable codeTable : codeTableList)
    {
      out.println("    <h3><a name=\"codesReceived." + codeTable.getTableId() + "\">" + codeTable.getTableLabel() + "</h3>");
      printCodeReceived(codeTableMap.get(codeTable));
    }
  }

  private void printTimeliness(MessageBatch messageBatch)
  {
    BatchReport report = messageBatch.getBatchReport();
    out.println("    <h2><a name=\"timeliness\">Timeliness</h2>");
    out.println("    <p class=\"dottedbox\">");
    out.println("      Timeliness measures the number of days between the");
    out.println("      date a message was received and the most recent administered vaccination");
    out.println("      indicated in that message. ");
    out.println("      Submitters should send administered vaccinations as soon as possible after");
    out.println("      administration, normally once a week. ");
    out.println("    </p>");
    out.println("    <h3>Timeliness Score</h3>");
    printScoringSummary("Timeliness", report.getTimelinessScore());
    out.println("    <table width=\"400\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Measurement</th>");
    out.println("        <th align=\"center\">Score</th>");
    out.println("        <th align=\"center\">Description</th>");
    out.println("        <th align=\"center\">Weight</th>");
    out.println("      </tr>");
    if (modelForm.getWeight("timeliness.early") > 0 && report.getTimelinessScoreEarly() > 0)
    {
      printScore(TIMELINESS_SCORE_EARLY, report.getTimelinessScoreEarly(), per(modelForm.getAbsoluteWeight("timeliness.early")), EXTRA_CREDIT);
    }
    if (modelForm.getWeight("timeliness.onTime") > 0 && report.getTimelinessScoreOnTime() > 0)
    {
      printScore(TIMELINESS_SCORE_ON_TIME, report.getTimelinessScoreOnTime(), per(modelForm.getAbsoluteWeight("timeliness.onTime")), REQUIRED);
    }
    if (modelForm.getWeight("timeliness.late") > 0 && report.getTimelinessScoreLate() > 0)
    {
      printScore(TIMELINESS_SCORE_LATE, report.getTimelinessScoreLate(), per(modelForm.getAbsoluteWeight("timeliness.late")), DEMERIT);
    }
    if (modelForm.getWeight("timeliness.veryLate") > 0 && report.getTimelinessScoreVeryLate() > 0)
    {
      printScore(TIMELINESS_SCORE_VERY_LATE, report.getTimelinessScoreVeryLate(), per(modelForm.getAbsoluteWeight("timeliness.veryLate")), DEMERIT);
    }
    out.println("    </table>");
    out.println("    <br/>");
    out.println("    <table width=\"350\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Vaccination Received</th>");
    out.println("        <th align=\"center\">Count</th>");
    out.println("        <th align=\"center\">Percent</th>");
    out.println("      </tr>");
    if (modelForm.getWeight("timeliness.early") > 0 && report.getTimelinessCountEarly() > 0)
    {
      printPer(TIMELINESS_EARLY, report.getTimelinessCountEarly(), report.getMessageWithAdminCount());
    }
    if (modelForm.getWeight("timeliness.onTime") > 0 && report.getTimelinessCountOnTime() > 0)
    {
      printPer(TIMELINESS_ON_TIME, report.getTimelinessCountOnTime(), report.getMessageWithAdminCount());
    }
    if (modelForm.getWeight("timeliness.late") > 0 && report.getTimelinessCountLate() > 0)
    {
      printPer(TIMELINESS_LATE, report.getTimelinessCountLate(), report.getMessageWithAdminCount());
    }
    if (modelForm.getWeight("timeliness.veryLate") > 0 && report.getTimelinessCountVeryLate() > 0)
    {
      printPer(TIMELINESS_VERY_LATE, report.getTimelinessCountVeryLate(), report.getMessageWithAdminCount());
    }
    if (modelForm.getWeight("timeliness.oldData") > 0 && report.getTimelinessCountOldData() > 0)
    {
      printPer(TIMELINESS_OLD_DATA, report.getTimelinessCountOldData(), report.getMessageWithAdminCount());
    }
    out.println("    </table>");
    out.println("    <br/>");
    out.println("    <table>");
    out.println("      <tr>");
    out.println("        <th align=\"left\" colspan=\"2\">Timeliness of Vaccination Update</th>");
    out.println("      </tr>");
    if (report.getTimelinessDateFirst() != null && report.getTimelinessDateLast() != null)
    {
      out.println("      <tr>");
      out.println("        <th align=\"left\">Vaccination Admininistered</th>");
      out.println("        <td>" + makeDateRange(report.getTimelinessDateFirst(), report.getTimelinessDateLast()) + "</th>");
      out.println("      </tr>");
    }
    out.println("      <tr>");
    out.println("        <th align=\"left\">Batch Received</th>");
    out.println("        <td>" + makeDateRange(messageBatch.getStartDate(), messageBatch.getEndDate()) + "</th>");
    out.println("      </tr>");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Average Elapsed Days</th>");
    DecimalFormat df = new DecimalFormat("0.0");
    out.println("        <td>" + df.format(report.getTimelinessAverage()) + "</th>");
    out.println("      </tr>");
    out.println("    </table>");

  }

  private void printScore(ToolTip tip, int score, String weight, char type)
  {
    printRow(tip, score < 70, String.valueOf(score), getScoreDescription(score, type), weight);
  }

  private void printScoreOverall(ToolTip tip, int score, String weight, char type)
  {
    printRow(tip, false, String.valueOf(score), getScoreDescription(score, type), weight);
  }

  private void printCodeReceived(Set<CodeReceived> codeReceivedSet)
  {
    List<CodeReceived> codeReceivedList = new ArrayList<CodeReceived>(codeReceivedSet);
    Collections.sort(codeReceivedList, new Comparator<CodeReceived>() {
      public int compare(CodeReceived arg0, CodeReceived arg1)
      {
        int c = arg0.getTable().getTableLabel().compareTo(arg1.getTable().getTableLabel());
        if (c == 0)
        {
          return arg0.getReceivedValue().compareTo(arg1.getReceivedValue());
        }
        return c;
      }
    });
    out.println("    <table width=\"550\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Table</th>");
    out.println("        <th align=\"center\">Value</th>");
    out.println("        <th align=\"center\">Label</th>");
    out.println("        <th align=\"center\">Mapped To</th>");
    out.println("      </tr>");
    for (CodeReceived cr : codeReceivedList)
    {
      printRow(cr.getTable().getTableLabel(), cr.getReceivedValue(), cr.getCodeLabel(), cr.getCodeValue());
    }
    out.println("    </table>");
  }

  private void printCodeReceived(List<BatchCodeReceived> batchCodeReceivedList)
  {
    Collections.sort(batchCodeReceivedList, new Comparator<BatchCodeReceived>() {
      public int compare(BatchCodeReceived arg0, BatchCodeReceived arg1)
      {
        return arg0.getCodeReceived().getReceivedValue().compareTo(arg1.getCodeReceived().getReceivedValue());
      }
    });
    out.println("    <table width=\"550\">");
    out.println("      <tr>");
    out.println("        <th align=\"center\">Value</th>");
    out.println("        <th align=\"center\">Label</th>");
    out.println("        <th align=\"center\">Mapped To</th>");
    out.println("        <th align=\"center\">Status</th>");
    out.println("        <th align=\"center\">Count</th>");
    out.println("      </tr>");
    for (BatchCodeReceived bcr : batchCodeReceivedList)
    {
      printRow(bcr.getCodeReceived().getReceivedValue(), bcr.getCodeReceived().getCodeLabel(), bcr.getCodeReceived().getCodeValue(), bcr
          .getCodeReceived().getCodeStatus().getCodeLabel(), "" + bcr.getReceivedCount());
    }
    out.println("    </table>");
  }

  private void printBatchIssues(List<BatchIssues> batchIssuesList)
  {
    Collections.sort(batchIssuesList, new Comparator<BatchIssues>() {
      public int compare(BatchIssues arg0, BatchIssues arg1)
      {
        Integer count0 = arg0.getIssueCount();
        Integer count1 = arg1.getIssueCount();
        return count1.compareTo(count0);
      }
    });
    out.println("    <table width=\"550\">");
    out.println("      <tr>");
    out.println("        <th align=\"left\">Description</th>");
    out.println("        <th align=\"center\">Count</th>");
    out.println("        <th align=\"center\">Percent</th>");
    out.println("      </tr>");
    for (BatchIssues batchIssues : batchIssuesList)
    {
      potentialIssueFoundSet.add(batchIssues.getIssue());
      PotentialIssue issue = batchIssues.getIssue();
      int denominator = 0;
      ReportDenominator reportDenominator = ReportDenominator.valueOf(issue.getReportDenominator().toUpperCase().trim().replace(' ', '_'));
      if (reportDenominator == ReportDenominator.MESSAGE_COUNT)
      {
        denominator = messageCount;
      } else if (reportDenominator == ReportDenominator.PATIENT_COUNT)
      {
        denominator = patientCount;
      } else if (reportDenominator == ReportDenominator.PATIENT_UNDERAGE_COUNT)
      {
        denominator = patientUnderageCount;
      } else if (reportDenominator == ReportDenominator.NEXTOFKIN_COUNT)
      {
        denominator = nextOfKinCount;
      } else if (reportDenominator == ReportDenominator.VACCINATION_COUNT)
      {
        denominator = vaccinationCount;
      } else if (reportDenominator == ReportDenominator.VACCINATION_ADMIN_COUNT)
      {
        denominator = vaccinationAdministeredCount;
      }
      if (issue.getTable() != null)
      {
        issue.getToolTip().setLink("#codesReceived." + issue.getTable().getTableId());
      }
      printPer(issue.getToolTip(), batchIssues.getIssueCount(), denominator);
    }
    out.println("    </table>");
  }

  private void printPer(ToolTip toolTip, int value, int denominator)
  {
    printPer(toolTip, value, denominator, 0, 100);
  }

  private void printPer(ToolTip toolTip, int value, int denominator, int okayLow, int okayHigh)
  {
    String percent = "&nbsp;";
    boolean outOfRange = false;
    if (denominator != 0)
    {
      if (value == 0)
      {
        percent = "-";
      } else
      {
        int per = (int) ((100.0 * value) / denominator + 0.5);
        if (per < okayLow || per > okayHigh)
        {
          outOfRange = true;
        }
        percent = String.valueOf(per) + "%";
      }
    }
    printRow(toolTip, outOfRange, num(value), percent);
  }

  private void print(CompletenessRow row, double overallWeight, double weightFactor, char type)
  {
    ToolTip toolTip = row.getToolTip();
    int value = row.getCount();
    int denominator = row.getDenominator();
    String percent = "&nbsp;";
    boolean outOfRange = false;
    String description = "";
    if (row.getTable() != null)
    {
      toolTip.setLink("#codesReceived." + row.getTable().getTableId());
    }
    if (type == REQUIRED || type == OPTIONAL)
    {
      if (row.getScore() < 0)
      {
        type = DEMERIT;
      }
    }
    if (row.getDenominator() > 0)
    {
      if (value == 0)
      {
        percent = "-";
        description = getScoreDescription(0, type);
      } else
      {
        int per = (int) ((100.0 * value) / denominator + 0.5);
        if (per < row.getOkayLow() || per > row.getOkayHigh())
        {
          outOfRange = true;
        }
        percent = String.valueOf(per) + "%";
        description = getScoreDescription(per, type);
      }
    }
    if (row.getScoreWeight() == 0)
    {
      printRowCompleteness(toolTip, outOfRange, row.getHl7Reference(), num(value), percent);
    } else
    {
      String scorePercent = "&nbsp;";
      if (overallWeight > 0)
      {
        Double score = row.getScoreWeight() / overallWeight;
        if (score > 1.0)
        {
          score = 1.0;
        }
        if (score < -1.0)
        {
          score = -1.0;
        }
        score = 100.0 * score * weightFactor;
        DecimalFormat df = new DecimalFormat("0.0");
        scorePercent = df.format(score) + "%";
      }
      if (row.getScoreWeight() < 0)
      {
        outOfRange = true;
      }
      printRowCompleteness(toolTip, outOfRange, row.getHl7Reference(), num(value), percent, description, scorePercent);
    }
  }

  protected void printRow(String... fields)
  {
    out.println("      <tr>");
    String align = "left";
    for (int i = 0; i < fields.length; i++)
    {
      out.println("        <td align=\"" + align + "\">" + (fields[i] == null ? "&nbsp;" : fields[i]) + "</td>");
      align = "center";
    }
    out.println("      </tr>");
  }

  private void printRow(ToolTip tip, boolean showRed, String... fields)
  {
    String cssClass = showRed ? " class=\"alert\"" : "";
    if (tip.isEmphasize())
    {
      cssClass = " class=\"highlight\"";
    }
    out.println("      <tr>");
    out.println("        <td align=\"left\"" + cssClass + ">" + tip.getHtml());
    out.println("        </td>");
    for (int i = 0; i < fields.length; i++)
    {
      out.println("        <td align=\"center\"" + cssClass + ">" + fields[i] + "</td>");
    }
    out.println("      </tr>");
  }

  private void printRowCompleteness(ToolTip tip, boolean showRed, String... fields)
  {
    String cssClass = showRed ? " class=\"alert\"" : "";
    if (tip.isEmphasize())
    {
      cssClass = " class=\"highlight\"";
    }
    out.println("      <tr>");
    out.println("        <td align=\"left\"" + cssClass + ">" + tip.getHtml());
    out.println("        </td>");
    for (int i = 0; i < fields.length; i++)
    {
      if (i == 0)
      {
        out.println("        <td align=\"left\"" + cssClass + ">" + fields[i] + "</td>");
      } else
      {
        out.println("        <td align=\"center\"" + cssClass + ">" + fields[i] + "</td>");
      }
    }
    out.println("      </tr>");
  }

  // Steel
  private static final String VERY_LIGHT = "#FFFFFF";
  private static final String LIGHT = "#CCDDDD";
  private static final String MEDIUM_LIGHT = " #AAC4C4;";
  private static final String MEDIUM = "#93AAAB";
  private static final String DARK = "#749749";
  private static final String BLACK = "#000000";
  private static final String RED = "#CE3100";
  private static final String BLUE = "#0031CE";

  protected static void printCss(PrintWriter out)
  {
    out.println("    <style><!--");
    out.println("      body {font-family: Tahoma, Geneva, Sans-serif}");
    out.println("      .dottedbox {width:700px; color:" + DARK + "; background:" + VERY_LIGHT
        + "; padding:6px; border-style:dashed; border-width:1px; border-color:" + LIGHT + "}");
    out.println("      h1 {color:" + BLACK + "; font-size:2.0em;}");
    out.println("      h2 {color:" + BLACK + "; font-size:2.0em; page-break-before:always;}");
    out.println("      h3 {color:" + BLACK + "; font-size:1.2em;}");
    out.println("      h4 {color:" + DARK + "; font-size:1.0em; }");
    out.println("      table {background:" + LIGHT + "; border-style:solid; border-width:1; border-color:" + MEDIUM + "; border-collapse:collapse}");
    out.println("      th {background:" + MEDIUM + "; font-size:0.8em; color:" + BLACK + "; border-style:none; padding-left:5px; padding-right:5px;}");
    out.println("      td {border-style:solid; border-width:1; border-color:" + MEDIUM + ";margin:0px; padding-left:5px; padding-right:5px;}");
    out.println("      pre {border-style:solid; border-width:1; border-color:" + MEDIUM + "; background:" + LIGHT + "; color: " + DARK
        + "; font-size=-1}");
    out.println("      .score {font-size:1.5em;}");
    out.println("      .alert {}");
    out.println("      .highlight {background: " + MEDIUM_LIGHT + ";}");
    out.println("      .excellent {color:" + BLUE + "; font-style:bold;}");
    out.println("      .good {color:" + BLUE + "; font-style:bold;}");
    out.println("      .poor {color:" + RED + "; font-style:bold;}");
    out.println("      .problem {color:" + RED + "; font-style:bold;}");
    out.println("      a:link {text-decoration:none; color:" + BLACK + "}");
    out.println("      a:visited {text-decoration:none; color:" + BLACK + "}");
    out.println("      a:hover {text-decoration:none; color:" + BLACK + "} ");
    out.println("      a:active {text-decoration:none; color:" + BLACK + "} ");
    out.println("      a.tooltip span {display:none; padding:2px 3px; margin-left:8px; width:130px;}");
    out.println("      a.tooltip:hover span{display:inline; position:absolute; background:" + LIGHT + "; border:1px solid " + DARK + "; color:"
        + DARK + "}");
    out.println("    --></style>");
  }

  private static char EXTRA_CREDIT = 'E';
  private static char REQUIRED = 'R';
  private static char OPTIONAL = '0';
  private static char DEMERIT = 'D';

  private String getScoreDescription(int score, char type)
  {
    if (type == EXTRA_CREDIT)
    {
      if (score > 0)
      {
        return "<span class=\"excellent\">Excellent</span>";
      } else
      {
        return "&nbsp;";
      }
    } else if (type == DEMERIT)
    {
      if (score > 0)
      {
        return "<span class=\"problem\">Problem</span>";
      } else
      {
        return "&nbsp;";
      }
    } else if (type == OPTIONAL)
    {

      if (score >= 90)
      {
        return "<span class=\"excellent\">Excellent</span>";
      } else if (score >= 80)
      {
        return "<span class=\"good\">Good</span>";
      } else if (score >= 0)
      {
        return "<span class=\"okay\">Okay</span>";
      } else
      {
        return "&nbsp;";
      }
    }

    if (score >= 90)
    {
      return "<span class=\"excellent\">Excellent</span>";
    } else if (score >= 80)
    {
      return "<span class=\"good\">Good</span>";
    } else if (score >= 70)
    {
      return "<span class=\"okay\">Okay</span>";
    } else if (score >= 60)
    {
      return "<span class=\"poor\">Poor</span></span>";
    } else
    {
      return "<span class=\"problem\">Problem</span>";
    }
  }

  DecimalFormat df = new DecimalFormat("#,##0");

  private String num(int i)
  {
    return df.format(i);
  }

  private String per(double d)
  {
    return ((int) (100.0 * d + 0.5)) + "%";
  }

  private List<Field> fieldList = null;
  PotentialIssues potentialIssues = null;
  List<PotentialIssueStatus> potentialIssueStatusList = null;


  public void printDocumentation(MessageBatch messageBatch)
  {
    initDocumentation(messageBatch);

    Map<PotentialIssue, PotentialIssueStatus> potentialIssueStatusMap = new HashMap<PotentialIssue, PotentialIssueStatus>();

    for (PotentialIssueStatus potentialIssueStatus : potentialIssueStatusList)
    {
      if (potentialIssueFoundSet.contains(potentialIssueStatus.getIssue()))
      {
        potentialIssueStatusMap.put(potentialIssueStatus.getIssue(), potentialIssueStatus);
        
      }
    }

    Map<PotentialIssue, MessageReceived> examples = potentialIssueFoundMessageReceivedExample;

    out.println("<h2>Issues Found Documentation</h2>");

    for (Field field : fieldList)
    {
      out.print(potentialIssues.getDocumentationForAnalysis(field, potentialIssueStatusMap, examples));
    }
  }

  public void printDocumentation(MessageBatch messageBatch, Set<PotentialIssue> potentialIssueFoundSet, PrintWriter printWriter)
  {
    initDocumentation(messageBatch);

    Map<PotentialIssue, PotentialIssueStatus> potentialIssueStatusMap = new HashMap<PotentialIssue, PotentialIssueStatus>();

    for (PotentialIssueStatus potentialIssueStatus : potentialIssueStatusList)
    {
      if (potentialIssueFoundSet.contains(potentialIssueStatus.getIssue()))
      {
        potentialIssueStatusMap.put(potentialIssueStatus.getIssue(), potentialIssueStatus);
      }
    }

    printWriter.println("<h2>Issues Found Documentation</h2>");

    for (Field field : fieldList)
    {
      printWriter.print(potentialIssues.getDocumentationForAnalysis(field, potentialIssueStatusMap, null));
    }
  }

  public void initDocumentation(MessageBatch messageBatch)
  {
    if (potentialIssueStatusList == null)
    {
      potentialIssues = PotentialIssues.getPotentialIssues();
      fieldList = potentialIssues.getAllFields();
      Collections.sort(fieldList);

      Set<Field> fieldSet = new HashSet<Field>();

      Query query;
      query = session.createQuery("from PotentialIssueStatus where profile = ?");
      query.setParameter(0, messageBatch.getProfile());
      potentialIssueStatusList = query.list();
    }
  }

}
