package ast;

public class ExpressionStatmentNode extends Node{

    private final Type type = Type.ExpressionStatment;
    private Node expression;

    @Override
    public Type getType() {
        return type;
    }

    public Node getExpression() {
        return expression;
    }

    public void setExpression(Node expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExpressionStatmentNode{" +
                "type=" + type +
                ", expression=" + expression +
                '}';
    }
}
