/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model.received;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.received.types.CodedEntity;
import org.openimmunizationsoftware.dqa.db.model.received.types.Id;
import org.openimmunizationsoftware.dqa.db.model.received.types.OrganizationName;
import org.openimmunizationsoftware.dqa.nist.Extracted;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class Vaccination extends Extracted implements Skippable, Serializable
{

  private static final long serialVersionUID = 2l;

  public static final String ACTION_CODE_ADD = "A";
  public static final String ACTION_CODE_DELETE = "D";
  public static final String ACTION_CODE_UPDATE = "U";

  public static final String COMPLETION_COMPLETED = "CP";
  public static final String COMPLETION_NOT_ADMINISTERED = "NA";

  public static final String COMPLETION_PARTIALLY_ADMINISTERED = "PA";
  public static final String COMPLETION_REFUSED = "RE";
  public static final String INFO_SOURCE_ADMIN = "00";
  public static final String INFO_SOURCE_HIST = "01";

  private CodedEntity action = new CodedEntity(CodeTable.Type.VACCINATION_ACTION_CODE);
  private CodedEntity admin = new CodedEntity(CodeTable.Type.VACCINATION_CVX_CODE);
  private final CodedEntity adminCpt = new CodedEntity(CodeTable.Type.VACCINATION_CPT_CODE);
  private final CodedEntity adminCvx = new CodedEntity(CodeTable.Type.VACCINATION_CVX_CODE);
  private Date adminDate = null;
  private Date adminDateEnd = null;
  private boolean administered = false;
  private String amount = "";
  private CodedEntity amountUnit = new CodedEntity(CodeTable.Type.ADMINISTRATION_UNIT);
  private CodedEntity bodyRoute = new CodedEntity(CodeTable.Type.BODY_ROUTE);
  private CodedEntity bodySite = new CodedEntity(CodeTable.Type.BODY_SITE);
  private CodedEntity completion = new CodedEntity(CodeTable.Type.VACCINATION_COMPLETION);
  private CodedEntity confidentiality = new CodedEntity(CodeTable.Type.VACCINATION_CONFIDENTIALITY);
  private Id enteredBy = new Id(CodeTable.Type.PHYSICIAN_NUMBER);
  private Date expirationDate = null;
  private OrganizationName facility = new OrganizationName();
  private CodedEntity facilityType = new CodedEntity(CodeTable.Type.FACILITY_TYPE);
  private CodedEntity financialEligibility = new CodedEntity(CodeTable.Type.FINANCIAL_STATUS_CODE);
  private Id givenBy = new Id(CodeTable.Type.PHYSICIAN_NUMBER);
  private String idPlacer = "";
  private String idSubmitter = "";
  private CodedEntity informationSource = new CodedEntity(CodeTable.Type.VACCINATION_INFORMATION_SOURCE);
  private String lotNumber = "";
  private CodedEntity manufacturer = new CodedEntity(CodeTable.Type.VACCINATION_MANUFACTURER_CODE);
  private MessageReceived messageReceived = null;
  private List<Observation> observations = new ArrayList<Observation>();
  private CodedEntity orderControl = new CodedEntity(CodeTable.Type.VACCINATION_ORDER_CONTROL_CODE);
  private Id orderedBy = new Id(CodeTable.Type.PHYSICIAN_NUMBER);
  private int positionId = 0;
  private CodedEntity product = new CodedEntity(CodeTable.Type.VACCINE_PRODUCT);
  private CodedEntity refusal = new CodedEntity(CodeTable.Type.VACCINATION_REFUSAL);
  private boolean skipped = false;
  private Date systemEntryDate = null;
  private long vaccinationId = 0l;
  private VaccineCvx vaccineCvx = null;
  private CodedEntity fundingSource = new CodedEntity(CodeTable.Type.VACCINATION_FUNDING_SOURCE);
  private String refusalReason = "";
  private List<VaccinationVIS> vaccinationVisList = new ArrayList<VaccinationVIS>();
  private CodedEntity tradeName = new CodedEntity(CodeTable.Type.VACCINATION_TRADE_NAME);
  private CodedEntity vaccineValidity = new CodedEntity(CodeTable.Type.VACCINATION_VALIDITY);
  
  public CodedEntity getFacilityType()
  {
    return facilityType;
  }

  public String getFacilityTypeCode()
  {
    return facilityType.getCode();
  }

  public void setFacilityTypeCode(String facilityTypeCode)
  {
    this.facilityType.setCode(facilityTypeCode);
  }

  public CodedEntity getTradeName()
  {
    return tradeName;
  }

  public String getTradeNameCode()
  {
    return tradeName.getCode();
  }

  public void setTradeNameCode(String tradeNameCode)
  {
    this.tradeName.setCode(tradeNameCode);
  }

  public CodedEntity getVaccineValidity()
  {
    return vaccineValidity;
  }

  public String getVaccineValidityCode()
  {
    return vaccineValidity.getCode();
  }

  public void setVaccineValidityCode(String vaccineValidityCode)
  {
    this.vaccineValidity.setCode(vaccineValidityCode);
  }

  public List<VaccinationVIS> getVaccinationVisList()
  {
    return vaccinationVisList;
  }



  public String getRefusalReason()
  {
    return refusalReason;
  }

  public void setRefusalReason(String refusalReason)
  {
		this.put("refusalReason", Locator.getPath());
    this.refusalReason = refusalReason;
  }

  public String getFundingSourceCode()
  {
    return fundingSource.getCode();
  }

  public void setFundingSourceCode(String fundingSourceCode)
  {
    this.fundingSource.setCode(fundingSourceCode);
  }

  public Date getVisPresentedDate()
  {
    return vaccinationVisList.size() > 0 ? vaccinationVisList.get(0).getPresentedDate() : null;
  }

  public void setVisPresentedDate(Date visPresentedDate)
  {
    if (vaccinationVisList.size() > 0)
    {
      vaccinationVisList.get(0).setPresentedDate(visPresentedDate);
    }
  }

  public String getVisDocumentCode()
  {
    return vaccinationVisList.size() > 0 ? vaccinationVisList.get(0).getDocumentCode() : "";
  }

  public void setVisDocumentCode(String visDocumentCode)
  {
    if (vaccinationVisList.size() > 0)
    {
      vaccinationVisList.get(0).setDocumentCode(visDocumentCode);
    }
  }

  public CodedEntity getFundingSource()
  {
    return fundingSource;
  }

  public CodedEntity getAction()
  {
    return action;
  }

  public String getActionCode()
  {
    return action.getCode();
  }

  public CodedEntity getAdmin()
  {
    return admin;
  }

  public String getAdminCode()
  {
    return admin.getCode();
  }

  public CodedEntity getAdminCpt()
  {
    return adminCpt;
  }

  public String getAdminCptCode()
  {
    return adminCpt.getCode();
  }

  public CodedEntity getAdminCvx()
  {
    return adminCvx;
  }

  public String getAdminCvxCode()
  {
    return adminCvx.getCode();
  }

  // public static final String ACTION_CODE_ADD = "A";
  // public static final String ACTION_CODE_DELETE = "D";
  // public static final String ACTION_CODE_UPDATE = "U";
  //
  // public static final String INFO_SOURCE_ADMIN = "00";
  // public static final String INFO_SOURCE_HIST = "01";

  public Date getAdminDate()
  {
    return adminDate;
  }

  public Date getAdminDateEnd()
  {
    return adminDateEnd;
  }

  public String getAmount()
  {
    return amount;
  }

  public CodedEntity getAmountUnit()
  {
    return amountUnit;
  }

  public String getAmountUnitCode()
  {
    return amountUnit.getCode();
  }

  public CodedEntity getBodyRoute()
  {
    return this.bodyRoute;
  }

  public String getBodyRouteCode()
  {
    return this.bodyRoute.getCode();
  }

  public CodedEntity getBodySite()
  {
    return this.bodySite;
  }

  public String getBodySiteCode()
  {
    return this.bodySite.getCode();
  }

  public CodedEntity getCompletion()
  {
    return completion;
  }

  public String getCompletionCode()
  {
    return completion.getCode();
  }

  public CodedEntity getConfidentiality()
  {
    return confidentiality;
  }

  public String getConfidentialityCode()
  {
    return confidentiality.getCode();
  }

  public Id getEnteredBy()
  {
    return enteredBy;
  }

  public String getEnteredByNameFirst()
  {
    return enteredBy.getName().getFirst();
  }

  public String getEnteredByNameLast()
  {
    return enteredBy.getName().getLast();
  }

  public String getEnteredByNumber()
  {
    return enteredBy.getNumber();
  }

  public Date getExpirationDate()
  {
    return expirationDate;
  }

  public OrganizationName getFacility()
  {
    return facility;
  }

  public String getFacilityIdNumber()
  {
    return facility.getIdNumber();
  }

  public String getFacilityName()
  {
    return facility.getName();
  }

  public CodedEntity getFinancialEligibility()
  {
    return financialEligibility;
  }

  public String getFinancialEligibilityCode()
  {
    return financialEligibility.getCode();
  }

  public Id getGivenBy()
  {
    return givenBy;
  }

  public String getGivenByNameFirst()
  {
    return givenBy.getName().getFirst();
  }

  public String getGivenByNameLast()
  {
    return givenBy.getName().getLast();
  }

  public String getGivenByNumber()
  {
    return givenBy.getNumber();
  }

  public String getIdPlacer()
  {
    return idPlacer;
  }

  public String getIdSubmitter()
  {
    return idSubmitter;
  }

  public CodedEntity getInformationSource()
  {
    return informationSource;
  }

  public String getInformationSourceCode()
  {
    return informationSource.getCode();
  }

  public String getLotNumber()
  {
    return lotNumber;
  }

  public CodedEntity getManufacturer()
  {
    return manufacturer;
  }

  public String getManufacturerCode()
  {
    return manufacturer.getCode();
  }

  public MessageReceived getMessageReceived()
  {
    return messageReceived;
  }

  public List<Observation> getObservations()
  {
    return observations;
  }

  public CodedEntity getOrderControl()
  {
    return orderControl;
  }

  public String getOrderControlCode()
  {
    return orderControl.getCode();
  }

  public Id getOrderedBy()
  {
    return orderedBy;
  }

  public String getOrderedByNameFirst()
  {
    return orderedBy.getName().getFirst();
  }

  public String getOrderedByNameLast()
  {
    return orderedBy.getName().getLast();
  }

  public String getOrderedByNumber()
  {
    return orderedBy.getNumber();
  }

  public int getPositionId()
  {
    return positionId;
  }

  public CodedEntity getProduct()
  {
    return this.product;
  }

  public CodedEntity getRefusal()
  {
    return refusal;
  }

  public String getRefusalCode()
  {
    return refusal.getCode();
  }

  public Date getSystemEntryDate()
  {
    return systemEntryDate;
  }

  public long getVaccinationId()
  {
    return vaccinationId;
  }

  public VaccineCvx getVaccineCvx()
  {
    return vaccineCvx;
  }

  public Date getVisPublicationDate()
  {
    return vaccinationVisList.size() > 0 ? vaccinationVisList.get(0).getPublishedDate() : null;
  }

  public boolean isActionAdd()
  {
    return action.getCode() != null && action.getCode().equals(ACTION_CODE_ADD);
  }

  public boolean isActionDelete()
  {
    return action.getCode() != null && action.getCode().equals(ACTION_CODE_DELETE);
  }

  public boolean isActionUpdate()
  {
    return action.getCode() != null && action.getCode().equals(ACTION_CODE_UPDATE);
  }

  public boolean isAdministered()
  {
    return administered;
  }

  public boolean isCompletionCompleted()
  {
    return completion.getCode() != null && completion.getCode().equals(COMPLETION_COMPLETED);
  }

  public boolean isCompletionNotAdministered()
  {
    return completion.getCode() != null && completion.getCode().equals(COMPLETION_NOT_ADMINISTERED);
  }

  public boolean isCompletionPartiallyAdministered()
  {
    return completion.getCode() != null && completion.getCode().equals(COMPLETION_PARTIALLY_ADMINISTERED);
  }

  public boolean isCompletionCompletedOrPartiallyAdministered()
  {
    return completion.getCode() != null
        && (completion.getCode().equals(COMPLETION_COMPLETED) || completion.getCode().equals(COMPLETION_PARTIALLY_ADMINISTERED));
  }

  public boolean isCompletionRefused()
  {
    return completion.getCode() != null && completion.getCode().equals(COMPLETION_REFUSED);
  }

  public boolean isInformationSourceAdmin()
  {
    return informationSource.getCode() != null && informationSource.getCode().equals(INFO_SOURCE_ADMIN);
  }

  public boolean isInformationSourceHist()
  {
    return informationSource.getCode() != null && informationSource.getCode().equals(INFO_SOURCE_HIST);
  }

  public boolean isSkipped()
  {
    return skipped;
  }

  public void setActionCode(String actionCode)
  {
    this.action.setCode(actionCode);
  }

  public void setAdminCode(String adminCode)
  {
    admin.setCode(adminCode);
  }

  public void setAdminCptCode(String adminCptCode)
  {
    this.adminCpt.setCode(adminCptCode);
  }

  public void setAdminCvxCode(String adminCvxCode)
  {
    this.adminCvx.setCode(adminCvxCode);
  }

  public void setAdminDate(Date adminDate)
  {
		this.put("adminDate", Locator.getPath());
    this.adminDate = adminDate;
  }

  public void setAdminDateEnd(Date adminDateEnd)
  {
	  this.put("adminDateEnd", Locator.getPath());
    this.adminDateEnd = adminDateEnd;
  }

  public void setAdministered(boolean administered)
  {
    this.administered = administered;
  }

  public void setAmount(String amount)
  {
	  this.put("amount", Locator.getPath());
    this.amount = amount;
  }

  public void setAmountUnitCode(String amountUnitCode)
  {
    amountUnit.setCode(amountUnitCode);
  }

  public void setBodyRouteCode(String bodyRouteCode)
  {
    this.bodyRoute.setCode(bodyRouteCode);
  }

  public void setBodySiteCode(String bodySiteCode)
  {
    this.bodySite.setCode(bodySiteCode);
  }

  public void setCompletionCode(String completionCode)
  {
    this.completion.setCode(completionCode);
  }

  public void setConfidentialityCode(String confidentialityCode)
  {
    confidentiality.setCode(confidentialityCode);
  }

  public void setEnteredByNameFirst(String enteredByNameFirst)
  {
    enteredBy.getName().setFirst(enteredByNameFirst);
  }

  public void setEnteredByNameLast(String enteredByNameLast)
  {
    enteredBy.getName().setLast(enteredByNameLast);
  }

  public void setEnteredByNumber(String enteredByNumber)
  {
    enteredBy.setNumber(enteredByNumber);
  }

  public void setExpirationDate(Date expirationDate)
  {
		this.put("expirationDate", Locator.getPath());
    this.expirationDate = expirationDate;
  }

  public void setFacilityIdNumber(String facilityIdNumber)
  {
    facility.setIdNumber(facilityIdNumber);
  }

  public void setFacilityName(String facilityName)
  {
    facility.setName(facilityName);
  }

  public void setFinancialEligibilityCode(String financialEligibilityCode)
  {
    this.financialEligibility.setCode(financialEligibilityCode);
  }

  public void setGivenByNameFirst(String givenByNameFirst)
  {
    givenBy.getName().setFirst(givenByNameFirst);
  }

  public void setGivenByNameLast(String givenByNameLast)
  {
    givenBy.getName().setLast(givenByNameLast);
  }

  public void setGivenByNumber(String givenByNumber)
  {
    givenBy.setNumber(givenByNumber);
  }

  public void setIdPlacer(String idPlacer)
  {
		this.put("idPlacer", Locator.getPath());
    this.idPlacer = idPlacer;
  }

  public void setIdSubmitter(String idSubmitter)
  {
	  this.put("idSubmitter", Locator.getPath());
    this.idSubmitter = idSubmitter;
  }

  public void setInformationSourceCode(String informationSourceCode)
  {
    informationSource.setCode(informationSourceCode);
  }

  public void setLotNumber(String lotNumber)
  {
		this.put("lotNumber", Locator.getPath());
    this.lotNumber = lotNumber;
  }

  public void setManufacturerCode(String manufacturerCode)
  {
    manufacturer.setCode(manufacturerCode);
  }

  public void setMessageReceived(MessageReceived messageReceived)
  {
    this.messageReceived = messageReceived;
  }

  public void setObservations(List<Observation> observations)
  {
    this.observations = observations;
  }

  public void setOrderControlCode(String code)
  {
    orderControl.setCode(code);
  }

  public void setOrderedByNameFirst(String orderedByNameFirst)
  {
    orderedBy.getName().setFirst(orderedByNameFirst);
  }

  public void setOrderedByNameLast(String orderedByNameLast)
  {
    orderedBy.getName().setLast(orderedByNameLast);
  }

  public void setOrderedByNumber(String orderedByNumber)
  {
    orderedBy.setNumber(orderedByNumber);
  }

  public void setPositionId(int positionId)
  {
    this.positionId = positionId;
  }

  public void setRefusalCode(String refusalCode)
  {
    refusal.setCode(refusalCode);
  }

  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

  public void setSystemEntryDate(Date systemEntryDate)
  {
		this.put("systemEntryDate", Locator.getPath());
    this.systemEntryDate = systemEntryDate;
  }

  public void setVaccinationId(long vaccinationId)
  {
	  this.put("vaccinationId", Locator.getPath());
    this.vaccinationId = vaccinationId;
  }

  public void setVaccineCvx(VaccineCvx vaccineCvx)
  {
    this.vaccineCvx = vaccineCvx;
  }

  public void setVisPublicationDate(Date visPublicationDate)
  {
	  this.put("visPublicationDate", Locator.getPath());
    if (vaccinationVisList.size() > 0)
    {
      vaccinationVisList.get(0).setPublishedDate(visPublicationDate);
    }
  }

}
