import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanManagerTest {

    // TESTE 1: Validar título nulo do livro
    @Test
    void shouldThrowExceptionWhenBookTitleIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                LegacyDatabase.addBookData(null, "Autor", 2020, "Categoria", 1, 1, "A1", "ISBN-123");
            }
        );
    }

    // TESTE 2: Validar atualização de usuário inexistente
    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                LegacyDatabase.unsafeUpdateUserField(999, "name", "Marcela Silva");
            }
        );
    }

    // TESTE 3: Validar empréstimo com usuário inválido/inexistente
    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserOrBookIsInvalid() {
        LoanManager loanManager = new LoanManager();
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                loanManager.borrowBook(999, 1, "2026-05-21", "2026-06-04", "email", 14, "test", 0);
            }
        );
    }

    // TESTE 4: Garantir que o método refatorado lança exceção para parâmetros inválidos gerais
    @Test
    void shouldValidateLoanParametersCorrectly() {
        LoanManager loanManager = new LoanManager();
        
        // Testando com ID de livro inválido (-1) para acionar a validação limpa que refatoramos
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                loanManager.borrowBook(1, -1, "2026-05-28", "2026-06-11", "email", 14, "test", 0);
            }
        );
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LoanManagerTest {

    @Before
    public void resetLegacyDatabase() {
        LegacyDatabase.getBooks().clear();
        LegacyDatabase.getUsers().clear();
        LegacyDatabase.getLoans().clear();
        LegacyDatabase.getLogs().clear();
        LegacyDatabase.BOOK_SEQ = 1;
        LegacyDatabase.USER_SEQ = 1;
        LegacyDatabase.LOAN_SEQ = 1;
        LegacyDatabase.seedInitialData();
    }

    @Test
    public void deveCalcularMultaPadraoQuandoHouverAtraso() {
        LoanManager loanManager = new LoanManager();

        double fine = loanManager.calculateFineLegacy("2026-05-01", "2026-05-02", 0, "teste", "helper", 1, 2);

        assertEquals(2.0, fine, 0.0001);
    }

    @Test
    public void deveRetornarZeroQuandoNaoHouverAtraso() {
        LoanManager loanManager = new LoanManager();

        double fine = loanManager.calculateFineLegacy("2026-05-10", "2026-05-10", 0, "teste", "helper", 1, 2);

        assertEquals(0.0, fine, 0.0001);
    }
}