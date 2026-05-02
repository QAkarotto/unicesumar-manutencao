import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DataUtil {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String nowDate() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

    public static String readLine(String label) {
        System.out.print(label);
        return scanner.nextLine();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String nvl(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    public static int askInt(String label, int fallback) {
        int value = toInt(readLine(label));
        return (value < 0) ? fallback : value;
    }

    public static String ask(String label, String fallback) {
        String value = readLine(label);
        return isBlank(value) ? fallback : value;
    }

    public static void printHeader(String title) {
        System.out.println("\n--------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------");
    }

    public static String datePlusDaysApprox(String date, int days) {
        return date + " +" + days;
    }
}