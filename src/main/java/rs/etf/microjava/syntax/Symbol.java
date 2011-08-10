package rs.etf.microjava.syntax;

public class Symbol {

    String name;
    Type type;
    Scope scope;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Type type) {
        this(name);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        if (type != null) {
            return '<' + getName() + ":" + type + '>';
        }
        return getName();
    }
}
