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
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.KnownName;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.types.Name;

public class KnownNames implements Reload
{
  private static KnownNames singleton = null;

  private static String INIT_BLOCK = "";

  public static KnownNames getKnownsNames()
  {
    if (singleton == null)
    {
      synchronized (INIT_BLOCK)
      {
        if (singleton == null)
        {
          singleton = new KnownNames();
          ReloadManager.registerReload(singleton);
        }
      }
    }
    return singleton;
  }

  private Map<String, List<KnownName>> knownNameListMap = null;

  public void reload()
  {
    singleton = null;
    getKnownsNames();
  }

  public boolean match(Patient patient, String nameType)
  {
    for (KnownName knownName : getKnownNameList(nameType))
    {
      if (knownName.match(patient.getName(), patient.getBirthDate()))
      {
        return true;
      }
    }
    return false;
  }

  public boolean match(Name name, String nameType)
  {
    for (KnownName knownName : getKnownNameList(nameType))
    {
      if (knownName.match(name, null))
      {
        return true;
      }
    }
    return false;
  }

  public List<KnownName> getKnownNameList(String nameType)
  {
    List<KnownName> knownNameList = knownNameListMap.get(nameType);
    if (knownNameList == null)
    {
      knownNameList = new ArrayList<KnownName>();
    }
    return knownNameList;
  }

  private KnownNames() {
    knownNameListMap = new HashMap<String, List<KnownName>>();
    SessionFactory factory = OrganizationManager.getSessionFactory();
    Session session = factory.openSession();
    Transaction tx = session.beginTransaction();

    Query query = session.createQuery("from KnownName");
    List<KnownName> knownNameList = query.list();

    for (KnownName knownName : knownNameList)
    {
      List<KnownName> list = knownNameListMap.get(knownName.getNameType());
      if (list == null)
      {
        list = new ArrayList<KnownName>();
        knownNameListMap.put(knownName.getNameType(), list);
      }
      list.add(knownName);
    }
    tx.commit();
    session.close();

  }

}
