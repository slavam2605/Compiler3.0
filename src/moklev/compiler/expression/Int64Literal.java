package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.ParseException;
import static moklev.compiler.util.StringBuilderPrinter.*;

import java.math.BigInteger;

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
        println(cb.sb, "mov rax, " + value);
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        return Type.INT64;
    }
}
