/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class BatchVaccineCvx implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int batchVaccineCvxId = 0;
  private MessageBatch messageBatch = null;
  private VaccineCvx vaccineCvx = null;
  private int receivedCount = 0;
  
  public BatchVaccineCvx()
  {
    // defaults
  }
  
  public void inc(BatchVaccineCvx batchVaccineCvx)
  {
    this.receivedCount += batchVaccineCvx.getReceivedCount();
  }
  
  public void incReceivedCount()
  {
    receivedCount++;
  }
  
  public BatchVaccineCvx(VaccineCvx vaccineCvx, MessageBatch messageBatch)
  {
    this.vaccineCvx = vaccineCvx;
    this.messageBatch = messageBatch;
  }
  
  public int getBatchVaccineCvxId()
  {
    return batchVaccineCvxId;
  }
  public void setBatchVaccineCvxId(int batchVaccineCvxId)
  {
    this.batchVaccineCvxId = batchVaccineCvxId;
  }
  public MessageBatch getMessageBatch()
  {
    return messageBatch;
  }
  public void setMessageBatch(MessageBatch messageBatch)
  {
    this.messageBatch = messageBatch;
  }
  public VaccineCvx getVaccineCvx()
  {
    return vaccineCvx;
  }
  public void setVaccineCvx(VaccineCvx vaccineCvx)
  {
    this.vaccineCvx = vaccineCvx;
  }
  public int getReceivedCount()
  {
    return receivedCount;
  }
  public void setReceivedCount(int receivedCount)
  {
    this.receivedCount = receivedCount;
  }

}
