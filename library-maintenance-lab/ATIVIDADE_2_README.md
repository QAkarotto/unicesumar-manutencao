# Atividade 2: Manutenção Corretiva e Evolutiva

##  Visão Geral

Este documento detalha as correções de bugs e a implementação de uma nova funcionalidade no sistema legado de biblioteca como parte da **Atividade 2**.

---

##  Bugs Corrigidos

### Bug 1: Tratamento Inconsistente de Erro em LoanManager.returnBook

**Descrição:**  
O método `returnBook()` em `LoanManager.java` retornava silenciosamente quando um `loanId` inexistente era fornecido, em vez de lançar uma exceção. Isso levava a um comportamento inconsistente no tratamento de erros.

**Reprodução (Antes):**  
- Chamar `returnBook()` com um `loanId` que não existe no `LegacyDatabase`
- O sistema simplesmente logava um evento e continuava, sem indicar ao usuário que a operação falhou

**Correção (Depois):**  
- Alterado o método para lançar uma `RuntimeException` com mensagem clara: `Loan not found for ID: [loanId]`
- Garante um tratamento de erro consistente e previsível

---

### Bug 2: Criação de Empréstimos Duplicados em LoanManager.borrowBook

**Descrição:**  
O método `borrowBook()` em `LoanManager.java` continha código legado que duplicava a criação de um empréstimo no `LegacyDatabase` especificamente para o canal "sms", resultando em registros inconsistentes.

**Reprodução (Antes):**  
- Realizar um empréstimo via CLI chamando `borrowBook()` com `channel = "sms"`
- Dois registros de empréstimo eram criados no `LegacyDatabase` para a mesma operação

**Correção (Depois):**  
- Removido o bloco de código responsável pela duplicação do registro
- Garante que apenas um registro seja criado por operação de empréstimo

---

### Bug 3: Cálculos Incorretos em ReportGenerator.generateSimpleReport

**Descrição:**  
O relatório simples apresentava dois erros de cálculo:
- `totalLoans` era inflacionado por um ajuste fixo `+1`
- `closedLoans` incrementava para TODOS os empréstimos, independentemente do status

**Reprodução (Antes):**  
```bash
java -cp src Main --report
```
- `totalLoans` era sempre 1 a mais do que o número real
- `closedLoans` era igual ao `totalLoans` (antes do ajuste +1)

**Correção (Depois):**  
- `totalLoans` agora reflete o tamanho real: `loans.size()`
- `closedLoans` incrementa apenas quando status ≠ "OPEN"
- Contagens são precisas e consistentes

---

##  Nova Funcionalidade: Histórico de Empréstimos por Usuário

### Descrição

Adicionada a capacidade de visualizar o histórico completo de empréstimos de um usuário específico, incluindo empréstimos abertos e fechados.

### Localização

- **Método Principal:** `getUserLoanHistory(int userId)` em `UserManager.java`
- **Handler CLI:** `handleUserLoanHistory()` em `LibrarySystem.java`
- **Opção de Menu:** `9 - User loan history`

### Funcionalidades

- Consulta todos os empréstimos associados a um ID de usuário
- Exibe detalhes de cada empréstimo:
  - ID do empréstimo
  - ID do livro
  - Data de empréstimo
  - Data de devolução prevista
  - Data de retorno efetivo
  - Status (OPEN/CLOSED)
  - Multa aplicada

### Impactos e Riscos Conhecidos

**Impacto Positivo:**
- Oferece transparência total ao usuário sobre seu histórico de empréstimos
- Facilita rastreamento de empréstimos abertos e multas pendentes

**Risco (Acoplamento):**
- A funcionalidade depende diretamente de `LegacyDatabase.getLoans()`, que expõe a lista mutável
- Futuras refatorações poderiam encapsular melhor o acesso aos dados de empréstimo

---

##  Evidências de Execução

### 1. Compilação

```bash
cd /home/ubuntu/unicesumar-manutencao-extracted/unicesumar-manutencao/library-maintenance-lab
javac src/*.java
```

**Resultado:**  Compilação bem-sucedida, sem erros

---

### 2. Fluxo Principal - Listagem

```bash
java -cp src Main --list
```

**Resultado:**  A listagem de livros, usuários e empréstimos é exibida corretamente, refletindo o estado inicial do sistema

---

### 3. Fluxo Principal - Relatório

```bash
java -cp src Main --report
```

**Resultado:**  Relatório gerado com contagens corrigidas:
- `totalLoans` reflete valores esperados
- `closedLoans` reflete contagem precisa

---

### 4. Nova Funcionalidade - Histórico de Empréstimos

**Execução Interativa:**

```bash
java -cp src Main
```

**Interação no CLI:**

```
Select option: 9
User ID: 3
--------------------------------------------
Loan History for User 3
--------------------------------------------
ID | BOOK | BORROW      | DUE            | RETURNED   | STATUS | FINE
1  | 4    | 2026-04-10  | 2026-04-10+14  | 2026-04-10 | CLOSED | 0.0
```

**Resultado:**  Histórico de empréstimos para o usuário 'Carlos' (ID 3) exibido corretamente, mostrando:
- Empréstimo do livro 'Legacy Java'
- Status: CLOSED
- Multa: 0.0

---

##  Estrutura de Arquivos Modificados

```
src/
├── LoanManager.java          (Bugs 1 e 2 corrigidos)
├── ReportGenerator.java      (Bug 3 corrigido)
├── UserManager.java          (Nova funcionalidade)
├── LibrarySystem.java        (Integração CLI da nova funcionalidade)
└── ... (outros arquivos)
```

---

##  Resumo das Alterações

| Componente | Tipo | Descrição |
|-----------|------|-----------|
| LoanManager | Bugfix | Tratamento de erro consistente em returnBook() |
| LoanManager | Bugfix | Remoção de duplicação de empréstimos em borrowBook() |
| ReportGenerator | Bugfix | Correção de cálculos de totalLoans e closedLoans |
| UserManager | Feature | Nova funcionalidade de histórico de empréstimos |
| LibrarySystem | Feature | Integração CLI da nova funcionalidade |

---

##  Critérios de Aceitação

- [x] Todos os 3 bugs foram corrigidos e testados
- [x] Nova funcionalidade funciona conforme esperado
- [x] Sistema compila sem erros
- [x] CLI funciona corretamente em todos os modos
- [x] Comportamento é consistente e previsível
- [x] Histórico de empréstimos é acessível via opção de menu

---

##  Como Executar

### Compilar o projeto:
```bash
javac src/*.java
```

### Listar dados:
```bash
java -cp src Main --list
```

### Gerar relatório:
```bash
java -cp src Main --report
```

### Modo interativo:
```bash
java -cp src Main
```

Selecione a opção `9` para acessar o histórico de empréstimos por usuário.

---

##  Notas Técnicas

- As correções mantêm compatibilidade com o código legado existente
- Não houve alterações na assinatura de métodos públicos principais
- O acoplamento com `LegacyDatabase` persiste conforme esperado para um sistema legado
- Futuras refatorações devem considerar abstração de camada de dados

---

##  Conclusão

A Atividade 2 foi concluída com sucesso, corrigindo 3 bugs críticos e implementando uma nova funcionalidade de alta importância para a usabilidade do sistema. O sistema permanece estável e funcional.
