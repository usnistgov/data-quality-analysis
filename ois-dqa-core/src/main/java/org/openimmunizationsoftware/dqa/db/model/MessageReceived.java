/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openimmunizationsoftware.dqa.db.model.received.NextOfKin;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;

public class MessageReceived extends MessageReceivedGeneric
{
  
  private static final long serialVersionUID = 1l;
  
  private List<NextOfKin> nextOfKins = new ArrayList<NextOfKin>();
  private Patient patient = new Patient();
  private long receivedId = 0l;
  private Map<String, Vaccination> vaccinationMap = new HashMap<String, Vaccination>();
  private List<Vaccination> vaccinations = new ArrayList<Vaccination>();
  
  private int internalTemporaryId = 0;
  
  public int getInternalTemporaryId()
  {
    return internalTemporaryId;
  }

  public void setInternalTemporaryId(int internalTemporaryId)
  {
    this.internalTemporaryId = internalTemporaryId;
  }

  public List<NextOfKin> getNextOfKins()
  {
    return nextOfKins;
  }

  public Patient getPatient()
  {
    return patient;
  }

  public long getReceivedId()
  {
    return receivedId;
  }

  public Map<String, Vaccination> getVaccinationMap()
  {
    return vaccinationMap;
  }

  public List<Vaccination> getVaccinations()
  {
    return vaccinations;
  }

  public void setNextOfKins(List<NextOfKin> nextOfKins)
  {
    this.nextOfKins = nextOfKins;
  }

  public void setPatient(Patient patient)
  {
    this.patient = patient;
  }

  public void setReceivedId(long receivedId)
  {
    this.receivedId = receivedId;
  }

  public void setVaccinationMap(Map<String, Vaccination> vaccinationMap)
  {
    this.vaccinationMap = vaccinationMap;
  }

  public void setVaccinations(List<Vaccination> vaccinations)
  {
    this.vaccinations = vaccinations;
  }
  
}
