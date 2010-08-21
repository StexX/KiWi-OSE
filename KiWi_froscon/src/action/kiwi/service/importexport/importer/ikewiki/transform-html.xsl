<?xml version="1.0" encoding="UTF-8"?>

<!--
    *  Copyright (C) 2005-2006
    *  Sebastian Schaffert and Rupert Westenthaler
    *  Salzburg Research Forschungsgesellschaft m.b.H.
    *
    *  IkeWiki is free software; you can redistribute it and/or
    * 	modify it under the terms of the GNU Lesser General Public
    * 	License as published by the Free Software Foundation; either
    * 	version 2 of the License, or (at your option) any later version.
    *
    * 	This program is distributed in the hope that it will be useful,
    * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
    * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    * 	Lesser General Public License for more details.
    *
    * 	You should have received a copy of the GNU Lesser General Public
    * 	License along with this library; if not, write to the
    * 	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
    * 	Boston, MA 02111-1307, USA.
    * 
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:iw="http://ikewiki.srfg.at/syntax/1.0/ext"
                xmlns:xfm="http://www.w3.org/2002/xforms"
                xmlns:wiklet-cv="http://ikewiki.srfg.at/syntax/1.0/wiklet/cv"
                xmlns:wiklet-pv="http://ikewiki.srfg.at/syntax/1.0/wiklet/pv"
                xpath-default-namespace="http://ikewiki.srfg.at/syntax/1.0/core"
                exclude-result-prefixes="fn iw xfm wiklet-cv wiklet-pv xs sparql" 
                xmlns:sparql="http://www.w3.org/2005/sparql-results#" 
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="2.0">
    <xsl:output method="xhtml" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>

    <!-- normal pages -->
    <xsl:template match="/page">
        <div xmlns:kiwi="http://www.kiwi-project.eu/kiwi/html/" kiwi:type="page">
            <p>
                <xsl:apply-templates select="content"/>
             </p>
        </div>        
    </xsl:template>
    
    <!--  helper function definition -->

    <xsl:function name="iw:index-of-node" as="xs:integer*">
        <xsl:param name="nodes" as="node()*"/> 
        <xsl:param name="nodeToFind" as="node()"/> 
 
        <xsl:sequence select="for $seq in (1 to count($nodes)) return $seq[$nodes[$seq] is $nodeToFind]"/>
    </xsl:function>

    <!--  the main content element - several content elements are used for several different languages -->
    <xsl:template match="content">
        <xsl:apply-templates/>
    </xsl:template>


    <!-- ##################################################################################### -->
    <!-- line level elements -->
    
    <!-- emphasis -->
    <xsl:template match="emph">
        <i><xsl:apply-templates/></i>
    </xsl:template>

    <xsl:template match="strong">
        <b><xsl:apply-templates/></b>
    </xsl:template>

    <xsl:template match="verystrong">
        <b><i><xsl:apply-templates/></i></b>
    </xsl:template>

    <!-- links -->
    
    <!-- create link -->
    <xsl:template match="intlink[@create='true' or @create='yes']">
        <xsl:choose>
            <xsl:when test="count(./node()) > 0">
                <a class="new"  lTarget="{@target}" name="link-{./text()}" href="index.jsp?title={@target}&amp;action=edit_content">
                    <xsl:apply-templates/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <a class="new"  lTarget="{@target}" name="link-{@target}" href="index.jsp?title={@target}&amp;action=edit_content">
                    <xsl:value-of select="@target"/>
                </a>                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Wikipedia link (starts with Wikipedia:) -->
    <xsl:template match="intlink[starts-with(@target,'Wikipedia:')]">
        <xsl:choose>
            <xsl:when test="count(./node()) > 0">
                <a class="external" href="http://en.wikipedia.org/wiki/{substring-after(@target,'Wikipedia:')}" lTarget="{@target}">
                    <xsl:apply-templates/>
                </a>                
            </xsl:when>
            <xsl:otherwise>
                <a class="external" href="http://en.wikipedia.org/wiki/{substring-after(@target,'Wikipedia:')}" lTarget="{@target}">
                    <xsl:value-of select="substring-after(@target,'Wikipedia:')"/>
                </a>                                
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:template>
    
    <!-- image via type -->
    <xsl:template match="intlink[contains(@iw:typeURI,'http://ikewiki.srfg.at/base/ImageLink') or contains(@iw:typeURI,'http://ikewiki.srfg.at/IkeWiki#asImage')]">
        <xsl:choose>
            <xsl:when test="count(./node()) > 0">
                <a  class="image" lTarget="{@target}" name="{./text()}" onmouseover="showTooltip('{@target}',this)"  href="index.jsp?title={@target}">
                    <img src="multimedia/{@target}" alt="{./text()}" />
                </a>                
            </xsl:when>
            <xsl:otherwise>
                <a  class="image" lTarget="{@target}" name="{@target}" onmouseover="showTooltip('{@target}',this)"  href="index.jsp?title={@target}">
                    <img src="multimedia/{@target}" alt="{fn:replace(@target,'_',' ')}" />
                </a>                                
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>
    
    <!-- image directly specified  -->
    <xsl:template match="imglink">
        <xsl:choose>
            <!--  thumbnail image (MediaWiki style) -->
            <xsl:when test="@kind = 'thumb'">
                <div class="float-{@align}">
                    <div class="thumb">
                        <div class="thumbinner" style="width: {@width+2}px">
                            <a  class="thumbimage" lTarget="{@target}" name="{./text()}" onmouseover="showTooltip('{@target}',this)"  href="index.jsp?title={@target}">
                                <img src="thumbs?width={@width}&amp;title={@target}" alt="{./text()}" width="{@width}px" height="{@imgHeight}px"/>
                            </a>
                            <div class="thumbcaption">
                                <div class="magnify" style="float:right">
                                    <a  class="internal" title="Enlarge" href="index.jsp?title={@target}">
                                        <img width="15" height="11" alt="" src="skins/common/images/magnify-clip.png"/>
                                    </a>
                                </div>
                                <xsl:apply-templates />
                            </div>
                        </div>
                    </div>
                </div>
            </xsl:when>
            
            <!--  image with specified alternate text -->
            <xsl:when test="count(./node()) > 0">
                <a  class="image" lTarget="{@target}" name="{./text()}" onmouseover="showTooltip('{@target}',this)"  href="index.jsp?title={@target}">
                    <img src="multimedia/{@target}" alt="{./text()}"  height="{@imgHeight}px"  width="{@imgWidth}px"/>
                </a>
            </xsl:when>
            
            <!-- image without alternate text -->
            <xsl:otherwise>
                <a  class="image" lTarget="{@target}" name="{@target}" onmouseover="showTooltip('{@target}',this)"  href="index.jsp?title={@target}">
                    <img src="multimedia/{@target}" alt="{fn:replace(@target,'_',' ')}"  height="{@imgHeight}px"  width="{@imgWidth}px"/>
                </a>                                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- flash video content -->
    <xsl:template match="videolink">       
        <xsl:variable name="player_id" select="generate-id()" />
                    
        <xsl:choose>
            <!--  video with specified alignment, width and alternate text -->
            <xsl:when test="@width">
                <div class="embedded-video float-{@align}">
                    <p id="{$player_id}"><a href="http://www.macromedia.com/go/getflashplayer">Get Flash</a> to see this player.</p>
                    <script type="text/javascript">
                        var so = new SWFObject('skins/jw_media_player/mediaplayer.swf','<xsl:value-of select="$player_id"/>',"<xsl:value-of select="@width"/>","<xsl:value-of select="@height"/>",'7');
                        so.addParam("allowfullscreen","true");
                        so.addVariable("file","../../multimedia/<xsl:value-of select="normalize-space(@target)" />");
                        so.addVariable("displayheight","<xsl:value-of select="fn:number(@height)-20"/>");
                        so.addVariable("showdownload","true");
                        so.addVariable("title","<xsl:value-of select="normalize-space(@target)" />");
                        so.addVariable("link","multimedia/<xsl:value-of select="normalize-space(@target)" />");
                        so.addVariable("type","flv");
                        so.write("<xsl:value-of select="$player_id"/>");
                    </script>
                </div>
            </xsl:when>
            
            <xsl:otherwise>
                <!-- video without alternate text -->
                <div class="embedded-video">
                    <p id="{$player_id}"><a href="http://www.macromedia.com/go/getflashplayer">Get Flash</a> to see this player.</p>
                    <script type="text/javascript">
                        var so = new SWFObject('skins/jw_media_player/mediaplayer.swf','<xsl:value-of select="$player_id"/>','400','400','7');
                        so.addParam("allowfullscreen","true");
                        so.addVariable("file","../../multimedia/<xsl:value-of select="normalize-space(@target)" />");
                        so.addVariable("displayheight","300");
                        so.addVariable("showdownload","true");
                        so.addVariable("title","<xsl:value-of select="normalize-space(@target)" />");
                        so.addVariable("link","multimedia/<xsl:value-of select="normalize-space(@target)" />");
                        so.addVariable("type","flv");
                        so.write('<xsl:value-of select="$player_id"/>');
                    </script>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- include -->
    <!-- 
    <xsl:template match="intlink[contains(@iw:typeURI,'http://ikewiki.srfg.at/base/IncludeLink')]">
        <xsl:variable name="target" select="@target"/>
        <xsl:variable name="ipage" select="//iw:includes/page[iw:qtitle = $target][1]"/>
        <xsl:variable name="ititle">
            <xsl:choose>
                <xsl:when test="count(./node()) > 0"><xsl:apply-templates/></xsl:when>
                <xsl:otherwise><xsl:value-of select="fn:replace(@target,'_',' ')"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <div class="included-page" id="included-{@target}">


        <h2>
            <xsl:value-of select="$ititle" />
            <div class="includes-actions">
                [<a name="link-{./text()}" lTarget="{@target}" href="index.jsp?title={@target}">Goto</a>,                 
                <a name="link-{./text()}" lTarget="{@target}" href="index.jsp?title={@target}&amp;action=edit_content">Edit</a>,                 
                <a class="add-type"  name="link-ext-{normalize-space(/page/iw:qtitle/text())}::{@target}" href="javascript:showExternaliseForm('{normalize-space(/page/iw:qtitle/text())}','{@target}');" onmouseover="showWMTTWithText('tt-ext-{@target}','Types: {@iw:type}')" >Types</a>]
            </div>
        </h2>
        
        <xsl:apply-templates select="$ipage/content[lang($language)]"/>
        
        <hr/>
        </div>
    </xsl:template>
    -->
   
    
    
    <!--  template link -->
    <xsl:template match="intlink[contains(@iw:typeURI,'http://ikewiki.srfg.at/base/TemplateLink')]">
        <xsl:variable name="target" select="@target"/>
        <xsl:variable name="ipage" select="//iw:includes/page[iw:qtitle = $target][1]"/>
        <xsl:variable name="ititle">
            <xsl:choose>
                <xsl:when test="count(./node()) > 0"><xsl:apply-templates/></xsl:when>
                <xsl:otherwise><xsl:value-of select="fn:replace(@target,'_',' ')"/></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <div class="included-template" id="included-{@target}">

        <xsl:apply-templates select="$ipage/content"/>
        
        </div>
    </xsl:template>



    <!-- internal link -->
    <xsl:template match="intlink">
        <xsl:choose>
            <xsl:when test="count(./node()) > 0 and (count(./text()) > 1 or normalize-space(./text()) != normalize-space(@target))">
                <a name="link-{./text()}" lTarget="{@target}" href="index.jsp?title={@target}">
                    <xsl:apply-templates/>
                </a>                
            </xsl:when>
            <xsl:otherwise>
                <a name="link-{@target}" lTarget="{@target}" href="index.jsp?title={@target}">
                    <xsl:value-of select="fn:replace(@target,'_',' ')"/>
                </a>                                
            </xsl:otherwise>
        </xsl:choose>
        
        <!-- externalise widget 
        <xsl:if test="@iw:type">
            <a class="delete-type"  name="link-remove-{normalize-space(/page/iw:qtitle/text())}::{@target}" href="javascript:showDeleteLinkTypeForm('{normalize-space(/page/iw:qtitle/text())}','{@target}');" onmouseover="showWMTTWithText('tt-ext-{@target}','Types: {@iw:type}')" >
               <img src="skins/common/images/type_delete.png"/>                                            
            </a>
        </xsl:if>
        <a class="add-type"  name="link-ext-{normalize-space(/page/iw:qtitle/text())}::{@target}" href="javascript:showExternaliseForm('{normalize-space(/page/iw:qtitle/text())}','{@target}');" onmouseover="showWMTTWithText('tt-ext-{@target}','Types: {@iw:type}')" >
            <xsl:choose >
                <xsl:when test="not(@iw:type)">
                    <img src="skins/common/images/type_new.png"/>                                            
                </xsl:when>
                <xsl:otherwise>
                    <img src="skins/common/images/type_add.png"/>                                            
                </xsl:otherwise>
            </xsl:choose>
        </a>
        -->
    </xsl:template>

    <!-- external link -->
    <xsl:template match="extlink">
        <xsl:choose>
            <xsl:when test="count(./node()) > 0">
                <a class="external" href="{@href}"><xsl:apply-templates/></a>                
            </xsl:when>
            <xsl:otherwise>
                <a class="external" href="{@href}"><xsl:value-of select="@href"/></a>                                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- newline -->
    <xsl:template match="newline">
        <br />
    </xsl:template>

    <!-- separator -->
    <xsl:template match="separator">
        <hr />
    </xsl:template>
    


    <!-- ##################################################################################### -->
    <!-- paragraph level elements -->


    <!-- headings are rendered using the HTML h2-h6 elemnents -->
    <xsl:template match="heading">
                
        <a name="{count(preceding::heading)}_{translate(fn:escape-html-uri(fn:string-join(./text(),' ')),' ','_')}"/>
        
        <xsl:choose>
            <xsl:when test="@level='1'"><h2><xsl:apply-templates/></h2></xsl:when>
            <xsl:when test="@level='2'"><h3><xsl:apply-templates/></h3></xsl:when>
            <xsl:when test="@level='3'"><h4><xsl:apply-templates/></h4></xsl:when>
            <xsl:when test="@level='4'"><h5><xsl:apply-templates/></h5></xsl:when>
            <xsl:when test="@level='5'"><h6><xsl:apply-templates/></h6></xsl:when>
            <xsl:otherwise><h6><xsl:apply-templates/></h6></xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>

    <!-- paragraphs are rendered in HTML as <p> elements -->
    <xsl:template match="paragraph[@iw:style]">
        <p style="{@iw:style}">
            <xsl:apply-templates/>
        </p>        
    </xsl:template>

    <xsl:template match="paragraph">
        <p>
            <xsl:apply-templates/>
        </p>        
    </xsl:template>
    
    <!-- blockquote -->
    <xsl:template match="quote">
        <blockquote>
            <xsl:apply-templates/>            
        </blockquote>
    </xsl:template>


    <!-- ordered lists are rendered in HTML using ol elements -->
    <xsl:template match="olist">
        <ol>
            <xsl:apply-templates/>
        </ol>        
    </xsl:template>

    <!-- unordered lists are rendered in HTML using ul elements -->
    <xsl:template match="ulist">
        <ul>
            <xsl:apply-templates/>
        </ul>
    </xsl:template>

    <!-- definition lists are rendered in HTML using dl elements -->
    <xsl:template match="dlist">
        <dl>
            <xsl:apply-templates/>
        </dl>
    </xsl:template>
    

    <!-- list items are rendered in HTML using li elements -->
    <xsl:template match="item[local-name(..) = 'olist' or local-name(..) = 'ulist']">
        <li><xsl:apply-templates/></li>
    </xsl:template>
    
    <!-- definition lists, item (has no representation in XHTML) -->
    <xsl:template match="item[local-name(..) = 'dlist']">
        <xsl:apply-templates/>
    </xsl:template>


    <!-- fallback to li elements -->
    <xsl:template match="item">
        <li><xsl:apply-templates/></li>
    </xsl:template>
    

    <!-- definition lists, phrase -->
    <xsl:template match="phrase">
        <dt><xsl:apply-templates/></dt>
    </xsl:template>

    <!-- definition lists, definition -->
    <xsl:template match="definition">
        <dd><xsl:apply-templates/></dd>
    </xsl:template>

    <!-- Code blocks -->
    <xsl:template match="code">
<!--    <pre><xsl:value-of select="."/></pre> -->
        <pre><xsl:apply-templates/></pre>
    </xsl:template>
 
    <!--  macro expansion -->
    <xsl:template match="iw:macro">
        <xsl:value-of select="@value" />
    </xsl:template>


    <xsl:template match="iw:form">
        <table>
            <xsl:apply-templates select="xfm:input|xfm:select|xfm:select1|xfm:textarea"/>
        </table>        
    </xsl:template>
    
    
    <xsl:template match="xfm:input|xfm:select|xfm:select1|xfm:textarea">
        <xsl:variable name="name" select="@ref"/>
        <tr>
            <td valign="top"><xsl:apply-templates select="xfm:label"/>:</td>
            <td>
                <xsl:for-each select="fn:tokenize(//xfm:instance//*[name(.) = $name]/text(),'\n')">
                    <xsl:value-of select="."/>
                    <xsl:if test="position() != last()"><br /></xsl:if>
                </xsl:for-each>
                <!-- <xsl:value-of select="//xfm:instance//*[name(.) = $name]/text()"/> -->
            </td>
        </tr>
    </xsl:template>
    


    <xsl:template match="xfm:label">
        <strong><xsl:apply-templates/></strong>
    </xsl:template>
    
    
    <!-- ##################################################################################### -->    
    <!-- tables, images, floats -->
    
    <xsl:template match="table[caption]">
        <div class="table">
            <table>
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates select="row"/>
            </table>
            <div class="caption"><xsl:apply-templates select="caption"/></div>
        </div>
    </xsl:template>
    
    <xsl:template match="table">
        <table>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </table>
    </xsl:template>
    
    <xsl:template match="row">
        <tr>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </tr>
    </xsl:template>
    
    <xsl:template match="col">
        <td>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </td>
    </xsl:template>

    <xsl:template match="head">
        <th>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </th>
    </xsl:template>
    
     <!-- identity transformation -->
    <xsl:template match="xhtml:*|@*|text()" name="identity">
    <xsl:message select="."/>
    <xsl:copy>
        <xsl:copy-of select="@*"/><!-- alle Attribute und Tags kopieren -->
        <xsl:apply-templates/>
    </xsl:copy>
    </xsl:template>

    <!-- The following would be even nicer, as it allows for customizing the transformation
         of attributes by the importing stylesheet, but it doesn't work with Saxon 8.9.
         (Christoph Lange) -->
    <!--
    <xsl:template match="*|@*|text()" name="identity">
      <xsl:copy>
        <xsl:apply-templates select="*|@*|text()" />
      </xsl:copy>
    </xsl:template>
    -->
    

</xsl:stylesheet>