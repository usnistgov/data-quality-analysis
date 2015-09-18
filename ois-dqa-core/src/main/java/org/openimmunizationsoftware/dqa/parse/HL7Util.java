/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.parse;

import static org.openimmunizationsoftware.dqa.parse.HL7Util.escapeHL7Chars;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openimmunizationsoftware.dqa.SoftwareVersion;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;

public class HL7Util
{
  public static final String MESSAGE_TYPE_VXU = "VXU";
  public static final String MESSAGE_TYPE_QBP = "QBP";

  public static final String ACK_ERROR = "AE";
  public static final String ACK_ACCEPT = "AA";
  public static final String ACK_REJECT = "AR";

  public static final String SEVERITY_ERROR = "E";
  public static final String SEVERITY_WARNING = "W";
  public static final String SEVERITY_INFORMATION = "I";

  public static final String PROCESSING_ID_DEBUGGING = "D";
  public static final String PROCESSING_ID_PRODUCTION = "P";
  public static final String PROCESSING_ID_TRAINING = "T";

  public static final String QUERY_RESULT_NO_MATCHES = "Z34";
  public static final String QUERY_RESULT_LIST_OF_CANDIDATES = "Z31";
  public static final String QUERY_RESULT_IMMUNIZATION_HISTORY = "Z32";

  public static final String QUERY_RESPONSE_TYPE = "RSP^K11^RSP_K11";

  public static final int BAR = 0;
  public static final int CAR = 1;
  public static final int TIL = 2;
  public static final int SLA = 3;
  public static final int AMP = 4;

  private static int ackCount = 1;

  public static synchronized int getNextAckCount()
  {
    if (ackCount == Integer.MAX_VALUE)
    {
      ackCount = 1;
    }
    return ackCount++;
  }

  public static boolean setupSeparators(String messageText, char[] separators)
  {
    if (messageText.startsWith("MSH") && messageText.length() > 10)
    {
      separators[BAR] = messageText.charAt(BAR + 3);
      separators[CAR] = messageText.charAt(CAR + 3);
      separators[TIL] = messageText.charAt(TIL + 3);
      separators[SLA] = messageText.charAt(SLA + 3);
      separators[AMP] = messageText.charAt(AMP + 3);
      return true;
    } else
    {
      setDefault(separators);
      return false;
    }
  }

  public static void setDefault(char[] separators)
  {
    separators[BAR] = '|';
    separators[CAR] = '^';
    separators[TIL] = '~';
    separators[SLA] = '\\';
    separators[AMP] = '&';
  }

  public static boolean checkSeparatorsAreValid(char[] separators)
  {
    boolean unique = true;
    // Make sure separators are unique for each other
    for (int i = 0; i < separators.length; i++)
    {
      for (int j = i + 1; j < separators.length; j++)
      {
        if (separators[i] == separators[j])
        {
          unique = false;
          break;
        }
      }
    }
    return unique;
  }

  public static String makeAckMessage(String ackType, String severityLevel, String message, PreParseMessageExaminer ppme, PotentialIssue pi)
  {
    StringBuilder ack = new StringBuilder();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
    String messageDate = sdf.format(new Date());
    // MSH
    ack.append("MSH|^~\\&");
    ack.append("|" + ppme.getSendingApplication()); // MSH-3 Sending Application
    ack.append("|" + ppme.getSendingFacility()); // MSH-4 Sending Facility
    ack.append("|" + ppme.getReceivingApplication()); // MSH-5 Receiving
                                                      // Application
    ack.append("|" + ppme.getReceivingFacility()); // MSH-6 Receiving Facility
    ack.append("|" + messageDate); // MSH-7 Date/Time of Message
    ack.append("|"); // MSH-8 Security
    ack.append("|ACK"); // MSH-9
    // Message
    // Type
    ack.append("|" + messageDate + "." + getNextAckCount()); // MSH-10 Message
                                                             // Control ID
    ack.append("|P"); // MSH-11 Processing ID
    ack.append("|2.5.1"); // MSH-12 Version ID
    ack.append("|\r");
    ack.append("SFT|" + SoftwareVersion.VENDOR + "|" + SoftwareVersion.VERSION + "|" + SoftwareVersion.PRODUCT + "|" + SoftwareVersion.BINARY_ID
        + "|\r");
    ack.append("MSA|" + ackType + "|" + ppme.getMessageKey() + "|\r");
    
    makeERRSegment(ack, severityLevel, "", message, pi);

    return ack.toString();

  }

  public static void appendErrorCode(StringBuilder ack, String hl7ErrorCode)
  {
    if (hl7ErrorCode != null)
    {
      ack.append(hl7ErrorCode);
      if (hl7ErrorCode.equals("0"))
      {
        ack.append("^Message accepted");
      } else if (hl7ErrorCode.equals("100"))
      {
        ack.append("^Segment sequence error");
      } else if (hl7ErrorCode.equals("101"))
      {
        ack.append("^Required field missing");
      } else if (hl7ErrorCode.equals("102"))
      {
        ack.append("^Data type error");
      } else if (hl7ErrorCode.equals("103"))
      {
        ack.append("^Table value not found");
      } else if (hl7ErrorCode.equals("200"))
      {
        ack.append("^Unsupported message type");
      } else if (hl7ErrorCode.equals("201"))
      {
        ack.append("^Unsupported event code");
      } else if (hl7ErrorCode.equals("202"))
      {
        ack.append("^Unsupported processing ID");
      } else if (hl7ErrorCode.equals("203"))
      {
        ack.append("^Unsupported version ID");
      } else if (hl7ErrorCode.equals("204"))
      {
        ack.append("^Unknown key identifier");
      } else if (hl7ErrorCode.equals("205"))
      {
        ack.append("^Duplicate key identifier");
      } else if (hl7ErrorCode.equals("206"))
      {
        ack.append("^Application record locked");
      } else if (hl7ErrorCode.equals("207"))
      {
        ack.append("^Application internal error");
      } else
      {
        ack.append("^");
      }
      ack.append("^HL70357");
    }
  }

  public static void makeERRSegment(StringBuilder ack, IssueFound issueFound)
  {
    PotentialIssue issue = issueFound.getIssue();
    String hl7ReferenceOrig = issue.getHl7Reference();
    String[] hl7ReferenceParts;
    if (hl7ReferenceOrig == null || hl7ReferenceOrig.equals(""))
    {
      hl7ReferenceParts = new String[] { "" };
    } else
    {
      hl7ReferenceParts = hl7ReferenceOrig.split("\\/");
    }
    for (String hl7Reference : hl7ReferenceParts)
    {
      String hl7ErrorCode = issue.getHl7ErrorCode();
      String severity = "I";

      if (issueFound.isError())
      {
        severity = "E";
      } else if (issueFound.isWarn())
      {
        severity = "W";
      } else if (issueFound.isSkip())
      {
        severity = "I";
      }
      ack.append("ERR||");
      // 2 Error Location
      printErr3(ack, issueFound, hl7Reference);
      ack.append("|");
      // 3 HL7 Error Code
      if (hl7ErrorCode != null)
      {
        HL7Util.appendErrorCode(ack, hl7ErrorCode);
      } else
      {
        HL7Util.appendErrorCode(ack, "0");
      }
      ack.append("|");
      // 4 Severity
      ack.append(severity);
      ack.append("|");
      // 5 Application Error Code
      appendAppErrorCode(ack, issueFound.getIssue());
      ack.append("|");
      // 6 Application Error Parameter
      ack.append("|");
      // 7 Diagnostic Information
      ack.append(escapeHL7Chars(issue.getIssueDescription()));
      if (issueFound.isSkip())
      {
        ack.append(" (This information was skipped, and not accepted)");
      }
      // 8 User Message
      ack.append("|");
      ack.append(escapeHL7Chars(issueFound.getDisplayText()));
      ack.append("|\r");
    }

  }





  public static void appendAppErrorCode(StringBuilder ack, PotentialIssue pi)
  {
    if (pi != null)
    {
      if (pi.getAppErrorCode() != null)
      {
        ack.append(pi.getAppErrorCode());
        ack.append("^");
        if (pi.getAppErrorCode().equals("1"))
        {
          ack.append("Illogical Date error");
        } else if (pi.getAppErrorCode().equals("2"))
        {
          ack.append("Invalid Date");
        } else if (pi.getAppErrorCode().equals("3"))
        {
          ack.append("Illogical Value error");
        } else if (pi.getAppErrorCode().equals("4"))
        {
          ack.append("Invalid value");
        } else if (pi.getAppErrorCode().equals("5"))
        {
          ack.append("Table value not found");
        } else if (pi.getAppErrorCode().equals("6"))
        {
          ack.append("Required observation missing");
        } else if (pi.getAppErrorCode().equals("7"))
        {
          ack.append("Required data missing");
        } else if (pi.getAppErrorCode().equals("8"))
        {
          ack.append("Data was ignored");
        }
        ack.append("^HL70533");

      } else
      {
        ack.append("^^^");
      }
      ack.append("DQA" + padZeros(pi.getIssueId()));
      ack.append("^");
      ack.append(pi.getDisplayText());
      ack.append("^HL70533");
    }

  }

  private static String padZeros(int i)
  {
    String s = "0000" + i;
    return s.substring(s.length() - 4);
  }

  private static void printErr3(StringBuilder ack, IssueFound issueFound, String hl7Reference)
  {
    int pos = hl7Reference.indexOf("-");
    if (pos == -1)
    {
      pos = hl7Reference.length();
    }
    if (pos > 0 && (pos + 1) <= hl7Reference.length())
    {
      ack.append(hl7Reference.substring(0, pos));
      hl7Reference = hl7Reference.substring(pos + 1);
      ack.append("^");
      ack.append(issueFound.getPositionId());
      if (hl7Reference.length() > 0)
      {
        ack.append("^");
        pos = hl7Reference.indexOf("\\.");
        if (pos == -1)
        {
          ack.append(hl7Reference);
          ack.append("^1");
        } else
        {
          ack.append(hl7Reference.substring(0, pos));
          ack.append("^1^");
          ack.append(hl7Reference.substring(pos + 1));
        }
      }
    }
  }

  public static void makeERRSegment(StringBuilder ack, String severity, String hl7ErrorCode, String textMessage, PotentialIssue pi)
  {

    if (severity.equals("E") && hl7ErrorCode.equals(""))
    {
      hl7ErrorCode = "102";
    }
    ack.append("ERR||");
    // 2 Error Location
    ack.append("|");
    // 3 HL7 Error Code
    HL7Util.appendErrorCode(ack, hl7ErrorCode);
    ack.append("|");
    // 4 Severity
    ack.append(severity);
    ack.append("|");
    // 5 Application Error Code
    HL7Util.appendAppErrorCode(ack, pi);
    ack.append("|");
    // 6 Application Error Parameter
    ack.append("|");
    // 7 Diagnostic Information
    ack.append("|");
    // 8 User Message
    ack.append(escapeHL7Chars(textMessage));
    ack.append("|\r");

  }

  public static String escapeHL7Chars(String s)
  {
	  if (s == null)
	  {
		  return "";
	  }
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray())
    {
      if (c >= ' ')
      {
        switch (c) {
        case '~':
          sb.append("\\R\\");
          break;
        case '\\':
          sb.append("\\E\\");
          break;
        case '|':
          sb.append("\\F\\");
          break;
        case '^':
          sb.append("\\S\\");
          break;
        case '&':
          sb.append("\\T\\");
          break;
        default:
          sb.append(c);
        }
      }
    }
    return sb.toString();
  }

}
