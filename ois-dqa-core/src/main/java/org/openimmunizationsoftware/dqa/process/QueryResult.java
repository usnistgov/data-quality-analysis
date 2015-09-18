/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.process;

import java.util.ArrayList;
import java.util.List;

import org.openimmunizationsoftware.dqa.db.model.received.NextOfKin;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
public class QueryResult
{
  private Patient patient = null;
  private List<NextOfKin> nextOfKinList = new ArrayList<NextOfKin>();
  private List<Vaccination> vaccinationList = new ArrayList<Vaccination>();
  

  public Patient getPatient()
  {
    return patient;
  }
  public void setPatient(Patient patient)
  {
    this.patient = patient;
  }
  public List<NextOfKin> getNextOfKinList()
  {
    return nextOfKinList;
  }
  public void setNextOfKinList(List<NextOfKin> nextOfKinList)
  {
    this.nextOfKinList = nextOfKinList;
  }
  public List<Vaccination> getVaccinationList()
  {
    return vaccinationList;
  }
  public void setVaccinationList(List<Vaccination> vaccinationList)
  {
    this.vaccinationList = vaccinationList;
  }
  
}
