package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.ParseException;
import static moklev.compiler.util.StringBuilderPrinter.*;

import java.math.BigInteger;

/**
 * @author Moklev Vyacheslav
 */
public class Int8Literal implements Expression {
    private String value;

    public Int8Literal(String value) {
        BigInteger bValue = new BigInteger(value);
        if (bValue.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) > 0
                || bValue.compareTo(BigInteger.valueOf(Byte.MIN_VALUE)) < 0)
            throw new ParseException("Value is not in int8 bounds: " + value);
        this.value = value;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        println(cb.sb, "mov al, " + value);
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        return Type.INT8;
    }
}
