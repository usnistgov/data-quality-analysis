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
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineMvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineProduct;

public class VaccineProductManager
{
  private static VaccineProductManager singleton = null;

  public static VaccineProductManager getVaccineProductManager()
  {
    if (singleton == null)
    {
      singleton = new VaccineProductManager();
    }
    return singleton;
  }

  private Map<String, List<VaccineProduct>> vaccineProducts = new HashMap<String, List<VaccineProduct>>();

  private VaccineProductManager() {
    SessionFactory factory = OrganizationManager.getSessionFactory();
    Session session = factory.openSession();
    Transaction tx = session.beginTransaction();

    Query query = session.createQuery("from VaccineProduct");
    List<VaccineProduct> vaccineProductsList = query.list();
    for (VaccineProduct vp : vaccineProductsList)
    {
      String key = vp.getCvx().getCvxCode() + "-" + vp.getMvx().getMvxCode();
      List<VaccineProduct> vpList = vaccineProducts.get(key);
      if (vpList == null)
      {
        vpList = new ArrayList<VaccineProduct>();
        vaccineProducts.put(key, vpList);
      }
      vpList.add(vp);
    }
    tx.commit();
    session.close();
  }

  public List<VaccineProduct> getVaccineProducts(VaccineCvx cvxCode, VaccineMvx mvxCode)
  {
    List<VaccineProduct> vaccineProductList = vaccineProducts.get(cvxCode.getCvxCode() + "-" + mvxCode.getMvxCode());
    return vaccineProductList;
  }
}
