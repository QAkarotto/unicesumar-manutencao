
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookManager {

    // MAINTENANCE NOTE:
    // This method mixes validation, defaults, persistence and logging.
    // Consider splitting it into smaller methods.
    public int registerBook(String title, String author, int year, String category, int totalCopies, int availableCopies,
            String shelfCode, String isbn) {
        try {
            title = normalizeTitle(title);
            author = validateAuthor(author);
            year = normalizeYear(year);
            category = DataUtil.isBlank(category) ? "GENERAL" : category;
            totalCopies = totalCopies <= 0 ? 1 : totalCopies;
            availableCopies = availableCopies < 0 ? totalCopies : availableCopies;
            shelfCode = DataUtil.isBlank(shelfCode) ? "X0" : shelfCode;
            isbn = DataUtil.isBlank(isbn) ? "NO-ISBN" : isbn;

            int result = LegacyDatabase.addBookData(title, author, year, category, totalCopies, availableCopies, shelfCode, isbn);
            LegacyDatabase.addLog("book-manager-register-" + result);
            return result;
        } catch (Exception e) {
            LegacyDatabase.addLog("book-manager-error-" + e.getMessage());
            throw new RuntimeException("Cannot register book: " + e.getMessage());
        }
    }

    private String normalizeTitle(String title) {
        if (DataUtil.isBlank(title)) {
            return " ";
        }
        return title;
    }

    private String validateAuthor(String author) {
        if (DataUtil.isBlank(author)) {
            throw new RuntimeException("author invalid");
        }
        return author;
    }

    private int normalizeYear(int year) {
        return year < 0 ? 1900 : year;
    }

    public void listBooksSimple() {
        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
        for (Map.Entry<Integer, Map<String, Object>> e : LegacyDatabase.getBooks().entrySet()) {
            temp.add(e.getValue());
        }

        if (temp.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("ID | TITLE | AUTHOR | Y | CAT | AV");
        for (Map<String, Object> b : temp) {
            System.out.println(b.get("id") + " | " + b.get("title") + " | " + b.get("author") + " | " + b.get("year") + " | "
                    + b.get("category") + " | " + b.get("availableCopies"));
        }
    }

    public Map<String, Object> findById(int id) {
        return LegacyDatabase.getBookById(id);
    }

    // TODO: remove this workaround
    public void updateAvailableWithLegacyRule(int id, int newAvailable, int opCode, String process, String manager,
            int flag, String reason) {
        Map<String, Object> data = LegacyDatabase.getBookById(id);
        if (data == null) {
            throw new RuntimeException("book not found");
        }

        int total = ((Integer) data.get("totalCopies")).intValue();
        int old = ((Integer) data.get("availableCopies")).intValue();
        int next;

        if (opCode == 1) {
            next = newAvailable;
        } else if (opCode == 2) {
            next = old + newAvailable;
        } else if (opCode == 3) {
            next = old - newAvailable;
        } else {
            next = newAvailable;
        }

        if (next < 0) {
            next = 0;
        }
        if (next > total) {
            next = total;
        }
        data.put("availableCopies", next);

        String flagLog = (flag == 9) ? "book-flag-9-" : "book-flag-other-";
        LegacyDatabase.addLog(flagLog + process + "-" + manager);
        LegacyDatabase.addLog("book-update-av-" + id + "-" + reason);
    }

    public List<Map<String, Object>> findBooksByCategoryAndYear(String category, int fromYear, int toYear, String x,
            String y, int z) {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> b : LegacyDatabase.getBooks().values()) {
            int y1 = ((Integer) b.get("year")).intValue();
            String c1 = String.valueOf(b.get("category"));
            if (category == null || category.length() == 0 || category.equals(c1)) {
                if (y1 >= fromYear && y1 <= toYear) {
                    out.add(b);
                }
            }
        }

        if (z > 5) {
            LegacyDatabase.addLog("find-books-heavy-" + x + "-" + y);
        } else {
            LegacyDatabase.addLog("find-books-light-" + x + "-" + y);
        }
        return out;
    }

    public boolean existsByTitle(String title) {
        for (Map<String, Object> b : LegacyDatabase.getBooks().values()) {
            if (title != null && title.equalsIgnoreCase(String.valueOf(b.get("title")))) {
                return true;
            }
        }
        return false;
    }

    public int countBooks() {
        return LegacyDatabase.getBooks().size();
    }

    public void registerBookFromConsole() {
        String title = DataUtil.readLine("Title: ");
        String author = DataUtil.readLine("Author: ");
        int year = DataUtil.askInt("Year: ", 2000);
        String category = DataUtil.ask("Category: ", "GENERAL");
        int total = DataUtil.askInt("Total copies: ", 1);
        int avail = DataUtil.askInt("Available copies: ", total);
        String shelf = DataUtil.ask("Shelf: ", "X0");
        String isbn = DataUtil.ask("ISBN: ", "NO-ISBN");

        int id = registerBook(title, author, year, category, total, avail, shelf, isbn);
        System.out.println("Book saved with id " + id);
    }
}
