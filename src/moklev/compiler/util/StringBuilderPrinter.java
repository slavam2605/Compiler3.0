package moklev.compiler.util;

/**
 * @author Moklev Vyacheslav
 */
public class StringBuilderPrinter {
    private static final String TAB = "    ";

    public static void nprintln(StringBuilder sb, Object... s) {
        for (Object value : s) sb.append(value);
        sb.append('\n');
    }

    public static void println(StringBuilder sb, Object... s) {
        sb.append(TAB);
        for (Object value : s) sb.append(value);
        sb.append('\n');
    }

    public static void nprint(StringBuilder sb, Object s) {
        sb.append(s);
    }

    public static void print(StringBuilder sb, Object s) {
        sb.append(TAB).append(s);
    }
}
