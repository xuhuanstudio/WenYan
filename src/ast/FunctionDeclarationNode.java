package ast;

import java.util.ArrayList;

public class FunctionDeclarationNode extends Node{

    private final Type type = Type.FunctionDeclaration;
    private IdentifierNode id;
    private ArrayList<IdentifierNode> parmas = new ArrayList<IdentifierNode>();
    private BlockStatementNode body;

    @Override
    public Type getType() {
        return type;
    }

    public IdentifierNode getId() {
        return id;
    }

    public void setId(IdentifierNode id) {
        this.id = id;
    }

    public ArrayList<IdentifierNode> getParmas() {
        return parmas;
    }

    public void setParmas(ArrayList<IdentifierNode> parmas) {
        this.parmas = parmas;
    }

    public BlockStatementNode getBody() {
        return body;
    }

    public void setBody(BlockStatementNode body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "FunctionDeclarationNode{" +
                "type=" + type +
                ", id=" + id +
                ", parmas=" + parmas +
                ", body=" + body +
                '}';
    }
}
