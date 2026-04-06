import Model.Book;
import Model.User;
import Repository.Library;

public class NotificationService {

    // O serviço de notificação agora precisa da Library para buscar os dados
    private Library library;

    public NotificationService(Library library) {
        this.library = library;
    }

    public void notifyLoanCreated(int userId, int bookId, String date, String dueDate, String channel, String template, String managerName) {
        // Busca os objetos tipados
        User user = library.getUserById(userId);
        Book book = library.getBookById(bookId);

        if (user != null && book != null) {
            String msg = "Model.Loan created for user " + user.getName() + " and book " + book.getTitle() + " due " + dueDate;

            if ("sms".equals(channel)) {
                System.out.println("SMS: " + msg);
            } else if ("email".equals(channel)) {
                System.out.println("EMAIL: " + msg);
            } else {
                System.out.println("LOG: " + msg);
            }
            // Substituímos o LegacyDatabase pelo log da nossa Library
            library.addLog("notify-loan-" + userId + "-" + bookId);
        }
    }

    public void notifyReturn(int userId, int bookId, String status, double fine, String channel) {
        User user = library.getUserById(userId);
        Book book = library.getBookById(bookId);

        if (user != null && book != null) {
            String msg = "Model.Book returned: " + book.getTitle() + " by " + user.getName() + ", fine=" + fine;

            if ("sms".equals(channel)) {
                System.out.println("SMS: " + msg);
            } else {
                System.out.println("EMAIL: " + msg);
            }
            library.addLog("notify-return-" + userId + "-" + bookId + "-" + status);
        }
    }

    // Este método nem tocava em livros ou usuários, só precisava arrumar o Log
    public void genericNotify(String x, String y, String z, int priority, int retry, String process) {
        if (priority > 5) {
            System.out.println("HIGH: " + x + " | " + y + " | " + z + " | " + process);
        } else {
            System.out.println("LOW: " + x + " | " + y + " | " + z + " | " + process);
        }

        if (retry > 3) {
            library.addLog("notify-retry-high");
        } else {
            library.addLog("notify-retry-low");
        }
    }

    public void sendDebtAlert(int userId, double value, int level, String manager) {
        User user = library.getUserById(userId);

        if (user != null) {
            if (level == 1) {
                System.out.println("Debt warning to " + user.getName() + ": " + value);
            } else if (level == 2) {
                System.out.println("Debt urgent warning to " + user.getName() + ": " + value);
            } else {
                System.out.println("Debt legal warning to " + user.getName() + ": " + value);
            }
        }
        library.addLog("notify-debt-" + userId + "-" + manager);
    }
}