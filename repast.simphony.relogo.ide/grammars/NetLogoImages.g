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
grammar NetLogoImages;

@header {
package relogo.netlogo.image;

import java.awt.Point;
import java.util.LinkedList;
import relogo.netlogo.image.NLImage;
import relogo.netlogo.image.NLImagePrimitive;
import relogo.netlogo.image.NLLinePrimitive;
import relogo.netlogo.image.NLCirclePrimitive;
import relogo.netlogo.image.NLRectanglePrimitive;
import relogo.netlogo.image.NLPolygonPrimitive;
}

@lexer::header {
package relogo.netlogo.image;
}

model returns [List<NLImage> images]	:	is=image_section { $images = $is.images; }
            ;

image_section returns [List<NLImage> images]
	:	{LinkedList<NLImage> list = new LinkedList<NLImage>();} (img=image { list.add($img.img); } )*
	{ $images = list; }
	;
	
image returns [NLImage img]	:
	n=name NEWLINE
        rot=bool NEWLINE
        cc=number NEWLINE
        { LinkedList<NLImagePrimitive> prims=new LinkedList<NLImagePrimitive>(); } 
        (sp=shape_prim {prims.add($sp.prim);} )*
        NEWLINE {$img = new NLImage($n.s, $rot.b, $cc.num, prims); }
	;
	
name returns [String s] :
	{ StringBuffer sb = new StringBuffer(); }
	n=NAME {sb.append($n.text);} ( n1=NAME {sb.append(" "); sb.append($n1.text);} | n2=NUMBER {sb.append(" "); sb.append($n2.text);} )*
	{ $s = sb.toString(); }
	;
	
bool returns [boolean b] :	'true' {$b=true;} | 'false' {$b=false;} ;

shape_prim returns [NLImagePrimitive prim]
	:	'Line' n=number  cc=bool  fr=point  to=point NEWLINE {$prim = new NLLinePrimitive($n.num, false, $cc.b, $fr.pt, $to.pt); }
	|	'Circle'  n=number  ol=bool  cc=bool  ul=point  d=number NEWLINE {$prim = new NLCirclePrimitive($n.num, $ol.b, $cc.b, $ul.pt, $d.num); }
	|	'Rectangle'  n=number  ol=bool  cc=bool  ul=point  lr=point NEWLINE {$prim = new NLRectanglePrimitive($n.num, $ol.b, $cc.b, $ul.pt, $lr.pt); }
	|	'Polygon' { LinkedList<Point> points=new LinkedList<Point>(); }  n=number  ol=bool  cc=bool {} ( pt=point {points.add($pt.pt);} )+ NEWLINE {$prim = new NLPolygonPrimitive($n.num, $ol.b, $cc.b, points); }
	;
	
point returns [Point pt]	:	x=number  y=number {$pt = new Point($x.num,$y.num); };

number returns [int num]: NUMBER {$num = Integer.parseInt($NUMBER.text);} ;

NUMBER 	:	('-')? ( '0' .. '9' )+ ;

NAME
    :	( 'a' .. 'z' | 'A' .. 'Z' )
        ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
    ;
    
SECT 	:	'@#$#@#$#@' ('\r')? '\n';

WS	:	(' ' | '\t')+ { $channel = HIDDEN; };

NEWLINE	:	('\r')? '\n' ;

