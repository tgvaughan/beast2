grammar Nexus;

// Parser

nexus: NEXUS_START block* ;

block:
    taxa_block
    | assumptions_block
    | characters_block
    | data_block
    | other_block
    ;

taxa_block: BEGIN TAXA ';' command* END ';' ;

assumptions_block: BEGIN ASSUMPTIONS ';' command* END ';' ;

characters_block: BEGIN CHARACTERS ';' command* END ';' ;

data_block: BEGIN DATA ';' command* END ';' ;

other_block: BEGIN WORD ';' command* END ';' ;

command: WORD WORD* ';' ;

// Lexer


// Keywords

NEXUS_START: '#' N E X U S ;
BEGIN: B E G I N;
END: E N D ;
TAXA: T A X A;
ASSUMPTIONS: A S S U M P T I O N S;
CHARACTERS: C H A R A C T E R S ;
DATA: D A T A;

SEMI: ';' ;

WORD: [a-zA-Z0-9] [a-zA-Z0-9]*
    | '"' [^"]* '"'
    | '\'' [^']* '\'' ;

WHITESPACE : [ \t\r\n]+ -> skip ;

// Fragments used for case-insensitive keyword lexing

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');
