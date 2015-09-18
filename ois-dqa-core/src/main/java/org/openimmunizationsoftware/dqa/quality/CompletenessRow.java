/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;

public class CompletenessRow
{
  private int count = 0;
  private int denominator = 0;
  private boolean invert = true;
  private boolean demerit = false;
  private int okayHigh = 100;
  private int okayLow = 0;
  private PotentialIssue potentialIssue = null;
  private ReportDenominator reportDenominator;
  private double score = 0;
  private float scoreWeight = 0;
  private String hl7Reference = "";
  private ToolTip toolTip;
  
  public CodeTable getTable()
  {
    return potentialIssue.getTable();
  }

  /**
   * 
   */
  public CompletenessRow() {
    // default
  }

  public CompletenessRow(CompletenessRow template, double score) {
    this.potentialIssue = template.potentialIssue;
    this.reportDenominator = template.reportDenominator;
    this.scoreWeight = template.scoreWeight;
    this.score = score;
    this.toolTip = new ToolTip(toolTip.getLabel(), "");
  }

  /**
   * @param label 
   * @param potentialIssue
   * @param reportDenominator
   * @param scoreWeight
   */
  public CompletenessRow(String label, PotentialIssue potentialIssue, ReportDenominator reportDenominator, float scoreWeight, boolean invert, String hl7Reference) {
    this.potentialIssue = potentialIssue;
    this.reportDenominator = reportDenominator;
    this.scoreWeight = scoreWeight;
    this.toolTip = new ToolTip(label, "");
    this.invert = invert;
    this.hl7Reference = hl7Reference;
  }
  

  public int getCount()
  {
    return count;
  }

  public int getDenominator()
  {
    return denominator;
  }

  public int getOkayHigh()
  {
    return okayHigh;
  }

  public int getOkayLow()
  {
    return okayLow;
  }

  public PotentialIssue getPotentialIssue()
  {
    return potentialIssue;
  }

  public ReportDenominator getReportDenominator()
  {
    return reportDenominator;
  }

  public double getScore()
  {
    return score;
  }

  public float getScoreWeight()
  {
    return scoreWeight;
  }

  public ToolTip getToolTip()
  {
    return toolTip;
  }

  public boolean isInvert()
  {
    return invert;
  }

  public void setCount(int count)
  {
    this.count = count;
  }

  public void setDenominator(int denominator)
  {
    this.denominator = denominator;
  }

  public CompletenessRow setIndent(boolean indent)
  {
    this.toolTip.setIndent(indent);
    return this;
  }
  
  public CompletenessRow setDemerit()
  {
    this.demerit = true;
    this.invert = false;
    this.toolTip.setIndent(true);
    return this;
  }

  public CompletenessRow setInvert(boolean invert)
  {
    this.invert = invert;
    return this;
  }

  public void setOkayHigh(int okayHigh)
  {
    this.okayHigh = okayHigh;
  }

  public void setOkayLow(int okayLow)
  {
    this.okayLow = okayLow;
  }

  public void setPotentialIssue(PotentialIssue potentialIssue)
  {
    this.potentialIssue = potentialIssue;
  }

  public void setReportDenominator(ReportDenominator reportDenominator)
  {
    this.reportDenominator = reportDenominator;
  }

  public void setScore(double score)
  {
    this.score = score;
  }

  public void setScoreWeight(int scoreWeight)
  {
    this.scoreWeight = scoreWeight;
  }

  public void setToolTip(ToolTip toolTip)
  {
    this.toolTip = toolTip;
  }

  public boolean isDemerit()
  {
    return demerit;
  }

  public String getHl7Reference()
  {
    return hl7Reference;
  }

  public void setHl7Reference(String hl7Reference)
  {
    this.hl7Reference = hl7Reference;
  }

}
