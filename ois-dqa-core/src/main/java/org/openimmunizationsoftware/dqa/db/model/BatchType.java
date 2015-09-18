/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class BatchType implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  public static final BatchType SUBMISSION = new BatchType("S", "Submission");
  public static final BatchType DAILY = new BatchType("D", "Daily");
  public static final BatchType WEEKLY = new BatchType("W", "Weekly");
  public static final BatchType MONTHLY = new BatchType("M", "Monthly");
  public static final BatchType OTHER = new BatchType("O", "Other");
  
  private String typeCode = "";
  private String typeLabel = "";
  
  public BatchType()
  {
    // default
  }
  
  public BatchType(String typeCode, String typeLabel)
  {
    this.typeCode = typeCode;
    this.typeLabel = typeLabel;
  }

  public String getTypeCode()
  {
    return typeCode;
  }
  public void setTypeCode(String typeCode)
  {
    this.typeCode = typeCode;
  }
  public String getTypeLabel()
  {
    return typeLabel;
  }
  public void setTypeLabel(String typeLabel)
  {
    this.typeLabel = typeLabel;
  }
  
}
