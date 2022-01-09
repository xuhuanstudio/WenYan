package ast;

import java.util.ArrayList;

public class VariableDeclarationNode extends Node{

    private final Type type = Type.VariableDeclaration;
    private ArrayList<VariableDeclaratorNode> declarations = new ArrayList<VariableDeclaratorNode>();

    @Override
    public Type getType() {
        return type;
    }

    public ArrayList<VariableDeclaratorNode> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(ArrayList<VariableDeclaratorNode> declarations) {
        this.declarations = declarations;
    }

    @Override
    public String toString() {
        return "VariableDeclarationNode{" +
                "type=" + type +
                ", declarations=" + declarations +
                '}';
    }
}
