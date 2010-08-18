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


package kiwi.model.ceq;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * Used only like temporary storage during the sun space
 * information import, this entity has no functional value. <br>
 * A space is equivalent with a community, this entity makes the
 * link between the space and its tags.<br>
 *
 * @author mradules
 * @version 0.9
 * @since 0.9
 */
@NamedQuery(name = "select.sunSpaceImportSpaceKeyword", query = "SELECT sp_keyword "
        + " FROM SunSpaceImportSpaceKeyword AS sp_keyword "
        + " WHERE sp_keyword.spaceId=:spaceId")
@Entity
public class SunSpaceImportSpaceKeyword {

    private Long id;

    private Long spaceId;

    private Long keywordId;

    public SunSpaceImportSpaceKeyword() {
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
     * @return the spaceId
     */
    public Long getSpaceId() {
        return spaceId;
    }

    /**
     * @param spaceId the spaceId to set
     */
    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
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

    @Override
    public String toString() {
        return "SunSpaceImportSpaceKeyword [id=" + id + ", spaceId=" + spaceId
                + ", keywordId=" + keywordId + "]";
    }
}
