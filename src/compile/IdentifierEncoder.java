package compile;

public class IdentifierEncoder {

    public static String encode(String input) {
        switch (input){
            case "打印":{
                return "output.print";
            }
            case "打印行":{
                return "output.println";
            }
            default:{
                char[] charArray = input.toCharArray();
                StringBuilder string = new StringBuilder();
                for (char character : charArray) {
                    string.append("_").append((int) character);
                }
                return string.toString();
            }
        }
    }

}
