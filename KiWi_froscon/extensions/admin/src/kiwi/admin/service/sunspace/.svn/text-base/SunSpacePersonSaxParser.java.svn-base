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
import kiwi.model.ceq.SunSpaceImportPerson;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Used to parse and persist the Sun Space Person related files. <br>
 * More preciselly this parser runs over a given xml file an
 * transforms its content in one or more
 * <code>SunSpaceImportPerson</code> instances and persist this
 * instances. <br>
 * This class is not designed to be extended.
 *
 * @author mradules
 * @version 0.7
 * @since 0.7
 */
final class SunSpacePersonSaxParser extends DefaultHandler {

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

    private final ImportMonitor importMonitor;

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
    private SunSpaceImportPerson person;

    /**
     * The date when the import starts.
     */
    private Date startTime;

    /**
     * Counts the processed items.
     */
    private int itemCount;

    private final StringBuilder personIdBuffer;

    private final StringBuilder personSurnameBuffer;

    private final StringBuilder personGivenNameBuffer;

    /**
     * Builds a <code>CEQPersonSaxParser</code> for a given list
     * of arguments.
     *
     * @param entityManager the persistence context.
     * @param log the logger used to log debug information.
     */
    SunSpacePersonSaxParser(final EntityManager entityManager, 
    		final TransactionService transactionService,
    		final Log log,
            ImportMonitor importMonitor) {
        this.entityManager = entityManager;
        this.transactionService = transactionService;
        this.log = log;
        this.importMonitor = importMonitor;

        personIdBuffer = new StringBuilder();
        personSurnameBuffer = new StringBuilder();
        personGivenNameBuffer = new StringBuilder();
    }

    @Override
    public void startDocument() throws SAXException {
        startTime = new Date();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("row".equals(qName)) {
            person = new SunSpaceImportPerson();
            inRow = true;
            property = null;
        }

        if ("column".equals(qName)) {
            property = attributes.getValue("name");
            inColumn = true;
        }

    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (inColumn) {
            final String value = new String(ch, start, length);
            if ("PERSON_ID".equals(property)) {
                personIdBuffer.append(value);
// person.setId(Long.valueOf(value));
// person.setPersonId(Long.valueOf(value));
            } else if ("PERSON_SURNAME".equals(property)) {
                personSurnameBuffer.append(value);
// person.setPersonSurname(value);
            } else if ("PERSON_GIVENNAME".equals(property)) {
                personGivenNameBuffer.append(value);
// person.setPersonName(value);
            }
        }
    }

    /**
     * Populate the person entity with data.
     */
    private void store() {

        final String id = personIdBuffer.toString();
        person.setId(Long.valueOf(id));
        person.setPersonId(Long.valueOf(id));

        final String surname = personSurnameBuffer.toString();
        person.setPersonSurname(surname);

        final String givenName = personGivenNameBuffer.toString();
        person.setPersonName(givenName);
    }

    private void cleanBuffers() {
        personIdBuffer.delete(0, personIdBuffer.length());
        personSurnameBuffer.delete(0, personSurnameBuffer.length());
        personGivenNameBuffer.delete(0, personGivenNameBuffer.length());
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
                if(!entityManager.contains(person) && person.getId() != null) {
                	person = entityManager.merge(person);
                } else {
                	entityManager.persist(person);
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
                message.append(" SunSpaceImportPerson items was imported.");

                importMonitor.notifyChange(message.toString());

            }
        } catch (final Exception e) {
            // I know, catching all exception is not a not nice
            // thing but in this case I am only interested if the
            // trans. works or not.
            log.warn("The #0 can not be imported", person);
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
                "Import of #0 ceq person was done in #1 seconds (#1 mins)";
        log.debug(msg, itemCount, (now.getTime() - startTime.getTime()) * 1000,
                (now.getTime() - startTime.getTime()) * 1000 / 60);

    }
}
