import java.util.Map;

public class UserManager {

    public int registerUser(String name, String email, String phone, String userType, String city, String document,
            String status) {
        int id = -1;
        if (DataUtil.isBlank(name)) {
            throw new RuntimeException("name invalid");
        }
        if (DataUtil.isBlank(email)) {
            throw new RuntimeException("email invalid");
        }
        if (!DataUtil.hasAt(email)) {
            throw new RuntimeException("email invalid");
        }
        if (DataUtil.isBlank(phone)) {
            phone = "0000-0000";
        }
        if (DataUtil.isBlank(userType)) {
            userType = "student";
        }
        if (DataUtil.isBlank(city)) {
            city = "Unknown";
        }
        if (DataUtil.isBlank(document)) {
            document = "NO-DOC";
        }
        if (DataUtil.isBlank(status)) {
            status = "ACTIVE";
        }

        id = LegacyDatabase.addUserData(name, DataUtil.normalizeEmail(email), phone, userType, city, document, status);
        LegacyDatabase.addLog("user-manager-register-" + id);
        return id;
    }

    public void registerUserFromConsole() {
        String name = DataUtil.readLine("Name: ");
        String email = DataUtil.readLine("Email: ");
        String phone = DataUtil.readLine("Phone: ");
        String type = DataUtil.readLine("Type (student/teacher): ");
        String city = DataUtil.readLine("City: ");
        String document = DataUtil.readLine("Document: ");
        String status = DataUtil.readLine("Status: ");
        int id = registerUser(name, email, phone, type, city, document, status);
        System.out.println("User saved with id " + id);
    }

    public Map<String, Object> findById(int id) {
        return LegacyDatabase.getUserById(id);
    }

    public void listUsers() {
        System.out.println("ID | NAME | EMAIL | TYPE | CITY | STATUS | DEBT");
        for (Map<String, Object> u : LegacyDatabase.getUsers().values()) {
            System.out.println(u.get("id") + " | " + u.get("name") + " | " + u.get("email") + " | " + u.get("userType") + " | "
                    + u.get("city") + " | " + u.get("status") + " | " + u.get("debt"));
        }
    }

    public void addDebt(int userId, double value, String source, int p1, int p2, String helper) {
        Map<String, Object> data = LegacyDatabase.getUserById(userId);
        if (data == null) {
            throw new RuntimeException("user not found");
        }
        double debt = ((Double) data.get("debt")).doubleValue();
        debt = debt + value;
        data.put("debt", debt);

        if (p1 > 10) {
            LegacyDatabase.addLog("debt-high-" + source + "-" + helper);
        } else {
            LegacyDatabase.addLog("debt-low-" + source + "-" + helper);
        }

        if (p2 == 99) {
            data.put("status", "SUSPENDED");
        }
    }

    public boolean canBorrow(int userId) {
        Map<String, Object> data = LegacyDatabase.getUserById(userId);
        if (data == null) {
            return false;
        }
        String status = String.valueOf(data.get("status"));
        double debt = ((Double) data.get("debt")).doubleValue();
        if (!"ACTIVE".equals(status)) {
            return false;
        }
        if (debt > 100.0) {
            return false;
        }
        return true;
    }

    // duplicate validation in another class too
    public boolean validateUserData(String name, String email, String phone) {
        if (DataUtil.isBlank(name)) {
            return false;
        }
        if (DataUtil.isBlank(email)) {
            return false;
        }
        if (!DataUtil.hasAt(email)) {
            return false;
        }
        if (DataUtil.isBlank(phone)) {
            return false;
        }
        return true;
    }
}
