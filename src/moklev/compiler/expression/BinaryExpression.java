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
        compilers.put(new Pair<>("-",  Type.INT32), BinaryExpression::compileInt32Minus);
        compilers.put(new Pair<>("-",  Type.INT16), BinaryExpression::compileInt16Minus);
        compilers.put(new Pair<>("-",  Type.INT8),  BinaryExpression::compileInt8Minus);

        compilers.put(new Pair<>("*",  Type.INT64), BinaryExpression::compileInt64Times);
        compilers.put(new Pair<>("*",  Type.INT32), BinaryExpression::compileInt32Times);
        compilers.put(new Pair<>("*",  Type.INT16), BinaryExpression::compileInt16Times);
        compilers.put(new Pair<>("*",  Type.INT8), BinaryExpression::compileInt8Times);

        compilers.put(new Pair<>("/",  Type.INT64), BinaryExpression::compileInt64Div);

        compilers.put(new Pair<>("<",  Type.INT64), BinaryExpression::compileInt64Less);

        compilers.put(new Pair<>("<=",  Type.INT64), BinaryExpression::compileInt64LessEq);

        compilers.put(new Pair<>(">",  Type.INT64), BinaryExpression::compileInt64Greater);

        compilers.put(new Pair<>(">=",  Type.INT64), BinaryExpression::compileInt64GreaterEq);

        compilers.put(new Pair<>("==", Type.INT64), BinaryExpression::compileInt64Equals);

        compilers.put(new Pair<>("!=", Type.INT64), BinaryExpression::compileInt64NotEquals);

        compilers.put(new Pair<>("=",  Type.INT64), BinaryExpression::compileInt64Assign);
        compilers.put(new Pair<>("=",  Type.INT32), BinaryExpression::compileInt32Assign);
        compilers.put(new Pair<>("=",  Type.INT16), BinaryExpression::compileInt16Assign);
        compilers.put(new Pair<>("=",  Type.INT8),  BinaryExpression::compileInt8Assign);

        compilers.put(new Pair<>("&&",  Type.BOOL),  BinaryExpression::compileBoolAnd);
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

    private static ReturnHint compileInt32Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mov r11, rax");
        println(cb.sb, "mov rax, r10");
        println(cb.sb, "sub eax, r11d");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt16Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mov r11, rax");
        println(cb.sb, "mov rax, r10");
        println(cb.sb, "sub ax, r11w");
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

    private static ReturnHint compileInt32Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mul r10d");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt16Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mul r10w");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt8Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mul r10b");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Less(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jl");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64LessEq(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jle");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Greater(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jg");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64GreaterEq(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, "jge");
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

    private static void compileIntAssign(BinaryExpression expr, CompilerBundle cb, String assignReg) {
        if (!(expr.left instanceof LValue))
            throw new IllegalArgumentException("Trying to assign to a not LValue: " + expr.left.getClass().getSimpleName());
        if (expr.left instanceof Variable) {
            expr.right.compile(cb);
            println(cb.sb, "mov [rbp - ", ((Variable) expr.left).getOffset(), "], ", assignReg);
        } else {
            ((LValue) expr.left).compileAddress(cb);
            println(cb.sb, "push rax");
            expr.right.compile(cb);
            println(cb.sb, "pop r10");
            println(cb.sb, "mov [r10], ", assignReg);
        }
    }

    private static ReturnHint compileInt64Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntAssign(expr, cb, "rax");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt32Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntAssign(expr, cb, "eax");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt16Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntAssign(expr, cb, "ax");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt8Assign(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntAssign(expr, cb, "al");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Div(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileIntOperation(expr, cb, "mov r11, rax");
        println(cb.sb, "mov rax, r10");
        println(cb.sb, "xor rdx, rdx");
        println(cb.sb, "div r11");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileBoolAnd(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        String labelFalse = cb.nextLabel();
        String labelAfter = cb.nextLabel();
        expr.left.compile(cb);
        println(cb.sb, "test rax, rax");
        println(cb.sb, "jz " + labelFalse);
        expr.right.compile(cb);
        println(cb.sb, "test rax, rax");
        println(cb.sb, "jz " + labelFalse);
        println(cb.sb, "mov rax, 1");
        println(cb.sb, "jmp " + labelAfter);
        nprintln(cb.sb, labelFalse + ":");
        println(cb.sb, "mov rax, 0");
        nprintln(cb.sb, labelAfter + ":");
        return ReturnHint.DEFAULT_RETURN;
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
            case "&&":
                return left.getType();
            case ">":
            case "<":
            case "==":
            case "!=":
                return Type.BOOL;
            default:
                throw new UnsupportedOperationException("Operation " + op + " is not supported");
        }
    }

    private void assertTypeConsistent() {
        if (!left.getType().equals(right.getType()))
            throw new IllegalStateException("Different types");
    }
}
