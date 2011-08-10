/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rs.etf.microjava.parser;

import java.io.IOException;
import java.io.StringReader;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.testng.annotations.Test;

/**
 *
 * @author luka
 */
public class ParserTest {

    @Test
    public void designatorTest() throws Exception {
        
        getParser("designator").designator();
        getParser("des123").designator();
        getParser("struct.field").designator();
        getParser("array[5]").designator();
        getParser("array [1 + 3]").designator();
        getParser("a4[x]").designator();
        getParser("struct.arr[3]").designator();
        getParser("arr[x.index]").designator();
        getParser("a[s.a[1 *4]]").designator();
        getParser("s1.a1[5].s2.a2[x % 2].s3").designator();

    }

    @Test
    public void factor() throws Exception {

        getParser("des_factor").factor();
        getParser("15").factor();
        getParser("'x'").factor();
        getParser("call()").factor();
        getParser("call(5)").factor();
        getParser("f(1 - 3, x)").factor();
        getParser("new Type").factor();
        getParser("new int[5]").factor();
        getParser("new arr[size]").factor();
        getParser("(2 * 5)").factor();

    }


    private MicroJavaParser getParser(String input) throws IOException {
        StringReader sr = new StringReader(input);
        CharStream in =  new ANTLRReaderStream(sr);
        MicroJavaLexer lex = new MicroJavaLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        MicroJavaParser p = new MicroJavaParser(tokens);
        return p;
    }

}
