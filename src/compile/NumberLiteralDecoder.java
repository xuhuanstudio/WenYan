package compile;

public class NumberLiteralDecoder {

//    private final static String[] Symbols = {"", "负"};

    private final static String[] Numbers = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private final static String[] Units = {"", "十", "百", "千", "万", "亿"};

    public static double parse(String string) {

        if (string == null) return 0;

        int symbol = 1;
        if (string.startsWith("正")) {
            symbol = 1;
            string = string.substring(1);
        } else if (string.startsWith("负")) {
            symbol = -1;
            string = string.substring(1);
        }

        double value = 0;

        int lastUIndex = Units.length;
        int nIndex = -1;

        if (string.startsWith("十")) {
            string = "一" + string;
        }

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);

            int uIndex = indexOfUnit(character);

            if (uIndex >= 0) {

                // 如果后面的单位大于前面的单位
                if (uIndex >= lastUIndex) {
                    if (i > 1) {
                        int n = indexOfNumber(string.charAt(i - 1));
                        if (n >= 0) { value += n; }
                    }
                    value *= Math.pow(10, uIndex);
                } else {
                    if (nIndex >= 0) {
                        value += nIndex * Math.pow(10, uIndex);
                    }
                }

                lastUIndex = uIndex;
            } else {
                nIndex = indexOfNumber(character);
                if (i == string.length() - 1 && nIndex >= 0) {
                    value += nIndex;
                }
            }
        }

        value *= symbol;

        return value;

    }

    private static int indexOfNumber(char character) {
        return indexOfArray(character, Numbers);
    }

    private static int indexOfUnit(char character) {
        return indexOfArray(character, Units);
    }

    private static int indexOfArray(char character, String[] array) {
        return indexOfArray(character + "", array);
    }

    private static int indexOfArray(String character, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (character.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

}
