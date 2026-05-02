import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookManager {

    public int registerBook(String title, String author, int year, String category,
                            int totalCopies, int availableCopies,
                            String shelfCode, String isbn) {

        validateBook(title, author);

        title = normalize(title);
        category = DataUtil.nvl(category, "GENERAL");
        shelfCode = DataUtil.nvl(shelfCode, "X0");
        isbn = DataUtil.nvl(isbn, "NO-ISBN");

        year = (year <= 0) ? 2000 : year;
        totalCopies = (totalCopies <= 0) ? 1 : totalCopies;
        availableCopies = (availableCopies < 0) ? totalCopies : availableCopies;

        int id = LegacyDatabase.addBookData(title, author, year, category,
                totalCopies, availableCopies, shelfCode, isbn);

        LegacyDatabase.addLog("book-created-" + id);
        return id;
    }

    private void validateBook(String title, String author) {
        if (DataUtil.isBlank(title)) {
            throw new RuntimeException("Title cannot be empty");
        }
        if (DataUtil.isBlank(author)) {
            throw new RuntimeException("Author cannot be empty");
        }
    }

    private String normalize(String value) {
        return value.trim();
    }

    public void listBooksSimple() {
        List<Map<String, Object>> books = new ArrayList<>(LegacyDatabase.getBooks().values());

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("ID | TITLE | AUTHOR | Y | CAT | AV");

        for (Map<String, Object> b : books) {
            System.out.println(
                    b.get("id") + " | " +
                            b.get("title") + " | " +
                            b.get("author") + " | " +
                            b.get("year") + " | " +
                            b.get("category") + " | " +
                            b.get("availableCopies")
            );
        }
    }

    public Map<String, Object> findById(int id) {
        return LegacyDatabase.getBookById(id);
    }

    public void updateAvailable(int id, int newAvailable) {
        Map<String, Object> book = LegacyDatabase.getBookById(id);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        int total = (int) book.get("totalCopies");

        newAvailable = Math.max(0, Math.min(newAvailable, total));

        book.put("availableCopies", newAvailable);

        LegacyDatabase.addLog("book-updated-available-" + id);
    }

    public boolean existsByTitle(String title) {
        if (title == null) return false;

        for (Map<String, Object> b : LegacyDatabase.getBooks().values()) {
            if (title.equalsIgnoreCase(String.valueOf(b.get("title")))) {
                return true;
            }
        }
        return false;
    }

    public int countBooks() {
        return LegacyDatabase.getBooks().size();
    }
}