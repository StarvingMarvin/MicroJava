package rs.etf.microjava.syntax;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements Scope {

    GlobalScope globals = new GlobalScope();
    Map<String, Symbol> symbols = new HashMap<String, Symbol>();

    public SymbolTable() {
        initTypeSystem();
    }

    protected void initTypeSystem() {
        globals.define(new BuiltInTypeSymbol("int"));
        globals.define(new BuiltInTypeSymbol("char"));
        globals.define(new BuiltInTypeSymbol("void"));
    }

    public String toString() {
        return globals.toString();
    }

    @Override
    public String getScopeName() {
        return "";
    }

    @Override
    public Scope getEnclosingScope() {
        return globals;
    }

    @Override
    public void define(Symbol sym) {
        symbols.put(sym.getName(), sym);
    }

    @Override
    public Symbol resolve(String name) {
        Symbol local = symbols.get(name);
        if (local != null) {
            return local;
        }
        return globals.resolve(name);
    }

    public Scope getCurrentScope() {
        return globals;
    }
}
