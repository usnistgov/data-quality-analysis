/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.quality.model;

import java.util.ArrayList;
import java.util.List;

public class ModelSection
{
  private int days = 0;
  private String name = "";
  private List<ModelScore> scores = new ArrayList<ModelScore>();
  private List<ModelSection> sections = new ArrayList<ModelSection>();
  private float weight = 0;

  public float getAbsoluteWeight(String path)
  {
    String firstPart = path;
    int pos = path.indexOf(".");
    if (pos == -1)
    {
      path = "";
    }
    else 
    {
      firstPart = path.substring(0, pos);
      path = path.substring(pos + 1);
    }
    for (ModelSection section : sections)
    {
      if (section.getName().equalsIgnoreCase(firstPart))
      {
        if (path.length() == 0)
        {
          return section.getWeight();
        }
        else
        {
          float absWeight = section.getAbsoluteWeight(path);
          float weight = section.getWeight();
          return weight * absWeight;
        }
      }
    }
    throw new IllegalArgumentException("Unable to find section named " + firstPart);
  }

  public int getDays()
  {
    return days;
  }
  
  

  public ModelSection getModelSection(String path)
  {
    String firstPart = path;
    int pos = path.indexOf(".");
    if (pos == -1)
    {
      path = "";
    }
    else 
    {
      firstPart = path.substring(0, pos);
      path = path.substring(pos + 1);
    }
    for (ModelSection section : sections)
    {
      if (section.getName().equalsIgnoreCase(firstPart))
      {
        if (path.length() == 0)
        {
          return section;          
        }
        else
        {
          return section.getModelSection(path);
        }
      }
    }
    throw new IllegalArgumentException("Unable to find section named " + firstPart);
  }

  public String getName()
  {
    return name;
  }

  public List<ModelScore> getScores()
  {
    return scores;
  }

  public List<ModelSection> getSections()
  {
    return sections;
  }

  public float getWeight()
  {
    return weight;
  }

  public float getWeight(String path)
  {
    return getModelSection(path).getWeight();
  }
  
  public void setDays(int days)
  {
    this.days = days;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public void setWeight(float weight)
  {
    this.weight = weight;
  }
}
