package ast;

import java.util.ArrayList;

public class CallExpressionNode extends Node{

    private final Type type = Type.CallExpression;
    private IdentifierNode callee;
    private ArrayList<Node> arguments = new ArrayList<Node>();

    @Override
    public Type getType() {
        return type;
    }

    public IdentifierNode getCallee() {
        return callee;
    }

    public void setCallee(IdentifierNode callee) {
        this.callee = callee;
    }

    public ArrayList<Node> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<Node> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "CallExpressionNode{" +
                "type=" + type +
                ", callee=" + callee +
                ", arguments=" + arguments +
                '}';
    }
}
