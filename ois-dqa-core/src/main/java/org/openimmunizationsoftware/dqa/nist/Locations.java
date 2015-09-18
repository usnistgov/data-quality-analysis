package org.openimmunizationsoftware.dqa.nist;

import java.util.HashMap;

public class Locations {
	private HashMap<String,String> location_map = new HashMap<String,String>();
	
	public String get(String attribute)
	{
		if(location_map.containsKey(attribute))
			return location_map.get(attribute);
		else
			return "NoNe";
	}
	
	public void put(String attribute, String location)
	{
		location_map.put(attribute, location);
	}
}
