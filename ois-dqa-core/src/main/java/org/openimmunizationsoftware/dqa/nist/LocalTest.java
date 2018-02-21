package org.openimmunizationsoftware.dqa.nist;



import static java.nio.file.Paths.get;

import java.io.InputStream;

import static java.nio.file.Files.readAllBytes;
import gov.nist.healthcare.unified.model.Section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class LocalTest {

	public static void main(String[] args) {
		String test;
		Scanner s = new Scanner(System.in);
		try {
			String exit = "";
			for(int i = 0; i < 1; i++){
				test = toString(LocalTest.class.getResourceAsStream("/msgtest.er7"));
				CompactReportModel cc= ProcessMessageHL7.getInstance().process(test, "1223");
				System.out.println(cc);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String toString(InputStream is){
		Scanner s = new Scanner(is).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		s.close();
		return result;
	}
}
