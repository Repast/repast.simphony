<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:score="http://scoreabm.org/score"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="score xsi">

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:template match="/">
        <xsl:apply-templates select="score:SContext" />
    </xsl:template>
  
  <xsl:template name="SContext" match="score:SContext">
    
    <context>
        <xsl:attribute name="id">
            <xsl:value-of select="@label"/>
        </xsl:attribute>
        <xsl:apply-templates/>
    </context>
  </xsl:template>
  
  <xsl:template match="agents">
    <xsl:if test="@xsi:type='score:SContext'">
        <xsl:call-template name="SContext"/>
    </xsl:if>
   </xsl:template>
  
  <xsl:template match="valueLayers">
    <projection>
        <xsl:attribute name="id">
            <xsl:value-of select="@ID"/>
        </xsl:attribute>
        
        <xsl:attribute name="type">value layer</xsl:attribute>
    </projection>
   </xsl:template>
  
  
  <xsl:template match="projections">
    <projection>
        <xsl:attribute name="id">
            <xsl:value-of select="@ID"/>
        </xsl:attribute>
        
        <xsl:if test="@xsi:type='score:SGrid'">
            <xsl:attribute name="type">grid</xsl:attribute>
            <xsl:call-template name="borderRule" />
            <xsl:if test="@multiOccupant">
            <attribute>
                <xsl:attribute name="id">allows multi</xsl:attribute>
                <xsl:attribute name="value">
                    <xsl:value-of select="@multiOccupant"/>
                </xsl:attribute>
                <xsl:attribute name="type">boolean</xsl:attribute>
            </attribute>
            </xsl:if>
        </xsl:if>
        <xsl:if test="@xsi:type='score:SContinuousSpace'">
            <xsl:attribute name="type">continuous space</xsl:attribute>
            <xsl:call-template name="borderRule" />
        </xsl:if>
        <xsl:if test="@xsi:type='score:SNetwork'">
            <xsl:attribute name="type">network</xsl:attribute>
            <xsl:if test="@directed">
            <attribute>
                <xsl:attribute name="id">directed</xsl:attribute>
                <xsl:attribute name="value">
                    <xsl:value-of select="@directed"/>
                </xsl:attribute>
                
                <xsl:attribute name="type">boolean</xsl:attribute>
                
            </attribute>
            </xsl:if>
        </xsl:if>
        <xsl:if test="@xsi:type='score:SGeography'">
            <xsl:attribute name="type">geography</xsl:attribute>
        </xsl:if>
        <xsl:apply-templates/>
        
    </projection>
   </xsl:template>
  
  <xsl:template name="borderRule">
        <xsl:if test="@borderRule">
        <attribute>
            <xsl:attribute name="id">border rule</xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="@borderRule"/>
            </xsl:attribute>
            <xsl:attribute name="type">string</xsl:attribute>
        </attribute>
        </xsl:if>
   </xsl:template>
  

  <xsl:template match="score:SContext/attributes">
    <xsl:call-template name="attribute" />
   </xsl:template>

  
  <xsl:template match="projections/attributes">
    <xsl:call-template name="attribute" />
   </xsl:template>
  
  <!-- id, display_name, value, type -->
  <xsl:template name="attribute">
    <xsl:if test="@xsi:type = false()">
        <attribute>
            <xsl:attribute name="id">
                <xsl:value-of select="@ID"/>
            </xsl:attribute>
            <xsl:attribute name="display_name">
                <xsl:value-of select="@label"/>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="@defaultValue"/>
            </xsl:attribute>
            
            <xsl:attribute name="type">
                <xsl:if test="@sType='INTEGER'">int</xsl:if>
                <xsl:if test="@sType='BOOLEAN'">boolean</xsl:if>
                <xsl:if test="@sType='FLOAT'">double</xsl:if>
                <xsl:if test="@sType='STRING'">string</xsl:if>
                <xsl:if test="@sType='LONG'">long</xsl:if>
                <xsl:if test="@sType='FILE'">java.io.File</xsl:if>
            </xsl:attribute>
        </attribute>
            
    </xsl:if>
   
   </xsl:template>
  
 
  
</xsl:stylesheet>