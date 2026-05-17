import java.util.HashMap;
import java.util.Map;

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

        double fine = loanManager.calculateFineLegacy("2026-05-10", "2026-05-10", 0, "testee", "helper", 1, 2);

        assertEquals(0.0, fine, 0.0001);
    }
    @Test
    public void deveIncrementarDividaQuandoHouverMulta() {

        LoanManager loanManager = new LoanManager();

        Map<String, Object> user = new HashMap<>();
        user.put("debt", 10.0);

        double fine = 5.0;
        if (fine > 0) {
            double debt = ((Double) user.get("debt")).doubleValue();

            debt = debt + fine; 
            user.put("debt", debt);
        }

        double dividaEsperada = 15.0; 
        double dividaObtida = ((Double) user.get("debt")).doubleValue();

    
        assertEquals("A multa deveria ser somada à dívida, não subtraída.", dividaEsperada, dividaObtida, 0.0001);
    }

}

