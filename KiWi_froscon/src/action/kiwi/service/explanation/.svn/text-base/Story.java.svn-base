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
package kiwi.service.explanation;

import java.util.ArrayList;
import java.util.List;

/** Story encapsulates explanations produced by the explanation service.
 * 
 * 
 * @author Jakub Kotowski
 *
 */
public class Story {
	List<Line> story;
	String justificationId;
	
	public Story(List<Line> story) {
		this.story = story;
	}

	public Story() {
		story = new ArrayList<Line>();
	}
	
	public Line getOpeningLine() {
		return story.get(0);
	}
	
	public List<Line> getBody() {
		return story.subList(1, story.size());
	}
	
	public List<Line> getStory() {
		return story;
	}

	public void setStory(List<Line> story) {
		this.story = story;
	}	
	
	public void addLine(String body, String connective) {
		story.add(new Line(body, connective));
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Line line : story)
			buf.append(line.toString());
		
		return buf.toString();
	}
		
	public String getJustificationId() {
		return justificationId;
	}

	public void setJustificationId(String justificationId) {
		this.justificationId = justificationId;
	}


	public class Line {
		private String body;
		private String connective;
		
		public Line(String body, String connective) {
			this.body = body;
			this.connective = connective;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getConnective() {
			return connective;
		}

		public void setConnective(String connective) {
			this.connective = connective;
		}		
		
		public String toString() {
			return body+" "+connective;
		}
	}
}
