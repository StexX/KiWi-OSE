/*
 * File : EquityServiceImpl.java Date : Feb 22, 2010 DO NOT ALTER OR REMOVE
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


package kiwi.service.equity;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.equity.EquityService;
import kiwi.model.ceq.SunSpaceComunityFacade;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Used to calculates the equity values for content items, user and tags.<br>
 * <b>Note :</b> You must be shore that the st.procedures are installed, use for
 * this ant task addStoringProcedure.XXX according with your system. </br> FIXME
 * : move all the native queries in orm files to ensure the portability.
 * 
 * @author mradules
 * @version 07-pre
 * @since 07-pre
 */
@Name("equityService")
@AutoCreate
public class EquityServiceImpl implements EquityService {

    private static final int DEFAULT_STEP = 10;

    @Logger
    private static Log log;

    @In
    private EntityManager entityManager;

    /**
     * @param user the involved user, can not be null.
     * @throws NullPointerException if the user argument is null.
     */
    @Override
    public double getPersonalEquity(User user) {

        if (user == null) {
            throw new NullPointerException("The user can ntobe null.");
        }

        // FIXME : This query must be named query, use for this named
        // native query with mapped result.
        final String queryStr = "SELECT SUM(calculate_equity(testAct.created, testAct.equity, testAct.dtype)) "
                + "FROM activity AS testAct "
                + "WHERE testAct.user_id = :user_id";

        final Query query = entityManager.createNativeQuery(queryStr);
        query.setParameter("user_id", user.getId());
        final Object singleResult = query.getSingleResult();

        if(singleResult == null){
        	log.error("getPersonalEquity: Query returns null! login(id): #0(#1) -> returning 0.", user.getLogin(), user.getId());
        	return 0;
        }
        
        // FIXME : I don't know why I need to to this
        // (in this way I avoid a cast exception).
        final double result = Double.valueOf(singleResult.toString())
                .doubleValue();
        return result;
    }

    /**
     * @param user the involved user, can not be null.
     * @throws NullPointerException if the user argument is null.
     */
    @Override
    public int getHits(User user) {

        if (user == null) {
            throw new NullPointerException("The user can ntobe null.");
        }

        final Query query = entityManager.createQuery("SELECT COUNT(testUser) "
                + "FROM Activity.user AS testUser "
                + "WHERE testUser.id = :user_id ");

        query.setParameter("user_id", user.getId());
        final Object singleResult = query.getSingleResult();
        // FIXME : I don't know why I need to to this
        // (in this way I avoid a cast exception).
        final int result = Integer.valueOf(singleResult.toString()).intValue();
        return result;
    }
    
    /**
     * @param contentItem the involved item, can not be null.
     * @throws NullPointerException if the contentItem argument is null.
     */
    @Override
    public double getContentItemEquity(Long id) {
    	final String queryStr = "SELECT SUM(calculate_equity(testAct.created, :action_date, testAct.equity, testAct.dtype)) "
            + "FROM activity AS testAct "
            + "WHERE testAct.contentitem_id = :contentitem_id";

    	final Query query = entityManager.createNativeQuery(queryStr);
    	query.setParameter("action_date", new Date());
    	query.setParameter("contentitem_id", id);

    	final Object singleResult = query.getSingleResult();
    	// FIXME : I don't know why I need to to this (in this way I avoid a
    	// cast exception).
    	if( singleResult != null ) return Double.valueOf(singleResult.toString()).doubleValue();
    	else return 0;
    }

    /**
     * @param contentItem the involved item, can not be null.
     * @throws NullPointerException if the contentItem argument is null.
     */
    @Override
    public double getContentItemEquity(ContentItem contentItem) {

        if (contentItem == null) {
            throw new NullPointerException("The user can ntobe null.");
        }

        // final String queryStr =
        // "SELECT SUM(fast_linearaging(testAct.created, testAct.equity)) "
        // + "FROM activity AS testAct "
        // + "WHERE testAct.contentitem_id = :contentitem_id";

        final String queryStr = "SELECT SUM(calculate_equity(testAct.created, :action_date, testAct.equity, testAct.dtype)) "
                + "FROM activity AS testAct "
                + "WHERE testAct.contentitem_id = :contentitem_id";

        final Query query = entityManager.createNativeQuery(queryStr);
        query.setParameter("action_date", new Date());
        query.setParameter("contentitem_id", contentItem.getId());

        final Object singleResult = query.getSingleResult();
        // FIXME : I don't know why I need to to this (in this way I avoid a
        // cast exception).
        if( singleResult != null ) return Double.valueOf(singleResult.toString()).doubleValue();
        else return 0;
    }

    @Override
    public Map<Date, Long> getEquityValues(ContentItem contentItem) {
        return getEquityValues(contentItem, DEFAULT_STEP);
    }

    /**
     * @param contentItem the involved item, can not be null.
     * @throws NullPointerException if the contentItem argument is null.
     */
    @Override
    public Map<Date, Long> getEquityValues(ContentItem contentItem, int steps) {
        final String timeQueryStr = "SELECT act.created "
                + "FROM  ContentItemActivity AS act "
                + "ORDER BY act.created ASC";
        final Query timeQuery = entityManager.createQuery(timeQueryStr);
        final List resultList = timeQuery.getResultList();
        // here this I obtain the date for the first action.
        final Date statDate = (Date) resultList.get(0);
        final Date endDate = new Date();

        return getEquityValues(contentItem, statDate, endDate, steps);
    }

    @Override
    public Map<Date, Long> getEquityValues(ContentItem contentItem, Date start,
            Date end, int steps) {

        if (contentItem == null) {
            throw new NullPointerException("The content item can not be null.");
        }

        if (start == null || end == null) {
            throw new NullPointerException(
                    "The start or the end can not be null.");
        }

        if (start.getTime() >= end.getTime()) {
            throw new IllegalArgumentException(
                    "The start date must the biger than end date.");
        }

        if (steps <= 1) {
            throw new IllegalArgumentException(
                    "The step must be greater than 1.");
        }

        // FIXME : this can be replaced by a JPQL and in this way I don't need
        // an other string procedure :).
        final String createEquitiesTimesStr = "SELECT select_equity_times(:start_time, :end_time, :steps)";
        final Query createEquitiesQuery = entityManager
                .createNativeQuery(createEquitiesTimesStr);
        createEquitiesQuery.setParameter("start_time", start);
        createEquitiesQuery.setParameter("end_time", end);
        createEquitiesQuery.setParameter("steps", Integer.valueOf(steps));

        final Object singleResult = createEquitiesQuery.getSingleResult();
//        System.out.println(singleResult);

        // FIXME : the calculate_equity can be avoided
        final String equitieValuesStr = "SELECT equity_time_intervals.compute_time, calculate_equity(equity_time_intervals.compute_time, :content_item_id) "
                + "FROM equity_time_intervals "
                + "ORDER BY equity_time_intervals.compute_time ASC";
        final Query equitieValuesQuery = entityManager
                .createNativeQuery(equitieValuesStr);
        final Long id = contentItem.getId();
        equitieValuesQuery.setParameter("content_item_id", id);

        final Map<Date, Long> result = getEquityMap(equitieValuesQuery);

        log.debug("Equity values for the inteval #0 - #1 are : #2", start, end,
                result);
        return result;
    }

    private Map<Date, Long> getEquityMap(final Query query) {
        final List<?> resultList = query.getResultList();
        final Map<Date, Long> result = new LinkedHashMap<Date, Long>();
        for (final Object o : resultList) {
            final Object[] objs = (Object[]) o;
            final Date date = (Date) objs[0];
            final Double equity = (Double) objs[1];
            if (date != null && equity != null) {
                result.put(date, Long.valueOf(equity.longValue()));
            }
        }
        return result;
    }

    @Override
    public Map<Date, Long> getEquityValues(ContentItem contentItem, Date start,
            int steps) {
        final Date now = new Date();
        final Map<Date, Long> result = getEquityValues(contentItem, start, now,
                steps);
        return result;
    }

    /**
     * @param contentItem the involved item, can not be null.
     * @throws NullPointerException if the contentItem argument is null.
     */
    @Override
    public int getHits(ContentItem contentItem) {
        final Query query = entityManager.createQuery("SELECT COUNT(testAct) "
                + "FROM ContentItemActivity AS testAct "
                + "WHERE testAct.contentItem.id = :contentitem_id ");
        query.setParameter("contentitem_id", contentItem.getId());

        final Object singleResult = query.getSingleResult();
        // FIXME : I don't know why I need to to this
        // (in this way I avoid a cast exception).
        final int result = Integer.valueOf(singleResult.toString()).intValue();

        return result;
    }

    @Override
    public ContentItem getBestContentItem() {
        // FIXME : under normal circumstances I don't need the where clause,
        // I do this only to avoid the null id for the content id

        final String queryStr = "SELECT testAct.contentitem_id "
                + "FROM activity AS testAct "
                + "WHERE testAct.contentitem_id IS NOT NULL "
                + "ORDER BY calculate_equity(testAct.created, testAct.equity, testAct.dtype) "
                + "DESC";
        final Query query = entityManager.createNativeQuery(queryStr);
        final List<?> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }

        final Object resultId = resultList.get(0);
        final Long id = Long.valueOf(resultId.toString());

        // FIXME : use result mapping to get the entity and to
        // avoid the second select (from find)
        final ContentItem find = entityManager.find(ContentItem.class, id);
        return find;
    }

    @Override
    public ContentItem getWorstContentItem() {
        // FIXME : under normal circumstances I don't need the where clause,
        // I do this only to avoid the null id for the content id

        final String queryStr = "SELECT testAct.contentitem_id "
                + "FROM activity AS testAct "
                + "WHERE testAct.contentitem_id IS NOT NULL "
                + "ORDER BY calculate_equity(testAct.created, testAct.equity, testAct.dtype) "
                + "ASC";
        final Query query = entityManager.createNativeQuery(queryStr);
        final List<?> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }

        final Object resultId = resultList.get(0);
        final Long id = Long.valueOf(resultId.toString());

        // FIXME : use result mapping to get the entity and to
        // avoid the second select (from find)
        final ContentItem find = entityManager.find(ContentItem.class, id);
        return find;
    }

    @Override
    public User getBestUser() {
        // FIXME : under normal circumstances I don't need the where clause,
        // I do this only to avoid the null id for the content id
        final String queryStr = "SELECT testAct.user_id "
                + "FROM activity AS testAct "
                + "WHERE testAct.contentitem_id IS NOT NULL "
                + "ORDER BY calculate_equity(testAct.created, testAct.equity, testAct.dtype) "
                + "DESC";
        final Query query = entityManager.createNativeQuery(queryStr);
        final List<?> resultList = query.getResultList();
        final Long id = Long.valueOf(resultList.get(0).toString());

        // FIXME : use result mapping to get the entity and to
        // avoid the second select (from find)
        final User find = entityManager.find(User.class, id);
        return find;
    }

    @Override
    public User getWorstUser() {
        // FIXME : under normal circumstances I don't need the where clause,
        // I do this only to avoid the null id for the content id
        final String queryStr = "SELECT testAct.user_id "
                + "FROM activity  AS testAct "
                + "WHERE testAct.contentitem_id IS NOT NULL "
                + "ORDER BY calculate_equity(testAct.created, testAct.equity, testAct.dtype) "
                + "ASC";
        final Query query = entityManager.createNativeQuery(queryStr);
        final List<?> resultList = query.getResultList();
        final Long id = Long.valueOf(resultList.get(0).toString());

        // FIXME : use result mapping to get the entity and to
        // avoid the second select (from find)
        final User find = entityManager.find(User.class, id);
        return find;
    }

    @Override
    public Map<Date, String> getActivityNames(ContentItem contentItem,
            Date start, Date end) {
        final String queryStr = "SELECT act.created, act "
                + "FROM ContentItemActivity AS act "
                + "WHERE (act.contentItem.id = :cont_item_id) "
                + "AND (act.created BETWEEN :start_date AND :end_date)";
        final Query query = entityManager.createQuery(queryStr);
        query.setParameter("cont_item_id", contentItem.getId());
        query.setParameter("start_date", start);
        query.setParameter("end_date", end);
        final List<?> resultList = query.getResultList();

        final Map<Date, String> result = new HashMap<Date, String>();
        for (final Object o : resultList) {
            final Object[] objs = (Object[]) o;
            final Date date = (Date) objs[0];
            // disclaimer : this is a very fast and not indicated way to get the
            // discriminator column value.
            // For this purposes this is ok but this may bring solution
            // in other contexts. Don't copy&paste it.
            final String name = objs[1].getClass().getSimpleName();
            result.put(date, name);
        }

        log.debug("Activities : #0", result);
        return result;
    }

    @Override
    public Map<Date, String> getActivityNames(ContentItem contentItem,
            Date start) {
        final Date now = new Date();
        return getActivityNames(contentItem, start, now);
    }

    @Override
    public Map<Date, String> getActivityNames(ContentItem contentItem) {
        final String queryStr = "SELECT act.created, act "
                + "FROM ContentItemActivity AS act "
                + "WHERE (act.contentItem.id = :cont_item_id) ";
        final Query query = entityManager.createQuery(queryStr);
        query.setParameter("cont_item_id", contentItem.getId());
        final List<?> resultList = query.getResultList();

        final Map<Date, String> result = new HashMap<Date, String>();
        for (final Object o : resultList) {
            final Object[] objs = (Object[]) o;
            final Date date = (Date) objs[0];
            // disclaimer : this is a very fast and not indicated way to get the
            // discriminator column value.
            // For this purposes this is ok but this may bring solution
            // in other contexts. Don't copy&paste it.
            final String name = objs[1].getClass().getSimpleName();
            result.put(date, name);

        }

        log.debug("Activities : #0", result);
        return result;
    }

    @Override
    public double getContributionEquity(User user, ContentItem contentItem) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getTagEquity(ContentItem tagging) {

        // I must check the both side of the tag the tagged and the tagging.
        // I get all the items which are tagged with a given content item (first
        // where part)
        // and after this I get all the items which are tagging (second part).
        // String tagQueryStr =
        // "SELECT SUM(fast_linearaging(act.created, act.equity)) " +
        final String tagQueryStr = "SELECT SUM(calculate_equity(testAct.created, testAct.equity, testAct.dtype)) "
                + "FROM activity AS testAct, tag AS t "
                + "WHERE t.taggedresource_id = testAct.contentitem_id "
                + "AND t.taggingresource_id = :tagging_id";
        final Query query = entityManager.createNativeQuery(tagQueryStr);
        final Long id = tagging.getId();
        query.setParameter("tagging_id", id);

        final Double result = (Double) query.getSingleResult();
        log.debug("Tagging content item [#0] equity : #1", tagging.getTitle(),
                result);
        return result == null ? 0 : result.doubleValue();
    }

	@Override
	public double getTagEquity(Tag tagging) {
		return getTagEquity(tagging.getResource().getContentItem());
	}

	@Override
	public double getCommunityEquity(User u) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ContentItem> getTopContributions(User u) {
		// TODO Auto-generated method stub
		return new LinkedList<ContentItem>();
	}

	@Override
	public double getCommunityEquity(SunSpaceComunityFacade com) {
		// TODO Auto-generated method stub
		return 0;
	}
}
