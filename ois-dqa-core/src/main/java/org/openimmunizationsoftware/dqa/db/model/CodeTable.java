/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class CodeTable implements Serializable
{
  public static enum Type {
    ACKNOWLEDGEMENT_TYPE(40), ADDRESS_COUNTRY(2), ADDRESS_COUNTY(3), ADDRESS_STATE(4), ADDRESS_TYPE(5), ADMINISTRATION_UNIT(6), BIRTH_ORDER(7), BODY_ROUTE(
        8), BODY_SITE(9), CDC_PHIN_VADS(59), CHARACTER_SET(36), FINANCIAL_STATUS_CODE(10), HL7_CODING_SYSTEM(46), HL7_VALUE_TYPE(45), ID_ASSIGNING_AUTHORITY(
        11), ID_TYPE_CODE(12), MESSAGE_PROCESSING_ID(39), MESSAGE_PROFILE_ID(37), OBSERVATION_IDENTIFIER(43), ORGANIZATION(13), PATIENT_CLASS(38), PATIENT_ETHNICITY(
        14), PATIENT_ID(15), PATIENT_PROTECTION(16), PATIENT_PUBLICITY(17), PATIENT_RACE(18), PATIENT_SEX(19), PERSON_LANGUAGE(20), PERSON_NAME_TYPE(
        21), PERSON_RELATIONSHIP(22), PHYSICIAN_NUMBER(23), REGISTRY_STATUS(24), TELECOMMUNICATION_EQUIPMENT(42), TELECOMMUNICATION_USE(41), VACCINATION_ACTION_CODE(
        25), VACCINATION_COMPLETION(26), VACCINATION_CONFIDENTIALITY(27), VACCINATION_CPT_CODE(28), VACCINATION_CVX_CODE(29), VACCINATION_INFORMATION_SOURCE(
        30), VACCINATION_MANUFACTURER_CODE(31), VACCINATION_ORDER_CONTROL_CODE(34), VACCINATION_REFUSAL(32), VACCINE_PRODUCT(33), VACCINATION_FUNDING_SOURCE(
        47), VACCINE_TYPE(48), CONTRAINDICATION_OR_PRECATION(49), VACCINATION_REACTION(50), EVIDENCE_OF_IMMUNITY(51), VACCINATION_SPECIAL_INDICATIONS(
        52), VACCINATION_VIS_DOC_TYPE(53), FORECAST_REASON(54), FORECAST_IMMUNIZATION_SCHEDULE(55), FORECAST_SERIES_STATUS(56), FINANCIAL_STATUS_OBS_METHOD(
        57), VACCINATION_VIS_VACCINES(58), VACCINATION_VIS_CVX_CODE(60), POTENTIAL_ISSUE(61), VACCINE_GROUP(62), FACILITY_TYPE(63), VACCINATION_TRADE_NAME(
        64), VACCINATION_VALIDITY(65);

    int tableId = 0;

    Type(int tableId) {
      this.tableId = tableId;
    }

    public int getTableId()
    {
      return tableId;
    }
  }

  private static final long serialVersionUID = 2l;

  private int tableId = 0;
  private String tableLabel = "";
  private String defaultCodeValue = "";

  public int getTableId()
  {
    return tableId;
  }

  public void setTableId(int tableId)
  {
    this.tableId = tableId;
  }

  public String getTableLabel()
  {
    return tableLabel;
  }

  public void setTableLabel(String tableLabel)
  {
    this.tableLabel = tableLabel;
  }

  public String getDefaultCodeValue()
  {
    return defaultCodeValue;
  }

  public void setDefaultCodeValue(String defaultCodeValue)
  {
    this.defaultCodeValue = defaultCodeValue;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof CodeTable)
    {
      return ((CodeTable) obj).getTableId() == this.getTableId();
    }
    return super.equals(obj);
  }

  @Override
  public int hashCode()
  {
    return this.getTableId();
  }
}
