package Model;

public class User {

    private String name;
    private String email;
    private String phone;
    private String userType;
    private String city;
    private String document;
    private String status;
    private double debt;
    int id;

    public User(int id, String name, String email, String phone, String userType, String city, String document, String status) {
        // 1. Validações críticas
        if (name == null || email == null) {
            throw new RuntimeException("name, email or phone invalid");
        }
        // 2. Atribuições com lógica de "fallback"
        this.name = name;
        this.email = email;
        this.phone = (phone == null) ? "00 0000-0000" : phone;
        this.userType = (userType == null) ? "Student" : userType;
        this.city = (city == null) ? "Unknown" : city;
        this.document = (document == null) ? "NO-DOC" : document;
        this.status = (status == null) ? "active" : status;
        this.debt = 0.0;
        this.id = id;
    }

    public void addDebt(double amount) {
        this.debt += amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDocument() {
        return document;
    }

    public double getDebt() {
        return debt;
    }

    public int getId() {
        return id;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }
}
