package ast;

public class VariableDeclaratorNode extends Node{

    private final Type type = Type.VariableDeclarator;
    private IdentifierNode id;
    private LiteralNode init;

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

    public LiteralNode getInit() {
        return init;
    }

    public void setInit(LiteralNode init) {
        this.init = init;
    }

    @Override
    public String toString() {
        return "VariableDeclaratorNode{" +
                "type=" + type +
                ", id=" + id +
                ", init=" + init +
                '}';
    }
}
