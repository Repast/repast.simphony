lexer grammar NetLogoRGW;
@header {
package relogo.netlogo.code;
}

T26 : 'breed' ;
T27 : '[' ;
T28 : ']' ;
T29 : 'globals' ;
T30 : '__includes' ;
T31 : 'to' ;
T32 : 'end' ;
T33 : 'to-report' ;
T34 : '(' ;
T35 : ')' ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 76
ID  	:  	('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'_')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'+'|'*'|'/'|'<'|'>'|'_'|':'|'0'..'9')* ;
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 77
STRING	:	'"' (~'"' | '\\"')* '"';
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 78
ARITHSYM:	('-'|'+'|'/'|'<'|'>'|'<='|'>='|'*'|'='|'!='|'^')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'+'|'*'|'/'|'<'|'>'|'_'|':'|'0'..'9')*;
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 79
INT 	:	('-'|'+')?('0'..'9')+;
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 80
FLOAT 	:	 ('-'|'+')?('0'..'9')* ('.' '0'..'9'*) ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 82
COMMENT: ';' .* '\n' { $channel = HIDDEN; };
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 83
NEWLINE: '\n' { $channel = HIDDEN; };
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoRGW.g" 84
WS : (' ' |'\t' )+ { $channel = HIDDEN; } ;
