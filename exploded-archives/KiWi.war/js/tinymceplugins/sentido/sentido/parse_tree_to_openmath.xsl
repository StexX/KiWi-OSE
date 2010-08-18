<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:c="http://www.matracas.org/ns/cascada"
  xmlns:om="http://www.openmath.org/OpenMath"
  xmlns:transformiix="http://www.mozilla.org/TransforMiix"
  version="1.0">
  
  <xsl:output method="xml"/>
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="c:result">
    <xsl:variable name="content" select="c:*[local-name() != 'line_separator']"/>
    <xsl:choose>
      <xsl:when test="$content">
        <xsl:for-each select="$content">
          <om:OMOBJ><xsl:apply-templates select="."/></om:OMOBJ>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <om:OMOBJ/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="om:OMOBJ">
    <c:result><xsl:apply-templates/></c:result>
  </xsl:template>
  
  <xsl:template match="transformiix:result">
    <xsl:apply-templates select="*[1]"/>
  </xsl:template>
  
  <xsl:template match="c:text">
    <om:OMSTR><xsl:value-of select="@literal"/></om:OMSTR>
  </xsl:template>
  
  <xsl:template match="c:string">
    <om:OMSTR>
      <xsl:for-each select="*[position() != 1 and position() != last()]">
	<xsl:value-of select="@literal"/>
      </xsl:for-each>
    </om:OMSTR>
  </xsl:template>
  
  <xsl:template match="om:OMSTR">
    <c:string>
      <c:token literal='"'/>
      <c:text literal="{.}"/>
      <c:token literal='"'/>
    </c:string>
  </xsl:template>
  
  <xsl:template match="c:group_object_parenthesis|c:group_object_brackets|c:group_object_braces">
    <xsl:variable name="first_token" select="c:token/@literal"/>
    <xsl:choose>
      <xsl:when test="$first_token='(' or $first_token='[' or $first_token='{'">
        <xsl:apply-templates select="c:*[position() mod 2 = 0]"/>        
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="c:*[position() mod 2 = 1]"/>        
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="c:line_separator"/>
  
  <xsl:template match="c:tuple_object">
    <xsl:choose>
      <xsl:when test="parent::c:group_object_parenthesis/parent::c:op_func_app or parent::c:group_object_brackets/parent::c:op_func_app">
        <xsl:apply-templates select="c:*[position() mod 2 = 1]"/>
      </xsl:when>
      <xsl:when test="parent::c:group_object_braces">
        <om:OMA>
          <om:OMS cd="set1" name="set"/>
          <xsl:apply-templates select="c:*[position() mod 2 = 1]"/>
        </om:OMA>
      </xsl:when>
      <xsl:otherwise>
        <om:OMA>
          <om:OMS cd="list1" name="list"/>
          <xsl:apply-templates select="c:*[position() mod 2 = 1]"/>
        </om:OMA>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="c:tuple_object_parenthesis|c:tuple_object_brackets">
    <xsl:choose>
      <xsl:when test="parent::c:op_func_app">
        <xsl:apply-templates select="c:*[position() mod 2 = 0]"/>
      </xsl:when>
      <xsl:otherwise>
        <om:OMA>
          <om:OMS cd="list1" name="list"/>
          <xsl:apply-templates select="c:*[position() mod 2 = 0]"/>
        </om:OMA>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="om:OMA[om:*[1][self::om:OMS][@cd='list1' and @name='list']]">
    <c:group_object_parenthesis>
      <c:token literal="("/>
      <c:tuple_object>
	<xsl:for-each select="om:*[position() != 1]">
	  <xsl:if test="position() != 1"><c:token literal=","/></xsl:if>
	  <xsl:apply-templates select="."/>
	</xsl:for-each>
      </c:tuple_object>
      <c:token literal=")"/>
    </c:group_object_parenthesis>
  </xsl:template>
  
  <xsl:template match="c:tuple_object_braces">
    <om:OMA>
      <om:OMS cd="set1" name="set"/>
      <xsl:apply-templates select="c:*[position() mod 2 = 0]"/>
    </om:OMA>
  </xsl:template>
  <xsl:template name="fenced">
    <xsl:param name="cd"/><xsl:param name="name"/>
  </xsl:template>
  
  <xsl:template name="binding">
    <xsl:param name="list" select="c:*[local-name() != 'text' or (@literal != ' ' and @literal != '.')]"/>
    <om:OMBIND>
      <xsl:apply-templates select="$list[1]"/>
      <om:OMBVAR>
        <xsl:for-each select="$list[2]/*[local-name() != 'text' or @literal != ' ']">
          <xsl:apply-templates/>
        </xsl:for-each>
      </om:OMBVAR>
      <xsl:apply-templates select="$list[3]"/>
      <!-- We ignore any extra arguments. -->
    </om:OMBIND>
  </xsl:template>
  
  <xsl:template name="prefix">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <om:OMA>
      <om:OMS cd="{$cd}" name="{$name}"/>
      <xsl:apply-templates select="*[2]"/><!-- Ignore spurious arguments. -->
    </om:OMA>
  </xsl:template>
  
  <xsl:template name="postfix">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <om:OMA>
      <xsl:apply-templates select="*[2]"/><!-- Ignore spurious arguments. -->
      <om:OMS cd="{$cd}" name="{$name}"/>
    </om:OMA>
  </xsl:template>
  
  <xsl:template name="infix_prefix_left-to-right_n-ary">
    <xsl:param name="list" select="c:*[local-name() != 'text' or @literal != ' ']"/>
    <xsl:param name="begins_with_unary" select="true"/>
    <xsl:param name="unary_cd"/><xsl:param name="unary_name"/>
    <xsl:variable name="list_length" select="count($list)"/>
    <xsl:choose>
      <xsl:when test="$list_length = 1">
        <xsl:choose>
          <xsl:when test="$unary_cd and $unary_name">
            <om:OMA>
              <!-- This is the prefix unary case, with specific symbol. -->
              <om:OMS cd="{$unary_cd}" name="{$unary_name}"/>
              <xsl:choose>
                <xsl:when test="$begins_with_unary">
                  <xsl:apply-templates select="$list[1]/c:*[2]"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:apply-templates select="$list[1]"/>
                </xsl:otherwise>
              </xsl:choose>
            </om:OMA>
          </xsl:when>
          <xsl:otherwise>
            <!-- This is the prefix unary case, if unary application is
                 the identity, like in the case of arith1:plus. -->
            <xsl:choose>
              <xsl:when test="$begins_with_unary">
                <xsl:apply-templates select="$list[1]/c:*[2]"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="$list[1]"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$list_length = 2">
        <xsl:choose>
          <xsl:when test="$begins_with_unary">
            <om:OMA>
              <xsl:apply-templates select="$list[2]/c:*[1]"/>
              <xsl:call-template name="infix_prefix_left-to-right_n-ary">
                <xsl:with-param name="list" select="$list[1]"/>
                <xsl:with-param name="begins_with_unary" select="$begins_with_unary"/>
                <xsl:with-param name="unary_cd" select="$unary_cd"/>
                <xsl:with-param name="unary_name" select="$unary_name"/>
              </xsl:call-template>
              <xsl:apply-templates select="$list[2]/c:*[2]"/>
            </om:OMA>
          </xsl:when>
          <xsl:otherwise>
            <om:OMA>
              <xsl:apply-templates select="$list[2]/c:*[1]"/>
              <xsl:apply-templates select="$list[1]"/>
              <xsl:apply-templates select="$list[2]/c:*[2]"/>
            </om:OMA>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:variable name="last_operator_token" select="$list[last()]/c:*[1]/@literal"/>
        <xsl:variable name="last_different_operator" select="$list/c:*[1][@literal != $last_operator_token][last()]"/>
        <om:OMA>
          <xsl:apply-templates select="$list[last()]/c:*[1]"/>
          <xsl:choose>
            <xsl:when test="$last_different_operator">
              <xsl:variable name="position_of_last_different_operator" select="count($last_different_operator/../preceding-sibling::c:*) + 1"/>
              <xsl:call-template name="infix_prefix_left-to-right_n-ary">
                <xsl:with-param name="list" select="$list[position() &lt;= $position_of_last_different_operator]"/>
              </xsl:call-template>
              <xsl:apply-templates select="$list[position() &gt; $position_of_last_different_operator]/c:*[2]"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$begins_with_unary">
                  <xsl:apply-templates select="$list/c:*[2]"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:apply-templates select="$list[1]"/>
                  <xsl:apply-templates select="$list[position() &gt; 1]/c:*[2]"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </om:OMA>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template name="infix_left-to-right_n-ary">
    <xsl:param name="list" select="c:*[local-name() != 'text' or @literal != ' ']"/>
    <xsl:variable name="list_length" select="count($list)"/>
    <om:OMA>
      <xsl:choose>
        <xsl:when test="$list_length &lt; 3">
          <!-- An infix operator should have at least two arguments. -->
          <xsl:text>ERROR:infix operator has only </xsl:text><xsl:value-of select="count($list)-1"/><xsl:text> argument: [</xsl:text><xsl:apply-templates select="$list"/><xsl:text>]</xsl:text>
        </xsl:when>
        <xsl:when test="$list_length = 3">
          <xsl:apply-templates select="$list[2]"/>
          <xsl:apply-templates select="$list[1]"/>
          <xsl:apply-templates select="$list[3]"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="operators" select="$list[position() mod 2 = 0]"/>
          <xsl:apply-templates select="$operators[last()]"/>
          <xsl:variable name="last_operator_token" select="$operators[last()]/@literal"/>
          <xsl:variable name="last_different_operator" select="$operators[@literal != $last_operator_token][last()]"/>
          <xsl:choose>
            <xsl:when test="$last_different_operator">
              <xsl:variable name="position_of_last_different_operator" select="count($last_different_operator/preceding-sibling::c:*) + 1"/>
              <xsl:variable name="position_of_first_argument_to_same_operator" select="$position_of_last_different_operator + 1"/>
              <xsl:call-template name="infix_left-to-right_n-ary">
                <xsl:with-param name="list" select="$list[position() &lt;= $position_of_first_argument_to_same_operator]"/>
              </xsl:call-template>
              <xsl:apply-templates select="$list[position() &gt; $position_of_first_argument_to_same_operator and position() mod 2 = 1]"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="$list[position() mod 2 = 1]"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </om:OMA>
  </xsl:template>
  
  <xsl:template name="infix_left-to-right">
    <xsl:param name="list" select="c:*[local-name() != 'text' or @literal != ' ']"/>
    <om:OMA>
      <xsl:choose>
        <xsl:when test="count($list) = 3">
          <xsl:apply-templates select="$list[2]"/>
          <xsl:apply-templates select="$list[1]"/>
          <xsl:apply-templates select="$list[3]"/>
        </xsl:when>
        <xsl:when test="count($list) &gt; 3">
          <xsl:apply-templates select="$list[last()-1]"/>
          <xsl:call-template name="infix_left-to-right">
            <xsl:with-param name="list" select="$list[position() &lt;= last()-2]"/>
          </xsl:call-template>
          <xsl:apply-templates select="$list[last()]"/>
        </xsl:when>
        <xsl:otherwise>
          <!-- An infix operator should have at least two arguments. -->
          <xsl:text>ERROR:infix operator has only </xsl:text><xsl:value-of select="count($list)-1"/><xsl:text> argument: [</xsl:text><xsl:apply-templates select="$list"/><xsl:text>]</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </om:OMA>
  </xsl:template>
  
  <xsl:template name="infix_right-to-left">
    <xsl:param name="list" select="c:*[local-name() != 'text' or @literal != ' ']"/>
    <om:OMA>
      <xsl:choose>
        <xsl:when test="count($list) = 3">
          <xsl:apply-templates select="$list[2]"/>
          <xsl:apply-templates select="$list[1]"/>
          <xsl:apply-templates select="$list[3]"/>
        </xsl:when>
        <xsl:when test="count($list) &gt; 3">
          <xsl:apply-templates select="$list[2]"/>
          <xsl:apply-templates select="$list[1]"/>
          <xsl:call-template name="infix_right-to-left">
            <xsl:with-param name="list" select="$list[position() &gt; 2]"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- An infix operator should have at least two arguments. -->
          <xsl:text>ERROR:infix operator has only </xsl:text><xsl:value-of select="count($list) - 1"/><xsl:text> argument: [</xsl:text><xsl:apply-templates select="$list"/><xsl:text>]</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </om:OMA>
  </xsl:template>
  
  <xsl:template name="juxtaposition_left-to-right_n-ary">
    <xsl:param name="cd"/><xsl:param name="name"/>
    <xsl:param name="list" select="c:*[local-name() != 'text' or @literal != ' ']"/>
    <om:OMA>
      <xsl:choose>
        <xsl:when test="count($list) = 2">
	  <om:OMS cd="{$cd}" name="{$name}"/>
          <xsl:apply-templates select="$list[1]"/>
          <xsl:apply-templates select="$list[2]"/>
        </xsl:when>
        <xsl:when test="count($list) &gt; 2">
          <om:OMS cd="{$cd}" name="{$name}"/>
          <xsl:call-template name="juxtaposition_left-to-right_n-ary">
            <xsl:with-param name="cd" select="$cd"/>
            <xsl:with-param name="name" select="$name"/>
            <xsl:with-param name="list" select="$list[position() &lt;= last()-1]"/>
          </xsl:call-template>
          <xsl:apply-templates select="$list[last()]"/>
        </xsl:when>
        <xsl:otherwise>
          <!-- A juxtaposition should have at least two arguments. -->
          <xsl:text>ERROR:juxtaposition has only </xsl:text><xsl:value-of select="count($list)-1"/><xsl:text> argument: [</xsl:text><xsl:apply-templates select="$list"/><xsl:text>]</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </om:OMA>
  </xsl:template>
  
  <!-- What to do with undefined tokens. -->
  <xsl:template match="c:token">
    <xsl:if test="@literal"><om:OMV name="{@literal}"/></xsl:if>
  </xsl:template>
  
  <xsl:template match="om:OMV">
    <c:token literal="{@name}"/>
  </xsl:template>
  
  <xsl:template match="c:token[@literal and translate(@literal, '0123456789', '') = '']">
    <xsl:value-of select="@literal"/>
  </xsl:template>
  
  <xsl:template match="c:num_int">
    <om:OMI><xsl:for-each select="c:token|c:text"><xsl:value-of select="@literal"/></xsl:for-each></om:OMI>
  </xsl:template>
  
  <xsl:template match="om:OMI">
    <c:num_int>
      <xsl:call-template name="split-into-literal-nodes">
	<xsl:with-param name="string" select="text()"/>
      </xsl:call-template>
    </c:num_int>
  </xsl:template>
  
  <xsl:template name="split-into-literal-nodes">
    <xsl:param name="string"/>
    <xsl:if test="$string">
      <c:text literal="{substring($string, 1, 1)}"/>
      <xsl:if test="string-length($string) &gt; 1">
	<xsl:call-template name="split-into-literal-nodes">
	  <xsl:with-param name="string" select="substring($string, 2)"/>
	</xsl:call-template>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="c:num_dec">
    <xsl:element name="om:OMF">
      <xsl:attribute name="dec">
	<xsl:for-each select="c:token|c:text"><xsl:value-of select="@literal"/></xsl:for-each>
      </xsl:attribute>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="om:OMF">
    <c:num_dec>
      <xsl:call-template name="split-into-literal-nodes">
	<xsl:with-param name="string" select="@dec"/>
      </xsl:call-template>
    </c:num_dec>
  </xsl:template>
  
  <xsl:template match="c:num_rat">
    <!-- TODO: simplify this select since we don't have space tokens in the parse tree any more. -->
    <xsl:variable name="fraction_line_operator" select="c:*[@literal and translate(@literal, '0123456789', '')][1]"/>
    <om:OMA>
      <om:OMS cd="nums1" name="rational"/>
      <om:OMI><xsl:apply-templates select="$fraction_line_operator/preceding-sibling::c:*"/></om:OMI>
      <om:OMI><xsl:apply-templates select="$fraction_line_operator/following-sibling::c:*"/></om:OMI>
    </om:OMA>
  </xsl:template>
  
  <xsl:template match="om:OMA[*[1][self::om:OMS][@cd='nums1' and @name='rational']]">
    <c:num_rat>
      <xsl:apply-templates select="*[2]"/>
      <c:literal token="{*[1]}"/>
      <xsl:apply-templates select="*[3]"/>
    </c:num_rat>
  </xsl:template>
  
  <xsl:template match="c:op_func_app">
    <om:OMA>
      <xsl:apply-templates/>
    </om:OMA>
  </xsl:template>
  
  <xsl:template match="om:OMA">
    <c:openmath_OMA>
      <c:token literal="OMA"/>
      <c:group_object_parenthesis>
        <c:token literal="("/>
        <xsl:apply-templates/>
        <c:token literal=")"/>
      </c:group_object_parenthesis>
    </c:openmath_OMA>
  </xsl:template>
  
  <xsl:template match="om:OMS">
    <c:openmath_OMS>
      <c:token literal="OMS"/>
      <c:group_object_parenthesis>
        <c:token literal="("/>
        <c:tuple_object>
          <c:text literal="{@cd}"/>
          <c:token literal=","/>
          <c:text literal="{@name}"/>
        </c:tuple_object>
        <c:token literal=")"/>
      </c:group_object_parenthesis>
    </c:openmath_OMS>
  </xsl:template>
  
  <xsl:template match="om:OMR">
    <c:openmath_OMR>
      <c:token literal="OMR"/>
      <c:group_object_parenthesis>
        <c:token literal="("/>
        <c:text literal="{@href}"/>
        <c:token literal=")"/>
      </c:group_object_parenthesis>
    </c:openmath_OMR>
  </xsl:template>
  
  <xsl:template match="c:openmath_OMA">
    <om:OMA>
      <xsl:apply-templates select="c:group_object_parenthesis/*[position() != 1 and position() != last()]"/>
    </om:OMA>
  </xsl:template>
  
  <xsl:template match="c:openmath_OMA[c:group_object_parenthesis/c:tuple_object]">
    <om:OMA c="hello">
      <xsl:apply-templates select="c:group_object_parenthesis/c:tuple_object/*[position() mod 2 = 1]"/>
    </om:OMA>
  </xsl:template>
  
  <xsl:template match="c:openmath_OMS">
    <xsl:variable name="tuple" select="c:group_object_parenthesis/c:tuple_object[1]"/>
    <xsl:variable name="cd">
      <xsl:for-each select="$tuple/c:string[1]/*[position() != 1 and position() != last()]">
        <xsl:value-of select="@literal"/>
      </xsl:for-each>
    </xsl:variable>
    <xsl:variable name="name">
      <xsl:for-each select="$tuple/c:string[2]/*[position() != 1 and position() != last()]">
        <xsl:value-of select="@literal"/>
      </xsl:for-each>
    </xsl:variable>
    <om:OMS cd="{$cd}" name="{$name}"/>
  </xsl:template>
  
  <!-- ========================= Defined symbols: ====================== -->
  
</xsl:stylesheet>
