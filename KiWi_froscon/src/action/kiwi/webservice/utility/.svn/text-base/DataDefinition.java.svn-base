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

import java.util.HashMap;
import java.util.Map;

/**
 * This Enum keeps the different datatypes for the template driven import.
 * @author Karsten Jahn
 *
 */
public class DataDefinition {
	// This is the mapping table, the key is the data description and the value is the URI of the Ontology.
	static Map<String, String> mapping = new HashMap<String, String>();

	static {
		// TODO Make this Property driven
		mapping.put("employee.name",	"http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasFullName");
		mapping.put("employee.number",	"");
		mapping.put("employee.initials",	"http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasInitials");
		mapping.put("employee.work_hours_per_week",	"http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#workHoursPerWeek");
		mapping.put("employee.comment",	"");
		mapping.put("employee.skill.level",	"http://www.kiwi-project.eu/kiwi/logica/SkillPlan#hasActualLevel");
		mapping.put("employee.skill.category",	"http://www.kiwi-project.eu/kiwi/logica/SkillCatergory#hasName");
		mapping.put("employee.skill.comment",	"http://www.kiwi-project.eu/kiwi/logica/SkillPlan#hasComment");
		mapping.put("employee.organizationalunit.name",	"");
		mapping.put("employee.organizationalunit.number",	"");
		mapping.put("employee.project.start",	"");
		mapping.put("employee.project.end",	"");
		mapping.put("employee.project.name",	"http://www.kiwi-project.eu/kiwi/logica/Project#hasName");
		mapping.put("employee.calendar.absent_from",	"");
		mapping.put("employee.calendar.absent_to",	"");
		mapping.put("employee.calendar.cause",	"");
		mapping.put("organizationalunit.responsible_manager.name",	"http://www.kiwi-project.eu/kiwi/logica/ProjectParticipant#hasFullName");
		mapping.put("organizationalunit.name",	"");
		mapping.put("organizationalunit.number",	"");
		mapping.put("organizationalunit.belongs_to.name",	"");
		//mapping.put("",	"");
	}
}
