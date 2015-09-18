package org.openimmunizationsoftware.dqa.manager;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssueStatus;
import org.openimmunizationsoftware.dqa.db.model.ReportTemplate;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;

public class SubmitterProfileManager
{
  public static void synchronizeToTemplate(SubmitterProfile profile, Session session)
  {
    ReportTemplate reportTemplate = profile.getReportTemplate();
    SubmitterProfile baseProfile = reportTemplate.getBaseProfile();
    if (!baseProfile.equals(profile))
    {
      Transaction tx = session.beginTransaction();
      Query query = session.createQuery("from PotentialIssueStatus where profile = ? ");
      query.setParameter(0, profile);
      List<PotentialIssueStatus> potentialIssueStatusList = query.list();
      for (PotentialIssueStatus potentialIssueStatus : potentialIssueStatusList)
      {
        query = session.createQuery("from PotentialIssueStatus where profile = ? and issue = ?");
        query.setParameter(0, baseProfile);
        query.setParameter(1, potentialIssueStatus.getIssue());
        List<PotentialIssueStatus> basePotentialIssueStatusList = query.list();
        if (basePotentialIssueStatusList.size() > 0)
        {
          PotentialIssueStatus basePotentialIssueStatus = basePotentialIssueStatusList.get(0);
          potentialIssueStatus.setAction(basePotentialIssueStatus.getAction());
          potentialIssueStatus.setExpectMin(basePotentialIssueStatus.getExpectMin());
          potentialIssueStatus.setExpectMax(basePotentialIssueStatus.getExpectMax());
          session.update(potentialIssueStatus);
        } else
        {
          session.delete(potentialIssueStatus);
        }
      }
      tx.commit();

      tx = session.beginTransaction();
      query = session.createQuery("from CodeReceived where profile = ? ");
      query.setParameter(0, profile);
      List<CodeReceived> codeReceivedList = query.list();
      for (CodeReceived codeReceived : codeReceivedList)
      {
        query = session.createQuery("from CodeReceived where profile = ? and table = ? and contextValue = ? and upper(receivedValue) = ?");
        query.setParameter(0, baseProfile);
        query.setParameter(1, codeReceived.getTable());
        query.setParameter(2, codeReceived.getContextValue());
        query.setParameter(3, codeReceived.getReceivedValue().toUpperCase());
        List<CodeReceived> baseCodeReceivedList = query.list();
        if (baseCodeReceivedList.size() > 0)
        {
          CodeReceived baseCodeReceived = baseCodeReceivedList.get(0);
          codeReceived.setCodeValue(baseCodeReceived.getCodeValue());
          codeReceived.setCodeStatus(baseCodeReceived.getCodeStatus());
          session.update(baseCodeReceived);
        }
      }
      tx.commit();

    }
  }
}
