package compile;

public class Token {
    public enum Type {
        /** 前单引号 */
        startSingleQuote,
        /** 后单引号 */
        endSingleQuote,
        /** 前双引号 */
        startDoubleQuote,
        /** 后双引号 */
        endDoubleQuote,
        /** 声明变量 */
        declareVariable,
        /** 声明函数形参 */
        declareFunctionVariable,
        /** 声明函数 */
        declareFunction,
        /** 声明函数结束 */
        declareFunctionEnd,
        /** 函数返回 */
        functionReturn,
        /** 变量命名 */
        variableNamed,
        /** 函数形参命名 */
        functionVariableNamed,
        /** 函数命名 */
        functionNamed,
        /** 数字 */
        number,
        /** 字符串 */
        string,
        /** 二元运算符 */
        binaryOperator,
        /** 名称 */
        name,
        /** 赋值分配 */
        assign,
        /** xx xx 以 xx，获取得的意思 */
        get,
        /** 判断（如果） */
        estimate,
        /** 否则 */
        otherwise,
        /** 结束判断 */
        endIf,
        /** 遍历 */
        traversal,
        /** 结束遍历 */
        endTraversal,
        /** 打印 */
        print,
        /** 打印，结尾换行 */
        println,
        /** 调用函数 */
        call,
        /** 函数传参 */
        callValue,
        /** 传参结束标识 */
        callValueEnd
    }

    private final Type type;
    private final String value;
    private int start;
    private int end;

    public Token(Type type, String value, int start, int end) {
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
