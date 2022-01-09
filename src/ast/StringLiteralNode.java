package ast;

public class StringLiteralNode extends LiteralNode{

    private final Type type = Type.StringLiteral;

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "StringLiteralNode{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
