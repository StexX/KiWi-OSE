<?xml version="1.0" encoding="UTF-8"?>
<!--
    * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
    * 
    * Copyright (c) 2008-2009, Christoph Lange, Jacobs University Bremen
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
-->
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="om mcd m xi"
                xpath-default-namespace="http://omdoc.org/ns"
                xmlns:omdoc="http://omdoc.org/ns"
                xmlns:mcd="http://www.w3.org/ns/mathml-cd" 
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns:om="http://www.openmath.org/OpenMath"
                xmlns:cd="http://www.openmath.org/OpenMathCD"
                xmlns:ocds="http://www.openmath.org/OpenMathCDS"
                xmlns:ocdg="http://www.openmath.org/OpenMathCDG"
        		xmlns:swim="http://kwarc.info/projects/swim"
                xmlns:m="http://www.w3.org/1998/Math/MathML"
                xmlns:xi="http://www.w3.org/2001/XInclude"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                version="2.0">
    <!-- retain all other KiWi markup (e.g. HTML) -->
    <import href="identity.xsl"/>

	<strip-space elements="om:OMOBJ om:OMA om:OMBIND om:OMBVAR om:OMATTR om:OMATP om:OMI om:OMF om:OMV om:OMS"/>

    <include href="metadata.xsl"/>

    <function name="swim:qname">
        <param name="focus"/>
        <variable name="prefix" select="document('')/*[1]/namespace::*[. eq namespace-uri($focus)][1]/name()"/>
        <if test="$prefix">
            <value-of select="concat($prefix, ':')"/>
        </if>
        <value-of select="local-name($focus)"/>
    </function>

	<xsl:template match="omdoc:*|mcd:*|cd:*|om:*|m:*|xi:*" mode="table-header"
	   xmlns="http://www.w3.org/1999/xhtml">
       	<thead>
       		<tr>
       			<th>
       				<!-- Element name -->
       				<xsl:value-of select="swim:qname(.)"/>
       			</th>
       			<th>
       				<!-- @xml:id
				TODO display @name for MMT-style OMDoc
				SWIM-58 -->
       				<xsl:choose>
       					<xsl:when test="@xml:id">
       						<xsl:value-of select="@xml:id"/>
       					</xsl:when>
       					<xsl:otherwise>
       						<br/>
       					</xsl:otherwise>
       				</xsl:choose>
       			</th>
       			<th>
       				<!-- all attributes except @xml:id -->
       				<xsl:for-each select="@*[name() ne 'xml:id']">
      						<xsl:value-of select="name()"/>
      						<xsl:text>=</xsl:text>
      						<xsl:value-of select="."/>
      						<xsl:if test="position() ne last()"><br/></xsl:if>
       				</xsl:for-each>
					<xsl:if test="not(@*[name() ne 'xml:id'])">
						<br/>
					</xsl:if>
       			</th>
       		</tr>
       	</thead>
	</xsl:template>

    <!-- handle embedded XML fragments (OMDoc/OpenMath/MathML for now) -->
   	<!-- This must match with editorInsertOMDoc in components/perspectives/js/edit_content.js -->
    <!-- This transformation reverses the one in transform-html-omdoc.xsl -->    
    <xsl:template match="omdoc:*|mcd:*|cd:*[not(@swim:meta)]|om:*|m:*|xi:*"
       xmlns="http://www.w3.org/1999/xhtml">
        <table class="omedit-annotation">
        	<xsl:apply-templates select="." mode="table-header"/>
        	<tbody>
        		<tr>
        			<td colspan="3">
        			    <xsl:choose>
                            <xsl:when test="self::xi:include">
                                <xsl:text>This content is editable on a separate wiki page.  Please navigate there.  This will soon become more user-friendly.</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
        			             <xsl:apply-templates/>
        			        </xsl:otherwise>
        			    </xsl:choose>
        			</td>
        		</tr>
        	</tbody>
        </table>
        <!-- <p class="omedit-spacer"/> -->
        <p/>
    </xsl:template>
    
    <!-- CDComment without RDFized metadata gets a special treatment -->
    <xsl:template match="cd:*[@swim:meta]|cd:CDComment[not(@swim:meta)]|comment()"
        xmlns="http://www.w3.org/1999/xhtml">
        <div class="omedit-meta">
            <!-- FIXME @class gets lost in TinyMCE cleanup; SWIM-59 -->
            <strong class="omedit-meta-label">
                <xsl:if test="self::cd:*[@swim:meta]">
                    <xsl:attribute name="title" select="concat(@swim:meta, ' (see “Metadata”)')"/>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="self::cd:*">
                        <xsl:value-of select="swim:qname(.)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="title" select="'Consider using the discussion page instead'"/>
                        <xsl:text>XML Comment</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </strong>
            <xsl:choose>
                <xsl:when test="self::cd:CDComment|self::comment()">
                    <pre>
                        <xsl:choose>
                            <xsl:when test="@swim:meta">
                                <xsl:apply-templates select="@swim:meta"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="."/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </pre> 
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="@swim:meta"/> 
                </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>
    
    <!-- Special rendering: prototype and rendering side-by-side -->
    <xsl:template match="mcd:notation"
        xmlns="http://www.w3.org/1999/xhtml">
        <table class="omedit-annotation">
        	<xsl:apply-templates select="." mode="table-header"/>
        	<tbody>
   				<!-- All prototypes that either are the first child of a notation definition, or follow a rendering -->
   				<xsl:for-each-group select="mcd:prototype|mcd:rendering" group-starting-with="mcd:prototype[not(preceding-sibling::*) or (preceding-sibling::*[1][self::mcd:rendering])]">
   					<tr>
   						<td colspan="2" width="50%"><xsl:apply-templates select="current-group()[self::mcd:prototype]"/></td>
   						<td width="50%"><xsl:apply-templates select="current-group()[self::mcd:rendering]"/></td>
					</tr>    					
   				</xsl:for-each-group>
        	</tbody>
        </table>
    </xsl:template>
    
    <!-- Converts an OpenMath object into a representation suitable for editing.  If the OpenMath object only consists of markup that is supported by the Sentido formula editor, we convert it to an escaped XML string that is put into an XHTML span.  If there is markup that Sentido does not (yet) support, we create an ASCII representation of OpenMath. -->
    <xsl:template match="om:OMOBJ"
        xmlns="http://www.w3.org/1999/xhtml">
    	<xsl:choose>
    		<xsl:when test="descendant::om:OMATTR|
    			descendant-or-self::om:*[@cdbase and (@cdbase ne 'http://www.openmath.org/cd')]">
    			<!-- If there are any elements that Sentido does not yet support,
    				 fall back to my ASCII syntax -->
    			<span class="omedit-mobj">
    				<!-- This is the counterpart to MObjParser.jj -->
    				<xsl:apply-templates mode="mobj"/>
    			</span>
    		</xsl:when>
    		<xsl:otherwise>
    			<span lang="x-xml-openmath">
    				<xsl:apply-templates mode="escape-omobj-sentido" select="."/>
    			</span>
			</xsl:otherwise>
		</xsl:choose>    			
    </xsl:template>

    <template match="om:*" mode="escape-omobj-sentido">
    	<text>&lt;</text>
    	<value-of select="local-name()" />
    	<if test="not(parent::om:*)">
    	   <text> xmlns="http://www.openmath.org/OpenMath"</text>
    	</if>
    	<apply-templates select="@*" mode="escape-omobj-sentido" />
    	<text>&gt;</text>
    	<apply-templates select="*" mode="escape-omobj-sentido" />
    	<text>&lt;/</text>
    	<value-of select="local-name()" />
    	<text>&gt;</text>
    </template>

    <template match="@*" mode="escape-omobj-sentido">
    	<text> </text>
    	<value-of select="name()" />
    	<text>="</text>
    	<value-of select="." />
    	<text>"</text>
    </template>

    <template match="om:OMA" mode="mobj">
    	<text>@(</text>
    	<apply-templates mode="mobj"/>
    	<text>)</text>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMBIND" mode="mobj">
    	<text>B(</text>
    	<apply-templates mode="mobj"/>
    	<text>)</text>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMBVAR" mode="mobj">
    	<text>[</text>
    	<apply-templates mode="mobj"/>
    	<text>]</text>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMATTR" mode="mobj">
    	<text>A(</text>
    	<apply-templates mode="mobj"/>
    	<text>)</text>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMATP" mode="mobj">
    	<text>[</text>
    	<for-each select="om:*">
    		<choose>
	   			<when test="position() mod 2 = 0">
	   				<apply-templates mode="mobj"/>
	   				<text>=</text>
    			</when>
    			<otherwise>
    				<apply-templates mode="mobj"/>
			    	<if test="not(position()=last())"><text>, </text></if>
    			</otherwise>
   			</choose>
    	</for-each>
    	<text>]</text>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMI" mode="mobj">
    	<value-of select="text()"/>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMF[@dec]" mode="mobj">
        <!-- TODO om:OMF[@hex]; SWIM-60 -->
    	<value-of select="@dec"/>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMV" mode="mobj">
    	<text>_</text>
    	<value-of select="@name"/>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <template match="om:OMS" mode="mobj">
    	<if test="@cd">
    		<variable name="cdbase" select="(ancestor-or-self::*/@cdbase)[last()]"/>
    		<if test="$cdbase">
    			<!-- We assume that the cdbase is given as a SWiM wiki namespace prefix.
    				 Conversion to an actual URI will be done on import/export -->
    			<value-of select="@cdbase"/>
    			<text>:</text>
    		</if>
    		<value-of select="@cd"/>
    		<text>#</text>
    	</if>
   		<value-of select="@name"/>
    	<if test="not(position()=last())"><text>, </text></if>
    </template>
    
    <!-- TODO (by priority):
    	 * OMSTR SWIM-61
         * */@id SWIM-62
    	 * OMR SWIM-63
    	 * OMF/@hex SWIM-60
    	 * OMB SWIM-64
    	 * OME SWIM-65
    	 * OMFOREIGN SWIM-66
     -->
</stylesheet>