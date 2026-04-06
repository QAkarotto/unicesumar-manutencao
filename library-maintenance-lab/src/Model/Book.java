package Model;
import Util.DataUtil;

public class Book {

    private String title;
    private String author;
    private int year;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private String shelfCode;
    private String isbn;
    int id;

    public Book(int id, String title, String author, int year, String category, int totalCopies, int availableCopies, String shelfCode, String isbn) {
        // 1. Validações críticas
        if (title == null || author == null) {
            throw new RuntimeException("Title or Author invalid");
        }

        // 2. Atribuições com lógica de "fallback"
        this.title = title;
        this.author = author;
        this.year = (year < 0) ? 1900 : year;
        this.category = (category == null) ? "GENERAL" : category;
        this.totalCopies = (totalCopies <= 0) ? 1 : totalCopies;

        // Garante que cópias disponíveis não sejam negativas nem maiores que o total
        this.availableCopies = (availableCopies < 0) ? this.totalCopies : availableCopies;
        this.shelfCode = DataUtil.isBlank(shelfCode) ? "X0" : shelfCode;
        this.isbn = DataUtil.isBlank(isbn) ? "NO-ISBN" : isbn;
        this.id = id;

    }

    public boolean borrowCopy() {
        if (this.availableCopies > 0) {
            this.availableCopies--;
            return true;
        }
        return false; // Não tem cópia
    }

    public void returnCopy() {
        if (this.availableCopies < this.totalCopies) {
            this.availableCopies++;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


}
