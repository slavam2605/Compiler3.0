package moklev.compiler.expression;

import moklev.compiler.util.CompilerBundle;

/**
 * @author Moklev Vyacheslav
 */
public class CompileAddressWrapper implements Expression {
    private LValue lValue;

    public CompileAddressWrapper(LValue lValue) {
        this.lValue = lValue;
    }

    @Override
    public ReturnHint compile(CompilerBundle cb, CompileHint hint) {
        return lValue.compileAddress(cb, hint);
    }

    @Override
    public Type getType() {
        // FIXME set pointer type
        return lValue.getType();
    }
}
