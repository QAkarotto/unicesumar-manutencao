# Atividade 2 – Relatório de Manutenção Corretiva e Evolutiva

## 1. Manutenção Corretiva (Bugs Corrigidos)

Foram identificados e resolvidos os seguintes bugs reais no código legado:

### Bug 1: Crash na listagem com banco vazio (`BookManager.listBooksSimple`)
* **Descrição:** Acesso direto ao índice `0` de uma lista vazia causava `IndexOutOfBoundsException`, quebrando o sistema inteiro.
* **Como reproduzir ANTES:** Iniciar o sistema sem gerar dados de teste (Seed) e acessar a opção "5 - List books" no menu. O sistema fechava com erro fatal.
* **Como reproduzir DEPOIS:** Acessar a mesma opção. O sistema exibe amigavelmente a mensagem *"Nenhum livro cadastrado na biblioteca"* e retorna ao menu.

### Bug 2: Contagem de empréstimos inconsistente (`Library.countOpenLoansByBook`)
* **Descrição:** O método legado (antigo `LegacyDatabase`) deveria filtrar a contagem pelo ID do livro, mas a chave de comparação verificava o `userId` em relação ao `bookId`.
* **Como reproduzir ANTES:** Emprestar o livro ID 1 para o usuário ID 5. Solicitar o relatório de empréstimos do livro ID 1. O sistema retornava "0" cópias emprestadas.
* **Como reproduzir DEPOIS:** A mesma operação agora retorna a contagem correta (1), pois o filtro compara `loan.getBookId() == bookId`.

### Bug 3: Falsa devolução de empréstimo inexistente (`LoanManager.returnBook`)
* **Descrição:** O método legado tentava fechar empréstimos que não existiam ou já estavam fechados, operando de forma silenciosa ou gerando `NullPointerException`.
* **Como reproduzir ANTES:** Digitar um `Loan ID` que não existe (ex: 999) na opção de devolução. O sistema aceitava a operação como concluída.
* **Como reproduzir DEPOIS:** A mesma operação agora é bloqueada pela validação, lançando no terminal a mensagem: *"Erro: O empréstimo com ID 999 não existe no sistema"*.

---

## 2. Manutenção Evolutiva (Nova Funcionalidade)

### Histórico de Empréstimos por Usuário
* **O que foi implementado:** Adicionada a opção "10 - User History" no menu principal (`LibrarySystem`). Ela solicita o ID de um usuário e lista todos os livros (ativos e devolvidos) associados a ele, exibindo o título do livro e o status do empréstimo.
* **Como testar:** 1. No menu principal, selecione a opção `10`.
    2. Insira o ID de um usuário (Ex: Usuário ID 1, carregado no Seed inicial).
    3. O terminal exibirá o histórico de movimentações daquele usuário.

---

## 3. Impactos e Riscos Conhecidos
* **Riscos de Transição Arquitetural:** Devido à substituição do `LegacyDatabase` pela classe `Library` e objetos de domínio fortemente tipados, a área de *Debug (Opção 9)* teve que ser adaptada. Atualizações de campos "inseguras" via String (antigo `unsafeUpdateBookField`) agora foram limitadas a *setters* controlados para evitar a corrupção do modelo de domínio.
* **Evidências de Execução:** A compilação é garantida via `javac src/**/*.java` e as flags solicitadas (`--list` e `--report`) foram implementadas e mapeadas no `Main.java` sem conflitos com o menu interativo.