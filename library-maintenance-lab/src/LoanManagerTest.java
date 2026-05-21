import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanManagerTest {

    private LoanManager loanManager = new LoanManager();

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() {

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> loanManager.borrowBook(
                        -1,
                        1,
                        "2026-05-01",
                        "2026-05-10",
                        "email",
                        10,
                        "test",
                        0
                )
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNoAvailableCopies() {

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> loanManager.borrowBook(
                        1,
                        999,
                        "2026-05-01",
                        "2026-05-10",
                        "email",
                        10,
                        "test",
                        0
                )
        );

        assertNotNull(exception);
    }

    @Test
    public void shouldCalculateFineCorrectly() {
        double fine = loanManager.calculateFineLegacy(
                "2026-05-01",
                "2026-05-10",
                0
        );

        assertTrue(fine > 0);
    }
}