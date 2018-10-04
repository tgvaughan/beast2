grammar Nexus;

// Parser

nexus: '#' NEXUS block* ;

block:
    BEGIN name=IDENTIFIER ';'
    command*
    END ';'
    ;


command:
    name=IDENTIFIER args=(ANYTHING|IDENTIFIER)* ';'
    ;

// Lexer

fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

fragment DIGIT: [0-9] ;
fragment LETTER: [a-zA-Z];

NEXUS: N E X U S ;
BEGIN: B E G I N ;
END: E N D ;

IDENTIFIER: (LETTER | '_')+ (LETTER | DIGIT | '_' | '.')* ;

COMMENT : '[' (~[&%\\/] .*?)? ']' -> skip ;
WHITESPACE : [ \t\r\n]+ -> skip ;

ANYTHING: ~[;]+?;
