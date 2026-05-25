import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class UserManagerTest {

    private UserManager userManager; 
    

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

        this.userManager = new UserManager(); 
    }

    @Test
    public void deveRetornarFalsoQuandoUsuarioNaoExistirNoBanco() {
        boolean resultado = userManager.canBorrow(999);
        
        assertFalse("Se o usuário for nulo, deve retornar false", resultado);
    }

    @Test
    public void deveRetornarFalsoQuandoUsuarioEstiverInativo() {

        Map<String, Object> usuarioInativo = new HashMap<>();
        usuarioInativo.put("id", 50);
        usuarioInativo.put("status", "INACTIVE");
        usuarioInativo.put("debt", 0.0);
        LegacyDatabase.getUsers().put(50, usuarioInativo);

        boolean resultado = userManager.canBorrow(50);
        
        assertFalse("Se o status não for ACTIVE, deve retornar false", resultado);
    }

    @Test
    public void deveRetornarFalsoQuandoUsuarioTiverDividaMaiorQueCem() {
        Map<String, Object> usuarioEndividado = new HashMap<>();
        usuarioEndividado.put("id", 51);
        usuarioEndividado.put("status", "ACTIVE");
        usuarioEndividado.put("debt", 120.50); 
        LegacyDatabase.getUsers().put(51, usuarioEndividado);

        boolean resultado = userManager.canBorrow(51);
        
        assertFalse("Se a dívida for maior que 100, deve retornar false", resultado);
    }

    @Test
    public void deveRetornarVerdadeiroQuandoUsuarioForValidoEComDividaEmDia() {
        Map<String, Object> usuarioRegular = new HashMap<>();
        usuarioRegular.put("id", 52);
        usuarioRegular.put("status", "ACTIVE");
        usuarioRegular.put("debt", 25.00); 
        LegacyDatabase.getUsers().put(52, usuarioRegular);

        boolean resultado = userManager.canBorrow(52);
        
        assertTrue("Usuário ativo e com dívida baixa deve retornar true", resultado);
    }

    @Test
    public void deveRegistrarUsuarioComSucessoEDeclirarIdValido() {
        UserManager.UserData user = new UserManager.UserData();
        user.name = "Gabriel Toledo";
        user.email = "fallen@unicesumar.edu.br";

        int idGerado = userManager.registerUser(user);

    
        assertTrue("O ID do usuário criado deve ser maior que zero", idGerado > 0);
        assertTrue("Deve registrar o log com o ID correto", 
            LegacyDatabase.getLogs().contains("user-manager-register-" + idGerado));
    }

    @Test(expected = RuntimeException.class)
    public void deveLancarExcecaoETravasAntesDeChegarNoIdQuandoNomeForInvalido() {
    UserManager.UserData user = new UserManager.UserData();
    user.name = "   ";
    user.email = "fallen@unicesumar.edu.br";

    userManager.registerUser(user);
}

}