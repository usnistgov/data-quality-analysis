/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa;

import java.util.HashSet;
import java.util.Set;

import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;

public class ProcessLocker
{
  private static final Set<SubmitterProfile> lockedProfiles = new HashSet<SubmitterProfile>();
  
  public static final void unlock(SubmitterProfile profile)
  {
    synchronized (lockedProfiles)
    {
      lockedProfiles.remove(profile);
      lockedProfiles.notifyAll();
    }
  }

  public static final void lock(SubmitterProfile profile)
  {
    boolean obtainedLock = false;
    while (!obtainedLock)
    {
      synchronized (lockedProfiles)
      {
        if (lockedProfiles.contains(profile))
        {
          obtainedLock = false;
        } else
        {
          lockedProfiles.add(profile);
          obtainedLock = true;
        }
        if (!obtainedLock)
        {
          try
          {
            lockedProfiles.wait();
          } catch (InterruptedException ignore)
          {
            // continue
          }
        }
      }
    }
  }
}
