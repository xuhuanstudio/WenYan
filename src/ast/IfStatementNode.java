package ast;

public class IfStatementNode extends Node{

    private final Type type = Type.IfStatement;
    private Node test;
    private Node consequent;
    private Node alternate;

    @Override
    public Type getType() {
        return type;
    }

    public Node getTest() {
        return test;
    }

    public void setTest(Node test) {
        this.test = test;
    }

    public Node getConsequent() {
        return consequent;
    }

    public void setConsequent(Node consequent) {
        this.consequent = consequent;
    }

    public Node getAlternate() {
        return alternate;
    }

    public void setAlternate(Node alternate) {
        this.alternate = alternate;
    }

    @Override
    public String toString() {
        return "IfStatementNode{" +
                "type=" + type +
                ", test=" + test +
                ", consequent=" + consequent +
                ", alternate=" + alternate +
                '}';
    }
}
