package moklev.compiler.expression;

/**
 * @author Vyacheslav Moklev
 */
public enum Register {
    RAX, RBX, RCX, RDX,
    RBP, RSP, RDI, RSI,
    R8,  R9,  R10, R11,
    R12, R13, R14, R15;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}