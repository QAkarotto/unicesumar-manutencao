import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanManagerTest {

    // TESTE 1: Validar título nulo do livro (Já corrigido e passando!)
    @Test
    void shouldThrowExceptionWhenBookTitleIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                LegacyDatabase.addBookData(
                    null,
                    "Autor",
                    2020,
                    "Categoria",
                    1,
                    1,
                    "A1",
                    "ISBN-123"
                );
            }
        );
    }

    // TESTE 2: Validar atualização de usuário inexistente (O novo Bug!)
    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                LegacyDatabase.unsafeUpdateUserField(999, "name", "Marcela Silva");
            }
        );
    }
}