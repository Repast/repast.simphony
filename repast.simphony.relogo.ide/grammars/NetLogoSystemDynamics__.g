lexer grammar NetLogoSystemDynamics;
@header {
package relogo.netlogo.dynamics;
}

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoSystemDynamics.g" 42
ID  	:  	(('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'_')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'_'|':'|'0'..'9')*)
                ('.'('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'_')('a'..'z'|'A'..'Z'|'?'|'!'|'#'|'$'|'%'|'-'|'_'|':'|'0'..'9')*)* ;
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoSystemDynamics.g" 44
STRING	:	'"' (~'"' | '\\"')* '"';
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoSystemDynamics.g" 45
INT 	:	('-'|'+')?('0'..'9')+;
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoSystemDynamics.g" 46
FLOAT 	:	 ('-'|'+')?('0'..'9')* ('.' '0'..'9'*) ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoSystemDynamics.g" 48
NEWLINE	:	('\r')? '\n' ;
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoSystemDynamics.g" 49
WS : (' ' |'\t' )+;
