lexer grammar NetLogoImages;
@header {
package relogo.netlogo.image;
}

T9 : 'true' ;
T10 : 'false' ;
T11 : 'Line' ;
T12 : 'Circle' ;
T13 : 'Rectangle' ;
T14 : 'Polygon' ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoImages.g" 56
NUMBER 	:	('-')? ( '0' .. '9' )+ ;

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoImages.g" 58
NAME
    :	( 'a' .. 'z' | 'A' .. 'Z' )
        ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
    ;
    
// $ANTLR src "C:\projects\netlogo\grammars\NetLogoImages.g" 63
SECT 	:	'@#$#@#$#@' ('\r')? '\n';

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoImages.g" 65
WS	:	(' ' | '\t')+ { $channel = HIDDEN; };

// $ANTLR src "C:\projects\netlogo\grammars\NetLogoImages.g" 67
NEWLINE	:	('\r')? '\n' ;

