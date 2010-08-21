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

package interedu.service.dataimport;

import interedu.api.dataimport.DokumentArtResolver;
import interedu.api.dataimport.InterEduArtikelFacade;
import interedu.api.dataimport.InterEduImportService;
import interedu.api.dataimport.ParseArtikelService;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.SKOSService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.TextContentNotChangedException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.user.User;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

/**
 * @author Rolf Sint
 */
@Stateless
@Name("interedu.intereduImportService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class InterEduImportServiceImpl implements InterEduImportService {

	/**
	 * The persistence layer will be flush after every XXX number of persisted
	 * items.
	 */
	private static int FLUSH_LIMIT = 20;
	private static int CLEAR_LIMIT = 100;

	@Logger
	private static Log log;

	@In(create = true)
	private ConfigurationService configurationService;

	private final ParseArtikelService parseArtikelService;
	
	@In(create = true)
	private TaggingService taggingService;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TransactionService transactionService;

	@In(create = true)
	private ContentItemService contentItemService;

	@In
	private TripleStore tripleStore;

	@In
	private User currentUser;

	private int persitCount;

	@In
	private EntityManager entityManager;
	
	@In(create=true)
	private SKOSService skosService;
	
	/**
	 * Builds a <code>InterEduImportServiceImpl</code> instance.
	 */
	public InterEduImportServiceImpl() {
		parseArtikelService = new ParseArtikelServiceImpl();
	}

	@Override
	public final void importDir(File dir) {
		importDir(dir, ALL);
	}

	// public void init(){
	// Query q = entityManager.createNamedQuery("contentItemService.byType");
	// q.setParameter("type", tripleStore
	// .createUriResource(Constants.NS_SKOS + "Concept"));
	//
	// q.setHint("org.hibernate.cacheable", true);
	// List<ContentItem> results = q.getResultList();
	//		
	// List<SKOSConcept> sList = kiwiEntityManager.createFacadeList(results,
	// SKOSConcept.class);
	// log.info(results.size());
	//
	// for(ContentItem res: sList){
	//			
	// }
	//		
	// log.info("---->"+ res.getPreferredLabel());
	// }

	public final void importDir(File dir, int articlesCount) {
		if (dir == null) {
			final NullPointerException exception = new NullPointerException(
					"The directory to import is null");
			log.warn(exception.getMessage(), exception);
			throw exception;
		}

		if (!dir.exists() || dir.isFile()) {
			final IllegalArgumentException exception = new IllegalArgumentException(
					"The directory to import is not a directory.");
			log.warn(exception.getMessage(), exception);
			throw exception;
		}

		if (configurationService == null) {
			final NullPointerException nullPointerException = new NullPointerException(
					"ConfigurationService is null, was not injected");
			log.warn(nullPointerException.getMessage(), nullPointerException);
			throw nullPointerException;
		}

		if (kiwiEntityManager == null) {
			final NullPointerException nullPointerException = new NullPointerException(
					"kiwiEntityManager is null, was not injected");
			log.warn(nullPointerException.getMessage(), nullPointerException);
			throw nullPointerException;
		}

		// init();

		log.debug("Import dir : #0", dir.getName());
		final File[] files = dir.listFiles();
		for (final File file : files) {
			try {
				importFile(file, articlesCount);
			} catch (final IllegalStateException illegalStateException) {
				// in this case I throw the exception to signal
				// that the article limit was reach.
				break;
			}
		}
		log.info("Import of #0 articles was succesfull.", persitCount);
		persitCount = 0;
	}

	public void importArticle(Artikel article, List<Kategorie> categories,
			Set<String> recTags) {

		log.debug("Import from file  #0", article);

		final ContentItem articleCI = contentItemService
				.createContentItem("interEdu/" + article.getArtikelId());
		contentItemService.updateTitle(articleCI, article.getArtikelTitel());
		articleCI.setAuthor(currentUser);
		final KiWiUriResource articleCiType = tripleStore
				.createUriResource(Constants.NS_INTEREDU_CORE + "Artikel");
		articleCI.addType(articleCiType);

		contentItemService.updateTextContentItem(articleCI, "<div>" + article.getInhalt() + "</div>");

		contentItemService.saveContentItem(articleCI);

		log.info("Artikeltitel: " + article.getArtikelTitel());
		final InterEduArtikelFacade articleFacade = buildArtFacade(article,articleCI);

		final Set<ContentItem> categoriesSet = new HashSet<ContentItem>();
		for (final Kategorie cat : categories) {

			// FIXME : I don't know if this is right, this
			// property is only used to log.
			final int parentId = cat.getParentId();
			final int id = cat.getId();
			final String name = cat.getName();

			// gets the CIs by its ID, CIs represent the SKOS
			// categories in the case that the Interedu SKOS
			// Thesaurus is imported
			final String catURI = "http://www.eduhui.at/concepts#";
			final ContentItem catContItem = contentItemService
					.getContentItemByUri(catURI + id);
			
			//set

			final String message = "Category with parent id=#0, id=#1 and name=#2 was sucessful imported";
			log.debug(message, parentId, id, name);

			categoriesSet.add(catContItem);
		}
//		kiwiEntityManager.flush();

	}

	/**
	 * @param artikelTmp
	 * @param articleCI
	 */
	private InterEduArtikelFacade buildArtFacade(final Artikel artikelTmp,
			final ContentItem articleCI) {
		final InterEduArtikelFacade result = kiwiEntityManager.createFacade(
				articleCI, InterEduArtikelFacade.class);

		result.setArtikelAutor(artikelTmp.getArtikelAutor());
		result.setUrlid(artikelTmp.getUrlid());
		result.setErscheindatum(artikelTmp.getErscheindatum());
		result.setVerfallsdatum(artikelTmp.getVerfallsdatum());
		result.setDokumentartid(artikelTmp.getDokumentartid());
		result.setDokumentArt(artikelTmp.getDokumentArt());
		String link = artikelTmp.getBildlink();
		if( link != null && link != "" ) {
			if( link.startsWith("http://") ) {
				result.setBildlink(link);
			} else  {
				result.setBildlink( "http://eduhi.at/" + link );
			}
		} else result.setBildlink("");
		result.setSchulstufen(artikelTmp.getSchulstufen());
		result.setSchultyp(artikelTmp.getSchultyp());
		result.setGegenstand(artikelTmp.getGegenstand());
		result.setQuelle(artikelTmp.getQuelle());
		result.setState(InterEduArtikelFacade.NEW);
		//result.setHauptGegenstand("Fachgebiete");
		return result;
	}

	private void importFile(File f, int articleCount) {

		log.debug("Import from file  #0", f.getName());

		try {
			parseArtikelService.init(f);
		} catch (final InterEduParseException e) {
			log.warn("The file #0 can not be process, try the next one.", f
					.getName());
			log.warn(e.getMessage(), e);
			return;
		}

		final KiWiUriResource articleCiType = tripleStore.createUriResource(Constants.NS_INTEREDU_CORE + "Artikel");

		final int nodeCount = parseArtikelService.getNodeSize();
		for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
			
			try {
				// This temporary object is used to hold the
				// Artikel properties parsed from the XML File
				final Artikel artikelTmp = parseArtikelService
				.getArtikel(nodeIndex);

				//set title lenght to max 255
				String s = artikelTmp.getArtikelTitel();
				if( s.length() > 255 ) {
					s = s.substring(0,250) + "...";
					artikelTmp.setArtikelTitel(s);
				}

				final ContentItem articleCI = contentItemService.createContentItem("interEdu/" + artikelTmp.getArtikelId());
				contentItemService.updateTitle(articleCI, artikelTmp.getArtikelTitel());
				articleCI.setAuthor(currentUser);
				articleCI.addType(articleCiType);
				articleCI.setLanguage(Locale.GERMAN);

				String dokumentArt = DokumentArtResolver.getDokumentArtById(artikelTmp.getDokumentartid());	
				artikelTmp.setDokumentArt(dokumentArt);

				String content = "<div xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:type=\"page\">" +
								 artikelTmp.getInhalt() +
								 "</div>";
				
				contentItemService.updateTextContentItem(articleCI, content);
				

				contentItemService.saveContentItem(articleCI);
				
				final InterEduArtikelFacade articleFacade = buildArtFacade(artikelTmp, articleCI);

				// parses all categories which belong to an article
				// from the article xml File, one article may have
				// several categories categories are stored in an own
				// category objects.
				final List<Kategorie> categories = parseArtikelService
				.getKategorien(nodeIndex);

				final LinkedList<Long> categoryIds = new LinkedList<Long>();
				int catCount = 0;

				for (final Kategorie cat : categories) {
					catCount++;

					final String catURI = "http://www.eduhui.at/concepts#";
					final ContentItem catContItem = contentItemService.getContentItemByUri(catURI + cat.getId());

					//add concept id
					categoryIds.add( catContItem.getId() );

					// set only the first category
					persitCount++;
					if (catCount == 1) {
						if( catContItem != null ) {
							String hauptGegenstand = calculateHauptGegenstand(
									catContItem, articleFacade);
							articleFacade.setHauptGegenstand(hauptGegenstand);
						} else catCount--;
					}
				}

				// check if hauptgegenstand is set
				if (articleFacade.getHauptGegenstand() == null) {
					articleFacade.setHauptGegenstand("Fachgebiete");
				}

				articleFacade.setCategoryIds(categoryIds);
				kiwiEntityManager.persist(articleFacade);

				//			if (articleFacade.getHauptGegenstand() == null) {
				//			articleFacade.setHauptGegenstand("Fachgebiete");
				//			}

				//should not be necessary
				//			for( SKOSConcept c : concepts ) {
				//				HashSet<ContentItem> a = c.getArticles();
				//				a.add(articleFacade.getDelegate());
				//				c.setArticles(a);
				//				kiwiEntityManager.persist(c);
				//			}


				if( persitCount % CLEAR_LIMIT == 0 ) {
					Transaction.instance().commit();
					entityManager.clear();
					Transaction.instance().begin();
					transactionService.registerSynchronization(
	                		KiWiSynchronizationImpl.getInstance(), 
	                		transactionService.getUserTransaction() );
				}

				if (articleCount != ALL && persitCount >= articleCount) {
					// This is a very ugly way to break the workflow
					// but is the is requires the less code. This
					// exception is used to signal when the load
					// article limit was reach.
					throw new IllegalStateException("Reach article limit");
				}
				persitCount++;

			} catch (NotSupportedException e) {
				log.error("error while starting new transaction (not supported)",e);
			} catch (SystemException e) {
				log.error("error while starting new transaction (system exception)",e);
			} catch (SecurityException e) {
				log.error("error while committing transaction (security exception)",e);
			} catch (IllegalStateException e) {
				log.error("error while committing transaction (illegal state)",e);
			} catch (RollbackException e) {
				log.error("error while committing transaction (rollback)",e);
			} catch (HeuristicMixedException e) {
				log.error("error while committing transaction (heuristic mixed)",e);
			} catch (HeuristicRollbackException e) {
				log.error("error while committing transaction (heuristic rollback)",e);
			}
		}

		try {
			Transaction.instance().commit();
			Transaction.instance().begin();
			transactionService.registerSynchronization(
            		KiWiSynchronizationImpl.getInstance(), 
            		transactionService.getUserTransaction() );
		} catch(Exception ex) {
			log.error("exception while committing transaction", ex);
		}

		log.debug("Import filename: #0 was succesful", f.getName());
	}



	// caculates the gegenstand of each concept
	public String calculateHauptGegenstand(ContentItem cat,
			InterEduArtikelFacade articleFacade) {
		// creates the facade out of the content item
		SKOSConcept x = kiwiEntityManager.createFacade(cat, SKOSConcept.class);
		log.info("prL " + x.getPreferredLabel());

		SKOSConcept sc = x.getBroader();
		log.info("1: " + sc.getPreferredLabel());
		String hauptGegenstand = "";
		while (!(sc.getPreferredLabel().equals("Fachgebiete") || sc
				.getPreferredLabel().equals("InterEduThesaurus"))) {
			log.info("2: " + sc.getPreferredLabel());
			hauptGegenstand = sc.getPreferredLabel();
			sc = sc.getBroader();

		}

		return hauptGegenstand;
	}

}

// FIXME : move this test in a test suite
// public void test(){
// javax.persistence.Query q =
// kiwiEntityManager.createQuery("SELECT ?artikel WHERE { ?artikel rdf:type <"+Constants.NS_INTEREDU_CORE+"Artikel> }",KiWiQueryLanguage.SPARQL,InterEduArtikelFacade.class);
// List<InterEduArtikelFacade> artikels =
// (List<InterEduArtikelFacade>)
// q.getResultList();
// log.info("Number of articles: "+artikels.size());
// for (Iterator iterator = artikels.iterator();
// iterator.hasNext();) {
// InterEduArtikelFacade name = (InterEduArtikelFacade)
// iterator.next();
// System.out.println("test");
//
// // if(name.getArtikelId() != null){
// // log.info("1"+name.getArtikelId());
// // }
//
// try{
// System.out.println("1"+name.getArtikelId());
// }
// catch(Exception e){
// e.printStackTrace();
// }
//
// System.out.println("2"+name.getArtikelTitel());
// System.out.println("3"+name.getArtikelAutor());
// System.out.println("4"+name.getUrlid());
// System.out.println("5"+name.getErscheindatum());
// System.out.println("6"+name.getVerfallsdatum());
// System.out.println("7"+name.getDokumentartid());
// System.out.println("8"+name.getBildlink());
// System.out.println("9" + name.getSchulstufen());
// System.out.println("10" + name.getSchultyp());
// System.out.println("10" + name.getGegenstand());
// System.out.println("10" + name.getQuelle());
//
// }
//
// }

