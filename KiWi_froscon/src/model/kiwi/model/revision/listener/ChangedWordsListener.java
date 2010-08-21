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
package kiwi.model.revision.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import kiwi.model.revision.TextContentUpdateDiff;

/**
 * This listener class is used by the TextContentUpdateDiff to
 * transform a map into a string, so that it can easily be 
 * stored in the database.
 * 
 * @author 	Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
public class ChangedWordsListener {

	/**
	 * Called after TextContentUpdateDiff has been persisted in order to
	 * transform the stored changedWords string back to a changedWordsMap.
	 * 
	 * @param diff
	 */
	@PostPersist
	@PostUpdate
	public void stringToMap(TextContentUpdateDiff diff) {
		if(diff.getChangedWords() != null) {
			List<String> elements = new ArrayList<String>( 
					Arrays.asList(diff.getChangedWords().split("[\"{]end[}]")) );
			HashMap<String, String> map = diff.getChangedWordsMap();
			if(map == null) {
				map = new HashMap<String, String>();
			}
			for(String e : elements) {
				String[] a = e.split("=\"[{]start[}]");
				
				if(a.length > 1) {
					map.put(a[0], a[1]);
				}
			}
			diff.setChangedWordsMap(map);
		}
	}
	
	/**
	 * Called before TextContentUpdateDiff is persisted or updated.
	 * Converts the transient changedWordsMap to the string changedWords 
	 * in order to store it in the database. 
	 * 
	 * @param diff
	 */
	@PreUpdate
	@PrePersist
	public void mapToString(TextContentUpdateDiff diff) {
		StringBuilder sb = new StringBuilder();
		HashMap<String,String> map;
		if((map = diff.getChangedWordsMap()) != null) {
			for(String i : map.keySet()) {
				sb.append(i);
				sb.append("={start}\"");
				sb.append(map.get(i));
				sb.append("\"{end}");
			}
	        diff.setChangedWords(sb.toString());
		}
	}
}
