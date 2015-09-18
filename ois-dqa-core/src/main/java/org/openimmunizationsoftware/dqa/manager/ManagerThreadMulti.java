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

public class ManagerThreadMulti extends ManagerThread
{
  protected Map<String, ManagerThread> threads = new HashMap<String, ManagerThread>();
  
  public ManagerThreadMulti(String label) {
    super(label);
  }
  
  public List<ManagerThread> getManagerThreads()
  {
    return new ArrayList<ManagerThread>(threads.values());
  }
}
