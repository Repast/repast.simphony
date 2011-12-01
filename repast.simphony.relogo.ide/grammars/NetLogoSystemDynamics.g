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
grammar NetLogoSystemDynamics;

options {
output=AST;
ASTLabelType=CommonTree;
}

tokens {
MODEL;
ENTRY;
REF;
STR;
INTG;
FLT;
INDENT;
}

@header {
package relogo.netlogo.dynamics;
}

@lexer::header {
package relogo.netlogo.dynamics;
}

file	:	mdl+=model+ -> $mdl*;

model	:	f=FLOAT NEWLINE ent+=entry* -> ^(MODEL $f $ent*);

entry	:	w=wsp cls=ID (WS (args+=ref | args+=str | args+=intg | args+=fltgt))* NEWLINE -> ^(ENTRY $w $cls $args*);

ref	:	a1=ID -> ^(REF $a1);

str	:	a2=STRING -> ^(STR $a2);

intg	:	a3=INT -> ^(INTG $a3);

fltgt	:	a4=FLOAT -> ^(FLT $a4);

wsp	:	ws=WS -> ^(INDENT $ws);

ID  	:  	(('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'_')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'_'|':'|'0'..'9')*)
                ('.'('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'_')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'_'|':'|'0'..'9')*)* ;
STRING	:	'"' (~'"' | '\\"')* '"';
INT 	:	('-'|'+')?('0'..'9')+;
FLOAT 	:	 ('-'|'+')?('0'..'9')* ('.' '0'..'9'*) ;

NEWLINE	:	('\r')? '\n' ;
WS : (' ' |'\t' )+;
