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

/**
 * @author Rolf Sint
 *
 */
public class Artikel {
	
	private int artikelId = -1;
	private String artikelTitel = "";
	private String inhalt = "";
	private String artikelAutor = "";
	private int  urlid = -1;
	private String erscheindatum = "";
	private String verfallsdatum ="";
	private int dokumentartid = -1;
	private String bildlink ="";
	private String schulstufen ="";
	private String schultyp ="";
	private String gegenstand ="";
	private String quelle ="";
	private String dokumentArt = "";
	
	public String getDokumentArt() {
		return dokumentArt;
	}
	public void setDokumentArt(String dokumentArt) {
		this.dokumentArt = dokumentArt;
	}
	public int getArtikelId() {
		return artikelId;
	}
	public void setArtikelId(int artikelId) {
		this.artikelId = artikelId;
	}
	public String getArtikelTitel() {
		return artikelTitel;
	}
	public void setArtikelTitel(String artikelTitel) {
		this.artikelTitel = artikelTitel;
	}
	public String getInhalt() {
		return inhalt;
	}
	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}
	public String getArtikelAutor() {
		return artikelAutor;
	}
	public void setArtikelAutor(String artikelAutor) {
		this.artikelAutor = artikelAutor;
	}
	public int getUrlid() {
		return urlid;
	}
	public void setUrlid(int urlid) {
		this.urlid = urlid;
	}
	public String getErscheindatum() {
		return erscheindatum;
	}
	public void setErscheindatum(String erscheindatum) {
		this.erscheindatum = erscheindatum;
	}
	public String getVerfallsdatum() {
		return verfallsdatum;
	}
	public void setVerfallsdatum(String verfallsdatum) {
		this.verfallsdatum = verfallsdatum;
	}
	public int getDokumentartid() {
		return dokumentartid;
	}
	public void setDokumentartid(int dokumentartid) {
		this.dokumentartid = dokumentartid;
	}
	public String getBildlink() {
		return bildlink;
	}
	public void setBildlink(String bildlink) {
		this.bildlink = bildlink;
	}
	public String getSchulstufen() {
		return schulstufen;
	}
	public void setSchulstufen(String schulstufen) {
		this.schulstufen = schulstufen;
	}
	public String getSchultyp() {
		return schultyp;
	}
	public void setSchultyp(String schultyp) {
		this.schultyp = schultyp;
	}
	public String getGegenstand() {
		return gegenstand;
	}
	public void setGegenstand(String gegenstand) {
		this.gegenstand = gegenstand;
	}
	public String getQuelle() {
		return quelle;
	}
	public void setQuelle(String quelle) {
		this.quelle = quelle;
	}
}
