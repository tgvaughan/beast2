grammar Nexus;

// Parser

nexus: '#' NEXUS block* ;

block:
    BEGIN
    ( taxa_block
    | trees_block
    | unknown_block)
    END ';' ;

// Trees blocks

trees_block: TREES ';'
    translate_command?  tree_command*;

translate_command : TRANSLATE translate_args ';' ;
translate_args : any+ ;
tree_command : TREE tree_name '=' tree_type? tree_string ';' ;
tree_name : any ;
tree_type : '[&' . ']' ;
tree_string : any+? ;

// Taxa blocks

taxa_block: TAXA ';' dimensions_command? taxlabels_command?;

dimensions_command : DIMENSIONS NTAX '=' taxcount ';' ;
taxcount: NUMBER ;
taxlabels_command : TAXLABELS taxon_name+? ';' ;
taxon_name : any;

// Unhandled blocks

data_block: (DATA|CHARACTERS) ';' command*;
calibration_block: CALIBRATION  ';'command*;
assumptions_block: (ASSUMPTIONS|SETS|MRBAYES) ';' command*;

unknown_block:
    name=. ';'
    command*
    ;

command:
    command_name command_args ';'
    ;
command_name: ~(SEMI | END | BEGIN);
command_args: any*;

any: ~SEMI;

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

SEMI: ';' ;
EQ: '=' ;
QUOTE: '"' ;
DOUBLEQUOTE: '\'' ;

NEXUS: N E X U S ;
BEGIN: B E G I N ;
END: E N D ;

DATA: D A T A;
CHARACTERS: C H A R A C T E R S;
CALIBRATION: C A L I B R A T I O N;
ASSUMPTIONS: A S S U M P T I O N S;
SETS: S E T S;
MRBAYES: M R B A Y E S;
TAXA: T A X A;
TREES: T R E E S;
TREE: T R E E;
TRANSLATE: T R A N S L A T E;
DIMENSIONS: D I M E N S I O N S;
NTAX: N T A X;
TAXLABELS: T A X L A B E L S;


IDENTIFIER: (LETTER | '_') (LETTER | DIGIT | '_' | '.')* ;
fragment STRING1: '"' ('\\"' | .)*? '"';
fragment STRING2: '\'' ('\\\'' | .)*? '\'';
STRING : STRING1 | STRING2 ;

NUMBER: DIGIT+;
COMMENT : '[' (~[&%\\/] .*?)? ']' -> skip ;
WHITESPACE : (' ' | '\t' | '\n' | '\r')+ -> skip ;

ANYTHING: ~[;];
