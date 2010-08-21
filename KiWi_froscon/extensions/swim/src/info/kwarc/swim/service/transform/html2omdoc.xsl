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

<!--
    This stylesheet transforms any HTML tables that actually represent OMDoc elements
    to OMDoc XML fragments embedded into the page.
    
    It is intended for inclusion into transform-html-wif.xsl.
-->
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:omdoc="http://omdoc.org/ns"
                xmlns:om="http://www.openmath.org/OpenMath"
                xmlns:cd="http://www.openmath.org/OpenMathCD"
                xmlns:ocds="http://www.openmath.org/OpenMathCDS"
                xmlns:ocdg="http://www.openmath.org/OpenMathCDG"
                xmlns:mcd="http://www.w3.org/ns/mathml-cd"
                xmlns:m="http://www.w3.org/1998/Math/MathML"
                xmlns:xi="http://www.w3.org/2001/XInclude"
				xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:swim="http://kwarc.info/projects/swim"
                xmlns:parsers="java:info.kwarc.swim.service.transform.Parsers"
                version="2.0">
    <import href="identity.xsl"/>
    
    

    <output method="xml" encoding="UTF-8" indent="no" omit-xml-declaration="no"/>
    
 	<!--
 		Returns whether an element of the given name is supported by SWiM's editor
 		This allows for a very basic sanity check.
 		
 		Note that this list also contains elements that should probably not be entered
 		into the editor, as they are mapped to metadata and thus editable via the
 		metadata perspective.
 	-->
 	<function name="omdoc:supported-element-name">
 		<param name="qname"/>
 		<sequence select="some $i in (
 				(: OMDoc 1.2 (subset) :)
 				'alternative',
 		    	'assertion',
 		    	'axiom',
 		    	'definition',
 		    	'example',
 		    	'imports',
 		    	'ref',
 		    	'symbol',
 				'theory',
 				'type',
 				
 				'omgroup',
 		    	'omtext',
 		    	'CMP',
 				'phrase',
 				'term',
 		    	'FMP',
 		    	'assumption',
 		    	'conclusion',
				
 		    	'proof',
 		    	'premise',
 		    	'method',
 		    	'derive',
 		    	'hypothesis',
 		    	'proofobject',
 		    	
 		    	(: OMDoc 1.6 (MMT) :)
 		    	'import',
 		    	'meta',
 		    	'view',
 		    	'substitution',
 		    	'hides',
 		    	'maps',
 		    	
 		    	(: MathML 3 notation definitions (probably incomplete) :)
 		    	'mcd:notation',
	 		    	'mcd:prototype',
		 		    	'mcd:exprlist',
			 		    	'mcd:expr',
 		    		'mcd:rendering',
		 		    	'mcd:iterate',
			 		    	'mcd:separator',
		 		    	'mcd:render',
 		    	
 		    	(: OpenMath 3 :)
 		    	'om:OMOBJ',
	 		    	'om:OMA',
	 		    	'om:OMATTR',
		 		    	'om:OMATP',
	 		    	'om:OMB',
	 		    	'om:OMBIND',
		 		    	'om:OMBVAR',
	 		    	'om:OMC',
	 		    	'om:OME',
	 		    	'om:OMF',
	 		    	'om:OMFOREIGN',
	 		    	'om:OMI',
	 		    	'om:OMR',
	 		    	'om:OMS',
	 		    	'om:OMSTR',
	 		    	'om:OMV',
 		    	
 		    	(: OpenMath 3 Content Dictionaries :)
 		    	'cd:CD',
	 		    	'cd:CDBase',
 			    	'cd:CDComment',
 		    		'cd:CDDate',
 		    		'cd:CDDefinition',
		 		    	'cd:CMP', (: in OpenMath 3: child of property :)
		 		    	'cd:FMP', (: in OpenMath 3: child of property :)
		 		    	'cd:Example',
		 		    	'cd:Name',
		 		    	'cd:Role',
		 		    	(: CDComment, too :)
		 		    	(: Description, too :)
 		    		'cd:CDName',
 		    		'cd:CDReviewDate',
 		    		'cd:CDRevision',
 		    		'cd:CDStatus',
 		    		'cd:CDURL',
 		    		'cd:CDUses',
                    'cd:CDVersion',
	 		    	'cd:Description',

				(: OpenMath 3/MathML 3 Content Dictionaries Transitional
				Clean up later
				http://wiki.kiwi-project.eu/atlassian-jira/browse/SWIM-20 :)
				(: CD :)
	 		    	'cd:discussion',
					(: CDDefinition :)
						'cd:Title', 
		 		    	'cd:Pragmatic',
			    			'cd:Attribute',
			    				(: Name, too :)
				 		    	'cd:Default',
				 		    	'cd:Model',
				 		    	'cd:Prescribed',
			 		    	'cd:Element',
			 		    	(: description, too :)
		 		    	'cd:description',
		 		    	(: discussion, too :)
		 		    	'cd:property',
		 		    	'cd:MMLexample',
 		    	(: fine-grained markup, mostly taken from MathML 3 :)
 		    	'cd:att',
                'cd:attval',
 		    	'cd:code',
                'cd:ednote',
                'cd:edtext',
                'cd:eg',
 		    	'cd:el',
                'cd:emph',
                'cd:graphic',
                'cd:intref',
                'cd:kw',
                'cd:name',
 		    	'cd:p',
 		    	'cd:pseq',
 		    	'cd:quote',
 		    	'cd:specref',
 		    	'cd:sub',
 		    	'cd:sup',
 		    	'cd:var',
 		    	
 		    	(: OpenMath 3 Signature Dictionaries :)
 		    	'ocds:CDSComment',
 		    	'ocds:CDSReviewDate',
 		    	'ocds:CDSStatus',
 		    	'ocds:CDSignatures',
 		    	'ocds:Signature',
 		    	
 		    	(: OpenMath 3 Content Dictionary Groups :)
 		    	'ocdg:CDGroup',
 		    	'ocdg:CDGroupName',
 		    	'ocdg:CDGroupVersion',
 		    	'ocdg:CDGroupRevision',
 		    	'ocdg:CDGroupURL',
 		    	'ocdg:CDGroupDescription',
 		    	'ocdg:CDGroupMember',
 		    	'ocdg:CDName',
 		    	'ocdg:CDVersion',
 		    	'ocdg:CDURL',
 		    	'ocdg:CDComment',
 		    	
				(: MathML 3 Presentation :)
 		    	'm:maction',
 		    	'm:maligngroup',
 		    	'm:malignmark',
 		    	'm:menclose',
 		    	'm:merror',
 		    	'm:mfenced',
 		    	'm:mfrac',
 		    	'm:mglyph',
 		    	'm:mi',
 		    	'm:mlabeledtr',
 		    	'm:mmultiscripts',
 		    	'm:mn',
 		    	'm:mo',
 		    	'm:mover',
 		    	'm:mpadded',
 		    	'm:mphantom',
 		    	'm:mprescripts',
 		    	'm:mroot',
 		    	'm:mrow',
 		    	'm:ms',
 		    	'm:mspace',
 		    	'm:msqrt',
 		    	'm:mstyle',
 		    	'm:msub',
 		    	'm:msubsup',
 		    	'm:msup',
 		    	'm:mtable',
 		    	'm:mtd',
 		    	'm:mtext',
 		    	'm:mtr',
 		    	'm:munder',
 		    	'm:munderover',
 		    	'm:none',
 		    	
 		    	(: MathML 3 Content :)
     			'm:abs',
     			'm:and',
     			'm:annotation',
     			'm:annotation-xml',
     			'm:apply',
     			'm:approx',
     			'm:arccos',
     			'm:arccosh',
     			'm:arccot',
     			'm:arccoth',
     			'm:arccsc',
     			'm:arccsch',
     			'm:arcsec',
     			'm:arcsech',
     			'm:arcsin',
     			'm:arcsinh',
     			'm:arctan',
     			'm:arctanh',
     			'm:arg',
     			'm:bind',
     			'm:bvar',
     			'm:card',
     			'm:cartesianproduct',
     			'm:ceiling',
     			'm:cerror',
     			'm:ci',
     			'm:cn',
     			'm:codomain',
     			'm:complexes',
     			'm:compose',
     			'm:condition',
     			'm:conjugate',
     			'm:cos',
     			'm:cosh',
     			'm:cot',
     			'm:coth',
     			'm:csc',
     			'm:csch',
     			'm:csymbol',
     			'm:curl',
     			'm:declare',
     			'm:defint',
     			'm:degree',
     			'm:determinant',
     			'm:diff',
     			'm:divergence',
     			'm:divide',
     			'm:domain',
     			'm:domainofapplication',
     			'm:emptyset',
     			'm:eq',
     			'm:equivalent',
     			'm:eulergamma',
     			'm:exists',
     			'm:exp',
     			'm:exponentiale',
     			'm:factorial',
     			'm:factorof',
     			'm:false',
     			'm:floor',
     			'm:fn',
     			'm:forall',
     			'm:gcd',
     			'm:geq',
     			'm:grad',
     			'm:gt',
     			'm:ident',
     			'm:image',
     			'm:imaginary',
     			'm:imaginaryi',
     			'm:implies',
     			'm:in',
     			'm:infinity',
     			'm:int',
     			'm:integers',
     			'm:intersect',
     			'm:interval',
     			'm:inverse',
     			'm:lambda',
     			'm:laplacian',
     			'm:lcm',
     			'm:leq',
     			'm:limit',
     			'm:list',
     			'm:ln',
     			'm:log',
     			'm:logbase',
     			'm:lowlimit',
     			'm:lt',
     			'm:math',
     			'm:matrix',
     			'm:matrixrow',
     			'm:max',
     			'm:mean',
     			'm:median',
     			'm:min',
     			'm:minus',
     			'm:mode',
     			'm:moment',
     			'm:momentabout',
     			'm:naturalnumbers',
     			'm:neq',
     			'm:not',
     			'm:notanumber',
     			'm:notin',
     			'm:notprsubset',
     			'm:notsubset',
     			'm:nthdiff',
     			'm:or',
     			'm:otherwise',
     			'm:outerproduct',
     			'm:partialdiff',
     			'm:pi',
     			'm:piece',
     			'm:piecewise',
     			'm:plus',
     			'm:power',
     			'm:primes',
     			'm:product',
     			'm:prsubset',
     			'm:quotient',
     			'm:rationals',
     			'm:real',
     			'm:reals',
     			'm:reln',
     			'm:rem',
     			'm:root',
     			'm:scalarproduct',
     			'm:sdev',
     			'm:sec',
     			'm:sech',
     			'm:selector',
     			'm:semantics',
     			'm:sep',
     			'm:set',
     			'm:setdiff',
     			'm:sin',
     			'm:sinh',
     			'm:subset',
     			'm:sum',
     			'm:tan',
     			'm:tanh',
     			'm:tendsto',
     			'm:times',
     			'm:transpose',
     			'm:true',
     			'm:union',
     			'm:uplimit',
     			'm:variance',
     			'm:vector',
     			'm:vectorproduct',
     			'm:xor',
     			
     			(: XInclude :)
     			'xi:include'
 			) satisfies $i = $qname"/>
 	</function>
 
    <template match="xhtml:p[contains(@class, 'omedit-spacer')][*[1][xhtml:table[omdoc:valid-table(.)]]]">
        <apply-templates/>
    </template>
    
    <template match="xhtml:p[contains(@class, 'omedit-spacer')][not(*)]"/>
 
    <!-- Returns whether the current node (an XHTML table) is a special table representing semantic markup -->
 	<function name="omdoc:valid-table" as="xs:boolean">
 		<param name="node"/>
 		<value-of select="contains($node/@class, 'omedit-annotation') and 
	    	count($node/xhtml:thead/xhtml:tr) eq 1 and
	    	count($node/xhtml:thead/xhtml:tr/xhtml:th) eq 3 and
	    	count($node/xhtml:tbody/xhtml:tr) eq 1 and
	    	(: we no longer require exactly one td child, for the special mcd:notation syntax :)
	    	omdoc:supported-element-name(normalize-space($node/xhtml:thead/xhtml:tr/xhtml:th[1]/text()[1])) "/>
 	</function>
 
   	<!-- This must match with editorInsertOMDoc in components/perspectives/js/edit_content.js -->
    <!-- This transformation is reversed in transform-omdoc-editor.xsl -->    
    <template match="xhtml:table[omdoc:valid-table(.)]">
    	<!-- *[1] takes care of discarding spurious <br/>s -->
    	<variable name="element-name" select="normalize-space(xhtml:thead/xhtml:tr/xhtml:th[1]/text()[1])"/>
    	<element name="{$element-name}">
    		<if test="xhtml:thead/xhtml:tr/xhtml:th[2]/text()">
    			<attribute name="xml:id" select="normalize-space(xhtml:thead/xhtml:tr/xhtml:th[2]/text()[1])"/>
    		</if>
    		<for-each select="xhtml:thead/xhtml:tr/xhtml:th[3]/text()">
    			<!-- TODO improve string matching; this is probably too naïve; SWIM-55 -->
    			<analyze-string select="." regex="([a-z:]+)=(.*)">
    				<matching-substring>
    					<attribute name="{regex-group(1)}" select="regex-group(2)"/>
    				</matching-substring>
    			</analyze-string>
    		</for-each>
    		<!--  Note that this also handles our mcd:notation layout correctly, where we have two 
    		      td's in that row: one for the prototypes, one for the renderings. -->
    		<if test="$element-name ne 'xi:include'">
    		    <!-- Skip our special help text for XIncludes -->
    		    <apply-templates select="xhtml:tbody/xhtml:tr/xhtml:td/node()"/>
    		</if>
    	</element>
    </template>

    <!-- Metadata fields -->
    <template match="xhtml:div[@class='omedit-meta']">
        <variable name="element" select="normalize-space(xhtml:strong[1])"/>
        <choose>
            <when test="$element eq 'XML Comment'">
                <comment select="string(xhtml:strong[1]/following-sibling::xhtml:pre)"/>
            </when>
            <otherwise>
		        <element name="{$element}">
		            <!-- No need to re-add @swim:meta, as RDF extraction and metadata stripping will be
		                 applied to this XML again -->
		            <value-of select="if ($element eq 'cd:CDComment')
		                then string(xhtml:strong[1]/following-sibling::xhtml:pre)
		                else normalize-space(xhtml:strong[1]/following-sibling::node())"/><!-- gets the value of the metadata field -->
		        </element>
		    </otherwise>
		</choose>
    </template>

	<!-- For math objects, undo the transformation of square brackets to external links
	     done by ExtractLinksSavelet -->
    <!-- FIXME this is no longer ikewikitype, but? SWIM-56 -->
	<template match="xhtml:a[@ikewikitype='extlink']" mode="mobj">
		<text>[</text>
		<apply-templates/>
		<text>]</text>
	</template>

	<!-- Transform mathematical objects to OpenMath -->
	<template match="xhtml:span[@class='omedit-mobj']">
		<variable name="mobj">
			<apply-templates mode="mobj"/>
		</variable>
		<!-- FIXME re-enable SWIM-57
		<copy-of select="swimfunc:str2mobj($mobj)"/> -->
	</template>

	<!-- Parses a literal XML string representing an OpenMath object
	     obtained from the Sentido formula editor back into actual OpenMath XML
	     
	     Note: other Sentido workarounds have been deleted.  They can be reactivated from the old SWiM/IkeWiki sources if needed. -->
	<template match="xhtml:span[@lang='x-xml-openmath']">
		<choose>
			<when test="not(*) and starts-with(text(), '&lt;om:OMOBJ')">
				<copy-of select="parsers:parseXML(text())"/>
			</when>
			<!-- More graceful behavior in case of error -->
			<otherwise>
				<message>THIS IS NOT FORMULA BUT A SENTIDO BUG!</message>
				<message select="."/>
				<om:OMOBJ>
					<om:OME>
						<om:OMS cd="swim" name="sentido_bug"/>
						<choose>
							<when test="*">
								<om:OMFOREIGN>
									<copy-of select="node()"/>
								</om:OMFOREIGN>
							</when>
							<otherwise>
								<om:OMSTR>
									<value-of select="text()"/>
								</om:OMSTR>
							</otherwise>
						</choose>
					</om:OME>
				</om:OMOBJ>
			</otherwise>
		</choose>
	</template>
</stylesheet>

<!--
vim:sts=8:sw=8:
-->
