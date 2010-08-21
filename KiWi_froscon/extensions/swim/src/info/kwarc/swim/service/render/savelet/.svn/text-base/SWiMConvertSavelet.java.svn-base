/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */

package info.kwarc.swim.service.render.savelet;

import info.kwarc.swim.api.transform.TransformingService;

import java.util.Locale;

import javax.ejb.Stateless;

import kiwi.model.Constants;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.render.savelet.TextContentSavelet;
import nu.xom.Nodes;
import nu.xom.XPathContext;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Transforms the special HTML markup from the SWiM-extended page editor to OMDoc and OpenMath mathematical markup
 * 
 * @author christoph.lange
 * 
 */
@Stateless
@AutoCreate
@Name("info.kwarc.swim.service.render.savelet.SWiMConvertSavelet")
public class SWiMConvertSavelet implements TextContentSavelet {

	@Logger
	private Log log;
	
	@In
	private TransformingService transformer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kiwi.service.render.savelet.Savelet#apply(kiwi.model.kbase.KiWiResource,
	 * java.lang.Object)
	 */
	@Override
	public TextContent apply(KiWiResource context, TextContent content) {
		content.setXmlDocument(transformer.transform(context, content.getXmlDocument(), TransformingService.HTML2OMDOC));
		return content;
	}

}
