package org.openimmunizationsoftware.dqa.nist;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openimmunizationsoftware.dqa.db.model.IssueFound;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;
import org.openimmunizationsoftware.dqa.manager.OrganizationManager;
import org.openimmunizationsoftware.dqa.manager.PotentialIssues;
import org.openimmunizationsoftware.dqa.parse.VaccinationParser;
import org.openimmunizationsoftware.dqa.parse.VaccinationParserFactory;
import org.openimmunizationsoftware.dqa.validate.Validator;

public class ProcessMessageHL7 {
	public Session session;
	private static ProcessMessageHL7 instance = new ProcessMessageHL7();
	
	private ProcessMessageHL7(){
		
	}
	
	public static ProcessMessageHL7 getInstance(){
		return instance;
	}
	
	public CompactReportModel process(String content,String profile_id)
	{
		if(session == null || !session.isOpen()){
			SessionFactory factory = OrganizationManager.getSessionFactory();
		    session = factory.openSession();
		}
		
	    String test = "";
		try 
		{
			test = content;
			
			// Instantiations
			SubmitterProfile profile = getProfile(session,profile_id);
			profile.initPotentialIssueStatus(session);
			
			VaccinationParser parser = VaccinationParserFactory.createVaccinationParser(test, profile);

			Validator validator = new Validator(profile,session);
			MessageReceived messageReceived = new MessageReceived();
			messageReceived.setRequestText(test);
			// Processing
			parser.createVaccinationUpdateMessage(messageReceived);
			validator.validateVaccinationUpdateMessage(messageReceived,null);

			//System.out.println(parser.makeAckMessage(messageReceived));
			CompactReportModel crm = new CompactReportModel();
			//System.out.println("Number of issues : "+messageReceived.getIssuesFound().size());
			for(IssueFound is : messageReceived.getIssuesFound())
			{
				if(/*!is.isSkip() &&*/ !is.isAccept())
				{
					CompactReportNode node = new CompactReportNode();
					node.issue_small_desc = is.getIssue().getDisplayText();//System.out.println(is.getIssue());
					node.location = is.getIssue().getHl7Reference();//+"//"+is.getIssue().getDraft();
					//System.out.println(is.getIssue().getDraft());
					if(is.getCodeReceived() != null)
						node.given_code = is.getCodeReceived().getReceivedValue();
					node.type = is.getIssueAction().getActionLabel();
					node.detailed_desc = PotentialIssues.getPotentialIssues().getLongDescription(is.getIssue());
					//System.out.println(PotentialIssues.getPotentialIssues().getLongDescription(is.getIssue()));//documentationTextProperties.getProperty(issue.getDisplayText());
					//System.out.println();
					node.potential_issue_id = is.getIssue().getIssueId()+"";
					crm.add(node);
				}
			}
			return crm;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static SubmitterProfile getProfile(Session session,String id) {
		 
		 SubmitterProfile profile = null;
			Query query = session.createQuery("from SubmitterProfile where profile_id = "+id);
			List<SubmitterProfile> submitterProfiles = query.list();
		    if (submitterProfiles.size() == 0)
		    {
		      System.out.println("Error No profile");
		    } 
		    else
		    {
		      profile = submitterProfiles.get(0);
			}
		    return profile;
	 }
}
