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
EQUALS:     '==';
NOT_EQUALS: '!=';

NOT:        '!';
AND:        '&&';
OR:         '||';

TYPE:       'bool' | 'int8' | 'int16' | 'int32' | 'int64' | 'float' | 'double';
IF:         'if';
RETURN:     'return';

INT64_LITERAL: '-'?[0-9][0-9]*;

ID:         [a-zA-Z_][a-zA-Z_0-9]*;

WS:         [ \t\r\n]+ -> skip;