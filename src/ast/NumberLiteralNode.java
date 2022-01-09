package ast;

public class NumberLiteralNode extends LiteralNode{

    private final Type type = Type.NumberLiteral;

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "NumberLiteralNode{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
