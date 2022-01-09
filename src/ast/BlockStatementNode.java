package ast;

import java.util.ArrayList;

public class BlockStatementNode extends Node{

    private final Type type = Type.BlockStatement;
    private ArrayList<Node> body = new ArrayList<Node>();

    @Override
    public Type getType() {
        return type;
    }

    public ArrayList<Node> getBody() {
        return body;
    }

    public void setBody(ArrayList<Node> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "BlockStatementNode{" +
                "type=" + type +
                ", body=" + body +
                '}';
    }
}
