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

public class ReloadManager
{
  private static List<Reload> reloadObjects = new ArrayList<Reload>();
  
  public static void registerReload(Reload reload)
  {
    reloadObjects.add(reload);
  }
  
  public static void triggerReload()
  {
    List<Reload> oldList = new ArrayList<Reload>(reloadObjects);
    reloadObjects = new ArrayList<Reload>();
    for (Reload reload : oldList)
    {
      reload.reload();
    }
  }
}
