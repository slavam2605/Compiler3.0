package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.ParseException;
import static moklev.compiler.util.StringBuilderPrinter.*;

import java.math.BigInteger;

/**
 * @author Moklev Vyacheslav
 */
public class Int16Literal implements Expression {
    private String value;

    public Int16Literal(String value) {
        BigInteger bValue = new BigInteger(value);
        if (bValue.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) > 0
                || bValue.compareTo(BigInteger.valueOf(Short.MIN_VALUE)) < 0)
            throw new ParseException("Value is not in int16 bounds: " + value);
        this.value = value;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        println(cb.sb, "mov ax, " + value);
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        return Type.INT16;
    }
}
