package org.openimmunizationsoftware.dqa.nist;

import gov.nist.healthcare.unified.model.Section;

import java.util.ArrayList;

public class CompactReportModel {
	private ArrayList<CompactReportNode> issuesList = new ArrayList<CompactReportNode>();
	
	public ArrayList<CompactReportNode> getIssuesList() {
		return issuesList;
	}

	public void setIssuesList(ArrayList<CompactReportNode> issuesList) {
		this.issuesList = issuesList;
	}

	public String toString(){
		String res = "Issues Count : "+issuesList.size()+"\n";
		for(CompactReportNode node : issuesList)
		{
			res += node.toString();
			res += " --- \n";
		}
		return res;
	}
	
	public void add(CompactReportNode n){
		if(!issuesList.contains(n))
			issuesList.add(n);
	}
	
	public ArrayList<Section> toSections(){
		ArrayList<Section> list = new ArrayList<Section>();
		for(CompactReportNode c : issuesList){
			if(!c.type.equals("Skip")){
				Section s = new Section("");
				s.put("path", c.location);
				
				if(!c.given_code.equals("N/A"))
					s.put("description", c.detailed_desc+" code found : "+ c.given_code+", issue ID : "+c.potential_issue_id+"");
				else
					s.put("description", c.detailed_desc+" issue ID : "+c.potential_issue_id+"");
				
				s.put("category", "DQA");
				
				if(c.type.equals("Error")){
					s.put("classification","Error");
				}
				else if(c.type.equals("Warn")){
					s.put("classification", "Warning");
				}
				else if(c.type.equals("Accept")){
					s.put("classification","Affirmative");
				}
				list.add(s);
			}
		}
		return list;
	}
	
	public ArrayList<Section> toSections(ArrayList<String> ids){
		ArrayList<Section> list = new ArrayList<Section>();
		for(CompactReportNode c : issuesList){
			if(!c.type.equals("Skip") && ids.contains(c.potential_issue_id)){
				Section s = new Section("");
				s.put("path", c.location);
				
				if(!c.given_code.equals("N/A"))
					s.put("description", c.detailed_desc+" code found : "+ c.given_code+", issue ID : "+c.potential_issue_id+"");
				else
					s.put("description", c.detailed_desc+" issue ID : "+c.potential_issue_id+"");
				
				s.put("category", "DQA");
				
				if(c.type.equals("Error")){
					s.put("classification","Error");
				}
				else if(c.type.equals("Warn")){
					s.put("classification", "Warning");
				}
				else if(c.type.equals("Accept")){
					s.put("classification","Affirmative");
				}
				list.add(s);
			}
		}
		return list;
		
	}
	
	
}
