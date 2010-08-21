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

package kiwi.model.revision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import kiwi.model.revision.listener.ChangedWordsListener;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * TextConntentUpdateDiff stores word diff and position
 * Used by TextContentUpdate to store differences in a text 
 * that occurred because of an update.
 * This class holds data that was previously in the text
 * 
 * @author Stephanie Stroka 
 * 			(sstroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@EntityListeners(ChangedWordsListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 20)
public class TextContentUpdateDiff implements Serializable, Comparable<TextContentUpdateDiff> {

	/**
	 * generated serial version id
	 */
	private static final long serialVersionUID = -3473884096241525512L;
	
	/**
	 * the id is generated to serve as a primary key 
	 * for this table in the relational model
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;

	@Lob
	private String changedWords;
	/**
	 * position of the changed word in the text and
	 * previous word that was at the position before,
	 * if it's empty it means that there wasn't any 
	 * word before
	 */
	@Transient
	private HashMap<String,String> changedWordsMap;
	
	/**
	 * the xpath location of the different word
	 * in the xhtml document
	 */
	@Lob
	private String xPath;
	
	/**
	 * default constructor
	 */
	public TextContentUpdateDiff() {
	}
	
	/**
	 * 
	 */
	public TextContentUpdateDiff(String xPath) {
		this.xPath = xPath;
		this.changedWordsMap = new HashMap<String,String>();
	}

	/**
	 * @return the changedWords
	 */
	public String getChangedWords() {
		return changedWords;
	}

	/**
	 * @param changedWords the changedWords to set
	 */
	public void setChangedWords(String changedWords) {
		this.changedWords = changedWords;
	}

	/**
	 * @return the changedWordsMap
	 */
	public HashMap<String, String> getChangedWordsMap() {
		if(changedWordsMap == null) {
			changedWordsMap = new HashMap<String, String>();
			if(changedWords != null) {
				List<String> elements = new ArrayList<String>( 
						Arrays.asList(changedWords.split("\\\"[{]end[}]")) );
				for(String e : elements) {
					String[] a = e.split("=[{]start[}]\\\"");
					if(a.length > 1) {
						changedWordsMap.put(a[0].trim(), a[1].trim());
					} else {
						changedWordsMap.put(a[0].trim(), "");
					}
				}
			}
		}
		return changedWordsMap;
	}

	/**
	 * @param changedWordsMap the changedWordsMap to set
	 */
	public void setChangedWordsMap(HashMap<String, String> changedWordsMap) {
		this.changedWordsMap = changedWordsMap;
	}

	/**
	 * @return the xPath
	 */
	public String getXPath() {
		return xPath;
	}

	/**
	 * @param path the xPath to set
	 */
	public void setXPath(String path) {
		xPath = path;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(TextContentUpdateDiff d) {
		if(this.getXPath() != null && d.getXPath() != null) {
			String xpathMe = this.getXPath();
			String xpathOpponent = d.getXPath();
			List<String> xpathNodesMe = new LinkedList<String>(Arrays.asList(xpathMe.split("\\Q/*[local-name()='\\E")));
			while(xpathNodesMe.remove(""));
			List<String> xpathNodesOpponent = new LinkedList<String>(Arrays.asList(xpathOpponent.split("\\Q/*[local-name()='\\E")));
			while(xpathNodesOpponent.remove(""));
			if(xpathNodesMe.size() > xpathNodesOpponent.size()) {
				return -1;
			} else if(xpathNodesMe.size() < xpathNodesOpponent.size()) {
				return 1;
			} else {
				StringBuilder strbMe = new StringBuilder();
				StringBuilder strbOpponent = new StringBuilder();
				for(String s : xpathNodesMe) {
					String[] tmpS1 = s.split("\\Q'][\\E");
					if(tmpS1.length != 2) {
						return 0;
					}
					if(!tmpS1[1].endsWith("]"))  {
						return 0;
					}
					strbMe.append(tmpS1[1].substring(0, tmpS1.length-1));
				}
				for(String s : xpathNodesOpponent) {
					String[] tmpS1 = s.split("\\Q'][\\E");
					if(tmpS1.length != 2) {
						return 0;
					}
					if(!tmpS1[1].endsWith("]"))  {
						return 0;
					}
					strbOpponent.append(tmpS1[1].substring(0, tmpS1.length-1));
				}
				if(strbMe.toString().compareTo(strbOpponent.toString()) == -1) {
					return 1;
				} else if(strbMe.toString().compareTo(strbOpponent.toString()) == 1) {
					return -1;
				} else if(strbMe.toString().compareTo(strbOpponent.toString()) == 0) {
					return 0;
				}
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		String s1 = new String("/*[local-name()='div'][1]/*[local-name()='div'][1]/*[local-name()='p'][1]");
		String s2 = new String("/*[local-name()='div'][1]/*[local-name()='div'][1]");
		String s3 = new String("/*[local-name()='div'][1]/*[local-name()='div'][1]/*[local-name()='p'][2]");
		
		TextContentUpdateDiff tcudiff1 = new TextContentUpdateDiff();
		tcudiff1.setXPath(s1);
		TextContentUpdateDiff tcudiff2 = new TextContentUpdateDiff();
		tcudiff2.setXPath(s2);
		TextContentUpdateDiff tcudiff3 = new TextContentUpdateDiff();
		tcudiff3.setXPath(s3);
		
		List<TextContentUpdateDiff> diffs = new LinkedList<TextContentUpdateDiff>();
		diffs.add(tcudiff1);
		diffs.add(tcudiff2);
		diffs.add(tcudiff3);
		Collections.sort(diffs);
		System.out.println("sorted list: ");
		for(TextContentUpdateDiff d : diffs) {
			System.out.println(d.getXPath() + ", ");
		}
	}
}
