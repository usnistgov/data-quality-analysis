package org.openimmunizationsoftware.dqa.nist;

public class Locator {
	public static boolean inParse = true;
	public static String segName = "";
	public static int segInst = 1;
	public static int fieldNum = -1;
	public static int fieldInst = 1;
	public static int compNum = -1;
	public static boolean empty = true;
	
	public static void init(){
		segName = "";
		segInst = 1;
		fieldNum = -1;
		fieldInst = 1;
		compNum = -1;
		empty = true;
	}
	
	public static void setName(String name)
	{
		segName = name;
		segInst = 1;
		fieldNum = -1;
		fieldInst = 1;
		compNum = -1;
		empty = false;
	}
	
	public static void setField(int f)
	{
		fieldNum = f;
		fieldInst = 1;
		compNum = -1;
		empty = false;
	}
	
	public static void setCmp(int c)
	{
		compNum = c;
		empty = false;
	}
	
	public static void incSegment(){
		segInst++;
		fieldNum = -1;
		fieldInst = 1;
		compNum = -1;
		empty = false;
	}
	
	public static void decSegment(){
		segInst--;
		fieldNum = -1;
		fieldInst = 1;
		compNum = -1;
		empty = false;
	}
	
	public static void incField(){
		fieldInst++;
		compNum = -1;
		empty = false;
	}
	
	public static String getPath(){
		String path = "";
		if(empty)
			return "EMPTY";
		
		path = segName + "["+segInst+"]";
		if(fieldNum != -1)
			path += "."+fieldNum+"["+fieldInst+"]";
		if(compNum != -1)
			path += "."+compNum;
		
		return path;
		
	}
	
	
	
}
