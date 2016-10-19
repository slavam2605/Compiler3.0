package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Vyacheslav Moklev
 */
public class Dereference implements LValue {
    private Expression innerExpr;

    public Dereference(Expression innerExpr) {
        this.innerExpr = innerExpr;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        innerExpr.compile(cb);
        println(cb.sb, "mov rax, [rax]");
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        // TODO refactor type system for pointers
        // FIXME FIXME FIXME normally make types
//        if (innerExpr.getType() == Type.INT64P) {
            return Type.INT64;
//        } else {
//            throw new UnsupportedOperationException("Dereferencing of " + innerExpr.getType() + " is not supported");
//        }
    }

    @Override
    public ReturnHint compileAddress(CompilerBundle cb, CompileHint hint) {
        return innerExpr.compile(cb);
    }
}
