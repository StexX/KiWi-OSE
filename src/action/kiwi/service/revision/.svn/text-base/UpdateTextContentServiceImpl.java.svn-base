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
package kiwi.service.revision;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.mail.MethodNotSupportedException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.revision.UpdateTextContentServiceLocal;
import kiwi.api.revision.UpdateTextContentServiceRemote;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.exception.ContentItemMissingException;
import kiwi.exception.CouldNotCreateStyleException;
import kiwi.exception.TagElementIsCorruptException;
import kiwi.exception.TextCountReachedException;
import kiwi.exception.XHTMLFormatException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.content.TextContent;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.TextContentUpdate;
import kiwi.model.revision.TextContentUpdateDiff;
import kiwi.service.config.ConfigurationServiceImpl;
import kiwi.util.KiWiStringUtils;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * This service prepares textcontent changes for updates. 
 * It is committed when transaction reached the beforeCompletion()
 * method.
 * @author 	Stephanie Stroka 
 * 			(sstroka@salzburgresearch.at)
 *
 */
@Stateless
@Name("updateTextContentService")
@AutoCreate
@Scope(ScopeType.STATELESS)
public class UpdateTextContentServiceImpl implements Serializable, 
		UpdateTextContentServiceLocal, UpdateTextContentServiceRemote {
	
	/**
	 * Generated serialization id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * injected Logger
	 */
	@Logger
	private static Log log;
	
	/**
	 * transaction service, needed to add the update to a transaction resource
	 */
	@In
    private TransactionService transactionService;
	
	@In
	private EntityManager entityManager;
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTextContentService#updateTextContent(kiwi.model.content.ContentItem, java.lang.String)
	 */
	public TextContentUpdate updateTextContent(ContentItemI itemI, TextContent tc) {
		
		ContentItem item = itemI.getDelegate();
			
		// first check if the content item contains a collection of revisions
		if(item.getVersions() == null) {
			// if it doesn't, create one
			item.setVersions(new ArrayList<CIVersion>());
		}

		// create new TextContentUpdate
		TextContentUpdate tcu = new TextContentUpdate();

		TextContent old = null;
		String lastContent = null;
		// does a text content exist?
		if(item.getTextContent() == null) {
			/*Can't use storingPipeline here, concurrent access to the storingPipeline
			 *  if this service called from a savelet (ComponentSavelet
			 *   needs to use the updateTextContent service to update the components) */
			//lastContent = storingPipeline.processHtmlSource(item.getResource(), " ");
			lastContent = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:type=\"page\"> </div>";
		} else {
			old = item.getTextContent();
			lastContent = old.getXmlString();
		}

		// generate changes between previous and current text content
		List<TextContentUpdateDiff> changes = generateChanges(tc.getXmlString(), lastContent);
		tcu.setChanges(changes);

		tcu.setTextContent(tc);

		EntityManager entityManager = (EntityManager) 
		Component.getInstance("entityManager");
		TripleStore tripleStore = (TripleStore) 
		Component.getInstance("tripleStore");
		entityManager.persist(tc);
		tripleStore.persist(item);
		//		kiwiEntityManager.persist(item);

		// add this update to the current transaction data
		try {
			transactionService.getTransactionCIVersionData(item).setTextContentUpdate(tcu);
			log.debug("UpdateTextContentServiceImpl.updateTextContent() called ts.getTransactionCIVersionData()");
		} catch (ContentItemMissingException e) {
			e.printStackTrace();
		}

		return tcu;
	}
	
	/**
	 * generates the changes between original and modified text
	 * @param curXMLContent
	 * @param prevXMLContent
	 * @return a list of diffs between original and modified text
	 */
	public List<TextContentUpdateDiff> generateChanges( String 
			curXMLContent, String prevXMLContent ) {
		// preparing the text means to add spaces between tags, 
		// to delete line feeds and to put replacement character  
		// for spaces inside of a tag
		curXMLContent = KiWiStringUtils.prepareHtmlText(curXMLContent);
		curXMLContent.trim();
		prevXMLContent = KiWiStringUtils.prepareHtmlText(prevXMLContent);
		prevXMLContent.trim();
		
		// create a list of strings of the words in the previous text
		List<String> origwords = new ArrayList<String>( 
				Arrays.asList(prevXMLContent.split(" ")) );
		// and remove all empty entries
		while(origwords.remove(""));
		
		// create a list of strings of the words in the current text
		List<String> modwords = new ArrayList<String>( 
				Arrays.asList(curXMLContent.split(" ")) );
		// and remove all empty entries
		while(modwords.remove(""));
		// return the differenced listed in a string
		return LCS.diff(origwords, modwords);
	}
	
	/**
	 * Surrounds the word(s) that have changed with a style tag.
	 * @param content
	 * @param additionalAtts
	 * @return
	 */
	private Element addRevStyleElement(String content, Attribute... additionalAtts) {
		// create a new <span> element
		Element span = new Element("span");
		span.addAttribute(new Attribute("class"
				,"revision"));
		if(additionalAtts != null) {
			for(int i = 0; i < additionalAtts.length; i++) {
				span.addAttribute(additionalAtts[i]);
			}
		}
		// and add the string with a space as a child node
		span.appendChild(content + " ");
		return span;
	}
	
	/**
	 * Surrounds the word(s) that have been deleted with a style tag.
	 * @param content
	 * @param additionalAtts
	 * @return
	 */
	private Element addRevDelStyleElement(String content, Attribute... additionalAtts) {
		// create a new <span> element
		Element span = new Element("span");
		span.addAttribute(new Attribute("class"
				,"revision_deleted"));
		if(additionalAtts != null) {
			for(int i = 0; i < additionalAtts.length; i++) {
				span.addAttribute(additionalAtts[i]);
			}
		}
		// and add the string with a space as a child node
		span.appendChild(content + " ");
		return span;
	}
	
	/**
	 * Returns the xom-node that has been changed in the TextContentUpdate.
	 * 
	 * @param currentDoc
	 * @param d
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	private Node getDiffNode(Document currentDoc, TextContentUpdateDiff d) throws IndexOutOfBoundsException {
		Nodes nodes = currentDoc.query(d.getXPath());
		if(currentDoc == null) {
			log.error("Queried node #0 is null", d.getXPath());
			return null;
		}
		log.info("Document: #0 ", currentDoc.toXML());
		
		// we expect the nodelist to contain just 1 node
		if(nodes.size() != 1) {
			throw new IndexOutOfBoundsException("Expected one node, but received: " + nodes.size());
		}
		
		return nodes.get(0);
		
	}
	
	/**
	 * 
	 * @param keySet
	 * @param n
	 * @return
	 * @throws Exception
	 */
	private Set<String> checkSurroundingTag(Set<String> keySet, Node n) throws Exception {
		DecimalFormat df = new DecimalFormat("000000");
		if(keySet.contains("000000.000000") && keySet.contains(df.format(999999) + ".000000")) {
			log.debug("Node has been added");
			// create a new element <span>
			Element info = new Element("span");
			info.addAttribute(new Attribute("class", "info"));
			Element text = new Element("span");
			text.addAttribute(new Attribute("class", "text"));
			text.appendChild("Format has changed");
			info.appendChild(text);
			n.getParent().replaceChild(n, info);
			info.appendChild(n);
			if(!keySet.remove("000000.000000")) {
				log.error("The element \"000000.000000\" has to be removable");
			}
			if(!keySet.remove(df.format(999999) + ".000000")) {
				log.error("The element \""+df.format(999999) +".000000\" has to be removable");
			}
		} else if(keySet.contains("000000.000000")) {
			log.error("Opening tag has been added, but closing tag hasn't!");
			throw new XHTMLFormatException("Closing tag has been added, but openening tag hasn't!");
		} else if(keySet.contains(df.format(999999) + ".000000")) {
			log.error("Closing tag has been added, but openening tag hasn't!");
			throw new XHTMLFormatException("Closing tag has been added, but openening tag hasn't!");
		}
		return keySet;
	}
	
	/**
	 * Helper method that extracts the name of the tag from the tag.
	 * @param str
	 * @return
	 */
	private Element extractTagFromString(String str) {
		if(!str.startsWith("</") && str.startsWith("<")) {
			String[] tmp = str.split("[{]attrGap[}]");
			// delete the first '<'
			String tmpTag = tmp[0].substring(1);
			String tag = new String();
			
			if(tmpTag.toString().endsWith("/>")) {
				// delete the last two characters '/>'
				tag = tmpTag.substring(0, tmpTag.length()-2);
			} else if (tmpTag.toString().endsWith(">")) {
				// delete the last character '>'
				tag = tmpTag.substring(0, tmpTag.length()-1);
			} else {
				tag = tmpTag;
			}
			
			Element element = new Element(tag);
			
			for(int i = 1; i < tmp.length; i++) {
				String[] attrArray = tmp[i].split("=\"");
				if(attrArray.length != 2) {
					attrArray = tmp[i].split("='");
				}
				if(attrArray.length == 2) {
					String value = attrArray[1].substring(0, attrArray.length-1);
					element.addAttribute(new Attribute(attrArray[0], value));
				}
			}
			return element;
		}
		return null;
	}
	
	/**
	 * Adds space characters and the word to the new formatted text.
	 * @param newContent
	 * @param word
	 * @throws TextCountReachedException
	 */
	private void appendTextBuffer(StringBuilder newContent, String word) throws TextCountReachedException {
		// check if the current textposition is NOT marked as a changed word position ..
		// for every word that hasn't changed, add it to the buffer
		if(newContent.length()>0 && !newContent.toString().endsWith(" ")) {
			log.debug("Adding word #0 ", word);
			newContent.append(" " + word + " ");
		} else {
			log.debug("Adding word #0 ", word);
			newContent.append(word + " ");
		}
	}
	
	/**
	 * Sets the <span style..> tags for displaying revision changes
	 * @param current
	 * @param currentPos
	 * @param diff
	 * @param styled
	 * @param showDeleted shows or doesn't show the deleted words
	 * @return a list of strings which form a preview of the styled text
	 * @throws Exception 
	 */
	public nu.xom.Document setStyleWithXOM(nu.xom.Document doc, 
			List<TextContentUpdateDiff> diff, Long revisionId, 
			boolean showDeleted) throws Exception {
		
		Collections.sort(diff);
		// deep copy, do not violate the original document
		nu.xom.Document currentDoc = (Document) doc.copy();
		for(TextContentUpdateDiff d : diff) {
			
			Set<String> keySet = new HashSet<String>( d.getChangedWordsMap().keySet() );
			
			// query the diff node
			Node node = null;
			try {
				node = getDiffNode(currentDoc, d);
			} catch (IndexOutOfBoundsException e1) {
				log.error("Expected one node");
				return currentDoc;
			}
			Element newElement = null;
			Element e = null;
			// if the node is an Element we can cast it
			if(node instanceof Element) {
				e = (Element) node;
				newElement = new Element(e.getQualifiedName());
			} else {
				log.error("Expected an element, but was: #0 ", node.getClass());
				return currentDoc;
			}
			
			keySet = checkSurroundingTag(keySet, node);
			
			// get the positions that indicate 
			// changed words in this xpath location
			List<String> positions = new ArrayList<String>( keySet );
			// sort the positions
			Collections.sort(positions);
			log.debug("Positions in xpath #0: #1 ", d.getXPath(), positions);
			Iterator<String> iter = positions.iterator();
			
			// if there exist no positions -> break for loop
			if(iter.hasNext()) {
				
				Integer wordCountInXPath = new Integer(0);
				
				// new string buffer for text content
				StringBuilder newContent = new StringBuilder();
	
				Integer position = null;
				Integer fraction = null;
				String sPos = null;
				boolean interrupt = false;
				
				log.debug("This element contains #0 nodes", e.getChildCount());
				// get the child nodes of this node
				for(int i=0; i<e.getChildCount(); i++) {
					
					Node child = e.getChild(i);
					// if there is a text child node ....
					if(child instanceof Text) {
						if(!interrupt) {
							wordCountInXPath++;
						}
						Text c = (Text) child;
						log.debug("Node is a text: #0 ", c.getValue());
						// split the text into words
						List<String> text = new ArrayList<String>(Arrays.asList(c.getValue().trim().split(" ")));
						
						while(text.remove(""));
						
						if(!interrupt) {
							if(iter.hasNext()) {
								sPos = iter.next();
								
								if(sPos.split("\\.") != null && sPos.split("\\.").length == 2) {
									position = new Integer(sPos.split("\\.")[0]);
									fraction = new Integer(sPos.split("\\.")[1]);
								} else {
									position = new Integer(sPos);
									fraction = new Integer(0);
								}
								log.debug("checking word at position #0 with fraction #1 ", position, fraction);
							} else {
								position = null;
								fraction = null;
								log.debug("There are no more changed positions");
							}
						} else {
							interrupt = false;
						}
						
						int tmpAmountOfWords = wordCountInXPath+text.size()-1;
						log.debug("tmpAmountOfWords: #0 ", tmpAmountOfWords);
						// if the position is not in that element
						if(position == null || position > tmpAmountOfWords) {
							// just add the element
							Text newText = (Text) c.copy();
							newElement.appendChild(newText);
							// and append a space character to the buffer
							newContent.append(" ");
							wordCountInXPath = tmpAmountOfWords + 1;
						}
						// else if the position is in that element
						else {
							// iterate through the text
							for(String word : text) {
								Element deletedElement = null;
								log.debug("Checking word: #0 ", word);
								// while the amount of words unter 
								// that xpath are less than the 
								// position at which a word has changed
								if(position == null || 
										(fraction != null && !fraction.equals(0) 
												&& wordCountInXPath.equals(position)) || 
										wordCountInXPath < position) {
									
									if(position != null && wordCountInXPath < position) {
										log.debug("wordCountInXPath #0 < position #1 ", wordCountInXPath, position);
									} else if(position != null && (fraction != null && !fraction.equals(0) 
											&& wordCountInXPath.equals(position))) {
										log.debug("wordCountInXPath #0 <= position #1 ", wordCountInXPath, position);
									} else {
										log.debug("appending the rest of the text, wordCountInXPath #0", wordCountInXPath);
									}
									try {
										// append the unchanged text
										appendTextBuffer(newContent, word);
										wordCountInXPath++;
									} catch (TextCountReachedException e1) {
										throw new CouldNotCreateStyleException("Style could not be created");
									}
								}
								else if(position != null && position > tmpAmountOfWords) {
									log.error("Position ( #0 ) cannot be bigger than text size ( #1 ) ", position, text.size());
								} 
								else if(position != null && fraction.equals(0) && wordCountInXPath >= position) {
									log.debug("At this position #0 , something has been changed", position);
									// if there is something in the buffer, 
									// append it as a text node
									if(newContent.length()>0) {
										newElement.appendChild(newContent.toString());
										newContent = new StringBuilder();
									}
									newElement.appendChild(addRevStyleElement(word));
									wordCountInXPath++;
									if(iter.hasNext()) {
										sPos = iter.next();
										
										if(sPos.split("\\.") != null && sPos.split("\\.").length == 2) {
											position = new Integer(sPos.split("\\.")[0]);
											fraction = new Integer(sPos.split("\\.")[1]);
										} else {
											position = new Integer(sPos);
											fraction = new Integer(0);
										}
										log.debug("checking word at position #0 with fraction #1 ", position, fraction);
									} else {
										sPos = null;
										position = null;
										log.debug("There are no more changed positions");
									}
								} 
								if(position != null && wordCountInXPath > position) {
									Integer lastPosition = position;
									while(position != null && ((lastPosition.equals(position) && 
											!fraction.equals(0)) || position == 0)) {
										
										// if there is something in the buffer, 
										// append it as a text node
										if(newContent.length()>0) {
											newElement.appendChild(newContent.toString());
											newContent = new StringBuilder();
										}
										// we add the string and decorate it with a 'text-decoration: line-through' style
										String prevWord;
										if((prevWord = d.getChangedWordsMap().get(sPos)) != null) {
											prevWord = prevWord.trim(); 
											if(showDeleted && !prevWord.equals("")) {
												// finally, append the new element
												if(prevWord.startsWith("<")) {
													if(!prevWord.startsWith("</")) {
														deletedElement = extractTagFromString(prevWord);
													} else {
														if(deletedElement != null) {
															newElement.appendChild(deletedElement);
															deletedElement = null;
														}
													}
												} else {
													if(deletedElement != null) {
														deletedElement.appendChild(addRevDelStyleElement(prevWord));
													} else {
														newElement.appendChild(addRevDelStyleElement(prevWord));
													}
												}
											}
										}
										
										if(iter.hasNext()) {
											sPos = iter.next();
											
											if(sPos.split("\\.") != null && sPos.split("\\.").length == 2) {
												position = new Integer(sPos.split("\\.")[0]);
												fraction = new Integer(sPos.split("\\.")[1]);
											} else {
												position = new Integer(sPos);
												fraction = new Integer(0);
											}
											log.debug("checking word at position #0 with fraction #1 ", position, fraction);
										} else {
											lastPosition = -1;
											sPos = null;
											position = null;
											log.debug("There are no more changed positions");
										}
									}		
								}
							}
							// if there is something in the buffer, 
							// append it as a text node
							if(newContent.length()>0) {
								newElement.appendChild(newContent.toString());
								newContent = new StringBuilder();
							}
						}
					}
					// if the child is another element (e.g. <b> ..)
					else if (child instanceof Element){
						log.debug("Node is another element: #0 ", ((Element) child).getLocalName());
						// if we don't have any changes at this 
						// position, just add the element
						Element newChild = (Element) child.copy();
						newElement.appendChild(newChild);
						// and append a space character to the buffer
						newContent.append(" ");
						interrupt = true;
					}
				}
				// replace the previous node with the new node
			node.getParent().replaceChild(node, newElement);
			}
			log.info("Document: #0 ", currentDoc.toXML());
		}
		return currentDoc;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTextContentService#createPreview(
	 * 				kiwi.model.revision.TextContentUpdate, 
	 * 				kiwi.api.revision.UpdateTextContentService.PreviewStyle, 
	 * 				boolean)
	 */
	public String createPreview(CIVersion version, 
			PreviewStyle style, boolean showDeleted) {
		
		String preview = null;
		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		if(version.getTextContentUpdate() == null) {
			javax.persistence.Query q = entityManager.createNamedQuery("version.lastTextContent");
			q.setParameter("ci", version.getRevisedContentItem());
			q.setParameter("vid", version.getVersionId());
			q.setMaxResults(1);
			TextContent textContent;
			try {
				textContent = (TextContent) q.getSingleResult();
				preview = textContent.getHtmlContent();
			} catch (NoResultException e) {
				preview = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants
					.NS_KIWI_HTML+"\" kiwi:type=\"page\"><i>Initial ContentItem creation</i></div>";
			}
		} else {
		
			TextContentUpdate tcu = version.getTextContentUpdate();
			
			switch (style) {
			case LAST:
				if((preview = tcu.getPreviewText()) == null) {
						TextContent tc = tcu.getTextContent();
						Document doc = tc.getXmlDocument();
						// creates a styled xml tree
						try {
							doc = setStyleWithXOM(doc, tcu.getChanges(), version.getVersionId(), showDeleted);
						} catch (CouldNotCreateStyleException e) {
							e.printStackTrace();
							log.error("#0", e);
						} catch (TagElementIsCorruptException e) {
							e.printStackTrace();
							log.error("#0", e);
						} catch(Exception e) {
							e.printStackTrace();
							log.error("#0", e);
						} finally {
							log.info("Document: #0 ", doc.toXML());
							preview = doc.toXML();
						}
				}
				break;
			case ALLAUTHORS:
				if((preview = tcu.getPreviewText()) == null) {
					ConfigurationService conf = (ConfigurationService) Component
							.getInstance("configurationService");
					try {
						Builder build = new Builder();
						TextContent tc = tcu.getTextContent();
						Document doc = build.build(tc.getXmlString(), 
								conf.getBaseUri());
						// creates a styled xml tree
						try {
							doc = setStyleWithXOM(doc, tcu.getChanges(), version.getVersionId(), showDeleted);
						} catch (CouldNotCreateStyleException e) {
							log.error("#0", e);
							e.printStackTrace();
						} catch (TagElementIsCorruptException e) {
							log.error("#0", e);
							e.printStackTrace();
						} catch(Exception e) {
							log.error("#0", e);
							e.printStackTrace();
						} finally {
							log.info("Document: #0 ", doc.toXML());
							preview = doc.toXML();
						}
					} catch (ValidityException e) {
						log.error("#0", e);
					} catch (ParsingException e) {
						log.error("#0", e);
					} catch (IOException e) {
						log.error("#0", e);
					}
				}
				break;
			case ALLUPDATES:
				if((preview = tcu.getPreviewText()) == null) {
					ConfigurationService conf = (ConfigurationService) Component
							.getInstance("configurationService");
					try {
						Builder build = new Builder();
						TextContent tc = tcu.getTextContent();
						Document doc = build.build(tc.getXmlString(), 
								conf.getBaseUri());
						// creates a styled xml tree
						try {
							doc = setStyleWithXOM(doc, tcu.getChanges(), version.getVersionId(), showDeleted);
						} catch (CouldNotCreateStyleException e) {
							log.error("#0", e);
							e.printStackTrace();
						} catch (TagElementIsCorruptException e) {
							log.error("#0", e);
							e.printStackTrace();
						} catch(Exception e) {
							log.error("#0", e);
							e.printStackTrace();
						} finally {
							log.info("Document: #0 ", doc.toXML());
							preview = doc.toXML();
						}
					} catch (ValidityException e) {
						log.error("#0", e);
					} catch (ParsingException e) {
						log.error("#0", e);
					} catch (IOException e) {
						log.error("#0", e);
					}
				}
				break;
			default: 
				break;
			}
		}
		return preview;
	}
	
	/**
	 * converts org.w3c.dom.Document into nu.xom.Document
	 * @param dom
	 * @return
	 */
	public nu.xom.Document dom2xom( org.w3c.dom.Document dom ) {
		return nu.xom.converters.DOMConverter.convert(dom);
	}
	
	/**
	 * converts a nu.xom.Document into org.w3c.dom.Document
	 * @param xom
	 * @return
	 */
	public org.w3c.dom.Document xom2dom( nu.xom.Document xom ) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return nu.xom.converters.DOMConverter.convert(xom, builder.getDOMImplementation());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTextContentService#undo(kiwi.model.revision.TextContent.TextContentUpdate)
	 */
	public void restore(CIVersion vers) {
		TextContent tc = null;
		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		if(vers.getTextContentUpdate() == null) {
			javax.persistence.Query q = entityManager.createNamedQuery("version.lastTextContent");
			q.setParameter("ci",vers.getRevisedContentItem());
			q.setParameter("vid",vers.getVersionId());
			q.setMaxResults(1);
			try {
				tc = (TextContent) q.getSingleResult();
			} catch (NoResultException ex) {
				if(!ConfigurationServiceImpl.testing) {
					log.error("The TextContentUpdate is linking to an invalid TextContent");
//					ex.printStackTrace();
				}
			}
		} else {
			tc = vers.getTextContentUpdate().getTextContent();
		}
		if(tc != null) {
			ContentItemService cis = (ContentItemService) 
					Component.getInstance("contentItemService");
			ContentItem item = vers.getRevisedContentItem();
			
			cis.updateTextContentItem(item, tc.getXmlString());
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTextContentService#commitUpdate(kiwi.model.revision.TextContent.TextContentUpdate)
	 */
	public void commitUpdate(CIVersion version) {
		if(version.getTextContentUpdate() != null) {
			entityManager.persist(version.getTextContentUpdate());
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTextContentService#rollbackUpdate(kiwi.model.revision.TextContent.TextContentUpdate)
	 */
	public void rollbackUpdate(CIVersion version) {
		version.setTextContentUpdate(null);
	}

	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.KiWiUpdateService#undo(kiwi.model.revision.CIVersion)
	 */
	@Override
	public void undo(CIVersion version) throws ContentItemDoesNotExistException {
		try {
			throw new MethodNotSupportedException("UpdateTextContentService.restore(CIVersion version) is not supported");
		} catch (MethodNotSupportedException e) {
			e.printStackTrace();
		}
	}
}
