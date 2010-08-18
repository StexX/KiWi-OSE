/*
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
 *
 *
 */


package interedu.service.dataimport;


import interedu.api.dataimport.ParseArtikelService;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;


/**
 * @author Rolf Sint
 */
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
@Name("parseArtikelService")
//@Transactional
public class ParseArtikelServiceImpl implements ParseArtikelService {

    @Logger
    private static Log log;

    private List<Kategorie> kategorien;

    private Nodes nodes;

    public void init(File f) throws InterEduParseException {
        final Builder parser = new Builder(false);
        try {
            log.info("Parsing XML File ...");
            final FileReader fr = new FileReader(f);
            log.info(fr.getEncoding());           
            
            final Document doc = parser.build(fr);
            // get all the artikel tags from the document
            nodes = doc.query("//artikel");
        } catch (final Exception exception) {
            final String msg = "The File #0 can not be processed";
            log.warn(msg, exception, f.getName());
            throw new InterEduParseException(exception);
        }
    }

    public int getNodeSize() {
        return nodes == null ? 0 : nodes.size();
    }

    public Artikel getArtikel(int position) {

        final Artikel artikelTmp = new Artikel();
        if (nodes == null) {
            return artikelTmp;
        }

        final Node n = nodes.get(position);

        final Nodes idNode = n.query("@id");
        if (idNode.size() == 1) {
            artikelTmp.setArtikelId(Integer.valueOf(idNode.get(0).getValue()));
        }

        final Nodes titelNode = n.query("@titel");
        if (titelNode.size() == 1) {
            artikelTmp.setArtikelTitel(titelNode.get(0).getValue());
        }

        final Nodes inhaltNode = n.query("@inhalt");
        if (inhaltNode.size() == 1) {
            artikelTmp.setInhalt(inhaltNode.get(0).getValue());
        }

        final Nodes artikelAutorNode = n.query("@autor");
        if (artikelAutorNode.size() == 1) {
            artikelTmp.setArtikelAutor(artikelAutorNode.get(0).getValue());
        }

        final Nodes urlIdNode = n.query("@urlid");
        if (urlIdNode.size() == 1) {
            artikelTmp.setUrlid(Integer.valueOf(urlIdNode.get(0).getValue()));
        }

        final Nodes erscheindatumNode = n.query("@erscheindatum");
        if (erscheindatumNode.size() == 1) {
            artikelTmp.setErscheindatum(erscheindatumNode.get(0).getValue());
        }

        final Nodes verfallsdatumNode = n.query("@verfallsdatum");
        if (verfallsdatumNode.size() == 1) {
            artikelTmp.setVerfallsdatum(verfallsdatumNode.get(0).getValue());
        }

        final Nodes dokumentartidNode = n.query("@dokumentartid");
        if (dokumentartidNode.size() == 1) {
            artikelTmp.setDokumentartid(Integer.valueOf(dokumentartidNode
                    .get(0).getValue()));
        }

        final Nodes bildLinkNode = n.query("@bildlink");
        if (bildLinkNode.size() == 1) {
            artikelTmp.setBildlink(bildLinkNode.get(0).getValue());
        }

        final Nodes schulstufenNode = n.query("@schulstufen");
        if (schulstufenNode.size() == 1) {
            artikelTmp.setSchulstufen(schulstufenNode.get(0).getValue());
        }

        final Nodes schultypNode = n.query("@schultyp");
        if (schultypNode.size() == 1) {
            artikelTmp.setSchultyp(schultypNode.get(0).getValue());
        }

        final Nodes gegenstandNode = n.query("@gegenstand");
        if (gegenstandNode.size() == 1) {
            artikelTmp.setGegenstand(gegenstandNode.get(0).getValue());
        }

        final Nodes quelleNode = n.query("@quelle");
        if (quelleNode.size() == 1) {
            artikelTmp.setQuelle(quelleNode.get(0).getValue());
        }

        return artikelTmp;
    }

    public List<Kategorie> getKategorien(int position) {
        final Node n = nodes.get(position);

        kategorien = new LinkedList<Kategorie>();

        final Nodes lm = n.query("child::artikel.kategorien/kategorie");

        for (int j = 0; j < lm.size(); j++) {

            final Kategorie kategorieTmp = new Kategorie();

            final Nodes parentId = lm.get(j).query("@parentid");
            kategorieTmp.setParentId(Integer
                    .valueOf(parentId.get(0).getValue()));

            final Nodes katId = lm.get(j).query("@id");
            kategorieTmp.setId(Integer.valueOf(katId.get(0).getValue()));

            final Nodes katName = lm.get(j).query("@name");
            kategorieTmp.setName(katName.get(0).getValue());

            kategorien.add(kategorieTmp);
        }

        return kategorien;
    }
}
