package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Moklev Vyacheslav
 */
public class Variable implements LValue {
    private String name;
    private Type type;
    private int offset;

    public Variable(String name, Type type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        // TODO support more types
        switch (type) {
            case INT8:
            case INT16:
            case INT32:
            case INT64:
                println(cb.sb, "mov " + getIntReturnRegister(type) + ", [rbp - ", offset, "]");
                break;
            default:
                throw new UnsupportedOperationException("Type " + type + " is not supported");
        }
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public ReturnHint compileAddress(CompilerBundle cb, CompileHint hint) {
        println(cb.sb, "lea rax, [rbp - ", offset, "]");
        return ReturnHint.DEFAULT_RETURN;
    }

    private String getIntReturnRegister(Type type) {
        switch (type) {
            case INT8:
                return "al";
            case INT16:
                return "ax";
            case INT32:
                return "eax";
            case INT64:
                return "rax";
            default:
                throw new UnsupportedOperationException("Unknown int type: " + type);
        }
    }

    @Override
    public Type getType() {
        return type;
    }
}
