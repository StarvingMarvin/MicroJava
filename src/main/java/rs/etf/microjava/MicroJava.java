/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.microjava;

import rs.etf.microjava.syntax.SymbolTable;
import java.io.IOException;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import rs.etf.microjava.parser.MicroJavaLexer;
import rs.etf.microjava.parser.MicroJavaParser;
import rs.etf.microjava.parser.MicroJavaTree;

/**
 *
 * @author luka
 */
public class MicroJava {

    public static void main(String[] args) throws IOException, RecognitionException {
        CharStream input = null;
        if (args.length > 0) {
            input = new ANTLRFileStream(args[0]);
        } else {
            input = new ANTLRInputStream(System.in);
        }
        MicroJavaLexer lex = new MicroJavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        MicroJavaParser p = new MicroJavaParser(tokens);
        RuleReturnScope r = p.program();   // launch parser
        CommonTree t = (CommonTree) r.getTree();    // get tree result
        //System.out.println("tree: "+t.toStringTree());
        //DOTTreeGenerator dot = new DOTTreeGenerator();
        //System.out.println(dot.toDOT(t));

        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
        nodes.setTokenStream(tokens);
        SymbolTable symtab = new SymbolTable();
        MicroJavaTree def = new MicroJavaTree(nodes, symtab); // use custom constructor
        def.program(symtab); // trigger symtab actions upon certain subtrees
        System.out.println("globals: " + symtab.getCurrentScope());
    }
}
