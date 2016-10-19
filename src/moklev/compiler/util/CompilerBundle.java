package moklev.compiler.util;

/**
 * @author Vyacheslav Moklev
 */
public class CompilerBundle {
    public StringBuilder sb;
    public int labelCount;

    public CompilerBundle(StringBuilder sb) {
        this.sb = sb;
        labelCount = 0;
    }

    public String nextLabel() {
        return "L" + labelCount++;
    }
}
