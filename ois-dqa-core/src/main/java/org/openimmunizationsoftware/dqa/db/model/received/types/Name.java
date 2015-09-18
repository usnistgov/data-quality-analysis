/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model.received.types;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.nist.Extracted;
import org.openimmunizationsoftware.dqa.nist.Locator;

public class Name extends Extracted
{
  private String last = "";
  private String first = "";
  private String middle = "";
  private String suffix = "";
  private String prefix = "";
  private CodedEntity type = new CodedEntity(CodeTable.Type.PERSON_NAME_TYPE);
  
  public String getLast()
  {
    return last;
  }

  public void setLast(String last)
  {
    this.put("last", Locator.getPath());
    this.last = last;
  }

  public String getFirst()
  {
    return first;
  }

  public void setFirst(String first)
  {
	this.put("first", Locator.getPath());  
    this.first = first;
  }

  public String getMiddle()
  {
    return middle;
  }

  public void setMiddle(String middle)
  {
	  this.put("middle", Locator.getPath());
    this.middle = middle;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix(String suffix)
  {
	  this.put("suffix", Locator.getPath());
    this.suffix = suffix;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
	  this.put("prefix", Locator.getPath());
    this.prefix = prefix;
  }

  public CodedEntity getType()
  {
    return type;
  }

  public String getTypeCode()
  {
    return type.getCode();
  }

  public void setTypeCode(String typeCode)
  {
    this.type.setCode(typeCode);
  }

  public String getFullName()
  {
    StringBuilder sb = new StringBuilder();
    if (prefix != null && !prefix.equals(""))
    {
      sb.append(prefix);
      sb.append(" ");
    }
    if (first != null && !first.equals(""))
    {
      sb.append(first);
      sb.append(" ");
    }
    if (middle != null && !middle.equals(""))
    {
      sb.append(middle);
      sb.append(" ");
    }
    if (last != null && !last.equals(""))
    {
      sb.append(last);
      sb.append(" ");
    }
    if (suffix != null && !suffix.equals(""))
    {
      sb.append(suffix);
      sb.append(" ");
    }
    return sb.toString().trim();
  }
}
