package Repository;

import Model.Book;
import Model.Loan;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class Library {

    // 1. Armazenamento Tipado (Substitui os Maps)
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<String> logs = new ArrayList<>();

    // 2. Controle de Sequências (IDs)
    private int bookSeq = 1;
    private int userSeq = 1;
    private int loanSeq = 1;

    // 3. Configurações Globais (Movidas do LegacyDatabase)
    public String systemMode = "LEGACY";
    public int globalFinePerDay = 2;
    public int globalMaxLoanDays = 14;
    public boolean workaroundFlag = true;

    // --- MÉTODOS DE ADIÇÃO (Fábricas) ---

    public int addBook(String title, String author, int year, String category, int totalCopies, int availableCopies, String shelfCode, String isbn) {
        Book book = new Book(bookSeq++, title, author, year, category, totalCopies, availableCopies, shelfCode, isbn);
        books.add(book);
        addLog("book-added-" + book.getId());
        return book.getId();
    }

    public int addUser(String name, String email, String phone, String userType, String city, String document, String status) {
        User user = new User(userSeq++, name, email, phone, userType, city, document, status);
        users.add(user);
        addLog("user-added-" + user.getId());
        return user.getId();
    }

    public int addLoan(int bookId, int userId, String borrowDate, String dueDate) {
        Loan loan = new Loan(loanSeq++, bookId, userId, borrowDate, dueDate);
        loans.add(loan);
        addLog("loan-added-" + loan.getId());
        return loan.getId();
    }

    // --- MÉTODOS DE BUSCA (Queries) ---

    public Book getBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public User getUserById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    public Loan getLoanById(int id) {
        for (Loan l : loans) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    // Retorna as listas completas (Para relatórios)
    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
    public List<Loan> getLoans() { return loans; }

    // --- REGRAS DE NEGÓCIO E CONTAGEM ---

    public int countOpenLoansByUser(int userId) {
        int c = 0;
        for (Loan loan : loans) {
            if (loan.getUserId() == userId && "OPEN".equals(loan.getStatus())) {
                c++;
            }
        }
        return c;
    }

    public int countOpenLoansByBook(int bookId) {
        int c = 0;
        for (Loan loan : loans) {
            // BUG RESOLVIDO: Trocado getUserId() por getBookId()
            if (loan.getBookId() == bookId && "OPEN".equals(loan.getStatus())) {
                c++;
            }
        }
        return c;
    }

    // --- LOGS E UTILITÁRIOS ---

    public void addLog(String value) {
        logs.add(value);
    }

    public void printLogs() {
        for (String s : logs) {
            System.out.println(s);
        }
    }

    public void clearLogsIfTooBig() {
        if (logs.size() > 500) {
            List<String> tmp = new ArrayList<>();
            for (int i = 400; i < logs.size(); i++) {
                tmp.add(logs.get(i));
            }
            logs = tmp;
        }
    }

    public void dumpState() {
        System.out.println("BOOKS=" + books.size() + "; USERS=" + users.size() + "; LOANS=" + loans.size());
    }

    public void seedInitialData() {
        if (!books.isEmpty() || !users.isEmpty()) {
            return;
        }
        // Repare que agora usamos o próprio método interno da Repository.Library
        addBook("Clean Code", "Robert C. Martin", 2008, "Software", 3, 3, "A1", "ISBN-111");
        addBook("Design Patterns", "GoF", 1994, "Software", 2, 2, "A2", "ISBN-222");
        addBook("Refactoring", "Martin Fowler", 1999, "Software", 4, 4, "A3", "ISBN-333");

        addUser("Ana", "ana@mail.com", "1111-1111", "student", "Maringa", "DOC-1", "ACTIVE");
        addUser("Bruno", "bruno@mail.com", "2222-2222", "teacher", "Maringa", "DOC-2", "ACTIVE");

        addLog("seed-loaded");
    }
}