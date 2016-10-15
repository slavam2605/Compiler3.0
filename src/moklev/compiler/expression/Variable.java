package moklev.compiler.expression;

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
    public ReturnHint compile(StringBuilder sb, CompileHint hint) {
        // TODO switch (type) ...
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        return type;
    }
}
