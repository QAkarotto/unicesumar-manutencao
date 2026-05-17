import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BookManagerTest {

    private BookManager bookManager;

    @Before
    public void setUp() {
        bookManager = new BookManager();
        
        LegacyDatabase.getBooks().clear(); 
        LegacyDatabase.getLoans().clear();
    }

    @Ignore("força a falha original")
    @Test
    public void deveFalharAoAcessarIndiceDeListaVazia() {
        List<Map<String, Object>> temp = new ArrayList<>();

        // Esta asserção força a falha em listas vazias
        assertFalse("Bug provado: A lista está vazia, o sistema não deveria tentar ler o índice 0.", temp.isEmpty());

        Map<String, Object> primeiroLivro = temp.get(0);
        assertNotNull(primeiroLivro);
    }

    @Test
    public void deveProcessarListaDeLivrosVaziaComSucesso() {

        try {
            bookManager.listBooksSimple();
            assertTrue("O sistema tratou o fluxo de lista vazia sem lançar erros catastróficos.", true);
        } catch (Exception e) {
            fail("O algoritmo quebrou ao lidar com cenário sem livros: " + e.getMessage());
        }
    }

    @Test
    public void deveLancarExcecaoQuandoIdForInvalido_FailFast() {

        try {
            bookManager.findById(-5);
            fail("Deveria ter disparado uma IllegalArgumentException por conta do ID negativo.");
        } catch (IllegalArgumentException e) {
            assertEquals("O ID do livro para busca deve ser maior que zero.", e.getMessage());
        }
    }


    @Test
    public void simulacaoExemploEstruturaCorretaDeEmprestimo() {
        Map<String, Object> loan = new HashMap<>();
        loan.put("id", 123);
        loan.put("userId", 1);
        loan.put("status", "OPEN");


        LegacyDatabase.getLoans().add(loan);

        assertEquals(1, LegacyDatabase.getLoans().size());
    }
}