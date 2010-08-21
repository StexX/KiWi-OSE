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

package kiwi.action.inspector.style;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

/**
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Name("kiwi.action.inspector.style.styleCreator")
@Scope(ScopeType.CONVERSATION) // event scoped so that it is rerendered when the page is displayed again
public class StyleCreator implements Serializable {
	
	/**
	 * Generated serialized id
	 */
	private static final long serialVersionUID = -1816012247639866494L;

	@Logger
	private Log log;
	
	private List<CIVersion> versions;
	
	@In(required = false,create=true)
	private ContentItem currentContentItem;
	
	private int numberOfColours;
	
	private HashMap<String, Set<CIVersion>> styles;
	
	private boolean perRevision;

	@DataModel(value="kiwi.action.inspector.style.colourLegend",scope=ScopeType.PAGE)
	private List<String> colourLegend;
	
	/**
	 * 
	 */
	public StyleCreator() {
		colourLegend = new ArrayList<String>();
	}
	
	public void init() {
		log.debug("Initializing StyleCreator");
		versions = currentContentItem.getVersions();
		if(versions == null) {
			return;
		}
		if(perRevision) {
			numberOfColours = versions.size();
		} else {
			HashMap<String,Set<CIVersion>> styles = new HashMap<String,Set<CIVersion>>();
			for(CIVersion v : versions) {
				if(v.getTextContentUpdate() != null) {
					User user = v.getRevision().getUser();
					if(!styles.containsKey(user.getLogin())) {
						Set<CIVersion> revSet = new HashSet<CIVersion>();
						revSet.add(v);
						styles.put(user.getLogin(), revSet);
					} else {
						Set<CIVersion> revSet = styles.get(user.getLogin());
						revSet.add(v);
					}
				}
			}
			this.styles = styles;
			numberOfColours = styles.size();
		}
	}
	
	private List<String> calcColours() {
		List<String> colours = new ArrayList<String>();
		for(int i = 0; i < numberOfColours; i++) {
			String colour = new String();
			double h;
			if(i == 0) {
				h = 0 + 140;
			} else {
				h = 360/(i*numberOfColours) + 140;
			}
			if(h >= 360) {
				h = h - 360;
			}
			double s = 1;
			double v = 0.5;
			int[] rgb = HSV2RGB(new double[]{h, s, v});
			String str_r = Integer.toHexString(rgb[0]);
			if(str_r.length() != 2) 
				str_r = "0" + str_r;
			String str_g = Integer.toHexString(rgb[1]);
			if(str_g.length() != 2) 
				str_g = "0" + str_g;
			String str_b = Integer.toHexString(rgb[2]);
			if(str_b.length() != 2) 
				str_b = "0" + str_b;
			
			colour = "#" + str_r + str_g + str_b;
			colours.add(colour);
		}
		return colours;
	}
	
	public void generateStyle() {
		log.debug("Generating Style");
		if(styles != null) {
			colourLegend.clear();
			List<String> colours = calcColours();
			if(perRevision) {
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				for(CIVersion v : versions) {
					String content = "<span class=\"revision_"+v.getId()+"\">Revision " + 
						v.getId() + " (" + df.format(v.getRevision().getCreationDate()) + ")</span>";
					v.setColour(colours.get(0));
					colourLegend.add(content);
					colours.remove(0);
				}
			} else {
				for(String login : styles.keySet()) {
					for(CIVersion r : styles.get(login)) {
						r.setColour(colours.get(0));
					}
					String content = "<span class=\"revision_"+styles.get(login).iterator().next().getId()+"\">User: " + login + "</span>";
					colourLegend.add(content);
					colours.remove(0);
				}
			}
		}
	}

	public int[] HSV2RGB(double[] hsv) {
		double h = hsv[0];
		double s = hsv[1];
		double v = hsv[2];
		
		int[] rgb = new int[]{0, 0, 0};
		double r;
		double g;
		double b;
		
		// black
		if(s == 0) {
			r = v;
			g = v;
			b = v;
		} else {
			double var_h = h/60;
			double var_i = Math.floor(var_h);
			double var_1 = v * (1 - s);
			double var_2 = v * (1 - s * (var_h - var_i));
			double var_3 = v * (1 - s * (1 - (var_h - var_i)));
			
			if(var_i == 0) { r = v; g = var_3; b = var_1; }
			else if(var_i == 1) { r = var_2; g = v; b = var_1; }
			else if(var_i == 2) { r = var_1; g = v; b = var_3; }
			else if(var_i == 3) { r = var_1; g = var_2; b = v; }
			else if(var_i == 4) { r = var_3; g = var_1; b = v; }
			else 			    { r = v; g = var_1; b = var_2; }
		}
		rgb[0] = (int) Math.floor(r * 255);
		rgb[1] = (int) Math.floor(g * 255);
		rgb[2] = (int) Math.floor(b * 255);
		return rgb;
	}
	
	public double[] RGB2HSV(double[] rgb) {
		
		double r = rgb[0]/255;
		double g = rgb[1]/255;
		double b = rgb[2]/255;
		
		double h;
		double s;
		double v;
		
		double[] hsv = new double[] {0, 0, 0};
		double min = Math.min(r, g);
		min = Math.min(min, b);
		double max = Math.max(r, g);
		max = Math.max(max, b);
		
		v = max;
		
		double delta = max - min;
	
		if(max != 0) {
			s = delta / max;
		} else {
			h = s = v = 0;
			hsv[0] = h;
			hsv[1] = s;
			hsv[2] = v;
			return hsv;
		}
		
		if(r == max) {
			h = (g - b)/delta; 		// between yellow & magenta
		} else if(g == max) {
			h = 2 + (b - r)/delta; 	// between cyan & yellow
		} else {
			h = 4 + (r - g)/delta; 	// between magenta & cyan
		}
		h = h * 60;
		if(h < 0) {
			h = h+360;
		}
		
		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;
		return hsv;
	}
}
