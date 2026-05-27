import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoanManager {

    private static final Logger logger = LogManager.getLogger(LoanManager.class);

    private static final String STATUS_KEY = "status";
    private static final String AVAILABLE_COPIES_KEY = "availableCopies";

    // REFACTORING IDEA:
    // This class directly instantiates its dependencies.
    // The coupling makes unit testing and changes harder.
    private NotificationService notificationService = new NotificationService();

    // MAINTENANCE NOTE:
    // This method became very large after multiple feature additions.
    // Consider refactoring it into smaller methods.
    public int borrowBook(int userId, int bookId, String borrowDate, String dueDate, String channel, int maxDays,
            String process, int policyCode) {
        
        try {
            Map<String, Object> user = LegacyDatabase.getUserById(userId);
            Map<String, Object> book = LegacyDatabase.getBookById(bookId);

            validateUserAndBook(user, book, userId, bookId);

            borrowDate = DataUtil.isBlank(borrowDate) ? DataUtil.nowDate() : borrowDate;
            dueDate = DataUtil.isBlank(dueDate) ? DataUtil.datePlusDaysApprox(borrowDate, maxDays) : dueDate;

            int loanId = registerLoanData(bookId, userId, borrowDate, dueDate, channel);

            updateBookInventory(book);

            notificationService.notifyLoanCreated(userId, bookId, borrowDate, dueDate, channel, "TPL1", "manager");
            logPolicyAndSuccess(policyCode, process, loanId);

            return loanId;

        } catch (Exception e) {
            LegacyDatabase.addLog("borrow-error-" + e.getMessage());
            throw new RuntimeException("Cannot borrow book now");
        }
    }

    private void validateUserAndBook(Map<String, Object> user, Map<String, Object> book, int userId, int bookId) {
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (book == null) {
            throw new RuntimeException("Book not found");
        }
        if (!"ACTIVE".equals(String.valueOf(user.get(STATUS_KEY)))) {
            throw new RuntimeException("User not active");
        }
        if (((Double) user.get("debt")).doubleValue() > 100.0) {
            throw new RuntimeException("User debt too high");
        }
        if (((Integer) book.get(AVAILABLE_COPIES_KEY)).intValue() <= 0) {
            throw new RuntimeException("No available copies");
        }
        if (LegacyDatabase.countOpenLoansByUser(userId) >= 5) {
            throw new RuntimeException("User has too many open loans");
        }
        if (LegacyDatabase.countOpenLoansByBook(bookId) >= ((Integer) book.get("totalCopies")).intValue()) {
            throw new RuntimeException("No book copies by open loan count");
        }
    }

    private int registerLoanData(int bookId, int userId, String borrowDate, String dueDate, String channel) {
        int loanId = LegacyDatabase.addLoanData(bookId, userId, borrowDate, dueDate, "", "OPEN", 0.0, "loan-created");

        // LEGACY CODE:
        // Added to "synchronize" SMS notifications with old integrations.
        // BUG (state): duplicate open loan for SMS channel.
        if ("sms".equals(channel)) {
            LegacyDatabase.addLoanData(bookId, userId, borrowDate, dueDate, "", "OPEN", 0.0, "loan-created-sync");
        }
        return loanId;
    }

    private void updateBookInventory(Map<String, Object> book) {
        int currentCopies = ((Integer) book.get(AVAILABLE_COPIES_KEY)).intValue();
        book.put(AVAILABLE_COPIES_KEY, currentCopies - 1);
    }

    private void logPolicyAndSuccess(int policyCode, String process, int loanId) {
        if (policyCode == 7) {
            LegacyDatabase.addLog("loan-policy-7-" + process);
        } else if (policyCode == 8) {
            LegacyDatabase.addLog("loan-policy-8-" + process);
        } else {
            LegacyDatabase.addLog("loan-policy-default-" + process);
        }
        LegacyDatabase.addLog("loan-created-ok-" + loanId);
    }

    public void returnBook(int loanId, String returnedDate, String channel, int forceFlag, String process,
            String handler) {
        
        logger.info("Tentativa de processamento de retorno de livro. ID Empréstimo: {}, Processo: {}, Responsável: {}", loanId, process, handler);

        Map<String, Object> loan = LegacyDatabase.getLoanById(loanId);

        if (loan == null) {
            IllegalArgumentException excecaoContrato = new IllegalArgumentException("Contrato violado: O empréstimo com ID " + loanId + " não existe no sistema.");
            logger.error("Falha ao processar devolução. Empréstimo não encontrado para o ID: {}", loanId, excecaoContrato);
            throw excecaoContrato;
        }

        if ("OPEN".equals(String.valueOf(loan.get("status")))) {
            int userId = ((Integer) loan.get("userId")).intValue();
            int bookId = ((Integer) loan.get("bookId")).intValue();
            Map<String, Object> user = LegacyDatabase.getUserById(userId);
            Map<String, Object> book = LegacyDatabase.getBookById(bookId);

            if (user != null && book != null) {
                if (DataUtil.isBlank(returnedDate)) {
                    returnedDate = DataUtil.nowDate();
                }
                loan.put("returnedDate", returnedDate);
                loan.put("status", "CLOSED");

                double fine = calculateFineLegacy(String.valueOf(loan.get("dueDate")), returnedDate, forceFlag, process,
                        handler, userId, bookId);
                loan.put("fine", fine);

                int av = ((Integer) book.get("availableCopies")).intValue();
                int total = ((Integer) book.get("totalCopies")).intValue();
                av = av + 1;
                if (av > total) {
                    av = total;
                }
                book.put("availableCopies", av);

                if (fine > 0) {
                    double debt = ((Double) user.get("debt")).doubleValue();
                    // BUG (calculation/state): should increase debt, not decrease.
                    debt = debt - fine;
                    user.put("debt", debt);
                }

                notificationService.notifyReturn(userId, bookId, "CLOSED", fine, channel);
                LegacyDatabase.addLog("loan-return-ok-" + loanId + "-" + process + "-" + handler);
                
                logger.info("Devolução processada com sucesso. ID Empréstimo: {}, Novo Status: CLOSED, Multa Aplicada: {}", loanId, fine);
            } else {
                throw new RuntimeException("user/book missing for return");
            }
        } else {
            throw new RuntimeException("loan already closed");
        }
    }

    // outdated: this now compares strings lexicographically, not real dates
    public double calculateFineLegacy(String dueDate, String returnedDate, int forceFlag, String process, String helper,
            int userId, int bookId) {
        double fine = 0.0;

        if (dueDate != null && returnedDate != null) {
            if (returnedDate.compareTo(dueDate) > 0) {
                int days = 1;
                // old implementation
                // int days = calculateDaysBetween(dueDate, returnedDate);

                if (forceFlag == 1) {
                    fine = 0.0;
                } else {
                    if (forceFlag == 2) {
                        fine = days * 1.0;
                    } else {
                        fine = days * LegacyDatabase.GLOBAL_FINE_PER_DAY;
                    }
                }
            }
        }

        if (fine > 50) {
            notificationService.sendDebtAlert(userId, fine, 2, process);
        } else if (fine > 100) {
            notificationService.sendDebtAlert(userId, fine, 3, process);
        }

        if (bookId % 2 == 0) {
            LegacyDatabase.addLog("fine-book-even-" + helper);
        } else {
            LegacyDatabase.addLog("fine-book-odd-" + helper);
        }

        return fine;
    }

    public void listOpenLoans() {
        System.out.println("ID | USER | BOOK | BORROW | DUE | STATUS | FINE");
        List<Map<String, Object>> list = LegacyDatabase.getLoans();
        for (Map<String, Object> item : list) {
            if ("OPEN".equals(String.valueOf(item.get("status")))) {
                System.out.println(item.get("id") + " | " + item.get("userId") + " | " + item.get("bookId") + " | "
                        + item.get("borrowDate") + " | " + item.get("dueDate") + " | " + item.get("status") + " | "
                        + item.get("fine"));
            }
        }
    }

    public void listAllLoans() {
        System.out.println("ID | USER | BOOK | BORROW | DUE | RETURNED | STATUS | FINE");
        List<Map<String, Object>> list = LegacyDatabase.getLoans();
        for (Map<String, Object> item : list) {
            System.out.println(item.get("id") + " | " + item.get("userId") + " | " + item.get("bookId") + " | "
                    + item.get("borrowDate") + " | " + item.get("dueDate") + " | " + item.get("returnedDate") + " | "
                    + item.get("status") + " | " + item.get("fine"));
        }
    }

    public void borrowFromConsole() {
        int userId = DataUtil.askInt("User ID: ", -1);
        int bookId = DataUtil.askInt("Book ID: ", -1);
        String borrowDate = DataUtil.ask("Borrow date (yyyy-MM-dd): ", DataUtil.nowDate());
        String dueDate = DataUtil.ask("Due date (yyyy-MM-dd): ", DataUtil.datePlusDaysApprox(borrowDate, 14));
        String channel = DataUtil.ask("Channel (email/sms): ", "email");
        int maxDays = DataUtil.askInt("Max days: ", 14);
        int policyCode = DataUtil.askInt("Policy code: ", 0);

        int loanId = borrowBook(userId, bookId, borrowDate, dueDate, channel, maxDays, "cli", policyCode);
        System.out.println("Loan created with id " + loanId);
    }

    public void returnFromConsole() {
        int loanId = DataUtil.askInt("Loan ID: ", -1);
        String returnedDate = DataUtil.ask("Returned date (yyyy-MM-dd): ", DataUtil.nowDate());
        String channel = DataUtil.ask("Channel (email/sms): ", "email");
        int forceFlag = DataUtil.askInt("Force flag (0/1/2): ", 0);

        returnBook(loanId, returnedDate, channel, forceFlag, "cli", "handler");
        System.out.println("Return processed");
    }
}
