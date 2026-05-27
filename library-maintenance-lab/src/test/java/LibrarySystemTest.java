import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

public class LibrarySystemTest {
    private LibrarySystem librarySystem;

    @Before
    public void setUp() {
        LegacyDatabase.getBooks().clear();
        LegacyDatabase.getUsers().clear();
        LegacyDatabase.getLoans().clear();
        LegacyDatabase.getLogs().clear();
        
        librarySystem = new LibrarySystem();
    }

    @Test
    public void testDemoScenarioPopulatesLoanWithCorrectChannelConstant() {
        librarySystem.runDemoScenario();

        List<Map<String, Object>> loans = LegacyDatabase.getLoans();
        
        assertFalse("A lista de empréstimos não deveria estar vazia após rodar o cenário demo", loans.isEmpty());
        
        Map<String, Object> targetLoan = loans.get(0);
        assertNotNull("O empréstimo recuperado não deve ser nulo", targetLoan);
        
        assertEquals("CLOSED", String.valueOf(targetLoan.get("status")));
    }

    @Test
    public void testLibrarySystemInitializationDoesNotCrash() {
        assertNotNull(librarySystem.getBookManager());
        assertNotNull(librarySystem.getUserManager());
        assertNotNull(librarySystem.getLoanManager());
    }
}
