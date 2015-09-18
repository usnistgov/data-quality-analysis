/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;
import java.util.Date;

import org.openimmunizationsoftware.dqa.db.model.received.types.CodedEntity;
import org.openimmunizationsoftware.dqa.nist.Extracted;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class MessageHeader extends Extracted implements Serializable 
{
  
  private static final long serialVersionUID = 1l;
  
  public static String PROCESSING_ID_DEBUGGING = "D";
  public static String PROCESSING_ID_PRODUCTION = "P";
  public static String PROCESSING_ID_TRAINING = "T";

  public static String VERSION_ID_2_3_1 = "2.3.1";
  public static String VERSION_ID_2_4 = "2.4";

  private MessageReceived messageReceived = null;
  private int headerId = 0;
  private CodedEntity ackTypeAccept = new CodedEntity(CodeTable.Type.ACKNOWLEDGEMENT_TYPE);
  private CodedEntity ackTypeApplication = new CodedEntity(CodeTable.Type.ACKNOWLEDGEMENT_TYPE);
  private CodedEntity characterSet = new CodedEntity(CodeTable.Type.CHARACTER_SET);
  private CodedEntity characterSetAlt = new CodedEntity(CodeTable.Type.CHARACTER_SET);
  private CodedEntity country = new CodedEntity(CodeTable.Type.ADDRESS_COUNTRY);
  private String messageControl = "";
  private Date messageDate = null;
  private String messageProfile = "";
  private String messageStructure = "";
  private String messageTrigger = "";
  private String messageType = "";
  private CodedEntity processingStatus = new CodedEntity(CodeTable.Type.MESSAGE_PROCESSING_ID);
  private String receivingApplication = "";
  private String receivingFacility = "";
  private String sendingApplication = "";
  private String sendingFacility = "";
  private String messageVersion = "";
  
  public int getHeaderId()
  {
    return headerId;
  }

  public void setHeaderId(int headerId)
  {
    this.headerId = headerId;
  }

  public CodedEntity getAckTypeAccept()
  {
    return ackTypeAccept;
  }

  public String getAckTypeAcceptCode()
  {
    return ackTypeAccept.getCode();
  }

  public CodedEntity getAckTypeApplication()
  {
    return ackTypeApplication;
  }

  public String getAckTypeApplicationCode()
  {
    return ackTypeApplication.getCode();
  }

  public CodedEntity getCharacterSet()
  {
    return characterSet;
  }

  public CodedEntity getCharacterSetAlt()
  {
    return characterSetAlt;
  }

  public String getCharacterSetCode()
  {
    return characterSet.getCode();
  }

  public String getCharacterSetAltCode()
  {
    return characterSetAlt.getCode();
  }

  public CodedEntity getCountry()
  {
    return country;
  }

  public String getCountryCode()
  {
    return country.getCode();
  }

  public String getMessageControl()
  {
    return messageControl;
  }

  public Date getMessageDate()
  {
    return messageDate;
  }

  public String getMessageProfile()
  {
    return messageProfile;
  }

  public String getMessageStructure()
  {
    return messageStructure;
  }

  public String getMessageTrigger()
  {
    return messageTrigger;
  }

  public String getMessageType()
  {
    return messageType;
  }

  public CodedEntity getProcessingStatus()
  {
    return processingStatus;
  }

  public String getProcessingStatusCode()
  {
    return processingStatus.getCode();
  }
  
  public void setProcessingStatusCode(String code)
  {
    processingStatus.setCode(code);
  }

  public String getReceivingApplication()
  {
    return receivingApplication;
  }

  public String getReceivingFacility()
  {
    return receivingFacility;
  }

  public String getSendingApplication()
  {
    return sendingApplication;
  }

  public String getSendingFacility()
  {
    return sendingFacility;
  }

  public String getMessageVersion()
  {
    return messageVersion;
  }

  public boolean isProcessingStatusDebugging()
  {
    return processingStatus.getCode().equals(PROCESSING_ID_DEBUGGING);
  }

  public boolean isProcessingStatusProduction()
  {
    return processingStatus.getCode().equals(PROCESSING_ID_PRODUCTION);
  }

  public boolean isProcessingStatusTraining()
  {
    return processingStatus.getCode().equals(PROCESSING_ID_DEBUGGING);
  }

  public void setAckTypeAcceptCode(String ackTypeAccept)
  {
    this.ackTypeAccept.setCode(ackTypeAccept);
  }

  public void setAckTypeApplicationCode(String ackTypeApplication)
  {
    this.ackTypeApplication.setCode(ackTypeApplication);
  }

  public void setCharacterSetCode(String characterSet)
  {
    this.characterSet.setCode(characterSet);
  }

  public void setCharacterSetAltCode(String characterSetAlt)
  {
    this.characterSetAlt.setCode(characterSetAlt);
  }

  public void setCountryCode(String countryCode)
  {
    this.country.setCode(countryCode);
  }

  public void setMessageControl(String messageControl)
  {
	this.put("messageControl", Locator.getPath());
    this.messageControl = messageControl;
  }

  public void setMessageDate(Date messageDate)
  {
	this.put("messageDate", Locator.getPath());
    this.messageDate = messageDate;
  }

  public void setMessageProfile(String messageProfileId)
  {
	this.put("messageProfile", Locator.getPath());
    this.messageProfile = messageProfileId;
  }

  public void setMessageStructure(String messageStructure)
  {
	this.put("messageStructure", Locator.getPath());
    this.messageStructure = messageStructure;
  }

  public void setMessageTrigger(String messageTrigger)
  {
	this.put("messageTrigger", Locator.getPath());
    this.messageTrigger = messageTrigger;
  }

  public void setMessageType(String messageType)
  {
	this.put("messageType", Locator.getPath());
    this.messageType = messageType;
  }

  public void setProcessingIdCode(String processingId)
  {
	this.put("processingId", Locator.getPath());
    this.processingStatus.setCode(processingId);
  }

  public void setReceivingApplication(String receivingApplication)
  {
	this.put("receivingApplication", Locator.getPath());
    this.receivingApplication = receivingApplication;
  }

  public void setReceivingFacility(String receivingFacility)
  {
	this.put("receivingFacility", Locator.getPath());
    this.receivingFacility = receivingFacility;
  }

  public void setSendingApplication(String sendingApplication)
  {
	this.put("sendingApplication", Locator.getPath());
    this.sendingApplication = sendingApplication;
  }

  public void setSendingFacility(String sendingFacility)
  {
	this.put("sendingFacility", Locator.getPath());
    this.sendingFacility = sendingFacility;
  }

  public void setMessageVersion(String versionId)
  {
	this.put("versionId", Locator.getPath());
    this.messageVersion = versionId;
  }

  public MessageReceived getMessageReceived()
  {
    return messageReceived;
  }

  public void setMessageReceived(MessageReceived messageReceived)
  {
    this.messageReceived = messageReceived;
  }

}
