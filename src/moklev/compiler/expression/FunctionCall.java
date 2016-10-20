package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

import static moklev.compiler.util.StringBuilderPrinter.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static moklev.compiler.expression.Register.*;

/**
 * @author Vyacheslav Moklev
 */
public class FunctionCall implements Expression {
    private static final Register[] intArgumentRegister = new Register[] {
            RDI, RSI, RDX, RCX, R8, R9
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
        List<Register> toRestore = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            if (i >= list.size())
                continue;
            if (!hint.availableReg(intArgumentRegister[i]) && hint.mustBeAvailable(intArgumentRegister[i])) {
                toRestore.add(intArgumentRegister[i]);
                println(cb.sb, "push ", intArgumentRegister[i]);
            }
        }
        for (int i = list.size() - 1; i >= 6; i--) {
            ReturnHint returnHint = list.get(i).compile(cb, hint.cloneArbitraryReturn());
            println(cb.sb, "push ", returnHint.getReturnReg());
        }
        for (int i = 5; i >= 0; i--) {
            if (i >= list.size())
                continue;
            ReturnHint returnHint = list.get(i).compile(cb, hint
                    .cloneArbitraryReturn()
                    .clonePreferredReg(intArgumentRegister[i]));
            hint.acquireIfAvailable(intArgumentRegister[i]);
            if (returnHint.getReturnReg() != intArgumentRegister[i])
                println(cb.sb, "mov ", intArgumentRegister[i], ", ", returnHint.getReturnReg());
        }
        println(cb.sb, "call " + funcName);
        if (list.size() > 6)
            println(cb.sb, "add rsp, ", 8 * (list.size() - 6));
        for (int i = toRestore.size() - 1; i >= 0; i--) {
            println(cb.sb, "pop ", toRestore.get(i));
        }
        return ReturnHint.DEFAULT_RETURN;
    }

    @Override
    public Type getType() {
        // FIXME
        return Type.INT64;
    }
}
