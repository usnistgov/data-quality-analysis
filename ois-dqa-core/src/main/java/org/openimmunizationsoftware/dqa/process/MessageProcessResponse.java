/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.process;

import org.openimmunizationsoftware.dqa.db.model.MessageReceivedGeneric;

public class MessageProcessResponse
{
  private MessageReceivedGeneric messageReceived = null;

  public MessageReceivedGeneric getMessageReceived()
  {
    return messageReceived;
  }

  public void setMessageReceived(MessageReceivedGeneric messageReceived)
  {
    this.messageReceived = messageReceived;
  }
  
}
