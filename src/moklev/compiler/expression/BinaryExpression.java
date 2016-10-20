package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;
import moklev.compiler.util.TriFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static moklev.compiler.expression.Register.RAX;
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
        compilers.put(new Pair<>("-",  Type.INT64), BinaryExpression::compileInt64Minus);
        compilers.put(new Pair<>("*",  Type.INT64), BinaryExpression::compileInt64Times);
        compilers.put(new Pair<>("/",  Type.INT64), BinaryExpression::compileInt64Div);
        compilers.put(new Pair<>("<",  Type.INT64), BinaryExpression::compileInt64Less);
        compilers.put(new Pair<>("<=",  Type.INT64), BinaryExpression::compileInt64LessEq);
        compilers.put(new Pair<>(">",  Type.INT64), BinaryExpression::compileInt64Greater);
        compilers.put(new Pair<>(">=",  Type.INT64), BinaryExpression::compileInt64GreaterEq);
        compilers.put(new Pair<>("==", Type.INT64), BinaryExpression::compileInt64Equals);
        compilers.put(new Pair<>("!=", Type.INT64), BinaryExpression::compileInt64NotEquals);
        compilers.put(new Pair<>("=",  Type.INT64), BinaryExpression::compileInt64Assign);
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
        return optimizeCompileInt64Operation(expr, cb, hint, (left, right) -> new Pair<>(
                new String[] {
                    "add " + left + ", " + right
                }, left
        ));
    }

    private static ReturnHint optimizeCompileInt64Operation(BinaryExpression expr, CompilerBundle cb, CompileHint hint, BiFunction<Register, Register, Pair<String[], Register>> cmdProducer) {
        ReturnHint returnLeft = expr.left.compile(cb, hint.cloneArbitraryReturn());
        Register leftReg = returnLeft.getReturnReg();
        if (leftReg == RAX) {
            Optional<Register> reg = hint.acquireReg();
            if (reg.isPresent()) {
                println(cb.sb, "mov " + reg.get() + ", rax");
                leftReg = reg.get();
            } else {
                println(cb.sb, "push rax");
                leftReg = null;
            }
        } else {
            hint.acquireReg(leftReg);
        }
        ReturnHint returnRight = expr.right.compile(cb, hint.cloneArbitraryReturn());
        if (leftReg == null) {
            println(cb.sb, "pop " + hint.getTempReg());
            leftReg = hint.getTempReg();
        }
        Pair<String[], Register> commands = cmdProducer.apply(leftReg, returnRight.getReturnReg());
        for (String command: commands.getFirst())
            println(cb.sb, command);
        leftReg = commands.getSecond(); // answer register
        if (leftReg == null)
            return null;
        if (hint.isDefaultReturnFlag()) {
            if (leftReg != RAX)
                println(cb.sb, "mov rax, ", leftReg);
            return ReturnHint.DEFAULT_RETURN;
        } else {
            if (leftReg == hint.getTempReg()) {
                println(cb.sb, "mov rax, ", hint.getTempReg());
                return ReturnHint.DEFAULT_RETURN;
            } else {
                return new ReturnHint(leftReg);
            }
        }
    }

    private static ReturnHint compileInt64Minus(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        return optimizeCompileInt64Operation(expr, cb, hint, (left, right) -> new Pair<>(
                new String[] {
                    "sub " + left + ", " + right
                }, left
        ));
//        expr.left.compile(cb);
//        println(cb.sb, "push rax");
//        expr.right.compile(cb);
//        println(cb.sb, "mov r10, rax");
//        println(cb.sb, "pop rax");
//        println(cb.sb, "sub rax, r10");
//        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Times(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        return optimizeCompileInt64Operation(expr, cb, hint, (left, right) -> {
            if (left == RAX) {
                return new Pair<>(new String[] {"mul " + right}, RAX);
            } else if (right == RAX) {
                return new Pair<>(new String[] {"mul " + left}, RAX);
            } else {
                return new Pair<> (
                        new String[] {
                            "mov rax, " + left,
                            "mul " + right
                        }, RAX
                );
            }
        });
//        compileIntOperation(expr, cb, "mul r10");
//        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Less(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, hint, "jl");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64LessEq(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, hint, "jle");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Greater(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, hint, "jg");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64GreaterEq(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, hint, "jge");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64Equals(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, hint, "je");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static ReturnHint compileInt64NotEquals(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        compileInt64Compare(expr, cb, hint, "jne");
        return ReturnHint.DEFAULT_RETURN;
    }

    private static void compileInt64Compare(BinaryExpression expr, CompilerBundle cb, CompileHint hint, String command) {
//        compileIntOperation(expr, cb, "cmp r10, rax");
        optimizeCompileInt64Operation(expr, cb, hint, (left, right) -> new Pair<>(
                new String[] { "cmp " + left + ", " + right }, null
        ));
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
        ReturnHint returnRight;
        if (expr.left instanceof Variable) {
            returnRight = expr.right.compile(cb, hint.cloneArbitraryReturn());
            println(cb.sb, "mov [rbp - ", ((Variable) expr.left).getOffset(), "], ", returnRight.getReturnReg());
        } else {
            ReturnHint returnLeft = ((LValue) expr.left).compileAddress(cb, hint.cloneArbitraryReturn());
            Register leftReg = returnLeft.getReturnReg();
            if (leftReg == RAX) {
                Optional<Register> reg = hint.acquireReg();
                if (reg.isPresent()) {
                    println(cb.sb, "mov " + reg.get() + ", rax");
                    leftReg = reg.get();
                } else {
                    println(cb.sb, "push rax");
                    leftReg = null;
                }
            } else {
                hint.acquireReg(leftReg);
            }
            returnRight = expr.right.compile(cb, hint.cloneArbitraryReturn());
            if (leftReg == null) {
                println(cb.sb, "pop " + hint.getTempReg());
                leftReg = hint.getTempReg();
            }
            println(cb.sb, "mov [", leftReg, "], ", returnRight.getReturnReg());
        }
        if (hint.isDefaultReturnFlag()) {
            if (returnRight.getReturnReg() != RAX)
                println(cb.sb, "mov rax, ", returnRight.getReturnReg());
            return ReturnHint.DEFAULT_RETURN;
        } else {
            if (returnRight.getReturnReg() == hint.getTempReg()) {
                println(cb.sb, "mov rax, ", hint.getTempReg());
                return ReturnHint.DEFAULT_RETURN;
            } else {
                return new ReturnHint(returnRight.getReturnReg());
            }
        }
    }

    private static ReturnHint compileInt64Div(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
//        expr.left.compile(cb, hint.clone());
//        println(cb.sb, "push rax");
//        expr.right.compile(cb, hint.clone());
//        println(cb.sb, "mov r10, rax");
//        println(cb.sb, "pop rax");
//        println(cb.sb, "xor rdx, rdx");
//        println(cb.sb, "div r10");
//        return ReturnHint.DEFAULT_RETURN;
        return optimizeCompileInt64Operation(expr, cb, hint, (left, right) -> {
            if (left == RAX) {
                return new Pair<>(
                        new String[] {
                            "xor rdx, rdx",
                            "div " + right
                        }, RAX
                );
            } else if (right == RAX) {
                return new Pair<>(
                        new String[] {
                            "xor " + left + ", " + right,
                            "xor " + right + ", " + left,
                            "xor " + left + ", " + right,
                            "xor rdx, rdx",
                            "div " + left
                        }, RAX
                );
            } else {
                return new Pair<>(
                        new String[] {
                            "mov rax, " + left,
                            "xor rdx, rdx",
                            "div " + right
                        }, RAX
                );
            }
        });
    }

    private static ReturnHint compileBoolAnd(BinaryExpression expr, CompilerBundle cb, CompileHint hint) {
        String labelFalse = cb.nextLabel();
        String labelAfter = cb.nextLabel();
        expr.left.compile(cb, hint.cloneDefaultReturn());
        println(cb.sb, "test rax, rax");
        println(cb.sb, "jz " + labelFalse);
        expr.right.compile(cb, hint.cloneDefaultReturn());
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
