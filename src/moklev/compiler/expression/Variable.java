package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Moklev Vyacheslav
 */
public class Variable implements Expression {
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
        // TODO switch (type) ...
        if (type == Type.INT64) {
            println(cb.sb, "mov rax, [rbp - ", offset, "]");
        }
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        return type;
    }
}
