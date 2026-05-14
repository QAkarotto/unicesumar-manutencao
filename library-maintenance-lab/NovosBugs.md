# NovosBugs — Relatórios profissionais (Atividade 2)

**Sistema:** Laboratório de Manutenção de Software — *Legacy Library* (`library-maintenance-lab`)  
**Stack:** Java 17+ (linha de comando), estado em memória via `LegacyDatabase`  
**Objetivo deste documento:** registrar cinco bugs adicionais com triagem mínima (reprodução, impacto, evidência em execução).

---

## Bug 1 — Dívida do usuário diminui após devolução com multa

**Título:** Na devolução com multa, o sistema subtrai o valor da multa da dívida em vez de somar.

**Descrição detalhada:**  
Em `LoanManager.returnBook`, quando a multa calculada é maior que zero, o código atualiza o campo `debt` do usuário com `debt = debt - fine`. O comportamento esperado em biblioteca é que a multa **aumente** a dívida (ou registre cobrança pendente). Em execução, usuários com dívida pré-existente ficam com saldo **menor** após pagar/atualizar multa, o que corrompe regras financeiras e relatórios em `ReportGenerator` (“Users with debt”).

**Passos para reproduzir:**

1. Compilar: `javac *.java` no diretório `library-maintenance-lab/src`.
2. Executar um trecho que: (a) registre livro e usuário ativo; (b) chame `addDebt(userId, 50.0, ...)`; (c) crie empréstimo com `dueDate` **anterior** à data de devolução; (d) chame `returnBook` com `forceFlag == 0`.
3. Comparar `user.get("debt")` antes e depois da devolução.

**Comportamento esperado:** Dívida final ≥ dívida anterior + multa (para multa > 0).

**Comportamento observado:** Dívida final = dívida anterior − multa.

**Ambiente de execução:** Windows 10, Java (`javac`/`java`), classpath `src`, dados apenas em memória (processo único).

**Evidências (saída da aplicação / log):**

```
Divida antes da devolucao (com atraso): 50.0
EMAIL: Book returned: Test Book by Maria, fine=2.0
Divida apos devolucao com multa: 48.0
(Esperado: 50 + multa; observado: subtrai multa do saldo)
```

Referência no código: `LoanManager.java` (atualização de `debt` após cálculo de `fine`).

**Severidade sugerida:** Alta (integridade financeira).  
**Prioridade sugerida:** P0/P1.

---

## Bug 2 — Data de vencimento padrão não é uma data válida (`yyyy-MM-dd`)

**Título:** `DataUtil.datePlusDaysApprox` retorna concatenação textual em vez de data civil, poluindo empréstimos e notificações.

**Descrição detalhada:**  
O método documentado como “TODO: refactor” devolve `date + " +" + days` (ex.: `2026-05-13 +14`). Esse valor é usado como `dueDate` padrão no fluxo de empréstimo (`LoanManager.borrowFromConsole`, `LibrarySystem.handleBorrowBook`, cenário demo em `LibrarySystem.runDemoScenario`). O sistema passa a tratar como “data” uma string que **não** obedece ao padrão `yyyy-MM-dd`, afetando notificações, listagens e qualquer lógica futura baseada em parsing de data.

**Passos para reproduzir:**

1. `javac *.java` em `src`.
2. `java Main --list` (dispara `runDemoScenario` antes da listagem).
3. Observar coluna **DUE** do empréstimo e a linha `EMAIL: Loan created... due ...`.

**Comportamento esperado:** `dueDate` no mesmo formato de `borrowDate` (`DataUtil.defaultDatePattern`), representando o dia civil de vencimento.

**Comportamento observado:** `dueDate` contém sufixo literal ` +14` (ou similar).

**Ambiente de execução:** Windows 10, Java, execução `java -cp src Main --list`.

**Evidências:**

```
EMAIL: Loan created for user Carlos and book Legacy Java due 2026-05-13 +14
...
1 | 3 | 4 | 2026-05-13 | 2026-05-13 +14 | 2026-05-13 | CLOSED | 0.0
```

Referência: `DataUtil.datePlusDaysApprox`.

**Severidade sugerida:** Alta (dados inválidos no domínio).  
**Prioridade sugerida:** P1.

---

## Bug 3 — Limite de empréstimos por exemplar do livro usa `userId` no lugar de `bookId`

**Título:** `LegacyDatabase.countOpenLoansByBook` filtra empréstimos comparando ID do usuário com o ID do livro.

**Descrição detalhada:**  
O método deveria contar empréstimos `OPEN` cujo `bookId` coincide com o argumento. A implementação atual compara `loan.get("userId")` com `bookId`. Isso faz a validação `LegacyDatabase.countOpenLoansByBook(bookId) >= totalCopies` em `LoanManager.validateBorrow` falhar silenciosamente na maioria dos casos (só “acerta” por coincidência numérica rara). Consequência: o sistema pode permitir mais empréstimos simultâneos do mesmo título do que o estoque/`totalCopies` deveria permitir, ou bloquear indevidamente quando IDs coincidem.

**Passos para reproduzir:**

1. Limpar listas em memória (novo processo Java) e inserir manualmente um mapa de empréstimo `OPEN` com `bookId = 3` e `userId = 5`.
2. Chamar `LegacyDatabase.countOpenLoansByBook(3)`.
3. Observar retorno `0` apesar de haver cópia emprestada.

**Comportamento esperado:** Contagem = número de empréstimos abertos daquele `bookId`.

**Comportamento observado:** Contagem baseada em `userId == bookId`.

**Ambiente de execução:** JVM isolada, teste via classe temporária ou depuração.

**Evidências:**

```
Emprestimo OPEN: bookId=3, userId=5
countOpenLoansByBook(3) = 0
(Esperado: 1 copia emprestada; observado: 0 pois compara userId==bookId)
```

Referência: `LegacyDatabase.countOpenLoansByBook`.

**Severidade sugerida:** Alta (regra de negócio de estoque).  
**Prioridade sugerida:** P0.

---

## Bug 4 — Ramo de alerta de dívida “nível 3” é inalcançável

**Título:** Em `calculateFineLegacy`, a condição `else if (fine > 100)` nunca executa após `if (fine > 50)`.

**Descrição detalhada:**  
Após o cálculo de `fine`, o código encadeia `if (fine > 50) { sendDebtAlert(..., 2, ...) } else if (fine > 100) { sendDebtAlert(..., 3, ...) }`. Qualquer multa > 100 também é > 50, portanto o primeiro bloco sempre consome o caso e o nível 3 nunca é acionado. Isso impede testes/operação do alerta “legal” (`level == 3` em `NotificationService.sendDebtAlert`) por esse caminho.

**Passos para reproduzir:**

1. Inspecionar `LoanManager.calculateFineLegacy` após o cálculo de `fine`.
2. (Opcional) Forçar `fine` alto via depuração ou mock — o ramo `fine > 100` não dispara `sendDebtAlert` com nível 3.

**Comportamento esperado:** Faixas de multa disjuntas (ex.: `> 100` antes de `> 50`) ou `if / else if` ordenados do maior para o menor threshold.

**Comportamento observado:** Apenas alertas correspondentes ao primeiro threshold são usados; nível 3 inatingível.

**Ambiente de execução:** Qualquer SO; análise estática confirmada em runtime quando `fine` ultrapassa 100.

**Evidências:** Trecho lógico em `LoanManager.java` (condições encadeadas). Complementar: com `GLOBAL_FINE_PER_DAY = 2` e `days` fixo em `1` (ver Bug 5), a multa por atraso na implementação atual raramente chega a valores altos por esse método — o defeito de ordenação permanece independentemente.

**Severidade sugerida:** Média (alertas incorretos / código morto).  
**Prioridade sugerida:** P2.

---

## Bug 5 — Multa por atraso usa sempre “1 dia”, ignorando o intervalo real entre datas

**Título:** `calculateFineLegacy` fixa `int days = 1` em vez de calcular dias entre `dueDate` e `returnedDate`.

**Descrição detalhada:**  
Há comentário de implementação antiga (`// int days = calculateDaysBetween(...)`). O valor efetivo usado é sempre `1` quando há atraso lexicográfico (`returnedDate.compareTo(dueDate) > 0`). Assim, mesmo com anos de atraso simulado em strings `yyyy-MM-dd`, a multa permanece `1 * GLOBAL_FINE_PER_DAY` (ex.: `2.0` com a constante atual), distorcendo sanções e qualquer política por dia de atraso.

**Passos para reproduzir:**

1. Chamar `calculateFineLegacy("2020-01-01", "2026-05-10", 0, "p", "helper", 1, 1)` em instância de `LoanManager`.
2. Observar retorno `2.0` com `GLOBAL_FINE_PER_DAY = 2`.

**Comportamento esperado:** Multa proporcional ao número de dias de atraso (ou política institucional documentada).

**Comportamento observado:** Multa equivalente a um único dia de atraso.

**Ambiente de execução:** Windows 10, Java.

**Evidências:**

```
Atraso ~anos, multa retornada (GLOBAL_FINE_PER_DAY=2): 2.0
(Esperado: proporcional aos dias; observado: sempre 1 dia * taxa)
```

Referência: `LoanManager.calculateFineLegacy`.

**Severidade sugerida:** Alta (cálculo de cobrança).  
**Prioridade sugerida:** P1.

---

## Triagem rápida (resumo para GitHub Projects / Issues)

| # | Título curto | Severidade | Prioridade | Componente |
|---|----------------|------------|------------|------------|
| 1 | Dívida subtraída na devolução com multa | Alta | P0/P1 | `LoanManager` |
| 2 | Vencimento `date +N` não é data válida | Alta | P1 | `DataUtil` / fluxo empréstimo |
| 3 | `countOpenLoansByBook` compara campo errado | Alta | P0 | `LegacyDatabase` |
| 4 | Alerta nível 3 inalcançável (`fine > 100`) | Média | P2 | `LoanManager` |
| 5 | Multa sempre 1 dia de atraso | Alta | P1 | `LoanManager` |

**Sugestão de colunas no GitHub Projects:** `Backlog` → `Triagem` → `Em correção` → `Em teste` → `Concluído`, com etiquetas `bug`, `severidade:alta|media`, `prioridade:P0|P1|P2`.

---

*Documento gerado com base no código em `library-maintenance-lab/src` e em saídas reais de `java Main --list` e de execução local de cenários de reprodução equivalentes aos passos descritos.*
