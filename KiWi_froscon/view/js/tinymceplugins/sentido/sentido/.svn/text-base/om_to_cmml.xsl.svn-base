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
  <xsl:template match="om:OMOBJ">
    <xsl:variable name="expression" select="*[1]"/>
    <m:math>
      <xsl:apply-templates select="$expression"/>
    </m:math>
  </xsl:template>
  <xsl:template match="om:OMI">
    <xsl:variable name="d" select="text()"/>
    <m:cn type="integer">
      <xsl:apply-templates select="$d"/>
    </m:cn>
  </xsl:template>
  <xsl:template match="om:OMF[@dec]">
    <xsl:variable name="d" select="@dec"/>
    <m:cn type="real">
      <xsl:apply-templates select="$d"/>
    </m:cn>
  </xsl:template>
  <xsl:template match="om:OMV[@name]">
    <xsl:variable name="v" select="@name"/>
    <xsl:variable name="pattern-name" select="@*[name()='mq:generic']"/>
    <xsl:variable name="anycount" select="@*[name()='mq:anycount']"/>
    <m:ci>
      <xsl:if test="$pattern-name != ''">
        <xsl:attribute name="mq:generic">
          <xsl:value-of select="$pattern-name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$anycount != ''">
        <xsl:attribute name="mq:anycount">
          <xsl:value-of select="$anycount"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates select="$v"/>
    </m:ci>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='arith1' and @name='abs']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:abs/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='divide']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:divide/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='minus']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:minus/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='plus']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:plus/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='power']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:power/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='product']] and *[2][self::om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='integer_interval']]]] and *[3][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]]]">
    <xsl:variable name="a" select="*[2]/*[2]"/>
    <xsl:variable name="b" select="*[2]/*[3]"/>
    <xsl:variable name="i" select="*[3]/*[2]/*[1]"/>
    <xsl:variable name="f" select="*[3]/*[3]"/>
    <m:apply>
      <m:product/>
      <m:bvar>
        <xsl:apply-templates select="$i"/>
      </m:bvar>
      <m:lowlimit>
        <xsl:apply-templates select="$a"/>
      </m:lowlimit>
      <m:uplimit>
        <xsl:apply-templates select="$b"/>
      </m:uplimit>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='root']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="n" select="*[3]"/>
    <m:apply>
      <m:root/>
      <m:degree>
        <xsl:apply-templates select="$n"/>
      </m:degree>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='root']] and *[3][self::om:OMI]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:root/>
      <m:degree>
        <m:ci type="integer">2</m:ci>
      </m:degree>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='sum']] and *[2][self::om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='integer_interval']]]] and *[3][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]]]">
    <xsl:variable name="a" select="*[2]/*[2]"/>
    <xsl:variable name="b" select="*[2]/*[3]"/>
    <xsl:variable name="i" select="*[3]/*[2]/*[1]"/>
    <xsl:variable name="f" select="*[3]/*[3]"/>
    <m:apply>
      <m:sum/>
      <m:bvar>
        <xsl:apply-templates select="$i"/>
      </m:bvar>
      <m:lowlimit>
        <xsl:apply-templates select="$a"/>
      </m:lowlimit>
      <m:uplimit>
        <xsl:apply-templates select="$b"/>
      </m:uplimit>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='arith1' and @name='times']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:times/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='arith1' and @name='unary_minus']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:minus/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='calculus1' and @name='defint']] and *[2][self::om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='interval_cc']]]] and *[3][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]]]">
    <xsl:variable name="a" select="*[2]/*[2]"/>
    <xsl:variable name="b" select="*[2]/*[3]"/>
    <xsl:variable name="x" select="*[3]/*[2]/*[1]"/>
    <xsl:variable name="f" select="*[3]/*[3]"/>
    <m:apply>
      <m:int/>
      <m:bvar>
        <xsl:apply-templates select="$x"/>
      </m:bvar>
      <m:lowlimit>
        <xsl:apply-templates select="$a"/>
      </m:lowlimit>
      <m:uplimit>
        <xsl:apply-templates select="$b"/>
      </m:uplimit>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='calculus1' and @name='int']] and *[2][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]]]">
    <xsl:variable name="x" select="*[2]/*[2]/*[1]"/>
    <xsl:variable name="f" select="*[2]/*[3]"/>
    <m:apply>
      <m:int/>
      <m:bvar>
        <xsl:apply-templates select="$x"/>
      </m:bvar>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='complex1' and @name='argument']]]">
    <xsl:variable name="z" select="*[2]"/>
    <m:apply>
      <m:arg/>
      <xsl:apply-templates select="$z"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='complex1' and @name='conjugate']]]">
    <xsl:variable name="z" select="*[2]"/>
    <m:apply>
      <m:conjugate/>
      <xsl:apply-templates select="$z"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='complex1' and @name='imaginary']]]">
    <xsl:variable name="z" select="*[2]"/>
    <m:apply>
      <m:imaginary/>
      <xsl:apply-templates select="$z"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='complex1' and @name='real']]]">
    <xsl:variable name="z" select="*[2]"/>
    <m:apply>
      <m:imaginary/>
      <xsl:apply-templates select="$z"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='fns1' and @name='domain']]]">
    <xsl:variable name="f" select="*[2]"/>
    <m:apply>
      <m:domain/>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='fns1' and @name='domainofapplication']]]">
    <xsl:variable name="D" select="*[2]"/>
    <m:domainofapplication>
      <xsl:apply-templates select="$D"/>
    </m:domainofapplication>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='fns1' and @name='identity']">
    <m:ident/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='fns1' and @name='image']]]">
    <xsl:variable name="f" select="*[2]"/>
    <m:apply>
      <m:image/>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='fns1' and @name='inverse']]]">
    <xsl:variable name="f" select="*[2]"/>
    <m:apply>
      <m:inverse/>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)&gt;=1]]]">
    <xsl:variable name="variable" select="*[2]/*[1]"/>
    <xsl:variable name="expression" select="*[3]"/>
    <m:lambda>
      <m:bvar>
        <xsl:apply-templates select="$variable"/>
      </m:bvar>
      <xsl:apply-templates select="$expression"/>
    </m:lambda>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='left_compose']]]">
    <xsl:variable name="f" select="*[2]"/>
    <xsl:variable name="g" select="*[3]"/>
    <m:apply>
      <m:compose/>
      <xsl:apply-templates select="$f"/>
      <xsl:apply-templates select="$g"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='fns1' and @name='range']]]">
    <xsl:variable name="f" select="*[2]"/>
    <m:apply>
      <m:codomain/>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='integer1' and @name='factorial']]]">
    <xsl:variable name="n" select="*[2]"/>
    <m:apply>
      <m:factorial/>
      <xsl:apply-templates select="$n"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='integer1' and @name='factorof']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:factorof/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='integer1' and @name='quotient']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:quotient/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='integer1' and @name='remainder']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:rem/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='integer_interval']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:interval>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:interval>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='interval_cc']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:interval closure="closed">
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:interval>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='interval_co']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:interval closure="closed-open">
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:interval>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='interval_oc']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:interval closure="open-closed">
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:interval>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='interval_oo']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:interval closure="open">
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:interval>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='linalg1' and @name='determinant']]]">
    <xsl:variable name="M" select="*[2]"/>
    <m:apply>
      <m:determinant/>
      <xsl:apply-templates select="$M"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=4 and *[1][self::om:OMS[@cd='linalg1' and @name='matrix_selector']]]">
    <xsl:variable name="r" select="*[2]"/>
    <xsl:variable name="c" select="*[3]"/>
    <xsl:variable name="M" select="*[4]"/>
    <m:apply>
      <m:selector/>
      <xsl:apply-templates select="$M"/>
      <xsl:apply-templates select="$r"/>
      <xsl:apply-templates select="$c"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='linalg1' and @name='outerproduct']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:outerproduct/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='linalg1' and @name='scalarproduct']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:scalarproduct/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='linalg1' and @name='transpose']]]">
    <xsl:variable name="M" select="*[2]"/>
    <m:apply>
      <m:transpose/>
      <xsl:apply-templates select="$M"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='linalg1' and @name='vector_selector']]]">
    <xsl:variable name="i" select="*[2]"/>
    <xsl:variable name="v" select="*[3]"/>
    <m:apply>
      <m:selector/>
      <xsl:apply-templates select="$v"/>
      <xsl:apply-templates select="$i"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='linalg1' and @name='vectorproduct']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:vectorproduct/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=1 and *[1][self::om:OMS[@cd='linalg2' and @name='matrix']]]">
    <xsl:variable name="row" select="*[2]"/>
    <m:apply>
      <m:matrix/>
      <xsl:apply-templates select="$row"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=1 and *[1][self::om:OMS[@cd='linalg2' and @name='matrixrow']]]">
    <xsl:variable name="cell" select="*[2]"/>
    <m:apply>
      <m:matrixrow/>
      <xsl:apply-templates select="$cell"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=1 and *[1][self::om:OMS[@cd='linalg2' and @name='vector']]]">
    <xsl:variable name="cell" select="*[2]"/>
    <m:apply>
      <m:vector/>
      <xsl:apply-templates select="$cell"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='limit1' and @name='above']">
    <m:tendsto type="above"/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='limit1' and @name='below']">
    <m:tendsto type="below"/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='limit1' and @name='both_sides']">
    <m:tendsto type="both_sides"/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=4 and *[1][self::om:OMS[@cd='limit1' and @name='limit']] and *[3][self::om:OMS[@cd='limit1' and @name='both_sides']] and *[4][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]]]">
    <xsl:variable name="x0" select="*[2]"/>
    <xsl:variable name="x" select="*[4]/*[2]/*[1]"/>
    <xsl:variable name="f" select="*[4]/*[3]"/>
    <m:apply>
      <m:limit/>
      <m:bvar>
        <xsl:apply-templates select="$x"/>
      </m:bvar>
      <m:lowlimit>
        <xsl:apply-templates select="$x0"/>
      </m:lowlimit>
      <xsl:apply-templates select="$f"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='limit1' and @name='null']">
    <m:tendsto/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=4 and *[1][self::om:OMS[@cd='list1' and @name='list']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <xsl:variable name="c" select="*[4]"/>
    <m:list>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
      <xsl:apply-templates select="$c"/>
    </m:list>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='list1' and @name='map']] and *[3][self::om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='interval1' and @name='integer_interval']]]]]">
    <xsl:variable name="f" select="*[2]"/>
    <xsl:variable name="a" select="*[3]/*[2]"/>
    <xsl:variable name="b" select="*[3]/*[3]"/>
    <m:list>
      <m:bvar>
        <xsl:apply-templates select="$x"/>
      </m:bvar>
      <m:condition>
        <m:apply>
          <m:in/>
          <xsl:apply-templates select="$x"/>
          <m:interval>
            <xsl:apply-templates select="$a"/>
            <xsl:apply-templates select="$b"/>
          </m:interval>
        </m:apply>
      </m:condition>
    </m:list>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='list1' and @name='map']] and *[2][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]] and *[3][self::om:OMA[count(*)=4 and *[1][self::om:OMS[@cd='set1' and @name='set']]]]]">
    <xsl:variable name="x" select="*[2]/*[2]/*[1]"/>
    <xsl:variable name="f" select="*[2]/*[3]"/>
    <xsl:variable name="x1" select="*[3]/*[2]"/>
    <xsl:variable name="x2" select="*[3]/*[3]"/>
    <xsl:variable name="x3" select="*[3]/*[4]"/>
    <m:list>
      <m:bvar>
        <xsl:apply-templates select="$x"/>
      </m:bvar>
      <m:condition>
        <m:apply>
          <m:in>
            <xsl:apply-templates select="$x"/>
            <m:list>
              <xsl:apply-templates select="$x1"/>
              <xsl:apply-templates select="$x2"/>
              <xsl:apply-templates select="$x3"/>
            </m:list>
          </m:in>
        </m:apply>
      </m:condition>
      <xsl:apply-templates select="$l"/>
    </m:list>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='logic1' and @name='and']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:and/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='logic1' and @name='equivalent']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:equivalent/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='logic1' and @name='false']">
    <m:false/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='logic1' and @name='implies']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:implies/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='logic1' and @name='or']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:or/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='logic1' and @name='true']">
    <m:true/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='logic1' and @name='xor']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:xor/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='minmax1' and @name='max']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:max/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='minmax1' and @name='min']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:min/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_cart_prod']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_cart_prod/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_empty']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_empty/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_in']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_in/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_intersect']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_intersect/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='multiset']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:multiset/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_notin']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_notin/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_notprsubset']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_notprsubset/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_notsubset']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_notsubset/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_prsubset']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_prsubset/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_setdiff']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_setdiff/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_size']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_size/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_card']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_card/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_subset']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_subset/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='multiset1' and @name='ms_union']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ms_union/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='nums1' and @name='based_integer']] and *[3][self::om:OMSTR[count(*)=1]]]">
    <xsl:variable name="b" select="*[2]"/>
    <xsl:variable name="n" select="*[3]/*[1]"/>
    <m:cn base="b" type="integer">
      <xsl:apply-templates select="$n"/>
    </m:cn>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='nums1' and @name='e']">
    <m:exponentiale/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='nums1' and @name='gamma']">
    <m:eulergamma/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='nums1' and @name='i']">
    <m:cn>ⅈ</m:cn>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='nums1' and @name='inf']">
    <m:infinity/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='nums1' and @name='NaN']">
    <m:notanumber/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='nums1' and @name='pi']">
    <m:pi/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='nums1' and @name='rational']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:cn type="rational">
      <xsl:apply-templates select="$a"/>
      <m:sep/>
      <xsl:apply-templates select="$b"/>
    </m:cn>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='piece1' and @name='otherwise']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:otherwise>
      <xsl:apply-templates select="$a"/>
    </m:otherwise>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='piece1' and @name='otherwise']]]">
    <xsl:variable name="c" select="*[2]"/>
    <xsl:variable name="a" select="*[3]"/>
    <m:piece>
      <xsl:apply-templates select="$c"/>
      <xsl:apply-templates select="$a"/>
    </m:piece>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='piece1' and @name='piecewise']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:piecewise>
      <xsl:apply-templates select="$a"/>
    </m:piecewise>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='quant1' and @name='exists']] and *[2][self::om:OMBVAR[count(*)&gt;=1]]]">
    <xsl:variable name="variable" select="*[2]/*[1]"/>
    <xsl:variable name="expression" select="*[3]"/>
    <m:apply>
      <m:exists/>
      <m:bvar>
        <xsl:apply-templates select="$variable"/>
      </m:bvar>
      <xsl:apply-templates select="$expression"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='quant1' and @name='forall']] and *[2][self::om:OMBVAR[count(*)&gt;=1]]]">
    <xsl:variable name="variable" select="*[2]/*[1]"/>
    <xsl:variable name="expression" select="*[3]"/>
    <m:apply>
      <m:forall/>
      <m:bvar>
        <xsl:apply-templates select="$variable"/>
      </m:bvar>
      <xsl:apply-templates select="$expression"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='approx']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:approx/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='eq']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:eq/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='geq']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:geq/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='gt']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:gt/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='leq']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:leq/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='lt']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:lt/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='relation1' and @name='neq']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <m:apply>
      <m:neq/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='setname1' and @name='C']">
    <m:complexes/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='setname1' and @name='N']">
    <m:naturalnumbers/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='setname1' and @name='P']">
    <m:primes/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='setname1' and @name='Q']">
    <m:rationals/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='setname1' and @name='R']">
    <m:reals/>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='setname1' and @name='Z']">
    <m:integers/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='rounding1' and @name='ceiling']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:ceiling/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='rounding1' and @name='floor']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:floor/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='setdiff']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:setdiff/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='union']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:union/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='intersect']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:intersect/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='cartesian_product']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:cartesianproduct/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMS[@cd='set1' and @name='emptyset']">
    <m:emptyset/>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='in']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="A" select="*[3]"/>
    <m:apply>
      <m:in/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$A"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='notin']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="A" select="*[3]"/>
    <m:apply>
      <m:notin/>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$A"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='notprsubset']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:notprsubset/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='notsubset']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:notsubset/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='prsubset']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:prsubset/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='subset']]]">
    <xsl:variable name="A" select="*[2]"/>
    <xsl:variable name="B" select="*[3]"/>
    <m:apply>
      <m:subset/>
      <xsl:apply-templates select="$A"/>
      <xsl:apply-templates select="$B"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=4 and *[1][self::om:OMS[@cd='set1' and @name='set']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="b" select="*[3]"/>
    <xsl:variable name="c" select="*[4]"/>
    <m:set>
      <xsl:apply-templates select="$a"/>
      <xsl:apply-templates select="$b"/>
      <xsl:apply-templates select="$c"/>
    </m:set>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='set1' and @name='size']]]">
    <xsl:variable name="A" select="*[2]"/>
    <m:apply>
      <m:card/>
      <xsl:apply-templates select="$A"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='set1' and @name='suchthat']] and *[3][self::om:OMBIND[count(*)=3 and *[1][self::om:OMS[@cd='fns1' and @name='lambda']] and *[2][self::om:OMBVAR[count(*)=1]]]]]">
    <xsl:variable name="X" select="*[2]"/>
    <xsl:variable name="x" select="*[3]/*[2]/*[1]"/>
    <xsl:variable name="c" select="*[3]/*[3]"/>
    <m:set>
      <m:bvar>
        <xsl:apply-templates select="$x"/>
      </m:bvar>
      <m:condition>
        <xsl:apply-templates select="$c"/>
      </m:condition>
      <xsl:apply-templates select="$x"/>
    </m:set>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='s_data1' and @name='mean']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:mean/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='s_data1' and @name='median']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:median/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='s_data1' and @name='mode']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:mode/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=4 and *[1][self::om:OMS[@cd='s_data1' and @name='moment']]]">
    <xsl:variable name="k" select="*[2]"/>
    <xsl:variable name="c" select="*[3]"/>
    <xsl:variable name="a" select="*[4]"/>
    <m:apply>
      <m:moment/>
      <m:degree>
        <xsl:apply-templates select="$k"/>
      </m:degree>
      <m:momentabout>
        <xsl:apply-templates select="$c"/>
      </m:momentabout>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='s_data1' and @name='sdev']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:sdev/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)&gt;=2 and *[1][self::om:OMS[@cd='s_data1' and @name='variance']]]">
    <xsl:variable name="a" select="*[2]"/>
    <m:apply>
      <m:variance/>
      <xsl:apply-templates select="$a"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='s_dist1' and @name='mean']]]">
    <xsl:variable name="X" select="*[2]"/>
    <m:apply>
      <m:mean/>
      <xsl:apply-templates select="$X"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=4 and *[1][self::om:OMS[@cd='s_dist1' and @name='moment']]]">
    <xsl:variable name="k" select="*[2]"/>
    <xsl:variable name="c" select="*[3]"/>
    <xsl:variable name="X" select="*[4]"/>
    <m:apply>
      <m:moment/>
      <m:degree>
        <xsl:apply-templates select="$k"/>
      </m:degree>
      <m:momentabout>
        <xsl:apply-templates select="$c"/>
      </m:momentabout>
      <xsl:apply-templates select="$X"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='s_dist1' and @name='sdev']]]">
    <xsl:variable name="X" select="*[2]"/>
    <m:apply>
      <m:sdev/>
      <xsl:apply-templates select="$X"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='s_dist1' and @name='variance']]]">
    <xsl:variable name="X" select="*[2]"/>
    <m:apply>
      <m:variance/>
      <xsl:apply-templates select="$X"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arccos']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arccos/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arccosh']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arccosh/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arccot']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arccot/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arccoth']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arccoth/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arccsc']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arccsc/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arccsch']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arccsch/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arcsec']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arcsec/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arcsech']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arcsech/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arcsin']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arcsin/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arcsinh']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arcsinh/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arctan']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arctan/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='arctanh']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:arctanh/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='cos']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:cos/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='cosh']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:cosh/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='cot']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:cot/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='coth']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:coth/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='csc']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:csc/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='csch']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:csch/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='exp']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:exp/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='ln']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:ln/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=3 and *[1][self::om:OMS[@cd='transc1' and @name='log']]]">
    <xsl:variable name="a" select="*[2]"/>
    <xsl:variable name="x" select="*[3]"/>
    <m:apply>
      <m:log/>
      <m:logbase>
        <xsl:apply-templates select="$a"/>
      </m:logbase>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='sec']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:sec/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='sech']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:sech/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='sin']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:sin/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='sinh']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:sinh/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='tan']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:tan/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='transc1' and @name='tanh']]]">
    <xsl:variable name="x" select="*[2]"/>
    <m:apply>
      <m:tanh/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='veccalc1' and @name='curl']]]">
    <xsl:variable name="v" select="*[2]"/>
    <m:apply>
      <m:curl/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='veccalc1' and @name='divergence']]]">
    <xsl:variable name="v" select="*[2]"/>
    <m:apply>
      <m:divergence/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='veccalc1' and @name='grad']]]">
    <xsl:variable name="v" select="*[2]"/>
    <m:apply>
      <m:divergence/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
  <xsl:template match="om:OMA[count(*)=2 and *[1][self::om:OMS[@cd='veccalc1' and @name='Laplacian']]]">
    <xsl:variable name="v" select="*[2]"/>
    <m:apply>
      <m:laplacian/>
      <xsl:apply-templates select="$x"/>
    </m:apply>
  </xsl:template>
</xsl:stylesheet>
