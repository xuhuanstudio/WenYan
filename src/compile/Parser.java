package compile;

import ast.*;

import java.util.ArrayList;

/** 语法分析器 */
public class Parser {
    private boolean isDebug = false;

    private ArrayList<Token> input;
    private int current = 0;

    /** 当前正在匹配的token */
    private Token token = null;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public ProgarmNode parse(ArrayList<Token> input) throws Exception {
        this.input = input;

        ProgarmNode ast = new ProgarmNode();

        while(this.current < this.input.size()) {
            ast.getBody().add(with());
        }

        return ast;
    }

    /**
     * 根据匹配类型的Token进步
     * @param type 匹配的类型
     * @return 匹配的Token
     */
    private Token tokenWith(Token.Type type) {
        if (this.current < this.input.size()) {
            Token token = this.input.get(this.current);
            if (token.getType() == type) {
                this.token = token;
                if (isDebug) {
                    Output.debugPrintln(String.format("解析完成: %s", token));
                }
                this.current++;
                return token;
            }
        }

        return null;
    }

    /**
     * 标识符进步
     * @return 标识符节点
     */
    private IdentifierNode identifierTokenWith() {
        Token nameToken = null;
        if (
            tokenWith(Token.Type.startSingleQuote) != null &&
            (nameToken = tokenWith(Token.Type.name)) != null &&
            tokenWith(Token.Type.endSingleQuote) != null
        ) {
            IdentifierNode identifierNode = new IdentifierNode();
            identifierNode.setName(nameToken.getValue());
            return identifierNode;
        }

        return null;
    }

    /**
     * 字符串字面量进步
     * @return 字符串字面量节点
     */
    private StringLiteralNode stringLiteralTokenWith() {
        Token stringLiteralToken = null;
        if (
            tokenWith(Token.Type.startDoubleQuote) != null &&
            (stringLiteralToken = tokenWith(Token.Type.string)) != null &&
            tokenWith(Token.Type.endDoubleQuote) != null
        ) {
            StringLiteralNode stringLiteralNode = new StringLiteralNode();
            stringLiteralNode.setValue(stringLiteralToken.getValue());
            return stringLiteralNode;
        }

        return null;
    }

    /**
     * 数字字面量进步
     * @return 数字字面量节点
     */
    private NumberLiteralNode numberLiteralTokenWith() {
        if (tokenWith(Token.Type.number) != null) {
            NumberLiteralNode numberLiteralNode = new NumberLiteralNode();
            numberLiteralNode.setValue(this.token.getValue());
            return numberLiteralNode;
        }

        return null;
    }

    /**
     * 二元运算
     * @return 二元运算节点
     */
    private BinaryExpressionNode binaryExpressionNodeTokenWith() throws Exception {
        if (tokenWith(Token.Type.binaryOperator) != null) {
            BinaryExpressionNode binaryExpressionNode = new BinaryExpressionNode();
            binaryExpressionNode.setOperator(this.token.getValue());

            binaryExpressionNode.setLeft(with());
            if (tokenWith(Token.Type.get) != null) {
                binaryExpressionNode.setRight(with());

                return binaryExpressionNode;
            }
        }
        return null;
    }

    private Node with() throws Exception {
        // 声明变量
        if (tokenWith(Token.Type.declareVariable) != null && tokenWith(Token.Type.variableNamed) != null) {
            VariableDeclarationNode variableDeclarationNode = new VariableDeclarationNode();
            VariableDeclaratorNode variableDeclaratorNode = new VariableDeclaratorNode();
            variableDeclarationNode.getDeclarations().add(variableDeclaratorNode);

            IdentifierNode identifierNode = identifierTokenWith();
            if (identifierNode != null) {
                variableDeclaratorNode.setId(identifierNode);
                NumberLiteralNode numberLiteralNode = new NumberLiteralNode();
                numberLiteralNode.setValue("零");
                variableDeclaratorNode.setInit(numberLiteralNode);
                return variableDeclarationNode;
            }
        }

        // 声明函数
        if (tokenWith(Token.Type.declareFunction) != null && tokenWith(Token.Type.functionNamed) != null) {
            FunctionDeclarationNode functionDeclarationNode = new FunctionDeclarationNode();

            // 函数名
            IdentifierNode identifierNode = identifierTokenWith();

            if (identifierNode != null) {
                functionDeclarationNode.setId(identifierNode);

                // 形参名
                if (
                        tokenWith(Token.Type.declareFunctionVariable) != null &&
                        this.token.getValue().equals("预行此术") &&
                        tokenWith(Token.Type.declareFunctionVariable) != null &&
                        this.token.getValue().equals("先得术引")
                ) {
                    while (tokenWith(Token.Type.functionVariableNamed) != null) {
                        identifierNode = identifierTokenWith();
                        functionDeclarationNode.getParmas().add(identifierNode);
                    }
                }

                // 获取函数体
                BlockStatementNode blockStatementNode = new BlockStatementNode();
                while (tokenWith(Token.Type.declareFunctionEnd) == null) {
                    blockStatementNode.getBody().add(with());
                }

                functionDeclarationNode.setBody(blockStatementNode);

                return functionDeclarationNode;

            }
        }

        // 标识符
        {
            IdentifierNode identifierNode = identifierTokenWith();
            if (identifierNode != null) {
                return identifierNode;
            }
        }

        // 字符串字面量
        {
            StringLiteralNode stringLiteralNode = stringLiteralTokenWith();
            if (stringLiteralNode != null) {
                return stringLiteralNode;
            }
        }

        // 数字字面量
        if (tokenWith(Token.Type.number) != null) {
            NumberLiteralNode numberLiteralNode = new NumberLiteralNode();
            numberLiteralNode.setValue(this.token.getValue());
            return  numberLiteralNode;
        }

        // 二元运算
        {
            BinaryExpressionNode binaryExpressionNode = binaryExpressionNodeTokenWith();
            if (binaryExpressionNode != null) {
                return binaryExpressionNode;
            }
        }

        // forin
        if (tokenWith(Token.Type.traversal) != null) {
            ForInStatementNode forInStatementNode = new ForInStatementNode();
            Node node;
            if ((node = identifierTokenWith()) != null || (node = numberLiteralTokenWith()) != null) {
                forInStatementNode.setLeft(node);

                // 这里自动为其声明各变量（计），来记录计数值，从一开始
                IdentifierNode identifierNode = new IdentifierNode();
                identifierNode.setName("计");
                forInStatementNode.setRight(identifierNode);

                // 获取遍历体
                BlockStatementNode blockStatementNode = new BlockStatementNode();
                while(tokenWith(Token.Type.endTraversal) == null) {
                    blockStatementNode.getBody().add(with());
                }

                forInStatementNode.setBody(blockStatementNode);
                return forInStatementNode;
            }
        }

        // if
        if (tokenWith(Token.Type.estimate) != null) {
            IfStatementNode ifStatementNode = new IfStatementNode();

            BinaryExpressionNode binaryExpressionNode = binaryExpressionNodeTokenWith();
            if (binaryExpressionNode != null) {
                ifStatementNode.setTest(binaryExpressionNode);

                BlockStatementNode blockStatementNode = new BlockStatementNode();
                // 遇到循环结束或else
                while(tokenWith(Token.Type.endIf) == null && tokenWith(Token.Type.otherwise) == null) {
                    blockStatementNode.getBody().add(with());
                }
                ifStatementNode.setConsequent(blockStatementNode);

                // 从else开始到循环结束，注意，这里并不是使用tokenWith
                if (this.token.getType() == Token.Type.otherwise) {
                    blockStatementNode = new BlockStatementNode();
                    while(tokenWith(Token.Type.endIf) == null) {
                        blockStatementNode.getBody().add(with());
                    }
                    ifStatementNode.setAlternate(blockStatementNode);
                }
                return ifStatementNode;
            }
        }

        // 返回值
        if (tokenWith(Token.Type.functionReturn) != null) {
            ReturnStatementNode returnStatementNode = new ReturnStatementNode();
            Node node = with();
            // 仅支持数值
            if (
                node instanceof NumberLiteralNode ||
                node instanceof StringLiteralNode ||
                node instanceof IdentifierNode ||
                node instanceof CallExpressionNode ||
                node instanceof BinaryExpressionNode
            ) {
                returnStatementNode.setArgument(node);
                return returnStatementNode;
            }
        }

        //非以上几种情况中，且独自为行时，外层多嵌套一层ExpressionStatmentNode
        //ExpressionStatmentNode expressionStatmentNode = new ExpressionStatmentNode();
        // 由于我们忽略换行的作用，所以无视该层级包装

        // 调用函数
        if (tokenWith(Token.Type.call) != null) {
            IdentifierNode identifierNode = identifierTokenWith();
            if (identifierNode != null) {
                CallExpressionNode callExpressionNode = new CallExpressionNode();
                callExpressionNode.setCallee(identifierNode);

                if (tokenWith(Token.Type.callValue) != null) {
                    while (tokenWith(Token.Type.callValueEnd) == null) {
                        callExpressionNode.getArguments().add(with());
                    }
                }
                return callExpressionNode;
            }
        }

        // 赋值
        if (tokenWith(Token.Type.assign) != null) {
            AssignmentExpressionNode assignmentExpressionNode = new AssignmentExpressionNode();

            IdentifierNode identifierNode = identifierTokenWith();
            if (identifierNode != null) {
                assignmentExpressionNode.setLeft(identifierNode);

                assignmentExpressionNode.setOperator("=");

                if (tokenWith(Token.Type.get) != null) {
                    Node node = with();
                    if (
                        node instanceof NumberLiteralNode ||
                        node instanceof StringLiteralNode ||
                        node instanceof IdentifierNode ||
                        node instanceof CallExpressionNode ||
                        node instanceof BinaryExpressionNode
                    ) {
                        assignmentExpressionNode.setRight(node);
                        return  assignmentExpressionNode;
                    }
                }
            }
        }

        // 调用自带方法
        {
            CallExpressionNode callExpressionNode = new CallExpressionNode();

            // 打印
            if (tokenWith(Token.Type.print) != null) {
                IdentifierNode identifierNode = new IdentifierNode();
                identifierNode.setName("打印");
                callExpressionNode.setCallee(identifierNode);

                Node node = with();
                if (
                    node instanceof NumberLiteralNode ||
                    node instanceof StringLiteralNode ||
                    node instanceof IdentifierNode ||
                    node instanceof CallExpressionNode
                ) {
                    callExpressionNode.getArguments().add(node);
                    return callExpressionNode;
                }
            }
            // 打印行
            if (tokenWith(Token.Type.println) != null) {
                IdentifierNode identifierNode = new IdentifierNode();
                identifierNode.setName("打印行");
                callExpressionNode.setCallee(identifierNode);

                Node node = with();
                if (
                    node instanceof NumberLiteralNode ||
                    node instanceof StringLiteralNode ||
                    node instanceof IdentifierNode ||
                    node instanceof CallExpressionNode
                ) {
                    callExpressionNode.getArguments().add(node);
                    return callExpressionNode;
                }
            }
        }

        throw new Exception(String.format("异常: %s", this.token));
//        return null;
    }

}
