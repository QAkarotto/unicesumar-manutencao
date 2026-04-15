📘 Atividade 1 – Análise de Código e Manutenção Preventiva
1. Problemas de Manutenibilidade Identificados (10+)

| # | Arquivo | Método/Classe | Code Smell | Descrição |
|---|---------|---------------|-----------|-----------|
| 1 | BookManager.java | listBooksSimple() | Bug / Edge Case | Quando não há livros, o código tenta temp.get(0) em lista vazia, causando IndexOutOfBoundsException. |
| 2 | BookManager.java | registerBook() | Validation Smell | Título em branco é aceito com workaround (title = " "), violando regra de negócio. |
| 3 | BookManager.java | registerBook() | Long Parameter List | 8 parâmetros primitivos tornam chamadas propensas a erros de ordem. |
| 4 | LegacyDatabase.java | countOpenLoansByBook() | Wrong Logic / Bad Naming | O método filtra por userId em vez de bookId, retornando dados incorretos. |
| 5 | LegacyDatabase.java | clearLogsIfTooBig() | Magic Numbers | Limites 500 e 400 sem constantes nomeadas ou documentação. |
| 6 | ReportGenerator.java | generateSimpleReport() | Calculation Bug | totalLoans = loans.size() + 1 infla o total sem justificativa. |
| 7 | ReportGenerator.java | generateSimpleReport() | Calculation Bug | closedLoans incrementa para todos os empréstimos, inclusive abertos. |
| 8 | LoanManager.java | borrowBook() | Deep Nesting | Muitos níveis de if aninhados dificultam manutenção. |
| 9 | LoanManager.java | returnBook() | Inconsistent Error Handling | Empréstimo não encontrado não lança erro, enquanto outros casos lançam. |
| 10 | LibrarySystem.java | handleDebugArea() | Deep Nesting / Long Method | Cadeia extensa de if/else para menu simples. |
| 11 | LoanManager.java | returnBook() | Calculation Bug | Multa usa debt - fine em vez de debt + fine. |
| 12 | LoanManager.java | calculateFineLegacy() | Logic Bug | Condição fine > 50 antes de fine > 100, tornando nível 3 inalcançável. |
2. Classificação dos Code Smells

| Categoria | Problemas |
|-----------|-----------|
| Bug / Lógica Incorreta | #1, #4, #6, #7, #11, #12 |
| Validation Smell | #2 |
| Long Parameter List | #3 |
| Magic Numbers | #5 |
| Deep Nesting | #8, #10 |
| Inconsistent Error Handling | #9 |
3. Refatorações Aplicadas
🔧 Refatoração 1 — BookManager.java → listBooksSimple()

Problema:
temp.get(0) causava crash quando a lista estava vazia.

Solução:
Aplicado early return com mensagem:

"No books found."

Arquivo: src/BookManager.java

🔧 Refatoração 2 — LibrarySystem.java → handleDebugArea()

Problema:
Estrutura com múltiplos níveis de if/else (pyramid of doom), dificultando leitura e manutenção.

Solução:
Refatorado para estrutura com else if, mantendo comportamento e simplificando fluxo.

Arquivo: src/LibrarySystem.java

🔧 Refatoração 3 — Main.java (formatação)

Problema:
Código com formatação inconsistente, dificultando leitura.

Solução:
Ajuste de indentação e organização visual sem alteração de comportamento.

Arquivo: src/Main.java

🔧 Refatoração 4 — LegacyDatabase.java → countOpenLoansByBook()

Problema:
Filtro utilizava userId em vez de bookId.

Solução:
Corrigido para usar bookId.

Arquivo: src/LegacyDatabase.java

🔧 Refatoração 5 — LegacyDatabase.java → clearLogsIfTooBig()

Problema:
Uso de números mágicos (500, 400).

Solução:
Extraídas constantes:

LOG_MAX_SIZE = 500
LOG_TRIM_OFFSET = 400

Arquivo: src/LegacyDatabase.java

🔧 Refatoração 6 — ReportGenerator.java → generateSimpleReport()

Problema:

totalLoans com +1 indevido
closedLoans contava empréstimos abertos

Solução:

Removido +1
Contagem corrigida para apenas "CLOSED"

Arquivo: src/ReportGenerator.java

4. Evidência de Comportamento Preservado
🧪 Roteiro de Validação Manual

Execute com:

java Main

| Passo |           Ação            |                   Resultado Esperado                    |
|:----:|:------------------------:|:-------------------------------------------------------:|
|   1  | Iniciar sistema           | Menu exibido, seed carregado                            |
|   2  | Opção 5 (List books)      | Lista exibida ou "No books found." sem crash            |
|   3  | Opção 3 (Borrow book)     | Empréstimo criado                                       |
|   4  | Opção 4 (Return book)     | Devolução processada                                    |
|   5  | Opção 6 (Report)          | Contagem correta (sem +1)                               |
|   6  | Opção 9 → Debug           | Funciona sem travamentos                                |
|   7  | Opção 0                   | Sistema encerra com "bye"                               |
✅ Comportamentos que NÃO mudaram
Fluxo de empréstimo e devolução
Geração de IDs
Sistema de logs
Seed de dados
Menu e navegação CLI