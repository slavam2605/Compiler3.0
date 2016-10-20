package moklev.compiler.parsing;

import moklev.compiler.expression.CompileHint;
import moklev.compiler.expression.Type;
import moklev.compiler.expression.Variable;
import moklev.compiler.util.CompilerBundle;
import moklev.compiler.util.Pair;
import moklev.compiler.util.Scope;

import java.util.ArrayList;
import java.util.List;

import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Moklev Vyacheslav
 */
public class ParserUtils {
    // TODO not only int64 arguments
    static void compileFunctionArguments(CompilerBundle cb, CompileHint hint, List<Pair<Type, String>> args,
                                         Scope scope, String[] intArgumentRegister) {
        List<Pair<Type, String>> intArgs = new ArrayList<>();
        for (Pair<Type, String> pair: args) {
            if (pair.getFirst().isInt())
                intArgs.add(pair);
        }
        int index = 0;
        for (Pair<Type, String> pair: intArgs) {
            if (index < 6) {
                Variable var = scope.allocateVariable(pair.getSecond(), pair.getFirst());
                Pair<String, String> newReg = setRegisterSize(intArgumentRegister[index], pair.getFirst().getSizeOf());
                if (newReg.getFirst() != null) {
                    println(cb.sb, "mov ", newReg.getFirst(), ", ", intArgumentRegister[index]);
                }
                println(cb.sb, "mov [rbp - ", var.getOffset(), "], ", newReg.getSecond());
            } else {
                scope.putVariable(new Variable(pair.getSecond(), pair.getFirst(), -8 * (index - 5) - 8));
            }
            index++;
        }
    }

    private static Pair<String, String> setRegisterSize(String reg, int sizeOf) {
        String suffix = reg.substring(1);
        if (suffix.endsWith("x")) {
            switch (sizeOf) {
                case 1:
                    return new Pair<>(null, suffix.replace('x', 'l'));
                case 2:
                    return new Pair<>(null, suffix);
                case 4:
                    return new Pair<>(null, "e" + suffix);
                case 8:
                    return new Pair<>(null, reg);
                default:
                    throw new UnsupportedOperationException("Unsupported sizeof: " + sizeOf);
            }
        } else {
            switch (sizeOf) {
                case 1:
                    return new Pair<>("r10", "r10b");
                case 2:
                    return new Pair<>("r10", "r10w");
                case 4:
                    return new Pair<>("r10", "r10d");
                case 8:
                    return new Pair<>(null, reg);
                default:
                    throw new UnsupportedOperationException("Unsupported sizeof: " + sizeOf);
            }
        }
    }
}
