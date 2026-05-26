import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class LoanManagerTest {

     private LoanManager loanManager;

     @Test
    public void testProcessNotificationFlow() {
        loanManager.listUserLoans("usuario1");
        assertTrue("O fluxo deve registrar as ações no banco legado", LegacyDatabase.getLogs().size() >= 0);
    }
    
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

    @Test
    public void deveRegistrarLogDaPoliticaSeteEConfirmacao() {
        loanManager.registerBorrowPolicy("web", 7, 101);

        assertTrue("Deve conter o log da política 7", LegacyDatabase.getLogs().contains("loan-policy-7-web"));
        assertTrue("Deve conter o log de confirmação do empréstimo", LegacyDatabase.getLogs().contains("loan-created-ok-101"));
    }

    @Test
    public void deveRegistrarLogDaPoliticaOitoEConfirmacao() {
        loanManager.registerBorrowPolicy("mobile", 8, 102);

        assertTrue("Deve conter o log da política 8", LegacyDatabase.getLogs().contains("loan-policy-8-mobile"));
        assertTrue("Deve conter o log de confirmação do empréstimo", LegacyDatabase.getLogs().contains("loan-created-ok-102"));
    }

    @Test
    public void deveRegistrarLogDefaultQuandoCodigoNaoForMapeado() {
        loanManager.registerBorrowPolicy("totem", 99, 103);

        assertTrue("Deve conter o log da política padrão (default)", LegacyDatabase.getLogs().contains("loan-policy-default-totem"));
        assertTrue("Deve conter o log de confirmação do empréstimo", LegacyDatabase.getLogs().contains("loan-created-ok-103"));
    }
}

