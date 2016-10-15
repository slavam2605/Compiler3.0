import moklev.compiler.parsing.LangLexer;
import moklev.compiler.parsing.LangParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PrimitiveIterator;

/**
 * @author Moklev Vyacheslav
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ANTLRInputStream is = new ANTLRInputStream(new FileInputStream("sample.src"));
        LangParser parser = new LangParser(new CommonTokenStream(new LangLexer(is)));
        System.out.println(parser.file().s);
        new PrintWriter(new FileWriter("sample.asm"), true).println(parser.file().s);
    }
}
