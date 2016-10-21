import moklev.compiler.parsing.LangLexer;
import moklev.compiler.parsing.LangParser;
import moklev.compiler.processing.AsmPostprocessor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Moklev Vyacheslav
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ANTLRInputStream is = new ANTLRInputStream(new FileInputStream("sample.src"));
        LangParser parser = new LangParser(new CommonTokenStream(new LangLexer(is)));
        String asmSource = AsmPostprocessor.process(parser.file().s);
        System.out.println(asmSource);
        new PrintWriter(new FileWriter("sample.asm"), true).println(asmSource);
    }
}
