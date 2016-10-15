package moklev.compiler.expression;

/**
 * @author Moklev Vyacheslav
 */
public interface Expression {
    ReturnHint compile(StringBuilder sb, CompileHint hint);
    Type getType();

    default ReturnHint compile(StringBuilder sb) {
        return compile(sb, CompileHint.EMPTY_HINT);
    }
}
