parser grammar LangParser;

options {tokenVocab = LangLexer;}

@header {
import moklev.compiler.expression.*;
import moklev.compiler.util.*;
}

@members {
private StringBuilder sb = new StringBuilder();
private Scope scope = new Scope();
private int offset = 0;

private void nprintln(Object... s) {
    for (int i = 0; i < s.length; i++)
        sb.append(s[i]);
    sb.append('\n');
}

private void println(Object... s) {
    sb.append("    ");
    for (int i = 0; i < s.length; i++)
        sb.append(s[i]);
    sb.append('\n');
}

private void nprint(Object s) {
    sb.append(s);
}

private void print(Object s) {
    sb.append("    ").append(s);
}
}

file returns [String s]
    :   function*
        { $s = sb.toString(); }
    ;

function
    :   TYPE ID '(' argList ')' '{'
            { nprintln("global ", $ID.text);
              nprintln($ID.text, ":");
              println("push rbp");
              println("mov rbp, rsp");
              scope.enterScope();
              offset = 0; }
            statement*
            { scope.leaveScope();
              println("pop rbp");
              println("ret"); }
        '}'
    ;

argList
    :   TYPE ID (',' TYPE ID)*
    |
    ;

statement
    :   TYPE ID ';'
        { Type type = Type.fromString($TYPE.text);
          int sizeOf = type.getSizeOf();
          println("sub rsp, ", sizeOf);
          offset += sizeOf;
          Variable var = new Variable($ID.text, type, offset);
          scope.putVariable(var); }
    |   expression ';'
        { $expression.expr.compile(sb); }
    |   'return' expression ';'
        { $expression.expr.compile(sb);
          println("pop rbp");
          println("ret"); }
    |   'if' '(' expression ')' '{'
            statement*
        '}'
    ;

expression returns [Expression expr]
    :   INT64_LITERAL { $expr = new Int64Literal($INT64_LITERAL.text); }
    |   ID { $expr = scope.getVariable($ID.text); }
    |   '(' e1=expression ')' { $expr = $e1.expr; }
    |   '-' expression
    |   '!' expression
    |   e1=expression op=('*'|'/') e2=expression            { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op=('+'|'-') e2=expression            { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op=('<'|'>'|'=='|'!=') e2=expression  { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op='&&' e2=expression                 { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op='||' e2=expression                 { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   <assoc=right> e1=expression op='=' e2=expression    { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    ;
