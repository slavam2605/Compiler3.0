package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

import java.util.Optional;

import static moklev.compiler.expression.Register.RAX;
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
        ReturnHint returnHint = innerExpr.compile(cb, hint.cloneArbitraryReturn());
        if (hint.isDefaultReturnFlag()) {
            println(cb.sb, "mov rax, [", returnHint.getReturnReg(), "]");
            return ReturnHint.DEFAULT_RETURN;
        } else {
            println(cb.sb, "mov ", returnHint.getReturnReg(), ", [", returnHint.getReturnReg(), "]");
            return new ReturnHint(returnHint.getReturnReg());
        }
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
        ReturnHint returnHint = innerExpr.compile(cb, hint.cloneArbitraryReturn());
        if (hint.isDefaultReturnFlag()) {
            if (returnHint.getReturnReg() != RAX)
                println(cb.sb, "mov rax, ", returnHint.getReturnReg());
            return ReturnHint.DEFAULT_RETURN;
        } else {
            return new ReturnHint(returnHint.getReturnReg());
        }
    }
}
