grammar ImperativeCompConst;

SEMICOLON: ';';
VARSYM: 'var';
COLON: ':';
INT_TYPE: 'int';
REAL_TYPE: 'real';
BOOL_TYPE: 'bool';
RECORD_TYPE: 'record';
ARRAY_TYPE: 'array';
TYPESYM: 'type';
ISSYM: 'is';
ROUTINESYM: 'routine';
ENDSYM: 'end';
LPAREN: '(';
RPAREN: ')';
LBRACKET: '[';
RBRACKET: ']';
LBRACE: '{';
RBRACE: '}';
COMMA: ',';
WHILESYM: 'while';
FORSYM: 'for';
FOREACHSYM: 'foreach';
FROMSYM: 'from';
LOOPSYM: 'loop';
INSYM: 'in';
RANGE: '..';
IFSYM: 'if';
THENSYM: 'then';
ELSESYM: 'else';
AND: 'and';
OR: 'or';
XOR: 'xor';
LSS: '<';
LEQ: '<=';
GTR: '>';
GEQ: '>=';
EQL: '=';
NEQ: '/=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
PERIOD: '.';
BECOMES: ':=';
TRUE: 'true';
FALSE: 'false';
RETURNSYM: 'return';
REVERSESYM: 'reverse';

IDENT: [a-zA-Z][a-zA-Z0-9_]*;
INT: [0-9]+;
REAL: [0-9]+[.][0-9]+;

WS: [ \t\n\r]+ -> skip;

input:
    /* empty */
    | input program
;

program: simple_declaration
    | routine_declaration
    | statement
;

simple_declaration: variable_declaration
    | type_declaration
    | array_variable_declaration
    | record_variable_declaration
;

variable_declaration: VARSYM IDENT ISSYM expression SEMICOLON
    | VARSYM IDENT COLON type SEMICOLON
    | VARSYM IDENT COLON type ISSYM expression SEMICOLON
;

array_variable_declaration: VARSYM IDENT COLON type ISSYM LBRACKET expressions RBRACKET SEMICOLON;

expressions: expression
    | expressions COMMA expression
;

record_variable_declaration: VARSYM IDENT COLON type ISSYM LBRACE record_field_assignments RBRACE SEMICOLON;

record_field_assignments: IDENT ISSYM expression SEMICOLON
    | record_field_assignments IDENT ISSYM expression SEMICOLON
;

type_declaration: TYPESYM IDENT ISSYM type SEMICOLON;

routine_declaration: ROUTINESYM IDENT LPAREN parameter_declarations RPAREN COLON type ISSYM body ENDSYM SEMICOLON
    | ROUTINESYM IDENT LPAREN parameter_declarations RPAREN ISSYM body ENDSYM SEMICOLON
    | ROUTINESYM IDENT LPAREN RPAREN COLON type ISSYM body ENDSYM SEMICOLON
    | ROUTINESYM IDENT LPAREN RPAREN ISSYM body ENDSYM SEMICOLON
;

parameter_declarations: IDENT COLON type
    | parameter_declarations COMMA IDENT COLON type
;

type: record_type
    | array_type
    | primitive_type
    | IDENT
;

primitive_type: INT_TYPE
    | REAL_TYPE
    | BOOL_TYPE
   ;

record_type: RECORD_TYPE LBRACE record_variable_declarations RBRACE ENDSYM;

record_variable_declarations: VARSYM IDENT COLON type SEMICOLON
    | record_variable_declarations VARSYM IDENT COLON type SEMICOLON
    ;

array_type: ARRAY_TYPE LBRACKET expression RBRACKET type;

body: simple_declaration
    | statement
    | return_expression
    | body simple_declaration
    | body statement
    | body return_expression
    ;

return_expression: RETURNSYM expression SEMICOLON;

statement: assignment SEMICOLON
    | routine_call SEMICOLON
    | while_loop SEMICOLON
    | for_loop SEMICOLON
    | foreach_loop SEMICOLON
    | if_statement SEMICOLON
    ;

assignment: modifiable_primary BECOMES expression
    | modifiable_primary BECOMES routine_call
    ;

routine_call: IDENT LPAREN routine_call_arguments RPAREN
    | IDENT LPAREN RPAREN
    ;

routine_call_arguments: expression
    | routine_call_arguments COMMA expression
    ;

while_loop: WHILESYM expression LOOPSYM body ENDSYM
;

for_loop: FORSYM IDENT range LOOPSYM body ENDSYM
;

foreach_loop: FOREACHSYM IDENT FROMSYM modifiable_primary LOOPSYM body ENDSYM
;

range: INSYM expression RANGE expression
    | INSYM REVERSESYM expression RANGE expression
;

if_statement: IFSYM expression THENSYM body ENDSYM
    | IFSYM expression THENSYM body ELSESYM body ENDSYM
    ;

expression: relation
    | relation AND relation
    | relation OR relation
    | relation XOR relation
    | routine_call
    ;

relation: simple
    | simple LSS simple
    | simple LEQ simple
    | simple GTR simple
    | simple GEQ simple
    | simple EQL simple
    | simple NEQ simple
    ;

simple: factor
    | factor TIMES factor
    | factor SLASH factor
    | factor PERCENT factor
    ;

factor: summand
    | summand PLUS summand
    | summand MINUS summand
    ;

summand: primary
    | LPAREN expression RPAREN
    ;

primary: REAL
    | INT
    | TRUE
    | FALSE
    | modifiable_primary
   ;

modifiable_primary: IDENT
    | modifiable_primary LBRACKET expression RBRACKET
    | modifiable_primary PERIOD IDENT
    ;