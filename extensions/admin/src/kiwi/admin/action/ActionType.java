/*
 * File : ActionType.java.java
 * Date : Apr 8, 2010
 * 
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
package kiwi.admin.action;

/**
 * Defines all the allowed equity action types, this type are used by import.
 * For more information see :
 * http://wiki.kiwi-project.eu/confluence/display/sunspace/Data+import+Specification.
 * 
 * @author mradules
 * @version 0.7
 * @since 0.7
 *
 */
public enum ActionType {

    ATTACHMENT_CREATED(0, "Attachment created"),
    ATTACHMENT_DOWNLOADED(1, "Attachment downloaded"),
    ATTACHMENT_RATED_1(2, "Attachment rated 1"),
    ATTACHMENT_RATED_2(3, "Attachment rated 2"),
    ATTACHMENT_RATED_3(4, "Attachment rated 3"),
    ATTACHMENT_RATED_4(5, "Attachment rated 4"),
    ATTACHMENT_RATED_5(6, "Attachment rated 5"),
    ATTACHMENT_COMMENTED(7, "Attachment commented"),
    ATTACHMENT_REPLACED(8, "Attachment replaced"),
    ATTACHMENT_META_EDITED(9, "Attachment meta edited"),
    ATTACHMENT_TAGGED(10, "Attachment tagged"),
    ATTACHMENT_DELETED(11, "Attachment deleted"),
    ATTACHMENT_META_VIEWED(12, "Attachment meta viewed"),
    WIKI_CREATED(15, "Wiki created"),
    WIKI_COMMENTED(21, "Wiki commented"),
    WIKI_EDITED(22, "Wiki edited"),
    WIKI_META_EDITED(23, "Wiki meta edited"),
    GROUP_CREATED(25, "Group created"),
    ACCESS_DENIED_GROUP(29, "Access denied group"),
    ACCESS_DENIED_INFO(30, "Access denied info"),    
    LOGIN(31, "Login"),
    LOGIN_FAILED_PWD(32, "Login failed pwd"),
    LOGIN_FAILED_USER(33, "Login failed user"),
    WIKI_VIEWED(35, "Wiki viewed"),
    WIKI_DELETED(36, "Wiki deleted"),
    AUTHOR_CHANGED(37, "Author changed"),
    ATTACHMENT_VERSION_CHANGED(38, "Attachment version changed");
    
    private final int id;
    private final String description;

    private ActionType(int id, String description) {
        this.id = id;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static ActionType getActionType(int id) {
        final ActionType[] values = ActionType.values();
        for (ActionType type : values) {
            if (type.id == id) {
                return type;
            }
        }

        return null;
    }
}
