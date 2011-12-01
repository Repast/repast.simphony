lexer grammar NetLogoInterface;
@header {
package relogo.netlogo.intf;
}

T11 : 'TEXTBOX' ;
T12 : 'SLIDER' ;
T13 : 'BUTTON' ;
T14 : 'CHOOSER' ;
T15 : 'SWITCH' ;
T16 : 'OUTPUT' ;
T17 : 'PLOT' ;
T18 : 'PENS' ;
T19 : 'MONITOR' ;
T20 : 'INPUTBOX' ;
T21 : 'CC-WINDOW' ;
T22 : 'GRAPHICS-WINDOW' ;
T23 : 'true' ;
T24 : 'false' ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 61
GRAPHIC_CHAR
	:	'!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '(' | ')' | '-' | '_' | '+' | '=' | '{' | '[' | '}' | ']' | ':' | ';' | '\'' | '"' | ',' | '.' | '<' | '>' | '/' | '?' | '\\' | '|' | '~' | '`';
	
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 64
NUMBER 	:	('-')? ( '0' .. '9' )+ ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 66
FLOAT 	:	('-')? ( '0' .. '9' )* '.' ('0' .. '9')* ('E' ('-')? ('0' .. '9')+ )?;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 68
QUOTATION
	:	'"' (~'"')* '"';

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 71
NAME
    :	( 'a' .. 'z' | 'A' .. 'Z' )
        ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
    ;
    
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 76
WS	:	(' ' | '\t')+ { $channel = HIDDEN; };

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoInterface.g" 78
NEWLINE	:	('\r')? '\n' ;

