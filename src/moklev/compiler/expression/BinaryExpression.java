package moklev.compiler.expression;

import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Moklev Vyacheslav
 */
public class BinaryExpression implements Expression {
    private Expression left;
    private Expression right;
    private String op;

    public BinaryExpression(String op, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public ReturnHint compile(StringBuilder sb, CompileHint hint) {
        assertTypeConsistent();
        // TODO implement for different types and operators
        switch (op) {
            case "+":
                switch (left.getType()) {
                    case INT64:
                        left.compile(sb);
                        println(sb, "push rax");
                        right.compile(sb);
                        println(sb, "pop rcx");
                        println(sb, "add rax, rcx");
                        break;
                    default:
                        throw new UnsupportedOperationException("Operation " + op +
                                " is not implemented for " + left.getType());
                }
                break;
            default:
                throw new UnsupportedOperationException("Not implemented operation: " + op);
        }
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        assertTypeConsistent();
        return left.getType();
    }

    private void assertTypeConsistent() {
        if (!left.getType().equals(right.getType()))
            throw new IllegalStateException("Different types");
    }
}
