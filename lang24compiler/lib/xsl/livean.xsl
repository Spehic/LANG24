<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="livean">
  <html>
    <style>
      table, tr, td {
      text-align: center;
      vertical-align: top;
      }
    </style>
    <body>
      <table>
	<xsl:apply-templates select="code"/>
      </table>
     </body>
  </html>
</xsl:template>

<xsl:template match="code">
  <td bgcolor="FFEE00">
    <table>
      <tr>
	<td bgcolor="EECF00">
	  <xsl:apply-templates select="frame"/>
	</td>
      </tr>
      <tr>
	<td bgcolor="EECF00">
	  <nobr>
	    body=<xsl:value-of select="@body"/>
	    epilogue=<xsl:value-of select="@epilogue"/>
	    #temps=<xsl:value-of select="@tempsize"/>
	  </nobr>
	</td>
      </tr>
      <xsl:apply-templates select="instructions"/>
    </table>
  </td>
</xsl:template>

<xsl:template match="instructions">
  <xsl:apply-templates select="instruction"/>
</xsl:template>

<xsl:template match="instruction">
  <tr></tr>
  <tr>
    <td bgcolor="00BBFF">
      <xsl:value-of select="@code"/>
    </td>
    <xsl:apply-templates select="temps"/>
  </tr>
</xsl:template>

<xsl:template match="temps">
  <tr bgcolor="00DDFF">
    <td>
      <xsl:value-of select="@name"/>:
      <xsl:apply-templates select="temp"/>
    </td>
  </tr>
</xsl:template>

<xsl:template match="temp">
  <xsl:text> </xsl:text>
  <xsl:value-of select="@name"/>
</xsl:template>

<xsl:template match="frame">
  <table width="100%">
    <tr>
      <td>
	<nobr>
	  FRAME
	  label=<font style="font-family:courier new"><xsl:value-of select="@label"/></font>
	</nobr>
      </td>
    </tr>
    <tr>
      <td>
	<nobr>
	  depth=<xsl:value-of select="@depth"/> 
	  size=<xsl:value-of select="@size"/> 
	  locs=<xsl:value-of select="@locssize"/>
	  args=<xsl:value-of select="@argssize"/>
	</nobr>
      </td>
    </tr>
    <tr>
      <td>
	<nobr>
    	  FP=<xsl:value-of select="@FP"/>
	  RV=<xsl:value-of select="@RV"/>
	</nobr>
      </td>
    </tr>
  </table>
</xsl:template>

<xsl:template match="frame">
  <table width="100%">
    <tr>
      <td>
	<nobr>
	  FRAME
	  label=<font style="font-family:courier new"><xsl:value-of select="@label"/></font>
	</nobr>
      </td>
    </tr>
    <tr>
      <td>
	<nobr>
	  depth=<xsl:value-of select="@depth"/> 
	  size=<xsl:value-of select="@size"/> 
	  locs=<xsl:value-of select="@locssize"/>
	  args=<xsl:value-of select="@argssize"/>
	</nobr>
      </td>
    </tr>
    <tr>
      <td>
	<nobr>
    	  FP=<xsl:value-of select="@FP"/>
	  RV=<xsl:value-of select="@RV"/>
	</nobr>
      </td>
    </tr>
  </table>
</xsl:template>

</xsl:stylesheet>
