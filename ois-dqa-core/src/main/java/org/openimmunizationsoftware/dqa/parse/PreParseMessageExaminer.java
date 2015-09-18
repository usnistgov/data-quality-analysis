/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

import java.util.ArrayList;
import java.util.List;

import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.manager.PotentialIssueManager;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;

public class PreParseMessageExaminer
{
  private boolean isHL7v2 = false;
  private String messageType = "";
  private String messageKey = "";
  private String receivingApplication = "";
  private String receivingFacility = "";
  private String sendingApplication = "";
  private String sendingFacility = "";
  private String processingId = "";
  private boolean debugOn = false;
  private PotentialIssue isssueEncountered = null;
  
  public PotentialIssue getIsssueEncountered()
  {
    return isssueEncountered;
  }

  public String getProcessingId()
  {
    return processingId;
  }

  public void setProcessingId(String processingId)
  {
    this.processingId = processingId;
  }

  public boolean isDebugOn()
  {
    return debugOn;
  }

  public void setDebugOn(boolean debugOn)
  {
    this.debugOn = debugOn;
  }

  public String getReceivingApplication()
  {
    return receivingApplication;
  }

  public void setReceivingApplication(String receivingApplication)
  {
    this.receivingApplication = receivingApplication;
  }

  public String getReceivingFacility()
  {
    return receivingFacility;
  }

  public void setReceivingFacility(String receivingFacility)
  {
    this.receivingFacility = receivingFacility;
  }

  public String getSendingApplication()
  {
    return sendingApplication;
  }

  public void setSendingApplication(String sendingApplication)
  {
    this.sendingApplication = sendingApplication;
  }

  public String getSendingFacility()
  {
    return sendingFacility;
  }

  public void setSendingFacility(String sendingFacility)
  {
    this.sendingFacility = sendingFacility;
  }

  private char[] separators = new char[5];

  public String getMessageKey()
  {
    return messageKey;
  }

  public void setMessageKey(String messageKey)
  {
    this.messageKey = messageKey;
  }

  public boolean isHL7v2()
  {
    return isHL7v2;
  }

  public void setHL7v2(boolean isHL7v2)
  {
    this.isHL7v2 = isHL7v2;
  }

  public String getMessageType()
  {
    return messageType;
  }

  public void setMessageType(String messageType)
  {
    this.messageType = messageType;
  }

  public PreParseMessageExaminer(String messageText) {
    isHL7v2 = HL7Util.setupSeparators(messageText, separators);
    if (isHL7v2)
    {
      if (!HL7Util.checkSeparatorsAreValid(separators))
      {
        isssueEncountered = PotentialIssues.getPotentialIssues().Hl7MshEncodingCharacterIsInvalid;
        HL7Util.setDefault(separators);
      }
      
      // truncate after first CR, just need to look at first line
      int pos = messageText.indexOf('\r');
      if (pos > -1)
      {
        messageText = messageText.substring(0, pos);
      }

      List<String> fields = new ArrayList<String>();

      int startPos = 0;
      int endPos = 0;
      while (startPos < messageText.length())
      {
        endPos = messageText.indexOf(separators[HL7Util.BAR], startPos);
        if (endPos == -1)
        {
          endPos = messageText.length();
        }
        fields.add(messageText.substring(startPos, endPos));
        startPos = endPos + 1;
      }

      receivingApplication = read(fields, 3);
      receivingFacility = read(fields, 4);
      sendingApplication = read(fields, 5);
      sendingFacility = read(fields, 6);
      processingId = read(fields, 11);

      messageType = read(fields, 9);
      messageKey = read(fields, 10);
      
      debugOn = HL7Util.PROCESSING_ID_DEBUGGING.equalsIgnoreCase(processingId);

    }
  }

  public String read(List<String> fields, int field)
  {
    return fields.size() > (field - 1) ? read(fields.get((field - 1))) : "";
  }

  public String read(String fieldText)
  {
    int pos;
    String value = "";
    if (fieldText != null)
    {
      pos = fieldText.indexOf(separators[HL7Util.CAR]);
      if (pos == -1)
      {
        value = fieldText;
      } else
      {
        value = fieldText.substring(0, pos);
      }
      pos = value.indexOf(separators[HL7Util.TIL]);
      if (pos > -1)
      {
        value = value.substring(0, pos);
      }
      pos = value.indexOf(separators[HL7Util.AMP]);
      if (pos > -1)
      {
        value = value.substring(0, pos);
      }
      value = value.toUpperCase().trim();
    }
    return value;
  }
}
