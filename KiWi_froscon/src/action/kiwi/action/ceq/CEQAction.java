/*
 * File : CEQAction.java
 * Date : Feb 20, 2010
 *
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
 */


package kiwi.action.ceq;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import kiwi.api.equity.EquityService;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.service.equity.StoredProcedureLoader;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;


/**
 * @author mradules
 * @version 07-pre
 * @since 07-pre
 */
@Name("ceqAction")
public class CEQAction {
    
    @Logger
    private static Log log;

    @In(create = true)
    private EquityService equityService;
    
    @In(create = true)
    private TaggingService  taggingService;
    
    @In
    private EntityManager entityManager;
    
    @In 
    private StoredProcedureLoader storedProcedureLoader; 


    public double getUserEquity(User user) {
        final double result = equityService.getPersonalEquity(user);
        return result;
    }

    public int getHits(User user) {
        final int result = equityService.getHits(user);
        return result;
    }

    public double getContentItemEquity(ContentItem contentItem) {
        final double contentItemEquity =
                equityService.getContentItemEquity(contentItem);
        return contentItemEquity;
    }

    public List<String> getContentItemEquityList(ContentItem contentItem) {
        final List<String> result = new ArrayList<String>();
        final double contentItemEquity = getContentItemEquity(contentItem);
        final String token = contentItemEquity < 0 ? "-" : "+";
        for (int i = 0; i < (int) Math.abs(contentItemEquity); i++) {
            result.add(token);
        }
        return result;
    }

    public int getHits(ContentItem contentItem) {
        final int result = equityService.getHits(contentItem);
        return result;
    }

    public String getBestItem() {
        final ContentItem bestContentItem = equityService.getBestContentItem();
        return bestContentItem.getTitle();
    }

    public String getWorstItem() {
        final ContentItem bestContentItem = equityService.getWorstContentItem();
        return bestContentItem.getTitle();
    }

    public String getBestUser() {
        final User bestUser = equityService.getBestUser();
        return bestUser.getLogin();
    }

    public String getWorstUser() {
        final User worstUser = equityService.getWorstUser();
        return worstUser.getLogin();
    }
    
    
    public List<String> getActivityNames(ContentItem  contentItem) {
        
       //final Map<Date, String> activityNames = equityService.getActivityNames(contentItem);
        final Date now = new Date();
       final Date dateInThePast = new Date(now.getTime() - 1000*60*60);
       final Map<Date, String> activityNames = equityService.getActivityNames(contentItem, dateInThePast);
       final List<String> result= new ArrayList<String>();
       for (Map.Entry<Date, String> entry : activityNames.entrySet()) {
           result.add(entry.getKey() + " => " + entry.getValue());
       }

       return result;
    }
    
    public List<String> getActivityValues(ContentItem  contentItem) {
        final Map<Date, Long> equityValues = equityService.getEquityValues(contentItem, 100);
        final List<String> result= new ArrayList<String>();
        for (Map.Entry<Date, Long> entry : equityValues.entrySet()) {
            result.add(entry.getKey() + " => " + entry.getValue());
        }

        return result;
    }

    public double getTagEquity(ContentItem  contentItem) {
        final double result = equityService.getTagEquity(contentItem);
        return result;
    }
    
    public void loadStroredProcedures() {
        try {
            storedProcedureLoader.processStoredProcedures();
        } catch (ClassNotFoundException exception) {
            log.warn(exception.getMessage(), exception);
        }
    }
}
