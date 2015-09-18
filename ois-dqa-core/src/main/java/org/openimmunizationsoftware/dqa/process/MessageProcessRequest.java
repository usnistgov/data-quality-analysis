/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.process;

import org.hibernate.Session;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.parse.PreParseMessageExaminer;
import org.openimmunizationsoftware.dqa.parse.VaccinationParserHL7;
import org.openimmunizationsoftware.dqa.quality.QualityCollector;

public class MessageProcessRequest extends PreParseMessageExaminer
{

  private boolean debugFlag = false;
  private VaccinationParserHL7 parser = null;
  private String messageText = null;
  private SubmitterProfile profile = null;
  private Session session = null;
  private QualityCollector qualityCollector = null;

  
  public MessageProcessRequest(String messageText)
  {
    super(messageText);
    this.messageText = messageText;
  }
  

  public boolean isDebugFlag()
  {
    return debugFlag;
  }
  public void setDebugFlag(boolean debugFlag)
  {
    this.debugFlag = debugFlag;
  }
  public VaccinationParserHL7 getParser()
  {
    return parser;
  }
  public void setParser(VaccinationParserHL7 parser)
  {
    this.parser = parser;
  }
  public String getMessageText()
  {
    return messageText;
  }
  public void setMessageText(String sb)
  {
    this.messageText = sb;
  }
  public SubmitterProfile getProfile()
  {
    return profile;
  }
  public void setProfile(SubmitterProfile profile)
  {
    this.profile = profile;
  }
  public Session getSession()
  {
    return session;
  }
  public void setSession(Session session)
  {
    this.session = session;
  }
  public QualityCollector getQualityCollector()
  {
    return qualityCollector;
  }
  public void setQualityCollector(QualityCollector qualityCollector)
  {
    this.qualityCollector = qualityCollector;
  }
  
}
