package moklev.compiler.expression;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static moklev.compiler.expression.Register.*;

/**
 * @author Moklev Vyacheslav
 */
public class CompileHint {
    public static final CompileHint EMPTY_HINT = new CompileHint(true, true, null);
    private static final CompileHint FULL_HINT = new CompileHint(false, false, null);

    private boolean finalFlag;
    private boolean defaultReturnFlag;
    private Set<Register> availableRegisters;
    private Register preferredReg;

    public Set<Register> getAvailableRegisters() {
        return availableRegisters;
    }

    public Register getPreferredReg() {
        return preferredReg;
    }

    public boolean isFinalFlag() {
        return finalFlag;
    }

    public boolean isDefaultReturnFlag() {
        return defaultReturnFlag;
    }

    private CompileHint(CompileHint hint) {
        this.finalFlag = hint.finalFlag;
        this.defaultReturnFlag = hint.defaultReturnFlag;
        this.preferredReg = hint.preferredReg;
        this.availableRegisters = new HashSet<>(hint.availableRegisters);
    }

    public CompileHint(boolean finalFlag, boolean defaultReturnFlag, Register preferredReg) {
        this.finalFlag = finalFlag;
        this.defaultReturnFlag = defaultReturnFlag;
        this.preferredReg = preferredReg;
        availableRegisters = new HashSet<>();
        if (!finalFlag) {
            availableRegisters.add(RCX);
            availableRegisters.add(RDI);
            availableRegisters.add(RSI);
            availableRegisters.add(R8);
            availableRegisters.add(R9);
            availableRegisters.add(R11);
        }
    }

    public Register getTempReg() {
        return R10;
    }

    public Optional<Register> acquireReg() {
        if (finalFlag)
            return Optional.empty();
        Optional<Register> result = availableRegisters.stream().findAny();
        result.ifPresent(reg -> availableRegisters.remove(reg));
        return result;
    }

    public Optional<Register> acquireRegPref() {
        if (finalFlag)
            return Optional.empty();
        if (preferredReg != null && availableRegisters.contains(preferredReg)) {
            availableRegisters.remove(preferredReg);
            return Optional.of(preferredReg);
        }
        return acquireReg();
    }

    public boolean mustBeAvailable(Register reg) {
        return FULL_HINT.availableRegisters.contains(reg);
    }

    public boolean availableReg(Register reg) {
        return availableRegisters.contains(reg);
    }

    public void acquireIfAvailable(Register reg) {
        if (availableRegisters.contains(reg))
            availableRegisters.remove(reg);
    }

    public void acquireReg(Register reg) {
        if (availableRegisters.contains(reg)) {
            availableRegisters.remove(reg);
        } else {
            throw new IllegalStateException("Trying to acquire already acquired register " + reg);
        }
    }

    public void releaseReg(Register reg) {
        if (availableRegisters.contains(reg)) {
            System.err.println("WARNING: Release not acquired register " + reg);
        } else {
            availableRegisters.add(reg);
        }
    }

    public CompileHint clone() {
        return new CompileHint(this);
    }

    public CompileHint cloneArbitraryReturn() {
        CompileHint hint = new CompileHint(this);
        hint.defaultReturnFlag = false;
        return hint;
    }

    public CompileHint cloneDefaultReturn() {
        CompileHint hint = new CompileHint(this);
        hint.defaultReturnFlag = true;
        return hint;
    }

    public CompileHint clonePreferredReg(Register reg) {
        CompileHint hint = new CompileHint(this);
        hint.preferredReg = reg;
        return hint;
    }
}
