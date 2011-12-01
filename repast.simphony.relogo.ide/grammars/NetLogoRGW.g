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
grammar NetLogoRGW ;

options {
output=AST;
ASTLabelType=CommonTree;
backtrack=true;
}

tokens {
ARGS;
CS;
CB;
IDN;
STR;
BREED;
GLOBALS;
INCLUDES;
INUM;
FNUM;
OWN;
ToEndBlock;
ToReportBlock;
Paren;
}

@header {
package relogo.netlogo.code;
}

@lexer::header {
package relogo.netlogo.code;
}


prog	:	stat* ;//-> $a*;
// one way to inline how the AST is output, not modular, and needs corresponding change in Test.java test rig
//(stat {if ($stat.tree.toStringTree() != "nil") System.out.println($stat.tree.toStringTree());} )+;

stat: 'breed' '[' a=ID b=ID? ']'  -> ^(BREED $a $b*)
	|	'globals' codeBlock  -> ^(GLOBALS codeBlock)
	|	'__includes' codeBlock -> ^(INCLUDES codeBlock)
	|	a=ID codeBlock  -> ^(OWN $a codeBlock)
	| 	toEndBlock -> toEndBlock
	| 	toReportBlock -> toReportBlock
;

toEndBlock
	:	'to' a=ID ('[' (args+=codeElement)+ ']')?  (c+=codeLine)* 'end' ->
	             ^(ToEndBlock $a ^(ARGS $args*) $c*)
;

toReportBlock
	:	'to-report' a=ID ('[' (args+=codeElement)+ ']')? (c+=codeLine)*  'end' ->
	             ^(ToReportBlock $a ^(ARGS $args*) $c*)
;

codeLine:	(a+=codeElement | a+=codeBlock)+ -> ^(CS $a*);

codeBlock
	:	'[' (a+=codeElement | a+=codeBlock)* ']' -> ^(CB $a*)
	;

codeElement
	:	'(' (a+=codeElement | a+=codeBlock)+ ')' -> ^(Paren  $a* )
	|	(i=ID | i='breed' | i='globals' | i='to' | i='to-report') -> ^(IDN $i)
	|	(str=STRING) -> ^(STR $str)
	| 	ar=ARITHSYM -> ^(IDN $ar)
	|	ni=INT -> ^(INUM $ni)
	|	nf=FLOAT -> ^(FNUM $nf);

/* Atom definition is primarily for my convenience. NetLogo typically doesn't care much about
 * token types, so you can get badly-behaved tokens like 'color->agent' or '-s' which look like
 * they contain operators. I have tried to walk a middle road to capture the ill-behaved cases
 * while still making it simple to parse. */

ID  	:  	('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'_')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'+'|'*'|'/'|'<'|'>'|'_'|':'|'0'..'9')* ;
STRING	:	'"' (~'"' | '\\"')* '"';
ARITHSYM:	('-'|'+'|'/'|'<'|'>'|'<='|'>='|'*'|'='|'!='|'^')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'+'|'*'|'/'|'<'|'>'|'_'|':'|'0'..'9')*;
INT 	:	('-'|'+')?('0'..'9')+;
FLOAT 	:	 ('-'|'+')?('0'..'9')* ('.' '0'..'9'*) ;

COMMENT: ';' .* '\n' { $channel = HIDDEN; };
NEWLINE: '\n' { $channel = HIDDEN; };
WS : (' ' |'\t' )+ { $channel = HIDDEN; } ;
