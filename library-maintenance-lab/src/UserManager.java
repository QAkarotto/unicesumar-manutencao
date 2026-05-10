import java.util.Map;

public class UserManager {

        static class UserData {
        String name;
        String email;
        String phone;
        String userType;
        String city;
        String document;
        String status;
    }

    public int registerUser(UserData user) {
        int id = -1;
        if (DataUtil.isBlank(user.name)) {
            throw new RuntimeException("name invalid");
        }
        if (DataUtil.isBlank(user.email)) {
            throw new RuntimeException("email invalid");
        }
        if (!DataUtil.hasAt(user.email)) {
            throw new RuntimeException("email invalid");
        }
        if (DataUtil.isBlank(user.phone)) {
            user.phone = "0000-0000";
        }
        if (DataUtil.isBlank(user.userType)) {
            user.userType = "student";
        }
        if (DataUtil.isBlank(user.city)) {
            user.city = "Unknown";
        }
        if (DataUtil.isBlank(user.document)) {
            user.document = "NO-DOC";
        }
        if (DataUtil.isBlank(user.status)) {
            user.status = "ACTIVE";
        }

        id = LegacyDatabase.addUserData(user.name, DataUtil.normalizeEmail(user.email), user.phone, user.userType, user.city, user.document, user.status);
        LegacyDatabase.addLog("user-manager-register-" + id);
        return id;
    }

    public void registerUserFromConsole() {
        UserData user = new UserData();

        user.name = DataUtil.readLine("Name: ");
        user.email = DataUtil.readLine("Email: ");
        user.phone = DataUtil.readLine("Phone: ");
        user.userType = DataUtil.readLine("Type (student/teacher): ");
        user.city = DataUtil.readLine("City: ");
        user.document = DataUtil.readLine("Document: ");
        user.status = DataUtil.readLine("Status: ");

        int id = registerUser(user);
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
