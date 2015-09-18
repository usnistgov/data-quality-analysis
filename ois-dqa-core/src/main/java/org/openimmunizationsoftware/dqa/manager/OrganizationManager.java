/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.openimmunizationsoftware.dqa.db.model.Organization;

public class OrganizationManager
{

  private static SessionFactory factory;
  private static String lock = "lock";

  public static SessionFactory getSessionFactory()
  {
    synchronized (lock)
    {
      if (factory == null)
      {
        factory = new AnnotationConfiguration().configure().buildSessionFactory();
      }
      return factory;
    }
  }

}
