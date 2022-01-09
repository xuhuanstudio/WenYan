package ast;

public class IdentifierNode extends Node{

    private final Type type = Type.Identifier;
    private String name;

    @Override
    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdentifierNode{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
