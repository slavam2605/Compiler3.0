package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.ParseException;
import static moklev.compiler.util.StringBuilderPrinter.*;

import java.math.BigInteger;
import java.util.Optional;

/**
 * @author Moklev Vyacheslav
 */
public class Int64Literal implements Expression {
    private String value;

    public Int64Literal(String value) {
        BigInteger bValue = new BigInteger(value);
        if (bValue.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || bValue.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
            throw new ParseException("Value is not in int64 bounds: " + value);
        this.value = value;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        Optional<Register> reg = hint.acquireReg();
        if (hint.isDefaultReturnFlag() || !reg.isPresent()) {
            println(cb.sb, "mov rax, " + value);
            return ReturnHint.DEFAULT_RETURN;
        } else {
            println(cb.sb, "mov ", reg.get(), ", " + value);
            return new ReturnHint(reg.get());
        }
    }

    @Override
    public Type getType() {
        return Type.INT64;
    }
}
