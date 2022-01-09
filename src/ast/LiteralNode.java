package ast;

public abstract class LiteralNode extends Node{

    protected final Type type = Type.Literal;
    protected String value;

    @Override
    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LiteralNode{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
