package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

/**
 * @author Moklev Vyacheslav
 */
public interface Expression {
    ReturnHint compile(CompilerBundle cb, CompileHint hint);
    Type getType();
}
