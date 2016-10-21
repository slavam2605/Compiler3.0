package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;
import moklev.compiler.util.TriFunction;

import java.util.HashMap;
import java.util.Map;

import static moklev.compiler.expression.Register.*;
import static moklev.compiler.util.StringBuilderPrinter.*;
import static moklev.compiler.compilation.CompilerUtils.*;

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
        compilers.put(new Pair<>("-",  Type.INT64), BinaryExpression::compileInt64Minus);
        compilers.put(new Pair<>("*",  Type.INT64), BinaryExpression::compileInt64Times);
        compilers.put(new Pair<>("/",  Type.INT64), BinaryExpression::compileInt64Div);
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
        Pair<Register, Register> result = optimizeCompileInt64(expr.left, expr.right, cb, hint);
        println(cb.sb, "add ", result.getFirst(), ", ", result.getSecond());
        return hintwiseReturn(cb, hint, result.getFirst());
    }

    private static ReturnHint compileInt64Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        Pair<Register, Register> result = optimizeCompileInt64(expr.left, expr.right, cb, hint);
        println(cb.sb, "sub ", result.getFirst(), ", ", result.getSecond());
        return hintwiseReturn(cb, hint, result.getFirst());
    }

    private static ReturnHint compileInt64Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        Pair<Register, Register> result = optimizeCompileInt64(expr.left, expr.right, cb, hint);
        if (result.getFirst() == RAX) {
            println(cb.sb, "mul ", result.getSecond());
        } else if (result.getSecond() == RAX) {
            println(cb.sb, "mul ", result.getFirst());
        } else {
            println(cb.sb, "mov rax, ", result.getFirst());
            println(cb.sb, "mul ", result.getSecond());
        }
        return hintwiseReturn(cb, hint, RAX);
    }

    private static ReturnHint compileInt64Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        if (!(expr.left instanceof LValue))
            throw new IllegalArgumentException("Trying to assign to a not LValue: " + expr.left.getClass().getSimpleName());
        if (expr.left instanceof Variable) {
            ReturnHint returnRight = expr.right.compile(cb, hint.cloneArbitraryReturn());
            println(cb.sb, "mov [rbp - ", ((Variable) expr.left).getOffset(), "], ", returnRight.getReturnReg());
            return hintwiseReturn(cb, hint, returnRight.getReturnReg());
        } else {
            Pair<Register, Register> result = optimizeCompileInt64(new CompileAddressWrapper((LValue) expr.left), expr.right, cb, hint);
            println(cb.sb, "mov [", result.getFirst(), "], ", result.getSecond());
            return hintwiseReturn(cb, hint, result.getSecond());
        }
    }

    private static ReturnHint compileInt64Div(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        // TODO save rdx if used somehow
        Pair<Register, Register> result = optimizeCompileInt64(expr.left, expr.right, cb, hint);
        if (result.getFirst() == RAX) {
            println(cb.sb, "xor rdx, rdx");
            println(cb.sb, "div ", result.getSecond());
        } else if (result.getSecond() == RAX) {
            println(cb.sb, "xor ", result.getFirst(), ", ", result.getSecond());
            println(cb.sb, "xor ", result.getSecond(), ", ", result.getFirst());
            println(cb.sb, "xor ", result.getFirst(), ", ", result.getSecond());
            println(cb.sb, "xor rdx, rdx");
            println(cb.sb, "div ", result.getFirst());
        } else {
            println(cb.sb, "mov rax, ", result.getFirst());
            println(cb.sb, "xor rdx, rdx");
            println(cb.sb, "div ", result.getSecond());
        }
        return hintwiseReturn(cb, hint, RAX);
    }

    @Override
    public Type getType() {
        assertTypeConsistent();
        switch (op) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "=":
                return left.getType();
            default:
                throw new UnsupportedOperationException("Operation " + op + " is not supported");
        }
    }

    private void assertTypeConsistent() {
        if (!left.getType().equals(right.getType()))
            throw new IllegalStateException("Different types");
    }
}
