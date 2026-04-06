import Model.Book;
import Model.Loan;
import Model.User;
import Repository.Library;

public class LoanManager {

    // Injeção de Dependência: O Manager precisa conhecer o repositório central
    private Library library;

    public LoanManager(Library library) {
        this.library = library;
    }

    // --- FLUXO DE EMPRÉSTIMO ---
    public void borrowBook(int bookId, int userId) {
        // 1. Validações de existência (Princípio Fail-Fast)
        User user = library.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Falha: Usuário ID " + userId + " não encontrado.");
        }

        Book book = library.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Falha: Livro ID " + bookId + " não encontrado.");
        }

        // 2. Regra de Negócio: Delega a verificação de cópias para o próprio livro
        try {
            book.borrowCopy(); // O próprio livro tenta diminuir o estoque. Se não der, ele lança erro.
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Não foi possível emprestar: " + e.getMessage());
        }

        // 3. Registra o empréstimo na Library
        // (Nota: Num sistema real usaríamos LocalDate, mas mantive Strings para não fugir muito do original)
        String borrowDate = "DataAtual"; // Aqui você usaria o seu Uti.DataUtil se ele ainda existir
        String dueDate = "DataDevolucao";

        int loanId = library.addLoan(book.getId(), user.getId(), borrowDate, dueDate);
        library.addLog("loan-created-success-" + loanId);

        System.out.println("Empréstimo " + loanId + " realizado com sucesso para o usuário " + user.getName() + ".");
    }

    // --- FLUXO DE DEVOLUÇÃO (E a correção do BUG da Atv. 2) ---
    public void returnBook(int loanId) {
        Loan loan = library.getLoanById(loanId);

        // CORREÇÃO DO BUG: Tratar consistentemente cenário de empréstimo inexistente.
        // O legado retornava em silêncio ou dava NullPointerException. Agora lançamos um erro claro.
        if (loan == null) {
            throw new IllegalArgumentException("Erro: O empréstimo com ID " + loanId + " não existe no sistema.");
        }

        // Proteção contra devolução dupla
        if ("CLOSED".equals(loan.getStatus())) {
            throw new IllegalStateException("Erro: Este empréstimo já foi encerrado anteriormente.");
        }

        // 1. Recupera o livro e devolve a cópia
        Book book = library.getBookById(loan.getBookId());
        if (book != null) {
            book.returnCopy(); // O livro cuida de somar +1 no próprio estoque
        }

        // 2. Encerra o empréstimo (Dizemos ao objeto Loan para se fechar)
        String returnDate = "DataAtual";
        double fine = 0.0; // Aqui entraria a lógica de cálculo de multa se estivesse atrasado

        loan.closeLoan(returnDate, fine, "Devolvido pelo LoanManager");
        library.addLog("loan-closed-success-" + loanId);

        System.out.println("Devolução processada com sucesso!");
    }
}