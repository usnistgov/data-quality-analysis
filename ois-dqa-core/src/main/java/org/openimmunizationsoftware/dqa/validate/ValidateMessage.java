/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.validate;

import java.io.PrintWriter;
import java.util.List;

import org.openimmunizationsoftware.dqa.db.model.CodeReceived;
import org.openimmunizationsoftware.dqa.db.model.IssueAction;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.QueryReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.db.model.received.NextOfKin;
import org.openimmunizationsoftware.dqa.db.model.received.Patient;
import org.openimmunizationsoftware.dqa.db.model.received.Skippable;
import org.openimmunizationsoftware.dqa.db.model.received.Vaccination;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;
import org.openimmunizationsoftware.dqa.nist.Locator;
import org.openimmunizationsoftware.dqa.quality.QualityCollector;

public abstract class ValidateMessage
{

  protected MessageReceived message = null;
  protected QualityCollector qualityCollector = null;
  protected SubmitterProfile profile = null;
  protected Patient patient = null;
  protected Vaccination vaccination = null;
  protected NextOfKin nextOfKin = null;
  protected Skippable skippableItem = null;
  protected PotentialIssues pi = PotentialIssues.getPotentialIssues();
  protected int positionId = 0;
  protected List<IssueFound> issuesFound = null;
  protected String segmentName;
  protected PrintWriter documentOut = null;

  public boolean hasIssue(PotentialIssue pi)
  {
    return getIssueFoundFirst(pi) != null;
  }
  public IssueFound getIssueFoundFirst(PotentialIssue pi)
  {
    for (IssueFound issueFound : issuesFound)
    {
      if (issueFound.getIssue() == pi)
      {
        return issueFound;
      }
    }
    return null;
  }

  public void document(String s)
  {
    if (documentOut != null)
    {
      documentOut.print(s);
    }
  }

  public void documentHeaderSub(String s)
  {
    if (documentOut != null)
    {
      documentOut.print("<h5>");
      documentOut.print(s);
      documentOut.println("</h5>");
    }
  }

  public void documentHeaderMain(String s)
  {
    if (documentOut != null)
    {
      documentOut.print("<h4>");
      documentOut.print(s);
      documentOut.println("</h4>");
    }
  }

  public void documentValuesFound(String... values)
  {
    if (documentOut != null)
    {
      boolean headerPrinted = false;
      for (int i = 1; i < values.length; i = i + 2)
      {
        if (!headerPrinted)
        {
          documentOut.println("<table>");
          documentOut.println("  <tr>");
          documentOut.println("    <th>Field</th>");
          documentOut.println("    <th>Value</th>");
          documentOut.println("  </tr>");
          headerPrinted = true;
        }
        documentOut.println("  <tr>");
        documentOut.println("    <td>" + values[i - 1] + "</td>");
        if (values[i] == null || values[i].length() == 0)
        {
          documentOut.println("    <td>&nbsp;</td>");
        } else
        {
          documentOut.println("    <td>" + values[i] + "</td>");

        }
        documentOut.println("  </tr>");
      }
      if (headerPrinted)
      {
        documentOut.println("</table>");
      }
    }
  }

  public void documentParagraph(String s)
  {
    if (documentOut != null)
    {
      documentOut.println("<p>");
      documentOut.println(s);
      documentOut.println("</p>");
    }
  }

  public void documentIssueFound(IssueFound issueFound)
  {
    if (documentOut != null)
    {
      IssueAction action = issueFound.getIssueAction();
      String cssClass = "";
      if (issueFound.isError())
      {
        cssClass = "problem";
      } else if (issueFound.isWarn())
      {
        cssClass = "poor";
      } else if (issueFound.isSkip())
      {
        cssClass = "poor";
      }
      documentOut.println("<p class=\"" + cssClass + "\">");
      if (!action.isAccept())
      {
        documentOut.print(action.getActionLabel() + ": ");
      }
      documentOut.println(issueFound.getDisplayText());
      documentOut.println("</p>");
    }
  }

  public boolean isDocument()
  {
    return documentOut != null;
  }

  public void setDocumentOut(PrintWriter documentOut)
  {
    this.documentOut = documentOut;
  }

  protected ValidateMessage(SubmitterProfile profile) {
    this.profile = profile;
  }

  protected void registerError(PotentialIssue potentialIssue,String where)
  {
    registerIssue(potentialIssue, IssueAction.ERROR,where);
  }

  protected void registerIssue(PotentialIssue potentialIssue,String where)
  {
    if (potentialIssue != null)
    {
      registerIssue(potentialIssue, profile.getPotentialIssueStatus(potentialIssue).getAction(), null, where);
    }
  }

  protected void registerIssue(PotentialIssue potentialIssue, CodeReceived codeReceived,String where)
  {
    if (potentialIssue != null)
    {
      registerIssue(potentialIssue, profile.getPotentialIssueStatus(potentialIssue).getAction(), codeReceived,where);
    }
  }

  protected void registerIssue(PotentialIssue potentialIssue, IssueAction issueAction,String where)
  {
    registerIssue(potentialIssue, issueAction, null,where);
  }

  protected void registerIssue(PotentialIssue potentialIssue, IssueAction issueAction, CodeReceived codeReceived,String where)
  {
    if (potentialIssue != null)
    {
      IssueFound issueFound = new IssueFound();
      issueFound.setMessageReceived(message);
      issueFound.setPositionId(positionId);
      issueFound.setIssue(potentialIssue);
      issueFound.setIssueAction(issueAction);
      issueFound.setCodeReceived(codeReceived);
      potentialIssue.setDraft(where);
      if (issuesFound != null)
      {
        issuesFound.add(issueFound);
      }
      if (issueAction.isSkip() && skippableItem != null)
      {
        skippableItem.setSkipped(true);
      }
      documentIssueFound(issueFound);
    }
  }

}
