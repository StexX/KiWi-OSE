/*
 * File : ImportEquityData.java
 * Date : Apr 7, 2010
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
 */


package kiwi.admin.service.sunspace;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kiwi.admin.api.sunspace.ImportException;
import kiwi.admin.api.sunspace.SunSpaceImportService;
import kiwi.admin.api.sunspace.SunSpaceImportServiceLocal;
import kiwi.admin.api.sunspace.SunSpaceImportServiceRemote;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.reasoning.ReasoningService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.Constants;
import kiwi.model.activity.Activity;
import kiwi.model.ceq.*;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Used to import sun space specific information. The usage is
 * easy the only one public method (the importFiles) triggers the
 * import. The import is done in two steeps :
 * <ul>
 * <li>import the dump files (in to some temporary tables)
 * <li>process the imported information in to KiWi relevant data.
 * </ul>
 * The reason for this two step workflow is the big size of dump
 * files - big files can not use a heap (memory) processing
 * strategy. The data contained in the dump files can be invalid
 * (wrong format) or inconsistent - if an item from the dump
 * files can not imported from any reasons then the item is
 * ignored and the error log. The log namespace is
 * "kiwi.admin.service.sunspace" (the package for this class). <br>
 * <b>Note : </b> the <code>importFiles</code> method call take a
 * while (from a couple of minutes until a couple of hours). This
 * method uses bean managed transaction <b>don't used in
 * container managed transaction context</b>.<br>
 * <b>Note : </b> the <code>importFiles</code> method call take a
 * generates a lot of logging, ensure that your application
 * server has enough free space or configure the log file
 * according with your needs. If you use JBoss then you can use
 * the example distributed together with kiwi placed in :
 * ..../resources/sun.space/jboss-log4j.xml.<br>
 * The name for this seam component is "sunSpaceImportService".<br>
 * The scope for this seam component is Application. <br>
 * This class is not designed to be extended.
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
@Name("sunSpaceImportService")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@TransactionManagement(TransactionManagementType.BEAN)
public class SunSpaceImportServiceImpl implements SunSpaceImportServiceLocal,
        SunSpaceImportServiceRemote {

    private static final String SPACE_ID = "spaceId";

    /**
     * The prefix used to generate sun space URLs.
     */
    private static final String SUN_SPACE_URL_PREFIX =
            "http://sunspace.sfbay.sun.com/display/";

    /**
     * The encoding for the dump files.
     */
    private static final String ENCODING = "UTF-8";

    /**
     * The subfix for the sun space item type.
     */
    private static final String SUN_SPACE_ITEM_TYPE = "SunSpaceItem";

    /**
     * The subfix for the sun space comunity type.
     */
    private static final String SUN_SPACE_COMUNITY_TYPE = "SunSpaceComunity";

    /**
     * Used to communicates with the underlying persistence
     * layer.
     */
    @In
    private EntityManager entityManager;

    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In
    private TransactionService transactionService;

    /**
     * Used for kiwi user related actions.
     */
    @In
    private UserService userService;

    /**
     * Used for content item user related actions.
     */
    @In
    private ContentItemService contentItemService;

    /**
     * Used to notify the progress.
     */
    @In
    private ImportMonitor importMonitor;

    /**
     * Used for tagging related actions.
     */
    @In
    private TaggingService taggingService;

    @In
    private TripleStore tripleStore;

    /**
     * A factory API that enables applications to configure and
     * obtain a SAX based parser to parse XML documents.
     */
    private SAXParserFactory factory;

    /**
     * Used for reading an XML document using callbacks.
     */
    private SAXParser parser;

    /**
     * All the log massages goes trough this field.
     */
    @Logger
    private Log logger;

    private int importedContentItems;

    /**
     * This user is used in the tagging process for the
     * communities.
     */
    private User systemUser;

    /**
     * The maximum numbers of imported tags, if is -1 then all
     * the activities will be imported.
     */
    private long tagLimit;

    /**
     * The maximum numbers of imported activities, if is -1 then
     * all the activities will be imported.
     */
    private long activityLimit;

    /**
     * The maximum numbers of imported sun space communities, if
     * is -1 then all the activities will be imported.
     */
    private long comunityLimit;

    /**
     * Builds an <code>EquityLogImportServiceImpl</code>
     * instance.
     */
    public SunSpaceImportServiceImpl() {
        // UNIMPLEMENTD
    }

    /**
     * Acts like a constructor in the Seam environment (it runs
     * in the post-construct phase).
     *
     * @throws SAXException if the SAX parser can not be
     *             initialized from any XML related reasons.
     * @throws ParserConfigurationException if the SAX parser can
     *             not be initialized from any reasons.
     */
    @Create
    public void init() throws ParserConfigurationException, SAXException {
        factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
        final String userLogin = "KiWi_System";
        systemUser = userService.getUserByLogin(userLogin);
        if (systemUser == null) {
            try {
                systemUser = userService.createUser(userLogin, "1w1k");
            } catch (final UserExistsException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Imports all the files specified in the <code>files</code>
     * argument.
     *
     * @param files the files to import, it can not be null or
     *            empty and it <b>must</b> contain values for all
     *            the keys defined in the
     *            <code>SunSpaceImportService.FILE</code> enum.
     */
    @Override
    @Observer("importFiles")
    public void importFiles(final Map<FILE, String> files, long logs,
            long spaces, long tags) throws ImportException {

        if (files == null) {
            final NullPointerException nullException =
                    new NullPointerException(
                            "The files argument can not be null.");
            logger.warn(nullException.getMessage(), nullException);
            throw nullException;
        }

        if (files.isEmpty()) {
            final NullPointerException nullException =
                    new NullPointerException(
                            "The files argument can not be null.");
            logger.warn(nullException.getMessage(), nullException);
            throw nullException;

        }

        for (final FILE file : FILE.values()) {
            if (!files.containsKey(file)) {
                final String msg =
                        "The files map does not contain the :" + file;
                final IllegalArgumentException illegalArgumentException =
                        new IllegalArgumentException(msg);
                logger.warn(msg, illegalArgumentException);
                throw illegalArgumentException;
            }
        }

        importMonitor.notifyChange("Read the input files.");

        tagLimit = tags;
        activityLimit = logs;
        comunityLimit = spaces;

        final String sunSpaceFile = files.get(FILE.SPACE_FILE);
        final String sunSpaceInfoFile = files.get(FILE.SPACE_INFO_FILE);
        final String sunSpaceKeywordFile = files.get(FILE.SPACE_KEYWORD_FILE);
        final String sunSpacePersonFile = files.get(FILE.SPACE_PERSON_FILE);

        final String personFile = files.get(FILE.PERSON_FILE);
        final String informationFile = files.get(FILE.INFORMATION_FILE);
        final String logFile = files.get(SunSpaceImportService.FILE.LOG_FILE);
        final String keywordFile = files.get(FILE.KEYWORD_FILE);
        final String infoKeywordFile = files.get(FILE.INFO_KEYWORDFILE);
        final ReasoningService reasoner =
                (ReasoningService) Component
                        .getInstance("kiwi.core.reasoningService");
        try {
            final boolean areSunInfoImported = areSunInfoImported();
            if (!areSunInfoImported) {
                // I import the communities and all other related
                // issues.
                importSunSpace(sunSpaceFile);
                importSunSpaceInformation(sunSpaceInfoFile);
                importSunSpaceKeyword(sunSpaceKeywordFile);
                importSunSpacePerson(sunSpacePersonFile);

                importPerson(personFile);
                importInformation(informationFile);
                importLog(logFile);
                importKeyword(keywordFile);
                importInfoKeyword(infoKeywordFile);
            }

            reasoner.disableReasoning();

            compulteLogs(1, activityLimit);
            // The reason why I choose to use 1 instead of is :
            // if for a reason or other the transaction fails
            // I will love all the n records and this because
            // only one record may create problems.
            compulteTags(1, tagLimit);


            // the spaces must be computed after the logs and the
            // tags.
            compulteSpaces(1, comunityLimit);
        } finally {
//            try {
//                cleanTables();
//            } catch (final Exception e) {
//                logger.warn(e.getMessage(), e);
//            }
            reasoner.enableReasoning();
        }

        importMonitor.notifyChange("Import was succesfful");

        logger.info("Import is succesfful");
    }

    private boolean areSunInfoImported() {

        // FIXME : this is not the best way to check if the sun
        // space information was loaded but I can presume if the
        // file is loaded then the sun space files was loaded.
        final String queryStr =
                "SELECT COUNT(space.id) FROM SunSpaceImportSpace AS space";
        final Query query = entityManager.createQuery(queryStr);
        final Long count = (Long) query.getSingleResult();

        return count.longValue() > 1;

    }

    private void importSunSpace(String sunSpaceFile) throws ImportException {
        final DefaultHandler handler =
                new SunSpaceSpaceSaxParser(entityManager, transactionService, logger, importMonitor);
        try {
            importFile(sunSpaceFile, handler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importSunSpaceInformation(String sunSpaceFile)
            throws ImportException {
        final DefaultHandler handle =
                new SunSpaceSpaceInformationSaxParser(entityManager, transactionService, 
                		logger, importMonitor);
        try {
            importFile(sunSpaceFile, handle);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importSunSpaceKeyword(String sunSpaceFile)
            throws ImportException {
        final DefaultHandler handle =
                new SunSpaceSpaceKeywordSaxParser(entityManager, transactionService, 
                		logger, importMonitor);
        try {
            importFile(sunSpaceFile, handle);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importSunSpacePerson(String sunSpacePersonFile)
            throws ImportException {
        final DefaultHandler handler =
                new SunSpaceSpacePersonSaxParser(entityManager, 
                		transactionService, 
                		logger, importMonitor);
        try {
            importFile(sunSpacePersonFile, handler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importInfoKeyword(String infoKeywordFile)
            throws ImportException {
        final DefaultHandler infoKeywordHandler =
                new SunSpaceInfoKeywordSaxParser(entityManager, transactionService, 
                		logger, importMonitor);
        try {
            importFile(infoKeywordFile, infoKeywordHandler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importKeyword(String keywordFile) throws ImportException {
        final DefaultHandler keywordHandler =
                new SunSpaceKeywordSaxParser(entityManager, transactionService,
                		logger, importMonitor);
        try {
            importFile(keywordFile, keywordHandler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importLog(String logFile) throws ImportException {
        final DefaultHandler logHandler =
                new SunSpaceLogSaxParser(entityManager, transactionService, 
                		logger, importMonitor);
        try {
            importFile(logFile, logHandler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importInformation(String informationFile)
            throws ImportException {
        final DefaultHandler infoHandler =
                new SunSpaceInformationSaxParser(entityManager, transactionService,
                		logger, importMonitor);
        try {
            importFile(informationFile, infoHandler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    private void importPerson(String personFile) throws ImportException {
        final DefaultHandler personHandler =
                new SunSpacePersonSaxParser(entityManager, transactionService, 
                		logger, importMonitor);
        try {
            importFile(personFile, personHandler);
        } catch (final Exception e) {
            // It is not recommended to catch the
            // java.lang.Exception but in this case I re-throw it
            // and log also.
            final ImportException importException = new ImportException(e);
            logger.warn(e.getMessage(), importException);
            throw importException;
        }
    }

    /**
     * Clean all the temporary tables used in the import process.
     *
     * @throws NotSupportedException
     * @throws SystemException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     * @throws IllegalStateException
     * @throws SecurityException
     */
    private void cleanTables() throws Exception {
        final UserTransaction transaction = Transaction.instance();
        if (!transaction.isActive()) {
            transaction.begin();
            transactionService.registerSynchronization(
            		KiWiSynchronizationImpl.getInstance(), 
            		transactionService.getUserTransaction() );
        }
        transaction.enlist(entityManager);

        final String deleteInfoQueryStr =
                "DELETE FROM SunSpaceImportInformation";
        final Query deleteInfoQuery =
                entityManager.createQuery(deleteInfoQueryStr);
        deleteInfoQuery.executeUpdate();

        final String deleteLogQueryStr = "DELETE FROM SunSpaceImportLog";
        final Query deleteLogQuery =
                entityManager.createQuery(deleteLogQueryStr);
        deleteLogQuery.executeUpdate();

        final String deletePersonQueryStr = "DELETE FROM SunSpaceImportPerson";
        final Query deletePersonQuery =
                entityManager.createQuery(deletePersonQueryStr);
        deletePersonQuery.executeUpdate();

        deleteTable(SunSpaceImportInfoKeyword.class);
        deleteTable(SunSpaceImportInformation.class);
        deleteTable(SunSpaceImportKeyword.class);
        deleteTable(SunSpaceImportLog.class);
        deleteTable(SunSpaceImportPerson.class);
        deleteTable(SunSpaceImportSpace.class);
        deleteTable(SunSpaceImportSpaceInformation.class);
        deleteTable(SunSpaceImportSpaceKeyword.class);
        deleteTable(SunSpaceImportSpacePerson.class);

        // FIXME : add here all the other tables.
        transaction.commit();
    }

    private void deleteTable(Class<?> clazz) {
        final StringBuilder queryStr = new StringBuilder("DELETE FROM ");
        queryStr.append(clazz.getSimpleName());
        final Query deleteLogQuery =
                entityManager.createQuery(queryStr.toString());
        deleteLogQuery.executeUpdate();
    }

    /**
     * Computes all all the imported Sun Space logs in to KiWi
     * related entities. Basically this methods iterates over all
     * the stored SunSpaceImportXXXX entities and transform it
     * according the kiwi needs. For more information about the
     * SunSpaceImportXXXX entities please consult the java doc
     * documentation (package kiwi.model.ceq).<br>
     * Because the number of imported Sun Space logs entities can
     * be preaty big this method spawns/commits transactions, a
     * certain amount of Sun Space logs are process in a single
     * transaction, the amount is defined with the
     * <code>recordPerTransaction</code> argument. If a singular
     * Sun Space log can not be imported from any reasons then
     * the involved Sun Space log is not imported (and the error
     * log).
     *
     * @param recordPerTransaction the number of records
     *            committed in a singular transaction, it must be
     *            greater than 1, otherwise an exception raises.
     * @throws ImportException if the import from any reasons, it
     *             chains the real cause for the exception.
     * @throws IllegalArgumentException if the
     *             <code>recordPerTransaction</code> is smaller
     *             than 1.
     */
    private void compulteLogs(int recordPerTransaction, long limit)
            throws ImportException {

        if (recordPerTransaction < 1) {
            throw new IllegalArgumentException(
                    "The recordPerTransaction must be bigger than 1.");
        }

        final long logsCount = limit == -1 ? countLogs() : limit;

        int startPosition = 0;
        int consummed = 0;
        while (true) {
            final Query query =
                    entityManager.createNamedQuery("select.allSunSpaceLogs");
            query.setMaxResults(recordPerTransaction);
            query.setFirstResult(startPosition);
            // if a class cast exception occurs here then you
            // have serious problems.
            final List<SunSpaceImportLog> logs = query.getResultList();

            if (logs.isEmpty()) {
                break;
            }

            if (limit != -1 && consummed >= limit) {
                logger.debug("Activity limit reached");
                break;
            }

            try {
                // starts an transaction for the next 100 items
                final UserTransaction transaction = Transaction.instance();
                if (!transaction.isActive()) {
                    transaction.begin();
                    transactionService.registerSynchronization(
                    		KiWiSynchronizationImpl.getInstance(), 
                    		transactionService.getUserTransaction() );
                }
                transaction.enlist(entityManager);

                for (final SunSpaceImportLog log : logs) {
                    try {
                        logger.debug("Try to consume log [#0].", log);
                        consumeLog(log);
                        consummed++;
                        importMonitor.notifyChange("Consume  " + consummed
                                + " Sun Space Activities from " + logsCount);
                        logger.debug("The log [#0] was connsumed.", log);
                    } catch (final Exception exception) {
                        // It is not recommended to catch the
                        // java.lang.Exception but in this case I
                        // re-throw it and log also.
                        logger.warn("The log [#0] can not be consumed.", log);
                        logger.warn(exception.getMessage(), exception);
                        importMonitor.notifyChange("Can not consume  : " + log);
                    }
                }
                startPosition = startPosition + logs.size();


                // mihai : the reason for this flush here is catch any
                // possible exception during the persit phase
                // (e.g. OptimisticLock)
                try {
//                    entityManager.flush();
                    transaction.commit();
                } catch (final Exception exception) {
                   // It is not recommended to catch the
                    // java.lang.Exception but in this case I
                    // re-throw it and log also.
                    logger.warn(exception.getMessage(), exception);
                }


            } catch (final Exception exception) {
                // It is not recommended to catch the
                // java.lang.Exception but in this case I
                // re-throw it and log also.
                logger.warn(exception.getMessage(), exception);
                throw new ImportException(exception);
            }
        }
    }

    private long countLogs() {
        final String queryStr =
                "SELECT COUNT(log) FROM SunSpaceImportLog AS log";
        final Query query = entityManager.createQuery(queryStr);
        final Long singleResult = (Long) query.getSingleResult();

        return singleResult.longValue();
    }

    private long countSpaces() {
        final String queryStr =
                "SELECT COUNT(log) FROM SunSpaceImportSpace AS log";
        final Query query = entityManager.createQuery(queryStr);
        final Long singleResult = (Long) query.getSingleResult();

        return singleResult.longValue();
    }

    /**
     * Consume a single sun space log item and transform it in
     * corresponding kiwi data.
     *
     * @param log the log to consume, it can not be null.
     * @throws ImportException if the log can not be consumed
     *             from any reasons. In most of the cases the
     *             <code>ImportException</code> chains the real
     *             cause for the exception.
     * @throws NullPointerException if the <code>log</code> is
     *             null.
     */
    private void consumeLog(SunSpaceImportLog log) throws ImportException {

        if (log == null) {
            throw new NullPointerException(
                    "The log to consule can not be null.");
        }

        logger.debug("Try to proccess #0 .", log);
        final Long informationId = log.getInformationId();
        final SunSpaceImportInformation information =
                getImportInformation(informationId);

        final ContentItem contentItem =
                getContentItem(information, SUN_SPACE_ITEM_TYPE);
        final double rating = getRatting(log);
        if (rating > 0) {
            contentItem.setRating(rating);
        }

        final Long actionId = log.getActionId();
        final int actId = actionId.intValue();
        // the upper code "getContentItem(information);" extracts
        // the user
        final Long personId = log.getPersonId();
        final SunSpaceImportPerson person = getPerson(personId);
        final User author = getUser(person);
        final Date create = log.getDate();
        final Activity activity =
                ActivityFactory.getActivity(actId, contentItem, author, create);
        entityManager.persist(activity);
    }

    /**
     * Extracts the rating value from a given log. The rating is
     * an integer (1..5).
     *
     * @param log the sun space log.
     * @return the rating value from a given log.
     */
    private double getRatting(SunSpaceImportLog log) {
        final Long actionId = log.getActionId();
        if (2 >= actionId.intValue() && actionId.intValue() <= 5) {
            // very ugly way to get the rating because the
            // activity type id may change.
            return actionId.intValue() - 1;
        }
        return -1;
    }

    /**
     * @param personId
     * @return
     */
    private SunSpaceImportPerson getPerson(Long personId) {
        final SunSpaceImportPerson result =
                entityManager.find(SunSpaceImportPerson.class, personId);

        if (result == null) {
            logger.warn("No SunSpaceImportPerson with the personId = #0 ",
                    personId);
            throw new NoResultException(
                    "No SunSpaceImportPerson with the personId =  " + personId);
        }

        return result;
    }

    private User getUser(SunSpaceImportPerson person) {
        final String userLogin = getUserLogin(person);
        final User userByLogin = userService.getUserByLogin(userLogin);
        if (userByLogin != null) {
            return userByLogin;
        }

        final String personName = person.getPersonName();
        final String personSurname = person.getPersonSurname();
        // FIXME : I need the Sun Space passwd here !
        User newUser;
        try {
            newUser =
                    userService.createUser(userLogin, personName,
                            personSurname, "password");
        } catch (final UserExistsException exception) {
            final IllegalStateException stateException =
                    new IllegalStateException(exception);
            logger.warn(exception.getMessage(), stateException);
            throw stateException;
        }
        return newUser;
    }

    private String getUserLogin(SunSpaceImportPerson person) {
        final StringBuilder result = new StringBuilder();
        result.append(person.getPersonName());
        result.append(".");
        result.append(person.getPersonId());

        return result.toString();
    }

    private ContentItem getContentItem(SunSpaceImportInformation information,
            String type) {

        final String title = information.getTitle();
        final ContentItem result =
                contentItemService.getContentItemByTitle(title);
        if (result != null) {
            return result;
        }

        final ContentItem newItem = contentItemService.createContentItem();
        newItem.setTitle(title);

        final KiWiUriResource sunSpaceType =
                tripleStore.createUriResource(Constants.NS_SUN_SPACE + type);
        newItem.addType(sunSpaceType);

        final Date created = information.getCreated();
        if (created != null) {
            // the created can be null if the import data is
            // wrong or the import fails.
            newItem.setCreated(created);
        }

        final Date changed = information.getChanged();
        if (changed != null) {
            // the changed can be null if the import data is
            // wrong or the import fails.
            newItem.setModified(changed);
        }

        final Long personId = information.getPersonId();
        final SunSpaceImportPerson person = getPerson(personId);
        final User author = getUser(person);
        if (author != null) {
            newItem.setAuthor(author);
        }

        // FIXME : the contet is lob and this can be big,
        // this can cause memory problems.
        final String content = information.getContent();
        if (content != null) {
            contentItemService.updateTextContentItem(newItem, content);
        }
        importedContentItems++;

        return newItem;
    }

    /**
     * @param informationId
     * @return
     * @throws ImportException if the imported data is in
     */
    private SunSpaceImportInformation getImportInformation(Long informationId)
            throws ImportException {

        final SunSpaceImportInformation result =
                entityManager.find(SunSpaceImportInformation.class,
                        informationId);

        if (result == null) {
            logger.warn(
                    "No SunSpaceImportInformation with the informationId = #0 ",
                    informationId);
            throw new NoResultException(
                    "No SunSpaceImportInformation with the informationId =  "
                            + informationId);
        }

        return result;
    }

    private void importFile(String file, DefaultHandler handler)
            throws SAXException, IOException {

        logger.debug("Try to process file #0", file);

        final InputSource stream = getStream(file, ENCODING);
        parser.parse(stream, handler);

        logger.debug("File #0 succesful process.", file);
    }

    /**
     * Builds a <code>org.xml.sax.InputSource</code> for a given
     * file and encoding.
     *
     * @param fileName the file name, must point to an existing
     *            file.
     * @param encoding the encoding used in the file.
     * @return the needed <code>org.xml.sax.InputSource</code>
     *         for a given file and encoding.
     * @throws UnsupportedEncodingException if the file
     *             (specified with <code>fileName</code>) uses a
     *             unsupported encoding.
     * @throws FileNotFoundException if the <code>fileName</code>
     *             points wrong.
     */
    private InputSource getStream(String fileName, String encoding)
            throws UnsupportedEncodingException, FileNotFoundException {
        final FileInputStream inputStream =
                new FileInputStream(new File(fileName));
        final InputStreamReader reader =
                new InputStreamReader(inputStream, encoding);
        final InputSource result = new InputSource(reader);
        return result;
    }

    /**
     * @param recordsPerTransaction
     * @throws ImportException
     */
    private void compulteTags(int recordsPerTransaction, long limit)
            throws ImportException {

        final long tagsCount = limit == -1 ? countTags() : limit;
        int startPosition = 0;
        int counsummed = 0;

        while (true) {
            final Query query = entityManager.createNamedQuery("select.allSunSpaceTags");
            query.setMaxResults(recordsPerTransaction);
            query.setFirstResult(startPosition);
            // if a class cast exception occurs here then you
            // have serious problems.
            final List<SunSpaceImportInfoKeyword> infoKeywords =
                    query.getResultList();

            if (infoKeywords.isEmpty()) {
                logger.debug("No tags to process.");
                break;
            }

            if (limit != -1 && counsummed >= limit) {
                logger.debug("Tag count limit reached.");
                break;
            }

            try {
                // starts an transaction for the next 100 items
                final UserTransaction transaction = Transaction.instance();
                if (!transaction.isActive()) {
                    transaction.begin();
                    transactionService.registerSynchronization(
                    		KiWiSynchronizationImpl.getInstance(), 
                    		transactionService.getUserTransaction() );
                }
                transaction.enlist(entityManager);

                for (final SunSpaceImportInfoKeyword infoKeyword : infoKeywords) {
                    try {
                        logger.debug("Try to consume InfoKeyword [#0].",
                                infoKeyword);
                        consumeInfoKeyword(infoKeyword);
                        counsummed++;
                        importMonitor.notifyChange("Consume : " + counsummed
                                + " Sun Space Tags from " + tagsCount);
                        logger.debug("The tag [#0] was connsumed.", infoKeyword);
                    } catch (final Exception exception) {
                        // It is not recommended to catch the
                        // java.lang.Exception but in this case I
                        // want to import all the available items
                        // without to care about any possible problems.
                        logger.warn("The Tag [#0] can not be consumed.",
                                infoKeyword);
                        logger.warn(exception.getMessage(), exception);
                        importMonitor.notifyChange("Can not consume  : "
                                + infoKeyword);
                    }
                }
                startPosition = startPosition + infoKeywords.size();

                // mihai : the reason for this flush here is catch any
                // possible exception during the persit phase
                // (e.g. OptimisticLock)
                try {
//                    entityManager.flush();
                    transaction.commit();
                } catch (final Exception exception) {
                   // It is not recommended to catch the
                    // java.lang.Exception but in this case I
                    // re-throw it and log also.
                    logger.warn(exception.getMessage(), exception);
                }


            } catch (final Exception exception) {
                // It is not recommended to catch the
                // java.lang.Exception but in this case I
                // re-throw it and log also.

                logger.warn(exception.getMessage(), exception);
                throw new ImportException(exception);
            }
        }
    }

    private long countTags() {
        final String queryStr =
                "SELECT COUNT(log) FROM SunSpaceImportInfoKeyword AS log";
        final Query query = entityManager.createQuery(queryStr);
        final Long singleResult = (Long) query.getSingleResult();

        return singleResult.longValue();
    }

    private void consumeInfoKeyword(SunSpaceImportInfoKeyword infoKeyword)
            throws ImportException {

        if (infoKeyword == null) {
            throw new NullPointerException(
                    "The loinfoKeywordg to consule can not be null.");
        }

        logger.debug("Try to proccess #0 .", infoKeyword);
        final Long informationId = infoKeyword.getInformatyionId();
        final SunSpaceImportInformation information;
        try {
            information = getImportInformation(informationId);
        } catch (final NoResultException persistenceException) {
            logger.warn(
                    "No SunSpaceImportInformation with the informationId = #0 ",
                    informationId);
            throw persistenceException;
        } catch (final NonUniqueResultException persistenceException) {
            logger.warn(
                    "More SunSpaceImportInformation with the informationId = #0 ",
                    informationId);
            throw persistenceException;
        }

        final Long keywordId = infoKeyword.getKeywordId();
        final ContentItem taggingItem;
        try {
            taggingItem = getContentItemForKeyword(keywordId);
        } catch (final NoResultException persistenceException) {
            logger.warn("No SunSpaceImportKeyword with the keywordId = #0 ",
                    keywordId);
            throw persistenceException;
        } catch (final NonUniqueResultException persistenceException) {
            logger.warn("More SunSpaceImportKeyword with the keywordId = #0 ",
                    informationId);
            throw persistenceException;
        }

        final ContentItem taggedItem =
                getContentItem(information, SUN_SPACE_ITEM_TYPE);

        final Long personId = infoKeyword.getPersonId();
        final SunSpaceImportPerson person = getPerson(personId);
        final User user = getUser(person);

        final String title = taggingItem.getTitle();
        final String tagTile = title != null ? title : "topic";
        taggingService.createTagging(tagTile, taggedItem, taggingItem,
                user);
        // FIXME : care on the tag activity time stamp.
    }

    /**
     * Returns the content item for a given keyword id.
     *
     * @param id
     * @return
     * @throws NoResultException
     * @throws NonUniqueResultException
     */
    private ContentItem getContentItemForKeyword(Long id) {

        final SunSpaceImportKeyword resultKeyword =
                entityManager.find(SunSpaceImportKeyword.class, id);
        if (resultKeyword == null) {
            logger.warn("No SunSpaceImportKeyword with the id = #0 ", id);
            throw new NoResultException(
                    "No SunSpaceImportKeyword with the id =  " + id);
        }

        // the keywordName is a long !
        final String keywordName = resultKeyword.getKeywordName();

        ContentItem result =
                contentItemService.getContentItemByTitle(keywordName);
        if (result != null) {
            return result;
        }

        result = contentItemService.createContentItem();
        result.setTitle(keywordName.toString());

        return result;
    }

    /**
     * @param recordsPerTransaction
     * @throws ImportException
     */
    private void compulteSpaces(int recordsPerTransaction, long limit)
            throws ImportException {


        final long countSpaces = limit == -1 ? countSpaces() : limit;

        int startPosition = 0;
        int consummed = 0;
        while (true) {
            final Query query =
            		entityManager.createNamedQuery("select.allSunSpaceSpace");
            query.setMaxResults(recordsPerTransaction);
            query.setFirstResult(startPosition);
            // if a class cast exception occurs here then you
            // have serious problems.
            final List<SunSpaceImportSpace> spaces = query.getResultList();

            if (spaces.isEmpty()) {
                // if I don't have any space to process I leave
                // the loop.
                logger.debug("No spaces to process.");
                break;
            }

            if (limit != -1 && consummed >= limit) {
                logger.debug("Comuniites count limit reached.");
                break;
            }

            try {
                // starts an transaction for the next 100 items
                final UserTransaction transaction = Transaction.instance();
                if (!transaction.isActive()) {
                    transaction.begin();
                    transactionService.registerSynchronization(
                    		KiWiSynchronizationImpl.getInstance(), 
                    		transactionService.getUserTransaction() );
                }
                transaction.enlist(entityManager);

                for (final SunSpaceImportSpace space : spaces) {
                    try {
                        logger.debug("Try to consume InfoKeyword [#0].", space);
                        consumeSpace(space);
                        consummed++;
                        importMonitor.notifyChange("Consume : " + consummed
                                + " SunSpace items from " + countSpaces);
                        logger.debug("The log [#0] was connsumed.", space);
                    } catch (final Exception exception) {
                        // It is not recommended to catch the
                        // java.lang.Exception but in this case I
                        // re-throw it and log also.
                        logger.warn("The space [#0] can not be consumed.",
                                space);
                        logger.warn(exception.getMessage(), exception);
                        importMonitor.notifyChange("Can not consume  : "
                                + space);
                    }
                }
                startPosition = startPosition + spaces.size();

                // mihai : the reason for this flush here is catch any
                // possible exception during the persit phase
                // (e.g. OptimisticLock)
                try {
//                    entityManager.flush();
                    transaction.commit();
                } catch (final Exception exception) {
                   // It is not recommended to catch the
                    // java.lang.Exception but in this case I
                    // re-throw it and log also.
                    logger.warn(exception.getMessage(), exception);
                }


            } catch (final Exception exception) {
                // It is not recommended to catch the
                // java.lang.Exception but in this case I
                // re-throw it and log also.

                logger.warn(exception.getMessage(), exception);
                throw new ImportException(exception);
            }
        }
    }

    /**
     * Consume a single sun space space item and transform it in
     * corresponding kiwi data.
     *
     * @param space the space to consume, it can not be null.
     * @throws ImportException if the log can not be consumed
     *             from any reasons. In most of the cases the
     *             <code>ImportException</code> chains the real
     *             cause for the exception.
     * @throws NullPointerException if the <code>log</code> is
     *             null.
     */
    private void consumeSpace(SunSpaceImportSpace space) throws ImportException {

        if (space == null) {
            throw new NullPointerException(
                    "The space to consule can not be null.");
        }

        logger.debug("Try to proccess #0 .", space);

        final ContentItem spaceContentItem = getContentItem(space);
        final SunSpaceComunityFacade comunityFacade =
                kiwiEntityManager.createFacade(spaceContentItem,
                        SunSpaceComunityFacade.class);

        final String description = space.getDescription();
        comunityFacade.setDescription(description);

        final String key = space.getKey();
        comunityFacade.setURL(SUN_SPACE_URL_PREFIX + key);

        final Long spaceId = space.getId();

        // compute tags
        final Query spaceTagQuery =
        		entityManager.createNamedQuery("select.sunSpaceImportSpaceKeyword");
        spaceTagQuery.setParameter(SPACE_ID, spaceId);
        final List<SunSpaceImportSpaceKeyword> resultList =
                spaceTagQuery.getResultList();
        for (final SunSpaceImportSpaceKeyword spaceKeyword : resultList) {
            final Long keywordId = spaceKeyword.getKeywordId();
            final SunSpaceImportKeyword keyword =
                    entityManager.find(SunSpaceImportKeyword.class, keywordId);
            if (keyword == null) {
                logger.warn("No SunSpaceImportKeyword for the given id #0",
                        keywordId);
                continue;
            }

            final ContentItem taggingItem;
            try {
                taggingItem = getContentItemForKeyword(keywordId);
                //entityManager.persist(taggingItem);
            } catch (final NoResultException noResultException) {
                continue;
            }

            final String tagTitle = taggingItem.getTitle();
            taggingService.createTagging(tagTitle, spaceContentItem,
                    taggingItem, systemUser);
        }

        // compute content

        final Query spaceContentQuery =
                entityManager
                        .createNamedQuery("select.sunSpaceImportSpaceInformation");
        spaceContentQuery.setParameter(SPACE_ID, spaceId);

        final List<SunSpaceImportSpaceInformation> spaceInformationRels;
        try {
            spaceInformationRels = spaceContentQuery.getResultList();
        } catch (final PersistenceException exception) {
            logger.warn(
                    "No SunSpaceImportSpaceInformation witht the spaceId = #0",
                    spaceId);
            throw exception;
        }

        for (final SunSpaceImportSpaceInformation spaceInformationRel : spaceInformationRels) {
            final Long informationId = spaceInformationRel.getInformationId();
            final SunSpaceImportInformation spaceInformaion =
                    entityManager.find(SunSpaceImportInformation.class,
                            informationId);

            if (spaceInformaion == null) {
                final String msg =
                        "No SunSpaceImportInformation with the id =  "
                                + informationId;
                final NoResultException noResultException =
                        new NoResultException(msg);
                logger.warn(noResultException.getMessage(), msg);
                throw noResultException;
            }

            // I am interested only in the information content
            // all the other space related informations are not
            // imported.
            // FIXME : the contet is lob and this can be big,
            // this can cause memory problems.
            final String content = spaceInformaion.getContent();
            if (content != null) {
                contentItemService.updateTextContentItem(spaceContentItem,
                        content);
            }
        }

        // compute users
        final Query personQuery = entityManager.createNamedQuery("select.sunSpaceImportSpacePerson");
        personQuery.setParameter(SPACE_ID, spaceId);
        final List<SunSpaceImportSpacePerson> personsInSpace =
                personQuery.getResultList();
        final List<User> usersList = new ArrayList<User>();
        final List<User> adminsLis = new ArrayList<User>();
        for (final SunSpaceImportSpacePerson personInfo : personsInSpace) {
            final Long personId = personInfo.getPersonId();
            final SunSpaceImportPerson person =
                    entityManager.find(SunSpaceImportPerson.class, personId);
            if (person == null) {
                logger.warn(
                        "The SunSpaceImportPerson with id #0 can not be found.",
                        personId);
                continue;
            }

            final Integer role = personInfo.getRole();
            final User user = getUser(person);
            if (role == 0) {
                usersList.add(user);
            } else {
                adminsLis.add(user);
            }
        }

        final List<ContentItem> administators = new ArrayList<ContentItem>();
        for (final User user : adminsLis) {
            administators.add(user.getContentItem());
        }
        comunityFacade.setParticipants(administators);

        final List<ContentItem> pariticipants = new ArrayList<ContentItem>();
        for (final User user : usersList) {
            pariticipants.add(user.getContentItem());
        }
        pariticipants.addAll(administators);
        comunityFacade.setParticipants(pariticipants);

        kiwiEntityManager.persist(comunityFacade);
    }

    private ContentItem getContentItem(SunSpaceImportSpace space) {

        final String title = space.getTitle();
        final ContentItem result =
                contentItemService.getContentItemByTitle(title);
        if (result != null) {
            return result;
        }

        final ContentItem newItem = contentItemService.createContentItem();

        final KiWiUriResource sunSpaceType =
                tripleStore.createUriResource(Constants.NS_SUN_SPACE
                        + SUN_SPACE_COMUNITY_TYPE);
        newItem.addType(sunSpaceType);
        newItem.setTitle(title);

        importedContentItems++;

        return newItem;
    }

    public long getTagLimit() {
        return tagLimit;
    }

    public void setTagLimit(long tagLimit) {
        this.tagLimit = tagLimit;
    }

    public long getActivityLimit() {
        return activityLimit;
    }

    public void setActivityLimit(long activityLimit) {
        this.activityLimit = activityLimit;
    }

    public long getComunityLimit() {
        return comunityLimit;
    }

    public void setComunityLimit(long comunityLimit) {
        this.comunityLimit = comunityLimit;
    }
}
