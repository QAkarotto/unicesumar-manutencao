import Controller.BookManager;
import Controller.UserManager;
import Model.Book;
import Model.Loan;
import Model.User;
import Repository.Library;
import Util.DataUtil;
import java.util.List;

/**
 * Controller principal responsável pela inicialização do sistema,
 * injeção de dependências e interface de linha de comando (CLI).
 */
public class LibrarySystem {

    private Library library;
    private BookManager bookManager;
    private UserManager userManager;
    private LoanManager loanManager;
    private ReportGenerator reportGenerator;
    private NotificationService notificationService;

    private String systemName = "University Library System";
    private boolean running = true;
    private int menuCounter = 0;

    public LibrarySystem() {
        this.library = new Library();
        this.library.seedInitialData();

        this.bookManager = new BookManager(library);
        this.userManager = new UserManager(library);
        this.loanManager = new LoanManager(library);
        this.reportGenerator = new ReportGenerator(library);
        this.notificationService = new NotificationService(library);
    }

    public void startCli() {
        DataUtil.printHeader(systemName);
        while (running) {
            try {
                showMenu();
                String option = DataUtil.readLine("Select option: ");
                menuCounter++;

                switch (option) {
                    case "1":
                        handleRegisterBook();
                        break;
                    case "2":
                        handleRegisterUser();
                        break;
                    case "3":
                        handleBorrowBook();
                        break;
                    case "4":
                        handleReturnBook();
                        break;
                    case "5":
                        handleListBooks();
                        break;
                    case "6":
                        handleGenerateReport();
                        break;
                    case "7":
                        handleListUsers();
                        break;
                    case "8":
                        handleListLoans();
                        break;
                    case "9":
                        handleDebugArea();
                        break;
                    case "10":
                        handleUserHistory();
                        break;
                    case "0":
                        running = false;
                        System.out.println("bye");
                        break;
                    default:
                        System.out.println("invalid option");
                        break;
                }

                if (menuCounter % 3 == 0) {
                    library.clearLogsIfTooBig();
                }
            } catch (Exception e) {
                System.out.println("General system error: " + e.getMessage());
                library.addLog("system-main-loop-error-" + e.getMessage());
            }
        }
    }

    private void showMenu() {
        DataUtil.printSeparator();
        System.out.println("1 - Register book");
        System.out.println("2 - Register user");
        System.out.println("3 - Borrow book");
        System.out.println("4 - Return book");
        System.out.println("5 - List books");
        System.out.println("6 - Generate report");
        System.out.println("7 - List users");
        System.out.println("8 - List loans");
        System.out.println("9 - Debug area");
        System.out.println("10 - User History");
        System.out.println("0 - Exit");
        DataUtil.printSeparator();
    }

    public void handleRegisterBook() {
        try {
            String title = DataUtil.readLine("Title: ");
            String author = DataUtil.readLine("Author: ");
            int year = DataUtil.askInt("Year: ", 2000);
            String category = DataUtil.ask("Category: ", "GENERAL");
            int total = DataUtil.askInt("Total copies: ", 1);
            int available = DataUtil.askInt("Available copies: ", total);
            String shelfCode = DataUtil.ask("Shelf code: ", "X0");
            String isbn = DataUtil.ask("ISBN: ", "NO-ISBN");

            int id = library.addBook(title, author, year, category, total, available, shelfCode, isbn);
            System.out.println("Book registered with id " + id);

            if (id % 2 == 0) {
                library.addLog("book-even-id");
            } else {
                library.addLog("book-odd-id");
            }
        } catch (Exception e) {
            System.out.println("Error register book: " + e.getMessage());
            library.addLog("handle-register-book-error");
        }
    }

    public void handleRegisterUser() {
        try {
            String name = DataUtil.readLine("Name: ");
            String email = DataUtil.readLine("Email: ");
            String phone = DataUtil.readLine("Phone: ");
            String type = DataUtil.ask("Type: ", "student");
            String city = DataUtil.ask("City: ", "Unknown");
            String document = DataUtil.ask("Document: ", "NO-DOC");
            String status = DataUtil.ask("Status: ", "ACTIVE");

            int id = library.addUser(name, email, phone, type, city, document, status);
            System.out.println("User registered with id " + id);
        } catch (Exception e) {
            System.out.println("Error register user: " + e.getMessage());
            library.addLog("handle-register-user-error");
        }
    }

    public void handleBorrowBook() {
        try {
            int userId = DataUtil.askInt("User ID: ", -1);
            int bookId = DataUtil.askInt("Book ID: ", -1);
            String borrowDate = DataUtil.ask("Borrow date: ", DataUtil.nowDate());
            String dueDate = DataUtil.ask("Due date: ", DataUtil.datePlusDaysApprox(borrowDate, 14));
            String channel = DataUtil.ask("Channel (email/sms): ", "email");

            loanManager.borrowBook(bookId, userId);
            notificationService.notifyLoanCreated(userId, bookId, borrowDate, dueDate, channel, "template", "main");

        } catch (Exception e) {
            System.out.println("Error borrow: " + e.getMessage());
            library.addLog("handle-borrow-error-" + e.getMessage());
        }
    }

    public void handleReturnBook() {
        try {
            int loanId = DataUtil.askInt("Loan ID: ", -1);
            loanManager.returnBook(loanId);
        } catch (Exception e) {
            System.out.println("Error return: " + e.getMessage());
            library.addLog("handle-return-error-" + e.getMessage());
        }
    }

    public void handleListBooks() {
        try {
            DataUtil.printHeader("Books");
            bookManager.listBooksSimple();
        } catch (Exception e) {
            System.out.println("Error list books");
            library.addLog("handle-list-books-error");
        }
    }

    public void handleListUsers() {
        try {
            DataUtil.printHeader("Users");
            userManager.listUsers();
        } catch (Exception e) {
            System.out.println("Error list users");
            library.addLog("handle-list-users-error");
        }
    }

    public void handleListLoans() {
        try {
            DataUtil.printHeader("Loans");
            for (Loan l : library.getLoans()) {
                System.out.println(l.getId() + " | BookID: " + l.getBookId() + " | UserID: " + l.getUserId() + " | Status: " + l.getStatus());
            }
        } catch (Exception e) {
            System.out.println("Error list loans");
            library.addLog("handle-list-loans-error");
        }
    }

    public void handleGenerateReport() {
        try {
            String reportName = DataUtil.ask("Report name: ", "Legacy Report");
            int mode = DataUtil.askInt("Mode (0/1): ", 1);
            int year = DataUtil.askInt("Filter year (0 for all): ", 0);
            String category = DataUtil.ask("Filter category: ", "");

            String report = reportGenerator.generateSimpleReport(reportName, mode, "manager", "helper", year, category);
            System.out.println(report);
        } catch (Exception e) {
            System.out.println("Error report: " + e.getMessage());
            library.addLog("handle-report-error");
        }
    }

    public void handleUserHistory() {
        try {
            DataUtil.printHeader("User Loan History");
            int userId = DataUtil.askInt("User ID: ", -1);
            User user = library.getUserById(userId);

            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            boolean hasLoans = false;
            for (Loan loan : library.getLoans()) {
                if (loan.getUserId() == userId) {
                    hasLoans = true;
                    Book b = library.getBookById(loan.getBookId());
                    String title = (b != null) ? b.getTitle() : "Unknown Book";
                    System.out.println("- Loan ID: " + loan.getId() + " | Book: " + title + " | Status: " + loan.getStatus());
                }
            }

            if (!hasLoans) {
                System.out.println("No history for this user.");
            }
        } catch (Exception e) {
            System.out.println("Error fetching history: " + e.getMessage());
        }
    }

    public void handleDebugArea() {
        DataUtil.printHeader("Debug Area");
        System.out.println("1-Print logs");
        System.out.println("2-Print state");
        System.out.println("3-Change mode");
        System.out.println("4-Unsafe field update (Adapted)");
        System.out.println("5-Loan histogram");
        System.out.println("6-Manual notify");
        System.out.println("0-Back");

        String option = DataUtil.readLine("Debug option: ");

        switch (option) {
            case "1":
                library.printLogs();
                break;
            case "2":
                library.dumpState();
                break;
            case "3":
                String mode = DataUtil.readLine("New mode: ");
                if (!DataUtil.isBlank(mode)) {
                    library.systemMode = mode;
                    System.out.println("mode changed");
                }
                break;
            case "4":
                String target = DataUtil.readLine("Target (book/user): ");
                int id = DataUtil.askInt("Id: ", -1);
                String field = DataUtil.readLine("Field: ");

                if ("book".equals(target)) {
                    Book b = library.getBookById(id);
                    if (b != null && "title".equals(field)) {
                        System.out.println("book updated (simulated)");
                    }
                } else if ("user".equals(target)) {
                    User u = library.getUserById(id);
                    if (u != null && "name".equals(field)) {
                        System.out.println("user updated (simulated)");
                    }
                }
                break;
            case "5":
                reportGenerator.printLoanHistogram();
                break;
            case "6":
                String x = DataUtil.ask("x: ", "x");
                String y = DataUtil.ask("y: ", "y");
                String z = DataUtil.ask("z: ", "z");
                int p = DataUtil.askInt("priority: ", 1);
                int r = DataUtil.askInt("retry: ", 0);
                notificationService.genericNotify(x, y, z, p, r, "debug");
                break;
            case "0":
                System.out.println("back");
                break;
            default:
                System.out.println("invalid debug option");
                break;
        }
    }

    public void runDemoScenario() {
        try {
            int idBook = library.addBook("Legacy Java", "Unknown", 2010, "CS", 2, 2, "B1", "ISBN-999");
            int idUser = library.addUser("Carlos", "carlos@mail.com", "3333-3333", "student", "Maringa", "DOC-3", "ACTIVE");

            loanManager.borrowBook(idBook, idUser);
            loanManager.returnBook(1);
        } catch (Exception e) {
            library.addLog("demo-error-" + e.getMessage());
        }
    }

    public BookManager getBookManager() {
        return bookManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public LoanManager getLoanManager() {
        return loanManager;
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public List<Loan> getAllLoansDirect() {
        return library.getLoans();
    }
}