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


package mars.action;


import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import mars.model.InfoNodeFacade;
import mars.model.PictureFacade;
import mars.util.ItemCollector;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Session scoped seam components used to import a Mars specific
 * gallery of objects. The Gallery is organized like a collection of
 * items, each item can have :
 * <ul>
 * <li> 0 or more relations to classification objects.
 * <li> one picture.
 * <li> meta data.
 * </ul>
 *
 * For each item a new KiWi content item is build and stored.
 * For each item relation a new KiWi content item is builded and
 * the item content item is tag with this new created item.
 * Each picture has a picture, each new KiWi content item contains
 * the URL for this picture. <br/>
 *
 * @author mradules
 * @version 07-pre
 * @since 07-pre
 */
@Name("marsImportAction")
@Scope(ScopeType.SESSION)
public class MarsImportAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * From here is the Mars gallery loaded.
     */
    private static final String GALERY_URL = "http://pf.mediamid.com/mediamanager/REST/auth/path/linkedMediaByAnr/COMCL173323.xml";

    /**
     * All the log messages will go through this member.
     */
    @Logger
    private Log log;

    @In(create = true)
    private ContentItemService contentItemService;

    @In
    private KiWiEntityManager kiwiEntityManager;

    @In
    private EntityManager entityManager;

    /**
     * Manage and manipulate tags.
     */
    @In
    private TaggingService taggingService;

    /**
     * The Current KiWi user.
     */
    @In(create=true)
    private User currentUser;

    /**
     * Imports the Mars ({@link #GALERY_URL}) gallery.
     *
     * @throws ParserConfigurationException the web service
     *          for the given URL returns an XML that can not be passed.
     * @throws IOException by any IO errors.
     * @throws SAXException by any XML problems, in this case the xml structure can be wrong.
     * @throws XPathException by any XPath problems, in this case the xml structure can be wrong.
     */
    public void importData() throws ParserConfigurationException, IOException, SAXException, XPathException {
        final List<String> archiveNumbers = ItemCollector.getItemsArchiveNumber(GALERY_URL);
        for (String archiveNumer : archiveNumbers) {
            final Document item = ItemCollector.getItemAfterArchiveNumber(archiveNumer);
            final List<Document> relations = ItemCollector.getItemRelationDocuments(item);
            log.info("Import item with archive number = #0 import begin.", archiveNumer);
            processItem(item, archiveNumer, relations);
            log.info("Import item with archive number = #0 import sucesful ends.", archiveNumer);
        }

        log.info("Import of all mars objects was succesful.");
    }

    /**
     * Process only one item, if the method is successful then the Mars
     * item (together with its relations) was process and stored like kiwi
     * kiwi content items (see the class comments for more information).
     *
     * @param item the item to process.
     * @param archiveNumber the archive number for the item to process.
     * @param relations the relations for the given item.
     */
    private void processItem(Document item, String archiveNumber, List<Document> relations) {
        final PictureFacade pictureFacade = buildPictureFacade(item, archiveNumber);
        // Here is the picture stored.
        kiwiEntityManager.persist(pictureFacade);
        for (Document relation : relations) {
            processInfoNode(pictureFacade.getDelegate(), relation);
        }

    }

    private PictureFacade buildPictureFacade(Document picture, String archiveNumber) {

        final Element title = getElement(picture, "TITLE");
        final Element caption = getElement(picture, "CAPTION");
        final Element shelfNumber = getElement(picture, "SHELFNUMBER");
        final Element copyright = getElement(picture, "COPYRIGHT");
        final Element originator = getElement(picture, "ORIGINATOR");
        final Element takeCity = getElement(picture, "TAKE_CITY");
        final Element takeCountry = getElement(picture, "TAKE_COUNTRY");
        final Element description = getElement(picture, "DESCRIPTION");

        final Element instance = getElement(picture, "ns2:ARCHIVENUMBER");
        final String oid = instance.getAttribute("oid");

        final ContentItem pictureCI =
            contentItemService.createContentItem("mars/" + oid);

        entityManager.persist(pictureCI);

        final PictureFacade picFacade =
            kiwiEntityManager.createFacade(pictureCI, PictureFacade.class);

        if (caption != null) {
            final String captitionText = caption.getTextContent();
            if (captitionText != null) {
                picFacade.setCaption(captitionText);
            }
        }

        if (copyright != null) {
            final String copyRightText = copyright.getTextContent();
            if (copyRightText != null) {
                picFacade.setCopyright(copyRightText);
            }
        }

        if (shelfNumber != null) {
            final String shelfNumberText = shelfNumber.getTextContent();
            if (shelfNumberText != null) {
                picFacade.setShelfnumber(shelfNumberText);
            }
        }

        if (takeCity != null) {
            final String cityText = takeCity.getTextContent();
            if (cityText != null) {
                picFacade.setTakeCity(cityText);
            }
        }

        if (originator != null) {
            picFacade.setOriginator(originator.getTextContent());
        }

        if (takeCountry != null) {
            final String takeCountryText = takeCountry.getTextContent();
            if (takeCountryText != null) {
                picFacade.setTakeCountry(takeCountryText);
            }
        }

        if (archiveNumber != null && !archiveNumber.isEmpty()) {
            picFacade.setPictureURL("http://pf.mediamid.com/mediamanager/REST/auth/path/thumb/"+archiveNumber+".jpg");
        }

        //set Title and description
        try {
            contentItemService.updateTitle(picFacade, title.getTextContent());
            contentItemService.updateTextContentItem(picFacade, description.getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return picFacade;
    }

    private void processInfoNode(ContentItem taggedItem, Document instance) {
        final InfoNodeFacade infoNodeFacade = buildInfoNodeFacade(instance);
        kiwiEntityManager.persist(infoNodeFacade);

        final String title = infoNodeFacade.getTitle();

        // here I set the relation between the pictures and items.
        final Tag tag =
            taggingService.createTagging(title != null ? title : "topic" , taggedItem, infoNodeFacade.getDelegate(), currentUser);

        kiwiEntityManager.persist(tag);

    }

    private InfoNodeFacade buildInfoNodeFacade(Document infoNode) {
        final Element instance = getElement(infoNode, "ns2:ARCHIVENUMBER");
        final String oid = instance.getAttribute("oid");

        final Element title = getElement(infoNode, "TITLE");
        final Element topic = getElement(infoNode, "TOPIC");
        // TODO : this is not be best way to get the string but the faster one.
        final Element text = getElement(infoNode, "translatedString");


        final ContentItem infoNodeCI =
            contentItemService.createContentItem("mars/" + oid);

        entityManager.persist(infoNodeCI);

        final InfoNodeFacade infoNodeFacade =
            kiwiEntityManager.createFacade(infoNodeCI, InfoNodeFacade.class);

        if (title != null) {
            contentItemService.updateTitle(infoNodeFacade, title.getTextContent());
        }

        if (topic != null) {
            infoNodeFacade.setTopic(topic.getTextContent());
        }

        if (text != null && text.getTextContent() != null) {
            try {
                contentItemService.updateTextContentItem(infoNodeFacade, text.getTextContent());
            } catch (DOMException e) {
                log.debug(e.getMessage(), e);
            } //catch (TextContentNotChangedException e) {
                // never, the item does not have text contet.
                //log.debug(e.getMessage(), e);
            //}
        }


        return infoNodeFacade;
    }

    private Element getElement(Document document, String tagName) {
        final NodeList elements = document.getElementsByTagName(tagName);
        final Element element = (Element) elements.item(0);

        return element;
    }
}
