/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openimmunizationsoftware.dqa.InitializationException;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;
import org.openimmunizationsoftware.dqa.quality.ReportDenominator;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModelFactory
{

  private static ModelForm modelFormDefault = null;

  public static ModelForm getModelFormDefault()
  {
    if (modelFormDefault == null)
    {
      try
      {
        modelFormDefault = createModelForm("modelForm.xml");
      } catch (Exception e)
      {
        throw new InitializationException("Unable to instantiate default model form", e);
      }
    }
    return modelFormDefault;
  }

  public static ModelForm createModelForm(SubmitterProfile profile)
  {
    try
    {
      ModelForm modelForm = new ModelForm();
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      String text = profile.getReportTemplate().getReportDefinition();
      Document doc = docBuilder.parse(new ByteArrayInputStream(text.getBytes()));
      doc.getDocumentElement().normalize();
      NodeList nodes = doc.getChildNodes();
      if (nodes.getLength() == 1)
      {
        addSections(modelForm, nodes.item(0).getChildNodes());
      }
      return modelForm;
    } catch (Exception e)
    {
      throw new IllegalArgumentException("Unable to create data quality model definition", e);
    }
  }

  public static ModelForm createModelForm(String resourceName) throws SAXException, IOException,
      ParserConfigurationException
  {
    ModelForm modelForm = new ModelForm();
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(modelForm.getClass().getResourceAsStream(resourceName));
    doc.getDocumentElement().normalize();
    NodeList nodes = doc.getChildNodes();
    if (nodes.getLength() == 1)
    {
      addSections(modelForm, nodes.item(0).getChildNodes());
    }
    return modelForm;
  }

  private static void addSections(ModelSection rootSection, NodeList nodes)
  {
    PotentialIssues pi = PotentialIssues.getPotentialIssues();
    float totalWeight = 0.0f;
    float firstWeight = 0.0f;
    for (int i = 0; i < nodes.getLength(); i++)
    {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
        NamedNodeMap map = node.getAttributes();
        if ("section".equals(node.getNodeName()))
        {
          ModelSection section = new ModelSection();
          section.setName(safe(map.getNamedItem("name")));
          section.setWeight(safeFloat(map.getNamedItem("weight")));
          section.setDays(safeInt(map.getNamedItem("days")));
          if (section.getWeight() > 0)
          {
            totalWeight += section.getWeight();
          }
          if (firstWeight == 0.0f)
          {
            firstWeight = section.getWeight();
          }
          rootSection.getSections().add(section);
          addSections(section, node.getChildNodes());
        } else if ("score".equals(node.getNodeName()))
        {
          ModelScore score = new ModelScore();
          score.setLabel(safe(map.getNamedItem("label")));
          score.setHl7Reference(safe(map.getNamedItem("hl7Reference")));
          String num = safe(map.getNamedItem("numerator"));
          score.setNumerator(num);
          String den = safe(map.getNamedItem("denominator"));
          String invert = safe(map.getNamedItem("invert")).toUpperCase();
          score.setInvert(invert.startsWith("Y") || invert.startsWith("T"));
          try
          {
            score.setDenominator(ReportDenominator.valueOf(den.toUpperCase().trim().replace(' ', '_')));
          } catch (IllegalArgumentException iae)
          {
            throw new IllegalArgumentException("Unrecognized denonminator of '" + den + "' where label = '"
                + score.getLabel() + "'", iae);
          }
          score.setWeight(safeFloat(map.getNamedItem("weight")));
          PotentialIssue potentialIssue = pi.getPotentialIssueByDisplayText(num);
          if (potentialIssue == null)
          {
            potentialIssue = pi.getPotentialIssueByDisplayText(num, PotentialIssue.ISSUE_TYPE_IS_MISSING);
            if (potentialIssue != null)
            {
              score.setInvert(true);
              addDemerit(pi, score, PotentialIssue.ISSUE_TYPE_IS_DEPRECATE, "Deprecated");
              addDemerit(pi, score, PotentialIssue.ISSUE_TYPE_IS_INVALID, "Invalid");
              addDemerit(pi, score, PotentialIssue.ISSUE_TYPE_IS_UNRECOGNIZED, "Unrecognized");
            }
          }
          if (potentialIssue == null)
          {
            throw new IllegalArgumentException("Score numerator '" + score.getNumerator()
                + "' is not recognized where label = '" + score.getLabel() + "'");
          }
          score.setPotentialIssue(potentialIssue);
          if (score.getWeight() > 0)
          {
            totalWeight += score.getWeight();
          }
          rootSection.getScores().add(score);
          totalWeight += addSubScores(node, map, score.getScores());
        }
      }
    }
    if (rootSection.getName().equals("timeliness"))
    {
      totalWeight = firstWeight;
    }
    if (totalWeight > 0)
    {
      // normalize weights to standard percentage
      for (ModelSection section : rootSection.getSections())
      {
        section.setWeight(section.getWeight() / totalWeight);
      }
      for (ModelScore score : rootSection.getScores())
      {
        score.setWeight(score.getWeight() / totalWeight);
        for (ModelScore childScore : score.getScores())
        {
          childScore.setWeight(childScore.getWeight() / totalWeight);
        }
      }
    }
  }

  private static void addDemerit(PotentialIssues pi, ModelScore score, String issueType, String label)
  {
    PotentialIssue demerit;
    demerit = pi.getPotentialIssueByDisplayText(score.getNumerator() + " " + issueType);
    if (demerit != null)
    {
      ModelScore subScore = new ModelScore();
      subScore.setLabel(label);
      subScore.setNumerator(score.getNumerator());
      subScore.setPotentialIssue(demerit);
      subScore.setDenominator(score.getDenominator());
      subScore.setWeight(-score.getWeight());
      score.getScores().add(subScore);
    }
  }

  private static float addSubScores(Node node, NamedNodeMap map, List<ModelScore> scores)
  {
    PotentialIssues pi = PotentialIssues.getPotentialIssues();
    float totalWeight = 0.0f;
    NodeList chldNodes = node.getChildNodes();
    for (int i = 0; i < chldNodes.getLength(); i++)
    {
      Node childNode = chldNodes.item(i);
      if (childNode.getNodeType() == Node.ELEMENT_NODE)
      {
        NamedNodeMap childMap = childNode.getAttributes();
        if ("score".equals(childNode.getNodeName()))
        {
          ModelScore score = new ModelScore();
          score.setLabel(safe(childMap.getNamedItem("label")));
          score.setHl7Reference(safe(map.getNamedItem("hl7Reference")));
          score.setNumerator(safe(childMap.getNamedItem("numerator")));
          String den = safe(map.getNamedItem("denominator"));
          try
          {
            score.setDenominator(ReportDenominator.valueOf(den.toUpperCase().trim().replace(' ', '_')));
          } catch (IllegalArgumentException iae)
          {
            throw new IllegalArgumentException("Unrecognized denonminator of '" + den + "' where label = '"
                + score.getLabel() + "'", iae);
          }
          score.setWeight(safeFloat(childMap.getNamedItem("weight")));
          PotentialIssue potentialIssue = pi.getPotentialIssueByDisplayText(score.getNumerator());
          if (potentialIssue == null)
          {
            potentialIssue = pi.getPotentialIssueByDisplayText(score.getNumerator(),
                PotentialIssue.ISSUE_TYPE_IS_MISSING);
            if (potentialIssue == null)
            {
              throw new IllegalArgumentException("Score numerator '" + score.getNumerator()
                  + "' is not recognized where label = '" + score.getLabel() + "'");
            }
            score.setInvert(true);
          }
          score.setPotentialIssue(potentialIssue);
          if (score.getWeight() > 0)
          {
            totalWeight += score.getWeight();
          }
          scores.add(score);
        }
      }
    }
    return totalWeight;
  }

  private static String safe(Node n)
  {
    if (n == null)
    {
      return "";
    }
    String s = n.getNodeValue();
    if (s == null)
    {
      return "";
    }
    return s;
  }

  private static float safeFloat(Node n)
  {
    if (n == null)
    {
      return 0.0f;
    }
    String s = n.getNodeValue();
    if (s == null || s.equals(""))
    {
      return 0.0f;
    }
    try
    {
      return Float.parseFloat(s.trim());
    } catch (NumberFormatException nf)
    {
      return 0.0f;
    }
  }

  private static int safeInt(Node n)
  {
    if (n == null)
    {
      return 0;
    }
    String s = n.getNodeValue();
    if (s == null || s.equals(""))
    {
      return 0;
    }
    try
    {
      return Integer.parseInt(s.trim());
    } catch (NumberFormatException nf)
    {
      return 0;
    }
  }
}
