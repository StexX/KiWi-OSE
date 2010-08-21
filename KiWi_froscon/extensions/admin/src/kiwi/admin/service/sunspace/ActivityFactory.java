/*
 * File : ActivityFactory.java.java Date : Apr 9, 2010 DO NOT ALTER OR REMOVE
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

import kiwi.model.activity.*;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

/**
 * Used to build kiwi activities for a given id. The supported id are : 0..12,
 * 15, 21..25, 29..38. The list below follows the syntax (id (ceq meaning) -
 * kiwi action).
 * <ul>
 * <li>0 (Attachment created) - CreateActivity
 * <li>1 (Attachment downloaded) - VisitActivity
 * <li>2 (Attachment rated 1) - RateActivity
 * <li>3 (Attachment rated 2) - RateActivity
 * <li>4 (Attachment rated 3) - RateActivity
 * <li>5 (Attachment rated 4) - RateActivity
 * <li>6 (Attachment rated 5) - RateActivity
 * <li>7 (Attachment commented) - CommentActivity
 * <li>8 (Attachment replaced) - EditActivity
 * <li>9 (Attachment meta edited) - EditActivity
 * <li>10 (Attachment tagged) - AddTagActivity
 * <li>11 (Attachment deleted) - DeleteActivity
 * <li>12 (Attachment meta viewed) - VisitActivity
 * <li>15 (Wiki created) - CreateActivity
 * <li>21 (Wiki commented) - CommentActivity
 * <li>22 (Wiki edited) - EditActivity
 * <li>23 (Wiki meta edited) - EditActivity
 * <li>23 (Wiki meta edited) - EditActivity
 * <li>25 (Group created) - GroupCreatedActivity
 * <li>29 (Access denied group) - RegisterActivity
 * <li>30 (Access denied info) - RegisterActivity
 * <li>31 (Login) - LoginActivity
 * <li>32 (Login failed pwd) - LoginActivity
 * <li>33 (Login failed user) - LoginActivity
 * <li>35 (Wiki viewed) - VisitActivity
 * <li>36 (Wiki deleted) - DeleteActivity
 * <li>37 (Author changed) - EditActivity
 * <li>38 (Attachment version changed) - EditActivity
 * </ul>
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
final class ActivityFactory {

    /**
     * Don't let anybody to instantiate this class.
     */
    private ActivityFactory() {
        // UNIMPLEMENTED
    }

    /**
     * Returns a <code>Activity</code> instance for the given id. <br>
     * Supported id are : 0..12, 15, 21..25, 29..38, for all
     * other ids values this values returns null.
     *
     * @param id action id (see the class comment for more
     *            information).
     * @param contentItem the content item for the resulted kiwi
     *            action.
     * @param user the user for the resulted kiwi action.
     * @param created date when the activity is created.
     * @return the id for the given id or null otherwise.
     */
    static Activity getActivity(final int id, final ContentItem contentItem,
            final User user, Date created) {
        final Activity result;

        // mihai : I know, this method is to long but this is the shortest way
        // to treat so many ids.
        switch (id) {
            case 0:
                // Attachment created
                result = new CreateActivity();
                break;
            case 1:
                // Attachment downloaded
                result = new VisitActivity();
                break;
            case 2:
                // $FALLS-THROUGH$
            case 3:
                // $FALLS-THROUGH$
            case 4:
                // $FALLS-THROUGH$
            case 5:
                // $FALLS-THROUGH$
            case 6:
                // Attachment rated 1..5
                result = new RateActivity();
                break;
            case 7:
                // Attachment commented
                result = new CommentActivity();
                break;
            case 8:
                // Attachment replaced
                result = new EditActivity();
                break;
            case 9:
                // Attachment meta edited
                result = new EditActivity();
                break;
            case 10:
                // Attachment tagged
                result = new AddTagActivity();
                break;
            case 11:
                // Attachment deleted
                result = new DeleteActivity();
                break;
            case 12:
                // Attachment meta viewed
                result = new VisitActivity();
                break;
            case 15:
                // Wiki created
                result = new CreateActivity();
                break;
            case 21:
                // Wiki commented
                result = new CommentActivity();
                break;
            case 22:
                // Wiki edited
                result = new EditActivity();
                break;
            case 23:
                // Wiki meta edited
                result = new EditActivity();
                break;
            case 25:
                // Group created
                result = new GroupCreatedActivity();
                break;
            case 29:
                // Access denied group
                result = new AccessDeniedActivity();
                break;
            case 30:
                // Access denied info
                result = new AccessDeniedActivity();
                break;
            case 31:
                // Login
                result = new LoginActivity();
                break;
            case 32:
                // Login failed pwd
                result = new LoginActivity();
                break;
            case 33:
                // Login failed user
                result = new LoginActivity();
                break;
            case 35:
                // Wiki viewed
                result = new VisitActivity();
                break;
            case 36:
                // Wiki deleted
                result = new DeleteActivity();
                break;
            case 37:
                // Author changed
                result = new ChangeAuthorActivity();
                break;
            case 38:
                // Attachment version changed
                result = new EditActivity();
                break;
            default:
                result = null;
                break;
        }

        // solves the content item for all the content item related actions
        switch (id) {
            case 0:
                // $FALLS-THROUGH$
            case 1:
                // $FALLS-THROUGH$
            case 2:
                // $FALLS-THROUGH$
            case 3:
                // $FALLS-THROUGH$
            case 4:
                // $FALLS-THROUGH$
            case 5:
                // $FALLS-THROUGH$
            case 6:
                // $FALLS-THROUGH$
            case 7:
                // $FALLS-THROUGH$
            case 8:
                // $FALLS-THROUGH$
            case 9:
                // $FALLS-THROUGH$
            case 11:
                // $FALLS-THROUGH$
            case 12:
                // $FALLS-THROUGH$
            case 15:
                // $FALLS-THROUGH$
            case 21:
                // $FALLS-THROUGH$
            case 22:
                // $FALLS-THROUGH$
            case 23:
                // $FALLS-THROUGH$
            case 29:
                // $FALLS-THROUGH$
            case 30:
                // $FALLS-THROUGH$
            case 35:
                // $FALLS-THROUGH$
            case 36:
                // $FALLS-THROUGH$
            case 37:
                // $FALLS-THROUGH$
            case 38:
                ((ContentItemActivity) result).setContentItem(contentItem);
                break;
        }

        if (result != null) {
            result.setUser(user);
        }

        if (created != null) {
            result.setCreated(created);
        }

        return result;
    }
}
