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

package kiwi.service.activity;

import java.util.Set;

import javax.persistence.EntityManager;

import kiwi.api.activity.ActivityLoggingServiceLocal;
import kiwi.api.activity.ActivityLoggingServiceRemote;
import kiwi.api.config.ConfigurationService;
import kiwi.api.event.KiWiEvents;
import kiwi.config.Configuration;
import kiwi.model.Constants;
import kiwi.model.activity.AddFriendActivity;
import kiwi.model.activity.AddTagActivity;
import kiwi.model.activity.AnnotateActivity;
import kiwi.model.activity.CommentActivity;
import kiwi.model.activity.CreateActivity;
import kiwi.model.activity.DeleteActivity;
import kiwi.model.activity.EditActivity;
import kiwi.model.activity.LoginActivity;
import kiwi.model.activity.LogoutActivity;
import kiwi.model.activity.RegisterActivity;
import kiwi.model.activity.RemoveFriendActivity;
import kiwi.model.activity.RemoveTagActivity;
import kiwi.model.activity.SearchActivity;
import kiwi.model.activity.ShareActivity;
import kiwi.model.activity.TweetActivity;
import kiwi.model.activity.VisitActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * The activity logging service implements a number of observer methods for events that are
 * raised by other components for activities performed by users.
 *
 * Unlike most other components, the activity logging service currently operates directly
 * on the underlying database for performance reasons.
 *
 * @author Sebastian Schaffert
 *
 */
@Scope(ScopeType.STATELESS)
@Name("activityLoggingService")
@AutoCreate
//@Transactional
public class ActivityLoggingServiceImpl implements ActivityLoggingServiceLocal, ActivityLoggingServiceRemote {

	/**
     * Logging service.
     */
    @Logger
    private Log log;

    /**
     * The Java EE EntityManager.
     *
     * The activity logging service has direct access to the Java EE entity manager for
     * performance reasons. It does not generate RDF metadata at the moment.
     */
    @In
    private EntityManager entityManager;

    @In
    private ConfigurationService configurationService;

    private double getUnit(String key, double defaultValue) {
        final Configuration configuration =
                 configurationService.getConfiguration(key, defaultValue);
         double result = configuration.getDoubleValue();
         return result;
    }

    /**
     * Register a tagging action performed by a user.
     *
     * @param user the user who added the tag
     * @param tag the tag that has been added
     */
    @Observer(KiWiEvents.ACTIVITY_ADDTAG)
    public void tagAdded(User user, Tag tag) {
        AddTagActivity a = new AddTagActivity();
        a.setUser(user);
        a.setTag(tag);

        final double unit = getUnit(Constants.CEQ_ADD_TAG_UNIT, 10.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered add tag event by user #0 of tag #1",user.getLogin(),tag.getTaggingResource().getTitle());
    }

    /**
     * Register the removal of a tag performed by a user.
     *
     * @param user the user who removed the tag
     * @param tag the tag that has been removed
     */
    @Observer(KiWiEvents.ACTIVITY_REMOVETAG)
    public void tagRemoved(User user, Tag tag) {
        RemoveTagActivity a = new RemoveTagActivity();
        a.setUser(user);
        a.setTag(tag);

        final double unit = getUnit(Constants.CEQ_REMOVE_TAG_UNIT, 10.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered remove tag event by user #0 of tag #1",user.getLogin(),tag.getTaggingResource().getTitle());
    }

    /**
     * Register the creation of a new content item by a user.
     *
     * @param user the user who created the new content item
     * @param item the item that has been created by the user
     */
    @Observer(KiWiEvents.ACTIVITY_CREATECONTENTITEM)
    public void contentItemCreated(User user, ContentItem item) {
        CreateActivity a = new CreateActivity();
        a.setUser(user);
        a.setContentItem(item);

        final double unit = getUnit(Constants.CEQ_ADD_CONTENT_UNIT, 20.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered create event by user #0 of content item #1",user.getLogin(),item.getTitle());
    }

    /**
     * Register editing of a content item performed by a user.
     *
     * @param user the user who edited the content item
     * @param item the content item that has been edited
     */
    @Observer(KiWiEvents.ACTIVITY_EDITCONTENTITEM)
    public void contentItemEdited(User user, ContentItem item) {
        EditActivity a = new EditActivity();
        a.setUser(user);
        a.setContentItem(item);

       final double unit = getUnit(Constants.CEQ_EDIT_CONTENT_UNIT, 10.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered edit event by user #0 of content item #1",user.getLogin(),item.getTitle());
    }

    /**
     * Register the deletion of a content item by a user.
     *
     * @param user the user who deleted the content item
     * @param item the content item has been deleted
     */
    @Observer(KiWiEvents.ACTIVITY_DELETECONTENTITEM)
    public void contentItemDeleted(User user, ContentItem item) {
        DeleteActivity a = new DeleteActivity();
        a.setUser(user);
        a.setContentItem(item);

        final double unit = getUnit(Constants.CEQ_REMOVE_CONTENT_UNIT, 10.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered delete event by user #0 of content item #1",user.getLogin(),item.getTitle());
    }

    /**
     * Register that a user has visited a content item (content item has been currentContentItem).
     *
     * @param user the user who visited the ContentItem
     * @param item the content item that has been visited
     */
    @Observer(KiWiEvents.ACTIVITY_VISITCONTENTITEM)
    public void contentItemVisited(User user, ContentItem item) {
        VisitActivity a = new VisitActivity();
        a.setUser(user);
        a.setContentItem(item);

        final double unit = getUnit(Constants.CEQ_VISIT_CONTENT_UNIT, 5.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered visit event by user #0 of content item #1",user.getLogin(),item.getTitle());
    }

    /**
     * Register that a user has shared a certain content item with a set of other users.
     *
     * @param user the user who shared the content item
     * @param item the content item that has been shared
     * @param sharedWith the users this content item has been shared with
     */
    @Observer(KiWiEvents.ACTIVITY_SHARECONTENTITEM)
    public void contentItemShared(User user, ContentItem item, Set<User> sharedWith) {
        ShareActivity a = new ShareActivity();
        a.setUser(user);
        a.setContentItem(item);
        a.setSharedWith(sharedWith);

        final double unit = getUnit(Constants.CEQ_SHARE_CONTENT_UNIT, 5.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered share event by user #0 of content item #1",user.getLogin(),item.getTitle());
    }




    /**
     * Register that a user has annotated a certain content item with semantic metadata
     *
     * @param user the user who annotated the content item
     * @param item the content item that has been annotated
     */
    @Observer(KiWiEvents.ACTIVITY_ANNOTATE)
    public void contentItemAnnotated(User user, ContentItem item) {
        AnnotateActivity a = new AnnotateActivity();
        a.setUser(user);
        a.setContentItem(item);

        final double unit = getUnit(Constants.CEQ_ANNOTATE_CONTENT_UNIT, 5.0);
        a.setEquity(unit);

        entityManager.persist(item);

        log.debug("registered annotate event by user #0 of content item #1",user.getLogin(),item.getTitle());
    }

    /**
     * Register that a user has logged in to the KiWi system.
     *
     * @param user the user who logged in to the KiWi system
     */
    @Observer(KiWiEvents.ACTIVITY_LOGIN)
    public void userLogin(User user) {
        LoginActivity a = new LoginActivity();
        a.setUser(user);

        entityManager.persist(a);

        log.debug("registered login event of user #0",user.getLogin());
    }

    /**
     * Register that a user has logged out from the KiWi system.
     *
     * @param user the user who has logged out of the KiWi system
     */
    @Observer(KiWiEvents.ACTIVITY_LOGOUT)
    public void userLogout(User user) {
        LogoutActivity a = new LogoutActivity();
        a.setUser(user);

        entityManager.persist(a);

        log.debug("registered logout event of user #0",user.getLogin());
    }

    /**
     * Register that a user has registered with the KiWi system.
     *
     * @param user the user who has registered with the KiWi system
     */
    @Observer(KiWiEvents.ACTIVITY_REGISTER)
    public void userRegistered(User user) {
        RegisterActivity a = new RegisterActivity();
        a.setUser(user);

        entityManager.persist(a);

        log.debug("registered registration event of user #0",user.getLogin());
    }

    /**
     * Register that a user has performed a free-form activity inside the KiWi system
     * that he manually specified. The activity is passed as argument "message".
     *
     * @param user the user who performed the activity
     * @param message the description of the activity performed
     */
    @Observer(KiWiEvents.ACTIVITY_TWEET)
    public void tweetActivity(User user, ContentItem message) {
        TweetActivity a = new TweetActivity();
        a.setUser(user);
        a.setContentItem(message);

        entityManager.persist(a);

        log.debug("registered user activity of user #0: #1",user.getLogin(),message);
    }

    /**
     * Register that a user has performed a search inside the KiWi system. The search string
     * must be passed as argument to this method.
     *
     * @param user the user who performed the search
     * @param search the search string used by the user
     */
    @Observer(KiWiEvents.ACTIVITY_SEARCH)
    public void searchPerformed(User user, String search) {
        SearchActivity a = new SearchActivity();
        a.setUser(user);
        a.setSearchString(search);

        entityManager.persist(a);

        log.debug("registered search activity of user #0: #1",user.getLogin(), search);
    }

    /**
     * Register that a user has added a comment to a content item. The user, the commented
     * content item and the comment are passed as arguments.
     *
     * @param user the user who added the comment
     * @param parent the parent content item to which the comment was added
     * @param comment the content item used as comment
     */
    @Observer(KiWiEvents.ACTIVITY_ADDCOMMENT)
    public void commentAdded(User user, ContentItem parent, ContentItem comment) {
        CommentActivity a = new CommentActivity();
        a.setContentItem(parent);
        a.setComment(comment);
        a.setUser(user);
        
        final double unit = getUnit(Constants.CEQ_COMMENT_CONTENT_UNIT, 5.0);
        a.setEquity(unit);

        entityManager.persist(a);

        log.debug("registered comment of user #0 on #1",user.getLogin(), parent.getTitle());
    }

    /**
     * Register that a user has established a connection to a friend.
     *
     * @param user the user who established the connection
     * @param friend the user added as contact
     */
    @Observer(KiWiEvents.ACTIVITY_ADDFRIEND)
    public void friendAdded(User user, User friend) {
        AddFriendActivity a = new AddFriendActivity();

        a.setUser(user);
        a.setFriend(friend);

        final double unit = getUnit(Constants.CEQ_ADD_CONTENT_UNIT, 20.0);
        a.setEquity(unit);

        
        entityManager.persist(a);

        log.debug("user #0 added #1 as friend",user.getLogin(), friend.getLogin());
    }

    /**
     * Register that a user has removed a connection to another user.
     *
     * @param user the user removing the connection
     * @param friend the user adding the connection
     */
    @Observer(KiWiEvents.ACTIVITY_REMOVEFRIEND)
    public void friendRemoved(User user, User friend) {
        RemoveFriendActivity a = new RemoveFriendActivity();

        a.setUser(user);
        a.setFriend(friend);

        entityManager.persist(a);

        log.debug("user #0 removed #1 as friend",user.getLogin(), friend.getLogin());
    }




}
