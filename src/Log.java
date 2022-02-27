import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static final String[] colors = {"\u001B[0m", "\u001B[30m", "\u001B[31m",
            "\u001B[32m", "\u001B[33m", "\u001B[34m",
            "\u001B[35m", "\u001B[36m", "\u001B[37m"};

    public enum Color {
        DEFAULT,
        BLACK,
        RED,
        GREEN,
        YELLOW,
        BLUE,
        PURPLE,
        CYAN,
        WHITE
    }

    public enum MessageType {
        System,
        Info,
        Event,
        ERROR,
    }

    private static String getMessageType(MessageType type) {
        String typeString = "[" + type.toString() + "]";
        return colors[Color.WHITE.ordinal()] + typeString;
    }

    private static String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = "[" + formatter.format(date) + "]";
        return colors[Color.WHITE.ordinal()] + dateString;
    }

    public static void print(String message, MessageType type, Color color) {
        String pre = getMessageType(type) + getDate();
        System.out.print(pre + " ");
        System.out.println(colors[color.ordinal()] + message);
    }
}
