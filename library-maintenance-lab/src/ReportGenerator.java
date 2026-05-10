import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    // IMPROVEMENT OPPORTUNITY:
    // This method combines formatting, data access and business rules.
    public String generateSimpleReport(
            String reportName,
            int mode,
            String manager,
            String helper,
            int yearFilter,
            String categoryFilter) {

        StringBuilder sb = new StringBuilder();

        sb.append("=== REPORT: ").append(reportName).append(" ===\n");

        sb.append("mode=")
                .append(mode)
                .append(" manager=")
                .append(manager)
                .append(" helper=")
                .append(helper)
                .append("\n");

        // feature envy: direct access to another class internals
        Map<Integer, Map<String, Object>> books = LegacyDatabase.getBooks();
        Map<Integer, Map<String, Object>> users = LegacyDatabase.getUsers();
        List<Map<String, Object>> loans = LegacyDatabase.getLoans();

        this.appendNumberOfBooks(books, sb);
        this.appendNumberOfUsers(users, sb);
        this.appendNumberOfLoans(loans, sb);
        this.appendOpenAndClosedLoans(loans, sb);

        this.appendBooksDetails(books, yearFilter, categoryFilter, sb);
        this.appendUsersWithDebt(users, sb);

        if (mode == 1) {
            this.appendRecentLogs(sb);
        }

        LegacyDatabase.addLog("report-generated-" + reportName + "-" + manager + "-" + helper);

        return sb.toString();
    }

    private void appendRecentLogs(
            StringBuilder sb) {
        sb.append("\nRecent logs:\n");

        List<String> logs = LegacyDatabase.getLogs();
        int start = logs.size() - 10;

        if (start < 0) {
            start = 0;
        }

        for (int i = start; i < logs.size(); i++) {
            sb.append(" * ").append(logs.get(i)).append("\n");
        }
    }

    private void appendOpenAndClosedLoans(
            List<Map<String, Object>> loans,
            StringBuilder sb) {
        int openLoans = 0;
        int closedLoans = 0;

        for (Map<String, Object> loan : loans) {
            String loanStatus = String.valueOf(loan.get("status"));

            if ("OPEN".equals(loanStatus)) {
                openLoans++;
            }

            if ("CLOSED".equals(loanStatus)) {
                closedLoans++;
            }
        }

        sb.append("Open loans: ").append(openLoans).append("\n");
        sb.append("Closed loans: ").append(closedLoans).append("\n");
    }

    private void appendNumberOfBooks(
            Map<Integer, Map<String, Object>> books,
            StringBuilder sb) {
        int totalBooks = books.size();
        sb.append("Books: ").append(totalBooks).append("\n");
    }

    private void appendNumberOfUsers(
            Map<Integer, Map<String, Object>> users,
            StringBuilder sb) {
        int totalUsers = users.size();

        sb.append("Users: ").append(totalUsers).append("\n");
    }

    private void appendNumberOfLoans(
            List<Map<String, Object>> loans,
            StringBuilder sb) {
        int totalLoans = loans.size();

        sb.append("Loans: ").append(totalLoans).append("\n");
    }

    private void appendBooksDetails(
            Map<Integer, Map<String, Object>> books,
            int yearFilter,
            String categoryFilter,
            StringBuilder sb) {
        sb.append("\nBooks detail:\n");

        for (Map<String, Object> book : books.values()) {
            int year = ((Integer) book.get("year")).intValue();

            String category = String.valueOf(book.get("category"));

            boolean isValidYearFilter = yearFilter <= 0 || year == yearFilter;
            boolean isValidCategoryFilter = DataUtil.isBlank(categoryFilter) || categoryFilter.equals(category);

            if (isValidYearFilter && isValidCategoryFilter) {
                Object id = book.get("id");
                Object title = book.get("title");
                Object author = book.get("author");
                Object availableCopies = book.get("availableCopies");

                sb.append(" - ")
                        .append(id)
                        .append(" | ")
                        .append(title)
                        .append(" | ")
                        .append(author)
                        .append(" | year=")
                        .append(year)
                        .append(" | cat=")
                        .append(category)
                        .append(" | av=")
                        .append(availableCopies)
                        .append("\n");
            }
        }
    }

    private void appendUsersWithDebt(Map<Integer, Map<String, Object>> users, StringBuilder sb) {
        sb.append("\nUsers with debt:\n");
        for (Map<String, Object> user : users.values()) {
            double debt = ((Double) user.get("debt")).doubleValue();

            if (debt > 0) {
                sb.append(" - ")
                        .append(user.get("id"))
                        .append(" | ")
                        .append(user.get("name"))
                        .append(" | debt=")
                        .append(debt)
                        .append(" | status=")
                        .append(user.get("status"))
                        .append("\n");
            }
        }
    }

    public void printSimpleReport() {
        String r = generateSimpleReport("Legacy Library", 1, "manager", "helper", 0, "");
        System.out.println(r);
    }

    public Map<String, Integer> countLoansByUser() {
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (Map<String, Object> loan : LegacyDatabase.getLoans()) {
            String userId = String.valueOf(loan.get("userId"));
            Integer loanCount = map.get(userId);

            if (loanCount == null) {
                loanCount = 0;
            }

            loanCount = loanCount + 1;

            map.put(userId, loanCount);
        }

        return map;
    }

    public void printLoanHistogram() {
        Map<String, Integer> map = countLoansByUser();

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            String bar = DataUtil.repeat("#", e.getValue());
            System.out.println("User " + e.getKey() + " -> " + bar);
        }
    }
}
