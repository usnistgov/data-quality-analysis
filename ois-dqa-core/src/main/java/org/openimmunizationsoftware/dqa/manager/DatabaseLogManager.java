/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openimmunizationsoftware.dqa.SoftwareVersion;
import org.openimmunizationsoftware.dqa.db.model.DatabaseLog;

public class DatabaseLogManager
{
  private static List<DatabaseLog> databaseLogList = null;
  private static DatabaseLog latestDatabaseLog = null;

  private static void initDatabaseLogList(Session dataSession)
  {
    if (latestDatabaseLog == null)
    {
      databaseLogList = new ArrayList<DatabaseLog>();
      Query query = dataSession.createQuery("from DatabaseLog order by changeId desc");

      List<DatabaseLog> list = query.list();
      if (list.size() > 0)
      {
        latestDatabaseLog = list.get(0);
        for (DatabaseLog databaseLog : list)
        {
          if (!databaseLog.getChangeVersion().equals(latestDatabaseLog.getChangeVersion()))
          {
            break;
          }
          databaseLogList.add(databaseLog);
        }
      }
    }
  }

  public static DatabaseLog getCurrentVersion(Session dataSession)
  {
    initDatabaseLogList(dataSession);
    return latestDatabaseLog;
  }
  
  public static String getVersionDescription(Session dataSession)
  {
    DatabaseLog databaseLog = getCurrentVersion(dataSession);
    String databaseVersion = "pre 1.04";
    if (databaseLog != null)
    {
      databaseVersion = databaseLog.getChangeVersion();
    }
    String softwareVersion = SoftwareVersion.VERSION;
    if (softwareVersion.startsWith(databaseVersion))
    {
      return "DQA Core " + softwareVersion;
    }
    else
    {
      return "DQA Core " + softwareVersion  + " DB " + databaseVersion;
    }
  }
  
  public static String getVersion(Session dataSession)
  {
    DatabaseLog databaseLog = getCurrentVersion(dataSession);
    String databaseVersion = "p1.04";
    if (databaseLog != null)
    {
      databaseVersion = databaseLog.getChangeVersion();
    }
    String softwareVersion = SoftwareVersion.VERSION;
    if (softwareVersion.startsWith(databaseVersion))
    {
      return "dqa." + softwareVersion;
    }
    else
    {
      return "dqa." + softwareVersion  + ".db." + databaseVersion;
    }
  }
}
