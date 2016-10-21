package moklev.compiler.expression;

import com.sun.istack.internal.Nullable;
import moklev.compiler.util.CompilerBundle;

import java.util.Optional;

import static moklev.compiler.expression.Register.RAX;
import static moklev.compiler.util.StringBuilderPrinter.nprintln;
import static moklev.compiler.util.StringBuilderPrinter.println;

/**
 * @author Moklev Vyacheslav
 */
public interface BooleanExpression extends Expression {
    void compileBranch(CompilerBundle cb, CompileHint hint, String labelTrue, String labelFalse);

    @Override
    default ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        String labelFalse = cb.nextLabel();
        String labelTrue = cb.nextLabel();
        String labelAfter = cb.nextLabel();
        compileBranch(cb, hint, labelTrue, labelFalse);
        Register reg;
        if (hint.isDefaultReturnFlag()) {
            reg = RAX;
        } else {
            Optional<Register> acqReg = hint.acquireRegPref();
            reg = acqReg.orElse(RAX);
        }
        nprintln(cb.sb, labelFalse, ":");
        println(cb.sb, "xor ", reg, ", ", reg);
        println(cb.sb, "jmp ", labelAfter);
        nprintln(cb.sb, labelTrue, ":");
        println(cb.sb, "mov ", reg, ", 1");
        nprintln(cb.sb, labelAfter, ":");
        return reg == RAX ? ReturnHint.DEFAULT_RETURN : new ReturnHint(reg);
    }
}
