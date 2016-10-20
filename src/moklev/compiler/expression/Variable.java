package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

import java.util.Optional;

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
            // FIXME optimize 8-32 bits
            case INT8:
            case INT16:
            case INT32:
                println(cb.sb, "mov " + getIntReturnRegister(type) + ", [rbp - ", offset, "]");
                return ReturnHint.DEFAULT_RETURN;
            case INT64:
                Optional<Register> reg = hint.acquireRegPref();
                if (hint.isDefaultReturnFlag() || !reg.isPresent()) {
                    println(cb.sb, "mov rax, [rbp - ", offset, "]");
                    return ReturnHint.DEFAULT_RETURN;
                } else {
                    println(cb.sb, "mov ", reg.get(), ", [rbp - ", offset, "]");
                    return new ReturnHint(reg.get());
                }
            default:
                throw new UnsupportedOperationException("Type " + type + " is not supported");
        }
    }

    @Override
    public ReturnHint compileAddress(CompilerBundle cb, CompileHint hint) {
        Optional<Register> reg = hint.acquireReg();
        if (hint.isDefaultReturnFlag() || !reg.isPresent()) {
            println(cb.sb, "lea rax, [rbp - ", offset, "]");
            return ReturnHint.DEFAULT_RETURN;
        } else {
            println(cb.sb, "lea ", reg.get(), ", [rbp - ", offset, "]");
            return new ReturnHint(reg.get());
        }
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
