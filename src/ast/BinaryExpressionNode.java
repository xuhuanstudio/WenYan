package ast;

public class BinaryExpressionNode extends Node{

    private final Type type = Type.BinaryExpression;
    private Node left;
    private String operator;
    private Node right;

    @Override
    public Type getType() {
        return type;
    }

    public Node getLeft() { return left; }

    public void setLeft(Node left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{" +
                "type=" + type +
                ", left=" + left +
                ", operator='" + operator + '\'' +
                ", right=" + right +
                '}';
    }
}
