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
package kiwi.api.event;

/**
 * @author Sebastian Schaffert
 *
 */
public class KiWiEvents {

    public final static String SEAM_POSTINIT =
            "org.jboss.seam.postInitialization";

    public final static String ENTITY_PERSISTED = "kiwiEntityPersisted";

    public final static String ENTITY_REMOVED = "kiwiEntityRemoved";

    public final static String TRANSACTION_SUCCESS = "kiwiTransactionSuccess";

    public final static String TRANSACTION_ABORT = "kiwiTransactionAbort";

    public final static String CONTENTITEM_CHANGED = "contentItemChanged";

    public final static String CONTENT_UPDATED = "contentUpdated";

    public final static String METADATA_UPDATED = "metadataUpdated";

    public final static String TITLE_UPDATED = "titleUpdated";

    public final static String ITEM_CREATED = "itemCreated";

    public final static String ITEM_REMOVED = "itemRemoved";

    /**
     * Raised immediately when a new triple not yet existant in
     * the database is created. The triple is passed as argument
     * of this event.
     */
    public final static String TRIPLE_CREATED = "tripleCreated";

    /**
     * Raised immediately when a triple is marked as deleted. The
     * triple is passed as argument of this event.
     */
    public final static String TRIPLE_REMOVED = "tripleRemoved";

    /**
     * Raised on transaction success when the triple store is
     * updated, i.e. a new triple is added or an existing triple
     * is removed.
     */
    public final static String TRIPLESTORE_UPDATED = "tripleStoreUpdated";

    /**
     * Raised when the import service is started; observed by importer components that then
     * register with the import service.
     */
    public final static String IMPORTSERVICE_INIT = "importServiceInitialised";

    /**
     * Raised when the import service is started; observed by importer components that then
     * register with the import service.
     */
    public final static String EXPORTSERVICE_INIT = "exportServiceInitialised";

    /**
     * Raised when the extension service is started; observed by extensions that then
     * register with the extension service.
     */
    public final static String EXTENSIONSERVICE_INIT = "extensionServiceInitialised";

    /**
     * Raised when the extension service is started; observed by extensions that then
     * register with the extension service.
     */
    public final static String CONFIGURATIONSERVICE_INIT = "configurationServiceInitialised";

    public final static String CONFIGURATIONSERVICE_CREATE_ADMIN = "configurationServiceCreateAdmin";

//    public final static String CONFIGURATIONSERVICE_INIT_NONASYNC = "configurationServiceInitialised_nonasync";

    /* activity events */
    public final static String ACTIVITY_ADDTAG = "activity.addTag";

    public final static String ACTIVITY_REMOVETAG = "activity.removeTag";

    public final static String ACTIVITY_CREATECONTENTITEM =
            "activity.createContentItem";

    public final static String ACTIVITY_EDITCONTENTITEM =
            "activity.editContentItem";

    public final static String ACTIVITY_DELETECONTENTITEM =
            "activity.deleteContentItem";

    public final static String ACTIVITY_VISITCONTENTITEM =
            "activity.visitContentItem";

    public final static String ACTIVITY_SHARECONTENTITEM =
            "activity.shareContentItem";

    public final static String ACTIVITY_ANNOTATE = "activity.annotate";

    public final static String ACTIVITY_LOGIN = "activity.login";

    public final static String ACTIVITY_LOGIN_FB = "activity.login.facebook";

    public final static String ACTIVITY_REGISTER_FB = "activity.register.facebook";

    public final static String EXCEPTION_REGISTER_FB_USERNAMEEXISTS
    	= "activity.register.facebook.userNameExists";

    public final static String ACTIVITY_LOGOUT = "activity.logout";

    public final static String ACTIVITY_REGISTER = "activity.register";

    public final static String ACTIVITY_TWEET = "activity.tweet";

    public final static String ACTIVITY_SEARCH = "activity.search";

    public final static String ACTIVITY_ADDCOMMENT = "activity.addComment";

    public final static String ACTIVITY_ADDFRIEND = "activity.addFriend";

    public final static String ACTIVITY_REMOVEFRIEND = "activity.removeFriend";

    public final static String ACTIVITY_RATECONTENTITEM = "activity.itemRated";

    public final static String ACTIVITY_UNRATECONTENTITEM =
            "activity.itemUnrated";

    public final static String ACTIVITY_GROUP_CREATED =
        "activity.groupCreated";

    public final static String ACTIVITY_GROUP_REMOVED =
        "activity.groupRemoved";

    public final static String ACTIVITY_USER_CREATED =
        "activity.userCreated";

    public final static String ACTIVITY_USER_REMOVED =
        "activity.userRemoved";

    public final static String ACTIVITY_AUTHOR_CHANGED =
            "activity.authorChanged";

    public final static String ACTIVITY_ACCES_DENIED = "activity.accessDenied";

    // CEQ events
    private static final String CEQ_PREFIX = "ceq.";

    public final static String CEQ_ITEM_CREATED = CEQ_PREFIX + ITEM_CREATED;

    public final static String CEQ_ITEM_REMOVED = CEQ_PREFIX + ITEM_REMOVED;

    public final static String CEQ_ACTIVITY_ADDTAG =
            CEQ_PREFIX + ACTIVITY_ADDTAG;

    public final static String CEQ_ACTIVITY_REMOVETAG =
            CEQ_PREFIX + ACTIVITY_REMOVETAG;

    public final static String CEQ_ACTIVITY_ANNOTATE =
            CEQ_PREFIX + ACTIVITY_ANNOTATE;

    public final static String CEQ_ACTIVITY_CREATECONTENTITEM =
            CEQ_PREFIX + ACTIVITY_CREATECONTENTITEM;

    public final static String CEQ_ACTIVITY_DELETECONTENTITEM =
            CEQ_PREFIX + ACTIVITY_DELETECONTENTITEM;

    public final static String CEQ_ACTIVITY_EDITCONTENTITEM =
            CEQ_PREFIX + ACTIVITY_EDITCONTENTITEM;

    public final static String CEQ_ACTIVITY_SHARECONTENTITEM =
            CEQ_PREFIX + ACTIVITY_SHARECONTENTITEM;

    public final static String CEQ_ACTIVITY_VISITCONTENTITEM =
            CEQ_PREFIX + ACTIVITY_VISITCONTENTITEM;

    public final static String CEQ_ACTIVITY_RATECONTENTITEM =
            CEQ_PREFIX + ACTIVITY_RATECONTENTITEM;

    public final static String CEQ_ACTIVITY_UNRATECONTENTITEM =
            CEQ_PREFIX + ACTIVITY_UNRATECONTENTITEM;

    public final static String CEQ_ACTIVITY_ADDCOMMENT =
            CEQ_PREFIX + "activity.addComment";

    public final static String CEQ_ACTIVITY_REGISTER_USER =
        CEQ_PREFIX + KiWiEvents.ACTIVITY_REGISTER;

}
