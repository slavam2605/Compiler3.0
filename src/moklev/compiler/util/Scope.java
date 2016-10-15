package moklev.compiler.util;

import moklev.compiler.expression.Type;
import moklev.compiler.expression.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moklev Vyacheslav
 */
public class Scope {
    private List<Map<String, Variable>> scope;

    public Scope() {
        scope = new ArrayList<>();
        scope.add(new HashMap<>());
    }

    public void enterScope() {
        scope.add(new HashMap<>(scope.get(scope.size() - 1)));
    }

    public void leaveScope() {
        scope.remove(scope.size() - 1);
    }

    public void putVariable(String name, Type type, int offset) {
        putVariable(new Variable(name, type, offset));
    }

    public void putVariable(Variable var) {
        scope.get(scope.size() - 1).put(var.getName(), var);
    }

    public Variable getVariable(String name) {
        return scope.get(scope.size() - 1).get(name);
    }
}
