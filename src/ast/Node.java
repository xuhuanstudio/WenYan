package ast;

public abstract class Node {

    public enum Type {
        Node,
        Empty,
        /** 程序 */
        Progarm,
        /** 声明变量 */
        VariableDeclaration,
        /** 单个变量声明 */
        VariableDeclarator,
        /** 声明函数 */
        FunctionDeclaration,
        /** 块表达式 */
        BlockStatement,
        /** 标识符 */
        Identifier,
        /**  */
        IfStatement,
        /** 遍历 */
        ForInStatment,
        /** 表达式语句 */
        ExpressionStatment,
        /** 函数返回语句 */
        ReturnStatement,
        /** 二元运算 */
        BinaryExpression,
        /** 字面量 */
        Literal,
        /** 字符串字面量 */
        StringLiteral,
        /** 数字字面量 */
        NumberLiteral,
        /** 调用函数 */
        CallExpression,
        /** 赋值符 */
        AssignmentExpression
    }

    protected Type type;

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Node{" +
                "type=" + type +
                '}';
    }
}
