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

package kiwi.service.render.savelet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;

import kiwi.model.kbase.KiWiResource;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * ExtractLinksSavelet: parses the HTML content passed to the savelet for
 * occurrences of wiki syntax links, i.e. [[ ]] and similar links. Occurrences
 * of wiki links are replaces by appropriate HTML anchor elements with kiwi
 * attributes detailing the relation (kiwi:kind with values “imagelink”,
 * “videolink”, “intlink”, “extlink”, kiwi:target containing the title of the
 * item linked to – later dereferenced to the URI identifying the item more
 * precisely). The ExtractLinksSavelet also takes care of identifying bookmarks
 * for fragments (marked up with kiwi:bookmarkstart/kiwi:bookmarkend elements).
 * <p>
 * 
 * Specifically, this method applies the following patterns to the content:
 * <ul>
 * <li> find all [[ ]] wiki-style links in the HTML content and replace them with
 *      &lt;a href="..." kiwi:kind="intlink" ...> elements </li>
 * <li> find all [ ] wiki-style links in the HTML content and replace them with 
 *      &lt;a href="..." kiwi:kind="extlink" ...> elements </li>
 * </ul>
 * 
 * @author Sebastian Schaffert, Marek Schmidt
 * 
 */
@Stateless
@Name("kiwi.service.render.savelet.ExtractLinksSavelet")
@AutoCreate
public class ExtractLinksSavelet implements SourceSavelet {
	
	@Logger
	private Log log;

	private static final String para_elems = "(?:h[1-6]|p|blockquote|pre|hr|ol|ul)";
	
	private static final Pattern p_br_p      = 
		Pattern.compile("(?:<br />)+(</"+para_elems+">)",Pattern.MULTILINE | Pattern.DOTALL);
	
	private static final Pattern p_bookmark_start_2 =
		Pattern.compile("\\[\\[\\s*BookmarkStart:([^\\|\\]]+?)\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]", Pattern.MULTILINE | Pattern.DOTALL);
	
	private static final Pattern p_bookmark_start_1 =
		Pattern.compile("\\[\\[\\s*BookmarkStart:([^\\]]+?)\\s*\\]\\]", Pattern.MULTILINE | Pattern.DOTALL);
	
	/*private static final Pattern p_fragment_end = 
			Pattern.compile("\\[\\[\\s*FragmentEnd\\s*\\]\\]", Pattern.MULTILINE | Pattern.DOTALL);
	*/
	private static final Pattern p_bookmark_end =
		Pattern.compile("\\[\\[\\s*BookmarkEnd:([^\\]]+?)\\s*\\]\\]", Pattern.MULTILINE | Pattern.DOTALL);
	
	private static final Pattern p_imglink_3 = 
		Pattern.compile("\\[\\[\\s*Image:([^\\|\\]]+?)\\s*\\|\\s*([^\\|\\]]+?)\\s*\\|\\s*thumb\\s*\\|\\s*([^\\|\\]]+?)px\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_imglink_1 = 
		Pattern.compile("\\[\\[\\s*Image:([^\\|\\]]+?)\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_imglink_2 = 
		Pattern.compile("\\[\\[\\s*Image:([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	// find video links enclosed in [[ ]] and make <a kiwi:target="..."> out of them
	private static final Pattern p_vidlink_3 = 
		Pattern.compile("\\[\\[\\s*Video:([^\\|\\]]+?)\\s*\\|\\s*([^\\|\\]]+?)\\s*\\|\\s*([^\\|\\]]+?)px\\s*\\|\\s*([^\\|\\]]+?)px\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_vidlink_1 = 
		Pattern.compile("\\[\\[\\s*Video:([^\\|\\]]+?)\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_vidlink_2 = 
		Pattern.compile("\\[\\[\\s*Video:([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);
	
	// find links enclosed in [[ ]] and make <a kiwi:target="..."> out of them
	private static final Pattern p_wikilink_1 = 
		Pattern.compile("\\[\\[\\s*([^\\|\\]]+?)\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_wikilink_2 = 
		Pattern.compile("\\[\\[\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);
	
	// find empty links and remove them
	private static final Pattern p_emptylink  = 
		Pattern.compile("<a[^>]*?>\\s*</a>",Pattern.MULTILINE);

	// find links enclosed in [ ] and make an external link out of them
	private static final Pattern p_wikilink_3 = 
		Pattern.compile("\\[\\s*([^\\|\\]]+?)\\s*\\|\\s*([^\\]]+?)\\s*\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_wikilink_4 = 
		Pattern.compile("\\[\\s*([^\\]]+?)\\s*\\]",Pattern.MULTILINE);

	// find links enclosed in [[ ]] and make <a kiwi:target="..."> out of them
	private static final Pattern p_smwlink_1 = 
		Pattern.compile("\\[\\[\\s*([^\\|\\]]+?)\\s*::\\s*([^\\|\\]]+?)\\s*\\|\\s*([^\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern p_smwlink_2 = 
		Pattern.compile("\\[\\[\\s*([^\\]]+?)\\s*::\\s*([^\\|\\]]+?)\\s*\\]\\]",Pattern.MULTILINE | Pattern.DOTALL);
	
	
	//private static final Pattern p_variable   = Pattern.compile("\\{\\{\\s*([^\\}]+?)\\s*\\}\\}",Pattern.MULTILINE);

	

	/**
	 * - find all [[ ]] wiki-style links in the HTML content and replace them with <a href="..." kiwi:kind="intlink" ...> elements
	 * - find all [ ] wiki-style links in the HTML content and replace them with <a href="..." kiwi:kind="extlink" ...> elements
	 * 
	 * @see kiwi.service.render.savelet.Savelet#apply(java.lang.Object)
	 */
	public String apply(KiWiResource context, String content) {
		String html = content;
		
		
		// This is an almost complete validation of the element name against the XML datatype "Name"
		// See http://www.w3.org/TR/xml/#NT-Name
//		html = html.replaceAll("(<[\\p{L}_:][\\p{L}_:\\p{N}.-]*)\\s+xmlns(?::xhtml)?=\"[^\"]*\"", "$1");
		html = html.replaceAll("<br[^>]*?>", "<br />");
		html = html.replaceAll("<hr[^>]*?>", "<hr />");
		html = html.replaceAll("&nbsp;"," ");
		// entity-encode \[ and \]
		html = html.replaceAll("\\\\\\[","&5b;");		
		html = html.replaceAll("\\\\\\]","&5d;");
		html = html.replaceAll("\\\\\\\\", "\\\\");
		// entity encode \< and \>
		html = html.replaceAll("\\\\\\&lt;","&#60;");
		html = html.replaceAll("\\\\\\&gt;","&#62;");
		
		//html = html.replaceAll("\n"," ");
		

		// find image links enclosed in [[ ]] and make <a kiwi:target="..."> out of them
		// TODO: Semantic MediaWiki compatibility: parse link types here
		
		Matcher m_br_p = p_br_p.matcher(html);
		html = m_br_p.replaceAll("$1");
		
		// Ignore the type in bookmarks... it is there only for rendering purposes...
		Matcher m_bookmark_start_2 = p_bookmark_start_2.matcher(html);
		html = m_bookmark_start_2.replaceAll("<kiwi:bookmarkstart id=\"$1\"></kiwi:bookmarkstart>");
		
		Matcher m_bookmark_start_1 = p_bookmark_start_1.matcher(html);
		html = m_bookmark_start_1.replaceAll("<kiwi:bookmarkstart id=\"$1\"></kiwi:bookmarkstart>");
		
		Matcher m_bookmark_end = p_bookmark_end.matcher(html);
		html = m_bookmark_end.replaceAll("<kiwi:bookmarkend id=\"$1\"></kiwi:bookmarkend>");
			
		Matcher m_imglink_3 = p_imglink_3.matcher(html);
		html = m_imglink_3.replaceAll("<a kiwi:kind=\"imagelink\" kiwi:target=\"$1\" " +
				"                         kind=\"thumb\" align=\"$2\" width=\"$3\" href=\"\">$4</a> ");

		Matcher m_imglink_1 = p_imglink_1.matcher(html);
		html = m_imglink_1.replaceAll("<a kiwi:kind=\"imagelink\" kiwi:target=\"$1\" href=\"\">$2</a> ");

		Matcher m_imglink_2 = p_imglink_2.matcher(html);
		html = m_imglink_2.replaceAll("<a kiwi:kind=\"imagelink\" kiwi:target=\"$1\" href=\"\">$1</a> ");

		// find video links enclosed in [[ ]] and make <a kiwi:target="..."> out of them
		Matcher m_vidlink_3 = p_vidlink_3.matcher(html);
		html = m_vidlink_3.replaceAll("<a kiwi:kind=\"videolink\" kiwi:target=\"$1\" " +
				                         "align=\"$2\" width=\"$3\" height=\"$4\" href=\"\">$5</a> ");

		Matcher m_vidlink_1 = p_vidlink_1.matcher(html);
		html = m_vidlink_1.replaceAll("<a kiwi:kind=\"videolink\" kiwi:target=\"$1\" href=\"\">$2</a> ");

		Matcher m_vidlink_2 = p_vidlink_2.matcher(html);
		html = m_vidlink_2.replaceAll("<a kiwi:kind=\"videolink\" kiwi:target=\"$1\" href=\"\">$1</a> ");

		
		// find Semantic MediaWiki-style links enclosed in [[ type :: target ]] and make <a kiwi:target="..."> out of them
		Matcher m_smwlink_1 = p_smwlink_1.matcher(html);
		html = m_smwlink_1.replaceAll("<a kiwi:kind=\"intlink\" kiwi:target=\"$2\" kiwi:rel=\"$1\" href=\"\">$3</a> ");

		Matcher m_smwlink_2 = p_smwlink_2.matcher(html);
		html = m_smwlink_2.replaceAll("<a kiwi:kind=\"intlink\" kiwi:target=\"$2\" kiwi:rel=\"$1\" href=\"\">$2</a> ");

		
		// find links enclosed in [[ ]] and make <a kiwi:target="..."> out of them
		Matcher m_wikilink_1 = p_wikilink_1.matcher(html);
		html = m_wikilink_1.replaceAll("<a kiwi:kind=\"intlink\" kiwi:target=\"$1\" href=\"\">$2</a> ");

		Matcher m_wikilink_2 = p_wikilink_2.matcher(html);
		html = m_wikilink_2.replaceAll("<a kiwi:kind=\"intlink\" kiwi:target=\"$1\" href=\"\">$1</a> ");
		
		// find empty links and remove them
		Matcher m_emptylink  = p_emptylink.matcher(html);
		html = m_emptylink.replaceAll("");

		// find links enclosed in [ ] and make an external link out of them
		Matcher m_wikilink_3 = p_wikilink_3.matcher(html);
		html = m_wikilink_3.replaceAll("<a kiwi:kind=\"extlink\" kiwi:target=\"$1\" href=\"$1\">$2</a>");

		Matcher m_wikilink_4 = p_wikilink_4.matcher(html);
		html = m_wikilink_4.replaceAll("<a kiwi:kind=\"extlink\" kiwi:target=\"$1\" href=\"$1\">$1</a>");

		
		// TODO how about factoring this out into separate savelets executed after this one? --christoph.lange
//		html = parseEmbeddedCode(html, EmbeddedCodeType.SPARQL_ON_RENDER);
//		html = parseEmbeddedCode(html, EmbeddedCodeType.RUBY_ON_RENDER);
//		html = parseEmbeddedCode(html, EmbeddedCodeType.RUBY_ON_SAVE);

		//Matcher m_variable = p_variable.matcher(html);
		//html = m_variable.replaceAll("<span class=\"macro\">$1</span>");
		
		
		// strangely, the Dojo editor creates nested itemize environments in the wrong form (new ul at the same level as the lis);
		// the following regexps should fix this by placing the ul in the preceding li
//		html = html.replaceAll("</li>\\s*<ul>","<ul>");
//		html = html.replaceAll("</ul>\\s*<li>","</ul></li><li>");
//		
//		html = html.replaceAll("</li>\\s*<ol>","<ol>");
//		html = html.replaceAll("</ol>\\s*<li>","</ol></li><li>");
//		
		// identify double paragraph nestings (might be produced above)
//		Pattern p_double_para_1 = Pattern.compile("<p>(<p>)+");
//		Matcher m_double_para_1 = p_double_para_1.matcher(html);
//		html = m_double_para_1.replaceAll("<p>");

//		Pattern p_double_para_2 = Pattern.compile("</p>(</p>)+");
//		Matcher m_double_para_2 = p_double_para_2.matcher(html);
//		html = m_double_para_2.replaceAll("</p>");

		// entity-decode square brackets
		// FIXME why is this necessary? Check TidySavelet!
		html = html.replaceAll("&5b;","[");		
		html = html.replaceAll("&5d;","]");
		
		if (log != null && log.isDebugEnabled()) {
			log.debug("Sanitized HTML: #0",html);
		}
		
		// TODO Auto-generated method stub
		return html;
	}


	
	
}
