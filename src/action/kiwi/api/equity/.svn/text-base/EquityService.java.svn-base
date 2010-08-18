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

package kiwi.api.equity;


import java.util.Date;
import java.util.List;
import java.util.Map;

import kiwi.model.ceq.SunSpaceComunityFacade;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

/**
 * Defines the equity service behavior. This service is used to calculates
 * equity values for content items, users and tags.
 * 
 * @author mradules
 */
public interface EquityService {

    /**
     * Returns the equity value for the given user.
     * 
     * @param user the involved user.
     * @return the equity value for the given user.
     */
    double getPersonalEquity(User user);

    double getContributionEquity(User user, ContentItem contentItem);

    double getTagEquity(Tag tagging);
    double getTagEquity(ContentItem tagging);

    /**
     * Reruns the number if hits for a given user, the number if hits represents
     * the number of actions accomplish by a certain user.
     * 
     * @param user the involved user.
     * @return the number if hits for a given user.
     */
    int getHits(User user);

    /**
     * Returns the user with the best equity values.
     * 
     * @return the user with the best equity values.
     * @see #getWorstUser()
     */
    User getBestUser();

    /**
     * Returns the user with the worst equity values.
     * 
     * @return the user with the worst equity values.
     */
    User getWorstUser();

    /**
     * Returns the equity value for the given content item.
     * 
     * @param contentItem item the involved content item.
     * @return the equity value for the given content item.
     */
    double getContentItemEquity(ContentItem contentItem);

	double getContentItemEquity(Long id);

    /**
     * Reruns the number if hits for a given content item, the number if hits
     * represents the number of actions accomplish by a certain user.
     * 
     * @param contentItem item the involved content item.
     * @return the number if hits for a given user.
     */
    int getHits(ContentItem contentItem);

    /**
     * Returns the content item with the best equity values.
     * 
     * @return the content item with the best equity values.
     * @see #getWorstContentItem()
     */
    ContentItem getBestContentItem();

    /**
     * Returns the content item with the worst equity values.
     * 
     * @return the content item with the worst equity values.
     * @see #getBestContentItem()
     */
    ContentItem getWorstContentItem();

    /**
     * Returns all the equities values for an given content item in a given time
     * interval. The given interval is divided in n parts, where n has the same
     * value line the <code>steps</code> arguments - the equity values is
     * calculated for each of this part. 
     * 
     * @param contentItem the involved content item.
     * @param start the interval begin.
     * @param end the interval end.
     * @param steps the number if parts for the given interval.
     * @return all the equities values for an given content item.
     */
    Map<Date, Long> getEquityValues(ContentItem contentItem, Date start,
            Date end, int steps);

    Map<Date, Long> getEquityValues(ContentItem contentItem, Date start,
            int steps);
    
    Map<Date, Long> getEquityValues(ContentItem contentItem);

    Map<Date, Long> getEquityValues(ContentItem contentItem, int steps);

    Map<Date, String> getActivityNames(ContentItem contentItem);

    Map<Date, String> getActivityNames(ContentItem contentItem, Date start);

    Map<Date, String> getActivityNames(ContentItem contentItem, Date start,
            Date end);

	double getCommunityEquity(User u);

	List<ContentItem> getTopContributions(User u);

	double getCommunityEquity(SunSpaceComunityFacade com);
}
