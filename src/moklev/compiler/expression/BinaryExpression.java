package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;
import moklev.compiler.util.StringBuilderPrinter;
import moklev.compiler.util.TriFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Moklev Vyacheslav
 */
public class BinaryExpression implements Expression {
    private Expression left;
    private Expression right;
    private String op;
    private static Map<Pair<String, Type>,
            TriFunction<BinaryExpression, CompilerBundle, CompileHint, ReturnHint>> compilers;

    static {
        compilers = new HashMap<>();
        compilers.put(new Pair<>("+",  Type.INT64), BinaryExpression::compileInt64Plus);
        compilers.put(new Pair<>("-",  Type.INT64), BinaryExpression::compileInt64Minus);
        compilers.put(new Pair<>("*",  Type.INT64), BinaryExpression::compileInt64Times);
        compilers.put(new Pair<>("=",  Type.INT64), BinaryExpression::compileInt64Assign);
        compilers.put(new Pair<>("<",  Type.INT64), BinaryExpression::compileInt64Less);
        compilers.put(new Pair<>(">",  Type.INT64), BinaryExpression::compileInt64Greater);
        compilers.put(new Pair<>("==", Type.INT64), BinaryExpression::compileInt64Equals);
        compilers.put(new Pair<>("!=", Type.INT64), BinaryExpression::compileInt64NotEquals);
    }

    public BinaryExpression(String op, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        assertTypeConsistent();
        TriFunction<BinaryExpression, CompilerBundle, CompileHint, ReturnHint> compiler =
                compilers.get(new Pair<>(op, left.getType()));
        if (compiler != null)
            return compiler.apply(this, cb, hint);
        else
            throw new UnsupportedOperationException("Operation " + op +
                    " is not implemented for " + left.getType());
    }

    private static ReturnHint compileInt64Plus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        expr.left.compile(cb);
        println(cb.sb, "push rax");
        expr.right.compile(cb);
        println(cb.sb, "pop r10");
        println(cb.sb, "add rax, r10");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        expr.left.compile(cb);
        println(cb.sb, "push rax");
        expr.right.compile(cb);
        println(cb.sb, "pop r10");
        println(cb.sb, "mov r11, rax");
        println(cb.sb, "mov rax, r10");
        println(cb.sb, "sub rax, r11");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        expr.left.compile(cb);
        println(cb.sb, "push rax");
        expr.right.compile(cb);
        println(cb.sb, "pop r10");
        println(cb.sb, "mul r10");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        // FIXME
        if (!(expr.left instanceof Variable))
            throw new IllegalArgumentException("Left expr is not variable");
        expr.right.compile(cb);
        Variable left = ((Variable) expr.left);
        int offset = left.getOffset();
        println(cb.sb, "mov [rbp - ", offset, "], rax");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Less(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jl");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Greater(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jg");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Equals(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "je");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64NotEquals(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jne");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static void compileInt64Compare(BinaryExpression expr, CompilerBundle cb, String command) {
        expr.left.compile(cb);
        println(cb.sb, "push rax");
        expr.right.compile(cb);
        println(cb.sb, "pop r10");
        println(cb.sb, "cmp r10, rax");
        String L1 = "L" + cb.labelCount++;
        String L2 = "L" + cb.labelCount++;
        String L3 = "L" + cb.labelCount++;
        println(cb.sb, command + " " + L2);
        nprintln(cb.sb, L1 + ":");
        println(cb.sb, "xor rax, rax");
        println(cb.sb, "jmp " + L3);
        nprintln(cb.sb, L2 + ":");
        println(cb.sb, "mov rax, 1");
        nprintln(cb.sb, L3 + ":");
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
