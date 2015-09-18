/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality;

import java.util.ArrayList;
import java.util.List;

import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.quality.model.ModelScore;
import org.openimmunizationsoftware.dqa.quality.model.ModelSection;

public class ScoringSet
{
  private double weight = 0;
  private List<CompletenessRow> completenessRow = null;
  private double score = 0;
  private double weightedScore = 0;
  private String label = "";
  private double overallWeight = 0;

  public double getOverallWeight()
  {
    return overallWeight;
  }

  public void setOverallWeight(double overallWeight)
  {
    this.overallWeight = overallWeight;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public double getWeightedScore()
  {
    return weightedScore;
  }

  public void setWeightedScore(double weightedScore)
  {
    this.weightedScore = weightedScore;
  }

  public ScoringSet() {
    completenessRow = new ArrayList<CompletenessRow>();
  }

  public void add(CompletenessRow row)
  {
    completenessRow.add(row);
  }

  public void setSection(ModelSection modelSection)
  {
    this.weight = modelSection.getWeight();
    for (ModelScore score : modelSection.getScores())
    {
      CompletenessRow cr = add(score);
      if (score.getWeight() < 0)
      {
        cr.setDemerit();
      }
      for (ModelScore subScore : score.getScores())
      {
        cr = add(subScore).setIndent(true);
      }
    }
  }

  private CompletenessRow add(ModelScore score)
  {
    return add(score.getLabel(), score.getPotentialIssue(), score.getDenominator(),
        score.getWeight(), score.isInvert(), score.getHl7Reference());
  }

  private CompletenessRow add(String label, PotentialIssue potentialIssue, ReportDenominator reportDenominator,
      float scoreWeight, boolean invert, String hl7Reference)
  {
    CompletenessRow row = new CompletenessRow(label, potentialIssue, reportDenominator, scoreWeight, invert, hl7Reference);
    completenessRow.add(row);
    return row;
  }

  public double getWeight()
  {
    return weight;
  }

  public void setWeight(double weight)
  {
    this.weight = weight;
  }

  public List<CompletenessRow> getCompletenessRow()
  {
    return completenessRow;
  }

  public void setCompletenessRow(List<CompletenessRow> completenessRow)
  {
    this.completenessRow = completenessRow;
  }

  public double getScore()
  {
    return score;
  }

  public void setScore(double score)
  {
    this.score = score;
  }
}
