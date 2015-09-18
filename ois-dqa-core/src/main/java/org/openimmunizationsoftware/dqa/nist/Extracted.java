package org.openimmunizationsoftware.dqa.nist;

public class Extracted {
	protected Locations location = new Locations();
	
	public String locate(String attr){
		return location.get(attr);
	}
	
	public void put(String attr,String loc){
		if(Locator.inParse)
			location.put(attr, loc);
	}
}
