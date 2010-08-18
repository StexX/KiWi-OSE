<?xml version="1.0" encoding="utf-8"?><xsl:stylesheet c:namespace-holder="keep-this-namespace" m:namespace-holder="keep-this-namespace" mq:namespace-holder="keep-this-namespace" om:namespace-holder="keep-this-namespace" version="1.0" xmlns:c="http://www.matracas.org/ns/cascada" xmlns:m="http://www.w3.org/1998/Math/MathML" xmlns:mq="http://mathweb.org/MathQuery" xmlns:om="http://www.openmath.org/OpenMath" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:strip-space elements="*"/>
  <xsl:output indent="yes" method="xml"/>
  <xsl:template match="*|text()">
    <xsl:copy>
      <xsl:for-each select="@*">
        <xsl:copy/>
      </xsl:for-each>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="m:math">
    <xsl:variable name="expression" select="*[1]"/>
    <om:OMOBJ>
      <xsl:apply-templates select="$expression"/>
    </om:OMOBJ>
  </xsl:template>
  <xsl:template match="m:cn[@type='integer']">
    <xsl:variable name="d" select="text()"/>
    <om:OMI>
      <xsl:apply-templates select="$d"/>
    </om:OMI>
  </xsl:template>
  <xsl:template match="m:cn[@type='real']">
    <xsl:variable name="d" select="text()"/>
    <om:OMF>
      <xsl:attribute name="dec">
        <xsl:value-of select="$d"/>
      </xsl:attribute>
    </om:OMF>
  </xsl:template>
  <xsl:template match="m:ci">
    <xsl:variable name="v" select="text()"/>
    <xsl:variable name="pattern-name" select="@mq:generic"/>
    <xsl:variable name="anycount" select="@mq:anycount"/>
    <om:OMV>
      <xsl:attribute name="name">
        <xsl:value-of select="$v"/>
      </xsl:attribute>
      <xsl:if test="$pattern-name != ''">
        <xsl:attribute name="mq:generic">
          <xsl:value-of select="$pattern-name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="mq:anycount">
        <xsl:value-of select="$anycount"/>
      </xsl:attribute>
    </om:OMV>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:abs]]">
    <xsl:variable name="a" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="abs"/>
      <xsl:apply-templates select="$a"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:divide]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="divide"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:minus]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="minus"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:plus]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="plus"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:power]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="power"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=5 and *[1][self::m:product] and *[2][self::m:bvar[count(*)&gt;=1]] and *[3][self::m:lowlimit[count(*)=1]] and *[4][self::m:uplimit[count(*)=1]]]">
    <xsl:variable name="i" select="*[2]/*[1]"/>
    <xsl:variable name="a" select="*[3]/*[1]"/>
    <xsl:variable name="b" select="*[4]/*[1]"/>
    <xsl:variable name="f" select="*[5]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="product"/>
      <om:OMA>
        <om:OMS cd="interval1" name="integer_interval"/>
        <xsl:apply-templates select="$a"/>
        <xsl:apply-templates select="$b"/>
      </om:OMA>
      <om:OMBIND>
        <om:OMS cd="fns1" name="lambda"/>
        <om:OMBVAR>
          <xsl:apply-templates select="$i"/>
        </om:OMBVAR>
        <xsl:apply-templates select="$f"/>
      </om:OMBIND>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:root] and *[2][self::m:degree[count(*)=1]]]">
    <xsl:variable name="n" select="*[2]/*[1]"/>
    <xsl:variable name="a" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="root"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$n"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:root] and *[2][self::m:degree[count(*)=1 and *[1][self::m:ci[@type='integer']]]]]">
    <xsl:variable name="a" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="root"/>
      <xsl:apply-templates select="$a"/>
      <om:OMI>2</om:OMI>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=5 and *[1][self::m:sum] and *[2][self::m:bvar[count(*)&gt;=1]] and *[3][self::m:lowlimit[count(*)=1]] and *[4][self::m:uplimit[count(*)=1]]]">
    <xsl:variable name="i" select="*[2]/*[1]"/>
    <xsl:variable name="a" select="*[3]/*[1]"/>
    <xsl:variable name="b" select="*[4]/*[1]"/>
    <xsl:variable name="f" select="*[5]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="sum"/>
      <om:OMA>
        <om:OMS cd="interval1" name="integer_interval"/>
        <xsl:apply-templates select="$a"/>
        <xsl:apply-templates select="$b"/>
      </om:OMA>
      <om:OMBIND>
        <om:OMS cd="fns1" name="lambda"/>
        <om:OMBVAR>
          <xsl:apply-templates select="$i"/>
        </om:OMBVAR>
        <xsl:apply-templates select="$f"/>
      </om:OMBIND>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:times]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="times"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:minus]]">
    <xsl:variable name="a" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="arith1" name="unary_minus"/>
      <xsl:apply-templates select="$a"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=5 and *[1][self::m:int] and *[2][self::m:bvar[count(*)=1]] and *[3][self::m:lowlimit[count(*)=1]] and *[4][self::m:uplimit[count(*)=1]]]">
    <xsl:variable name="x" select="*[2]/*[1]"/>
    <xsl:variable name="a" select="*[3]/*[1]"/>
    <xsl:variable name="b" select="*[4]/*[1]"/>
    <xsl:variable name="f" select="*[5]"/>
    <om:OMA>
      <om:OMS cd="calculus1" name="defint"/>
      <om:OMA>
        <om:OMS cd="interval1" name="interval_cc"/>
        <xsl:apply-templates select="$a"/>
        <xsl:apply-templates select="$b"/>
      </om:OMA>
      <om:OMBIND>
        <om:OMS cd="fns1" name="lambda"/>
        <om:OMBVAR>
          <xsl:apply-templates select="$x"/>
        </om:OMBVAR>
        <xsl:apply-templates select="$f"/>
      </om:OMBIND>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:int] and *[2][self::m:bvar[count(*)=1]]]">
    <xsl:variable name="x" select="*[2]/*[1]"/>
    <xsl:variable name="f" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="calculus1" name="int"/>
      <om:OMBIND>
        <om:OMS cd="fns1" name="lambda"/>
        <om:OMBVAR>
          <xsl:apply-templates select="$x"/>
        </om:OMBVAR>
        <xsl:apply-templates select="$f"/>
      </om:OMBIND>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:arg]]">
    <xsl:variable name="z" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="complex1" name="argument"/>
      <xsl:apply-templates select="$z"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:conjugate]]">
    <xsl:variable name="z" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="complex1" name="conjugate"/>
      <xsl:apply-templates select="$z"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:imaginary]]">
    <xsl:variable name="z" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="complex1" name="imaginary"/>
      <xsl:apply-templates select="$z"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:imaginary]]">
    <xsl:variable name="z" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="complex1" name="real"/>
      <xsl:apply-templates select="$z"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:domain]]">
    <xsl:variable name="f" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="domain"/>
      <xsl:apply-templates select="$f"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:domainofapplication[count(*)=1]">
    <xsl:variable name="D" select="*[1]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="domainofapplication"/>
      <xsl:apply-templates select="$D"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:ident">
    <om:OMS cd="fns1" name="identity"/>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:image]]">
    <xsl:variable name="f" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="image"/>
      <xsl:apply-templates select="$f"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:inverse]]">
    <xsl:variable name="f" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="inverse"/>
      <xsl:apply-templates select="$f"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:lambda[count(*)=2 and *[1][self::m:bvar[count(*)&gt;=1]]]">
    <xsl:variable name="variable" select="*[1]/*[1]"/>
    <xsl:variable name="expression" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="lambda"/>
      <om:OMBVAR>
        <xsl:apply-templates select="$variable"/>
      </om:OMBVAR>
      <xsl:apply-templates select="$expression"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:compose]]">
    <xsl:variable name="f" select="*[2]"/>
    <xsl:variable name="g" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="left_compose"/>
      <xsl:apply-templates select="$f"/>
      <xsl:apply-templates select="$g"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:codomain]]">
    <xsl:variable name="f" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="fns1" name="range"/>
      <xsl:apply-templates select="$f"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:factorial]]">
    <xsl:variable name="n" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="integer1" name="factorial"/>
      <xsl:apply-templates select="$n"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:factorof]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="integer1" name="factorof"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:quotient]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="integer1" name="quotient"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:rem]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="integer1" name="remainder"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:interval[count(*)=2]">
    <xsl:variable name="a" select="*[1]"/>
    <xsl:variable name="b" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="interval1" name="integer_interval"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:interval[count(*)=2 and @closure='closed']">
    <xsl:variable name="a" select="*[1]"/>
    <xsl:variable name="b" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="interval1" name="interval_cc"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:interval[count(*)=2 and @closure='closed-open']">
    <xsl:variable name="a" select="*[1]"/>
    <xsl:variable name="b" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="interval1" name="interval_co"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:interval[count(*)=2 and @closure='open-closed']">
    <xsl:variable name="a" select="*[1]"/>
    <xsl:variable name="b" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="interval1" name="interval_oc"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:interval[count(*)=2 and @closure='open']">
    <xsl:variable name="a" select="*[1]"/>
    <xsl:variable name="b" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="interval1" name="interval_oo"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:determinant]]">
    <xsl:variable name="M" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="determinant"/>
      <xsl:apply-templates select="$M"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=4 and *[1][self::m:selector]]">
    <xsl:variable name="M" select="*[2]"/>
    <xsl:variable name="r" select="*[3]"/>
    <xsl:variable name="c" select="*[4]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="matrix_selector"/>
      <xsl:apply-templates select="$r"/>
      <xsl:apply-templates select="$c"/>
      <xsl:apply-templates select="$M"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:outerproduct]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="outerproduct"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:scalarproduct]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="scalarproduct"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=2 and *[1][self::m:transpose]]">
    <xsl:variable name="M" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="transpose"/>
      <xsl:apply-templates select="$M"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:selector]]">
    <xsl:variable name="v" select="*[2]"/>
    <xsl:variable name="i" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="vector_selector"/>
      <xsl:apply-templates select="$i"/>
      <xsl:apply-templates select="$v"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=3 and *[1][self::m:vectorproduct]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="linalg1" name="vectorproduct"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)&gt;=1 and *[1][self::m:matrix]]">
    <xsl:variable name="row" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="linalg2" name="matrix"/>
      <xsl:apply-templates select="$row"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)&gt;=1 and *[1][self::m:matrixrow]]">
    <xsl:variable name="cell" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="linalg2" name="matrixrow"/>
      <xsl:apply-templates select="$cell"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:apply[count(*)&gt;=1 and *[1][self::m:vector]]">
    <xsl:variable name="cell" select="*[2]"/>
    <om:OMA>
      <om:OMS cd="linalg2" name="vector"/>
      <xsl:apply-templates select="$cell"/>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:tendsto[@type='above']">
    <om:OMS cd="limit1" name="above"/>
  </xsl:template>
  <xsl:template match="m:tendsto[@type='below']">
    <om:OMS cd="limit1" name="below"/>
  </xsl:template>
  <xsl:template match="m:tendsto[@type='both_sides']">
    <om:OMS cd="limit1" name="both_sides"/>
  </xsl:template>
  <xsl:template match="m:apply[count(*)=4 and *[1][self::m:limit] and *[2][self::m:bvar[count(*)=1]] and *[3][self::m:lowlimit[count(*)=1]]]">
    <xsl:variable name="x" select="*[2]/*[1]"/>
    <xsl:variable name="x0" select="*[3]/*[1]"/>
    <xsl:variable name="f" select="*[4]"/>
    <om:OMA>
      <om:OMS cd="limit1" name="limit"/>
      <xsl:apply-templates select="$x0"/>
      <om:OMS cd="limit1" name="both_sides"/>
      <om:OMBIND>
        <om:OMS cd="fns1" name="lambda"/>
        <om:OMBVAR>
          <xsl:apply-templates select="$x"/>
        </om:OMBVAR>
        <xsl:apply-templates select="$f"/>
      </om:OMBIND>
    </om:OMA>
  </xsl:template>
  <xsl:template match="m:tendsto">
    <om:OMS cd="limit1" name="null"/>
  </xsl:template>
  <xsl:template match="m:list[count(*)=3]">
    <xsl:variable name="a" select="*[1]"/>
    <xsl:variable name="b" select="*[2]"/>
    <xsl:variable name="c" select="*[3]"/>
    <om:OMA>
      <om:OMS cd="list1" name="list"/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
      <xsl:apply-templates select="$c"/>
    </om:OMA>
  </xsl:template>
</xsl:stylesheet>
