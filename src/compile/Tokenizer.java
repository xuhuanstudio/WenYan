package compile;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 词法分析器
 */
public class Tokenizer {

    private boolean isDebug = false;

    private Token token = null;
    private ArrayList<Token> tokens = null;

    private String input;
    private int current = 0;

    private int s_current = 0;

    /** 当前配对字符 */
    private String string = null;

    public Token getToken() {
        return token;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    private void clearInvalidCharacter() {
        if (this.current < this.input.length()) {
            // 排除无视的无效字符
            Matcher matcher = Pattern.compile("^((\\s|\\n)*)((，|。)?)((\\s|\\n)*)").matcher(this.input.substring(this.current));
            if (matcher.find()) {
                this.current += matcher.group(0).length();
            }
        }
    }

    private String stringWith(String regex) {
        if (this.current < this.input.length()) {

            clearInvalidCharacter();

//            if (isDebug) {
//                Output.println(this.input.substring(this.current));
//            }

            if (this.current < this.input.length()) {
                Matcher matcher = Pattern.compile("^(" + regex + ")").matcher(this.input.substring(this.current));
                if (matcher.find()) {
                    this.string = matcher.group(0);
                    this.current += this.string.length();
                    this.s_current = this.current;

                    clearInvalidCharacter();

                    return this.string;
                }
            }
        }

        return null;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public void addToken(Token token) {
        if (this.tokens == null) this.tokens = new ArrayList<Token>();
        this.token = token;
        this.tokens.add(token);
    }

    public ArrayList<Token> parse (String input) throws Exception {

        this.input = input;

        // 去除无效 空格 逗号 句号 换行 (由于会改变原有代码，导致定位错误时会出现不必要的麻烦，故而不使用该方法)
        // this.input = this.input.replaceAll("(\\s|，|。|\\n)+", "");

        while (current < this.input.length()) {

            // 单引号 标识名
            if (stringWith("「") != null) {
                addToken(new Token(Token.Type.startSingleQuote, this.string,
                        this.s_current - this.string.length(), this.s_current));

                if (stringWith("[^」]+") != null) {
                    addToken(new Token(Token.Type.name, this.string,
                            this.s_current - this.string.length(), this.s_current));

                    if (stringWith("」") != null) {
                        addToken(new Token(Token.Type.endSingleQuote, this.string,
                                this.s_current - this.string.length(), this.s_current));
                        continue;
                    }
                    continue;
                }
                continue;
            }

            // 双引号 字符串
            if (stringWith("『") != null) {
                addToken(new Token(Token.Type.startDoubleQuote, this.string,
                        this.s_current - this.string.length(), this.s_current));

                if (stringWith("[^』]+") != null) {
                    addToken(new Token(Token.Type.string, this.string,
                            this.s_current - this.string.length(), this.s_current));

                    if (stringWith("』") != null) {
                        addToken(new Token(Token.Type.endDoubleQuote, this.string,
                                this.s_current - this.string.length(), this.s_current));
                        continue;
                    }
                    continue;
                }
                continue;
            }

            // 二元运算符
            if (stringWith("((加|减|乘|除)|((不)?(逾|微|同)))") != null) {
                addToken(new Token(Token.Type.binaryOperator, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 声明变量
            if (stringWith("吾有一数") != null) {
                addToken(new Token(Token.Type.declareVariable, this.string,
                        this.s_current - this.string.length(), this.s_current));
                // 变量命名
                if (stringWith("名曰为") != null) {
                    addToken(new Token(Token.Type.variableNamed, this.string,
                            this.s_current - this.string.length(), this.s_current));
                    continue;
                }
                continue;
            }

            // 声明函数
            if (stringWith("吾有一术") != null) {
                addToken(new Token(Token.Type.declareFunction, this.string,
                        this.s_current - this.string.length(), this.s_current));
                // 函数命名
                if (stringWith("名曰为") != null) {
                    addToken(new Token(Token.Type.functionNamed, this.string,
                            this.s_current - this.string.length(), this.s_current));
                    continue;
                }
                continue;
            }

            // 声明形参
            if (stringWith("预行此术") != null) {
                addToken(new Token(Token.Type.declareFunctionVariable, this.string,
                        this.s_current - this.string.length(), this.s_current));
                if (stringWith("先得术引") != null) {
                    addToken(new Token(Token.Type.declareFunctionVariable, this.string,
                            this.s_current - this.string.length(), this.s_current));
                }
                continue;
            }
            // 函数形参命名
            if (stringWith("曰") != null) {
                addToken(new Token(Token.Type.functionVariableNamed, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 函数返回值
            if (stringWith("乃得") != null) {
                addToken(new Token(Token.Type.functionReturn, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            if (stringWith("是谓术也") != null) {
                addToken(new Token(Token.Type.declareFunctionEnd, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 遍历
            if (stringWith("遍之以") != null) {
                addToken(new Token(Token.Type.traversal, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 遍历终止
            if (stringWith("尔尔") != null) {
                addToken(new Token(Token.Type.endTraversal, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 数字
            if (stringWith("(零|一|二|三|四|五|六|七|八|九|十|百|千|万)+(点(零|一|二|三|四|五|六|七|八|九)+)?") != null) {
                addToken(new Token(Token.Type.number, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 判断
            if (stringWith("若") != null) {
                addToken(new Token(Token.Type.estimate, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 条件终止标识
//            if (stringWith("者") != null) {
//                addToken(new Token(Token.Type.estimate, this.string));
//                continue;
//            }

            // 否则
            if (stringWith("非此道") != null) {
                addToken(new Token(Token.Type.otherwise, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 判断终止
            if (stringWith("云云") != null) {
                addToken(new Token(Token.Type.endIf, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 打印
            if (stringWith("书之(以?)") != null) {
                addToken(new Token(Token.Type.print, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }
            // 打印行
            if (stringWith("(书行(以?)|以行书(之?))") != null) {
                addToken(new Token(Token.Type.println, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 运行函数
            if (stringWith("施术以") != null || stringWith("施术") != null) {
                addToken(new Token(Token.Type.call, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 赋值
            if (stringWith("施") != null) {
                addToken(new Token(Token.Type.assign, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            if (stringWith("以") != null) {
                addToken(new Token(Token.Type.get, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 函数传参
            if (stringWith("予以") != null) {
                addToken(new Token(Token.Type.callValue, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            // 传参结束
            if (stringWith("驱之") != null) {
                addToken(new Token(Token.Type.callValueEnd, this.string,
                        this.s_current - this.string.length(), this.s_current));
                continue;
            }

            throw new Exception(String.format("异常: %s, 错误位置：%d", this.input.substring(current), current));

        }

        return tokens;

    }

}

