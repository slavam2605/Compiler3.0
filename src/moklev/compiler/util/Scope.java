package moklev.compiler.util;

import moklev.compiler.expression.Type;
import moklev.compiler.expression.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static moklev.compiler.util.StringBuilderPrinter.*;

/**
 * @author Moklev Vyacheslav
 */
public class Scope {
    private StringBuilder sb;
    private List<Map<String, Variable>> scope;
    private List<Integer> offset;

    public Scope(StringBuilder sb) {
        this.sb = sb;
        scope = new ArrayList<>();
        scope.add(new HashMap<>());
        offset = new ArrayList<>();
        offset.add(0);
    }

    public void enterScope() {
        scope.add(new HashMap<>(scope.get(scope.size() - 1)));
        offset.add(offset.get(offset.size() - 1));
        if (scope.size() == 2) {
            println(sb, "push rbp");
            println(sb, "mov rbp, rsp");
        }
    }

    public void leaveScope() {
        scope.remove(scope.size() - 1);
        int previousOffset = offset.get(offset.size() - 2);
        int toDeallocate = offset.get(offset.size() - 1) - previousOffset;
        offset.remove(offset.size() - 1);
        if (toDeallocate > 0)
            println(sb, "add rsp, " + toDeallocate);
    }

    public void breakAllScopes() {
        if (offset.get(offset.size() - 1) > 0)
            println(sb, "add rsp, " + offset.get(offset.size() - 1));
    }

    public void putVariable(Variable var) {
        scope.get(scope.size() - 1).put(var.getName(), var);
    }

    public Variable allocateVariable(String name, Type type) {
        int newOffset = offset.get(offset.size() - 1) + type.getSizeOf();
        offset.set(offset.size() - 1, newOffset);
        Variable var = new Variable(name, type, newOffset);
        putVariable(var);
        println(sb, "sub rsp, ", type.getSizeOf());
        return var;
    }

    public Variable getVariable(String name) {
        return scope.get(scope.size() - 1).get(name);
    }
}
