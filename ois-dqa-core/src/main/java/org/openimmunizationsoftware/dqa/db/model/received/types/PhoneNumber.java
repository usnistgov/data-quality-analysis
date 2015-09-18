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

public class PhoneNumber extends Extracted
{
  private String number = "";
  private CodedEntity telUse = new CodedEntity(CodeTable.Type.TELECOMMUNICATION_USE);
  private CodedEntity telEquip = new CodedEntity(CodeTable.Type.TELECOMMUNICATION_EQUIPMENT);
  private String email = "";
  private String countryCode = "";
  private String areaCode = "";
  private String localNumber = "";
  private String extension = "";
  
  public PhoneNumber()
  {
    // default
  }
  
  public PhoneNumber(String phoneNumberString)
  {
    setNumber(phoneNumberString);
  }
  
  public PhoneNumber(String areaCode, String localNumber)
  {
    setAreaCode(areaCode);
    setLocalNumber(localNumber);
  }

  public String getNumber()
  {
    if (number == null || number.equals(""))
    {
      if (localNumber != null && !localNumber.equals(""))
      {
        StringBuilder sb = new StringBuilder();
        if (areaCode != null && !areaCode.equals(""))
        {
          sb.append("(");
          sb.append(areaCode);
          sb.append(")");
        }
        if (localNumber != null && !localNumber.equals(""))

        {
          if (localNumber.length() == 7)
          {
            sb.append(localNumber.substring(0, 3));
            sb.append("-");
            sb.append(localNumber.substring(3, 7));
          } else
          {
            sb.append(localNumber);
          }
        }
        return sb.toString();
      }
      return "";
    }
    return number;
  }

  public void setNumber(String number)
  {
	this.put("number", Locator.getPath());  
    this.number = number;
    if ((number != null && !number.equals("")) && (localNumber == null || localNumber.equals("")))
    {
      if (areaCode == null || areaCode.equals(""))
      {
        StringBuilder justDigits = new StringBuilder();
        for (char c : number.toCharArray())
        {
          if (c >= '0' && c <= '9')
          {
            justDigits.append(c);
          }
        }
        if (justDigits.length() == 7)
        {
          this.localNumber = justDigits.toString();
        } else if (justDigits.length() == 10)
        {
          this.areaCode = justDigits.toString().substring(0, 3);
          this.localNumber = justDigits.toString().substring(3, 10);
        }
      }
    }
  }

  public CodedEntity getTelUse()
  {
    return telUse;
  }

  public String getTelUseCode()
  {
    return telUse.getCode();
  }

  public void setTelUseCode(String telUseCode)
  {
    this.telUse.setCode(telUseCode);
  }

  public String getTelEquipCode()
  {
    return telEquip.getCode();
  }

  public CodedEntity getTelEquip()
  {
    return telEquip;
  }

  public void setTelEquipCode(String telEquipCode)
  {
    this.telEquip.setCode(telEquipCode);
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
	this.put("email", Locator.getPath());  
    this.email = email;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode(String countryCode)
  {
	this.put("countryCode", Locator.getPath());  
    this.countryCode = countryCode;
  }

  public String getAreaCode()
  {
    return areaCode;
  }

  public void setAreaCode(String areaCode)
  {
	this.put("areaCode", Locator.getPath());  
    this.areaCode = areaCode;
  }

  public String getLocalNumber()
  {
    return localNumber;
  }

  public void setLocalNumber(String localNumber)
  {
	this.put("localNumber", Locator.getPath());  
    this.localNumber = localNumber;
  }

  public String getExtension()
  {
    return extension;
  }

  public void setExtension(String extension)
  {
	this.put("extension", Locator.getPath());  
    this.extension = extension;
  }
}
