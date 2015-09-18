/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality;

import java.util.HashMap;
import java.util.Map;

import org.openimmunizationsoftware.dqa.quality.model.ModelForm;

public class QualityScoring
{

  public static final String VACCINATION_REQUIRED = "vaccinationRequired";
  public static final String VACCINATION_RECOMMENDED = "vaccinationRecommended";
  public static final String VACCINATION_OPTIONAL = "vaccinationOptional";
  public static final String VACCINATION_EXPECTED = "vaccinationExpected";
  public static final String PATIENT_REQUIRED = "patientRequired";
  public static final String PATIENT_RECOMMENDED = "patientRecommended";
  public static final String PATIENT_OPTIONAL = "patientOptional";
  public static final String PATIENT_EXPECTED = "patientExpected";

  private Map<String, ScoringSet> scoringSets = new HashMap<String, ScoringSet>();
  
  public ScoringSet getScoringSet(String label)
  {
    ScoringSet scoringSet = scoringSets.get(label);
    if (scoringSet == null)
    {
      scoringSet = new ScoringSet();
      scoringSet.setLabel(label);
      scoringSets.put(label, scoringSet);
    }
    return scoringSet;
  }

  public QualityScoring(ModelForm modelForm) {

    ScoringSet patExp = getScoringSet(PATIENT_EXPECTED);
    ScoringSet patOpt = getScoringSet(PATIENT_OPTIONAL);
    ScoringSet patRec = getScoringSet(PATIENT_RECOMMENDED);
    ScoringSet patReq = getScoringSet(PATIENT_REQUIRED);
    ScoringSet vacExp = getScoringSet(VACCINATION_EXPECTED);
    ScoringSet vacOpt = getScoringSet(VACCINATION_OPTIONAL);
    ScoringSet vacRec = getScoringSet(VACCINATION_RECOMMENDED);
    ScoringSet vacReq = getScoringSet(VACCINATION_REQUIRED);

    patReq.setSection(modelForm.getModelSection("completeness.patient.required"));
    patExp.setSection(modelForm.getModelSection("completeness.patient.expected"));
    patRec.setSection(modelForm.getModelSection("completeness.patient.recommended"));
    patOpt.setSection(modelForm.getModelSection("completeness.patient.optional"));
    vacReq.setSection(modelForm.getModelSection("completeness.vaccination.required"));
    vacExp.setSection(modelForm.getModelSection("completeness.vaccination.expected"));
    vacRec.setSection(modelForm.getModelSection("completeness.vaccination.recommended"));
    vacOpt.setSection(modelForm.getModelSection("completeness.vaccination.optional"));

  }

}
