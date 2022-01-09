package ast;

public class ForInStatementNode extends Node{

    private final Type type = Type.ForInStatment;
    private Node left;
    private IdentifierNode right;
    private Node body;

    @Override
    public Type getType() {
        return type;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public IdentifierNode getRight() {
        return right;
    }

    public void setRight(IdentifierNode right) {
        this.right = right;
    }

    public Node getBody() {
        return body;
    }

    public void setBody(Node body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ForInStatementNode{" +
                "type=" + type +
                ", left=" + left +
                ", right=" + right +
                ", body=" + body +
                '}';
    }
}
