lexer grammar LangLexer;

LBRACKET:   '(';
RBRACKET:   ')';
LBRACE:     '{';
RBRACE:     '}';
SEMICOLON:  ';';
COMMA:      ',';

ASSIGN:     '=';
PLUS:       '+';
MINUS:      '-';
TIMES:      '*';
DIV:        '/';

LESS:       '<';
GREATER:    '>';
LESS_EQ:    '<=';
GREATER_EQ: '>=';
EQUALS:     '==';
NOT_EQUALS: '!=';

NOT:        '!';
AND:        '&&';
OR:         '||';

TYPE:       'bool' | 'int8' | 'int16' | 'int32' | 'int64' | 'float' | 'double';
IF:         'if';
RETURN:     'return';
EXTERN:     'extern';
FOR:        'for';

INT64_LITERAL: '-'?[0-9][0-9]*;
INT32_LITERAL: INT64_LITERAL 'd';
INT16_LITERAL: INT64_LITERAL 'w';
INT8_LITERAL:  INT64_LITERAL 'b';

ID:         [a-zA-Z_][a-zA-Z_0-9]*;

WS:             [ \t\r\n]+ -> skip;
COMMENT:        '/*' .*? '*/' -> skip;
LINE_COMMENT:   '//' ~'\n'* '\n' -> skip;