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
package kiwi.service.query.kwql;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.kwql.KiWiSOLRTuple;
import kiwi.api.query.kwql.KwqlAxis;
import kiwi.api.query.kwql.KwqlConstraints;
import kiwi.api.query.kwql.KwqlContentItemQualifier;
import kiwi.api.query.kwql.KwqlFragmentQualifier;
import kiwi.api.query.kwql.KwqlLinkQualifier;
import kiwi.api.query.kwql.KwqlQueryServiceLocal;
import kiwi.api.query.kwql.KwqlQueryServiceRemote;
import kiwi.api.query.kwql.KwqlResource;
import kiwi.api.query.kwql.KwqlSolrQueryGenerator;
import kiwi.api.query.kwql.KwqlTagQualifier;
import kiwi.api.query.kwql.KwqlTernary;
import kiwi.api.query.kwql.UnexpectedNodeException;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.tagging.TaggingService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.service.query.kwql.parser.KWQL;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeAdaptor;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import choco.kernel.solver.ContradictionException;

/**
 * Implementation of the KWQL evaluation engine.
 * 
 * @author Steffen Hausmann
 * 
 */
@Name("kiwi.query.kwqlQueryService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class KwqlQueryServiceImpl implements KwqlQueryServiceLocal, KwqlQueryServiceRemote {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private SolrService solrService;

	@In
	private ContentItemService contentItemService;

	@In
	private FragmentService fragmentService;

	@In
	private TaggingService taggingService;

	@In
	private FacesMessages facesMessages;

	@Override
	public KiWiSearchResults search(KWQLComponent kwqlBody) {
		KiWiSearchResults results = new KiWiSearchResults();
			kwqlBody = new KWQLComponent(kwqlBody.getBody().getChild(0));
			Tree body = kwqlBody.getBody();

			kwqlBody.printTree();

			normalizeBody(body);

			kwqlBody.printTree();

			KwqlConstraints constraints = new KwqlConstraints(body);

			HashSet<KiWiSOLRTuple> solrResults = querySOLR(constraints, body);

			int noSolrResults = solrResults.size();
			log.info("solr query returned #0 results", noSolrResults);
			HashSet<KiWiSOLRTuple> res = new HashSet<KiWiSOLRTuple>();
//			if (kwqlQuery.hasConstruction()) {
//				res = queryKWQLBindings(solrResults, constraints, body);
//			} else {
				res = queryKWQL(solrResults, constraints, body);
			results = formatKwqlSearchResults(res);
			int kwqlResults = results.getResults().size();
			log.info("filter step removed #0 results (#1 left)", noSolrResults
					- kwqlResults, kwqlResults);

		results.setResultCount(results.getResults().size());
		return results;
	}

	@Override
	public KiWiQueryResult query(KWQLComponent kwqlBody) {
		KiWiQueryResult results = new KiWiQueryResult();

			kwqlBody = new KWQLComponent(kwqlBody.getBody().getChild(0));
			Tree body = kwqlBody.getBody();

			log.info("query body: #0", body.toStringTree());

			normalizeBody(body);

			log.info("normalized query body: #0", body.toStringTree());

			KwqlConstraints constraints = new KwqlConstraints(body);

			HashSet<KiWiSOLRTuple> solrResults = querySOLR(constraints, body);

			int noSolrResults = solrResults.size();
			log.info("solr query returned #0 results", noSolrResults);
			HashSet<KiWiSOLRTuple> res = new HashSet<KiWiSOLRTuple>();
//			if (kwqlQuery.hasConstruction()) {
//				res = queryKWQLBindings(solrResults, constraints, body);
//			} else {
				res = queryKWQL(solrResults, constraints, body);

			results = formatKwqlQueryResults(res);

			int kwqlResults = results.size();
			log.info("filter step removed #0 results (#1 left)", noSolrResults
					- kwqlResults, kwqlResults);

		return results;
	}

	/**
	 * @param solrQuery
	 * @param kwqlQuery
	 * @param constraints
	 * @param body
	 * @return
	 */
	private HashSet<KiWiSOLRTuple> querySOLR(KwqlConstraints constraints, Tree body) {
		HashSet<KiWiSOLRTuple> solrResults = new HashSet<KiWiSOLRTuple>();
		SolrQuery solrQuery = KwqlSolrQueryGenerator.buildSolrQuery(body,
				constraints.getInterdependentVariables());
		QueryResponse response = solrService.search(solrQuery);

		for (SolrDocument document : response.getResults()) {
			Long id = Long.parseLong(document.getFieldValue("id").toString());
			ContentItem ci = entityManager.find(ContentItem.class, id);
//			System.out.println(ci);

			if (ci != null) { // continue if the index is corrupt
				solrResults.add(new KiWiSOLRTuple(document, ci));
			}
		}
		return solrResults;
	}

	/**
	 * Evaluates the KWLQ query on a set of previously generated possible
	 * answers.
	 * 
	 * @param rsp
	 *            a list of candidates generated by Solr that could be answers
	 *            of the query
	 * @param body
	 *            the root of the syntax tree
	 * @return a KiWiQueryResult that can be further processed by the KiWi
	 *         system
	 * @throws ContradictionException
	 */
	private KiWiQueryResult formatKwqlQueryResults(HashSet<KiWiSOLRTuple> res) {
		KiWiQueryResult results = new KiWiQueryResult();
		LinkedList<String> columnTitles = new LinkedList<String>();

		Map<String, Object> headQualifiers = new LinkedHashMap<String, Object>();
		headQualifiers.put("", null);

		columnTitles.addAll(headQualifiers.keySet());

		results.setResultFormat(null);
		results.setColumnTitles(columnTitles);

		for (KiWiSOLRTuple result : res) {
			LinkedHashMap<String, Object> mappedQualifiers = new LinkedHashMap<String, Object>();
			mappedQualifiers.put("", result.getCi());
			results.add(mappedQualifiers);
		}
		return results;

	}

	private HashSet<KiWiSOLRTuple> queryKWQL(
			HashSet<KiWiSOLRTuple> solrResults, KwqlConstraints constraints,
			Tree body) {
		HashSet<KiWiSOLRTuple> results = new HashSet<KiWiSOLRTuple>();

		for (KiWiSOLRTuple result : solrResults) {

			constraints.clearValues();

			if (evaluateKwqlQuery(result.getCi(), body, constraints)
					.isSatisfiable()) {
				if (!constraints.containsNontrivialConstrains()
						|| constraints.solve()) {
					results.add(result);
				}
			}
		}
		return results;
	}

	private HashSet<KiWiSOLRTuple> queryKWQLBindings(
			HashSet<KiWiSOLRTuple> solrResults, KwqlConstraints constraints,
			Tree body) {
		HashSet<KiWiSOLRTuple> results = new HashSet<KiWiSOLRTuple>();

		for (KiWiSOLRTuple result : solrResults) {
			ContentItem ci = result.getCi();
			constraints.clearValues();

			if (evaluateKwqlQuery(ci, body, constraints).isSatisfiable()) {
				boolean satisfied = false;
				int i = 0;
				if (constraints.solve()) {
					results.add(result);
					log.info("bindings for ci #0 (#1):", ci.getResource()
							.getId(), ci.getTitle());
					do {
						satisfied |= true;

						log.info("\t #0", constraints
								.variableBindingsToString());

						// for (KwqlTargetVariable var : variables) {
						// log.info("\t $#0 = #1", var.getName(),
						// var.getValue());
						// }
					} while (constraints.nextSolution() && i < 50);

					if (i == 50) {
						log.info("INCOMPLETE SOLUTUION SET");
					}
				}
			}
		}
		return results;
	}

	/**
	 * Evaluates the KWLQ query on a set of previously generated possible
	 * answers.
	 * 
	 * @param solrResults
	 *            a list of candidates generated by Solr that could be answers
	 *            of the query
	 * @param body
	 *            the root of the syntax tree
	 * @return a KiWiQueryResult that can be further processed by the KiWi
	 *         system
	 * @throws ContradictionException
	 */
	private KiWiSearchResults formatKwqlSearchResults(HashSet<KiWiSOLRTuple> res) {
		KiWiSearchResults results = new KiWiSearchResults();

		// System.out.println("solr results: "+solrResults.size());
		// HashSet<KiWiSOLRTuple> res = evaluate(solrResults,constraints,body);
		// System.out.println("after results: "+res.size());
		//		
		// //if (kwqlQuery.hasConstruction()){
		// ArrayList<KwqlTargetVariable> bindings = getVariableBindings(res,
		// constraints);
		// System.out.println("got bindings");
		// System.out.println(bindings);//}

		for (KiWiSOLRTuple result : res) {
			SolrDocument document = result.getDocument();
			ContentItem ci = result.getCi();
			results.getResults().add(
					new SearchResult(ci, document, (Float) document
							.getFieldValue("score")));
		}

		return results;
	}

	private KwqlTernary evaluateKwqlQuery(ContentItem ci, Tree body,
			KwqlConstraints constraints) {
		constraints.setRootId(ci.getResource().getId().intValue());

		return evaluateResource(ci, body, KwqlAxis.none, constraints);
	}

	private KwqlTernary evaluateResource(ContentItem ci, Tree node,
			KwqlAxis axis, KwqlConstraints constraints) {
		return evaluateResource(ci, node.getChild(1), node.getChild(0)
				.getChild(0), axis, constraints);
	}

	private KwqlTernary evaluateResource(ContentItem ci, Tree node, Tree type,
			KwqlAxis axis, KwqlConstraints constraints) {
		if (type.getType() == KWQL.DISJUNCTION) {
			return evaluateResource(ci, node, type.getChild(0), axis,
					constraints).or(
					evaluateResource(ci, node, type.getChild(1), axis,
							constraints));
		} else if (type.getType() == KWQL.CI) {
			Collection<ContentItem> children;

			KwqlTernary match = KwqlTernary.FALSE;

			int ancestorId = ci == null ? -1 : ci.getResource().getId()
					.intValue();

			switch (axis) {
			case none:
				return evaluateContentItem(ci, node, constraints);

			case child:
				children = ci == null ? null : ci.getNestedContentItems();

				constraints.addToRelation(KwqlContentItemQualifier.child,
						ancestorId, -1); // add a dummy entry to the relation
				// which is needed for proper
				// evaluation of disjunctive and
				// negated queries

				if (children != null && children.size() > 0) {
					for (ContentItem child : children) {
						int childId = child.getResource().getId().intValue();

						constraints.addToRelation(
								KwqlContentItemQualifier.child, ancestorId,
								childId);

						match = match.or(evaluateContentItem(child, node,
								constraints));
					}
				} else {
					evaluateContentItem(null, node, constraints);
				}

				return match;

			case descendant:
				LinkedList<ContentItem> queue = new LinkedList<ContentItem>();

				constraints.addToRelation(KwqlContentItemQualifier.descendant,
						ancestorId, -1); // add a dummy entry to the relation
				// which is needed for proper
				// evaluation of disjunctive and
				// negated queries

				queue.add(ci);

				while (!queue.isEmpty()) {
					ci = queue.pop();
					children = ci == null ? null : ci.getNestedContentItems();

					if (children != null && children.size() > 0) {
						for (ContentItem child : children) {
							int childId = child.getResource().getId()
									.intValue();

							queue.add(child);
							constraints.addToRelation(
									KwqlContentItemQualifier.descendant,
									ancestorId, childId);

							match = match.or(evaluateContentItem(child, node,
									constraints));
						}
					} else {
						evaluateContentItem(null, node, constraints);
					}
				}

				return match;

			case target:
				constraints.addToRelation(KwqlLinkQualifier.target, ancestorId,
						-1); // add a dummy entry to the relation which is
				// needed for proper evaluation of disjunctive
				// and negated queries

				try {
					Collection<KiWiTriple> triples = ci == null ? null : ci
							.getResource().listOutgoing(
									Constants.NS_KIWI_CORE + "internalLink");

					if (triples != null && triples.size() > 0) {
						for (KiWiTriple triple : triples) {
							ContentItem target = contentItemService
									.getContentItemByUri(((KiWiUriResource) triple
											.getObject()).getUri());

							int targetId = target.getResource().getId()
									.intValue();

							constraints.addToRelation(KwqlLinkQualifier.target,
									ancestorId, targetId);

							match = match.or(evaluateContentItem(target, node,
									constraints));
						}
					} else {
						evaluateContentItem(null, node, constraints);
					}
				} catch (NamespaceResolvingException e) {
				}

				return match;

			default:
				throw new IllegalArgumentException();
			}
		} else if (type.getType() == KWQL.TAG) {
			KwqlTernary match = KwqlTernary.FALSE;

			int ciId = ci == null ? -1 : ci.getResource().getId().intValue();
			List<Tag> tags = ci == null ? null : taggingService.getTags(ci);

			constraints.addToRelation(KwqlResource.tag, ciId, -1); // add a
			// dummy
			// entry to
			// the
			// relation
			// which is
			// needed
			// for
			// proper
			// evaluation
			// of
			// disjunctive
			// and
			// negated
			// queries

			if (tags != null && tags.size() > 0) {
				for (Tag tag : tags) {
					int tagId = tag.getResource().getId().intValue();

					constraints.addToRelation(KwqlResource.tag, ciId, tagId);

					match = match.or(evaluateTag(tag, node, constraints));
				}
			} else {
				evaluateTag(null, node, constraints);
			}

			return match;
		} else if (type.getType() == KWQL.FRAG) {
			Collection<FragmentFacade> children;

			KwqlTernary match = KwqlTernary.FALSE;

			int ancestorId = ci == null ? -1 : ci.getResource().getId()
					.intValue();

			switch (axis) {
			case none:
				children = ci == null ? null : fragmentService
						.getContentItemFragments(ci, FragmentFacade.class);

				constraints
						.addToRelation(KwqlResource.fragment, ancestorId, -1); // add
				// a
				// dummy
				// entry
				// to
				// the
				// relation
				// which
				// is
				// needed
				// for
				// proper
				// evaluation
				// of
				// disjunctive
				// and
				// negated
				// queries

				if (children != null && children.size() > 0) {
					for (FragmentFacade ff : children) {
						int fragmentId = ff.getResource().getId().intValue();

						constraints.addToRelation(KwqlResource.fragment,
								ancestorId, fragmentId);

						match = match
								.or(evaluateFragment(ff, node, constraints));
					}
				} else {
					evaluateFragment(null, node, constraints);
				}

				return match;

			case child:
				children = ci == null ? null : fragmentService
						.getContentItemFragments(ci, FragmentFacade.class);

				constraints.addToRelation(KwqlFragmentQualifier.child,
						ancestorId, -1); // add a dummy entry to the relation
				// which is needed for proper
				// evaluation of disjunctive and
				// negated queries

				if (children != null && children.size() > 0) {
					for (FragmentFacade child : children) {
						int childId = child.getResource().getId().intValue();

						constraints.addToRelation(KwqlFragmentQualifier.child,
								ancestorId, childId);

						match = match.or(evaluateFragment(child, node,
								constraints));
					}
				} else {
					evaluateFragment(null, node, constraints);
				}

				return match;

			case descendant:
				LinkedList<ContentItem> queue = new LinkedList<ContentItem>();

				constraints.addToRelation(KwqlFragmentQualifier.descendant,
						ancestorId, -1); // add a dummy entry to the relation
				// which is needed for proper
				// evaluation of disjunctive and
				// negated queries

				queue.add(ci);

				while (!queue.isEmpty()) {
					ci = queue.pop();
					children = ci == null ? null : fragmentService
							.getContentItemFragments(ci, FragmentFacade.class);

					if (children != null && children.size() > 0) {
						for (FragmentFacade child : children) {
							int childId = child.getResource().getId()
									.intValue();

							queue.add(child.getDelegate());
							constraints.addToRelation(
									KwqlFragmentQualifier.descendant,
									ancestorId, childId);

							match = match.or(evaluateFragment(child, node,
									constraints));
						}
					} else {
						evaluateFragment(null, node, constraints);
					}
				}

				return match;

			default:
				throw new IllegalArgumentException();

			}
		} else if (type.getType() == KWQL.LINK) {
			return evaluateLink(ci, node, constraints);
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateContentItem(ContentItem ci, Tree node,
			KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateContentItem(ci, node.getChild(0), constraints).not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateContentItem(ci, node.getChild(0), constraints).and(
					evaluateContentItem(ci, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateContentItem(ci, node.getChild(0), constraints).or(
					evaluateContentItem(ci, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.RESOURCE) {
			return evaluateResource(ci, node, KwqlAxis.none, constraints);
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);

			// no qualifier given -> try them all
			if (label == null) {
				KwqlTernary match = KwqlTernary.FALSE;

				for (KwqlContentItemQualifier qualifier : KwqlContentItemQualifier
						.values()) {
					if (qualifier.isValue()) {
						match = match.or(evaluateContentItemLabel(ci, node
								.getChild(1), qualifier, constraints));
					}
				}

				return match;
			} else {
				return evaluateContentItemLabel(ci, node.getChild(1),
						KwqlContentItemQualifier.valueOf(label.getText()),
						constraints);
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateFragment(FragmentFacade ff, Tree node,
			KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateFragment(ff, node.getChild(0), constraints).not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateFragment(ff, node.getChild(0), constraints).and(
					evaluateFragment(ff, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateFragment(ff, node.getChild(0), constraints).or(
					evaluateFragment(ff, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.RESOURCE) {
			ContentItem delegate = ff == null ? null : ff.getDelegate();

			return evaluateResource(delegate, node, KwqlAxis.none, constraints);
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);

			// no qualifier given -> try them all
			if (label == null) {
				KwqlTernary match = KwqlTernary.FALSE;

				for (KwqlFragmentQualifier qualifier : KwqlFragmentQualifier
						.values()) {
					if (qualifier.isValue()) {
						match = match.or(evaluateFragmentLabel(ff, node
								.getChild(1), qualifier, constraints));
					}
				}

				return match;
			} else {
				return evaluateFragmentLabel(ff, node.getChild(1),
						KwqlFragmentQualifier.valueOf(label.getText()),
						constraints);
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateTag(Tag tag, Tree node,
			KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateTag(tag, node.getChild(0), constraints).not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateTag(tag, node.getChild(0), constraints).and(
					evaluateTag(tag, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateTag(tag, node.getChild(0), constraints).or(
					evaluateTag(tag, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);

			// no qualifier given -> try them all
			if (label == null) {
				KwqlTernary match = KwqlTernary.FALSE;

				for (KwqlTagQualifier qualifier : KwqlTagQualifier.values()) {
					if (qualifier.isValue()) {
						match = match.or(evaluateTagLabel(tag,
								node.getChild(1), qualifier, constraints));
					}
				}

				return match;
			} else {
				return evaluateTagLabel(tag, node.getChild(1), KwqlTagQualifier
						.valueOf(label.getText()), constraints);
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateLink(ContentItem container, Tree node,
			KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateLink(container, node.getChild(0), constraints).not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateLink(container, node.getChild(0), constraints).and(
					evaluateLink(container, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateLink(container, node.getChild(0), constraints).or(
					evaluateLink(container, node.getChild(1), constraints));
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);

			if (label == null) {
				return KwqlTernary.FALSE; // currently all possible qualifiers
				// are unsupported
			}

			int nodeType = node.getChild(1).getType();

			if (nodeType == KWQL.RESOURCE) {
				if (label.getText().equals("target")) {
					return evaluateResource(container, node.getChild(1),
							KwqlAxis.target, constraints);
				} else if (label.getText().matches("(origin|tag)")) {
					throw new UnsupportedOperationException(String.format(
							"%s: qualifier '%s' not supported yet", node
									.toStringTree(), label));
				} else {
					throw new IllegalArgumentException();
				}
			} else if (nodeType == KWQL.STRING) {
				String keyword = node.getChild(1).getChild(0).getText();

				if (label.equals("anchor text")) {
					throw new UnsupportedOperationException(String.format(
							"%s: qualifier '%s' not supported yet", node
									.toStringTree(), label));
				} else {
					throw new IllegalArgumentException();
				}
			} else {
				throw new UnexpectedNodeException();
			}

		} else if (node.getType() == KWQL.RESOURCE) {
			int resourceType = node.getChild(0).getChild(0).getType();

			if (resourceType == KWQL.TAG) {
				return KwqlTernary.FALSE; // links are currently no real
				// resources therefore they cannot
				// be tagged
			} else {
				throw new UnexpectedNodeException();
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateContentItemLabel(ContentItem ci, Tree node,
			KwqlContentItemQualifier label, KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateContentItemLabel(ci, node.getChild(0), label,
					constraints).not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateContentItemLabel(ci, node.getChild(0), label,
					constraints).and(
					evaluateContentItemLabel(ci, node.getChild(1), label,
							constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateContentItemLabel(ci, node.getChild(0), label,
					constraints).or(
					evaluateContentItemLabel(ci, node.getChild(1), label,
							constraints));
		} else if (node.getType() == KWQL.RESOURCE) {
			switch (label) {
			case child:
				return evaluateResource(ci, node, KwqlAxis.child, constraints);

			case descendant:
				return evaluateResource(ci, node, KwqlAxis.descendant,
						constraints);

			default:
				throw new IllegalArgumentException();
			}
		} else if (node.getType() == KWQL.STRING
				|| node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			KiWiResource resource = ci == null ? null : ci.getResource();

			return evaluateLabelValue(resource, label, node, label
					.getValues(ci), constraints);
		} else if (node.getType() == KWQL.VAR) {
			int id = ci == null ? -1 : ci.getResource().getId().intValue();

			constraints.addToValues(label, id, label.getValues(ci));

			return KwqlTernary.UNKNOWN;
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateFragmentLabel(FragmentFacade ff, Tree node,
			KwqlFragmentQualifier label, KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateFragmentLabel(ff, node.getChild(0), label,
					constraints).not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateFragmentLabel(ff, node.getChild(0), label,
					constraints).and(
					evaluateFragmentLabel(ff, node.getChild(1), label,
							constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateFragmentLabel(ff, node.getChild(0), label,
					constraints).or(
					evaluateFragmentLabel(ff, node.getChild(1), label,
							constraints));
		} else if (node.getType() == KWQL.RESOURCE) {
			switch (label) {
			case child:
				return evaluateResource(ff.getDelegate(), node, KwqlAxis.child,
						constraints);

			case descendant:
				return evaluateResource(ff.getDelegate(), node,
						KwqlAxis.descendant, constraints);

			default:
				throw new IllegalArgumentException();
			}
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			KiWiResource resource = ff == null ? null : ff.getResource();

			return evaluateLabelValue(resource, label, node, label
					.getValues(ff), constraints);
		} else if (node.getType() == KWQL.VAR) {
			int fragmentId = ff == null ? -1 : ff.getResource().getId()
					.intValue();

			constraints.addToValues(label, fragmentId, label.getValues(ff));

			return KwqlTernary.UNKNOWN;
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlTernary evaluateTagLabel(Tag tag, Tree node,
			KwqlTagQualifier label, KwqlConstraints constraints) {
		if (node.getType() == KWQL.NEGATION) {
			return evaluateTagLabel(tag, node.getChild(0), label, constraints)
					.not();
		} else if (node.getType() == KWQL.AND
				|| node.getType() == KWQL.CONJUNCTION) {
			return evaluateTagLabel(tag, node.getChild(0), label, constraints)
					.and(
							evaluateTagLabel(tag, node.getChild(1), label,
									constraints));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return evaluateTagLabel(tag, node.getChild(0), label, constraints)
					.or(
							evaluateTagLabel(tag, node.getChild(1), label,
									constraints));
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			KiWiResource resource = tag == null ? null : tag.getResource();
			
			return evaluateLabelValue(resource, label, node, label
					.getValues(tag), constraints);
		} else if (node.getType() == KWQL.VAR) {
			int tagId = tag == null ? -1 : tag.getResource().getId().intValue();

			constraints.addToValues(label, tagId, label.getValues(tag));

			return KwqlTernary.UNKNOWN;
		} else {
			throw new UnexpectedNodeException();
		}
	}

	@SuppressWarnings("unchecked")
	private KwqlTernary evaluateLabelValue(KiWiResource resource, Enum label,
			Tree node, String[] values, KwqlConstraints constraints) {
		KwqlTernary match = KwqlTernary.FALSE;
		int id = resource == null ? -1 : resource.getId().intValue();

		Pattern pattern = Pattern.compile(node.getChild(1).getText(),
				Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		constraints.addToValues(label, id, null);

		if (values != null && values.length > 0) {
			for (String value : values) {
				constraints.addToValues(label, id, values);

				if (value != null) {
					match = match.or(pattern.matcher(value).matches());
				}
			}
		}

		return match;
	}

	private void normalizeBody(Tree body) {
		normalizeResource(body, new HashSet<KwqlResource>());
	}

	private void normalizeResource(Tree node, Set<KwqlResource> resources) {
		int nodeType = node.getType();

		if (nodeType == KWQL.NEGATION) {
			normalizeResource(node.getChild(0), resources);
		} else if (nodeType == KWQL.AND || nodeType == KWQL.CONJUNCTION
				|| nodeType == KWQL.DISJUNCTION) {
			normalizeResource(node.getChild(0), resources);
			normalizeResource(node.getChild(1), resources);
		} else if (nodeType == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);

			if (label != null) {
				for (Iterator<KwqlResource> it = resources.iterator(); it
						.hasNext();) {
					KwqlResource resource = it.next();

					if (!resource.hasQualifier(label.getText())) {
						it.remove();
					}
				}
			}

			normalizeResource(node.getChild(1), resources);
		} else if (nodeType == KWQL.RESOURCE) {
			Tree resourceType = node.getChild(0).getChild(0);

			Set<KwqlResource> subResources = new HashSet<KwqlResource>();

			if (resourceType == null) {
				if (node.getParent().getType() == KWQL.QUALIFIER) { // if this
					// resource
					// is a
					// child/descendant
					// it may
					// only be a
					// ci or
					// fragment
					subResources.add(KwqlResource.ci);
					subResources.add(KwqlResource.fragment);
				} else {
					for (KwqlResource resource : KwqlResource.values()) {
						subResources.add(resource);
					}
				}

				normalizeResource(node.getChild(1), subResources);

				Tree resource = node.getChild(0);
				CommonTreeAdaptor adaptor = new CommonTreeAdaptor();

				for (Iterator<KwqlResource> it = subResources.iterator(); it
						.hasNext();) {
					KwqlResource subResourceType = it.next();

					if (it.hasNext()) {
						CommonTree or = (CommonTree) adaptor.create(
								KWQL.DISJUNCTION, "OR");

						resource.addChild(or);
						resource = or;
					}

					resource.addChild((CommonTree) adaptor.create(
							subResourceType.getAntlrId(), subResourceType
									.getAntlrName()));
				}
			} else {
				collectResources(node.getChild(0).getChild(0), subResources);

				normalizeResource(node.getChild(1), subResources);
			}

			if (node.getParent().getType() == KWQL.QUALIFIER) {
				Set<KwqlResource> cap = new HashSet<KwqlResource>();

				if (subResources.contains(KwqlResource.ci)) {
					cap.add(KwqlResource.ci);
				} else {
					cap.add(KwqlResource.ci);
					cap.add(KwqlResource.fragment);
				}

				resources.retainAll(cap);
			} else {
				for (KwqlResource resource : subResources) {
					for (Iterator<KwqlResource> it = resources.iterator(); it
							.hasNext();) {
						KwqlResource feasibleResource = it.next();

						if (!feasibleResource.hasQualifier(resource.name())) {
							it.remove();
						}
					}
				}
			}
		} else if (nodeType == KWQL.STRING || node.getType() == KWQL.INTEGER
				|| node.getType() == KWQL.UR) {
			CommonTree re;

			String text = node.getChild(0).getText();
			TreeAdaptor adaptor = new CommonTreeAdaptor();

			if (text.matches("('|\\\").*('|\\\")")) {
				re = (CommonTree) adaptor.create(KWQL.KW, text.substring(1,
						text.length() - 1));
			} else {
				re = (CommonTree) adaptor.create(KWQL.KW, String.format(
						".*%s.*", text));
			}

			node.addChild(re);
		}
	}

	private void collectResources(Tree node, Set<KwqlResource> resources) {
		if (node.getType() == KWQL.DISJUNCTION) {
			collectResources(node.getChild(0), resources);
			collectResources(node.getChild(1), resources);
		} else {
			resources.add(KwqlResource.valueOf(node.getType()));
		}
	}
}