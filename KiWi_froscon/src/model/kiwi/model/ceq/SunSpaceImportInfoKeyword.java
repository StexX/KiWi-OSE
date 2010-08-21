/*
 * File : SunSpaceImportInfoKeyword.java
 * Date : May 10, 2010
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * Copyright 2008 The KiWi Project. All rights
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


package kiwi.model.ceq;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * Used only like temporary storage during the sun space
 * information import, this entity has no functional value. <br>
 * This table is used to relate the Information, Person and the
 * Keyword table.
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
@NamedQuery(name = "select.allSunSpaceTags", query = "SELECT infoKeyword "
        + "FROM SunSpaceImportInfoKeyword AS infoKeyword")
@Entity
public class SunSpaceImportInfoKeyword {

    private Long id;

    private Long informatyionId;

    private Long keywordId;

    private Long personId;

    public SunSpaceImportInfoKeyword() {
        // UNIMPLEMENTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the keywordId
     */
    public Long getKeywordId() {
        return keywordId;
    }

    /**
     * @param keywordId the keywordId to set
     */
    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    /**
     * @return the personId
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * @return the informatyionId
     */
    public Long getInformatyionId() {
        return informatyionId;
    }

    /**
     * @param informatyionId the informatyionId to set
     */
    public void setInformatyionId(Long informatyionId) {
        this.informatyionId = informatyionId;
    }

    @Override
    public String toString() {
        return "SunSpaceImportInfoKeyword [informatyionId=" + informatyionId
                + ", keywordId=" + keywordId + ", personId=" + personId + "]";
    }
}
