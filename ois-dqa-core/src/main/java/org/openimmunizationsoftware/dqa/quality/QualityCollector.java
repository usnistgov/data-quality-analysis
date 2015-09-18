/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openimmunizationsoftware.dqa.db.model.BatchCodeReceived;
import org.openimmunizationsoftware.dqa.db.model.BatchIssues;
import org.openimmunizationsoftware.dqa.db.model.BatchReport;
import org.openimmunizationsoftware.dqa.db.model.BatchType;
import org.openimmunizationsoftware.dqa.db.model.BatchVaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.MessageHeader;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.MessageBatch;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssueStatus;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineGroup;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.manager.VaccineGroupManager;
import org.openimmunizationsoftware.dqa.quality.model.ModelFactory;
import org.openimmunizationsoftware.dqa.quality.model.ModelForm;
import org.openimmunizationsoftware.dqa.quality.model.ModelSection;

public class QualityCollector
{
  private Set<String> patientIds = new HashSet<String>();
  private Set<String> vaccinationIds = new HashSet<String>();
  private MessageBatch messageBatch = null;
  private BatchReport report = null;
  private SubmitterProfile profile = null;
  private List<BatchIssues> errorIssues = new ArrayList<BatchIssues>();
  private List<BatchIssues> warnIssues = new ArrayList<BatchIssues>();
  private List<BatchIssues> skipIssues = new ArrayList<BatchIssues>();
  private Date vaccinationAdminDateEarliest = null;
  private Date vaccinationAdminDateLatest = null;
  private int numeratorVaccinationAdminDateAge = 0;
  private QualityScoring scoring = null;
  private ModelForm modelForm = null;
  private MessageHeader exampleHeader = null;

  public void setExampleHeader(MessageHeader exampleHeader)
  {
    this.exampleHeader = exampleHeader;
  }

  public MessageHeader getExampleHeader()
  {
    return exampleHeader;
  }

  public ModelForm getModelForm()
  {
    return modelForm;
  }

  public int getVaccineCvxCount(VaccineCvx vaccineCvx)
  {
    BatchVaccineCvx batchVaccineCvx = messageBatch.getBatchVaccineCvxMap().get(vaccineCvx);
    if (batchVaccineCvx == null)
    {
      return 0;
    }
    return batchVaccineCvx.getReceivedCount();
  }

  public QualityScoring getCompletenessScoring()
  {
    return scoring;
  }

  private static final long AS_THE_DAY_IS_LONG = 24 * 60 * 60 * 1000;

  public MessageBatch getMessageBatch()
  {
    return messageBatch;
  }

  public QualityCollector(String title, BatchType batchType, SubmitterProfile profile) {
    messageBatch = new MessageBatch();
    messageBatch.setBatchTitle(title);
    messageBatch.setStartDate(new Date());
    messageBatch.setEndDate(new Date());
    messageBatch.setBatchType(batchType);
    messageBatch.setProfile(profile);
    this.profile = profile;
    modelForm = ModelFactory.createModelForm(profile);
    report = messageBatch.getBatchReport();

  }

  public void registerCodeReceived(CodeReceived codeReceived)
  {
    messageBatch.getBatchCodeReceived(codeReceived).incReceivedCount();
  }

  public void registerProcessedMessage(MessageReceived messageReceived)
  {
    if (exampleHeader == null)
    {
      exampleHeader = messageReceived.getMessageHeader();
    }
    report.incMessageCount();
    String patientId = messageReceived.getPatient().getIdSubmitter().getNumber();
    if (patientId == null || patientId.equals(""))
    {
      patientId = "MESSAGE#" + report.getMessageCount();
    }
    if (!patientIds.contains(patientId))
    {
      patientIds.add(patientId);
    } else
    {
      // TODO register issue, can't do here, too late
    }
    report.incPatientCount();
    if (messageReceived.getPatient().getBirthDate() != null)
    {
      boolean underage = isUnderage(messageReceived);
      if (underage)
      {
        report.incPatientUnderageCount();
      }
    }
    messageBatch.incBatchActionCount(messageReceived.getIssueAction());
    for (IssueFound issueFound : messageReceived.getIssuesFound())
    {
      messageBatch.incBatchIssueCount(issueFound.getIssue());
    }
    report.incNextOfKinCount(messageReceived.getNextOfKins().size());
    int vacPos = 0;
    long timelinessGap = Long.MAX_VALUE;
    Date latestAdmin = null;
    boolean hadAdmin = false;
    for (Vaccination vaccination : messageReceived.getVaccinations())
    {
      vacPos++;
      String vaccinationId = vaccination.getIdSubmitter();
      if (vaccinationId == null || vaccinationId.equals(""))
      {
        if (vaccination.getAdminCode() != null && !vaccination.getAdminCode().equals("")
            && vaccination.getAdminDate() != null)
        {
          vaccinationId = vaccination.getAdminCode() + "-" + vaccination.getAdminDate().getTime();
        } else
        {
          vaccinationId = "VACCINATION#" + vacPos;
        }
        vaccinationId = patientId + "-" + vaccinationId;
      }
      if (!vaccinationIds.contains(vaccinationId))
      {
        vaccinationIds.add(vaccinationId);
      } else
      {
        // TODO register issue
      }
      if (vaccination.isActionDelete())
      {
        report.incVaccinationDeleteCount();
      } else if (vaccination.isCompletionNotAdministered() || vaccination.isCompletionPartiallyAdministered()
          || vaccination.isCompletionRefused())
      {
        report.incVaccinationNotAdministeredCount();
        if (vaccination.isCompletionRefused())
        {
          report.incVaccinationRefusalCount();
        }
      } else if (vaccination.isInformationSourceAdmin())
      {
        report.incVaccinationAdministeredCount();
        hadAdmin = true;
        if (vaccination.getAdminDate() != null)
        {
          long diff = messageReceived.getReceivedDate().getTime() - vaccination.getAdminDate().getTime();
          if (diff >= 0 && diff < timelinessGap)
          {
            timelinessGap = diff;
            latestAdmin = vaccination.getAdminDate();
          }
        }
        if (vaccination.getVaccineCvx() != null)
        {
          messageBatch.getBatchVaccineCvx(vaccination.getVaccineCvx()).incReceivedCount();
        }
      } else
      {
        report.incVaccinationHistoricalCount();
      }
      report.incObservationCount(vaccination.getObservations().size());
      report.incVaccinationVisCount(vaccination.getVaccinationVisList().size());
    }

    if (hadAdmin)
    {
      report.incMessageWithAdminCount();
      if (timelinessGap < Long.MAX_VALUE)
      {
        numeratorVaccinationAdminDateAge += (timelinessGap / AS_THE_DAY_IS_LONG);
      }
      if (latestAdmin != null)
      {
        if (vaccinationAdminDateEarliest == null || latestAdmin.before(vaccinationAdminDateEarliest))
        {
          vaccinationAdminDateEarliest = latestAdmin;
        }
        if (vaccinationAdminDateLatest == null || latestAdmin.after(vaccinationAdminDateLatest))
        {
          vaccinationAdminDateLatest = latestAdmin;
        }
      }
      int dayEarly = modelForm.getModelSection("timeliness.early").getDays();
      int dayOnTime = modelForm.getModelSection("timeliness.onTime").getDays();
      int dayLate = modelForm.getModelSection("timeliness.late").getDays();
      int dayVeryLate = modelForm.getModelSection("timeliness.veryLate").getDays();
      if (timelinessGap < (AS_THE_DAY_IS_LONG * dayEarly))
      {
        report.incTimelinessCountEarly();
      } else if (timelinessGap < (AS_THE_DAY_IS_LONG * dayOnTime))
      {
        report.incTimelinessCountOnTime();
      } else if (timelinessGap < (AS_THE_DAY_IS_LONG * dayLate))
      {
        report.incTimelinessCountLate();
      } else if (timelinessGap < (AS_THE_DAY_IS_LONG * dayVeryLate))
      {
        report.incTimelinessCountVeryLate();
      } else
      {
        report.incTimelinessCountOldData();
      }
    }
  }

  private boolean isUnderage(MessageReceived messageReceived)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(messageReceived.getReceivedDate());
    cal.add(Calendar.YEAR, -18);
    boolean underage = cal.getTime().before(messageReceived.getPatient().getBirthDate());
    return underage;
  }

  public void score()
  {
    scoreTimeliness();
    scoreQuality();
    scoreCompleteness();
    int overallScore = (int) (modelForm.getWeight("timeliness") * report.getTimelinessScore()
        + modelForm.getWeight("quality") * report.getQualityScore() + modelForm.getWeight("completeness")
        * report.getCompletenessScore() + 0.5);
    report.setOverallScore(overallScore);
  }

  private void scoreCompleteness()
  {
    scoring = new QualityScoring(modelForm);
    Map<PotentialIssue, BatchIssues> batchIssuesMap = messageBatch.getBatchIssuesMap();
    ScoringSet patientExpected = scoring.getScoringSet(QualityScoring.PATIENT_EXPECTED);
    ScoringSet patientOptional = scoring.getScoringSet(QualityScoring.PATIENT_OPTIONAL);
    ScoringSet patientRecommended = scoring.getScoringSet(QualityScoring.PATIENT_RECOMMENDED);
    ScoringSet patientRequired = scoring.getScoringSet(QualityScoring.PATIENT_REQUIRED);
    ScoringSet vaccinationExpected = scoring.getScoringSet(QualityScoring.VACCINATION_EXPECTED);
    ScoringSet vaccinationOptional = scoring.getScoringSet(QualityScoring.VACCINATION_OPTIONAL);
    ScoringSet vaccinationRecommended = scoring.getScoringSet(QualityScoring.VACCINATION_RECOMMENDED);
    ScoringSet vaccinationRequired = scoring.getScoringSet(QualityScoring.VACCINATION_REQUIRED);

    score(batchIssuesMap, patientExpected);
    score(batchIssuesMap, patientOptional);
    score(batchIssuesMap, patientRecommended);
    score(batchIssuesMap, patientRequired);
    score(batchIssuesMap, vaccinationExpected);
    score(batchIssuesMap, vaccinationOptional);
    score(batchIssuesMap, vaccinationRecommended);
    score(batchIssuesMap, vaccinationRequired);
    double patientScore = patientExpected.getWeightedScore() + patientOptional.getWeightedScore()
        + patientRecommended.getWeightedScore() + patientRequired.getWeightedScore();
    double vaccinationScore = vaccinationExpected.getWeightedScore() + vaccinationOptional.getWeightedScore()
        + vaccinationRecommended.getWeightedScore() + vaccinationRequired.getWeightedScore();

    double vaccineGroupScore = scoreVaccineGroupScore();

    int completenessScore = (int) (100.0 * patientScore * modelForm.getWeight("completeness.patient") + 100.0
        * vaccinationScore * modelForm.getWeight("completeness.vaccination") + 100.0 * vaccineGroupScore
        * modelForm.getWeight("completeness.vaccineGroup") + 0.5);
    report.setCompletenessPatientScore((int) (patientScore * 100 + 0.5));
    report.setCompletenessVaccinationScore((int) (vaccinationScore * 100 + 0.5));
    report.setCompletenessVaccineGroupScore((int) (vaccineGroupScore * 100 + 0.5));
    report.setCompletenessScore(completenessScore);
  }

  private double scoreVaccineGroupScore()
  {
    VaccineGroupManager vaccineGroupManager = VaccineGroupManager.getVaccineGroupManager();
    double score = 0.0;
    score = scoreVaccineGroup(vaccineGroupManager, modelForm.getModelSection("completeness.vaccineGroup.expected"));
    score = score
        + scoreVaccineGroup(vaccineGroupManager, modelForm.getModelSection("completeness.vaccineGroup.recommended"));
    score = score
        + scoreVaccineGroup(vaccineGroupManager, modelForm.getModelSection("completeness.vaccineGroup.unexpected"));
    if (score < 0)
    {
      score = 0;
    }
    return score;
  }

  private double scoreVaccineGroup(VaccineGroupManager vaccineGroupManager, ModelSection vgsection)
  {
    float score = 0;
    float denominator = 0;
    for (ModelSection section : vgsection.getSections())
    {
      denominator += section.getWeight();
      VaccineGroup vaccineGroup = vaccineGroupManager.getVaccineGroup(section.getName());
      if (vaccineGroup == null)
      {
        throw new IllegalArgumentException("Invalid vaccine group name '" + section.getName() + "'");
      }
      for (VaccineCvx vaccineCvx : vaccineGroup.getVaccineCvxList())
      {
        if (getVaccineCvxCount(vaccineCvx) > 0)
        {
          score += section.getWeight();
          break;
        }
      }
    }
    return (denominator > 0 ? score / denominator : 0) * vgsection.getWeight();
  }

  private void score(Map<PotentialIssue, BatchIssues> batchIssuesMap, ScoringSet scoringSet)
  {
    List<CompletenessRow> completenessRows = scoringSet.getCompletenessRow();
    double overallScore = 0.0;
    double overallWeight = 0;
    for (CompletenessRow completenessRow : completenessRows)
    {
      PotentialIssue potentialIssue = completenessRow.getPotentialIssue();
      int numerator = 0;
      numerator = getNumerator(batchIssuesMap, potentialIssue, numerator);
      int denominator = getDenominator(completenessRow);
      if (denominator > 0)
      {
        if (completenessRow.isInvert())
        {
          numerator = invertNumerator(completenessRow, numerator, denominator);
        }
        double score = completenessRow.getScoreWeight() * numerator / (double) denominator;
        completenessRow.setScore(score);
        completenessRow.setCount(numerator);
        completenessRow.setDenominator(denominator);
        overallScore += score;
      }
      if (completenessRow.getScoreWeight() > 0)
      {
        overallWeight += completenessRow.getScoreWeight();
      }
    }
    scoringSet.setOverallWeight(overallWeight);
    double overallPercent = overallWeight > 0 ? overallScore / overallWeight : 0;
    if (overallPercent < 0)
    {
      overallPercent = 0;
    } else if (overallPercent > 1)
    {
      overallPercent = 1;
    }
    scoringSet.setScore(overallPercent);
    scoringSet.setWeightedScore(overallPercent * scoringSet.getWeight());
  }

  private int invertNumerator(CompletenessRow completenessRow, int numerator, int denominator)
  {
    // Most issues are inverted, for example if there are 100 patients and 10
    // have 'missing first name'
    // then the inverse is 90 patients are NOT missing first name
    if (completenessRow.isInvert())
    {
      if (numerator > denominator)
      {
        numerator = denominator;
      }
      numerator = denominator - numerator;
    }
    return numerator;
  }

  private int getNumerator(Map<PotentialIssue, BatchIssues> batchIssuesMap, PotentialIssue potentialIssue, int numerator)
  {
    BatchIssues batchIssues = batchIssuesMap.get(potentialIssue);
    if (batchIssues != null)
    {
      numerator = batchIssues.getIssueCount();
    }
    return numerator;
  }

  private int getDenominator(CompletenessRow completenessRow)
  {
    int denominator = 0;
    if (completenessRow.getReportDenominator() == ReportDenominator.MESSAGE_COUNT)
    {
      denominator = report.getMessageCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.NEXTOFKIN_COUNT)
    {
      denominator = report.getNextOfKinCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.OBSERVATION_COUNT)
    {
      denominator = report.getObservationCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.PATIENT_COUNT)
    {
      denominator = report.getPatientCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.PATIENT_UNDERAGE_COUNT)
    {
      denominator = report.getPatientUnderageCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.VACCINATION_COUNT)
    {
      denominator = report.getVaccinationCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.VACCINATION_ADMIN_COUNT)
    {
      denominator = report.getVaccinationAdministeredCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.VACCINATION_VIS_COUNT)
    {
      denominator = report.getVaccinationVisCount();
    } else if (completenessRow.getReportDenominator() == ReportDenominator.VACCINATION_REFUSAL_COUNT)
    {
      denominator = report.getVaccinationRefusalCount();
    }
    return denominator;
  }

  private void scoreQuality()
  {
    int errorCount = 0;
    int warningCount = 0;
    Map<PotentialIssue, BatchIssues> batchIssuesMap = messageBatch.getBatchIssuesMap();
    for (PotentialIssueStatus potentialIssueStatus : profile.getPotentialIssueStatusMap().values())
    {
      PotentialIssue potentialIssue = potentialIssueStatus.getIssue();
      BatchIssues batchIssues = batchIssuesMap.get(potentialIssue);
      if (batchIssues != null && batchIssues.getIssueCount() > 0)
      {
        if (potentialIssueStatus.getAction().isError())
        {
          errorIssues.add(batchIssues);
          errorCount += batchIssues.getIssueCount();
        } else if (potentialIssueStatus.getAction().isWarn())
        {
          warnIssues.add(batchIssues);
          warningCount += batchIssues.getIssueCount();
        } else if (potentialIssueStatus.getAction().isSkip())
        {
          skipIssues.add(batchIssues);
        }
      }
    }

    int denominator = report.getMessageCount() + report.getVaccinationCount();
    // If there are more than 10% errors then the score is 0.
    double denominatorScaled = 0.03 * denominator;
    double qualityErrorScore;
    if (errorCount > denominatorScaled)
    {
      qualityErrorScore = 0;
    } else
    {
      qualityErrorScore = 1.0 - (1.0 * errorCount / denominatorScaled);
    }
    if (qualityErrorScore < 0)
    {
      qualityErrorScore = 0;
    }
    denominatorScaled = 0.3 * denominator;
    double qualityWarnScore;
    if (warningCount > denominatorScaled)
    {
      qualityWarnScore = 0;
    } else
    {
      qualityWarnScore = 1.0 - (1.0 * warningCount / denominatorScaled);
    }
    if (qualityWarnScore < 0)
    {
      qualityWarnScore = 0;
    }
    report.setQualityWarnScore((int) (100.0 * qualityWarnScore + 0.5));
    report.setQualityErrorScore((int) (100.0 * qualityErrorScore + 0.5));
    report.setQualityScore((int) (100.0 * qualityErrorScore * modelForm.getWeight("quality.errors") + 100.0
        * qualityWarnScore * modelForm.getWeight("quality.warnings") + 0.5));
  }

  private void scoreTimeliness()
  {
    double scoreEarly = 0;
    double scoreOnTime = 0;
    double scoreLate = 0;
    double scoreVeryLate = 0;
    double timelinessAverage = 0;
    if (report.getMessageWithAdminCount() > 0)
    {
      scoreEarly = report.getTimelinessCountEarly() / report.getMessageWithAdminCount();
      scoreOnTime = report.getTimelinessCountOnTime() / report.getMessageWithAdminCount();
      scoreLate = report.getTimelinessCountLate() / report.getMessageWithAdminCount();
      scoreVeryLate = report.getTimelinessCountVeryLate() / report.getMessageWithAdminCount();
      timelinessAverage = numeratorVaccinationAdminDateAge / (double) report.getMessageWithAdminCount();
    }

    int timeliness = (int) (100.0 * (modelForm.getWeight("timeliness.early") * scoreEarly
        + modelForm.getWeight("timeliness.onTime") * scoreOnTime + modelForm.getWeight("timeliness.late") * scoreLate + modelForm
        .getWeight("timeliness.veryLate") * scoreVeryLate) + 0.5);
    if (timeliness > 100)
    {
      timeliness = 100;
    }
    if (timeliness < 0)
    {
      timeliness = 0;
    }
    report.setTimelinessScore(timeliness);
    report.setTimelinessScoreEarly((int) (100.0 * scoreEarly + 0.5));
    report.setTimelinessScoreOnTime((int) (100.0 * scoreOnTime + 0.5));
    report.setTimelinessScoreLate((int) (100.0 * scoreLate + 0.5));
    report.setTimelinessScoreVeryLate((int) (100.0 * scoreVeryLate + 0.5));
    // Set values if they have not already been set
    if (report.getTimelinessAverage() == 0.0)
    {
      report.setTimelinessAverage(timelinessAverage);
    }
    if (report.getTimelinessDateFirst() == null)
    {
      report.setTimelinessDateFirst(vaccinationAdminDateEarliest);
    }
    if (report.getTimelinessDateLast() == null)
    {
      report.setTimelinessDateLast(vaccinationAdminDateLatest);
    }
  }

  public void close()
  {
    messageBatch.setEndDate(new Date());
  }

  public List<BatchIssues> getErrorIssues()
  {
    return errorIssues;
  }

  public List<BatchIssues> getWarnIssues()
  {
    return warnIssues;
  }

  public List<BatchIssues> getSkipIssues()
  {
    return skipIssues;
  }

  public Set<CodeReceived> getInvalidCodes()
  {
    Set<CodeReceived> codes = new HashSet<CodeReceived>();
    for (BatchCodeReceived batchCode : messageBatch.getBatchCodeReceivedMap().values())
    {
      if (batchCode.getCodeReceived().getCodeStatus().isInvalid())
      {
        codes.add(batchCode.getCodeReceived());
      }
    }
    return codes;
  }

  public Set<CodeReceived> getUnrecognizedCodes()
  {
    Set<CodeReceived> codes = new HashSet<CodeReceived>();
    for (BatchCodeReceived batchCode : messageBatch.getBatchCodeReceivedMap().values())
    {
      if (batchCode.getCodeReceived().getCodeStatus().isUnrecognized())
      {
        codes.add(batchCode.getCodeReceived());
      }
    }
    return codes;
  }

  public Set<CodeReceived> getDeprecatedCodes()
  {
    Set<CodeReceived> codes = new HashSet<CodeReceived>();
    for (BatchCodeReceived batchCode : messageBatch.getBatchCodeReceivedMap().values())
    {
      if (batchCode.getCodeReceived().getCodeStatus().isDeprecated())
      {
        codes.add(batchCode.getCodeReceived());
      }
    }
    return codes;
  }

}
