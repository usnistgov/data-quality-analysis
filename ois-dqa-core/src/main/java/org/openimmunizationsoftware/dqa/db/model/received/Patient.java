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
import org.openimmunizationsoftware.dqa.db.model.received.types.CodedEntity;
import org.openimmunizationsoftware.dqa.db.model.received.types.Id;
import org.openimmunizationsoftware.dqa.db.model.received.types.Name;
import org.openimmunizationsoftware.dqa.db.model.received.types.OrganizationName;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientAddress;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientIdNumber;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientImmunity;
import org.openimmunizationsoftware.dqa.db.model.received.types.PatientPhone;
import org.openimmunizationsoftware.dqa.nist.Extracted;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class Patient extends Extracted implements Skippable, Serializable
{

  private static final long serialVersionUID = 1l;

  private List<PatientAddress> patientAddressList = new ArrayList<PatientAddress>();
  private Name alias = new Name();
  private Date birthDate = null;
  private String birthMultiple = "";
  private CodedEntity birthOrder = new CodedEntity(CodeTable.Type.BIRTH_ORDER);
  private String birthPlace = "";
  private Date deathDate = null;
  private String deathIndicator = "";
  private CodedEntity ethnicity = new CodedEntity(CodeTable.Type.PATIENT_ETHNICITY);
  private OrganizationName facility = new OrganizationName();
  private CodedEntity financialEligibility = new CodedEntity(CodeTable.Type.FINANCIAL_STATUS_CODE);
  private Date financialEligibilityDate = null;
  private PatientIdNumber idMedicaid = new PatientIdNumber();
  private PatientIdNumber idRegistry = new PatientIdNumber();
  private PatientIdNumber idSsn = new PatientIdNumber();
  private PatientIdNumber idSubmitter = new PatientIdNumber();
  private List<PatientIdNumber> patientIdNumberList = new ArrayList<PatientIdNumber>(); 
  private MessageReceived messageReceived = null;
  private String motherMaidenName = "";
  private Name name = new Name();
  private long patientId = 0;
  private PatientPhone phone = new PatientPhone();
  private Id physician = new Id(CodeTable.Type.PHYSICIAN_NUMBER);
  private CodedEntity primaryLanguage = new CodedEntity(CodeTable.Type.PERSON_LANGUAGE);
  private CodedEntity protection = new CodedEntity(CodeTable.Type.PATIENT_PROTECTION);
  private CodedEntity publicity = new CodedEntity(CodeTable.Type.PATIENT_PUBLICITY);
  private CodedEntity race = new CodedEntity(CodeTable.Type.PATIENT_RACE);
  private CodedEntity registryStatus = new CodedEntity(CodeTable.Type.REGISTRY_STATUS);
  private CodedEntity patientClass = new CodedEntity(CodeTable.Type.PATIENT_CLASS);
  private NextOfKin responsibleParty = null;
  private CodedEntity sex = new CodedEntity(CodeTable.Type.PATIENT_SEX);
  private boolean isUnderAged = false;
  private boolean skipped = false;
  private List<PatientPhone> patientPhoneList = new ArrayList<PatientPhone>();
  private List<PatientImmunity> patientImmunityList = new ArrayList<PatientImmunity>();
  private Date systemCreationDate = null;

  public Date getSystemCreationDate()
  {
    return systemCreationDate;
  }

  public void setSystemCreationDate(Date systemCreationDate)
  {
		this.put("systemCreationDate", Locator.getPath());
    this.systemCreationDate = systemCreationDate;
  }

  public List<PatientImmunity> getPatientImmunityList()
  {
    return patientImmunityList;
  }

  public List<PatientIdNumber> getPatientIdNumberList()
  {
    return patientIdNumberList;
  }

  public List<PatientPhone> getPatientPhoneList()
  {
    return patientPhoneList;
  }

  public List<PatientAddress> getPatientAddressList()
  {
    return patientAddressList;
  }

  public PatientAddress getAddress()
  {
    PatientAddress address = null;
    if (patientAddressList.size() == 0)
    {
      address = new PatientAddress();
      address.setPatient(this);
      address.setPositionId(1);
      patientAddressList.add(address);
    } else
    {
      address = patientAddressList.get(0);
    }
    return address;
  }

  public String getAddressCity()
  {
    return getAddress().getCity();
  }

  public String getAddressCountryCode()
  {
    return getAddress().getCountry().getCode();
  }

  public String getAddressCountyParishCode()
  {
    return getAddress().getCountyParishCode();
  }

  public String getAddressStateCode()
  {
    return getAddress().getStateCode();
  }

  public String getAddressStreet()
  {
    return getAddress().getStreet();
  }

  public String getAddressStreet2()
  {
    return getAddress().getStreet2();
  }

  public String getAddressTypeCode()
  {
    return getAddress().getTypeCode();
  }

  public String getAddressZip()
  {
    return getAddress().getZip();
  }

  public Name getAlias()
  {
    return alias;
  }

  public String getAliasFirst()
  {
    return alias.getFirst();
  }

  public String getAliasLast()
  {
    return alias.getLast();
  }

  public String getAliasMiddle()
  {
    return alias.getMiddle();
  }

  public String getAliasPrefix()
  {
    return alias.getPrefix();
  }

  public String getAliasSuffix()
  {
    return alias.getSuffix();
  }

  public String getAliasTypeCode()
  {
    return alias.getTypeCode();
  }

  public Date getBirthDate()
  {
    return birthDate;
  }

  public String getBirthMultiple()
  {
    return birthMultiple;
  }

  public CodedEntity getBirthOrder()
  {
    return birthOrder;
  }

  public String getBirthOrderCode()
  {
    return birthOrder.getCode();
  }

  public String getBirthPlace()
  {
    return birthPlace;
  }

  public Date getDeathDate()
  {
    return deathDate;
  }

  public String getDeathIndicator()
  {
    return deathIndicator;
  }

  public CodedEntity getEthnicity()
  {
    return ethnicity;
  }

  public String getEthnicityCode()
  {
    return ethnicity.getCode();
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

  public PatientIdNumber getIdMedicaid()
  {
    return idMedicaid;
  }

  public String getIdMedicaidNumber()
  {
    return idMedicaid.getNumber();
  }

  public PatientIdNumber getIdRegistry()
  {
    return idRegistry;
  }

  public String getIdRegistryNumber()
  {
    return idRegistry.getNumber();
  }

  public PatientIdNumber getIdSsn()
  {
    return idSsn;
  }

  public String getIdSsnNumber()
  {
    return idSsn.getNumber();
  }

  public PatientIdNumber getIdSubmitter()
  {
    return idSubmitter;
  }

  public String getIdSubmitterAssigningAuthorityCode()
  {
    return idSubmitter.getAssigningAuthority().getCode();
  }

  public String getIdSubmitterNumber()
  {
    return idSubmitter.getNumber();
  }

  public String getIdSubmitterTypeCode()
  {
    return idSubmitter.getTypeCode();
  }

  public MessageReceived getMessageReceived()
  {
    return messageReceived;
  }

  public String getMotherMaidenName()
  {
    return motherMaidenName;
  }

  public Name getName()
  {
    return name;
  }

  public String getNameFirst()
  {
    return name.getFirst();
  }

  public String getNameLast()
  {
    return name.getLast();
  }

  public String getNameMiddle()
  {
    return name.getMiddle();
  }

  public String getNamePrefix()
  {
    return name.getPrefix();
  }

  public String getNameSuffix()
  {
    return name.getSuffix();
  }

  public String getNameTypeCode()
  {
    return name.getTypeCode();
  }

  public long getPatientId()
  {
    return patientId;
  }

  public PatientPhone getPhone()
  {
    return phone;
  }

  public String getPhoneNumber()
  {
    return phone.getNumber();
  }

  public Id getPhysician()
  {
    return physician;
  }

  public String getPhysicianNameFirst()
  {
    return physician.getName().getFirst();
  }

  public String getPhysicianNameLast()
  {
    return physician.getName().getLast();
  }

  public String getPhysicianNumber()
  {
    return physician.getNumber();
  }

  public CodedEntity getPrimaryLanguage()
  {
    return primaryLanguage;
  }

  public String getPrimaryLanguageCode()
  {
    return primaryLanguage.getCode();
  }

  public CodedEntity getProtection()
  {
    return protection;
  }

  public String getProtectionCode()
  {
    return protection.getCode();
  }

  public CodedEntity getPublicity()
  {
    return publicity;
  }

  public String getPublicityCode()
  {
    return publicity.getCode();
  }

  public CodedEntity getRace()
  {
    return race;
  }

  public String getRaceCode()
  {
    return race.getCode();
  }

  public CodedEntity getRegistryStatus()
  {
    return registryStatus;
  }

  public String getRegistryStatusCode()
  {
    return registryStatus.getCode();
  }

  public NextOfKin getResponsibleParty()
  {
    return responsibleParty;
  }

  public CodedEntity getSex()
  {
    return sex;
  }

  public String getSexCode()
  {
    return sex.getCode();
  }

  public boolean isSkipped()
  {
    return skipped;
  }

  public void setAddressCity(String addressCity)
  {
    getAddress().setCity(addressCity);
  }

  public void setAddressCountryCode(String addressCountryCode)
  {
    getAddress().getCountry().setCode(addressCountryCode);
  }

  public void setAddressCountyParishCode(String addressCountyParishCode)
  {
    getAddress().getCountyParish().setCode(addressCountyParishCode);
  }

  public void setAddressStateCode(String addressStateCode)
  {
    getAddress().getState().setCode(addressStateCode);
  }

  public void setAddressStreet(String addressStreet)
  {
    getAddress().setStreet(addressStreet);
  }

  public void setAddressStreet2(String addressStreet2)
  {
    getAddress().setStreet2(addressStreet2);
  }

  public void setAddressTypeCode(String addressTypeCode)
  {
    getAddress().getType().setCode(addressTypeCode);
  }

  public void setAddressZip(String addressZip)
  {
    getAddress().setZip(addressZip);
  }

  public void setAliasFirst(String nameFirst)
  {
    alias.setFirst(nameFirst);
  }

  public void setAliasLast(String nameLast)
  {
    alias.setLast(nameLast);
  }

  public void setAliasMiddle(String nameMiddle)
  {
    alias.setMiddle(nameMiddle);
  }

  public void setAliasPrefix(String namePrefix)
  {
    alias.setPrefix(namePrefix);
  }

  public void setAliasSuffix(String nameSuffix)
  {
    alias.setSuffix(nameSuffix);
  }

  public void setAliasTypeCode(String nameTypeCode)
  {
    alias.setTypeCode(nameTypeCode);
  }

  public void setBirthDate(Date birthDate)
  {
	  this.put("birthDate", Locator.getPath());  
    this.birthDate = birthDate;
  }

  public void setBirthMultiple(String birthMultiple)
  {
	  this.put("birthMultiple", Locator.getPath());  
    this.birthMultiple = birthMultiple;
  }

  public void setBirthOrderCode(String birthOrderCode)
  {
    this.birthOrder.setCode(birthOrderCode);
  }

  public void setBirthPlace(String birthPlace)
  {
	  this.put("birthPlace", Locator.getPath());  
    this.birthPlace = birthPlace;
  }

  public void setDeathDate(Date deathDate)
  {
	  this.put("deathDate", Locator.getPath());  
    this.deathDate = deathDate;
  }

  public void setDeathIndicator(String deathIndicator)
  {
	  this.put("deathIndicator", Locator.getPath());  
    this.deathIndicator = deathIndicator;
  }

  public void setEthnicityCode(String ethnicityCode)
  {
    ethnicity.setCode(ethnicityCode);
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

  public void setIdMedicaidNumber(String idMedicaidNumber)
  {
    this.idMedicaid.setNumber(idMedicaidNumber);
  }

  public void setIdRegistryNumber(String idRegistryNumber)
  {
    this.idRegistry.setNumber(idRegistryNumber);
  }

  public void setIdSsnNumber(String idSsnNumber)
  {
    this.idSsn.setNumber(idSsnNumber);
  }

  public void setIdSubmitterAssigningAuthorityCode(String assigningAuthorityCode)
  {
    idSubmitter.setAssigningAuthorityCode(assigningAuthorityCode);
  }

  public void setIdSubmitterNumber(String number)
  {
    idSubmitter.setNumber(number);
  }

  public void setIdSubmitterTypeCode(String typeCode)
  {
    idSubmitter.setTypeCode(typeCode);
  }

  public void setMessageReceived(MessageReceived messageReceived)
  {
    this.messageReceived = messageReceived;
  }

  public void setMotherMaidenName(String motherMaidenName)
  {
	  this.put("motherMaidenName", Locator.getPath());  
    this.motherMaidenName = motherMaidenName;
  }

  public void setNameFirst(String nameFirst)
  {
    name.setFirst(nameFirst);
  }

  public void setNameLast(String nameLast)
  {
    name.setLast(nameLast);
  }

  public void setNameMiddle(String nameMiddle)
  {
    name.setMiddle(nameMiddle);
  }

  public void setNamePrefix(String namePrefix)
  {
    name.setPrefix(namePrefix);
  }

  public void setNameSuffix(String nameSuffix)
  {
    name.setSuffix(nameSuffix);
  }

  public void setNameTypeCode(String nameTypeCode)
  {
    name.setTypeCode(nameTypeCode);
  }

  public void setPatientId(long patientId)
  {
    this.patientId = patientId;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    phone.setNumber(phoneNumber);
  }

  public void setPhysicianNameFirst(String physicianNameFirst)
  {
    physician.getName().setFirst(physicianNameFirst);
  }

  public void setPhysicianNameLast(String physicianNameLast)
  {
    physician.getName().setLast(physicianNameLast);
  }

  public void setPhysicianNumber(String physicianNumber)
  {
    physician.setNumber(physicianNumber);
  }

  public void setPrimaryLanguageCode(String primaryLanguageCode)
  {
    primaryLanguage.setCode(primaryLanguageCode);
  }

  public void setProtectionCode(String protectionCode)
  {
    protection.setCode(protectionCode);
  }

  public void setPublicityCode(String publicityCode)
  {
    publicity.setCode(publicityCode);
  }

  public void setRaceCode(String raceCode)
  {
    race.setCode(raceCode);
  }

  public void setRegistryStatusCode(String registryStatusCode)
  {
    this.registryStatus.setCode(registryStatusCode);
  }

  public void setResponsibleParty(NextOfKin responsibleParty)
  {
    this.responsibleParty = responsibleParty;
  }

  public void setSexCode(String sexCode)
  {
    this.sex.setCode(sexCode);
  }

  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

  public String getPatientClassCode()
  {
    return patientClass.getCode();
  }

  public void setPatientClassCode(String code)
  {
    patientClass.setCode(code);
  }

  public CodedEntity getPatientClass()
  {
    return patientClass;
  }

  public Date getFinancialEligibilityDate()
  {
    return financialEligibilityDate;
  }

  public void setFinancialEligibilityDate(Date financialEligibilityDate)
  {
	  this.put("financialEligibilityDate", Locator.getPath());  
	  
    this.financialEligibilityDate = financialEligibilityDate;
  }

  public boolean isUnderAged()
  {
    return isUnderAged;
  }

  public void setUnderAged(boolean isUnderAged)
  {
    this.isUnderAged = isUnderAged;
  }

}
