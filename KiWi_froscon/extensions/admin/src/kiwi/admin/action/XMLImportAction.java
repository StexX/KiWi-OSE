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


package kiwi.admin.action;


import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.entiy.Gfx;
import kiwi.exception.UserExistsException;
import kiwi.export.ExportCategory;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;


/**
 * This seam component is used to import information from one or
 * more xml files paced in a given directory. For each xml file a
 * new <code>ContentItem</code> will be generated and all the
 * content item are persisted also.<br>
 * The name for this seam component is
 * <i>kiwi.admin.xmlImportAction</i>
 * 
 * @author mradules
 * @version 02-pre
 * @since 02-pre
 */
@Name("kiwi.admin.xmlImportAction")
public class XMLImportAction {

    /**
     * The persistence layer will be flush after every XXX number
     * of persisted items.
     */
    private static int FLUSH_LIMIT = 10;

    @Logger
    private static Log log;

    /**
     * Used to access the underlying persistence layer.
     */
    @In
    private KiWiEntityManager kiwiEntityManager;

    /**
     * Used to manage and manipulate content items.
     */
    @In(create = true)
    private ContentItemService contentItemService;

    @In
    private TaggingService taggingService;
    
    @In
    private UserService userService;
    
    @In
    private TripleStore tripleStore;

    /**
     * Counts the number of persisted items.
     */
    private int persistedItemsCount;

    /**
     * The import directory, from here are all the xml files
     * read.
     */
    private String dirToImport;

    public XMLImportAction() {
        // prevent the that the dirToImport is null.
        dirToImport = "";
    }

    /**
     * Gets the directory to import, from here are all the xml
     * files read.
     * 
     * @return the directory to import.
     */
    public String getDirToImport() {
        return dirToImport;
    }

    /**
     * Register a new value for the directory to import.
     * 
     * @param dirToImport the new directory to import.
     */
    public void setDirToImport(String dirToImport) {
        this.dirToImport = dirToImport;
    }

    /**
     * Reads all the files placed under the {@link #dirToImport}
     * transforms them in <code>ContentItem</code> instances and
     * persist this.<br>
     * If the {@link #dirToImport} is null or it points to a
     * non-directory entry (one something which is not a
     * directory) then this method has no effect.
     */
    public void importData() {

        if (dirToImport == null) {
            log.debug("No dir to import");
            return;
        }

        log.debug("Try to import all the information from #0", dirToImport);
        final File importDir = new File(dirToImport);
        if (!importDir.exists() || !importDir.isDirectory()) {
            return;
        }

        final File[] files = importDir.listFiles();
        for (final File file : files) {

            if (!file.toString().endsWith(".xml")) {
                continue;
            }

            final FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (final FileNotFoundException e) {
                if (log.isWarnEnabled()) {
                    log.warn("The file : #0 can not be imported", file, e);
                }
                continue;
            }

            final BufferedInputStream inputStream =
                    new BufferedInputStream(fileInputStream);
            final XMLDecoder encoder = new XMLDecoder(inputStream);

            final kiwi.export.PointOfInterest readObject;
            try {
                readObject = (kiwi.export.PointOfInterest) encoder.readObject();
            } catch (final Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("The file : #0 can not be imported", file, e);
                }
                continue;
            }

            encoder.close();

            final PointOfInterestFacade toPersist =
                    buildPointOfInterest(readObject);
            kiwiEntityManager.persist(toPersist);

            if (persistedItemsCount % FLUSH_LIMIT == 0) {
                kiwiEntityManager.flush();
            }
            persistedItemsCount++;

            if (log.isDebugEnabled()) {
                log.debug("The item #0 was importerd.", file);
            }
        }
    }

    /**
     * Builds a <code>PointOfInterestFacade</code> for a given
     * <code>kiwi.export.PointOfInterest</code>.
     * 
     * @param in the source data, the information from this is
     *            used to build the resulted
     *            <code>PointOfInterestFacade</code>.
     * @return a <code>PointOfInterestFacade</code> for a given
     *         <code>kiwi.export.PointOfInterest</code>.
     */
    private PointOfInterestFacade buildPointOfInterest(
            kiwi.export.PointOfInterest in) {

        if (in == null) {
            final NullPointerException nullEx =
                    new NullPointerException(
                            "The specified PointOfInterest is null.");
            log.debug(nullEx.getMessage(), nullEx);
            throw nullEx;
        }
        
        User user = userService.getUserByLogin(in.getUser().getUsername());
        if(user == null) {
        	try {
				user = userService.createUser(
						in.getUser().getUsername(), in.getUser().getFullname(), "", "randomPW");
			} catch (UserExistsException e) {
				e.printStackTrace();
			}
        }

        final ContentItem contentItem = contentItemService.createContentItem();
        final PointOfInterestFacade result =
                kiwiEntityManager.createFacade(contentItem,
                        PointOfInterestFacade.class);

        final String title = in.getTitel();
        contentItemService.updateTitle(result, title);

        result.setAuthor(user);
        
        final String latitudeStr = in.getLatitude();
        final double latitude = Double.parseDouble(latitudeStr);
        final String longitudeStr = in.getLongitude();
        final double longitude = Double.parseDouble(longitudeStr);

        result.setLatitude(latitude);
        result.setLongitude(longitude);

        final String comment = in.getComment();
        if (comment != null) {
            final ContentItem contentItemComment =
                    contentItemService.createContentItem();
            contentItemService.updateTextContentItem(contentItemComment, comment);
            final LinkedList<ContentItem> comments =
                    new LinkedList<ContentItem>();
            comments.add(contentItemComment);
            result.setComments(comments);
        }

        final String adresse = in.getAdresse();
        result.setAddress(adresse);

        final Date datum = in.getDatum();
        result.setCreated(datum);

        
        // text content
        if(in.getBeschreibung() != null) {
        	contentItemService.updateTextContentItem(result, in.getBeschreibung());
        }
        
        // categories
        for(ExportCategory cat : in.getCategorites()) {
        	ContentItem ci_cat = contentItemService.getContentItemByTitle(cat.getName());
        	if(ci_cat == null) {
        		ci_cat = contentItemService.createContentItem();
        		contentItemService.updateTitle(ci_cat, cat.getName());
        	}
        	Tag t_cat = taggingService.createTagging(cat.getName(), result.getDelegate(), ci_cat, user);
        	
        	
        }
        
        final Set<Gfx> pictures = in.getPictures();

        final LinkedList<ContentItem> media = new LinkedList<ContentItem>();
        for (final Gfx pic : pictures) {
            final String pathname = dirToImport + "/gfx" + "/" + pic.getImage();

            final byte[] bytes;
            try {
                bytes = FileUtils.readFileToByteArray(new File(pathname));
            } catch (final Exception e) {
                log.warn("Error reading the  file : #0 ", pathname);
                continue;
            }

            
            final ContentItem picItem = contentItemService.createMediaContentItem(bytes, "image", pic.getImage());

            // TODO: Mihai: is this a bug, or deliberate?
            contentItemService.updateTitle(contentItem, pic.getImage());
            
            /*
            final ContentItem picItem = contentItemService.createContentItem();
            final MediaContent mediaContent = new MediaContent(picItem);
            mediaContent.setData(bytes);
            mediaContent.setMimeType("image");
            mediaContent.setFileName(pic.getImage());

            // I must persist the picture extra because event if
            // the owner content item is persisted its
            // relations (it does not cascade).
            kiwiEntityManager.persist(picItem);
            */

// result.setMediaContent(mediaContent);
            media.add(picItem);

            log.debug("Image file : #0 was successful proccess", pathname);
        }

        result.setMultimedia(media);

        result.addType(tripleStore
                .createUriResource("http://www.newmedialab.at/fcp/Location"));
        return result;
    }
}
