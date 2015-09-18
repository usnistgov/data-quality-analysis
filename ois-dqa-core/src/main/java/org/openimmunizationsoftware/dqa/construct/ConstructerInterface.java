/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.construct;

import org.openimmunizationsoftware.dqa.db.model.MessageReceived;

public interface ConstructerInterface
{
  public String constructMessage(MessageReceived messageReceived);

  public String makeHeader(MessageReceived messageReceived);

  public String makeFooter(MessageReceived messageReceived);
}
