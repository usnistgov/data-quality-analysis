/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.process;

public class MessageProcessorException extends Exception {

  static final long serialVersionUID = 1L;
  
  public MessageProcessorException()
  {
    super();
  }
	  
  public MessageProcessorException(String message)
  {
    super(message);
  }
  
  public MessageProcessorException(Throwable t)
  {
    super(t);
  }
  
  public MessageProcessorException(String message, Throwable t)
  {
    super(message, t);
  }
}
