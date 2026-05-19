package test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LegacyDatabaseTest {

  @Before
  public void setUp() {
    LegacyDatabase.getLoans().clear();
  }

  @Test
  public void deveFalharAoContarEmprestimosAbertosPorLivro_BugDeFiltro() {

    Map<String, Object> loanAberto = new HashMap<>();
    loanAberto.put("id", 1);
    loanAberto.put("bookId", 99);
    loanAberto.put("userId", 1);
    loanAberto.put("status", "OPEN");

    LegacyDatabase.getLoans().add(loanAberto);

    int quantidadeContada = LegacyDatabase.countOpenLoansByBook(99);

    assertEquals("Bug provado: O sistema deveria contar 1 empréstimo aberto para o livro 99", 1, quantidadeContada);
  }
}
