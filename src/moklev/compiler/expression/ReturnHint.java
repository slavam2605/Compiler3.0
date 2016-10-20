package moklev.compiler.expression;

import static moklev.compiler.expression.Register.RAX;

/**
 * @author Moklev Vyacheslav
 */
public class ReturnHint {
    public static ReturnHint DEFAULT_RETURN = new ReturnHint(RAX);

    private Register reg;

    public ReturnHint(Register reg) {
        this.reg = reg;
    }

    public Register getReturnReg() {
        return reg;
    }
}
