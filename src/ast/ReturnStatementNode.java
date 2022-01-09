package ast;

public class ReturnStatementNode extends Node{

    private final Type type = Type.ReturnStatement;
    private Node argument;

    @Override
    public Type getType() {
        return type;
    }

    public Node getArgument() {
        return argument;
    }

    public void setArgument(Node argument) {
        this.argument = argument;
    }

    @Override
    public String toString() {
        return "ReturnStatementNode{" +
                "type=" + type +
                ", argument=" + argument +
                '}';
    }
}
