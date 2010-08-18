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
package kiwi.test.unit;

import java.io.StringReader;

import kiwi.service.query.kiwi.parser.KiWiQueryParser;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Sebastian Schaffert
 *
 */
@Test
public class KiWiQueryParserTest  {

	@Test
	public void testKiWiParser() throws Exception{
		StringReader is = new StringReader("SELECT ? ?exif:fNumber ?dc:creator WHERE { tag:rolf rdf:type:\"kiwi:Image\" } FORMAT calendar");
		
		KiWiQueryParser p = new KiWiQueryParser(is);
		
		p.parseQuery();
		
		Assert.assertEquals(p.getFormat(), "calendar");
		Assert.assertEquals(p.getSearchString(),"tag:rolf rdf:type:\"kiwi:Image\"");
		Assert.assertEquals(p.getProjectedVars().size(), 3);

	
	}
	
	@Test
	public void testKiWiParserDefault() throws Exception{
		StringReader is = new StringReader("SELECT ? WHERE { tag:rolf rdf:type:\"kiwi:Image\" } FORMAT calendar");
		
		KiWiQueryParser p = new KiWiQueryParser(is);
		
		p.parseQuery();
		
		Assert.assertEquals(p.getFormat(), "calendar");
		Assert.assertEquals(p.getSearchString(),"tag:rolf rdf:type:\"kiwi:Image\"");
		Assert.assertEquals(p.getProjectedVars().size(), 1);

	
	}
	
	@Test
	public void testKiWiParserNoFormat() throws Exception{
		StringReader is = new StringReader("SELECT ? ?exif:fNumber ?dc:creator WHERE { tag:rolf rdf:type:\"kiwi:Image\" }");
		
		KiWiQueryParser p = new KiWiQueryParser(is);
		
		p.parseQuery();
		
		Assert.assertEquals(p.getFormat(), null);
		Assert.assertEquals(p.getSearchString(),"tag:rolf rdf:type:\"kiwi:Image\"");
		Assert.assertEquals(p.getProjectedVars().size(), 3);

	
	}

}

