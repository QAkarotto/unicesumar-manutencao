package Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Classe utilitária refatorada (Dispensables removidos).
 * Contém apenas os métodos estritamente utilizados pelo sistema após alterações
 */
public class DataUtil {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Melhoria: Utiliza a API moderna do Java (LocalDate) no lugar de SimpleDateFormat e Date.
     */
    public static String nowDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    public static String readLine(String label) {
        System.out.print(label);
        return scanner.nextLine();
    }

    /**
     * Melhoria: Simplificado utilizando métodos nativos mais eficientes.
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static void printSeparator() {
        System.out.println("--------------------------------------------");
    }

    public static void printHeader(String title) {
        printSeparator();
        System.out.println(title);
        printSeparator();
    }

    public static String ask(String label, String fallback) {
        String v = readLine(label);
        return isBlank(v) ? fallback : v;
    }

    /**
     * Melhoria: O tratamento de erro (NumberFormatException) foi trazido para dentro do método,
     * eliminando a necessidade do método 'toInt' redundante que existia no legado.
     */
    public static int askInt(String label, int fallback) {
        String v = readLine(label);
        if (isBlank(v)) {
            return fallback;
        }
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * Melhoria: Substituída a concatenação manual de Strings em loop (+) pelo uso de StringBuilder,
     * evitando vazamento de memória e lentidão (Code Smell: Inefficient String Concatenation).
     */
    public static String repeat(String value, int times) {
        if (times <= 0) return "";
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < times; i++) {
            out.append(value);
        }
        return out.toString();
    }

    /**
     * Melhoria: Substitui a gambiarra legada (date + " +" + days) por um
     * cálculo de data real utilizando a API java.time, mantendo um fallback de segurança.
     */
    public static String datePlusDaysApprox(String dateStr, int days) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            return date.plusDays(days).format(DATE_FORMATTER);
        } catch (Exception e) {
            // Fallback para o comportamento legado caso o formato de entrada seja inválido
            return dateStr + " +" + days;
        }
    }
}