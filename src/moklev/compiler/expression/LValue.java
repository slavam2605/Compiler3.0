package moklev.compiler.expression;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import moklev.compiler.util.CompilerBundle;

/**
 * @author Moklev Vyacheslav
 */
public interface LValue extends Expression {
    ReturnHint compileAddress(CompilerBundle cb, CompileHint hint);

//    default ReturnHint compileAddress(CompilerBundle cb) {
//        return compileAddress(cb, CompileHint.EMPTY_HINT);
//    }
}
