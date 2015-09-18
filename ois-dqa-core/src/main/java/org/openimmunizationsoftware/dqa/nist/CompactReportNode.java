package org.openimmunizationsoftware.dqa.nist;

public class CompactReportNode {
	
	/**
	 * Issue's name containing a small description
	 */
	public String issue_small_desc;
	/**
	 * The location where the issue was reported
	 */
	public String location;
	/**
	 * Issue's type, can be Error or Warning 
	 */
	public String type;
	/**
	 * A detailed description of the issue
	 */
	public String detailed_desc;
	/**
	 * The issue id
	 */
	public String potential_issue_id;
	/**
	 * The given code in the message if the issue concerns an invalid code
	 */
	public String given_code = "N/A";
	
	
	public String toString(){
		return  "ISSUE : "+this.issue_small_desc+"\n"+
				"LOCATION : "+this.location+"\n"+
				"ISSUE TYPE : "+this.type+"\n"+
				"DESCRIPTION : "+this.detailed_desc+"\n"+
				"ISSUE ID : "+this.potential_issue_id+"\n"+
				"GIVEN CODE : "+this.given_code+"\n";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((detailed_desc == null) ? 0 : detailed_desc.hashCode());
		result = prime * result
				+ ((given_code == null) ? 0 : given_code.hashCode());
		result = prime
				* result
				+ ((issue_small_desc == null) ? 0 : issue_small_desc.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime
				* result
				+ ((potential_issue_id == null) ? 0 : potential_issue_id
						.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompactReportNode other = (CompactReportNode) obj;
		if (detailed_desc == null) {
			if (other.detailed_desc != null)
				return false;
		} else if (!detailed_desc.equals(other.detailed_desc))
			return false;
		if (given_code == null) {
			if (other.given_code != null)
				return false;
		} else if (!given_code.equals(other.given_code))
			return false;
		if (issue_small_desc == null) {
			if (other.issue_small_desc != null)
				return false;
		} else if (!issue_small_desc.equals(other.issue_small_desc))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (potential_issue_id == null) {
			if (other.potential_issue_id != null)
				return false;
		} else if (!potential_issue_id.equals(other.potential_issue_id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
