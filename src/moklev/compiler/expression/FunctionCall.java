package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

import static moklev.compiler.util.StringBuilderPrinter.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Moklev
 */
public class FunctionCall implements Expression {
    private static final String[] intArgumentRegister = new String[] {
            "rdi", "rsi", "rdx", "rcx", "r8", "r9"
    };

    private String funcName;
    private List<Expression> list;

    public String getFuncName() {
        return funcName;
    }

    public List<Expression> getList() {
        return list;
    }

    public FunctionCall(String funcName, List<Expression> list) {
        this.funcName = funcName;
        this.list = list;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        // FIXME
        // TODO not only int64 arguments
        // TODO save volatile registers
        Collections.reverse(list);
        for (Expression expr: list) {
            expr.compile(cb);
            println(cb.sb, "push rax");
        }
        for (int i = 0; i < Math.min(6, list.size()); i++) {
            println(cb.sb, "pop " + intArgumentRegister[i]);
        }
        println(cb.sb, "call " + funcName);
        if (list.size() > 6)
            println(cb.sb, "add rsp, ", 8 * (list.size() - 6));
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        // FIXME
        return Type.INT64;
    }
}
