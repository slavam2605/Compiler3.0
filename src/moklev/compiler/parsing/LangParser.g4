parser grammar LangParser;

options {tokenVocab = LangLexer;}

@header {
import java.util.*;
import moklev.compiler.expression.*;
import moklev.compiler.util.*;
import static moklev.compiler.parsing.ParserUtils.*;
}

@members {
private StringBuilder sb = new StringBuilder();
private CompilerBundle cb = new CompilerBundle(sb);
private Scope scope = new Scope(sb);

private static final String[] intArgumentRegister = new String[] {
    "rdi", "rsi", "rdx", "rcx", "r8", "r9"
};

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

private static String drop(int count, String s) {
    return s.substring(0, s.length() - count);
}
}

file returns [String s]
    :   (function | extern)*
        { $s = cb.sb.toString(); }
    ;

extern
    :   'extern' ID ';'
        { nprintln("extern ", $ID.text); }
    ;

function
    :   TYPE ID '(' argList ')' '{'
            { nprintln("global ", $ID.text);
              nprintln($ID.text, ":");
              scope.enterScope();
              compileFunctionArguments(cb, $argList.args, scope, intArgumentRegister);
            }
            statement*
            { scope.leaveScope();
              println("pop rbp");
              println("ret"); }
        '}'
    ;

argList returns [List<moklev.compiler.util.Pair<Type, String>> args]
@init {
    $args = new ArrayList<>();
}
    :   t1=TYPE id1=ID {$args.add(new moklev.compiler.util.Pair<>(Type.fromString($t1.text), $id1.text));}
        (',' tc=TYPE idc=ID {$args.add(new moklev.compiler.util.Pair<>(Type.fromString($tc.text), $idc.text));})*
    |
    ;

statement locals [boolean flag, String beginLabel, String endLabel]
@init {$flag = false;}
    :   TYPE ID ('=' expression { $flag = true; })? ';'
        { Type type = Type.fromString($TYPE.text);
          int sizeOf = type.getSizeOf();
          Variable var = scope.allocateVariable($ID.text, type);
          if ($flag) {
              new BinaryExpression(
                  "=", var, $expression.expr
              ).compile(cb);
          }
        }
    |   expression ';'
        { $expression.expr.compile(cb); }
    |   'return' expression ';'
        { $expression.expr.compile(cb);
          scope.breakAllScopes();
          println("pop rbp");
          println("ret"); }
    |   'if' '(' expression ')' '{'
        { $expression.expr.compile(cb);
          println("test rax, rax");
          String after = "L" + cb.labelCount++;
          println("jz " + after);
          scope.enterScope(); }
            statement*
        { scope.leaveScope();
          nprintln(after + ":"); }
        '}'
    |   'for'
        { scope.enterScope(); }
        '(' statement* ';' e1=expression ';' e2=expression ')' '{'
            { $beginLabel = cb.nextLabel();
              $endLabel = cb.nextLabel();
              nprintln($beginLabel + ":");
              if ($e1.expr.getType() != Type.BOOL)
                  throw new IllegalArgumentException("Condition in 'if' is not BOOL: found " + $e1.expr.getType());
              $e1.expr.compile(cb);
              println("test rax, rax");
              println("jz " + $endLabel);
              scope.enterScope(); }
            statement*
            { scope.leaveScope();
              $e2.expr.compile(cb);
              println("jmp " + $beginLabel);
              nprintln($endLabel + ":"); }
        '}' { scope.leaveScope(); }
    ;

expression returns [Expression expr]
    :   INT64_LITERAL { $expr = new Int64Literal($INT64_LITERAL.text); }
    |   INT32_LITERAL { $expr = new Int32Literal(drop(1, $INT32_LITERAL.text)); }
    |   INT16_LITERAL { $expr = new Int16Literal(drop(1, $INT16_LITERAL.text)); }
    |   INT8_LITERAL  { $expr = new Int8Literal (drop(1, $INT8_LITERAL.text)); }
    |   ID '(' exprList ')' { $expr = new FunctionCall($ID.text, $exprList.list); }
    |   ID { $expr = scope.getVariable($ID.text); }
    |   '(' e1=expression ')' { $expr = $e1.expr; }
    |   '-' e1=expression
    |   '!' e1=expression
    |   '*' e1=expression { $expr = new Dereference($e1.expr); }
    |   e1=expression op=('*'|'/') e2=expression            { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op=('+'|'-') e2=expression            { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression
        op=('<'|'>'|'=='|'!='|'>='|'<=') e2=expression      { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op='&&' e2=expression                 { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   e1=expression op='||' e2=expression                 { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    |   <assoc=right> e1=expression op='=' e2=expression    { $expr = new BinaryExpression($op.text, $e1.expr, $e2.expr); }
    ;

exprList returns [List<Expression> list]
@init {
    $list = new ArrayList<>();
}
    :   e1=expression { $list.add($e1.expr); }
        (',' ec=expression { $list.add($ec.expr); })*
    |
    ;











