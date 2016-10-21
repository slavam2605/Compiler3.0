package moklev.compiler.compilation;

import moklev.compiler.expression.CompileHint;
import moklev.compiler.expression.Expression;
import moklev.compiler.expression.Register;
import moklev.compiler.expression.ReturnHint;
import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;

import java.util.Optional;

import static moklev.compiler.expression.Register.RAX;
import static moklev.compiler.util.StringBuilderPrinter.println;

/**
 * @author Moklev Vyacheslav
 */
public class CompilerUtils {
    // TODO use acquireRegPref() somehow
    public static Pair<Register, Register> optimizeCompileInt64(Expression left, Expression right, CompilerBundle cb, CompileHint hint) {
        ReturnHint returnLeft = left.compile(cb, hint.cloneArbitraryReturn());
        Register leftReg = returnLeft.getReturnReg();
        if (leftReg == RAX) {
            Optional<Register> reg = hint.acquireReg();
            if (reg.isPresent()) {
                println(cb.sb, "mov " + reg.get() + ", rax");
                leftReg = reg.get();
            } else {
                println(cb.sb, "push rax");
                leftReg = null;
            }
        } else {
            hint.acquireReg(leftReg);
        }
        ReturnHint returnRight = right.compile(cb, hint.cloneArbitraryReturn());
        if (leftReg == null) {
            println(cb.sb, "pop " + hint.getTempReg());
            leftReg = hint.getTempReg();
        }
        return new Pair<>(leftReg, returnRight.getReturnReg());
    }

    public static ReturnHint hintwiseReturn(CompilerBundle cb, CompileHint hint, Register leftReg) {
        if (hint.isDefaultReturnFlag()) {
            if (leftReg != RAX)
                println(cb.sb, "mov rax, ", leftReg);
            return ReturnHint.DEFAULT_RETURN;
        } else {
            if (leftReg == hint.getTempReg()) {
                println(cb.sb, "mov rax, ", hint.getTempReg());
                return ReturnHint.DEFAULT_RETURN;
            } else {
                return new ReturnHint(leftReg);
            }
        }
    }
}
