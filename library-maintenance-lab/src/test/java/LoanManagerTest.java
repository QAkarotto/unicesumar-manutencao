import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanManagerTest {

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
}