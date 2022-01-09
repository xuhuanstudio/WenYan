package ast;

public class AssignmentExpressionNode extends Node{

    private final Type type = Type.AssignmentExpression;
    private String operator = "=";
    private IdentifierNode left;
    private Node right;

    @Override
    public Type getType() {
        return type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public IdentifierNode getLeft() {
        return left;
    }

    public void setLeft(IdentifierNode left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "AssignmentExpressionNode{" +
                "type=" + type +
                ", operator='" + operator + '\'' +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
