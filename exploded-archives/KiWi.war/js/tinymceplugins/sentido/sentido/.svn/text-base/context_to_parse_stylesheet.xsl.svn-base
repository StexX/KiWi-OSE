<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:q="http://www.matracas.org/ns/qmath"
  xmlns:om="http://www.openmath.org/OpenMath"
  xmlns:c="http://www.matracas.org/ns/cascada"
  exclude-result-prefixes="q"
  version="1.0">
  
  <xsl:strip-space elements="*"/>
  <xsl:output method="xml" version="1.0" indent="yes"/>
  
  <xsl:variable name="function_application_open">
    <xsl:choose>
      <xsl:when test="/q:document/q:token[@name = 'function_application_open']">
        <xsl:value-of select="/q:document/q:token[@name = 'function_application_open']/@literal"/>
      </xsl:when>
      <xsl:otherwise><xsl:text>(</xsl:text></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="function_application_close">
    <xsl:choose>
      <xsl:when test="/q:document/q:token[@name = 'function_application_close']">
        <xsl:value-of select="/q:document/q:token[@name = 'function_application_close']/@literal"/>
      </xsl:when>
      <xsl:otherwise><xsl:text>)</xsl:text></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:variable name="group_object_set_open">
    <xsl:choose>
      <xsl:when test="/q:document/q:token[@name = 'set_open']">
        <xsl:value-of select="/q:document/q:token[@name = 'set_open']/@literal"/>
      </xsl:when>
      <xsl:otherwise><xsl:text>{</xsl:text></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="group_object_set_close">
    <xsl:choose>
      <xsl:when test="/q:document/q:token[@name = 'set_close']">
        <xsl:value-of select="/q:document/q:token[@name = 'set_close']/@literal"/>
      </xsl:when>
      <xsl:otherwise><xsl:text>}</xsl:text></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:template match="/">
    <xsl:element name="xsl:stylesheet" use-attribute-sets="namespaces">
      <xsl:attribute name="version">1.0</xsl:attribute>
      <!-- This is here just to force the namespaces to be included in the output, which they would normally not be although the prefixes appear in the XPath expressions. It seems that both xsltproc and Transformiix perform a exclude-result-prefixes in all the prefixes by default, even if only some of them are specified.
           http://mail.gnome.org/archives/xslt/2001-June/msg00015.html
           http://mail.gnome.org/archives/xslt/2001-July/msg00027.html
           -->
      <xsl:attribute name="c:namespace-holder">keep-this-namespace</xsl:attribute>
      <xsl:attribute name="om:namespace-holder">keep-this-namespace</xsl:attribute>
      <xsl:element name="xsl:strip-space">
        <xsl:attribute name="elements">*</xsl:attribute>
      </xsl:element>
      <xsl:element name="xsl:output">
	<xsl:attribute name="method">xml</xsl:attribute>
	<xsl:attribute name="indent">yes</xsl:attribute>
      </xsl:element>
      
      <xsl:element name="xsl:include">
        <xsl:attribute name="href">parse_tree_to_openmath.xsl</xsl:attribute>
      </xsl:element>
      
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="q:document">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="q:grammar"/>
  
  <xsl:template match="q:group">
    <xsl:param name="cd-param"/>
    <xsl:param name="name-param"/>
    <xsl:param name="token-param"/>
    <xsl:param name="type-param"/>
    <!-- Transformiix fails when parameters have the same names as variables. -->
    <xsl:apply-templates select="q:group|q:symbol">
      <xsl:with-param name="cd-param"><xsl:choose><xsl:when test="@cd"><xsl:value-of select="@cd"/></xsl:when><xsl:otherwise><xsl:value-of select="$cd-param"/></xsl:otherwise></xsl:choose></xsl:with-param>
      <xsl:with-param name="name-param"><xsl:choose><xsl:when test="@name"><xsl:value-of select="@name"/></xsl:when><xsl:otherwise><xsl:value-of select="$name-param"/></xsl:otherwise></xsl:choose></xsl:with-param>
      <xsl:with-param name="token-param"><xsl:choose><xsl:when test="@token"><xsl:value-of select="@token"/></xsl:when><xsl:otherwise><xsl:value-of select="$token-param"/></xsl:otherwise></xsl:choose></xsl:with-param>
      <xsl:with-param name="type-param"><xsl:choose><xsl:when test="@type"><xsl:value-of select="@type"/></xsl:when><xsl:otherwise><xsl:value-of select="$type-param"/></xsl:otherwise></xsl:choose></xsl:with-param>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="q:unknown-symbol">
    <xsl:param name="cd-param"/>
    <xsl:param name="name-param"/>
    <xsl:param name="type-param"/>
    <xsl:variable name="cd"><xsl:choose><xsl:when test="@cd"><xsl:value-of select="@cd"/></xsl:when><xsl:otherwise><xsl:value-of select="$cd-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    <xsl:variable name="name"><xsl:choose><xsl:when test="@name"><xsl:value-of select="@name"/></xsl:when><xsl:otherwise><xsl:value-of select="$name-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    <xsl:variable name="type"><xsl:choose><xsl:when test="@type"><xsl:value-of select="@type"/></xsl:when><xsl:otherwise><xsl:value-of select="$type-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    
    <xsl:choose>
      <xsl:when test="$type = 'object_variable' or $type = 'object_morphism_variable' or $type = 'object_morphism_morphism_variable'">
	<xsl:element name="xsl:template">
	  <xsl:attribute name="match">c:undefined_symbol</xsl:attribute>
	  <xsl:element name="om:OMV">
            <xsl:attribute name="name">{c:token/@literal}</xsl:attribute>
          </xsl:element>
	</xsl:element>
	<xsl:element name="xsl:template">
	  <xsl:attribute name="match">om:OMV</xsl:attribute>
	  <xsl:element name="xsl:value-of">
            <xsl:attribute name="select">@name</xsl:attribute>
	  </xsl:element>
	</xsl:element>
      </xsl:when>
      <xsl:when test="$type = 'object'">
	<xsl:element name="xsl:template">
          <xsl:attribute name="match">c:undefined_symbol</xsl:attribute>
	  <om:OMS cd="{$cd}" name="{$name}"/>
	</xsl:element>
	<xsl:element name="xsl:template">
	  <xsl:attribute name="match">om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']</xsl:attribute>
	  <xsl:element name="xsl:value-of">
            <xsl:attribute name="select">$token</xsl:attribute>
          </xsl:element>
	</xsl:element>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="q:symbol">
    <xsl:param name="cd-param"/>
    <xsl:param name="name-param"/>
    <xsl:param name="token-param"/>
    <xsl:param name="type-param"/>
    <xsl:variable name="cd"><xsl:choose><xsl:when test="@cd"><xsl:value-of select="@cd"/></xsl:when><xsl:otherwise><xsl:value-of select="$cd-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    <xsl:variable name="name"><xsl:choose><xsl:when test="@name"><xsl:value-of select="@name"/></xsl:when><xsl:otherwise><xsl:value-of select="$name-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    <xsl:variable name="token"><xsl:choose><xsl:when test="@token"><xsl:value-of select="@token"/></xsl:when><xsl:otherwise><xsl:value-of select="$token-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    <xsl:variable name="type"><xsl:choose><xsl:when test="@type"><xsl:value-of select="@type"/></xsl:when><xsl:otherwise><xsl:value-of select="$type-param"/></xsl:otherwise></xsl:choose></xsl:variable>
    
    <xsl:choose>
      <xsl:when test="$type = 'object_variable' or $type = 'object_morphism_variable' or $type = 'object_morphism_morphism_variable'">
        <!-- xsl:call-template name="variable">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
        </xsl:call-template -->
      </xsl:when>
      <xsl:when test="$type = 'object'">
        <xsl:call-template name="symbol">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'object_morphism'">
        <xsl:call-template name="function">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_fact'">
        <xsl:call-template name="postfix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">100</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_exp'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">80</xsl:with-param>
          <xsl:with-param name="associativity">right-to-left</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_prod'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">60</xsl:with-param>
          <xsl:with-param name="associativity">left-to-right_n-ary</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_plus'">
        <xsl:call-template name="infix_prefix">
	  <!-- The unary_cd and unary_name are for this special case,
	       which can be either an infix operator or a prefix (unary) one,
	       perhaps with different translations, as is the case for minus
	       with unary_minus as unary prefix form. In other cases, like
	       plus, the unary case has no symbol, as "(+4)" is just the
	       same as "4". -->
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">30</xsl:with-param>
          <xsl:with-param name="associativity">left-to-right_n-ary</xsl:with-param>
	  <xsl:with-param name="unary_cd" select="@unary_cd"/>
	  <xsl:with-param name="unary_name" select="@unary_name"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_interval'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">15</xsl:with-param>
          <xsl:with-param name="associativity">right-to-left</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_eq'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">10</xsl:with-param>
          <xsl:with-param name="associativity">right-to-left</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_not'">
        <xsl:call-template name="prefix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">9</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_and'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">8</xsl:with-param>
          <xsl:with-param name="associativity">left-to-right</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_or'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">6</xsl:with-param>
          <xsl:with-param name="associativity">left-to-right</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'op_impl'">
        <xsl:call-template name="infix">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
          <xsl:with-param name="precedence">4</xsl:with-param>
          <xsl:with-param name="associativity">right-to-left</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'fenced'">
        <xsl:call-template name="fenced">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type">
            <xsl:choose>
              <xsl:when test="$token='()'">parenthesis</xsl:when>
              <xsl:when test="$token='[]'">brackets</xsl:when>
              <xsl:when test="$token='{}'">braces</xsl:when>
              <xsl:otherwise>parenthesis</xsl:otherwise>
            </xsl:choose>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$type = 'binding'">
        <xsl:call-template name="binding">
          <xsl:with-param name="cd" select="$cd"/>
          <xsl:with-param name="name" select="$name"/>
          <xsl:with-param name="token" select="$token"/>
          <xsl:with-param name="type" select="$type"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <q:symbol cd="{$cd}" name="{$name}" token="{$token}" type="{$type}"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!-- ==================================================== -->
  <xsl:template name="variable">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:token[@literal='<xsl:value-of select="$token"/>']</xsl:attribute>
      <om:OMV name="{$name}"/>
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMV[@name='<xsl:value-of select="$name"/>']</xsl:attribute>
      <xsl:element name="xsl:text">
        <xsl:value-of select="$token"/>
      </xsl:element>
      <xsl:element name="xsl:apply-templates"/>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="symbol">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:token[@literal='<xsl:value-of select="$token"/>']</xsl:attribute>
      <om:OMS cd="{$cd}" name="{$name}"/>
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']</xsl:attribute>
      <!-- xsl:element name="c:token"><xsl:attribute name="literal"><xsl:value-of select="$token"/></xsl:attribute></xsl:element -->
      <!-- This is needed to support tokens that contain the characters "{}". -->
      <xsl:element name="xsl:element"><xsl:attribute name="name"><xsl:text>c:token</xsl:text></xsl:attribute><xsl:element name="xsl:attribute"><xsl:attribute name="name"><xsl:text>literal</xsl:text></xsl:attribute><xsl:value-of select="$token"/></xsl:element></xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="function">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:op_func_app[*[1][local-name() = 'token' and @literal='<xsl:value-of select="$token"/>']]</xsl:attribute>
      <om:OMA>
        <om:OMS cd="{$cd}" name="{$name}"/>
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">*[position() != 1]</xsl:attribute>
        </xsl:element>
      </om:OMA>
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:*[1][local-name() = 'OMS' and @cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">om:*[1]</xsl:attribute>
      </xsl:element>
      <xsl:element name="c:token">
        <xsl:attribute name="literal"><xsl:value-of select="$function_application_open"/></xsl:attribute>
      </xsl:element>
      <xsl:call-template name="function_arguments"/>
      <xsl:element name="c:token">
        <xsl:attribute name="literal"><xsl:value-of select="$function_application_close"/></xsl:attribute>
      </xsl:element>
    </xsl:element>
    
    <!-- Define also the symbol standalone. -->
    <xsl:call-template name="symbol">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="token" select="$token"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="function_arguments">
    <xsl:param name="start">2</xsl:param>
    <xsl:element name="xsl:for-each">
      <xsl:attribute name="select">*[position() &gt;= <xsl:value-of select="$start"/>]</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">.</xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">parent_precedence</xsl:attribute>
          <xsl:text>0</xsl:text>
        </xsl:element>
      </xsl:element>
      <xsl:element name="xsl:if">
        <xsl:attribute name="test">position() != last()</xsl:attribute>
        <xsl:element name="c:token">
          <xsl:attribute name="literal">,</xsl:attribute>
        </xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="binding">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:binding_app[*[1][local-name() = 'token' and @literal='<xsl:value-of select="$token"/>']]</xsl:attribute>
      <om:OMBIND>
        <om:OMS cd="{$cd}" name="{$name}"/>
	<om:OMBVAR>
	  <xsl:element name="xsl:choose">
	    <xsl:element name="xsl:when">
	      <xsl:attribute name="test">count(*[2]/*) != 0</xsl:attribute>
	      <xsl:element name="xsl:apply-templates">
		<xsl:attribute name="select">*[2]/*[position() != 1 and position() != last()]/*[position() mod 2 = 1]</xsl:attribute>
	      </xsl:element>
	    </xsl:element>
	    <xsl:element name="xsl:otherwise">
	      <xsl:element name="xsl:apply-templates">
		<xsl:attribute name="select">*[2]</xsl:attribute>
	      </xsl:element>
	    </xsl:element>
	  </xsl:element>
	</om:OMBVAR>
	<xsl:element name="xsl:apply-templates">
	  <xsl:attribute name="select">*[position() &gt; 3]</xsl:attribute>
	</xsl:element>
      </om:OMBIND>
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMBIND[om:*[1][local-name() = 'OMS' and @cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">om:*[1]</xsl:attribute>
      </xsl:element>
      <xsl:call-template name="binding_argument_variables"/>
      <xsl:element name="c:token">
        <xsl:attribute name="literal">.</xsl:attribute>
      </xsl:element>
      <xsl:call-template name="function_arguments">
	<xsl:with-param name="start">3</xsl:with-param>
      </xsl:call-template>
    </xsl:element>
    
    <!-- Define also the symbol standalone. -->
    <xsl:call-template name="symbol">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="token" select="$token"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="binding_argument_variables">
    <xsl:element name="xsl:if">
      <xsl:attribute name="test">count(*[2][self::om:OMBVAR]/*) &gt; 1</xsl:attribute>
      <!-- Here we have to build the stylesheet so that it creates the final c:token element with xsl:element, to avoid having the @literal attribute value evaluated if it contains things like "{}" or "$" -->
      <xsl:element name="xsl:element">
	<xsl:attribute name="name">c:token</xsl:attribute>
	<xsl:element name="xsl:attribute">
	  <xsl:attribute name="name">literal</xsl:attribute>
	  <xsl:value-of select="$group_object_set_open"/>
	</xsl:element>
      </xsl:element>
    </xsl:element>
    <xsl:element name="xsl:for-each">
      <xsl:attribute name="select">*[2][self::om:OMBVAR]/*</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">.</xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">parent_precedence</xsl:attribute>
          <xsl:text>0</xsl:text>
        </xsl:element>
      </xsl:element>
      <xsl:element name="xsl:if">
        <xsl:attribute name="test">position() != last()</xsl:attribute>
        <xsl:element name="c:token">
          <xsl:attribute name="literal">,</xsl:attribute>
        </xsl:element>
      </xsl:element>
    </xsl:element>
    <xsl:element name="xsl:if">
      <xsl:attribute name="test">count(*[2][self::om:OMBVAR]/*) &gt; 1</xsl:attribute>
      <xsl:element name="xsl:element">
	<xsl:attribute name="name">c:token</xsl:attribute>
	<xsl:element name="xsl:attribute">
	  <xsl:attribute name="name">literal</xsl:attribute>
	  <xsl:value-of select="$group_object_set_close"/>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="infix_prefix">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:param name="precedence"/><xsl:param name="associativity"/>
    <xsl:param name="unary_cd"/><xsl:param name="unary_name"/>
    
    <!-- First the special case for prefix unary:
      (op_plus_app (op_plus_app_unary [-] [5]))
      -->
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:<xsl:value-of select="$type"/>_app[count(*) = 1][*[1][self::c:<xsl:value-of select="$type"/>_app_unary][*[1][self::c:token][@literal = '<xsl:value-of select="$token"/>']]]</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">c:<xsl:value-of select="$type"/>_app_unary</xsl:attribute>
      </xsl:element>
    </xsl:element>
    
    <!-- (op_plus_app_unary [-] [5]) -->
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:<xsl:value-of select="$type"/>_app_unary[*[1][self::c:token][@literal = '<xsl:value-of select="$token"/>']]|c:<xsl:value-of select="$type"/>_app_unary_in_exponent[*[1][self::c:token][@literal = '<xsl:value-of select="$token"/>']]</xsl:attribute>
      <xsl:choose>
        <xsl:when test="$unary_cd and $unary_name">
          <xsl:element name="xsl:call-template">
            <xsl:attribute name="name">prefix</xsl:attribute>
            <xsl:element name="xsl:with-param">
              <xsl:attribute name="name">symbol</xsl:attribute>
              <xsl:value-of select="$token"/>
            </xsl:element>
            <xsl:element name="xsl:with-param">
              <xsl:attribute name="name">precedence</xsl:attribute>
              <xsl:value-of select="$precedence"/>
            </xsl:element>
            <xsl:element name="xsl:with-param">
              <xsl:attribute name="name">cd</xsl:attribute>
              <xsl:value-of select="$unary_cd"/>
            </xsl:element>
            <xsl:element name="xsl:with-param">
              <xsl:attribute name="name">name</xsl:attribute>
              <xsl:value-of select="$unary_name"/>
            </xsl:element>
          </xsl:element>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select">*[2]</xsl:attribute>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
    
    <!-- Now the n-ary case:
      (op_plus_app [4] (op_plus_app_unary [-] [5]) ...)
      (op_plus_app (op_plus_app_unary [+] [4]) (op_plus_app_unary [-] [5]) ...)
      -->
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:<xsl:value-of select="$type"/>_app[c:<xsl:value-of select="$type"/>_app_unary[1][*[1][self::c:token][@literal = '<xsl:value-of select="$token"/>']]]</xsl:attribute>
      <xsl:element name="xsl:call-template">
        <xsl:attribute name="name">infix_prefix_<xsl:value-of select="$associativity"/></xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">symbol</xsl:attribute>
          <xsl:value-of select="$token"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">precedence</xsl:attribute>
          <xsl:value-of select="$precedence"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">begins_with_unary</xsl:attribute>
          <xsl:attribute name="select">boolean(c:*[1][local-name() = '<xsl:value-of select="$type"/>_app_unary'])</xsl:attribute>
        </xsl:element>
        <xsl:if test="$unary_cd and $unary_name">
          <xsl:element name="xsl:with-param">
            <xsl:attribute name="name">unary_cd</xsl:attribute>
            <xsl:value-of select="$unary_cd"/>
          </xsl:element>
          <xsl:element name="xsl:with-param">
            <xsl:attribute name="name">unary_name</xsl:attribute>
            <xsl:value-of select="$unary_name"/>
          </xsl:element>
        </xsl:if>
      </xsl:element>
    </xsl:element>
    
    <!-- Unparse -->
    <xsl:call-template name="infix_prefix_unparse">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="precedence" select="$precedence"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
    
    <!-- Define also the symbol standalone. -->
    <xsl:call-template name="symbol">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="token" select="$token"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="juxtaposition">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:param name="precedence"/><xsl:param name="associativity"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:juxt_app_type_oo_o</xsl:attribute>
      <xsl:element name="xsl:call-template">
        <xsl:attribute name="name">juxtaposition_<xsl:value-of select="$associativity"/></xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">cd</xsl:attribute>
          <xsl:value-of select="$cd"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">name</xsl:attribute>
          <xsl:value-of select="$name"/>
        </xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="infix">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:param name="precedence"/><xsl:param name="associativity"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:<xsl:value-of select="$type"/>_app[*[position() = 2 and local-name() = 'token' and @literal='<xsl:value-of select="$token"/>']]</xsl:attribute>
      <xsl:element name="xsl:call-template">
        <xsl:attribute name="name">infix_<xsl:value-of select="$associativity"/></xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">symbol</xsl:attribute>
          <xsl:value-of select="$token"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">precedence</xsl:attribute>
          <xsl:value-of select="$precedence"/>
        </xsl:element>
      </xsl:element>
    </xsl:element>
    
    <!-- Unparse -->
    <xsl:call-template name="infix_unparse">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="precedence" select="$precedence"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
    
    <!-- Define also the symbol standalone. -->
    <xsl:call-template name="symbol">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="token" select="$token"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
    
    <!-- Special case: infix operators can be implicit by juxtaposition. -->
    <xsl:if test="@juxtaposition='true'">
      <xsl:call-template name="juxtaposition">
        <xsl:with-param name="cd" select="$cd"/>
        <xsl:with-param name="name" select="$name"/>
        <xsl:with-param name="token" select="$token"/>
        <xsl:with-param name="type" select="$type"/>
        <xsl:with-param name="precedence" select="$precedence"/>
        <xsl:with-param name="associativity">left-to-right_n-ary</xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="infix_unparse">
    <xsl:param name="cd"/>
    <xsl:param name="name"/>
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>0<!-- A value of 100 would put parenthesis in unhandled cases, but then it does also around the whole expression. By setting it to 0, errors should be easier to spot (no parenthesis where there should be) and the root OMA is not enclosed. --></xsl:element>
      <xsl:element name="xsl:choose">
	<xsl:element name="xsl:when">
	  <xsl:attribute name="test">$parent_precedence &gt; <xsl:value-of select="$precedence"/></xsl:attribute>
	  <xsl:element name="c:group_object_parenthesis">
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">(</xsl:attribute>
	    </xsl:element>
	    <xsl:call-template name="infix_args_unparse">
	      <xsl:with-param name="precedence" select="$precedence"/>
	      <xsl:with-param name="type" select="$type"/>
	    </xsl:call-template>
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">)</xsl:attribute>
	    </xsl:element>
	  </xsl:element><!-- c:group_object_parenthesis -->
	</xsl:element>
	<xsl:element name="xsl:otherwise">
	  <xsl:call-template name="infix_args_unparse">
	    <xsl:with-param name="precedence" select="$precedence"/>
	    <xsl:with-param name="type" select="$type"/>
	  </xsl:call-template>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="infix_args_unparse">
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="c:{$type}_app">
      <xsl:element name="xsl:for-each">
	<xsl:attribute name="select">*[position() != 1]</xsl:attribute>
	<xsl:element name="xsl:if">
	  <xsl:attribute name="test">position() != 1</xsl:attribute>
	  <xsl:element name="xsl:apply-templates">
	    <xsl:attribute name="select">../*[1]</xsl:attribute>
	  </xsl:element>
	</xsl:element>
	<xsl:element name="xsl:apply-templates">
	  <xsl:attribute name="select">.</xsl:attribute>
	  <xsl:element name="xsl:with-param">
	    <xsl:attribute name="name">parent_precedence</xsl:attribute>
	    <xsl:value-of select="$precedence"/>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="infix_prefix_unparse">
    <xsl:param name="cd"/>
    <xsl:param name="name"/>
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>0<!-- A value of 100 would put parenthesis in unhandled cases, but then it does also around the whole expression. By setting it to 0, errors should be easier to spot (no parenthesis where there should be) and the root OMA is not enclosed. --></xsl:element>
      <xsl:element name="xsl:choose">
	<xsl:element name="xsl:when">
	  <xsl:attribute name="test">$parent_precedence &gt; <xsl:value-of select="$precedence"/></xsl:attribute>
	  <xsl:element name="c:group_object_parenthesis">
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">(</xsl:attribute>
	    </xsl:element>
	    <xsl:call-template name="infix_prefix_args_unparse">
	      <xsl:with-param name="precedence" select="$precedence"/>
	      <xsl:with-param name="type" select="$type"/>
	    </xsl:call-template>
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">)</xsl:attribute>
	    </xsl:element>
	  </xsl:element><!-- c:group_object_parenthesis -->
	</xsl:element>
	<xsl:element name="xsl:otherwise">
	  <xsl:call-template name="infix_prefix_args_unparse">
	    <xsl:with-param name="precedence" select="$precedence"/>
	    <xsl:with-param name="type" select="$type"/>
	  </xsl:call-template>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="infix_prefix_args_unparse">
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="c:{$type}_app">
      <xsl:element name="xsl:for-each">
	<xsl:attribute name="select">*[position() != 1]</xsl:attribute>
	<xsl:element name="xsl:choose">
	  <xsl:element name="xsl:when">
	    <xsl:attribute name="test">position() != 1</xsl:attribute>
	    <xsl:element name="c:{$type}_app_unary">
	      <xsl:element name="xsl:apply-templates">
		<xsl:attribute name="select">../*[1]</xsl:attribute>
	      </xsl:element>
	      <xsl:element name="xsl:apply-templates">
		<xsl:attribute name="select">.</xsl:attribute>
		<xsl:element name="xsl:with-param">
		  <xsl:attribute name="name">parent_precedence</xsl:attribute>
		  <xsl:value-of select="$precedence"/>
		</xsl:element>
	      </xsl:element>
	    </xsl:element>
	  </xsl:element>
	  <xsl:element name="xsl:otherwise">
	    <xsl:element name="xsl:apply-templates">
	      <xsl:attribute name="select">.</xsl:attribute>
	      <xsl:element name="xsl:with-param">
		<xsl:attribute name="name">parent_precedence</xsl:attribute>
		<xsl:value-of select="$precedence"/>
	      </xsl:element>
	    </xsl:element>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="prefix">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:param name="precedence"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:<xsl:value-of select="$type"/>_app[*[position() = 1 and local-name() = 'token' and @literal='<xsl:value-of select="$token"/>']]</xsl:attribute>
      <om:OMA>
        <om:OMS cd="{$cd}" name="{$name}"/>
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">*[position() != 1]</xsl:attribute>
        </xsl:element>
      </om:OMA>
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[position() = 1 and @cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>100</xsl:element>
      <xsl:element name="xsl:call-template">
        <xsl:attribute name="name">prefix</xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">symbol</xsl:attribute>
          <xsl:value-of select="$token"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">precedence</xsl:attribute>
          <xsl:value-of select="$precedence"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">parent_precedence</xsl:attribute>
	  <xsl:value-of select="$precedence"/>
        </xsl:element>
      </xsl:element>
    </xsl:element>
    
    <!-- Unparse -->
    <xsl:call-template name="prefix_unparse">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="precedence" select="$precedence"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
    
    <!-- Define also the symbol standalone. -->
    <xsl:call-template name="symbol">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="token" select="$token"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="prefix_unparse">
    <xsl:param name="cd"/>
    <xsl:param name="name"/>
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>0<!-- A value of 100 would put parenthesis in unhandled cases, but then it does also around the whole expression. By setting it to 0, errors should be easier to spot (no parenthesis where there should be) and the root OMA is not enclosed. --></xsl:element>
      <xsl:element name="xsl:choose">
	<xsl:element name="xsl:when">
	  <xsl:attribute name="test">$parent_precedence &gt; <xsl:value-of select="$precedence"/></xsl:attribute>
	  <xsl:element name="c:group_object_parenthesis">
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">(</xsl:attribute>
	    </xsl:element>
	    <xsl:call-template name="prefix_args_unparse">
	      <xsl:with-param name="precedence" select="$precedence"/>
	      <xsl:with-param name="type" select="$type"/>
	    </xsl:call-template>
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">)</xsl:attribute>
	    </xsl:element>
	  </xsl:element><!-- c:group_object_parenthesis -->
	</xsl:element>
	<xsl:element name="xsl:otherwise">
	  <xsl:call-template name="prefix_args_unparse">
	    <xsl:with-param name="precedence" select="$precedence"/>
	    <xsl:with-param name="type" select="$type"/>
	  </xsl:call-template>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="prefix_args_unparse">
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="c:{$type}_app">
      <xsl:element name="xsl:apply-templates">
	<xsl:attribute name="select">*[1]</xsl:attribute>
      </xsl:element>
      <xsl:element name="xsl:for-each">
	<xsl:attribute name="select">*[position() != 1]</xsl:attribute>
	<xsl:element name="xsl:apply-templates">
	  <xsl:attribute name="select">.</xsl:attribute>
	  <xsl:element name="xsl:with-param">
	    <xsl:attribute name="name">parent_precedence</xsl:attribute>
	    <xsl:value-of select="$precedence"/>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="postfix">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:param name="precedence"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">c:<xsl:value-of select="$type"/>_app[*[position() = 2 and local-name() = 'token' and @literal='<xsl:value-of select="$token"/>']]</xsl:attribute>
      <om:OMA>
        <om:OMS cd="{$cd}" name="{$name}"/>
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">*[position() != 2]</xsl:attribute>
        </xsl:element>
      </om:OMA>
    </xsl:element>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[position() = 1 and @cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>100</xsl:element>
      <xsl:element name="xsl:call-template">
        <xsl:attribute name="name">postfix</xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">symbol</xsl:attribute>
          <xsl:value-of select="$token"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">precedence</xsl:attribute>
          <xsl:value-of select="$precedence"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">parent_precedence</xsl:attribute>
	  <xsl:value-of select="$precedence"/>
        </xsl:element>
      </xsl:element>
    </xsl:element>
    
    <!-- Unparse -->
    <xsl:call-template name="postfix_unparse">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="precedence" select="$precedence"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
    
    <!-- Define also the symbol standalone. -->
    <xsl:call-template name="symbol">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="token" select="$token"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="postfix_unparse">
    <xsl:param name="cd"/>
    <xsl:param name="name"/>
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>0<!-- A value of 100 would put parenthesis in unhandled cases, but then it does also around the whole expression. By setting it to 0, errors should be easier to spot (no parenthesis where there should be) and the root OMA is not enclosed. --></xsl:element>
      <xsl:element name="xsl:choose">
	<xsl:element name="xsl:when">
	  <xsl:attribute name="test">$parent_precedence &gt; <xsl:value-of select="$precedence"/></xsl:attribute>
	  <xsl:element name="c:group_object_parenthesis">
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">(</xsl:attribute>
	    </xsl:element>
	    <xsl:call-template name="postfix_args_unparse">
	      <xsl:with-param name="precedence" select="$precedence"/>
	      <xsl:with-param name="type" select="$type"/>
	    </xsl:call-template>
	    <xsl:element name="c:token">
	      <xsl:attribute name="literal">)</xsl:attribute>
	    </xsl:element>
	  </xsl:element><!-- c:group_object_parenthesis -->
	</xsl:element>
	<xsl:element name="xsl:otherwise">
	  <xsl:call-template name="postfix_args_unparse">
	    <xsl:with-param name="precedence" select="$precedence"/>
	    <xsl:with-param name="type" select="$type"/>
	  </xsl:call-template>
	</xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="postfix_args_unparse">
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="c:{$type}_app">
      <xsl:element name="xsl:for-each">
	<xsl:attribute name="select">*[position() != 1]</xsl:attribute>
	<xsl:element name="xsl:apply-templates">
	  <xsl:attribute name="select">.</xsl:attribute>
	  <xsl:element name="xsl:with-param">
	    <xsl:attribute name="name">parent_precedence</xsl:attribute>
	    <xsl:value-of select="$precedence"/>
	  </xsl:element>
	</xsl:element>
      </xsl:element>
      <xsl:element name="xsl:apply-templates">
	<xsl:attribute name="select">*[1]</xsl:attribute>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
  <xsl:template name="fenced">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="token"/><xsl:param name="type"/>
    <xsl:param name="precedence"/><xsl:param name="associativity"/>
    <xsl:element name="xsl:template">
      <!-- TODO: finish this pattern. -->
      <xsl:attribute name="match">c:NO_group_object_<xsl:value-of select="$type"/></xsl:attribute>
      <xsl:element name="xsl:call-template">
        <xsl:attribute name="name">fenced</xsl:attribute>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">cd</xsl:attribute>
          <xsl:value-of select="$cd"/>
        </xsl:element>
        <xsl:element name="xsl:with-param">
          <xsl:attribute name="name">name</xsl:attribute>
          <xsl:value-of select="$name"/>
        </xsl:element>
      </xsl:element>
    </xsl:element>
    
    <!-- Unparse -->
    <xsl:call-template name="fenced_unparse">
      <xsl:with-param name="cd" select="$cd"/>
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="precedence" select="$precedence"/>
      <xsl:with-param name="type" select="$type"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="fenced_unparse">
    <xsl:param name="cd"/>
    <xsl:param name="name"/>
    <xsl:param name="precedence"/>
    <xsl:param name="type"/>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">om:OMA[om:OMS[@cd='<xsl:value-of select="$cd"/>' and @name='<xsl:value-of select="$name"/>']]</xsl:attribute>
      <xsl:element name="xsl:param"><xsl:attribute name="name">parent_precedence</xsl:attribute>0<!-- A value of 100 would put parenthesis in unhandled cases, but then it does also around the whole expression. By setting it to 0, errors should be easier to spot (no parenthesis where there should be) and the root OMA is not enclosed. --></xsl:element>
      <xsl:variable name="open">
        <xsl:choose>
          <xsl:when test="$type='parenthesis'">(</xsl:when>
          <xsl:when test="$type='brackets'">[</xsl:when>
          <xsl:when test="$type='braces'">{{</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="close">
        <xsl:choose>
          <xsl:when test="$type='parenthesis'">)</xsl:when>
          <xsl:when test="$type='brackets'">]</xsl:when>
          <xsl:when test="$type='braces'">}}</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:element name="c:group_object_{$type}">
        <c:token literal="{$open}"/>
        <c:tuple_object>
          <xsl:call-template name="function_arguments"/>
        </c:tuple_object>
        <c:token literal="{$close}"/>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  
</xsl:stylesheet>
