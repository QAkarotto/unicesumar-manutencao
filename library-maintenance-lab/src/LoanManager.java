import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class LoanManager {

    private NotificationService notificationService = new NotificationService();

    public int borrowBook(int userId, int bookId, String borrowDate, String dueDate, String channel, int maxDays,
                          String process, int policyCode) {

        Map<String, Object> user = LegacyDatabase.getUserById(userId);
        if (user == null) throw new RuntimeException("User not found");

        Map<String, Object> book = LegacyDatabase.getBookById(bookId);
        if (book == null) throw new RuntimeException("Book not found");

        validateUser(user);
        validateBookAvailability(book, userId, bookId);

        borrowDate = DataUtil.isBlank(borrowDate) ? DataUtil.nowDate() : borrowDate;
        dueDate = DataUtil.isBlank(dueDate) ? DataUtil.datePlusDaysApprox(borrowDate, maxDays) : dueDate;

        int loanId = LegacyDatabase.addLoanData(bookId, userId, borrowDate, dueDate, "", "OPEN", 0.0, "loan-created");

        decreaseAvailableCopies(book);

        notificationService.notifyLoanCreated(userId, bookId, borrowDate, dueDate, channel, "TPL1", "manager");

        logPolicy(policyCode, process);
        LegacyDatabase.addLog("loan-created-ok-" + loanId);

        return loanId;
    }

    private void validateUser(Map<String, Object> user) {
        if (!"ACTIVE".equals(String.valueOf(user.get("status")))) {
            throw new RuntimeException("User not active");
        }

        if ((Double) user.get("debt") > 100.0) {
            throw new RuntimeException("User debt too high");
        }
    }

    private void validateBookAvailability(Map<String, Object> book, int userId, int bookId) {
        if ((Integer) book.get("availableCopies") <= 0) {
            throw new RuntimeException("No available copies");
        }

        if (LegacyDatabase.countOpenLoansByUser(userId) >= 5) {
            throw new RuntimeException("User has too many open loans");
        }

        if (LegacyDatabase.countOpenLoansByBook(bookId) >= (Integer) book.get("totalCopies")) {
            throw new RuntimeException("No book copies by open loan count");
        }
    }

    private void decreaseAvailableCopies(Map<String, Object> book) {
        int av = (Integer) book.get("availableCopies");
        book.put("availableCopies", av - 1);
    }

    private void logPolicy(int policyCode, String process) {
        switch (policyCode) {
            case 7:
                LegacyDatabase.addLog("loan-policy-7-" + process);
                break;
            case 8:
                LegacyDatabase.addLog("loan-policy-8-" + process);
                break;
            default:
                LegacyDatabase.addLog("loan-policy-default-" + process);
        }
    }

    public void returnBook(int loanId, String returnedDate, String channel, int forceFlag, String process,
                           String handler) {

        Map<String, Object> loan = LegacyDatabase.getLoanById(loanId);
        if (loan == null) throw new RuntimeException("Loan not found");

        if (!"OPEN".equals(String.valueOf(loan.get("status")))) {
            throw new RuntimeException("loan already closed");
        }

        int userId = (Integer) loan.get("userId");
        int bookId = (Integer) loan.get("bookId");

        Map<String, Object> user = LegacyDatabase.getUserById(userId);
        Map<String, Object> book = LegacyDatabase.getBookById(bookId);

        if (user == null || book == null) {
            throw new RuntimeException("user/book missing for return");
        }

        returnedDate = DataUtil.isBlank(returnedDate) ? DataUtil.nowDate() : returnedDate;

        loan.put("returnedDate", returnedDate);
        loan.put("status", "CLOSED");

        double fine = calculateFine(String.valueOf(loan.get("dueDate")), returnedDate, forceFlag);
        loan.put("fine", fine);

        increaseAvailableCopies(book);

        if (fine > 0) {
            double debt = (Double) user.get("debt");
            user.put("debt", debt + fine); // FIX
        }

        notificationService.notifyReturn(userId, bookId, "CLOSED", fine, channel);
        LegacyDatabase.addLog("loan-return-ok-" + loanId + "-" + process + "-" + handler);
    }

    private void increaseAvailableCopies(Map<String, Object> book) {
        int av = (Integer) book.get("availableCopies");
        int total = (Integer) book.get("totalCopies");

        book.put("availableCopies", Math.min(av + 1, total));
    }

    public double calculateFine(String dueDate, String returnedDate, int forceFlag) {
        try {
            LocalDate due = LocalDate.parse(dueDate);
            LocalDate ret = LocalDate.parse(returnedDate);

            if (!ret.isAfter(due)) return 0.0;

            long days = ChronoUnit.DAYS.between(due, ret);

            if (forceFlag == 1) return 0.0;
            if (forceFlag == 2) return days * 1.0;

            return days * LegacyDatabase.GLOBAL_FINE_PER_DAY;

        } catch (Exception e) {
            return 0.0;
        }
    }
}