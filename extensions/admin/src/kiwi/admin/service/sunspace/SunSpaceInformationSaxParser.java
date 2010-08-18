/*
 * File : ImportEquityData.java.java Date : Apr 7, 2010 DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS HEADER. Copyright 2008 The KiWi Project. All rights
 * reserved. http://www.kiwi-project.eu The contents of this file are subject to
 * the terms of either the GNU General Public License Version 2 only ("GPL") or
 * the Common Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the License.
 * You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html or nbbuild/licenses/CDDL-GPL-2-CP.
 * See the License for the specific language governing permissions and
 * limitations under the License. When distributing the software, include this
 * License Header Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP. KiWi designates this particular file as
 * subject to the "Classpath" exception as provided by Sun in the GPL Version 2
 * section of the License file that accompanied this code. If applicable, add
 * the following below the License Header, with the fields enclosed by brackets
 * [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]" If you wish your
 * version of this file to be governed by only the CDDL or only the GPL Version
 * 2, indicate your decision by adding "[Contributor] elects to include this
 * software in this distribution under the [CDDL or GPL Version 2] license." If
 * you do not indicate a single choice of license, a recipient has the option to
 * distribute your version of this file under either the CDDL, the GPL Version 2
 * or to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL Version
 * 2 license, then the option applies only if the new code is made subject to
 * such option by the copyright holder. Contributor(s):
 */


package kiwi.admin.service.sunspace;


import java.util.Date;

import javax.persistence.EntityManager;

import kiwi.api.transaction.TransactionService;
import kiwi.model.ceq.SunSpaceImportInformation;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Used to parse and persist the Sun Space Information related
 * files. <br>
 * More preciselly this parser runs over a given xml file an
 * transforms its content in one or more
 * <code>SunSpaceImportInformation</code> instances and persist
 * this instances. <br>
 * This class is not designed to be extended.
 *
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
final class SunSpaceInformationSaxParser extends DefaultHandler {

    /**
     * The number of items needed persisted in a transaction.
     */
    private final static int FLUSH_COUNT = 30;

    /**
     * Used to communicates with the underlying persistence
     * layer.
     */
    private final EntityManager entityManager;

    private final TransactionService transactionService;
    
    /**
     * All the log massages goes trough this field.
     */
    private final Log log;

    /**
     * True if this parser parses a row.
     */
    private boolean inRow;

    /**
     * True if this parser parses a column.
     */
    private boolean inColumn;

    /**
     * Holds the name for the parsed property, only if I parse a
     * row.
     */
    private String property;

    /**
     * The parsed data object.
     */
    private SunSpaceImportInformation information;

    private final ImportMonitor importMonitor;

    /**
     * Counts the processed items.
     */
    private int itemCount;

    /**
     * The date when the import starts.
     */
    private Date startTime;

    private final StringBuilder informationIdBuffer;

    private final StringBuilder titleBuffer;

    private final StringBuilder descriptionBuffer;

    private final StringBuilder createdBuffer;

    private final StringBuilder changedBuffer;

    private final StringBuilder personIdBuffer;

    private final StringBuilder attatchmentFormatsBuffer;

    private final StringBuilder contentBuffer;

    /**
     * Builds a <code>CEQInformationSaxParser</code> for a given
     * list of arguments.
     *
     * @param entityManager the persistence context.
     * @param log the logger used to log debug information.
     */
    SunSpaceInformationSaxParser(final EntityManager entityManager,
    		final TransactionService transactionService,
            final Log log, ImportMonitor importMonitor) {
        this.entityManager = entityManager;
        this.transactionService = transactionService;
        this.log = log;
        this.importMonitor = importMonitor;

        informationIdBuffer = new StringBuilder();
        titleBuffer = new StringBuilder();
        descriptionBuffer = new StringBuilder();
        createdBuffer = new StringBuilder();
        changedBuffer = new StringBuilder();
        personIdBuffer = new StringBuilder();
        attatchmentFormatsBuffer = new StringBuilder();
        contentBuffer = new StringBuilder();
    }

    @Override
    public void startDocument() throws SAXException {
        startTime = new Date();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("row".equals(qName)) {
            information = new SunSpaceImportInformation();
            inRow = true;
            property = null;
        }

        if ("column".equals(qName)) {
            property = attributes.getValue("name");
            inColumn = true;
        }
    }

    private Date parseDate(String str) {
        final Date result = DateParser.parseDate(str);
        if (result == null) {
            log.warn("The #0=#1 can not be parsed like a date.", property, str);
        }
        return result;
    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (inColumn) {
            final String value = new String(ch, start, length);
            if ("INFORMATION_ID".equals(property)) {
                informationIdBuffer.append(value.trim());
            } else if ("INFORMATION_TITLE".equals(property)) {
                titleBuffer.append(value.trim());
            } else if ("INFORMATION_DESCRIPTION".equals(property)) {
                descriptionBuffer.append(value.trim());
            } else if ("INFORMATION_CREATED".equals(property)) {
                createdBuffer.append(value.trim());
            } else if ("INFORMATION_CHANGED".equals(property)) {
                changedBuffer.append(value.trim());
            } else if ("INFORMATION_PERSON_ID".equals(property)) {
                personIdBuffer.append(value.trim());
            } else if ("INFORMATION_ATTATCHMENTFORMATS".equals(property)) {
                attatchmentFormatsBuffer.append(value.trim());
            } else if ("INFORMATION_CONTENT".equals(property)) {
                if (contentBuffer.length() >= 1000) {
                    return;
                }

                // the content is a LOB and it will be moved in a
                // other flow step. For convenience I only care
                // about the first 1000 chars.
                final boolean bigValue = value.length() > 1000;
                final String cntVal =
                        bigValue ? value.substring(0, 999) : value;
                contentBuffer.append(cntVal);
            }
        }
    }

    /**
     * Populate the information entity with data.
     */
    private void store() {
        final String idStr = informationIdBuffer.toString();
        information.setId(Long.valueOf(idStr));
        information.setInformationId(Long.valueOf(idStr));

        informationIdBuffer.delete(0, informationIdBuffer.length());

        final String titleStr = titleBuffer.toString();
        information.setTitle(titleStr);

        final String descStr = descriptionBuffer.toString();
        information.setDescription(descStr);

        final Date createDate = parseDate(createdBuffer.toString());
        if (createDate != null) {
            information.setCreated(createDate);
        }

        final Date changeDate = parseDate(changedBuffer.toString());
        if (changeDate != null) {
            information.setCreated(changeDate);
        }

        final String persId = personIdBuffer.toString();
        information.setPersonId(Long.valueOf(persId));

        final String attachStr = attatchmentFormatsBuffer.toString();
        information.setAttatchmentFormats(attachStr);

        final String cntStr = contentBuffer.toString();
        if (!cntStr.trim().isEmpty()) {
            information.setContent(cntStr);
        }

    }

    private void cleanBuffers() {
        informationIdBuffer.delete(0, informationIdBuffer.length());
        titleBuffer.delete(0, titleBuffer.length());
        descriptionBuffer.delete(0, descriptionBuffer.length());
        createdBuffer.delete(0, createdBuffer.length());
        changedBuffer.delete(0, changedBuffer.length());
        personIdBuffer.delete(0, personIdBuffer.length());
        attatchmentFormatsBuffer.delete(0, attatchmentFormatsBuffer.length());
        contentBuffer.delete(0, contentBuffer.length());
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        property = null;
        try {
            if ("row".equals(qName)) {
                final UserTransaction transaction = Transaction.instance();
                if (!transaction.isActive()) {
                    transaction.begin();
                    transactionService.registerSynchronization(
                    		KiWiSynchronizationImpl.getInstance(), 
                    		transactionService.getUserTransaction() );
                }
                transaction.enlist(entityManager);

                inRow = false;
                store();
                cleanBuffers();
                // I use there because the (unique) id are set by
                // hand and hyberenate think that the entity has
                // already an persistence identity - this will
                // lead to an exception if I use persist.
                if(!entityManager.contains(information) && information.getId() != null) {
                	information = entityManager.merge(information);
                } else {
                	entityManager.persist(information);
                }

                if (itemCount % FLUSH_COUNT == 0) {
                    // mihai : under normal circumstances I don't
                    // need this but it in this case the
                    // SunSpaceImportLog import remain still.
//                    entityManager.flush();
                    transaction.commit();
                }
                itemCount++;

                final StringBuilder message = new StringBuilder();
                message.append(itemCount);
                message.append(" SunSpaceImportInformation items was imported.");

                importMonitor.notifyChange(message.toString());
            }
        } catch (final Exception e) {
            // I know, catching all exception is not a not nice
            // thing but in this case I am only interested if the
            // trans. works or not.
            log.warn("The #0 can not be imported", information);
            log.warn(e.getMessage(), e);
        }
        if ("column".equals(qName)) {
            inColumn = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {

        try {
            final UserTransaction transaction = Transaction.instance();
            if (!transaction.isActive()) {
                transaction.begin();
                transactionService.registerSynchronization(
                		KiWiSynchronizationImpl.getInstance(), 
                		transactionService.getUserTransaction() );
            }
            transaction.enlist(entityManager);
//            entityManager.flush();

            transaction.commit();
        } catch (final Exception exception) {
            // I know, catching all exception is not a not nice
            // thing but in this case I am only interested if the
            // trans. works or not.
            log.warn(exception.getMessage(), exception);
        }

        final Date now = new Date();
        final String msg =
                "Import of #0 ceq information was done in #1 seconds (#1 mins)";
        final long secTime = (now.getTime() - startTime.getTime()) * 1000;
        final long minTime = secTime / 60;
        log.debug(msg, itemCount, secTime, minTime);
    }

}
