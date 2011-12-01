/*
 * Copyright (c) 2009 The MITRE Corporation. All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the MITRE Corporation nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * NOTICE
 * 
 * This software was produced for the U. S. Government
 * under Contract No. W15P7T-09-C-F600, and is
 * subject to the Rights in Noncommercial Computer Software
 * and Noncommercial Computer Software Documentation
 * Clause 252.227-7014 (JUN 1995)
 * 
 * Copyright 2009 The MITRE Corporation. All Rights Reserved. */
grammar NetLogoInterface;

@header {
package relogo.netlogo.intf;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
}

@lexer::header {
package relogo.netlogo.intf;
}

control_section returns [List<NLControl> controls]
	:	{LinkedList<NLControl> list = new LinkedList<NLControl>();} (img=control { list.add($img.img); } )*
	{ $controls = list; }
	;
	
control returns [NLControl img]	:
		'TEXTBOX' NEWLINE bb=bounding_box t=anything (unk_t1=anything)* NEWLINE {$img=new NLTextbox($bb.rect, $t.text);}
	|	'SLIDER' NEWLINE bb=bounding_box label=anything variable=anything {double minimum=0.0, maximum=0.0, sliderStep=1.0; Number value=null;} (min=number {minimum=$min.num;} NEWLINE | fmin=floatnum {minimum=$fmin.num;} NEWLINE) (max=number {maximum=$max.num;} NEWLINE | fmax=floatnum {maximum=$fmax.num;} NEWLINE | anything {/* expression! */}) (val=number {value=$val.num;} NEWLINE | fval=floatnum {value=$fval.num;} NEWLINE) (step=number {sliderStep=$step.num;} NEWLINE | fstep=floatnum {sliderStep=$fstep.num;} NEWLINE) unk1=number NEWLINE units=anything (orientation=anything) NEWLINE  {$img=new NLSlider($bb.rect, $label.text, $variable.text, minimum, maximum, value, sliderStep, $unk1.num, $units.text);}
	|	'BUTTON' {String updateText=null, actionKeyText=null;} NEWLINE bb=bounding_box label=anything commands=anything doforever=name NEWLINE unk1=number NEWLINE unk2=name NEWLINE agent=name NEWLINE (update=anything {updateText = $update.text;} (actionkey=anything {actionKeyText = $actionkey.text;} (unk21=anything unk22=anything)?)?)? NEWLINE {$img=new NLButton($bb.rect, $label.text, $commands.text, $doforever.s, $unk1.num, $unk2.s, $agent.s, updateText, actionKeyText);}
	|	'CHOOSER' NEWLINE bb=bounding_box label=anything variable=anything {LinkedList<String> list = new LinkedList<String>();}(qt=quote  { list.add($qt.q); } | n=name  { list.add($n.s); } | num1=number  { list.add($num1.text); } | fnum=floatnum  { list.add($fnum.text); })+ NEWLINE first=number NEWLINE NEWLINE {$img=new NLChooser($bb.rect, $label.text, $variable.text, list, $first.num);}
	|	'SWITCH' NEWLINE bb=bounding_box label=anything variable=anything val1=number NEWLINE val2=number NEWLINE val3=number NEWLINE NEWLINE {$img=new NLSwitch($bb.rect, $label.text, $variable.text, $val1.num, $val2.num, $val3.num);}
	|	'OUTPUT' NEWLINE bb=bounding_box (anything)* NEWLINE {$img=new NLOutput($bb.rect);}
	|	'PLOT' NEWLINE bb=bounding_box title=anything x=anything y=anything xmin=floatnum NEWLINE xmax=floatnum NEWLINE ymin=floatnum NEWLINE ymax=floatnum NEWLINE autoplot=bool NEWLINE showLegend=bool NEWLINE {LinkedList penList=new LinkedList();} ('PENS' NEWLINE (p=pen {penList.add($p.pen);} )*)? NEWLINE {$img=new NLPlot($bb.rect, $title.text, $x.text, $y.text, $xmin.num, $xmax.num, $ymin.num, $ymax.num, $autoplot.b, $showLegend.b, penList);}
	|	'MONITOR' NEWLINE bb=bounding_box label=anything variable=anything dplaces=number NEWLINE unk1=number NEWLINE (unk_m2=anything)? NEWLINE {$img=new NLMonitor($bb.rect, $label.text, $variable.text, $dplaces.num, $unk1.num);}
	|	'INPUTBOX' NEWLINE bb=bounding_box variable=anything initial=anything  
		( (bool | number) NEWLINE (bool | number) NEWLINE)? (type=anything)?
		 NEWLINE {$img=new NLInputBox($bb.rect, $variable.text, $initial.text);}
	|	'CC-WINDOW' NEWLINE bb=bounding_box t=anything {int unkNum=-1;} (unk1=number {unkNum=$unk1.num;} NEWLINE)? NEWLINE {$img=new NLCommandConsole($bb.rect, $t.text, unkNum);}
	|	'GRAPHICS-WINDOW' NEWLINE bb=bounding_box number NEWLINE number NEWLINE floatnum NEWLINE 
		number NEWLINE number NEWLINE number NEWLINE number NEWLINE (number NEWLINE)* (name NEWLINE)? NEWLINE {$img=new NLGraphicsWindow($bb.rect);}
	;
	
bounding_box returns [Rectangle rect] :
	x1=number NEWLINE y1=number NEWLINE x2=number NEWLINE y2=number NEWLINE {$rect=new Rectangle($x1.num, $y1.num,($x2.num-$x1.num),($y2.num-$y1.num));}
	;

pen returns [NLPen pen]	:	q=QUOTATION f=floatnum n1=number rgb=number showInLegend=bool NEWLINE {$pen=new NLPen($q.text, $f.num, $n1.num, $rgb.num, $showInLegend.b);} ;
	
anything:	(~NEWLINE)+ NEWLINE;

name returns [String s] :
	{ StringBuffer sb = new StringBuffer(); }
	(n=NAME {sb.append($n.text);}) ((n1=NAME {sb.append(" "); sb.append($n1.text);}) | (n2=NUMBER {sb.append(" "); sb.append($n2.text);} ))*
	{ $s = sb.toString(); }
	;
	
quote returns [String q]: QUOTATION {$q = $QUOTATION.text;} ;

bool returns [boolean b] :	'true' {$b=true;} | 'false' {$b=false;} ;

point returns [Point pt]	:	x=number  y=number {$pt = new Point($x.num,$y.num); };

number returns [int num]: NUMBER {$num = Integer.parseInt($NUMBER.text);} ;

floatnum returns [double num]: FLOAT {$num = Double.parseDouble($FLOAT.text);} ;

GRAPHIC_CHAR
	:	'!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '(' | ')' | '-' | '_' | '+' | '=' | '{' | '[' | '}' | ']' | ':' | ';' | '\'' | '"' | ',' | '.' | '<' | '>' | '/' | '?' | '\\' | '|' | '~' | '`';
	
NUMBER 	:	('-')? ( '0' .. '9' )+ ;

FLOAT 	:	('-')? ( '0' .. '9' )* '.' ('0' .. '9')* ('E' ('-')? ('0' .. '9')+ )?;

QUOTATION
	:	'"' (~'"')* '"';

NAME
    :	( 'a' .. 'z' | 'A' .. 'Z' )
        ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
    ;
    
WS	:	(' ' | '\t')+ { $channel = HIDDEN; };

NEWLINE	:	('\r')? '\n' ;

