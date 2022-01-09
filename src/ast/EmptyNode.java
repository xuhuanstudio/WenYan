package ast;

public class EmptyNode extends Node{

    private final Type type = Type.Empty;

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "EmptyNode{" +
                "type=" + type +
                '}';
    }
}
