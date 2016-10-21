package moklev.compiler.util;

/**
 * @author Vyacheslav Moklev
 */
public class CompilerBundle {
    public StringBuilder sb;
    public Scope scope;
    public int labelCount;

    public CompilerBundle(StringBuilder sb) {
        this.sb = sb;
        labelCount = 0;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String nextLabel() {
        return "L" + labelCount++;
    }
}
