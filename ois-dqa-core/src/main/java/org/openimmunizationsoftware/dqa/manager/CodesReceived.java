/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.CodeMaster;
import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.CodeStatus;
import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.VaccineCpt;
import org.openimmunizationsoftware.dqa.db.model.VaccineCvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineMvx;
import org.openimmunizationsoftware.dqa.db.model.VaccineProduct;

public class CodesReceived
{
  private static CodesReceived singleton = null;

  public static CodesReceived getCodesReceived()
  {
    if (singleton == null)
    {
      singleton = new CodesReceived(true);
    }
    return singleton;
  }

  public void dumpInternalDetails(PrintStream out, String step)
  {
    out.println("*** CODES RECEIVED **** AT STEP " + step);
    out.println("Code table count: " + codeTableMaps.size());
    for (CodeTable codeTable : codeTableMaps.keySet())
    {
      if (codeTableMaps.get(codeTable).size() > 0)
      {
        out.println(" * " + codeTable.getTableLabel() + " [" + codeTable.getTableId() + "]");
        for (CodeReceived codeReceived : codeTableMaps.get(codeTable).values())
        {
          out.println("    - '" + codeReceived.getReceivedValue() + "' ==> '" + codeReceived.getCodeValue() + "' #" + codeReceived.getReceivedCount()
              + " [" + codeReceived.getCodeId() + "] profile=" + codeReceived.getProfile().getProfileId());
        }
      }
    }
  }

  public static CodesReceived getCodesReceived(SubmitterProfile profile, Session session)
  {
    getCodesReceived();
    CodesReceived codesReceived = new CodesReceived(false);

    if (profile.getReportTemplate() != null)
    {
      if (!profile.getReportTemplate().getBaseProfile().equals(profile))
      {
        codesReceived.parent = getCodesReceived(profile.getReportTemplate().getBaseProfile(), session);
      }
    }
    if (codesReceived.parent == null)
    {
      codesReceived.parent = singleton;
    }

    for (CodeTable codeTable : codeTables.values())
    {
      String sql = "from CodeReceived where profile = ? and table = ?";
      Query query = session.createQuery(sql);
      query.setParameter(0, profile);
      query.setParameter(1, codeTable);
      codesReceived.addToCodeTableMaps(codeTable, query.list());
    }
    return codesReceived;
  }

  public void saveCodesReceived(Session session)
  {
    for (Map<String, CodeReceived> map : codeTableMaps.values())
    {
      for (CodeReceived cr : map.values())
      {
        session.saveOrUpdate(cr);
      }
    }
    // private Map<CodeTable, Map<String, CodeReceived>> codeTableMaps = new
    // HashMap<CodeTable, Map<String, CodeReceived>>();
  }

  public void registerCodeReceived(CodeReceived codeReceived, CodeReceived context)
  {
    String receivedValue = codeReceived.getReceivedValue().toUpperCase();
    if (context != null)
    {
      receivedValue = context.getContextWithCodeValue() + "-" + receivedValue;
    }
    registerCodeReceived(receivedValue, codeReceived);
  }

  public void registerCodeReceived(CodeReceived codeReceived, String contextValue)
  {
    String receivedValue = codeReceived.getReceivedValue().toUpperCase();
    if (contextValue != null)
    {
      receivedValue = contextValue + "-" + receivedValue;
    }
    registerCodeReceived(receivedValue, codeReceived);
  }

  private void registerCodeReceived(String receivedValue, CodeReceived codeReceived)
  {
    Map<String, CodeReceived> codesReceived = codeTableMaps.get(codeReceived.getTable());
    if (codesReceived == null)
    {
      codesReceived = new HashMap<String, CodeReceived>();
      codeTableMaps.put(codeReceived.getTable(), codesReceived);
    }
    codesReceived.put(receivedValue, codeReceived);
  }

  public CodeReceived getCodeReceived(String receivedValue, CodeTable codeTable, CodeReceived context)
  {
    receivedValue = receivedValue.toUpperCase();
    if (context != null)
    {
      receivedValue = context.getContextWithCodeValue() + "-" + receivedValue;
    } 
    return getCodeReceived(receivedValue, codeTable);
  }

  public CodeReceived getCodeReceived(String receivedValue, CodeTable codeTable, String contextValue)
  {
    receivedValue = receivedValue.toUpperCase();
    if (contextValue != null)
    {
      receivedValue = contextValue + "-" + receivedValue;
    }
    return getCodeReceived(receivedValue, codeTable);
  }

  protected CodeReceived getCodeReceived(String receivedValue, CodeTable codeTable)
  {
    CodeReceived cr = null;
    Map<String, CodeReceived> codesReceived = codeTableMaps.get(codeTable);
    if (codesReceived != null)
    {
      cr = codesReceived.get(receivedValue);
    }
    if (cr == null && parent != null)
    {
      // Didn't find under profile, looking at parent now
      cr = parent.getCodeReceived(receivedValue, codeTable);
    }
    return cr;
  }

  public List<CodeReceived> getCodesReceived(CodeTable codeTable)
  {
    List<CodeReceived> codesReceived = new ArrayList<CodeReceived>();
    if (codeTableMaps.containsKey(codeTable))
    {
      codesReceived.addAll(codeTableMaps.get(codeTable).values());
    }
    // TODO sort list?
    return codesReceived;
  }

  public static CodeTable getCodeTable(CodeTable.Type table)
  {
    getCodesReceived();
    CodeTable ct = codeTables.get(table.getTableId());
    if (ct == null)
    {
      throw new NullPointerException("Code table " + table.getTableId() + " unrecognized");
    }
    return ct;
  }

  public Map<Integer, CodeTable> getCodeTables()
  {
    return codeTables;
  }

  public List<CodeTable> getCodeTableList()
  {
    List<CodeTable> list = new ArrayList<CodeTable>(codeTables.values());
    // TODO sort array?
    return list;
  }

  private static Map<Integer, CodeTable> codeTables = new HashMap<Integer, CodeTable>();

  private Map<CodeTable, Map<String, CodeReceived>> codeTableMaps = new HashMap<CodeTable, Map<String, CodeReceived>>();
  private CodesReceived parent = null;

  public CodesReceived getParent()
  {
    return parent;
  }

  private CodesReceived(boolean parent) {
    if (parent)
    {
      SessionFactory factory = OrganizationManager.getSessionFactory();
      Session session = factory.openSession();
      Transaction tx = session.beginTransaction();

      Query query = session.createQuery("from CodeTable");
      List<CodeTable> codeTableList = query.list();
      for (CodeTable codeTable : codeTableList)
      {
        codeTables.put(codeTable.getTableId(), codeTable);
        if (codeTable.getTableId() == CodeTable.Type.VACCINATION_CPT_CODE.getTableId())
        {
          query = session.createQuery("from VaccineCpt");
          addToCodeTableMapsCpt(codeTable, query.list(), (SubmitterProfile) session.get(SubmitterProfile.class, 1));
        } else if (codeTable.getTableId() == CodeTable.Type.VACCINATION_CVX_CODE.getTableId())
        {
          query = session.createQuery("from VaccineCvx");
          addToCodeTableMapsCvx(codeTable, query.list(), (SubmitterProfile) session.get(SubmitterProfile.class, 1));
        } else if (codeTable.getTableId() == CodeTable.Type.VACCINATION_MANUFACTURER_CODE.getTableId())
        {
          query = session.createQuery("from VaccineMvx");
          addToCodeTableMapsMvx(codeTable, query.list(), (SubmitterProfile) session.get(SubmitterProfile.class, 1));
        } else if (codeTable.getTableId() == CodeTable.Type.VACCINE_PRODUCT.getTableId())
        {
          query = session.createQuery("from VaccineProduct");
          addToCodeTableMapsVaccineProduct(codeTable, query.list(), (SubmitterProfile) session.get(SubmitterProfile.class, 1));
        } else
        {
          String sql = "from CodeMaster where table = ?";
          query = session.createQuery(sql);
          query.setParameter(0, codeTable);
          addMastersToCodeTableMaps(codeTable, query.list(), (SubmitterProfile) session.get(SubmitterProfile.class, 1));
        }
      }
      tx.commit();
      session.close();
    }
  }

  protected void addToCodeTableMaps(CodeTable codeTable, List<CodeReceived> codesReceived)
  {
    Map<String, CodeReceived> codeReceivedMap = new HashMap<String, CodeReceived>();
    for (CodeReceived codeReceived : codesReceived)
    {
      String receivedValue = codeReceived.getReceivedValue();
       if (codeReceived.getContextValue() != null && !codeReceived.getContextValue().equals(""))
       {
         receivedValue = codeReceived.getContextValue() + "-" + receivedValue;
       }
      codeReceivedMap.put(receivedValue, codeReceived);
    }
    codeTableMaps.put(codeTable, codeReceivedMap);
  }

  protected void addMastersToCodeTableMaps(CodeTable codeTable, List<CodeMaster> codeMasters, SubmitterProfile profile)
  {
    Map<String, CodeReceived> codeReceivedMap = new HashMap<String, CodeReceived>();
    for (CodeMaster codeMaster : codeMasters)
    {
      CodeReceived codeReceived = new CodeReceived();
      codeReceived.setCodeValue(codeMaster.getContextValue());
      codeReceived.setReceivedValue(codeMaster.getCodeValue());
      codeReceived.setCodeStatus(codeMaster.getCodeStatus());
      codeReceived.setCodeValue(codeMaster.getUseValue());
      codeReceived.setProfile(profile);
      codeReceived.setTable(codeTable);
      codeReceived.setCodeLabel(codeMaster.getCodeLabel());
      String receivedValue = codeReceived.getReceivedValue();
      if (codeMaster.getContext() != null )
      {
        receivedValue = codeMaster.getContextValue() + "-" + receivedValue;
        codeReceived.setContextValue(codeMaster.getContextValue());
      }
      codeReceivedMap.put(receivedValue, codeReceived);
    }
    codeTableMaps.put(codeTable, codeReceivedMap);
  }

  protected void addToCodeTableMapsCpt(CodeTable codeTable, List<VaccineCpt> vaccineCpts, SubmitterProfile profile)
  {
    Map<String, CodeReceived> codeReceivedMap = new HashMap<String, CodeReceived>();
    for (VaccineCpt vaccineCpt : vaccineCpts)
    {
      CodeReceived codeReceived = new CodeReceived();
      codeReceived.setReceivedValue(vaccineCpt.getCptCode());
      codeReceived.setCodeStatus(CodeStatus.VALID);
      codeReceived.setCodeValue(vaccineCpt.getCptCode());
      codeReceived.setProfile(profile);
      codeReceived.setTable(codeTable);
      codeReceivedMap.put(codeReceived.getContextWithCodeValue(), codeReceived);
    }
    codeTableMaps.put(codeTable, codeReceivedMap);
  }

  protected void addToCodeTableMapsCvx(CodeTable codeTable, List<VaccineCvx> vaccineCvxs, SubmitterProfile profile)
  {
    Map<String, CodeReceived> codeReceivedMap = new HashMap<String, CodeReceived>();
    for (VaccineCvx vaccineCvx : vaccineCvxs)
    {
      CodeReceived codeReceived = new CodeReceived();
      codeReceived.setReceivedValue(vaccineCvx.getCvxCode());
      codeReceived.setCodeStatus(CodeStatus.VALID);
      codeReceived.setCodeValue(vaccineCvx.getCvxCode());
      codeReceived.setProfile(profile);
      codeReceived.setTable(codeTable);
      codeReceivedMap.put(codeReceived.getContextWithCodeValue(), codeReceived);
    }
    codeTableMaps.put(codeTable, codeReceivedMap);
  }

  protected void addToCodeTableMapsVaccineProduct(CodeTable codeTable, List<VaccineProduct> vaccineProductList, SubmitterProfile profile)
  {
    Map<String, CodeReceived> codeReceivedMap = new HashMap<String, CodeReceived>();
    for (VaccineProduct vaccineProduct : vaccineProductList)
    {
      CodeReceived codeReceived = new CodeReceived();
      codeReceived.setReceivedValue(vaccineProduct.getProductCode());
      codeReceived.setCodeStatus(CodeStatus.VALID);
      codeReceived.setCodeValue(vaccineProduct.getProductCode());
      codeReceived.setProfile(profile);
      codeReceived.setTable(codeTable);
      codeReceived.setCodeLabel(vaccineProduct.getProductName());
      codeReceivedMap.put(codeReceived.getContextWithCodeValue(), codeReceived);
    }
    codeTableMaps.put(codeTable, codeReceivedMap);
  }

  protected void addToCodeTableMapsMvx(CodeTable codeTable, List<VaccineMvx> vaccineMvxs, SubmitterProfile profile)
  {
    Map<String, CodeReceived> codeReceivedMap = new HashMap<String, CodeReceived>();
    for (VaccineMvx vaccineMvx : vaccineMvxs)
    {
      CodeReceived codeReceived = new CodeReceived();
      codeReceived.setReceivedValue(vaccineMvx.getMvxCode());
      codeReceived.setCodeStatus(CodeStatus.VALID);
      codeReceived.setCodeValue(vaccineMvx.getMvxCode());
      codeReceived.setProfile(profile);
      codeReceived.setTable(codeTable);
      codeReceivedMap.put(codeReceived.getContextWithCodeValue(), codeReceived);
    }
    codeTableMaps.put(codeTable, codeReceivedMap);
  }
}
