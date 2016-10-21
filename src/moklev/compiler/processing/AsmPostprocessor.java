package moklev.compiler.processing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Moklev Vyacheslav
 */
public class AsmPostprocessor {
    public static String process(String s) {
        return removeUselessLabels(
                removeRedundantJumps(s)
        );
    }

    private static String removeUselessLabels(String s) {
        StringBuilder sb = new StringBuilder();
        String[] lines = s.split("\n");
        Set<String> usedLabels = new HashSet<>();
        for (String line: lines) {
            if (line.startsWith("L"))
                continue;
            List<String> ids = getIds(line);
            usedLabels.addAll(ids);
        }
        for (String line: lines) {
            if (line.startsWith("L")) {
                String label = line.substring(0, line.indexOf(':'));
                if (usedLabels.contains(label)) {
                    sb.append(line).append('\n');
                }
            } else {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private static List<String> getIds(String s) {
        List<String> ids = new ArrayList<>();
        Matcher matcher = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_]*").matcher(s);
        int pos = 0;
        while (pos < s.length()) {
            if (!matcher.find(pos))
                break;
            ids.add(matcher.group());
            pos = matcher.end();
        }
        return ids;
    }

    private static String removeRedundantJumps(String s) {
        StringBuilder sb = new StringBuilder();
        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().startsWith("jmp ") && i < lines.length - 1) {
                String label = lines[i].trim().substring(4).trim();
                if (!lines[i + 1].startsWith(label + ":"))
                    sb.append(lines[i]).append('\n');
            } else {
                sb.append(lines[i]).append('\n');
            }
        }
        return sb.toString();
    }
}
