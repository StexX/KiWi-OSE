/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 * 
 * 
 */

package kiwi.service.revision;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kiwi.exception.TagElementIsCorruptException;
import kiwi.model.revision.TextContentUpdateDiff;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * LCS - Longest Common Subsequence
 * cannot be an Entity-Bean because calcLCS returns a 
 * multi-dimensional matrix (Hibernate bug).
 * 
 * The Longest Common Subsequence algorithm calculates the similarity between two XHTML texts 
 * by comparing each word of one text agains the other. It creates a list of 
 * TextContentUpdateDiffs, in order to be able to 
 * 
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
public class LCS {

	/**
	 * calculates the LCS matrix between the original and the modified text
	 * @param original
	 * @param modified
	 * @param 	xPathsPosition identifies the current word with 
	 * 			its position in the text (currently counted 
	 * 			amount of words) and its XPath and the position 
	 * 			of the word inside of the XPath.
	 * 			The first parameter is the position of the word in the whole text.
	 * 			The second parameter is the relationship between 
	 * 				the XPath and the position of the word inside of this XPath
	 * @return
	 */
	private static int[][] calcLCS( List<String> original, 
			List<String> modified, Map<Integer,Map<String,Integer>> xPathsPosition) throws Exception {
		
		Log log = Logging.getLog(LCS.class);
		/* temporarily stores all xpaths that are available for the current position */
		List<String> currentXpathQueue = new ArrayList<String>();
		
		/* counts the appearance of similar tags under the same xpath */
		Map<String,Integer> countXPaths = new HashMap<String,Integer>();
		
		/* counts the words under each xpath */
		Map<String,Integer> countWordsUnderXPath = new HashMap<String, Integer>();
		
		int[][] num = new int[original.size()+1][modified.size()+1];
		
		if(xPathsPosition == null) {
			xPathsPosition = new HashMap<Integer, Map<String,Integer>>();
		}
		
		for(int mod_index=0; mod_index<modified.size()+1; mod_index++) {
		//for(int orig_index=0; orig_index<original.size()+1; orig_index++) {
			
			String mod = null;
			if(mod_index != 0) {
				mod = modified.get(mod_index-1);
				if(mod.startsWith("</")) {
					if(currentXpathQueue.size() <= 0) {
						// TODO: specify exception
						throw new Exception();
					} else {
						Map<String,Integer> xPathsAndPathPosition = new HashMap<String, Integer>();
						String tmpxpath = currentXpathQueue.get(currentXpathQueue.size()-1);
						xPathsAndPathPosition.put(tmpxpath, 999999);
						xPathsPosition.put(mod_index, xPathsAndPathPosition);
						log.debug("current closing tag: #0 ", mod);
						log.debug("removing xpath #0 ", currentXpathQueue.get(currentXpathQueue.size()-1));
						currentXpathQueue.remove(currentXpathQueue.size()-1);
					}
				} else if(mod.startsWith("<") && mod.endsWith("/>")) {
					log.debug("current opening/closing tag: #0 ", mod);
					String newXPath = null;
					if(currentXpathQueue.size() > 0) {
						newXPath = generateXPath(mod, currentXpathQueue
								.get(currentXpathQueue.size()-1), countXPaths);
					} else {
						newXPath = generateXPath(mod, null, countXPaths);
					}
					if(newXPath == null) {
						throw new Exception("New Xpath could not be created");
					}
					countWordsUnderXPath.put(newXPath, 0);
					Map<String,Integer> xPathsAndPathPosition = new HashMap<String, Integer>();
					xPathsAndPathPosition.put(
							newXPath, 0);
					xPathsPosition.put(mod_index, xPathsAndPathPosition);
				} else if(mod.startsWith("<")) {
					log.debug("current opening tag: #0 ", mod);
					String newXPath = null;
					if(currentXpathQueue.size() > 0) {
						newXPath = generateXPath(mod, currentXpathQueue
								.get(currentXpathQueue.size()-1), countXPaths);
					} else {
						newXPath = generateXPath(mod, null, countXPaths);
					}
					if(newXPath == null) {
						throw new Exception("New Xpath could not be created");
					}
					countWordsUnderXPath.put(newXPath, 0);
					currentXpathQueue.add(newXPath);
					Map<String,Integer> xPathsAndPathPosition = new HashMap<String, Integer>();
					xPathsAndPathPosition.put(
							currentXpathQueue.get(currentXpathQueue.size()-1), 0);
					xPathsPosition.put(mod_index, xPathsAndPathPosition);
				} else {
					if(currentXpathQueue.size() > 0) {
						String tmpxpath = currentXpathQueue.get(currentXpathQueue.size()-1);
						Map<String,Integer> xPathsAndPathPosition = new HashMap<String, Integer>();
						int newCount = countWordsUnderXPath.get(tmpxpath)+1;
						countWordsUnderXPath.put(tmpxpath, newCount);
						xPathsAndPathPosition.put(tmpxpath, newCount);
						xPathsPosition.put(mod_index, xPathsAndPathPosition);
					} else {
						Map<String,Integer> xPathsAndPathPosition = new HashMap<String, Integer>();
						xPathsAndPathPosition.put("", mod_index);
						xPathsPosition.put(mod_index, xPathsAndPathPosition);
					}
				}
			}
			
			for(int orig_index=0; orig_index<original.size()+1; orig_index++) {
//			for(int mod_index=0; mod_index<modified.size()+1; mod_index++) {
				if((orig_index==0) || (mod_index==0)) {
					num[orig_index][mod_index] = 0;
				} else {
					String orig = original.get(orig_index-1);
					if(orig == null) {
						throw new Exception("The original word should not be null at orig-Position " + (orig_index-1));
					}
					if(orig.equals(mod)) {
						num[orig_index][mod_index] = 1 + num[orig_index-1][mod_index-1];
					} else {
						if((orig_index==1) && (mod_index==1)) {
							num[orig_index][mod_index] = 0;
						} else if((orig_index==1) && !(mod_index==1)){
							num[orig_index][mod_index] = Math.max(0, num[orig_index][mod_index-1]);
						} else if(!(orig_index==1) && (mod_index==1)){
							num[orig_index][mod_index] = Math.max(num[orig_index-1][mod_index],0);
						} else if(!(orig_index==1) && !(mod_index==1)) {
							num[orig_index][mod_index] = Math.max(num[orig_index-1][mod_index], num[orig_index][mod_index-1]);
						}
					}
				}
			}
		}
		return num;
	}
	
	/**
	 * generated the xpath for the word together with the lastXpath
	 * @param word
	 * @param lastXpath
	 * @param countXPaths
	 * @return
	 */
	private static String generateXPath(String word, String lastXpath, 
			Map<String,Integer> countXPaths) {
		Log log = Logging.getLog(LCS.class);
		log.debug("generating xpath for new tag #0 , last xpath was: #1 ", word, lastXpath);
		String tag = getTagnameByWord(word);
		tag = "/*[local-name()='" + tag + "']" ;
		// the xpath without [x]
		StringBuilder tmpPath = new StringBuilder();
		if(lastXpath == null || lastXpath.equals("")) {
			tmpPath.append(tag);
		} else {
			tmpPath.append(lastXpath);
			tmpPath.append(tag);
		}
		// look up if the xpath existed
		if( !countXPaths.containsKey(tmpPath.toString()) ) {
			countXPaths.put(tmpPath.toString(), 1);
			return tmpPath.toString() + "[1]";
		} else {
			int newCount = countXPaths.get(tmpPath.toString())+1;
			countXPaths.put(tmpPath.toString(), newCount);
			return tmpPath.toString() + "["+ newCount + "]";
		}
	}
	
	/**
	 * calls the methods readDiffsfromBackTrack(), which is the string based
	 * diff technique and generates a xhtml based diff, containing xpaths and 
	 * xpathpositions (much easier to handle)We have a problem 
	 * @param original the original text splitted into words
	 * @param modified the modified text splitted into words
	 * @return
	 */
	public static List<TextContentUpdateDiff> diff(List<String> original, List<String> modified) {
		
		Map<Integer,Map<String,Integer>> xPathPositions = new HashMap<Integer,Map<String,Integer>>();
		int[][] backtrack = null;
		try {
			backtrack = calcLCS(original, modified, xPathPositions);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(backtrack == null) {
			return null;
		}
		try {
			try {
				return LCS.readDiffsfromBackTrack(backtrack, original, 
						modified, xPathPositions);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Reads the LCS matrix and returns diffs for changed words between two sentences.
	 * @param backtrack, LCS matrix
	 * @param original, the original sentence
	 * @param modified, the modified sentence
	 * @param xPathsPosition, the xpaths for each word
	 * @return
	 * @throws Exception 
	 */
	private static List<TextContentUpdateDiff> readDiffsfromBackTrack(int[][] backtrack, 
			List<String> original, List<String> modified, 
			Map<Integer,Map<String,Integer>>  xPathsPosition)
			throws Exception {
		
		int deletedWords = 999999;
		
		Log log = Logging.getLog(LCS.class);
		log.debug("===============================");
		log.debug("Original: #0 ", original);
		log.debug("Modified: #0 ", modified);
		log.debug("===============================");
		
		List<TextContentUpdateDiff> diffs = new LinkedList<TextContentUpdateDiff>();
		/* identifies a diff with an xpath */
		Map<String,TextContentUpdateDiff> xpathdiff = new HashMap<String, TextContentUpdateDiff>();
		
		int origPos = original.size();
		int modPos = modified.size();
		if(backtrack[origPos][modPos] == original.size() 
				&& backtrack[origPos][modPos] == modified.size()) {
			// the original text and the modified text are equal
			log.debug("Text has not changed");
			return diffs;
		} else {
			while(!(origPos == 0 && modPos == 0)) {
				/* The current xpath and position inside the xpath */
				Map<String,Integer> xpath = xPathsPosition.get(modPos);
				/* The current xpath */
				String currentXPath = null;
				/* The position of the current word inside of the xpath */
				Integer posInXPath = null;
				if(xpath != null && xpath.size() != 1) {
					log.error("Unexpected size of xpath map");
					throw new Exception("Unexpected size of xpath map");
				} else if(xpath != null) {
					for(String s : xpath.keySet()) {
						currentXPath = s;
						posInXPath = xpath.get(s);
					}
				} else {
					currentXPath = "";
					posInXPath = origPos;
				}
				if(origPos == 0 && modPos != 0) {
					// A word has been added at the beginning of the sentence
					log.debug("Word #0 has been added", modified.get(modPos-1));
					
					TextContentUpdateDiff diff = null;
					if(!xpathdiff.containsKey(currentXPath)) {
						diff = new TextContentUpdateDiff(currentXPath);
						diffs.add(diff);
					} else {
						diff = xpathdiff.get(currentXPath);
					}
					
					DecimalFormat df = new DecimalFormat("000000");
					String formPos = df.format(posInXPath);
					
					String finalForm = formPos + ".000000";
					diff.getChangedWordsMap().put(finalForm, "");
					
					xpathdiff.put(currentXPath, diff);
					modPos = modPos - 1;
					
				} else if(origPos != 0 && modPos == 0) {
					// A word has been deleted at the beginning of the sentence
					log.debug("Word #0 has been deleted", original.get(origPos-1));
					
					TextContentUpdateDiff diff = null;
					if(!xpathdiff.containsKey(currentXPath)) {
						diff = new TextContentUpdateDiff(currentXPath);
						diffs.add(diff);
					} else {
						diff = xpathdiff.get(currentXPath);
					}
					
					DecimalFormat df = new DecimalFormat("000000");
					String formPos = df.format(posInXPath);
					String formDeletedWords = df.format(deletedWords);
					String finalForm = null;
					
					finalForm = formPos + "." + formDeletedWords;
					diff.getChangedWordsMap().put(finalForm, original.get(origPos-1));
					
					xpathdiff.put(currentXPath, diff);
					origPos = origPos - 1;
					deletedWords--;
				} else if(origPos == 0 && modPos == 0) {
					// An error occurred. But this is rather impossible
					log.error("[readDiffsfromBackTrack] Critical failure. This should never happen.");
					throw new RuntimeException("[readDiffsfromBackTrack] Critical " +
							"failure. This should never happen.");
				} else {
					
					/* the content of the current position in the 2D LCS-matrix (backtrack[][])*/
					int c_currentPos = backtrack[origPos][modPos];
					/* the content of the current position minus one of the 
					 * original position in the 2D LCS-matrix (backtrack[][])*/
					int c_origBefore = backtrack[origPos-1][modPos];
					/* the content of the current position minus one of the 
					 * modified position in the 2D LCS-matrix (backtrack[][])*/
					int c_modBefore = backtrack[origPos][modPos-1];
					if(c_currentPos == c_origBefore+1 && c_currentPos == c_modBefore+1) {
						// The word at currentPos has not changed
						log.debug("Word #0 has not changed", original.get(origPos-1));
						
						origPos = origPos - 1;
						modPos = modPos - 1;
					}
					else if(c_currentPos == c_origBefore && c_currentPos == c_modBefore+1) {
						// Check whether word is a opening tag and there was no added closing tag
						boolean diffExists = false;
						if(!original.get(origPos-1).startsWith("</") && 
								original.get(origPos-1).startsWith("<") && 
								!original.get(origPos-1).endsWith("/>")) {
							DecimalFormat df = new DecimalFormat("000000");
							String formPos = df.format(posInXPath);
							for(TextContentUpdateDiff d : diffs) {
								if(d.getXPath().equals(currentXPath)) {
									for(String s : d.getChangedWordsMap().keySet()) {
										if(s.startsWith(formPos)) {
											if(d.getChangedWordsMap().get(s).startsWith("</")) {
												diffExists = true;
											}
										}
									}
								}
							}
						}
						if(!original.get(origPos-1).startsWith("</") && 
								original.get(origPos-1).startsWith("<") && 
								!diffExists && !original.get(origPos-1).endsWith("/>")) {
							
							// TODO: get xpath of the next word, this should be the same tag as the current word
							Integer tmpPos = origPos-1;
							String tmpTag = null;
							while(!getTagnameByWord(original.get(origPos-1))
									.equals(getTagnameByWord(tmpTag)) && tmpPos > 0) {
								if(!original.get(tmpPos-1).startsWith("</") && 
										original.get(tmpPos-1).startsWith("<")) {
									tmpTag = original.get(tmpPos-1);
								} else {
									tmpTag = null;
								}
								tmpPos--;
							}
							// The word  at currentPos has been deleted
							log.debug("Word #0 has been deleted", original.get(origPos-1));
							
							TextContentUpdateDiff diff = null;
							if(!xpathdiff.containsKey(currentXPath)) {
								diff = new TextContentUpdateDiff(currentXPath);
								diffs.add(diff);
							} else {
								diff = xpathdiff.get(currentXPath);
							}
							
							DecimalFormat df = new DecimalFormat("000000");
							String formPos = df.format(posInXPath);
							String formDeletedWords = df.format(1);
							String finalForm = null;
							
							finalForm = formPos + "." + formDeletedWords;
							diff.getChangedWordsMap().put(finalForm, original.get(origPos-1));
							
							xpathdiff.put(currentXPath, diff);
							origPos = origPos - 1;
						} else {
							// The word  at currentPos has been deleted
							log.debug("Word #0 has been deleted", original.get(origPos-1));
							
							TextContentUpdateDiff diff = null;
							if(!xpathdiff.containsKey(currentXPath)) {
								diff = new TextContentUpdateDiff(currentXPath);
								diffs.add(diff);
							} else {
								diff = xpathdiff.get(currentXPath);
							}
							
							DecimalFormat df = new DecimalFormat("000000");
							String formPos = df.format(posInXPath);
							String formDeletedWords = df.format(deletedWords);
							String finalForm = null;
							
							finalForm = formPos + "." + formDeletedWords;
							diff.getChangedWordsMap().put(finalForm, original.get(origPos-1));
							
							xpathdiff.put(currentXPath, diff);
							origPos = origPos - 1;
							deletedWords--;
						}
					}
					else if(c_currentPos == c_origBefore+1 && c_currentPos == c_modBefore) {
						// Check whether word is a opening tag and there was no added closing tag
						boolean diffExists = false;
						if(!modified.get(modPos-1).startsWith("</") && 
								modified.get(modPos-1).startsWith("<") && 
								!modified.get(modPos-1).endsWith("/>")) {
							for(TextContentUpdateDiff d : diffs) {
								if(d.getXPath().equals(currentXPath)) {
									if(d.getChangedWordsMap().containsKey("999999.000000")) {
										diffExists = true;
									}
								}
							}
						}
						if(!modified.get(modPos-1).startsWith("</") && 
								modified.get(modPos-1).startsWith("<") && 
								!diffExists && !modified.get(modPos-1).endsWith("/>")) {
							
							// TODO: get xpath of the next word, this should be the same tag as the current word
							String tmpNextXPath = null;
							Integer tmpPos = modPos-1;
							while(!getTagnameByWord(modified.get(modPos-1))
									.equals(getTagnameByXPath(tmpNextXPath)) && tmpPos >= 0) {
								Map<String,Integer> tmpXPath = xPathsPosition.get(tmpPos);
								if(tmpXPath != null && tmpXPath.size() != 1) {
									log.error("Unexpected size of tmpXPath map");
									throw new Exception("Unexpected size of tmpXPath map");
								} else if(tmpXPath != null) {
									for(String s : tmpXPath.keySet()) {
										tmpNextXPath = s;
									}
									tmpPos--;
								} else {
									if(modPos > 1) {
										throw new Exception("Problem with getting the xpath for the word " + modified.get(modPos-2));
									} else {
										throw new TagElementIsCorruptException("It seams that the xml document is not wellformed " + modified);
									}
								}
							}
							
							// Check whether last added word is the same tag
							if(getTagnameByWord(modified.get(modPos-1))
									.equals(getTagnameByXPath(tmpNextXPath))) {
								// The opening tag is the same as the next opening tag
								// -> handle the first opening tag as if it hasn't been changed
								log.debug("Tag #0 under xpath #1 has not changed", modified.get(modPos-1), currentXPath);
								
//								origPos = origPos - 1;
								modPos = modPos - 1;
								
								// we then have to add the next opening tag to the diff
								log.debug("Tag #0 under #1 has been added", modified.get(modPos), tmpNextXPath);
								
								TextContentUpdateDiff diff = null;
								if(!xpathdiff.containsKey(tmpNextXPath)) {
									diff = new TextContentUpdateDiff(tmpNextXPath);
									diffs.add(diff);
								} else {
									diff = xpathdiff.get(tmpNextXPath);
								}
								
								DecimalFormat df = new DecimalFormat("000000");
								String formPos = df.format(0);
								
								String finalForm = formPos + ".000000";
								diff.getChangedWordsMap().put(finalForm, "");
								
								xpathdiff.put(tmpNextXPath, diff);
							} else {
								throw new TagElementIsCorruptException("It seams that the xml document is not wellformed " + modified);
							}
						} else {
						
							// The word  at currentPos has been added
							log.debug("Word #0 has been added", modified.get(modPos-1));
							
							TextContentUpdateDiff diff = null;
							if(!xpathdiff.containsKey(currentXPath)) {
								diff = new TextContentUpdateDiff(currentXPath);
								diffs.add(diff);
							} else {
								diff = xpathdiff.get(currentXPath);
							}
							
							DecimalFormat df = new DecimalFormat("000000");
							String formPos = df.format(posInXPath);
							
							String finalForm = formPos + ".000000";
							diff.getChangedWordsMap().put(finalForm, "");
							
							xpathdiff.put(currentXPath, diff);
							modPos = modPos - 1;
						}
					}
					else if(c_currentPos == c_origBefore && c_currentPos == c_modBefore) {
						// The word  at currentPos has been modified (deleted old and added new)
						log.debug("Word #0 has been changed to #1 ", 
								original.get(origPos-1), modified.get(modPos-1));
						
						TextContentUpdateDiff diff = null;
						if(!xpathdiff.containsKey(currentXPath)) {
							diff = new TextContentUpdateDiff(currentXPath);
							diffs.add(diff);
						} else {
							diff = xpathdiff.get(currentXPath);
						}
						
						DecimalFormat df = new DecimalFormat("000000");
						String formPos = df.format(posInXPath);
						String formDeletedWords = df.format(deletedWords);
						
						String finalForm1 = formPos + "." + formDeletedWords;
						diff.getChangedWordsMap().put(finalForm1, original.get(origPos-1));
						
						String finalForm2 = formPos + ".000000";
						diff.getChangedWordsMap().put(finalForm2, "");
						
						xpathdiff.put(currentXPath, diff);
						origPos = origPos - 1;
						modPos = modPos - 1;
					}
				}
			}
		}
		log.debug("Text has #0 diffs", diffs.size());
		return diffs;
	}
	
	private static String getTagnameByWord(String word) {
		if(word == null) {
			return null;
		}
		String[] tmp = word.split("[{]attrGap[}]");
		// delete the first '<'
		String tmpTag = tmp[0].substring(1);
		StringBuilder tag = new StringBuilder();
		
		if(tmpTag.toString().endsWith("/>")) {
			// delete the last two characters '/>'
			tag.append(tmpTag.substring(0, tmpTag.length()-2));
		} else if (tmpTag.toString().endsWith(">")) {
			// delete the last character '>'
			tag.append(tmpTag.substring(0, tmpTag.length()-1));
		} else {
			tag.append(tmpTag);
		}
		return tag.toString();
	}
	
	private static String getTagnameByXPath(String xpath) {
		if(xpath == null) {
			return null;
		}
		String[] tmp = xpath.split("/");
		String tagAndCount = tmp[tmp.length-1];
		String[] tagName = tagAndCount.split("\\[");
		if(tagName.length > 0) {
			if(tagName[1].contains("local-name()")) {
				String[] tagSplit1 = tagName[1].split("local-name\\(\\)\\=\\'");
				String[] tagSplit2 = tagSplit1[tagSplit1.length-1].split("\\'");
				return tagSplit2[0];
			} else {
				return tagName[0];
			}
		} else {
			return null;
		}
	}
}
