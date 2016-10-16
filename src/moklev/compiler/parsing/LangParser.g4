parser grammar LangParser;

options {tokenVocab = LangLexer;}

@header {
import java.util.*;
import moklev.compiler.expression.*;
import moklev.compiler.util.*;
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
}

file returns [String s]
    :   function*
        { $s = cb.sb.toString(); }
    ;

function
    // TODO not only int64 arguments
    :   TYPE ID '(' argList ')' '{'
            { nprintln("global ", $ID.text);
              nprintln($ID.text, ":");
              scope.enterScope();
              List<moklev.compiler.util.Pair<Type, String>> intArgs = new ArrayList<>();
              for (moklev.compiler.util.Pair<Type, String> pair: $argList.args) {
                  if (pair.getFirst().isInt())
                      intArgs.add(pair);
              }
              int index = 0;
              for (moklev.compiler.util.Pair<Type, String> pair: intArgs) {
                  if (index < 6) {
                      Variable var = scope.allocateVariable(pair.getSecond(), pair.getFirst());
                      println("mov [rbp - ", var.getOffset(), "], ", intArgumentRegister[index]);
                  } else {
                      scope.putVariable(new Variable(pair.getSecond(), pair.getFirst(), -8 * (index - 5) - 8));
                  }
                  index++;
              }
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

statement
    :   TYPE ID ';'
        { Type type = Type.fromString($TYPE.text);
          int sizeOf = type.getSizeOf();
          scope.allocateVariable($ID.text, type); }
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
    ;

expression returns [Expression expr]
    :   INT64_LITERAL { $expr = new Int64Literal($INT64_LITERAL.text); }
    // TODO not only int64 arguments
    // TODO save volatile registers
    |   ID '(' exprList ')' { $expr = new FunctionCall($ID.text, $exprList.list); }
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

exprList returns [List<Expression> list]
@init {
    $list = new ArrayList<>();
}
    :   e1=expression { $list.add($e1.expr); }
        (',' ec=expression { $list.add($ec.expr); })*
    |
    ;











