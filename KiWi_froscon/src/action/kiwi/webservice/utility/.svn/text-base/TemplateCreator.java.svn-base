/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */
package kiwi.webservice.utility;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * @author Karsten Jahn
 *
 */
public class TemplateCreator {

	/**
	 * Empty constructor
	 */
	public TemplateCreator() {
		super();
	}

	/**
	 * create a template for employees.
	 * @return XML that represents the template content.
	 */
	public static Element createEmployeeTemplate() {
		Element root = new Element("div");	// The div as a root.
		
		// create elements for ontology
		Element employeeName = new Element("span");
		employeeName.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasFullName"));
		employeeName.appendChild("@employee.name@");
		
		Element employeeNo = new Element("span");
		//employeeNo.addAttribute(new Attribute("property", "logica:employee.no"));
		employeeNo.appendChild("@employee.number@");

		Element employeeInitials = new Element("span");
		employeeInitials.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasInitials"));
		employeeInitials.appendChild("@employee.initials@");

		Element employeeWorkingHours = new Element("span");
		employeeWorkingHours.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#workHoursPerWeek"));
		employeeWorkingHours.appendChild("@employee.work_hours_per_week@");

		Element employeeComments = new Element("span");
		//employeeComments.addAttribute(new Attribute("property", "logica:employee.comment"));
		employeeComments.appendChild("@employee.comment@");

		Element employeeSkill = new Element("span");
		employeeSkill.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/SkillPlan#hasActualLevel"));
		employeeSkill.appendChild("@employee.skill.level@");

		Element employeeSkillCategory = new Element("span");
		employeeSkillCategory.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/SkillCatergory#hasName"));
		employeeSkillCategory.appendChild("@employee.skill.category@");

		Element employeeSkillComment = new Element("span");
		employeeSkillComment.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/SkillPlan#hasComment"));
		employeeSkillComment.appendChild("@employee.skill.comment@");

		Element organizationalUnit = new Element("span");
		//organizationalUnit.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/OrganizationalUnit#hasName"));
		organizationalUnit.appendChild("@employee.organizationalunit.name@");

		Element organizationalUnitNo = new Element("span");
		//organizationalUnit.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/OrganizationalUnit#hasNumber"));
		organizationalUnitNo.appendChild("@employee.organizationalunit.number@");

		Element projectFrom = new Element("span");
		//projectFrom.addAttribute(new Attribute("property", "logica:project.from"));
		projectFrom.appendChild("@employee.project.start@");

		Element projectTo = new Element("span");
		//projectTo.addAttribute(new Attribute("property", "logica:project.to"));
		projectTo.appendChild("@employee.project.end@");
		
		Element projectName = new Element("span");
		projectName.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Project#hasName"));
		projectName.appendChild("@employee.project.name@");
		
		Element calendarFrom = new Element("span");
		//calendarFrom.addAttribute(new Attribute("property", "logica:calendar.from"));
		calendarFrom.appendChild("@employee.calendar.absent_from@");

		Element calendarTo = new Element("span");
		//calendarTo.addAttribute(new Attribute("property", "logica:calendar.to"));
		calendarTo.appendChild("@employee.calendar.absent_to@");
		
		Element calendarReason = new Element("span");
		//calendarReason.addAttribute(new Attribute("property", "logica:calendar.reason"));
		calendarReason.appendChild("@employee.calendar.cause@");
		
		// 1. paragraph: worker information
		Element p1 = new Element("p");
		p1.appendChild(employeeName);
		p1.appendChild(" (Employee No. ");
		p1.appendChild(employeeNo);
		p1.appendChild(") uses the initials ");
		p1.appendChild(employeeInitials);
		p1.appendChild(". He works ");
		p1.appendChild(employeeWorkingHours);
		p1.appendChild(" hours per week. Comments on him: ");
		p1.appendChild(employeeComments);
		p1.appendChild(new Element("br"));
		p1.appendChild("In ");
		p1.appendChild(employeeSkillCategory);
		p1.appendChild(" he is of level \"");
		p1.appendChild(employeeSkill);
		p1.appendChild("\". Comment: ");
		p1.appendChild(employeeSkillComment);
		
		// 2. paragraph: project information
		Element p2 = new Element("p");
		Element project = new Element("strong");
		project.appendChild("Projects:");
		p2.appendChild(project);
		p2.appendChild(new Element("br"));
		p2.appendChild(employeeName.copy());
		p2.appendChild(" is part of the organizational Unit [[Template: OrganizationalUnit|");
		p2.appendChild(organizationalUnitNo);
		p2.appendChild(": ");
		p2.appendChild(organizationalUnit);
		p2.appendChild("]]");	// TODO make link property driven
		p2.appendChild(new Element("br"));
		p2.appendChild("@(@From ");
		p2.appendChild(projectFrom);
		p2.appendChild(" to ");
		p2.appendChild(projectTo);
		p2.appendChild(" he works on the ");
		p2.appendChild(projectName);
		p2.appendChild(" project.");
		p2.appendChild(new Element("br"));
		p2.appendChild("@)@");
		
		// 3. paragraph: calendar
		Element p3 = new Element("p");
		Element calendar = new Element("strong");
		calendar.appendChild("Calendar:");
		p3.appendChild(calendar);
		p3.appendChild(new Element("br"));
		p3.appendChild("@(@From ");
		p3.appendChild(calendarFrom);
		p3.appendChild(" to ");
		p3.appendChild(calendarTo);
		p3.appendChild(" he is ");
		p3.appendChild(calendarReason);
		p3.appendChild(".");
		p3.appendChild(new Element("br"));
		p3.appendChild("@)@");
		
		root.appendChild(p1);
		root.appendChild(p2);
		root.appendChild(p3);
		
		return root;
	}

	/**
	 * create a template for employees.
	 * @return XML that represents the template content.
	 */
	public static Element createOrganizationalUnitTemplate() {
		Element root = new Element("div");	// The div as a root.
		
		// create elements for ontology
		Element employeeName = new Element("span");
		employeeName.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasFullName"));
		employeeName.appendChild("@organizationalunit.responsible_manager.name@");
		
		Element organizationalUnit = new Element("span");
		//organizationalUnit.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/OrganizationalUnit#hasName"));
		organizationalUnit.appendChild("@organizationalunit.name@");

		Element organizationalUnitNo = new Element("span");
		//organizationalUnit.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/OrganizationalUnit#hasNumber"));
		organizationalUnitNo.appendChild("@organizationalunit.number@");
		
		Element organizationalUnitSuper = new Element("span");
		//organizationalUnitSuper.addAttribute(new Attribute("property", "logica:organizational.unit.super"));
		organizationalUnitSuper.appendChild("@organizationalunit.belongs_to.name@");

		// Paragraph of text
		Element p1 = new Element("p");
		p1.appendChild("The Organizational Unit ");
		p1.appendChild(organizationalUnitNo);
		p1.appendChild(" carries the name ");
		p1.appendChild(organizationalUnit);
		p1.appendChild(" and belongs to the unit [[Template: OrganizationalUnit|");
		p1.appendChild(organizationalUnitSuper);
		p1.appendChild("]]. The responsible manager is  [[Template: Employee|");
		p1.appendChild(employeeName);
		p1.appendChild("]].");	// TODO make link property driven

		root.appendChild(p1);
		
		return root;
	}

	/**
	 * create a template for employees.
	 * @return XML that represents the template content.
	 */
	public static Element createRiskTemplate() {
		Element root = new Element("div");	// The div as a root.
		
		// create elements for ontology
		Element riskName = new Element("span");
		riskName.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasTitle"));
		riskName.appendChild("@risk.name@");
		
		Element riskPlan = new Element("span");
		riskPlan.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/ProjectPlan#hasName"));
		riskPlan.appendChild("@risk.plan.name@");
		
		Element riskStatus = new Element("span");
		//riskStatus.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		riskStatus.appendChild("@risk.status@");
		
		Element riskResponsibleEmployee = new Element("span");
		riskResponsibleEmployee.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasFullName"));
		riskResponsibleEmployee.appendChild("@risk.responsible_employee.name@");
		
		Element riskCategory = new Element("span");
		//riskCategory.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		riskCategory.appendChild("@risk.category.name@");
		
		Element likelihoodRating = new Element("span");
		//likelihoodRating.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		likelihoodRating.appendChild("@risk.likelihood_rating.name@");
		
		Element impactRating = new Element("span");
		impactRating.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasImpact"));
		impactRating.appendChild("@risk.impact_rating.name@");
		
		Element dateIdentified = new Element("span");
		dateIdentified.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#wasIdentifiedAt"));
		dateIdentified.appendChild("@risk.date.identified@");
		
		Element dateLastReview = new Element("span");
		//dateLastReview.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		dateLastReview.appendChild("@risk.date.last_review@");
		
		Element dateEarliestImpact = new Element("span");
		//dateEarliestImpact.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		dateEarliestImpact.appendChild("@risk.date.earliest_impact@");
		
		Element thresoldForAction = new Element("span");
		//thresoldForAction.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		thresoldForAction.appendChild("@risk.threshold_for_action@");
		
		Element riskDesc = new Element("span");
		riskDesc.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasDescription"));
		riskDesc.appendChild("@risk.description@");
		
		Element riskImpactDesc = new Element("span");
		//riskImpactDesc.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		riskImpactDesc.appendChild("@risk.impact_description@");
		
		Element riskRemarks = new Element("span");
		//riskRemarks.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		riskRemarks.appendChild("@risk.remarks@");
		
		Element impactRS = new Element("span");
		//impactRS.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		impactRS.appendChild("@risk.impact_reduction_stragegy@");
		
		Element likelihoodRS = new Element("span");
		//likelihoodRS.addAttribute(new Attribute("property", "http://www.kiwi-project.eu/kiwi/logica/Risk#hasFullName"));
		likelihoodRS.appendChild("@risk.likelihood_reduction_strategy@");
		
		
		// Paragraph of text
		Element p1 = new Element("p");
		p1.appendChild("The Risk \"");
		p1.appendChild(riskName);
		p1.appendChild("\" is from the Project Plan \"[[ Template: ProjectPlan |");
		p1.appendChild(riskPlan);
		p1.appendChild(" ]]\" and has the Status ");
		p1.appendChild(riskStatus);
		p1.appendChild(". The responsible Project Participant is [[ Template: Employee |");
		p1.appendChild(riskResponsibleEmployee);
		p1.appendChild(" ]]. The Risk is of Category ");
		p1.appendChild(riskCategory);
		p1.appendChild(", the likelihood is ");
		p1.appendChild(likelihoodRating);
		p1.appendChild(" and the impact is ");
		p1.appendChild(impactRating);
		p1.appendChild(".");
		p1.appendChild(new Element("br"));
		p1.appendChild("This Risk was identified at ");
		p1.appendChild(dateIdentified);
		p1.appendChild(", the date for the last review is ");
		p1.appendChild(dateLastReview);
		p1.appendChild(" and the earliest impact might be at ");
		p1.appendChild(dateEarliestImpact);
		p1.appendChild(". The Threshold for action is ");
		p1.appendChild(thresoldForAction);
		p1.appendChild(".");
		
		// Paragraph of text
		Element p2 = new Element("p");
		p2.appendChild("Following brief descriptions...");
		p2.appendChild(new Element("br"));
		p2.appendChild("The Risk itself: ");
		p2.appendChild(riskDesc);
		p2.appendChild(new Element("br"));
		p2.appendChild("The Impact of the Risk: ");
		p2.appendChild(riskImpactDesc);
		p2.appendChild(new Element("br"));
		p2.appendChild("Remarks: ");
		p2.appendChild(riskRemarks);
		
		// Paragraph of text
		Element p3 = new Element("p");
		p3.appendChild("Strategy for reducing the Impact: ");
		p3.appendChild(impactRS);
		p3.appendChild(new Element("br"));
		p3.appendChild("Strategy for reducing the Likelihood: ");
		p3.appendChild(likelihoodRS);

		root.appendChild(p1);
		root.appendChild(p2);
		root.appendChild(p3);
		
		return root;
	}
}
