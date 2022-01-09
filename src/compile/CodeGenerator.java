package compile;

import ast.*;

import java.util.HashMap;

public class CodeGenerator {

    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String parse(ProgarmNode ast) throws Exception {
        String newCode = parse((Node) ast);
        if (isDebug) {
            Output.debugPrintln(newCode);
        }
        return newCode;
    }

    public String encodeBinaryOperator(String input) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("加", "+");
        map.put("减", "-");
        map.put("乘", "*");
        map.put("除", "/");
        map.put("模", "%");
        map.put("逾", ">");
        map.put("同", "==");
        map.put("微", "<");
        map.put("不逾", "<=");
        map.put("不同", "!=");
        map.put("不微", ">=");

        String s = map.get(input);
        if (s == null) {
            throw new Exception("异常的二元符");
        } else {
            return s;
        }
    }

    private String parse(Node node) throws Exception {
        switch (node.getType()) {
            case Progarm:{
                StringBuilder progarm = new StringBuilder();
//                progarm.append("此处可以写入预定义的函数等");
                ProgarmNode progarmNode = (ProgarmNode) node;
                for (Node n : progarmNode.getBody()) {
                    progarm.append(parse(n));
                }
                return progarm.toString();
            }
            case StringLiteral:{
                StringLiteralNode stringLiteralNode = (StringLiteralNode) node;
                return String.format("\"%s\"", stringLiteralNode.getValue());
            }
            case NumberLiteral:{
                NumberLiteralNode numberLiteralNode = (NumberLiteralNode) node;
                return "" + NumberLiteralDecoder.parse(numberLiteralNode.getValue());
            }
            case Identifier:{
                IdentifierNode identifierNode = (IdentifierNode) node;
                return IdentifierEncoder.encode(identifierNode.getName());
            }
            case BlockStatement:{
                BlockStatementNode blockStatementNode = (BlockStatementNode) node;
                StringBuilder stringBuilder = new StringBuilder("{");
                for (Node n : blockStatementNode.getBody()) {
                    stringBuilder.append(parse(n));
                }
                stringBuilder.append("}");
                return stringBuilder.toString();
            }
            case ReturnStatement:{
                ReturnStatementNode returnStatementNode = (ReturnStatementNode) node;
                return String.format(";return %s;", parse(returnStatementNode.getArgument()));
            }
            case BinaryExpression:{
                BinaryExpressionNode binaryExpressionNode = (BinaryExpressionNode) node;
                return String.format(
                        " (%s %s %s) ",
                        parse(binaryExpressionNode.getRight()),
                        encodeBinaryOperator(binaryExpressionNode.getOperator()) ,
                        parse(binaryExpressionNode.getLeft())
                );
            }
            case FunctionDeclaration:{
                FunctionDeclarationNode functionDeclarationNode = (FunctionDeclarationNode) node;
                // 遍历获取参数
                StringBuilder parmas = new StringBuilder();
                for (int i = 0, size = functionDeclarationNode.getParmas().size(); i < size; i++) {
                    Node n = functionDeclarationNode.getParmas().get(i);
                    parmas.append(parse(n));
                    if (i < size - 1) {
                        parmas.append(", ");
                    }
                }

                return String.format(
                        ";function %s(%s) %s",
                        parse(functionDeclarationNode.getId()),
                        parmas,
                        parse(functionDeclarationNode.getBody())
                );
            }
            case VariableDeclaration:{
                VariableDeclarationNode variableDeclarationNode = (VariableDeclarationNode) node;

                StringBuilder declarations = new StringBuilder();
                for (int i = 0, size = variableDeclarationNode.getDeclarations().size(); i < size; i++) {
                    Node n = variableDeclarationNode.getDeclarations().get(i);
                    declarations.append(parse(n));
                    if (i < size - 1) {
                        declarations.append(", ");
                    }
                }

                return String.format(
                        ";var %s;",
                        declarations
                );
            }
            case VariableDeclarator:{
                VariableDeclaratorNode variableDeclaratorNode = (VariableDeclaratorNode) node;
                return  String.format(
                        "%s = %s;",
                        parse(variableDeclaratorNode.getId()),
                        parse(variableDeclaratorNode.getInit())
                );
            }
            case CallExpression:{
                CallExpressionNode callExpressionNode = (CallExpressionNode) node;
                // 遍历获取参数
                StringBuilder arguments = new StringBuilder();
                for (int i = 0, size = callExpressionNode.getArguments().size(); i < size; i++) {
                    Node n = callExpressionNode.getArguments().get(i);
                    arguments.append(parse(n));
                    if (i < size - 1) {
                        arguments.append(", ");
                    }
                }

                return String.format(
                        "\n%s(%s) ",
                        parse(callExpressionNode.getCallee()),
                        arguments.toString()
                );
            }
            case IfStatement:{
                IfStatementNode ifStatementNode = (IfStatementNode) node;
                return String.format(
                        ";if (%s) %s%s",
                        parse(ifStatementNode.getTest()),
                        parse(ifStatementNode.getConsequent()),
                        ifStatementNode.getAlternate() == null ? "" : String.format(
                                " else %s",
                                parse(ifStatementNode.getAlternate())
                        )
                );
            }
            case ForInStatment:{
                ForInStatementNode forInStatementNode = (ForInStatementNode) node;
                return String.format(
                  ";for (var %s = 1; %s <= %s; %s++) %s",
                        parse(forInStatementNode.getRight()),
                        parse(forInStatementNode.getRight()),
                        parse(forInStatementNode.getLeft()),
                        parse(forInStatementNode.getRight()),
                        parse(forInStatementNode.getBody())
                );
            }
            case AssignmentExpression:{
                AssignmentExpressionNode assignmentExpressionNode = (AssignmentExpressionNode) node;
                return String.format(
                        "%s %s %s;",
                        parse(assignmentExpressionNode.getLeft()),
                        assignmentExpressionNode.getOperator(),
                        parse(assignmentExpressionNode.getRight())
                );
            }
            default:
                throw new Exception(String.format("解析到异常的节点类型: %s", node.getType()));
        }
    }

}
