package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.ParseException;
import static moklev.compiler.util.StringBuilderPrinter.*;

import java.math.BigInteger;

/**
 * @author Moklev Vyacheslav
 */
public class Int32Literal implements Expression {
    private String value;

    public Int32Literal(String value) {
        BigInteger bValue = new BigInteger(value);
        if (bValue.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0
                || bValue.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0)
            throw new ParseException("Value is not in int32 bounds: " + value);
        this.value = value;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        println(cb.sb, "mov eax, " + value);
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        return Type.INT32;
    }
}
