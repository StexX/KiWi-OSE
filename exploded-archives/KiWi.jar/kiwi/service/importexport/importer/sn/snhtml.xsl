<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xfm="http://www.w3.org/2002/xforms"
	xmlns="http://www.w3.org/1999/xhtml" version="2.0">

	<xsl:output method="xhtml" encoding="UTF-8" indent="no"
		omit-xml-declaration="yes" />

	<xsl:template match="/SNXML">
		<div xmlns:kiwi="http://www.kiwi-project.eu/kiwi/html/" kiwi:type="page">
			<xsl:apply-templates select="//INHALT" />
		</div>
	</xsl:template>

	<xsl:template match="ARTIKEL">
		<xsl:apply-templates />
	</xsl:template>

    <xsl:template match="TITEL"/>

	<xsl:template match="INHALT">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="UNTERZEILE">
		<h3>
			<xsl:apply-templates />
		</h3>
	</xsl:template>

	<xsl:template match="VORSPANN">
		<p>
			<em>
				<xsl:apply-templates />
			</em>
		</p>
	</xsl:template>

	<xsl:template match="P">
		<p>
			<xsl:apply-templates />
		</p>
	</xsl:template>

	<xsl:template match="ABSATZ">
		<p>
			<xsl:apply-templates />
		</p>
	</xsl:template>


</xsl:stylesheet>