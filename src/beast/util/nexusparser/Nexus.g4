grammar Nexus;

// Parser

nexus: NEXUS_START block* ;

block:
    taxa_block
    | assumptions_block
    | characters_block
    | data_block
    | trees_block
    | other_block
    ;

taxa_block: BEGIN TAXA ';' command* END ';' ;

assumptions_block: BEGIN ASSUMPTIONS ';' command* END ';' ;

characters_block: BEGIN CHARACTERS ';' (dimensions_command | command)* END ';' ;
data_block: BEGIN DATA ';' (dimensions_command | command)* END ';' ;
dimensions_command: DIMENSIONS NEWTAXA? (NTAX '=' ntax=INT)? NCHAR '=' nchar=INT ';' ;

trees_block: BEGIN TREES ';' command* END ';' ;

other_block: BEGIN WORDSTRING ';' command* END ';' ;

command: command_name=word ((key=word '=' val=word) | word)* ';' ;

word : INT | WORDSTRING ;

// Lexer

SEMI: ';' ;
EQ: '=' ;

NEXUS_START: '#' N E X U S ;
BEGIN: B E G I N;
END: E N D;
DATA: D A T A;
TAXA: T A X A;
CHARACTERS: C H A R A C T E R S;
ASSUMPTIONS: A S S U M P T I O N S;
TREES: T R E E S;

DIMENSIONS: D I M E N S I O N S;
NTAX: N T A X;
NCHAR: N C H A R;
NEWTAXA: N E W T A X A;

INT : DIGIT DIGIT*;
fragment DIGIT: [0-9];

WORDSTRING: WCHAR WCHAR*
    | '"' .*? '"'
    | '\'' .*? '\'' ;
fragment WCHAR: [a-zA-Z0-9_\-?];

COMMENT : '[' .*? ']' -> skip ;
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



