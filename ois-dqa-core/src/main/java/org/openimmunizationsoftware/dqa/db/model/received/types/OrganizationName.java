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

public class OrganizationName extends Extracted
{
  private String name = "";
  private Id id = new Id(CodeTable.Type.ORGANIZATION);
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
	this.put("name", Locator.getPath());
    this.name = name;
  }
  public String getIdNumber()
  {
	
    return id.getNumber();
  }
  public Id getId()
  {
    return id;
  }
  public void setIdNumber(String idNumber)
  {
	  this.put("idNumber", Locator.getPath());  
    this.id.setNumber(idNumber);
  }
}
