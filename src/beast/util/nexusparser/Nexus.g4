grammar Nexus;

// Parser

nexus: '#nexus' block* ;

block: 'begin' block_declaration 'end' ';' ;

block_declaration:
    block_taxa
    | block_characters
    | block_unaligned
    | block_distances
    | block_data
    | block_codons
    | block_sets
    | block_assumptions
    | block_trees
    | block_notes
    | block_unknown
    ;


/*----------------------------------------------------------------------*/
/*               TAXA                                                   */
/*----------------------------------------------------------------------*/
    
block_taxa:
    'taxa' ';'
    'dimensions' 'ntax' '=' INT ';'
    taxlabels
    ;

 /*----------------------------------------------------------------------*/
/*               CHARACTERS                                             */
/*----------------------------------------------------------------------*/

 block_data:
    'data' ';'
       'dimensions' newtaxa 'nchar' '=' INT ';'
       format_characters
       options_data
       eliminate
       taxlabels_optional
       charstate
       'matrix' matrix_data ';'
       ;

format_characters:
    'format' format_characters_item format_characters_item* ';'
    ;

format_characters_item:
  | 'gap' '=' character_symbol
  | 'matchchar' '=' matchchar_symbol
  | 'transpose'
  | 'items' '=' item_value
  | 'datatype' '=' IDENTIFIER
  | 'respectcase'
  | 'interleave'
  | 'statesformat' '=' statesformat_option
  | missing
  | symbols
  | equate
  | labels
  | tokens
  ;


/* ----------------------------------------------------------------------------
   Individual rules used by various commands or blocks
   ---------------------------------------------------------------------------- */

newtaxa: 'newtaxa' ntax
    | ntax ;

ntax: 'ntax' '=' INT ;

taxlabels:
    'taxlabels' IDENTIFIER IDENTIFIER* ';' ;

// Lexer

SEMI: ';' ;
EQ: '=' ;

INT : DIGIT DIGIT*;

IDENTIFIER: (LETTER | '_')+ (LETTER | DIGIT | '_' | '.')* ;
fragment WCHAR: [a-zA-Z0-9_\\-?];

fragment DIGIT: [0-9];
fragment LETTER: [a-zA-Z];

STRING: '"' .*? '"' | '\'' .*? '\'' ;

COMMENT : '[' .*? ']' -> skip ;
WHITESPACE : [ \t\r\n]+ -> skip ;
