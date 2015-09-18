/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;

public class ReceiveQueue implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  private int receiveQueueId = 0;
  private MessageBatch messageBatch = null;
  private MessageReceived messageReceived = null;
  private SubmitStatus submitStatus = null;
  
  public int getReceiveQueueId()
  {
    return receiveQueueId;
  }
  public void setReceiveQueueId(int receiveQueueId)
  {
    this.receiveQueueId = receiveQueueId;
  }
  public MessageBatch getMessageBatch()
  {
    return messageBatch;
  }
  public void setMessageBatch(MessageBatch messageBatch)
  {
    this.messageBatch = messageBatch;
  }
  public MessageReceived getMessageReceived()
  {
    return messageReceived;
  }
  public void setMessageReceived(MessageReceived messageReceived)
  {
    this.messageReceived = messageReceived;
  }
  public SubmitStatus getSubmitStatus()
  {
    return submitStatus;
  }
  public void setSubmitStatus(SubmitStatus submitStatus)
  {
    this.submitStatus = submitStatus;
  }
  
}
