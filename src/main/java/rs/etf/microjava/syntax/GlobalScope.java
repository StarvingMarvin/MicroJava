package rs.etf.microjava.syntax;

public class GlobalScope extends BaseScope {
    public GlobalScope() {
        super(null);
    }

    public String getScopeName() { 
        return "global";
    }
}
