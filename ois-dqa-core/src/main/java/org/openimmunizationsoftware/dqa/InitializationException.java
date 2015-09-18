/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa;

public class InitializationException extends RuntimeException
{
  public InitializationException()
  {
    super();
  }
  
  public InitializationException(String message)
  {
    super(message);
  }
  
  public InitializationException(Throwable t)
  {
    super(t);
  }
  
  public InitializationException(String message, Throwable t)
  {
    super(message, t);
  }
}
