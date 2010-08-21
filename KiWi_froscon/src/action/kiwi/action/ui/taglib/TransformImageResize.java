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
package kiwi.action.ui.taglib;

import java.io.IOException;

import javax.faces.component.UIComponentBase;

import org.jboss.seam.ui.graphicImage.Image;
import org.jboss.seam.ui.graphicImage.ImageTransform;

/**
 * 
 * @author Szabolcs Grünwald
 *
 */
public class TransformImageResize extends UIComponentBase implements ImageTransform {
	/**
     * component type
     */
    public static final String COMPONENT_TYPE = "kiwi.action.ui.taglib.TransformImageResize";

    /**
     * component family
     */
    public static final String COMPONENT_FAMILY = "kiwi.action.ui.taglib.TransformImageResize";

    
    private boolean decreaseOnly=false;
    private Integer height, width, targetHeight, targetWidth;
    
	@Override
	public void applyTransform(Image image) throws IOException {
		if(width==null && height==null){
			return;
		}
		if((height!=null && height>=image.getHeight()) || 
				(width!=null && width >= image.getWidth())){
			if(decreaseOnly)
				return;
		}
		
		if(height!=null && width ==null){
			targetWidth= (int)(1.0 * height * image.getRatio());
			targetHeight = height;
		}
		if(width!=null && height ==null){
			targetHeight = (int)(1.0 * width / image.getRatio());
			targetWidth = width;
		}
		if(targetHeight!=null && targetWidth!=null){
			image.setBufferedImage(image.resize(targetWidth, targetHeight).getBufferedImage());
//			System.out.println("image changed from " + width + "x" + height + " to " + targetWidth + "x" + targetHeight);
		}
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public boolean isDecreaseOnly() {
		return decreaseOnly;
	}

	public void setDecreaseOnly(boolean decreaseOnly) {
		this.decreaseOnly = decreaseOnly;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
}
