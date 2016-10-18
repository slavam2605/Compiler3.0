package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;
import moklev.compiler.util.TriFunction;

import java.util.HashMap;
import java.util.Map;

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
        // TODO implement all operations for all types
        compilers.put(new Pair<>("+",  Type.INT64), BinaryExpression::compileInt64Plus);
        compilers.put(new Pair<>("+",  Type.INT32), BinaryExpression::compileInt32Plus);
        compilers.put(new Pair<>("+",  Type.INT16), BinaryExpression::compileInt16Plus);
        compilers.put(new Pair<>("+",  Type.INT8),  BinaryExpression::compileInt8Plus);
        compilers.put(new Pair<>("-",  Type.INT64), BinaryExpression::compileInt64Minus);
        compilers.put(new Pair<>("-",  Type.INT8),  BinaryExpression::compileInt8Minus);
        compilers.put(new Pair<>("*",  Type.INT64), BinaryExpression::compileInt64Times);
        compilers.put(new Pair<>("<",  Type.INT64), BinaryExpression::compileInt64Less);
        compilers.put(new Pair<>(">",  Type.INT64), BinaryExpression::compileInt64Greater);
        compilers.put(new Pair<>("==", Type.INT64), BinaryExpression::compileInt64Equals);
        compilers.put(new Pair<>("!=", Type.INT64), BinaryExpression::compileInt64NotEquals);
        compilers.put(new Pair<>("=",  Type.INT64), BinaryExpression::compileInt64Assign);
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
        compileIntOperation(expr, cb, "add rax, r10");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static void compileIntOperation(BinaryExpression expr, CompilerBundle cb, String s3) {
        expr.left.compile(cb);
        println(cb.sb, "push rax");
        expr.right.compile(cb);
        println(cb.sb, "pop r10");
        println(cb.sb, s3);
    }

    private static ReturnHint compileInt32Plus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "add eax, r10d");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt16Plus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "add ax, r10w");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt8Plus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "add al, r10b");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mov r11, rax");
        println(cb.sb, "mov rax, r10");
        println(cb.sb, "sub rax, r11");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt8Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mov r11, rax");
        println(cb.sb, "mov rax, r10");
        println(cb.sb, "sub al, r11b");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mul r10");
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
        compileIntOperation(expr, cb, "cmp r10, rax");
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

    private static ReturnHint compileInt64Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        if (!(expr.left instanceof LValue))
            throw new IllegalArgumentException("Trying to assign to a not LValue: " + expr.left.getClass().getSimpleName());
        ((LValue) expr.left).compileAddress(cb);
        println(cb.sb, "push rax");
        expr.right.compile(cb);
        println(cb.sb, "pop r10");
        println(cb.sb, "mov [r10], rax");
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
