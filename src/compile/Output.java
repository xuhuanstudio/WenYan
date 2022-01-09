package compile;

import java.text.DecimalFormat;

public class Output {

    static DecimalFormat decimalFormat = new DecimalFormat("###.##");

    public void print(Object value) {
        Output.debugPrint(value);
    }

    public void println(Object value) {
        Output.debugPrintln(value);
    }

    public static void debugPrint(Object value) {
        if (value instanceof Float || value instanceof Double) {
            value = decimalFormat.format(value);
        }

        EventManager.event(Event.Print, value);
        System.out.print(value);
    }

    public static void debugPrintln(Object value) {
        if (value instanceof Float || value instanceof Double) {
            value = decimalFormat.format(value);
        }

        EventManager.event(Event.Println, value);
        System.out.println(value);
    }

}
