grammar Nexus;

// Parser

nexus: NEXUS_START block* ;

block: other_block ;

other_block: BEGIN WORD ';' command* END ';' ;

command: WORD WORD* ';' ;

// Lexer

NEXUSSTART: '#nexus' ;

BEGIN: [bB][eE][gG][iI][nN] ;
END: [eE][nN][dD] ;

SEMI: ';' ;

WORD: [a-zA-Z0-9] [a-zA-Z0-9]*
    | '"' [^"]* '"'
    | '\'' [^']* '\'' ;

WHITESPACE : [ \t\r\n]+ -> skip ;
