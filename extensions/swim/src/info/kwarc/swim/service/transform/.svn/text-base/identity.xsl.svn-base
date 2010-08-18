<?xml version="1.0" encoding="UTF-8"?>

<!--
    *  Copyright (C) 2008
    *  Christoph Lange
    *  Jacobs University Bremen
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

<!--
    This stylesheet performs an identity transformation. 
-->
<stylesheet version="2.0" xmlns="http://www.w3.org/1999/XSL/Transform">
    <!-- identity transformation -->
    <!-- OLD CODE -->
    <!-- 
    <template match="@*|node()" name="identity">
	<copy>
	    <copy-of select="@*"/><!- alle Attribute und Tags kopieren ->
	    <apply-templates/>
	</copy>
    </template>
    -->
    
    <!-- The following would be even nicer, as it allows for customizing the transformation
         of attributes by the importing stylesheet, but it doesn't work with Saxon 8.9.
         (Christoph Lange) -->
    <template match="node()|@*|comment()" name="identity" mode="#all">
      <param name="same-mode" select="true()"/>
      <copy>
        <choose>
          <when test="$same-mode">
            <apply-templates select="node()|@*|comment()" mode="#current"/>
          </when>
          <otherwise>
            <apply-templates select="node()|@*|comment()"/>
          </otherwise>
        </choose>
      </copy>
    </template>
</stylesheet>
