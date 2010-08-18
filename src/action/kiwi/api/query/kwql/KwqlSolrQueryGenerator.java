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
package kiwi.api.query.kwql;

import java.util.Collection;

import kiwi.model.Constants;
import kiwi.service.query.kwql.parser.KWQL;
import kiwi.util.MD5;

import org.antlr.runtime.tree.Tree;
import org.apache.solr.client.solrj.SolrQuery;

public class KwqlSolrQueryGenerator {
	
	/**
	 * Converts the parts of an Antl syntax tree which are expressible by Solr into a SolrQuery.
	 * 
	 * @param ast the root of the tree
	 * @return a Solr query that can be evaluated by the Solr api 
	 */
	public static SolrQuery buildSolrQuery(Tree body, Collection<String> interdependentVariables) {
		SolrQuery solrQuery = new SolrQuery();
		
		String solrQueryString = KwqlSolrQueryGenerator.convertQuery(body, interdependentVariables);

		solrQuery.setQuery(solrQueryString);
		
		solrQuery.setRows(Integer.MAX_VALUE);
		solrQuery.setFields("id","title", "kiwiid", "tag", "score");
		
		solrQuery.setHighlight(true);
		solrQuery.setHighlightFragsize(200);
		solrQuery.setHighlightSimplePre("<span class=\"highlight\">");
		solrQuery.setHighlightSimplePost("</span>");

		return solrQuery;
	}
	
	/**
	 * Traverses an Antl tree in depth first order and converts it into a Solr query String.
	 * 
	 * @param ast the root of the tree
	 * @return a Solr query String
	 */
	private static String convertQuery(Tree body, Collection<String> interdependentVariables) {
		StringBuilder query = new StringBuilder();

		convertQuery(body, interdependentVariables, true, query);
		
		return query.toString();
	}
	
	/**
	 * Traverses an Antl tree in depth first order and converts it into a Solr query String. 
	 * 
	 * @param node the root of the tree
	 * @param query the so far generated Solr query
	 */	
	private static void convertQuery(Tree node, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		int backtrackPosition = query.length();
		
		if (node.getType() == KWQL.NEGATION) {
			query.append("(*:* AND ");				//workaround for Solr bug
			query.append("-");
			
			convertQuery(node.getChild(0), interdependentVariables, false, query);
			
			query.append(")"); 
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION || node.getType() == KWQL.DISJUNCTION) {
			query.append("(");

			convertQuery(node.getChild(0), interdependentVariables, quantify, query);
			
			query.append(" ");
			query.append(node.toString());
			query.append(" ");
			
			convertQuery(node.getChild(1), interdependentVariables, quantify, query);
			
			query.append(")");
		} else if (node.getType() == KWQL.RESOURCE) {
			query.append("(");
			
			query.append("u_");
			query.append(MD5.md5sum(Constants.NS_RDF + "type"));
			query.append(":");
			
			query.append("uri\\:\\:");
			query.append(Constants.NS_KIWI_CORE.replace(":", "\\:"));
			query.append("ContentItem");
			
			backtrackPosition = query.length();
			
			query.append(" AND ");

			if (convertResource(node, interdependentVariables, quantify, query) != SolrBacktrackingOption.NONE) {
				query.setLength(backtrackPosition);
			}
			
			query.append(")");
		}
	}
	
	private static SolrBacktrackingOption convertResource(Tree node, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		return convertResource(node.getChild(1), node.getChild(0).getChild(0), interdependentVariables, quantify, query);
	}
	
	private static SolrBacktrackingOption convertResource(Tree node, Tree type, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		SolrBacktrackingOption backtrack = SolrBacktrackingOption.NONE;
		int backtrackPosition = query.length();
		
		if (type.getType() == KWQL.DISJUNCTION) {
			SolrBacktrackingOption localBacktracking;
			
			query.append("(");

			backtrackPosition = query.length();
			
			localBacktracking = backtrack = convertResource(node, type.getChild(0), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			} else {
				backtrackPosition = query.length();

				query.append(" OR ");
			}
			
			localBacktracking = convertResource(node, type.getChild(1), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			}
			
			if (backtrack != SolrBacktrackingOption.NONE && localBacktracking != SolrBacktrackingOption.NONE) {
				backtrack = SolrBacktrackingOption.QUALIFIER;
			} else {
				backtrack = SolrBacktrackingOption.NONE;
			}
			
			query.append(")");
		} else if (type.getType() == KWQL.CI) {
			return convertContentItem(node, interdependentVariables, quantify, query);
		} else if (type.getType() == KWQL.TAG) {
			return convertTag(node, KwqlResource.ci, interdependentVariables, quantify, query);
		} else if (type.getType() == KWQL.FRAG) {
			return convertFragment(node, interdependentVariables, quantify, query);
		} else if (type.getType() == KWQL.LINK) {
			return SolrBacktrackingOption.QUALIFIER;
		} else {
			throw new UnexpectedNodeException();
		}
		
		return backtrack;
	}
	
	/**
	 * Converts qualifiers of a content item into a Solr query string.
	 *  
	 * @param node the node of the ast that should be converted
	 * @param query the so far produced query string
	 * @return true, if the properties of node cannot be expressed by Solr and therefore backtracking is needed
	 */
	private static SolrBacktrackingOption convertContentItem(Tree node, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		SolrBacktrackingOption backtrack = SolrBacktrackingOption.NONE;
		int backtrackPosition = query.length();

		if (node.getType() == KWQL.NEGATION) {
			query.append("(*:* AND ");			//workaround for Solr bug
			query.append("-");
			
			backtrack = convertContentItem(node.getChild(0), interdependentVariables, false, query);
			
			query.append(")"); 
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION || node.getType() == KWQL.DISJUNCTION) {
			SolrBacktrackingOption localBacktracking;
			
			query.append("(");

			backtrackPosition = query.length();
			
			localBacktracking = backtrack = convertContentItem(node.getChild(0), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			} else {
				backtrackPosition = query.length();

				query.append(" ");
				query.append(node.toString());
				query.append(" ");
			}
			
			localBacktracking = convertContentItem(node.getChild(1), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			}
			
			if (backtrack != SolrBacktrackingOption.NONE && localBacktracking != SolrBacktrackingOption.NONE) {
				backtrack = SolrBacktrackingOption.QUALIFIER;
			} else {
				backtrack = SolrBacktrackingOption.NONE;
			}
			
			query.append(")");
		} else if (node.getType() == KWQL.QUALIFIER) {
			StringBuilder sb = new StringBuilder();
			Tree label = node.getChild(0).getChild(0);
			
			if (label == null) {
				query.append("(");

				sb.setLength(0);				
				convertLabel(node.getChild(1), interdependentVariables, quantify, sb);
				
				for (KwqlContentItemQualifier qualifier : KwqlContentItemQualifier.values()) {
					if (qualifier.isField() && qualifier.isApplicable(sb)) {									//only use qualifier with a valid data type, because otherwise Solr raises an error				
						query.append(qualifier.getFieldName());
						query.append(":");
						query.append(sb);
						
						query.append(" OR ");
					}
				}
				
				query.setLength(query.length()-4);			//strip off spare OR
				
				query.append(")");
			} else if (label.getText().equals("author")) {
				sb.setLength(0);
				
				backtrack = convertLabel(node.getChild(1), interdependentVariables, quantify, sb);
				
				if (backtrack == SolrBacktrackingOption.NONE) {
					query.append("(");
					query.append(KwqlContentItemQualifier.author.getFieldName());
					query.append(":");
					query.append(sb);
					query.append(" OR ");
					query.append(KwqlContentItemQualifier.login.getFieldName());
					query.append(":");
					query.append(sb);
					query.append(")");
				}
			} else {
				backtrack = convertLabel(node.getChild(1), KwqlContentItemQualifier.valueOf(label.getText()).getFieldName(), interdependentVariables, quantify, query);
			}
		} else if (node.getType() == KWQL.RESOURCE) {
			int type = node.getChild(0).getChild(0).getType();
			
			if (type == KWQL.TAG) {
				backtrack = convertTag(node.getChild(1), KwqlResource.ci, interdependentVariables, quantify, query);
				
				if (backtrack == SolrBacktrackingOption.QUALIFIER) {
					query.setLength(backtrackPosition);
					
					query.append(KwqlTagQualifier.name.getFieldName());				//if the tag property could not be converted to a solr query and the query is not negated add at least an existential query for tags
					query.append(":[* TO *]");
					
					backtrack = SolrBacktrackingOption.NONE;
				}
			} else if (type == KWQL.FRAG) {
				backtrack = convertFragment(node.getChild(1), interdependentVariables, quantify, query);
				
				if (backtrack == SolrBacktrackingOption.QUALIFIER) {
					query.setLength(backtrackPosition);
					
					query.append(KwqlFragmentQualifier.URI.getFieldName());			//if the fragment property could not be converted to a solr query and the query is not negated add at least an existential query for fragments
					query.append(":[* TO *]");
					
					backtrack = SolrBacktrackingOption.NONE;
				}
			} else if (type == KWQL.LINK) {
				backtrack = SolrBacktrackingOption.QUALIFIER;
			} else {
				throw new UnexpectedNodeException();
			}
		}
		
		return backtrack;
	}
	
	/**
	 * Converts qualifiers of a tag into a Solr query string.
	 *  
	 * @param node the node of the ast that should be converted
	 * @param prefix a prefix that is added to the field name of tags
	 * @param query the so far produced query string
	 * @return true, if the properties of node cannot be expressed by Solr and therefore backtracking is needed
	 */
	private static SolrBacktrackingOption convertTag(Tree node, KwqlResource container, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		SolrBacktrackingOption backtrack = SolrBacktrackingOption.NONE;
		int backtrackPosition = query.length();

		if (node.getType() == KWQL.NEGATION) {
			query.append("(*:* AND ");				//workaround for Solr bug
			query.append("-");
			
			backtrack = convertTag(node.getChild(0), container, interdependentVariables, false, query);
			
			query.append(")"); 
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION || node.getType() == KWQL.DISJUNCTION) {
			SolrBacktrackingOption localBacktracking;
			
			query.append("(");
			
			backtrackPosition = query.length();

			localBacktracking = backtrack = convertTag(node.getChild(0), container, interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			} else {
				backtrackPosition = query.length();
				
				query.append(" ");
				query.append(node.toString());
				query.append(" ");
			}
			
			localBacktracking = convertTag(node.getChild(1), container, interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			}
			
			if (backtrack != SolrBacktrackingOption.NONE && localBacktracking != SolrBacktrackingOption.NONE) {
				backtrack = SolrBacktrackingOption.QUALIFIER;
			} else {
				backtrack = SolrBacktrackingOption.NONE;
			}

			query.append(")");
		} else if (node.getType() == KWQL.QUALIFIER) {
			StringBuilder sb = new StringBuilder();
			Tree label = node.getChild(0).getChild(0);
			
			if (label == null) {
				query.append("(");

				sb.setLength(0);				
				convertLabel(node.getChild(1), interdependentVariables, quantify, sb);
				
				for (KwqlTagQualifier qualifier : KwqlTagQualifier.values()) {
					if (qualifier.isField() && qualifier.isApplicable(sb)) {							//only use qualifier with a valid data type, because otherwise Solr raises an error				
						query.append(qualifier.getFieldName(container));
						query.append(":");
						query.append(sb);
						
						query.append(" OR ");
					}
				}
				
				query.setLength(query.length()-4);			//strip off spare OR
				
				query.append(")");
			} else if (label.getText().equals("author")) {
				sb.setLength(0);
				
				backtrack = convertLabel(node.getChild(1), interdependentVariables, quantify, sb);

				if (backtrack == SolrBacktrackingOption.NONE) {
					query.append("(");
					query.append(KwqlTagQualifier.author.getFieldName(container));
					query.append(":");
					query.append(sb);
					query.append(" OR ");
					query.append(KwqlTagQualifier.login.getFieldName(container));
					query.append(":");
					query.append(sb);
					query.append(")");
				}
			} else {
				backtrack = convertLabel(node.getChild(1), KwqlTagQualifier.valueOf(label.getText()).getFieldName(container), interdependentVariables, quantify, query);
			}
		}
		
		return backtrack;
	}
	
	
	/**
	 * Converts qualifiers of a tag into a Solr query string.
	 *  
	 * @param node the node of the ast that should be converted
	 * @param query the so far produced query string
	 * @return true, if the properties of node cannot be expressed by Solr and therefore backtracking is needed
	 */
	private static SolrBacktrackingOption convertFragment(Tree node, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		SolrBacktrackingOption backtrack = SolrBacktrackingOption.NONE;
		int backtrackPosition = query.length();

		if (node.getType() == KWQL.NEGATION) {
			query.append("(*:* AND ");				//workaround for Solr bug
			query.append("-");
			
			backtrack = convertFragment(node.getChild(0), interdependentVariables, false, query);
			
			query.append(")"); 
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION || node.getType() == KWQL.DISJUNCTION) {
			SolrBacktrackingOption localBacktracking;

			query.append("(");
			
			backtrackPosition = query.length();

			localBacktracking = backtrack = convertFragment(node.getChild(0), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			} else {
				backtrackPosition = query.length();
				
				query.append(" ");
				query.append(node.toString());
				query.append(" ");
			}
			
			localBacktracking = convertFragment(node.getChild(1), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			}
			
			if (backtrack != SolrBacktrackingOption.NONE && localBacktracking != SolrBacktrackingOption.NONE) {
				backtrack = SolrBacktrackingOption.QUALIFIER;
			} else {
				backtrack = SolrBacktrackingOption.NONE;
			}

			query.append(")");
		} else if (node.getType() == KWQL.QUALIFIER) {
			StringBuilder sb = new StringBuilder();
			Tree label = node.getChild(0).getChild(0);
			
			if (label == null) {
				query.append("(");

				sb.setLength(0);				
				convertLabel(node.getChild(1), interdependentVariables, quantify, sb);
				
				for (KwqlFragmentQualifier qualifier : KwqlFragmentQualifier.values()) {
					if (qualifier.isField() && qualifier.isApplicable(sb)) {									//only use qualifier with a valid data type, because otherwise Solr raises an error				
						query.append(qualifier.getFieldName());
						query.append(":");
						query.append(sb);
						
						query.append(" OR ");
					}
				}
				
				query.setLength(query.length()-4);			//strip off spare OR
				
				query.append(")");
			} else if (label.getText().equals("author")) {
				sb.setLength(0);
				
				backtrack = convertLabel(node.getChild(1), interdependentVariables, quantify, sb);

				if (backtrack == SolrBacktrackingOption.NONE) {
					query.append("(");
					query.append(KwqlFragmentQualifier.author.getFieldName());
					query.append(":");
					query.append(sb);
					query.append(" OR ");
					query.append(KwqlFragmentQualifier.login.getFieldName());
					query.append(":");
					query.append(sb);
					query.append(")");
				}
			} else {
				backtrack = convertLabel(node.getChild(1), KwqlFragmentQualifier.valueOf(label.getText()).getFieldName(), interdependentVariables, quantify, query);
			}
		} else if (node.getType() == KWQL.RESOURCE) {
			int type = node.getChild(0).getChild(0).getType();
			
			if (type == KWQL.TAG) {
				backtrack = convertTag(node.getChild(1), KwqlResource.fragment, interdependentVariables, quantify, query);
				
				if (backtrack == SolrBacktrackingOption.QUALIFIER) {			//if the tag property could not be converted to a solr query add at least an existential query for tags
					query.setLength(backtrackPosition);
					
					query.append("fragment_");
					query.append(KwqlTagQualifier.name.getFieldName());
					query.append(":[* TO *]");
					
					backtrack = SolrBacktrackingOption.NONE;
				}
			} else {
				throw new UnexpectedNodeException();
			}
		}
		
		return backtrack;
	}

	/**
	 * Converts a label into a Solr query string.
	 * 
	 * @param node the node representing the label 
	 * @param field the name of the Solr field for this label
	 * @param query the so far generated Solr query string
	 * @return true, if the label cannot be expressed by Solr (i.e. has a unknown type) and therefore backtracking is needed
	 */
	private static SolrBacktrackingOption convertLabel(Tree node, String field, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		query.append(field);
		query.append(":");
		
		return convertLabel(node, interdependentVariables, quantify, query);
	}
	
	/**
	 * Converts the value of a label into a Solr query string.
	 * 
	 * @param node the node representing the label value
	 * @param query the so far generated Solr query string
	 * @return true, if the label cannot be expressed by Solr (i.e. has a unknown type) and therefore backtracking is needed
	 */
	private static SolrBacktrackingOption convertLabel(Tree node, Collection<String> interdependentVariables, boolean quantify, StringBuilder query) {
		SolrBacktrackingOption backtrack = SolrBacktrackingOption.NONE;
		int backtrackPosition = query.length();

		if (node.getType() == KWQL.NEGATION) {
			query.append("(*:* AND ");			//workaround for Solr bug
			query.append("-");
			
			backtrack = convertLabel(node.getChild(0), interdependentVariables, quantify, query);
			
			query.append(")"); 
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION || node.getType() == KWQL.DISJUNCTION) {
			SolrBacktrackingOption localBacktracking;
			
			query.append("(");

			backtrackPosition = query.length();
			
			localBacktracking = backtrack = convertLabel(node.getChild(0), interdependentVariables, quantify, query);

			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			} else {
				backtrackPosition = query.length();
				
				query.append(" ");
				query.append(node.toString());
				query.append(" ");
			}
			
			localBacktracking = convertLabel(node.getChild(1), interdependentVariables, quantify, query);
			
			if (localBacktracking == SolrBacktrackingOption.QUALIFIER) {
				query.setLength(backtrackPosition);
			} else if (localBacktracking == SolrBacktrackingOption.RESOURCE) {
				return localBacktracking;
			}
			
			if (backtrack != SolrBacktrackingOption.NONE && localBacktracking != SolrBacktrackingOption.NONE) {
				backtrack = SolrBacktrackingOption.QUALIFIER;
			} else {
				backtrack = SolrBacktrackingOption.NONE;
			}

			query.append(")");
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			String keyword = node.getChild(0).getText();
			
			query.append(keyword.replace(":","\\:").replaceAll("'(.*)'", "\"$1\""));
		} else if (node.getType() == KWQL.VAR) {
			String name = node.getChild(0).getText();

			if (quantify || !interdependentVariables.contains(name)) {
				return SolrBacktrackingOption.QUALIFIER;
			} else {
				return SolrBacktrackingOption.RESOURCE;				//as the query is negated and contains interdependent variables the current resource cannot be existentially quantified
			}
		} else {
			return SolrBacktrackingOption.QUALIFIER;			//subresources cannot be expressed in Solr 
		}
		
		return backtrack;
	}

}
