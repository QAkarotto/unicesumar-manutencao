package Model;

public class Loan {
    private int id;
    private int bookId;
    private int userId;
    private String borrowDate;
    private String dueDate;
    private String returnedDate;
    private String status;
    private double fine;
    private String notes;

    // Construtor principal para a criação de um novo empréstimo
    public Loan(int id, int bookId, int userId, String borrowDate, String dueDate) {
        if (borrowDate == null || borrowDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: A data de empréstimo é obrigatória.");
        }
        if (dueDate == null || dueDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: A data de devolução prevista é obrigatória.");
        }

        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "OPEN"; // Todo empréstimo nasce aberto
        this.fine = 0.0;
        this.notes = "";
    }

    // Comportamento de domínio: Fechar o empréstimo
    public void closeLoan(String returnedDate, double fine, String notes) {
        this.status = "CLOSED";
        this.returnedDate = returnedDate;
        this.fine = fine;
        this.notes = (notes == null) ? "" : notes;
    }

    // Getters (Apenas leitura para a maioria)
    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getUserId() { return userId; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public String getReturnedDate() { return returnedDate; }
    public String getStatus() { return status; }
    public double getFine() { return fine; }
    public String getNotes() { return notes; }

    // ‘Setters’ permitidos (apenas para campos que podem ser ajustados com ele aberto)
    public void setNotes(String notes) { this.notes = notes; }
    public void setFine(double fine) { this.fine = fine; }
}