package moklev.compiler.expression;

import com.sun.istack.internal.Nullable;
import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;
import moklev.compiler.util.QuadriConsumer;

import java.util.HashMap;
import java.util.Map;

import static moklev.compiler.util.StringBuilderPrinter.*;
import static moklev.compiler.compilation.CompilerUtils.*;

/**
 * @author Moklev Vyacheslav
 */
public class BinaryBooleanExpression implements BooleanExpression {
    private String op;
    private Expression left;
    private Expression right;
    private static Map<Pair<String, Type>,
                QuadriConsumer<BinaryBooleanExpression, CompilerBundle, CompileHint, Pair<String, String>>> compilers;

    static {
        compilers = new HashMap<>();
        compilers.put(new Pair<>("<",  Type.INT64), BinaryBooleanExpression::compileInt64Less);
        compilers.put(new Pair<>("<=", Type.INT64), BinaryBooleanExpression::compileInt64LessEq);
        compilers.put(new Pair<>(">",  Type.INT64), BinaryBooleanExpression::compileInt64Greater);
        compilers.put(new Pair<>(">=", Type.INT64), BinaryBooleanExpression::compileInt64GreaterEq);
        compilers.put(new Pair<>("==", Type.INT64), BinaryBooleanExpression::compileInt64Equals);
        compilers.put(new Pair<>("!=", Type.INT64), BinaryBooleanExpression::compileInt64NotEquals);
        compilers.put(new Pair<>("&&", Type.BOOL),  BinaryBooleanExpression::compileBoolAnd);
    }

    public BinaryBooleanExpression(String op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public void compileBranch(CompilerBundle cb, CompileHint hint, String labelTrue, @Nullable String labelFalse) {
        assertTypeConsistent();
        QuadriConsumer<BinaryBooleanExpression, CompilerBundle, CompileHint, Pair<String, String>> compiler =
                compilers.get(new Pair<>(op, left.getType()));
        if (compiler != null)
            compiler.accept(this, cb, hint, new Pair<>(labelTrue, labelFalse));
        else
            throw new UnsupportedOperationException("Operation " + op +
                    " is not implemented for " + left.getType());
    }

    private static void compileInt64Compare(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels, String cmpOp) {
        Pair<Register, Register> result = optimizeCompileInt64(expr.left, expr.right, cb, hint);
        println(cb.sb, "cmp ", result.getFirst(), ", ", result.getSecond());
        println(cb.sb, cmpOp, " ", labels.getFirst());
        println(cb.sb, "jmp ", labels.getSecond());
    }

    private static void compileInt64Less(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        compileInt64Compare(expr, cb, hint, labels, "jl");
    }

    private static void compileInt64LessEq(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        compileInt64Compare(expr, cb, hint, labels, "jle");
    }

    private static void compileInt64Greater(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        compileInt64Compare(expr, cb, hint, labels, "jg");
    }

    private static void compileInt64GreaterEq(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        compileInt64Compare(expr, cb, hint, labels, "jge");
    }

    private static void compileInt64Equals(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        compileInt64Compare(expr, cb, hint, labels, "je");
    }

    private static void compileInt64NotEquals(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        compileInt64Compare(expr, cb, hint, labels, "jne");
    }

    private static void compileBoolAnd(BinaryBooleanExpression expr, CompilerBundle cb, CompileHint hint, Pair<String, String> labels) {
        BooleanExpression left = (BooleanExpression) expr.left;
        BooleanExpression right = (BooleanExpression) expr.right;
        String labelTrue = cb.nextLabel();
        left.compileBranch(cb, hint, labelTrue, labels.getSecond());
        nprintln(cb.sb, labelTrue, ":");
        right.compileBranch(cb, hint, labels.getFirst(), labels.getSecond());
    }

    @Override
    public Type getType() {
        return Type.BOOL;
    }

    private void assertTypeConsistent() {
        if (!left.getType().equals(right.getType()))
            throw new IllegalStateException("Different types");
    }
}
