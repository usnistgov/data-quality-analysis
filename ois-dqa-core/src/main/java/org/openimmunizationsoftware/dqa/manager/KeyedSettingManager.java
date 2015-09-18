/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.InitializationException;
import org.openimmunizationsoftware.dqa.db.model.Application;
import org.openimmunizationsoftware.dqa.db.model.KeyedSetting;

public class KeyedSettingManager implements Reload
{
  private static KeyedSettingManager singleton = null;
  private static final String INIT_BLOCK = "init";

  public static KeyedSettingManager getKeyedSettingManager()
  {
    if (singleton == null)
    {
      synchronized (INIT_BLOCK)
      {
        if (singleton == null)
        {
          singleton = new KeyedSettingManager();
          ReloadManager.registerReload(singleton);
        }
      }
    }
    return singleton;
  }

  public void reload()
  {
    if (singleton != null)
    {
      singleton = null;
    }
    getKeyedSettingManager();
  }

  public static Application getApplication()
  {
    return getKeyedSettingManager().application;
  }

  private Map<String, KeyedSetting> keyedSettingsMap = new HashMap<String, KeyedSetting>();
  private Application application = null;
  private KeyedSettingManager parent = null;

  private KeyedSettingManager() {
    SessionFactory factory = OrganizationManager.getSessionFactory();
    Session session = factory.openSession();
    Transaction tx = session.beginTransaction();
    findApplicationToRun(session);
    Query query = session.createQuery("from KeyedSetting where objectCode = ? and objectId = ?");
    query.setParameter(0, "Application");
    query.setParameter(1, application.getApplicationId());
    List<KeyedSetting> keyedSettings = query.list();
    for (KeyedSetting keyedSetting : keyedSettings)
    {
      keyedSettingsMap.put(keyedSetting.getKeyedCode(), keyedSetting);
    }
    tx.commit();
    session.close();
  }

  public int getKeyedValueInt(String keyedCode, int defaultValue)
  {
    String value = getKeyedValue(keyedCode, String.valueOf(defaultValue));
    try
    {
      return Integer.parseInt(value);
    } catch (NumberFormatException nfe)
    {
      return defaultValue;
    }
  }

  public boolean getKeyedValueBoolean(String keyedCode, boolean defaultValue)
  {
    String value = getKeyedValue(keyedCode, defaultValue ? "Y" : "N");
    if (value.toUpperCase().startsWith("Y") || value.toUpperCase().startsWith("T"))
    {
      return true;
    }
    return false;
  }

  public String getKeyedValue(String keyedCode, String defaultValue)
  {
    KeyedSetting keyedSetting = keyedSettingsMap.get(keyedCode);
    if (keyedSetting == null)
    {
      if (parent != null)
      {
        return parent.getKeyedValue(keyedCode, defaultValue);
      } else
      {
        return defaultValue;
      }
    }
    return keyedSetting.getKeyedValue();
  }

  private void findApplicationToRun(Session session)
  {
    //session.evict(application);
    Query query = session.createQuery("from Application where runThis = true");
    List<Application> applications = query.list();
    if (applications.size() > 0)
    {
      application = applications.get(0);
    } else
    {
      throw new InitializationException("Unable to start application, no application is currently defined to run");
    }
  }

}
