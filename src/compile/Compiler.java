package compile;

import ast.ProgarmNode;

import java.util.ArrayList;

public class Compiler {

    private Tokenizer tokenizer;
    private Parser parser;
    private CodeGenerator codeGenerator;

    private boolean isDebug = false;

    public String compile(String input) throws Exception {
        tokenizer = new Tokenizer();
        parser = new Parser();
        codeGenerator = new CodeGenerator();

        tokenizer.setDebug(isDebug);
        parser.setDebug(isDebug);
        codeGenerator.setDebug(isDebug);

        Debug("【开始词法解析】");
        ArrayList<Token> tokenArrayList = null;

        tokenArrayList = tokenizer.parse(input);
        for (Token token : tokenArrayList) { Debug(token); }
        Debug("【开始语法解析】");
        ProgarmNode ast = null;
        ast = parser.parse(tokenArrayList);
        Debug("【构建抽象语法树】");
        Debug(ast);
        Debug("【生成代码】");
        String newCode = codeGenerator.parse(ast);
        Debug(newCode);

        return newCode;
    }

    public void Debug(Object object) {
        if (isDebug) {
            Output.debugPrintln(object);
        }
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
