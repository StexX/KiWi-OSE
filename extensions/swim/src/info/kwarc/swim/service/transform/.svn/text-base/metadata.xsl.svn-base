<?xml version="1.0" encoding="UTF-8"?>

<!--
    *  Copyright (C) 2008
    *  Christoph Lange
    *  Jacobs University Bremen
    *
    *   SWiM is free software; you can redistribute it and/or
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

<!-- This stylesheet handles metadata that are extracted to the RDF store
     by ExtractRDFSavelet and replaced by placeholders in the XML -->

<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:swim="http://kwarc.info/projects/swim"
                xmlns:MetadataProvider="java:info.kwarc.swim.api.triplestore.MetadataProvider"
                xmlns:jt="http://saxon.sf.net/java-type"
                exclude-result-prefixes="#all"
                version="2.0">
    <param name="metadata-provider" required="yes" as="jt:info.kwarc.swim.api.triplestore.MetadataProvider"/>
                
    <template match="@swim:meta" mode="#all">    
        <value-of select="MetadataProvider:getProperty($metadata-provider, base-uri(), .)"/>
    </template>
    
    <function name="swim:get-metadata" as="xs:string">
        <!-- an element that has a @swim:meta attribute -->
        <param name="focus"/>
        <value-of select="MetadataProvider:getProperty($metadata-provider, base-uri($focus), $focus/@swim:meta)"/>
    </function>
    
    <!-- applicable to any element that has (or has not) a @swim:metadata attribute -->
    <template match="*" mode="swim:get-metadata">
        <apply-templates select="@swim:meta"/>
    </template>
</stylesheet>
