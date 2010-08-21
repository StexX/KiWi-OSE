<?xml version="1.0" encoding="utf-8"?>
<!-- This file is part of Sentido: http://www.matracas.org/sentido/
     Author: Alberto González Palomo
     © 2001,2002,2003,2004,2005,2006,2007,2008 Alberto González Palomo
     -->
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1998/Math/MathML" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:m="http://www.w3.org/1998/Math/MathML" xmlns:om="http://www.openmath.org/OpenMath" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <!-- xsl:include href="o2ht_om_oms.xsl"/ -->
  
  <xsl:output method="xml"/>

<!-- ============================================================ -->
<!-- ==================        OpenMath        ================== --> 
<!-- ============================================================ -->

  <xsl:template match="om:OMOBJ[parent::*[count(*) = 1 and normalize-space(text()) = '']]">
    <h:div class="OMOBJ">
      <xsl:call-template name="formula">
        <xsl:with-param name="math-mode">display</xsl:with-param>
      </xsl:call-template>
    </h:div>
  </xsl:template>
  
  <xsl:template match="om:OMOBJ">
    <h:span class="OMOBJ">
      <xsl:call-template name="formula">
        <xsl:with-param name="math-mode"/>
      </xsl:call-template>
    </h:span>
  </xsl:template>
  
  <xsl:template name="formula">
    <xsl:param name="math-mode"/>
    <xsl:choose>
      <xsl:when test="count(*) = 0">
        <xsl:attribute name="style">color:grey; border:thin inset grey; background-color:#FFCCCC</xsl:attribute>
        <xsl:text>math</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:element name="m:math">
          <xsl:if test="@xml:id">
            <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
          </xsl:if>
          <xsl:if test="$math-mode">
            <xsl:attribute name="mode"><xsl:value-of select="$math-mode"/></xsl:attribute>
          </xsl:if>
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- xsl:template match="om:OMR[@xlink:href]">
    <xsl:variable name="href" select="@xlink:href"/>
    <m:mfenced separators=":" open="[" close="]">
      <xsl:call-template name="intradocument_href">
        <xsl:with-param name="class">_attr_href</xsl:with-param>
        <xsl:with-param name="href" select="$href"/>
      </xsl:call-template>
      <xsl:text> </xsl:text>
      <m:mrow><xsl:apply-templates select="//om:*[@xml:id=$href]"/></m:mrow>
    </m:mfenced>
  </xsl:template -->
  
  <!-- xsl:template match="om:OMR[@href]">
    <xsl:variable name="href" select="translate(@href, '#', '')"/>
    <xsl:choose>
      <xsl:when test="ancestor-or-self::*[@xml:id=$href]">
	<m:merror>Loop at <xsl:value-of select="@href"/></m:merror>
      </xsl:when>
      <xsl:otherwise>
        <m:mstyle color="blue">
	  <xsl:apply-templates select="//om:*[@xml:id=$href]"/>
	</m:mstyle>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template -->
  
  <xsl:template name="infix_arguments">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:for-each select="*">
      <xsl:choose>
        <xsl:when test="position()=1"/>
        <xsl:otherwise>
          <xsl:apply-templates select=".">
            <xsl:with-param name="parent_precedence" select="$precedence"/>
          </xsl:apply-templates>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="not(position()=1 or (position()=last()))">
        <xsl:apply-templates select="../*[1]">
          <xsl:with-param name="node-before" select="."/>
          <xsl:with-param name="node-after" select="./following-sibling::*[1]"/>
        </xsl:apply-templates>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="prefix_arguments">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:for-each select="*[position() &gt; 1]">
      <m:mrow>
	<xsl:apply-templates select=".">
	  <xsl:with-param name="parent_precedence" select="$precedence"/>
	</xsl:apply-templates>
      </m:mrow>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="infix_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:element name="m:mfenced">
      <xsl:attribute name="separators"/>
      <xsl:if test="@xml:id">
        <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:attribute name="fixity">infix</xsl:attribute>
      <xsl:if test="not($parent_precedence &gt; $precedence)">
        <xsl:attribute name="open"/>
        <xsl:attribute name="close"/>
      </xsl:if>
      <xsl:call-template name="infix_arguments">
        <xsl:with-param name="precedence" select="$precedence"/>
        <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
      </xsl:call-template>
    </xsl:element>
  </xsl:template>

  <xsl:template name="infix_notation_breakable">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:if test="$parent_precedence &gt; $precedence"><m:mo>(</m:mo></xsl:if>
    
    <!-- We lose the ID here: xsl:if test="@xml:id">
      <xsl:attribute name="xml:id"><xsl:value-of select="@xml:id"/></xsl:attribute>
    </xsl:if -->
    <xsl:call-template name="infix_arguments">
      <xsl:with-param name="precedence" select="$precedence"/>
      <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
    </xsl:call-template>
    
    <xsl:if test="$parent_precedence &gt; $precedence"><m:mo>)</m:mo></xsl:if>
  </xsl:template>

  <xsl:template name="superindex_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:element name="m:msup">
      <xsl:if test="@xml:id">
        <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <m:mrow>
        <xsl:apply-templates select="*[2]">
          <xsl:with-param name="parent_precedence" select="$precedence"/>
        </xsl:apply-templates>
      </m:mrow>
      <m:mrow>
        <xsl:apply-templates select="*[3]">
          <xsl:with-param name="parent_precedence" select="$precedence"/>
        </xsl:apply-templates>
      </m:mrow>
    </xsl:element>
  </xsl:template>

  <xsl:template name="subindex_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:element name="m:msub">
      <xsl:if test="@xml:id">
        <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <m:mrow>
        <xsl:apply-templates select="*[2]">
          <xsl:with-param name="parent_precedence" select="$precedence"/>
        </xsl:apply-templates>
      </m:mrow>
      <m:mrow>
        <xsl:apply-templates select="*[3]">
          <xsl:with-param name="parent_precedence" select="$precedence"/>
        </xsl:apply-templates>
      </m:mrow>
    </xsl:element>
  </xsl:template>

  <xsl:template name="subindex_application_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:element name="m:msub">
      <xsl:if test="@xml:id">
        <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <m:mrow>
        <xsl:apply-templates select="*[1]">
          <xsl:with-param name="parent_precedence" select="$precedence"/>
        </xsl:apply-templates>
      </m:mrow>
      <m:mrow>
        <xsl:apply-templates select="*[2]">
          <xsl:with-param name="parent_precedence" select="$precedence"/>
        </xsl:apply-templates>
      </m:mrow>
    </xsl:element>
  </xsl:template>

  <xsl:template name="root_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="*[3][self::om:OMI]='2' or *[3][self::om:OMF]/@dec='2'">
	<xsl:element name="m:msqrt">
	  <xsl:attribute name="degree"><xsl:value-of select="string(*[3])"/></xsl:attribute>
	  <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
	  <xsl:apply-templates select="*[2]"/>
	</xsl:element>
      </xsl:when>
      <xsl:otherwise>
	<xsl:element name="m:mroot">
	  <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
	  <m:mrow><xsl:apply-templates select="*[2]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates></m:mrow>
	  <m:mrow><xsl:apply-templates select="*[3]"/></m:mrow>
	</xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="function_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:element name="m:mrow">
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:apply-templates select="*[1]"/>
      <xsl:choose>
        <xsl:when test="count(*) &gt; 2 or om:*[1][self::om:OMV] or om:OMA or $parent_precedence &gt; $precedence">
          <m:mfenced separators=",">
            <xsl:call-template name="prefix_arguments">
              <xsl:with-param name="precedence" select="$precedence"/>
              <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
            </xsl:call-template>
          </m:mfenced>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text> </xsl:text>
          <xsl:call-template name="prefix_arguments">
            <xsl:with-param name="precedence" select="$precedence"/>
            <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="prefix_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
	<xsl:element name="m:mfenced">
	  <xsl:attribute name="separators"/>
	  <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
          <xsl:apply-templates select="*[1]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
          <xsl:apply-templates select="*[2]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
	<xsl:element name="m:mrow">
	  <xsl:attribute name="separators">,</xsl:attribute>
	  <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
          <xsl:apply-templates select="*[1]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
          <xsl:apply-templates select="*[2]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="suffix_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
	<xsl:element name="m:mfenced">
	  <xsl:attribute name="separators"/>
	  <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
          <xsl:apply-templates select="*[2]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
          <xsl:apply-templates select="*[1]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
	<xsl:element name="m:mrow">
          <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
          <xsl:apply-templates select="*[2]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
          <xsl:apply-templates select="*[1]"><xsl:with-param name="parent_precedence" select="$precedence"/></xsl:apply-templates>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="sumatory_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:element name="m:mrow">
      <xsl:copy-of select="@xml:id"/>
      <m:munderover>
        <xsl:apply-templates select="*[1]"/>
        <m:mrow>
          <xsl:apply-templates select="*[3]/*[2]/*"/>
          <m:mo>=</m:mo>
          <xsl:apply-templates select="*[2]/*[2]"/>
        </m:mrow>
        <m:mrow><xsl:apply-templates select="*[2]/*[3]"/></m:mrow>
      </m:munderover>
      <xsl:choose>
        <xsl:when test="$parent_precedence &gt; $precedence">
          <m:mfenced close=")" open="(" separators="">
            <xsl:apply-templates select="*[3]/*[3]"/>
          </m:mfenced>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="*[3]/*[3]"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="integral_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
	<!-- TODO: identifier. -->
        <m:mfenced close=")" open="(" separators="">
	  <xsl:call-template name="integral_notation_content"/>
        </m:mfenced>
      </xsl:when>
      <xsl:otherwise>
	<!-- TODO: identifier. -->
	<m:mrow>
	  <xsl:call-template name="integral_notation_content"/>
	</m:mrow>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="integral_notation_content">
    <xsl:choose>
      <xsl:when test="*[3][self::om:OMBIND]">
	<!-- Definite integral -->
	<m:msubsup>
	  <xsl:apply-templates select="*[1]"/>
	  <mrow><xsl:apply-templates select="*[2]/*[2]"/></mrow>
	  <mrow><xsl:apply-templates select="*[2]/*[3]"/></mrow>
	</m:msubsup>
	<xsl:apply-templates select="*[3]/*[3]"/>
	<m:mspace width="0.1em"/><m:mrow><m:mi>d<!-- Differential d &#x2146; does not look right -->
</m:mi><xsl:apply-templates select="*[3]/*[2]"/></m:mrow>
      </xsl:when>
      <xsl:otherwise>
	<!-- Indefinite integral -->
	<xsl:apply-templates select="*[1]"/>
	<xsl:apply-templates select="*[2]/*[3]"/>
	<m:mspace width="0.1em"/><m:mrow><m:mi>d<!-- Differential d &#x2146; does not look right -->
</m:mi><xsl:apply-templates select="*[2]/*[2]"/></m:mrow>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="differential_notation">
    <xsl:param name="precedence">100</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
	<!-- TODO: identifier. -->
        <m:mfenced close=")" open="(" separators="">
	  <xsl:call-template name="differential_notation_content">
	    <xsl:with-param name="precedence" select="$precedence"/>
	  </xsl:call-template>
        </m:mfenced>
      </xsl:when>
      <xsl:otherwise>
	<!-- TODO: identifier. -->
	<m:mrow>
	  <xsl:call-template name="differential_notation_content">
	    <xsl:with-param name="precedence" select="$precedence"/>
	  </xsl:call-template>
	</m:mrow>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="differential_notation_content">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="*[1][@name='diff']">
	<m:msub>
	  <xsl:apply-templates select="*[1]"/>
	  <mrow><xsl:apply-templates select="*[2]/*[2]"/></mrow>
	</m:msub>
	<xsl:apply-templates select="*[2]/*[3]">
	  <xsl:with-param name="parent_precedence" select="$precedence"/>
	</xsl:apply-templates>
      </xsl:when>
      <xsl:when test="*[1][@name='nthdiff']">
	<m:msup>
	  <xsl:apply-templates select="*[1]"/>
	  <mrow><xsl:apply-templates select="*[2]"/></mrow>
	</m:msup>
	<xsl:apply-templates select="*[3]">
	  <xsl:with-param name="parent_precedence" select="$precedence"/>
	</xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
	<m:msub>
	  <xsl:apply-templates select="*[1]"/>
	  <mrow>
	    <xsl:variable name="variables" select="*[3]/*[2]/om:OMV"/>
	    <xsl:for-each select="*[2]/om:OMI">
	      <xsl:if test="position() != 1"><m:mo>,</m:mo></xsl:if>
	      <xsl:variable name="index" select="number()"/>
	      <xsl:apply-templates select="$variables[$index]"/>
	    </xsl:for-each>
	  </mrow>
	</m:msub>
	<xsl:apply-templates select="*[3]/*[3]">
	  <xsl:with-param name="parent_precedence" select="$precedence"/>
	</xsl:apply-templates>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="limit_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
	<!-- TODO: identifier. -->
        <m:mfenced close=")" open="(" separators="">
	  <xsl:call-template name="limit_notation_content"/>
        </m:mfenced>
      </xsl:when>
      <xsl:otherwise>
	<!-- TODO: identifier. -->
	<m:mrow>
	  <xsl:call-template name="limit_notation_content"/>
	</m:mrow>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="limit_notation_content">
    <m:munder>
      <xsl:apply-templates select="*[1]"/>
      <mrow>
        <xsl:apply-templates select="*[4]/*[2]"/>
        <xsl:choose>
          <xsl:when test="*[3]/@name='above'">
            <m:msup>
              <m:mrow>
                <mo>→</mo><xsl:apply-templates select="*[2]"/>
              </m:mrow>
              <mo>+</mo>
            </m:msup>
          </xsl:when>
          <xsl:when test="*[3]/@name='below'">
            <m:msup>
              <m:mrow>
                <mo>→</mo><xsl:apply-templates select="*[2]"/>
              </m:mrow>
              <mo>-</mo>
            </m:msup>
          </xsl:when>
          <xsl:when test="*[3]/@name='both_sides'">
            <mo>→</mo><xsl:apply-templates select="*[2]"/>
          </xsl:when>
          <xsl:otherwise>
            <mo>→</mo><xsl:apply-templates select="*[2]"/>
          </xsl:otherwise>
        </xsl:choose>
      </mrow>
    </m:munder>
    <xsl:apply-templates select="*[4]/*[3]"/>
  </xsl:template>
  
  <xsl:template name="piecewise_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <m:mfenced close="" open="{{">
      <m:mtable>
        <xsl:for-each select="*[position() &gt; 1]">
          <m:mtr>
            <m:mtd>
              <xsl:apply-templates select="*[2]"/>
            </m:mtd>
            <m:mtd>
              <xsl:choose>
                <xsl:when test="*[1][self::om:OMS]/@name='piece'">
                  <xsl:apply-templates select="*[3]"/>
                </xsl:when>
                <xsl:otherwise>
                  <m:mo><xsl:value-of select="*[1]/@name"/></m:mo>
                </xsl:otherwise>
              </xsl:choose>
            </m:mtd>
          </m:mtr>
        </xsl:for-each>
      </m:mtable>
    </m:mfenced>
  </xsl:template>
  
  <xsl:template name="fenced_notation">
    <xsl:param name="open">(</xsl:param>
    <xsl:param name="close">)</xsl:param>
    <xsl:param name="separators">,</xsl:param>
    <!-- TODO: identifier. -->
    <m:mfenced close="{$close}" open="{$open}" separators="{$separators}">
      <xsl:for-each select="*[position() &gt; 1]">
	<m:mrow><xsl:apply-templates select="."/></m:mrow>
      </xsl:for-each>
    </m:mfenced>
  </xsl:template>
  
  <xsl:template name="fraction_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
        <m:mfenced close=")" open="(" separators="">
	  <xsl:element name="m:mfrac">
	    <xsl:if test="@xml:id">
	      <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	    </xsl:if>
            <xsl:for-each select="*">
              <xsl:choose>
                <xsl:when test="position()=1"/>
                <xsl:otherwise>
                  <m:mrow><xsl:apply-templates select="."/></m:mrow>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:for-each>
          </xsl:element>
        </m:mfenced>
      </xsl:when>
      <xsl:otherwise>
	<xsl:element name="m:mfrac">
	  <xsl:if test="@xml:id">
	    <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	  </xsl:if>
          <xsl:for-each select="*">
            <xsl:choose>
              <xsl:when test="position()=1"/>
              <xsl:otherwise>
                <m:mrow><xsl:apply-templates select="."/></m:mrow>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="rational_notation">
    <xsl:param name="precedence">0</xsl:param>
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$parent_precedence &gt; $precedence">
        <m:mfenced separators="">
          <m:mn>
	    <xsl:element name="m:mfrac">
	      <xsl:if test="@xml:id">
		<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	      </xsl:if>
              <m:mrow><xsl:apply-templates select="*[2]"/></m:mrow>
              <m:mrow><xsl:apply-templates select="*[3]"/></m:mrow>
	    </xsl:element>
          </m:mn>
        </m:mfenced>
      </xsl:when>
      <xsl:otherwise>
        <m:mn>
	  <xsl:element name="m:mfrac">
	    <xsl:if test="@xml:id">
	      <xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
	    </xsl:if>
            <m:mrow><xsl:apply-templates select="*[2]"/></m:mrow>
            <m:mrow><xsl:apply-templates select="*[3]"/></m:mrow>
	  </xsl:element>
        </m:mn>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:call-template name="function_notation">
      <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
    </xsl:call-template>
  </xsl:template>
  
  
  <!-- alg1 has only constants -->
  
  <xsl:template match="om:OMA[*[1]/@cd='arith1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='abs'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">|</xsl:with-param>
          <xsl:with-param name="close">|</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='divide'">
        <xsl:call-template name="fraction_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='minus'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">50</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='plus'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">50</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='power'">
        <xsl:call-template name="superindex_notation">
          <xsl:with-param name="precedence">70</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='product'">
        <xsl:call-template name="sumatory_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='root'">
        <xsl:call-template name="root_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='sum'">
        <xsl:call-template name="sumatory_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='times'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='unary_minus'">
        <xsl:call-template name="prefix_notation">
          <xsl:with-param name="precedence">80</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='calculus1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='int'">
        <xsl:call-template name="integral_notation"/>
      </xsl:when>
      <xsl:when test="$name='defint'">
        <xsl:call-template name="integral_notation"/>
      </xsl:when>
      <xsl:when test="$name='diff'">
        <xsl:call-template name="differential_notation"/>
      </xsl:when>
      <xsl:when test="$name='nthdiff'">
        <xsl:call-template name="differential_notation"/>
      </xsl:when>
      <xsl:when test="$name='partialdiff'">
        <xsl:call-template name="differential_notation"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='complex1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='complex_cartesian'">
        <m:mn><xsl:apply-templates select="*[2]"/><m:mo>+</m:mo><xsl:apply-templates select="*[3]"/><m:mo>ⅈ</m:mo></m:mn>
      </xsl:when>
      <xsl:when test="$name='complex_polar'">
        <m:mn><m:msup><xsl:apply-templates select="*[2]"/><xsl:apply-templates select="*[3]"/></m:msup></m:mn>
      </xsl:when>
      <xsl:when test="$name='conjugate'">
        <m:mover><xsl:apply-templates select="*[2]"/><xsl:apply-templates select="*[1]"/></m:mover>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='fns1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='left_compose'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">80</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='inverse'">
        <m:msup>
          <m:mrow><xsl:apply-templates select="*[2]"/></m:mrow>
          <m:mn>-1</m:mn>
        </m:msup>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='integer1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='factorial'">
        <xsl:call-template name="suffix_notation">
          <xsl:with-param name="precedence">90</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='remainder'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='interval1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='integer_interval'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">[</xsl:with-param>
          <xsl:with-param name="close">]</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='interval'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">(</xsl:with-param>
          <xsl:with-param name="close">)</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='interval_oo'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">(</xsl:with-param>
          <xsl:with-param name="close">)</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='interval_oc'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">(</xsl:with-param>
          <xsl:with-param name="close">]</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='interval_co'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">[</xsl:with-param>
          <xsl:with-param name="close">)</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='interval_cc'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">[</xsl:with-param>
          <xsl:with-param name="close">]</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='linalg1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='determinant'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">|</xsl:with-param>
          <xsl:with-param name="close">|</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='matrix_selector'">
        <m:msub>
          <xsl:apply-templates select="*[4]"/>
          <m:mrow>
            <xsl:apply-templates select="*[2]"/>
            <m:mo>,</m:mo>
            <xsl:apply-templates select="*[3]"/>
          </m:mrow>
        </m:msub>
      </xsl:when>
      <xsl:when test="$name='outerproduct'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='scalarproduct'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='transpose'">
        <m:msup>
          <xsl:apply-templates select="*[2]"/>
          <m:mo>T</m:mo>
        </m:msup>
      </xsl:when>
      <xsl:when test="$name='vector_selector'">
        <m:msub>
          <xsl:apply-templates select="*[3]"/>
          <xsl:apply-templates select="*[2]"/>
        </m:msub>
      </xsl:when>
      <xsl:when test="$name='vectorproduct'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='linalg2']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='matrix' and ../*[1][self::om:OMS][@cd='linalg1' and @name='determinant']">
        <m:mtable>
          <xsl:apply-templates select="*[position() &gt; 1]"/>
        </m:mtable>
      </xsl:when>
      <xsl:when test="$name='matrix'">
        <m:mfenced close="]" open="[">
          <m:mtable>
            <xsl:apply-templates select="*[position() &gt; 1]"/>
          </m:mtable>
        </m:mfenced>
      </xsl:when>
      <xsl:when test="$name='matrixrow'">
        <m:mtr>
          <xsl:for-each select="*[position() &gt; 1]">
            <m:mtd><xsl:apply-templates select="."/></m:mtd>
          </xsl:for-each>
        </m:mtr>
      </xsl:when>
      <xsl:when test="$name='vector'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">(</xsl:with-param>
          <xsl:with-param name="close">)</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='limit1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='limit'">
        <xsl:call-template name="limit_notation"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='list1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='list'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">(</xsl:with-param>
          <xsl:with-param name="close">)</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='map'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">50</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='suchthat'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">50</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='logic1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='and'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">11</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='equivalent'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">5</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='implies'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">5</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='not'">
        <xsl:call-template name="prefix_notation">
          <xsl:with-param name="precedence">12</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='or'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">10</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='xor'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">10</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMA[*[1]/@cd='nums1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='rational'">
        <xsl:call-template name="rational_notation">
          <xsl:with-param name="precedence">65</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='based_integer'">
        <m:mn><m:msub><xsl:apply-templates select="*[3]"/><xsl:apply-templates select="*[2]"/></m:msub></m:mn>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMA[*[1]/@cd='piece1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='piecewise'">
        <xsl:call-template name="piecewise_notation"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- quant1 has only bindings -->
  
  <xsl:template match="om:OMA[*[1]/@cd='relation1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='approx'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='eq'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='geq'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='leq'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='gt'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='lt'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
     <xsl:when test="$name='neq'">
        <xsl:call-template name="infix_notation_breakable">
          <xsl:with-param name="precedence">20</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
       <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- setname1 has only constants -->
  
  <xsl:template match="om:OMA[*[1]/@cd='rounding1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='ceiling'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">⌈</xsl:with-param>
          <xsl:with-param name="close">⌉</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='floor'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">⌊</xsl:with-param>
          <xsl:with-param name="close">⌋</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='s_data1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='mean'">
        <m:mover>
          <xsl:call-template name="fenced_notation">
            <xsl:with-param name="open"/>
            <xsl:with-param name="close"/>
            <xsl:with-param name="separators">,</xsl:with-param>
          </xsl:call-template>
          <xsl:apply-templates select="*[1]"/>
        </m:mover>
      </xsl:when>
      <xsl:when test="$name='moment'">
        <m:mrow>
          <m:msub>
            <xsl:apply-templates select="*[1]"/>
            <xsl:apply-templates select="*[2]"/>
          </m:msub>
          <m:mfenced>
            <xsl:apply-templates select="*[3]"/>
            <m:mfenced>
              <xsl:apply-templates select="*[position() &gt; 3]"/>
            </m:mfenced>
          </m:mfenced>
        </m:mrow>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='s_dist1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='mean'">
        <m:mover>
          <xsl:call-template name="fenced_notation">
            <xsl:with-param name="open"/>
            <xsl:with-param name="close"/>
            <xsl:with-param name="separators">,</xsl:with-param>
          </xsl:call-template>
          <xsl:apply-templates select="*[1]"/>
        </m:mover>
      </xsl:when>
      <xsl:when test="$name='moment'">
        <m:mrow>
          <m:msub>
            <xsl:apply-templates select="*[1]"/>
            <xsl:apply-templates select="*[2]"/>
          </m:msub>
          <m:mfenced>
            <xsl:apply-templates select="*[3]"/>
            <m:mfenced>
              <xsl:apply-templates select="*[position() &gt; 3]"/>
            </m:mfenced>
          </m:mfenced>
        </m:mrow>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1][@cd='set1' or @cd='multiset1']]">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='cartesian_product'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='in'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">44</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='intersect'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">46</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='map'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">50</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='notin'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">44</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='notprsubset'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">45</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='notsubset'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">45</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='prsubset'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">45</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='set'">
        <xsl:call-template name="fenced_notation">
          <xsl:with-param name="open">{</xsl:with-param>
          <xsl:with-param name="close">}</xsl:with-param>
          <xsl:with-param name="separators">,</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='setdiff'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">50</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='subset'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">45</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='suchthat'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">10</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='union'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">45</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1][@cd='owl']]">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='intersectionOf'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">46</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='unionOf'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">45</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$name='Restriction'">
	<mrow>
	  <xsl:apply-templates select="*[3]"/>
	  <xsl:apply-templates select="*[2]"/>
	</mrow>
      </xsl:when>
      <xsl:when test="$name='minCardinality' or $name='cardinality' or $name='maxCardinality'">
	<xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
	</xsl:call-template>
      </xsl:when> 
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMA[*[1]/@cd='transc1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='exp'">
        <m:msup>
          <m:mi>ⅇ</m:mi>
          <m:mrow><xsl:apply-templates select="*[2]"/></m:mrow>
        </m:msup>
      </xsl:when>
      <xsl:when test="$name='log'">
        <m:mrow>
          <m:msub>
            <m:mrow><xsl:apply-templates select="*[1]"/></m:mrow>
            <m:mrow><xsl:apply-templates select="*[2]"/></m:mrow>
          </m:msub>
          <xsl:apply-templates select="*[3]"/>
        </m:mrow>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='veccalc1']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:call-template name="function_notation">
      <xsl:with-param name="precedence">0</xsl:with-param>
      <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
    </xsl:call-template>
  </xsl:template>
  
  
  <!-- Not in the MathML group: -->
  <xsl:template match="om:OMA[*[1]/@cd='simpletypes']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='funtype'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">11</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1]/@cd='sts']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='mapsto'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">11</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="function_notation">
          <xsl:with-param name="precedence">0</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  
  <xsl:template match="om:OMA[*[1]/@cd='org.matracas.cd.chemistry']">
    <xsl:param name="parent_precedence">0</xsl:param>
    <xsl:variable name="name" select="*[1]/@name"/>
    <xsl:choose>
      <xsl:when test="$name='group'">
        <xsl:call-template name="infix_notation">
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="subindex_application_notation">
          <xsl:with-param name="precedence">70</xsl:with-param>
          <xsl:with-param name="parent_precedence" select="$parent_precedence"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  
  
  <xsl:template match="om:OMBIND">
    <xsl:element name="m:mrow">
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:apply-templates select="*[1]"/>
      <xsl:apply-templates select="*[2]"/>
      <m:mo>.</m:mo>
      <m:mo>(</m:mo><xsl:apply-templates select="*[3]"/><m:mo>)</m:mo>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="om:OMBVAR">
    <!-- TODO: identifier. -->
    <xsl:choose>
      <xsl:when test="count(*) &gt; 1">
	<m:mfenced close="]" open="[" separators=" ">
	  <xsl:apply-templates/>
	</m:mfenced>
      </xsl:when>
      <xsl:otherwise>
	<xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMATTR">
    <!-- TODO: identifier. -->
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="om:OMATP">
    <!-- TODO: identifier. -->
    <m:mo>{</m:mo><xsl:apply-templates/><m:mo>}</m:mo>
  </xsl:template>
  
  <xsl:template match="om:OMATP[om:OMS[@cd='simpletypes' and @name='type']]">
    <mfenced class="_math_type" close="〉" open="〈">
      <!-- More than one argument is an error. -->
      <xsl:apply-templates select="*[position()!=1]"/>
    </mfenced>
  </xsl:template>
  
  <xsl:template match="om:OMI">
    <xsl:element name="m:mn">
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="om:OMF">
    <xsl:element name="m:mn">
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:choose>
	<xsl:when test="@dec">
	  <xsl:value-of select="@dec"/>
	</xsl:when>
	<xsl:when test="@hex">
	  <xsl:value-of select="concat('#x', @hex)"/>
	</xsl:when>
	<xsl:otherwise>
	  <xml:text>NaN</xml:text>
	</xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="om:OMV">
    <xsl:element name="m:mi">
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:variable name="digits_blanked" select="translate(@name, '0123456789_', '           ')"/>
      <xsl:choose>
        <xsl:when test="$digits_blanked != @name and normalize-space($digits_blanked) != ''">
          <xsl:variable name="head" select="substring-before($digits_blanked, ' ')"/>
          <xsl:variable name="tail" select="substring(@name, string-length($head) + 1)"/>
          <!-- xsl:message>digits_blanked: '<xsl:value-of select="$digits_blanked"/>'</xsl:message -->
          <!-- xsl:message>Head: <xsl:value-of select="$head"/></xsl:message -->
          <!-- xsl:message>Tail: <xsl:value-of select="$tail"/></xsl:message -->
          <m:msub>
            <m:mi><xsl:value-of select="$head"/></m:mi>
            <xsl:choose>
              <xsl:when test="starts-with($tail, '_')">
                <m:mn><xsl:value-of select="substring($tail, 2)"/></m:mn>
              </xsl:when>
              <xsl:otherwise>
                <m:mn><xsl:value-of select="$tail"/></m:mn>
              </xsl:otherwise>
            </xsl:choose>
          </m:msub>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@name"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="om:OMSTR">
    <xsl:element name="m:mtext">
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="om:OME">
    <!-- m:merror><xsl:apply-templates/></m:merror -->
    <xsl:element name="m:mfenced">
      <xsl:attribute name="open">[</xsl:attribute>
      <xsl:attribute name="close">]</xsl:attribute>
      <xsl:if test="@xml:id">
	<xsl:attribute name="id"><xsl:value-of select="@xml:id"/></xsl:attribute>
      </xsl:if>
      <m:mrow>&gt;<xsl:apply-templates/></m:mrow>
    </xsl:element>
  </xsl:template>
  


  

  <xsl:template match="om:OMS">
    <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
  </xsl:template>

  <xsl:template match="om:OMS[@cd='alg1']">
    <mo><xsl:value-of select="@name"/></mo>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='bigfloat1']">
    <mo><xsl:value-of select="@name"/></mo>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='arith1']">
    <xsl:param name="node-before"/>
    <xsl:param name="node-after"/>
    <xsl:choose>
      <xsl:when test="@name='abs'">
        <mo>abs</mo>
      </xsl:when>
      <xsl:when test="@name='divide'">
        <mo>/</mo>
      </xsl:when>
      <xsl:when test="@name='gcd'">
        <mo>gcd</mo>
      </xsl:when>
      <xsl:when test="@name='lcm'">
        <mo>lcm</mo>
      </xsl:when>
      <xsl:when test="@name='minus'">
        <mo>−</mo>
      </xsl:when>
      <xsl:when test="@name='plus'">
        <mo>+</mo>
      </xsl:when>
      <xsl:when test="@name='power'">
        <mo>^</mo>
      </xsl:when>
      <xsl:when test="@name='product'">
        <mo>∏</mo>
      </xsl:when>
      <xsl:when test="@name='root'">
        <mo>root</mo>
      </xsl:when>
      <xsl:when test="@name='sum'">
        <mo>∑</mo>
      </xsl:when>
      <xsl:when test="@name='times'">
        <xsl:choose>
          <xsl:when test="$node-after[self::om:OMI] or $node-after[self::om:OMF]">
            <mo>·</mo>
          </xsl:when>
          <xsl:otherwise>
            <mo>⁢</mo>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="@name='unary_minus'">
        <mo>−</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMS[@cd='calculus1']">
    <xsl:choose>
      <xsl:when test="@name='int'">
        <mo>∫</mo>
      </xsl:when>
      <xsl:when test="@name='defint'">
        <mo>∫</mo>
      </xsl:when>
      <xsl:when test="@name='diff'">
        <mo>∂</mo>
      </xsl:when>
      <xsl:when test="@name='nthdiff'">
        <mo>∂</mo>
      </xsl:when>
      <xsl:when test="@name='partialdiff'">
        <mo>∂</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='complex1']">
    <xsl:choose>
      <xsl:when test="@name='argument'">
        <mo>arg</mo>
      </xsl:when>
      <xsl:when test="@name='imaginary'">
        <mo>ℑ</mo>
      </xsl:when>
      <xsl:when test="@name='real'">
        <mo>ℜ</mo>
      </xsl:when>
      <xsl:when test="@name='conjugate'">
        <mo>¯</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='fns1']">
    <xsl:choose>
      <xsl:when test="@name='domain'">
        <mo>Dom</mo>
      </xsl:when>
      <xsl:when test="@name='domainofapplication'">
        <mo>appdomain</mo>
      </xsl:when>
      <xsl:when test="@name='identity'">
        <mo>Id</mo>
      </xsl:when>
      <xsl:when test="@name='image'">
        <mo>Im</mo>
      </xsl:when>
      <xsl:when test="@name='inverse'">
        <mo>inv</mo>
      </xsl:when>
      <xsl:when test="@name='lambda'">
        <mo>λ</mo>
      </xsl:when>
      <xsl:when test="@name='left_compose'">
        <mo>○</mo>
      </xsl:when>
      <xsl:when test="@name='left_inverse'">
        <msup><mo>inv</mo><mo>-</mo></msup>
      </xsl:when>
      <xsl:when test="@name='range'">
        <mo>range</mo>
      </xsl:when>
      <xsl:when test="@name='right_inverse'">
        <msup><mo>inv</mo><mo>+</mo></msup>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMS[@cd='integer1']">
    <xsl:choose>
      <xsl:when test="@name='factorial'">
        <mo>!</mo>
      </xsl:when>
      <xsl:when test="@name='quotient'">
        <mo>quot</mo>
      </xsl:when>
      <xsl:when test="@name='remainder'">
        <mo lspace="mediummathspace" rspace="mediummathspace">mod</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- interval1 has only fenced operators -->
  
  <xsl:template match="om:OMS[@cd='linalg1']">
    <xsl:choose>
      <xsl:when test="@name='outerproduct'">
        <mo>⊗</mo>
      </xsl:when>
      <xsl:when test="@name='scalarproduct'">
        <mo>·</mo>
      </xsl:when>
      <xsl:when test="@name='transpose'">
        <mo>T</mo>
      </xsl:when>
      <xsl:when test="@name='vectorproduct'">
        <mo>×</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='linalg2']">
    <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='limit1']">
    <xsl:choose>
      <xsl:when test="@name='above'">
        <mo>above</mo>
      </xsl:when>
      <xsl:when test="@name='below'">
        <mo>below</mo>
      </xsl:when>
      <xsl:when test="@name='both_sides'">
        <mo>both_sides</mo>
      </xsl:when>
      <xsl:when test="@name='limit'">
        <mo>lim</mo>
      </xsl:when>
      <xsl:when test="@name='null'">
        <mo>unspecified</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='list1']">
    <xsl:choose>
      <xsl:when test="@name='map'">
        <mo>→</mo>
      </xsl:when>
      <xsl:when test="@name='suchthat'">
        <mo>|</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='logic1']">
    <xsl:choose>
      <xsl:when test="@name='and'">
        <mo>∧</mo>
      </xsl:when>
      <xsl:when test="@name='equivalent'">
        <mo>⇔</mo>
      </xsl:when>
      <xsl:when test="@name='false'">
        <mo>⊥</mo>
      </xsl:when>
      <xsl:when test="@name='implies'">
        <mo>⇒</mo>
      </xsl:when>
      <xsl:when test="@name='not'">
        <mo>¬</mo>
      </xsl:when>
      <xsl:when test="@name='or'">
        <mo>∨</mo>
      </xsl:when>
      <xsl:when test="@name='xor'">
        <mo>⊻</mo>
      </xsl:when>
      <xsl:when test="@name='true'">
        <mo>⊤</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='mathmltypes']">
    <mo><xsl:value-of select="substring-before(@name, '_type')"/></mo>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='minmax1']">
    <mo><xsl:value-of select="@name"/></mo>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='multiset1']">
    <xsl:choose>
      <xsl:when test="@name='cartesian_product'">
        <mo>×</mo>
      </xsl:when>
      <xsl:when test="@name='emptyset'">
        <mi>∅</mi>
      </xsl:when>
      <xsl:when test="@name='in'">
        <mo>∈</mo>
      </xsl:when>
      <xsl:when test="@name='intersect'">
        <mo>∩</mo>
      </xsl:when>
      <xsl:when test="@name='multiset'">
        <mo>multiset</mo>
      </xsl:when>
      <xsl:when test="@name='notin'">
        <mo>∉</mo>
      </xsl:when>
      <xsl:when test="@name='notprsubset'">
        <mo>⊄</mo>
      </xsl:when>
      <xsl:when test="@name='notsubset'">
        <mo>⊈</mo>
      </xsl:when>
      <xsl:when test="@name='prsubset'">
        <mo>⊊</mo>
      </xsl:when>
      <xsl:when test="@name='set'">
        <mo>set</mo>
      </xsl:when>
      <xsl:when test="@name='setdiff'">
        <mo>\</mo>
      </xsl:when>
      <xsl:when test="@name='size'">
        <mo>card</mo>
      </xsl:when>
      <xsl:when test="@name='subset'">
        <mo>⊂</mo>
      </xsl:when>
      <xsl:when test="@name='suchthat'">
        <mo>|</mo>
      </xsl:when>
      <xsl:when test="@name='union'">
        <mo>∪</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='nums1']">
    <xsl:choose>
      <xsl:when test="@name='e'">
        <mn>ⅇ</mn>
      </xsl:when>
      <xsl:when test="@name='gamma'">
        <mn>γ</mn>
      </xsl:when>
      <xsl:when test="@name='i'">
        <mn>ⅈ</mn>
      </xsl:when>
      <xsl:when test="@name='infinity'">
        <mn>∞</mn>
      </xsl:when>
      <xsl:when test="@name='NaN'">
        <mn>NaN</mn>
      </xsl:when>
      <xsl:when test="@name='pi'">
        <mn>π</mn>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMS[@cd='owl']">
    <xsl:choose>
      <xsl:when test="@name='Thing'">
	<mo>⊤</mo>
      </xsl:when>
      <xsl:when test="@name='Nothing'">
	<mo>⊥</mo>
      </xsl:when>
      <xsl:when test="@name='unionOf'">
	<mo>⊔</mo>
      </xsl:when>
      <xsl:when test="@name='intersectionOf'">
	<mo>⊓</mo>
      </xsl:when>
      <xsl:when test="@name='minCardinality'">
	<mo>≥</mo>
      </xsl:when>
      <xsl:when test="@name='maxCardinality'">
	<mo>≤</mo>
      </xsl:when>
      <xsl:when test="@name='cardinality'">
	<mo>=</mo>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMS[@cd='piece1']">
    <mo><xsl:value-of select="@name"/></mo>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='quant1']">
    <xsl:choose>
      <xsl:when test="@name='forall'">
        <mo>∀</mo>
      </xsl:when>
      <xsl:when test="@name='exists'">
        <mo>∃</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='relation1']">
    <xsl:choose>
      <xsl:when test="@name='approx'">
        <mo>≈</mo>
      </xsl:when>
      <xsl:when test="@name='eq'">
        <mo>=</mo>
      </xsl:when>
      <xsl:when test="@name='geq'">
        <mo>≥</mo>
      </xsl:when>
      <xsl:when test="@name='gt'">
        <mo>&gt;</mo>
      </xsl:when>
      <xsl:when test="@name='leq'">
        <mo>≤</mo>
      </xsl:when>
      <xsl:when test="@name='lt'">
        <mo>&lt;</mo>
      </xsl:when>
      <xsl:when test="@name='neq'">
        <mo>≠</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='setname1']">
    <xsl:choose>
      <xsl:when test="@name='C'">
        <mo>ℂ</mo>
      </xsl:when>
      <xsl:when test="@name='N'">
        <mo>ℕ</mo>
      </xsl:when>
      <xsl:when test="@name='P'">
        <mo>ℙ</mo>
      </xsl:when>
      <xsl:when test="@name='Q'">
        <mo>ℚ</mo>
      </xsl:when>
      <xsl:when test="@name='R'">
        <mo>ℝ</mo>
      </xsl:when>
      <xsl:when test="@name='Z'">
        <mo>ℤ</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='rounding1']">
    <xsl:choose>
      <xsl:when test="@name='round'">
        <mo>round</mo>
      </xsl:when>
      <xsl:when test="@name='trunc'">
        <mo>trunc</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='set1']">
    <xsl:choose>
      <xsl:when test="@name='cartesian_product'">
        <mo>×</mo>
      </xsl:when>
      <xsl:when test="@name='emptyset'">
        <mi>∅</mi>
      </xsl:when>
      <xsl:when test="@name='in'">
        <mo>∈</mo>
      </xsl:when>
      <xsl:when test="@name='intersect'">
        <mo>∩</mo>
      </xsl:when>
      <xsl:when test="@name='map'">
        <mo>→</mo>
      </xsl:when>
      <xsl:when test="@name='notin'">
        <mo>∉</mo>
      </xsl:when>
      <xsl:when test="@name='notprsubset'">
        <mo>⊄</mo>
      </xsl:when>
      <xsl:when test="@name='notsubset'">
        <mo>⊄</mo>
      </xsl:when>
      <xsl:when test="@name='prsubset'">
        <mo>⊊</mo>
      </xsl:when>
      <xsl:when test="@name='setdiff'">
        <mo>\</mo>
      </xsl:when>
      <xsl:when test="@name='size'">
        <mo>card</mo>
      </xsl:when>
      <xsl:when test="@name='set'">
        <mo>set</mo>
      </xsl:when>
      <xsl:when test="@name='subset'">
        <mo>⊂</mo>
      </xsl:when>
      <xsl:when test="@name='suchthat'">
        <mo>|</mo>
      </xsl:when>
      <xsl:when test="@name='union'">
        <mo>∪</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='s_data1']">
    <xsl:choose>
      <xsl:when test="@name='mean'">
        <mo>¯</mo>
      </xsl:when>
      <xsl:when test="@name='median'">
        <mo>median</mo>
      </xsl:when>
      <xsl:when test="@name='mode'">
        <mo>mode</mo>
      </xsl:when>
      <xsl:when test="@name='moment'">
        <mo>µ</mo>
      </xsl:when>
      <xsl:when test="@name='sdev'">
        <mo>σ</mo>
      </xsl:when>
      <xsl:when test="@name='variance'">
        <mo>var</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='s_dist1']">
    <xsl:choose>
      <xsl:when test="@name='mean'">
        <mo>¯</mo>
      </xsl:when>
      <xsl:when test="@name='moment'">
        <mo>µ</mo>
      </xsl:when>
      <xsl:when test="@name='sdev'">
        <mo>σ</mo>
      </xsl:when>
      <xsl:when test="@name='variance'">
        <mo>var</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='transc1']">
    <xsl:choose>
      <xsl:when test="@name='arccos'">
        <mo>arccos</mo>
      </xsl:when>
      <xsl:when test="@name='arccosh'">
        <mo>arccosh</mo>
      </xsl:when>
      <xsl:when test="@name='arccot'">
        <mo>arccot</mo>
      </xsl:when>
      <xsl:when test="@name='arccoth'">
        <mo>arccoth</mo>
      </xsl:when>
      <xsl:when test="@name='arccsc'">
        <mo>arccsc</mo>
      </xsl:when>
      <xsl:when test="@name='arccsch'">
        <mo>arccsch</mo>
      </xsl:when>
      <xsl:when test="@name='arcsec'">
        <mo>arcsec</mo>
      </xsl:when>
      <xsl:when test="@name='arcsech'">
        <mo>arcsech</mo>
      </xsl:when>
      <xsl:when test="@name='arcsin'">
        <mo>arcsin</mo>
      </xsl:when>
      <xsl:when test="@name='arcsinh'">
        <mo>arcsinh</mo>
      </xsl:when>
      <xsl:when test="@name='arctan'">
        <mo>arctan</mo>
      </xsl:when>
      <xsl:when test="@name='arctanh'">
        <mo>arctanh</mo>
      </xsl:when>
      <xsl:when test="@name='cos'">
        <mo>cos</mo>
      </xsl:when>
      <xsl:when test="@name='cosh'">
        <mo>cosh</mo>
      </xsl:when>
      <xsl:when test="@name='cot'">
        <mo>cot</mo>
      </xsl:when>
      <xsl:when test="@name='coth'">
        <mo>coth</mo>
      </xsl:when>
      <xsl:when test="@name='csc'">
        <mo>csc</mo>
      </xsl:when>
      <xsl:when test="@name='csch'">
        <mo>csch</mo>
      </xsl:when>
      <xsl:when test="@name='exp'">
        <mo>exp</mo>
      </xsl:when>
      <xsl:when test="@name='ln'">
        <mo>ln</mo>
      </xsl:when>
      <xsl:when test="@name='log'">
        <mo>log</mo>
      </xsl:when>
      <xsl:when test="@name='sec'">
        <mo>sec</mo>
      </xsl:when>
      <xsl:when test="@name='sech'">
        <mo>sech</mo>
      </xsl:when>
      <xsl:when test="@name='sin'">
        <mo>sin</mo>
      </xsl:when>
      <xsl:when test="@name='sinh'">
        <mo>sinh</mo>
      </xsl:when>
      <xsl:when test="@name='tan'">
        <mo>tan</mo>
      </xsl:when>
      <xsl:when test="@name='tanh'">
        <mo>tanh</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='veccalc1']">
    <xsl:choose>
      <xsl:when test="@name='curl'">
        <mo>curl</mo>
      </xsl:when>
      <xsl:when test="@name='divergence'">
        <mo>div</mo>
      </xsl:when>
      <xsl:when test="@name='grad'">
        <mo>∇</mo>
      </xsl:when>
      <xsl:when test="@name='Laplacian'">
        <mo><msup><mo>∇</mo><mn>2</mn></msup></mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='altenc']">
    <mo><xsl:value-of select="substring-before(@name, '_')"/></mo>
  </xsl:template>
  
  <!-- Not in the MathML group: -->
  <xsl:template match="om:OMS[@cd='simpletypes']">
    <xsl:choose>
      <xsl:when test="@name='funtype'">
        <mo>→</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="om:OMS[@cd='sts']">
    <xsl:choose>
      <xsl:when test="@name='mapsto'">
        <mo>→</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@cd"/>:<xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  
  <xsl:template match="om:OMS[@cd='org.matracas.cd.chemistry']">
    <xsl:choose>
      <xsl:when test="@name='deuterium'">
        <mo>D</mo>
      </xsl:when>
      <xsl:when test="@name='tritium'">
        <mo>T</mo>
      </xsl:when>
      <xsl:when test="@name='group'"/>
      <xsl:when test="@name='generic_element_X'">
        <mi>X</mi>
      </xsl:when>
      <xsl:when test="@name='generic_element_Y'">
        <mi>Y</mi>
      </xsl:when>
      <xsl:when test="@name='generic_element_Z'">
        <mi>Z</mi>
      </xsl:when>
      <xsl:when test="@name='generic_metal_M'">
        <mi>M</mi>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMS[@cd='org.matracas.cd.qmath-placeholders']">
    <xsl:choose>
      <xsl:when test="@name='generic'">
        <mo>□</mo>
      </xsl:when>
      <xsl:when test="@name='generic-main'">
        <mo>■</mo>
      </xsl:when>
      <xsl:otherwise>
        <mo><xsl:value-of select="@name"/></mo>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
