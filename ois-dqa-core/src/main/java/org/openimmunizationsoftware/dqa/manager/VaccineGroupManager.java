/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvxGroup;
import org.openimmunizationsoftware.dqa.db.model.VaccineGroup;

public class VaccineGroupManager
{
  private static VaccineGroupManager singleton = new VaccineGroupManager();

  public static VaccineGroupManager getVaccineGroupManager()
  {
    if (singleton == null)
    {
      singleton = new VaccineGroupManager();
    }
    return singleton;
  }

  private Map<String, VaccineGroup> vaccineGroups = new HashMap<String, VaccineGroup>();
  private List<VaccineCvxGroup> vaccineCvxGroups = null;
  
  public List<VaccineCvxGroup> getVaccineCvxGroups(VaccineCvx vaccineCvx)
  {
    List<VaccineCvxGroup> list = new ArrayList<VaccineCvxGroup>();
    for (VaccineCvxGroup vaccineCvxGroup : vaccineCvxGroups)
    {
      if (vaccineCvxGroup.getVaccineCvx().equals(vaccineCvx))
      {
        list.add(vaccineCvxGroup);
      }
    }
    return list;
  }
  
  public VaccineGroup getVaccineGroup(String groupCode)
  {
    return vaccineGroups.get(groupCode);
  }

  public VaccineGroupManager() {
    SessionFactory factory = OrganizationManager.getSessionFactory();
    Session session = factory.openSession();
    Transaction tx = session.beginTransaction();
    Query query = session.createQuery("from VaccineCvxGroup");
    vaccineCvxGroups = query.list();
    for (VaccineCvxGroup vaccineCvxGroup : vaccineCvxGroups)
    {
      VaccineGroup vaccineGroup = vaccineCvxGroup.getVaccineGroup();
      vaccineGroup.getVaccineCvxList().add(vaccineCvxGroup.getVaccineCvx());
      vaccineGroups.put(vaccineGroup.getGroupCode(), vaccineGroup);
    }
    tx.commit();
    session.close();
  }
}
