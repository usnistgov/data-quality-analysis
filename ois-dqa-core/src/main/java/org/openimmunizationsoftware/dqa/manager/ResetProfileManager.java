/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.IssueAction;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssueStatus;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;

public class ResetProfileManager
{
  public void resetProfile(SubmitterProfile profile, Session session)
  {
    Transaction trans = session.beginTransaction();
    
    CodesReceived codesReceived = CodesReceived.getCodesReceived(profile, session);
    CodesReceived codesReceivedParent = codesReceived.getParent();
    
    for (CodeTable codeTable : codesReceived.getCodeTableList())
    {
      for (CodeReceived codeReceived : codesReceived.getCodesReceived(codeTable))
      {
        CodeReceived codeReceivedParent = codesReceivedParent.getCodeReceived(codeReceived.getReceivedValue(), codeTable, codeReceived.getContextValue());
        if (codeReceivedParent != null)
        {
          codeReceived.setCodeStatus(codeReceivedParent.getCodeStatus());
          codeReceived.setCodeValue(codeReceivedParent.getCodeValue());
          session.update(codeReceived);
        }
      }
    }
    
    SubmitterProfile parentProfile = null;
    if (profile.getReportTemplate() != null)
    {
      parentProfile = profile.getReportTemplate().getBaseProfile();
    }

    // Now go through each potential issue status and update it
    Query query = session.createQuery("from PotentialIssueStatus where profile = ?");
    query.setParameter(0, profile);
    List<PotentialIssueStatus> potentialIssueStatusList = query.list();
    for (PotentialIssueStatus potentialIssueStatus : potentialIssueStatusList)
    {
      IssueAction issueAction = null;
        
      if (parentProfile != null)
      {
        query = session.createQuery("from PotentialIssueStatus where profile = ? and issue = ?");
        query.setParameter(0, parentProfile);
        query.setParameter(1, potentialIssueStatus.getIssue());
        List<PotentialIssueStatus> parentPotentialIssueStatusList = query.list();
        if (parentPotentialIssueStatusList.size() > 0)
        {
          PotentialIssueStatus parentPotentialIssueStatus = parentPotentialIssueStatusList.get(0);
          issueAction = parentPotentialIssueStatus.getAction();
        }
      }
      if (issueAction == null)
      {
        issueAction = potentialIssueStatus.getIssue().getDefaultIssueAction();
      }
      if (issueAction != null && !issueAction.equals(potentialIssueStatus.getIssue()))
      {
        potentialIssueStatus.setAction(issueAction);
        session.update(potentialIssueStatus);
      }
    }
        
    trans.commit();
  }
}
