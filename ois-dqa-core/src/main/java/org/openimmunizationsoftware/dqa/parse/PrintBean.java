/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;

public class PrintBean
{
  public static List<String> print(Object object, PrintWriter out) throws IllegalAccessException, InvocationTargetException
  {
    return print(object, "  ", out);
  }
  
  public static List<String> print(Object object, String indent, PrintWriter out) throws IllegalAccessException, InvocationTargetException
  {
    List<String> thisPrinted = new ArrayList<String>();
    List<String> subPrinted = new ArrayList<String>();
    Method[] methods = object.getClass().getMethods();
    Arrays.sort(methods, new Comparator<Method>() {
      public int compare(Method o1, Method o2)
      {
        return o1.getName().compareTo(o2.getName());
      }
    });
    for (Method method : methods)
    {
      if (method.getName().startsWith("get") && !method.getName().equals("getClass")
          && !method.getName().equals("getMessageReceived") && !method.getName().equals("getProfile")
          && !method.getName().equals("getRequestText") && !method.getName().equals("getResponseText")
          && !method.getName().equals("getIssuesFound") && !method.getReturnType().equals(Void.TYPE)
          && method.getParameterTypes().length == 0)
      {
        Object returnValue = method.invoke(object);
        String fieldName = method.getName().substring(3);
        if (returnValue == null || subPrinted.contains(fieldName))
        {
          // do nothing
        } else if (method.getReturnType() == String.class)
        {
          if (!((String) returnValue).equals(""))
          {
            out.print(indent);
            out.print(fieldName);
            out.print(" = '");
            out.print(returnValue);
            out.print("'");
            out.print("\r");
            thisPrinted.add(fieldName);
          }
        } else if (method.getReturnType() == Date.class)
        {
          out.print(indent);
          out.print(fieldName);
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
          out.print(" = ");
          out.print(sdf.format((Date) returnValue));
          out.print("\r");
          thisPrinted.add(fieldName);
        } else if (method.getReturnType() == List.class)
        {
          List list = (List) returnValue;
          for (int i = 0; i < list.size(); i++)
          {
            out.print(indent);
            out.print(fieldName);
            out.print(" #");
            out.print(i + 1);
            out.print("\r");
            print(list.get(i), indent + "  ", out);
          }
        } else if (method.getReturnType() == CodeTable.Type.class)
        {
          // don't process
        } else
        {
          out.print(indent);
          out.print(fieldName);
          out.print("\r");
          List<String> returnPrints = print(returnValue, indent + "  ", out);
          for (String returnPrint : returnPrints)
          {
            subPrinted.add(fieldName + returnPrint);
          }
        }
      }
    }
    return thisPrinted;
  }


}
