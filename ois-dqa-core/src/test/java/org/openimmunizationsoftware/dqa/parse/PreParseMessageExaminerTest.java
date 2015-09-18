package org.openimmunizationsoftware.dqa.parse;

import junit.framework.TestCase;

public class PreParseMessageExaminerTest extends TestCase
{
  public void testConstructor()
  {
    PreParseMessageExaminer ppme;
    ppme = new PreParseMessageExaminer("");
    assertFalse(ppme.isHL7v2());
    
    ppme = new PreParseMessageExaminer("Hi!");
    assertFalse(ppme.isHL7v2());
    
    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST");
    assertTrue(ppme.isHL7v2());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());
    
    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU |MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944|| VXU|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||vxu|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||^vxu|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU~ADT|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU&HI|MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944|||MCIR3943959000|P|2.5.1|\rPID|1||MCIR3943959000^^^OIS-TEST^MR||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\rPD1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("", ppme.getMessageType());

    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());
    assertEquals("MCIR3943959000", ppme.getMessageKey());
    
    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());
    assertEquals("MCIR3943959000", ppme.getMessageKey());
    
    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000^");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());
    assertEquals("MCIR3943959000", ppme.getMessageKey());
    
    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000~");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());
    assertEquals("MCIR3943959000", ppme.getMessageKey());
    
    ppme = new PreParseMessageExaminer("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000 |P|2.5.1|");
    assertTrue(ppme.isHL7v2());
    assertEquals("VXU", ppme.getMessageType());
    assertEquals("MCIR3943959000", ppme.getMessageKey());
    

  }
}
