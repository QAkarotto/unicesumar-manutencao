import java.util.*;

public class LegacyDatabase {

    private static final Map<Integer, Map<String, Object>> books = new HashMap<>();
    private static final Map<Integer, Map<String, Object>> users = new HashMap<>();
    private static final List<Map<String, Object>> loans = new ArrayList<>();
    private static final List<String> logs = new ArrayList<>();

    private static int BOOK_SEQ = 1;

    public static int addBookData(String title, String author, int year,
                                  String category, int totalCopies,
                                  int availableCopies, String shelfCode, String isbn) {

        Map<String, Object> data = new HashMap<>();
        int id = BOOK_SEQ++;

        data.put("id", id);
        data.put("title", title);
        data.put("author", author);
        data.put("year", year);
        data.put("category", category);
        data.put("totalCopies", totalCopies);
        data.put("availableCopies", availableCopies);
        data.put("shelfCode", shelfCode);
        data.put("isbn", isbn);

        books.put(id, data);
        logs.add("book-added-" + id);

        return id;
    }

    public static Map<String, Object> getBookById(int id) {
        return books.get(id);
    }

    public static Map<Integer, Map<String, Object>> getBooks() {
        return books;
    }

    public static void addLog(String log) {
        logs.add(log);
    }

    public static void printLogs() {
        logs.forEach(System.out::println);
    }

    // FIX BUG
    public static int countOpenLoansByBook(int bookId) {
        int count = 0;

        for (Map<String, Object> loan : loans) {
            if ((int) loan.get("bookId") == bookId &&
                    "OPEN".equals(loan.get("status"))) {
                count++;
            }
        }

        return count;
    }
}