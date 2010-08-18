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

package kiwi.model.activity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


import kiwi.model.user.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.validator.NotNull;

/**
 * Activities describe actions performed by the user. They are used for tracking
 * certain kinds of user interactions with the system and can be used both for
 * informational purposes and for building up a user model that can then be
 * used e.g. for personalisation purposes. The following kinds of activities
 * are currently forseen:
 * <ul>
 * <li>visiting a content item (VISIT)</li>
 * <li>creating a new content item (CREATE) of a certain type</li>
 * <li>editing a content item (EDIT)</li>
 * <li>deleting a content item (DELETE)</li>
 * <li>tagging a content item with a certain tag (ADD_TAG)</li>
 * <li>removing a tag from a content item (REMOVE_TAG)</li>
 * <li>uploading a multimedia document (UPLOAD)</li>
 * <li>registering (REGISTER)</lI>
 * <li>logging in (LOGIN)</li>
 * <li>logging out (LOGOUT)</li>
 * </ul>
 * At a later stage, we could have in addition the following types of activities:
 * <ul>
 *   <li>connecting to another user (social networking)</li>
 * </ul>
 *
 * All activities are associated at least with a user and possibly with a content item
 * and a tag, and all activities have a timestamp and an IP address.
 *
 * @author Sebastian Schaffert
 * @see ActivityTypes
 */
@Entity
@Immutable
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
    @NamedQuery(name  = "activities.listActivities",
                query = "select a from Activity a order by created desc"),
    @NamedQuery(name  = "activities.listActivitiesByUser",
                query = "select a from Activity a, User u left join u.watchedContent " +
                        "where u = :user " +
                        "and (a.user.id = u.id " +
                        "     or a.contentItem in elements(u.watchedContent)" +
                        "     or a.user in elements(u.friends)" +
                        "     ) " +
                        "and a.class != 'VISIT' " +
                        "and a.class != 'LOGIN' " +
                        "and a.class != 'LOGOUT' " +
                        "and a.class != 'SEARCH' " +
                        "order by a.created desc"),
      @NamedQuery(name  = "activities.listTweetActivitiesByUser",
                  query = "select a from Activity a, User u where u = :user and a.class = 'TWEET'"
                      +"and (a.user.id = u.id " +
                        "     or a.contentItem in elements(u.watchedContent)" +
                        "     or a.user in elements(u.friends)" +
                        "     )  order by a.created desc"),
    @NamedQuery(name  = "activities.listActivitiesByContentItem",
                query = "select a " +
                        "from Activity a left join a.tag t " +
                        "where (a.contentItem = :ci or t.taggedResource = :ci) " +
                        "and a.class != 'VISIT' " +
                        "order by a.created desc"),
    @NamedQuery(name  = "activities.listActivitiesByContentItemURI",
                query = "select a " +
                        "from ContentItem ci, Activity a left join a.tag t " +
                        "where ci.resource.uri = :uri" +
                        " and (a.contentItem = ci or t.taggedResource = ci) " +
                        " and a.class != 'VISIT' " +
                        " order by a.created desc"),
    @NamedQuery(name  = "activities.listLastUserVisits",
                query = "select a " +
                        "from VisitActivity a inner join fetch a.contentItem " +
                        "where a.user.login = :login " +
                        "order by a.created desc"),
    @NamedQuery(name  = "activities.listLastUserEdits",
                query = "select a " +
                        "from EditActivity a inner join fetch a.contentItem " +
                        "where a.user.login = :login " +
                        "order by a.created desc"),
    @NamedQuery(name  = "activities.listLastUserTags",
                query = "select a " +
                        "from AddTagActivity a inner join fetch a.tag " +
                        "where a.user.login = :login " +
                        "order by a.created desc"),
    @NamedQuery(name  = "activities.listLastUserComments",
                query = "select a " +
                        "from CommentActivity a inner join fetch a.comment inner join fetch a.contentItem " +
                        "where a.user.login = :login " +
                        "order by a.created desc"),
    @NamedQuery(name  = "activities.listLastUserSearches",
                query = "select a " +
                        "from SearchActivity a " +
                        "where a.user.login = :login " +
                        "order by a.created desc"),
    @NamedQuery(name  = "activities.listLastDistinctUserSearches",
                query = "select a " +
                        "from SearchActivity a " +
                        "where a.user.login = :login " +
                        "order by a.created desc group by a.searchString"),
    @NamedQuery(name  = "activities.listLastDistinctUserVisits",
			            query = "select a " +
			            "from VisitActivity a inner join fetch a.contentItem " +
			            "where a.user.login = :login " +
			            "order by a.created desc group by a.contentItem.title")                     
})
public abstract class Activity {

    /** The database identifier of this activity. */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;

    /** The user who performed the activity. */
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @Index(name="idx_activity_user")
    @NotNull
    private User user;

    /** The time at which the activity was performed. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false,updatable=false)
    @NotNull
    private Date created;

    /**
     * The equity values for this action.
     */
    private double equity;

    @Version
    private Long version;


    /**
     * Initialise the activity by setting the current time stamp.
     */
    public Activity() {
        this.created = new Date();
    }

    /**
     * Return an identifier that can be used for looking up a description of this activity
     * in the localized message bundles.
     * @return the message identifier for this activity
     */
    public abstract String getMessageIdentifier();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }


    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }


    /**
     * @return the equity
     */
    public double getEquity() {
        return equity;
    }

    /**
     * @param equity the equity to set
     */
    public void setEquity(double equity) {
        this.equity = equity;
    }
}
